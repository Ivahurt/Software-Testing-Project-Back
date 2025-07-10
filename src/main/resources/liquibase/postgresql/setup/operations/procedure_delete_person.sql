create or replace procedure delete_person(person_delete_id bigint)
as $$
begin
delete from person where id = person_delete_id;
end;
$$ language plpgsql;