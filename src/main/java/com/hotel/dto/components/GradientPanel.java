package com.hotel.dto.components;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;
    private boolean horizontal;
    
    public GradientPanel(Color startColor, Color endColor) {
        this(startColor, endColor, false);
    }
    
    public GradientPanel(Color startColor, Color endColor, boolean horizontal) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.horizontal = horizontal;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gradient;
        if (horizontal) {
            gradient = new GradientPaint(0, 0, startColor, width, 0, endColor);
        } else {
            gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
        }
        
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, width, height, 12, 12);
        g2d.dispose();
    }
}
