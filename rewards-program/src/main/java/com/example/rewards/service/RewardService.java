package com.example.rewards.service;

import com.example.rewards.entity.Transaction;
import com.example.rewards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final TransactionRepository transactionRepository;

    public int calculatePoints(BigDecimal amount) {
        int points = 0;
        long dollars = amount.longValue();

        if (dollars > 100) {
            points += (dollars - 100) * 2;
            points += 50; // 1 point for each dollar between 50–100
        } else if (dollars > 50) {
            points += (dollars - 50);
        }
        return points;
    }

    public int getRewardsForCustomer(Long customerId, LocalDate start, LocalDate end) {
        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, s, e);

        return transactions.stream()
                .mapToInt(t -> calculatePoints(t.getAmount()))
                .sum();
    }
}
