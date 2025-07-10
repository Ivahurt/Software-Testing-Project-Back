create or replace view adults as
select first_name, last_name, unique_identification_number,
       extract(year from age(current_date, date_of_birth)) as age_in_years
from person
where extract(year from age(current_date, date_of_birth)) >= 18;