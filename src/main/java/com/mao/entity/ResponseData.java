package com.mao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 信息统一返回体
 * @author mao in 22:32 2019/9/13
 */
@Getter
@Setter
@AllArgsConstructor
public class ResponseData<T> {

    private int code;
    private T data;

}