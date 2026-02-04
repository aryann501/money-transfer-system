package com.money_transfer.backend.dao;

import com.money_transfer.backend.models.TransactionLog;

import java.util.List;

public interface TransactionLogDAO {
    String saveTransaction(TransactionLog transactionLog);
    List<TransactionLog> getTransactionsByAccountId(Integer accountId);
}

