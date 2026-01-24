SET FOREIGN_KEY_CHECKS = 0;

-- =============================
-- CLEAN (SAFE ORDER)
-- =============================
DELETE FROM order_hub_tracking;
DELETE FROM order_status_history;
DELETE FROM delivery_assignment;
DELETE FROM payment;
DELETE FROM courier_order;
DELETE FROM delivery_staff_profile;
DELETE FROM hub_route;
DELETE FROM hub;
DELETE FROM address;
DELETE FROM support_ticket;
DELETE FROM users;
DELETE FROM city;

ALTER TABLE city AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE hub AUTO_INCREMENT = 1;
ALTER TABLE address AUTO_INCREMENT = 1;
ALTER TABLE courier_order AUTO_INCREMENT = 1;
ALTER TABLE delivery_staff_profile AUTO_INCREMENT = 1;

-- =============================
-- CITIES (5)
-- =============================
INSERT INTO city (cityName, latitude, longitude, state)
VALUES
('Pune', 18.5204, 73.8567, 'Maharashtra'),
('Mumbai', 19.0760, 72.8777, 'Maharashtra'),
('Bangalore', 12.9716, 77.5946, 'Karnataka'),
('Delhi', 28.6139, 77.2090, 'Delhi'),
('Hyderabad', 17.3850, 78.4867, 'Telangana');

-- =============================
-- USERS (12)
-- =============================
INSERT INTO users
(createdAt, updatedAt, email, name, password, phone, role, status)
VALUES
-- ADMIN
(NOW(), NOW(), 'admin@courierwala.com', 'Admin User', 'Admin@123', '9000000001', 'ROLE_ADMIN', 'ACTIVE'),

-- STAFF MANAGERS (5)
(NOW(), NOW(), 'manager.pune@cw.com', 'Pune Manager', 'Manager@123', '9000000002', 'ROLE_STAFF_MANAGER', 'ACTIVE'),
(NOW(), NOW(), 'manager.mumbai@cw.com', 'Mumbai Manager', 'Manager@123', '9000000003', 'ROLE_STAFF_MANAGER', 'ACTIVE'),
(NOW(), NOW(), 'manager.blr@cw.com', 'Bangalore Manager', 'Manager@123', '9000000004', 'ROLE_STAFF_MANAGER', 'ACTIVE'),
(NOW(), NOW(), 'manager.delhi@cw.com', 'Delhi Manager', 'Manager@123', '9000000005', 'ROLE_STAFF_MANAGER', 'ACTIVE'),
(NOW(), NOW(), 'manager.hyd@cw.com', 'Hyderabad Manager', 'Manager@123', '9000000006', 'ROLE_STAFF_MANAGER', 'ACTIVE'),

-- DELIVERY STAFF (3)
(NOW(), NOW(), 'delivery1@cw.com', 'Delivery One', 'Del@123', '9000000007', 'ROLE_DELIVERY_STAFF', 'ACTIVE'),
(NOW(), NOW(), 'delivery2@cw.com', 'Delivery Two', 'Del@123', '9000000008', 'ROLE_DELIVERY_STAFF', 'ACTIVE'),
(NOW(), NOW(), 'delivery3@cw.com', 'Delivery Three', 'Del@123', '9000000009', 'ROLE_DELIVERY_STAFF', 'ACTIVE'),

-- CUSTOMERS (3)
(NOW(), NOW(), 'cust1@gmail.com', 'Customer One', 'Cust@123', '9000000010', 'ROLE_CUSTOMER', 'ACTIVE'),
(NOW(), NOW(), 'cust2@gmail.com', 'Customer Two', 'Cust@123', '9000000011', 'ROLE_CUSTOMER', 'ACTIVE'),
(NOW(), NOW(), 'cust3@gmail.com', 'Customer Three', 'Cust@123', '9000000012', 'ROLE_CUSTOMER', 'ACTIVE');

-- =============================
-- HUBS (5)
-- =============================
INSERT INTO hub (hubName, latitude, longitude, city_id)
VALUES
('Pune Hub', 18.5204, 73.8567, 1),
('Mumbai Hub', 19.0760, 72.8777, 2),
('Bangalore Hub', 12.9716, 77.5946, 3),
('Delhi Hub', 28.6139, 77.2090, 4),
('Hyderabad Hub', 17.3850, 78.4867, 5);

