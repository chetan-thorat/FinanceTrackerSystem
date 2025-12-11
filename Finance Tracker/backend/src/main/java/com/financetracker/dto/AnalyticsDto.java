package com.financetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class AnalyticsDto {
    private BigDecimal totalExpenses;
    private BigDecimal averageDailyExpense;
    private Long totalTransactions;
    private List<CategoryExpenseDto> expensesByCategory;
    private List<DailyExpenseDto> dailyExpenses;
    private Map<String, BigDecimal> monthlyExpenses;
}

@Data
@AllArgsConstructor
public class CategoryExpenseDto {
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private BigDecimal totalAmount;
    private Long transactionCount;
}

@Data
@AllArgsConstructor
public class DailyExpenseDto {
    private String date;
    private BigDecimal amount;
}

