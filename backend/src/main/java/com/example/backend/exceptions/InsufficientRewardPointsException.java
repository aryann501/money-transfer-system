package com.example.backend.exceptions;

public class InsufficientRewardPointsException extends RuntimeException {
    public InsufficientRewardPointsException(String message) {
        super(message);
    }

    public InsufficientRewardPointsException(int points, int available) {

    }
}
