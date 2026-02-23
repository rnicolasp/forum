package com.example.forum.controllers;

import com.example.forum.entities.dto.AuthResponse;
import com.example.forum.entities.dto.UserDto;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.UserRepository;
import com.example.forum.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/getprofile")
    public ResponseEntity<UserDto> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserDto(currentUser));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updatedData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (updatedData.containsKey("name")) {
            currentUser.setName(updatedData.get("name"));
        }
        if (updatedData.containsKey("email")) {
            currentUser.setEmail(updatedData.get("email"));
        }
        if (updatedData.containsKey("avatar")) {
            currentUser.setAvatarUrl(updatedData.get("avatar"));
        }

        userRepository.save(currentUser);

        String newToken = jwtService.generateToken(currentUser);

        return ResponseEntity.ok(new AuthResponse(new UserDto(currentUser), newToken));
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> passwords) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");

        if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            return ResponseEntity.badRequest().body("La contraseña actual es incorrecta");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);

        return ResponseEntity.ok().build();
    }
}
