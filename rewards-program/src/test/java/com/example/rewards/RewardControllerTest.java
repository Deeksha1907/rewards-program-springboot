package com.example.rewards;

import com.example.rewards.controller.RewardController;
import com.example.rewards.dto.MonthlyRewardDTO;
import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardControllerTest {

    private RewardService rewardService;
    private RewardController rewardController;

    @BeforeEach
    void setUp() {
       
        rewardService = Mockito.mock(RewardService.class);
      
        rewardController = new RewardController(rewardService);
    }

    @Test
    void testGetRewardsForCustomer() {
        RewardResponseDTO response = new RewardResponseDTO(
                "John Doe", "john@example.com", 100, "2025-06-01 to 2025-06-30"
        );

      
        Mockito.when(rewardService.getRewardsForCustomer(
                Mockito.eq(1L),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)))
                .thenReturn(response);

      
        RewardResponseDTO result = rewardController.getRewardsForCustomer(
                1L, LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-30")
        );

        
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(100, result.getTotalPoints());
    }

    @Test
    void testGetMonthlyRewardsForCustomer() {
        List<MonthlyRewardDTO> monthlyRewards = Collections.singletonList(
                new MonthlyRewardDTO("June", 90)
        );

       
        Mockito.when(rewardService.getMonthlyRewards(
                Mockito.eq(1L),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)))
                .thenReturn(monthlyRewards);

        
        List<MonthlyRewardDTO> result = rewardController.getMonthlyRewardsForCustomer(
                1L, LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-30")
        );

        
        assertEquals(1, result.size());
        assertEquals("June", result.get(0).getMonth());
        assertEquals(90, result.get(0).getPoints());
    }
}
