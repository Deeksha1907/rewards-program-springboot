package com.example.rewards;

import com.example.rewards.service.RewardService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardServiceTest {

    private final RewardService rewardService = new RewardService(null, null);
    

    @Test
    void testPointsBelow50() {
        int points = rewardService.calculatePoints(BigDecimal.valueOf(40));
        assertEquals(0, points, "Amount below 50 should give 0 points");
    }

    @Test
    void testPointsBetween50And100() {
        int points = rewardService.calculatePoints(BigDecimal.valueOf(80));
        assertEquals(30, points, "Amount 80 should give 30 points");
    }

    @Test
    void testPointsAbove100() {
        int points = rewardService.calculatePoints(BigDecimal.valueOf(120));
        assertEquals(90, points, "Amount 120 should give 90 points");
    }
}
