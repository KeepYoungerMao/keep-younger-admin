package com.mao.service.check.impl;

import com.mao.entity.check.Check;
import com.mao.entity.check.LenEnum;
import com.mao.entity.check.MInt;
import com.mao.entity.check.MString;
import com.mao.service.check.CheckService;
import com.mao.util.SU;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * 用于检测数据业务
 * @author mao by 14:56 2019/11/6
 */
@Service
public class CheckServiceImpl implements CheckService {

    /**
     * 检测对象字段是否合法
     * @param object 对象
     * @return 不合法提示 / null
     */
    @Override
    public String check(Object object) {
        Check check = object.getClass().getAnnotation(Check.class);
        //需要检测
        if (null != check){
            //获取属性
            Field[] fields = object.getClass().getDeclaredFields();
            if (fields.length > 0){
                for (Field field : fields) {
                    //设置可访问
                    field.setAccessible(true);
                    //int 类型
                    MInt mInt = field.getAnnotation(MInt.class);
                    if (null != mInt){
                        String s1 = checkInt(mInt,field,object);
                        if (null != s1) return s1;
                    }
                    //String 类型
                    MString mString = field.getAnnotation(MString.class);
                    if (null != mString){
                        String s2 = checkString(mString,field,object);
                        if (null != s2) return s2;
                    }
                }
            }
        }
        return null;
    }

    /**
     * int 字段的检测
     * @param mInt 注解
     * @param field 字段
     * @param object 对象
     * @return 错误信息 / null
     */
    private String checkInt(MInt mInt, Field field, Object object) {
        Object value;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
        if (value instanceof Integer){
            int i = (Integer) value;
            if (mInt.positive() && i <= 0)
                return "字段["+field.getName()+"]必须为正数";
            if (i < mInt.min())
                return "字段["+field.getName()+"]小于允许范围";
            if (i > mInt.max())
                return "字段["+field.getName()+"]超过允许范围";
        }
        return null;
    }

    /**
     * String 字段的检测
     * @param mString 注解
     * @param field 字段
     * @param object 对象
     * @return 错误信息 / null
     */
    private String checkString(MString mString, Field field, Object object){
        Object value;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            value = null;
        }
        if (value instanceof String){
            String s = (String) value;
            if (SU.isEmpty(s) && !mString.none())
                return "字段["+field.getName()+"]不允许为空";
            LenEnum type = mString.type();
            int min = mString.min(), max = mString.max();
            int s_len = (type == LenEnum.LEN) ? s.length() : s.getBytes().length;
            String fix = (type == LenEnum.LEN) ? "长度" : "bytes";
            if (min > 0 && min > s_len)
                return "字段["+field.getName()+"]不能小于"+min+fix;
            if (s_len > max)
                return "字段["+field.getName()+"]不能大于"+min+fix;
        }
        return null;
    }

}