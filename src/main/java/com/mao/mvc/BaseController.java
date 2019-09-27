package com.mao.mvc;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 错误页面跳转、无权限页面跳转、普通页面跳转
 * @author mao by 17:12 2019/9/17
 */
@Controller
public class BaseController implements ErrorController {

    private static final String ERROR = "error";

    @Override
    public String getErrorPath() {
        return ERROR;
    }

    //错误页面
    @GetMapping(ERROR)
    public String error(){
        return ERROR;
    }

    //无权限页面
    @GetMapping("unAuth")
    public String unAuth(){
        return "unAuth";
    }

    //系统管理：用户管理页面
    @GetMapping("sys/user")
    @RequiresPermissions("sys_user_page")
    public String sys_user(){
        return "sys/user";
    }

    //系统管理：角色权限管理页面
    @GetMapping("sys/permission")
    @RequiresPermissions("sys_permission_page")
    public String sys_permission(){
        return "sys/permission";
    }

    //系统管理：个人资料管理页面
    @GetMapping("sys/self")
    @RequiresPermissions("sys_self_page")
    public String sys_self(){
        return "sys/self";
    }

}