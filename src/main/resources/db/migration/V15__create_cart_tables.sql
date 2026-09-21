create table carts(
    id             BIGSERIAL primary key ,
    customer_id    BIGINT not null references customers(id),
    restaurant_id  BIGINT not null references restaurants(id),
    created_at     timestamp default now(),
    updated_at     timestamp default now()
);

create table cart_items(
    id             BIGSERIAL primary key ,
    cart_id        BIGINT not null references carts(id) on delete CASCADE ,
    food_menu_id   BIGINT not null references food_menus(id),
    quantity       integer not null default 1,
    price_snapshot decimal(10,2) not null ,
    created_at     timestamp default now()
);