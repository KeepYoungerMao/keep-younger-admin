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
    private String role_name;       //角色名称

    private String company;         //公司
    private String dept;            //部门
    private String note;            //便签
    private String image;           //头像地址
    private String identity_code;   //身份证号
    private String address;         //现住地址
    private String qq;              //qq号
    private String wx;              //微信号
    private String phone;           //手机号
    private String email;           //邮箱

}