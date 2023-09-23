package com.proj.kafkachatserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proj.kafkachatserver.models.User;
import com.proj.kafkachatserver.services.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/login")
    public void loginUser(@PathVariable Long userId) {
        userService.setUserOnlineStatus(userId, true);
    }

    @PostMapping("/{userId}/logout")
    public void logoutUser(@PathVariable Long userId) {
        userService.setUserOnlineStatus(userId, false);
    }

    @GetMapping("/online-count")
    public int getOnlineUserCount() {
        return userService.getOnlineUserCount();
    }
    @GetMapping("/online-users")
    public List<User> getOnlineUsers() {
        return userService.getOnlineUsers();
    }
}
