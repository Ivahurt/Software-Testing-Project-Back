create or replace function check_unique_person_id_before_insert()
returns trigger as $$
declare
    existing_person person;
begin
    select *
    into existing_person
    from person
    where unique_identification_number = new.unique_identification_number
    limit 1;

    if existing_person is not null then
        raise exception 'Osoba sa id-em % već postoji: %', new.unique_identification_number, existing_person.id;
    end if;

    return new;
end;
$$ language plpgsql;
