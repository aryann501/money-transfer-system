package com.example.backend.entities;

import com.example.backend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holderName;

    private Double balance;

    @Column(name = "account_id", unique = true, nullable = false, length = 7)
    private String accountId;

    private AccountStatus status;

    private Integer version;

    private LocalDateTime lastUpdated;

    // Outgoing transactions (this account is the sender)
    @OneToMany(mappedBy = "fromAccount", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TransactionLog> outgoingTransactions;

    // Incoming transactions (this account is the receiver)
    @OneToMany(mappedBy = "toAccount", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TransactionLog> incomingTransactions;


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                ", Account Id=" + accountId +
                ", status=" + status +
                ", version=" + version +
                ", lastUpdated=" + lastUpdated +
                ", outgoingTransactions=" + outgoingTransactions +
                ", incomingTransactions=" + incomingTransactions +
                '}';
    }
}
