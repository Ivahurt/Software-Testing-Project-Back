create or replace function protect_sum_of_payments()
returns trigger as $$
begin
    if current_setting('app.bypass_sum_of_payments', true) = 'on' then
        return new;
    end if;

    if TG_OP = 'INSERT' and NEW.sum_of_payments is distinct from 0 then
        raise exception 'Direktan unos kolone sum_of_payments nije dozvoljen';
    end if;

    if TG_OP = 'UPDATE' and NEW.sum_of_payments is distinct from OLD.sum_of_payments then
            raise exception 'Direktna izmena kolone sum_of_payments nije dozvoljena';
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_protect_sum_of_payments
before insert or update of sum_of_payments on person
for each row
execute function protect_sum_of_payments();