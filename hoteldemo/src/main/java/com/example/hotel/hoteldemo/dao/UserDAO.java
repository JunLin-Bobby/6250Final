package com.example.hotel.hoteldemo.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel.dto.LoginForm;
import com.example.hotel.hoteldemo.pojo.User;
@Transactional
@Repository
public class UserDAO {
    @Autowired
    private SessionFactory sessionFactory;

    
    public void save(User user) {
        
        Session  session = sessionFactory.openSession();
        
        try{
            session.beginTransaction();
            session.persist(user);
            System.out.println("save User done.");
            session.getTransaction().commit();
        }catch(Exception e){
            session.getTransaction().rollback();
            throw e;
        }finally{
            session.close();
        }
    }
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
            System.out.println(UserDAO.class.getName() + " Error");
            throw e;
        }finally{
            session.close();
        }
        return user;
    }
    public User findById(Long id) {
        return getSession().get(User.class, id);
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
