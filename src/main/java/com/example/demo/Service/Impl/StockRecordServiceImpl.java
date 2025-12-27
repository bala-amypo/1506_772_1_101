package com.example.demo.service.impl;

import com.example.demo.model.StockRecord;
import com.example.demo.model.Product;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.StockRecordService;
import com.example.demo.service.ProductService;
import com.example.demo.service.WarehouseService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRecordServiceImpl implements StockRecordService {
    
    private final StockRecordRepository stockRecordRepository;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    
    @Override
    @Transactional
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord stockRecord) {
        Product product = productService.getProduct(productId);
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
        
        // Check if stock record already exists for this product-warehouse combination
        stockRecordRepository.findByProductAndWarehouse(productId, warehouseId)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                        "Stock record already exists for product " + productId + 
                        " and warehouse " + warehouseId
                    );
                });
        
        // Validate stock quantities
        if (stockRecord.getCurrentQuantity() == null || stockRecord.getCurrentQuantity() < 0) {
            throw new IllegalArgumentException("Current quantity must be non-negative");
        }
        
        if (stockRecord.getReorderThreshold() == null || stockRecord.getReorderThreshold() < 0) {
            throw new IllegalArgumentException("Reorder threshold must be non-negative");
        }
        
        stockRecord.setProduct(product);
        stockRecord.setWarehouse(warehouse);
        stockRecord.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(stockRecord);
    }
    
    @Override
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock record not found with id: " + id));
    }
    
    @Override
    public List<StockRecord> getRecordsBy_product(Long productId) {
        return stockRecordRepository.findByProductId(productId);
    }
    
    @Override
    @Transactional
    public StockRecord updateStockQuantity(Long id, Integer newQuantity) {
        if (newQuantity == null || newQuantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        
        StockRecord stockRecord = getStockRecord(id);
        stockRecord.setCurrentQuantity(newQuantity);
        stockRecord.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(stockRecord);
    }
    
    @Override
    @Transactional
    public StockRecord adjustStockQuantity(Long id, Integer adjustment) {
        StockRecord stockRecord = getStockRecord(id);
        
        int newQuantity = stockRecord.getCurrentQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Cannot adjust stock below zero");
        }
        
        stockRecord.setCurrentQuantity(newQuantity);
        stockRecord.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(stockRecord);
    }
    
    @Override
    @Transactional
    public StockRecord updateReorderThreshold(Long id, Integer newThreshold) {
        if (newThreshold == null || newThreshold < 0) {
            throw new IllegalArgumentException("Reorder threshold must be non-negative");
        }
        
        StockRecord stockRecord = getStockRecord(id);
        stockRecord.setReorderThreshold(newThreshold);
        stockRecord.setLastUpdated(LocalDateTime.now());
        
        return stockRecordRepository.save(stockRecord);
    }
    
    @Override
    @Transactional
    public void deleteStockRecord(Long id) {
        StockRecord stockRecord = getStockRecord(id);
        stockRecordRepository.delete(stockRecord);
    }
    
    @Override
    public List<StockRecord> getLowStockRecords() {
        return stockRecordRepository.findAll().stream()
                .filter(record -> record.getCurrentQuantity() <= record.getReorderThreshold())
                .toList();
    }
    
    @Override
    public StockRecord getStockRecordByProductAndWarehouse(Long productId, Long warehouseId) {
        return stockRecordRepository.findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Stock record not found for product " + productId + 
                    " and warehouse " + warehouseId
                ));
    }
}