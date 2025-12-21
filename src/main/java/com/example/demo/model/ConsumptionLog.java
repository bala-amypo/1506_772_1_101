package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class ConsumptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_record_id", nullable = false)
    private StockRecord stockRecord;

    private Integer consumedQuantity;

    private LocalDate consumedDate;

    public ConsumptionLog() {
    }

    public ConsumptionLog(Long id, StockRecord stockRecord, Integer consumedQuantity, LocalDate consumedDate) {
        this.id = id;
        this.stockRecord = stockRecord;
        this.consumedQuantity = consumedQuantity;
        this.consumedDate = consumedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockRecord getStockRecord() {
        return stockRecord;
    }

    public void setStockRecord(StockRecord stockRecord) {
        this.stockRecord = stockRecord;
    }

    public Integer getConsumedQuantity() {
        return consumedQuantity;
    }

    public void setConsumedQuantity(Integer consumedQuantity) {
        this.consumedQuantity = consumedQuantity;
    }

    public LocalDate getConsumedDate() {
        return consumedDate;
    }

    public void setConsumedDate(LocalDate consumedDate) {
        this.consumedDate = consumedDate;
    }
}
