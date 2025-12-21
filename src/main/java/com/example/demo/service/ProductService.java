package com.example.demo.service;

import com.example.demo.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
}package com.example.demo.service;

import com.example.demo.model.Warehouse;
import java.util.List;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse warehouse);
    Warehouse getWarehouse(Long id);
    List<Warehouse> getAllWarehouses();
}
package com.example.demo.service;

import com.example.demo.model.StockRecord;
import java.util.List;

public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsByProduct(Long productId);
    List<StockRecord> getRecordsByWarehouse(Long warehouseId);
}

package com.example.demo.service;

import com.example.demo.model.StockRecord;
import java.util.List;

public interface StockRecordService {
    StockRecord createStockRecord(Long productId, Long warehouseId, StockRecord record);
    StockRecord getStockRecord(Long id);
    List<StockRecord> getRecordsByProduct(Long productId);
    List<StockRecord> getRecordsByWarehouse(Long warehouseId);
}

package com.example.demo.service;

import com.example.demo.model.ConsumptionLog;
import java.util.List;

public interface ConsumptionLogService {
    ConsumptionLog logConsumption(Long stockRecordId, ConsumptionLog log);
    List<ConsumptionLog> getLogsByStockRecord(Long stockRecordId);
    ConsumptionLog getLog(Long id);
}

package com.example.demo.service;

import com.example.demo.model.PredictionRule;
import java.time.LocalDate;
import java.util.List;

public interface PredictionService {
    LocalDate predictRestockDate(Long stockRecordId);
    List<PredictionRule> getAllRules();
    PredictionRule createRule(PredictionRule rule);
}

package com.example.demo.service;

import com.example.demo.model.User;
import java.util.Map;

public interface UserService {
    User register(User user);
    Map<String, Object> login(String email, String password);
    User getByEmail(String email);
}