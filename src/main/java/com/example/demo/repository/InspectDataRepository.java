package com.example.demo.repository;

import com.example.demo.entity.InspectionData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectDataRepository extends JpaRepository<InspectionData, Integer> {
    
    // 获取所有设备的最新检测数据
    @EntityGraph(attributePaths = {"device"})
    @Query("SELECT i FROM InspectionData i " +
           "WHERE i.inspectTime = (SELECT MAX(i2.inspectTime) FROM InspectionData i2 " +
           "                      WHERE i2.device.deviceId = i.device.deviceId " +
           "                      AND i2.isValid = 1)")
    List<InspectionData> findLatestInspections();
    
    // 获取单个设备的最新检测数据
    @EntityGraph(attributePaths = {"device"})
    @Query("SELECT i FROM InspectionData i " +
           "WHERE i.device.deviceCode = :deviceCode AND i.isValid = 1 " +
           "ORDER BY i.inspectTime DESC")
    List<InspectionData> findLatestByDeviceId(String deviceCode);
}
