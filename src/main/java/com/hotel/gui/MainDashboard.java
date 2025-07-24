package com.hotel.gui;

import com.hotel.gui.panels.*;
import com.hotel.gui.components.GradientPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panel instances
    private RoomPanel roomPanel;
    private GuestPanel guestPanel;
    private ReservationPanel reservationPanel;
    private BillingPanel billingPanel;
    private DashboardPanel dashboardPanel;
    
    public MainDashboard() {
        initializeComponents();
        setupLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Hotel Reservation System");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(243, 244, 246));
        
        // Initialize panels
        dashboardPanel = new DashboardPanel();
        roomPanel = new RoomPanel();
        guestPanel = new GuestPanel();
        reservationPanel = new ReservationPanel();
        billingPanel = new BillingPanel();
        
        // Add panels to card layout
        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(roomPanel, "Rooms");
        contentPanel.add(guestPanel, "Guests");
        contentPanel.add(reservationPanel, "Reservations");
        contentPanel.add(billingPanel, "Billing");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create modern navigation panel
        JPanel navigationPanel = createModernNavigationPanel();
        add(navigationPanel, BorderLayout.WEST);
        
        // Create main content wrapper
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(new Color(248, 250, 252));
        
        // Create simple header panel
        JPanel headerPanel = createSimpleHeaderPanel();
        mainWrapper.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with proper background
        contentPanel.setBackground(new Color(248, 250, 252));
        mainWrapper.add(contentPanel, BorderLayout.CENTER);
        
        add(mainWrapper, BorderLayout.CENTER);
        
        // Show dashboard by default
        cardLayout.show(contentPanel, "Dashboard");
    }
    
    private JPanel createSimpleHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        
        // Center title with better styling
        JLabel titleLabel = new JLabel("Hotel Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Admin User section with better styling
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        userPanel.setOpaque(false);
        
        JLabel userIcon = new JLabel("●");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        userIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        
        JLabel userLabel = new JLabel("Admin User");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(new Color(75, 85, 99));
        
        userPanel.add(userIcon);
        userPanel.add(userLabel);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createModernNavigationPanel() {
        GradientPanel navPanel = new GradientPanel(new Color(45, 55, 130), new Color(25, 35, 80));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(280, 0)); // Increased width to prevent cutting
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        
        // Hotel logo/title - perfectly centered
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(280, 60));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        JLabel logoIcon = new JLabel("■");
        logoIcon.setFont(new Font("Arial", Font.BOLD, 18));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setPreferredSize(new Dimension(25, 25));
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel("Hotel System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        logoPanel.add(logoIcon);
        logoPanel.add(titleLabel);
        navPanel.add(logoPanel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Navigation buttons with perfect alignment
        String[] buttonNames = {"Dashboard", "Rooms", "Guests", "Reservations", "Billing"};
        String[] buttonIcons = {"■", "| |", "●", "≡", "$"};
        
        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = createPerfectNavButton(buttonNames[i], buttonIcons[i]);
            
            JPanel buttonContainer = new JPanel();
            buttonContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            buttonContainer.setOpaque(false);
            buttonContainer.setMaximumSize(new Dimension(280, 50));
            buttonContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            
            buttonContainer.add(button);
            navPanel.add(buttonContainer);
            navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        // Add flexible space
        navPanel.add(Box.createVerticalGlue());
        
        // Logout button at bottom
        JButton logoutButton = createPerfectNavButton("Logout", "◐");
        JPanel logoutContainer = new JPanel();
        logoutContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoutContainer.setOpaque(false);
        logoutContainer.setMaximumSize(new Dimension(280, 50));
        logoutContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        logoutContainer.add(logoutButton);
        navPanel.add(logoutContainer);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return navPanel;
    }
    
    private JButton createPerfectNavButton(String text, String icon) {
        JButton button = new JButton();
        button.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        button.setPreferredSize(new Dimension(220, 45));
        button.setMaximumSize(new Dimension(220, 45));
        button.setBackground(new Color(255, 255, 255, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Create content with perfect alignment
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 12));
        content.setOpaque(false);
        
        // Icon with fixed width for perfect alignment
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setPreferredSize(new Dimension(25, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Text label with proper spacing
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        content.add(iconLabel);
        content.add(textLabel);
        button.add(content);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Logout".equals(text)) {
                    System.exit(0);
                    return;
                }
                
                cardLayout.show(MainDashboard.this.contentPanel, text);
                
                // Refresh panels when switching
                switch (text) {
                    case "Rooms":
                        roomPanel.refreshData();
                        break;
                    case "Guests":
                        guestPanel.refreshData();
                        break;
                    case "Reservations":
                        reservationPanel.refreshData();
                        break;
                    case "Billing":
                        billingPanel.refreshData();
                        break;
                    case "Dashboard":
                        dashboardPanel.refreshData();
                        break;
                }
            }
        });
        
        // Smooth hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(55, 65, 140, 100));
                button.setOpaque(true);
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 0));
                button.setOpaque(false);
                button.repaint();
            }
        });
        
        return button;
    }
}
