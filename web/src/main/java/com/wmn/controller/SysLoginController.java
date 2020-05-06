package com.wmn.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wmn.entity.SysLogin;
import com.wmn.entity.UserInfo;
import com.wmn.mapper.SysLoginMapper;
import com.wmn.mapper.UserInfoMapper;
import com.wmn.utils.JWTToken;
import com.wmn.utils.JWTVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@Controller

@ResponseBody
public class SysLoginController {
    private Map<String, Object> map;


    @Autowired
    SysLoginMapper sysLoginMapper;

    @RequestMapping(value = "sys-log", method = RequestMethod.POST)
    public String getUser(@RequestHeader("Authorization") String authorization, @RequestBody HashMap<String,Integer> hashMap) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {
            map = new HashMap<String, Object>();
            Integer currentPage = hashMap.get("currentPage");
            Integer pageSize = hashMap.get("pageSize");
            Page<SysLogin> page = new Page<>(currentPage,pageSize);
            List<SysLogin> sysLogins = sysLoginMapper.selectPage(page,new EntityWrapper<SysLogin>().orderBy("loginTime",false));
            map.put("code", 200);
            map.put("msg", "success");
            map.put("user_log", sysLogins);
            map.put("total", sysLoginMapper.selectCount(new EntityWrapper<SysLogin>()));
            return JSON.toJSONString(map);
        }
    }
}
