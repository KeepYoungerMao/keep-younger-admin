package com.mao.service;

import com.mao.entity.ErrMsg;
import com.mao.entity.ResponseData;
import com.mao.entity.ResponseEnum;
import com.mao.entity.sys.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * 基本service
 * @author mao by 17:04 2019/9/12
 */
@Service
public class BaseService {

    protected Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public <T> ResponseData<T> ok(T data){
        return data(ResponseEnum.SUCCESS,data);
    }

    public  ResponseData bad(String msg){
        return data(ResponseEnum.BAD_REQUEST,new ErrMsg(msg));
    }

    public ResponseData error(String msg){
        return data(ResponseEnum.ERROR,new ErrMsg(msg));
    }

    public ResponseData permission(String msg){
        return data(ResponseEnum.PERMISSION,new ErrMsg(msg));
    }

    private <T> ResponseData<T> data(ResponseEnum type, T data){
        return new ResponseData<>(type.getType(),data);
    }

}