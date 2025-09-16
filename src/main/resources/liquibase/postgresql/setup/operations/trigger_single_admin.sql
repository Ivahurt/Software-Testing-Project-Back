create trigger trg_single_admin
before insert or update on users
for each row
execute function check_single_admin();