/*package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    private final StockRecordRepository stockRepository;
    private final ConsumptionLogRepository logRepository;
    private final PredictionRuleRepository ruleRepository;

    @Override
    public PredictionRule createRule(PredictionRule rule) {

        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Invalid average window");
        }

        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Invalid daily usage range");
        }

        if (ruleRepository.findByRuleName(rule.getRuleName()).isPresent()) {
            throw new IllegalArgumentException("Rule already exists");
        }

        rule.setCreatedAt(LocalDateTime.now());
        return ruleRepository.save(rule);
    }

    @Override
    public List<PredictionRule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {

        StockRecord record = stockRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));

        List<ConsumptionLog> logs =
                logRepository.findByStockRecordId(stockRecordId);

        if (logs.isEmpty()) {
            return LocalDate.now();
        }

        double avgUsage = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .average()
                .orElse(0);

        if (avgUsage == 0) {
            return LocalDate.now();
        }

        int daysRemaining =
                (int) ((record.getCurrentQuantity() - record.getReorderThreshold()) / avgUsage);

        return LocalDate.now().plusDays(Math.max(daysRemaining, 0));
    }
}
*/
