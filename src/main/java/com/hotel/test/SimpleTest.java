package com.hotel.test;

import com.hotel.model.User;
import com.hotel.util.DatabaseConnection;

public class SimpleTest {
    
    public static void main(String[] args) {
        System.out.println("Hotel Reservation System - Simple Test");
        System.out.println("=====================================");
        
        try {
            // Test basic functionality
            System.out.println("1. Testing database connection...");
            boolean dbAvailable = DatabaseConnection.testConnection();
            
            if (dbAvailable) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.out.println("⚠️  Database not available - will run in demo mode");
            }
            
            System.out.println("2. Testing application startup...");
            
            // Test GUI components
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    System.out.println("3. Creating main dashboard...");
                    User testUser = new User();
                    testUser.setId("test-admin");
                    testUser.setEmail("admin@hotel.com");
                    testUser.setFullName("Test Administrator");
                    testUser.setRole("admin");
                    testUser.setActive(true);
                    com.hotel.gui.MainDashboard dashboard = new com.hotel.gui.MainDashboard(testUser);
                    System.out.println("✅ Application started successfully!");
                    
                    // Close after 5 seconds for testing
                    javax.swing.Timer timer = new javax.swing.Timer(5000, e -> {
                        System.out.println("Test completed - closing application");
                        System.exit(0);
                    });
                    timer.setRepeats(false);
                    timer.start();
                    
                } catch (Exception e) {
                    System.err.println("❌ Error starting application: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
            });
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
