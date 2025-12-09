package com.skillstorm.project1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.DTOs.WarehouseDTO;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.WarehouseRepository;
import com.skillstorm.project1.services.WarehouseService;

import java.lang.annotation.Repeatable;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService, WarehouseRepository warehouseRepository) {
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> findAllWarehouses() {
        try {
            return ResponseEntity.ok(warehouseService.findAllWarehousesWithCapacity());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // return 500
        }
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> findWarehouse(@PathVariable int warehouseId) {
        try {
            WarehouseDTO warehouseDTO = warehouseService.findWarehouseWithCapacity(warehouseId);
            if (warehouseDTO == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(warehouseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // return 500
        }
    }
    
    // not sure if I should have the PostMapping address be /warehouse or just blank as I have
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse newWarehouse = warehouseService.saveWarehouse(warehouse);
        return new ResponseEntity<Warehouse>(newWarehouse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable int id, @RequestBody Warehouse updated) {
        Warehouse warehouse = warehouseService.updateWarehouse(id, updated);
        if (warehouse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(warehouse);
    }
    
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable int warehouseId) {
        int result = warehouseService.deleteWarehouse(warehouseId);
        if (result == 1) {
            return ResponseEntity.ok(1);
        } else return ResponseEntity.ok().build();
    }
    
}
