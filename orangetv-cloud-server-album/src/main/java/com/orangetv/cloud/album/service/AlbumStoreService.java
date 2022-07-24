package com.orangetv.cloud.album.service;

import com.orangetv.cloud.album.openfeign.VideoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumStoreService {

    private final VideoClient videoClient;
    private final GridFsTemplate gridFsTemplate;
    private final TaskExecutor taskExecutor;

    public void onVideoMetadataGenerated(int videoId, String msg) {
        String videoPath = getVideoPath(msg);
        if (StringUtils.isBlank(videoPath)) {
            log.error("fail to extract video path.");
            return;
        }
        try (var outputStream = new PipedOutputStream();
             var inputStream = new PipedInputStream(outputStream)) {
            taskExecutor.execute(() -> {
                try {
                    videoImage(videoPath, outputStream);
                } catch (FrameGrabber.Exception e) {
                    log.error("Failed to extract image.", e);
                }
            });
            var objectId = gridFsTemplate
                    .store(inputStream, getPngPath(), MediaType.IMAGE_PNG_VALUE);
            videoClient.videoPosterGenerated(videoId, objectId.toString());
        } catch (IOException e) {
            log.error("Failed to pipe stream.", e);
        }
    }

    private String getVideoPath(String msg) {
        return "";
    }

    byte[] generatePoster(String videoPath) {
        return null;
    }

    /**
     * 截取视频第六帧的图片
     *
     * @param filePath     视频路径
     * @param outputStream 输出流
     * @throws FrameGrabber.Exception
     */
    public static void videoImage(String filePath, OutputStream outputStream)
            throws FrameGrabber.Exception {
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);

        ff.start();
        int ffLength = ff.getLengthInFrames();
        Frame f;
        int i = 0;
        while (i < ffLength) {
            f = ff.grabImage();
            //截取第6帧
            if ((i > 5) && (f.image != null)) {
                //执行截图并写入到输出流
                doExecuteFrame(f, outputStream);
                break;
            }
            i++;
        }
        ff.stop();
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
