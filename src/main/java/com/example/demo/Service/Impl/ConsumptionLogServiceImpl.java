package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.service.PredictionService;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PredictionServiceImpl implements PredictionService {
    private final PredictionRuleRepository predictionRuleRepository;
    private final StockRecordService stockRecordService;
    private final ConsumptionLogRepository consumptionLogRepository;
    
    @Override
    public PredictionRule createRule(PredictionRule rule) {
        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("averageDaysWindow must be positive");
        }
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("minDailyUsage cannot exceed maxDailyUsage");
        }
        if (rule.getCreatedAt() == null) {
            rule.setCreatedAt(LocalDateTime.now());
        }
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        List<PredictionRule> rules = getAllRules();
        if (rules.isEmpty()) {
            // Default fallback: assume 5 days if no rules configured
            return LocalDate.now().plusDays(5);
        }
        
        PredictionRule rule = rules.get(0); // Use first rule for prediction
        
        // Get consumption logs for the last N days
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(rule.getAverageDaysWindow());
        
        List<ConsumptionLog> logs = consumptionLogRepository
                .findByStockRecordIdAndConsumedDateBetween(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            // No consumption data, use min daily usage for prediction
            int daysUntilEmpty = calculateDaysUntilEmpty(stockRecord, rule.getMinDailyUsage());
            return LocalDate.now().plusDays(daysUntilEmpty);
        }
        
        // Calculate average daily consumption
        double totalConsumed = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        double avgDailyConsumption = totalConsumed / rule.getAverageDaysWindow();
        
        // Apply rule constraints
        avgDailyConsumption = Math.max(rule.getMinDailyUsage(), 
                                     Math.min(rule.getMaxDailyUsage(), avgDailyConsumption));
        
        int daysUntilEmpty = calculateDaysUntilEmpty(stockRecord, avgDailyConsumption);
        return LocalDate.now().plusDays(daysUntilEmpty);
    }
    
    private int calculateDaysUntilEmpty(StockRecord stockRecord, double dailyConsumption) {
        if (dailyConsumption <= 0) {
            return 30; // Default to 30 days if no consumption
        }
        return (int) Math.ceil(stockRecord.getCurrentQuantity() / dailyConsumption);
    }
}