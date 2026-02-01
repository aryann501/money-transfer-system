package com.money_transfer.backend.domain_entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionLog {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private LocalDateTime transactionTime;
    private TransactionStatus status;
    private String description;

    public TransactionLog(Long id, Long accountId, BigDecimal amount, LocalDateTime transactionTime, TransactionStatus status, String description) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.status = status;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}