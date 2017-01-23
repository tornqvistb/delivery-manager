insert into system_property (id, string_value, number_value) values ('default-company-group-id', 'Standardvärde för id på aktiv kundgrupp vid start av LIM.', 1);
commit;

--Archive DB
insert into system_property (id, string_value, number_value) values ('archiverjob-frequency','',300);
commit;
