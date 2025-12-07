package com.skillstorm.project1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Warehouse {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // warehouse name, e.g. Headquarters
    @Column
    private String name;

    @Column
    private String location;

    // maybe should have a limit on how high the number can be?
    @Column
    private int maxCapacity;

    // same here
    @Column
    private int currentCapacity;

    // inventoryProducts here? idk 



}
