package com.orangetv.cloud.videostore.vo;

import com.orangetv.cloud.videostore.model.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class VideoVO extends Video {

    private String url;
    private String coverUrl;

    public static VideoVO from(Video video) {
        assert video.getName() != null
                && video.getPath() != null;
        var videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);
        if (StringUtils.isNotBlank(video.getCover())) {
            videoVO.setCoverUrl("/ALBUM/store/" + video.getCover());
        }
        videoVO.setUrl("/videoRepo" + (StringUtils.isBlank(video.getPath()) ?
                "/" : (video.getPath() + "/")) + video.getName());
        return videoVO;
    }
}
