package com.hotel.dao;

import com.hotel.model.Room;
import com.hotel.util.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomDAO {
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        
        // Return demo data if no database connection
        if (!DatabaseConnection.isConnectionAvailable()) {
            return getDemoRooms();
        }
        
        String sql = "SELECT * FROM Rooms ORDER BY room_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setPrice(rs.getBigDecimal("price"));
                room.setStatus(rs.getString("status"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return getDemoRooms(); // Fallback to demo data
        }
        return rooms;
    }
    
    private List<Room> getDemoRooms() {
        List<Room> demoRooms = new ArrayList<>();
        demoRooms.add(new Room("101", "Single", new java.math.BigDecimal("100.00"), "Available"));
        demoRooms.add(new Room("102", "Single", new java.math.BigDecimal("100.00"), "Available"));
        demoRooms.add(new Room("201", "Double", new java.math.BigDecimal("150.00"), "Available"));
        demoRooms.add(new Room("202", "Double", new java.math.BigDecimal("150.00"), "Occupied"));
        demoRooms.add(new Room("301", "Suite", new java.math.BigDecimal("300.00"), "Available"));
        
        // Set demo IDs
        for (int i = 0; i < demoRooms.size(); i++) {
            demoRooms.get(i).setRoomId(i + 1);
        }
        
        return demoRooms;
    }
    
    public boolean addRoom(Room room) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            JOptionPane.showMessageDialog(null, "Database not available. Running in demo mode.", 
                "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        String sql = "INSERT INTO Rooms (room_number, room_type, price, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setBigDecimal(3, room.getPrice());
            pstmt.setString(4, room.getStatus());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoom(Room room) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            JOptionPane.showMessageDialog(null, "Database not available. Running in demo mode.", 
                "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        String sql = "UPDATE Rooms SET room_number=?, room_type=?, price=?, status=? WHERE room_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setBigDecimal(3, room.getPrice());
            pstmt.setString(4, room.getStatus());
            pstmt.setInt(5, room.getRoomId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRoom(int roomId) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            JOptionPane.showMessageDialog(null, "Database not available. Running in demo mode.", 
                "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        String sql = "DELETE FROM Rooms WHERE room_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        
        // Return demo data if no database connection
        if (!DatabaseConnection.isConnectionAvailable()) {
            return getDemoRooms().stream()
                .filter(room -> "Available".equals(room.getStatus()))
                .collect(Collectors.toList());
        }
        
        String sql = "SELECT * FROM Rooms WHERE status = 'Available' ORDER BY room_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setPrice(rs.getBigDecimal("price"));
                room.setStatus(rs.getString("status"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
}
