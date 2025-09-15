package com.example.rewards.service;

import com.example.rewards.dto.MonthlyRewardDTO;
import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.entity.Customer;
import com.example.rewards.entity.Transaction;
import com.example.rewards.exception.ResourceNotFoundException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public int calculatePoints(BigDecimal amount) {
        int points = 0;
        long dollars = amount.longValue();

        if (dollars > 100) {
            points += (dollars - 100) * 2;
            points += 50;
        } else if (dollars > 50) {
            points += (dollars - 50);
        }
        return points;
    }

    public RewardResponseDTO getRewardsForCustomer(Long customerId, LocalDate start, LocalDate end) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));

        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, s, e);

        int totalPoints = transactions.stream()
                .mapToInt(t -> calculatePoints(t.getAmount()))
                .sum();

        String period = start + " to " + end;

        return new RewardResponseDTO(
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail(),
                totalPoints,
                period
        );
    }

    public List<MonthlyRewardDTO> getMonthlyRewards(Long customerId, LocalDate start, LocalDate end) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));

        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, s, e);

        Map<String, Integer> monthlyPoints = new HashMap<>();

        for (Transaction t : transactions) {
            String month = t.getTransactionDate()
                    .getMonth()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            monthlyPoints.put(month,
                    monthlyPoints.getOrDefault(month, 0) + calculatePoints(t.getAmount()));
        }

        return monthlyPoints.entrySet().stream()
                .map(entry -> new MonthlyRewardDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
