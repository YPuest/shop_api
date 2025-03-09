INSERT INTO order_status (status) VALUES
    ('Pending'),
    ('Shipped'),
    ('Processing'),
    ('Delivered'),
    ('Cancelled');

INSERT INTO customer (name, email) VALUES
    ('Max Mustermann', 'max.mustermann@example.com'),
    ('Erika Musterfrau', 'erika.musterfrau@example.com'),
    ('John Doe', 'john.doe@example.com'),
    ('Jane Smith', 'jane.smith@example.com'),
    ('Michael Johnson', 'michael.johnson@example.com');

INSERT INTO address (customer_id, street, city, postal_code, country) VALUES
    (1, 'Musterstraße 1', 'Musterstadt', '12345', 'Deutschland'),
    (2, 'Beispielallee 42', 'Beispielstadt', '54321', 'Deutschland'),
    (3, '123 Elm Street', 'Springfield', '10001', 'USA'),
    (4, '456 Maple Ave', 'Anytown', '20002', 'USA'),
    (5, '789 Oak St', 'Metropolis', '30003', 'USA');

INSERT INTO category (name) VALUES
    ('Laptop'),
    ('Maus'),
    ('Tastatur'),
    ('Monitor');

INSERT INTO product (category_id, description, price, stock) VALUES
    (1, 'Gaming Laptop mit RTX 3070', 1499.99, 10),
    (2, 'Kabellose Gaming-Maus', 49.99, 50),
    (3, 'Mechanische RGB-Tastatur', 79.99, 30),
    (4, '27 Zoll 144Hz Gaming Monitor', 299.99, 15);

INSERT INTO orders (customer_id, status_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 1);

INSERT INTO order_item (order_id, product_id, quantity) VALUES
    (1, 1, 1),
    (1, 2, 1),
    (2, 4, 1),
    (2, 3, 1),
    (3, 5, 1),
    (4, 3, 1),
    (5, 1, 1),
    (5, 2, 2),
    (5, 4, 1);