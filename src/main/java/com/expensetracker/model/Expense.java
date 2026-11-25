package com.expensetracker.model;

import java.time.LocalDate;
import java.util.Objects;

public final class Expense {
    private final int id;
    private final String title;
    private final double amount;
    private final String category;
    private final LocalDate date;

    public Expense(int id, String title, double amount, String category, LocalDate date) {
        if (id <= 0 || title == null || title.isBlank() || !Double.isFinite(amount) || amount <= 0 || category == null || category.isBlank())
            throw new IllegalArgumentException("Invalid expense data");
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = Objects.requireNonNull(date);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Expense)) return false;
        return id == ((Expense) obj).id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
