package com.orangetv.cloud.album;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangetv.cloud.album.model.Video;
import com.orangetv.cloud.album.oauth2client.ReactiveVideoClient;
import lombok.var;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlbumApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReactiveVideoClient videoClient;

    @Test
    @Disabled
    void contextLoads() {
    }

    @Test
    @Disabled
    public void updateById() throws JsonProcessingException {
        var video = new Video();
        video.setId(999);
        var response = videoClient.updateById(video);
        System.out.println(objectMapper.writeValueAsString(response));
    }

    @Test
    @Disabled
    public void ping() {
        System.out.println(videoClient.ping());
    }
}
