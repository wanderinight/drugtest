package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.example.demo.common.Result;
// import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.Staff;
import com.example.demo.service.UserService;
import com.example.demo.service.PermissionService;
import com.example.demo.utils.JwtTokenUtil;

// 这个控制器用于处理权限相关的请求
// 比如检查某个员工是否有某个权限
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService authService;
    // 注入认证管理器和JWT工具类
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                         JwtTokenUtil jwtTokenUtil,
                         UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }
 

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
    //    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // ADMIN或USER角色可以访问-->权限管理
    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody Staff staff) {
        Staff registeredStaff = authService.register(staff);
        return ResponseEntity.ok(Result.success(registeredStaff));
    }
    
    // @PostMapping("/login")
    // public ResponseEntity<Result> login(@RequestParam String staffcode, 
    //                                  @RequestParam String password) {
    //     Staff staff = authService.login(staffcode, password);
    //     return ResponseEntity.ok(Result.success(staff));
    // }


    @GetMapping("/get")
    public ResponseEntity<Result> getStaff(@RequestParam String staffcode) {
        Staff staff = authService.getStaffByStaffcode(staffcode);
        if (staff != null) {
            return ResponseEntity.ok(Result.success(staff));
        } else {
            return ResponseEntity.badRequest().body(Result.error());
        }
    }
    //DTO事例：
    // @PostMapping("/login")
    // public ResponseEntity<Result> login(@RequestParam String staffcode, 
    //                                  @RequestParam String password) {
    //     Staff staff = authService.login(staffcode, password);
    //     return ResponseEntity.ok(Result.success(staff));
    // }
     @PostMapping("/login")
    public ResponseEntity<?> authlogin(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. 使用原有AuthService验证登录（保持业务逻辑）
            Staff staff = authService.login(loginRequest.getStaffcode(), loginRequest.getPassword());
            
            // 2. 使用Spring Security生成Token
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getStaffcode(), 
                    loginRequest.getPassword())
            );
            
            // 3. 生成包含更多信息的Token
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Map<String, Object> claims = Map.of(
                "staffId", staff.getStaffId(),
                "staffName", staff.getStaffname()
                // "department", staff.getDepartment()
            );

            final String token = jwtTokenUtil.generateToken(userDetails, claims);

            // 4. 返回Token和用户信息（保持原有返回格式）
            return ResponseEntity.ok(Result.success(Map.of(
                "token", token,
                "staff", staff
            )));
            
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(Result.error("401","用户名或密码错误"));
        }
    }
    

    // 请求和响应DTO
    static class LoginRequest {
        private String staffcode;
        private String password;
        public LoginRequest() {}
        // getters and setters
        public String getStaffcode() {
            return staffcode;
        }
        public void setStaffcode(String staffcode) {
            this.staffcode = staffcode;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        
    }

    class JwtResponse {
        private String token;
        
        public JwtResponse(String token) {
            this.token = token;
        }
        // getter
        public String getToken() {
            return token;
        }
    }

}
