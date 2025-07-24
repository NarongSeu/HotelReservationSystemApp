package com.hotel.test;

import com.hotel.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("Testing Hotel Reservation System Database Connection...");
        System.out.println("================================================");
        
        try {
            // Test database connection
            Connection conn = DatabaseConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection successful!");
                System.out.println("Database URL: jdbc:mysql://localhost:3306/hotel_db");
                System.out.println("Connection valid: " + conn.isValid(5));
                
                // Test a simple query
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT COUNT(*) as room_count FROM Rooms");
                if (rs.next()) {
                    System.out.println("✅ Sample data loaded - Rooms count: " + rs.getInt("room_count"));
                }
                rs.close();
                stmt.close();
                
            } else {
                System.out.println("❌ Database connection failed!");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
            System.out.println("\nTroubleshooting tips:");
            System.out.println("1. Make sure MySQL server is running");
            System.out.println("2. Check username/password in DatabaseConnection.java");
            System.out.println("3. Verify MySQL is accessible on localhost:3306");
            System.out.println("4. Ensure MySQL Connector J is in classpath");
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
        
        System.out.println("\nTest completed.");
    }
}
