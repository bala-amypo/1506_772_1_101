/*package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int consumedQuantity;

    private LocalDate consumedDate;

    @ManyToOne
    private StockRecord stockRecord;
}
*/
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumption_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_record_id", nullable = false)
    private StockRecord stockRecord;
    
    @Column(name = "consumed_quantity", nullable = false)
    private Integer consumedQuantity;
    
    @Column(name = "consumed_date", nullable = false)
    private LocalDate consumedDate;
    
    @Column(name = "logged_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime loggedAt = LocalDateTime.now();
}