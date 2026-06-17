package com.example.backend.util;

import java.util.UUID;

/**
 * Simple ID generator for reward entities.
 */
public class IdGenerator {
    /** Generates a 12‑character alphanumeric reward grant ID. */
    public static String generateRewardId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase();
    }

    /** Generates a 12‑character alphanumeric reward redemption ID. */
    public static String generateRedemptionId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase();
    }
}
