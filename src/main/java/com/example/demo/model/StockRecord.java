package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_records", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    
    @Column(nullable = false)
    private Integer currentQuantity;
    
    private Integer reorderThreshold;
    
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();
    
    @Builder.Default
    @OneToMany(mappedBy = "stockRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumptionLog> consumptionLogs = new ArrayList<>();
}