package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AccountDTO {

    // Getters and Setters
    private Long id;
    private String holderName;
    private Double balance;
    private String accountId;
    private String status;
    private Integer version;
    private LocalDateTime lastUpdated;

}
