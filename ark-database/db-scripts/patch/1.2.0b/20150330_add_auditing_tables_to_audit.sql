# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 5.6.21)
# Database: audit
# Generation Time: 2015-03-30 03:53:28 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table aud_address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_address`;

CREATE TABLE `aud_address` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ADDRESS_LINE_1` varchar(255) DEFAULT NULL,
  `CITY` varchar(30) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `OTHER_STATE` varchar(45) DEFAULT NULL,
  `POST_CODE` varchar(10) DEFAULT NULL,
  `PREFERRED_MAILING_ADDRESS` tinyint(1) DEFAULT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `STREET_ADDRESS` varchar(255) DEFAULT NULL,
  `ADDRESS_STATUS_ID` bigint(20) DEFAULT NULL,
  `ADDRESS_TYPE_ID` bigint(20) DEFAULT NULL,
  `COUNTRY_ID` bigint(20) DEFAULT NULL,
  `STATE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKBF943D25F03BED18` (`REV`),
  CONSTRAINT `FKBF943D25F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_ark_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_ark_user_role`;

CREATE TABLE `aud_ark_user_role` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ARK_MODULE_ID` bigint(20) DEFAULT NULL,
  `ARK_ROLE_ID` bigint(20) DEFAULT NULL,
  `ARK_USER_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK18AA0136F03BED18` (`REV`),
  CONSTRAINT `FK18AA0136F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_barcode_label
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_barcode_label`;

CREATE TABLE `aud_barcode_label` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `BARCODE_PRINTER_NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `LABEL_PREFIX` varchar(50) DEFAULT NULL,
  `LABEL_SUFFIX` varchar(50) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK738F7F06F03BED18` (`REV`),
  CONSTRAINT `FK738F7F06F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_barcode_label_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_barcode_label_data`;

CREATE TABLE `aud_barcode_label_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `X_COORD` int(11) DEFAULT NULL,
  `Y_COORD` int(11) DEFAULT NULL,
  `COMMAND` varchar(10) DEFAULT NULL,
  `DATA` varchar(255) DEFAULT NULL,
  `LINE_FEED` varchar(255) DEFAULT NULL,
  `P1` varchar(255) DEFAULT NULL,
  `P2` varchar(255) DEFAULT NULL,
  `P3` varchar(255) DEFAULT NULL,
  `P4` varchar(255) DEFAULT NULL,
  `P5` varchar(255) DEFAULT NULL,
  `P6` varchar(255) DEFAULT NULL,
  `P7` varchar(255) DEFAULT NULL,
  `P8` varchar(255) DEFAULT NULL,
  `QUOTE_LEFT` varchar(255) DEFAULT NULL,
  `QUOTE_RIGHT` varchar(255) DEFAULT NULL,
  `BARCODE_LABEL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK34EE9D43F03BED18` (`REV`),
  CONSTRAINT `FK34EE9D43F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_bilable_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_bilable_item`;

CREATE TABLE `aud_bilable_item` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(255) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `COMMENCE_DATE` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `INVOICE` varchar(1) DEFAULT NULL,
  `ITEM_COST` double DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `TOTAL_COST` double DEFAULT NULL,
  `TOTAL_GST` double DEFAULT NULL,
  `TYPE` varchar(10) DEFAULT NULL,
  `BILLABLE_TYPE` bigint(20) DEFAULT NULL,
  `REQUEST_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK3120ACE2F03BED18` (`REV`),
  CONSTRAINT `FK3120ACE2F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_billable_item_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_billable_item_type`;

CREATE TABLE `aud_billable_item_type` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ITEM_NAME` varchar(45) DEFAULT NULL,
  `QUANTITY_PER_UNIT` int(11) DEFAULT NULL,
  `QUANTITY_TYPE` varchar(45) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `UNIT_PRICE` double DEFAULT NULL,
  `STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK93690217F03BED18` (`REV`),
  CONSTRAINT `FK93690217F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_bio_transaction
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_bio_transaction`;

