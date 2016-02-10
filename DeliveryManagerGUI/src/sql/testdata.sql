ALTER TABLE attachment CHANGE file_content file_content LONGBLOB default NULL;
ALTER TABLE email CHANGE content content VARCHAR(10000) default NULL;
ALTER TABLE error_record CHANGE message message VARCHAR(10000) default NULL;
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
insert into equipment(order_line_id, serial_no, stealing_tag, creation_date, to_correct)
values(5, 'SZ1111122222', '111111', '2016-01-01', 0);
insert into equipment(order_line_id, serial_no, stealing_tag, creation_date, to_correct)
values(5, 'SZ1111133333', '111112', '2016-01-01', 0);
insert into order_comment(order_header_id, order_line, message, creation_date)
values (2, 'Generellt', 'Tack för din beställning!', '2016-01-01');
insert into order_comment(order_header_id, order_line, message, creation_date)
values (2, '1', 'Artikel HP A1 är slut. Kommer nästa vecka.', '2016-01-02');

insert into order_header(id, order_date, customer_name, order_number, customer_order_number, status, transmit_error_message)
values (3, '2016-01-01', 'Lundby', '11113', 'REQ11113', 'registration_done', '');
insert into order_header(id, order_date, customer_name, order_number, customer_order_number, status, transmit_error_message)
values (4, '2016-01-01', 'Lundby', '11114', 'REQ11114', 'not_accepted', '');

update order_header set customer_sales_order = customer_order_number where id > 0;

insert into system_property (id, string_value, number_value) values ('mailjob-frequency', '', 10000);
insert into system_property (id, string_value, number_value) values ('order-transmitjob-frequency', '', 10000);
insert into system_property (id, string_value, number_value) values ('orderjob-frequency', '', 10000);
insert into system_property (id, string_value, number_value) values ('mail-host', 'pop.gmail.com', 0);
insert into system_property (id, string_value, number_value) values ('mail-username', 'lim.lanteam@gmail.com', 0);
insert into system_property (id, string_value, number_value) values ('mail-password', 'limlanteam', 0);
insert into system_property (id, string_value, number_value) values ('mail-smtps-host', 'smtp.gmail.com', 0);
insert into system_property (id, string_value, number_value) values ('file-image-folder', 'C:/Projekt/lanteam/filedirs/images', 0);
insert into system_property (id, string_value, number_value) values ('file-source-folder', 'C:/Projekt/lanteam/filedirs/input', 0);
insert into system_property (id, string_value, number_value) values ('file-destination-folder', 'C:/Projekt/lanteam/filedirs/output', 0);
insert into system_property (id, string_value, number_value) values ('file-error-folder', 'C:/Projekt/lanteam/filedirs/error', 0);
insert into system_property (id, string_value, number_value) values ('file-transmit-folder', 'C:/Projekt/lanteam/filedirs/transmit', 0);
insert into system_property (id, string_value, number_value) values ('ws-endpoint-order-delivery', 'http://esbat.goteborg.se/Wsdl/GBCA003A_LeveransAvisering_https_.wsdl', 0);
insert into system_property (id, string_value, number_value) values ('ws-endpoint-order-comment', 'http://esbat.goteborg.se/Wsdl/GBCA002A_LeveransStatus_https_.wsdl', 0);
insert into system_property (id, string_value, number_value) values ('ws-username-gbca', 'user', 0);
insert into system_property (id, string_value, number_value) values ('ws-password-gbca', 'password', 0);
insert into system_property (id, string_value, number_value) values ('order-correction-mail-sender','lim.lanteam@gmail.com',0);
insert into system_property (id, string_value, number_value) values ('order-correction-mail-receiver','tornqvistb@gmail.com',0);
commit;

commit;
