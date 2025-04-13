package com.example.hotel.hoteldemo.dao;


import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.hotel.hoteldemo.pojo.Reservation;
import org.hibernate.SessionFactory;

@Repository
public class ReservationDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void saveReservation(Reservation reservation){
        Session session = sessionFactory.openSession();
        try{
            session.beginTransaction();
            session.persist(reservation);
            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(this.getClass().getName() + " Error - saveReservation");
            session.getTransaction().rollback();
            throw e;
        }finally{
            session.close();
        }
    }

}
