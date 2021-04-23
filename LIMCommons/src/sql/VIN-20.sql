update order_header set flexible_registration = b'0' where id > 0;
commit;