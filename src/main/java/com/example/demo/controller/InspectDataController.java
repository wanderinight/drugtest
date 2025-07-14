package com.example.demo.controller;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Result;
import com.example.demo.entity.InspectionData;
import com.example.demo.service.InspectDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/inspect-data")
public class InspectDataController {
    @Autowired
    private InspectDataService inspectDataService;

    // 返回所有设备的最新检测数据
    @GetMapping("/showrecent-all")
    public ResponseEntity<Result> getLatestInspections() {
        List<InspectionData> inspections = inspectDataService.getLatestInspectionsWithDeviceInfo();
        return ResponseEntity.ok(Result.success(inspections));
    }
    
    // 返回指定设备的最新检测数据
    @GetMapping("/showrecent-bydevice")
    public ResponseEntity<Result> getLatestByDevice(@RequestParam String deviceCode) {
        Optional<InspectionData> inspection = inspectDataService.getLatestInspectionByDeviceId(deviceCode);
        return inspection.map(i -> ResponseEntity.ok(Result.success(i)))
                         .orElseGet(() -> ResponseEntity.ok(Result.error("200","未找到该设备的检测数据")));
    }
    
    //返回检测报告批-次-产品-时间
}
