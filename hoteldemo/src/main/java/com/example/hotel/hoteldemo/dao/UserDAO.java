package com.example.hotel.hoteldemo.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.hotel.hoteldemo.dto.LoginForm;
import com.example.hotel.hoteldemo.pojo.User;

@Repository
public class UserDAO {
    @Autowired
    private SessionFactory sessionFactory;

    
    public void save(User user) {
        
        Session  session = sessionFactory.openSession();
        
        try{
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }catch(Exception e){
            session.getTransaction().rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    ///////////Find user by id
    public User findById(Long id) {
        return getSession().get(User.class, id);
    }
    ///////////Find user by email
    public User findByEmail(LoginForm loginForm){
        Session  session = sessionFactory.openSession();
        User user = null;
        try{
            session.beginTransaction();
            user = session.createQuery("FROM User WHERE email = :email",User.class)
                          .setParameter("email", loginForm.getEmail())
                          .uniqueResult();
            session.getTransaction().commit();
        }catch(Exception e){
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            
            System.out.println(this.getClass().getName() + " Error - findByEmail");
            throw e;
        }finally{
            session.close();
        }
        return user;
    }
    ///////////Check existing email
    public boolean existsByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
            Long count = session.createQuery(hql, Long.class)
                                .setParameter("email", email)
                                .uniqueResult();
            return count > 0;
        } finally {
            session.close();
        }
    }
    ///////////Check existing phoneNumber
    public boolean existsByPhoneNumber(String phoneNumber) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "SELECT COUNT(u) FROM User u WHERE u.phoneNumber = :phone";
            Long count = session.createQuery(hql, Long.class)
                                .setParameter("phone", phoneNumber)
                                .uniqueResult();
            return count > 0;
        } finally {
            session.close();
        }
    }
    

    public void delete(User user) {
        getSession().remove(user);
    }

    public void update(User user) {
        getSession().merge(user);
    }
 
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    

}
