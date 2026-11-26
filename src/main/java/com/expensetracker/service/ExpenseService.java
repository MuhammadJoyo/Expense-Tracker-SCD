package com.expensetracker.service;

import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public void addExpense(Expense expense) { repository.add(expense); }
    public List<Expense> getAll() { return repository.findAll(); }
    public boolean deleteExpense(int id) { return repository.delete(id); }
    public boolean updateExpense(Expense expense) { return repository.update(expense); }

    public List<Expense> search(String keyword) {
        String q = keyword.toLowerCase(Locale.ROOT);
        return getAll().stream()
            .filter(e -> e.getTitle().toLowerCase(Locale.ROOT).contains(q)
                || e.getCategory().toLowerCase(Locale.ROOT).contains(q))
            .collect(Collectors.toList());
    }

    public List<Expense> filterByCategory(String category) {
        return getAll().stream()
            .filter(e -> e.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    public double getTotal() {
        return getAll().stream().map(Expense::getAmount)
            .reduce(0.0, Double::sum);
    }

    public Map<String, Double> totalsByCategory() {
        return getAll().stream().collect(Collectors.groupingBy(
            Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));
    }

    /** Evaluates the small Expense Query Language (EQL). */
    public List<Expense> advancedSearch(String query) {
        return getAll().stream()
            .filter(expense -> com.expensetracker.query.ExpenseQuery.matches(query, expense))
            .collect(Collectors.toList());
    }

    public List<String> expenseTitles() {
        return getAll().stream().map(Expense::getTitle)
            .collect(Collectors.toList());
    }
}
