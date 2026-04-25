import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrackerApp extends JFrame {
    JComboBox<String> cbCategory;
    JTextField tfDate;
    JTextField tfAmount;
    List<Expense> expenseList = new ArrayList<>();
    DefaultTableModel tableModel;
    JLabel lblTotal;
    JTable table;

    public TrackerApp() {
        setTitle("Expense Tracker");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel with input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        cbCategory = new JComboBox<>(new String[]{"Food", "Transport", "Entertainment", "Other"});
        tfDate = new JTextField("2026-04-10"); // default format yyyy-MM-dd
        tfAmount = new JTextField();

        JButton btnAdd = new JButton("Add Expense");
        btnAdd.addActionListener((ActionEvent e) -> addExpense());

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(cbCategory);
        inputPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        inputPanel.add(tfDate);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(tfAmount);
        inputPanel.add(new JLabel(""));
        inputPanel.add(btnAdd);

        add(inputPanel, BorderLayout.NORTH);

        // Table for expenses
        tableModel = new DefaultTableModel(new Object[]{"Category", "Amount", "Date"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel with total + edit/delete buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total: 0.0");
        bottomPanel.add(lblTotal, BorderLayout.WEST);

        JPanel btnPanel = new JPanel();
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");

        btnEdit.addActionListener(e -> editExpense());
        btnDelete.addActionListener(e -> deleteExpense());

        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addExpense() {
        String category = cbCategory.getSelectedItem().toString();
        String date = tfDate.getText().trim();
        double amount;

        try {
            amount = Double.parseDouble(tfAmount.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Expense exp = new Expense(category, amount, date);
        expenseList.add(exp);
        tableModel.addRow(new Object[]{category, amount, date});
        tfAmount.setText("");
        updateTotal();
    }

    private void editExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String category = cbCategory.getSelectedItem().toString();
        String date = tfDate.getText().trim();
        double amount;

        try {
            amount = Double.parseDouble(tfAmount.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update expense in list
        Expense exp = expenseList.get(selectedRow);
        exp.setCategory(category);
        exp.setAmount(amount);
        exp.setDate(date);

        // Update table
        tableModel.setValueAt(category, selectedRow, 0);
        tableModel.setValueAt(amount, selectedRow, 1);
        tableModel.setValueAt(date, selectedRow, 2);

        updateTotal();
    }

    private void deleteExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        expenseList.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        updateTotal();
    }

    private void updateTotal() {
        double total = expenseList.stream().mapToDouble(Expense::getAmount).sum();
        lblTotal.setText("Total: " + total);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrackerApp app = new TrackerApp();
            app.setVisible(true);
        });
    }
}

// Expense class with setters
class Expense {
    private String category;
    private double amount;
    private String date;

    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
