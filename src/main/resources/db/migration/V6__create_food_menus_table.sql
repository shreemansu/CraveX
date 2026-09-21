create table food_menus(
    id                BIGSERIAL primary key ,
    restaurant_id     BIGINT not null references restaurants(id) on delete cascade ,
    item_name         varchar(255) not null ,
    food_type         varchar(20) not null ,
    description       text,
    price             decimal(10,2) not null ,
    is_available      boolean default true,
    image_url         varchar(500) not null ,
    preparation_time  integer default 30,
    created_at        timestamp default now(),
    updated_at        timestamp default now()
);