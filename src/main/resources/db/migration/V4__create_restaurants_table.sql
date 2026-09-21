create table restaurants(
    id                  BIGSERIAL primary key ,
    user_id             BIGINT not null unique references users(id) on DELETE CASCADE ,
    restaurantName      varchar(255) not null ,
    restaurantPhone     varchar(20) not null unique ,
    address             varchar(500) not null ,
    openingTime         time not null ,
    closingTime         time not null,
    is_open             boolean not null default true,
    location            GEOMETRY(Point,4326),
    image_url           varchar(500) not null ,
    created_at          timestamp default now(),
    updated_at          timestamp default now()
);