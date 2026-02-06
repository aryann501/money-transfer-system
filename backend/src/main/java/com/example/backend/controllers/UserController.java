package com.example.backend.controllers;

import com.example.backend.entity.User;
import com.example.backend.exceptions.UserAuthenticationException;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) throws UserAuthenticationException {
        User createdUser = userService.signup(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password)
            throws UserAuthenticationException {
        User user = userService.login(username, password);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

