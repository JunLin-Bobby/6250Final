package com.example.hotel.hoteldemo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.hotel.hoteldemo.dao.CreditcardDAO;

import com.example.hotel.hoteldemo.pojo.Creditcard;

import com.example.hotel.hoteldemo.pojo.User;
import com.example.hotel.hoteldemo.validator.CreditCardValidator;


import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class paymentController {    
    @Autowired
    private CreditcardDAO creditcardDAO;
    @Autowired
    private CreditCardValidator creditCardValidator;

    @GetMapping("/payment")
    public String showPaymentPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Creditcard> cards = creditcardDAO.findByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("cards", cards);
        model.addAttribute("newCard", new Creditcard());
        return "payment";
    }

    @PostMapping("/payment/add")
    public String addCard(@ModelAttribute("newCard") Creditcard card, HttpSession session, RedirectAttributes redirectAttributes,Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
     

        if (!creditCardValidator.isValidCardNumber(card.getCardNumber())) {
            model.addAttribute("error", "Card number must be 16 digits.");
            model.addAttribute("cards", creditcardDAO.findByUser(user));
            return "payment";  
        }

        if (!creditCardValidator.isValidCVV(card.getCvv())) {
            model.addAttribute("error", "CVV must be 3 digits.");
            model.addAttribute("cards", creditcardDAO.findByUser(user));
            return "payment";
        }

        if (!creditCardValidator.isValidExpireDate(card.getExpireDate())) {
            model.addAttribute("error", "Expire date must be in MM/YY format.");
            model.addAttribute("cards", creditcardDAO.findByUser(user));
            return "payment";
        }
        card.setUser(user);
        creditcardDAO.save(card);
        redirectAttributes.addFlashAttribute("message", "New card added successfully.");
        return "redirect:/payment";
    }
    
    @PostMapping("/payment/delete")
    public String deleteCard(@RequestParam("cardId") int cardId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Creditcard card = creditcardDAO.findById(cardId);
        if (card != null && card.getUser().getUserID() == user.getUserID()) {
            creditcardDAO.delete(card);
            redirectAttributes.addFlashAttribute("message", "Card deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "You do not have permission to delete this card.");
        }

        return "redirect:/payment";
    }
}
