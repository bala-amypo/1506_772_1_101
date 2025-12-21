package com.example.demo.controller;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/consumption")
public class ConsumptionLogController {
    
    private final ConsumptionLogService consumptionLogService;
    
    @Autowired
    public ConsumptionLogController(ConsumptionLogService consumptionLogService) {
        this.consumptionLogService = consumptionLogService;
    }
    
    @PostMapping("/{stockRecordId}")
    public ResponseEntity<ConsumptionLog> logConsumption(
            @PathVariable Long stockRecordId,
            @RequestBody ConsumptionLog log) {
        ConsumptionLog createdLog = consumptionLogService.logConsumption(stockRecordId, log);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }
    
    @GetMapping("/record/{stockRecordId}")
    public ResponseEntity<List<ConsumptionLog>> getLogsByStockRecord(@PathVariable Long stockRecordId) {
        List<ConsumptionLog> logs = consumptionLogService.getLogsByStockRecord(stockRecordId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ConsumptionLog> getLog(@PathVariable Long id) {
        ConsumptionLog log = consumptionLogService.getLog(id);
        return ResponseEntity.ok(log);
    }
}