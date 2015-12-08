USE `pheno`;
--
-- Table structure for table `pheno_dataset_field`
--
DROP TABLE IF EXISTS `pheno_dataset_field`;
CREATE TABLE `pheno_dataset_field`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `description` text,
  `FIELD_TYPE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `UNIT_TYPE_ID` int(11) DEFAULT NULL,
  `MIN_VALUE` varchar(45) DEFAULT NULL,
  `MAX_VALUE` varchar(45) DEFAULT NULL,
  `ENCODED_VALUES` text,
  `MISSING_VALUE` varchar(45) DEFAULT NULL,
  `HAS_DATA` TINYINT(4) NOT NULL DEFAULT 0,
  `PHENO_FIELD_LABEL` varchar(255) DEFAULT NULL,
  `DEFAULT_VALUE` text,
  `UNIT_TYPE_IN_TEXT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_UNIT_TYPE_ID` (`UNIT_TYPE_ID`),
  KEY `FK_PHENOFIELD_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  KEY `FK_PHENOFIELD_FIELD_TYPE_ID` (`FIELD_TYPE_ID`),
  CONSTRAINT `FK_PHENOFIELD_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENOFIELD_FIELD_TYPE_ID` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_FIELD_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PHENO_FIELD_UNIT_TYPE_ID` FOREIGN KEY (`UNIT_TYPE_ID`) REFERENCES `study`.`unit_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1066 DEFAULT CHARSET=latin1;

