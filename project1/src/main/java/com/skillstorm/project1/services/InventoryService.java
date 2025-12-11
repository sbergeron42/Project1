package com.skillstorm.project1.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.DTOs.CreateInventoryRequest;
import com.skillstorm.project1.DTOs.TransferRequest;
import com.skillstorm.project1.models.Inventory;
import com.skillstorm.project1.models.Product;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.InventoryRepository;
import com.skillstorm.project1.repositories.ProductRepository;
import com.skillstorm.project1.repositories.WarehouseRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ActivityLogService activityLogService;

    // constructor injection
    public InventoryService(InventoryRepository inventoryRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, ActivityLogService activityLogService) {
        this.inventoryRepository = inventoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.activityLogService = activityLogService;
    }

    public List<Inventory> findAllInventories() {
        return inventoryRepository.findAll();
    }

    public List<Inventory> searchInventory(int warehouseId, String name, String sku, String manufacturer, String category) {
        return inventoryRepository.search(warehouseId, name, sku, manufacturer); // removed category param for time being
    }

    public Inventory addItemToWarehouse(CreateInventoryRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
            .orElseThrow(() -> new RuntimeException("Warehouse not found!"));
        Product product = productRepository.findBySku(request.getProduct().getSku())
            .orElseGet(() -> {
                Product newProduct = new Product();
                newProduct.setName(request.getProduct().getName());
                newProduct.setManufacturer(request.getProduct().getManufacturer());
                newProduct.setSku(request.getProduct().getSku());
                newProduct.setDescription(request.getProduct().getDescription());
                return productRepository.save(newProduct);
            });
        // Create inventory record
        Inventory inventory = new Inventory(
            warehouse,
            product,
            request.getQuantity(),
            request.getStorageLocation()
        );
        Inventory saved = inventoryRepository.save(inventory);

        // makes a log
        activityLogService.log(
            "INVENTORY_ADDED",
            "INVENTORY",
            saved.getId(),
            "Added " + saved.getQuantity() + " units of '" + product.getName() + 
            "' to " + warehouse.getName()
        );

        return saved;
    
    }

    public Inventory updateInventory(int warehouseId, int inventoryId, Map<String, Object> updates) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getWarehouse().getId() != warehouseId) {
            throw new RuntimeException("Inventory does not belong to warehouse " + warehouseId);
        }

        if (updates.containsKey("quantity")) {
            inventory.setQuantity((Integer) updates.get("quantity"));
        }
        if (updates.containsKey("storageLocation")) {
            inventory.setStorageLocation((String) updates.get("storageLocation"));
        }
        if (updates.containsKey("description")) {
            inventory.getProduct().setDescription((String) updates.get("description"));
        }
        Inventory saved = inventoryRepository.save(inventory);

        // makes a log
        activityLogService.log(
            "INVENTORY_UPDATED",
            "INVENTORY",
            saved.getId(),
            "Updated '" + saved.getProduct().getName() + "' in " + saved.getWarehouse().getName()
        );

        return saved;

    }


    @Transactional
    public void deleteFromWarehouse(int warehouseId, List<Integer> ids) {
        List<Inventory> items = inventoryRepository.findAllById(ids);

        for (Inventory item : items) {
            if (item.getWarehouse().getId() != warehouseId) {
                throw new IllegalArgumentException("Item does not belong to this warehouse");
            }
        }

        // makes a log
        for (Inventory item : items) {
            activityLogService.log(
                "INVENTORY_DELETED",
                "INVENTORY",
                item.getId(),
                "Deleted " + item.getQuantity() + " units of '" + 
                item.getProduct().getName() + "' from " + item.getWarehouse().getName()
            );
        }

        inventoryRepository.deleteAllInBatch(items);
    }

    @Transactional
    public Map<String, Object> transferInventory(TransferRequest request) {
        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Transfer quantity must be positive");
        }

        Warehouse sourceWarehouse = warehouseRepository.findById(request.getSourceWarehouseId())
            .orElseThrow(() -> new RuntimeException("Source warehouse not found"));
        Warehouse destinationWarehouse = warehouseRepository.findById(request.getDestinationWarehouseId())
            .orElseThrow(() -> new RuntimeException("Destination warehouse not found"));

        // Get the product from the source inventory ID
        Inventory exampleSourceInventory = inventoryRepository.findById(request.getInventoryId())
            .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        Product product = exampleSourceInventory.getProduct();

        // Sum total quantity of product in source warehouse
        List<Inventory> sourceInventories = inventoryRepository.findByWarehouseIdAndProductId(sourceWarehouse.getId(), product.getId());
        int totalAvailable = sourceInventories.stream().mapToInt(Inventory::getQuantity).sum();
        if (request.getQuantity() > totalAvailable) {
            throw new RuntimeException("Not enough inventory in source warehouse");
        }

        // Check destination warehouse capacity
        int destinationCurrentLoad = inventoryRepository.sumQuantitiesByWarehouse(destinationWarehouse.getId());
        if (destinationCurrentLoad + request.getQuantity() > destinationWarehouse.getMaxCapacity()) {
            throw new RuntimeException("Transfer exceeds destination warehouse capacity");
        }

        // Reduce quantities across source inventories
        int remainingToTransfer = request.getQuantity();
        for (Inventory inv : sourceInventories) {
            if (remainingToTransfer <= 0) break;
            int deduct = Math.min(inv.getQuantity(), remainingToTransfer);
            inv.setQuantity(inv.getQuantity() - deduct);
            remainingToTransfer -= deduct;
            inventoryRepository.save(inv);
        }

        List<Inventory> destInventories = inventoryRepository.findByWarehouseIdAndProductId(destinationWarehouse.getId(), product.getId());

        Inventory destinationInventory;
        if (destInventories.isEmpty()) {
            destinationInventory = new Inventory(destinationWarehouse, product, 0, "UNASSIGNED");
        } else {
            destinationInventory = destInventories.get(0); // pick first existing row
        }
        
        destinationInventory.setQuantity(destinationInventory.getQuantity() + request.getQuantity());
        inventoryRepository.save(destinationInventory);

        // makes a log
        activityLogService.log(
            "INVENTORY_TRANSFERRED",
            "INVENTORY",
            request.getInventoryId(),
            "Transferred " + request.getQuantity() + " units of '" + product.getName() + 
            "' from " + sourceWarehouse.getName() + " to " + destinationWarehouse.getName()
        );
        
        return Map.of(
            "sourceInventories", sourceInventories.stream().filter(i -> i.getQuantity() > 0).collect(Collectors.toList()),
            "destinationInventory", destinationInventory
        );
    }

}
