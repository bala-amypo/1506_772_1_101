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

    // POST - log consumption
    @PostMapping("/{stockRecordId}")
    public ConsumptionLog logConsumption(
            @PathVariable Long stockRecordId,
            @RequestBody ConsumptionLog log) {
        return consumptionLogService.logConsumption(stockRecordId, log);
    }

    // GET - list logs for stock record
    @GetMapping("/record/{stockRecordId}")
    public List<ConsumptionLog> getLogsByStockRecord(@PathVariable Long stockRecordId) {
        return consumptionLogService.getLogsByStockRecord(stockRecordId);
    }

    // GET - get log by id
    @GetMapping("/{id}")
    public ConsumptionLog getLog(@PathVariable Long id) {
        return consumptionLogService.getLog(id);
    }
}
