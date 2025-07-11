package com.example.demo.dto;

public class LoginRequest {
    private String staffcode;
    private String password;

    // Getters and Setters
    public String getStaffcode() {
        return staffcode;
    }

    public void setStaffcode(String staffcode) {
        this.staffcode = staffcode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
