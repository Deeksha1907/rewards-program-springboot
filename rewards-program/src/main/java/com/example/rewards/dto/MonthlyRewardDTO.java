package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * DTO representing monthly reward points in a cleaner format.
 */

@Data
@AllArgsConstructor
public class MonthlyRewardDTO {
	private int year;
    private String month;
    private int points;
}
