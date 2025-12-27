package com.example.demo.service;

import com.example.demo.model.StockRecord;
import com.example.demo.model.Product;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRecordService {
    private final StockRecordRepository stockRecordRepository;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    
    @Transactional
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord stockRecord) {
        Product product = productService.getProduct(productId);
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
        
        stockRecordRepository.findByProductAndWarehouse(productId, warehouseId)
                .ifPresent(s -> {
                    throw new IllegalArgumentException("StockRecord already exists for this product-warehouse pair");
                });
        
        stockRecord.setProduct(product);
        stockRecord.setWarehouse(warehouse);
        return stockRecordRepository.save(stockRecord);
    }
    
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
    }
    
    public List<StockRecord> getRecordsBy_product(Long productId) {
        return stockRecordRepository.findByProductId(productId);
    }
}