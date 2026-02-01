package com.money_transfer.backend.dtos;

public class TransferResponse {
    
    private String transactionId;
    private String status;
    private Double transferredAmount;
    private String message;

    // Constructors, Getters, and Setters
    public TransferResponse(String transactionId, String status, Double transferredAmount, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.transferredAmount = transferredAmount;
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTransferredAmount() {
        return transferredAmount;
    }

    public void setTransferredAmount(Double transferredAmount) {
        this.transferredAmount = transferredAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
