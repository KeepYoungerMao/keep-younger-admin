package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 角色保存时使用封装类
 * @author mao by 11:23 2019/10/12
 */
@Getter
@Setter
public class RoleDo {

    private int id;                         //id
    private String name;                    //name
    private String intro;                   //介绍

    private List<Integer> permissions;      //权限ids

}