package com.skillstorm.project1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.project1.models.Inventory;

/**
 * This repository is responsible for managing inventory entries.
 * Provides search, filtering, and aggregation capabilities.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

    /**
     * Retrieves all inventory records for a given warehouse.
     */
    List<Inventory> findByWarehouseId(int warehouseId);

    /**
     * Searches inventory within a warehouse using optional filters.
     * Supports partial matches on product name, SKU, and manufacturer.
     */
    @Query("""
        SELECT i FROM Inventory i
        WHERE i.warehouse.id = :warehouseId
        AND (:name IS NULL OR LOWER(i.product.name) LIKE LOWER(CAST(:name AS text)))
        AND (:sku IS NULL OR LOWER(i.product.sku) LIKE LOWER(CAST(:sku AS text)))
        AND (:manufacturer IS NULL OR LOWER(i.product.manufacturer) LIKE LOWER(CAST(:manufacturer AS text)))
    """)
    List<Inventory> search(
        @Param("warehouseId") int warehouseId,
        @Param("name") String name,
        @Param("sku") String sku,
        @Param("manufacturer") String manufacturer//,
        // @Param("category") String category       // not implemented yet
        // also keeping this here             AND (:category IS NULL OR LOWER(i.product.category) LIKE LOWER(CONCAT('%', :category, '%')))
    );

    /**
     * Finds inventory entries for a specific warehouse and product.
     */
    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Inventory i WHERE i.warehouse.id = :warehouseId")
    int sumQuantitiesByWarehouse(int warehouseId);

    List<Inventory> findByWarehouseIdAndProductId(int warehouseId, int productId);


}
