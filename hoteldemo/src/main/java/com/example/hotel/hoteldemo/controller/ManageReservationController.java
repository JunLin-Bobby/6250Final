package com.example.hotel.hoteldemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hotel.hoteldemo.dao.ReservationDAO;
import com.example.hotel.hoteldemo.pojo.Reservation;
import com.example.hotel.hoteldemo.pojo.User;

import jakarta.servlet.http.HttpSession;


@Controller
public class ManageReservationController {
    @Autowired
    ReservationDAO reservationDAO;


    @GetMapping("/my-reservation")
    public String showPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        List<Reservation> reservations = reservationDAO.findByUser(user);
        model.addAttribute("reservations", reservations);
        model.addAttribute("user", user);
        return "my-reservation";
    }

    @GetMapping("/reservation-detail")
    public String showReservationDetail(@RequestParam int id, Model model, HttpSession session) {
    User user = (User) session.getAttribute("loggedInUser");
    Reservation reservation = reservationDAO.findById(id);

    
    if (reservation == null || reservation.getUser().getUserID() != user.getUserID()) {
        return "redirect:/my-reservation";
    }

    model.addAttribute("reservation", reservation);
    model.addAttribute("user", user);
    return "reservation-detail";
}

    
}
