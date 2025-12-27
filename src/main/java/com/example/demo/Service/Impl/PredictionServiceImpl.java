package com.example.demo.service.impl;

import com.example.demo.model.PredictionRule;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.service.PredictionService;
import com.example.demo.service.StockRecordService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    
    private final PredictionRuleRepository predictionRuleRepository;
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordService stockRecordService;
    
    @Override
    @Transactional
    public PredictionRule createRule(PredictionRule rule) {
        // Validate rule
        if (rule.getRuleName() == null || rule.getRuleName().trim().isEmpty()) {
            throw new IllegalArgumentException("Rule name is required");
        }
        
        if (rule.getAverageDaysWindow() == null || rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Average days window must be positive");
        }
        
        if (rule.getMinDailyUsage() == null || rule.getMinDailyUsage() < 0) {
            throw new IllegalArgumentException("Minimum daily usage must be non-negative");
        }
        
        if (rule.getMaxDailyUsage() == null || rule.getMaxDailyUsage() < 0) {
            throw new IllegalArgumentException("Maximum daily usage must be non-negative");
        }
        
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Minimum daily usage cannot exceed maximum daily usage");
        }
        
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    public PredictionRule getRule(Long id) {
        return predictionRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prediction rule not found with id: " + id));
    }
    
    @Override
    @Transactional
    public PredictionRule updateRule(Long id, PredictionRule ruleDetails) {
        PredictionRule rule = getRule(id);
        
        rule.setRuleName(ruleDetails.getRuleName());
        rule.setAverageDaysWindow(ruleDetails.getAverageDaysWindow());
        rule.setMinDailyUsage(ruleDetails.getMinDailyUsage());
        rule.setMaxDailyUsage(ruleDetails.getMaxDailyUsage());
        
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    @Transactional
    public void deleteRule(Long id) {
        PredictionRule rule = getRule(id);
        predictionRuleRepository.delete(rule);
    }
    
    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        // Get the default prediction rule
        PredictionRule defaultRule = getDefaultRule();
        
        // Get consumption logs for the specified window
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(defaultRule.getAverageDaysWindow());
        
        List<ConsumptionLog> logs = consumptionLogRepository
                .findByStockRecordIdAndConsumedDateBetween(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            // No consumption data, use fallback calculation
            return calculateFallbackRestockDate(stockRecord, defaultRule);
        }
        
        // Calculate average daily consumption
        double averageDailyConsumption = calculateAverageConsumption(logs, startDate, endDate);
        
        // Apply rule constraints
        averageDailyConsumption = Math.max(averageDailyConsumption, defaultRule.getMinDailyUsage());
        averageDailyConsumption = Math.min(averageDailyConsumption, defaultRule.getMaxDailyUsage());
        
        // Calculate days until stockout
        if (averageDailyConsumption <= 0) {
            return LocalDate.now().plusYears(1); // Never runs out
        }
        
        int daysUntilStockout = (int) (stockRecord.getCurrentQuantity() / averageDailyConsumption);
        
        // Return predicted restock date (when stock reaches reorder threshold)
        int daysUntilReorder = daysUntilStockout - (stockRecord.getCurrentQuantity() / stockRecord.getReorderThreshold());
        daysUntilReorder = Math.max(1, daysUntilReorder); // At least 1 day
        
        return LocalDate.now().plusDays(daysUntilReorder);
    }
    
    @Override
    public Integer predictDaysUntilStockout(Long stockRecordId) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        PredictionRule defaultRule = getDefaultRule();
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(defaultRule.getAverageDaysWindow());
        
        List<ConsumptionLog> logs = consumptionLogRepository
                .findByStockRecordIdAndConsumedDateBetween(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            return null; // Insufficient data
        }
        
        double averageDailyConsumption = calculateAverageConsumption(logs, startDate, endDate);
        
        if (averageDailyConsumption <= 0) {
            return Integer.MAX_VALUE; // Never runs out
        }
        
        return (int) (stockRecord.getCurrentQuantity() / averageDailyConsumption);
    }
    
    private double calculateAverageConsumption(List<ConsumptionLog> logs, LocalDate startDate, LocalDate endDate) {
        if (logs.isEmpty()) {
            return 0.0;
        }
        
        int totalConsumed = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        daysBetween = Math.max(1, daysBetween); // Avoid division by zero
        
        return (double) totalConsumed / daysBetween;
    }
    
    private LocalDate calculateFallbackRestockDate(StockRecord stockRecord, PredictionRule rule) {
        // Use min daily usage as conservative estimate
        int dailyUsage = Math.max(1, rule.getMinDailyUsage());
        int daysUntilStockout = stockRecord.getCurrentQuantity() / dailyUsage;
        
        // Add safety buffer
        int daysUntilReorder = Math.max(1, daysUntilStockout / 2);
        
        return LocalDate.now().plusDays(daysUntilReorder);
    }
    
    private PredictionRule getDefaultRule() {
        return predictionRuleRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    // Create default rule if none exists
                    PredictionRule defaultRule = PredictionRule.builder()
                            .ruleName("Default Rule")
                            .averageDaysWindow(30)
                            .minDailyUsage(1)
                            .maxDailyUsage(100)
                            .build();
                    return predictionRuleRepository.save(defaultRule);
                });
    }
    
    @Override
    public List<StockRecord> getStockRecordsNeedingRestock() {
        // Get all stock records
        List<StockRecord> allStockRecords = stockRecordService.getAllStockRecords();
        
        return allStockRecords.stream()
                .filter(record -> {
                    try {
                        LocalDate restockDate = predictRestockDate(record.getId());
                        // Consider needing restock if predicted within 7 days
                        return restockDate.isBefore(LocalDate.now().plusDays(7));
                    } catch (Exception e) {
                        return false; // Skip records with prediction errors
                    }
                })
                .toList();
    }
}