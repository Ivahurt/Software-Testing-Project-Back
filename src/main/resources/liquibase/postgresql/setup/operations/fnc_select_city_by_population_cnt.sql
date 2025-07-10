create or replace function select_cities_by_population(min_population int)
returns setof city as $$
begin
    return query
    select *
    from city
    where population > min_population;
end;
$$ language plpgsql;