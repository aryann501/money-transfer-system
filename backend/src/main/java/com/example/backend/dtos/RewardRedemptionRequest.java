package com.example.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for redeeming reward points against a transfer.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardRedemptionRequest {
    @NotBlank(message = "Transaction ID must not be blank")
    private String transactionId;

    @Positive(message = "Points to use must be a positive integer")
    private int pointsToUse;
}
