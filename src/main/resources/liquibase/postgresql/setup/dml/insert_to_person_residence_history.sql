INSERT INTO person_residence_history (person_id, city_id, residence_start, residence_end)
SELECT
    p.id,
    p.city_residence_id,
    CURRENT_DATE,
    NULL
FROM person p
WHERE p.city_residence_id IS NOT NULL;