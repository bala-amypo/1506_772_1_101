package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String ruleName;
    
    @Column(nullable = false)
    private Integer averageDaysWindow;
    
    @Column(nullable = false)
    private Integer minDailyUsage;
    
    @Column(nullable = false)
    private Integer maxDailyUsage;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    @PrePersist
    protected void validate() {
        if (minDailyUsage > maxDailyUsage) {
            throw new IllegalArgumentException("minDailyUsage cannot be greater than maxDailyUsage");
        }
        if (averageDaysWindow <= 0) {
            throw new IllegalArgumentException("averageDaysWindow must be positive");
        }
    }
}