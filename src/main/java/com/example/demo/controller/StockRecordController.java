package com.example.demo.controller;

import com.example.demo.model.StockRecord;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockRecordController {
    private final StockRecordService stockRecordService;
    
    @PostMapping("/{productId}/{warehouseId}")
    public ResponseEntity<StockRecord> createStockRecord(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @Valid @RequestBody StockRecord stockRecord) {
        return ResponseEntity.ok(stockRecordService.createStockRecord(productId, warehouseId, stockRecord));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StockRecord> getStockRecord(@PathVariable Long id) {
        return ResponseEntity.ok(stockRecordService.getStockRecord(id));
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockRecord>> getRecordsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockRecordService.getRecordsByProduct(productId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StockRecord> updateStockRecord(
            @PathVariable Long id,
            @Valid @RequestBody StockRecord stockRecord) {
        return ResponseEntity.ok(stockRecordService.updateStockRecord(id, stockRecord));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockRecord(@PathVariable Long id) {
        stockRecordService.deleteStockRecord(id);
        return ResponseEntity.ok().build();
    }
}