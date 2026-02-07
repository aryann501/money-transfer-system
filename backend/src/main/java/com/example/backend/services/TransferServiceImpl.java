package com.example.backend.services;

import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionLog;
import com.example.backend.enums.AccountStatus;
import com.example.backend.enums.TransactionStatus;
import com.example.backend.exceptions.*;
import com.example.backend.repositories.AccountRepository;
import com.example.backend.repositories.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Override
    public TransactionResponse transfer(String fromAccountId, String toAccountId, Double amount, String idempotencyKey)
            throws AccountNotFoundException, AccountNotActiveException,
            InsufficientBalanceException, DuplicateTransferException {

        // Initialize transaction status as SUCCESS
        TransactionStatus transactionStatus = TransactionStatus.SUCCESS;

        // Fetch the sender and receiver accounts
        Account fromAccount = accountRepository.findByAccountId(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found: " + fromAccountId));

        Account toAccount = accountRepository.findByAccountId(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found: " + toAccountId));

        try {
            // Check if both accounts are active
            if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new AccountNotActiveException("Sender account is not active");
            }
            if (toAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new AccountNotActiveException("Receiver account is not active");
            }

            // Check if sender has sufficient balance
            if (fromAccount.getBalance() < amount) {
                throw new InsufficientBalanceException("Insufficient balance in sender account");
            }

            // Check if the transaction with the given idempotency key already exists
            if (transactionLogRepository.findByIdempotencyKey(idempotencyKey) != null) {
                throw new DuplicateTransferException("Duplicate transfer detected with idempotency key: " + idempotencyKey);
            }

            // Execute the transfer
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            // Save updated account balances
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

        } catch (Exception e) {
            // If any exception occurs, set the status to FAILED
            transactionStatus = TransactionStatus.FAILED;
        }

        // Log the transaction, whether it succeeded or failed
        TransactionLog log = new TransactionLog();
        log.setFromAccount(fromAccount);
        log.setToAccount(toAccount);
        log.setAmount(amount);
        log.setStatus(transactionStatus);  // Set dynamic status (SUCCESS/FAILED)
        log.setIdempotencyKey(idempotencyKey);
        log.setCreatedOn(LocalDateTime.now());

        // Save the transaction log to the database
        transactionLogRepository.save(log);

        // Return a simplified response with transaction details and status
        TransactionResponse response = new TransactionResponse();
        response.setFromAccountId(fromAccount.getAccountId());
        response.setFromAccountHolderName(fromAccount.getHolderName());
        response.setToAccountId(toAccount.getAccountId());
        response.setToAccountHolderName(toAccount.getHolderName());
        response.setAmount(amount);
        response.setStatus(transactionStatus.name());  // Use enum's string value (SUCCESS/FAILED)
        response.setCreatedOn(LocalDateTime.now());

        return response;
    }
}
