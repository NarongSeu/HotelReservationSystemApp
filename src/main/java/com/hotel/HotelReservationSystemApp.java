package com.hotel;

import com.hotel.gui.MainDashboard;
import com.hotel.util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;

public class HotelReservationSystemApp {
    
    public static void main(String[] args) {
        // Set system look and feel with fallback options
        setLookAndFeel();
        
        // Show splash screen
        showSplashScreen();
        
        // Test database connection
        SwingUtilities.invokeLater(() -> {
            if (testDatabaseConnection()) {
                // Launch main application
                new MainDashboard();
            } else {
                showDatabaseErrorDialog();
            }
        });
    }

    private static void setLookAndFeel() {
        try {
            // Try system look and feel first
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e1) {
            try {
                // Fallback to cross-platform look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeel());
            } catch (Exception e2) {
                try {
                    // Try Nimbus look and feel
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            return;
                        }
                    }
                    // Default Metal look and feel
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception e3) {
                    System.err.println("Could not set look and feel, using default: " + e3.getMessage());
                }
            }
        }
    }
    
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(400, 300);
        splash.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Hotel Reservation System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Loading...", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Initializing Database...");
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        splash.add(panel);
        splash.setVisible(true);
        
        // Simulate loading time
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        splash.dispose();
    }
    
    private static boolean testDatabaseConnection() {
        try {
            System.out.println("Testing database connection...");
            boolean connected = DatabaseConnection.testConnection();
            if (connected) {
                System.out.println("Database connection successful!");
                return true;
            } else {
                System.err.println("Database connection failed!");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error testing database connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static void showDatabaseErrorDialog() {
        String message = """
            Database Connection Failed!
            
            Please ensure:
            1. MySQL server is running
            2. Database 'hotel_db' exists or can be created
            3. Username and password are correct
            4. MySQL Connector J 9.3.0 is in classpath
            
            Check DatabaseConnection.java for configuration.
            """;
        
        JOptionPane.showMessageDialog(null, message, "Database Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
