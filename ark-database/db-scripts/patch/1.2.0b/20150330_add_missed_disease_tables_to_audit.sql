# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 5.6.21)
# Database: audit
# Generation Time: 2015-03-30 05:05:19 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table aud_affection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_affection`;

CREATE TABLE `aud_affection` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `RECORD_DATE` date DEFAULT NULL,
  `AFFECTION_STATUS_ID` bigint(20) DEFAULT NULL,
  `DISEASE_ID` bigint(20) DEFAULT NULL,
  `LINKSUBJECTSTUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK4EBD81C4F03BED18` (`REV`),
  CONSTRAINT `FK4EBD81C4F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_affection_custom_field_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_affection_custom_field_data`;

CREATE TABLE `aud_affection_custom_field_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `AFFECTION_ID` bigint(20) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKEAE5E7C2F03BED18` (`REV`),
  CONSTRAINT `FKEAE5E7C2F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_affection_position
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_affection_position`;

CREATE TABLE `aud_affection_position` (
  `REV` int(11) NOT NULL,
  `AFFECTION_ID` bigint(20) NOT NULL,
  `POSITION_ID` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`AFFECTION_ID`,`POSITION_ID`),
  KEY `FK6F4A3924F03BED18` (`REV`),
  CONSTRAINT `FK6F4A3924F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
