package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.PredictionRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionService {
    private final PredictionRuleRepository predictionRuleRepository;
    private final ConsumptionLogRepository consumptionLogRepository;
    private final StockRecordService stockRecordService;
    
    public PredictionRule createRule(PredictionRule rule) {
        return predictionRuleRepository.save(rule);
    }
    
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        // Get consumption logs for last 30 days
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        
        List<ConsumptionLog> logs = consumptionLogRepository
                .findByStockRecordIdAndConsumedDateBetween(stockRecordId, startDate, endDate);
        
        if (logs.isEmpty()) {
            // Fallback: use default rule
            PredictionRule defaultRule = predictionRuleRepository.findAll().stream()
                    .findFirst()
                    .orElseGet(() -> PredictionRule.builder()
                            .averageDaysWindow(7)
                            .minDailyUsage(1)
                            .maxDailyUsage(5)
                            .build());
            
            int daysToStockout = stockRecord.getCurrentQuantity() / defaultRule.getMinDailyUsage();
            return LocalDate.now().plusDays(daysToStockout);
        }
        
        // Calculate average daily consumption
        double totalConsumed = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double avgDailyConsumption = totalConsumed / daysBetween;
        
        // Predict restock date
        int daysUntilReorder = (int) (stockRecord.getCurrentQuantity() / avgDailyConsumption);
        return LocalDate.now().plusDays(daysUntilReorder);
    }
}