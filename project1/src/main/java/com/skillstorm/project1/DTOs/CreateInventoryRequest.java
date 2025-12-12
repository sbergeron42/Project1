package com.skillstorm.project1.DTOs;

/**
 * Request DTO for creating a new inventory item within a warehouse.
 * Contains product details, quantity, and storage location information.
 */
public class CreateInventoryRequest {

    private int warehouseId;
    private ProductDTO product;
    private int quantity;
    private String storageLocation;

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public static class ProductDTO {
        private String name;
        private String manufacturer;
        private String sku;
        private String description;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getManufacturer() {
            return manufacturer;
        }
        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }
        public String getSku() {
            return sku;
        }
        public void setSku(String sku) {
            this.sku = sku;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        
    }

}
