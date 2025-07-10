create trigger trigger_check_person_id_before_insert
before insert on person
for each row
execute function check_unique_person_id_before_insert();