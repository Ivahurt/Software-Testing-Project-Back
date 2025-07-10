create or replace view people_from_belgrade as
select *
from person
where city_birth_id = 1;