package com.example.backend.security.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {

    // Getters and Setters
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;

    // No-args constructor
    public JwtResponse() {
    }

    // All-args constructor
    public JwtResponse(String accessToken, String tokenType, Long id, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    // Custom constructor (without tokenType, defaults to "Bearer")
    public JwtResponse(String accessToken, Long id, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    // Convenience method
    public String getToken() {
        return accessToken;
    }

    // toString method
    @Override
    public String toString() {
        return "JwtResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
