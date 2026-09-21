create table order_history(
    id                bigserial primary key ,
    order_id          bigint not null references orders(id) on DELETE CASCADE ,
    food_menu_id      bigint not null references food_menus(id) on DELETE CASCADE ,
    quantity          integer not null ,
    price_per_item    decimal(10,2) not null
);