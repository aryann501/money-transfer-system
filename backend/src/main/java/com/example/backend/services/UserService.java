package com.example.backend.services;


import com.example.backend.entity.User;

public interface UserService {
    User signup(User user);
    User login(String username, String password);
}

