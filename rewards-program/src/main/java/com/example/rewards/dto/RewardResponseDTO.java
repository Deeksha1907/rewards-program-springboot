package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RewardResponseDTO {
    private String customerName;
    private String email;       
    private int totalPoints;
    private String period;
}
