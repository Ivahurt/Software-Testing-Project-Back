create trigger trg_protect_sum_of_payments
before insert or update of sum_of_payments on person
for each row
execute function protect_sum_of_payments();