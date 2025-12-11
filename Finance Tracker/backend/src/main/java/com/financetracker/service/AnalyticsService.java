package com.financetracker.service;

import com.financetracker.dto.AnalyticsDto;
import com.financetracker.dto.CategoryExpenseDto;
import com.financetracker.dto.DailyExpenseDto;
import com.financetracker.entity.Expense;
import com.financetracker.entity.ExpenseCategory;
import com.financetracker.entity.User;
import com.financetracker.repository.ExpenseRepository;
import com.financetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public AnalyticsDto getAnalytics(LocalDate startDate, LocalDate endDate) {
        User user = getCurrentUser();
        
        // Use optimized query with indexes
        List<Expense> expenses = expenseRepository
                .findByUserAndExpenseDateBetween(user, startDate, endDate);
        
        if (expenses.isEmpty()) {
            return new AnalyticsDto(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    0L,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );
        }
        
        // Calculate total expenses
        BigDecimal totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate average daily expense
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal averageDailyExpense = totalExpenses.divide(
                BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_UP);
        
        // Expenses by category
        Map<Long, List<Expense>> expensesByCategory = expenses.stream()
                .collect(Collectors.groupingBy(e -> e.getCategory().getId()));
        
        List<CategoryExpenseDto> categoryExpenses = expensesByCategory.entrySet().stream()
                .map(entry -> {
                    List<Expense> categoryExpenseList = entry.getValue();
                    BigDecimal categoryTotal = categoryExpenseList.stream()
                            .map(Expense::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    ExpenseCategory category = categoryExpenseList.get(0).getCategory();
                    return new CategoryExpenseDto(
                            category.getId(),
                            category.getName(),
                            category.getIcon(),
                            categoryTotal,
                            (long) categoryExpenseList.size()
                    );
                })
                .sorted((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()))
                .collect(Collectors.toList());
        
        // Daily expenses
        Map<String, BigDecimal> dailyExpensesMap = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getExpenseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
        
        List<DailyExpenseDto> dailyExpenses = dailyExpensesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DailyExpenseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        
        // Monthly expenses
        Map<String, BigDecimal> monthlyExpenses = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getExpenseDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
        
        return new AnalyticsDto(
                totalExpenses,
                averageDailyExpense,
                (long) expenses.size(),
                categoryExpenses,
                dailyExpenses,
                monthlyExpenses
        );
    }
}

