package org.example.moneylog.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MonthlyStatisticsResponse {
    private final long income;
    private final long expense;
    private final long balance;
    private final List<CategoryTotalResponse> byCategory;

    public MonthlyStatisticsResponse(long income, long expense, List<CategoryTotalResponse> byCategory) {
        this.income = income;
        this.expense = expense;
        this.balance = income - expense;
        this.byCategory = byCategory;
    }
}
