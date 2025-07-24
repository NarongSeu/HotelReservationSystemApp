package com.hotel.model;

public class Guest {
    private int guestId;
    private String fullName;
    private String phone;
    private String idPassport;
    private String address;
    
    // Constructors
    public Guest() {}
    
    public Guest(String fullName, String phone, String idPassport, String address) {
        this.fullName = fullName;
        this.phone = phone;
        this.idPassport = idPassport;
        this.address = address;
    }
    
    // Getters and Setters
    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getIdPassport() { return idPassport; }
    public void setIdPassport(String idPassport) { this.idPassport = idPassport; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    @Override
    public String toString() {
        return fullName + " (" + phone + ")";
    }
}
