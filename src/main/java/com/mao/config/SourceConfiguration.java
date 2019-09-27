package com.mao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置资源映射、拦截器等
 * @author mao by 15:23 2019/9/27
 */
@Configuration
public class SourceConfiguration implements WebMvcConfigurer {

    /**
     * 配置资源映射访问
     * @param registry 资源管理注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:C:\\Users\\zongx\\Desktop\\self\\");
    }
}