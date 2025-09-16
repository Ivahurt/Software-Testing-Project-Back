create trigger trg_forbid_age_insert
before insert or update of age_in_months on person
for each row
execute function forbid_direct_age_insert();