package com.hotel.gui;

import com.hotel.gui.panels.*;
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
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
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
        
        // Create navigation panel
        JPanel navigationPanel = createNavigationPanel();
        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        // Show dashboard by default
        cardLayout.show(contentPanel, "Dashboard");
    }
    
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(52, 73, 94));
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Hotel logo/title
        JLabel titleLabel = new JLabel("Hotel Management");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(titleLabel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Navigation buttons
        String[] buttonNames = {"Dashboard", "Rooms", "Guests", "Reservations", "Billing"};
        
        for (String buttonName : buttonNames) {
            JButton button = createNavButton(buttonName);
            navPanel.add(button);
            navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        return navPanel;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Try different look and feel options
                setLookAndFeel();
            } catch (Exception e) {
                System.err.println("Could not set look and feel: " + e.getMessage());
                // Continue with default look and feel
            }
            new MainDashboard();
        });
    }

    private static void setLookAndFeel() {
        try {
            // Try system look and feel first
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            try {
                // Fallback to cross-platform look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e2) {
                try {
                    // Fallback to Nimbus if available
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            return;
                        }
                    }
                    // If nothing works, use default Metal look and feel
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception e3) {
                    // Use default look and feel
                    System.err.println("Using default look and feel");
                }
            }
        }
    }
}
