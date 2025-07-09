package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PermissionAdminService;

// 这个控制器用于处理staff相关

@RestController
@RequestMapping("/api/admin/permissions")//api/user
public class UserController {
    
    @Autowired
    private PermissionAdminService adminService;

    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(
        @RequestBody AssignRoleRequest request
    ) {
        adminService.assignRoleToStaff(request.staffId(), request.roleId());
        return ResponseEntity.ok().build();
    }
}

// DTO示例
record AssignRoleRequest(Integer staffId, Integer roleId) {}
