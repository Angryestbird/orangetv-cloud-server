package com.orangetv.cloud.videostore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoController {

    @GetMapping("hello")
    public String sayHello() {
        return "hello";
    }
}
