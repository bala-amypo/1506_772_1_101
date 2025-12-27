package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        if (stockRecordRepository.findByProductIdAndWarehouseId(productId, warehouseId).isPresent()) {
            throw new IllegalArgumentException("StockRecord already exists for this product-warehouse pair");
        }
        
        Product product = productService.getProduct(productId);
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
        
        stockRecord.setProduct(product);
        stockRecord.setWarehouse(warehouse);
        
        return stockRecordRepository.save(stockRecord);
    }
    
    @Override
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found with id: " + id));
    }
    
    @Override
    public List<StockRecord> getRecordsByProduct(Long productId) {
        return stockRecordRepository.findByProductId(productId);
    }
    
    @Override
    public StockRecord updateStockRecord(Long id, StockRecord stockRecord) {
        StockRecord existing = getStockRecord(id);
        existing.setCurrentQuantity(stockRecord.getCurrentQuantity());
        existing.setReorderThreshold(stockRecord.getReorderThreshold());
        return stockRecordRepository.save(existing);
    }
    
    @Override
    public void deleteStockRecord(Long id) {
        stockRecordRepository.deleteById(id);
    }
    
    @Override
    public StockRecord updateStockQuantity(Long stockRecordId, Integer newQuantity) {
        StockRecord record = getStockRecord(stockRecordId);
        record.setCurrentQuantity(newQuantity);
        return stockRecordRepository.save(record);
    }
}