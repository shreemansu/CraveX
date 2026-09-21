create index if not exists idx_restaurant_location on restaurants using gist (location);

create index if not exists idx_customer_location on customers using gist (location);

create index if not exists idx_agent_current_location on delivery_agents using gist (current_location);

create index if not exists idx_order_delivery_location on orders using gist (delivery_location);

create index if not exists idx_order_tracking_location on order_tracking using gist (location);
