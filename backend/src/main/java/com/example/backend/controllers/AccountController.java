package com.example.backend.controllers;

import com.example.backend.dtos.AccountDTO;
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entity.Account;
import com.example.backend.entity.TransactionLog;
import com.example.backend.exceptions.AccountNotFoundException;
import com.example.backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Get account details
    // GET http://localhost:8080/api/v1/accounts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) throws AccountNotFoundException {
        AccountDTO account = accountService.getAccount(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Get account balance
    // GET http://localhost:8080/api/v1/accounts/{id}/balance
    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long id) throws AccountNotFoundException {
        Double balance = accountService.getBalance(id);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    // Get transaction history
    // GET http://localhost:8080/api/v1/accounts/{id}/transactions
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long id) throws AccountNotFoundException {
        List<TransactionResponse> transactions = accountService.getTransactions(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
