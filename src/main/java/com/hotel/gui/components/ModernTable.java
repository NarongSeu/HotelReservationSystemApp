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
        // Table styling
        setRowHeight(50);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionBackground(new Color(59, 130, 246, 50));
        setSelectionForeground(ModernColors.TEXT_PRIMARY);
        setBackground(ModernColors.CARD_BACKGROUND);
        setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Header styling
        JTableHeader header = getTableHeader();
        header.setBackground(ModernColors.TABLE_HEADER);
        header.setForeground(ModernColors.TEXT_SECONDARY);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        
        // Cell renderer for alternating row colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(ModernColors.CARD_BACKGROUND);
                    } else {
                        c.setBackground(new Color(248, 250, 252));
                    }
                }
                
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                setForeground(ModernColors.TEXT_PRIMARY);
                
                return c;
            }
        });
    }
}
