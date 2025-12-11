package com.skillstorm.project1.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.models.CapacitySnapshot;
import com.skillstorm.project1.services.CapacitySnapshotService;

@CrossOrigin
@RestController
@RequestMapping("/capacity-reports")
public class CapacitySnapshotController {

    private CapacitySnapshotService capacitySnapshotService;
    public CapacitySnapshotController(CapacitySnapshotService capacitySnapshotService) {
        this.capacitySnapshotService = capacitySnapshotService;
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<CapacitySnapshot> getWarehouseTrend(@PathVariable int warehouseId, @RequestParam(defaultValue = "30") int days) {
        return capacitySnapshotService.getWarehouseTrend(warehouseId, days);
    }
    
}
