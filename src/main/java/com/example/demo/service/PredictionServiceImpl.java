package com.example.demo.service.impl;

import com.example.demo.model.PredictionRule;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.service.PredictionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {

    private final PredictionRuleRepository ruleRepository;

    public PredictionServiceImpl(PredictionRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public PredictionServiceRule createRule(PredictionRule rule) {

        // validation
        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("averageDaysWindow must be > 0");
        }

        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("minDailyUsage must be <= maxDailyUsage");
        }

        ruleRepository.findByRuleName(rule.getRuleName())
                .ifPresent(r -> {
                    throw new IllegalArgumentException("Rule name already exists");
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
        // Simple placeholder logic
        return LocalDate.now().plusDays(5);
    }
}
