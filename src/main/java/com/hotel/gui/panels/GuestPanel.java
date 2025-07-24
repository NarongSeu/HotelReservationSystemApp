package com.hotel.gui.panels;

import com.hotel.dao.GuestDAO;
import com.hotel.model.Guest;
import com.hotel.gui.components.ModernTable;
import com.hotel.gui.components.ModernFormPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuestPanel extends JPanel {
    private ModernTable guestTable;
    private DefaultTableModel tableModel;
    private GuestDAO guestDAO;
    private JTextField nameField, phoneField, idField;
    private JTextArea addressArea;
    
    public GuestPanel() {
        guestDAO = new GuestDAO();
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        setBackground(new Color(243, 244, 246)); // Light gray background like reference
        
        // Table setup - exactly like reference
        String[] columnNames = {"ID", "Full Name", "Phone", "ID/Passport", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        guestTable = new ModernTable(tableModel);
        
        // Form fields
        nameField = ModernFormPanel.createModernTextField();
        phoneField = ModernFormPanel.createModernTextField();
        idField = ModernFormPanel.createModernTextField();
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setFont(new Font("Arial", Font.PLAIN, 13));
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title - exactly like reference
        JLabel titleLabel = new JLabel("Guest Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Table panel - clean white background like reference
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        JScrollPane scrollPane = new JScrollPane(guestTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        ModernFormPanel formPanel = new ModernFormPanel("Guest Details");
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(createFieldLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(nameField, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Phone:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(phoneField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(createFieldLabel("ID/Passport:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(idField, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Address:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(new JScrollPane(addressArea), gbc);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        
        JButton addButton = ModernFormPanel.createModernButton("Add Guest", new Color(34, 197, 94));
        JButton updateButton = ModernFormPanel.createModernButton("Update Guest", new Color(59, 130, 246));
        JButton deleteButton = ModernFormPanel.createModernButton("Delete Guest", new Color(239, 68, 68));
        JButton clearButton = ModernFormPanel.createModernButton("Clear", new Color(107, 114, 128));
        
        addButton.addActionListener(e -> addGuest());
        updateButton.addActionListener(e -> updateGuest());
        deleteButton.addActionListener(e -> deleteGuest());
        clearButton.addActionListener(e -> clearForm());
        
        guestTable.getSelectionModel().addListSelectionListener(e -> {
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
    
    private void addGuest() {
        if (validateForm()) {
            Guest guest = createGuestFromForm();
            if (guestDAO.addGuest(guest)) {
                JOptionPane.showMessageDialog(this, "Guest added successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding guest!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guest to update!");
            return;
        }
        
        if (validateForm()) {
            Guest guest = createGuestFromForm();
            guest.setGuestId((Integer) tableModel.getValueAt(selectedRow, 0));
            
            if (guestDAO.updateGuest(guest)) {
                JOptionPane.showMessageDialog(this, "Guest updated successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating guest!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guest to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this guest?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int guestId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (guestDAO.deleteGuest(guestId)) {
                JOptionPane.showMessageDialog(this, "Guest deleted successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting guest!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name is required!");
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone is required!");
            return false;
        }
        return true;
    }
    
    private Guest createGuestFromForm() {
        Guest guest = new Guest();
        guest.setFullName(nameField.getText().trim());
        guest.setPhone(phoneField.getText().trim());
        guest.setIdPassport(idField.getText().trim());
        guest.setAddress(addressArea.getText().trim());
        return guest;
    }
    
    private void populateFormFromSelection() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            phoneField.setText((String) tableModel.getValueAt(selectedRow, 2));
            idField.setText((String) tableModel.getValueAt(selectedRow, 3));
            addressArea.setText((String) tableModel.getValueAt(selectedRow, 4));
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        idField.setText("");
        addressArea.setText("");
        guestTable.clearSelection();
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Guest> guests = guestDAO.getAllGuests();
        for (Guest guest : guests) {
            Object[] row = {
                guest.getGuestId(),
                guest.getFullName(),
                guest.getPhone(),
                guest.getIdPassport(),
                guest.getAddress()
            };
            tableModel.addRow(row);
        }
    }
}
