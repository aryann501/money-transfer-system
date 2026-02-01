package com.money_transfer.backend.custom_exceptions;

public class InsufficientBalanceException extends RuntimeException {
    
    public InsufficientBalanceException(String accountId, double balance, double transferAmount) {
        super("Account with ID " + accountId + " has insufficient balance. Current balance: " 
              + balance + ", Transfer amount: " + transferAmount);
    }
}
