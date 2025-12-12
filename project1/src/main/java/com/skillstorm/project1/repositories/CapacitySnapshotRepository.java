package com.skillstorm.project1.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.project1.models.CapacitySnapshot;

/**
 * This is a repository responsible for retrieving warehouse capacity snapshot data.
 * Used for trend analysis and reporting.
 */
@Repository
public interface CapacitySnapshotRepository extends JpaRepository<CapacitySnapshot, Integer> {

    /**
     * Finds all capacity snapshots for a warehouse, ordered from newest to oldest.
     * @param warehouseId the warehouse ID
     * @return ordered list of snapshots (descending by date)
     */
    List<CapacitySnapshot> findByWarehouseIdOrderBySnapshotDateDesc(int warehouseId);

    /**
     * Retrives snapshots for a warehouse within a specific date range
     * @param warehouseId the warehouse ID
     * @param startDate start of date range (inclusive)
     * @param endDate end of date range (inclusive)
     * @return list of snapshots iwthin the given date range
     */
    List<CapacitySnapshot> findByWarehouseIdAndSnapshotDateBetweenOrderBySnapshotDate(
        int warehouseId, LocalDate startDate, LocalDate endDate);
}
