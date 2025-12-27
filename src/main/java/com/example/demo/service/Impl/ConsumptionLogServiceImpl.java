package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
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
        
        // Validate consumed date is not in future
        if (consumptionLog.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be in the future");
        }
        
        consumptionLog.setStockRecord(stockRecord);
        
        // Update stock quantity
        int newQuantity = stockRecord.getCurrentQuantity() - consumptionLog.getConsumedQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        stockRecordService.updateStockQuantity(stockRecordId, newQuantity);
        
        return consumptionLogRepository.save(consumptionLog);
    }
    
    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
    
    @Override
    public Double calculateAverageDailyConsumption(Long stockRecordId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<ConsumptionLog> logs = consumptionLogRepository
                .findByStockRecordIdAndConsumedDateBetween(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            return 0.0;
        }
        
        int totalConsumption = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        return (double) totalConsumption / days;
    }
}