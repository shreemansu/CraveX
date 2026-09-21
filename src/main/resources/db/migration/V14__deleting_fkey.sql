alter table order_items DROP CONSTRAINT order_history_food_menu_id_fkey;

alter table order_items drop constraint order_history_order_id_fkey;

alter table order_items add constraint fk_order_items_food_menu foreign key (food_menu_id) references food_menus(id);

alter table order_items add constraint fk_order_items_order foreign key (order_id) references orders(id) on DELETE CASCADE ;