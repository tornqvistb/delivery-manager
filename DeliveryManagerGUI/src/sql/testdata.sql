delete from equipment;
delete from order_line;
delete from order_header;
select * from order_header;
select * from order_line;

insert into order_header(id, creation_date, customer, description, order_id, reference_id, status, transmit_error_message)
values (1, '2016-01-01', 'Lundby', 'Pelles datorer', '11111', 'REQ11111', 'Ny', '');
insert into order_line(id, order_header_id, line_id, has_serial_no, model, price, registered, remaining, total)
values (1, 1, 1, 1, 'HP A1', 5000, 0, 5, 5);
insert into order_line(id, order_header_id, line_id, has_serial_no, model, price, registered, remaining, total)
values (2, 1, 2, 1, 'HP A2', 4000, 0, 4, 4);
insert into order_line(id, order_header_id, line_id, has_serial_no, model, price, registered, remaining, total)
values (3, 1, 3, 0, 'HP Docka', 1000, 0, 2, 2);

insert into order_header(id, creation_date, customer, description, order_id, reference_id, status, transmit_error_message)
values (2, '2016-01-01', 'Lundby', 'Kalles datorer', '11112', 'REQ11112', 'Påbörjad', '');
insert into order_line(id, order_header_id, line_id, has_serial_no, model, price, registered, remaining, total)
values (4, 2, 1, 1, 'HP A1', 5000, 0, 5, 5);
insert into order_line(id, order_header_id, line_id, has_serial_no, model, price, registered, remaining, total)
values (5, 2, 2, 1, 'HP A2', 4000, 2, 2, 4);
insert into equipment(order_line_id, serial_no, stealing_tag, creation_date)
values(5, 'SZ1111122222', '111111', '2016-01-01');
insert into equipment(order_line_id, serial_no, stealing_tag, creation_date)
values(5, 'SZ1111133333', '111112', '2016-01-01');
commit;
