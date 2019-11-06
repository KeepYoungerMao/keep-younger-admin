package com.mao.entity.check;

import java.lang.annotation.*;

/**
 * String 类型字段的检测。作用于字段上
 *   none()     是否允许空。默认允许
 *   type()     长度类型。具体见该类。默认计算长度
 *   min()      最小长度，基于长度类型type()的最小长度。默认为0，无限制
 *   max()      最大长度，基于长度类型type()的最大长度。默认为0，无限制
 * @author mao by 14:37 2019/11/6
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MString {
    boolean none() default true;
    LenEnum type() default LenEnum.LEN;
    int min() default 0;
    int max() default 0;
}