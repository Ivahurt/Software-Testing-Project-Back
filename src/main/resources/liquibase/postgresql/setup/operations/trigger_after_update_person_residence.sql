create trigger trg_update_residence_history
after update of city_residence_id on person
for each row
execute function update_residence_history();