package com.example.rewards.util;

import java.math.BigDecimal;

/**
 * Utility class that contains reward points calculation logic.
 */
public final class RewardPointsCalculator {

    private RewardPointsCalculator() {
        // utility class - no instances
    }

    /**
     * Calculate reward points for a given purchase amount using business rules:
     * - 2 points for every dollar spent over $100
     * - 1 point for every dollar spent between $50 and $100
     *
     * @param amount purchase amount (BigDecimal)
     * @return points earned (int)
     */
    public static int calculatePoints(BigDecimal amount) {
        int points = 0;
        long dollars = amount == null ? 0L : amount.longValue();

        if (dollars > 100) {
            points += (dollars - 100) * 2;
            points += 50; // points for the 50 dollars between 50 and 100
        } else if (dollars > 50) {
            points += (dollars - 50);
        }
        return points;
    }
}
