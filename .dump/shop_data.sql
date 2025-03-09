INSERT INTO product (name, description, price, stock) VALUES
    ('Laptop', 'Gaming Laptop mit RTX 3070', 1499.99, 10),
    ('Maus', 'Kabellose Gaming-Maus', 49.99, 50),
    ('Tastatur', 'Mechanische RGB-Tastatur', 79.99, 30),
    ('Monitor', '27 Zoll 144Hz Gaming Monitor', 299.99, 15),
    ('Headset', 'Noise Cancelling Gaming Headset', 129.99, 20);

INSERT INTO customer (name, email, address) VALUES
    ('Max Mustermann', 'max.mustermann@example.com', 'Musterstraße 1, 12345 Musterstadt'),
    ('Erika Musterfrau', 'erika.musterfrau@example.com', 'Beispielallee 42, 54321 Beispielstadt'),
    ('John Doe', 'john.doe@example.com', '123 Elm Street, Springfield'),
    ('Jane Smith', 'jane.smith@example.com', '456 Maple Ave, Anytown'),
    ('Michael Johnson', 'michael.johnson@example.com', '789 Oak St, Metropolis');

INSERT INTO orders (customer_id, total_price, status) VALUES
    (1, 1549.98, 'Pending'),
    (2, 379.98, 'Shipped'),
    (3, 129.99, 'Processing'),
    (4, 79.99, 'Delivered'),
    (5, 1849.97, 'Pending');

INSERT INTO order_item (order_id, product_id, quantity, price) VALUES
    (1, 1, 1, 1499.99),
    (1, 2, 1, 49.99),
    (2, 4, 1, 299.99),
    (2, 3, 1, 79.99),
    (3, 5, 1, 129.99),
    (4, 3, 1, 79.99),
    (5, 1, 1, 1499.99),
    (5, 2, 2, 99.98),
    (5, 4, 1, 299.99);