use study;

DROP TABLE IF EXISTS `correspondence`;
CREATE TABLE `correspondence` (
  `ID` int(11) NOT NULL auto_increment,
  `DATE_OF_CORRESPONDENCE` date default NULL,
  `SUMMARY` varchar(255) default NULL,
  `CORRESPONDENCE_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`CORRESPONDENCE_TYPE_ID`),
  KEY `CORRESPONDENCE_CO_TYPE_FK` USING BTREE (`CORRESPONDENCE_TYPE_ID`),
  CONSTRAINT `correspondence_ibfk_1` FOREIGN KEY (`CORRESPONDENCE_TYPE_ID`) REFERENCES `correspondence_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`CORRESPONDENCE_TYPE_ID`) REFER `stud';

DROP TABLE IF EXISTS `correspondence_audit`;
CREATE TABLE `correspondence_audit` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) default NULL,
  `AUDIT_DATE` date NOT NULL,
  `AUDIT_TIME` varchar(255) NOT NULL,
  `STATUS_TYPE_ID` int(11) default NULL,
  `STUDY_MANAGER` varchar(255) default NULL,
  `CORRESPONDENCE_DATE` date default NULL,
  `CORRESPONDENCE_TIME` varchar(255) default NULL,
  `REASON` varchar(4096) default NULL,
  `MODE_TYPE_ID` int(11) default NULL,
  `DIRECTION_TYPE_ID` int(11) default NULL,
  `OUTCOME_TYPE_ID` int(11) default NULL,
  `DETAILS` varchar(4096) default NULL,
  `COMMENTS` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `corres` USING BTREE (`CORRESPONDENCE_ID`),
  KEY `correspondence_audit_correspondence_id` USING BTREE (`CORRESPONDENCE_ID`),
  KEY `correspondence_audit_status_type_id` USING BTREE (`STATUS_TYPE_ID`),
  KEY `correspondence_audit_mode_type_id` USING BTREE (`MODE_TYPE_ID`),
  KEY `correspondence_audit_direction_type_id` USING BTREE (`DIRECTION_TYPE_ID`),
  KEY `correspondence_audit_outcome_type_id` USING BTREE (`OUTCOME_TYPE_ID`),
  KEY `correspondence_audit_study_id` USING BTREE (`STUDY_ID`),
  CONSTRAINT `correspondence_audit_correspondence_id` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondences` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_status_type_id` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `correspondence_status_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_attachment`;
CREATE TABLE `correspondence_attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `SIZE` int(10) unsigned NOT NULL,
  `CONTENTS` blob NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `correspondence` USING BTREE (`CORRESPONDENCE_ID`),
  CONSTRAINT `correspondence_attachment_correspondence_id` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondences` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_audit_attachment`;
CREATE TABLE `correspondence_audit_attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_AUDIT_ID` int(11) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `SIZE` int(10) unsigned NOT NULL,
  `CONTENTS` blob NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `correspondence_audit_attachment_correspondence_audit_id` USING BTREE (`CORRESPONDENCE_AUDIT_ID`),
  CONSTRAINT `correspondence_audit_attachment_correspondence_audit_id` FOREIGN KEY (`CORRESPONDENCE_AUDIT_ID`) REFERENCES `correspondence_audit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_direction_type`;
CREATE TABLE `correspondence_direction_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_mod_type`;
CREATE TABLE `correspondence_mode_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_outcome_type`;
CREATE TABLE `correspondence_outcome_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(496) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_status_type`;
CREATE TABLE `correspondence_status_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `correspondence_type`;
CREATE TABLE `correspondence_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `TYPE_DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `correspondences`;
CREATE TABLE `correspondences` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `STATUS_TYPE_ID` int(11) default NULL,
  `STUDY_MANAGER` varchar(255) default NULL,
  `DATE` date default NULL,
  `TIME` varchar(255) default NULL,
  `REASON` varchar(4096) default NULL,
  `MODE_TYPE_ID` int(11) default NULL,
  `DIRECTION_TYPE_ID` int(11) default NULL,
  `OUTCOME_TYPE_ID` int(11) default NULL,
  `DETAILS` varchar(4096) default NULL,
  `COMMENTS` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `status_type` USING BTREE (`STATUS_TYPE_ID`),
  KEY `mode_type` USING BTREE (`MODE_TYPE_ID`),
  KEY `direction_type` USING BTREE (`DIRECTION_TYPE_ID`),
  KEY `outcome_type` USING BTREE (`OUTCOME_TYPE_ID`),
  KEY `correspondences_study_id` USING BTREE (`STUDY_ID`),
  CONSTRAINT `correspondences_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_status_type_id` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `correspondence_status_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
