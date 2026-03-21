INSERT INTO users (name, email, role)
SELECT * FROM (
    SELECT 'John', 'john@example.com', 'CUSTOMER'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'john@example.com');

INSERT INTO users (name, email, role)
SELECT * FROM (
    SELECT 'Amal', 'amal@example.com', 'CUSTOMER'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'amal@example.com');

INSERT INTO users (name, email, role)
SELECT * FROM (
    SELECT 'Nimal', 'nimal@example.com', 'CUSTOMER'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'nimal@example.com');

INSERT INTO orders (order_id, total_amount, status, customer_id)
SELECT * FROM (
    SELECT 123, 500.00, 'PENDING_APPROVAL', (SELECT user_id FROM users WHERE email = 'john@example.com' LIMIT 1)
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE order_id = 123);

INSERT INTO orders (order_id, total_amount, status, customer_id)
SELECT * FROM (
    SELECT 124, 850.00, 'PENDING_APPROVAL', (SELECT user_id FROM users WHERE email = 'amal@example.com' LIMIT 1)
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE order_id = 124);

INSERT INTO orders (order_id, total_amount, status, customer_id)
SELECT * FROM (
    SELECT 125, 1200.00, 'PENDING_APPROVAL', (SELECT user_id FROM users WHERE email = 'nimal@example.com' LIMIT 1)
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE order_id = 125);
