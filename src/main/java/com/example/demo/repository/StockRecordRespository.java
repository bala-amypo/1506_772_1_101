package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.StockRecord;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {

    List<StockRecord> findByProductId(Long productId);

    List<StockRecord> findByWarehouseId(Long warehouseId);

    Optional<StockRecord> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
