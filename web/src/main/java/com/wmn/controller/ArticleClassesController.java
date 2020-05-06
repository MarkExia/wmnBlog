package com.wmn.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wmn.entity.ArticleClasses;
import com.wmn.mapper.ArticleClassesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wmn
 * @since 2019-09-25
 */
@RestController
@RequestMapping("/articleClasses")
public class ArticleClassesController {
    @Autowired
    ArticleClassesMapper articleClassesMapper;


    @GetMapping("get-classes")
    public String getClasses(){
        Map hashMap = new HashMap<>();
        List<ArticleClasses> articleClasses = articleClassesMapper.selectList(new EntityWrapper<ArticleClasses>());
        hashMap.put("classes",articleClasses);
        hashMap.put("code",200);
        hashMap.put("msg","success");

        return JSON.toJSONString(hashMap);
    }
}
