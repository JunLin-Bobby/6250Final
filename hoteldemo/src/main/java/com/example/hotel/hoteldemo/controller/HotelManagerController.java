package com.example.hotel.hoteldemo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hotel.hoteldemo.dao.ManagerDAO;
import com.example.hotel.hoteldemo.dao.ReservationDAO;
import com.example.hotel.hoteldemo.dto.LoginForm;
import com.example.hotel.hoteldemo.pojo.HotelManager;
import com.example.hotel.hoteldemo.pojo.Reservation;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/system_admin")
public class HotelManagerController {
    @Autowired
    ManagerDAO managerDAO;
    @Autowired
    ReservationDAO reservationDAO;

    @GetMapping("/login")
    public String showLoginPage(HttpSession session,Model model) {
         if (session.getAttribute("loggedInManager") != null) {
            return "redirect:/system_admin/dashboard";
        }
        model.addAttribute("loginForm", new LoginForm());
        return "managerLogin";
    }
    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.invalidate();
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
        return "redirect:/system_admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                            Model model) {
    List<Reservation> reservations;
    if (startDate != null && endDate != null) {
        reservations = reservationDAO.findByDateRange(startDate, endDate);
    } else {
        reservations = reservationDAO.findAll();
    }
    model.addAttribute("reservations", reservations);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    return "ManagerDashboard";
}


}
