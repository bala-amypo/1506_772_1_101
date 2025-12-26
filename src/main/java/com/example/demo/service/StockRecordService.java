/*package com.example.demo.service;

import com.example.demo.model.StockRecord;

import java.util.List;

public interface StockRecordService {

    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record);

    StockRecord getStockRecord(Long id);

    List<StockRecord> getRecordsBy_product(Long productId);

    List<StockRecord> getRecordsByWarehouse(Long warehouseId);
}
*/
package com.example.demo.service;

import com.example.demo.model.StockRecord;
import java.util.List;

public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsByProduct(Long productId);
    List<StockRecord> getRecordsByWarehouse(Long warehouseId);
    StockRecord updateStockRecord(Long id, StockRecord record);
    void deleteStockRecord(Long id);
}