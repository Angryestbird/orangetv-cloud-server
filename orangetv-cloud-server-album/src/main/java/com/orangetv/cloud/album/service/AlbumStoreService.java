package com.orangetv.cloud.album.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangetv.cloud.album.config.OrangeTVConfigProps;
import com.orangetv.cloud.album.model.Video;
import com.orangetv.cloud.album.oauth2client.ReactiveVideoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumStoreService {

    private final GridFsTemplate gridFsTemplate;
    private final AsyncTaskExecutor taskExecutor;
    private final ReactiveVideoClient videoClient;

    private final ObjectMapper objectMapper;
    private final OrangeTVConfigProps props;

    public void onVideoMetadataGenerated(long videoId, String msg) {
        log.info("video {} metadata generated\n{}", videoId, msg);
        Video video = parseJSON(msg, Video.class);
        String videoPath = getVideoPath(video);
        if (StringUtils.isBlank(videoPath)) {
            log.error("fail to extract video path.");
            return;
        }
        try (var outputStream = new PipedOutputStream();
             var inputStream = new PipedInputStream(outputStream)) {
            var latch = new CountDownLatch(1);
            var lengthFuture = taskExecutor.submit(() -> {
                long length = 0;
                try {
                    log.info("processing {}", videoPath);
                    length = videoImage(videoPath, outputStream);

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
            var objectId = gridFsTemplate.store(inputStream, getPngPath(),
                    MediaType.IMAGE_PNG_VALUE, video);
            latch.countDown();
            video.setCover(objectId.toString());
            video.setLength(lengthFuture.get());
            videoClient.updateById(video);
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("failed to store image to GridFS", e);
        }
    }

    private String getVideoPath(Video video) {
        var dir = new File(props.getRootPath() + video.getPath());
        return new File(dir, video.getName()).getPath();
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
     * @param filePath     视频路径
     * @param outputStream 输出流
     * @return 视频时间长度
     * @throws FrameGrabber.Exception 解码异常
     */
    public static long videoImage(String filePath, OutputStream outputStream)
            throws FrameGrabber.Exception {
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);

        ff.start();
        int ffFrameLength = ff.getLengthInFrames();
        long ffTimeLength = ff.getLengthInTime();
        Frame f;
        int i = 0;
        while (i < ffFrameLength) {
            f = ff.grabImage();
            //截取第6帧
            if ((i > 5) && (f.image != null)) {
                //执行截图并写入到输出流
                doExecuteFrame(f, outputStream);
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("failed to close", e);
                }
                break;
            }
            i++;
        }
        ff.stop();

        return ffTimeLength;
    }

    /**
     * 生成图片的相对路径
     *
     * @return 图片的相对路径 例：pic/1.png
     */
    private static String getPngPath() {
        return getUUID() + ".png";
    }


    /**
     * 生成唯一的uuid
     *
     * @return uuid
     */
    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 截取缩略图
     *
     * @param f            Frame
     * @param outputStream 输出流
     */
    private static void doExecuteFrame(Frame f, OutputStream outputStream) {
        String imageFormat = "png";
        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        try {
            ImageIO.write(bi, imageFormat, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
