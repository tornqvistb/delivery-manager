insert into system_property (id, string_value, number_value) values ('default-company-group-id', 'Standardvärde för id på aktiv kundgrupp vid start av LIM.', 1);
insert into system_property (id, string_value, number_value) values ('pdf-images-folder', 'D:/lim/resources/', 1);
insert into system_property (id, string_value, number_value) values ('work-orders-folder', 'D:/lim/pdf/work-orders/', 1);
insert into system_property (id, string_value, number_value) values ('delivery-notes-folder', 'D:/lim/pdf/delivery-notes/', 1);
insert into system_property (id, string_value, number_value) values ('joint-invoicing-cust-numbers', '0450', 0);
insert into system_property (id, string_value, number_value) values ('mail-reply-to-address', 'avisering@lanteam.se', 0);
insert into system_property (id, string_value, number_value) values ('customer-group-intraservice', 'Intraservice', 0);
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

insert into customer_group (id, name, email_address, creation_date) values (1, 'Intraservice', 'tornqvistb@gmail.com', sysdate());
insert into customer_group (id, name, email_address, creation_date) values (2, 'Partille', 'tornqvistb@gmail.com',sysdate());
insert into customer_group (id, name, email_address, creation_date) values (3, 'Partille Kommun', 'tornqvistb@gmail.com',sysdate());

insert into registration_config (id, customer_group_id) values (1,1);
insert into registration_config (id, customer_group_id) values (2,2);
insert into registration_config (id, customer_group_id) values (3,3);


insert into reports_config (id, customer_group_id) values (1,1);
insert into reports_config (id, customer_group_id) values (2,2);
insert into reports_config (id, customer_group_id) values (3,3);


update order_header set customer_group_id = 1 where id > 1;

insert into custom_field (identification, label, creation_date) values (6, 'OU/image', sysdate());
insert into custom_field (identification, label, creation_date) values (7, 'Extra program', sysdate());
insert into custom_field (identification, label, creation_date) values (4, 'Ekonomisk leveransadress', sysdate());
insert into custom_field (identification, label, creation_date) values (3, 'Telefonnummer', sysdate());
insert into custom_field (identification, label, creation_date) values (2, 'Mailadress', sysdate());
insert into custom_field (identification, label, creation_date) values (5, 'Förvalting', sysdate());
insert into custom_field (identification, label, creation_date) values (1, 'Användare i klartext', sysdate());
insert into custom_field (identification, label, creation_date) values (8, 'Gammalt datornamn', sysdate());
insert into custom_field (identification, label, creation_date) values (9, 'Samleverans', sysdate());
insert into custom_field (identification, label, creation_date) values (10, 'Övrigt', sysdate());

-- Update article ids field on order_header
CREATE PROCEDURE `update_articles_on_order_header`()
BLOCK1: BEGIN
    
    DECLARE orderId INT(11);
    DECLARE articleNumbers VARCHAR(1000);
    DECLARE done INTEGER DEFAULT 0;
    DECLARE oh_cursor CURSOR FOR SELECT id FROM lanteam.order_header;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    SET done = 0;
    OPEN oh_cursor;
    orderLoop: LOOP
		FETCH oh_cursor INTO orderId;
        IF done = 1 THEN LEAVE orderLoop; END IF;
        BLOCK2: BEGIN
			DECLARE done2 INTEGER DEFAULT 0;
			DECLARE articleNumber VARCHAR(100);
            DECLARE ol_cursor CURSOR FOR SELECT article_number FROM lanteam.order_line WHERE order_header_id = orderId;
            DECLARE CONTINUE HANDLER FOR NOT FOUND SET done2 = 1;
			OPEN ol_cursor;
			SET articleNumbers := '';
			orderLineLoop: LOOP
				FETCH ol_cursor INTO articleNumber;
                IF done2 = 1 THEN LEAVE orderLineLoop; END IF;
				SET articleNumbers := CONCAT(articleNumbers, ';', articleNumber);        	
			END LOOP orderLineLoop;
        END BLOCK2;
        UPDATE order_header SET article_numbers = articleNumbers where id = orderId;
	END LOOP orderLoop;
    COMMIT;

END BLOCK1

insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (1, 1, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (2, 2, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (3, 3, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (4, 4, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (5, 5, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (6, 6, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (7, 7, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (8, 8, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (9, 9, 1, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (10, 10, 1, 0, 0, 0, 0, sysdate());

insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (11, 1, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (12, 2, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (13, 3, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (14, 4, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (15, 5, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (16, 6, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (17, 7, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (18, 8, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (19, 9, 2, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (20, 10, 2, 0, 0, 0, 0, sysdate());

insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (21, 1, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (22, 2, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (23, 3, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (24, 4, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (25, 5, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (26, 6, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (27, 7, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (28, 8, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (29, 9, 3, 0, 0, 0, 0, sysdate());
insert into customer_custom_field (id, custom_field_identification, customer_group_id, show_in_delivery_note, show_in_delivery_report, show_in_sla_report, show_in_work_note, creation_date) values (30, 10, 3, 0, 0, 0, 0, sysdate());


commit;

--Archive DB
insert into system_property (id, string_value, number_value) values ('archiverjob-frequency','',300);
commit;

CREATE DEFINER=`root`@`localhost` PROCEDURE `copy_to_archive_db`(OUT result varchar(100))
BEGIN
    
    DECLARE orderId INT(11);
    DECLARE done INTEGER DEFAULT 0;
    DECLARE oh_cursor CURSOR FOR SELECT id FROM lanteam.order_header WHERE to_be_archived = 1;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    SET done = 0;
    OPEN oh_cursor;
    orderLoop: LOOP
		FETCH oh_cursor INTO orderId;
        IF done = 1 THEN LEAVE orderLoop; END IF;
		INSERT INTO lim_archive.order_header (SELECT * FROM lanteam.order_header WHERE id = orderId);    
		INSERT INTO lim_archive.order_line (SELECT * FROM lanteam.order_line WHERE order_header_id = orderId);
		INSERT INTO lim_archive.attachment (SELECT * FROM lanteam.attachment WHERE order_header_id = orderId);
		INSERT INTO lim_archive.order_comment (SELECT * FROM lanteam.order_comment WHERE order_header_id = orderId);
		INSERT INTO lim_archive.equipment (SELECT * FROM lanteam.equipment WHERE order_line_id IN (SELECT id FROM lanteam.order_line WHERE order_header_id = orderId));
		
		DELETE FROM lanteam.equipment WHERE order_line_id IN (SELECT id FROM lanteam.order_line WHERE order_header_id = orderId);
		DELETE FROM lanteam.order_line WHERE order_header_id = orderId;
		DELETE FROM lanteam.attachment WHERE order_header_id = orderId;
		DELETE FROM lanteam.order_comment WHERE order_header_id = orderId;
		DELETE FROM lanteam.order_header WHERE id = orderId;
	END LOOP orderLoop;
    COMMIT;
    SET result = 'Det gick bra';

END
