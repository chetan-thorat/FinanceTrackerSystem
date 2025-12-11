package com.financetracker.repository;

import com.financetracker.entity.Expense;
import com.financetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Optimized queries using indexes
    List<Expense> findByUserOrderByExpenseDateDesc(User user);
    
    List<Expense> findByUserAndExpenseDateBetween(
        User user, 
        LocalDate startDate, 
        LocalDate endDate
    );
    
    List<Expense> findByUserAndCategoryId(
        User user, 
        Long categoryId
    );
    
    List<Expense> findByUserAndExpenseDateBetweenAndCategoryId(
        User user, 
        LocalDate startDate, 
        LocalDate endDate, 
        Long categoryId
    );
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate")
    Double getTotalExpensesByUserAndDateRange(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT e.category.id, SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate GROUP BY e.category.id")
    List<Object[]> getExpensesByCategory(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

