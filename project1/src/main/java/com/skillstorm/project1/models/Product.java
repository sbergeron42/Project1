package com.skillstorm.project1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// need to see if our db table will match the tablename here
@Table
public class Product {
    // serial number, maybe SKU? not sure if they're separate, i mean probably right? idk
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // product name
    @Column
    private String name;
    
    // manufacturer name here. not sure how i'd set this up with the tables idk

    // description here

    // quantity here maybe? maybe that's in inventory

    // storageLocation here?

    // could put a user review rating here
    
    // i wonder if there should be a picture property here too?


}
