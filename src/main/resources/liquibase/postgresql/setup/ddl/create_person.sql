CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(30)
    NOT NULL CHECK (LENGTH(first_name) BETWEEN 2 AND 30 AND first_name ~ '^[A-ZČĆŠŽĐ][a-zčćšžđ]{1,}$'),
    last_name VARCHAR(30)
    NOT NULL CHECK (LENGTH(last_name) BETWEEN 2 AND 30 AND last_name ~ '^[A-ZČĆŠŽĐ][a-zčćšžđ]{1,}$'),
    height_in_cm INT NOT NULL
    CHECK (height_in_cm between 70 and 260),
    date_of_birth DATE NOT NULL CHECK (date_of_birth BETWEEN '1950-01-01' AND '2005-12-31'),
    age_in_months INT,
    unique_identification_number BIGINT NOT NULL UNIQUE,
    city_birth_id BIGINT,
    city_residence_id BIGINT,
    FOREIGN KEY (city_birth_id) REFERENCES city(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (city_residence_id) REFERENCES city(id) ON UPDATE SET NULL ON DELETE SET NULL
);