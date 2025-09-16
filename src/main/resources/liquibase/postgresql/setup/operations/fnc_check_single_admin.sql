create or replace function check_single_admin()
returns trigger as $$
begin
    if new.role = 'Administrator' then
        if exists (
            select 1 from users
            where role = 'Administrator'
              and id <> coalesce(new.id, -1)
        ) then
            raise exception 'Može postojati samo jedan korisnik sa ulogom Administrator.';
        end if;
    end if;

    return new;
end;
$$ language plpgsql;
