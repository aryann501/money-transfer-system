package com.example.backend.dtos;

public class UserTransferRequest {

    private String toAccountId;
    private Double amount;
    private String idempotencyKey;

    // Getters
    public String getToAccountId() {
        return toAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    // Setters
    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
