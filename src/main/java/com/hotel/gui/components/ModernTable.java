package com.hotel.gui.components;

import com.hotel.gui.ModernColors;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernTable extends JTable {
    
    public ModernTable(DefaultTableModel model) {
        super(model);
        setupModernStyling();
    }
    
    private void setupModernStyling() {
        // Table styling - exactly like reference (clean and minimal)
        setRowHeight(40);
        setShowGrid(true);
        setGridColor(new Color(229, 231, 235)); // Very light gray grid
        setIntercellSpacing(new Dimension(1, 1));
        setSelectionBackground(new Color(239, 246, 255)); // Very light blue selection
        setSelectionForeground(new Color(55, 65, 81));
        setBackground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Header styling - exactly matching reference
        JTableHeader header = getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(55, 65, 81)); // Dark text
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        
        // Custom header renderer - clean and simple
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.WHITE);
                c.setForeground(new Color(55, 65, 81));
                setFont(new Font("Arial", Font.BOLD, 13));
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });
        
        // Cell renderer - clean and minimal like reference
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Clean white background - no alternating colors
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(239, 246, 255));
                }
                
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                setForeground(new Color(55, 65, 81));
                setFont(new Font("Arial", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.LEFT);
                
                return c;
            }
        });
    }
}
