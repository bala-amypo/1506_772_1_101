/*package com.example.demo.service;

import com.example.demo.model.PredictionRule;

import java.time.LocalDate;
import java.util.List;

public interface PredictionService {

    LocalDate predictRestockDate(Long stockRecordId);

    PredictionRule createRule(PredictionRule rule);

    List<PredictionRule> getAllRules();
}*/
package com.example.demo.service;

import com.example.demo.model.PredictionRule;
import java.time.LocalDate;
import java.util.List;

public interface PredictionService {
    LocalDate predictRestockDate(Long stockRecordId);
    List<PredictionRule> getAllRules();
    PredictionRule createRule(PredictionRule rule);
    PredictionRule getRule(Long id);
    void deleteRule(Long id);
}
