package com.example.forum.controllers;

import com.example.forum.entities.dto.AuthResponse;
import com.example.forum.entities.dto.UserDto;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.UserRepository;
import com.example.forum.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        user = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(new UserDto(user), jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String jwtToken = jwtService.generateToken(user);
                return ResponseEntity.ok(new AuthResponse(new UserDto(user), jwtToken));
            }
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}