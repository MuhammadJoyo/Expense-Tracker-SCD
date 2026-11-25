package com.expensetracker;
import com.expensetracker.model.Expense;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
class ExpenseTest {
    @Test
    void validExpenseCreation() {
        Expense e = new Expense(1, "Lunch", 500, "FOOD", LocalDate.now());
        assertEquals("Lunch", e.getTitle());
        assertEquals(500, e.getAmount());
    }
    @Test
    void rejectNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
            () -> new Expense(2, "Lunch", -100, "FOOD", LocalDate.now()));
    }
    @Test
    void rejectEmptyTitle() {
        assertThrows(IllegalArgumentException.class,
            () -> new Expense(3, "", 100, "FOOD", LocalDate.now()));
    }
    @Test
    void sameIdMeansEqualExpenses() {
        Expense a = new Expense(5, "Lunch", 500, "FOOD", LocalDate.now());
        Expense b = new Expense(5, "Dinner", 800, "FOOD", LocalDate.now());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
