package com.hotel.gui.panels;

import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class RoomPanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomDAO roomDAO;
    private JTextField roomNumberField, roomTypeField, priceField;
    private JComboBox<String> statusComboBox;
    
    public RoomPanel() {
        roomDAO = new RoomDAO();
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Room Number", "Room Type", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Form fields
        roomNumberField = new JTextField(15);
        roomTypeField = new JTextField(15);
        priceField = new JTextField(15);
        statusComboBox = new JComboBox<>(new String[]{"Available", "Occupied", "Maintenance", "Out of Order"});
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Room Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with form and buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Room Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Room Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roomNumberField, gbc);
        
        // Room Type
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 3;
        formPanel.add(roomTypeField, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);
        
        // Status
        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        formPanel.add(statusComboBox, gbc);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton addButton = new JButton("Add Room");
        JButton updateButton = new JButton("Update Room");
        JButton deleteButton = new JButton("Delete Room");
        JButton clearButton = new JButton("Clear");
        
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        clearButton.addActionListener(e -> clearForm());
        
        // Add selection listener to populate form
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        return buttonPanel;
    }
    
    private void addRoom() {
        if (validateForm()) {
            Room room = createRoomFromForm();
            if (roomDAO.addRoom(room)) {
                JOptionPane.showMessageDialog(this, "Room added successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding room!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to update!");
            return;
        }
        
        if (validateForm()) {
            Room room = createRoomFromForm();
            room.setRoomId((Integer) tableModel.getValueAt(selectedRow, 0));
            
            if (roomDAO.updateRoom(room)) {
                JOptionPane.showMessageDialog(this, "Room updated successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating room!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this room?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int roomId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (roomDAO.deleteRoom(roomId)) {
                JOptionPane.showMessageDialog(this, "Room deleted successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting room!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateForm() {
        if (roomNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room number is required!");
            return false;
        }
        if (roomTypeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room type is required!");
            return false;
        }
        try {
            new BigDecimal(priceField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price!");
            return false;
        }
        return true;
    }
    
    private Room createRoomFromForm() {
        Room room = new Room();
        room.setRoomNumber(roomNumberField.getText().trim());
        room.setRoomType(roomTypeField.getText().trim());
        room.setPrice(new BigDecimal(priceField.getText().trim()));
        room.setStatus((String) statusComboBox.getSelectedItem());
        return room;
    }
    
    private void populateFormFromSelection() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            roomNumberField.setText((String) tableModel.getValueAt(selectedRow, 1));
            roomTypeField.setText((String) tableModel.getValueAt(selectedRow, 2));
            priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
        }
    }
    
    private void clearForm() {
        roomNumberField.setText("");
        roomTypeField.setText("");
        priceField.setText("");
        statusComboBox.setSelectedIndex(0);
        roomTable.clearSelection();
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room room : rooms) {
            Object[] row = {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPrice(),
                room.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}
