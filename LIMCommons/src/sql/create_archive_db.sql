CREATE DATABASE `lim_archive` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE TABLE `order_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `contact1email` varchar(255) DEFAULT NULL,
  `contact1name` varchar(255) DEFAULT NULL,
  `contact1phone` varchar(255) DEFAULT NULL,
  `contact2email` varchar(255) DEFAULT NULL,
  `contact2name` varchar(255) DEFAULT NULL,
  `contact2phone` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_number` varchar(255) DEFAULT NULL,
  `customer_order_number` varchar(255) DEFAULT NULL,
  `customer_sales_order` varchar(255) DEFAULT NULL,
  `delivery_city` varchar(255) DEFAULT NULL,
  `delivery_postal_address1` varchar(255) DEFAULT NULL,
  `delivery_postal_address2` varchar(255) DEFAULT NULL,
  `delivery_postal_code` varchar(255) DEFAULT NULL,
  `leasing_number` varchar(255) DEFAULT NULL,
  `order_date` datetime DEFAULT NULL,
  `order_number` varchar(255) DEFAULT NULL,
  `partner_id` varchar(255) DEFAULT NULL,
  `postal_address1` varchar(255) DEFAULT NULL,
  `postal_address2` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transmit_error_message` varchar(255) DEFAULT NULL,
  `delivery_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1434 DEFAULT CHARSET=utf8;

CREATE TABLE `order_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_description` varchar(255) DEFAULT NULL,
  `article_number` varchar(255) DEFAULT NULL,
  `registered` int(11) DEFAULT NULL,
  `remaining` int(11) DEFAULT NULL,
  `restriction_code` varchar(255) DEFAULT NULL,
  `row_number` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_motmrqscchlpoxf1gbsypg0ku` (`order_header_id`),
  CONSTRAINT `FK_motmrqscchlpoxf1gbsypg0ku` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2627 DEFAULT CHARSET=utf8;

CREATE TABLE `equipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `previous_serial_no` varchar(255) DEFAULT NULL,
  `previous_stealing_tag` varchar(255) DEFAULT NULL,
  `serial_no` varchar(255) DEFAULT NULL,
  `stealing_tag` varchar(255) DEFAULT NULL,
  `to_correct` bit(1) NOT NULL,
  `order_line_id` bigint(20) DEFAULT NULL,
  `registered_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2rr7n2y7g4pb4mrqjvw3q4kvt` (`order_line_id`),
  CONSTRAINT `FK_2rr7n2y7g4pb4mrqjvw3q4kvt` FOREIGN KEY (`order_line_id`) REFERENCES `order_line` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_content` longblob,
  `file_name` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ptd1ndxwj5ue5q4pe0p5vt081` (`order_header_id`),
  CONSTRAINT `FK_ptd1ndxwj5ue5q4pe0p5vt081` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1183 DEFAULT CHARSET=utf8;

CREATE TABLE `order_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `order_line` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_q3x3xre32o6v1c3sfwimf61lv` (`order_header_id`),
  CONSTRAINT `FK_q3x3xre32o6v1c3sfwimf61lv` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1435 DEFAULT CHARSET=utf8;



