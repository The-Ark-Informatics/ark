USE study;

CREATE TABLE `study`.`upload` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime default NULL,
  `UPLOAD_REPORT` longblob,
  PRIMARY KEY  (`ID`),
  KEY `fk_upload_file_format` USING BTREE (`FILE_FORMAT_ID`),
  KEY `fk_upload_delimiter` USING BTREE (`DELIMITER_TYPE_ID`),
  KEY `ID` USING BTREE (`ID`),
  KEY `fk_upload_study` USING BTREE (`STUDY_ID`),
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`)';


ALTER TABLE `study`.`upload` DROP COLUMN `INSERT_TIME` ;
