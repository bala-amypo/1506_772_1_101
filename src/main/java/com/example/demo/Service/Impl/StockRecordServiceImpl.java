package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.StockRecordService;
import com.example.demo.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockRecordServiceImpl implements StockRecordService {

    private final StockRecordRepository stockRecordRepository;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    @Override
    public StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord stockRecord) {

        if (stockRecordRepository
                .findByProductIdAndWarehouseId(productId, warehouseId)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "StockRecord already exists for product-warehouse combination");
        }

        Product product = productService.getProduct(productId);
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId);

        stockRecord.setProduct(product);
        stockRecord.setWarehouse(warehouse);
        stockRecord.setLastUpdated(LocalDateTime.now());

        if (stockRecord.getCurrentQuantity() < 0) {
            throw new IllegalArgumentException("Current quantity cannot be negative");
        }

        return stockRecordRepository.save(stockRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public StockRecord getStockRecord(Long id) {
        return stockRecordRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("StockRecord not found with id: " + id));
    }

    // ✅ Correct method
    @Override
    @Transactional(readOnly = true)
    public List<StockRecord> getRecordsByProduct(Long productId) {
        return stockRecordRepository.findByProductId(productId);
    }
}
