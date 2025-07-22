package com.example.demo.controller;

import com.example.demo.dto.AddUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Role;
import com.example.demo.entity.Staff;
import com.example.demo.service.PermissionAdminService;
import com.example.demo.common.Result;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    @Autowired
    private PermissionAdminService adminService;

    /**
     * 为员工更新角色（替换现有角色）
     */
    @PostMapping("/update/{staffId}/role/{roleId}")
    public ResponseEntity<Result> updateRole( // 方法名从assignOrUpdateRole改为updateRole
                                              @PathVariable Integer staffId,
                                              @PathVariable Integer roleId
    ) {
        adminService.updateRoleForStaff(staffId, roleId); // 调用服务层新方法名
        return ResponseEntity.ok(Result.success("角色更新成功"));
    }

    /**
     * 添加新用户（同时分配角色）
     */
    @PostMapping("/add")
    public ResponseEntity<Result> addUser(@RequestBody AddUserRequest request) {
        Staff savedStaff = adminService.addUserAndAssignRole(request.getStaff(), request.getRoleId());
        return ResponseEntity.ok(Result.success(savedStaff));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<Result> deleteUser(@PathVariable Integer staffId) {
        adminService.deleteUser(staffId);
        return ResponseEntity.ok(Result.success("用户删除成功"));
    }

    /**
     * 查询用户当前角色
     */
    @GetMapping("/{staffId}/role")
    public ResponseEntity<Result> getUserRole(@PathVariable Integer staffId) {
        Role role = adminService.getUserRole(staffId);
        return ResponseEntity.ok(Result.success(role));
    }
}