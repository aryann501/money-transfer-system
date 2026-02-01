package com.money_transfer.backend.custom_exceptions;

public class DuplicateTransferException extends RuntimeException {
    
    public DuplicateTransferException(String idempotencyKey) {
        super("A transfer with idempotency key " + idempotencyKey + " has already been processed.");
    }
}
