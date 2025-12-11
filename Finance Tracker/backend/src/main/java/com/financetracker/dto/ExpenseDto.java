package com.financetracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseDto {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
    private String paymentMethod;
    private String location;
    private String notes;
    private LocalDate createdAt;
}

