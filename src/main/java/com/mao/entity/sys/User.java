package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表
 * @author mao by 17:10 2019/9/12
 */
@Getter
@Setter
public class User {

    private int id;
    private String user_name;
    private String user_login;
    private String user_pass;
    private int user_role;

}