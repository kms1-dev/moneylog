package org.example.moneylog.service;

import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.CategoryType;
import org.example.moneylog.dto.CategoryTotalResponse;
import org.example.moneylog.dto.MonthlyStatisticsResponse;
import org.example.moneylog.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TransactionRepository transactionRepository;

    public MonthlyStatisticsResponse getMonthlyStatistics(Long userId, YearMonth yearMonth) {
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();

        long income = transactionRepository.sumAmount(userId, CategoryType.INCOME, from, to);
        long expense = transactionRepository.sumAmount(userId, CategoryType.EXPENSE, from, to);

        var byCategory = transactionRepository.sumByCategory(userId, CategoryType.EXPENSE, from, to).stream()
                .map(row -> new CategoryTotalResponse(row.getCategoryName(), row.getTotal()))
                .toList();

        return new MonthlyStatisticsResponse(income, expense, byCategory);
    }
}
