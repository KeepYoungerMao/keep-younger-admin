package com.mao.mapper.sys;

import com.mao.entity.sys.Permission;
import com.mao.entity.sys.Role;
import com.mao.entity.sys.RoleDo;
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
    void saveRole(RoleDo role);

    //更新一个角色数据
    void updateRole(RoleDo role);

    //根据名称查询是否存在（有id时排除id）
    int getCountByRoleName(@Param("name") String name, @Param("id") int id);

    //根据角色id获取该角色权限id列表
    List<Integer> getPermissionIdsByRoleId(@Param("role") int role);

    //保存角色的权限列表
    void savePermissions(@Param("ids") List<Integer> ids, @Param("role") int role);

    //根据角色id删除权限（删除关联表数据）
    void delPermissionByRoleId(@Param("role") int role);

}