CREATE TABLE `aud_bio_transaction` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `REASON` longtext,
  `RECORDER` varchar(255) DEFAULT NULL,
  `TRANSACTION_DATE` datetime DEFAULT NULL,
  `REQUEST_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_ID` bigint(20) DEFAULT NULL,
  `STATUS_ID` bigint(20) DEFAULT NULL,
  `UNIT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK6E469678F03BED18` (`REV`),
  CONSTRAINT `FK6E469678F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_biocollection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_biocollection`;

CREATE TABLE `aud_biocollection` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `BIOCOLLECTION_UID` varchar(255) DEFAULT NULL,
  `COLLECTIONDATE` datetime DEFAULT NULL,
  `COLLECTIONGROUP` varchar(50) DEFAULT NULL,
  `COLLECTIONGROUP_ID` int(11) DEFAULT NULL,
  `COMMENTS` longtext,
  `DELETED` int(11) DEFAULT NULL,
  `DIAG_CATEGORY` varchar(50) DEFAULT NULL,
  `DIAG_DATE` datetime DEFAULT NULL,
  `DISCHARGEDATE` datetime DEFAULT NULL,
  `EPISODE_DESC` varchar(50) DEFAULT NULL,
  `EPISODE_NUM` varchar(50) DEFAULT NULL,
  `HOSPITAL` varchar(50) DEFAULT NULL,
  `HOSPITAL_UR` varchar(50) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PATHLABNO` varchar(50) DEFAULT NULL,
  `PATIENTAGE` int(11) DEFAULT NULL,
  `REF_DOCTOR` varchar(50) DEFAULT NULL,
  `SURGERYDATE` datetime DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `TISSUECLASS` varchar(50) DEFAULT NULL,
  `TISSUETYPE` varchar(50) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKAD8A9637F03BED18` (`REV`),
  CONSTRAINT `FKAD8A9637F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_biocollection_custom_field_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_biocollection_custom_field_data`;

CREATE TABLE `aud_biocollection_custom_field_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `BIO_COLLECTION_ID` bigint(20) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK2A267D55F03BED18` (`REV`),
  CONSTRAINT `FK2A267D55F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_biospecimen
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_biospecimen`;

CREATE TABLE `aud_biospecimen` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `BARCODED` tinyint(1) DEFAULT NULL,
  `BIOSPECIMEN_UID` varchar(255) DEFAULT NULL,
  `COMMENTS` longtext,
  `CONCENTRATION` double DEFAULT NULL,
  `DELETED` tinyint(1) DEFAULT NULL,
  `DEPTH` bigint(20) DEFAULT NULL,
  `OLD_ID` bigint(20) DEFAULT NULL,
  `OTHERID` varchar(50) DEFAULT NULL,
  `PARENTID` varchar(50) DEFAULT NULL,
  `PROCESSED_DATE` datetime DEFAULT NULL,
  `PROCESSED_TIME` datetime DEFAULT NULL,
  `PURITY` double DEFAULT NULL,
  `QTY_COLLECTED` double DEFAULT NULL,
  `QTY_REMOVED` double DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `SAMPLE_DATE` datetime DEFAULT NULL,
  `SAMPLE_TIME` datetime DEFAULT NULL,
  `SUBSTUDY_ID` bigint(20) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_PROTOCOL_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_GRADE_ID` bigint(20) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_QUALITY_ID` bigint(20) DEFAULT NULL,
  `SAMPLETYPE_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_SPECIES_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_STATUS_ID` bigint(20) DEFAULT NULL,
  `BIOSPECIMEN_STORAGE_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `TREATMENT_TYPE_ID` bigint(20) DEFAULT NULL,
  `UNIT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK14AFCE1F03BED18` (`REV`),
  CONSTRAINT `FK14AFCE1F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_biospecimen_biospecimen
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_biospecimen_biospecimen`;

CREATE TABLE `aud_biospecimen_biospecimen` (
  `REV` int(11) NOT NULL,
  `PARENT_ID` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`PARENT_ID`,`id`),
  KEY `FKF2338972F03BED18` (`REV`),
  CONSTRAINT `FKF2338972F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_biospecimen_custom_field_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_biospecimen_custom_field_data`;

CREATE TABLE `aud_biospecimen_custom_field_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `BIOSPECIMEN_ID` bigint(20) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK83FE4D7FF03BED18` (`REV`),
  CONSTRAINT `FK83FE4D7FF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_biospecimen_species
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_biospecimen_species`;

CREATE TABLE `aud_biospecimen_species` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD98D73DEF03BED18` (`REV`),
  CONSTRAINT `FKD98D73DEF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_config_fields
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_config_fields`;

