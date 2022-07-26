package com.orangetv.cloud.videostore.service;

import com.orangetv.cloud.videostore.mapper.MyVideoMapper;
import com.orangetv.cloud.videostore.util.Pageable;
import com.orangetv.cloud.videostore.vo.VideoVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    @Getter
    private final MyVideoMapper videoMapper;

    /**
     * 分页查询
     *
     * @param current  当前页
     * @param pageSize 页条数
     * @return Pageable<VideoRecord>
     */
    public Pageable<VideoVO> page(int current, int pageSize) {
        var result = videoMapper.page(current, pageSize)
                .stream().map(VideoVO::from).collect(Collectors.toList());
        return Pageable.<VideoVO>builder().data(result)
                .current(current).total(videoMapper.pageCnt(current, pageSize)).build();
    }

    /**
     * 全文搜索分页查询
     *
     * @param current  当前页
     * @param pageSize 页条数
     * @param query    搜索内容
     * @return Pageable<VideoRecord>
     */
    public Pageable<VideoVO> page(int current, int pageSize, String query) {
        var result = videoMapper.search(current, pageSize, query)
                .stream().map(VideoVO::from).collect(Collectors.toList());
        return Pageable.<VideoVO>builder().data(result)
                .current(current).total(videoMapper.searchCnt(current, pageSize, query)).build();
    }
}
