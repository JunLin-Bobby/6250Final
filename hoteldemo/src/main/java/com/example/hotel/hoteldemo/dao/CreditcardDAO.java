package com.example.hotel.hoteldemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.hotel.hoteldemo.pojo.Creditcard;

import com.example.hotel.hoteldemo.pojo.User;

@Repository
public class CreditcardDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(Creditcard card) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(card);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    //Find by user
    public List<Creditcard> findByUser(User user){
        Session session = sessionFactory.openSession();
        try{
            String hql ="FROM Creditcard c WHERE c.user = :user";
            Query<Creditcard> query = session.createQuery(hql, Creditcard.class);
            query.setParameter("user", user);
            return query.getResultList();
        }catch(Exception e){
            System.out.println(this.getClass().getName()+" findByUser Error (CreditDAO)");
            throw e;
        }finally{
            session.close();
        }
    }

    public Creditcard findById(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Creditcard.class, id);
        } finally {
            session.close();
        }
    }
    
    public void delete(Creditcard card) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.remove(card);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
}
