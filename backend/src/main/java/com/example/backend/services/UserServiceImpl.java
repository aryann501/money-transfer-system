package com.example.backend.services;

import com.example.backend.entity.Account;
import com.example.backend.entity.User;
import com.example.backend.enums.AccountStatus;
import com.example.backend.exceptions.UserAuthenticationException;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final double MINIMUM_BALANCE = 3000.0;

    @Override
    public User signup(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAuthenticationException("Username already exists");
        }

        Account account = user.getAccount();

        if (account != null) {
            // Validate provided account
            if (account.getBalance() < MINIMUM_BALANCE) {
                throw new UserAuthenticationException("Account must have a minimum balance of " + MINIMUM_BALANCE);
            }
            // Normalize account fields
            account.setHolderName(user.getUsername());
            account.setStatus(AccountStatus.ACTIVE);
            account.setLastUpdated(LocalDateTime.now());
        } else {
            // Create a new account if none provided
            account = new Account();
            account.setHolderName(user.getUsername());
            account.setBalance(MINIMUM_BALANCE);
            account.setStatus(AccountStatus.ACTIVE);
            account.setLastUpdated(LocalDateTime.now());
        }

        user.setAccount(account);

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new UserAuthenticationException("Invalid username or password");
        }
        return user;
    }
}
