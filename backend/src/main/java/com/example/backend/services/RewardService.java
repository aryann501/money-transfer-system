package com.example.backend.services;

import com.example.backend.dtos.RewardSummaryResponse;
import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionLog;
import com.example.backend.entities.UserEntity;

import java.math.BigDecimal;

public interface RewardService {

    void processRewardForTransfer(TransactionLog transaction, Account fromAccount, Account toAccount);



    int getAvailablePoints(Long userId);

    /** Validates reward redemption; returns cash portion still required from account balance. */
    BigDecimal resolveCashAmount(UserEntity user, BigDecimal transferAmount, Integer rewardPointsToUse);

    RewardSummaryResponse getMyRewards();

    /**
     * Process a reward redemption request. Creates a negative‑point grant record.
     * @param userId ID of the user redeeming points
     * @param transactionId ID of the transaction where points are applied
     * @param pointsToRedeem Amount of points to redeem (must be > 0)
     */
    void processRewardRedemption(Long userId, String transactionId, int pointsToRedeem);
}
