package com.example.backend.util;

import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionLog;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Utility class containing business rules for reward eligibility and point calculation.
 */
public class RewardRules {
    private static final Logger logger = LoggerFactory.getLogger(RewardRules.class);
    private static final BigDecimal THRESHOLD_AMOUNT = BigDecimal.valueOf(100); // ₹100 per point

    /**
     * Determines if a transfer is eligible for reward points.
     * Eligibility criteria:
     *   - Transaction status is SUCCESS
     *   - Transfer amount is at least the threshold (₹100)
     *   - Sender and receiver are different users (no self‑transfer)
     */
    public static boolean isEligible(TransactionLog transaction, Account fromAccount, Account toAccount) {
        if (transaction == null || fromAccount == null || toAccount == null) {
            return false;
        }
        if (transaction.getStatus() == null) {
            return false;
        }
        // Only allow SUCCESS status for reward eligibility
        String statusName = transaction.getStatus().name();
        if (!statusName.equalsIgnoreCase("SUCCESS")) {
            logger.debug("Reward not eligible due to status: {}", statusName);
            return false;
        }
        // Amount threshold
        if (transaction.getAmount() == null) {
            return false;
        }
        if (BigDecimal.valueOf(transaction.getAmount()).compareTo(THRESHOLD_AMOUNT) < 0) {
            return false;
        }
        // No self‑transfer (different accounts)
        return !fromAccount.getId().equals(toAccount.getId());
    }

    /**
     * Calculates reward points based on the transfer amount and eligibility.
     * Points = floor(amount / 100) when eligible, otherwise 0.
     */
    public static int calculatePoints(BigDecimal amount, boolean eligible) {
        if (!eligible || amount == null) {
            return 0;
        }
        return amount.divide(THRESHOLD_AMOUNT, 0, RoundingMode.FLOOR).intValue();
    }
}
