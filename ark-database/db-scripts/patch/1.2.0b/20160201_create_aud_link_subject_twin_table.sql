use audit;

CREATE TABLE `aud_link_subject_twin` (
  `ID` bigint(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `FIRST_SUBJECT` bigint(11) NOT NULL,
  `SECOND_SUBJECT` bigint(11) NOT NULL,
  `TWIN_TYPE_ID` bigint(11) NOT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `fk_aud_link_subject_twin` (`REV`),
  CONSTRAINT `fk_aud_link_subject_twin` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
