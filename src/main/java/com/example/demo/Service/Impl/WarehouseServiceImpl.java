package com.example.demo.service.impl;

import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.WarehouseService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    
    @Override
    @Transactional
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse.getWarehouseName() == null || warehouse.getWarehouseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse name is required");
        }
        
        return warehouseRepository.save(warehouse);
    }
    
    @Override
    public Warehouse getWarehouse(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
    }
    
    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
    
    @Override
    @Transactional
    public Warehouse updateWarehouse(Long id, Warehouse warehouseDetails) {
        Warehouse warehouse = getWarehouse(id);
        
        warehouse.setWarehouseName(warehouseDetails.getWarehouseName());
        warehouse.setLocation(warehouseDetails.getLocation());
        
        return warehouseRepository.save(warehouse);
    }
    
    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = getWarehouse(id);
        warehouseRepository.delete(warehouse);
    }
}