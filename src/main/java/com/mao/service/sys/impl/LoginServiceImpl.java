package com.mao.service.sys.impl;

import com.mao.config.Config;
import com.mao.entity.ResponseData;
import com.mao.entity.sys.Permission;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.User;
import com.mao.mapper.sys.SystemMapper;
import com.mao.service.BaseService;
import com.mao.service.sys.LoginService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录相关请求处理
 * @author mao by 17:14 2019/9/12
 */
@Service
public class LoginServiceImpl extends BaseService implements LoginService {

    @Resource
    private SystemMapper systemMapper;

    @Resource
    private Config config;

    @Override
    public ResponseData login(String u, String p, boolean r) {
        Subject subject = getSubject();
        //设置超时时间
        subject.getSession().setTimeout(config.getMaxLoginTime()*1000);
        UsernamePasswordToken token = new UsernamePasswordToken(u,p,r);
        try {
            subject.login(token);
            User user = getUserByLogin(u);
            return ok(user);
        } catch (IncorrectCredentialsException e){
            return permission("密码错误");
        }catch (UnknownAccountException e){
            return permission("未知账户");
        }catch (LockedAccountException e){
            return permission("账户锁定");
        }catch (ExcessiveAttemptsException e){
            return permission("响应超时");
        }
    }

    /**
     * 根据登录名查询角色和权限
     * @param login 登录名
     * @return 角色和权限列表
     */
    @Override
    public Role getRoleByUserLogin(String login) {
        Role role = systemMapper.getRoleByUserLogin(login);
        if (null != role){
            List<Permission> permissions = systemMapper.getPermissionByRoleId(role.getId());
            if (permissions.size() > 0){
                role.setPermissions(permissions);
                return role;
            }
        }
        return null;
    }

    /**
     * 根据用户登录名查询用户信息
     * @param login 登录名
     * @return 用户信息
     */
    @Override
    public User getUserByLogin(String login) {
        return systemMapper.getUserByLogin(login);
    }
}