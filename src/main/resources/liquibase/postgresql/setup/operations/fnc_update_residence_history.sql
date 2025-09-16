create or replace function update_residence_history()
returns trigger as $$
begin
    if new.city_residence_id is distinct from old.city_residence_id then
        raise notice 'Ne može se direktno ažurirati mesto stanovanja za osobu';

        update person_residence_history
        set residence_end = current_date
        where person_id = old.id
          and city_id = old.city_residence_id
          and residence_end is null;

        insert into person_residence_history (person_id, city_id, residence_start, residence_end)
        values (new.id, new.city_residence_id, current_date, null);
    end if;

    return new;
end;
$$ language plpgsql;