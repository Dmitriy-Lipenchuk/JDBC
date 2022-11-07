INSERT INTO organisations (name, inn, bank_account)
VALUES ('Intel', 100100100, 200200200);

INSERT INTO organisations (name, inn, bank_account)
VALUES ('Nvidia', 300300300, 400400400);

INSERT INTO organisations (name, inn, bank_account)
VALUES ('Kingston', 500500500, 600600600);

INSERT INTO products (id, name)
VALUES (1, 'CPU');

INSERT INTO products (id, name)
VALUES (2, 'GPU');

INSERT INTO products (id, name)
VALUES (3, 'RAM');

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-01', 1);

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-02', 1);

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-03', 1);


INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-01', 2);

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-02', 2);

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-03', 2);


INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-01', 3);

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-02', 3);

INSERT INTO invoices (date, organisation_id)
VALUES (date '2022-10-03', 3);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (1, 1, 500, 10);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (1, 1, 2000, 1);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (2, 2, 1000, 5);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (1, 3, 400, 50);


INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (2, 4, 800, 10);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (2, 5, 1000, 5);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (2, 5, 3000, 1);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (3, 6, 99, 56);


INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (3, 7, 100, 10);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (3, 8, 110, 5);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (3, 9, 80, 50);

INSERT INTO invoice_positions (product_id, invoice_id, price, quantity)
VALUES (3, 9, 110, 5);