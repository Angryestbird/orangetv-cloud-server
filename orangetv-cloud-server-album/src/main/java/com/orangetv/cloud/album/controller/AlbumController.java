package com.orangetv.cloud.album.controller;

import com.orangetv.cloud.album.openfeign.VideoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("store")
@RequiredArgsConstructor
@Slf4j
public class AlbumController {

    private final VideoClient videoClient;

    @GetMapping(value = "{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getById(@PathVariable int id) {
        log.info("get image from gridFS,id is {}", id);
        return "album".getBytes(StandardCharsets.UTF_8);
    }

    @GetMapping("hello")
    public String hello() {
        log.info("hello from VIDEO-STORE");
        return videoClient.hello();
    }
}