-- Assign managers (1 hub = 1 manager)
UPDATE hub SET manager_id = 2 WHERE hub_id = 1;
UPDATE hub SET manager_id = 3 WHERE hub_id = 2;
UPDATE hub SET manager_id = 4 WHERE hub_id = 3;
UPDATE hub SET manager_id = 5 WHERE hub_id = 4;
UPDATE hub SET manager_id = 6 WHERE hub_id = 5;

-- =============================
-- HUB ROUTES (8)
-- =============================
INSERT INTO hub_route (distanceKm, from_hub_id, to_hub_id)
VALUES
(150, 1, 2),
(840, 1, 3),
(1400, 1, 4),
(620, 2, 5),
(2150, 3, 4),
(570, 3, 5),
(1600, 4, 2),
(710, 5, 1);

-- =============================
-- DELIVERY STAFF PROFILES (3)
-- =============================
INSERT INTO delivery_staff_profile
(activeOrders, isAvailable, isVerified, licenseNumber, rating, totalDeliveries,
 vehicleNumber, vehicleType, hub_id, user_id)
VALUES
(2, b'1', b'1', 'MH12DL1111', 4.6, 150, 'MH12AA1111', 'BIKE', 1, 7),
(1, b'1', b'1', 'MH14DL2222', 4.3, 120, 'MH14BB2222', 'SCOOTER', 2, 8),
(0, b'1', b'1', 'KA01DL3333', 4.1, 90,  'KA01CC3333', 'BIKE', 3, 9);

-- =============================
-- ADDRESSES (6)
-- =============================
INSERT INTO address
(addressLine, isDefault, pincode, city_id, user_id)
VALUES
('Baner, Pune', b'1', '411045', 1, 10),
('Hinjewadi, Pune', b'0', '411057', 1, 10),
('Andheri, Mumbai', b'1', '400069', 2, 11),
('Bandra, Mumbai', b'0', '400050', 2, 11),
('Whitefield, Bangalore', b'1', '560066', 3, 12),
('Electronic City, Bangalore', b'0', '560100', 3, 12);

-- =============================
-- COURIER ORDERS (6)
-- =============================
INSERT INTO courier_order
(deliveryType, distanceKm, orderStatus, packageType, packageWeight, price,
 trackingNumber, customer_id, pickup_address_id, delivery_address_id)
VALUES
('STANDARD', 150, 'CREATED', 'Box', 2.5, 350, 'TRK1001', 10, 1, 3),
('EXPRESS', 840, 'IN_TRANSIT', 'Envelope', 0.8, 1200, 'TRK1002', 11, 3, 5),
('STANDARD', 620, 'ASSIGNED', 'Box', 3.2, 500, 'TRK1003', 12, 5, 1),
('EXPRESS', 1400, 'PICKED_UP', 'Box', 5.0, 1800, 'TRK1004', 10, 2, 6),
('STANDARD', 570, 'CREATED', 'Bag', 1.1, 300, 'TRK1005', 11, 4, 2),
('EXPRESS', 710, 'IN_TRANSIT', 'Envelope', 0.5, 950, 'TRK1006', 12, 6, 3);

-- =============================
-- DELIVERY ASSIGNMENTS (4)
-- =============================
INSERT INTO delivery_assignment
(assignedBy, deliveryStatus, delivery_staff_id, manager_id, order_id)
VALUES
('MANAGER', 'ASSIGNED', 1, 2, 1),
('AUTO', 'PICKED_UP', 2, 3, 2),
('MANAGER', 'ASSIGNED', 3, 4, 3),
('AUTO', 'DELIVERED', 1, 5, 4);

SET FOREIGN_KEY_CHECKS = 1;

-- =============================
-- FINAL CHECK
-- =============================
SELECT
    h.hubName,
    c.cityName,
    u.name AS manager
FROM hub h
JOIN city c ON h.city_id = c.city_id
JOIN users u ON h.manager_id = u.user_id;
