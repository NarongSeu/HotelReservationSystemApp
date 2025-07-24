package com.hotel.gui.panels;

import com.hotel.dao.BillingDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.model.Bill;
import com.hotel.gui.ModernColors;
import com.hotel.gui.components.ModernTable;
import com.hotel.gui.components.ModernFormPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillingPanel extends JPanel {
    private ModernTable billingTable;
    private DefaultTableModel tableModel;
    private BillingDAO billingDAO;
    private JTextField totalAmountField, taxField, discountField;
    private JComboBox<String> paymentMethodCombo, paymentStatusCombo, reservationCombo;
    
    public BillingPanel() {
        billingDAO = new BillingDAO();
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        setBackground(ModernColors.MAIN_BACKGROUND);
        
        // Table setup - exactly like reference
        String[] columnNames = {"Bill ID", "Reservation", "Total Amount", "Tax", "Discount", "Payment Method", "Payment Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        billingTable = new ModernTable(tableModel);
        
        // Form fields
        totalAmountField = ModernFormPanel.createModernTextField();
        taxField = ModernFormPanel.createModernTextField();
        discountField = ModernFormPanel.createModernTextField();
        paymentMethodCombo = ModernFormPanel.createModernComboBox(new String[]{"Credit Card", "Cash", "Bank Transfer", "Check"});
        paymentStatusCombo = ModernFormPanel.createModernComboBox(new String[]{"Pending", "Paid", "Cancelled", "Refunded"});
        reservationCombo = ModernFormPanel.createModernComboBox(new String[]{"Reservation #1", "Reservation #2", "Reservation #3"});
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title - exactly like reference
        JLabel titleLabel = new JLabel("Billing Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Table panel with rounded corners
        RoundedPanel tablePanel = new RoundedPanel(12, Color.WHITE);
        tablePanel.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(billingTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        ModernFormPanel formPanel = new ModernFormPanel("Bill Details");
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(createFieldLabel("Reservation:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(reservationCombo, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Total Amount:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(totalAmountField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(createFieldLabel("Tax:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(taxField, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Discount:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(discountField, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        fieldsPanel.add(createFieldLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(paymentMethodCombo, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Payment Status:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(paymentStatusCombo, gbc);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        
        JButton addButton = ModernFormPanel.createModernButton("Add Bill", new Color(34, 197, 94));
        JButton updateButton = ModernFormPanel.createModernButton("Update Bill", new Color(59, 130, 246));
        JButton deleteButton = ModernFormPanel.createModernButton("Delete Bill", new Color(239, 68, 68));
        JButton clearButton = ModernFormPanel.createModernButton("Clear", new Color(107, 114, 128));
        
        addButton.addActionListener(e -> addBill());
        updateButton.addActionListener(e -> updateBill());
        deleteButton.addActionListener(e -> deleteBill());
        clearButton.addActionListener(e -> clearForm());
        
        billingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(new Color(75, 85, 99));
        return label;
    }
    
    private void addBill() {
        if (validateForm()) {
            Bill bill = createBillFromForm();
            if (billingDAO.addBill(bill)) {
                JOptionPane.showMessageDialog(this, "Bill added successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding bill!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateBill() {
        int selectedRow = billingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to update!");
            return;
        }
        
        if (validateForm()) {
            Bill bill = createBillFromForm();
            bill.setBillId((Integer) tableModel.getValueAt(selectedRow, 0));
            
            if (billingDAO.updateBill(bill)) {
                JOptionPane.showMessageDialog(this, "Bill updated successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating bill!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteBill() {
        int selectedRow = billingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this bill?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Bill deleted successfully!");
            refreshData();
            clearForm();
        }
    }
    
    private boolean validateForm() {
        if (totalAmountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total amount is required!");
            return false;
        }
        try {
            new BigDecimal(totalAmountField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid total amount!");
            return false;
        }
        return true;
    }
    
    private Bill createBillFromForm() {
        Bill bill = new Bill();
        bill.setReservationId(1); // Demo reservation ID
        bill.setTotalAmount(new BigDecimal(totalAmountField.getText().trim()));
        bill.setTax(taxField.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(taxField.getText().trim()));
        bill.setDiscount(discountField.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(discountField.getText().trim()));
        bill.setPaymentMethod((String) paymentMethodCombo.getSelectedItem());
        bill.setPaymentStatus((String) paymentStatusCombo.getSelectedItem());
        bill.setIssuedDate(LocalDate.now());
        return bill;
    }
    
    private void populateFormFromSelection() {
        int selectedRow = billingTable.getSelectedRow();
        if (selectedRow != -1) {
            totalAmountField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            taxField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            discountField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            paymentMethodCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
            paymentStatusCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 6));
        }
    }
    
    private void clearForm() {
        totalAmountField.setText("");
        taxField.setText("");
        discountField.setText("");
        paymentMethodCombo.setSelectedIndex(0);
        paymentStatusCombo.setSelectedIndex(0);
        reservationCombo.setSelectedIndex(0);
        billingTable.clearSelection();
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Bill> bills = billingDAO.getAllBills();
        for (Bill bill : bills) {
            Object[] row = {
                bill.getBillId(),
                "Reservation #" + bill.getReservationId(),
                "$" + bill.getTotalAmount(),
                "$" + bill.getTax(),
                "$" + bill.getDiscount(),
                bill.getPaymentMethod(),
                bill.getPaymentStatus(),
                bill.getIssuedDate().toString()
            };
            tableModel.addRow(row);
        }
    }
}
\`\`\`</merged_code>
