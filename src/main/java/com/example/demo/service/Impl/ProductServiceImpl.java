package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.PredictionRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    private final PredictionRuleRepository predictionRuleRepository;
    private final StockRecordService stockRecordService;
    private final ConsumptionLogService consumptionLogService;
    
    @Override
    public PredictionRule createRule(PredictionRule rule) {
        // Validate min <= max
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("minDailyUsage must be less than or equal to maxDailyUsage");
        }
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordService.getStockRecord(stockRecordId);
        
        // Get the default rule (in real app, you'd select based on product/category)
        PredictionRule rule = predictionRuleRepository.findAll().stream()
                .findFirst()
                .orElse(PredictionRule.builder()
                        .averageDaysWindow(7)
                        .minDailyUsage(1)
                        .maxDailyUsage(10)
                        .build());
        
        // Calculate average daily consumption
        Double avgConsumption = consumptionLogService.calculateAverageDailyConsumption(
            stockRecordId, rule.getAverageDaysWindow());
        
        // Use rule bounds
        double boundedConsumption = Math.max(rule.getMinDailyUsage(), 
            Math.min(rule.getMaxDailyUsage(), avgConsumption));
        
        if (boundedConsumption <= 0) {
            boundedConsumption = rule.getMinDailyUsage();
        }
        
        // Calculate days until reorder threshold
        int daysUntilReorder = (int) Math.ceil(
            (stockRecord.getCurrentQuantity() - stockRecord.getReorderThreshold()) / boundedConsumption);
        
        if (daysUntilReorder <= 0) {
            daysUntilReorder = 1; // Immediate reorder
        }
        
        return LocalDate.now().plusDays(daysUntilReorder);
    }
}