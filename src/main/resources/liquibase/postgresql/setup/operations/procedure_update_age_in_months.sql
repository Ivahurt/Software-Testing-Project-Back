create or replace procedure update_person_age_in_month(person_id bigint)
as $$
begin
    update person
    set age_in_months = extract(year from age(current_date, date_of_birth)) * 12 +
                        extract(month from age(current_date, date_of_birth))

    where id = person_id;

end;
$$ language plpgsql;