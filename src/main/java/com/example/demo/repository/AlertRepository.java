package com.example.demo.repository;

import java.time.LocalDateTime;

import com.example.demo.entity.Alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    // 查询今天的告警数量
    @Query("SELECT COUNT(a) FROM Alert a WHERE DATE(a.alertTime) = DATE(:parsedTime)")
    Long countToday(@Param("parsedTime") LocalDateTime parsedTime);

    //查询一周的告警数量
    @Query("SELECT COUNT(a) FROM Alert a WHERE YEARWEEK(a.alertTime, 1) = YEARWEEK(:parsedTime, 1)")
    Long countWeek(@Param("parsedTime") LocalDateTime parsedTime);
    // 分页查询
    @Query("SELECT a.alertTime, d.deviceCode, d.location, " +
        "a.alertType, a.alertLevel, a.context " +  
        "FROM Alert a JOIN a.device d " +
        "ORDER BY a.alertTime DESC")
    Page<Object[]> findAlertDetails(Pageable pageable);

    // 根据时间范围分页查询报警记录详情
    @Query("SELECT a.alertTime, d.deviceCode, d.location, " +
        "a.alertType, a.alertLevel, a.context " +
        "FROM Alert a JOIN a.device d " +
        "WHERE a.alertTime BETWEEN :startTime AND :endTime " +
        "ORDER BY a.alertTime DESC")
    Page<Object[]> findAlertDetailsByTimeRange(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);

}
