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
        if (current < 1) current = 1;
        int offset = (current - 1) * 10;
        var list = videoMapper.select(c -> c.limit(pageSize).offset(offset));
        var result = list.stream().map(VideoVO::from).collect(Collectors.toList());
        return Pageable.<VideoVO>builder().data(result).current(current)
                .total((int) videoMapper.count(c -> c)).build();
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
        if (current < 1) current = 1;
        int offset = (current - 1) * 10;
        var result = videoMapper.search(pageSize, offset, query)
                .stream().map(VideoVO::from).collect(Collectors.toList());
        return Pageable.<VideoVO>builder().data(result).current(current)
                .total(videoMapper.searchCnt(pageSize, offset, query)).build();
    }
}
