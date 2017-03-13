CREATE TABLE `config`.`settings_validator` (
	  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
	  `setting_id` int(11) NOT NULL,
	  `value` text,
	  `validation_type` varchar(255) NOT NULL DEFAULT '',
	  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
