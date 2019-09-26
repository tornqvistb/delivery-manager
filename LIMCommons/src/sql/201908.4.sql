update customer_group set allow_pre_delivery_info = b'0';
update customer_group set allow_pre_delivery_info = b'1' where id = 1;
update order_header set transfer_date = delivery_date;
alter table order_header modify article_numbers VARCHAR(10000);