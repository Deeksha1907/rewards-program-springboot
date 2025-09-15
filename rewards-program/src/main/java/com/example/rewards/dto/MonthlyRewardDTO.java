package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRewardDTO {
    private String month;
    private int points;
}
