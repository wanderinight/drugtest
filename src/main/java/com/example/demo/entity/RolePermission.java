package com.example.demo.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "roleprm")
public class RolePermission {
    @EmbeddedId
    private RolePermissionKey id;  // 复合主键类

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "roleid")
    private Role role;

    @ManyToOne
    @MapsId("permId")
    @JoinColumn(name = "permid")
    private Permission permission;

    public RolePermissionKey getId() {
        return id;
    }

    public void setId(RolePermissionKey id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
