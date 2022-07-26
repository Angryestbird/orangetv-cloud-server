package com.orangetv.cloud.videostore.controller;

import com.orangetv.cloud.videostore.model.Video;
import com.orangetv.cloud.videostore.service.VideoService;
import com.orangetv.cloud.videostore.util.Pageable;
import com.orangetv.cloud.videostore.util.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("page")
    public Response<Pageable<Video>> page(@RequestParam(defaultValue = "0") int current,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String search) {
        if (StringUtils.isNotBlank(search)) {
            return Response.ok(videoService.page(current, pageSize, search));
        } else {
            return Response.ok(videoService.page(current, pageSize));
        }
    }
}
