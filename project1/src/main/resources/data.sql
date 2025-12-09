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
(1, 1, 50, 'First Bay'),
(1, 1, 30, 'Second Bay'),
(1, 2, 20, 'Third Bay'),
(2, 3, 15, 'Office'),
(2, 4, 25, 'Central Bay'),
(3, 5, 40, 'North Bay'),
(3, 1, 10, 'South Bay'),
(4, 2, 5, '1st Aisle'),
(4, 3, 7, '2nd Aisle');
