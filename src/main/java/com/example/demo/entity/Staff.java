package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name ="staffinfo")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staffid")
    private Integer staffId;

    // 确保字段名和查询中的 JOIN s.staffRoles 一致
    @OneToMany(mappedBy = "staff")
    private List<StaffRole> staffRoles;

    @Column(name = "staffname")
    private String staffname;

    @Column(name = "staffcode")
    private String staffcode;

    @Column(name = "password")
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
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
