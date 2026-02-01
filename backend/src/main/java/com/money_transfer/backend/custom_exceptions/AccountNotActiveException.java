package com.money_transfer.backend.custom_exceptions;

public class AccountNotActiveException extends RuntimeException {

    public AccountNotActiveException(String accountId) {
        super("Account with ID " + accountId + " is not active. It may be LOCKED or CLOSED.");
    }
}
