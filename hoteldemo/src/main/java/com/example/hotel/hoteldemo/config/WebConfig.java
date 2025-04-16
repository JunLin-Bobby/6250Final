package com.example.hotel.hoteldemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.hotel.hoteldemo.interceptor.AdminLoginInterceptor;
import com.example.hotel.hoteldemo.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

   
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         //Interceptor for normal users login
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") 
                .excludePathPatterns("/login","/logout","/register" ,"/dashboard","/system_admin/**");
        
                //Interceptor for admins login
        registry.addInterceptor(new AdminLoginInterceptor())
                    .addPathPatterns("/system_admin/**")
                    .excludePathPatterns("/system_admin/login");            
    }
    
    
}

