package com.example.hotel.hoteldemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.hotel.hoteldemo.dao.RoomDAO;
import com.example.hotel.hoteldemo.pojo.Room;
import com.example.hotel.hoteldemo.pojo.User;

@SpringBootApplication
public class HoteldemoApplication implements CommandLineRunner{
	@Autowired
	RoomDAO roomDAO;
	public static void main(String[] args) {
		SpringApplication.run(HoteldemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println("🚀 HotelApp is running inserting RoomData....");

        
		User user = new User();
		user.setFirstName("qweqwe");
		user.setLastName("lsast");
		user.setPassword("123123");
		user.setPhoneNumber("1232132313");
		user.setEmail("bob@gmail.com");
		
        for (int i = 1; i <= 4; i++) {
            Room room = new Room();
            room.setRoomNumber("R00" + i);
            room.setCapacity(i);
            room.setPricePerNight(100.0 * i); // 價格範例

            roomDAO.save(room);
        }

       
    }
	

}
