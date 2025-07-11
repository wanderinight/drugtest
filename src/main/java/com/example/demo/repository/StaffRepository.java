package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Staff s WHERE s.staffcode = :staffcode")
    boolean existsByStaffcode(String staffcode);

   @Query("SELECT s FROM Staff s WHERE s.staffcode = :staffcode") 
    Staff findByStaffcode(String staffcode);
}
