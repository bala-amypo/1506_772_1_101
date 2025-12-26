package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_rules", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ruleName")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;

    private Integer averageDaysWindow;

    private Integer minDailyUsage;

    private Integer maxDailyUsage;

    private LocalDateTime createdAt;
}
