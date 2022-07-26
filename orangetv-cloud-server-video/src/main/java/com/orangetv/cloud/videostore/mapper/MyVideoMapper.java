package com.orangetv.cloud.videostore.mapper;

import com.orangetv.cloud.videostore.mapper.generated.VideoMapper;
import com.orangetv.cloud.videostore.model.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyVideoMapper extends VideoMapper {

    @Select("SELECT * FROM video_record limit(( #{current} - 1) * #{pageSize}, #{pageSize})")
    List<Video> page(@Param("current") int current, @Param("pageSize") int pageSize);

    @Select("SELECT count(*) FROM video_record")
    int pageCnt(@Param("current") int current, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM video_record WHERE MATCH (name) AGAINST (#{query}) " +
            "limit(( #{current} - 1) * #{pageSize}, #{pageSize})")
    List<Video> search(@Param("current") int current, @Param("pageSize") int pageSize,
                       @Param("query") String query);

    @Select("SELECT COUNT(*) FROM video_record WHERE MATCH (name) AGAINST (#{query}) ")
    int searchCnt(@Param("current") int current, @Param("pageSize") int pageSize,
                  @Param("query") String query);

}
