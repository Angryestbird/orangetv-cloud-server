package com.orangetv.cloud.videostore.controller;

import com.orangetv.cloud.videostore.model.Video;
import com.orangetv.cloud.videostore.service.GatewayClient;
import com.orangetv.cloud.videostore.service.PlayTopService;
import com.orangetv.cloud.videostore.service.VideoService;
import com.orangetv.cloud.videostore.util.Pageable;
import com.orangetv.cloud.videostore.util.Response;
import com.orangetv.cloud.videostore.vo.VideoVO;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final GatewayClient gatewayClient;
    private final PlayTopService playTopService;

    @GetMapping("{id}")
    Response<VideoVO> getById(@PathVariable int id) {
        //noinspection OptionalGetWithoutIsPresent
        return Response.ok(VideoVO.from(videoService.getVideoMapper()
                .selectByPrimaryKey(id).get()));
    }

    @GetMapping("query/page")
    public Response<Pageable<VideoVO>> page(@RequestParam(defaultValue = "1") int current,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(required = false) String search) {
        if (StringUtils.isNotBlank(search)) {
            return Response.ok(videoService.page(current, pageSize, search.trim()));
        } else {
            return Response.ok(videoService.page(current, pageSize));
        }
    }

    @PutMapping("{id}")
    Response<Video> putById(@PathVariable int id, @RequestBody Video video) {
        assert id == video.getId();
        //noinspection OptionalGetWithoutIsPresent
        videoService.getVideoMapper().selectByPrimaryKey(id).get();
        var mapper = videoService.getVideoMapper();
        mapper.updateByPrimaryKeySelective(video);
        return Response.ok(video);
    }

    @GetMapping("repo/{id}")
    public ResponseEntity<String> getVideoRepoUri(@PathVariable int id) {
        var route = gatewayClient.getGatewayRoute("video-repo-" + id);
        if (route != null) {
            return ResponseEntity.ok(route.getUri());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("play/{id}")
    public void videoPlay(@PathVariable int id) {
        playTopService.videoPlay(id);
    }

    @GetMapping("play/top/{num}")
    public Response<List<VideoVO>> playTop(@PathVariable int num) {
        return Response.ok(playTopService.getPlayTop(num));
    }

    @GetMapping("test/ping")
    public String ping() {
        return "pong";
    }
}
