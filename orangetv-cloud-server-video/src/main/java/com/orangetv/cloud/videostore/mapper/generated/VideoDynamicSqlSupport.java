package com.orangetv.cloud.videostore.mapper.generated;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class VideoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    public static final Video video = new Video();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: video.id")
    public static final SqlColumn<Integer> id = video.id;

    /**
     * Database Column Remarks:
     *   视频名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: video.name")
    public static final SqlColumn<String> name = video.name;

    /**
     * Database Column Remarks:
     *   存储路径
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: video.path")
    public static final SqlColumn<String> path = video.path;

    /**
     * Database Column Remarks:
     *   封面ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: video.cover")
    public static final SqlColumn<String> cover = video.cover;

    /**
     * Database Column Remarks:
     *   播放时长
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: video.length")
    public static final SqlColumn<Long> length = video.length;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    public static final class Video extends AliasableSqlTable<Video> {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> path = column("path", JDBCType.VARCHAR);

        public final SqlColumn<String> cover = column("cover", JDBCType.VARCHAR);

        public final SqlColumn<Long> length = column("length", JDBCType.BIGINT);

        public Video() {
            super("video", Video::new);
        }
    }
}