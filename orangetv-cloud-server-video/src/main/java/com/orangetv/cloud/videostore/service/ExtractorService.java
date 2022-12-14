package com.orangetv.cloud.videostore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangetv.cloud.videostore.config.OrangeTVConfigProps;
import com.orangetv.cloud.videostore.mapper.MyVideoMapper;
import com.orangetv.cloud.videostore.model.Video;
import com.orangetv.cloud.videostore.repo.VideoRepo;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * 视频元信息提前服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExtractorService implements ApplicationRunner {

    private final MyVideoMapper videoMapper;
    private final ObjectMapper objectMapper;
    private final OrangeTVConfigProps props;
    private final EventPublisher publisher;

    public void extractMetadata(File file) {
        log.info("Adding video to repo, filename is {}.", file.getName());

        var video = new Video();
        video.setName(file.getName());
        video.setRepoId(props.getVideoRepoId());
        File rootDir = new File(props.getVideoScanPath());
        video.setPath(calculateUrlPath(rootDir, file));
        if (videoMapper.selectOne(VideoRepo.videoExists(video)).isPresent()) {
            log.warn("file: '{}' metadata exists", file.getPath());
            return;
        }
        videoMapper.insertSelective(video);
        String json = toJsonString(video);
        try {
            publisher.publishMetadata(video.getId(), json);
        } catch (KafkaException e) {
            videoMapper.deleteByPrimaryKey(video.getId());
            log.error("failed send message to kafka", e);
        }
    }

    @SneakyThrows
    String toJsonString(Object o) {
        return objectMapper.writeValueAsString(o);
    }

    static String calculateUrlPath(File rootDir, File file) {
        var fileDir = file.getParentFile();
        var list = new ArrayList<String>();
        while (!fileDir.equals(rootDir)) {
            list.add(fileDir.getName());
            fileDir = fileDir.getParentFile();
        }
        var stringBuilder = new StringBuilder();
        for (int i = list.size() - 1; i >= 0; i--) {
            stringBuilder.append("/");
            stringBuilder.append(list.get(i));
        }
        stringBuilder.append("/");
        return stringBuilder.toString();
    }

    @Override
    public void run(ApplicationArguments args) {
        FileScanner.builder()
                .basePath(props.getVideoScanPath())
                .suffixes(props.getVideoScanSuffixes().split(","))
                .callback(this::extractMetadata)
                .build().scan();
    }

    @Builder
    @RequiredArgsConstructor
    static class FileScanner {
        private final String basePath;
        private final String[] suffixes;
        private final Consumer<File> callback;

        public void scan() {
            doScan(basePath);
        }

        private void doScan(String path) {
            var dir = new File(path);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IllegalArgumentException("path not a dir:" + path);
            }

            // apply callback to normal file
            for (var file : emptyIfNull(dir.listFiles(File::isFile), File.class))
                for (var suffix : suffixes)
                    if (file.getName().endsWith(suffix))
                        callback.accept(file);

            for (var subDir : emptyIfNull(dir.listFiles(File::isDirectory), File.class)) {
                doScan(subDir.getPath());
            }
        }

        @SuppressWarnings("unchecked")
        private static <T> T[] emptyIfNull(T[] arr, @SuppressWarnings("SameParameterValue")
                Class<T> clazz) {
            if (arr == null)
                return (T[]) Array.newInstance(clazz, 0);
            return arr;
        }
    }
}
