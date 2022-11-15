CREATE TABLE products
(
    id   INT     NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE organisations
(
    id           SERIAL  NOT NULL PRIMARY KEY,
    name         VARCHAR NOT NULL,
    inn          INT     NOT NULL UNIQUE,
    bank_account INT     NULL UNIQUE
);

CREATE TABLE invoices
(
    id              SERIAL    NOT NULL PRIMARY KEY,
    date            TIMESTAMP NOT NULL,
    organisation_id INT       NOT NULL REFERENCES organisations (id) ON UPDATE CASCADE
);

CREATE TABLE invoice_positions
(
    id         SERIAL NOT NULL PRIMARY KEY,
    product_id INT    NOT NULL REFERENCES products (id) ON UPDATE CASCADE,
    invoice_id INT    NOT NULL REFERENCES invoices (id) ON UPDATE CASCADE ON DELETE CASCADE,
    price      INT    NOT NULL CHECK (price >= 0),
    quantity   INT    NOT NULL CHECK (quantity > 0)
)