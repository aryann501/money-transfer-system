package com.money_transfer.backend.dto;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;


@Getter
@Setter
@ToString
public class TransferRequest {

    @NotNull(message = "From Account ID cannot be null")
    private Integer fromAccountId;

    @NotNull(message = "To Account ID cannot be null")
    private Integer toAccountId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.1", message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Idempotency Key cannot be null")
    private String idempotencyKey;
}
