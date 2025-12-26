/*package com.example.demo.repository;

import com.example.demo.model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumptionLogRepository
        extends JpaRepository<ConsumptionLog, Long> {

    List<ConsumptionLog> findByStockRecordId(Long stockRecordId);
}*/

package com.example.demo.repository;

import com.example.demo.model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {
    List<ConsumptionLog> findByStockRecordId(Long stockRecordId);
    List<ConsumptionLog> findByStockRecordIdAndConsumedDateBetween(
            Long stockRecordId, java.time.LocalDate startDate, java.time.LocalDate endDate);
}