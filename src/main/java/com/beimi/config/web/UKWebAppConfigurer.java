package com.beimi.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.beimi.web.interceptor.CrossInterceptorHandler;
import com.beimi.web.interceptor.UserInterceptorHandler;

@Configuration
public class UKWebAppConfigurer 
        extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
    	registry.addInterceptor(new UserInterceptorHandler()).addPathPatterns("/**").excludePathPatterns("/login.html").excludePathPatterns("/tokens").excludePathPatterns("/api/**");
    	registry.addInterceptor(new CrossInterceptorHandler()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}