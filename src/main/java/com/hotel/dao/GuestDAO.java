package com.hotel.dao;

import com.hotel.model.Guest;
import com.hotel.util.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        if (!DatabaseConnection.isConnectionAvailable()) {
            return getDemoGuests();
        }

        String sql = "SELECT * FROM Guests ORDER BY full_name";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return getDemoGuests();
            }
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
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

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
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

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
        if (!DatabaseConnection.isConnectionAvailable()) {
            showDemoModeMessage();
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return false;

            // Cascade delete at the application layer so we never leave orphan Reservations
            // (and so FK constraints won't block deletion if ON DELETE CASCADE isn't enabled).
            conn.setAutoCommit(false);
            try {
                // Delete RoomService details first (depends on RoomServiceOrders).
                String deleteOrderDetailsSql =
                        "DELETE FROM OrderDetails WHERE order_id IN (" +
                        "  SELECT order_id FROM RoomServiceOrders WHERE reservation_id IN (" +
                        "    SELECT reservation_id FROM Reservations WHERE guest_id=? " +
                        "  )" +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteOrderDetailsSql)) {
                    pstmt.setInt(1, guestId);
                    pstmt.executeUpdate();
                }

                // Delete room-service orders tied to the guest's reservations.
                String deleteRoomServiceOrdersSql =
                        "DELETE FROM RoomServiceOrders WHERE reservation_id IN (" +
                        "  SELECT reservation_id FROM Reservations WHERE guest_id=? " +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteRoomServiceOrdersSql)) {
                    pstmt.setInt(1, guestId);
                    pstmt.executeUpdate();
                }

                // Delete check-in/out and billing tied to the guest's reservations.
                String deleteCheckInOutSql =
                        "DELETE FROM CheckInOut WHERE reservation_id IN (" +
                        "  SELECT reservation_id FROM Reservations WHERE guest_id=? " +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteCheckInOutSql)) {
                    pstmt.setInt(1, guestId);
                    pstmt.executeUpdate();
                }

                String deleteBillingSql =
                        "DELETE FROM Billing WHERE reservation_id IN (" +
                        "  SELECT reservation_id FROM Reservations WHERE guest_id=? " +
                        ")";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteBillingSql)) {
                    pstmt.setInt(1, guestId);
                    pstmt.executeUpdate();
                }

                // Delete reservations tied to this guest.
                String deleteReservationsSql = "DELETE FROM Reservations WHERE guest_id=?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteReservationsSql)) {
                    pstmt.setInt(1, guestId);
                    pstmt.executeUpdate();
                }

                // Finally delete the guest row.
                String deleteGuestSql = "DELETE FROM Guests WHERE guest_id=?";
                int updated;
                try (PreparedStatement pstmt = conn.prepareStatement(deleteGuestSql)) {
                    pstmt.setInt(1, guestId);
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
    
    public Guest getGuestById(int guestId) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            return getDemoGuests().stream()
                    .filter(guest -> guest.getGuestId() == guestId)
                    .findFirst()
                    .orElse(null);
        }

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

    private List<Guest> getDemoGuests() {
        List<Guest> demoGuests = new ArrayList<>();
        demoGuests.add(createDemoGuest(1, "Sok Dara", "+855-12-345-678", "K12345678", "Phnom Penh, Cambodia"));
        demoGuests.add(createDemoGuest(2, "Chantha Srey", "+855-15-987-654", "K87654321", "Siem Reap, Cambodia"));
        demoGuests.add(createDemoGuest(3, "Vannak Rith", "+855-10-112-233", "K11223344", "Battambang, Cambodia"));
        demoGuests.add(createDemoGuest(4, "Sopheaktra Ly", "+855-77-445-566", "K55667788", "Kampot, Cambodia"));
        demoGuests.add(createDemoGuest(5, "Pisey Heng", "+855-93-778-899", "K99887766", "Sihanoukville, Cambodia"));
        return demoGuests;
    }

    private Guest createDemoGuest(int id, String fullName, String phone, String idPassport, String address) {
        Guest guest = new Guest(fullName, phone, idPassport, address);
        guest.setGuestId(id);
        return guest;
    }

    private void showDemoModeMessage() {
        JOptionPane.showMessageDialog(null,
                "Database not available. Guest changes are disabled in demo mode.",
                "Demo Mode",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
