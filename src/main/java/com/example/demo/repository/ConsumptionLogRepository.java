package com.example.demo.repository;

import com.example.demo.model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {

    List<ConsumptionLog> findByStockRecordIdAndConsumedDateBetween(
            Long id, LocalDate start, LocalDate end);

    List<ConsumptionLog> findByStockRecordIdOrderByConsumedDateDesc(Long id);
}
