package com.example.backend.security.payload.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private final Long userId;
    private final String username;
    private final String accountId;
    private final String holderName;
    private final Double balance;

    public SignupResponse(Long userId, String username, String accountId, String holderName, Double balance) {
        this.userId = userId;
        this.username = username;
        this.accountId = accountId;
        this.holderName = holderName;
        this.balance = balance;
    }

}
