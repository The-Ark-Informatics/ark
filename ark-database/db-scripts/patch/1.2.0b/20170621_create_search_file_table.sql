USE `reporting`;
DROP TABLE IF EXISTS `search_file`;
CREATE TABLE `search_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT ,
  `STUDY_ID` int(11) NOT NULL,
  `SEARCH_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `CHECKSUM` varchar(50)  NOT NULL,
  `USER_ID` varchar(100)  NOT NULL,
  `COMMENTS` varchar(45)  DEFAULT NULL,
  `FILE_ID` varchar(1000)  DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_search_file_study_id_idx` (`STUDY_ID`),
  KEY `fk_search_file_search_id_idx` (`SEARCH_ID`),
  CONSTRAINT `fk_search_file_search_id` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_search_file_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


USE `audit`;
DROP TABLE IF EXISTS `aud_search_file`;
CREATE TABLE `aud_search_file` (
   `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `STUDY_ID`  bigint(20) DEFAULT NULL,
  `SEARCH_ID`  bigint(20) DEFAULT NULL,
  `FILENAME` text NOT NULL,
  `CHECKSUM` varchar(50)  NOT NULL,
  `USER_ID` varchar(100)  NOT NULL,
  `COMMENTS` varchar(45)  DEFAULT NULL,
  `FILE_ID` varchar(1000)  DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_search_file` (`REV`),
  CONSTRAINT `FK_CONSTRAIN_search_file` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
