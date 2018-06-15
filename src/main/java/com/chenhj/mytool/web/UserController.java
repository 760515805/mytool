package com.chenhj.mytool.web;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: UserController.java
* @Description: 该类的功能描述
*有用户注册才需要这个
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月15日 上午9:31:35 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月15日     chenhj          v1.0.0               修改原因
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
    @Value(value = "${oss.security.userName}")
    private String userN;

    @Value(value = "${oss.security.password}")
    private String passwd;


    @GetMapping(value = {"/","/index"})
    public String index(){
        return "redirect:/oss/list/";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String loginValidate(String username, String password, Model model, HttpServletRequest request){
        if (StrUtil.equalsIgnoreCase(username,userN)&&StrUtil.equalsIgnoreCase(password,passwd)){
        	JSONObject js = new JSONObject();
        	js.put("userName", username);
            //request.getSession().setAttribute("USER", ImmutableMap.of(,username));
        	request.getSession().setAttribute("USER", js);
            return "redirect:/oss/list/";
        }
        model.addAttribute("message","用户名或密码错误");
        return "login";
    }
}
