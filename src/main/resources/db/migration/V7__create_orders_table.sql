create table orders(
    id                              bigserial primary key ,
    order_number                    varchar(255) not null unique ,
    customer_id                     bigint not null references customers(id) ,
    restaurant_id                   bigint not null references restaurants(id),
    delivery_agent_id               bigint references delivery_agents(id),
    order_status                    varchar(20) not null default 'PLACED',
    total_amount                    decimal(10,2) not null ,
    delivery_address                varchar(500) not null ,
    delivery_location               geometry(Point,4326),
    estimated_delivery_time         timestamp,
    actual_delivery_time            timestamp,
    payment_status                  varchar(20) not null default 'PENDING',
    payment_method                  varchar(10) not null default 'UPI',
    created_at                      timestamp default now(),
    updated_at                      timestamp default now()
);