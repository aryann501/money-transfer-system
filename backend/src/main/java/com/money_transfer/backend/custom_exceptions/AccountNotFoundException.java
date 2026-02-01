package com.money_transfer.backend.custom_exceptions;

public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(String accountId) {
        super("Account with ID " + accountId + " not found.");
    }
}
