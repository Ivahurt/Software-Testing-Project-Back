create table if not exists users (
    id bigserial primary key,
    first_name varchar(50) not null check (
        first_name ~ '^[A-ZČĆŽŠĐ][a-zčćžšđ]+$'
    ),
    last_name varchar(50) not null check (
        last_name ~ '^[A-ZČĆŽŠĐ][a-zčćžšđ]+$'
    ),
    username varchar(50) not null unique check (
        username ~ '^(?=.*[a-zA-ZČĆŽŠĐ])[a-zA-Z0-9ČĆŽŠĐ]{4,}$'
    ),
    password varchar(255) not null check (
        password ~ '^(?=.*[a-zA-ZČĆŽŠĐ])[a-zA-Z0-9ČĆŽŠĐ]{4,}$'
    ),
    role varchar(30) not null
);