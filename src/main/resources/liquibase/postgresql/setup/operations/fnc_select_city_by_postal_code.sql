create or replace function select_city_by_postal_code(postalcode_param int)
returns city as $$
declare
    result_city city;
begin
    select *
    into result_city
    from city
    where postalcode = postalcode_param
    limit 1;

    return result_city;
end;
$$ language plpgsql;