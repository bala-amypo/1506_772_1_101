package com.example.demo.controller;

import com.example.demo.model.PredictionRule;
import com.example.demo.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictionController {
    private final PredictionService predictionService;
    
    @PostMapping("/rules")
    public ResponseEntity<PredictionRule> createRule(@Valid @RequestBody PredictionRule rule) {
        return ResponseEntity.ok(predictionService.createRule(rule));
    }
    
    @GetMapping("/rules")
    public ResponseEntity<List<PredictionRule>> getAllRules() {
        return ResponseEntity.ok(predictionService.getAllRules());
    }
    
    @GetMapping("/restock-date/{stockRecordId}")
    public ResponseEntity<LocalDate> predictRestockDate(@PathVariable Long stockRecordId) {
        return ResponseEntity.ok(predictionService.predictRestockDate(stockRecordId));
    }
}