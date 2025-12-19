package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.StockRecord;
import com.example.demo.model.Product;
import com.example.demo.model.Warehouse;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    StockRecord findByProductAndWarehouse(Product product, Warehouse warehouse);
    List<StockRecord> findByProduct(Product product);
    List<StockRecord> findByWarehouse(Warehouse warehouse);
}
