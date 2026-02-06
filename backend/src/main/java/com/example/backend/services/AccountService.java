package com.example.backend.services;


import com.example.backend.dtos.AccountDTO;
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entity.Account;
import com.example.backend.entity.TransactionLog;
import com.example.backend.exceptions.AccountNotFoundException;

import java.util.List;

public interface AccountService {
    AccountDTO getAccount(Long id) throws AccountNotFoundException;
    Double getBalance(Long id) throws AccountNotFoundException;
    List<TransactionResponse> getTransactions(Long id) throws AccountNotFoundException;
}
