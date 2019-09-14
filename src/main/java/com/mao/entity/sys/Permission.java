package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

/**
 * 权限表：sys_permission
 * @author mao in 22:54 2019/9/13
 */
@Getter
@Setter
public class Permission {
    private int id;                 //id
    private String url;             //权限名称
    private String name;            //权限介绍
}