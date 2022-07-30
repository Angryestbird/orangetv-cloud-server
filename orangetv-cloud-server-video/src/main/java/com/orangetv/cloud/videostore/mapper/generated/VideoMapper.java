package com.orangetv.cloud.videostore.mapper.generated;

import static com.orangetv.cloud.videostore.mapper.generated.VideoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import com.orangetv.cloud.videostore.model.Video;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface VideoMapper extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, path, cover, length, play, repoId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Integer.class)
    int insert(InsertStatementProvider<Video> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="VideoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
        @Result(column="cover", property="cover", jdbcType=JdbcType.VARCHAR),
        @Result(column="length", property="length", jdbcType=JdbcType.BIGINT),
        @Result(column="play", property="play", jdbcType=JdbcType.BIGINT),
        @Result(column="repo_id", property="repoId", jdbcType=JdbcType.INTEGER)
    })
    List<Video> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("VideoResult")
    Optional<Video> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, video, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, video, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int insert(Video row) {
        return MyBatis3Utils.insert(this::insert, row, video, c ->
            c.map(name).toProperty("name")
            .map(path).toProperty("path")
            .map(cover).toProperty("cover")
            .map(length).toProperty("length")
            .map(play).toProperty("play")
            .map(repoId).toProperty("repoId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int insertSelective(Video row) {
        return MyBatis3Utils.insert(this::insert, row, video, c ->
            c.map(name).toPropertyWhenPresent("name", row::getName)
            .map(path).toPropertyWhenPresent("path", row::getPath)
            .map(cover).toPropertyWhenPresent("cover", row::getCover)
            .map(length).toPropertyWhenPresent("length", row::getLength)
            .map(play).toPropertyWhenPresent("play", row::getPlay)
            .map(repoId).toPropertyWhenPresent("repoId", row::getRepoId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default Optional<Video> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, video, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default List<Video> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, video, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default List<Video> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, video, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default Optional<Video> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, video, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    static UpdateDSL<UpdateModel> updateAllColumns(Video row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalTo(row::getName)
                .set(path).equalTo(row::getPath)
                .set(cover).equalTo(row::getCover)
                .set(length).equalTo(row::getLength)
                .set(play).equalTo(row::getPlay)
                .set(repoId).equalTo(row::getRepoId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Video row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalToWhenPresent(row::getName)
                .set(path).equalToWhenPresent(row::getPath)
                .set(cover).equalToWhenPresent(row::getCover)
                .set(length).equalToWhenPresent(row::getLength)
                .set(play).equalToWhenPresent(row::getPlay)
                .set(repoId).equalToWhenPresent(row::getRepoId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int updateByPrimaryKey(Video row) {
        return update(c ->
            c.set(name).equalTo(row::getName)
            .set(path).equalTo(row::getPath)
            .set(cover).equalTo(row::getCover)
            .set(length).equalTo(row::getLength)
            .set(play).equalTo(row::getPlay)
            .set(repoId).equalTo(row::getRepoId)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: video")
    default int updateByPrimaryKeySelective(Video row) {
        return update(c ->
            c.set(name).equalToWhenPresent(row::getName)
            .set(path).equalToWhenPresent(row::getPath)
            .set(cover).equalToWhenPresent(row::getCover)
            .set(length).equalToWhenPresent(row::getLength)
            .set(play).equalToWhenPresent(row::getPlay)
            .set(repoId).equalToWhenPresent(row::getRepoId)
            .where(id, isEqualTo(row::getId))
        );
    }
}