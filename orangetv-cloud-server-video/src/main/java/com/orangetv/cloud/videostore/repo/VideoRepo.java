package com.orangetv.cloud.videostore.repo;

import com.orangetv.cloud.videostore.model.Video;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Repository;

import static com.orangetv.cloud.videostore.mapper.generated.VideoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

@Repository
public class VideoRepo {

    public static SelectStatementProvider videoExists(Video videoVO) {
        return select(id).from(video)
                .where(path, isEqualTo(videoVO.getPath()))
                .and(name, isEqualTo(videoVO.getName()))
                .build().render(RenderingStrategies.MYBATIS3);
    }
}
