package com.mao.mvc.sys;

import com.mao.entity.ResponseData;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.User;
import com.mao.service.sys.SystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 系统部分请求
 * @author mao by 15:29 2019/9/18
 */
@RestController
@RequestMapping("v/sys")
public class SystemController {

    @Resource
    private SystemService systemService;

    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    @GetMapping("role")
    @RequiresPermissions("sys_role_list")
    public ResponseData getRoles(){
        return systemService.getRoles();
    }

    /**
     * 保存一个角色数据
     * @param role 角色数据
     * @return 成功/失败
     */
    @PutMapping("role")
    @RequiresPermissions("sys_role_add")
    public ResponseData saveRole(@RequestBody Role role){
        return systemService.saveRole(role);
    }

    /**
     * 获取所有权限列表
     * @return 权限列表
     */
    @GetMapping("permission")
    @RequiresPermissions("sys_permission_list")
    public ResponseData getPermissions(){
        return systemService.getPermissions();
    }

    /**
     * 查询用户列表
     * @param role 角色id
     * @param name 名字关键字
     * @param login 登录名关键字
     * @param page 页码
     * @return 用户列表
     */
    @GetMapping("user")
    @RequiresPermissions("sys_user_list")
    public ResponseData getUser(Integer role, String name, String login, Integer page){
        return systemService.getUser(role,name,login,page);
    }

    /**
     * 根据id获取用户详情信息
     * @param id 用户id
     * @return 用户详细信息
     */
    @GetMapping("user/{id}")
    @RequiresPermissions("sys_user_data")
    public ResponseData getUserIntro(@PathVariable("id") String id){
        return systemService.getUserById(id);
    }

    /**
     * 查询用户总页数
     * @param role 角色id
     * @param name 名字关键字
     * @param login 登录名关键字
     * @return 用户列表
     */
    @GetMapping("users")
    @RequiresPermissions("sys_user_count")
    public ResponseData getUsers(Integer role, String name, String login){
        return systemService.getUsers(role,name,login);
    }

    /**
     * 保存一个新用户
     * @param user 用户数据
     * @return 成功 / 失败
     */
    @PutMapping("user")
    @RequiresPermissions("sys_user_add")
    public ResponseData saveUser(@RequestBody User user){
        return systemService.saveUser(user);
    }

    /**
     * 更新一个用户数据
     * @param user 用户数据
     * @return 成功 / 失败
     */
    @PostMapping("user")
    @RequiresPermissions("sys_user_update")
    public ResponseData updateUser(@RequestBody User user){
        return systemService.updateUser(user);
    }

}