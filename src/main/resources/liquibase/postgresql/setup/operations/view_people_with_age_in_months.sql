create view person_with_age as
select p.*,
    (extract (year from age(current_date, p.date_of_birth)) * 12 +
    extract (month from age(current_date, p.date_of_birth))) :: int
        as age_in_months_valid
from person p;