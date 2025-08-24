create or replace function check_person_residence_history()
returns trigger as $$
begin
    if exists (
        select 1
        from person_residence_history
        where person_id = new.person_id
          and residence_end is null
    ) then
        raise exception 'Osoba % već ima aktivno mesto stanovanja.',
            new.person_id;
    end if;

    return new;
end;
$$ language plpgsql;