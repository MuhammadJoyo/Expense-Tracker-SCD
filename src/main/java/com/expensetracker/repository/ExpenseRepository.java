package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    void add(Expense expense);
    List<Expense> findAll();
    Optional<Expense> findById(int id);
    boolean delete(int id);
    boolean update(Expense expense);
}
