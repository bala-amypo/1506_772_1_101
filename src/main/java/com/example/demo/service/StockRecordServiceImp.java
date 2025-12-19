package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.StockRecord;
import com.example.demo.entity.Product;
import com.example.demo.entity.Warehouse;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class StockRecordServiceImpl implements StockRecordService {

    @Autowired
    private StockRecordRepository stockRecordRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record) {
        Product product = productRepository.findById(productId).orElse(null);
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);

        if (product == null || warehouse == null) return null;

        StockRecord existing = stockRecordRepository.findByProductAndWarehouse(product, warehouse);
        if (existing != null) return null; // Duplicate

        if (record.getCurrentQuantity() < 0 || record.getReorderThreshold() <= 0) return null;

        record.setProduct(product);
        record.setWarehouse(warehouse);
        record.setLastUpdated(LocalDateTime.now());

        return stockRecordRepository.save(record);
    }

    @Override
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id).orElse(null);
    }

    @Override
    public List<StockRecord> getRecordsByProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return List.of();
        return stockRecordRepository.findByProduct(product);
    }

    @Override
    public List<StockRecord> getRecordsByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse == null) return List.of();
        return stockRecordRepository.findByWarehouse(warehouse);
    }
}
