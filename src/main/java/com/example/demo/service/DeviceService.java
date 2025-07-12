package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Device;
import com.example.demo.repository.DeviceRepository;


@Service
public class DeviceService {
    
    @Autowired
    private DeviceRepository deviceRepository;

    // 获取设备总数
    public long getTotalDeviceCount() {
        return deviceRepository.countTotalDevices();
    }

    // 获取D4设备数量
    public long getD4DeviceCount() {
        return deviceRepository.counttype();
    }

    public List<Device> getDeviceByStaffCode(String staffcode) {
        List<Device> devices = deviceRepository.findDevicesByStaffcode(staffcode);
        return devices;
    }

    public Integer getCountAll() {
        return deviceRepository.countAll();
    }

}
