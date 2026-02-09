package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserTransferRequest {
    private String toAccountId;
    private Double amount;
    private String idempotencyKey;

}
