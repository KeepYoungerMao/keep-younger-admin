package com.mao.config.shiro;

import com.mao.entity.sys.Permission;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.User;
import com.mao.service.sys.LoginService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * 用户的权限和认证管理
 *  shiro的实现
 * @author mao in 22:45 2019/9/13
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private LoginService loginService;

    /**
     * 用户的权限管理
     * @param principalCollection 接口：获取登录用户信息
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户登录名
        String login = (String) principalCollection.getPrimaryPrincipal();
        //根据登录名查询该用户的角色权限信息
        Role role = loginService.getRoleByUserLogin(login);
        if (null != role){
            //添加角色和权限
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole(role.getName());
            for (Permission permission : role.getPermissions()){
                info.addStringPermission(permission.getUrl());
            }
            return info;
        }
        return null;
    }

    /**
     * 用户的认证管理
     * @param authenticationToken 用户的票据
     * @return AuthenticationInfo 认证信息
     * @throws AuthenticationException 当获取不到用户时抛出异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后再到请求
        if (authenticationToken.getPrincipal() == null)
            return null;
        //获取用户信息
        String login = (String) authenticationToken.getPrincipal();
        User user = loginService.getUserByLogin(login);
        //账号不存在
        if (null == user)
            throw new UnknownAccountException();
        //账号已锁定
        if (user.isLocked())
            throw new LockedAccountException();
        //getName()为此realm的name，父类方法
        return new SimpleAuthenticationInfo(login,user.getUser_pass(),getName());
    }

}