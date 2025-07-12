package com.example.demo.repository;

import java.time.LocalDateTime;

import com.example.demo.entity.Alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    // 查询今天的告警数量
    @Query("SELECT COUNT(a) FROM Alert a WHERE DATE(a.alertTime) = DATE(:parsedTime)")
    Long countToday(@Param("parsedTime") LocalDateTime parsedTime);

}
