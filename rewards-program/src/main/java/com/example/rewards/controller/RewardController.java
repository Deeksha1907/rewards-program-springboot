package com.example.rewards.controller;

import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

/**
 * REST controller exposing rewards endpoints.
 */
@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    /**
     * Get total rewards for a customer in a date range along with transaction details.
     *
     * Example: GET /api/rewards/customer/1?start=2025-06-01&end=2025-08-31
     *
     * @param id customer id
     * @param start start date (yyyy-MM-dd)
     * @param end end date (yyyy-MM-dd)
     * @return RewardResponseDTO
     */
    @GetMapping("/customer/{id}")
    public RewardResponseDTO getRewardsForCustomer(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return rewardService.getRewardsForCustomer(id, start, end);
    }

    /**
     * Get monthly rewards (year/month/points) for a customer in a date range.
     *
     * Example: GET /api/rewards/customer/1/monthly?start=2025-06-01&end=2025-08-31
     *
     * @param id customer id
     * @param start start date (yyyy-MM-dd)
     * @param end end date (yyyy-MM-dd)
     * @return list of MonthlyRewardDTO
     */
    @GetMapping("/customer/{id}/monthly")
    public Map<String, Object> getMonthlyRewardsForCustomer(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return rewardService.getMonthlyRewards(id, start, end);
    }

}
