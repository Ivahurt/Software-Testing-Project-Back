create or replace function get_postalcode_by_city_name(city_name_param text)
returns int as $$
declare
    result_postalcode int;
begin
    select postal_code
    into result_postalcode
    from city
    where name = city_name_param
    limit 1;

    return result_postalcode;
end;
$$ language plpgsql;