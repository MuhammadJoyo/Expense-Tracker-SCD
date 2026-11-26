package com.expensetracker;

import com.expensetracker.model.Expense;
import com.expensetracker.validation.ExpenseValidator;
import com.expensetracker.query.ExpenseQuery;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ValidationAndQueryTest {
    private Expense sample() {
        return new Expense(10, "Grocery", 1500, "FOOD", LocalDate.now());
    }

    @Test void regexAcceptsValidTitleAndRejectsInvalidTitle() {
        assertTrue(ExpenseValidator.validTitle("Monthly Grocery"));
        assertFalse(ExpenseValidator.validTitle("Invalid@Title!"));
    }

    @Test void regexValidatesAmountAndCategory() {
        assertTrue(ExpenseValidator.validAmount("1500.50"));
        assertFalse(ExpenseValidator.validAmount("-150"));
        assertTrue(ExpenseValidator.validCategory("food"));
        assertFalse(ExpenseValidator.validCategory("UNKNOWN"));
    }

    @Test void queryLanguageEvaluatesAndExpression() {
        assertTrue(ExpenseQuery.matches("amount > 1000 AND category == FOOD", sample()));
    }

    @Test void queryLanguageSupportsRecursiveParenthesesAndOr() {
        assertTrue(ExpenseQuery.matches("(category == FOOD OR category == BILLS) AND amount < 2000", sample()));
    }

    @Test void queryLanguageRejectsUnknownField() {
        assertThrows(IllegalArgumentException.class,
            () -> ExpenseQuery.matches("salary > 100", sample()));
    }
}
