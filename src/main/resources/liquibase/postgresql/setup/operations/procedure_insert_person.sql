create or replace procedure insert_person(
    first_name_param VARCHAR,
    last_name_param VARCHAR,
    height_in_cm_param INT,
    date_of_birth_param DATE,
    unique_identification_number_param BIGINT,
    age_in_months_param INT,
    city_birth_id_param BIGINT,
    city_residence_id_param BIGINT
)
as $$
begin
    insert into person (
        first_name,
        last_name,
        height_in_cm,
        date_of_birth,
        unique_identification_number,
        age_in_months,
        city_birth_id,
        city_residence_id
    )
    values (
        first_name_param,
        last_name_param,
        height_in_cm_param,
        date_of_birth_param,
        unique_identification_number_param,
        age_in_months_param,
        city_birth_id_param,
        city_residence_id_param
    );
end;
$$ language plpgsql;