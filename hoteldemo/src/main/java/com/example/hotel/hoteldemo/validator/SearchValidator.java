package com.example.hotel.hoteldemo.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SearchValidator {

    public void validate(LocalDate checkinDate, LocalDate checkoutDate, Model model) {
        LocalDate today = LocalDate.now();

        if (checkinDate.isBefore(today)) {
            model.addAttribute("dateError", "Check-in date cannot be in the past.");
            return;
        }


        if (!checkoutDate.isAfter(checkinDate)) {
            model.addAttribute("dateError", "Checkout date must be after check-in date.");
        }
    }
}

