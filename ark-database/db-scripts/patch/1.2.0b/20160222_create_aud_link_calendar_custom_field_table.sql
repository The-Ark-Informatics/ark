use audit;

CREATE TABLE `aud_link_calendar_custom_field` (
  `ID` bigint(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` TINYINT(4) NOT NULL,	
  `CALENDAR_ID` bigint(11) NOT NULL,
  `CUSTOM_FIELD_ID` bigint(11) NOT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `fk_aud_link_subject_twin` (`REV`),
  CONSTRAINT `fk_aud_link_calendar_custom_field` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
