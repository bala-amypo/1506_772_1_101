/*package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ConsumptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumptionLogServiceImpl implements ConsumptionLogService {

    private final ConsumptionLogRepository logRepository;
    private final StockRecordRepository stockRepository;

    @Override
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log) {

        StockRecord stockRecord = stockRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));

        if (log.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid consumed quantity");
        }

        if (log.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }

        log.setStockRecord(stockRecord);
        return logRepository.save(log);
    }

    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        return logRepository.findByStockRecordId(stockRecordId);
    }

    @Override
    public ConsumptionLog getLog(Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConsumptionLog not found"));
    }
}
*/
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.ConsumptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumptionLogServiceImpl implements ConsumptionLogService {
    
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordRepository stockRecordRepository;
    
    @Override
    @Transactional
    public ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        if (log.getConsumedQuantity() <= 0) {
            throw new IllegalArgumentException("Consumed quantity must be greater than zero");
        }
        
        if (log.getConsumedDate() == null) {
            log.setConsumedDate(LocalDate.now());
        }
        
        if (log.getConsumedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }
        
        // Update stock record quantity
        int newQuantity = stockRecord.getCurrentQuantity() - log.getConsumedQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        stockRecord.setCurrentQuantity(newQuantity);
        stockRecord.setLastUpdated(LocalDateTime.now());
        stockRecordRepository.save(stockRecord);
        
        log.setStockRecord(stockRecord);
        log.setLoggedAt(LocalDateTime.now());
        
        return consumptionLogRepository.save(log);
    }
    
    @Override
    public ConsumptionLog getLog(Long id) {
        return consumptionLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ConsumptionLog not found"));
    }
    
    @Override
    public List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId) {
        return consumptionLogRepository.findByStockRecordId(stockRecordId);
    }
    
    @Override
    @Transactional
    public void deleteLog(Long id) {
        if (!consumptionLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("ConsumptionLog not found");
        }
        consumptionLogRepository.deleteById(id);
    }
}