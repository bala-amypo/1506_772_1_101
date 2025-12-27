package com.example.demo.service;

import com.example.demo.model.StockRecord;
import java.util.List;

public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord s);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsBy_product(Long productId);
}
