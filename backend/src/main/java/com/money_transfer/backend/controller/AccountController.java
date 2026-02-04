package com.money_transfer.backend.controller;


import com.money_transfer.backend.dto.AccountResponse;
import com.money_transfer.backend.dto.ErrorResponse;
import com.money_transfer.backend.exceptions.AccountNotFoundException;
import com.money_transfer.backend.models.Account;
import com.money_transfer.backend.models.TransactionLog;
import com.money_transfer.backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Integer id) {
        Account account = accountService.getAccount(id);
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setHolderName(account.getHolderName());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus().name());
        return response;
    }

    @GetMapping("/{id}/balance")
    public Double getBalance(@PathVariable Integer id) {
        return accountService.getBalance(id);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionLog> getTransactions(@PathVariable Integer id) {
        return accountService.getTransactions(id);
    }

    // Example of handling exceptions locally
    @ExceptionHandler(AccountNotFoundException.class)
    public ErrorResponse handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("ACCOUNT_NOT_FOUND");
        error.setErrorMessage(ex.getMessage());
        return error;
    }
}

