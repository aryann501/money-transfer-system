package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TransactionResponse {

    // Getters and Setters
    private String fromAccountId;
    private String fromAccountHolderName;
    private String toAccountId;
    private String toAccountHolderName;
    private Double amount;
    private String status;
    private String failureReason;
    private LocalDateTime createdOn;

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "fromAccountId=" + fromAccountId +
                ", fromAccountHolderName='" + fromAccountHolderName + '\'' +
                ", toAccountId=" + toAccountId +
                ", toAccountHolderName='" + toAccountHolderName + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
