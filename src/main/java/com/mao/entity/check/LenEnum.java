package com.mao.entity.check;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 长度类型
 * 用于String类型检测长度计算方式
 * @author mao by 14:35 2019/11/6
 */
@Getter
@AllArgsConstructor
public enum LenEnum {

    LEN(null),          //计算长度
    SIZE("byte");       //计算大小

    private String unit;

}