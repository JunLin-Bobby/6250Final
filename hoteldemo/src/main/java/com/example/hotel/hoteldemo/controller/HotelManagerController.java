package com.example.hotel.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hotel.hoteldemo.dao.ManagerDAO;
import com.example.hotel.hoteldemo.dto.LoginForm;
import com.example.hotel.hoteldemo.pojo.HotelManager;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/system_admin")
public class HotelManagerController {
    @Autowired
    ManagerDAO managerDAO;

    @GetMapping("/login")
    public String getMethodName(HttpSession session,Model model) {
         if (session.getAttribute("loggedInManager") != null) {
            return "redirect:/system_admin/dashboard";
        }
        model.addAttribute("loginForm", new LoginForm());
        return "managerLogin";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") LoginForm loginForm,
                               BindingResult result,
                               HttpSession session,
                               Model model) {
        if (result.hasErrors()) {
            return "managerLogin";
        }

        HotelManager manager = managerDAO.findByUsername(loginForm.getEmail());
        if (manager == null || !manager.getPassword().equals(loginForm.getPassword())) {
            model.addAttribute("loginError", "Invalid username or password");
            return "managerLogin";
        }

        session.setAttribute("loggedInManager", manager);
        return "redirect:/system_admin/manager_dashboard";
    }

    @GetMapping("/manager_dashboard")
    public String showManagerDashboard(HttpSession session) {
        
        return "managerDashboard";
    }

}
