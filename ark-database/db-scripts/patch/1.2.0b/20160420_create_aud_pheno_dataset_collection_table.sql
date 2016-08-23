USE `audit`;
DROP TABLE IF EXISTS `aud_pheno_dataset_collection`;
CREATE TABLE `aud_pheno_dataset_collection` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  `PHENO_DATASET_FIELD_GROUP_ID` bigint(20) DEFAULT NULL,
  `QUESTIONNAIRE_STATUS_ID` bigint(20) DEFAULT NULL,
  `REVIEWED_BY_ID` bigint(20) DEFAULT NULL,
  `RECORD_DATE` date DEFAULT NULL,
  `REVIEWED_DATE` date DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_aud_pheno_dataset_collection` (`REV`),
  CONSTRAINT `FK_Constrian_aud_aud_pheno_dataset_collection` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



