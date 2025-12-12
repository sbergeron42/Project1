package com.skillstorm.project1.DTOs;

import com.skillstorm.project1.models.Warehouse;

/**
 * This DTO is responsible for representing a warehouse along with its calculated capacity.
 * Used for returning summarized warehouse information in API responses.
 */
public class WarehouseDTO {

    private int id;
    private String name;
    private String location;
    private int maxCapacity;
    private int currentCapacity;

    public WarehouseDTO(Warehouse warehouse, int currentCapacity) {
        this.id = warehouse.getId();
        this.name = warehouse.getName();
        this.location = warehouse.getLocation();
        this.maxCapacity = warehouse.getMaxCapacity();
        this.currentCapacity = currentCapacity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentCapacity() { return currentCapacity; }
}
