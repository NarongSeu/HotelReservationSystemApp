package com.hotel.gui.panels;

import com.hotel.dao.GuestDAO;
import com.hotel.model.Guest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuestPanel extends JPanel {
    private JTable guestTable;
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
        String[] columnNames = {"ID", "Full Name", "Phone", "ID/Passport", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        guestTable = new JTable(tableModel);
        
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        idField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Guest Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(guestTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Guest Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("ID/Passport:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(addressArea), gbc);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Guest");
        JButton updateButton = new JButton("Update Guest");
        JButton deleteButton = new JButton("Delete Guest");
        JButton clearButton = new JButton("Clear");
        
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
        
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
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
