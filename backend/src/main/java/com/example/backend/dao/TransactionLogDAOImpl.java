package com.example.backend.dao;

import com.example.backend.entity.TransactionLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionLogDAOImpl implements TransactionLogDAO {
    Map<Integer, TransactionLog> store = new HashMap<Integer, TransactionLog>();
    @Override
    public void saveTransaction(TransactionLog transactionLog) {
        store.put(transactionLog.getId(), transactionLog);
    }
}
