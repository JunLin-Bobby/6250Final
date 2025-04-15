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
        


        // 檢查 Email 是否已存在
        if (userDAO.existsByEmail(userDTO.getEmail())) {
            result.rejectValue("email", null, "This email is already registered.");
        }

        // 檢查 Phone 是否已存在
        if (userDAO.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            result.rejectValue("phoneNumber", null, "Phone number already in use.");
        }

        // 額外檢查格式（非空、長度正確）可用 DTO 註解完成
        if (!userDTO.getPhoneNumber().matches("\\d{10}")) {
            result.rejectValue("phoneNumber", null, "Phone number must be exactly 10 digits.");
        }

        // 基本格式檢查：必須包含一個 @，且前後都有文字
        String email = userDTO.getEmail();
        if (email == null || !email.contains("@") || email.startsWith("@") || email.endsWith("@") || email.split("@").length != 2) {
            result.rejectValue("email", null, "Invalid email format.");
        }



    }
}
