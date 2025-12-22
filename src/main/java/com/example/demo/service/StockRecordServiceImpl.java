package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.StockRecordService;

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

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        stockRecordRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .ifPresent(r -> {
                    throw new IllegalArgumentException("StockRecord already exists");
                });

        if (record.getCurrentQuantity() < 0) {
            throw new IllegalArgumentException("currentQuantity must be >= 0");
        }

        if (record.getReorderThreshold() <= 0) {
            throw new IllegalArgumentException("reorderThreshold must be > 0");
        }

        record.setProduct(product);
        record.setWarehouse(warehouse);
        record.setLastUpdated(LocalDateTime.now());

        return stockRecordRepository.save(record);
    }

    @Override
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
    }

    @Override
    public List<StockRecord> getRecordsByProduct(Long productId) {
        return stockRecordRepository.findByProductId(productId);
    }

    @Override
    public List<StockRecord> getRecordsByWarehouse(Long warehouseId) {
        return stockRecordRepository.findByWarehouseId(warehouseId);
    }
}
