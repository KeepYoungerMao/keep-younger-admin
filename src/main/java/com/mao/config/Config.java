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

    //图片上传最大bit值
    @Value("${max-upload-image-size}")
    private long maxImageSize;

    //图片本地储存路径
    @Value("${user-image-local-path}")
    private String locationPath;

    //图片访问路径
    @Value("${user-image-link-pre}")
    private String linkPath;

    @Value("${user-image-link-package}")
    private String userLinkPath;

}