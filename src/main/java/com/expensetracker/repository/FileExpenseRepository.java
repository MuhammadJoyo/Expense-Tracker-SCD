package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Base64;

public class FileExpenseRepository implements ExpenseRepository {
    private final Path file;

    public FileExpenseRepository(Path file) {
        this.file = file;
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            if (!Files.exists(file)) Files.createFile(file);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize expense storage", e);
        }
    }

    @Override
    public synchronized void add(Expense expense) {
        List<Expense> all = findAll();
        if (all.stream().anyMatch(e -> e.getId() == expense.getId()))
            throw new IllegalArgumentException("Expense ID already exists");
        all.add(expense);
        save(all);
    }

    @Override
    public synchronized List<Expense> findAll() {
        List<Expense> result = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                result.add(new Expense(Integer.parseInt(p[0]),
                    decode(p[1]), Double.parseDouble(p[2]),
                    decode(p[3]), LocalDate.parse(p[4])));
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read expense storage", e);
        }
    }

    @Override
    public synchronized Optional<Expense> findById(int id) {
        return findAll().stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public synchronized boolean delete(int id) {
        List<Expense> all = findAll();
        boolean removed = all.removeIf(e -> e.getId() == id);
        if (removed) save(all);
        return removed;
    }

    @Override
    public synchronized boolean update(Expense expense) {
        List<Expense> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == expense.getId()) {
                all.set(i, expense);
                save(all);
                return true;
            }
        }
        return false;
    }

    private void save(List<Expense> expenses) {
        List<String> lines = new ArrayList<>();
        for (Expense e : expenses) {
            lines.add(e.getId() + "|" + encode(e.getTitle()) + "|" +
                e.getAmount() + "|" + encode(e.getCategory()) + "|" + e.getDate());
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save expenses", e);
        }
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
