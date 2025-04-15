package com.example.hotel.hoteldemo.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="HotelManager")
public class HotelManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int managerID;
    @Column(name="user_name" ,nullable=false)
    private String userName;
    @Column(name="password" ,nullable=false)
    private String password;
    public int getManagerID() {
        return managerID;
    }
    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
