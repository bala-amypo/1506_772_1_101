package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;

@RestController
@RequestMapping("/api")
public class ConsumptionLogController {

    @Autowired
    private StockRecordRepository stockRecordRepository;

    @Autowired
    private ConsumptionLogRepository consumptionLogRepository;

    @PostMapping("/consumptions/{stockRecordId}")
    public ResponseEntity<ConsumptionLog> addConsumption(
            @PathVariable Long stockRecordId,
            @RequestBody ConsumptionLog consumptionLog) {

        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new RuntimeException("StockRecord not found"));

        // Set relationship here
        consumptionLog.setStockRecord(stockRecord);

        ConsumptionLog savedLog = consumptionLogRepository.save(consumptionLog);

        return ResponseEntity.ok(savedLog);
    }
}
