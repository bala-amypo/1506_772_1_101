package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumptionLogService {
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordService stockRecordService;
    
    @Transactional
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog consumptionLog) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        if (consumptionLog.getConsumedDate().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }
        
        consumptionLog.setStockRecord(stockRecord);
        
        // Update stock quantity
        stockRecord.setCurrentQuantity(
            stockRecord.getCurrentQuantity() - consumptionLog.getConsumedQuantity()
        );
        
        return consumptionLogRepository.save(consumptionLog);
    }
    
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
}