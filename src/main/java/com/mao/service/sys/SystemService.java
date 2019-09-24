package com.mao.service.sys;

import com.mao.entity.ResponseData;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.User;

/**
 * 系统部分业务处理
 * @author mao by 15:30 2019/9/18
 */
public interface SystemService {

    //获取所有角色列表
    ResponseData getRoles();

    //保存一个角色数据
    ResponseData saveRole(Role role);

    //获取所有权限列表
    ResponseData getPermissions();

    //查询用户列表
    ResponseData getUser(Integer role, String name, String login, Integer page);

    //查询用户总页数
    ResponseData getUsers(Integer role, String name, String login);

    //根据id获取用户详情信息
    ResponseData getUserById(String id);

    //保存一个新的用户
    ResponseData saveUser(User user);

    //更新一个用户数据
    ResponseData updateUser(User user);

    //根据登录名查询用户信息
    ResponseData getUser(String login);
}