create triger_check_postal_codes_before_insertion
before insert on city
for each row
execute function check_postal_code_before_insert();