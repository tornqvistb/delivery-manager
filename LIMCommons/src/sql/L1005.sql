update order_header set creation_date = sysdate() where status = 'receiving';
update order_header set creation_date = order_date where status != 'receiving';
commit;