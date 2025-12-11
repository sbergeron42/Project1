package com.skillstorm.project1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.DTOs.WarehouseDTO;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.InventoryRepository;
import com.skillstorm.project1.repositories.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final ActivityLogService activityLogService;

    // constructor injection
    public WarehouseService(WarehouseRepository warehouseRepository, InventoryRepository inventoryRepository, ActivityLogService activityLogService) {
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
        this.activityLogService = activityLogService;
    }

    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse findWarehouse(int warehouseId) {
        return warehouseRepository.findById(warehouseId).orElse(null);
    }

    public Warehouse saveWarehouse(Warehouse warehouse) {
        Warehouse saved = warehouseRepository.save(warehouse);

        // makes a log
        activityLogService.log(
            "WAREHOUSE_CREATED",
            "WAREHOUSE",
            saved.getId(),
            "Created warehouse '" + saved.getName() + "' at " + saved.getLocation()
        );

        return saved;
    }

    public Warehouse updateWarehouse(int id, Warehouse updated) {
        return warehouseRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setLocation(updated.getLocation());
                existing.setMaxCapacity(updated.getMaxCapacity());
                Warehouse saved = warehouseRepository.save(existing);

                // makes a log
                activityLogService.log(
                    "WAREHOUSE_UPDATED",
                    "WAREHOUSE",
                    saved.getId(),
                    "Updated warehouse '" + saved.getName() + "'"
                );
                return saved;
            })

        
            .orElse(null);
    }

    public int deleteWarehouse(int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse != null) {
            String name = warehouse.getName();
            warehouseRepository.delete(warehouse);

            // makes a log
            activityLogService.log(
                "WAREHOUSE_DELETED",
                "WAREHOUSE",
                warehouseId,
                "Deleted warehouse '" + name + "'"
            );

            return 1;
        } else return 0;
    }

    public int getCurrentCapacity(int warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId).stream().mapToInt(item -> item.getQuantity()).sum();
    }

    // All WarehouseDTOs
    public List<WarehouseDTO> findAllWarehousesWithCapacity() {
        return warehouseRepository.findAll().stream().map(warehouse -> new WarehouseDTO(warehouse, getCurrentCapacity(warehouse.getId()))).toList();
    }

    // Return DTO for one warehouse
    public WarehouseDTO findWarehouseWithCapacity(int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse == null) return null;
        int capacity = getCurrentCapacity(warehouseId);
        return new WarehouseDTO(warehouse, capacity);
    }

}
