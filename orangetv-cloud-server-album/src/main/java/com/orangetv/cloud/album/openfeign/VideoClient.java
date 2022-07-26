package com.orangetv.cloud.album.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * OpenFeign OAuth2 拦截器只能处理来自gateway的调用
 */
@FeignClient("VIDEO-STORE")
public interface VideoClient {

    @GetMapping("video/ping")
    String ping();
}
