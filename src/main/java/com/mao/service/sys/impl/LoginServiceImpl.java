package com.mao.service.sys.impl;

import com.mao.service.BaseService;
import com.mao.service.sys.LoginService;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * @author mao by 17:14 2019/9/12
 */
@Service
public class LoginServiceImpl extends BaseService implements LoginService {

    @Override
    public String login(String u, String p, boolean r) {
        UsernamePasswordToken token = new UsernamePasswordToken(u,p,r);
        Subject subject = getSubject();
        if (null != subject)
            subject.logout();
        try {
            subject.login(token);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        return "OK";
    }

}