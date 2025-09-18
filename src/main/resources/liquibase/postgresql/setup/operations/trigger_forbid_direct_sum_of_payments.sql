create trigger trg_forbid_direct_sum_of_payments
before insert or update on person
for each  row
execute function forbid_direct_sum_of_payments();