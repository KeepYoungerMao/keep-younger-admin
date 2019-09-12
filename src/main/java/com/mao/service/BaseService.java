package com.mao.service;

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

    protected User getCurrentUser(){
        return (User) getSubject().getPrincipal();
    }

    protected Session getSession(){
        return getSubject().getSession();
    }

}