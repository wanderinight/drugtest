package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class StaffRoleKey implements Serializable{
    public StaffRoleKey(Integer staffId2, Integer roleId2) {
        
        this.staffId = staffId2;
        this.roleId = roleId2;
    }
    private Integer staffId;
    private Integer roleId;

    public StaffRoleKey() {}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StaffRoleKey that = (StaffRoleKey) o;

        if (!staffId.equals(that.staffId)) return false;
        return roleId.equals(that.roleId);
    }

    @Override
    public int hashCode() {
        int result = staffId.hashCode();
        result = 31 * result + roleId.hashCode();
        return result;
    }

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
