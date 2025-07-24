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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint rounded background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

        g2d.dispose();
        super.paintComponent(g);
    }
    
    private void setupModernStyling() {
        // Table styling with rounded corners
        setRowHeight(40);
        setShowGrid(true);
        setGridColor(new Color(229, 231, 235));
        setIntercellSpacing(new Dimension(1, 1));
        setSelectionBackground(new Color(239, 246, 255));
        setSelectionForeground(new Color(55, 65, 81));
        setBackground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 13));
        setOpaque(false); // Make transparent for rounded corners
        
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
