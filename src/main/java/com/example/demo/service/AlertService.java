package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.AlertRepository;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public Object getTodayAlertCount(String currentTime) {
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
    
}
