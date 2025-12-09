package com.skillstorm.project1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.models.Product;
import com.skillstorm.project1.models.Warehouse;
import com.skillstorm.project1.services.ProductService;
import com.skillstorm.project1.services.WarehouseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK); // return 200
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // return 500
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updated) {
        Product product = productService.updateProduct(id, updated);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
    

}
