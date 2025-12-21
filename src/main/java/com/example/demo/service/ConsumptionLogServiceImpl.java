package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.ConsumptionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ConsumptionLogServiceImpl implements ConsumptionLogService {
    
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordRepository stockRecordRepository;
    
    @Autowired
    public ConsumptionLogServiceImpl(
            ConsumptionLogRepository consumptionLogRepository,
            StockRecordRepository stockRecordRepository) {
        this.consumptionLogRepository = consumptionLogRepository;
        this.stockRecordRepository = stockRecordRepository;
    }
    
    @Override
    @Transactional
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        if (log.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("Consumed quantity must be greater than zero");
        }
        
        if (log.getConsumedDate() == null || log.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }
        
        log.setStockRecord(stockRecord);
        
        return consumptionLogRepository.save(log);
    }
    
    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
    
    @Override
    public ConsumptionLog getLog(Long id) {
        return consumptionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConsumptionLog not found"));
    }
}
