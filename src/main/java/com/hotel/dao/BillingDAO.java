package com.hotel.dao;

import com.hotel.model.Bill;
import com.hotel.util.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BillingDAO {
    
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        
        // Return demo data if no database connection
        if (!DatabaseConnection.isConnectionAvailable()) {
            return getDemoBills();
        }
        
        String sql = "SELECT b.*, r.reservation_id, g.full_name, rm.room_number " +
            "FROM Billing b " +
            "JOIN Reservations r ON b.reservation_id = r.reservation_id " +
            "JOIN Guests g ON r.guest_id = g.guest_id " +
            "JOIN Rooms rm ON r.room_id = rm.room_id " +
            "ORDER BY b.issued_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setReservationId(rs.getInt("reservation_id"));
                bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                bill.setTax(rs.getBigDecimal("tax"));
                bill.setDiscount(rs.getBigDecimal("discount"));
                bill.setPaymentMethod(rs.getString("payment_method"));
                bill.setPaymentStatus(rs.getString("payment_status"));
                bill.setIssuedDate(rs.getDate("issued_date").toLocalDate());
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return getDemoBills();
        }
        return bills;
    }
    
    private List<Bill> getDemoBills() {
        List<Bill> demoBills = new ArrayList<>();
        demoBills.add(new Bill(1, new BigDecimal("250.00"), new BigDecimal("25.00"), 
            new BigDecimal("0.00"), "Credit Card", "Paid", LocalDate.now().minusDays(1)));
        demoBills.add(new Bill(2, new BigDecimal("180.00"), new BigDecimal("18.00"), 
            new BigDecimal("20.00"), "Cash", "Paid", LocalDate.now().minusDays(2)));
        demoBills.add(new Bill(3, new BigDecimal("450.00"), new BigDecimal("45.00"), 
            new BigDecimal("0.00"), "Credit Card", "Pending", LocalDate.now()));
        
        // Set demo IDs
        for (int i = 0; i < demoBills.size(); i++) {
            demoBills.get(i).setBillId(i + 1);
        }
        
        return demoBills;
    }
    
    public boolean addBill(Bill bill) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            JOptionPane.showMessageDialog(null, "Database not available. Running in demo mode.", 
                "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        String sql = "INSERT INTO Billing (reservation_id, total_amount, tax, discount, payment_method, payment_status, issued_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bill.getReservationId());
            pstmt.setBigDecimal(2, bill.getTotalAmount());
            pstmt.setBigDecimal(3, bill.getTax());
            pstmt.setBigDecimal(4, bill.getDiscount());
            pstmt.setString(5, bill.getPaymentMethod());
            pstmt.setString(6, bill.getPaymentStatus());
            pstmt.setDate(7, Date.valueOf(bill.getIssuedDate()));
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateBill(Bill bill) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            JOptionPane.showMessageDialog(null, "Database not available. Running in demo mode.", 
                "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        String sql = "UPDATE Billing SET total_amount=?, tax=?, discount=?, payment_method=?, payment_status=? WHERE bill_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, bill.getTotalAmount());
            pstmt.setBigDecimal(2, bill.getTax());
            pstmt.setBigDecimal(3, bill.getDiscount());
            pstmt.setString(4, bill.getPaymentMethod());
            pstmt.setString(5, bill.getPaymentStatus());
            pstmt.setInt(6, bill.getBillId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBill(int billId) {
        if (!DatabaseConnection.isConnectionAvailable()) {
            JOptionPane.showMessageDialog(null, "Database not available. Running in demo mode.",
                "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        String sql = "DELETE FROM Billing WHERE bill_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
