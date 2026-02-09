package com.example.backend.controllers;

import com.example.backend.dtos.AccountDTO;
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.exceptions.AccountNotFoundException;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    // --- ADMIN ENDPOINTS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsById(@PathVariable String id) throws AccountNotFoundException {
        List<TransactionResponse> transactions = accountService.getTransactions(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // --- USER ENDPOINTS (accessible to USER only) ---
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-details")
    public ResponseEntity<?> getMyAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: {}, Roles: {}", auth.getName(), auth.getAuthorities());
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return ResponseEntity.ok(accountService.getAccount(userDetails.getAccountId()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/balance")
    public ResponseEntity<?> getMyBalance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: {}, Roles: {}", auth.getName(), auth.getAuthorities());
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        AccountDTO account = accountService.getAccount(userDetails.getAccountId());
        return ResponseEntity.ok(account.getBalance());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-transactions")
    public ResponseEntity<List<TransactionResponse>> getMyTransactions() throws AccountNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: {}, Roles: {}", auth.getName(), auth.getAuthorities());
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<TransactionResponse> transactions = accountService.getTransactions(userDetails.getAccountId());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
