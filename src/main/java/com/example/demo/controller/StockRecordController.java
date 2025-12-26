package com.example.demo.controller;

import com.example.demo.model.StockRecord;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockRecordController {

    private final StockRecordService stockRecordService;

    @PostMapping("/{productId}/{warehouseId}")
    public StockRecord createStockRecord(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody StockRecord record
    ) {
        return stockRecordService.createStockRecord(productId, warehouseId, record);
    }

    @GetMapping("/{id}")
    public StockRecord getStockRecord(@PathVariable Long id) {
        return stockRecordService.getStockRecord(id);
    }

    @GetMapping("/product/{productId}")
    public List<StockRecord> getByProduct(@PathVariable Long productId) {
        return stockRecordService.getRecordsBy_product(productId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<StockRecord> getByWarehouse(@PathVariable Long warehouseId) {
        return stockRecordService.getRecordsByWarehouse(warehouseId);
    }
}
