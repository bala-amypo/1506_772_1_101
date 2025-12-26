package com.example.demo.repository;

import com.example.demo.model.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByProductId(Long productId);
    List<StockRecord> findByWarehouseId(Long warehouseId);
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