CREATE TABLE `aud_config_fields` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `TYPE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK3A05BEE7F03BED18` (`REV`),
  CONSTRAINT `FK3A05BEE7F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_consent
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_consent`;

CREATE TABLE `aud_consent` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENTED_BY` varchar(255) DEFAULT NULL,
  `RECEIVED_DATE` date DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED_ID` bigint(20) DEFAULT NULL,
  `CONSENT_STATUS_ID` bigint(20) DEFAULT NULL,
  `CONSENT_TYPE_ID` bigint(20) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_COMP_ID` bigint(20) DEFAULT NULL,
  `STUDY_COMP_STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK3CB3614BF03BED18` (`REV`),
  CONSTRAINT `FK3CB3614BF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_correspondences
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_correspondences`;

CREATE TABLE `aud_correspondences` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ATTACHMENT_CHECKSUM` varchar(255) DEFAULT NULL,
  `ATTACHMENT_FILE_ID` varchar(255) DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(255) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `COMMENTS` longtext,
  `DATE` date DEFAULT NULL,
  `DETAILS` longtext,
  `REASON` longtext,
  `TIME` varchar(255) DEFAULT NULL,
  `BILLABLE_ITEM_ID` bigint(20) DEFAULT NULL,
  `DIRECTION_TYPE_ID` bigint(20) DEFAULT NULL,
  `MODE_TYPE_ID` bigint(20) DEFAULT NULL,
  `OUTCOME_TYPE_ID` bigint(20) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  `ARK_USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKC08C786AF03BED18` (`REV`),
  CONSTRAINT `FKC08C786AF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_custom_field
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_custom_field`;

CREATE TABLE `aud_custom_field` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `HAS_DATA` tinyint(1) DEFAULT NULL,
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `ENCODED_VALUES` varchar(255) DEFAULT NULL,
  `CUSTOM_FIELD_LABEL` varchar(255) DEFAULT NULL,
  `MAX_VALUE` varchar(100) DEFAULT NULL,
  `MIN_VALUE` varchar(100) DEFAULT NULL,
  `MISSING_VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UNIT_TYPE_IN_TEXT` varchar(255) DEFAULT NULL,
  `ARK_FUNCTION_ID` bigint(20) DEFAULT NULL,
  `CATEGORY_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `UNIT_TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK88F407DBF03BED18` (`REV`),
  CONSTRAINT `FK88F407DBF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_custom_field_display
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_custom_field_display`;

CREATE TABLE `aud_custom_field_display` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) DEFAULT NULL,
  `REQUIRED` tinyint(1) DEFAULT NULL,
  `REQUIRED_MESSAGE` varchar(255) DEFAULT NULL,
  `SEQUENCE` bigint(20) DEFAULT NULL,
  `CUSTOM_FIELD_ID` bigint(20) DEFAULT NULL,
  `CUSTOM_FIELD_GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK2588483EF03BED18` (`REV`),
  CONSTRAINT `FK2588483EF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_custom_field_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_custom_field_group`;

CREATE TABLE `aud_custom_field_group` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `NAME` varchar(100) DEFAULT NULL,
  `PUBLISHED` tinyint(1) DEFAULT NULL,
  `ARK_FUNCTION_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK34B2D8FBF03BED18` (`REV`),
  CONSTRAINT `FK34B2D8FBF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_disease
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_disease`;

CREATE TABLE `aud_disease` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK679C92ADF03BED18` (`REV`),
  CONSTRAINT `FK679C92ADF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_disease_custom_fields
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_disease_custom_fields`;

CREATE TABLE `aud_disease_custom_fields` (
  `REV` int(11) NOT NULL,
  `DISEASE_ID` bigint(20) NOT NULL,
  `CUSTOM_FIELD_ID` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`DISEASE_ID`,`CUSTOM_FIELD_ID`),
  KEY `FK1471CD55F03BED18` (`REV`),
  CONSTRAINT `FK1471CD55F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_gene
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_gene`;

