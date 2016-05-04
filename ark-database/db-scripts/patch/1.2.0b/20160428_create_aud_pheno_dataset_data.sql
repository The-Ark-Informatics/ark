USE `audit`;
DROP TABLE IF EXISTS `aud_pheno_dataset_data`;
CREATE TABLE `aud_pheno_dataset_data` (
   `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `PHENO_DATASET_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  `PHENO_DATASET_COLLECTION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_aud_pheno_dataset_data` (`REV`),
  CONSTRAINT `FK_CONSTRAIN_aud_pheno_dataset_data` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

