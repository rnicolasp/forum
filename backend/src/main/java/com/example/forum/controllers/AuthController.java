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

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("message", "Este email ya está en uso. Por favor, elige otro."));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        
        return ResponseEntity.ok(new AuthResponse(new UserDto(savedUser), jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "El email o la contraseña son incorrectos."));
        }

        User user = userOpt.get();
        String jwtToken = jwtService.generateToken(user);
        
        return ResponseEntity.ok(new AuthResponse(new UserDto(user), jwtToken));
    }
}