CREATE TABLE `aud_gene` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK6A03A764F03BED18` (`REV`),
  CONSTRAINT `FK6A03A764F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_gene_disease
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_gene_disease`;

CREATE TABLE `aud_gene_disease` (
  `REV` int(11) NOT NULL,
  `GENE_ID` bigint(20) NOT NULL,
  `DISEASE_ID` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`DISEASE_ID`,`GENE_ID`),
  KEY `FKDADEA3A1F03BED18` (`REV`),
  CONSTRAINT `FKDADEA3A1F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_inv_box
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_inv_box`;

CREATE TABLE `aud_inv_box` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `NOOFCOL` int(11) DEFAULT NULL,
  `NOOFROW` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `TRANSFER_ID` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `COLNOTYPE_ID` bigint(20) DEFAULT NULL,
  `RACK_ID` bigint(20) DEFAULT NULL,
  `ROWNOTYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK4DFFF9CEF03BED18` (`REV`),
  CONSTRAINT `FK4DFFF9CEF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_inv_cell
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_inv_cell`;

CREATE TABLE `aud_inv_cell` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `COLNO` bigint(20) DEFAULT NULL,
  `DELETED` bigint(20) DEFAULT NULL,
  `ROWNO` bigint(20) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `BIOSPECIMEN_ID` bigint(20) DEFAULT NULL,
  `BOX_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK71FF8DBFF03BED18` (`REV`),
  CONSTRAINT `FK71FF8DBFF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_inv_freezer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_inv_freezer`;

CREATE TABLE `aud_inv_freezer` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `COMMISSIONDATE` datetime DEFAULT NULL,
  `DECOMMISSIONDATE` datetime DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `LASTSERVICEDATE` datetime DEFAULT NULL,
  `LASTSERVICENOTE` longtext,
  `LOCATION` longtext,
  `NAME` varchar(50) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `SITE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKCE8DDB3EF03BED18` (`REV`),
  CONSTRAINT `FKCE8DDB3EF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_inv_rack
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_inv_rack`;

CREATE TABLE `aud_inv_rack` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `NAME` varchar(50) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `FREEZER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK72064F34F03BED18` (`REV`),
  CONSTRAINT `FK72064F34F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_inv_site
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_inv_site`;

CREATE TABLE `aud_inv_site` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ADDRESS` longtext,
  `CONTACT` varchar(50) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `LDAP_GROUP` varchar(50) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `PHONE` varchar(50) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK7206E3A4F03BED18` (`REV`),
  CONSTRAINT `FK7206E3A4F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_link_study_studycomp
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_link_study_studycomp`;

CREATE TABLE `aud_link_study_studycomp` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_COMP_ID` bigint(20) DEFAULT NULL,
  `STUDY_COMP_STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKC411312CF03BED18` (`REV`),
  CONSTRAINT `FKC411312CF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_link_subject_contact
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_link_subject_contact`;

CREATE TABLE `aud_link_subject_contact` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `FAMILY_ID` bigint(20) DEFAULT NULL,
  `PERSON_CONTACT_ID` bigint(20) DEFAULT NULL,
  `PERSON_SUBJECT_ID` bigint(20) DEFAULT NULL,
  `RELATIONSHIP_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK2B6C0BD7F03BED18` (`REV`),
  CONSTRAINT `FK2B6C0BD7F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_link_subject_study
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_link_subject_study`;

CREATE TABLE `aud_link_subject_study` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `COMMENTS` longtext,
  `CONSENT_DATE` date DEFAULT NULL,
  `HEARD_ABOUT_STUDY` longtext,
  `SUBJECT_UID` varchar(50) DEFAULT NULL,
  `CONSENT_DOWNLOADED` bigint(20) DEFAULT NULL,
  `CONSENT_STATUS_ID` bigint(20) DEFAULT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` bigint(20) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` bigint(20) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` bigint(20) DEFAULT NULL,
  `CONSENT_TYPE_ID` bigint(20) DEFAULT NULL,
  `PERSON_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `SUBJECT_STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK76257B40F03BED18` (`REV`),
  CONSTRAINT `FK76257B40F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_link_subject_studycomp
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_link_subject_studycomp`;

