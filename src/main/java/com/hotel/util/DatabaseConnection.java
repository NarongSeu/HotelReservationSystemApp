package com.hotel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static Connection connection = null;
    private static boolean connectionTested = false;
    private static boolean connectionAvailable = false;
    
    public static Connection getConnection() {
        if (!connectionTested) {
            testConnectionAvailability();
        }
        
        if (!connectionAvailable) {
            System.err.println("Database connection is not available. Please check MySQL server.");
            return null;
        }
        
        try {
            if (connection == null || connection.isClosed()) {
                // Load MySQL Connector J 9.3.0 driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
                
                // Initialize database tables if they don't exist
                initializeDatabase();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Connector J driver not found!");
            System.err.println("Please ensure mysql-connector-j-9.3.0.jar is in your classpath");
            connectionAvailable = false;
            return null;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            connectionAvailable = false;
            return null;
        }
        return connection;
    }
    
    private static void testConnectionAvailability() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection testConn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (testConn != null && !testConn.isClosed()) {
                connectionAvailable = true;
                testConn.close();
                System.out.println("Database connection test successful!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Connector J driver not found!");
            connectionAvailable = false;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            System.err.println("Please ensure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database credentials are correct");
            System.err.println("3. MySQL is accessible on localhost:3306");
            connectionAvailable = false;
        }
        connectionTested = true;
    }
    
    private static void initializeDatabase() {
        if (connection == null) return;
        
        try (Statement stmt = connection.createStatement()) {
            // Create database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS hotel_db");
            stmt.executeUpdate("USE hotel_db");
            
            // Create tables according to your schema
            createTables(stmt);
            
            // Insert sample data
            insertSampleData(stmt);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTables(Statement stmt) throws SQLException {
        // Create Rooms table
        String createRoomsTable = "CREATE TABLE IF NOT EXISTS Rooms (" +
            "room_id INT PRIMARY KEY AUTO_INCREMENT," +
            "room_number VARCHAR(10) NOT NULL UNIQUE," +
            "room_type VARCHAR(50)," +
            "price DECIMAL(10,2)," +
            "status VARCHAR(20) DEFAULT 'Available'" +
            ")";
        
        // Create Guests table
        String createGuestsTable = "CREATE TABLE IF NOT EXISTS Guests (" +
            "guest_id INT PRIMARY KEY AUTO_INCREMENT," +
            "full_name VARCHAR(100)," +
            "phone VARCHAR(20)," +
            "id_passport VARCHAR(50)," +
            "address TEXT" +
            ")";
        
        // Create Reservations table
        String createReservationsTable = "CREATE TABLE IF NOT EXISTS Reservations (" +
            "reservation_id INT PRIMARY KEY AUTO_INCREMENT," +
            "guest_id INT," +
            "room_id INT," +
            "check_in_date DATE," +
            "check_out_date DATE," +
            "status VARCHAR(20) DEFAULT 'Confirmed'," +
            "FOREIGN KEY (guest_id) REFERENCES Guests(guest_id)," +
            "FOREIGN KEY (room_id) REFERENCES Rooms(room_id)" +
            ")";
        
        // Create Billing table
        String createBillingTable = "CREATE TABLE IF NOT EXISTS Billing (" +
            "bill_id INT PRIMARY KEY AUTO_INCREMENT," +
            "reservation_id INT," +
            "total_amount DECIMAL(10,2)," +
            "tax DECIMAL(10,2)," +
            "discount DECIMAL(10,2)," +
            "payment_method VARCHAR(20)," +
            "payment_status VARCHAR(20) DEFAULT 'Pending'," +
            "issued_date DATE," +
            "FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)" +
            ")";
        
        // Create CheckInOut table
        String createCheckInOutTable = "CREATE TABLE IF NOT EXISTS CheckInOut (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "reservation_id INT," +
            "check_in_time DATETIME," +
            "check_out_time DATETIME," +
            "FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)" +
            ")";
        
        // Create RoomServiceItems table
        String createRoomServiceItemsTable = "CREATE TABLE IF NOT EXISTS RoomServiceItems (" +
            "item_id INT PRIMARY KEY AUTO_INCREMENT," +
            "item_name VARCHAR(100) NOT NULL," +
            "description TEXT," +
            "price DECIMAL(10,2) NOT NULL," +
            "category VARCHAR(50)" +
            ")";
        
        // Create RoomServiceOrders table
        String createRoomServiceOrdersTable = "CREATE TABLE IF NOT EXISTS RoomServiceOrders (" +
            "order_id INT PRIMARY KEY AUTO_INCREMENT," +
            "reservation_id INT," +
            "order_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "delivery_status VARCHAR(20) DEFAULT 'Pending'," +
            "total_amount DECIMAL(10,2)," +
            "FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)" +
            ")";
        
        // Create OrderDetails table
        String createOrderDetailsTable = "CREATE TABLE IF NOT EXISTS OrderDetails (" +
            "order_detail_id INT PRIMARY KEY AUTO_INCREMENT," +
            "order_id INT," +
            "item_id INT," +
            "quantity INT NOT NULL," +
            "unit_price DECIMAL(10,2) NOT NULL," +
            "FOREIGN KEY (order_id) REFERENCES RoomServiceOrders(order_id)," +
            "FOREIGN KEY (item_id) REFERENCES RoomServiceItems(item_id)" +
            ")";
        
        // Execute table creation
        stmt.executeUpdate(createRoomsTable);
        stmt.executeUpdate(createGuestsTable);
        stmt.executeUpdate(createReservationsTable);
        stmt.executeUpdate(createBillingTable);
        stmt.executeUpdate(createCheckInOutTable);
        stmt.executeUpdate(createRoomServiceItemsTable);
        stmt.executeUpdate(createRoomServiceOrdersTable);
        stmt.executeUpdate(createOrderDetailsTable);
        
        System.out.println("All tables created successfully!");
    }
    
    private static void insertSampleData(Statement stmt) throws SQLException {
        // Check if data already exists
        var rs = stmt.executeQuery("SELECT COUNT(*) FROM Rooms");
        if (rs.next() && rs.getInt(1) > 0) {
            return; // Data already exists
        }
        
        // Insert sample rooms
        String insertRooms = "INSERT INTO Rooms (room_number, room_type, price, status) VALUES " +
            "('101', 'Single', 100.00, 'Available')," +
            "('102', 'Single', 100.00, 'Available')," +
            "('103', 'Single', 100.00, 'Maintenance')," +
            "('201', 'Double', 150.00, 'Available')," +
            "('202', 'Double', 150.00, 'Occupied')," +
            "('203', 'Double', 150.00, 'Available')," +
            "('301', 'Suite', 300.00, 'Available')," +
            "('302', 'Suite', 300.00, 'Available')," +
            "('401', 'Deluxe', 250.00, 'Available')," +
            "('402', 'Deluxe', 250.00, 'Available')";
        
        // Insert sample guests
        String insertGuests = "INSERT INTO Guests (full_name, phone, id_passport, address) VALUES " +
            "('John Doe', '+1-555-0101', 'P123456789', '123 Main St, New York, NY')," +
            "('Jane Smith', '+1-555-0102', 'DL987654321', '456 Oak Ave, Los Angeles, CA')," +
            "('Robert Johnson', '+1-555-0103', 'ID456789123', '789 Pine Rd, Chicago, IL')," +
            "('Emily Davis', '+44-20-7946-0958', 'P987654321', '10 Downing St, London, UK')," +
            "('Michael Wilson', '+1-555-0105', 'P111222333', '321 Elm St, Miami, FL')";
        
        // Insert sample room service items
        String insertRoomServiceItems = "INSERT INTO RoomServiceItems (item_name, description, price, category) VALUES " +
            "('Club Sandwich', 'Triple-decker sandwich with turkey, bacon, lettuce, tomato', 15.99, 'Food')," +
            "('Caesar Salad', 'Fresh romaine lettuce with caesar dressing and croutons', 12.99, 'Food')," +
            "('Grilled Salmon', 'Atlantic salmon with lemon butter sauce', 24.99, 'Food')," +
            "('Coffee', 'Freshly brewed coffee', 4.99, 'Beverage')," +
            "('Orange Juice', 'Fresh squeezed orange juice', 6.99, 'Beverage')," +
            "('Laundry Service', 'Same-day laundry service', 25.00, 'Laundry')," +
            "('Room Cleaning', 'Additional room cleaning service', 30.00, 'Service')";
        
        stmt.executeUpdate(insertRooms);
        stmt.executeUpdate(insertGuests);
        stmt.executeUpdate(insertRoomServiceItems);
        
        System.out.println("Sample data inserted successfully!");
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Test connection method
    public static boolean testConnection() {
        if (!connectionTested) {
            testConnectionAvailability();
        }
        return connectionAvailable;
    }
    
    public static boolean isConnectionAvailable() {
        return connectionAvailable;
    }
}
