ALTER TABLE `lanteam`.`order_header` CHANGE COLUMN `delivery_signature` `delivery_signature` BLOB NULL DEFAULT NULL ;

ALTER TABLE order_line CHANGE article_description article_description VARCHAR(10000) default NULL;

insert into system_property (id, string_value, number_value) values ('file-incoming-wh-folder', '//ltvisma1/vine/Test/LIM', 0);
insert into system_property (id, string_value, number_value) values ('file-processed-wh-folder', 'C:/Projekt/lim/filedirs/processed_wh', 0);
insert into system_property (id, string_value, number_value) values ('file-error-wh-folder', 'C:/Projekt/lim/filedirs/error_wh', 0);
insert into system_property (id, string_value, number_value) values ('file-outgoing-wh-folder', '//ltvisma1/vine/Test/Lexit', 0);

insert into system_property (id, string_value, number_value) values ('file-incoming-shop-folder', '//ltvisma1/vine/Test/LIM', 0);
insert into system_property (id, string_value, number_value) values ('file-processed-shop-folder', 'C:/Projekt/lim/filedirs/processed_shop', 0);
insert into system_property (id, string_value, number_value) values ('file-error-shop-folder', 'C:/Projekt/lim/filedirs/error_shop', 0);
insert into system_property (id, string_value, number_value) values ('file-outgoing-shop-folder', '//ltvisma1/vine/Test/Netset', 0);

insert into custom_field (identification, label) values (0, 'Användare i klartext');
update custom_field set label = 'Mailadress' where identification = 1;
update custom_field set label = 'Telefonnummer' where identification = 2;
update custom_field set label = 'Ekonomisk leveransadress' where identification = 3;
update custom_field set label = 'Förvalting' where identification = 4;
update custom_field set label = 'OU/image' where identification = 5;
update custom_field set label = 'Extra program' where identification = 6;
update custom_field set label = 'Gammalt datornamn' where identification = 7;
update custom_field set label = 'Samleverans' where identification = 8;
update custom_field set label = 'Övrigt' where identification = 9;

update customer_custom_field set custom_field_identification = custom_field_identification -1;

-- update corresponding on all orders.
