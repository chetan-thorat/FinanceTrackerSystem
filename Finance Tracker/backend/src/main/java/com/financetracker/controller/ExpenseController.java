package com.financetracker.controller;

import com.financetracker.dto.ExpenseDto;
import com.financetracker.dto.ExpenseRequest;
import com.financetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpenses() {
        List<ExpenseDto> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        ExpenseDto expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }
    
    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseDto expense = expenseService.createExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseDto expense = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(expense);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}

