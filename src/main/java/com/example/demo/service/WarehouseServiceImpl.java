package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {

        if (warehouse.getLocation() == null || warehouse.getLocation().isEmpty()) {
            throw new RuntimeException("Location must not be empty");
        }

        if (warehouseRepository.existsByWarehouseName(warehouse.getWarehouseName())) {
            throw new RuntimeException("Warehouse name already exists");
        }

        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse getWarehouse(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
}
