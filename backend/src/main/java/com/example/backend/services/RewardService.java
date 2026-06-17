package com.example.backend.services;

import com.example.backend.dtos.RewardRedemptionResponse;
import com.example.backend.dtos.RewardSummaryResponse;
import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionLog;
import com.example.backend.entities.UserEntity;

import java.math.BigDecimal;

public interface RewardService {

    void processRewardForTransfer(TransactionLog transaction, Account fromAccount, Account toAccount);

    RewardRedemptionResponse redeemPointsForTransfer(UserEntity user, String transactionId, int pointsToUse);

    int getAvailablePoints(Long userId);

    /** Validates reward redemption; returns cash portion still required from account balance. */
    BigDecimal resolveCashAmount(UserEntity user, BigDecimal transferAmount, Integer rewardPointsToUse);

    RewardSummaryResponse getMyRewards();
}
