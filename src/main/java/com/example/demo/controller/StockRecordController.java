package com.example.demo.controller;

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

    @PostMapping("/create/{productId}/{warehouseId}")
    public StockRecord createStockRecord(@PathVariable Long productId,
                                         @PathVariable Long warehouseId,
                                         @RequestBody StockRecord record) {
        return stockRecordService.createStockRecord(productId, warehouseId, record);
    }

    @GetMapping("/{id}")
    public StockRecord getStockRecord(@PathVariable Long id) {
        return stockRecordService.getStockRecord(id);
    }

    @GetMapping("/product/{productId}")
    public List<StockRecord> getRecordsByProduct(@PathVariable Long productId) {
        return stockRecordService.getRecordsByProduct(productId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<StockRecord> getRecordsByWarehouse(@PathVariable Long warehouseId) {
        return stockRecordService.getRecordsByWarehouse(warehouseId);
    }
}
