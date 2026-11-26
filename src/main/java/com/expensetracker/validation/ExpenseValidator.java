package com.expensetracker.validation;
import java.util.Set;
import java.util.regex.Pattern;
/** Regex-based validation of expense form values. */
public final class ExpenseValidator {
    private static final Pattern TITLE = Pattern.compile("[\\p{L}\\p{N}][\\p{L}\\p{N} .,'()_-]{0,59}");
    private static final Pattern AMOUNT = Pattern.compile("[0-9]{1,9}(\\.[0-9]{1,2})?");
    private static final Set<String> CATEGORIES = Set.of("FOOD","TRANSPORT","EDUCATION","SHOPPING","BILLS","HEALTH","ENTERTAINMENT","OTHER");
    private ExpenseValidator() {}
    public static boolean validTitle(String s) { return s != null && TITLE.matcher(s.trim()).matches(); }
    public static boolean validAmount(String s) {
        if (s == null || !AMOUNT.matcher(s.trim()).matches()) return false;
        try { return Double.parseDouble(s.trim()) > 0 && Double.isFinite(Double.parseDouble(s.trim())); }
        catch (NumberFormatException ex) { return false; }
    }
    public static boolean validCategory(String s) { return s != null && CATEGORIES.contains(s.trim().toUpperCase()); }
    public static void validate(String title, String amount, String category) {
        if (!validTitle(title)) throw new IllegalArgumentException("Enter a valid title (1-60 letters, numbers or basic punctuation).");
        if (!validAmount(amount)) throw new IllegalArgumentException("Amount must be positive with up to 2 decimal places.");
        if (!validCategory(category)) throw new IllegalArgumentException("Choose a valid expense category.");
    }
}
