CREATE TABLE `settings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL DEFAULT '',
  `highest_type` varchar(255) NOT NULL DEFAULT '',
  `propertyType` varchar(255) NOT NULL,
  `property_name` text NOT NULL,
  `property_value` text NOT NULL,
  `study_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
