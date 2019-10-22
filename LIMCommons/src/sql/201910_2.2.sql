update system_property set string_value = 'outlook.office365.com' where id = 'mail-host';
update system_property set string_value = 'limkontoIop890!' where id = 'mail-password';
update system_property set string_value = 'm.outlook.com' where id = 'mail-smtps-host';
update system_property set string_value = 'lim@visolit.se' where id = 'mail-username';

insert into system_user (id,first_name,last_name,password,role,user_name) values (1,'Lim','User','lim2020','common-user','lim');
insert into system_user (id,first_name,last_name,password,role,user_name) values (2,'Limadm','Administrator','limadm2020','administrator','limadm');
