package com.orangetv.cloud.videostore;

import com.orangetv.cloud.videostore.mapper.MyVideoMapper;
import com.orangetv.cloud.videostore.model.Video;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class VideoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MyVideoMapper videoMapper;

    @Test
    @Disabled
    public void testGeneratedKey() {
        Video video = new Video();
        video.setPath("path/to/video/files");
        video.setName("name" + LocalDateTime.now());
        videoMapper.insert(video);
        System.out.println(video.getId());
    }
}
