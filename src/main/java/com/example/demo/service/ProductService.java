package com.example.demo.service;

import java.util.List;

import com.example.demo.model.PredictionRule;

public interface PredictionService {

    PredictionRule createRule(PredictionRule rule);

    List<PredictionRule> getAllRules();
}
