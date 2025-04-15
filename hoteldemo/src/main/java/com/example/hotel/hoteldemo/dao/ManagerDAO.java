package com.example.hotel.hoteldemo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.hotel.hoteldemo.pojo.HotelManager;

@Repository
public class ManagerDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(HotelManager HotelManager) {
        
        Session  session = sessionFactory.openSession();
        
        try{
            session.beginTransaction();
            session.persist(HotelManager);
            session.getTransaction().commit();
        }catch(Exception e){
            session.getTransaction().rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    ///////////Find manager by username
    public HotelManager findByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM HotelManager WHERE userName = :username";
            return session.createQuery(hql, HotelManager.class)
                          .setParameter("username", username)
                          .uniqueResult();
        } finally {
            session.close();
        }
    }

    
}
