USE `audit`;
DROP TABLE IF EXISTS `aud_pheno_dataset_field_group`;
CREATE TABLE `aud_pheno_dataset_field_group` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `NAME` varchar(100) DEFAULT NULL,
  `PUBLISHED` tinyint(1) DEFAULT NULL,
  `ARK_FUNCTION_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_aud_pheno_dataset_field_group` (`REV`),
  CONSTRAINT `FK_Constrian_aud_pheno_dataset_field_group` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `audit`.`aud_pheno_dataset_field_group` 
ADD COLUMN `ARK_USER_ID` BIGINT(20) NULL AFTER `STUDY_ID`;

