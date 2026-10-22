-- Run this whole script once in MySQL Workbench
CREATE DATABASE IF NOT EXISTS shopping_db;
USE shopping_db;

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;

CREATE TABLE products (
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category     VARCHAR(50)  NOT NULL,
    price        DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    stock        INT NOT NULL CHECK (stock >= 0),
    description  VARCHAR(255)
);

CREATE TABLE orders (
    order_id       INT AUTO_INCREMENT PRIMARY KEY,
    order_date     DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount   DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    order_status   VARCHAR(30) NOT NULL DEFAULT 'PLACED'
);

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id      INT NOT NULL,
    product_id    INT NOT NULL,
    quantity      INT NOT NULL CHECK (quantity > 0),
    price         DECIMAL(10,2) NOT NULL,
    subtotal      DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO products (product_name, category, price, stock, description) VALUES
('Wireless Mouse',      'Electronics', 599.00,  50, '2.4GHz wireless optical mouse'),
('Mechanical Keyboard', 'Electronics', 2499.00, 25, 'RGB mechanical keyboard'),
('Bluetooth Headphones','Electronics', 1799.00, 30, 'Over-ear with mic'),
('USB-C Charger 65W',   'Electronics', 1299.00, 40, 'Fast charger for phones and laptops'),
('Cotton T-Shirt',      'Clothing',    499.00,  100,'Plain cotton round neck'),
('Running Shoes',       'Footwear',    2999.00, 20, 'Lightweight daily trainers'),
('Notebook (Pack of 5)','Stationery',  250.00,  80, 'Ruled 200-page notebooks'),
('Water Bottle 1L',     'Home',        399.00,  60, 'Stainless steel bottle'),
('Backpack 30L',        'Accessories', 1499.00, 35, 'Water-resistant laptop backpack');
