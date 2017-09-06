SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE customer_group MODIFY COLUMN id BIGINT(20) auto_increment;
ALTER TABLE customer_custom_field MODIFY COLUMN id BIGINT(20) auto_increment;
ALTER TABLE registration_config MODIFY COLUMN id BIGINT(20) auto_increment;
ALTER TABLE reports_config MODIFY COLUMN id BIGINT(20) auto_increment;

SET FOREIGN_KEY_CHECKS=1;