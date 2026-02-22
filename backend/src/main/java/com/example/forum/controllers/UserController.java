package com.example.forum.controllers;

import com.example.forum.entities.dto.UserDto;
import com.example.forum.entities.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/getprofile")
    public ResponseEntity<UserDto> getProfile() {
        // Sacamos el usuario de la petición autenticada actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        // Lo devolvemos envuelto en el UserDto para no mostrar la contraseña en el JSON
        return ResponseEntity.ok(new UserDto(currentUser));
    }
}
