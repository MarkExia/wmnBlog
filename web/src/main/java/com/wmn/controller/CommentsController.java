package com.wmn.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wmn.VO.UserCommentVO;
import com.wmn.entity.Comments;
import com.wmn.mapper.CommentsMapper;
import com.wmn.utils.JWTToken;
import com.wmn.utils.JWTVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wmn
 * @since 2020-03-09
 */
@Controller
@RequestMapping("/comments")
@ResponseBody
public class CommentsController {
    private Map<String, Object> map;

    @Autowired
    CommentsMapper commentsMapper;


    /**
     * 新增职位
     */
    @RequestMapping(value = "add-comment", method = RequestMethod.POST)
    public String addComment(@RequestBody HashMap<String,String> requestMap, @RequestHeader("Authorization") String authorization) throws ParseException {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null) {
            return JSON.toJSONString(erroToken);
        } else {
            map = new HashMap<String, Object>();
            //处理富文本数据
            //comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));

            String time = requestMap.get("time");
            String content = HtmlUtils.htmlEscape(requestMap.get("content"));
            String id =  JWTToken.verifyTokenToGetData("id");
            String nickName = JWTToken.verifyTokenToGetData("nickName");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(time);

            Comments comment = new Comments();
            comment.setContent(content);
            comment.setTime(parse);
            comment.setUserId(Integer.parseInt(id));
            System.out.println("userid="+id);
            int insert = commentsMapper.insert(comment);
            if(insert != 0){
                map.put("msg","留言成功");
                map.put("code", 200);
            }
            return JSON.toJSONString(map);
        }
    }

    /**
     * 查询所有留言
     * @param authorization
     * @return
     */
    @RequestMapping(value = "get-comments", method = RequestMethod.GET)
    public String getAllComs(@RequestHeader("Authorization") String authorization) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null) {
            return JSON.toJSONString(erroToken);
        } else {
            //List<Jobs> jobList=jobsMapper.selectList(new EntityWrapper<Jobs>());
            //查找所有用户数count
            int count = commentsMapper.selectCount(new EntityWrapper<Comments>());
            map = new HashMap<String, Object>();
            //分页查找用户
            EntityWrapper<Comments> queryWrapper =  new EntityWrapper<>();
            queryWrapper.orderBy("id",true);
            List<UserCommentVO> userComment = commentsMapper.getUserComment();
            //Page<UserCommentVO> userComment = commentsService.getUserComment(page);
            map = new HashMap<String, Object>();
            if(userComment.size() != 0){
                for (UserCommentVO userCommentVO : userComment) {

                    userCommentVO.setContent(HtmlUtils.htmlUnescape(userCommentVO.getContent()));
                    //System.out.println(userCommentVO.toString());
                }
                map.put("code", 200);
                map.put("msg", "success");
                map.put("comments_list", userComment);
                map.put("total", count);
                return JSON.toJSONString(map);
            }
            return JSON.toJSONString(erroToken);
        }
    }


    @RequestMapping(value = "delete-com", method = RequestMethod.GET)
    public String deleteCom (@RequestHeader("Authorization") String authorization,@RequestParam("id") String id) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {
            int isDelete = commentsMapper.deleteById((Integer.parseInt(id)));
            map = new HashMap<String, Object>();
            if (isDelete != 0) {
                map.put("code", 200);
                map.put("msg", "删除成功");
                return JSON.toJSONString(map);
            } else {
                map.put("code", 500);
                map.put("msg", "删除失败");
                return JSON.toJSONString(map);
            }
        }
    }
    /**
     * 查询所有留言
     * 前台显示接口,采用后端分页方式
     * @param authorization
     * @return
     */
    @RequestMapping(value = "show-comments", method = RequestMethod.POST)
    public String getAllComs(@RequestHeader("Authorization") String authorization,@RequestBody HashMap<String,String> requestMap) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null) {
            return JSON.toJSONString(erroToken);
        } else {
            //List<Jobs> jobList=jobsMapper.selectList(new EntityWrapper<Jobs>());
            //查找所有用户数count
            int count = commentsMapper.selectCount(new EntityWrapper<Comments>());
            map = new HashMap<String, Object>();
            //分页查找用户
//            EntityWrapper<Comments> queryWrapper =  new EntityWrapper<>();
//            queryWrapper.orderBy("id",true);
            int currentPage =  Integer.parseInt(requestMap.get("currentPage"));
            int pageSize =  Integer.parseInt(requestMap.get("pageSize"));
            // 查询第currentPage页，每页返回pageSize条
            Page<UserCommentVO> page = new Page<>(currentPage,pageSize);
            List<UserCommentVO> userComment = commentsMapper.showUserComment(page);
            map = new HashMap<String, Object>();
            if(userComment.size() != 0){
                for (UserCommentVO userCommentVO : userComment) {

                    userCommentVO.setContent(HtmlUtils.htmlUnescape(userCommentVO.getContent()));
                    //System.out.println(userCommentVO.toString());
                }
                map.put("code", 200);
                map.put("msg", "success");
                map.put("comment_list", userComment);
                map.put("total", count);
                map.put("currentPage", currentPage);
                map.put("pageSize", pageSize);
                return JSON.toJSONString(map);
            }
            return JSON.toJSONString(erroToken);
        }
    }


}
