package com.money_transfer.backend.dao;

import com.money_transfer.backend.models.TransactionLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionLogDAOImpl implements TransactionLogDAO {

    private static List<TransactionLog> transactions = new ArrayList<>();
    private static int transactionCounter = 1;

    @Override
    public String saveTransaction(TransactionLog transactionLog) {
        transactionLog.setId(transactionCounter++);
        transactionLog.setCreatedOn(LocalDateTime.now());
        transactions.add(transactionLog);
        return "Transaction saved successfully!";
    }

    @Override
    public List<TransactionLog> getTransactionsByAccountId(Integer accountId) {
        List<TransactionLog> result = new ArrayList<>();
        for (TransactionLog transaction : transactions) {
            if (transaction.getFromAccountId().equals(accountId) || transaction.getToAccountId().equals(accountId)) {
                result.add(transaction);
            }
        }
        return result;
    }
}
