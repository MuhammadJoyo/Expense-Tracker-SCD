package com.expensetracker;
import com.expensetracker.recursion.RecursiveExpenseSearch;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class RecursiveExpenseSearchTest {
    @Test
    void findsExistingExpenseTitle() {
        assertTrue(RecursiveExpenseSearch.containsTitle(
                List.of("Groceries", "Transport", "Books"), "books"));
    }
    @Test
    void returnsFalseWhenTitleDoesNotExist() {
        assertFalse(RecursiveExpenseSearch.containsTitle(
                List.of("Groceries", "Transport"), "Rent"));
    }
    @Test
    void handlesEmptyList() {
        assertFalse(RecursiveExpenseSearch.containsTitle(
                List.of(), "Food"));
    }
}