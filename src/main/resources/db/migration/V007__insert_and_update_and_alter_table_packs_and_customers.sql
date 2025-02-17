INSERT INTO customers (name, document, phone_number, email, address)
SELECT 'Default Customer', '12345678900', '11995551234', 'default@customer.com', '123 Default St'
FROM packs
WHERE NOT EXISTS (
    SELECT * FROM customers WHERE name = 'Default Customer'
)
LIMIT 1;

UPDATE packs
SET customer_id = 1
WHERE customer_id IS NULL;


ALTER TABLE packs 
MODIFY COLUMN customer_id BIGINT NOT NULL,
ADD CONSTRAINT fk_customer
FOREIGN KEY (customer_id) REFERENCES customers(id);
