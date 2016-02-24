ALTER TABLE attachment CHANGE file_content file_content LONGBLOB default NULL;
ALTER TABLE email CHANGE content content VARCHAR(10000) default NULL;
ALTER TABLE error_record CHANGE message message VARCHAR(10000) default NULL;

insert into system_property (id, string_value, number_value) values ('mailjob-frequency', '', 30000);
insert into system_property (id, string_value, number_value) values ('order-transmitjob-frequency', '', 30000);
insert into system_property (id, string_value, number_value) values ('orderjob-frequency', '', 30000);
insert into system_property (id, string_value, number_value) values ('mail-host', 'pop.gmail.com', 0);
insert into system_property (id, string_value, number_value) values ('mail-username', 'lim.lanteam@gmail.com', 0);
insert into system_property (id, string_value, number_value) values ('mail-password', 'limlanteam', 0);
insert into system_property (id, string_value, number_value) values ('mail-smtps-host', 'smtp.gmail.com', 0);
insert into system_property (id, string_value, number_value) values ('file-image-folder', 'D:/lim/filedirs/images', 0);
insert into system_property (id, string_value, number_value) values ('file-incoming-folder', 'D:/lim/filedirs/incoming', 0);
insert into system_property (id, string_value, number_value) values ('file-processed-folder', 'D:/lim/filedirs/processed', 0);
insert into system_property (id, string_value, number_value) values ('file-error-folder', 'D:/lim/filedirs/error', 0);
insert into system_property (id, string_value, number_value) values ('file-outgoing-folder', 'D:/lim/filedirs/outgoing', 0);
insert into system_property (id, string_value, number_value) values ('ws-endpoint-order-delivery', 'http://esb.goteborg.se/Wsdl/GBCA003A_LeveransAvisering_https_.wsdl', 0);
insert into system_property (id, string_value, number_value) values ('ws-endpoint-order-comment', 'http://esb.goteborg.se/Wsdl/GBCA002A_LeveransStatus_https_.wsdl', 0);
insert into system_property (id, string_value, number_value) values ('ws-username-gbca', 'GBCALanTeamProd', 0);
insert into system_property (id, string_value, number_value) values ('ws-password-gbca', 'alsdKf21', 0);
insert into system_property (id, string_value, number_value) values ('order-correction-mail-sender','lim.lanteam@gmail.com',0);
insert into system_property (id, string_value, number_value) values ('order-correction-mail-receiver','ake.holmquist@intraservice.goteborg.se',0);

//UPDATE DATABASECHANGELOGLOCK SET LOCKED=FALSE, LOCKGRANTED=null, LOCKEDBY=null where ID=1;

commit;
