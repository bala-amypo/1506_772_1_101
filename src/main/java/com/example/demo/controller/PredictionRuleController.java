package com.example.demo.controller;

import com.example.demo.model.PredictionRule;
import com.example.demo.service.PredictionRuleService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/predict")
public class PredictionRuleController {

    private final PredictionRuleService predictionRuleService;

    public PredictionRuleController(PredictionRuleService predictionRuleService) {
        this.predictionRuleService = predictionRuleService;
    }

    @PostMapping("/rules")
    public PredictionRule createRule(@RequestBody PredictionRule rule) {
        return predictionRuleService.createRule(rule);
    }

    @GetMapping("/rules")
    public List<PredictionRule> getAllRules() {
        return predictionRuleService.getAllRules();
    }

    @GetMapping("/restock-date/{stockRecordId}")
    public LocalDate predict(@PathVariable Long stockRecordId) {
        return predictionRuleService.predictRestockDate(stockRecordId);
    }
}
