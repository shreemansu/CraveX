create table delivery_agents(
    id                 BIGSERIAL primary key ,
    user_id            BIGINT not null unique references users(id) on DELETE CASCADE ,
    vehicle_type       varchar(10) not null ,
    vehicle_number     varchar(20) not null ,
    is_available       boolean default false,
    current_location   GEOMETRY(Point,4326),
    created_at         timestamp default now(),
    updated_at         timestamp default now()
);