package com.proj.kafkachatserver.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.kafkachatserver.repository.UserRepository;
import com.proj.kafkachatserver.models.User;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserOnlineStatus(Long userId, boolean onlineStatus) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setOnlineStatus(onlineStatus);
            userRepository.save(user);
        }
    }

    public int getOnlineUserCount() {
        return userRepository.countByOnlineStatus(true);
    }
    
    public List<User> getOnlineUsers() {
        return userRepository.findByOnlineStatus(true);
    }
}