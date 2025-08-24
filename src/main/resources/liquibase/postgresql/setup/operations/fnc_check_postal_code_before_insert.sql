create or replace function check_postal_code_before_insert()
returns trigger as $$
declare
    existing_city city;
begin
    existing_city := select_city_by_postal_code(new.postalcode);
    if existing_city is not null then
        raise exception 'Poštanski kod % već postoji za mesto sa id: %', new.postalcode, existing_city.city_id;
    end if;
    return new;
end;
$$ language plpgsql;