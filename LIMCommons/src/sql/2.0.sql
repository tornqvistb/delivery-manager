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

insert into customer_group (id, name, email_address, creation_date) values (1, 'Göteborgs Stad', 'tornqvistb@gmail.com', sysdate());
insert into customer_group (id, name, email_address, creation_date) values (2, 'Partille Kommun', 'tornqvistb@gmail.com',sysdate());

insert into registration_config (id, customer_group_id) values (1,1);
insert into registration_config (id, customer_group_id) values (2,2);

insert into reports_config (id, customer_group_id) values (1,1);
insert into reports_config (id, customer_group_id) values (2,2);

update order_header set customer_group_id = 1 where id > 1;

insert into custom_field (identification, label, creation_date) values (1, 'OU/image', sysdate());
insert into custom_field (identification, label, creation_date) values (2, 'Extra program', sysdate());
insert into custom_field (identification, label, creation_date) values (3, 'Ekonomisk leveransadress', sysdate());
insert into custom_field (identification, label, creation_date) values (4, 'Telefonnummer', sysdate());
insert into custom_field (identification, label, creation_date) values (5, 'Mailadress', sysdate());
insert into custom_field (identification, label, creation_date) values (6, 'Förvalting', sysdate());
insert into custom_field (identification, label, creation_date) values (7, 'Användare i klartext', sysdate());
insert into custom_field (identification, label, creation_date) values (8, 'Gammalt datornamn', sysdate());
insert into custom_field (identification, label, creation_date) values (9, 'Godsmärkning', sysdate());
insert into custom_field (identification, label, creation_date) values (10, 'Övrigt 2', sysdate());

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
