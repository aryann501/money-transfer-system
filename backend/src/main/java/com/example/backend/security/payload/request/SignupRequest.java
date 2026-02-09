package com.example.backend.security.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
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

    @Min(1000)
    private Double minBalance;

    public SignupRequest() {}

    public SignupRequest(String username, Set<String> role, String password, String holderName, Double minBalance) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.holderName = holderName;
        this.minBalance = minBalance;
    }

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
