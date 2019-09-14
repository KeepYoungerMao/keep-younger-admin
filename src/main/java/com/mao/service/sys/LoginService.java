package com.mao.service.sys;

import com.mao.entity.ResponseData;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.User;

/**
 * 登录等操作
 * @author mao by 17:13 2019/9/12
 */
public interface LoginService {

    //登录
    ResponseData login(String u, String p, boolean r);

    //根据登录名查询角色权限
    Role getRoleByUserLogin(String login);

    //根据登录名查询用户
    User getUserByLogin(String login);

}