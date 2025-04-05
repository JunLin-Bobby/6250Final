package com.example.hotel.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.hotel.hoteldemo.dao.UserDAO;
import com.example.hotel.hoteldemo.pojo.User;

@Controller
public class RegisterController {
    @Autowired
    UserDAO userDAO;
    @GetMapping("/register")
    public String showPage(Model model,User user){
        model.addAttribute("user",user);
        return "registerPage";
    }
    
    @PostMapping("/register")
    public String showPage(@ModelAttribute("user") User user){
        userDAO.save(user);
        return "success";
    }
}
