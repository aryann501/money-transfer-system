package com.example.backend.controllers;

import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entity.TransactionLog;
import com.example.backend.services.TransferService;
import com.example.backend.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/transfers")
public class TransactionController {

    @Autowired
    private TransferService transferService;

    // Execute fund transfer
    // POST http://localhost:8080/api/v1/transfers
    @PostMapping
    public ResponseEntity<TransactionResponse> transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam Double amount,
            @RequestParam String idempotencyKey
    ) throws AccountNotFoundException, AccountNotActiveException,
            InsufficientBalanceException, DuplicateTransferException {

        TransactionResponse res = transferService.transfer(fromAccountId, toAccountId, amount, idempotencyKey);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}

