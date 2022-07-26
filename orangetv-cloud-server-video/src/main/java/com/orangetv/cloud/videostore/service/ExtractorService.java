package com.orangetv.cloud.videostore.service;

import com.orangetv.cloud.videostore.config.OrangeTVConfigProps;
import com.orangetv.cloud.videostore.mapper.MyVideoMapper;
import com.orangetv.cloud.videostore.model.Video;
import com.orangetv.cloud.videostore.repo.VideoRepo;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Array;
import java.util.function.Consumer;

/**
 * 视频元信息提前服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExtractorService {

    private final MyVideoMapper videoMapper;
    private final OrangeTVConfigProps props;

    @PostConstruct
    public void generateMetadata() {
        var scanner = FileScanner.builder()
                .basePath(props.getVideoScanPath())
                .suffixes(props.getVideoScanSuffixes().split(","))
                .callback(this::extractMetadata)
                .build();
        scanner.scan();
    }

    public void extractMetadata(File file) {
        log.info("Adding video to repo, filename is {}.", file.getName());

        var video = new Video();
        video.setName(file.getName());
        video.setPath(file.getPath());
        if (videoMapper.selectOne(VideoRepo.videoExists(video)).isPresent()) {
            log.info("file {} metadata exists", file.getPath());
            file.getAbsolutePath();
            return;
        }
        videoMapper.insert(video);
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
