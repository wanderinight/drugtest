package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.repository.AlertRepository;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

     // 默认最大分页大小
    private static final int MAX_PAGE_SIZE = 100;
    public Object getTodayAlertCount(String currentTime) {//时间问题未解决
        // 可以将 currentTime 转换为 LocalDateTime 进行数据库查询
        // 示例：查找在此时间之前触发的告警数量
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
            LocalDateTime parsedTime = LocalDateTime.parse(currentTime, formatter);
            return alertRepository.countToday(parsedTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("时间格式错误，请使用 yyyy-MM-dd HH:mm:ss");//待修改
        }
    }

    public Object getWeekAlertCount(String currentTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
            LocalDateTime parsedTime = LocalDateTime.parse(currentTime, formatter);
            return alertRepository.countWeek(parsedTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("时间格式错误，请使用 yyyy-MM-dd HH:mm:ss");//待修改
        }
    }

      // 获取报警记录详情(分页)
    public Map<String, Object> getAlertDetails(int page, int size) {
        // 限制每页最大数量
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);//page：当前页码
        
        Page<Object[]> results = alertRepository.findAlertDetails(pageable);
        
        List<Map<String, Object>> content = results.getContent().stream()
            .map(record -> Map.of(
                "alertTime", record[0],      // 报警时间
                "deviceCode", record[1],    // 设备编号
                "location", record[2],      // 设备位置
                "alertType", record[3],     // 报警类型(DRUG/MECHINE)
                "alertLevel", record[4],   // 报警等级(YELLOW/RED)
                "context", record[5]        // 报警内容
            ))
            .collect(Collectors.toList());
            
        return Map.of(
            "content", content,                         //当前页数据
            "totalElements", results.getTotalElements(),   // 总记录数
            "totalPages", results.getTotalPages(),       // 总页数
            "currentPage", results.getNumber(),          // 当前页码
            "pageSize", results.getSize()                 // 每页大小
        );
    }

    // 带时间范围的报警记录查询
    public Map<String, Object> getAlertDetailsByTimeRange(
            LocalDateTime startTime, 
            LocalDateTime endTime,
            int page, 
            int size) {
        
          // 1. 参数校验和默认值设置
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(7); // 默认查最近7天
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }

        // 限制最大时间范围为3个月
        if (startTime.isBefore(endTime.minusMonths(3))) {
            startTime = endTime.minusMonths(3);
        }      

        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);
        
        Page<Object[]> results = alertRepository.findAlertDetailsByTimeRange(
            startTime, endTime, pageable);
        
        List<Map<String, Object>> content = results.getContent().stream()
            .map(record -> Map.of(
               "alertTime", record[0],      // 报警时间
                "deviceCode", record[1],    // 设备编号
                "location", record[2],      // 设备位置
                "alertType", record[3],     // 报警类型(DRUG/MECHINE)
                "alertLevel", record[4],   // 报警等级(YELLOW/RED)
                "context", record[5]        // 报警内容
            ))
            .collect(Collectors.toList());
            
        return Map.of(
            "content", content,
            "totalElements", results.getTotalElements(),
            "totalPages", results.getTotalPages(),
            "currentPage", results.getNumber(),
            "pageSize", results.getSize()
        );
    }



    
}
