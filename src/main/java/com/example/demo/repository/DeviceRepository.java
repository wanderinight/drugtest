package com.example.demo.repository;

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

    

}
