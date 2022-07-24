package com.orangetv.cloud.videostore;

import com.orangetv.cloud.videostore.service.VideoEventPublisher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(VideoEventPublisher publisher) {
        return args -> publisher.onPosterGenerated(0, "hello");
    }
}
