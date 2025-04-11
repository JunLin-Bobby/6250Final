package com.example.hotel.hoteldemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.hotel.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 攔所有頁面
                .excludePathPatterns("/login","/logout","/register","/dashboard"); // 排除登入、註冊頁面
    }
}

