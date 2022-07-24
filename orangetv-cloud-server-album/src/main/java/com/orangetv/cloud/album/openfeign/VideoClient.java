package com.orangetv.cloud.album.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("VIDEO-STORE")
public interface VideoClient {

    @PutMapping("video/poster/{videoId}/{posterId}")
    void videoPosterGenerated(@PathVariable Integer videoId, @PathVariable String posterId);
}
