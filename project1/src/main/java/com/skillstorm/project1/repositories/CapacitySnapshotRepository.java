package com.skillstorm.project1.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.project1.models.CapacitySnapshot;

@Repository
public interface CapacitySnapshotRepository extends JpaRepository<CapacitySnapshot, Integer> {
    List<CapacitySnapshot> findByWarehouseIdOrderBySnapshotDateDesc(int warehouseId);

    // Within date range
    List<CapacitySnapshot> findByWarehouseIdAndSnapshotDateBetweenOrderBySnapshotDate(
        int warehouseId, LocalDate startDate, LocalDate endDate);
}
