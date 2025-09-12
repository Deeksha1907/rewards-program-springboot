package com.example.rewards.controller;

import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/customer/{id}")
    public RewardResponseDTO getRewardsForCustomer(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return rewardService.getRewardsForCustomer(id, start, end);
    }
}
