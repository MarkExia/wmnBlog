package com.wmn.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.wmn.VO.AlbumsVO;
import com.wmn.entity.Albums;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wmn
 * @since 2020-03-13
 */
@Mapper
public interface AlbumsMapper extends BaseMapper<Albums> {
    @Select("SELECT * from (SELECT albums.* , albums_classes.album_name from albums,albums_classes where albums.class_id=albums_classes.id ORDER BY albums.class_id) temp GROUP BY temp.album_name")
    List<AlbumsVO> getCover(Pagination page);

//    int deleteByArrayForPhoto(@Param("path") String path[]);
}
