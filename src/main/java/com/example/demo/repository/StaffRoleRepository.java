package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Role;
import com.example.demo.entity.StaffRole;
import com.example.demo.entity.StaffRoleKey;

public interface StaffRoleRepository extends JpaRepository<StaffRole, StaffRoleKey> {
    
    @Query("SELECT sr.role FROM StaffRole sr WHERE sr.staff.staffId = :staffId")
    List<Role> findRolesByStaffId(Integer staffId);
}
