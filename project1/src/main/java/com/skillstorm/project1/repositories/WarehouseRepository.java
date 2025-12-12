package com.skillstorm.project1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.project1.models.Warehouse;

/**
 * This repository is responsible for performing CRUD operations on warehouses.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

}
