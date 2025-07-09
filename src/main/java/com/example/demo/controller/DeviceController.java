package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DeviceService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
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
}
