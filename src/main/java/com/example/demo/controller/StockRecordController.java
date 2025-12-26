package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Warehouse warehouse;

    private Integer currentQuantity;
    private Integer reorderThreshold;
    private LocalDateTime lastUpdated;
}
