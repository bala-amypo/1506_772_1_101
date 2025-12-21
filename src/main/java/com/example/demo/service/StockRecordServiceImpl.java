package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.StockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StockRecordServiceImpl implements StockRecordService {
    
    private final StockRecordRepository stockRecordRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    
    @Autowired
    public StockRecordServiceImpl(
            StockRecordRepository stockRecordRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository) {
        this.stockRecordRepository = stockRecordRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }
    
    @Override
    @Transactional
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        
        if (stockRecordRepository.findByProductIdAndWarehouseId(productId, warehouseId).isPresent()) {
            throw new IllegalArgumentException("StockRecord already exists");
        }
        
        if (record.getCurrentQuantity() < 0) {
            throw new IllegalArgumentException("Current quantity must be greater than or equal to zero");
        }
        
        if (record.getReorderThreshold() <= 0) {
            throw new IllegalArgumentException("Reorder threshold must be greater than zero");
        }
        
        record.setProduct(product);
        record.setWarehouse(warehouse);
        
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