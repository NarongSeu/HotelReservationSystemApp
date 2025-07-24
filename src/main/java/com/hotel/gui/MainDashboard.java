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
        contentPanel.setBackground(new Color(243, 244, 246)); // Light gray background like reference
        
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
        mainWrapper.setBackground(new Color(248, 250, 252)); // Slightly different background
        
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
        headerPanel.setBackground(Color.WHITE); // Clean white background
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)), // Bottom border
            BorderFactory.createEmptyBorder(20, 30, 20, 30) // Padding
        ));
        headerPanel.setPreferredSize(new Dimension(0, 70)); // Slightly taller
        
        // Center title with better styling
        JLabel titleLabel = new JLabel("Hotel Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55)); // Darker text
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Admin User section with better styling
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        userPanel.setOpaque(false);
        
        // User icon
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        userIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        
        JLabel userLabel = new JLabel("Admin User");
        userLabel.setFont(new Font("Arial", Font.MEDIUM, 14));
        userLabel.setForeground(new Color(75, 85, 99));
        
        userPanel.add(userIcon);
        userPanel.add(userLabel);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createModernNavigationPanel() {
        GradientPanel navPanel = new GradientPanel(new Color(45, 55, 130), new Color(25, 35, 80));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(250, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        // Hotel logo/title - exactly like reference
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(250, 60));
        
        JLabel logoIcon = new JLabel("🏨");
        logoIcon.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel titleLabel = new JLabel("Hotel System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        logoPanel.add(logoIcon);
        logoPanel.add(titleLabel);
        navPanel.add(logoPanel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Navigation buttons - exactly like reference
        String[] buttonNames = {"Dashboard", "Rooms", "Guests", "Reservations", "Billing"};
        String[] buttonIcons = {"📊", "🏠", "👥", "📅", "💰"};
        
        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = createModernNavButton(buttonNames[i], buttonIcons[i]);
            navPanel.add(button);
            navPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        // Add flexible space
        navPanel.add(Box.createVerticalGlue());
        
        // Logout button
        JButton logoutButton = createModernNavButton("Logout", "🚪");
        navPanel.add(logoutButton);
        
        return navPanel;
    }
    
    private JButton createModernNavButton(String text, String icon) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setMaximumSize(new Dimension(210, 45));
        button.setPreferredSize(new Dimension(210, 45));
        button.setBackground(new Color(255, 255, 255, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));
        
        // Text label
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        button.add(iconLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Logout".equals(text)) {
                    System.exit(0);
                    return;
                }
                
                cardLayout.show(contentPanel, text);
                
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
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(55, 65, 140));
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
