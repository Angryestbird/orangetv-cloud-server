package com.orangetv.cloud.videostore.mapper;

import com.orangetv.cloud.videostore.mapper.generated.VideoMapper;
import com.orangetv.cloud.videostore.model.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MyVideoMapper extends VideoMapper {

    @Select("SELECT * FROM video WHERE MATCH (name) AGAINST ( #{query} ) LIMIT #{pageSize} OFFSET #{offset}")
    List<Video> search(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("query") String query);

    @Select("SELECT COUNT(*) FROM video WHERE MATCH ( name ) AGAINST ( #{query} )")
    int searchCnt(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("query") String query);

    @Update("UPDATE video SET play = play + #{incr} WHERE id = #{id}")
    int videoPlay(int id,long incr);
}
