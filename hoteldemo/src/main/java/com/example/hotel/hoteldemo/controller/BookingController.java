package com.example.hotel.hoteldemo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.hotel.hoteldemo.dao.RoomDAO;
import com.example.hotel.hoteldemo.pojo.Room;
import com.example.hotel.hoteldemo.pojo.User;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class BookingController {
    @Autowired
    RoomDAO roomDAO;

    @GetMapping("/booking")
    public String showPage(Model model,HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser"); 
        model.addAttribute("user", user);
        return "make-reservation";
    }
    @PostMapping("/search-reservation")
    public String searchRoom(@RequestParam double minPrice,
                         @RequestParam double maxPrice,
                         @RequestParam int roomType,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkinDate,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkoutDate,
                         Model model,
                         HttpSession session) {
        List<Room> rooms = roomDAO.searchRooms(minPrice, maxPrice, roomType, checkinDate, checkoutDate);
        
        // 將目前登入使用者加進 model（供 navbar 顯示）
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        
        model.addAttribute("rooms", rooms);
        return "make-reservation"; // 回到原頁面並顯示結果
}
    

}
