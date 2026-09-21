create table notifications(
    id                   bigserial primary key ,
    order_id             bigint not null references orders(id),
    recipient_id         bigint not null ,
    recipient_type       varchar(15) not null ,
    notification_type    varchar(20) not null ,
    message              text,
    channel_type         varchar(20) not null ,
    is_read              boolean default false,
    created_at           timestamp default now()
);