package com.orangetv.cloud.videostore;

import com.orangetv.cloud.videostore.service.EventPublisher;
import lombok.var;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.ZoneId;

@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
@ConfigurationPropertiesScan
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(EventPublisher publisher) {
        var epoch = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        return args -> publisher.publishSysInfo(epoch, "started");
    }
}
