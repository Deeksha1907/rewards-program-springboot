package com.example.rewards.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * DTO representing a transaction returned in the customer rewards response.
 */

@Data
@AllArgsConstructor
public class TransactionDTO {
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private int pointsEarned;
}
