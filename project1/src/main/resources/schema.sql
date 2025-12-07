/* DELETE WHEN DONE TESTING */

drop table if exists WAREHOUSES;
drop table if exists PRODUCTS;
drop table if exists INVENTORY;

create table WAREHOUSES (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    location VARCHAR(50),
    max_capacity INT
    /* add more later */
);

create table PRODUCTS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    manufacturer VARCHAR(100),
    sku VARCHAR(50) UNIQUE,        -- identifies the product type
    description TEXT
    /* add more later */
);

create table INVENTORY (
    id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT,
    product_id INT,
    quantity INT DEFAULT 0,
    FOREIGN KEY (warehouse_id) REFERENCES WAREHOUSES(id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(id)
);