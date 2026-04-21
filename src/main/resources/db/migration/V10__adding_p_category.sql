-- 1. tambah column (nullable dulu)
ALTER TABLE products ADD COLUMN category VARCHAR(20);

-- 2. isi data lama
UPDATE products SET category = 'OTHER';

-- 3. enforce constraint
ALTER TABLE products ALTER COLUMN category SET NOT NULL;