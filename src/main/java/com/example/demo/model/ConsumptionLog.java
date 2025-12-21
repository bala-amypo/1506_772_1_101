package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consumption_logs")
public class ConsumptionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_record_id", nullable = false)
    private StockRecord stockRecord;
    
    @Column(name = "consumed_quantity")
    private Integer consumedQuantity;
    
    @Column(name = "consumed_date")
    private LocalDate consumedDate;
    
    // Constructors
    public ConsumptionLog() {}
    
    public ConsumptionLog(StockRecord stockRecord, Integer consumedQuantity, LocalDate consumedDate) {
        this.stockRecord = stockRecord;
        this.consumedQuantity = consumedQuantity;
        this.consumedDate = consumedDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public StockRecord getStockRecord() { return stockRecord; }
    public void setStockRecord(StockRecord stockRecord) { this.stockRecord = stockRecord; }
    
    public Integer getConsumedQuantity() { return consumedQuantity; }
    public void setConsumedQuantity(Integer consumedQuantity) { this.consumedQuantity = consumedQuantity; }
    
    public LocalDate getConsumedDate() { return consumedDate; }
    public void setConsumedDate(LocalDate consumedDate) { this.consumedDate = consumedDate; }
}