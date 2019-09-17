package com.mao.mvc;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 异常捕捉
 * @author mao by 17:45 2019/9/17
 */
@ControllerAdvice
public class AdviceController {

    /**
     * 无权限拦截
     * @return 无权限页面
     */
    @ExceptionHandler(UnauthorizedException.class)
    public String unAuthorizedException(){
        return "unAuth";
    }

    /**
     * 权限异常拦截
     * @return 无权限页面
     */
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationExceptionHandler(){
        return "unAuth";
    }

}