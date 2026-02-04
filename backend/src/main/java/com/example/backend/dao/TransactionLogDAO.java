package com.example.backend.dao;

import com.example.backend.entity.TransactionLog;

import java.util.List;

public interface TransactionLogDAO {
    void saveTransaction(TransactionLog transactionLog);
}
