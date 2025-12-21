package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {
    
    private final PredictionRuleRepository predictionRuleRepository;
    private final StockRecordRepository stockRecordRepository;
    private final ConsumptionLogRepository consumptionLogRepository;
    
    @Autowired
    public PredictionServiceImpl(
            PredictionRuleRepository predictionRuleRepository,
            StockRecordRepository stockRecordRepository,
            ConsumptionLogRepository consumptionLogRepository) {
        this.predictionRuleRepository = predictionRuleRepository;
        this.stockRecordRepository = stockRecordRepository;
        this.consumptionLogRepository = consumptionLogRepository;
    }
    
    @Override
    @Transactional
    public PredictionRule createRule(PredictionRule rule) {
        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Average days window must be greater than zero");
        }
        
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Minimum daily usage must be less than or equal to maximum daily usage");
        }
        
        if (predictionRuleRepository.findByRuleName(rule.getRuleName()).isPresent()) {
            throw new IllegalArgumentException("Rule name must be unique");
        }
        
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        List<ConsumptionLog> logs = consumptionLogRepository.findByStockRecordId(stockRecordId);
        
        if (logs.isEmpty()) {
            throw new IllegalArgumentException("No consumption logs available for prediction");
        }
        
        // Get default rule or first rule
        PredictionRule rule = predictionRuleRepository.findAll().stream()
                .findFirst()
                .orElse(PredictionRule.builder()
                        .averageDaysWindow(30)
                        .minDailyUsage(1)
                        .maxDailyUsage(100)
                        .build());
        
        // Calculate average daily consumption
        double totalConsumption = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        long daysBetween = ChronoUnit.DAYS.between(
                logs.get(0).getConsumedDate(),
                logs.get(logs.size() - 1).getConsumedDate()
        );
        
        if (daysBetween == 0) daysBetween = 1;
        
        double averageDailyConsumption = totalConsumption / daysBetween;
        
        // Calculate days until reorder threshold
        int remainingQuantity = stockRecord.getCurrentQuantity() - stockRecord.getReorderThreshold();
        if (remainingQuantity <= 0) {
            return LocalDate.now(); // Already at or below threshold
        }
        
        double daysUntilReorder = remainingQuantity / averageDailyConsumption;
        
        return LocalDate.now().plusDays((long) Math.ceil(daysUntilReorder));
    }
}
