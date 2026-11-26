import com.expensetracker.model.Expense;
import com.expensetracker.repository.FileExpenseRepository;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.validation.ExpenseValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Main extends JFrame {

    private final ExpenseService service;
    private final DefaultTableModel model;
    private final JTable table;
    private final JLabel totalValue = new JLabel("Rs. 0.00");
    private final JLabel countValue = new JLabel("0");
    private final JLabel status = new JLabel("  All expenses");

    private final Color BG = new Color(15, 19, 35);
    private final Color PANEL = new Color(25, 31, 52);
    private final Color PURPLE = new Color(130, 95, 255);
    private final Color WHITE = new Color(235, 238, 250);
    private final Color MUTED = new Color(155, 165, 190);

    public Main() {
        service = new ExpenseService(
            new FileExpenseRepository(Paths.get("data/expenses.csv"))
        );

        model = new DefaultTableModel(
            new String[]{"ID", "TITLE", "AMOUNT", "CATEGORY", "DATE"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        buildUI();
        loadData();

        setVisible(true);
    }

    private void buildUI() {
        setTitle("Expense Tracker | Personal Finance");
        setSize(1050, 680);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(18, 18));

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(25, 28, 25, 28));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel heading = new JLabel("Expense Dashboard");
        heading.setForeground(WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel subtitle = new JLabel("Manage and monitor your daily spending");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 5));
        titles.setOpaque(false);
        titles.add(heading);
        titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);

        JLabel date = new JLabel(LocalDate.now().toString());
        date.setForeground(MUTED);
        header.add(date, BorderLayout.EAST);

        // Summary cards
        JPanel cards = new JPanel(new GridLayout(1, 2, 15, 0));
        cards.setOpaque(false);

        cards.add(createCard("TOTAL EXPENSES", totalValue));
        cards.add(createCard("TOTAL TRANSACTIONS", countValue));

        JPanel upper = new JPanel(new BorderLayout(0, 20));
        upper.setOpaque(false);
        upper.add(header, BorderLayout.NORTH);
        upper.add(cards, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 10));
        toolbar.setBackground(PANEL);

        JButton add = button("+ Add Expense", PURPLE);
        JButton edit = button("Edit", new Color(48, 111, 180));
        JButton delete = button("Delete", new Color(190, 65, 85));
        JButton search = button("Search", new Color(65, 80, 110));
        JButton refresh = button("Refresh", new Color(65, 80, 110));

        JComboBox<String> filter = new JComboBox<>(new String[]{
            "ALL", "FOOD", "TRANSPORT", "EDUCATION",
            "SHOPPING", "BILLS", "HEALTH",
            "ENTERTAINMENT", "OTHER"
        });

        filter.setPreferredSize(new Dimension(145, 36));

        toolbar.add(add);
        toolbar.add(edit);
        toolbar.add(delete);
        toolbar.add(search);
        toolbar.add(refresh);
        toolbar.add(filter);

        // Table
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(WHITE);
        table.setBackground(PANEL);
        table.setGridColor(new Color(45, 53, 78));
        table.setSelectionBackground(new Color(70, 57, 120));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.getTableHeader().setBackground(new Color(35, 43, 68));
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(PANEL);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
        tablePanel.setBackground(PANEL);
        tablePanel.setBorder(new EmptyBorder(16, 16, 12, 16));

        JLabel tableTitle = new JLabel("Transaction History");
        tableTitle.setForeground(WHITE);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        // Footer
        status.setForeground(MUTED);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(toolbar, BorderLayout.NORTH);
        center.add(tablePanel, BorderLayout.CENTER);

        root.add(upper, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(status, BorderLayout.SOUTH);

        add.addActionListener(e -> addExpense());
        edit.addActionListener(e -> editExpense());
        delete.addActionListener(e -> deleteExpense());
        refresh.addActionListener(e -> loadData());

        // Smart Search: automatically chooses normal or advanced search.
        search.addActionListener(e -> performSmartSearch());

        filter.addActionListener(e -> {
            String category = filter.getSelectedItem().toString();

            if (category.equals("ALL")) {
                loadData();
            } else {
                display(service.filterByCategory(category));
                status.setText("  Filter: " + category);
            }
        });
    }

    /**
     * Opens the search dialog and automatically detects whether the user
     * entered a normal keyword or an Expense Query Language expression.
     */
    private void performSmartSearch() {
        String key = JOptionPane.showInputDialog(
            this,
            "Smart Search: enter a title, category, or condition.\n"
                + "Examples:\n"
                + "  books\n"
                + "  FOOD\n"
                + "  amount > 500\n"
                + "  category == FOOD AND amount >= 500\n"
                + "  (category == FOOD OR category == EDUCATION) AND amount >= 500",
            "Smart Search",
            JOptionPane.QUESTION_MESSAGE
        );

        if (key == null || key.isBlank()) {
            return;
        }

        key = key.trim();

        try {
            // Backward compatibility: users may still type the old prefix.
            if (key.regionMatches(true, 0, "query:", 0, 6)) {
                key = key.substring(6).trim();
            }

            if (isAdvancedQuery(key)) {
                List<Expense> results = service.advancedSearch(key);
                display(results);
                status.setText("  Advanced query: " + key
                    + " | Results: " + results.size());
            } else {
                List<Expense> results = service.search(key);
                display(results);
                status.setText("  Search results: " + key
                    + " | Results: " + results.size());
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not process the search.\n"
                    + "Please check the query syntax.\n\n"
                    + ex.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Detects EQL expressions by looking for a supported field followed by
     * a comparison operator. Plain words such as "books" and "FOOD" remain
     * normal title/category searches.
     */
    private boolean isAdvancedQuery(String input) {
        String query = input.trim();

        return query.matches(
            "(?is).*\\b(amount|category|title)\\s*"
                + "(==|!=|>=|<=|>|<|contains)\\s*.+"
        );
    }

    private JPanel createCard(String title, JLabel value) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 10));
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(17, 20, 17, 20));

        JLabel label = new JLabel(title);
        label.setForeground(MUTED);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));

        value.setForeground(new Color(170, 145, 255));
        value.setFont(new Font("Segoe UI", Font.BOLD, 25));

        card.add(label);
        card.add(value);
        return card;
    }

    private JButton button(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(125, 36));
        return b;
    }

    private void addExpense() {
        JTextField title = new JTextField();
        JTextField amount = new JTextField();

        String[] categories = {
            "FOOD", "TRANSPORT", "EDUCATION", "SHOPPING",
            "BILLS", "HEALTH", "ENTERTAINMENT", "OTHER"
        };

        JComboBox<String> category = new JComboBox<>(categories);

        Object[] fields = {
            "Expense Title:", title,
            "Amount (Rs.):", amount,
            "Category:", category
        };

        if (JOptionPane.showConfirmDialog(
            this, fields, "Add New Expense",
            JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            ExpenseValidator.validate(
                title.getText(),
                amount.getText(),
                category.getSelectedItem().toString()
            );

            int id = service.getAll().stream()
                .mapToInt(Expense::getId)
                .max()
                .orElse(0) + 1;

            Expense expense = new Expense(
                id,
                title.getText().trim(),
                Double.parseDouble(amount.getText()),
                category.getSelectedItem().toString(),
                LocalDate.now()
            );

            service.addExpense(expense);
            loadData();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void editExpense() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a transaction.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Expense old = service.getAll().stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);

        if (old == null) {
            return;
        }

        JTextField title = new JTextField(old.getTitle());
        JTextField amount = new JTextField(String.valueOf(old.getAmount()));
        JTextField category = new JTextField(old.getCategory());

        Object[] fields = {
            "Title:", title,
            "Amount:", amount,
            "Category:", category
        };

        if (JOptionPane.showConfirmDialog(
            this, fields, "Edit Expense",
            JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            ExpenseValidator.validate(
                title.getText(),
                amount.getText(),
                category.getText()
            );

            Expense updated = new Expense(
                id,
                title.getText().trim(),
                Double.parseDouble(amount.getText()),
                category.getText().trim().toUpperCase(),
                old.getDate()
            );

            if (service.updateExpense(updated)) {
                loadData();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteExpense() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a transaction.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        if (JOptionPane.showConfirmDialog(
            this, "Delete this expense?",
            "Confirm", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {
            service.deleteExpense(id);
            loadData();
        }
    }

    private void loadData() {
        display(service.getAll());
        status.setText("  All expenses");
    }

    private void display(List<Expense> expenses) {
        model.setRowCount(0);

        for (Expense e : expenses) {
            model.addRow(new Object[]{
                e.getId(),
                e.getTitle(),
                String.format("%.2f", e.getAmount()),
                e.getCategory(),
                e.getDate()
            });
        }

        // Keep dashboard totals based on all stored expenses, not only
        // the currently displayed search results.
        totalValue.setText(String.format("Rs. %.2f", service.getTotal()));
        countValue.setText(String.valueOf(service.getAll().size()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
