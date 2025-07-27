package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // 今日报警数量
    @PostMapping("/count-today")
    public ResponseEntity<Result> alertcounttoday(@RequestBody Map<String, String> payload) {
         String currentTime = payload.get("currentTime");  // 前端发送的当前时间字符串
    return ResponseEntity.ok(Result.success(alertService.getTodayAlertCount(currentTime)));
    }
   // 一周报警数量---待完成时间上的问题
    @PostMapping("/count-week")
    public ResponseEntity<Result> alertCountWeek(@RequestBody Map<String, String> payload) {
        String currentTime = payload.get("currentTime");  // 前端发送的当前时间字符串
        return ResponseEntity.ok(Result.success(alertService.getWeekAlertCount(currentTime)));
    }

   // 分页获取报警记录详情（报警时间-设备编号-设备位置-报警内容）
    @GetMapping("/details")
    public ResponseEntity<Result> getAlertDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(Result.success(alertService.getAlertDetails(page, size)));
    }

    // 带时间范围的分页查询---防止请求过大
    @GetMapping("/details-by-time-range")
    public ResponseEntity<Result> getAlertDetailsByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);
        
        return ResponseEntity.ok(Result.success(
            alertService.getAlertDetailsByTimeRange(start, end, page, size)
        ));
    }

}
