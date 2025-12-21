package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}