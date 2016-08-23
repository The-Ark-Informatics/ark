# Dump of table aud_custom_field_category
# ------------------------------------------------------------
use audit;

DROP TABLE IF EXISTS `aud_pheno_dataset_category`;
CREATE TABLE `aud_pheno_dataset_category` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `STUDY_ID` bigint(20) NOT NULL,
  `ARK_FUNCTION_ID` bigint(20) NOT NULL,
  `PARENT_ID` bigint(20) NULL,
  `ORDER_NUMBER` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_AUD_PHENO_DATASET_CATEGORY` (`REV`),
  CONSTRAINT `FK_AUD_PHENO_DATASET_CATEGORY` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

