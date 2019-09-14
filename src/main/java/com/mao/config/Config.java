package com.mao.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置类
 * @author mao in 11:33 2019/9/14
 */
@Component
@PropertySource("classpath:config.properties")
@Getter
@Setter
public class Config {

    @Value("${max-login-time}")
    private int maxLoginTime;

}