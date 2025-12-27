package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.ConsumptionLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsumptionLogServiceImpl implements ConsumptionLogService {

    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordRepository stockRecordRepository;

    public ConsumptionLogServiceImpl(ConsumptionLogRepository consumptionLogRepository,
                                     StockRecordRepository stockRecordRepository) {
        this.consumptionLogRepository = consumptionLogRepository;
        this.stockRecordRepository = stockRecordRepository;
    }

    @Override
    @Transactional
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found with id: " + stockRecordId));

        // Validate the log
        if (log.getConsumedQuantity() == null || log.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("Consumed quantity must be positive");
        }

        if (log.getConsumedDate() != null && log.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Consumption date cannot be in the future");
        }

        // Update stock quantity
        Integer currentQuantity = stockRecord.getCurrentQuantity();
        if (currentQuantity < log.getConsumedQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + currentQuantity + 
                                               ", Requested: " + log.getConsumedQuantity());
        }
        
        // Update stock record
        stockRecord.setCurrentQuantity(currentQuantity - log.getConsumedQuantity());
        stockRecord.setLastUpdated(LocalDate.now());
        stockRecordRepository.save(stockRecord);

        // Save consumption log
        log.setStockRecord(stockRecord);
        return consumptionLogRepository.save(log);
    }

    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        // Check if stock record exists
        if (!stockRecordRepository.existsById(stockRecordId)) {
            throw new ResourceNotFoundException("StockRecord not found with id: " + stockRecordId);
        }
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }

    @Override
    public ConsumptionLog getLog(Long id) {
        return consumptionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConsumptionLog not found with id: " + id));
    }
}