package com.example.backend.dao;

import com.example.backend.entity.Account;
import com.example.backend.entity.TransactionLog;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {
    void createAccount(Account account);
    Optional<Account> getAccount(Integer accountId);
    void updateAccount(Account account);
    void deleteAccount(Integer accountId);
    List<Account> getAllAccounts();
    List<TransactionLog> getTransactionsByAccountId(Integer accountId);
}
