package com.example.backend.services;

import com.example.backend.entity.Account;
import com.example.backend.entity.TransactionLog;
import com.example.backend.enums.AccountStatus;
import com.example.backend.exceptions.AccountNotActiveException;
import com.example.backend.exceptions.InvalidAmountException;
import com.example.backend.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    public AccountService accountService;
    public TransactionLogRepository log;

    @Override
    public void credit(int accountId, double amount) {
    }

    @Override
    public TransactionLogRepository debit(int accountId, double amount) {
        return null;
    }

    @Override
    public TransactionLogRepository transfer(int fromAccountId, int toAccountId, double amount) {
        return null;
    }

    @Override
    public List<TransactionLogRepository> getTransactionsByAccountId(Integer accountId) {
        return null;
    }

    @Override
    public List<TransactionLogRepository> getAllTransactions() {
        return null;
    }
}
