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
        // Table styling - exactly like the reference
        setRowHeight(45);
        setShowGrid(true);
        setGridColor(new Color(226, 232, 240)); // Light gray grid lines
        setIntercellSpacing(new Dimension(1, 1));
        setSelectionBackground(new Color(59, 130, 246, 30));
        setSelectionForeground(ModernColors.TEXT_PRIMARY);
        setBackground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Header styling - exactly matching reference
        JTableHeader header = getTableHeader();
        header.setBackground(new Color(249, 250, 251)); // Very light gray
        header.setForeground(new Color(75, 85, 99)); // Dark gray text
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setReorderingAllowed(false);
        
        // Custom header renderer
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(249, 250, 251));
                c.setForeground(new Color(75, 85, 99));
                setFont(new Font("Arial", Font.BOLD, 13));
                setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });
        
        // Cell renderer for clean alternating rows
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(249, 250, 251)); // Very light alternating color
                    }
                } else {
                    c.setBackground(new Color(59, 130, 246, 30));
                }
                
                setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                setForeground(new Color(55, 65, 81)); // Dark gray text
                setFont(new Font("Arial", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.LEFT);
                
                return c;
            }
        });
        
        // Status column special renderer
        setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Apply standard styling first
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(249, 250, 251));
                    }
                } else {
                    c.setBackground(new Color(59, 130, 246, 30));
                }
                
                setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                setFont(new Font("Arial", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.LEFT);
                
                // Special styling for status columns
                String columnName = table.getColumnName(column);
                if ("Status".equals(columnName) || "Payment Status".equals(columnName)) {
                    String status = value != null ? value.toString() : "";
                    
                    // Create status badge
                    if ("Available".equals(status) || "Paid".equals(status) || "Confirmed".equals(status)) {
                        setForeground(new Color(34, 197, 94)); // Green
                        setText("● " + status);
                    } else if ("Occupied".equals(status) || "Pending".equals(status) || "Checked-In".equals(status)) {
                        setForeground(new Color(251, 191, 36)); // Yellow
                        setText("● " + status);
                    } else if ("Maintenance".equals(status) || "Cancelled".equals(status)) {
                        setForeground(new Color(239, 68, 68)); // Red
                        setText("● " + status);
                    } else {
                        setForeground(new Color(55, 65, 81));
                        setText(status);
                    }
                } else {
                    setForeground(new Color(55, 65, 81));
                }
                
                return c;
            }
        });
    }
}
