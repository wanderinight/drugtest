package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.RolePermission;
import com.example.demo.entity.RolePermissionKey;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionKey> {
    
    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN true ELSE false END " +
           "FROM RolePermission rp " +
           "WHERE rp.role.roleId = :roleId AND rp.permission.permission = :permName")
    boolean existsByRoleIdAndPermissionName(Integer roleId, String permName);
}