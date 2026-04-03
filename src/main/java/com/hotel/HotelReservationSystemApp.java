package com.hotel;

import com.hotel.gui.MainDashboard;
import com.hotel.util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;

public class HotelReservationSystemApp {
    
    public static void main(String[] args) {
        System.out.println("Starting Hotel Reservation System...");
        
        // Set system look and feel with fallback options
        setLookAndFeel();
        
        // Show splash screen
        showSplashScreen();
        
        // Test database connection and launch application
        SwingUtilities.invokeLater(() -> {
            boolean dbConnected = testDatabaseConnection();
            
            if (dbConnected) {
                System.out.println("Launching application with database connection...");
                new MainDashboard();
            } else {
                System.out.println("Launching application in demo mode (no database)...");
                showDatabaseWarningAndContinue();
            }
        });
    }

    private static void setLookAndFeel() {
        try {
            // Try system look and feel first
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Using system look and feel");
        } catch (Exception e1) {
            try {
                // Fallback to cross-platform look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                System.out.println("Using cross-platform look and feel");
            } catch (Exception e2) {
                try {
                    // Try Nimbus look and feel
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            System.out.println("Using Nimbus look and feel");
                            return;
                        }
                    }
                    // Default Metal look and feel
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    System.out.println("Using Metal look and feel");
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
        progressBar.setString("Initializing Application...");
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        splash.add(panel);
        splash.setVisible(true);
        
        // Simulate loading time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    private static void showDatabaseWarningAndContinue() {
        String message = "Database Connection Warning!\n\n" +
            "The application will run in demo mode without database functionality.\n\n" +
            "To enable full functionality:\n" +
            "1. Install and start MySQL server\n" +
            "2. Update credentials in DatabaseConnection.java\n" +
            "3. Restart the application\n\n" +
            "Click OK to continue in demo mode.";
        
        int result = JOptionPane.showConfirmDialog(null, message, "Database Warning", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            new MainDashboard();
        } else {
            System.exit(0);
        }
    }
}
