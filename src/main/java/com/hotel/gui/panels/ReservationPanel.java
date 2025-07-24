package com.hotel.gui.panels;

import com.hotel.dao.ReservationDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Reservation;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.gui.components.ModernTable;
import com.hotel.gui.components.ModernFormPanel;
import com.hotel.gui.components.RoundedPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationPanel extends JPanel {
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;
    
    private JComboBox<Guest> guestComboBox;
    private JComboBox<Room> roomComboBox;
    private JTextField checkInField, checkOutField;
    private JComboBox<String> statusComboBox;
    
    public ReservationPanel() {
        reservationDAO = new ReservationDAO();
        guestDAO = new GuestDAO();
        roomDAO = new RoomDAO();
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        String[] columnNames = {"ID", "Guest", "Room", "Check-In", "Check-Out", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(tableModel);
        
        guestComboBox = new JComboBox<>();
        roomComboBox = new JComboBox<>();
        checkInField = new JTextField(15);
        checkOutField = new JTextField(15);
        statusComboBox = new JComboBox<>(new String[]{"Confirmed", "Checked-In", "Checked-Out", "Cancelled"});
        
        // Set default dates
        checkInField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        checkOutField.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        loadComboBoxData();
    }
    
    private void loadComboBoxData() {
        // Load guests
        guestComboBox.removeAllItems();
        List<Guest> guests = guestDAO.getAllGuests();
        for (Guest guest : guests) {
            guestComboBox.addItem(guest);
        }
        
        // Load available rooms
        roomComboBox.removeAllItems();
        List<Room> rooms = roomDAO.getAvailableRooms();
        for (Room room : rooms) {
            roomComboBox.addItem(room);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(new Color(248, 250, 252)); // Match main background
        
        // Title with better spacing
        JLabel titleLabel = new JLabel("Reservation Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content with proper spacing
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Table panel with rounded corners
        RoundedPanel tablePanel = new RoundedPanel(12, Color.WHITE);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(reservationTable);
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
    
    private JPanel createBottomPanel() {
        return createFormPanel();
    }

    private JPanel createFormPanel() {
        ModernFormPanel formPanel = new ModernFormPanel("Reservation Details");
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        
        // Row 1 - Guest and Room
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(createFieldLabel("Guest:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        guestComboBox.setPreferredSize(new Dimension(250, 35));
        fieldsPanel.add(guestComboBox, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(createFieldLabel("Room:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        roomComboBox.setPreferredSize(new Dimension(250, 35));
        fieldsPanel.add(roomComboBox, gbc);
        
        // Row 2 - Check-in and Check-out dates
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(createFieldLabel("Check-In (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        checkInField.setPreferredSize(new Dimension(250, 35));
        fieldsPanel.add(checkInField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(createFieldLabel("Check-Out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        checkOutField.setPreferredSize(new Dimension(250, 35));
        fieldsPanel.add(checkOutField, gbc);
        
        // Row 3 - Status (centered)
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(createFieldLabel("Status:"), gbc);
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.WEST;
        statusComboBox.setPreferredSize(new Dimension(250, 35));
        fieldsPanel.add(statusComboBox, gbc);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Enhanced buttons with better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton addButton = ModernFormPanel.createModernButton("Add Reservation", new Color(34, 197, 94));
        JButton updateButton = ModernFormPanel.createModernButton("Update Reservation", new Color(59, 130, 246));
        JButton deleteButton = ModernFormPanel.createModernButton("Delete Reservation", new Color(239, 68, 68));
        JButton checkInButton = ModernFormPanel.createModernButton("Check In", new Color(168, 85, 247));
        JButton checkOutButton = ModernFormPanel.createModernButton("Check Out", new Color(245, 158, 11));
        JButton clearButton = ModernFormPanel.createModernButton("Clear", new Color(107, 114, 128));
        
        // Set consistent button sizes
        Dimension buttonSize = new Dimension(140, 40);
        addButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        checkInButton.setPreferredSize(buttonSize);
        checkOutButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(clearButton);
        
        addButton.addActionListener(e -> addReservation());
        updateButton.addActionListener(e -> updateReservation());
        deleteButton.addActionListener(e -> deleteReservation());
        checkInButton.addActionListener(e -> checkInReservation());
        checkOutButton.addActionListener(e -> checkOutReservation());
        clearButton.addActionListener(e -> clearForm());
        
        reservationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(55, 65, 81));
        return label;
    }
    
    private void addReservation() {
        if (validateForm()) {
            Reservation reservation = createReservationFromForm();
            if (reservationDAO.addReservation(reservation)) {
                JOptionPane.showMessageDialog(this, "Reservation added successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding reservation!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to update!");
            return;
        }
        
        if (validateForm()) {
            Reservation reservation = createReservationFromForm();
            reservation.setReservationId((Integer) tableModel.getValueAt(selectedRow, 0));
            
            if (reservationDAO.updateReservation(reservation)) {
                JOptionPane.showMessageDialog(this, "Reservation updated successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating reservation!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to delete!");
            return;
        }
        
        // Check if reservation can be deleted (not checked-in)
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        if ("Checked-In".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "Cannot delete a checked-in reservation. Please check out the guest first.", 
                "Cannot Delete", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this reservation?\nThis will also delete related billing and check-in/out records.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int reservationId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (reservationDAO.deleteReservation(reservationId)) {
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting reservation!\nThis reservation may have related records that prevent deletion.", 
                    "Delete Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void checkInReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to check in!");
            return;
        }
        
        int reservationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        if (reservationDAO.checkIn(reservationId)) {
            JOptionPane.showMessageDialog(this, "Guest checked in successfully!");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Error checking in guest!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkOutReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to check out!");
            return;
        }
        
        int reservationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        if (reservationDAO.checkOut(reservationId)) {
            JOptionPane.showMessageDialog(this, "Guest checked out successfully!");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Error checking out guest!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateForm() {
        if (guestComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a guest!");
            return false;
        }
        if (roomComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a room!");
            return false;
        }
        try {
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim());
            LocalDate checkOut = LocalDate.parse(checkOutField.getText().trim());
            if (!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date!");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates in YYYY-MM-DD format!");
            return false;
        }
        return true;
    }
    
    private Reservation createReservationFromForm() {
        Reservation reservation = new Reservation();
        Guest selectedGuest = (Guest) guestComboBox.getSelectedItem();
        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
        
        reservation.setGuestId(selectedGuest.getGuestId());
        reservation.setRoomId(selectedRoom.getRoomId());
        reservation.setCheckInDate(LocalDate.parse(checkInField.getText().trim()));
        reservation.setCheckOutDate(LocalDate.parse(checkOutField.getText().trim()));
        reservation.setStatus((String) statusComboBox.getSelectedItem());
        
        return reservation;
    }
    
    private void populateFormFromSelection() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow != -1) {
            // Find and select the guest and room in combo boxes
            String guestName = (String) tableModel.getValueAt(selectedRow, 1);
            String roomNumber = (String) tableModel.getValueAt(selectedRow, 2);
            
            for (int i = 0; i < guestComboBox.getItemCount(); i++) {
                Guest guest = guestComboBox.getItemAt(i);
                if (guest.getFullName().equals(guestName)) {
                    guestComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < roomComboBox.getItemCount(); i++) {
                Room room = roomComboBox.getItemAt(i);
                if (room.getRoomNumber().equals(roomNumber)) {
                    roomComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            checkInField.setText((String) tableModel.getValueAt(selectedRow, 3));
            checkOutField.setText((String) tableModel.getValueAt(selectedRow, 4));
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        }
    }
    
    private void clearForm() {
        guestComboBox.setSelectedIndex(0);
        roomComboBox.setSelectedIndex(0);
        checkInField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        checkOutField.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        statusComboBox.setSelectedIndex(0);
        reservationTable.clearSelection();
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = reservationDAO.getAllReservations();
        for (Reservation reservation : reservations) {
            Object[] row = {
                reservation.getReservationId(),
                reservation.getGuestName(),
                reservation.getRoomNumber(),
                reservation.getCheckInDate().toString(),
                reservation.getCheckOutDate().toString(),
                reservation.getStatus()
            };
            tableModel.addRow(row);
        }
        loadComboBoxData(); // Refresh combo box data
    }
}
