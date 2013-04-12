USE reporting;
delimiter $$

DROP TABLE IF EXISTS `search_subject`;

CREATE TABLE `search_subject` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEARCH_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_search_subject_1_idx` (`SEARCH_ID`),
  KEY `fk_search_subject_2` (`LINK_SUBJECT_STUDY_ID`),
  CONSTRAINT `fk_search_subject_1` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_search_subject_2` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1$$