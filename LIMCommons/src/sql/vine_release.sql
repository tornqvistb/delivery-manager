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

--TODO
--update all custom_field, one step ahead CH uppdatera customercustomfield;
-- update customer_custom_field
update customer_custom_field set custom_field_identification = custom_field_identification -1;
