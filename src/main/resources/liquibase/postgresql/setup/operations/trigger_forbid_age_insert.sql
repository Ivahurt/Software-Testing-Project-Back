create or replace function forbid_direct_age_insert()
returns trigger as $$
begin
if new.age_in_months is not null then
    raise notice 'Direktno unošenje starosti u godinama nije moguće';
    end if;

    new.age_in_months :=
        extract(year from age(current_date, new.date_of_birth)) * 12 +
        extract(month from age(current_date, new.date_of_birth));

    return new;
end;
$$ language plpgsql;

create trigger trg_forbid_age_insert
before insert or update of age_in_months on person
for each row
execute function forbid_direct_age_insert();