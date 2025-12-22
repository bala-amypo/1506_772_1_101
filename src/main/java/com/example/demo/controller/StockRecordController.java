/*package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.StockRecord;
import com.example.demo.service.StockRecordService;

@RestController
@RequestMapping("/api/stocks")
public class StockRecordController {

    @Autowired
    private StockRecordService stockRecordService;

    @PostMapping("/{productId}/{warehouseId}")
    public StockRecord addStockRecord(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody StockRecord record) {

        return stockRecordService.createStockRecord(productId, warehouseId, record);
    }

    @GetMapping("/{id}")
    public StockRecord getStockRecord(@PathVariable Long id) {
        return stockRecordService.getStockRecord(id);
    }

    @GetMapping("/product/{productId}")
    public List<StockRecord> getByProduct(@PathVariable Long productId) {
        return stockRecordService.getRecordsByProduct(productId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<StockRecord> getByWarehouse(@PathVariable Long warehouseId) {
        return stockRecordService.getRecordsByWarehouse(warehouseId);
    }
}
