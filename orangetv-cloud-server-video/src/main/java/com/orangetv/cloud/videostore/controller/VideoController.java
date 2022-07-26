package com.orangetv.cloud.videostore.controller;

import com.orangetv.cloud.videostore.model.Video;
import com.orangetv.cloud.videostore.service.VideoService;
import com.orangetv.cloud.videostore.util.Pageable;
import com.orangetv.cloud.videostore.util.Response;
import com.orangetv.cloud.videostore.vo.VideoVO;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("{id}")
    Response<Video> getById(@PathVariable int id) {
        //noinspection OptionalGetWithoutIsPresent
        return Response.ok(VideoVO.from(videoService.getVideoMapper()
                .selectByPrimaryKey(id).get()));
    }

    @GetMapping("query/page")
    public Response<Pageable<VideoVO>> page(@RequestParam(defaultValue = "0") int current,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(required = false) String search) {
        if (StringUtils.isNotBlank(search)) {
            return Response.ok(videoService.page(current, pageSize, search));
        } else {
            return Response.ok(videoService.page(current, pageSize));
        }
    }

    @PutMapping("{id}")
    Response<Video> putById(@PathVariable int id, @RequestBody Video video) {
        //noinspection OptionalGetWithoutIsPresent
        videoService.getVideoMapper().selectByPrimaryKey(id).get();
        var mapper = videoService.getVideoMapper();
        mapper.updateByPrimaryKeySelective(video);
        return Response.ok(video);
    }

    @GetMapping("test/ping")
    public String ping() {
        return "pong";
    }

}
