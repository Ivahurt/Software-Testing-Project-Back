create or replace function protect_sum_of_payments()
returns trigger as $$
begin
    if current_setting('app.bypass_sum_of_payments', true) = 'on' then
        return new;
    end if;

    raise exception 'Direct update of sum_of_payments is not allowed';
end;
$$ language plpgsql;

create trigger trg_protect_sum_of_payments
before insert or update of sum_of_payments on person
for each row
execute function protect_sum_of_payments();