package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_rules")
public class PredictionRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_name", unique = true, nullable = false)
    private String ruleName;
    
    @Column(name = "average_days_window")
    private Integer averageDaysWindow;
    
    @Column(name = "min_daily_usage")
    private Integer minDailyUsage;
    
    @Column(name = "max_daily_usage")
    private Integer maxDailyUsage;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public PredictionRule() {}
    
    public PredictionRule(String ruleName, Integer averageDaysWindow, Integer minDailyUsage, Integer maxDailyUsage) {
        this.ruleName = ruleName;
        this.averageDaysWindow = averageDaysWindow;
        this.minDailyUsage = minDailyUsage;
        this.maxDailyUsage = maxDailyUsage;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public Integer getAverageDaysWindow() { return averageDaysWindow; }
    public void setAverageDaysWindow(Integer averageDaysWindow) { this.averageDaysWindow = averageDaysWindow; }
    
    public Integer getMinDailyUsage() { return minDailyUsage; }
    public void setMinDailyUsage(Integer minDailyUsage) { this.minDailyUsage = minDailyUsage; }
    
    public Integer getMaxDailyUsage() { return maxDailyUsage; }
    public void setMaxDailyUsage(Integer maxDailyUsage) { this.maxDailyUsage = maxDailyUsage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}