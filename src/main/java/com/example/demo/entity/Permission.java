package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="permissioninfo")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissionid")
    private Integer permissionId;

    @Column(name = "permission")
    private String permission;
    
    public Integer getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
}
