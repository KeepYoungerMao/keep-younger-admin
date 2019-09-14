package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户表
 * @author mao by 17:10 2019/9/12
 */
@Getter
@Setter
@ToString
public class User {

    private int id;                 //id
    private String user_name;       //名称
    private String user_login;      //登录名
    private String user_pass;       //密码
    private boolean locked;         //是否锁定
    private int user_role;          //角色id

}