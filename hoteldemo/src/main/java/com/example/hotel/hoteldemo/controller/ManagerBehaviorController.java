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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hotel.hoteldemo.dao.ReservationDAO;
import com.example.hotel.hoteldemo.dao.RoomDAO;
import com.example.hotel.hoteldemo.dao.UserDAO;
import com.example.hotel.hoteldemo.pojo.Reservation;
import com.example.hotel.hoteldemo.pojo.ReservationStatus;
import com.example.hotel.hoteldemo.pojo.Room;
import com.example.hotel.hoteldemo.pojo.User;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/system_admin")
public class ManagerBehaviorController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    RoomDAO roomDAO;
    @Autowired
    ReservationDAO reservationDAO;
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<User> users = userDAO.findAll(); 
        model.addAttribute("users", users);
        return "view-users"; 
    }

    @GetMapping("/rooms")
    public String viewAllRooms(Model model) {
        List<Room> rooms = roomDAO.findAll();
        model.addAttribute("rooms", rooms);
        return "view-rooms";
    }
    @GetMapping("/rooms/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-room-form";
    }

    @PostMapping("/rooms/add")
    public String processAddRoom(@Valid @ModelAttribute("room") Room room,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-room-form";
        }
        roomDAO.save(room);
        return "redirect:/system_admin/rooms";
    }

    @GetMapping("/rooms/delete")
    public String showDeleteRoomPage(Model model) {
        model.addAttribute("rooms", roomDAO.findAll());
        return "delete-room-form";
    }

    @PostMapping("/rooms/delete")
    public String processDeleteRoom(@RequestParam("roomID") int roomID, RedirectAttributes redirectAttributes) {
        Room room = roomDAO.findByRoomID(roomID);
        boolean hasCreatedReservation = room.getReservations().stream()
            .anyMatch(r -> r.getStatus().name().equals("CREATED"));

        if (hasCreatedReservation) {
            redirectAttributes.addFlashAttribute("errorMessage", "Room has active reservations and cannot be deleted.");
            return "redirect:/system_admin/rooms/delete";
        }

        roomDAO.delete(room);
        return "redirect:/system_admin/rooms";
    }

    @GetMapping("/reservation-detail")
    public String showReservationDetail(@RequestParam int id, Model model) {
        Reservation reservation = reservationDAO.findById(id);
        if (reservation == null) {
            return "redirect:/system_admin/dashboard";
        }
        model.addAttribute("reservation", reservation);
        model.addAttribute("isCancelable", reservation.getCheckOutDate().isAfter(LocalDate.now()));
        return "manager-reservation-detail";
    }

    @PostMapping("/reservation/cancel")
    public String cancelReservation(@RequestParam int id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationDAO.findById(id);
        if (reservation != null && reservation.getCheckOutDate().isAfter(LocalDate.now())) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationDAO.update(reservation);
            redirectAttributes.addFlashAttribute("message", "Reservation successfully cancelled.");
        }
        return "redirect:/system_admin/reservation-detail?id=" + id;
    }

    @PostMapping("/reservation/update-dates")
    public String updateReservationDates(@RequestParam int id,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newCheckin,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newCheckout,
                                        RedirectAttributes redirectAttributes) {

        Reservation reservation = reservationDAO.findById(id);
        if (reservation == null) {
            redirectAttributes.addFlashAttribute("error", "Reservation not found.");
            return "redirect:/system_admin/dashboard";
        }

    
        if (!newCheckout.isAfter(newCheckin)) {
            redirectAttributes.addFlashAttribute("error", "Check-out date must be after check-in date.");
            return "redirect:/system_admin/reservation-detail?id=" + id;
        }

        reservation.setCheckInDate(newCheckin);
        reservation.setCheckOutDate(newCheckout);
        reservationDAO.update(reservation);

        redirectAttributes.addFlashAttribute("message", "Reservation dates updated successfully.");
        return "redirect:/system_admin/reservation-detail?id=" + id;
    }


}
