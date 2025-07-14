package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.InspectionData;
import com.example.demo.repository.InspectDataRepository;

@Service
public class InspectDataService {
    
    @Autowired
    private InspectDataRepository inspectDataRepository;
     // 获取所有设备的最新检测数据（包含设备信息）
    public List<InspectionData> getLatestInspectionsWithDeviceInfo() {
        return inspectDataRepository.findLatestInspections();
    }
    
    // 获取单个设备的最新检测数据
    public Optional<InspectionData> getLatestInspectionByDeviceId(String deviceCode) {
        List<InspectionData> result = inspectDataRepository.findLatestByDeviceId(deviceCode);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    

}
