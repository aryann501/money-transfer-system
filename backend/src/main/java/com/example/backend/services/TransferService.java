package com.example.backend.services;


import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entity.TransactionLog;
import com.example.backend.exceptions.*;

public interface TransferService {
    TransactionResponse transfer(Long fromAccountId, Long toAccountId, Double amount, String idempotencyKey)
            throws AccountNotFoundException, AccountNotActiveException,
            InsufficientBalanceException, DuplicateTransferException;
}

