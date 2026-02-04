package com.example.backend.services;

import com.example.backend.entity.Account;
import com.example.backend.exceptions.AccountNotFoundException;

public interface AccountService {

    // Create Account
    public Account createAccount(Account account);

    // Get Account Data
    public Account getAccountById(Integer accountId) throws AccountNotFoundException;

    // Update Account
    public Account updateAccount(Account account) throws AccountNotFoundException;

    // Delete Account
    public void deleteAccount(Account account) throws AccountNotFoundException;
}
