CREATE TABLE IF NOT EXISTS payment (
    person_id BIGINT NOT NULL,
    payment_id BIGSERIAL NOT NULL,
    amount BIGINT NOT NULL CHECK (amount > 0),
    reason VARCHAR(255) NOT NULL,
    payment_date DATE NOT NULL,
    PRIMARY KEY (person_id, payment_id),
    FOREIGN KEY (person_id) REFERENCES person(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);