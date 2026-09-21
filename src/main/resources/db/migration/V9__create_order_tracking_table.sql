create table order_tracking(
    id                 bigserial primary key ,
    order_id           bigint not null references orders(id) ,
    delivery_agent_id  bigint not null references delivery_agents(id),
    location           geometry(Point,4326) not null,
    timestamp          timestamp not null default now()
);