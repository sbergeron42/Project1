package com.skillstorm.project1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.models.Product;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.repositories.ProductRepository;
import com.skillstorm.project1.repositories.WarehouseRepository;

/**
 * This service is responsible for handling operations related to products
 * such as fetching all products and updating product details.
 */
@Service
public class ProductService {

    /**
     * Constructor injection
     */
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Returns a list of all products in the system.
     * @return All Product objects
     */
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Updates an existing product by its ID.
     * @param id The product ID
     * @param updated Product object containing updated values
     * @return The updated product, or null if not found
     */
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
    }

}
