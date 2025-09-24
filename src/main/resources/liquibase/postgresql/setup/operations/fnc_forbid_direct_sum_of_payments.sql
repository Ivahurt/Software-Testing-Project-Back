create or replace function forbid_direct_sum_of_payments()
returns trigger as $$
begin
   if new.sum_of_payments is distinct from 0 then
    raise EXCEPTION 'Direktan unos ukupne isplate nije dozvoljen';
   end if;

   return new;
end;
$$ language plpgsql;