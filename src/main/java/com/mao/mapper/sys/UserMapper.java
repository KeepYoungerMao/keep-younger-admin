package com.mao.mapper.sys;

import com.mao.entity.sys.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统用户数据操作
 * @author mao by 15:32 2019/9/18
 */
@Repository
@Mapper
public interface UserMapper {

    //根据登录名查询用户信息
    User getUserByLogin(@Param("login") String login);

    //根据条件查询用户列表
    List<User> getUser(@Param("role") int role, @Param("name") String name,
                       @Param("login") String login, @Param("page") int page);

    //根据id查询用户详细信息
    User getUserById(@Param("id") int id);

    //根据条件查询用户列表总个数
    int getUserCount(@Param("role") int role, @Param("name") String name,
                     @Param("login") String login);

    //新建一个用户
    void saveUser(User user);

    //更新一个用户数据
    void updateUser(User user);

    //根据登录名查询用户信息
    User getUserIntroByLogin(@Param("login") String login);

    //根据id更新用户便签
    int updateUserNoteById(@Param("id") int id, @Param("note") String note);

    //用户更新自己的个人资料
    int updateUserBySelf(User user);

    int updateUserImageByLogin(@Param("image") String image, @Param("login") String login);
}