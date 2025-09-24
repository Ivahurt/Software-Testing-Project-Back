create or replace function forbid_direct_sum_of_payments()
returns trigger as $$
begin
    if tg_op = 'insert' and new.sum_of_payments is not null and new.sum_of_payments <> 0 then
        raise exception 'Direktan unos ukupne isplate za osobu nije dozvoljen';
    end if;

    if tg_op = 'update' and new.sum_of_payments is distinct from old.sum_of_payments then
        raise exception 'Direktna izmena ukupne isplate za osobu nije dozvoljena';
    end if;

    return new;
end;
$$ language plpgsql;