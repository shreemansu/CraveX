create table customers(
    id                  BIGSERIAL primary key ,
    user_id             BIGINT not null unique references users(id) on DELETE CASCADE ,
    address             varchar(500) not null ,
    location            GEOMETRY(Point,4326),
    created_at          timestamp default now(),
    updated_at          timestamp default now()
);
