package com.hotel.gui.panels;

import javax.swing.*;
import java.awt.*;

public class ReservationPanel extends JPanel {
    
    public ReservationPanel() {
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Reservation Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JLabel comingSoonLabel = new JLabel("Reservation Management - Coming Soon");
        comingSoonLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(comingSoonLabel, BorderLayout.CENTER);
    }
    
    public void refreshData() {
        // Implementation for refreshing reservation data
    }
}
