package com.example.hotel.hoteldemo.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.example.hotel.hoteldemo.dao.UserDAO;
import com.example.hotel.hoteldemo.dto.UserRegistrationDTO;

@Component
public class RegisterValidator {
    @Autowired
    private UserDAO userDAO;
    

    public void validate(UserRegistrationDTO userDTO, BindingResult result) {
        


        
        if (userDAO.existsByEmail(userDTO.getEmail())) {
            result.rejectValue("email", null, "This email is already registered.");
        }
        
        String password = userDTO.getPassword();
        if (password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            result.rejectValue("password", null,
                "Password must be at least 8 characters long and include both letters and numbers.");
        }
                
        if (userDAO.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            result.rejectValue("phoneNumber", null, "Phone number already in use.");
        }

        
        if (!userDTO.getPhoneNumber().matches("\\d{10}")) {
            result.rejectValue("phoneNumber", null, "Phone number must be exactly 10 digits.");
        }


        String email = userDTO.getEmail();
        if (email == null || !email.contains("@") || email.split("@").length != 2) {
            result.rejectValue("email", null, "Invalid email format.");
        }

        

    }
}
