/*package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;

    private int averageDaysWindow;
    private int minDailyUsage;
    private int maxDailyUsage;

    private LocalDateTime createdAt;
}*/
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_name", unique = true, nullable = false)
    private String ruleName;
    
    @Column(name = "average_days_window", nullable = false)
    private Integer averageDaysWindow;
    
    @Column(name = "min_daily_usage", nullable = false)
    private Integer minDailyUsage;
    
    @Column(name = "max_daily_usage", nullable = false)
    private Integer maxDailyUsage;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}