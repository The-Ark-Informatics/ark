USE STUDY;
DROP TABLE IF EXISTS  `correspondence_audit_attachment`;
CREATE TABLE `correspondence_audit_attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_AUDIT_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  PRIMARY KEY  (`ID`),
  KEY `correspondence_audit_attachment_correspondence_audit_id` USING BTREE (`CORRESPONDENCE_AUDIT_ID`),
  CONSTRAINT `correspondence_audit_attachment_correspondence_audit_id` FOREIGN KEY (`CORRESPONDENCE_AUDIT_ID`) REFERENCES `correspondence_audit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS  `correspondence_attachment`;
CREATE TABLE `correspondence_attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  PRIMARY KEY  (`ID`),
  KEY `correspondence` USING BTREE (`CORRESPONDENCE_ID`),
  CONSTRAINT `correspondence_attachment_correspondence_id` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondences` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;