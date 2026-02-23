package com.example.forum.entities.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class AuthResponse {
    private UserDto user;
    @JsonProperty("token")
    private String jwtToken;
    public AuthResponse(UserDto user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }
    public UserDto getUser() {
        return user;
    }
    public void setUser(UserDto user) {
        this.user = user;
    }
    public String getJwtToken() {
        return jwtToken;
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
