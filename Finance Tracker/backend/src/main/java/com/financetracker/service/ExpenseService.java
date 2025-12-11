package com.financetracker.service;

import com.financetracker.dto.ExpenseDto;
import com.financetracker.dto.ExpenseRequest;
import com.financetracker.entity.Expense;
import com.financetracker.entity.ExpenseCategory;
import com.financetracker.entity.User;
import com.financetracker.repository.ExpenseCategoryRepository;
import com.financetracker.repository.ExpenseRepository;
import com.financetracker.repository.UserRepository;
import com.financetracker.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ExpenseCategoryRepository categoryRepository;
    
    @Autowired
    private EncryptionUtil encryptionUtil;
    
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Transactional
    public ExpenseDto createExpense(ExpenseRequest request) {
        User user = getCurrentUser();
        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        Expense expense = new Expense();
        expense.setUser(user);
        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        
        // Encrypt sensitive payment method data
        if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
            expense.setPaymentMethod(encryptionUtil.encryptPaymentMethod(request.getPaymentMethod()));
        }
        
        expense.setLocation(request.getLocation());
        expense.setNotes(request.getNotes());
        
        expense = expenseRepository.save(expense);
        return convertToDto(expense);
    }
    
    @Transactional
    public ExpenseDto updateExpense(Long id, ExpenseRequest request) {
        User user = getCurrentUser();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this expense");
        }
        
        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        
        // Encrypt sensitive payment method data
        if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
            expense.setPaymentMethod(encryptionUtil.encryptPaymentMethod(request.getPaymentMethod()));
        }
        
        expense.setLocation(request.getLocation());
        expense.setNotes(request.getNotes());
        
        expense = expenseRepository.save(expense);
        return convertToDto(expense);
    }
    
    @Transactional
    public void deleteExpense(Long id) {
        User user = getCurrentUser();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        
        expenseRepository.delete(expense);
    }
    
    public List<ExpenseDto> getAllExpenses() {
        User user = getCurrentUser();
        return expenseRepository.findByUserOrderByExpenseDateDesc(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public ExpenseDto getExpenseById(Long id) {
        User user = getCurrentUser();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to access this expense");
        }
        
        return convertToDto(expense);
    }
    
    private ExpenseDto convertToDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setCategoryId(expense.getCategory().getId());
        dto.setCategoryName(expense.getCategory().getName());
        dto.setCategoryIcon(expense.getCategory().getIcon());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setExpenseDate(expense.getExpenseDate());
        
        // Decrypt payment method for display
        if (expense.getPaymentMethod() != null && !expense.getPaymentMethod().isEmpty()) {
            try {
                dto.setPaymentMethod(encryptionUtil.decryptPaymentMethod(expense.getPaymentMethod()));
            } catch (Exception e) {
                dto.setPaymentMethod(expense.getPaymentMethod()); // Fallback if decryption fails
            }
        }
        
        dto.setLocation(expense.getLocation());
        dto.setNotes(expense.getNotes());
        dto.setCreatedAt(expense.getCreatedAt().toLocalDate());
        return dto;
    }
}

