package com.money_transfer.backend.dtos;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class TransferRequest {
    
    @NotNull(message = "From Account ID cannot be null")
    private String fromAccountId;

    @NotNull(message = "To Account ID cannot be null")
    private String toAccountId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private Double amount;

    // Constructors, Getters, and Setters

    public TransferRequest(String fromAccountId, String toAccountId, Double amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