CREATE TABLE `aud_link_subject_studycomp` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `PERSON_SUBJECT_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_COMP_ID` bigint(20) DEFAULT NULL,
  `STUDY_COMP_STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKA495824FF03BED18` (`REV`),
  CONSTRAINT `FKA495824FF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_mutation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_mutation`;

CREATE TABLE `aud_mutation` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `screened` tinyint(1) DEFAULT NULL,
  `tested` tinyint(1) DEFAULT NULL,
  `DISEASE_ID` bigint(20) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKAF301778F03BED18` (`REV`),
  CONSTRAINT `FKAF301778F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_otherid
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_otherid`;

CREATE TABLE `aud_otherid` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `OtherID` varchar(30) DEFAULT NULL,
  `OtherID_Source` varchar(255) DEFAULT NULL,
  `PersonID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKF818B15CF03BED18` (`REV`),
  CONSTRAINT `FKF818B15CF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_person
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_person`;

CREATE TABLE `aud_person` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `CAUSE_OF_DEATH` varchar(255) DEFAULT NULL,
  `DATE_LAST_KNOWN_ALIVE` date DEFAULT NULL,
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `DATE_OF_DEATH` date DEFAULT NULL,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `OTHER_EMAIL` varchar(150) DEFAULT NULL,
  `PREFERRED_EMAIL` varchar(150) DEFAULT NULL,
  `PREFERRED_NAME` varchar(150) DEFAULT NULL,
  `GENDER_TYPE_ID` bigint(20) DEFAULT NULL,
  `MARITAL_STATUS_ID` bigint(20) DEFAULT NULL,
  `OTHER_EMAIL_STATUS` bigint(20) DEFAULT NULL,
  `PERSON_CONTACT_METHOD_ID` bigint(20) DEFAULT NULL,
  `PREFERRED_EMAIL_STATUS` bigint(20) DEFAULT NULL,
  `TITLE_TYPE_ID` bigint(20) DEFAULT NULL,
  `VITAL_STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK7150BE4F03BED18` (`REV`),
  CONSTRAINT `FK7150BE4F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_person_lastname_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_person_lastname_history`;

CREATE TABLE `aud_person_lastname_history` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `LASTNAME` varchar(50) DEFAULT NULL,
  `PERSON_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK76794351F03BED18` (`REV`),
  CONSTRAINT `FK76794351F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_pheno_collection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_pheno_collection`;

CREATE TABLE `aud_pheno_collection` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `RECORD_DATE` datetime DEFAULT NULL,
  `REVIEWED_DATE` datetime DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  `CUSTOM_FIELD_GROUP_ID` bigint(20) DEFAULT NULL,
  `REVIEWED_BY_ID` bigint(20) DEFAULT NULL,
  `QUESTIONNAIRE_STATUS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKE541D97EF03BED18` (`REV`),
  CONSTRAINT `FKE541D97EF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_pheno_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_pheno_data`;

CREATE TABLE `aud_pheno_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  `PHENO_COLLECTION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK8B12CE8AF03BED18` (`REV`),
  CONSTRAINT `FK8B12CE8AF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_phone
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_phone`;

CREATE TABLE `aud_phone` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AREA_CODE` varchar(10) DEFAULT NULL,
  `COMMENT` longtext,
  `DATE_RECEIVED` date DEFAULT NULL,
  `PHONE_NUMBER` varchar(10) DEFAULT NULL,
  `SOURCE` longtext,
  `PERSON_ID` bigint(20) DEFAULT NULL,
  `PHONE_TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKD6F17ADFF03BED18` (`REV`),
  CONSTRAINT `FKD6F17ADFF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_position
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_position`;

CREATE TABLE `aud_position` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `GENE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKA82A1DF8F03BED18` (`REV`),
  CONSTRAINT `FKA82A1DF8F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_researcher
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_researcher`;

CREATE TABLE `aud_researcher` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ACCOUNT_NAME` varchar(50) DEFAULT NULL,
  `ACCOUNT_NUMBER` varchar(30) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `BANK` varchar(50) DEFAULT NULL,
  `BSB` varchar(8) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `FAX` varchar(12) DEFAULT NULL,
  `FIRST_NAME` varchar(30) DEFAULT NULL,
  `LAST_NAME` varchar(45) DEFAULT NULL,
  `MOBILE` varchar(12) DEFAULT NULL,
  `OFFICE_PHONE` varchar(12) DEFAULT NULL,
  `ORGANIZATION` varchar(50) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `BILLING_TYPE_ID` bigint(20) DEFAULT NULL,
  `ROLE_ID` bigint(20) DEFAULT NULL,
  `STATUS_ID` bigint(20) DEFAULT NULL,
  `TITLE_TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK70755C97F03BED18` (`REV`),
  CONSTRAINT `FK70755C97F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_study
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_study`;

