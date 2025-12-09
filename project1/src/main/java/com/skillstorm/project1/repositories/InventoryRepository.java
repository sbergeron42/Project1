package com.skillstorm.project1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.project1.models.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

    // for summing inventory by warehouse
    List<Inventory> findByWarehouseId(int warehouseId);

    @Query("""
        SELECT i FROM Inventory i
        WHERE i.warehouse.id = :warehouseId
            AND (:name IS NULL OR LOWER(i.product.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:sku IS NULL OR LOWER(i.product.sku) LIKE LOWER(CONCAT('%', :sku, '%')))
            AND (:manufacturer IS NULL OR LOWER(i.product.manufacturer) LIKE LOWER(CONCAT('%', :manufacturer, '%')))
        """)
    List<Inventory> search(
        @Param("warehouseId") int warehouseId,
        @Param("name") String name,
        @Param("sku") String sku,
        @Param("manufacturer") String manufacturer//,
        // @Param("category") String category       // not implemented yet
        // also keeping this here             AND (:category IS NULL OR LOWER(i.product.category) LIKE LOWER(CONCAT('%', :category, '%')))
    );

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Inventory i WHERE i.warehouse.id = :warehouseId")
    int sumQuantitiesByWarehouse(int warehouseId);

    List<Inventory> findByWarehouseIdAndProductId(int warehouseId, int productId);


}
