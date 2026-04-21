-- 1. tambah column baru
ALTER TABLE products ADD COLUMN seller_id UUID;

-- 2. copy data lama
UPDATE products SET seller_id = created_by;

-- 3. set not null
ALTER TABLE products ALTER COLUMN seller_id SET NOT NULL;

-- 4. drop old column
ALTER TABLE products DROP COLUMN created_by;

ALTER TABLE products
    ADD CONSTRAINT fk_product_seller
        FOREIGN KEY (seller_id) REFERENCES users(id);