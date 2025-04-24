package com.example.hotel.hoteldemo.controller;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.hotel.hoteldemo.dao.CreditcardDAO;
import com.example.hotel.hoteldemo.dao.ReservationDAO;
import com.example.hotel.hoteldemo.dao.RoomDAO;
import com.example.hotel.hoteldemo.pojo.Creditcard;
import com.example.hotel.hoteldemo.pojo.Reservation;
import com.example.hotel.hoteldemo.pojo.ReservationStatus;
import com.example.hotel.hoteldemo.pojo.Room;
import com.example.hotel.hoteldemo.pojo.User;
import com.example.hotel.hoteldemo.validator.CreditCardValidator;
import com.example.hotel.hoteldemo.validator.SearchValidator;


import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;


//

@Controller
public class BookingController {
    @Autowired
    RoomDAO roomDAO;
    @Autowired
    ReservationDAO reservationDAO;
    @Autowired
    private SearchValidator searchValidator;
    @Autowired
    private CreditCardValidator CreditCardValidator;
    @Autowired
    CreditcardDAO creditcardDAO;

    //Show the make-reservation.html page
    //Allow users to filter rooms based on checkin checkout Date,capacity(roomType)
    @GetMapping("/search-reservation")
    public String showPage(Model model,HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser"); 
        model.addAttribute("user", user);
        return "make-reservation";
    }

    //Handle the filter request from users
    @PostMapping("/search-reservation")
    public String searchRoom(@RequestParam double minPrice,
                         @RequestParam double maxPrice,
                         @RequestParam int roomType,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                         Model model,
                         HttpSession session) {
        //Validate the date input error                    
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
    //Show the process-reservation.html
    //Allow users input their contactInfo, select card used for payment
    @PostMapping("/process-reservation")
    public String processReservation(@RequestParam int roomID,
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
        List<Creditcard> cards = creditcardDAO.findByUser(user);
        model.addAttribute("cards", cards);                        
        return "process-reservation";
    }
    //Process to review-reservation 
    //Check card information if users crate new card at process-reservation.html page
    @PostMapping("/review-reservation")
    public String reviewReservation(@RequestParam int roomID,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                                    @RequestParam String contactFirstName,
                                    @RequestParam String contactLastName,
                                    @RequestParam String contactPhoneNumber,
                                    @RequestParam double totalAmount,
                                    @RequestParam(required = false) Integer creditCardID,
                                    @RequestParam(required = false) String newCardNumber,
                                    @RequestParam(required = false) String newExpireDate,
                                    @RequestParam(required = false) String newCVV,
                                    Model model, HttpSession session) {
        
        Room room = roomDAO.findByRoomID(roomID);
        User user = (User) session.getAttribute("loggedInUser");
        Creditcard selectedCard = null;
        if (creditCardID != null) {
            selectedCard = creditcardDAO.findById(creditCardID);
        }
        // use new card
        else if (newCardNumber != null && newExpireDate != null && newCVV != null) {
            if (!CreditCardValidator.isValidCardNumber(newCardNumber)) {
                return returnToProcessWithError("Card number must be 16 digits.", model, room, user, checkinDate, checkoutDate, totalAmount);
            }
            if (!CreditCardValidator.isValidCVV(newCVV)) {
                return returnToProcessWithError("CVV must be 3 digits.", model, room, user, checkinDate, checkoutDate, totalAmount);
            }
            if (!CreditCardValidator.isValidExpireDate(newExpireDate)) {
                return returnToProcessWithError("Expire date must be in MM/YY format.", model, room, user, checkinDate, checkoutDate, totalAmount);
            }
            Creditcard newCard = new Creditcard();
            newCard.setCardNumber(newCardNumber);
            newCard.setExpireDate(newExpireDate);
            newCard.setCvv(newCVV);
            newCard.setUser(user);
            creditcardDAO.save(newCard);
            selectedCard = newCard;
        }

        // validate if selected card or not 
        if (selectedCard == null) {
            model.addAttribute("error", "Please select or enter a valid credit card.");
            return "process-reservation";
        }

        model.addAttribute("user", user);
        model.addAttribute("room", room);
        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("contactFirstName", contactFirstName);
        model.addAttribute("contactLastName", contactLastName);
        model.addAttribute("contactPhoneNumber", contactPhoneNumber);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("creditCard", selectedCard);

        return "review-reservation";
    }
    //Finalize and then create Reservation
    @PostMapping("/finalize-reservation")
    public String finalizeReservation(@RequestParam int roomID,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
                                    @RequestParam String contactFirstName,
                                    @RequestParam String contactLastName,
                                    @RequestParam String contactPhoneNumber,
                                    @RequestParam double totalAmount,
                                    @RequestParam int creditCardID,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session,
                                    Model model) {

        
        User user = (User) session.getAttribute("loggedInUser");
        Room room = roomDAO.findByRoomID(roomID);
        Creditcard creditCard = creditcardDAO.findById(creditCardID);
        String cardNumber = creditCard.getCardNumber();
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkinDate);
        reservation.setCheckOutDate(checkoutDate);
        reservation.setContactFirstName(contactFirstName);
        reservation.setContactLastName(contactLastName);
        reservation.setContactPhoneNumber(contactPhoneNumber);
        reservation.setTotalAmount(totalAmount);
        reservation.setCardLast4(cardNumber.substring(cardNumber.length() - 4));
        reservation.setCardExpireDate(creditCard.getExpireDate());
        reservation.setStatus(ReservationStatus.CREATED);

        reservationDAO.saveReservation(reservation);

        model.addAttribute("user", user);
        model.addAttribute("reservation", reservation);
        redirectAttributes.addFlashAttribute("message", "Reservation successful!");
        redirectAttributes.addFlashAttribute("reservationID", reservation.getReservationID());
        
        return "redirect:/reservation-success";

        
    }

    @GetMapping("/reservation-success")
    public String showSuccessPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        return "reservation-success";
    }

    private String returnToProcessWithError(String error, Model model, Room room, User user, LocalDate checkin, LocalDate checkout, double totalAmount) {
        model.addAttribute("error", error);
        model.addAttribute("room", room);
        model.addAttribute("user", user);
        model.addAttribute("checkinDate", checkin);
        model.addAttribute("checkoutDate", checkout);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("cards", creditcardDAO.findByUser(user));
        return "process-reservation";
    }
    


}
