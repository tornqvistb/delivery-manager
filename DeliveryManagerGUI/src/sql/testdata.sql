ALTER TABLE attachment CHANGE file_content file_content LONGBLOB default NULL;
delete from equipment where id > 0;
delete from order_line where id > 0;
delete from order_comment where id > 0;
delete from attachment where id > 0;
delete from order_header where id > 0;

insert into order_header(id, order_date, customer_name, order_number, customer_order_number, status, transmit_error_message)
values (1, '2016-01-01', 'Lundby', '11111', 'REQ11111', 'new', '');
insert into order_line(id, order_header_id, row_number, restriction_code, article_number, article_description, registered, remaining, total)
values (1, 1, 1, '1', 'HP001002', 'HP dator A1', 0, 5, 5);
insert into order_line(id, order_header_id, row_number, restriction_code, article_number, article_description, registered, remaining, total)
values (2, 1, 2, '1', 'HP001003', 'HP dator A2', 0, 4, 4);
insert into order_line(id, order_header_id, row_number, restriction_code, article_number, article_description, registered, remaining, total)
values (3, 1, 3, '0', 'HP001004', 'HP Docka', 0, 2, 2);
insert into order_comment(order_header_id, order_line, message, creation_date)
values (1, 'Generellt', 'Tack för din beställning!', '2016-01-01');

insert into order_header(id, order_date, customer_name, order_number, customer_order_number, status, transmit_error_message)
values (2, '2016-01-01', 'Lundby', '11112', 'REQ11112', 'started', '');
insert into order_line(id, order_header_id, row_number, restriction_code, article_number, article_description, registered, remaining, total)
values (4, 2, 1, '1', 'HP001002', 'HP dator A1', 0, 5, 5);
insert into order_line(id, order_header_id, row_number, restriction_code, article_number, article_description, registered, remaining, total)
values (5, 2, 2, '1', 'HP001003', 'HP dator A2', 2, 2, 4);
insert into equipment(order_line_id, serial_no, stealing_tag, creation_date)
values(5, 'SZ1111122222', '111111', '2016-01-01');
insert into equipment(order_line_id, serial_no, stealing_tag, creation_date)
values(5, 'SZ1111133333', '111112', '2016-01-01');
insert into order_comment(order_header_id, order_line, message, creation_date)
values (2, 'Generellt', 'Tack för din beställning!', '2016-01-01');
insert into order_comment(order_header_id, order_line, message, creation_date)
values (2, '1', 'Artikel HP A1 är slut. Kommer nästa vecka.', '2016-01-02');

insert into order_header(id, order_date, customer_name, order_number, customer_order_number, status, transmit_error_message)
values (3, '2016-01-01', 'Lundby', '11113', 'REQ11113', 'registration_done', '');
insert into order_header(id, order_date, customer_name, order_number, customer_order_number, status, transmit_error_message)
values (4, '2016-01-01', 'Lundby', '11114', 'REQ11114', 'not_accepted', '');


commit;
