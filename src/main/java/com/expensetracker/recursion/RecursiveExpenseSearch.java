package com.expensetracker.recursion;
import java.util.List;
public final class RecursiveExpenseSearch {
    private RecursiveExpenseSearch() {}
    public static boolean containsTitle(List<String> titles, String target) {
        return search(titles, target, 0);
    }
    private static boolean search(List<String> titles, String target, int index) {
        if (index >= titles.size()) {
            return false;
        }
        if (titles.get(index).equalsIgnoreCase(target)) {
            return true;
        }
        return search(titles, target, index + 1);
    }
}