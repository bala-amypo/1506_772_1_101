package com.example.demo.repository;

import com.example.demo.model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {
    List<ConsumptionLog> findByStockRecordId(Long stockRecordId);
    
    List<ConsumptionLog> findByStockRecordIdOrderByConsumedDateDesc(Long stockRecordId);
    
    @Query("SELECT cl FROM ConsumptionLog cl WHERE cl.stockRecord.id = :stockRecordId AND cl.consumedDate BETWEEN :start AND :end")
    List<ConsumptionLog> findByStockRecordIdAndConsumedDateBetween(Long stockRecordId, LocalDate start, LocalDate end);
}