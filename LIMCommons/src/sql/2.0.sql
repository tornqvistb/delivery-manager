insert into system_property (id, string_value, number_value) values ('default-company-group-id', 'Standardvärde för id på aktiv kundgrupp vid start av LIM.', 1);
insert into system_property (id, string_value, number_value) values ('pdf-images-folder', '/Projekt/lanteam/lim/resources/', 1);
insert into system_property (id, string_value, number_value) values ('work-orders-folder', '/Projekt/lanteam/lim/pdf/work-orders/', 1);
insert into system_property (id, string_value, number_value) values ('delivery-notes-folder', '/Projekt/lanteam/lim/pdf/delivery-notes/', 1);
commit;

--Archive DB
insert into system_property (id, string_value, number_value) values ('archiverjob-frequency','',300);
commit;
