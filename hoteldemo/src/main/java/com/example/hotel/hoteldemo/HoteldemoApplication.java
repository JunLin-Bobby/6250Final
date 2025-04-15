package com.example.hotel.hoteldemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.hotel.hoteldemo.dao.ManagerDAO;
import com.example.hotel.hoteldemo.dao.RoomDAO;
import com.example.hotel.hoteldemo.dao.UserDAO;
import com.example.hotel.hoteldemo.pojo.HotelManager;
import com.example.hotel.hoteldemo.pojo.Room;
import com.example.hotel.hoteldemo.pojo.User;

@SpringBootApplication
public class HoteldemoApplication implements CommandLineRunner{
	@Autowired
	RoomDAO roomDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ManagerDAO managerDAO;
	public static void main(String[] args) {
		SpringApplication.run(HoteldemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println(" HotelApp is running inserting DemoData....");

        HotelManager sa = new HotelManager();
		sa.setUserName("sa");
		sa.setPassword("0000");
		managerDAO.save(sa);
		User user = new User();
		user.setFirstName("Jun");
		user.setLastName("Lin");
		user.setPassword("123");
		user.setPhoneNumber("678938719");
		user.setEmail("lin.jun1@northeastern.edu");
		userDAO.save(user);
		
        for (int i = 1; i <= 4; i++) {
            Room room = new Room();
            room.setRoomNumber("R00" + i);
            room.setCapacity(i);
            room.setPricePerNight(100.0 * i); 
            roomDAO.save(room);
        }
		for (int i = 5; i <= 8; i++) {
            Room room = new Room();
            room.setRoomNumber("R00" + i);
            room.setCapacity(1);
            room.setPricePerNight(120.0 * i); 
            roomDAO.save(room);
        }

       
    }
	

}
