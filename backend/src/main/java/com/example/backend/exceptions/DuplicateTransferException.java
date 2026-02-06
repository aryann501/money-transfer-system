package com.example.backend.exceptions;

public class DuplicateTransferException extends RuntimeException {
    public DuplicateTransferException() {
    }

    public DuplicateTransferException(String message) {
        super(message);
    }

    public DuplicateTransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateTransferException(Throwable cause) {
        super(cause);
    }

    public DuplicateTransferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
