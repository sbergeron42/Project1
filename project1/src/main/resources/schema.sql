/* DELETE WHEN DONE TESTING */

drop table if exists INVENTORY CASCADE;
drop table if exists WAREHOUSES CASCADE;
drop table if exists PRODUCTS CASCADE;
drop table if exists ACTIVITY_LOG CASCADE;
drop table if exists CAPACITY_SNAPSHOTS CASCADE;


create table WAREHOUSES (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    location VARCHAR(50),
    max_capacity INT
    /* add more later */
);

create table PRODUCTS (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    manufacturer VARCHAR(100),
    sku VARCHAR(50) UNIQUE,        -- identifies the product type
    description TEXT
    /* add more later */
);

create table INVENTORY (
    id SERIAL PRIMARY KEY,
    warehouse_id INT,
    product_id INT,
    quantity INT DEFAULT 0,
    storage_location VARCHAR(50),
    FOREIGN KEY (warehouse_id) REFERENCES WAREHOUSES(id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(id),
    UNIQUE (warehouse_id, product_id, storage_location)
);

-- Activity Log Table
create table ACTIVITY_LOG (
    id SERIAL PRIMARY KEY,
    activity_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INTEGER,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER,
    details TEXT
);

-- Capacity Snapshots Table
create table CAPACITY_SNAPSHOTS (
    id SERIAL PRIMARY KEY,
    warehouse_id INTEGER NOT NULL,
    snapshot_date DATE NOT NULL,
    current_capacity INTEGER NOT NULL,
    max_capacity INTEGER NOT NULL,
    utilization_percentage DECIMAL(5,2) NOT NULL,
    total_items INTEGER,
    FOREIGN KEY (warehouse_id) REFERENCES WAREHOUSES(id) ON DELETE CASCADE,
    UNIQUE(warehouse_id, snapshot_date) -- Prevent duplicate snapshots for same day
);
