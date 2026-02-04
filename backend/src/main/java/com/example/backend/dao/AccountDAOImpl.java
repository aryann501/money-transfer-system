package com.example.backend.dao;

import com.example.backend.entity.Account;
import com.example.backend.entity.TransactionLog;
import com.example.backend.exceptions.AccountNotFoundException;

import java.util.*;

public class AccountDAOImpl implements AccountDAO {

    Map<Integer, Account> store = new HashMap<Integer, Account>();
    @Override
    public void createAccount(Account account) {
        store.put(account.getId(), account);
    }

    @Override
    public Optional<Account> getAccount(Integer accountId) {
        return Optional.ofNullable(store.get(accountId));
    }

    @Override
    public void updateAccount(Account account) {
        store.replace(account.getId(), account);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        store.remove(accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<TransactionLog> getTransactionsByAccountId(Integer accountId) {
        return new ArrayList<>(store.get(accountId).getTransactions());
    }
}
