package com.example.demo.controller;

import com.example.demo.model.StockRecord;
import com.example.demo.service.StockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockRecordController {
    
    private final StockRecordService stockRecordService;
    
    @Autowired
    public StockRecordController(StockRecordService stockRecordService) {
        this.stockRecordService = stockRecordService;
    }
    
    @PostMapping("/{productId}/{warehouseId}")
    public ResponseEntity<StockRecord> createStockRecord(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody StockRecord record) {
        StockRecord createdRecord = stockRecordService.createStockRecord(productId, warehouseId, record);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockRecord>> getRecordsByProduct(@PathVariable Long productId) {
        List<StockRecord> records = stockRecordService.getRecordsByProduct(productId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<StockRecord>> getRecordsByWarehouse(@PathVariable Long warehouseId) {
        List<StockRecord> records = stockRecordService.getRecordsByWarehouse(warehouseId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StockRecord> getStockRecord(@PathVariable Long id) {
        StockRecord record = stockRecordService.getStockRecord(id);
        return ResponseEntity.ok(record);
    }
}