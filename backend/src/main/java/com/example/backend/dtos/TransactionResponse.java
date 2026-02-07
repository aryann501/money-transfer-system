package com.example.backend.dtos;

import java.time.LocalDateTime;

public class TransactionResponse {

    private String fromAccountId;
    private String fromAccountHolderName;
    private String toAccountId;
    private String toAccountHolderName;
    private Double amount;
    private String status;  // SUCCESS or FAILED
    private LocalDateTime createdOn;

    // Getters and Setters
    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getFromAccountHolderName() {
        return fromAccountHolderName;
    }

    public void setFromAccountHolderName(String fromAccountHolderName) {
        this.fromAccountHolderName = fromAccountHolderName;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToAccountHolderName() {
        return toAccountHolderName;
    }

    public void setToAccountHolderName(String toAccountHolderName) {
        this.toAccountHolderName = toAccountHolderName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "fromAccountId=" + fromAccountId +
                ", fromAccountHolderName='" + fromAccountHolderName + '\'' +
                ", toAccountId=" + toAccountId +
                ", toAccountHolderName='" + toAccountHolderName + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}

