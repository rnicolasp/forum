package com.example.forum.controllers;

import com.example.forum.entities.dto.AuthResponse;
import com.example.forum.entities.models.User;
import com.example.forum.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User loginRequest) {
        return authService.login(loginRequest);
    }
}
