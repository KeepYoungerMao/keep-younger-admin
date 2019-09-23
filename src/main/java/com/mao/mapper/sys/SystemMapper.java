package com.mao.mapper.sys;

import com.mao.entity.sys.Permission;
import com.mao.entity.sys.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统类型数据操作
 * 角色权限类数据
 * role、permission等表的数据操作放置一处
 * @author mao in 23:02 2019/9/13
 */
@Repository
@Mapper
public interface SystemMapper {

    //根据角色查询权限
    Role getRoleByUserLogin(@Param("name") String name);

    //根据角色id查询权限列表
    List<Permission> getPermissionByRoleId(@Param("id") int id);

    //获取所有角色列表
    List<Role> getRoles();

    //保存一个角色数据
    void saveRole(Role role);

    //获取所有权限列表
    List<Permission> getPermissions();

}