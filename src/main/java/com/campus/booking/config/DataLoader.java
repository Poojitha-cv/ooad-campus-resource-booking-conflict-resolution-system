package com.campus.booking.config;

import com.campus.booking.model.User;
import com.campus.booking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void loadAdmin() {

        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");

            userRepository.save(admin);
        }
    }
}