create or replace function update_sum_of_payments()
returns trigger as $$
begin
    perform set_config('app.bypass_sum_of_payments', 'on', true);

    if tg_op = 'INSERT' then
        update person
        set sum_of_payments = sum_of_payments + new.amount
        where id = new.person_id;

    elsif tg_op = 'DELETE' then
        update person
        set sum_of_payments = sum_of_payments - old.amount
        where id = old.person_id;

    elsif tg_op = 'UPDATE' then
        update person
        set sum_of_payments = sum_of_payments - old.amount + new.amount
        where id = new.person_id;
    end if;

    return null;
end;
$$ language plpgsql;

create trigger trg_update_sum_of_payments
after insert or update or delete on payment
for each row
execute function update_sum_of_payments();