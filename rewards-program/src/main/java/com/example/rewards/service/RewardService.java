package com.example.rewards.service;

import com.example.rewards.dto.MonthlyRewardDTO;
import com.example.rewards.dto.RewardResponseDTO;
import com.example.rewards.dto.TransactionDTO;
import com.example.rewards.entity.Customer;
import com.example.rewards.entity.Transaction;
import com.example.rewards.exception.InvalidDateRangeException;
import com.example.rewards.exception.ResourceNotFoundException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.util.RewardPointsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer that contains business logic for rewards calculation.
 */
@Service
@RequiredArgsConstructor
public class RewardService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    /**
     * Return total rewards and transaction-level details for a customer in the given date range.
     *
     * @param customerId customer id
     * @param start inclusive start date
     * @param end inclusive end date
     * @return RewardResponseDTO containing customer info, total points, period, and transaction list
     * @throws InvalidDateRangeException if start is after end
     * @throws ResourceNotFoundException if customer not found
     */
    public RewardResponseDTO getRewardsForCustomer(Long customerId, LocalDate start, LocalDate end) {
        validateDateRange(start, end);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));

        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, s, e);

        // Map transactions to TransactionDTO including points per transaction
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(t -> new TransactionDTO(t.getAmount(), t.getTransactionDate(), RewardPointsCalculator.calculatePoints(t.getAmount())))
                .collect(Collectors.toList());

        int totalPoints = transactionDTOs.stream()
                .mapToInt(TransactionDTO::getPointsEarned)
                .sum();

        String period = start + " to " + end;

        return new RewardResponseDTO(
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail(),
                totalPoints,
                period,
                transactionDTOs
        );
    }

    /**
     * Returns a combined structure with customer details, transactions, monthly reward points,
     * and total points for the given date range.
     *
     * @param customerId customer id
     * @param start inclusive start date
     * @param end inclusive end date
     * @return a map representing the full response (customer, transactions, monthly, total)
     */
    public Map<String, Object> getMonthlyRewards(Long customerId, LocalDate start, LocalDate end) {
        validateDateRange(start, end);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));

        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, s, e);

        // Build transaction DTO list
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getAmount(),
                        t.getTransactionDate(),
                        com.example.rewards.util.RewardPointsCalculator.calculatePoints(t.getAmount())))
                .collect(Collectors.toList());

        // Calculate total points
        int totalPoints = transactionDTOs.stream()
                .mapToInt(TransactionDTO::getPointsEarned)
                .sum();

        // Build monthly breakdown (using existing MonthlyRewardDTO)
        Map<YearMonth, Integer> monthlyMap = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            YearMonth ym = YearMonth.from(t.getTransactionDate());
            int pts = com.example.rewards.util.RewardPointsCalculator.calculatePoints(t.getAmount());
            monthlyMap.put(ym, monthlyMap.getOrDefault(ym, 0) + pts);
        }

        List<MonthlyRewardDTO> monthlyRewards = monthlyMap.entrySet().stream()
                .map(entry -> new MonthlyRewardDTO(
                        entry.getKey().getYear(),
                        entry.getKey().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        entry.getValue()))
                .collect(Collectors.toList());

        // Construct final response map
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("customerName", customer.getFirstName() + " " + customer.getLastName());
        response.put("email", customer.getEmail());
        response.put("period", start + " to " + end);
        response.put("totalPoints", totalPoints);
        response.put("transactions", transactionDTOs);
        response.put("monthlyRewards", monthlyRewards);

        return response;
    }

    /**
     * Validates that start <= end.
     *
     * @param start start date
     * @param end end date
     * @throws InvalidDateRangeException if start is after end
     */
    private void validateDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new InvalidDateRangeException("Start and end dates must be provided");
        }
        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("Start date must be before or equal to end date");
        }
    }
}
