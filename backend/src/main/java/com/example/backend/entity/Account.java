package com.example.backend.entity;

import com.example.backend.enums.AccountStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holderName;

    private Double balance;

    private AccountStatus status;

    private Integer version;

    private LocalDateTime lastUpdated;

    // Outgoing transactions (this account is the sender)
    @OneToMany(mappedBy = "fromAccount", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TransactionLog> outgoingTransactions;

    // Incoming transactions (this account is the receiver)
    @OneToMany(mappedBy = "toAccount", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TransactionLog> incomingTransactions;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public AccountStatus getStatus() {
        return status;
    }
    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<TransactionLog> getOutgoingTransactions() {
        return outgoingTransactions;
    }
    public void setOutgoingTransactions(List<TransactionLog> outgoingTransactions) {
        this.outgoingTransactions = outgoingTransactions;
    }

    public List<TransactionLog> getIncomingTransactions() {
        return incomingTransactions;
    }
    public void setIncomingTransactions(List<TransactionLog> incomingTransactions) {
        this.incomingTransactions = incomingTransactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                ", version=" + version +
                ", lastUpdated=" + lastUpdated +
                ", outgoingTransactions=" + outgoingTransactions +
                ", incomingTransactions=" + incomingTransactions +
                '}';
    }
}
