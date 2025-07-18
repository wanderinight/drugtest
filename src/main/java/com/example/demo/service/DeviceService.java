package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Device;
import com.example.demo.repository.DeviceRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DeviceService {
    
    @Autowired
    private DeviceRepository deviceRepository;
    // 获取所有设备列表
     public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    // 获取设备总数
    public long getTotalDeviceCount() {
        return deviceRepository.countTotalDevices();
    }

    // 获取D4设备数量
    public long getD4DeviceCount() {
        return deviceRepository.counttype();
    }
    // 获取satffcode对应关注设备列表
    public List<Device> getDeviceByStaffCode(String staffcode) {
        List<Device> devices = deviceRepository.findDevicesByStaffcode(staffcode);
        return devices;
    }
    // 获取所有设备数量
    public Integer getCountAll() {
        return deviceRepository.countAll();
    }

    // 获取待校准设备数量
    public Integer getWaitCalibrationCountNow() {
        return deviceRepository.countWaitCalibrationNow();
    }
    // 获取监控中设备数量
    public Integer getMonitorOnCountNow() {
        return deviceRepository.countMonitorOnNow();
    }
     // 获取监控中断设备数量
    public Integer getMonitorInterruptCountNow() {
        return deviceRepository.countMonitorInterruptNow();

    }
     // 获取未在监控中设备数量
    public Integer getMonitorOffCountNow() {
        return deviceRepository.countMonitorOffNow();

    }

    // 获取警报中设备数量
    public Integer getAlertOnCountNow() {
        return deviceRepository.countAlertOnNow();
    }
    // 获取运行中设备数量
    public Integer getOperationalOnCountNow() {
        return deviceRepository.countOperationalOnNow();
    }

     // 获取待机中设备数量
    public Integer getOperationalOfflineCountNow() {
        return deviceRepository.countOperationalOfflineNow();
    }

    public Device addDevice(Device device) {
        // 直接判断返回值是否为null
        if (deviceRepository.findByDeviceCode(device.getDeviceCode()) != null) {
            throw new IllegalArgumentException("设备编号已存在：" + device.getDeviceCode());
        }
        return deviceRepository.save(device);
    }

    public Device getDeviceByCode(String deviceCode) {
        Device device = deviceRepository.findByDeviceCode(deviceCode);
        if (device == null) {
            throw new IllegalArgumentException("设备不存在：" + deviceCode);
        }
        return device;
    }


    @Transactional
    public void deleteDevice(String deviceCode) {
        Device device = deviceRepository.findByDeviceCode(deviceCode);
        if (device == null) {
            throw new IllegalArgumentException("设备不存在: " + deviceCode);
        }
        // 物理删除（直接删除记录）
        deviceRepository.delete(device);

    }
}
