package com.hotel.gui.panels;

import com.hotel.dao.RoomDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.model.Room;
import com.hotel.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    private JLabel totalRoomsLabel, availableRoomsLabel, occupiedRoomsLabel, totalGuestsLabel;
    
    public DashboardPanel() {
        roomDAO = new RoomDAO();
        guestDAO = new GuestDAO();
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        totalRoomsLabel = new JLabel("0");
        availableRoomsLabel = new JLabel("0");
        occupiedRoomsLabel = new JLabel("0");
        totalGuestsLabel = new JLabel("0");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Hotel Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Create stat cards
        JPanel totalRoomsCard = createStatCard("Total Rooms", totalRoomsLabel, new Color(52, 152, 219));
        JPanel availableRoomsCard = createStatCard("Available Rooms", availableRoomsLabel, new Color(46, 204, 113));
        JPanel occupiedRoomsCard = createStatCard("Occupied Rooms", occupiedRoomsLabel, new Color(231, 76, 60));
        JPanel totalGuestsCard = createStatCard("Total Guests", totalGuestsLabel, new Color(155, 89, 182));
        
        statsPanel.add(totalRoomsCard);
        statsPanel.add(availableRoomsCard);
        statsPanel.add(occupiedRoomsCard);
        statsPanel.add(totalGuestsCard);
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void refreshData() {
        List<Room> allRooms = roomDAO.getAllRooms();
        List<Room> availableRooms = roomDAO.getAvailableRooms();
        List<Guest> allGuests = guestDAO.getAllGuests();
        
        totalRoomsLabel.setText(String.valueOf(allRooms.size()));
        availableRoomsLabel.setText(String.valueOf(availableRooms.size()));
        
        long occupiedCount = allRooms.stream()
            .filter(room -> "Occupied".equals(room.getStatus()))
            .count();
        occupiedRoomsLabel.setText(String.valueOf(occupiedCount));
        
        totalGuestsLabel.setText(String.valueOf(allGuests.size()));
    }
}
