create table payments(
    id                   bigserial primary key ,
    order_id             bigint not null references orders(id),
    amount               decimal(10,2) not null ,
    payment_method       varchar(10) ,
    payment_status       varchar(10) default 'PENDING',
    transaction_id       varchar(255),
    failure_reason       text,
    created_at           timestamp default now(),
    updated_at           timestamp default now()
);