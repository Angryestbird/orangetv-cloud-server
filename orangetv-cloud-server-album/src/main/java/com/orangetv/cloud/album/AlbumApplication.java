package com.orangetv.cloud.album;

import com.orangetv.cloud.album.utilities.BearerTokenRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
public class AlbumApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumApplication.class, args);
    }

    @Bean
    public BearerTokenRequestInterceptor interceptor() {
        return new BearerTokenRequestInterceptor();
    }
}
