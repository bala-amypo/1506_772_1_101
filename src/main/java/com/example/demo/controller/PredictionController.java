package com.example.demo.controller;

import com.example.demo.model.PredictionRule;
import com.example.demo.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {
    
    private final PredictionService predictionService;
    
    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }
    
    @GetMapping("/restock-date/{stockRecordId}")
    public ResponseEntity<LocalDate> predictRestockDate(@PathVariable Long stockRecordId) {
        LocalDate restockDate = predictionService.predictRestockDate(stockRecordId);
        return ResponseEntity.ok(restockDate);
    }
    
    @PostMapping("/rules")
    public ResponseEntity<PredictionRule> createRule(@RequestBody PredictionRule rule) {
        PredictionRule createdRule = predictionService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
    }
    
    @GetMapping("/rules")
    public ResponseEntity<List<PredictionRule>> getAllRules() {
        List<PredictionRule> rules = predictionService.getAllRules();
        return ResponseEntity.ok(rules);
    }
}