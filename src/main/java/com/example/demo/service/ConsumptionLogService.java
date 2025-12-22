package com.example.demo.service;

import java.util.List;

import com.example.demo.model.ConsumptionLog;

public interface ConsumptionLogService {
    ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log);
    List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId);
    ConsumptionLog getLog(Long id);
}

