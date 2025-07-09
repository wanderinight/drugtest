package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "devices")
public class Device {
    
    @Id
    @Column(name = "device_id", nullable = false)
    private Integer deviceId;  // 如果改为自增，应使用 @GeneratedValue
    
    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Column(name = "device_name", length = 50, nullable = false)
    private String deviceName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, columnDefinition = "ENUM('D4','DB')")
    private DeviceType deviceType;
    
    @Column(name = "location", length = 100)
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", nullable = false, columnDefinition = "ENUM('running','disconnected','offline')")
    private OperationalStatus operationalStatus = OperationalStatus.OFFLINE;
    
    @Column(name = "last_data_time")
    private LocalDateTime lastDataTime;
    
    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    @Column(name = "last_calibration")
    private LocalDateTime lastCalibration;
    
    @Column(name = "next_calibration")
    private LocalDateTime nextCalibration;
    
    @Column(name = "calibration_operator", length = 20)
    private String calibrationOperator;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

   // createdAt的特殊处理（通常在@PrePersist中设置）
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
   

    // 枚举定义
    public enum DeviceType {
        D4, DB
    }

    public enum OperationalStatus {
        RUNNING, DISCONNECTED, OFFLINE
    }

    

    
 
}