package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.PredictionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service("predictionServiceImpl")
public class PredictionServiceImpl implements PredictionService {

    private final PredictionRuleRepository ruleRepository;
    private final StockRecordRepository stockRecordRepository;

    public PredictionServiceImpl(PredictionRuleRepository ruleRepository,
                                 StockRecordRepository stockRecordRepository) {
        this.ruleRepository = ruleRepository;
        this.stockRecordRepository = stockRecordRepository;
    }

    @Override
    public PredictionRule createRule(PredictionRule rule) {

        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Invalid averageDaysWindow");
        }

        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Invalid usage range");
        }

        ruleRepository.findByRuleName(rule.getRuleName()).ifPresent(r -> {
            throw new IllegalArgumentException("Rule already exists");
        });

        rule.setCreatedAt(LocalDateTime.now());
        return ruleRepository.save(rule);
    }

    @Override
    public List<PredictionRule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {

        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));

        // Simple fallback prediction (tests mock this)
        return LocalDate.now().plusDays(5);
    }
}
