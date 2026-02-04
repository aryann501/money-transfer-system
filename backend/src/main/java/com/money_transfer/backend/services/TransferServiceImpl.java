package com.money_transfer.backend.services;

import com.money_transfer.backend.models.Account;
import com.money_transfer.backend.models.TransactionLog;
import com.money_transfer.backend.models.TransactionStatus;
import com.money_transfer.backend.dao.AccountDAOImpl;
import com.money_transfer.backend.dao.TransactionLogDAOImpl;
import com.money_transfer.backend.dto.TransferResponse;
import com.money_transfer.backend.dto.TransferRequest;
import com.money_transfer.backend.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountDAOImpl accountDAO;

    @Autowired
    private TransactionLogDAOImpl transactionLogDAO;

    @Override
    public TransferResponse transfer(TransferRequest request)
            throws AccountNotFoundException, AccountNotActiveException,
            InsufficientBalanceException, DuplicateTransferException {

        // Validate accounts
        Account fromAccount = accountDAO.getAccount(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("From Account not found"));
        Account toAccount = accountDAO.getAccount(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("To Account not found"));

        // Check if accounts are active
        if (!fromAccount.isActive()) throw new AccountNotActiveException("From Account is not active");
        if (!toAccount.isActive()) throw new AccountNotActiveException("To Account is not active");

        // Check balance
        if (fromAccount.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in From Account");
        }

        // Check duplicate transfer (idempotency key)
        for (TransactionLog log : transactionLogDAO.getTransactionsByAccountId(fromAccount.getId())) {
            if (log.getIdempotencyKey().equals(request.getIdempotencyKey())) {
                throw new DuplicateTransferException("Duplicate transfer detected");
            }
        }

        // Execute transfer
        fromAccount.debit(request.getAmount());
        toAccount.credit(request.getAmount());

        // Save transaction log
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setFromAccountId(fromAccount.getId());
        transactionLog.setToAccountId(toAccount.getId());
        transactionLog.setAmount(request.getAmount());
        transactionLog.setStatus(TransactionStatus.SUCCESS);
        transactionLog.setIdempotencyKey(request.getIdempotencyKey());
        transactionLog.setCreatedOn(LocalDateTime.now());
        transactionLogDAO.saveTransaction(transactionLog);

        // Build response
        TransferResponse response = new TransferResponse();
        response.setMessage("Transfer successful");
        response.setTransactionId(transactionLog.getId());
        response.setStatus(TransactionStatus.SUCCESS.name());

        return response;
    }
}

