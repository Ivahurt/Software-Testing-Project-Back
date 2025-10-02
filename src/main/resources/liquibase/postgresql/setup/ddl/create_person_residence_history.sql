    CREATE TABLE  person_residence_history (
        person_id BIGINT NOT NULL,
        city_id BIGINT NOT NULL,
        residence_start DATE NOT NULL,
        residence_end DATE,

        PRIMARY KEY (person_id, city_id, residence_start),

        FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE,
        FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE RESTRICT,

        CONSTRAINT chk_dates CHECK (
            residence_end IS NULL OR residence_end >= residence_start
        )
    );