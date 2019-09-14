package com.mao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回类型
 * @author mao in 22:33 2019/9/13
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(200),           //成功
    NOT_FOUND(404),         //404
    BAD_REQUEST(405),       //请求错误
    PERMISSION(406),        //权限错误
    ERROR(500);             //系统错误

    private int type;

}