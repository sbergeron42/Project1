package com.skillstorm.project1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.models.Inventory;
import com.skillstorm.project1.services.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {


    private final InventoryService inventoryService;
    
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> findAllInventories() {
        try {
            List<Inventory> inventories = inventoryService.findAllInventories();
            return new ResponseEntity<>(inventories, HttpStatus.OK); // return 200
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // return 500
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
}
