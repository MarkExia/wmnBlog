package com.wmn.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.wmn.VO.AlbumsVO;
import com.wmn.entity.Article;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wmn
 * @since 2019-09-25
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("SELECT * from article where articleclasses = #{classes}")
    List<Article> getArticles(Pagination page,String classes);
}
