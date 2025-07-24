package com.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {
    private int billId;
    private int reservationId;
    private BigDecimal totalAmount;
    private BigDecimal tax;
    private BigDecimal discount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDate issuedDate;
    
    // Constructors
    public Bill() {}
    
    public Bill(int reservationId, BigDecimal totalAmount, BigDecimal tax, BigDecimal discount, 
                String paymentMethod, String paymentStatus, LocalDate issuedDate) {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.discount = discount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.issuedDate = issuedDate;
    }
    
    // Getters and Setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }
    
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDate getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }
}
