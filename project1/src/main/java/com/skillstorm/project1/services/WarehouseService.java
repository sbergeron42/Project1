package com.skillstorm.project1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    // constructor injection
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public Warehouse updateWarehouse(int id, Warehouse updated) {
        return warehouseRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setLocation(updated.getLocation());
                existing.setMaxCapacity(updated.getMaxCapacity());
                return warehouseRepository.save(existing);
            })
            .orElse(null);
    }

    public int deleteWarehouse(int warehouse_id) {
        Warehouse warehouse = warehouseRepository.findById(warehouse_id).orElse(null);
        if (warehouse != null) {
            warehouseRepository.delete(warehouse);
            return 1;
        } else return 0;
    }

}
