package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Product;
public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsByProduct(Long productId);
    List<StockRecord> getRecordsByWarehouse(Long warehouseId);
}
