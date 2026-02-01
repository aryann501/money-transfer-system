package com.money_transfer.backend.dtos;

public class AccountResponse {

    private String accountId;
    private String holderName;
    private Double balance;
    private String status;

    // Constructors, Getters, and Setters
    public AccountResponse(String accountId, String holderName, Double balance, String status) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.balance = balance;
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
