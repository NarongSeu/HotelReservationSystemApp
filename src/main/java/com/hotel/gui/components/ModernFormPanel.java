package com.hotel.gui.components;

import com.hotel.gui.ModernColors;
import javax.swing.*;
import java.awt.*;

public class ModernFormPanel extends JPanel {
    
    public ModernFormPanel(String title) {
        setupStyling(title);
    }
    
    private void setupStyling(String title) {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(55, 65, 81));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);
    }
    
    public static JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(200, 35));
        return field;
    }
    
    public static JComboBox<String> createModernComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        combo.setPreferredSize(new Dimension(200, 35));
        combo.setBackground(Color.WHITE);
        return combo;
    }
    
    public static JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
}
