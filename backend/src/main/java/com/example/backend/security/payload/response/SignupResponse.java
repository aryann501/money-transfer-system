package com.example.backend.security.payload.response;

public class SignupResponse {
    private Long userId;
    private String username;
    private String accountId;
    private String holderName;
    private Double balance;

    public SignupResponse(Long userId, String username, String accountId, String holderName, Double balance) {
        this.userId = userId;
        this.username = username;
        this.accountId = accountId;
        this.holderName = holderName;
        this.balance = balance;
    }

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getAccountId() { return accountId; }
    public String getHolderName() { return holderName; }
    public Double getBalance() { return balance; }
}
