package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="staffinfo")
public class Staff {
    @Id
    private Integer staffId;
    private String staffname;
    private String staffcode;
    
    public Integer getStaffId() {
        return staffId;
    }
    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
    public String getStaffname() {
        return staffname;
    }
    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }
    public String getStaffcode() {
        return staffcode;
    }
    public void setStaffcode(String staffcode) {
        this.staffcode = staffcode;
    }

}
