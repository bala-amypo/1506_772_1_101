package com.example.demo.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consumption_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "stock_record_id", nullable = false)
    private StockRecord stockRecord;
    
    @Column(nullable = false)
    private Integer consumedQuantity;
    
    @Column(nullable = false)
    private LocalDate consumedDate;
    
    @PrePersist
    protected void onCreate() {
        if (consumedDate == null) {
            consumedDate = LocalDate.now();
        }
    }
}