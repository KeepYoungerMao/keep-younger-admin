package com.mao.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro的过滤器配置
 * @author mao in 22:44 2019/9/13
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 配置自定义拦截管理器，并配置散列算法
     * @return ShiroRealm
     */
    @Bean
    public ShiroRealm shiroRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     * shiro的权限认证管理
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroRealm());
        return manager;
    }

    /**
     * 散列算法配置
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //散列算法
        matcher.setHashAlgorithmName("md5");
        //散列次数
        matcher.setHashIterations(1);
        return matcher;
    }

    /**
     * shiro的过滤工厂：过滤条件和跳转条件
     * @param manager SecurityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager manager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);

        Map<String, String> map = new HashMap<>();
        //退出登录
        map.put("/logout","logout");

        //排除静态资源/static/**，anon为排除选项，可配置多个
        map.put("/static/**","anon");
        //map.put("/index","anon");
        //map.put("/","anon");
        map.put("*.ico","anon");
        map.put("/login","anon");
        bean.setFilterChainDefinitionMap(map);
        //对所有用户认证
        map.put("/**","authc");
        //登录
        bean.setLoginUrl("/login");
        //首页
        bean.setSuccessUrl("/index");
        //未授权界面
        bean.setUnauthorizedUrl("/unAuth");
        return bean;
    }

    /**
     * shiro注解认证的启用
     * @param manager SecurityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager manager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    /**
     * 启动SpringAOP对shiro注解的扫描
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}