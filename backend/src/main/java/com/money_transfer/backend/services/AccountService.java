package com.money_transfer.backend.services;


import com.money_transfer.backend.exceptions.AccountNotFoundException;
import com.money_transfer.backend.models.Account;
import com.money_transfer.backend.models.TransactionLog;

import java.util.List;

public interface AccountService {
    Account getAccount(Integer accountId) throws AccountNotFoundException;
    Double getBalance(Integer accountId) throws AccountNotFoundException;
    List<TransactionLog> getTransactions(Integer accountId) throws AccountNotFoundException;
}

