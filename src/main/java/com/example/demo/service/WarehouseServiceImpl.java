
package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.WarehouseService;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse.getWarehouseName() == null || warehouse.getWarehouseName().isEmpty()) {
            throw new IllegalArgumentException("warehouseName must not be empty");
        }
        if (warehouse.getLocation() == null || warehouse.getLocation().isEmpty()) {
            throw new IllegalArgumentException("location must not be empty");
        }
        
        warehouseRepository.findByWarehouseName(warehouse.getWarehouseName())
                .ifPresent(w -> {
                    throw new IllegalArgumentException("warehouseName already exists");
                });
        
        warehouse.setCreatedAt(LocalDateTime.now());
        return warehouseRepository.save(warehouse);
    }
    
    @Override
    public Warehouse getWarehouse(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
    }
    
    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
}
