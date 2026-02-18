package com.example.forum.entities.dto;

import com.example.forum.entities.models.User;

public class UserDto {
    private String role;
    private String _id;
    private String id;
    private String email;
    private String name;
    private int __v = 0; // Simula la versión de documento de Mongoose/MongoDB
    private String avatarUrl;
    private PermissionsDto permissions;

    public UserDto(User user) {
        this.role = user.getRole();
        // El frontend espera un string en el ID (formato MongoDB). Parseamos nuestro Integer a String.
        this._id = String.valueOf(user.getUser_id());
        this.id = String.valueOf(user.getUser_id());
        this.email = user.getEmail();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : "";
        this.permissions = new PermissionsDto();
    }

    // Getters
    public String getRole() { return role; }
    public String get_id() { return _id; }
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public int get__v() { return __v; }
    public String getAvatarUrl() { return avatarUrl; }
    public PermissionsDto getPermissions() { return permissions; }
}