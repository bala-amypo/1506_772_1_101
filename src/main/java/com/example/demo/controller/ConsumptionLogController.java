package com.example.demo.controller;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@SecurityRequirement(name="bearerAuth")
public class ConsumptionLogController {

    private final ConsumptionLogService consumptionLogService;

    public ConsumptionLogController(ConsumptionLogService consumptionLogService) {
        this.consumptionLogService = consumptionLogService;
    }

    @PostMapping("/{stockRecordId}")
    public ConsumptionLog create(@PathVariable Long stockRecordId,
                                 @RequestBody ConsumptionLog log) {
        return consumptionLogService.logConsumption(stockRecordId, log);
    }

    @GetMapping("/record/{stockRecordId}")
    public List<ConsumptionLog> getByRecord(@PathVariable Long stockRecordId) {
        return consumptionLogService.getLogsByStockRecord(stockRecordId);
    }

    @GetMapping("/{id}")
    public ConsumptionLog get(@PathVariable Long id) {
        return consumptionLogService.getLog(id);
    }
}
