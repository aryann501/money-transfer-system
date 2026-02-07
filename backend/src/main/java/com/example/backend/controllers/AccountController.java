package com.example.backend.controllers;

import com.example.backend.dtos.AccountDTO;
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entities.Account;
import com.example.backend.exceptions.AccountNotFoundException;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    // Get account balance
    // GET http://localhost:8080/api/v1/accounts/balance
    @GetMapping("/balance")
    public ResponseEntity<?> getMyBalance() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        AccountDTO account = accountService.getAccount(userDetails.getAccountId());
        return ResponseEntity.ok(account.getBalance());
    }


    // Get transaction history
    // GET http://localhost:8080/api/v1/accounts/{id}/transactions
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String id) throws AccountNotFoundException {
        List<TransactionResponse> transactions = accountService.getTransactions(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
