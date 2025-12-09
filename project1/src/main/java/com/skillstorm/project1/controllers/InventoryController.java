package com.skillstorm.project1.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.DTOs.CreateInventoryRequest;
import com.skillstorm.project1.DTOs.TransferRequest;
import com.skillstorm.project1.models.Inventory;
import com.skillstorm.project1.services.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping
public class InventoryController {


    private final InventoryService inventoryService;
    
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventories")
    public ResponseEntity<List<Inventory>> findAllInventories() {
        try {
            List<Inventory> inventories = inventoryService.findAllInventories();
            return new ResponseEntity<>(inventories, HttpStatus.OK); // return 200
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // return 500
        }
    }

    @GetMapping("/warehouses/{warehouseId}/inventories")
    public ResponseEntity<List<Inventory>> findInventory(
            @PathVariable int warehouseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String category
        ) {
        try {
            List<Inventory> inventories = inventoryService.searchInventory(warehouseId, name, sku, manufacturer, category);
            return ResponseEntity.ok(inventories); // return 200
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // return 500
        }
    }

    @PostMapping("/warehouses/{warehouseId}/inventories")
    public ResponseEntity<Inventory> addInventory(@PathVariable int warehouseId, @RequestBody CreateInventoryRequest request) {
        request.setWarehouseId(warehouseId);
        try {
            Inventory inventory = inventoryService.addItemToWarehouse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/warehouses/{warehouseId}/inventories/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable int warehouseId, @PathVariable int inventoryId, @RequestBody Map<String, Object> updates) {
        try {
            Inventory updatedInventory = inventoryService.updateInventory(warehouseId, inventoryId, updates);
            return ResponseEntity.ok(updatedInventory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/warehouses/{warehouseId}/inventory")
    public ResponseEntity<Void> deleteFromWarehouse(@PathVariable int warehouseId, @RequestParam List<Integer> ids) {
        inventoryService.deleteFromWarehouse(warehouseId, ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/warehouses/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransferRequest request) {
        try {
            Map<String, Object> updated = inventoryService.transferInventory(request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

    /*
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updated) {
        Product product = productService.updateProduct(id, updated);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }*/

