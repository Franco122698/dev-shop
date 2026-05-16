-- Initialize DevShop Database Schema
-- This script creates the base tables for all services

-- Drop tables if they exist (for fresh start)
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;

-- Products table (Inventory Service)
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    store_id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19, 2) NOT NULL,
    stock_count INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_store UNIQUE (store_id, sku)
);

CREATE INDEX idx_products_store_id ON products(store_id);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_active ON products(is_active);

-- Orders table (Order Service)
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    store_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    total_price NUMERIC(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    failure_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_store_id ON orders(store_id);
CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- Sample data
INSERT INTO products (store_id, sku, name, description, price, stock_count, version)
VALUES
    (1, 'FLASH-001', 'Flash Sale Tee', 'Premium cotton t-shirt', 19.99, 100, 0),
    (1, 'FLASH-002', 'Flash Sale Hoodie', 'Comfortable cotton hoodie', 49.99, 50, 0),
    (2, 'FLASH-003', 'Premium Tee', 'Boutique B premium shirt', 29.99, 75, 0);

COMMIT;

