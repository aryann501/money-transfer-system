package com.example.backend.security.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    private String holderName;

    @Min(1000) // ✅ enforce minimum balance
    private Double minBalance;

    public SignupRequest() {}

    public SignupRequest(String username, Set<String> role, String password, String holderName, Double minBalance) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.holderName = holderName;
        this.minBalance = minBalance;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Set<String> getRole() { return role; }
    public void setRole(Set<String> role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public Double getMinBalance() { return minBalance; }
    public void setMinBalance(Double minBalance) { this.minBalance = minBalance; }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", holderName='" + holderName + '\'' +
                ", minBalance=" + minBalance +
                '}';
    }
}
