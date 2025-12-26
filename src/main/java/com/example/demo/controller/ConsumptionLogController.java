package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;

@RestController
@RequestMapping("/api/consumption")
public class ConsumptionLogController {

    @Autowired
    private ConsumptionLogService consumptionLogService;

    @PostMapping("/{stockRecordId}")
    public ConsumptionLog addLog(
            @PathVariable Long stockRecordId,
            @RequestBody ConsumptionLog log) {

        return consumptionLogService.logConsumption(stockRecordId, log);
    }

    @GetMapping("/record/{stockRecordId}")
    public List<ConsumptionLog> getByStockRecord(@PathVariable Long stockRecordId) {
        return consumptionLogService.getLogsByStockRecord(stockRecordId);
    }

    @gGetMapping("/{id}")
    public ConsumptionLog getLog(@PathVariable Long id) {
        return consumptionLogService.getLog(id);
    }
}
