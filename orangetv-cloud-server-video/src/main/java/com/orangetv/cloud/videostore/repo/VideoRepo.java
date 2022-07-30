package com.orangetv.cloud.videostore.repo;

import com.orangetv.cloud.videostore.mapper.generated.VideoDynamicSqlSupport;
import com.orangetv.cloud.videostore.model.Video;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Repository;

import static com.orangetv.cloud.videostore.mapper.generated.VideoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

@Repository
public class VideoRepo {

    public static SelectStatementProvider videoExists(Video video) {
        return select(id).from(VideoDynamicSqlSupport.video)
                .where(repoId, isEqualTo(video.getRepoId()))
                .and(path, isEqualTo(video.getPath()))
                .and(name, isEqualTo(video.getName()))
                .build().render(RenderingStrategies.MYBATIS3);
    }

    public static SelectStatementProvider getTop(int num) {
        return select(id, name, play).from(video)
                .orderBy(play.descending()).limit(num)
                .build().render(RenderingStrategies.MYBATIS3);
    }
}
