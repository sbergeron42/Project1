package com.skillstorm.project1.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.models.CapacitySnapshot;
import com.skillstorm.project1.models.Inventory;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.CapacitySnapshotRepository;
import com.skillstorm.project1.repositories.InventoryRepository;
import com.skillstorm.project1.repositories.WarehouseRepository;

/**
 * This service is responsible for generating and retrieving warehouse capacity snapshots.
 * Snapshots represent usage levels taken at a specific point in time.
 */
@Service
public class CapacitySnapshotService {
    
    /**
     * Constructor injection
     */
    private CapacitySnapshotRepository capacitySnapshotRepository;
    private WarehouseRepository warehouseRepository;
    private InventoryRepository  inventoryRepository;
    public CapacitySnapshotService(CapacitySnapshotRepository capacitySnapshotRepository, WarehouseRepository warehouseRepository, InventoryRepository inventoryRepository) {
        this.capacitySnapshotRepository = capacitySnapshotRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates a new snapshot for the given warehouse based on its current inventory load and max capacity.
     * @param warehouseId The ID of the warehouse to capture
     */
    public void createSnapshot(int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow();
        
        /**
         * Calculates capacity from inventory
         */
        int currentCapacity = inventoryRepository.findByWarehouseId(warehouseId)
            .stream()
            .mapToInt(Inventory::getQuantity)
            .sum();
        
        CapacitySnapshot snapshot = new CapacitySnapshot();
        snapshot.setWarehouse(warehouse);
        snapshot.setSnapshotDate(LocalDate.now());
        snapshot.setCurrentCapacity(currentCapacity);
        snapshot.setMaxCapacity(warehouse.getMaxCapacity());
        snapshot.setUtilizationPercentage((double) currentCapacity / warehouse.getMaxCapacity() * 100);
        
        capacitySnapshotRepository.save(snapshot);
    }

    /**
     * Retrieves capacity snapshots for a warehouse over a recent time period.
     * @param warehouseId The warehouse ID
     * @param days How far back to retrieve data (inclusive)
     * @return A chronological list of capacity snapshots
     */
    public List<CapacitySnapshot> getWarehouseTrend(int warehouseId, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return capacitySnapshotRepository.findByWarehouseIdAndSnapshotDateBetweenOrderBySnapshotDate(
            warehouseId, startDate, LocalDate.now());
    }

}
