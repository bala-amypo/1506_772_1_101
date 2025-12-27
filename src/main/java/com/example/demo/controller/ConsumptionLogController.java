package com.example.demo.controller;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
public class ConsumptionLogController {
    private final ConsumptionLogService consumptionLogService;
    
    @PostMapping("/{stockRecordId}")
    public ResponseEntity<ConsumptionLog> logConsumption(
            @PathVariable Long stockRecordId,
            @Valid @RequestBody ConsumptionLog consumptionLog) {
        return ResponseEntity.ok(consumptionLogService.logConsumption(stockRecordId, consumptionLog));
    }
    
    @GetMapping("/record/{stockRecordId}")
    public ResponseEntity<List<ConsumptionLog>> getLogsByStockRecord(@PathVariable Long stockRecordId) {
        return ResponseEntity.ok(consumptionLogService.getLogsByStockRecord(stockRecordId));
    }
}