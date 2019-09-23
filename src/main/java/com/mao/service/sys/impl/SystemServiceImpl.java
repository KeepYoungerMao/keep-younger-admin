package com.mao.service.sys.impl;

import com.mao.entity.ResponseData;
import com.mao.entity.sys.Permission;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.User;
import com.mao.mapper.sys.SystemMapper;
import com.mao.mapper.sys.UserMapper;
import com.mao.service.BaseService;
import com.mao.service.sys.SystemService;
import com.mao.util.EnCryptUtil;
import com.mao.util.SU;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统部分业务处理
 * @author mao by 15:31 2019/9/18
 */
@Service
public class SystemServiceImpl extends BaseService implements SystemService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SystemMapper systemMapper;

    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    @Override
    public ResponseData getRoles() {
        List<Role> roles = systemMapper.getRoles();
        return ok(roles);
    }

    /**
     * 保存一个角色数据
     * @param role 角色数据
     * @return 成功/失败
     */
    @Transactional
    @Override
    public ResponseData saveRole(Role role) {
        systemMapper.saveRole(role);
        return role.getId() > 0 ? ok("saved as "+role.getId()) : bad("failed to save");
    }

    /**
     * 获取所有权限列表
     * @return 权限列表
     */
    @Override
    public ResponseData getPermissions() {
        List<Permission> permissions = systemMapper.getPermissions();
        return ok(permissions);
    }

    /**
     * 查询用户列表
     * @param role 角色id
     * @param name 名字关键字
     * @param login 登录名关键字
     * @param page 页码
     * @return 用户列表
     */
    @Override
    public ResponseData getUser(Integer role, String name, String login, Integer page) {
        page = null == page ? 0 : (page == 1 ? 0 : (page - 1)*10);
        if (SU.isEmpty(name)) name = null;
        if (SU.isEmpty(login)) login = null;
        if (null == role || role < 0) role = 0;
        List<User> list = userMapper.getUser(role,name,login,page);
        return ok(list);
    }

    /**
     * 根据id获取用户详情信息
     * @param id 用户id
     * @return 用户详细信息
     */
    @Override
    public ResponseData getUserById(String id) {
        Integer _id = SU.getNumber(id);
        if (null == _id || _id <= 0)
            return bad("invalid param: "+id);
        User user = userMapper.getUserById(_id);
        return ok(user);
    }

    /**
     * 查询用户总页数
     * @param role 角色id
     * @param name 名字关键字
     * @param login 登录名关键字
     * @return 用户列表
     */
    @Override
    public ResponseData getUsers(Integer role, String name, String login) {
        if (SU.isEmpty(name)) name = null;
        if (SU.isEmpty(login)) login = null;
        if (null == role || role < 0) role = 0;
        int count = userMapper.getUserCount(role,name,login);
        if (count > 0)
            count = (count/10) + 1;
        return ok(count);
    }

    /**
     * 保存一个新用户
     * @param user 用户数据
     * @return 成功 / 失败
     */
    @Transactional
    @Override
    public ResponseData saveUser(User user) {
        String s = checkUser(user);
        if (null != s)
            return bad(s);
        user.setUser_pass(EnCryptUtil.MD5(user.getUser_pass()));
        userMapper.saveUser(user);
        int id = user.getId();
        return id > 0 ? ok("saved as "+id) : bad("failed to save");
    }

    /**
     * 更新一个用户数据
     * @param user 用户数据
     * @return 成功 / 失败
     */
    @Transactional
    @Override
    public ResponseData updateUser(User user) {
        if (user.getId() <= 0)
            return bad("param 'id' cannot be null");
        String s = checkUser(user);
        if (null != s)
            return bad(s);
        userMapper.updateUser(user);
        return ok("update success");
    }

    /**
     * 检查用户数据
     * @param user 用户数据
     * @return null / 错误信息
     */
    private String checkUser(User user){
        if (!SU.isRightString(user.getUser_name(),2,10))
            return "invalid param user_name";
        if (!SU.isRightString(user.getUser_login(),4,16))
            return "invalid param user_login";
        if (user.getId() <= 0){
            if (!SU.isRightString(user.getUser_pass(),6,20))
                return "invalid param user_pass";
        }
        if (!SU.isRightString(user.getCompany(),4,20))
            return "invalid param company";
        if (!SU.isRightString(user.getDept(),1,20))
            return "invalid param dept";
        //身份证号
        if (!SU.isIdCard(user.getIdentity_code())){
            return "user id card is not right";
        }
        if (!SU.isRightString(user.getAddress(),1,50))
            return "invalid param address";
        //手机号、邮箱
        if (!SU.isPhone(user.getPhone()))
            return "user phone is not right";
        if (!SU.isEmail(user.getEmail()))
            return "user email id not right";
        return null;
    }
}