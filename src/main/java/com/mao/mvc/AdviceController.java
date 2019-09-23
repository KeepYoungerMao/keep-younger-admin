package com.mao.mvc;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常捕捉
 * @author mao by 17:45 2019/9/17
 */
@ControllerAdvice
public class AdviceController {

    /**
     * 无权限拦截
     */
    @ExceptionHandler(UnauthorizedException.class)
    public void unAuthorizedException(HttpServletRequest request,
                                      HttpServletResponse response) throws IOException{
        redirect(request, response);
    }

    /**
     * 权限异常拦截
     */
    @ExceptionHandler(AuthorizationException.class)
    public void authorizationExceptionHandler(HttpServletRequest request,
                                              HttpServletResponse response) throws IOException{
        redirect(request, response);
    }

    /**
     * 重定向方法
     * ajax请求发送指定参数，让前端判断跳转
     * 页面请求直接重定向
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException e
     */
    private void redirect(HttpServletRequest request,
                          HttpServletResponse response) throws IOException{
        //获取源url
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()+request.getContextPath();
        //判断是否是ajax
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTENT-PATH", basePath+"/unAuth");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.sendRedirect("/unAuth");
        }
    }

}