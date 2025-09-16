create table if not exists users (
    id bigserial primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    username varchar(50) not null unique,
    password varchar(255) not null,
    role varchar(30) not null
);