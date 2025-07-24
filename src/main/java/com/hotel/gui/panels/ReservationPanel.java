package com.hotel.gui.panels;

import com.hotel.dao.ReservationDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Reservation;
import com.hotel.model.Guest;
import com.hotel.model.Room;
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
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Reservation Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Reservation Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Guest:"), gbc);
        gbc.gridx = 1;
        formPanel.add(guestComboBox, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 3;
        formPanel.add(roomComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Check-In (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkInField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Check-Out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        formPanel.add(checkOutField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusComboBox, gbc);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Reservation");
        JButton updateButton = new JButton("Update Reservation");
        JButton deleteButton = new JButton("Delete Reservation");
        JButton checkInButton = new JButton("Check In");
        JButton checkOutButton = new JButton("Check Out");
        JButton clearButton = new JButton("Clear");
        
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
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(clearButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
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
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this reservation?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int reservationId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (reservationDAO.deleteReservation(reservationId)) {
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting reservation!", "Error", JOptionPane.ERROR_MESSAGE);
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
