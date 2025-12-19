package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ConsumptionLog;
import com.example.demo.entity.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.StockRecordRepository;

@Service
public class ConsumptionLogServiceImpl implements ConsumptionLogService {

    @Autowired
    private ConsumptionLogRepository consumptionLogRepository;

    @Autowired
    private StockRecordRepository stockRecordRepository;

    @Override
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log) {

        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId).orElse(null);
        if (stockRecord == null) return null;

        if (log.getConsumedQuantity() <= 0) return null;

        if (log.getConsumedDate().isAfter(LocalDate.now())) return null;

        log.setStockRecord(stockRecord);

        return consumptionLogRepository.save(log);
    }

    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId).orElse(null);
        if (stockRecord == null) return List.of();

        return consumptionLogRepository.findByStockRecord(stockRecord);
    }

    @Override
    public ConsumptionLog getLog(Long id) {
        return consumptionLogRepository.findById(id).orElse(null);
    }
}
