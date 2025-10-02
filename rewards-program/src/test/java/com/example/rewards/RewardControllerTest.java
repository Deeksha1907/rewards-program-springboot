package com.example.rewards;

import com.example.rewards.controller.RewardController;
import com.example.rewards.dto.MonthlyRewardDTO;
import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.service.RewardService;
import com.example.rewards.exception.InvalidDateRangeException;
import com.example.rewards.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                "John Doe", "john@example.com", 100, "2025-06-01 to 2025-06-30", Collections.emptyList()
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
                new MonthlyRewardDTO(2025, "June", 90)
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

    @Test
    void testControllerHandlesInvalidDateRangeFromService() {
        Mockito.when(rewardService.getRewardsForCustomer(
                Mockito.eq(1L),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)))
                .thenThrow(new InvalidDateRangeException("Start date must be before end date"));

        assertThrows(InvalidDateRangeException.class, () -> {
            rewardController.getRewardsForCustomer(1L, LocalDate.parse("2025-09-01"), LocalDate.parse("2025-06-01"));
        });
    }

    @Test
    void testGlobalExceptionHandlerForDateParse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        DateTimeParseException ex = new DateTimeParseException("Failed to parse date", "abc", 0);
        var response = handler.handleDateParse(ex);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testGlobalExceptionHandlerForTypeMismatch() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        // create a simple MethodArgumentTypeMismatchException (name + required type)
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("abc", Integer.class, "id", null, new IllegalArgumentException());
        var response = handler.handleTypeMismatch(ex);
        assertEquals(400, response.getStatusCodeValue());
    }
}
