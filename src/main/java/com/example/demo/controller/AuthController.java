package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.example.demo.service.PermissionService;

// 这个控制器用于处理权限相关的请求
// 比如检查某个员工是否有某个权限
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/check-permission")
    public ResponseEntity<?> checkPermission(
        @RequestParam Integer staffId,//可能这里从页面读的是code？
        @RequestParam String permName
    ) {
        boolean hasPerm = permissionService.hasPermission(staffId, permName);
        return ResponseEntity.ok(
            Map.of("hasPermission", hasPerm)
        );
    }
}
