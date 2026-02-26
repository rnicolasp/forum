package com.example.forum.services;

import com.example.forum.entities.dto.AuthResponse;
import com.example.forum.entities.dto.UserDto;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.UserRepository;
import com.example.forum.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public AuthResponse updateProfile(Map<String, String> updatedData, User currentUser) {
        if (updatedData.containsKey("name")) currentUser.setName(updatedData.get("name"));
        if (updatedData.containsKey("avatar")) currentUser.setAvatarUrl(updatedData.get("avatar"));
        if (updatedData.containsKey("email")) currentUser.setEmail(updatedData.get("email"));
        userRepository.save(currentUser);
        return new AuthResponse(new UserDto(currentUser), jwtService.generateToken(currentUser));
    }

    public void updatePassword(Map<String, String> passwords, User currentUser) {
        currentUser.setPassword(passwordEncoder.encode(passwords.get("newPassword")));
        userRepository.save(currentUser);
    }
}
