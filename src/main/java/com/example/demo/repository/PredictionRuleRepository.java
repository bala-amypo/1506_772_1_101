package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PredictionRule;

public interface PredictionRuleRepository extends JpaRepository<PredictionRule, Long> {

    Optional<PredictionRule> findByRuleName(String ruleName);
}
