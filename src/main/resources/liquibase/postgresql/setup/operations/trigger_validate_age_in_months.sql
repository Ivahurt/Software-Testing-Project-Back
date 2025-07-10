create trigger trigger_validate_age_in_months
before insert or update on person
for each row
execute function validate_age_in_months();