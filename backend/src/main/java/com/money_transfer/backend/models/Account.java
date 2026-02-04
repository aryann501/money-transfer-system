package com.money_transfer.backend.models;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Account {

    private Integer id;
    private String holderName;
    private Double balance;
    private AccountStatus status;
    private Integer version;
    private LocalDateTime lastUpdated;

    // Business methods
    public void debit(Double amount) {
        this.balance -= amount;
        this.lastUpdated = LocalDateTime.now();
    }

    public void credit(Double amount) {
        this.balance += amount;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }
}
