package com.hotel.dao;

import com.hotel.model.Guest;
import com.hotel.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM Guests ORDER BY full_name";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Guest guest = new Guest();
                    guest.setGuestId(rs.getInt("guest_id"));
                    guest.setFullName(rs.getString("full_name"));
                    guest.setPhone(rs.getString("phone"));
                    guest.setIdPassport(rs.getString("id_passport"));
                    guest.setAddress(rs.getString("address"));
                    guests.add(guest);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guests;
    }
    
    public boolean addGuest(Guest guest) {
        String sql = "INSERT INTO Guests (full_name, phone, id_passport, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, guest.getFullName());
            pstmt.setString(2, guest.getPhone());
            pstmt.setString(3, guest.getIdPassport());
            pstmt.setString(4, guest.getAddress());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateGuest(Guest guest) {
        String sql = "UPDATE Guests SET full_name=?, phone=?, id_passport=?, address=? WHERE guest_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, guest.getFullName());
            pstmt.setString(2, guest.getPhone());
            pstmt.setString(3, guest.getIdPassport());
            pstmt.setString(4, guest.getAddress());
            pstmt.setInt(5, guest.getGuestId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteGuest(int guestId) {
        String sql = "DELETE FROM Guests WHERE guest_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, guestId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Guest getGuestById(int guestId) {
        String sql = "SELECT * FROM Guests WHERE guest_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, guestId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Guest guest = new Guest();
                guest.setGuestId(rs.getInt("guest_id"));
                guest.setFullName(rs.getString("full_name"));
                guest.setPhone(rs.getString("phone"));
                guest.setIdPassport(rs.getString("id_passport"));
                guest.setAddress(rs.getString("address"));
                return guest;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
