update customer_group set book_order_before_registration = 0 where id > 0;
update customer_group set delivery_flag_toerp = 0 where id > 0;
commit;