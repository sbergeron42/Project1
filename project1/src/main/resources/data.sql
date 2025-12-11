/* DELETE WHEN DONE TESTING */

insert into WAREHOUSES (name, location, max_capacity) values ('Headquarters', 'Orlando', 10000);
insert into WAREHOUSES (name, location, max_capacity) values ('North Hub', 'Jacksonville', 2000);
insert into WAREHOUSES (name, location, max_capacity) values ('South Hub', 'Miami', 4000);
insert into WAREHOUSES (name, location, max_capacity) values ('West Hub', 'Tampa', 3000);

insert into PRODUCTS (name, manufacturer, sku) values ('Orion Trackers', 'SomaticVR', 'ORI-FUL');
insert into PRODUCTS (name, manufacturer, sku) values ('HaritoraX 2', 'Shiftall', 'HAR-FUL');
insert into PRODUCTS (name, manufacturer, sku) values ('SlimeVR', 'SlimeVR BV', 'SLI-FUL');
insert into PRODUCTS (name, manufacturer, sku) values ('Fluxpose', 'Fluxpose SL', 'FLX-FUL');
insert into PRODUCTS (name, manufacturer, sku) values ('QSense Motion', '2M Engineering', 'QSE-FUL');

insert into INVENTORY (warehouse_id, product_id, quantity, storage_location) values
(1, 1, 5000, 'First Bay'),
(1, 1, 3000, 'Second Bay'),
(1, 2, 750, 'Third Bay'),
(2, 3, 800, 'Office'),
(2, 4, 250, 'Central Bay'),
(3, 5, 400, 'North Bay'),
(3, 1, 100, 'South Bay'),
(4, 2, 500, '1st Aisle'),
(4, 3, 700, '2nd Aisle');

-- Activity log entries for your actual warehouses
INSERT INTO ACTIVITY_LOG (activity_type, entity_type, entity_id, description, created_at) VALUES
('WAREHOUSE_CREATED', 'WAREHOUSE', 1, 'Created warehouse ''Headquarters''', NOW() - INTERVAL '30 days'),
('WAREHOUSE_CREATED', 'WAREHOUSE', 2, 'Created warehouse ''North Hub''', NOW() - INTERVAL '29 days'),
('WAREHOUSE_CREATED', 'WAREHOUSE', 3, 'Created warehouse ''South Hub''', NOW() - INTERVAL '28 days'),
('WAREHOUSE_CREATED', 'WAREHOUSE', 4, 'Created warehouse ''West Hub''', NOW() - INTERVAL '27 days'),
('INVENTORY_ADDED', 'INVENTORY', 1, 'Added 5000 units of ''Orion Trackers'' to Headquarters', NOW() - INTERVAL '26 days'),
('INVENTORY_ADDED', 'INVENTORY', 2, 'Added 3000 units of ''Orion Trackers'' to Headquarters', NOW() - INTERVAL '25 days'),
('INVENTORY_ADDED', 'INVENTORY', 3, 'Added 750 units of ''HaritoraX 2'' to Headquarters', NOW() - INTERVAL '24 days'),
('INVENTORY_TRANSFERRED', 'INVENTORY', 1, 'Transferred 100 units from Headquarters to North Hub', NOW() - INTERVAL '20 days'),
('INVENTORY_UPDATED', 'INVENTORY', 4, 'Updated quantity for ''SlimeVR'' in North Hub', NOW() - INTERVAL '15 days'),
('WAREHOUSE_UPDATED', 'WAREHOUSE', 1, 'Updated capacity for Headquarters', NOW() - INTERVAL '10 days'),
('INVENTORY_ADDED', 'INVENTORY', 5, 'Added 400 units of ''QSense Motion'' to South Hub', NOW() - INTERVAL '8 days'),
('CAPACITY_WARNING', 'WAREHOUSE', 1, 'Headquarters reached 88% capacity', NOW() - INTERVAL '5 days'),
('INVENTORY_TRANSFERRED', 'INVENTORY', 2, 'Transferred 200 units from Headquarters to South Hub', NOW() - INTERVAL '3 days'),
('INVENTORY_ADDED', 'INVENTORY', 7, 'Added 500 units of ''HaritoraX 2'' to West Hub', NOW() - INTERVAL '2 days'),
('WAREHOUSE_UPDATED', 'WAREHOUSE', 3, 'Updated storage layout for South Hub', NOW() - INTERVAL '1 day'),
('INVENTORY_UPDATED', 'INVENTORY', 8, 'Updated storage location for ''SlimeVR'' in West Hub', NOW() - INTERVAL '12 hours');

-- Headquarters (Orlando) - capacity snapshots showing gradual increase
INSERT INTO CAPACITY_SNAPSHOTS (warehouse_id, snapshot_date, current_capacity, max_capacity, utilization_percentage, total_items)
SELECT 
    1 as warehouse_id,
    (CURRENT_DATE - (n || ' days')::interval)::date as snapshot_date,
    LEAST(10000, 5000 + (n * 150)) as current_capacity,
    10000 as max_capacity,
    ROUND(LEAST(100, (5000 + (n * 150))::numeric / 10000 * 100), 2) as utilization_percentage,
    3 as total_items
FROM generate_series(30, 0, -1) as n;

-- North Hub (Jacksonville) - smaller warehouse with stable capacity
INSERT INTO CAPACITY_SNAPSHOTS (warehouse_id, snapshot_date, current_capacity, max_capacity, utilization_percentage, total_items)
SELECT 
    2 as warehouse_id,
    (CURRENT_DATE - (n || ' days')::interval)::date as snapshot_date,
    1000 + (random() * 200)::int as current_capacity,
    2000 as max_capacity,
    ROUND((1000 + (random() * 200))::numeric / 2000 * 100, 2) as utilization_percentage,
    2 as total_items
FROM generate_series(30, 0, -1) as n;

-- South Hub (Miami) - medium warehouse with fluctuating capacity
INSERT INTO CAPACITY_SNAPSHOTS (warehouse_id, snapshot_date, current_capacity, max_capacity, utilization_percentage, total_items)
SELECT 
    3 as warehouse_id,
    (CURRENT_DATE - (n || ' days')::interval)::date as snapshot_date,
    CASE 
        WHEN n > 15 THEN 300 + (n * 20)
        ELSE 600 - ((15 - n) * 10)
    END as current_capacity,
    4000 as max_capacity,
    ROUND(CASE 
        WHEN n > 15 THEN (300 + (n * 20))::numeric / 4000 * 100
        ELSE (600 - ((15 - n) * 10))::numeric / 4000 * 100
    END, 2) as utilization_percentage,
    2 as total_items
FROM generate_series(30, 0, -1) as n;

-- West Hub (Tampa) - medium warehouse with growing inventory
INSERT INTO CAPACITY_SNAPSHOTS (warehouse_id, snapshot_date, current_capacity, max_capacity, utilization_percentage, total_items)
SELECT 
    4 as warehouse_id,
    (CURRENT_DATE - (n || ' days')::interval)::date as snapshot_date,
    800 + ((30 - n) * 30) as current_capacity,
    3000 as max_capacity,
    ROUND((800 + ((30 - n) * 30))::numeric / 3000 * 100, 2) as utilization_percentage,
    2 as total_items
FROM generate_series(30, 0, -1) as n;