package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "staffrole")
public class StaffRole {
    @EmbeddedId
    @Column(name = "id")
    private StaffRoleKey id;  // 复合主键类

    @ManyToOne
    @MapsId("staffId")
    @JoinColumn(name = "staffid")
    private Staff staff;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "roleid")
    private Role role;

    public void setId(StaffRoleKey staffRoleKey) {
        
        this.id = staffRoleKey;
    }

    public StaffRoleKey getId() {
        return id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
