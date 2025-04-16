package com.example.hotel.hoteldemo.dao;


import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.hotel.hoteldemo.pojo.Reservation;
import com.example.hotel.hoteldemo.pojo.User;

import org.hibernate.query.Query;
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
    //Finf all reservation
    public List<Reservation> findAll(){
        Session session = sessionFactory.openSession();
        try{
            String hql = "FROM Reservation";
            return session.createQuery(hql, Reservation.class).getResultList();
        }finally{
            session.close();
        }
    }
    //Find by RangeDATE
    public List<Reservation> findByDateRange(LocalDate start, LocalDate end){
        Session session = sessionFactory.openSession();
        try{
            String hql = "FROM Reservation r where r.checkInDate >= : start AND r.checkInDate <= :end";
            return session.createQuery(hql, Reservation.class)
                   .setParameter("start", start)
                   .setParameter("end", end).getResultList();
        }finally{
            session.close();
        }
    }
    //Find by user
    public List<Reservation> findByUser(User user){
        Session session = sessionFactory.openSession();
        try{
            String hql ="FROM Reservation r WHERE r.user = :user ORDER BY r.checkInDate DESC";
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            query.setParameter("user", user);
            return query.getResultList();
        }catch(Exception e){
            System.out.println(this.getClass().getName()+" findByUser Error (ReservationDAO)");
            throw e;
        }finally{
            session.close();
        }
    }
    //Find by reservationid
    public Reservation findById(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Reservation.class, id);
        } finally {
            session.close();
        }
    }

    public void update(Reservation reservation) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.merge(reservation);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }


    
}
