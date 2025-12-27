package com.example.demo.service;

import com.example.demo.model.Warehouse;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse w);
    Warehouse getWarehouse(Long id);
}
