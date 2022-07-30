package com.orangetv.cloud.videostore;

import com.orangetv.cloud.videostore.mapper.MyVideoMapper;
import com.orangetv.cloud.videostore.model.Video;
import com.orangetv.cloud.videostore.service.GatewayClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest
class VideoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MyVideoMapper videoMapper;

    @Autowired
    private GatewayClient gatewayClient;

    @Test
    @Disabled
    public void testGeneratedKey() {
        Video video = new Video();
        video.setPath("path/to/video/files");
        video.setName("name" + LocalDateTime.now());
        videoMapper.insert(video);
        System.out.println(video.getId());
    }

    @Test
    @Disabled
    @SneakyThrows
    public void testGatewayClient() {
        ResponseEntity<String> hello = gatewayClient.hello();
        System.out.println(hello.getStatusCode());
        System.out.println(hello.getHeaders());
        System.out.println(hello.getBody());
    }
}
