package com.example.rewards.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Response object for total rewards request. Includes basic customer info,
 * total points in range, the period, and the transactions in that range.
 */

@Data
@AllArgsConstructor
public class RewardResponseDTO {
    private String customerName;
    private String email;       
    private int totalPoints;
    private String period;
    private List<TransactionDTO> transactions;
}
