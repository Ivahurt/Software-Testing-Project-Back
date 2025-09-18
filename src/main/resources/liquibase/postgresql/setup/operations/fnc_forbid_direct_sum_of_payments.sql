create or replace function forbid_direct_sum_of_payments()
returns trigger as $$
begin
    if new.sum_of_payments is distinct from old.sum_of_payments then
        raise exception 'Direktan unos ili izmena ukupne isplate za osobu nije dozvoljen';
    end if;
    return new;
end;
$$ language plpgsql;
