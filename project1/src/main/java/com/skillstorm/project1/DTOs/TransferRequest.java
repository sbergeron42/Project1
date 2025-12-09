package com.skillstorm.project1.DTOs;

public class TransferRequest {

    private int sourceWarehouseId;
    private int destinationWarehouseId;
    private int inventoryId;
    private int quantity;

    public int getSourceWarehouseId() {
        return sourceWarehouseId;
    }
    public void setSourceWarehouseId(int sourceWarehouseId) {
        this.sourceWarehouseId = sourceWarehouseId;
    }
    public int getDestinationWarehouseId() {
        return destinationWarehouseId;
    }
    public void setDestinationWarehouseId(int destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }
    public int getInventoryId() {
        return inventoryId;
    }
    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
