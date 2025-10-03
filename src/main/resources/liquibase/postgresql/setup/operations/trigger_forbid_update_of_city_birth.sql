create or replace function forbid_birth_city_update()
returns trigger as $$
begin
    if new.city_birth_id is distinct from old.city_birth_id then
        raise EXCEPTION 'Mesto rođenja ne može da se izmeni';
    end if;
    return new;
end
$$ language plpgsql;

create trigger trg_forbid_birth_city_update
before update on person
for each row
execute function forbid_birth_city_update();