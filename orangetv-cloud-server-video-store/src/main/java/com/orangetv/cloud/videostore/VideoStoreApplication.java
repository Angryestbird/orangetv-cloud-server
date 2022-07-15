package com.orangetv.cloud.videostore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class VideoStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoStoreApplication.class, args);
    }
}