CREATE TABLE `aud_study` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AUTO_CONSENT` tinyint(1) DEFAULT NULL,
  `AUTO_GENERATE_BIOCOLLECTIONUID` tinyint(1) DEFAULT NULL,
  `AUTO_GENERATE_BIOSPECIMENUID` tinyint(1) DEFAULT NULL,
  `AUTO_GENERATE_SUBJECTUID` tinyint(1) DEFAULT NULL,
  `CHIEF_INVESTIGATOR` varchar(50) DEFAULT NULL,
  `CO_INVESTIGATOR` varchar(50) DEFAULT NULL,
  `CONTACT_PERSON` varchar(50) DEFAULT NULL,
  `CONTACT_PERSON_PHONE` varchar(20) DEFAULT NULL,
  `DATE_OF_APPLICATION` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ESTIMATED_YEAR_OF_COMPLETION` bigint(20) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `LDAP_GROUP_NAME` varchar(100) DEFAULT NULL,
  `NAME` varchar(150) DEFAULT NULL,
  `SAVE_TO_PARENT` tinyint(1) DEFAULT NULL,
  `SAVE_TO_PARENT_LOCKED` tinyint(1) DEFAULT NULL,
  `STUDY_LOGO` longblob,
  `SUB_STUDY_BIOSPECIMEN_PREFIX` varchar(20) DEFAULT NULL,
  `SUBJECTUID_PREFIX` varchar(3) DEFAULT NULL,
  `SUBJECTUID_START` bigint(20) DEFAULT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  `STUDY_STATUS_ID` bigint(20) DEFAULT NULL,
  `SUBJECTUID_PADCHAR_ID` bigint(20) DEFAULT NULL,
  `SUBJECTUID_TOKEN_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKD7214B3AF03BED18` (`REV`),
  CONSTRAINT `FKD7214B3AF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_study_comp
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_study_comp`;

CREATE TABLE `aud_study_comp` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKD94908F4F03BED18` (`REV`),
  CONSTRAINT `FKD94908F4F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_study_comp_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_study_comp_status`;

CREATE TABLE `aud_study_comp_status` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK126D7D9DF03BED18` (`REV`),
  CONSTRAINT `FK126D7D9DF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_study_inv_site
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_study_inv_site`;

CREATE TABLE `aud_study_inv_site` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `INV_SITE_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKCD54621AF03BED18` (`REV`),
  CONSTRAINT `FKCD54621AF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_subject_custom_field_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_subject_custom_field_data`;

CREATE TABLE `aud_subject_custom_field_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK7AEC81BF03BED18` (`REV`),
  CONSTRAINT `FK7AEC81BF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_user_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_user_config`;

CREATE TABLE `aud_user_config` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKFDA7CB07F03BED18` (`REV`),
  CONSTRAINT `FKFDA7CB07F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table aud_work_request
# ------------------------------------------------------------

DROP TABLE IF EXISTS `aud_work_request`;

CREATE TABLE `aud_work_request` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `COMMENCED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `GST` double DEFAULT NULL,
  `GST_ALLOW` tinyint(1) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `STATUS_ID` bigint(20) DEFAULT NULL,
  `RESEARCHER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKD59BD890F03BED18` (`REV`),
  CONSTRAINT `FKD59BD890F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table revinfo
# ------------------------------------------------------------

DROP TABLE IF EXISTS `revinfo`;

CREATE TABLE `revinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
