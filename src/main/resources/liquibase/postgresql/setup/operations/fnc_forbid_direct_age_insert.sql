create or replace function forbid_direct_age_insert()
returns trigger as $$
begin
if new.age_in_months is not null then
    raise exception 'Direktno unošenje starosti u godinama nije moguće';

    end if;
    return new;
end;
$$ language plpgsql;