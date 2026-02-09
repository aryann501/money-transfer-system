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

        TransactionStatus transactionStatus = TransactionStatus.SUCCESS;
        String failureReason = null;

        Account fromAccount = accountRepository.findByAccountId(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found: " + fromAccountId));

        Account toAccount = accountRepository.findByAccountId(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found: " + toAccountId));

        try {
            if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new AccountNotActiveException("Sender account is not active");
            }
            if (toAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new AccountNotActiveException("Receiver account is not active");
            }

            if (fromAccount.getBalance() < amount) {
                throw new InsufficientBalanceException("Insufficient balance in sender account");
            }

            if (transactionLogRepository.findByIdempotencyKey(idempotencyKey) != null) {
                throw new DuplicateTransferException("Duplicate transfer detected with idempotency key: " + idempotencyKey);
            }

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

        } catch (Exception e) {
            transactionStatus = TransactionStatus.FAILED;
            failureReason = e.getMessage();  // capture reason
        }

        TransactionLog log = new TransactionLog();
        log.setFromAccount(fromAccount);
        log.setToAccount(toAccount);
        log.setAmount(amount);
        log.setStatus(transactionStatus);
        log.setFailureReason(failureReason); // set failure reason
        log.setIdempotencyKey(idempotencyKey);
        log.setCreatedOn(LocalDateTime.now());

        transactionLogRepository.save(log);

        TransactionResponse response = new TransactionResponse();
        response.setFromAccountId(fromAccount.getAccountId());
        response.setFromAccountHolderName(fromAccount.getHolderName());
        response.setToAccountId(toAccount.getAccountId());
        response.setToAccountHolderName(toAccount.getHolderName());
        response.setAmount(amount);
        response.setStatus(transactionStatus.name());
        response.setFailureReason(failureReason); // include in response
        response.setCreatedOn(LocalDateTime.now());

        return response;
    }
}
