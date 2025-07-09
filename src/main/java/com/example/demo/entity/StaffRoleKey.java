package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class StaffRoleKey implements Serializable{
    public StaffRoleKey(Integer staffId2, Integer roleId2) {
        //TODO Auto-generated constructor stub
        this.staffId = staffId2;
        this.roleId = roleId2;
    }
    private Integer staffId;
    private Integer roleId;
    public Integer getStaffId() {
        return staffId;
    }
    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
    public Integer getRoleId() {
        return roleId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
