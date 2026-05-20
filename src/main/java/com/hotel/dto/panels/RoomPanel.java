package com.hotel.dto.panels;

import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;
import com.hotel.dto.ModernColors;
import com.hotel.dto.components.ModernTable;
import com.hotel.dto.components.ModernFormPanel;
import com.hotel.dto.components.RoundedPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class RoomPanel extends JPanel {
    private ModernTable roomTable;
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
        setBackground(ModernColors.MAIN_BACKGROUND);
        
        // Table setup - exactly like reference
        String[] columnNames = {"ID", "Room Number", "Room Type", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new ModernTable(tableModel);
        
        // Form fields
        roomNumberField = ModernFormPanel.createModernTextField();
        roomTypeField = ModernFormPanel.createModernTextField();
        priceField = ModernFormPanel.createModernTextField();
        statusComboBox = ModernFormPanel.createModernComboBox(new String[]{"Available", "Occupied", "Maintenance", "Out of Order"});
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title - exactly like reference
        JLabel titleLabel = new JLabel("Room Management");
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

        JScrollPane scrollPane = new JScrollPane(roomTable);
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
        ModernFormPanel formPanel = new ModernFormPanel("Room Details");
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(createFieldLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(roomNumberField, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Room Type:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(roomTypeField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(createFieldLabel("Price:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(priceField, gbc);
        
        gbc.gridx = 2;
        fieldsPanel.add(createFieldLabel("Status:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(statusComboBox, gbc);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        
        JButton addButton = ModernFormPanel.createModernButton("Add Room", new Color(34, 197, 94));
        JButton updateButton = ModernFormPanel.createModernButton("Update Room", new Color(59, 130, 246));
        JButton deleteButton = ModernFormPanel.createModernButton("Delete Room", new Color(239, 68, 68));
        JButton clearButton = ModernFormPanel.createModernButton("Clear", new Color(107, 114, 128));
        
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        clearButton.addActionListener(e -> clearForm());
        
        roomTable.getSelectionModel().addListSelectionListener(e -> {
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
        
        // Fix price validation - handle the $ symbol
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Price is required!");
            return false;
        }
        
        try {
            // Remove $ symbol if present
            if (priceText.startsWith("$")) {
                priceText = priceText.substring(1);
            }
            // Remove any commas
            priceText = priceText.replace(",", "");
            
            BigDecimal price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price (numbers only)!");
            return false;
        }
        return true;
    }
    
    private Room createRoomFromForm() {
        Room room = new Room();
        room.setRoomNumber(roomNumberField.getText().trim());
        room.setRoomType(roomTypeField.getText().trim());
        
        // Handle price with $ symbol
        String priceText = priceField.getText().trim();
        if (priceText.startsWith("$")) {
            priceText = priceText.substring(1);
        }
        priceText = priceText.replace(",", "");
        
        room.setPrice(new BigDecimal(priceText));
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
                "$" + room.getPrice(),
                room.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}
