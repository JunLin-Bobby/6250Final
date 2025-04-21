package com.example.hotel.hoteldemo.controller;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.hotel.hoteldemo.dao.ReservationDAO;
import com.example.hotel.hoteldemo.dao.RoomDAO;
import com.example.hotel.hoteldemo.pojo.Reservation;
import com.example.hotel.hoteldemo.pojo.ReservationStatus;
import com.example.hotel.hoteldemo.pojo.Room;
import com.example.hotel.hoteldemo.pojo.User;
import com.example.hotel.hoteldemo.validator.SearchValidator;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class BookingController {
    @Autowired
    RoomDAO roomDAO;
    @Autowired
    ReservationDAO reservationDAO;
    @Autowired
    private SearchValidator searchValidator;

    @GetMapping("/search-reservation")
    public String showPage(Model model,HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser"); 
        model.addAttribute("user", user);
        return "make-reservation";
    }

    @PostMapping("/search-reservation")
    public String searchRoom(@RequestParam double minPrice,
                         @RequestParam double maxPrice,
                         @RequestParam int roomType,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                         Model model,
                         HttpSession session) {

        searchValidator.validate(checkinDate, checkoutDate, model);                    
        if (!model.containsAttribute("dateError")) {
            List<Room> rooms = roomDAO.searchRooms(minPrice, maxPrice, roomType, checkinDate, checkoutDate);
            model.addAttribute("rooms", rooms);
        }   

        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("roomType", roomType);
       
        
        return "make-reservation";
    }

    @PostMapping("/confirm-reservation")
    public String showConfirmPage(@RequestParam int roomID,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                               Model model,HttpSession session) {
        Room room = roomDAO.findByRoomID(roomID); 
        long nights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
        double totalAmount = room.getPricePerNight() * nights;
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        model.addAttribute("room", room);
        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("totalAmount", totalAmount);

        return "confirm-reservation";
    }

    @PostMapping("/review-reservation")
    public String reviewReservation(@RequestParam int roomID,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                                    @RequestParam String contactFirstName,
                                    @RequestParam String contactLastName,
                                    @RequestParam String contactPhoneNumber,
                                    @RequestParam double totalAmount,
                                    Model model, HttpSession session) {
        Room room = roomDAO.findByRoomID(roomID);
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        model.addAttribute("room", room);
        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("contactFirstName", contactFirstName);
        model.addAttribute("contactLastName", contactLastName);
        model.addAttribute("contactPhoneNumber", contactPhoneNumber);
        model.addAttribute("totalAmount", totalAmount);

        return "review-reservation";
    }
    @PostMapping("/finalize-reservation")
    public String finalizeReservation(@RequestParam int roomID,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                                    @RequestParam String contactFirstName,
                                    @RequestParam String contactLastName,
                                    @RequestParam String contactPhoneNumber,
                                    @RequestParam double totalAmount,
                                    HttpSession session,
                                    Model model) {

        
        User user = (User) session.getAttribute("loggedInUser");
        Room room = roomDAO.findByRoomID(roomID);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkinDate);
        reservation.setCheckOutDate(checkoutDate);
        reservation.setContactFirstName(contactFirstName);
        reservation.setContactLastName(contactLastName);
        reservation.setContactPhoneNumber(contactPhoneNumber);
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(ReservationStatus.CREATED);


        
        reservationDAO.saveReservation(reservation);

        model.addAttribute("user", user);
        model.addAttribute("reservation", reservation);

        return "dashboard"; 
    }



}
