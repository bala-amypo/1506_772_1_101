package com.example.demo.service;

import com.example.demo.model.PredictionRule;
import java.time.LocalDate;
import java.util.List;

public interface PredictionService {
    PredictionRule createRule(PredictionRule rule);
    List<PredictionRule> getAllRules();
    LocalDate predictRestockDate(Long stockRecordId);
}