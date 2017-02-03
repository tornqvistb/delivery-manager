insert into system_property (id, string_value, number_value) values ('default-company-group-id', 'Standardvärde för id på aktiv kundgrupp vid start av LIM.', 1);
insert into system_property (id, string_value, number_value) values ('pdf-images-folder', '/Projekt/lanteam/lim/resources/', 1);
insert into system_property (id, string_value, number_value) values ('work-orders-folder', '/Projekt/lanteam/lim/pdf/work-orders/', 1);
insert into system_property (id, string_value, number_value) values ('delivery-notes-folder', '/Projekt/lanteam/lim/pdf/delivery-notes/', 1);
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

commit;

--Archive DB
insert into system_property (id, string_value, number_value) values ('archiverjob-frequency','',300);
commit;
