package com.wmn.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.wmn.VO.UserCommentVO;
import com.wmn.entity.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wmn
 * @since 2020-03-09
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
    @Select("SELECT comments.* ,`user_info`.nickname FROM comments,`user_info` WHERE comments.user_id=`user_info`.id")
    List<UserCommentVO> getUserComment();


    @Select("SELECT comments.* ,`user_info`.nickname FROM comments,`user_info` WHERE comments.user_id=`user_info`.id")
    List<UserCommentVO> showUserComment(Page page);


}
