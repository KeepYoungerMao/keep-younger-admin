package com.mao.service.sys.impl;

import com.mao.config.Config;
import com.mao.entity.ResponseData;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.RoleDo;
import com.mao.entity.sys.User;
import com.mao.mapper.sys.SystemMapper;
import com.mao.mapper.sys.UserMapper;
import com.mao.service.BaseService;
import com.mao.service.sys.SystemService;
import com.mao.util.EnCryptUtil;
import com.mao.util.SU;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    @Resource
    private Config config;

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
    public ResponseData saveRole(RoleDo role) {
        String check = checkRole(role);
        if(null != check)
            return bad(check);
        List<Integer> ids = getPermissionIds(role.getPermissions());
        //保存角色
        systemMapper.saveRole(role);
        int id = role.getId();
        if (id <= 0)
            return bad("保存失败");
        //保存该角色权限
        if (ids.size() > 0)
            systemMapper.savePermissions(ids,id);
        return ok("保存成功");
    }

    /**
     * 更新一个角色数据
     * @param role 角色数据
     * @return 成功 / 失败
     */
    @Transactional
    @Override
    public ResponseData updateRole(RoleDo role) {
        if (role.getId() <= 0)
            return bad("无效id");
        String check = checkRole(role);
        if(null != check)
            return bad(check);
        List<Integer> ids = getPermissionIds(role.getPermissions());
        //更新角色
        systemMapper.updateRole(role);
        //删除原关联数据
        systemMapper.delPermissionByRoleId(role.getId());
        //添加新数据
        systemMapper.savePermissions(ids,role.getId());
        return ok("保存成功");
    }

    /**
     * 提出不正常id
     * @param ids 权限id列表
     * @return 权限id列表
     */
    private List<Integer> getPermissionIds(List<Integer> ids){
        List<Integer> list = new ArrayList<>();
        for (Integer id : ids) {
            if (null != id && id > 0){
                list.add(id);
            }
        }
        return list;
    }

    /**
     * 角色保存或更新之前的检查
     * @param role 角色数据
     * @return null / 错误信息
     */
    private String checkRole(RoleDo role){
        if (SU.isEmpty(role.getName()))
            return "缺少角色名称";
        if (SU.isEmpty(role.getIntro()))
            return "缺少角色描述";
        int count = systemMapper.getCountByRoleName(role.getName(),role.getId());
        if (count > 0)
            return "角色名称已经使用过";
        return null;
    }

    /**
     * 根据角色id获取该角色所有权限列表
     * @param role 角色id
     * @return 权限列表
     */
    @Override
    public ResponseData getPermissions(Integer role) {
        if (null == role || role < 0) role = 0;
        List<Integer> ids = systemMapper.getPermissionIdsByRoleId(role);
        return ok(ids);
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
        if (null == role || role < 0) role = 0;
        if (SU.isEmpty(name)) name = null;
        if (SU.isEmpty(login)) login = null;
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

    /**
     * 获取当前登录的用户信息
     * @param login 登录名
     * @return 用户信息
     */
    @Override
    public ResponseData getUser(String login) {
        User user = userMapper.getUserIntroByLogin(login);
        return ok(user);
    }

    /**
     * 更新用户便签
     * @param id 用户id
     * @param note 便签数据
     * @return 成功 / 失败
     */
    @Override
    public ResponseData updateUserNote(Integer id, String note) {
        if (null == id)
            return bad("loss param id");
        if (id <= 0)
            return bad("param id cannot be " + id);
        if (!SU.isRightString(note,1,100)){
            return bad("invalid param note");
        }
        int count = userMapper.updateUserNoteById(id,note);
        return count > 0 ? ok("update success") :
                bad("cannot find user by id: " + id);
    }

    /**
     * 用户自己更新自己的数据
     * @param user 用户数据
     * @return 成功 / 失败
     */
    @Override
    public ResponseData updateUserBySelf(User user) {
        if (user.getId() <= 0)
            return bad("loss param id");
        User _user = new User();
        _user.setId(user.getId());
        boolean flag = false;
        //登录名
        if (SU.isNotEmpty(user.getUser_login())){
            if (SU.isRightString(user.getUser_login(),4,16)){
                _user.setUser_login(user.getUser_login());
                flag = true;
            } else {
                return bad("invalid param user_login:"+user.getUser_login());
            }
        } else if (SU.isNotEmpty(user.getNote())){
            if (SU.isRightString(user.getNote(),1,100)){
                _user.setNote(user.getNote());
                flag = true;
            } else {
                return bad("invalid param note: "+user.getNote());
            }
        } else if (SU.isNotEmpty(user.getAddress())){
            if (SU.isRightString(user.getAddress(),1,50)){
                _user.setAddress(user.getAddress());
                flag = true;
            } else {
                return bad("invalid param address: "+user.getAddress());
            }
        } else if (SU.isNotEmpty(user.getPhone())){
            if (SU.isPhone(user.getPhone())){
                _user.setPhone(user.getPhone());
                flag = true;
            } else {
                return bad("invalid param phone: "+user.getPhone());
            }
        } else if (SU.isNotEmpty(user.getEmail())){
            if (SU.isEmail(user.getEmail())){
                _user.setEmail(user.getEmail());
                flag = true;
            } else {
                return bad("invalid param email: "+user.getEmail());
            }
        } else if (SU.isNotEmpty(user.getQq())){
            if (SU.isRightString(user.getQq(),4,16) && SU.isNumber(user.getQq())){
                _user.setQq(user.getQq());
                flag = true;
            } else {
                return bad("invalid param qq: "+user.getQq());
            }
        } else if (SU.isNotEmpty(user.getWx())){
            if (SU.isRightString(user.getWx(),1,30)){
                _user.setWx(user.getWx());
                flag = true;
            } else {
                return bad("invalid param: "+user.getWx());
            }
        }
        if (flag){
            int count = userMapper.updateUserBySelf(_user);
            return count > 0 ? ok("update success") :
                    bad("cannot find user by id: " + user.getId());
        } else {
            return bad("cannot update anything because anything is null");
        }
    }

    /**
     * 更新用户头像
     * @param file 头像图片
     * @return 成功 / 失败
     */
    @Transactional
    @Override
    public ResponseData updateUserImage(MultipartFile file) {
        //无文件
        if (file.isEmpty() || file.getSize() <= 0)
            return bad("image file is not found");
        //文件过大
        if (file.getSize() > config.getMaxImageSize())
            return bad("image file is too large,max size is 500K");
        String name = file.getOriginalFilename();
        //文件无名称
        if (SU.isEmpty(name))
            return bad("cannot get file name");
        String name_fix = name.substring(name.lastIndexOf("."));
        String new_name = SU.getRandomString(32)+name_fix;
        File final_file = new File(config.getLocationPath()+config.getUserLinkPath()+new_name);
        try {
            file.transferTo(final_file);
            String linkPath = config.getLinkPath()+config.getUserLinkPath()+new_name;
            //保存至数据库
            String login = (String) getSubject().getPrincipal();
            userMapper.updateUserImageByLogin(linkPath,login);
            return ok(linkPath);
        } catch (IOException e) {
            return bad("cannot save image file because of "+e.getMessage());
        }
    }
}