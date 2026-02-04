package com.money_transfer.backend.services;

import com.money_transfer.backend.exceptions.AccountNotFoundException;
import com.money_transfer.backend.models.Account;
import com.money_transfer.backend.models.TransactionLog;
import com.money_transfer.backend.dao.AccountDAOImpl;
import com.money_transfer.backend.dao.TransactionLogDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDAOImpl accountDAO;

    @Autowired
    private TransactionLogDAOImpl transactionLogDAO;

    @Override
    public Account getAccount(Integer accountId) throws AccountNotFoundException {
        return accountDAO.getAccount(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
    }

    @Override
    public Double getBalance(Integer accountId) throws AccountNotFoundException {
        Account account = getAccount(accountId);
        return account.getBalance();
    }

    @Override
    public List<TransactionLog> getTransactions(Integer accountId) throws AccountNotFoundException {
        getAccount(accountId); // ensure account exists
        return transactionLogDAO.getTransactionsByAccountId(accountId);
    }
}

