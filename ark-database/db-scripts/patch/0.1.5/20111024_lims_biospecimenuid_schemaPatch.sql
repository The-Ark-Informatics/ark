USE LIMS;
CREATE TABLE `biospecimenuid_padchar` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `biospecimenuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `biospecimenuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `lims`.`biospecimenuid_padchar`
SELECT * FROM `study`.`subjectuid_padchar`;

INSERT INTO `lims`.`biospecimenuid_token`
SELECT * FROM `study`.`subjectuid_token`;
