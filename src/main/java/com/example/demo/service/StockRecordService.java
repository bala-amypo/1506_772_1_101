package com.example.demo.service;

import com.example.demo.model.StockRecord;
import java.util.List;

public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord stockRecord);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsByProduct(Long productId);
    StockRecord updateStockRecord(Long id, StockRecord stockRecord);
    void deleteStockRecord(Long id);
    StockRecord updateStockQuantity(Long stockRecordId, Integer newQuantity);
}