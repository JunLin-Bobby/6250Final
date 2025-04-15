package com.example.hotel.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.hotel.hoteldemo.dao.UserDAO;
import com.example.hotel.hoteldemo.dto.UserRegistrationDTO;
import com.example.hotel.hoteldemo.pojo.User;
import com.example.hotel.hoteldemo.validator.RegisterValidator;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class RegisterController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    private RegisterValidator userValidator;

    @GetMapping("/register")
    public String showPage(Model model,HttpSession session){
        if(session.getAttribute("loggedInUser")!=null){
            return "redirect:/dashboard";
        }
        model.addAttribute("registerUser",new UserRegistrationDTO());
        return "registerPage";
    }
    
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("registerUser") @Valid UserRegistrationDTO userDTO,
                                  BindingResult result,
                                  Model model){
         
        if (result.hasErrors()) {
            return "registerPage";
        }

        //validate userDTO        
        userValidator.validate(userDTO, result);
        if (result.hasErrors()) {
            return "registerPage";
        }

        //save User
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());

        userDAO.save(user);
        return "redirect:/login";
    }
}
