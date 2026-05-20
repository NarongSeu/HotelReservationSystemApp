package com.hotel.dto.components;

import com.hotel.dto.ModernColors;
import javax.swing.*;
import java.awt.*;

public class StatCard extends GradientPanel {
    private JLabel titleLabel;
    private JLabel valueLabel;
    private JLabel changeLabel;
    private JLabel iconLabel;
    
    public StatCard(String title, String value, String change, JLabel iconLabel, Color startColor, Color endColor) {
        super(startColor, endColor, true);
        initializeComponents(title, value, change, iconLabel);
        setupLayout();
    }
    
    private void initializeComponents(String title, String value, String change, JLabel iconLabel) {
        setPreferredSize(new Dimension(280, 120));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Use provided icon
        this.iconLabel = iconLabel;
        this.iconLabel.setForeground(ModernColors.TEXT_WHITE);
        
        // Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(ModernColors.TEXT_WHITE);
        
        // Value
        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(ModernColors.TEXT_WHITE);
        
        // Change
        changeLabel = new JLabel(change);
        changeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        changeLabel.setForeground(new Color(255, 255, 255, 200));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(iconLabel, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(valueLabel, BorderLayout.WEST);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(changeLabel, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void updateValues(String value, String change) {
        valueLabel.setText(value);
        changeLabel.setText(change);
        repaint();
    }
}
