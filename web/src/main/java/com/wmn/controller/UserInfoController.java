package com.wmn.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wmn.entity.SysLogin;
import com.wmn.entity.UserInfo;
import com.wmn.mapper.SysLoginMapper;
import com.wmn.mapper.UserInfoMapper;
import com.wmn.service.IUserInfoService;
import com.wmn.service.impl.UserInfoServiceImpl;
import com.wmn.utils.JWTToken;
import com.wmn.utils.JWTVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wmn
 * @since 2019-09-25
 */
@Controller
@RequestMapping("/user")
@ResponseBody
public class UserInfoController {
    private Map<String, Object> map;

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    SysLoginMapper sysLoginMapper;
    @Autowired
    IUserInfoService iUserInfoService;

    @RequestMapping(value = "getuserinfo", method = RequestMethod.GET)
    public String getUser(@RequestHeader("Authorization") String authorization) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization)) ) {
            return JSON.toJSONString(erroToken);
        } else {
            map = new HashMap<String, Object>();
            List<UserInfo> userInfos = userInfoMapper.selectList(new EntityWrapper<UserInfo>());
            map.put("code", 200);
            map.put("msg", "success");
            map.put("user_list", userInfos);
            map.put("total", userInfos.size());
            return JSON.toJSONString(map);
        }
    }

    /**
     * 更新用户密码
     *
     * @return
     */
    @RequestMapping(value = "updataUserpwd", method = RequestMethod.POST)

    public String updataUserpwd(@RequestHeader("Authorization") String authorization, @RequestParam("uid") String uid, @RequestParam("pwd") String pwd) {
//        String uid = request.getParameter("uid");
//        String pwd = request.getParameter("pwd");
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {
            UserInfo u = userInfoMapper.selectById(Integer.parseInt(uid));
            map = new HashMap<String, Object>();
            u.setPwd(pwd);
            userInfoMapper.updateById(u);
            System.out.println(u.toString());
            map = new HashMap<String, Object>();
            map.put("code", 200);
            map.put("msg", "success");
            return JSON.toJSONString(map);
        }


    }

    @RequestMapping(value = "deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestHeader("Authorization") String authorization, @RequestParam("uid") String uid) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {
            int isDelete = userInfoMapper.deleteById((Integer.parseInt(uid)));
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
     * 注册
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@RequestBody UserInfo u) {
//        String nickName = requestMap.get("nickname");
//        String pwd = requestMap.get("pwd");
        map = new HashMap<String, Object>();
//        UserInfo  u = new UserInfo();
//        u.setRealname(nickName);
//        u.setPwd(pwd);
        int insert = userInfoMapper.insert(u);
        if(insert != 0){
            map.put("msg","注册成功");
            map.put("code", 200);
        }
        return JSON.toJSONString(map);
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String doLogin(@RequestBody HashMap<String,String> requestMap) {
        String nickName = requestMap.get("nickname");
        String pwd = requestMap.get("pwd");
        map = new HashMap<String, Object>();
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("nickname", nickName);
        List<UserInfo> users = userInfoMapper.selectByMap(selectMap);
        if (users.size() != 0) {
            UserInfo user = users.get(0);
            if (pwd.equals(user.getPwd())) {
                SysLogin sysLogin = new SysLogin();
                sysLogin.setLoginTime(new Date());
                sysLogin.setUserName(nickName);
                sysLoginMapper.insert(sysLogin);
                try {
                    map.put("token", JWTToken.createToken(user));
                    map.put("user_name", user.getNickname());
                    map.put("code", 200);
                    map.put("msg", "success");
                    return JSON.toJSONString(map);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                map.put("code", 201);
                map.put("msg", "密码错误");
                return JSON.toJSONString(map);
            }
        } else {
            map.put("code", 500);
            map.put("msg", "用户名不存在");
            return JSON.toJSONString(map);
        }
        return null;
    }

    @GetMapping("get-user")
    public  String getInfo(){

        return String.valueOf(iUserInfoService.selectById(1));
    }

}
