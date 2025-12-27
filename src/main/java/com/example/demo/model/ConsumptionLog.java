package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consumption_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    protected void validate() {
        if (consumedDate == null) {
            consumedDate = LocalDate.now();
        }
        if (consumedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("consumedDate cannot be future");
        }
    }
}