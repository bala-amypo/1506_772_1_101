package com.example.demo.service.impl;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.service.ConsumptionLogService;
import com.example.demo.service.StockRecordService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumptionLogServiceImpl implements ConsumptionLogService {
    
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordService stockRecordService;
    
    @Override
    @Transactional
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog consumptionLog) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        // Validate consumption quantity
        if (consumptionLog.getConsumedQuantity() == null || consumptionLog.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("Consumed quantity must be positive");
        }
        
        // Validate consumption date
        LocalDate consumedDate = consumptionLog.getConsumedDate();
        if (consumedDate == null) {
            consumedDate = LocalDate.now();
            consumptionLog.setConsumedDate(consumedDate);
        }
        
        if (consumedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Consumption date cannot be in the future");
        }
        
        // Check if stock is sufficient
        if (stockRecord.getCurrentQuantity() < consumptionLog.getConsumedQuantity()) {
            throw new IllegalArgumentException(
                "Insufficient stock. Available: " + stockRecord.getCurrentQuantity() + 
                ", Requested: " + consumptionLog.getConsumedQuantity()
            );
        }
        
        // Update stock quantity
        stockRecordService.adjustStockQuantity(stockRecordId, -consumptionLog.getConsumedQuantity());
        
        // Save consumption log
        consumptionLog.setStockRecord(stockRecord);
        return consumptionLogRepository.save(consumptionLog);
    }
    
    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        // Verify stock record exists
        stockRecordService.getStockRecord(stockRecordId);
        
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
    
    @Override
    public List<ConsumptionLog> getLogsByStockRecordAndDateRange(Long stockRecordId, LocalDate startDate, LocalDate endDate) {
        // Validate date range
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        if (startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the future");
        }
        
        // Verify stock record exists
        stockRecordService.getStockRecord(stockRecordId);
        
        return consumptionLogRepository.findByStockRecordIdAndConsumedDateBetween(
            stockRecordId, startDate, endDate
        );
    }
    
    @Override
    public ConsumptionLog getConsumptionLog(Long id) {
        return consumptionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumption log not found with id: " + id));
    }
    
    @Override
    @Transactional
    public void deleteConsumptionLog(Long id) {
        ConsumptionLog consumptionLog = getConsumptionLog(id);
        
        // Restore stock if needed
        StockRecord stockRecord = consumptionLog.getStockRecord();
        stockRecordService.adjustStockQuantity(stockRecord.getId(), consumptionLog.getConsumedQuantity());
        
        consumptionLogRepository.delete(consumptionLog);
    }
    
    @Override
    public List<ConsumptionLog> getRecentConsumptionLogs(Long stockRecordId, int limit) {
        List<ConsumptionLog> logs = consumptionLogRepository.findByStockRecordIdOrderByConsumedDateDesc(stockRecordId);
        return logs.stream().limit(limit).toList();
    }
    
    @Override
    public Double calculateAverageDailyConsumption(Long stockRecordId, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive");
        }
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<ConsumptionLog> logs = getLogsByStockRecordAndDateRange(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            return 0.0;
        }
        
        int totalConsumed = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        long actualDays = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate));
        return (double) totalConsumed / actualDays;
    }
}