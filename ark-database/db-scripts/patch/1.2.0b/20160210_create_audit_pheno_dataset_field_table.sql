USE audit;
--
-- Table structure for table `aud_pheno_dataset_field`
--
DROP TABLE IF EXISTS `aud_pheno_dataset_field`;
CREATE TABLE `aud_pheno_dataset_field`(
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `description` text,
  `FIELD_TYPE_ID` bigint(20) NOT NULL,
  `STUDY_ID` bigint(20) NOT NULL,
  `ARK_FUNCTION_ID` bigint(20) NOT NULL,
  `UNIT_TYPE_ID` bigint(20),
  `MIN_VALUE` varchar(45),
  `MAX_VALUE` varchar(45),
  `ENCODED_VALUES` text,
  `MISSING_VALUE` varchar(45),
  `HAS_DATA` TINYINT(4) NOT NULL,
  `PHENO_FIELD_LABEL` varchar(255) NULL,
  `DEFAULT_VALUE` text,
  `UNIT_TYPE_IN_TEXT` varchar(20) NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_AUD_PHENO_DATASET_FIELD` (`REV`),
  CONSTRAINT `FK_AUD_PHENO_DATASET_FIELD` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1066 DEFAULT CHARSET=latin1;

