USE `audit`;
--
-- Table structure for table `aud_pheno_dataset_field_display`
--
DROP TABLE IF EXISTS `aud_pheno_dataset_field_display`;
CREATE TABLE `aud_pheno_dataset_field_display` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `PHENO_DATASET_FIELD_ID` bigint(20) NOT NULL,
  `PHENO_DATASET_FIELD_GROUP_ID` bigint(20) DEFAULT NULL,
  `SEQUENCE` bigint(20) DEFAULT NULL,
  `REQUIRED` bigint(20) DEFAULT NULL,
  `REQUIRED_MESSAGE` varchar(45) DEFAULT NULL,
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`ID`,`REV`),
   KEY `FK_AUD_PHENO_DATASET_FIELD_DISPLAY` (`REV`),
   CONSTRAINT `FK_AUD_PHENO_DATASET_FIELD_DISPLAY` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1109 DEFAULT CHARSET=latin1;

