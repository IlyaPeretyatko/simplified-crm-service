CREATE TABLE IF NOT EXISTS sellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    contact_info VARCHAR NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT REFERENCES sellers ON DELETE CASCADE,
    amount DECIMAL NOT NULL,
    payment_type VARCHAR NOT NULL,
    transaction_date TIMESTAMP NOT NULL
);