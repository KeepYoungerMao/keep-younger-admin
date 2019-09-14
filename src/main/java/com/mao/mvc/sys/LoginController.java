package com.mao.mvc.sys;

import com.mao.entity.ResponseData;
import com.mao.service.sys.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 登录、注册、退出
 * @author mao by 16:50 2019/9/12
 */
@Controller
public class LoginController {

    @Resource
    private LoginService loginService;

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public ResponseData login(String u, String p, boolean r){
        return loginService.login(u, p, r);
    }

}