package com.example.demo.controller;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import com.example.demo.common.Result;
import com.example.demo.entity.InspectionData;
import com.example.demo.service.InspectDataService;
import com.example.demo.service.ReportService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/inspect-data")
public class InspectDataController {
    @Autowired
    private InspectDataService inspectDataService;

    @Autowired
    private ReportService reportService;
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
    //返回时间段内的检测数据
    @GetMapping("/details-by-time-range")
    public ResponseEntity<Result> getInspectDataBytime(
            // @RequestParam String startTime, 
            // @RequestParam String endTime) throws Exception
        @RequestParam 
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
        LocalDateTime startTime, 

        @RequestParam 
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
        LocalDateTime endTime) throws Exception {

        ZonedDateTime start = startTime.atZone(ZonedDateTime.now().getZone());
        ZonedDateTime end = endTime.atZone(ZonedDateTime.now().getZone());
        
        List<InspectionData> inspection = inspectDataService.getInspectionsByTimeRange(start, end);
        return ResponseEntity.ok(Result.success(inspection));

    }
    
    
    //返回检测报告批-次-产品-时间
     // 生成按批次的报告并下载
    @GetMapping("/report/batch")
    public ResponseEntity<byte[]> generateBatchReport(@RequestParam String batchNo) throws Exception {
        List<InspectionData> dataList = inspectDataService.getInspectionsByBatch(batchNo);
        
        // 生成PDF报告
        byte[] pdfBytes = reportService.generatePdfReport(dataList, "按批次报告", "批次号: " + batchNo);
        
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);//告诉浏览器返回一个pdf文件
        headers.setContentDispositionFormData("attachment", //attachment表示下载
            "药品检测报告_批次_" + batchNo + "_" + UUID.randomUUID().toString().substring(0, 8) + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
    // 生成按产品的报告并下载
    @GetMapping("/report/product")
    public ResponseEntity<byte[]> generateProductReport(@RequestParam String productId) throws Exception {
        List<InspectionData> dataList = inspectDataService.getInspectionsByProduct(productId);
        
        // 生成PDF报告
        byte[] pdfBytes = reportService.generatePdfReport(dataList, "按产品报告", "产品ID: " + productId);
        
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "药品检测报告_产品_" + productId + "_" + UUID.randomUUID().toString().substring(0, 8) + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
    // 生成按时间范围的报告并下载
    @GetMapping("/report/time-range")//时间模式！！！！！
    public ResponseEntity<byte[]> generateTimeRangeReport(
            // @RequestParam String startTime, 
            // @RequestParam String endTime) throws Exception
        @RequestParam 
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
        LocalDateTime startTime, 

        @RequestParam 
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
        LocalDateTime endTime) throws Exception {

        ZonedDateTime start = startTime.atZone(ZonedDateTime.now().getZone());
        ZonedDateTime end = endTime.atZone(ZonedDateTime.now().getZone());
        
        List<InspectionData> dataList = inspectDataService.getInspectionsByTimeRange(start, end);
        
        // 生成PDF报告
        byte[] pdfBytes = reportService.generatePdfReport(dataList, "按时间范围报告", 
            "时间范围: " + start.toString() + " 至 " + end.toString());
        
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "药品检测报告_时间范围_" + start.toLocalDate() + "_至_" + end.toLocalDate() + "_" + 
            UUID.randomUUID().toString().substring(0, 8) + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
