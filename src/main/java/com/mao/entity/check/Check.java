package com.mao.entity.check;

import java.lang.annotation.*;

/**
 * 检验：作用于类上。表示这个类需要检测
 * @author mao by 14:14 2019/11/6
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Check {
}