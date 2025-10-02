package com.example.rewards;

import com.example.rewards.exception.InvalidDateRangeException;
import com.example.rewards.service.RewardService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RewardServiceTest {

    private final RewardService rewardService = new RewardService(null, null);

    @Test
    void testPointsBelow50() {
        int points = com.example.rewards.util.RewardPointsCalculator.calculatePoints(BigDecimal.valueOf(40));
        assertEquals(0, points, "Amount below 50 should give 0 points");
    }

    @Test
    void testPointsBetween50And100() {
        int points = com.example.rewards.util.RewardPointsCalculator.calculatePoints(BigDecimal.valueOf(80));
        assertEquals(30, points, "Amount 80 should give 30 points");
    }

    @Test
    void testPointsAbove100() {
        int points = com.example.rewards.util.RewardPointsCalculator.calculatePoints(BigDecimal.valueOf(120));
        assertEquals(90, points, "Amount 120 should give 90 points");
    }

    @Test
    void testGetRewardsForCustomerStartAfterEndThrows() {
        LocalDate start = LocalDate.parse("2025-08-01");
        LocalDate end = LocalDate.parse("2025-06-01");

        assertThrows(InvalidDateRangeException.class, () -> {
            // repos are null but validateDateRange is expected to fail before repo usage
            rewardService.getRewardsForCustomer(1L, start, end);
        });
    }

    @Test
    void testGetMonthlyRewardsStartAfterEndThrows() {
        LocalDate start = LocalDate.parse("2025-09-01");
        LocalDate end = LocalDate.parse("2025-06-01");

        assertThrows(InvalidDateRangeException.class, () -> {
            rewardService.getMonthlyRewards(1L, start, end);
        });
    }
}