# Dump of table aud_custom_field_category
# ------------------------------------------------------------
use audit;

DROP TABLE IF EXISTS `aud_custom_field_category`;
CREATE TABLE `aud_custom_field_category` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `STUDY_ID` bigint(20) NOT NULL,
  `ARK_FUNCTION_ID` bigint(20) NOT NULL,
  `PARENT_ID` bigint(20) NOT NULL,
  `ORDER_NUMBER` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_AUD_CUSFIELD_CATEGORY` (`REV`),
  CONSTRAINT `FK_AUD_CUSFIELD_CATEGORY` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

