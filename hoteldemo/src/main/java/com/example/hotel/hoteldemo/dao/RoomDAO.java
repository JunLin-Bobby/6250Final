package com.example.hotel.hoteldemo.dao;

import java.time.LocalDate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.example.hotel.hoteldemo.pojo.Room;

@Repository
public class RoomDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(Room room){
        Session session = sessionFactory.openSession();
        try{
            session.beginTransaction();
            session.persist(room);
            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(UserDAO.class.getName() + " Save Room Error");
            session.getTransaction().rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    public List<Room> findAll(){
        Session session = sessionFactory.openSession();
        try{
            String hql = "FROM Room";
            return session.createQuery(hql,Room.class).getResultList();
        }finally{
            session.close();
        }
    }
    //filter for searching room
    public List<Room> searchRooms(double minPrice, double maxPrice, int capacity,
                                LocalDate checkIn, LocalDate checkOut) {
    Session session = sessionFactory.openSession();
    try {
        String hql = """
            SELECT r
            FROM Room r
            WHERE r.pricePerNight BETWEEN :min AND :max
              AND r.capacity = :cap
              AND r.roomID NOT IN (
                  SELECT res.room.roomID
                  FROM Reservation res
                  WHERE (:checkOut > res.checkInDate AND :checkIn < res.checkOutDate)
                  AND res.status = 'CREATED'
              )
            """;

        Query<Room> query = session.createQuery(hql, Room.class);
        query.setParameter("min", minPrice);
        query.setParameter("max", maxPrice);
        query.setParameter("cap", capacity);
        query.setParameter("checkIn", checkIn);
        query.setParameter("checkOut", checkOut);

        return query.getResultList();
    } catch(Exception e){
        System.out.println("Search Room Error");
        throw e;
    }finally {
        session.close();
        }
    }
    //find by roomid
    public Room findByRoomID(int roomID){
        Session session = sessionFactory.openSession();
        
        try{
           Room room = session.get(Room.class,roomID);
            if(room!=null){
                Hibernate.initialize(room.getReservations());
            }
            return room;
        }catch(Exception e){
            System.out.println(this.getClass().getName()+" Error (findByRoomID)");
            System.out.println(e.getMessage());
        }finally{
            session.close();
        }
        return null;
    }

    //delete room
    public void delete(Room room) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.remove(room); 
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    

}
