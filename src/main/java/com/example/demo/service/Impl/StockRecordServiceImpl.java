/*package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRecordServiceImpl implements StockRecordService {

    private final StockRecordRepository stockRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        if (stockRepository.existsByProductIdAndWarehouseId(productId, warehouseId)) {
            throw new IllegalArgumentException("StockRecord already exists");
        }

        if (record.getCurrentQuantity() < 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (record.getReorderThreshold() <= 0) {
            throw new IllegalArgumentException("Invalid threshold");
        }

        record.setProduct(product);
        record.setWarehouse(warehouse);
        record.setLastUpdated(LocalDateTime.now());

        return stockRepository.save(record);
    }

    @Override
    public StockRecord getStockRecord(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
    }

    @Override
    public List<StockRecord> getRecordsBy_product(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    @Override
    public List<StockRecord> getRecordsByWarehouse(Long warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId);
    }
}
*/
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRecordServiceImpl implements StockRecordService {
    
    private final StockRecordRepository stockRecordRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    
    @Override
    @Transactional
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        
        if (stockRecordRepository.existsByProductIdAndWarehouseId(productId, warehouseId)) {
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
    
    @Override
    @Transactional
    public StockRecord updateStockRecord(Long id, StockRecord record) {
        StockRecord existing = getStockRecord(id);
        
        if (record.getCurrentQuantity() != null) {
            if (record.getCurrentQuantity() < 0) {
                throw new IllegalArgumentException("Current quantity must be greater than or equal to zero");
            }
            existing.setCurrentQuantity(record.getCurrentQuantity());
        }
        
        if (record.getReorderThreshold() != null) {
            if (record.getReorderThreshold() <= 0) {
                throw new IllegalArgumentException("Reorder threshold must be greater than zero");
            }
            existing.setReorderThreshold(record.getReorderThreshold());
        }
        
        existing.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deleteStockRecord(Long id) {
        if (!stockRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("StockRecord not found");
        }
        stockRecordRepository.deleteById(id);
    }
}