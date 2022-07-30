package com.orangetv.cloud.videostore.vo;

import com.orangetv.cloud.videostore.model.Video;
import lombok.Data;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

@Data
public class VideoVO {
    private Integer id;
    private String url;
    private String title;
    private String coverUrl;
    private Long length;
    private Long play;

    public static VideoVO from(Video video) {
        assert video.getName() != null
                && video.getPath() != null;
        var videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);
        if (StringUtils.isNotBlank(video.getCover())) {
            videoVO.setCoverUrl("/api/ALBUM/store/" + video.getCover());
        }
        if (StringUtils.isNotBlank(video.getPath())) {
            videoVO.setUrl("/video-repo/" + video.getRepoId() + video.getPath() + video.getName());
        }
        var dotPosition = video.getName().lastIndexOf(".");
        videoVO.title = video.getName().substring(0, dotPosition);
        return videoVO;
    }
}
