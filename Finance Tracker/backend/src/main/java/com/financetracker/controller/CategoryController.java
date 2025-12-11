package com.financetracker.controller;

import com.financetracker.entity.ExpenseCategory;
import com.financetracker.repository.ExpenseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private ExpenseCategoryRepository categoryRepository;
    
    @GetMapping
    public ResponseEntity<List<ExpenseCategory>> getAllCategories() {
        List<ExpenseCategory> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }
}

