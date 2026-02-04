package com.example.backend.services;

import com.example.backend.repository.TransactionLogRepository;

import java.util.List;

public interface TransferService {
    void credit(int accountId, double amount);
    TransactionLogRepository debit(int accountId, double amount);
    TransactionLogRepository transfer(int fromAccountId, int toAccountId, double amount);
    List<TransactionLogRepository> getTransactionsByAccountId(Integer accountId);
    List<TransactionLogRepository> getAllTransactions();
}
