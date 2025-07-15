package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Result;
import com.example.demo.entity.Device;
import com.example.demo.entity.Staff;
import com.example.demo.service.DeviceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/device")
public class DeviceController {
    
    @Autowired
    private DeviceService deviceService;
    
    @GetMapping("/getall")
    public ResponseEntity<Result> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(Result.success(devices));
    }

    @GetMapping("/stats")
    public Map<String, Long> getDeviceStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalDevices", deviceService.getTotalDeviceCount());
        stats.put("D4Devices", deviceService.getD4DeviceCount());
        return stats;
    }

    @GetMapping("/follow")// 获取关注设备列表
   public ResponseEntity<Result> getDevicefollow(@RequestParam String staffcode) {
        List<Device> device= deviceService.getDeviceByStaffCode(staffcode);
        // 提取所有设备的 devicecode 并转换为字符串列表
    // List<String> deviceCodes = devices.stream()
    //                                   .map(Device::getDevicecode)
    //                                   .toList();  // Java 16+ 使用 toList()，低版本可用 Collectors.toList()
        return ResponseEntity.ok(Result.success(device));
    }
    
    @GetMapping("/countall")// 获取所有设备数量
     public ResponseEntity<Result> getcountall() {
        return ResponseEntity.ok(Result.success(deviceService.getCountAll()));
    }
    
    @GetMapping("/waitcalibration-countnow")// 获取待校准设备数量
     public ResponseEntity<Result> getWaitCalibrationCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getWaitCalibrationCountNow()));
    }

    @GetMapping("/monitoron-countnow")// 获取监控中设备数量
     public ResponseEntity<Result> getMonitorOnCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getMonitorOnCountNow()));
    }
    @GetMapping("/monitorinterrupt-countnow")// 获取监控中断设备数量
     public ResponseEntity<Result> getMonitorInterruptCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getMonitorInterruptCountNow()));
    }
    @GetMapping("/monitoroff-countnow")// 获取未在监控中设备数量
     public ResponseEntity<Result> getMonitorOffCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getMonitorOffCountNow()));
    }
    @GetMapping("/alerton-countnow")// 获取告警中设备数量
     public ResponseEntity<Result> getAlertOnCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getAlertOnCountNow()));
    }
    @GetMapping("/operationalon-countnow")// 获取运行中设备数量
     public ResponseEntity<Result> getOperationalOnCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getOperationalOnCountNow()));
    }
    @GetMapping("/operationaloffline-countnow")// 获取待机设备数量
    public ResponseEntity<Result> getOperationalOfflineCountNow() {
        return ResponseEntity.ok(Result.success(deviceService.getOperationalOfflineCountNow()));
    }
    //查询设备校准情况----有待讨论，到底要什么情况？？？
}
