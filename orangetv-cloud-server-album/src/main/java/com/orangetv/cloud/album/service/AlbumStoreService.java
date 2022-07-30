package com.orangetv.cloud.album.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangetv.cloud.album.model.Video;
import com.orangetv.cloud.album.oauth2client.ReactiveVideoClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumStoreService {

    private final GridFsTemplate gridFsTemplate;
    private final AsyncTaskExecutor taskExecutor;
    private final ReactiveVideoClient videoClient;
    private final ObjectMapper objectMapper;

    public void onVideoMetadataGenerated(long videoId, String msg) {
        log.info("video {} metadata generated\n{}", videoId, msg);
        Video video = parseJSON(msg, Video.class);
        String videoUrl = getVideoUrl(video);
        try (var outputStream = new PipedOutputStream();
             var inputStream = new PipedInputStream(outputStream)) {
            var latch = new CountDownLatch(1);
            var lengthFuture = taskExecutor.submit(() -> {
                long length = 0;

                // pipe image from FFmpeg frame grabber
                //noinspection UnnecessaryLocalVariable
                try (OutputStream imageStream = outputStream) {
                    String url = UriUtils.encodePath(videoUrl,
                            StandardCharsets.UTF_8.name());
                    log.info("processing {}", url);
                    length = videoImage(url, imageStream);
                } catch (FrameGrabber.Exception e) {
                    log.error("Failed to extract image.", e);
                }

                try {
                    //noinspection ResultOfMethodCallIgnored
                    latch.await(50, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error("interrupted", e);
                }
                return length;
            });

            // sava image to GridFS
            var oldFile = gridFsTemplate.findOne(
                    query(where("metadata.repoId").is(video.getRepoId())
                            .and("metadata.path").is(video.getPath())
                            .and("metadata.name").is(video.getName())));
            if (oldFile != null) {
                gridFsTemplate.delete(query(where("_id").is(oldFile.getId())));
            }
            var objectId = gridFsTemplate.store(inputStream, genImageFilename(),
                    MediaType.IMAGE_PNG_VALUE, video);
            latch.countDown();

            // update video info
            video.setCover(objectId.toString());
            video.setLength(lengthFuture.get());
            videoClient.updateById(video);

            log.info("finished: {}", objectMapper.writeValueAsString(video));
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("failed to store image to GridFS", e);
        }
    }

    /**
     * 生成图片的相对路径
     *
     * @return 图片的相对路径 例：1.png
     */
    static String genImageFilename() {
        return getUUID() + ".png";
    }

    /**
     * 生成唯一的uuid
     *
     * @return uuid
     */
    static String getUUID() {
        return UUID.randomUUID().toString()
                .replace("-", "");
    }


    private String getVideoUrl(Video video) {
        String videoHost = videoClient.getVideoRepoUrl(video);
        return videoHost + video.getPath() + video.getName();
    }

    @SuppressWarnings("SameParameterValue")
    private <T> T parseJSON(String s, Class<T> clazz) {
        try {
            return objectMapper.readValue(s, clazz);
        } catch (JsonProcessingException e) {
            log.error("failed to parse json", e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 截取视频第六帧的图片
     *
     * @param url        视频地址
     * @param imageSteam 图片输出流
     * @return 视频时间长度
     * @throws FrameGrabber.Exception 解码异常
     */
    public static long videoImage(String url, OutputStream imageSteam) throws IOException {
        GrabResult grabResult;
        try (var videoStream = new URL(url).openStream();
             var grabber = new FFmpegFrameGrabber(videoStream, 0)) {
            grabber.start();
            grabResult = grabKeyFrame(imageSteam, grabber);
            grabber.stop();
        }
        if (!grabResult.success) {
            try (var videoStream = new URL(url).openStream();
                 var grabber = new FFmpegFrameGrabber(videoStream)) {
                grabber.start();
                grabResult = grabKeyFrame(imageSteam, grabber);
                grabber.stop();
            }
        }
        return grabResult.length;
    }

    static GrabResult grabKeyFrame(OutputStream imageSteam, FFmpegFrameGrabber grabber)
            throws FFmpegFrameGrabber.Exception {
        boolean success = false;
        long lengthInTime = grabber.getLengthInTime();
        int lengthInFrames = grabber.getLengthInFrames();
        log.info("lengthInFrames:{},lengthInTime:{}", lengthInFrames, lengthInTime);

        try (Frame frame = grabber.grabKeyFrame()) {
            if (frame != null) {
                log.info("saving frame...");
                saveFrame(frame, imageSteam);
                success = true;
            } else {
                log.error("frame is null");
            }
        }
        return new GrabResult(success, lengthInTime);
    }

    /**
     * 截取缩略图
     *
     * @param f            Frame
     * @param outputStream 输出流
     */
    static void saveFrame(Frame f, OutputStream outputStream) {
        String imageFormat = "png";
        if (null == f || null == f.image) {
            return;
        }
        try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
            BufferedImage image = converter.getBufferedImage(f);
            ImageIO.write(image, imageFormat, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AllArgsConstructor
    static class GrabResult {
        boolean success;
        long length;
    }
}
