package com.mao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 配置资源映射、拦截器等
 * @author mao by 15:23 2019/9/27
 */
@Configuration
public class SourceConfiguration implements WebMvcConfigurer {

    @Resource
    private Config config;

    /**
     * 配置资源映射访问
     * @param registry 资源管理注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(config.getLinkPath()+"**")
                .addResourceLocations("file:"+config.getLocationPath());
    }
}