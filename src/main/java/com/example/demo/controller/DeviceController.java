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
    
    @GetMapping("/stats")
    public Map<String, Long> getDeviceStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalDevices", deviceService.getTotalDeviceCount());
        stats.put("D4Devices", deviceService.getD4DeviceCount());
        return stats;
    }

    @PostMapping("/follow")
   public ResponseEntity<Result> getDevicefollow(@RequestParam String staffcode) {
        List<Device> device= deviceService.getDeviceByStaffCode(staffcode);
        // 提取所有设备的 devicecode 并转换为字符串列表
    // List<String> deviceCodes = devices.stream()
    //                                   .map(Device::getDevicecode)
    //                                   .toList();  // Java 16+ 使用 toList()，低版本可用 Collectors.toList()
        return ResponseEntity.ok(Result.success(device));
    }
    
    @GetMapping("/countall")
     public ResponseEntity<Result> getcountall() {
        return ResponseEntity.ok(Result.success(deviceService.getCountAll()));
    }
    


}
