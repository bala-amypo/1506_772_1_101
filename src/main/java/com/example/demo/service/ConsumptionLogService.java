/*package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;

import java.util.List;

public interface ConsumptionLogService {

    ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log);

    List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId);

    ConsumptionLog getLog(Long id);
}*/
package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;
import java.util.List;

public interface ConsumptionLogService {
    ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log);
    ConsumptionLog getLog(Long id);
    List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId);
    void deleteLog(Long id);
}

