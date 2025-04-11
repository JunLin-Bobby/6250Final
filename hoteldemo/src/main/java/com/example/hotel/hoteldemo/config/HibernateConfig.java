package com.example.hotel.hoteldemo.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import com.example.hotel.hoteldemo.pojo.*;
import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        Properties hibernateProps = new Properties();
        hibernateProps.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        hibernateProps.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/hotel_db?createDatabaseIfNotExist=true");
        hibernateProps.put("hibernate.connection.username", "jun6250");
        hibernateProps.put("hibernate.connection.password", "0000");
        hibernateProps.put("hibernate.connection.pool_size", "100");
        hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProps.put("hibernate.current_session_context_class", "thread");
        hibernateProps.put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheRegionFactoryAvailableException");
        hibernateProps.put("hibernate.show_sql", "true");
        hibernateProps.put("hibernate.hbm2ddl.auto", "create");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(hibernateProps)
                .build();

        return new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Reservation.class)
                .buildMetadata()
                .buildSessionFactory();
    }
}