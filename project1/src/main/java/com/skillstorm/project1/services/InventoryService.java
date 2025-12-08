package com.skillstorm.project1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.models.Inventory;
import com.skillstorm.project1.repositories.InventoryRepository;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // constructor injection
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> findAllInventories() {
        return inventoryRepository.findAll();
    }


    /*
    public Product updateProduct(int id, Product updated) {
        return productRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setManufacturer(updated.getManufacturer());
                existing.setSku(updated.getSku());
                existing.setDescription(updated.getDescription());
                return productRepository.save(existing);
            })
            .orElse(null);
    }*/
    
}
