package com.example.demo.repository;

import com.example.demo.model.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByProductId(Long productId);
    
    @Query("SELECT sr FROM StockRecord sr WHERE sr.product.id = :productId AND sr.warehouse.id = :warehouseId")
    Optional<StockRecord> findByProductAndWarehouse(Long productId, Long warehouseId);
}