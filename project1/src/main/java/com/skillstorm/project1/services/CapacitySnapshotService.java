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

@Service
public class CapacitySnapshotService {
    private CapacitySnapshotRepository capacitySnapshotRepository;
    private WarehouseRepository warehouseRepository;
    private InventoryRepository  inventoryRepository;

    public CapacitySnapshotService(CapacitySnapshotRepository capacitySnapshotRepository, WarehouseRepository warehouseRepository, InventoryRepository inventoryRepository) {
        this.capacitySnapshotRepository = capacitySnapshotRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // Create snapshot for a warehouse
    public void createSnapshot(int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow();
        
        // Calculate current capacity from inventory
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

    // Get trend data for a warehouse
    public List<CapacitySnapshot> getWarehouseTrend(int warehouseId, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return capacitySnapshotRepository.findByWarehouseIdAndSnapshotDateBetweenOrderBySnapshotDate(
            warehouseId, startDate, LocalDate.now());
    }

}
