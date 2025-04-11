package com.example.hotel.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hotel.dto.LoginForm;
import com.example.hotel.hoteldemo.dao.UserDAO;
import com.example.hotel.hoteldemo.pojo.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    UserDAO userDAO;

    @GetMapping("/login")
    public String showPage(Model model) {
        model.addAttribute("loginForm",new LoginForm());
        return "loginPage";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                BindingResult result,
                                HttpSession session,Model model) {
        if(result.hasErrors()){
            return "loginPage";
        }

        User user = userDAO.findByEmail(loginForm);
        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            model.addAttribute("loginError", "Error email or password");
            return "loginPage"; // ❌ 自訂錯誤：帳密不符
        }

        session.setAttribute("loggedInUser", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser"); // 假設你把登入使用者放在 session 裡
        model.addAttribute("user", user);
        return "dashboard";
    }

}
