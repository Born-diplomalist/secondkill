package com.born.controller;

import com.born.domain.entity.User;
import com.born.domain.vo.LoginVo;
import com.born.service.UserService;
import com.born.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-07 22:10:33
 */
@RestController
public class IndexController {

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String toLogin(){
        return "/static/login.htm";
    }


    /*
    登录
    1.验证用户名是否存在
    2.验证密码是否匹配
    3.存储信息到Redis，存储token到客户端
     */
    @PostMapping("/login")
    public Result login(HttpServletResponse response,@Validated LoginVo loginVo) {
        return userService.login(response, loginVo);
    }


    /**
     * 注销
     */
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request){
        return userService.logOut(request);
    }



}
