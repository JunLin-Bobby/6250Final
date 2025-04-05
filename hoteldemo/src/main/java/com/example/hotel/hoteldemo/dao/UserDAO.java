package com.example.hotel.hoteldemo.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            throw e;
        }finally{
            session.close();
        }
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
