create trigger trg_update_sum_of_payments
after insert or update or delete on payment
for each row
execute function update_sum_of_payments();