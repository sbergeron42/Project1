package com.skillstorm.project1.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This is responsible for representinga historical snapshot of a warehouse's capacity on a specific date. 
 * Used to track trends in storage usage and utilization over time.
 */
@Entity
@Table(name = "CAPACITY_SNAPSHOTS")
public class CapacitySnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "snapshot_date")
    private LocalDate snapshotDate;

    @Column(name = "current_capacity")
    private int currentCapacity;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "utilization_percentage")
    private double utilizationPercentage;

    public CapacitySnapshot() {
    }

    public CapacitySnapshot(Warehouse warehouse, LocalDate snapshotDate, int currentCapacity, int maxCapacity,
            double utilizationPercentage) {
        this.warehouse = warehouse;
        this.snapshotDate = snapshotDate;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.utilizationPercentage = utilizationPercentage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public double getUtilizationPercentage() {
        return utilizationPercentage;
    }

    public void setUtilizationPercentage(double utilizationPercentage) {
        this.utilizationPercentage = utilizationPercentage;
    }

    

}
