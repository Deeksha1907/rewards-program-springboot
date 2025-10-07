package com.example.rewards;

import com.example.rewards.controller.RewardController;
import com.example.rewards.dto.MonthlyRewardDTO;
import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.dto.TransactionDTO;
import com.example.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RewardControllerTest {

    private RewardService rewardService;
    private RewardController rewardController;

    @BeforeEach
    void setUp() {
        
        rewardService = Mockito.mock(RewardService.class);
        rewardController = new RewardController(rewardService);
    }

    /**
     * Test: Get total rewards for a customer (normal case)
     */
    @Test
    void testGetRewardsForCustomer() {
      
        RewardResponseDTO mockResponse = new RewardResponseDTO(
                "John Doe",
                "john@example.com",
                100,
                "2025-06-01 to 2025-06-30",
                Collections.emptyList()
        );
    
        Mockito.when(rewardService.getRewardsForCustomer(
                Mockito.eq(1L),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class))
        ).thenReturn(mockResponse);

        RewardResponseDTO result = rewardController.getRewardsForCustomer(
                1L, LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-30")
        );

        assertEquals("John Doe", result.getCustomerName());
        assertEquals(100, result.getTotalPoints());
        assertEquals("2025-06-01 to 2025-06-30", result.getPeriod());
    }

    /**
     * Test: Get monthly rewards for customer (map-based response)
     */
    @Test
    void testGetMonthlyRewardsForCustomer() {
        List<TransactionDTO> transactions = Arrays.asList(
                new TransactionDTO(BigDecimal.valueOf(120), LocalDateTime.parse("2025-06-15T10:30:00"), 90),
                new TransactionDTO(BigDecimal.valueOf(80), LocalDateTime.parse("2025-07-10T09:15:00"), 30)
        );

        List<MonthlyRewardDTO> monthlyRewards = Arrays.asList(
                new MonthlyRewardDTO(2025, "June", 90),
                new MonthlyRewardDTO(2025, "July", 30)
        );

        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("customerName", "John Doe");
        mockResponse.put("email", "john@example.com");
        mockResponse.put("period", "2025-06-01 to 2025-07-31");
        mockResponse.put("totalPoints", 120);
        mockResponse.put("transactions", transactions);
        mockResponse.put("monthlyRewards", monthlyRewards);

        Mockito.when(rewardService.getMonthlyRewards(
                Mockito.eq(1L),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class))
        ).thenReturn(mockResponse);

        Map<String, Object> result = rewardController.getMonthlyRewardsForCustomer(
                1L, LocalDate.parse("2025-06-01"), LocalDate.parse("2025-07-31")
        );

        assertEquals("John Doe", result.get("customerName"));
        assertEquals("john@example.com", result.get("email"));
        assertEquals("2025-06-01 to 2025-07-31", result.get("period"));
        assertEquals(120, result.get("totalPoints"));


        List<TransactionDTO> txList = (List<TransactionDTO>) result.get("transactions");
        assertEquals(2, txList.size());
        assertEquals(90, txList.get(0).getPointsEarned());

        List<MonthlyRewardDTO> monthlyList = (List<MonthlyRewardDTO>) result.get("monthlyRewards");
        assertEquals(2, monthlyList.size());
        assertEquals("June", monthlyList.get(0).getMonth());
        assertTrue(monthlyList.get(1).getPoints() > 0);
    }
}
