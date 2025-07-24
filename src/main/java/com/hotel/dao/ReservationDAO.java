package com.hotel.dao;

import com.hotel.model.Reservation;
import com.hotel.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
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
        String sql = "DELETE FROM Reservations WHERE reservation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean checkIn(int reservationId) {
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
}
