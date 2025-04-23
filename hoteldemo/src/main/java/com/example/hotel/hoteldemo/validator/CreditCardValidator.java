package com.example.hotel.hoteldemo.validator;


import org.springframework.stereotype.Component;


@Component
public class CreditCardValidator {

    public  boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }

    public  boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    public  boolean isValidExpireDate(String expireDate) {
        return expireDate != null && expireDate.matches("^(0[1-9]|1[0-2])/\\d{2}$");
    }

}
