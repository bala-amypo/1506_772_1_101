package com.example.demo.repository;

import com.example.demo.model.PredictionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PredictionRuleRepository extends JpaRepository<PredictionRule, Long> {
    Optional<PredictionRule> findByRuleName(String ruleName);
}