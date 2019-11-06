package com.mao.entity.check;

import java.lang.annotation.*;

/**
 * int 类型字段的检测。作用于字段上
 *   positive()     是否需要为正数。默认需要。
 *   min()          最小范围。默认允许最小
 *   max()          最大范围。默认允许最大
 * @author mao by 14:20 2019/11/6
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MInt {
    boolean positive() default true;
    int min() default -Integer.MAX_VALUE;
    int max() default Integer.MAX_VALUE;
}