insert into system_property (id, string_value, number_value) values ('default-company-group-id', 'Standardvärde för id på aktiv kundgrupp vid start av LIM.', 1);
insert into system_property (id, string_value, number_value) values ('pdf-images-folder', 'D:/lim/resources/', 1);
insert into system_property (id, string_value, number_value) values ('work-orders-folder', 'D:/lim/pdf/work-orders/', 1);
insert into system_property (id, string_value, number_value) values ('delivery-notes-folder', 'D:/lim/pdf/delivery-notes/', 1);
commit;

insert into delivery_week_day (id, name, sorting, creation_date) values (1, 'Måndag', 1, sysdate());
insert into delivery_week_day (id, name, sorting, creation_date) values (2, 'Tisdag', 2, sysdate());
insert into delivery_week_day (id, name, sorting, creation_date) values (3, 'Onsdag', 3, sysdate());
insert into delivery_week_day (id, name, sorting, creation_date) values (4, 'Torsdag', 4, sysdate());
insert into delivery_week_day (id, name, sorting, creation_date) values (5, 'Fredag', 5, sysdate());

insert into delivery_area (id, name, creation_date) values (1, 'Norr', sysdate());
insert into delivery_area (id, name, creation_date) values (2, 'Söder', sysdate());
insert into delivery_area (id, name, creation_date) values (3, 'Väster', sysdate());
insert into delivery_area (id, name, creation_date) values (4, 'Öster', sysdate());
insert into delivery_area (id, name, creation_date) values (5, 'Hisingen', sysdate());
insert into delivery_area (id, name, creation_date) values (6, 'Marks kommun', sysdate());
insert into delivery_area (id, name, creation_date) values (7, 'Partille', sysdate());

insert into customer_group (id, name, email_address, creation_date) values (1, 'Göteborgs Stad', sysdate());
insert into customer_group (id, name, email_address, creation_date) values (2, 'Partille Kommun', sysdate());

insert into customer_group (id, name, email_address, creation_date) values (1, 'Göteborgs Stad', 'tornqvistb@gmail.com', sysdate());
insert into customer_group (id, name, email_address, creation_date) values (2, 'Partille Kommun', 'tornqvistb@gmail.com',sysdate());

insert into registration_config (id, customer_group_id) values (1,1);
insert into registration_config (id, customer_group_id) values (2,2);

insert into reports_config (id, customer_group_id) values (1,1);
insert into reports_config (id, customer_group_id) values (2,2);

update order_header set customer_group_id = 1 where id > 1;

commit;

--Archive DB
insert into system_property (id, string_value, number_value) values ('archiverjob-frequency','',300);
commit;
