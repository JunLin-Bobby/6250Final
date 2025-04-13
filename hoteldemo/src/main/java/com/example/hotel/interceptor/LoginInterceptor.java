package com.example.hotel.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle (HttpServletRequest request,
                               HttpServletResponse response, 
                               Object handler) throws Exception {
        HttpSession session = request.getSession(false);//if session doesn't exist, return null
        if(session!=null&&session.getAttribute("loggedInUser")!=null){
            return true;
        }
    response.sendRedirect("/login");    
    System.out.println("intercepted");
    return false;
}

}
