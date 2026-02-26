package com.example.forum.controllers;

import com.example.forum.entities.dto.AuthResponse;
import com.example.forum.entities.dto.UserDto;
import com.example.forum.entities.models.User;
import com.example.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/getprofile")
    public UserDto getProfile() {
        return new UserDto(getCurrentUser());
    }

    @PutMapping("/profile")
    public AuthResponse updateProfile(@RequestBody Map<String, String> updatedData) {
        return userService.updateProfile(updatedData, getCurrentUser());
    }

    @PutMapping("/profile/password")
    public void updatePassword(@RequestBody Map<String, String> passwords) {
        userService.updatePassword(passwords, getCurrentUser());
    }
}
