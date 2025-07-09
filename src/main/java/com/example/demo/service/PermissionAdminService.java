package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.RolePermission;
import com.example.demo.entity.RolePermissionKey;
import com.example.demo.entity.StaffRole;
import com.example.demo.entity.StaffRoleKey;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.StaffRoleRepository;

@Service
public class PermissionAdminService {
    
    @Autowired
    private StaffRoleRepository staffRoleRepo;
    @Autowired
    private RolePermissionRepository rolePermRepo;

    /**
     * 为员工分配角色
     */
    // @Transactional（如果需要事务支持，可以加上）
    public void assignRoleToStaff(Integer staffId, Integer roleId) {
        StaffRole relation = new StaffRole();
        relation.setId(new StaffRoleKey(staffId, roleId));
        staffRoleRepo.save(relation);
    }

    /**
     * 从角色移除权限
     */
    public void removePermissionFromRole(Integer roleId, Integer permId) {
        // rolePermRepo.deleteById(new RolePermissionKey(roleId, permId));
        RolePermission relation = new RolePermission();
        relation.setId(new RolePermissionKey(roleId, permId));
        rolePermRepo.delete(relation);
    }
}
