update system_property set string_value = '//ltvisma1/vine/Produktion/LIM/Import_Salesorder' where id = 'file-incoming-shop-folder';
update system_property set string_value = '//ltvisma1/vine/Produktion/LIM/Import_Salesorder/Arkiv' where id = 'file-processed-shop-folder';
update system_property set string_value = '//ltvisma1/vine/Produktion/LIM/Import_Salesorder/Ej_importerad' where id = 'file-error-shop-folder';

update system_property set string_value = '//ltvisma1/vine/Produktion/LIM/Import_Lexitorder' where id = 'file-incoming-wh-folder';
update system_property set string_value = '//ltvisma1/vine/Produktion/LIM/Import_Lexitorder/Arkiv' where id = 'file-processed-wh-folder';
update system_property set string_value = '//ltvisma1/vine/Produktion/Lexit/Import_LimOrder' where id = 'file-outgoing-wh-folder';
update system_property set string_value = '//ltvisma1/vine/Produktion/LIM/Import_Lexitorder/Ej_importerad' where id = 'file-error-wh-folder';

delete from system_property where id = 'file-error-wh-folder';
delete from system_property where id = 'file-incoming-folder';
delete from system_property where id = 'file-outgoing-folder';
delete from system_property where id = 'file-processed-folder';
delete from system_property where id = 'file-outgoing-shop-folder';
delete from system_property where id = 'netset-files-basedir';