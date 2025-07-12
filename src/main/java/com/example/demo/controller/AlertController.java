package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Result;
import com.example.demo.entity.Staff;
import com.example.demo.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    // Define your endpoints here
    @PostMapping("/count-today")
    public ResponseEntity<Result> alertcounttoday(@RequestBody Map<String, String> payload) {
         String currentTime = payload.get("currentTime");  // 前端发送的当前时间字符串
    return ResponseEntity.ok(Result.success(alertService.getTodayAlertCount(currentTime)));
    }
    
}
