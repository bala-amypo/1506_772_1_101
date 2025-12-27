// File: src/main/java/com/example/demo/service/ConsumptionLogService.java
package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;
import java.util.List;

public interface ConsumptionLogService {
    ConsumptionLog createLog(ConsumptionLog log);
    List<ConsumptionLog> getAllLogs();
    ConsumptionLog getLogById(Long id);
    void deleteLog(Long id);
}