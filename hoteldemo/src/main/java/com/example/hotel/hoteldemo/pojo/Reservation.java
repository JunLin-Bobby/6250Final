package com.example.hotel.hoteldemo.pojo;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationID;

    //fk
    @ManyToOne
    @JoinColumn(name="userID",nullable = false)
    private User user;

    //fk
    @ManyToOne
    @JoinColumn(name="roomID",nullable = false)
    private Room room;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "contact_firstname")
    private String contactFirstName;
    @Column(name = "contact_lastname")
    private String contactLastName;
    @Column(name = "contact_number")
    private String contactPhoneNumber;
    @Column(name = "total_Amount")
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;
    
    
    public int getReservationID() {
        return reservationID;
    }
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    public String getContactFirstName() {
        return contactFirstName;
    }
    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }
    public String getContactLastName() {
        return contactLastName;
    }
    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }
    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }
    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public ReservationStatus getStatus() {
        return status;
    }
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }


}
