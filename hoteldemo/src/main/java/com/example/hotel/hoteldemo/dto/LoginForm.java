package com.example.hotel.hoteldemo.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "Email can not be blank!")
    private String email;

    @NotBlank(message = "Password can not be blank!")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
