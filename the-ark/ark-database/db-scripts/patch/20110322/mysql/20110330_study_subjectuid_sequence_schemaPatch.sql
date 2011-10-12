-- For auto subject UID generator
USE study;
CREATE TABLE `subjectuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL default '0',
  `INSERT_LOCK` int(11) NOT NULL default '0',
  PRIMARY KEY  (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
