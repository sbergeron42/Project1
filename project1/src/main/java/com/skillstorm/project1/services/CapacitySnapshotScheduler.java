package com.skillstorm.project1.services;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.WarehouseRepository;

@Component
public class CapacitySnapshotScheduler {

    private CapacitySnapshotService capacitySnapshotService;
    private WarehouseRepository warehouseRepository;

    public CapacitySnapshotScheduler(CapacitySnapshotService capacitySnapshotService, WarehouseRepository warehouseRepository) {
        this.capacitySnapshotService = capacitySnapshotService;
        this.warehouseRepository = warehouseRepository;
    }

    // Run daily at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void captureSnapshots() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        for (Warehouse w : warehouses) {
            capacitySnapshotService.createSnapshot(w.getId());
        }
    }
}
