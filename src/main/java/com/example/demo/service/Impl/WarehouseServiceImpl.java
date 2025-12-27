package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("warehouseServiceImpl")
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse.getWarehouseName() == null || warehouse.getWarehouseName().isBlank()) {
            throw new IllegalArgumentException("Warehouse name cannot be empty");
        }

        if (warehouse.getLocation() == null || warehouse.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }

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
