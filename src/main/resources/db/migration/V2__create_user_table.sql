create table users(
    id                  BIGSERIAL primary key,
    email               varchar(255) not null unique ,
    password            varchar(255) not null ,
    phone               varchar(20) not null unique ,
    first_name           varchar(255) not null ,
    last_name            varchar(255) not null ,
    is_active           boolean not null default true,
    role                varchar(15) not null ,
    account_status      varchar(10) not null ,
    created_at          timestamp default now(),
    updated_at          timestamp default now()
);