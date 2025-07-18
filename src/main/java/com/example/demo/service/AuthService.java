package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.Staff;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.StaffRepository;

@Service
public class AuthService {
    @Autowired
    private  StaffRepository staffRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    
    public Staff register(Staff staff) {
        if (staffRepository.existsByStaffcode(staff.getStaffcode())) {
            throw new RuntimeException("Employee ID already exists");
        }
        
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return staffRepository.save(staff);
    }


     public Staff login(String staffcode, String password) {
        Staff staff = staffRepository.findByStaffcode(staffcode);//还需补充账号不存在情况
        if (staff == null) {
            throw new CustomException("500", "账号不存在");
        }
        if (!passwordEncoder.matches(password, staff.getPassword())) {
            throw new CustomException("500", "账号密码不匹配");
        }
        return staff;
    }


    public Staff getStaffByStaffcode(String staffcode) {
        return staffRepository.findByStaffcode(staffcode);
    }

    // public Staff authenticate(Integer staffId, String password) {
    //     Staff staff = staffRepository.findBystaffId(staffId)
    //             .orElseThrow(() -> new RuntimeException("User not found"));
        
    //     if (!passwordEncoder.matches(password, staff.getPassword())) {
    //         throw new RuntimeException("Invalid password");
    //     }
        
    //     if (!staff.getIsActive()) {
    //         throw new RuntimeException("Account is inactive");
    //     }
        
    //     staff.setLastLoginTime(LocalDateTime.now());
    //     staffRepository.save(staff);
        
    //     return staff;
    // }


}
