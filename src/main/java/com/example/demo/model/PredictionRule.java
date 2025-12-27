package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
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
    
    @Column(nullable = false)
    private String ruleName;
    
    @Column(nullable = false)
    private Integer averageDaysWindow;
    
    @Column(nullable = false)
    private Integer minDailyUsage;
    
    @Column(nullable = false)
    private Integer maxDailyUsage;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}