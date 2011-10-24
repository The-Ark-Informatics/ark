USE lims;

CREATE TABLE `biospecimenuid_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `BIOSPECIMENUID_TOKEN_ID` int(11) DEFAULT NULL,
  `BIOSPECIMENUID_PREFIX` varchar(45) NOT NULL,
  `BIOSPECIMENUID_PADCHAR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_study_study` (`STUDY_ID`),
  KEY `fk_study_biospecimenuid_padchar` (`BIOSPECIMENUID_PADCHAR_ID`),
  KEY `fk_study_biospecimenuid_token` (`BIOSPECIMENUID_TOKEN_ID`),
  CONSTRAINT `fk_study_biospecimenuid_padchar` FOREIGN KEY (`BIOSPECIMENUID_PADCHAR_ID`) REFERENCES `biospecimenuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_biospecimenuid_token` FOREIGN KEY (`BIOSPECIMENUID_TOKEN_ID`) REFERENCES `biospecimenuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Default biospecimen templates for each study
INSERT INTO `lims`.`biospecimenuid_template`
(`STUDY_ID`,
`BIOSPECIMENUID_TOKEN_ID`,
`BIOSPECIMENUID_PREFIX`,
`BIOSPECIMENUID_PADCHAR_ID`)
SELECT ID, NULL, SUBJECTUID_PREFIX, 10
FROM study.study;
