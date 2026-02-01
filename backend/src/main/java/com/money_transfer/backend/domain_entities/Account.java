package com.money_transfer.backend.domain_entities;

import java.time.LocalDateTime;

public class Account {
    private String id;
    private String holderName;
    private double balance;
    private AccountStatus status;
    private int version;
    private LocalDateTime lastUpdated;

    public Account(String id, String holderName, double balance, AccountStatus status, int version) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
        this.status = status;
        this.version = version;
        this.lastUpdated = LocalDateTime.now();
    }

    public void debit(double amount) {
        if (status == AccountStatus.ACTIVE && balance >= amount) {
            balance -= amount;
            lastUpdated = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Insufficient funds or account is not active.");
        }
    }

    public void credit(double amount) {
        if (status == AccountStatus.ACTIVE) {
            balance += amount;
            lastUpdated = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Account is not active.");
        }
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
