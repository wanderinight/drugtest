package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class RolePermissionKey implements Serializable {
    private Integer roleId;
    private Integer permId;

    public RolePermissionKey(Integer roleId2, Integer permId2) {
        //TODO Auto-generated constructor stub
        this.roleId = roleId2;
        this.permId = permId2;
    }
    public Integer getRoleId() {
        return roleId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public Integer getPermId() {
        return permId;
    }
    public void setPermId(Integer permId) {
        this.permId = permId;
    }
}

