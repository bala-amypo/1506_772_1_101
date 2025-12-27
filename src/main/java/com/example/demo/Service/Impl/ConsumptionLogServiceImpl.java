package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.service.ConsumptionLogService;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumptionLogServiceImpl implements ConsumptionLogService {
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordService stockRecordService;
    
    @Override
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog consumptionLog) {
        if (consumptionLog.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }
        
        if (consumptionLog.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("consumedQuantity must be positive");
        }
        
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        // Check if sufficient stock exists
        if (stockRecord.getCurrentQuantity() < consumptionLog.getConsumedQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + stockRecord.getCurrentQuantity());
        }
        
        // Update stock quantity
        int newQuantity = stockRecord.getCurrentQuantity() - consumptionLog.getConsumedQuantity();
        stockRecord.setCurrentQuantity(newQuantity);
        stockRecord.setLastUpdated(LocalDate.now().atStartOfDay());
        
        consumptionLog.setStockRecord(stockRecord);
        
        return consumptionLogRepository.save(consumptionLog);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        stockRecordService.getStockRecord(stockRecordId); // Validate stock record exists
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
}