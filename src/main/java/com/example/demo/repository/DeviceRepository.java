package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    // 查询设备总数
    @Query("SELECT COUNT(d) FROM Device d")
    Long countTotalDevices();
    
    // 查询D4设备数量D
    @Query("SELECT COUNT(d) FROM Device d WHERE d.deviceType = 'D4'")
    Long counttype();

    // 查询staffcode关注的设备列表
    @Query("SELECT sd.device FROM StaffDevice sd WHERE sd.staff.staffcode = :staffcode")
    List<Device> findDevicesByStaffcode(String staffcode);

    // 查询所有设备数量
    @Query("SELECT COUNT(d) FROM Device d")
    Integer countAll();

}
