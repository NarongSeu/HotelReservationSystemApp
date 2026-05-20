package com.hotel.dao;

import com.hotel.model.Reservation;
import com.hotel.util.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        if (!DatabaseConnection.isConnectionAvailable()) {
            return getDemoReservations();
        }

        String sql = "SELECT r.*, g.full_name, rm.room_number " +
            "FROM Reservations r " +
            "JOIN Guests g ON r.guest_id = g.guest_id " +
            "JOIN Rooms rm ON r.room_id = rm.room_id " +
            "ORDER BY r.check_in_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setGuestId(rs.getInt("guest_id"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
                reservation.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
                reservation.setStatus(rs.getString("status"));
                reservation.setGuestName(rs.getString("full_name"));
                reservation.setRoomNumber(rs.getString("room_number"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    public boolean addReservation(Reservation reservation) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

        String sql = "INSERT INTO Reservations (guest_id, room_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservation.getGuestId());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setDate(3, Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setString(5, reservation.getStatus());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateReservation(Reservation reservation) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

        String sql = "UPDATE Reservations SET guest_id=?, room_id=?, check_in_date=?, check_out_date=?, status=? WHERE reservation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservation.getGuestId());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setDate(3, Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setString(5, reservation.getStatus());
            pstmt.setInt(6, reservation.getReservationId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteReservation(int reservationId) {
    if (!DatabaseConnection.isConnectionAvailable()) {
        showDemoModeMessage();
        return false;
    }

    try (Connection conn = DatabaseConnection.getConnection()) {
        // Start transaction
        conn.setAutoCommit(false);
        
        try {
            // Delete room service dependent rows first (OrderDetails -> RoomServiceOrders).
            String deleteOrderDetailsSql =
                    "DELETE FROM OrderDetails WHERE order_id IN (" +
                    "  SELECT order_id FROM RoomServiceOrders WHERE reservation_id=? " +
                    ")";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteOrderDetailsSql)) {
                pstmt1.setInt(1, reservationId);
                pstmt1.executeUpdate();
            }

            String deleteRoomServiceOrdersSql =
                    "DELETE FROM RoomServiceOrders WHERE reservation_id=?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteRoomServiceOrdersSql)) {
                pstmt1.setInt(1, reservationId);
                pstmt1.executeUpdate();
            }

            // First delete related records from CheckInOut table
            String deleteCheckInOutSql = "DELETE FROM CheckInOut WHERE reservation_id=?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteCheckInOutSql)) {
                pstmt1.setInt(1, reservationId);
                pstmt1.executeUpdate();
            }
            
            // Then delete related records from Billing table
            String deleteBillingSql = "DELETE FROM Billing WHERE reservation_id=?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(deleteBillingSql)) {
                pstmt2.setInt(1, reservationId);
                pstmt2.executeUpdate();
            }
            
            // Finally delete the reservation
            String deleteReservationSql = "DELETE FROM Reservations WHERE reservation_id=?";
            try (PreparedStatement pstmt3 = conn.prepareStatement(deleteReservationSql)) {
                pstmt3.setInt(1, reservationId);
                int result = pstmt3.executeUpdate();
                
                // Commit transaction
                conn.commit();
                return result > 0;
            }
            
        } catch (SQLException e) {
            // Rollback transaction on error
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
        
    } catch (SQLException e) {
        System.err.println("Error deleting reservation: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    public boolean checkIn(int reservationId) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

        String sql = "UPDATE Reservations SET status='Checked-In' WHERE reservation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            
            if (pstmt.executeUpdate() > 0) {
                // Also insert check-in record
                String checkInSql = "INSERT INTO CheckInOut (reservation_id, check_in_time) VALUES (?, NOW())";
                try (PreparedStatement checkInStmt = conn.prepareStatement(checkInSql)) {
                    checkInStmt.setInt(1, reservationId);
                    checkInStmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkOut(int reservationId) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

        String sql = "UPDATE Reservations SET status='Checked-Out' WHERE reservation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            
            if (pstmt.executeUpdate() > 0) {
                // Update check-out time
                String checkOutSql = "UPDATE CheckInOut SET check_out_time=NOW() WHERE reservation_id=? AND check_out_time IS NULL";
                try (PreparedStatement checkOutStmt = conn.prepareStatement(checkOutSql)) {
                    checkOutStmt.setInt(1, reservationId);
                    checkOutStmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<Reservation> getDemoReservations() {
        List<Reservation> demoReservations = new ArrayList<>();
        demoReservations.add(createDemoReservation(1, 1, 1, "Sok Dara", "101",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), "Confirmed"));
        demoReservations.add(createDemoReservation(2, 2, 4, "Chantha Srey", "202",
                LocalDate.now().minusDays(3), LocalDate.now().plusDays(1), "Checked-In"));
        demoReservations.add(createDemoReservation(3, 3, 5, "Vannak Rith", "301",
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(5), "Confirmed"));
        return demoReservations;
    }

    private Reservation createDemoReservation(int reservationId, int guestId, int roomId, String guestName,
                                              String roomNumber, LocalDate checkIn, LocalDate checkOut, String status) {
        Reservation reservation = new Reservation(guestId, roomId, checkIn, checkOut, status);
        reservation.setReservationId(reservationId);
        reservation.setGuestName(guestName);
        reservation.setRoomNumber(roomNumber);
        return reservation;
    }

    private void showDemoModeMessage() {
        JOptionPane.showMessageDialog(null,
                "Database not available. Reservation changes are disabled in demo mode.",
                "Demo Mode",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
