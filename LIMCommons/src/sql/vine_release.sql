ALTER TABLE `lanteam`.`order_header` CHANGE COLUMN `delivery_signature` `delivery_signature` BLOB NULL DEFAULT NULL ;

insert into system_property (id, string_value, number_value) values ('file-incoming-wh-folder', 'C:/Projekt/lim/filedirs/incoming_wh', 0);
insert into system_property (id, string_value, number_value) values ('file-processed-wh-folder', 'C:/Projekt/lim/filedirs/processed_wh', 0);
insert into system_property (id, string_value, number_value) values ('file-error-wh-folder', 'C:/Projekt/lim/filedirs/error_wh', 0);
insert into system_property (id, string_value, number_value) values ('file-outgoing-wh-folder', 'C:/Projekt/lim/filedirs/outgoing_wh', 0);

insert into system_property (id, string_value, number_value) values ('file-incoming-shop-folder', 'C:/Projekt/lim/filedirs/incoming_shop', 0);
insert into system_property (id, string_value, number_value) values ('file-processed-shop-folder', 'C:/Projekt/lim/filedirs/processed_shop', 0);
insert into system_property (id, string_value, number_value) values ('file-error-shop-folder', 'C:/Projekt/lim/filedirs/error_shop', 0);
insert into system_property (id, string_value, number_value) values ('file-outgoing-shop-folder', 'C:/Projekt/lim/filedirs/outgoing_shop', 0);
