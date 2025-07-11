package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class RolePermissionKey implements Serializable {
    private Integer roleId;
    private Integer permissionId;

    public RolePermissionKey() {}
    public RolePermissionKey(Integer roleId2, Integer permissionId2) {

        this.roleId = roleId2;
        this.permissionId = permissionId2;
    }

    @Override
        public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePermissionKey that = (RolePermissionKey) o;

        if (!roleId.equals(that.roleId)) return false;
        return permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        int result = roleId.hashCode();
        result = 31 * result + permissionId.hashCode();
        return result;
    }
    public Integer getRoleId() {
        return roleId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public Integer getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}

