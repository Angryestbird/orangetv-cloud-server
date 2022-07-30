package com.orangetv.cloud.videostore.service;

import com.orangetv.cloud.videostore.mapper.MyVideoMapper;
import com.orangetv.cloud.videostore.repo.VideoRepo;
import com.orangetv.cloud.videostore.vo.VideoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayTopService {

    private final MyVideoMapper videoMapper;

    private final ConcurrentHashMap<Integer, AtomicLong>
            playTopCache = new ConcurrentHashMap<>();

    public void videoPlay(Integer id) {
        AtomicLong atomicLong = playTopCache.putIfAbsent(id, new AtomicLong());
        if (atomicLong == null) {
            playTopCache.get(id).incrementAndGet();
        } else {
            atomicLong.incrementAndGet();
        }
    }

    public List<VideoVO> getPlayTop(int num) {
        if (num > 50) throw new IllegalArgumentException("num can't exceed 10");
        return videoMapper.selectMany(VideoRepo.getTop(num)).stream().map(VideoVO::from)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    void SyncToDatabase() {
        log.info("sync cache to DB...");
        for (Integer key : playTopCache.keySet()) {
            long incr = playTopCache.remove(key).get();
            if (incr > 0) {
                videoMapper.videoPlay(key, incr);
            }
        }
    }
}
