alter table reports_config drop show_contact_persons;
alter table reports_config drop show_contact_persons1;
alter table reports_config drop show_contact_persons2;

update reports_config set show_contact_person1 = b'1',
show_contact_person2 = b'1',
show_customer_city = b'1',
show_customer_name = b'1',
show_customer_number = b'1',
show_customer_order_number = b'1',
show_customer_sales_order = b'1',
show_delivery_address = b'1',
show_leasing_number = b'1',
show_delivery_date = b'1',
show_netset_order_number = b'1',
show_order_date = b'1',
show_order_number = b'1';
 
