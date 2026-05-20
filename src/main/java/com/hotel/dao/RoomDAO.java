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

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return false;

            // Cascade delete at application layer to avoid orphan Reservations.
            conn.setAutoCommit(false);
            try {
                // Delete room service details first (depends on RoomServiceOrders).
                String deleteOrderDetailsSql =
                        "DELETE FROM OrderDetails WHERE order_id IN (" +
                        "  SELECT order_id FROM RoomServiceOrders WHERE reservation_id IN (" +
                        "    SELECT reservation_id FROM Reservations WHERE room_id=? " +
                        "  )" +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteOrderDetailsSql)) {
                    pstmt.setInt(1, roomId);
                    pstmt.executeUpdate();
                }

                // Delete room service orders tied to the room's reservations.
                String deleteRoomServiceOrdersSql =
                        "DELETE FROM RoomServiceOrders WHERE reservation_id IN (" +
                        "  SELECT reservation_id FROM Reservations WHERE room_id=? " +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteRoomServiceOrdersSql)) {
                    pstmt.setInt(1, roomId);
                    pstmt.executeUpdate();
                }

                // Delete check-in/out and billing tied to the room's reservations.
                String deleteCheckInOutSql =
                        "DELETE FROM CheckInOut WHERE reservation_id IN (" +
                        "  SELECT reservation_id FROM Reservations WHERE room_id=? " +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteCheckInOutSql)) {
                    pstmt.setInt(1, roomId);
                    pstmt.executeUpdate();
                }

                String deleteBillingSql =
                        "DELETE FROM Billing WHERE reservation_id IN (" +
                        "  SELECT reservation_id FROM Reservations WHERE room_id=? " +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteBillingSql)) {
                    pstmt.setInt(1, roomId);
                    pstmt.executeUpdate();
                }

                // Delete reservations for the room.
                String deleteReservationsSql = "DELETE FROM Reservations WHERE room_id=?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteReservationsSql)) {
                    pstmt.setInt(1, roomId);
                    pstmt.executeUpdate();
                }

                // Finally delete the room row.
                String deleteRoomSql = "DELETE FROM Rooms WHERE room_id=?";
                int updated;
                try (PreparedStatement pstmt = conn.prepareStatement(deleteRoomSql)) {
                    pstmt.setInt(1, roomId);
                    updated = pstmt.executeUpdate();
                }

                conn.commit();
                return updated > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
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
