create or replace function validate_age_in_months()
returns trigger as $$
declare
    expected_age int;
begin
    expected_age := extract(year from age(current_date, new.date_of_birth)) * 12 +
                    extract(month from age(current_date, new.date_of_birth));

    if new.age_in_months is not null and new.age_in_months != expected_age then
        raise exception 'Broj godina u mesecima je netačno';

    end if;

    return new;
end;
$$ language plpgsql;