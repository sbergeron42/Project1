package com.skillstorm.project1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.DTOs.WarehouseDTO;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.InventoryRepository;
import com.skillstorm.project1.repositories.WarehouseRepository;

/**
 * This service is responsible for managing warehouse operations, including creating,
 * updating, and deleting warehouses, as well as retrieving capacity information.
 */
@Service
public class WarehouseService {

    /**
     * Constructor injection
     */
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final ActivityLogService activityLogService;
    public WarehouseService(WarehouseRepository warehouseRepository, InventoryRepository inventoryRepository, ActivityLogService activityLogService) {
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
        this.activityLogService = activityLogService;
    }

    /**
     * Retrieves all warehouses.
     * @return List of Warehouse objects
     */
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    /**
     * Finds a single warehouse by ID.
     * @param warehouseId The warehouse ID
     * @return The warehouse, or null if not found
     */
    public Warehouse findWarehouse(int warehouseId) {
        return warehouseRepository.findById(warehouseId).orElse(null);
    }

    /**
     * Saves a new warehouse and logs its creation.
     * @param warehouse The warehouse to save
     * @return The saved warehouse
     */
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

    /**
     * Updates an existing warehouse.
     * @param id Warehouse ID
     * @param updated Updated warehouse data
     * @return The updated warehouse or null
     */
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

    /**
     * Deletes a warehouse and logs the action
     */
    public int deleteWarehouse(int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse != null) {
            String name = warehouse.getName();
            warehouseRepository.delete(warehouse);

            activityLogService.log(
                "WAREHOUSE_DELETED",
                "WAREHOUSE",
                warehouseId,
                "Deleted warehouse '" + name + "'"
            );

            return 1;
        } else return 0;
    }

    /**
     * Calculates the current inventory load of a warehouse.
     * @param warehouseId The warehouse ID
     * @return Current total quantity of items stored
     */
    public int getCurrentCapacity(int warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId).stream().mapToInt(item -> item.getQuantity()).sum();
    }

    /**
     * Retrieves all warehouses paired with current capacity amounts.
     * @return Current total quantity of items stored
     */
    public List<WarehouseDTO> findAllWarehousesWithCapacity() {
        return warehouseRepository.findAll().stream().map(warehouse -> new WarehouseDTO(warehouse, getCurrentCapacity(warehouse.getId()))).toList();
    }

    /**
     * Retrieves a single warehouse along with its current capacity.
     * @param warehouseId The warehouse ID
     * @return A WarehouseDTO or null if the warehouse is not found
     */
    public WarehouseDTO findWarehouseWithCapacity(int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse == null) return null;
        int capacity = getCurrentCapacity(warehouseId);
        return new WarehouseDTO(warehouse, capacity);
    }

}
