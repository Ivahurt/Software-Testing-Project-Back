create or replace function forbid_direct_sum_of_payments()
returns trigger as $$
begin
   if current_setting('app.bypass_sum_of_payments', true) = 'on' then
      return new;
   end if;

   if TG_OP = 'INSERT' and NEW.sum_of_payments is distinct from 0 then
      raise EXCEPTION 'Direktan unos ukupne sume za isplatu nije dozvoljen';
   end if;

   if TG_OP = 'UPDATE'
      and NEW.sum_of_payments is distinct from OLD.sum_of_payments then
      raise EXCEPTION 'Direktna izmena ukupne sume za isplatu nije dozvoljena';
   end if;

   return new;
end;
$$ language plpgsql;

create trigger trg_forbid_direct_sum_of_payments
before insert or update on person
for each row
execute function forbid_direct_sum_of_payments();