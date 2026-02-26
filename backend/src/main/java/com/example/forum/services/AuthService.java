package com.example.forum.services;

import com.example.forum.entities.dto.AuthResponse;
import com.example.forum.entities.dto.UserDto;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.UserRepository;
import com.example.forum.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    public AuthResponse register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este email ya está en uso. Por favor, elige otro.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole().equals("user") || user.getRole().isEmpty()) user.setRole("admin");
        
        User savedUser = userRepository.save(user);
        return new AuthResponse(new UserDto(savedUser), jwtService.generateToken(savedUser));
    }

    public AuthResponse login(User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El email o la contraseña son incorrectos.");
        }
        User user = userOpt.get();
        return new AuthResponse(new UserDto(user), jwtService.generateToken(user));
    }
}