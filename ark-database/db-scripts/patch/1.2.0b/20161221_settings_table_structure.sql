CREATE TABLE `config`.`settings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL DEFAULT '',
  `highest_type` varchar(255) NOT NULL DEFAULT '',
  `propertyType` varchar(255) NOT NULL,
  `property_name` text NOT NULL,
  `property_value` text NOT NULL,
  `study_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `audit`.`aud_settings` (
  `id` bigint(29) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `type` varchar(255) NOT NULL DEFAULT '',
  `highest_type` varchar(255) NOT NULL DEFAULT '',
  `propertyType` varchar(255) NOT NULL,
  `property_name` text NOT NULL,
  `property_value` text NOT NULL,
  `study_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `aud_settings_rev` (`REV`),
  CONSTRAINT `aud_settings_rev` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
