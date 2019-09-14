package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 角色表：sys_role
 * @author mao in 22:53 2019/9/13
 */
@Getter
@Setter
public class Role {

    private int id;                             //id
    private String name;                        //角色名称
    private String intro;                       //角色介绍

    private List<Permission> permissions;       //权限列表

}