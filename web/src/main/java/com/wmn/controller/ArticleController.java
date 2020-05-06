package com.wmn.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wmn.VO.UserCommentVO;
import com.wmn.entity.Article;
import com.wmn.entity.ArticleClasses;
import com.wmn.entity.ArticleTree;
import com.wmn.mapper.ArticleClassesMapper;
import com.wmn.mapper.ArticleMapper;
import com.wmn.utils.JWTToken;
import com.wmn.utils.JWTVerify;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 前端控制器
 * </p>
 *此controller 还未验证token ，后续修改添加
 * @author wmn
 * @since 2019-09-25
 */
@Controller
@RequestMapping("/article")
@ResponseBody
public class ArticleController {

    Map<String, Object> map;


    @Autowired
    ArticleClassesMapper articleClassesMapper;
    @Autowired
    ArticleMapper articleMapper;

    @RequestMapping(value = "showArticls", method = RequestMethod.GET)
    public String showArticls(@RequestHeader("Authorization") String authorization) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {

            map = new HashMap<String, Object>();
            // 数据处理，返回指定格式
            ArrayList<Map> list = new ArrayList<Map>();
            // ConcurrentHashMap 线程安全
            ConcurrentHashMap<String, Object> parentMap;
            List<ArticleClasses> findAll = articleClassesMapper.selectList(null);
            // AtomicInteger 线程安全，起始值1000
            AtomicInteger atomicInteger = new AtomicInteger(1000);
            for (ArticleClasses articleClasses : findAll) {
                parentMap = new ConcurrentHashMap<String, Object>();
                Wrapper<Article> selectByClasses = new EntityWrapper<>();
                selectByClasses.eq("articleclasses", articleClasses.getClasses());
                // 如果此列表下有子列表，
                if (articleMapper.selectList(selectByClasses).size() != 0) {
                    parentMap.put("id", atomicInteger.getAndIncrement());
                    parentMap.put("label", articleClasses.getClasses());
                    List<Article> articles = articleMapper.selectList(selectByClasses);
                    ArticleTree articleTree;
                    List<ArticleTree> articleTreeList = new ArrayList<ArticleTree>();
                    for (Article article : articles) {
                        articleTree = new ArticleTree();
                        articleTree.setId(article.getId());
                        articleTree.setLabel(article.getTitle());
                        articleTreeList.add(articleTree);
                    }
                    parentMap.put("children", articleTreeList);
                } else {
                    parentMap.put("id", atomicInteger.getAndIncrement());
                    parentMap.put("label", articleClasses.getClasses());
                }
                list.add(parentMap);
            }

            map.put("menuData", list);
            map.put("code", 200);

            return JSON.toJSONString(map);
        }
    }


    @RequestMapping(value = "article-detail", method = RequestMethod.GET)
    public String getArticleDetail(@RequestParam("id") String id ) {
        map = new HashMap<String, Object>();
        Article article = articleMapper.selectById(Integer.valueOf(id));
        if(article ==null){
            map.put("msg", "未找到笔记！");
            map.put("code", 500);
            return JSON.toJSONString(map);
        }else{
            map.put("msg", "success");
            map.put("code", 200);
            map.put("articledetail", article);
            return JSON.toJSONString(map);
        }
    }

    @RequestMapping(value = "addClasses", method = RequestMethod.GET)
    public String addClasses(@RequestParam("newClasses") String newClasses){
        map = new HashMap<String, Object>();
        ArticleClasses articleClasses = new ArticleClasses();
        articleClasses.setClasses(newClasses);
        articleClassesMapper.insert(articleClasses);
        map.put("msg", "success");
        map.put("code", 200);
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "addNote", method = RequestMethod.POST)
    public String addNote(@RequestBody HashMap<String,String> requestMap)  {
        map = new HashMap<String, Object>();
        // 格式{"title":"测试java","classes":"JAVA","content":"啦啦啦"}
        // Date date = new Date();
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Article article = new Article();
        article.setTitle(requestMap.get("title"));
        article.setContent(requestMap.get("content"));

        article.setArticleclasses(requestMap.get("classes"));
        article.setTime(new Date());
        System.out.println(article.toString());

        int save = articleMapper.insert(article);
        if (save != 0) {
            map.put("msg", "success");
            map.put("code", 200);
            return JSON.toJSONString(map);
        }else{
            map.put("msg", "failed");
            map.put("code", 500);
            return JSON.toJSONString(map);
        }

    }
    @RequestMapping(value = "updataNote", method = RequestMethod.POST)
    public String updataNote(@RequestBody HashMap<String,String> requestMap)  {
        map = new HashMap<String, Object>();
        // 格式{"title":"测试java","classes":"JAVA","content":"啦啦啦"}

        String classes = requestMap.get("classes");
        // Date date = new Date();
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Article article = new Article();
        article.setId(Integer.parseInt(requestMap.get("id")));
        article.setTitle(requestMap.get("title"));
        article.setContent(requestMap.get("content"));

        article.setArticleclasses(requestMap.get("articleclasses"));
        article.setTime(new Date());
        System.out.println(article.toString());

        int update = articleMapper.updateById(article);
        if (update != 0) {
            map.put("msg", "success");
            map.put("code", 200);
            return JSON.toJSONString(map);
        }else{
            map.put("msg", "failed");
            map.put("code", 500);
            return JSON.toJSONString(map);
        }

    }

    @RequestMapping(value = "deleteNote", method = RequestMethod.GET)
    public String deleteNote(@RequestParam("id") int id){
        map = new HashMap<String, Object>();

        int delete = articleMapper.deleteById(id);
        if (delete != 0) {
            map.put("msg", "success");
            map.put("code", 200);
            return JSON.toJSONString(map);
        }else{
            map.put("msg", "failed");
            map.put("code", 500);
            return JSON.toJSONString(map);
        }


    }

    @PostMapping("getArticles")
    public String getArticles(@RequestBody HashMap<String,String> hashMap){
        map = new HashMap<>();
        Wrapper<Article> selectByClasses = new EntityWrapper<>();
        selectByClasses.eq("articleclasses", hashMap.get("classes"));
        //List<Article> articles = articleMapper.selectList(selectByClasses);
        int currentPage = Integer.parseInt(hashMap.get("currentPage"));
        int pageSize = Integer.parseInt(hashMap.get("pageSize"));
        // 查询第currentPage页，每页返回pageSize条
        Page<Article> page = new Page<>(currentPage,pageSize);
        //List<Article> articles = articleMapper.selectPage(page, selectByClasses);
        List<Article> articles = articleMapper.getArticles(page,hashMap.get("classes"));
        for (Article article : articles) {
            article.setContent(article.getContent().substring(0,article.getContent().length()/3)+"...");
        }
        Integer total = articleMapper.selectCount(selectByClasses);
        map.put("articles",articles);
        map.put("total",total);
        map.put("msg", "success");
        map.put("code", 200);
        return JSON.toJSONString(map);
    }
    @GetMapping("get-home-notes")
    public String getHomeNote(){
        map = new HashMap<>();
        List<Article> limit4 = articleMapper.selectList(new EntityWrapper<Article>().last("limit 4"));

        map.put("articles",limit4);
        map.put("msg", "success");
        map.put("code", 200);
        return JSON.toJSONString(map);
    }
}
