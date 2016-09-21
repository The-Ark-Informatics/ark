/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `admin`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `admin` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `admin`;

--
-- Table structure for table `bilable_item`
--

DROP TABLE IF EXISTS `bilable_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bilable_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `COMMENCE_DATE` date DEFAULT NULL,
  `TYPE` varchar(10) NOT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  `INVOICE` varchar(5) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `BILLABLE_TYPE` int(11) DEFAULT NULL,
  `TOTAL_COST` double DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(45) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `TOTAL_GST` double DEFAULT NULL,
  `ITEM_COST` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_bilable_item_billable_type` (`BILLABLE_TYPE`),
  KEY `fk_bilable_item_request_id` (`REQUEST_ID`),
  KEY `fk_bilable_item_status` (`STATUS_ID`),
  CONSTRAINT `fk_bilable_item_billable_type` FOREIGN KEY (`BILLABLE_TYPE`) REFERENCES `billable_item_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bilable_item_request_id` FOREIGN KEY (`REQUEST_ID`) REFERENCES `work_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bilable_item`
--

LOCK TABLES `bilable_item` WRITE;
/*!40000 ALTER TABLE `bilable_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `bilable_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billable_item_type`
--

DROP TABLE IF EXISTS `billable_item_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billable_item_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ITEM_NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `QUANTITY_PER_UNIT` int(11) DEFAULT NULL,
  `UNIT_PRICE` double DEFAULT NULL,
  `STATUS_ID` int(1) DEFAULT '1',
  `STUDY_ID` int(11) NOT NULL,
  `TYPE` varchar(10) NOT NULL DEFAULT 'DEFAULT',
  `QUANTITY_TYPE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_billable_item_type_study_id` (`STUDY_ID`),
  KEY `fk_billable_item_type_status_id` (`STATUS_ID`),
  CONSTRAINT `fk_billable_item_type_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `billable_item_type_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_billable_item_type_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billable_item_type`
--

LOCK TABLES `billable_item_type` WRITE;
/*!40000 ALTER TABLE `billable_item_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `billable_item_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billable_item_type_status`
--

DROP TABLE IF EXISTS `billable_item_type_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billable_item_type_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billable_item_type_status`
--

LOCK TABLES `billable_item_type_status` WRITE;
/*!40000 ALTER TABLE `billable_item_type_status` DISABLE KEYS */;
INSERT INTO `billable_item_type_status` VALUES (1,'ACTIVE','Active record');
INSERT INTO `billable_item_type_status` VALUES (2,'INACTIVE','Inactive record');
/*!40000 ALTER TABLE `billable_item_type_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billing_type`
--

DROP TABLE IF EXISTS `billing_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing_type`
--

LOCK TABLES `billing_type` WRITE;
/*!40000 ALTER TABLE `billing_type` DISABLE KEYS */;
INSERT INTO `billing_type` VALUES (1,'EFT',NULL);
INSERT INTO `billing_type` VALUES (2,'CHEQUE',NULL);
INSERT INTO `billing_type` VALUES (3,'CASH',NULL);
/*!40000 ALTER TABLE `billing_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `researcher`
--

DROP TABLE IF EXISTS `researcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `researcher` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(30) DEFAULT NULL,
  `LAST_NAME` varchar(45) DEFAULT NULL,
  `ORGANIZATION` varchar(50) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `OFFICE_PHONE` varchar(25) DEFAULT NULL,
  `MOBILE` varchar(25) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `FAX` varchar(25) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `BILLING_TYPE_ID` int(11) DEFAULT NULL,
  `ACCOUNT_NUMBER` varchar(30) DEFAULT NULL,
  `BSB` varchar(8) DEFAULT NULL,
  `BANK` varchar(50) DEFAULT NULL,
  `ACCOUNT_NAME` varchar(50) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `TITLE_TYPE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_researcher_role_id` (`ROLE_ID`),
  KEY `fk_researcher_status_id` (`STATUS_ID`),
  KEY `fk_researcher_billing_type_id` (`BILLING_TYPE_ID`),
  CONSTRAINT `fk_researcher_billing_type_id` FOREIGN KEY (`BILLING_TYPE_ID`) REFERENCES `billing_type` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_researcher_role_id` FOREIGN KEY (`ROLE_ID`) REFERENCES `researcher_role` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_researcher_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `researcher_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `researcher`
--

LOCK TABLES `researcher` WRITE;
/*!40000 ALTER TABLE `researcher` DISABLE KEYS */;
/*!40000 ALTER TABLE `researcher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `researcher_role`
--

DROP TABLE IF EXISTS `researcher_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `researcher_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ORDER_ID` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `researcher_role`
--

LOCK TABLES `researcher_role` WRITE;
/*!40000 ALTER TABLE `researcher_role` DISABLE KEYS */;
INSERT INTO `researcher_role` VALUES (0,'Principal Investigator',NULL,1);
INSERT INTO `researcher_role` VALUES (1,'Chief Investigator',NULL,2);
INSERT INTO `researcher_role` VALUES (2,'Associate Investigator',NULL,3);
INSERT INTO `researcher_role` VALUES (3,'Other Investigator',NULL,5);
INSERT INTO `researcher_role` VALUES (4,'Research Assistant',NULL,4);
/*!40000 ALTER TABLE `researcher_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `researcher_status`
--

DROP TABLE IF EXISTS `researcher_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `researcher_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `researcher_status`
--

LOCK TABLES `researcher_status` WRITE;
/*!40000 ALTER TABLE `researcher_status` DISABLE KEYS */;
INSERT INTO `researcher_status` VALUES (1,'Active',NULL);
INSERT INTO `researcher_status` VALUES (2,'Inactive','');
/*!40000 ALTER TABLE `researcher_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_request`
--

DROP TABLE IF EXISTS `work_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `RESEARCHER_ID` int(11) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `COMMENCED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `GST` double DEFAULT NULL,
  `GST_ALLOW` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_work_request_researcher_id` (`RESEARCHER_ID`),
  KEY `fk_work_request_status_id` (`STATUS_ID`),
  CONSTRAINT `fk_work_request_researcher_id` FOREIGN KEY (`RESEARCHER_ID`) REFERENCES `researcher` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_work_request_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `work_request_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_request`
--

LOCK TABLES `work_request` WRITE;
/*!40000 ALTER TABLE `work_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_request_status`
--

DROP TABLE IF EXISTS `work_request_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_request_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_request_status`
--

LOCK TABLES `work_request_status` WRITE;
/*!40000 ALTER TABLE `work_request_status` DISABLE KEYS */;
INSERT INTO `work_request_status` VALUES (1,'Not Commenced','');
INSERT INTO `work_request_status` VALUES (2,'Commenced','');
INSERT INTO `work_request_status` VALUES (3,'Completed','');
/*!40000 ALTER TABLE `work_request_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `audit`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `audit` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `audit`;

--
-- Table structure for table `aud_address`
--

DROP TABLE IF EXISTS `aud_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `VALID_FROM` date DEFAULT NULL,
  `VALID_TO` date DEFAULT NULL,
  `PERSON_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKBF943D25F03BED18` (`REV`),
  CONSTRAINT `FKBF943D25F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_address`
--

LOCK TABLES `aud_address` WRITE;
/*!40000 ALTER TABLE `aud_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_affection`
--

DROP TABLE IF EXISTS `aud_affection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_affection`
--

LOCK TABLES `aud_affection` WRITE;
/*!40000 ALTER TABLE `aud_affection` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_affection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_affection_custom_field_data`
--

DROP TABLE IF EXISTS `aud_affection_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_affection_custom_field_data`
--

LOCK TABLES `aud_affection_custom_field_data` WRITE;
/*!40000 ALTER TABLE `aud_affection_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_affection_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_affection_position`
--

DROP TABLE IF EXISTS `aud_affection_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_affection_position` (
  `REV` int(11) NOT NULL,
  `AFFECTION_ID` bigint(20) NOT NULL,
  `POSITION_ID` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`AFFECTION_ID`,`POSITION_ID`),
  KEY `FK6F4A3924F03BED18` (`REV`),
  CONSTRAINT `FK6F4A3924F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_affection_position`
--

LOCK TABLES `aud_affection_position` WRITE;
/*!40000 ALTER TABLE `aud_affection_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_affection_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_ark_user_role`
--

DROP TABLE IF EXISTS `aud_ark_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_ark_user_role`
--

LOCK TABLES `aud_ark_user_role` WRITE;
/*!40000 ALTER TABLE `aud_ark_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_ark_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_barcode_label`
--

DROP TABLE IF EXISTS `aud_barcode_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_barcode_label`
--

LOCK TABLES `aud_barcode_label` WRITE;
/*!40000 ALTER TABLE `aud_barcode_label` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_barcode_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_barcode_label_data`
--

DROP TABLE IF EXISTS `aud_barcode_label_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_barcode_label_data`
--

LOCK TABLES `aud_barcode_label_data` WRITE;
/*!40000 ALTER TABLE `aud_barcode_label_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_barcode_label_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_bilable_item`
--

DROP TABLE IF EXISTS `aud_bilable_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_bilable_item`
--

LOCK TABLES `aud_bilable_item` WRITE;
/*!40000 ALTER TABLE `aud_bilable_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_bilable_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_billable_item_type`
--

DROP TABLE IF EXISTS `aud_billable_item_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_billable_item_type`
--

LOCK TABLES `aud_billable_item_type` WRITE;
/*!40000 ALTER TABLE `aud_billable_item_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_billable_item_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_bio_transaction`
--

DROP TABLE IF EXISTS `aud_bio_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_bio_transaction`
--

LOCK TABLES `aud_bio_transaction` WRITE;
/*!40000 ALTER TABLE `aud_bio_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_bio_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_biocollection`
--

DROP TABLE IF EXISTS `aud_biocollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_biocollection`
--

LOCK TABLES `aud_biocollection` WRITE;
/*!40000 ALTER TABLE `aud_biocollection` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_biocollection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_biocollection_custom_field_data`
--

DROP TABLE IF EXISTS `aud_biocollection_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_biocollection_custom_field_data`
--

LOCK TABLES `aud_biocollection_custom_field_data` WRITE;
/*!40000 ALTER TABLE `aud_biocollection_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_biocollection_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_biospecimen`
--

DROP TABLE IF EXISTS `aud_biospecimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_biospecimen`
--

LOCK TABLES `aud_biospecimen` WRITE;
/*!40000 ALTER TABLE `aud_biospecimen` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_biospecimen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_biospecimen_biospecimen`
--

DROP TABLE IF EXISTS `aud_biospecimen_biospecimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_biospecimen_biospecimen` (
  `REV` int(11) NOT NULL,
  `PARENT_ID` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`PARENT_ID`,`id`),
  KEY `FKF2338972F03BED18` (`REV`),
  CONSTRAINT `FKF2338972F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_biospecimen_biospecimen`
--

LOCK TABLES `aud_biospecimen_biospecimen` WRITE;
/*!40000 ALTER TABLE `aud_biospecimen_biospecimen` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_biospecimen_biospecimen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_biospecimen_custom_field_data`
--

DROP TABLE IF EXISTS `aud_biospecimen_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_biospecimen_custom_field_data`
--

LOCK TABLES `aud_biospecimen_custom_field_data` WRITE;
/*!40000 ALTER TABLE `aud_biospecimen_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_biospecimen_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_biospecimen_species`
--

DROP TABLE IF EXISTS `aud_biospecimen_species`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_biospecimen_species` (
  `id` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD98D73DEF03BED18` (`REV`),
  CONSTRAINT `FKD98D73DEF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_biospecimen_species`
--

LOCK TABLES `aud_biospecimen_species` WRITE;
/*!40000 ALTER TABLE `aud_biospecimen_species` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_biospecimen_species` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_config_fields`
--

DROP TABLE IF EXISTS `aud_config_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_config_fields`
--

LOCK TABLES `aud_config_fields` WRITE;
/*!40000 ALTER TABLE `aud_config_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_config_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_consent`
--

DROP TABLE IF EXISTS `aud_consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_consent`
--

LOCK TABLES `aud_consent` WRITE;
/*!40000 ALTER TABLE `aud_consent` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_consent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_correspondences`
--

DROP TABLE IF EXISTS `aud_correspondences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_correspondences`
--

LOCK TABLES `aud_correspondences` WRITE;
/*!40000 ALTER TABLE `aud_correspondences` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_correspondences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_custom_field`
--

DROP TABLE IF EXISTS `aud_custom_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_custom_field` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `HAS_DATA` tinyint(1) DEFAULT NULL,
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `ENCODED_VALUES` text,
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
  `CUSTOM_FIELD_TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK88F407DBF03BED18` (`REV`),
  CONSTRAINT `FK88F407DBF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_custom_field`
--

LOCK TABLES `aud_custom_field` WRITE;
/*!40000 ALTER TABLE `aud_custom_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_custom_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_custom_field_category`
--

DROP TABLE IF EXISTS `aud_custom_field_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_custom_field_category` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `CUSTOM_FIELD_TYPE_ID` bigint(20) NOT NULL,
  `STUDY_ID` bigint(20) NOT NULL,
  `ARK_FUNCTION_ID` bigint(20) NOT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  `ORDER_NUMBER` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_AUD_CUSFIELD_CATEGORY` (`REV`),
  CONSTRAINT `FK_AUD_CUSFIELD_CATEGORY` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_custom_field_category`
--

LOCK TABLES `aud_custom_field_category` WRITE;
/*!40000 ALTER TABLE `aud_custom_field_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_custom_field_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_custom_field_display`
--

DROP TABLE IF EXISTS `aud_custom_field_display`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_custom_field_display`
--

LOCK TABLES `aud_custom_field_display` WRITE;
/*!40000 ALTER TABLE `aud_custom_field_display` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_custom_field_display` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_custom_field_group`
--

DROP TABLE IF EXISTS `aud_custom_field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_custom_field_group`
--

LOCK TABLES `aud_custom_field_group` WRITE;
/*!40000 ALTER TABLE `aud_custom_field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_custom_field_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_disease`
--

DROP TABLE IF EXISTS `aud_disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_disease`
--

LOCK TABLES `aud_disease` WRITE;
/*!40000 ALTER TABLE `aud_disease` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_disease_custom_fields`
--

DROP TABLE IF EXISTS `aud_disease_custom_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_disease_custom_fields` (
  `REV` int(11) NOT NULL,
  `DISEASE_ID` bigint(20) NOT NULL,
  `CUSTOM_FIELD_ID` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`DISEASE_ID`,`CUSTOM_FIELD_ID`),
  KEY `FK1471CD55F03BED18` (`REV`),
  CONSTRAINT `FK1471CD55F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_disease_custom_fields`
--

LOCK TABLES `aud_disease_custom_fields` WRITE;
/*!40000 ALTER TABLE `aud_disease_custom_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_disease_custom_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_family_custom_field_data`
--

DROP TABLE IF EXISTS `aud_family_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_family_custom_field_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `FAMILY_UID` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_family_custom_field_data`
--

LOCK TABLES `aud_family_custom_field_data` WRITE;
/*!40000 ALTER TABLE `aud_family_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_family_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_gene`
--

DROP TABLE IF EXISTS `aud_gene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_gene`
--

LOCK TABLES `aud_gene` WRITE;
/*!40000 ALTER TABLE `aud_gene` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_gene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_gene_disease`
--

DROP TABLE IF EXISTS `aud_gene_disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_gene_disease` (
  `REV` int(11) NOT NULL,
  `GENE_ID` bigint(20) NOT NULL,
  `DISEASE_ID` bigint(20) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`DISEASE_ID`,`GENE_ID`),
  KEY `FKDADEA3A1F03BED18` (`REV`),
  CONSTRAINT `FKDADEA3A1F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_gene_disease`
--

LOCK TABLES `aud_gene_disease` WRITE;
/*!40000 ALTER TABLE `aud_gene_disease` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_gene_disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_inv_box`
--

DROP TABLE IF EXISTS `aud_inv_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_inv_box`
--

LOCK TABLES `aud_inv_box` WRITE;
/*!40000 ALTER TABLE `aud_inv_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_inv_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_inv_cell`
--

DROP TABLE IF EXISTS `aud_inv_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_inv_cell`
--

LOCK TABLES `aud_inv_cell` WRITE;
/*!40000 ALTER TABLE `aud_inv_cell` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_inv_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_inv_freezer`
--

DROP TABLE IF EXISTS `aud_inv_freezer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_inv_freezer`
--

LOCK TABLES `aud_inv_freezer` WRITE;
/*!40000 ALTER TABLE `aud_inv_freezer` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_inv_freezer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_inv_rack`
--

DROP TABLE IF EXISTS `aud_inv_rack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_inv_rack`
--

LOCK TABLES `aud_inv_rack` WRITE;
/*!40000 ALTER TABLE `aud_inv_rack` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_inv_rack` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_inv_site`
--

DROP TABLE IF EXISTS `aud_inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_inv_site`
--

LOCK TABLES `aud_inv_site` WRITE;
/*!40000 ALTER TABLE `aud_inv_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_inv_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_link_study_studycomp`
--

DROP TABLE IF EXISTS `aud_link_study_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_link_study_studycomp`
--

LOCK TABLES `aud_link_study_studycomp` WRITE;
/*!40000 ALTER TABLE `aud_link_study_studycomp` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_link_study_studycomp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_link_study_studysite`
--

DROP TABLE IF EXISTS `aud_link_study_studysite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_link_study_studysite` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `STUDY_SITE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKC4186164F03BED18` (`REV`),
  CONSTRAINT `FKC4186164F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_link_study_studysite`
--

LOCK TABLES `aud_link_study_studysite` WRITE;
/*!40000 ALTER TABLE `aud_link_study_studysite` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_link_study_studysite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_link_study_substudy`
--

DROP TABLE IF EXISTS `aud_link_study_substudy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_link_study_substudy` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `SUB_STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK2C22C815F03BED18` (`REV`),
  CONSTRAINT `FK2C22C815F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_link_study_substudy`
--

LOCK TABLES `aud_link_study_substudy` WRITE;
/*!40000 ALTER TABLE `aud_link_study_substudy` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_link_study_substudy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_link_subject_contact`
--

DROP TABLE IF EXISTS `aud_link_subject_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_link_subject_contact`
--

LOCK TABLES `aud_link_subject_contact` WRITE;
/*!40000 ALTER TABLE `aud_link_subject_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_link_subject_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_link_subject_study`
--

DROP TABLE IF EXISTS `aud_link_subject_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_link_subject_study`
--

LOCK TABLES `aud_link_subject_study` WRITE;
/*!40000 ALTER TABLE `aud_link_subject_study` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_link_subject_study` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_link_subject_studycomp`
--

DROP TABLE IF EXISTS `aud_link_subject_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_link_subject_studycomp`
--

LOCK TABLES `aud_link_subject_studycomp` WRITE;
/*!40000 ALTER TABLE `aud_link_subject_studycomp` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_link_subject_studycomp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_lss_pipeline`
--

DROP TABLE IF EXISTS `aud_lss_pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_lss_pipeline` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `LSS_ID` bigint(20) DEFAULT NULL,
  `PIPELINE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK7DFD1B24F03BED18` (`REV`),
  CONSTRAINT `FK7DFD1B24F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_lss_pipeline`
--

LOCK TABLES `aud_lss_pipeline` WRITE;
/*!40000 ALTER TABLE `aud_lss_pipeline` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_lss_pipeline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_mutation`
--

DROP TABLE IF EXISTS `aud_mutation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_mutation`
--

LOCK TABLES `aud_mutation` WRITE;
/*!40000 ALTER TABLE `aud_mutation` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_mutation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_otherid`
--

DROP TABLE IF EXISTS `aud_otherid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_otherid`
--

LOCK TABLES `aud_otherid` WRITE;
/*!40000 ALTER TABLE `aud_otherid` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_otherid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_person`
--

DROP TABLE IF EXISTS `aud_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `CURRENT_OR_DEATH_AGE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK7150BE4F03BED18` (`REV`),
  CONSTRAINT `FK7150BE4F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_person`
--

LOCK TABLES `aud_person` WRITE;
/*!40000 ALTER TABLE `aud_person` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_person_lastname_history`
--

DROP TABLE IF EXISTS `aud_person_lastname_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_person_lastname_history`
--

LOCK TABLES `aud_person_lastname_history` WRITE;
/*!40000 ALTER TABLE `aud_person_lastname_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_person_lastname_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_pheno_collection`
--

DROP TABLE IF EXISTS `aud_pheno_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_pheno_collection`
--

LOCK TABLES `aud_pheno_collection` WRITE;
/*!40000 ALTER TABLE `aud_pheno_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_pheno_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_pheno_data`
--

DROP TABLE IF EXISTS `aud_pheno_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_pheno_data`
--

LOCK TABLES `aud_pheno_data` WRITE;
/*!40000 ALTER TABLE `aud_pheno_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_pheno_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_pheno_dataset_category`
--

DROP TABLE IF EXISTS `aud_pheno_dataset_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_pheno_dataset_category` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `STUDY_ID` bigint(20) NOT NULL,
  `ARK_FUNCTION_ID` bigint(20) NOT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  `ORDER_NUMBER` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK_AUD_PHENO_DATASET_CATEGORY` (`REV`),
  CONSTRAINT `FK_AUD_PHENO_DATASET_CATEGORY` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_pheno_dataset_category`
--

LOCK TABLES `aud_pheno_dataset_category` WRITE;
/*!40000 ALTER TABLE `aud_pheno_dataset_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_pheno_dataset_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_phone`
--

DROP TABLE IF EXISTS `aud_phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_phone` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AREA_CODE` varchar(10) DEFAULT NULL,
  `COMMENT` longtext,
  `DATE_RECEIVED` date DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `SOURCE` longtext,
  `PERSON_ID` bigint(20) DEFAULT NULL,
  `PHONE_TYPE_ID` bigint(20) DEFAULT NULL,
  `PHONE_STATUS_ID` bigint(20) DEFAULT NULL,
  `SILENT` bigint(20) DEFAULT NULL,
  `PREFERRED_PHONE_NUMBER` tinyint(1) DEFAULT NULL,
  `VALID_FROM` date DEFAULT NULL,
  `VALID_TO` date DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKD6F17ADFF03BED18` (`REV`),
  CONSTRAINT `FKD6F17ADFF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_phone`
--

LOCK TABLES `aud_phone` WRITE;
/*!40000 ALTER TABLE `aud_phone` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_pipeline`
--

DROP TABLE IF EXISTS `aud_pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_pipeline` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK656A2F71F03BED18` (`REV`),
  CONSTRAINT `FK656A2F71F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_pipeline`
--

LOCK TABLES `aud_pipeline` WRITE;
/*!40000 ALTER TABLE `aud_pipeline` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_pipeline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_position`
--

DROP TABLE IF EXISTS `aud_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_position`
--

LOCK TABLES `aud_position` WRITE;
/*!40000 ALTER TABLE `aud_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_process`
--

DROP TABLE IF EXISTS `aud_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_process` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `COMMAND_ID` bigint(20) DEFAULT NULL,
  `PIPELINE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKF189BFA0F03BED18` (`REV`),
  CONSTRAINT `FKF189BFA0F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_process`
--

LOCK TABLES `aud_process` WRITE;
/*!40000 ALTER TABLE `aud_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_process_input`
--

DROP TABLE IF EXISTS `aud_process_input`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_process_input` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `INPUT_FILE_HASH` varchar(255) DEFAULT NULL,
  `INPUT_FILE_LOCATION` varchar(255) DEFAULT NULL,
  `INPUT_FILE_TYPE` varchar(255) DEFAULT NULL,
  `INPUT_KEPT` int(11) DEFAULT NULL,
  `INPUT_SERVER` varchar(255) DEFAULT NULL,
  `PROCESS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK63294A4BF03BED18` (`REV`),
  CONSTRAINT `FK63294A4BF03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_process_input`
--

LOCK TABLES `aud_process_input` WRITE;
/*!40000 ALTER TABLE `aud_process_input` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_process_input` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_process_output`
--

DROP TABLE IF EXISTS `aud_process_output`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_process_output` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `OUTPUT_FILE_HASH` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_LOCATION` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_TYPE` varchar(255) DEFAULT NULL,
  `OUTPUT_KEPT` int(11) DEFAULT NULL,
  `OUTPUT_SERVER` varchar(255) DEFAULT NULL,
  `PROCESS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FKCA17680F03BED18` (`REV`),
  CONSTRAINT `FKCA17680F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_process_output`
--

LOCK TABLES `aud_process_output` WRITE;
/*!40000 ALTER TABLE `aud_process_output` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_process_output` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_researcher`
--

DROP TABLE IF EXISTS `aud_researcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_researcher`
--

LOCK TABLES `aud_researcher` WRITE;
/*!40000 ALTER TABLE `aud_researcher` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_researcher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_study`
--

DROP TABLE IF EXISTS `aud_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_study`
--

LOCK TABLES `aud_study` WRITE;
/*!40000 ALTER TABLE `aud_study` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_study` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_study_comp`
--

DROP TABLE IF EXISTS `aud_study_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_study_comp`
--

LOCK TABLES `aud_study_comp` WRITE;
/*!40000 ALTER TABLE `aud_study_comp` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_study_comp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_study_comp_status`
--

DROP TABLE IF EXISTS `aud_study_comp_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_study_comp_status`
--

LOCK TABLES `aud_study_comp_status` WRITE;
/*!40000 ALTER TABLE `aud_study_comp_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_study_comp_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_study_inv_site`
--

DROP TABLE IF EXISTS `aud_study_inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_study_inv_site`
--

LOCK TABLES `aud_study_inv_site` WRITE;
/*!40000 ALTER TABLE `aud_study_inv_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_study_inv_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_study_pedigree_config`
--

DROP TABLE IF EXISTS `aud_study_pedigree_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aud_study_pedigree_config` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `AGE_ALLOWED` tinyint(1) DEFAULT NULL,
  `DOB_ALLOWED` tinyint(1) DEFAULT NULL,
  `STATUS_ALLOWED` tinyint(1) DEFAULT NULL,
  `CUSTOM_FIELD_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `FAMILY_ID` bigint(20) DEFAULT NULL,
  `INBREED_ALLOWED` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`),
  KEY `FK4FF0A2B7F03BED18` (`REV`),
  CONSTRAINT `FK4FF0A2B7F03BED18` FOREIGN KEY (`REV`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_study_pedigree_config`
--

LOCK TABLES `aud_study_pedigree_config` WRITE;
/*!40000 ALTER TABLE `aud_study_pedigree_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_study_pedigree_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_subject_custom_field_data`
--

DROP TABLE IF EXISTS `aud_subject_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_subject_custom_field_data`
--

LOCK TABLES `aud_subject_custom_field_data` WRITE;
/*!40000 ALTER TABLE `aud_subject_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_subject_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_user_config`
--

DROP TABLE IF EXISTS `aud_user_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_user_config`
--

LOCK TABLES `aud_user_config` WRITE;
/*!40000 ALTER TABLE `aud_user_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_user_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aud_work_request`
--

DROP TABLE IF EXISTS `aud_work_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aud_work_request`
--

LOCK TABLES `aud_work_request` WRITE;
/*!40000 ALTER TABLE `aud_work_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `aud_work_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_entity`
--

DROP TABLE IF EXISTS `audit_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_entity` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASS_IDENTIFIER` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PACKAGE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FKC09398E79E094944` (`PACKAGE_ID`),
  CONSTRAINT `FKC09398E79E094944` FOREIGN KEY (`PACKAGE_ID`) REFERENCES `audit_package` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_entity`
--

LOCK TABLES `audit_entity` WRITE;
/*!40000 ALTER TABLE `audit_entity` DISABLE KEYS */;
INSERT INTO `audit_entity` VALUES (1,'au.org.theark.core.model.config.entity.UserConfig','User Config',1);
INSERT INTO `audit_entity` VALUES (2,'au.org.theark.core.model.disease.entity.Affection','Affection',2);
INSERT INTO `audit_entity` VALUES (3,'au.org.theark.core.model.disease.entity.AffectionCustomFieldData','Affection Custom Field Data',2);
INSERT INTO `audit_entity` VALUES (4,'au.org.theark.core.model.disease.entity.Disease','Disease',2);
INSERT INTO `audit_entity` VALUES (5,'au.org.theark.core.model.disease.entity.Gene','Gene',2);
INSERT INTO `audit_entity` VALUES (6,'au.org.theark.core.model.disease.entity.Mutation','Mutation',2);
INSERT INTO `audit_entity` VALUES (7,'au.org.theark.core.model.disease.entity.Position','Position',2);
INSERT INTO `audit_entity` VALUES (8,'au.org.theark.core.model.lims.entity.BarcodeLabel','Barcode Label',3);
INSERT INTO `audit_entity` VALUES (9,'au.org.theark.core.model.lims.entity.BarcodeLabelData','Barcode Label Data',3);
INSERT INTO `audit_entity` VALUES (10,'au.org.theark.core.model.lims.entity.BioCollection','BioCollection',3);
INSERT INTO `audit_entity` VALUES (11,'au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData','BioCollection Custom Field Data',3);
INSERT INTO `audit_entity` VALUES (12,'au.org.theark.core.model.lims.entity.Biospecimen','Biospecimen',3);
INSERT INTO `audit_entity` VALUES (13,'au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData','Biospecimen Custom Field Data',3);
INSERT INTO `audit_entity` VALUES (14,'au.org.theark.core.model.lims.entity.BiospecimenSpecies','Biospecimen Species',3);
INSERT INTO `audit_entity` VALUES (15,'au.org.theark.core.model.lims.entity.BioTransaction','BioTransaction',3);
INSERT INTO `audit_entity` VALUES (16,'au.org.theark.core.model.lims.entity.InvBox','Inventory Box',3);
INSERT INTO `audit_entity` VALUES (17,'au.org.theark.core.model.lims.entity.InvCell','Inventory Cell',3);
INSERT INTO `audit_entity` VALUES (18,'au.org.theark.core.model.lims.entity.InvFreezer','Inventory Freezer',3);
INSERT INTO `audit_entity` VALUES (19,'au.org.theark.core.model.lims.entity.InvRack','Inventory Rack',3);
INSERT INTO `audit_entity` VALUES (20,'au.org.theark.core.model.lims.entity.InvSite','Inventory Site',3);
INSERT INTO `audit_entity` VALUES (21,'au.org.theark.core.model.pheno.entity.PhenoCollection','Pheno Collection',4);
INSERT INTO `audit_entity` VALUES (22,'au.org.theark.core.model.pheno.entity.PhenoData','Pheno Data',4);
INSERT INTO `audit_entity` VALUES (23,'au.org.theark.core.model.study.entity.Address','Address',5);
INSERT INTO `audit_entity` VALUES (24,'au.org.theark.core.model.study.entity.ArkUserRole','Ark User Role',5);
INSERT INTO `audit_entity` VALUES (25,'au.org.theark.core.model.study.entity.Consent','Consent',5);
INSERT INTO `audit_entity` VALUES (26,'au.org.theark.core.model.study.entity.Correspondences','Correspondence',5);
INSERT INTO `audit_entity` VALUES (27,'au.org.theark.core.model.study.entity.CustomField','Custom Field',5);
INSERT INTO `audit_entity` VALUES (28,'au.org.theark.core.model.study.entity.CustomFieldDisplay','Custom Field Display',5);
INSERT INTO `audit_entity` VALUES (29,'au.org.theark.core.model.study.entity.CustomFieldGroup','Custom Field Group',5);
INSERT INTO `audit_entity` VALUES (30,'au.org.theark.core.model.study.entity.LinkStudyStudycomp','Link Study - Study Component',5);
INSERT INTO `audit_entity` VALUES (31,'au.org.theark.core.model.study.entity.LinkSubjectContact','Link Subject - Contact',5);
INSERT INTO `audit_entity` VALUES (32,'au.org.theark.core.model.study.entity.LinkSubjectStudy','Link Subject - Study',5);
INSERT INTO `audit_entity` VALUES (33,'au.org.theark.core.model.study.entity.LinkSubjectStudycomp','Link Subject - Study Component',5);
INSERT INTO `audit_entity` VALUES (34,'au.org.theark.core.model.study.entity.OtherID','Other ID',5);
INSERT INTO `audit_entity` VALUES (35,'au.org.theark.core.model.study.entity.Person','Person',5);
INSERT INTO `audit_entity` VALUES (36,'au.org.theark.core.model.study.entity.PersonLastnameHistory','Person Lastname History',5);
INSERT INTO `audit_entity` VALUES (37,'au.org.theark.core.model.study.entity.Phone','Phone',5);
INSERT INTO `audit_entity` VALUES (38,'au.org.theark.core.model.study.entity.Study','Study',5);
INSERT INTO `audit_entity` VALUES (39,'au.org.theark.core.model.study.entity.StudyComp','Study Component',5);
INSERT INTO `audit_entity` VALUES (40,'au.org.theark.core.model.study.entity.StudyCompStatus','Study Component Status',5);
INSERT INTO `audit_entity` VALUES (41,'au.org.theark.core.model.lims.entity.StudyInvSite','Study Inventory - Site',3);
INSERT INTO `audit_entity` VALUES (42,'au.org.theark.core.model.study.entity.SubjectCustomFieldData','Subject Custom Field Data',5);
INSERT INTO `audit_entity` VALUES (43,'au.org.theark.core.model.worktracking.entity.BillableItem','Billable Item',6);
INSERT INTO `audit_entity` VALUES (44,'au.org.theark.core.model.worktracking.entity.BillableItemType','Billable Item Type',6);
INSERT INTO `audit_entity` VALUES (45,'au.org.theark.core.model.worktracking.entity.Researcher','Researcher',6);
INSERT INTO `audit_entity` VALUES (46,'au.org.theark.core.model.worktracking.entity.WorkRequest','Work Request',6);
INSERT INTO `audit_entity` VALUES (47,'au.org.theark.core.model.study.entity.LinkStudyStudysite','Link Study - Study Site',5);
INSERT INTO `audit_entity` VALUES (48,'au.org.theark.core.model.study.entity.LinkStudySubstudy','Link Study - Sub-Study',5);
/*!40000 ALTER TABLE `audit_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_field`
--

DROP TABLE IF EXISTS `audit_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_field` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ENTITY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FKED7BB756F02A6C50` (`ENTITY_ID`),
  CONSTRAINT `FKED7BB756F02A6C50` FOREIGN KEY (`ENTITY_ID`) REFERENCES `audit_entity` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_field`
--

LOCK TABLES `audit_field` WRITE;
/*!40000 ALTER TABLE `audit_field` DISABLE KEYS */;
INSERT INTO `audit_field` VALUES (1,'value','Value',1);
INSERT INTO `audit_field` VALUES (2,'arkUser','Ark User',1);
INSERT INTO `audit_field` VALUES (3,'configField','Config Field',1);
INSERT INTO `audit_field` VALUES (4,'recordDate','Record Date',2);
INSERT INTO `audit_field` VALUES (5,'affectionStatus','Affection Status',2);
INSERT INTO `audit_field` VALUES (6,'disease','Disease',2);
INSERT INTO `audit_field` VALUES (7,'linkSubjectStudy','LinkSubjectStudy',2);
INSERT INTO `audit_field` VALUES (8,'study','Study',2);
INSERT INTO `audit_field` VALUES (9,'dateDataValue','Date Data Value',3);
INSERT INTO `audit_field` VALUES (10,'errorDataValue','Error Data Value',3);
INSERT INTO `audit_field` VALUES (11,'numberDataValue','Number Data Value',3);
INSERT INTO `audit_field` VALUES (12,'textDataValue','Text Data Value',3);
INSERT INTO `audit_field` VALUES (13,'affection','Affection',3);
INSERT INTO `audit_field` VALUES (14,'customFieldDisplay','Custom Field Display',3);
INSERT INTO `audit_field` VALUES (15,'name','Name',4);
INSERT INTO `audit_field` VALUES (16,'study','Study',4);
INSERT INTO `audit_field` VALUES (17,'name','Name',5);
INSERT INTO `audit_field` VALUES (18,'study','Study',5);
INSERT INTO `audit_field` VALUES (19,'screened','Screened',6);
INSERT INTO `audit_field` VALUES (20,'tested','Tested',6);
INSERT INTO `audit_field` VALUES (21,'disease','Disease',6);
INSERT INTO `audit_field` VALUES (22,'linkSubjectStudy','LinkSubjectStudy',6);
INSERT INTO `audit_field` VALUES (23,'name','Name',7);
INSERT INTO `audit_field` VALUES (24,'gene','Gene',7);
INSERT INTO `audit_field` VALUES (25,'barcodePrinterName','Barcode Printer Name',8);
INSERT INTO `audit_field` VALUES (26,'description','Description',8);
INSERT INTO `audit_field` VALUES (27,'labelPrefix','Label Prefix',8);
INSERT INTO `audit_field` VALUES (28,'labelSuffix','Label Suffix',8);
INSERT INTO `audit_field` VALUES (29,'name','Name',8);
INSERT INTO `audit_field` VALUES (30,'version','Version',8);
INSERT INTO `audit_field` VALUES (31,'study','Study',8);
INSERT INTO `audit_field` VALUES (32,'xCoord','X-Coordinate',9);
INSERT INTO `audit_field` VALUES (33,'yCoord','Y-Coordinate',9);
INSERT INTO `audit_field` VALUES (34,'command','Command',9);
INSERT INTO `audit_field` VALUES (35,'data','Data',9);
INSERT INTO `audit_field` VALUES (36,'lineFeed','Line Feed',9);
INSERT INTO `audit_field` VALUES (37,'p1','P1',9);
INSERT INTO `audit_field` VALUES (38,'p2','P2',9);
INSERT INTO `audit_field` VALUES (39,'p3','P3',9);
INSERT INTO `audit_field` VALUES (40,'p4','P4',9);
INSERT INTO `audit_field` VALUES (41,'p5','P5',9);
INSERT INTO `audit_field` VALUES (42,'p6','P6',9);
INSERT INTO `audit_field` VALUES (43,'p7','P7',9);
INSERT INTO `audit_field` VALUES (44,'p8','P8',9);
INSERT INTO `audit_field` VALUES (45,'quoteLeft','Quote Left',9);
INSERT INTO `audit_field` VALUES (46,'quoteRight','Quote Right',9);
INSERT INTO `audit_field` VALUES (47,'barcodeLabel','Barcode Label',9);
INSERT INTO `audit_field` VALUES (48,'biocollectionUid','Biocollection UID',10);
INSERT INTO `audit_field` VALUES (49,'collectionDate','Collection Date',10);
INSERT INTO `audit_field` VALUES (50,'collectiongroup','Collection Group',10);
INSERT INTO `audit_field` VALUES (51,'collectiongroupId','Collection Group ID',10);
INSERT INTO `audit_field` VALUES (52,'comments','Comments',10);
INSERT INTO `audit_field` VALUES (53,'deleted','Deleted',10);
INSERT INTO `audit_field` VALUES (54,'diagCategory','Diagnosis Category',10);
INSERT INTO `audit_field` VALUES (55,'diadDate','Diagnosis Date',10);
INSERT INTO `audit_field` VALUES (56,'dischargeDate','Discharge Date',10);
INSERT INTO `audit_field` VALUES (57,'episodeDesc','Episode Description',10);
INSERT INTO `audit_field` VALUES (58,'episodeNum','Episode Number',10);
INSERT INTO `audit_field` VALUES (59,'hospital','Hospital',10);
INSERT INTO `audit_field` VALUES (60,'hospitalUr','Hospital UR',10);
INSERT INTO `audit_field` VALUES (61,'name','Name',10);
INSERT INTO `audit_field` VALUES (62,'pathlabno','Path Lab Number',10);
INSERT INTO `audit_field` VALUES (63,'patientage','Patient Age',10);
INSERT INTO `audit_field` VALUES (64,'refDoctor','RefDoctor',10);
INSERT INTO `audit_field` VALUES (65,'surgeryDate','Surgery Date',10);
INSERT INTO `audit_field` VALUES (66,'timestamp','Time Stamp',10);
INSERT INTO `audit_field` VALUES (67,'tissueclass','Tissue Class',10);
INSERT INTO `audit_field` VALUES (68,'tissuetupe','Tissue Type',10);
INSERT INTO `audit_field` VALUES (69,'linkSubjectStudy','LinkSubjectStudy',10);
INSERT INTO `audit_field` VALUES (70,'study','Study',10);
INSERT INTO `audit_field` VALUES (71,'dateDataValue','Date Data Value',11);
INSERT INTO `audit_field` VALUES (72,'errorDataValue','Error Data Value',11);
INSERT INTO `audit_field` VALUES (73,'numberDataValue','Number Data Value',11);
INSERT INTO `audit_field` VALUES (74,'textDataValue','Text Data Value',11);
INSERT INTO `audit_field` VALUES (75,'bioCollection','Biocolletion',11);
INSERT INTO `audit_field` VALUES (76,'customFieldDisplay','Custom Field Display',11);
INSERT INTO `audit_field` VALUES (77,'barcoded','Barcoded',12);
INSERT INTO `audit_field` VALUES (78,'biospecimenUid','Biospecimen UID',12);
INSERT INTO `audit_field` VALUES (79,'comments','Comments',12);
INSERT INTO `audit_field` VALUES (80,'concentration','Concentration',12);
INSERT INTO `audit_field` VALUES (81,'deleted','Deleted',12);
INSERT INTO `audit_field` VALUES (82,'depth','Depth',12);
INSERT INTO `audit_field` VALUES (83,'oldId','Old ID',12);
INSERT INTO `audit_field` VALUES (84,'otherid','Other ID',12);
INSERT INTO `audit_field` VALUES (85,'parentUid','Parent UID',12);
INSERT INTO `audit_field` VALUES (86,'processedDate','Processed Date',12);
INSERT INTO `audit_field` VALUES (87,'processedTime','Processed Time',12);
INSERT INTO `audit_field` VALUES (88,'purity','Purity',12);
INSERT INTO `audit_field` VALUES (89,'qtyCollected','Quantity Collected',12);
INSERT INTO `audit_field` VALUES (90,'qtyRemoved','Quantity Removed',12);
INSERT INTO `audit_field` VALUES (91,'quantity','Quantity',12);
INSERT INTO `audit_field` VALUES (92,'sampleDate','Sample Date',12);
INSERT INTO `audit_field` VALUES (93,'sampleTime','Sample Time',12);
INSERT INTO `audit_field` VALUES (94,'substudyId','Sub Study ID',12);
INSERT INTO `audit_field` VALUES (95,'timestamp','Time Stamp',12);
INSERT INTO `audit_field` VALUES (96,'anticoag','Anticoagulant',12);
INSERT INTO `audit_field` VALUES (97,'biospecimenProtocol','Protocol',12);
INSERT INTO `audit_field` VALUES (98,'grade','Grade',12);
INSERT INTO `audit_field` VALUES (99,'linkSubjectStudy','LinkSubjectStudy',12);
INSERT INTO `audit_field` VALUES (100,'quality','Quality',12);
INSERT INTO `audit_field` VALUES (101,'sampleType','Sample Type',12);
INSERT INTO `audit_field` VALUES (102,'species','Species',12);
INSERT INTO `audit_field` VALUES (103,'status','Status',12);
INSERT INTO `audit_field` VALUES (104,'storedIn','Storage',12);
INSERT INTO `audit_field` VALUES (105,'study','Study',12);
INSERT INTO `audit_field` VALUES (106,'treatmentType','Treatment Type',12);
INSERT INTO `audit_field` VALUES (107,'unit','Unit',12);
INSERT INTO `audit_field` VALUES (108,'dateDataValue','Date Data Value',13);
INSERT INTO `audit_field` VALUES (109,'errorDataValue','Error Data Value',13);
INSERT INTO `audit_field` VALUES (110,'numberDataValue','Number Data Value',13);
INSERT INTO `audit_field` VALUES (111,'textDataValue','Text Data Value',13);
INSERT INTO `audit_field` VALUES (112,'biospecimen','Biospecimen',13);
INSERT INTO `audit_field` VALUES (113,'customFieldDisplay','Custom Field Display',13);
INSERT INTO `audit_field` VALUES (114,'name','Name',14);
INSERT INTO `audit_field` VALUES (115,'quantity','Quantity',15);
INSERT INTO `audit_field` VALUES (116,'reason','Reason',15);
INSERT INTO `audit_field` VALUES (117,'recorder','Recorded By',15);
INSERT INTO `audit_field` VALUES (118,'transactionDate','Transaction Date',15);
INSERT INTO `audit_field` VALUES (119,'accessRequest','Request',15);
INSERT INTO `audit_field` VALUES (120,'biospecimen','Biospecimen',15);
INSERT INTO `audit_field` VALUES (121,'status','Status',15);
INSERT INTO `audit_field` VALUES (122,'unit','Unit',15);
INSERT INTO `audit_field` VALUES (123,'available','Available',16);
INSERT INTO `audit_field` VALUES (124,'capacity','Capacity',16);
INSERT INTO `audit_field` VALUES (125,'deleted','Deleted',16);
INSERT INTO `audit_field` VALUES (126,'name','Name',16);
INSERT INTO `audit_field` VALUES (127,'noofcol','Number Of Columns',16);
INSERT INTO `audit_field` VALUES (128,'noofrow','Number Of Rows',16);
INSERT INTO `audit_field` VALUES (129,'timestamp','Time Stamp',16);
INSERT INTO `audit_field` VALUES (130,'transferId','Transfer',16);
INSERT INTO `audit_field` VALUES (131,'type','Type',16);
INSERT INTO `audit_field` VALUES (132,'colnotype','Column Number Type',16);
INSERT INTO `audit_field` VALUES (133,'rownotype','Row Number Type',16);
INSERT INTO `audit_field` VALUES (134,'invRack','Rack',16);
INSERT INTO `audit_field` VALUES (135,'colno','Column Number',17);
INSERT INTO `audit_field` VALUES (136,'deleted','Deleted',17);
INSERT INTO `audit_field` VALUES (137,'rowno','Row Number',17);
INSERT INTO `audit_field` VALUES (138,'status','Status',17);
INSERT INTO `audit_field` VALUES (139,'timestamp','Time Stamp',17);
INSERT INTO `audit_field` VALUES (140,'biospecimen','Biospecimen',17);
INSERT INTO `audit_field` VALUES (141,'invBox','Box',17);
INSERT INTO `audit_field` VALUES (142,'available','Available',18);
INSERT INTO `audit_field` VALUES (143,'capacity','Capacity',18);
INSERT INTO `audit_field` VALUES (144,'commissiondate','Commission Date',18);
INSERT INTO `audit_field` VALUES (146,'deleted','Deleted',18);
INSERT INTO `audit_field` VALUES (149,'lastservicenote','Last Service Note',18);
INSERT INTO `audit_field` VALUES (150,'location','Location',18);
INSERT INTO `audit_field` VALUES (153,'timestamp','Time Stamp',18);
INSERT INTO `audit_field` VALUES (154,'invSite','Site',18);
INSERT INTO `audit_field` VALUES (157,'status','Status',18);
INSERT INTO `audit_field` VALUES (160,'name','Name',18);
INSERT INTO `audit_field` VALUES (162,'decommissiondate','Decommision Date',18);
INSERT INTO `audit_field` VALUES (164,'lastservicedate','Last Service Date',18);
INSERT INTO `audit_field` VALUES (165,'description','Description',18);
INSERT INTO `audit_field` VALUES (166,'siteFreezer','Site Freezer',18);
INSERT INTO `audit_field` VALUES (167,'invFreezer','Freezer',19);
INSERT INTO `audit_field` VALUES (168,'deleted','Deleted',19);
INSERT INTO `audit_field` VALUES (169,'name','Name',19);
INSERT INTO `audit_field` VALUES (170,'available','Available',19);
INSERT INTO `audit_field` VALUES (171,'description','Description',19);
INSERT INTO `audit_field` VALUES (172,'capacity','Capacity',19);
INSERT INTO `audit_field` VALUES (173,'siteFreezerRack','Site Freezer Rack',19);
INSERT INTO `audit_field` VALUES (174,'deleted','Deleted',20);
INSERT INTO `audit_field` VALUES (175,'contact','Contact',20);
INSERT INTO `audit_field` VALUES (176,'address','Address',20);
INSERT INTO `audit_field` VALUES (177,'name','Name',20);
INSERT INTO `audit_field` VALUES (178,'phone','Phone',20);
INSERT INTO `audit_field` VALUES (179,'ldapGroup','LDAP Group',20);
INSERT INTO `audit_field` VALUES (180,'description','Description',21);
INSERT INTO `audit_field` VALUES (181,'linkSubjectStudy','Link Subject Study',21);
INSERT INTO `audit_field` VALUES (182,'recordDate','Record Date',21);
INSERT INTO `audit_field` VALUES (183,'reviewedDate','Reviewed Date',21);
INSERT INTO `audit_field` VALUES (184,'reviewedBy','Reviewed By',21);
INSERT INTO `audit_field` VALUES (185,'questionnaire','Questionnaire',21);
INSERT INTO `audit_field` VALUES (186,'status','Status',21);
INSERT INTO `audit_field` VALUES (187,'customFieldDisplay','Custom Field Display',22);
INSERT INTO `audit_field` VALUES (188,'phenoCollection','Pheno Collection',22);
INSERT INTO `audit_field` VALUES (189,'dateDataValue','Date Data Value',22);
INSERT INTO `audit_field` VALUES (190,'errorDataValue','Error Data Value',22);
INSERT INTO `audit_field` VALUES (191,'numberDataValue','Number Data Value',22);
INSERT INTO `audit_field` VALUES (192,'textDataValue','Text Data Value',22);
INSERT INTO `audit_field` VALUES (193,'person','Person',23);
INSERT INTO `audit_field` VALUES (194,'addressLineOne','Address Line One',23);
INSERT INTO `audit_field` VALUES (195,'streetAddress','Street Address',23);
INSERT INTO `audit_field` VALUES (196,'postCode','Post Code',23);
INSERT INTO `audit_field` VALUES (197,'city','City',23);
INSERT INTO `audit_field` VALUES (198,'country','Country',23);
INSERT INTO `audit_field` VALUES (199,'state','State',23);
INSERT INTO `audit_field` VALUES (200,'otherState','Other State',23);
INSERT INTO `audit_field` VALUES (201,'addressStatus','Address Status',23);
INSERT INTO `audit_field` VALUES (202,'addressType','Address Type',23);
INSERT INTO `audit_field` VALUES (203,'dateReceived','Date Recieved',23);
INSERT INTO `audit_field` VALUES (204,'comments','Comments',23);
INSERT INTO `audit_field` VALUES (205,'preferredMailingAddress','Preferred Mailing Address',23);
INSERT INTO `audit_field` VALUES (206,'source','Source',23);
INSERT INTO `audit_field` VALUES (207,'arkRole','Ark Role',24);
INSERT INTO `audit_field` VALUES (208,'arkModule','Ark Module',24);
INSERT INTO `audit_field` VALUES (209,'arkUser','Ark User',24);
INSERT INTO `audit_field` VALUES (210,'study','Study',24);
INSERT INTO `audit_field` VALUES (211,'study','Study',25);
INSERT INTO `audit_field` VALUES (212,'linkSubjectStudy','Link Subject Study',25);
INSERT INTO `audit_field` VALUES (213,'studyComp','Study Component',25);
INSERT INTO `audit_field` VALUES (214,'studyComponentStatus','Study Component Status',25);
INSERT INTO `audit_field` VALUES (215,'consentStatus','Consent Status',25);
INSERT INTO `audit_field` VALUES (216,'consentType','Consent Type',25);
INSERT INTO `audit_field` VALUES (217,'consentDate','Consent Date',25);
INSERT INTO `audit_field` VALUES (218,'consentedBy','Consented By',25);
INSERT INTO `audit_field` VALUES (219,'comments','Comments',25);
INSERT INTO `audit_field` VALUES (220,'requestedDate','Requested Date',25);
INSERT INTO `audit_field` VALUES (221,'receivedDate','Recieved Date',25);
INSERT INTO `audit_field` VALUES (222,'completedDate','Completed Date',25);
INSERT INTO `audit_field` VALUES (223,'consentDownloaded','Consent Downloaded',25);
INSERT INTO `audit_field` VALUES (224,'lss','Link Subject Study',26);
INSERT INTO `audit_field` VALUES (225,'operator','Operator',26);
INSERT INTO `audit_field` VALUES (226,'date','Date',26);
INSERT INTO `audit_field` VALUES (227,'time','Time',26);
INSERT INTO `audit_field` VALUES (228,'reason','Reason',26);
INSERT INTO `audit_field` VALUES (229,'correspondenceModeType','Correspondence Mode Type',26);
INSERT INTO `audit_field` VALUES (230,'correspondenceDirectionType','Correspondence Direction Type',26);
INSERT INTO `audit_field` VALUES (231,'correspondenceOutcomeType','Correspondence Outcome Type',26);
INSERT INTO `audit_field` VALUES (232,'details','Details',26);
INSERT INTO `audit_field` VALUES (233,'comments','Comments',26);
INSERT INTO `audit_field` VALUES (234,'attachmentFilename','Attachment Filename',26);
INSERT INTO `audit_field` VALUES (235,'attachmentPayload','Attachment Payload',26);
INSERT INTO `audit_field` VALUES (236,'attachmentChecksum','Attachment Checksum',26);
INSERT INTO `audit_field` VALUES (237,'attachmentFileId','Attachment File ID',26);
INSERT INTO `audit_field` VALUES (238,'billableItem','BillableItem',26);
INSERT INTO `audit_field` VALUES (239,'name','Name',27);
INSERT INTO `audit_field` VALUES (240,'category','Category',27);
INSERT INTO `audit_field` VALUES (241,'description','Description',27);
INSERT INTO `audit_field` VALUES (242,'fieldType','Field Type',27);
INSERT INTO `audit_field` VALUES (243,'study','Study',27);
INSERT INTO `audit_field` VALUES (244,'arkFunction','Ark Function',27);
INSERT INTO `audit_field` VALUES (245,'unitType','Unit Type',27);
INSERT INTO `audit_field` VALUES (246,'minValue','Minimum Value',27);
INSERT INTO `audit_field` VALUES (247,'maxValue','Maximum Value',27);
INSERT INTO `audit_field` VALUES (248,'encodedValues','Encoded Values',27);
INSERT INTO `audit_field` VALUES (249,'missingValue','Missing Value',27);
INSERT INTO `audit_field` VALUES (250,'customFieldHasData','Custom Field Has Data',27);
INSERT INTO `audit_field` VALUES (251,'fieldLabel','Field Label',27);
INSERT INTO `audit_field` VALUES (252,'defaultValue','Default Value',27);
INSERT INTO `audit_field` VALUES (253,'unitTypeInText','Unit Type (In Text)',27);
INSERT INTO `audit_field` VALUES (254,'customField','Custom Field',28);
INSERT INTO `audit_field` VALUES (255,'customFieldGroup','Custom Field Group',28);
INSERT INTO `audit_field` VALUES (256,'required','Required',28);
INSERT INTO `audit_field` VALUES (257,'requiredMessage','Required Message',28);
INSERT INTO `audit_field` VALUES (258,'allowMultiselect','Allow Multiple Selections',28);
INSERT INTO `audit_field` VALUES (259,'sequence','Sequence',28);
INSERT INTO `audit_field` VALUES (260,'descriptiveNameIncludingCFGName','Descriptive Name',28);
INSERT INTO `audit_field` VALUES (261,'name','Name',29);
INSERT INTO `audit_field` VALUES (262,'description','Description',29);
INSERT INTO `audit_field` VALUES (263,'study','Study',29);
INSERT INTO `audit_field` VALUES (264,'published','Published',29);
INSERT INTO `audit_field` VALUES (265,'arkFunction','Ark Function',29);
INSERT INTO `audit_field` VALUES (266,'studyComp','Study Component',30);
INSERT INTO `audit_field` VALUES (267,'study','Study',30);
INSERT INTO `audit_field` VALUES (268,'studyCompStatus','Study Component Status',30);
INSERT INTO `audit_field` VALUES (269,'study','Study',31);
INSERT INTO `audit_field` VALUES (270,'relationship','Relationship',31);
INSERT INTO `audit_field` VALUES (271,'personBySubjectId','Person (by Subject ID)',31);
INSERT INTO `audit_field` VALUES (272,'personByContactId','Person (by Contact ID)',31);
INSERT INTO `audit_field` VALUES (273,'familyId','Family ID',31);
INSERT INTO `audit_field` VALUES (274,'study','Study',32);
INSERT INTO `audit_field` VALUES (275,'subjectStatus','Subject Status',32);
INSERT INTO `audit_field` VALUES (276,'person','Person',32);
INSERT INTO `audit_field` VALUES (277,'subjectUID','Subject UID',32);
INSERT INTO `audit_field` VALUES (278,'consentToActiveContact','Consent To Active Contact',32);
INSERT INTO `audit_field` VALUES (279,'consentToPassiveDataGathering','Consent To Passive Data Gathering',32);
INSERT INTO `audit_field` VALUES (280,'consentToUseData','Consent To Use Data',32);
INSERT INTO `audit_field` VALUES (281,'consentStatus','Consent Status',32);
INSERT INTO `audit_field` VALUES (282,'consentType','Consent Type',32);
INSERT INTO `audit_field` VALUES (283,'consentDate','Consent Date',32);
INSERT INTO `audit_field` VALUES (284,'heardAboutStudy','Heard About Study',32);
INSERT INTO `audit_field` VALUES (285,'comment','Comment',32);
INSERT INTO `audit_field` VALUES (286,'consentDownloaded','Consent Downloaded',32);
INSERT INTO `audit_field` VALUES (287,'updateConsent','Update Consent',32);
INSERT INTO `audit_field` VALUES (288,'studyComp','Study Component',33);
INSERT INTO `audit_field` VALUES (289,'study','Study',33);
INSERT INTO `audit_field` VALUES (290,'person','Person',33);
INSERT INTO `audit_field` VALUES (291,'studyComponentStatus','Study Component Status',33);
INSERT INTO `audit_field` VALUES (292,'person','Person',34);
INSERT INTO `audit_field` VALUES (293,'OtherID_Source','Other ID Source',34);
INSERT INTO `audit_field` VALUES (294,'otherID','Other ID',34);
INSERT INTO `audit_field` VALUES (295,'firstName','First Name',35);
INSERT INTO `audit_field` VALUES (296,'middleName','Middle Name',35);
INSERT INTO `audit_field` VALUES (297,'lastName','Last Name',35);
INSERT INTO `audit_field` VALUES (298,'preferredName','Preferred Name',35);
INSERT INTO `audit_field` VALUES (299,'genderType','Gender Type',35);
INSERT INTO `audit_field` VALUES (300,'vitalStatus','Vital Status',35);
INSERT INTO `audit_field` VALUES (301,'titleType','Title Type',35);
INSERT INTO `audit_field` VALUES (302,'maritalStatus','Marital Status',35);
INSERT INTO `audit_field` VALUES (303,'dateOfBirth','Date Of Birth',35);
INSERT INTO `audit_field` VALUES (304,'dateOfDeath','Date Of Death',35);
INSERT INTO `audit_field` VALUES (305,'causeOfDeath','Cause Of Death',35);
INSERT INTO `audit_field` VALUES (306,'personContactMethod','Contact Method',35);
INSERT INTO `audit_field` VALUES (307,'preferredEmail','Preferred Email',35);
INSERT INTO `audit_field` VALUES (308,'otherEmail','Other Email',35);
INSERT INTO `audit_field` VALUES (309,'preferredEmailStatus','Preferred Email Status',35);
INSERT INTO `audit_field` VALUES (310,'otherEmailStatus','Other Email Status',35);
INSERT INTO `audit_field` VALUES (311,'dateLastKnownAlive','Date Last Known Alive',35);
INSERT INTO `audit_field` VALUES (312,'person','Person',36);
INSERT INTO `audit_field` VALUES (313,'lastName','Last Name',36);
INSERT INTO `audit_field` VALUES (314,'phoneType','Phone Type',37);
INSERT INTO `audit_field` VALUES (315,'person','Person',37);
INSERT INTO `audit_field` VALUES (316,'phoneNumber','Phone Number',37);
INSERT INTO `audit_field` VALUES (317,'areaCode','Area Code',37);
INSERT INTO `audit_field` VALUES (318,'phoneStatus','Phone Status',37);
INSERT INTO `audit_field` VALUES (319,'source','Source',37);
INSERT INTO `audit_field` VALUES (320,'dateReceived','Date Recieved',37);
INSERT INTO `audit_field` VALUES (321,'silentMode','Silent Mode',37);
INSERT INTO `audit_field` VALUES (322,'comment','Comment',37);
INSERT INTO `audit_field` VALUES (323,'studyStatus','Study Status',38);
INSERT INTO `audit_field` VALUES (324,'name','Name',38);
INSERT INTO `audit_field` VALUES (325,'description','Description',38);
INSERT INTO `audit_field` VALUES (326,'dateOfApplication','Date Of Application',38);
INSERT INTO `audit_field` VALUES (327,'estimatedYearOfCompletion','Estimated Year Of Completion',38);
INSERT INTO `audit_field` VALUES (328,'chiefInvestigator','Chief Investigator',38);
INSERT INTO `audit_field` VALUES (329,'coInvestigator','Co-Investigator',38);
INSERT INTO `audit_field` VALUES (330,'contactPerson','Contact Person',38);
INSERT INTO `audit_field` VALUES (331,'contactPersonPhone','Contact Person Phone',38);
INSERT INTO `audit_field` VALUES (332,'ldapGroupName','LDAP Group Name',38);
INSERT INTO `audit_field` VALUES (333,'autoConsent','Automatic Consent',38);
INSERT INTO `audit_field` VALUES (334,'subStudyBiospecimenPrefix','Sub-Study Biospecimen Prefix',38);
INSERT INTO `audit_field` VALUES (335,'studyLogoBlob','Study Logo Blob',38);
INSERT INTO `audit_field` VALUES (336,'filename','Filename',38);
INSERT INTO `audit_field` VALUES (337,'autoGenerateSubjectUid','Auto Generate Subject UID',38);
INSERT INTO `audit_field` VALUES (338,'autoGenerateBiospecimenUid','Auto Generate Biospecimen UID',38);
INSERT INTO `audit_field` VALUES (339,'autoGenerateBiocollectionUid','Auto Generate Biocollection UID',38);
INSERT INTO `audit_field` VALUES (340,'subjectUidStart','Subject UID Start',38);
INSERT INTO `audit_field` VALUES (341,'subjectUidPrefix','Subject UID Prefix',38);
INSERT INTO `audit_field` VALUES (342,'subjectUidToken','Subject UID Token',38);
INSERT INTO `audit_field` VALUES (343,'subjectUidPadChar','Subject UID Padding Character',38);
INSERT INTO `audit_field` VALUES (344,'parentStudy','Parent Study',38);
INSERT INTO `audit_field` VALUES (345,'pedigreeConfiguration','Pedigree Configuration',38);
INSERT INTO `audit_field` VALUES (346,'study','Study',39);
INSERT INTO `audit_field` VALUES (347,'name','Name',39);
INSERT INTO `audit_field` VALUES (348,'description','Description',39);
INSERT INTO `audit_field` VALUES (349,'keyword','Keyword',39);
INSERT INTO `audit_field` VALUES (350,'name','Name',40);
INSERT INTO `audit_field` VALUES (351,'description','Description',40);
INSERT INTO `audit_field` VALUES (352,'study','Study',41);
INSERT INTO `audit_field` VALUES (353,'invSite','InvSite',41);
INSERT INTO `audit_field` VALUES (354,'linkSubjectStudy','Link Subject Study',42);
INSERT INTO `audit_field` VALUES (356,'customFieldDisplay','Custom Field Display',42);
INSERT INTO `audit_field` VALUES (357,'dateDataValue','Date Data Value',42);
INSERT INTO `audit_field` VALUES (358,'errorDataValue','Error Data Value',42);
INSERT INTO `audit_field` VALUES (359,'numberDataValue','Number Data Value',42);
INSERT INTO `audit_field` VALUES (360,'textDataValue','Text Data Value',42);
INSERT INTO `audit_field` VALUES (361,'description','Description',43);
INSERT INTO `audit_field` VALUES (362,'quantity','Quantity',43);
INSERT INTO `audit_field` VALUES (363,'commenceDate','Commence Date',43);
INSERT INTO `audit_field` VALUES (364,'type','Type',43);
INSERT INTO `audit_field` VALUES (365,'workRequest','Work Request',43);
INSERT INTO `audit_field` VALUES (366,'invoice','Invoice',43);
INSERT INTO `audit_field` VALUES (367,'studyId','Study ID',43);
INSERT INTO `audit_field` VALUES (368,'billableItemType','Billable Item Type',43);
INSERT INTO `audit_field` VALUES (369,'totalCost','Total Cost',43);
INSERT INTO `audit_field` VALUES (370,'itemCost','Item Cost',43);
INSERT INTO `audit_field` VALUES (371,'totalGST','Total GST',43);
INSERT INTO `audit_field` VALUES (372,'itemName','Item Name',44);
INSERT INTO `audit_field` VALUES (373,'description','Descripton',44);
INSERT INTO `audit_field` VALUES (374,'quantityPerUnit','Quantity Per Unit',44);
INSERT INTO `audit_field` VALUES (375,'unitPrice','Unit Price',44);
INSERT INTO `audit_field` VALUES (376,'billableItemTypeStatus','Billable Item Type Status',44);
INSERT INTO `audit_field` VALUES (377,'studyId','Study ID',44);
INSERT INTO `audit_field` VALUES (378,'type','Type',44);
INSERT INTO `audit_field` VALUES (379,'quantityType','Quantity Type',44);
INSERT INTO `audit_field` VALUES (380,'firstName','First Name',45);
INSERT INTO `audit_field` VALUES (381,'lastName','Last Name',45);
INSERT INTO `audit_field` VALUES (382,'organization','Organization',45);
INSERT INTO `audit_field` VALUES (383,'address','Address',45);
INSERT INTO `audit_field` VALUES (384,'researcherRole','Researcher Role',45);
INSERT INTO `audit_field` VALUES (385,'researcherStatus','Researcher Status',45);
INSERT INTO `audit_field` VALUES (386,'createdDate','Created Date',45);
INSERT INTO `audit_field` VALUES (387,'officePhone','Office Phone',45);
INSERT INTO `audit_field` VALUES (388,'mobile','Mobile',45);
INSERT INTO `audit_field` VALUES (389,'email','Email',45);
INSERT INTO `audit_field` VALUES (390,'fax','Fax',45);
INSERT INTO `audit_field` VALUES (391,'comment','Comment',45);
INSERT INTO `audit_field` VALUES (392,'billingType','Billing Type',45);
INSERT INTO `audit_field` VALUES (393,'accountNumber','Account Number',45);
INSERT INTO `audit_field` VALUES (394,'bsb','BSB',45);
INSERT INTO `audit_field` VALUES (396,'bank','Bank',45);
INSERT INTO `audit_field` VALUES (397,'accountName','Account Name',45);
INSERT INTO `audit_field` VALUES (398,'studyId','Study ID',45);
INSERT INTO `audit_field` VALUES (399,'titleType','Title Type',45);
INSERT INTO `audit_field` VALUES (400,'name','Name',46);
INSERT INTO `audit_field` VALUES (402,'description','Description',46);
INSERT INTO `audit_field` VALUES (403,'requestStatus','Request Status',46);
INSERT INTO `audit_field` VALUES (404,'researcher','Researcher',46);
INSERT INTO `audit_field` VALUES (405,'requestedDate','Requested Date',46);
INSERT INTO `audit_field` VALUES (406,'commencedDate','Commenced Date',46);
INSERT INTO `audit_field` VALUES (407,'studyId','Study ID',46);
INSERT INTO `audit_field` VALUES (408,'gst','GST',46);
INSERT INTO `audit_field` VALUES (409,'gstAllow','GST Allow',46);
INSERT INTO `audit_field` VALUES (410,'study','Study',47);
INSERT INTO `audit_field` VALUES (411,'studySite','Study Site',47);
INSERT INTO `audit_field` VALUES (412,'mainStudy','Main Study',48);
INSERT INTO `audit_field` VALUES (413,'subStudy','Sub-Study',48);
/*!40000 ALTER TABLE `audit_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_package`
--

DROP TABLE IF EXISTS `audit_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_package` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_package`
--

LOCK TABLES `audit_package` WRITE;
/*!40000 ALTER TABLE `audit_package` DISABLE KEYS */;
INSERT INTO `audit_package` VALUES (1,'Config');
INSERT INTO `audit_package` VALUES (2,'Disease');
INSERT INTO `audit_package` VALUES (3,'LIMS');
INSERT INTO `audit_package` VALUES (4,'Pheno');
INSERT INTO `audit_package` VALUES (5,'Study');
INSERT INTO `audit_package` VALUES (6,'Work Tracking');
/*!40000 ALTER TABLE `audit_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_history`
--

DROP TABLE IF EXISTS `consent_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) DEFAULT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  `CONSENT_STATUS_ID` int(11) NOT NULL,
  `CONSENT_TYPE_ID` int(11) NOT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENTED_BY` varchar(100) DEFAULT NULL,
  `COMMENTS` varchar(500) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `RECEIVED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_history`
--

LOCK TABLES `consent_history` WRITE;
/*!40000 ALTER TABLE `consent_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `consent_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lss_consent_history`
--

DROP TABLE IF EXISTS `lss_consent_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lss_consent_history` (
  `ID` int(1) NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_ID` int(11) DEFAULT NULL,
  `CONSENT_TYPE_ID` int(11) DEFAULT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lss_consent_history`
--

LOCK TABLES `lss_consent_history` WRITE;
/*!40000 ALTER TABLE `lss_consent_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `lss_consent_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `revchanges`
--

DROP TABLE IF EXISTS `revchanges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `revchanges` (
  `REV` int(11) NOT NULL,
  `ENTITYNAME` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `revchanges`
--

LOCK TABLES `revchanges` WRITE;
/*!40000 ALTER TABLE `revchanges` DISABLE KEYS */;
/*!40000 ALTER TABLE `revchanges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `revinfo`
--

DROP TABLE IF EXISTS `revinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `revinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `revinfo`
--

LOCK TABLES `revinfo` WRITE;
/*!40000 ALTER TABLE `revinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `revinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `config`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `config` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `config`;

--
-- Table structure for table `config_fields`
--

DROP TABLE IF EXISTS `config_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_fields` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` text NOT NULL,
  `DESCRIPTION` text NOT NULL,
  `TYPE` int(11) NOT NULL,
  `DEFAULT_VALUE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_fields`
--

LOCK TABLES `config_fields` WRITE;
/*!40000 ALTER TABLE `config_fields` DISABLE KEYS */;
INSERT INTO `config_fields` VALUES (1,'ROWS_PER_PAGE','Rows Per Page',2,'20');
INSERT INTO `config_fields` VALUES (2,'CUSTOM_FIELDS_PER_PAGE','Custom Fields Per Page',2,'20');
/*!40000 ALTER TABLE `config_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_config`
--

DROP TABLE IF EXISTS `user_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_config` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `VALUE` text NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`,`FIELD_ID`),
  KEY `FK_CONFIG_FIELD_ID` (`FIELD_ID`),
  CONSTRAINT `user_config_ibfk_1` FOREIGN KEY (`FIELD_ID`) REFERENCES `config_fields` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_config_ibfk_3` FOREIGN KEY (`USER_ID`) REFERENCES `study`.`ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_config`
--

LOCK TABLES `user_config` WRITE;
/*!40000 ALTER TABLE `user_config` DISABLE KEYS */;
INSERT INTO `user_config` VALUES (1,1,1,20);
INSERT INTO `user_config` VALUES (2,1,2,20);
/*!40000 ALTER TABLE `user_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `disease`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `disease` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `disease`;

--
-- Table structure for table `affection`
--

DROP TABLE IF EXISTS `affection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `affection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DISEASE_ID` int(11) NOT NULL,
  `LINKSUBJECTSTUDY_ID` int(11) NOT NULL,
  `AFFECTION_STATUS_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `RECORD_DATE` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DISEASE_ID` (`DISEASE_ID`),
  KEY `FK_LINKSUBJECTSTUDY_ID` (`LINKSUBJECTSTUDY_ID`),
  KEY `FK_AFFECTION_STATUS_ID` (`AFFECTION_STATUS_ID`),
  CONSTRAINT `FK_AFFECTION_STATUS_ID` FOREIGN KEY (`AFFECTION_STATUS_ID`) REFERENCES `affection_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_DISEASE_ID` FOREIGN KEY (`DISEASE_ID`) REFERENCES `disease` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINKSUBJECTSTUDY_ID` FOREIGN KEY (`LINKSUBJECTSTUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `affection`
--

LOCK TABLES `affection` WRITE;
/*!40000 ALTER TABLE `affection` DISABLE KEYS */;
/*!40000 ALTER TABLE `affection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `affection_custom_field_data`
--

DROP TABLE IF EXISTS `affection_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `affection_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AFFECTION_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UQ_SCFD_CFD_LSS` (`AFFECTION_ID`,`CUSTOM_FIELD_DISPLAY_ID`) USING BTREE,
  KEY `FK_CFD_LINK_SUBJECT_STUDY_ID` (`AFFECTION_ID`),
  KEY `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`AFFECTION_ID`) REFERENCES `affection` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `affection_custom_field_data`
--

LOCK TABLES `affection_custom_field_data` WRITE;
/*!40000 ALTER TABLE `affection_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `affection_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `affection_position`
--

DROP TABLE IF EXISTS `affection_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `affection_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `AFFECTION_ID` int(11) NOT NULL,
  `POSITION_ID` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `affection_position`
--

LOCK TABLES `affection_position` WRITE;
/*!40000 ALTER TABLE `affection_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `affection_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `affection_status`
--

DROP TABLE IF EXISTS `affection_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `affection_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `affection_status`
--

LOCK TABLES `affection_status` WRITE;
/*!40000 ALTER TABLE `affection_status` DISABLE KEYS */;
INSERT INTO `affection_status` VALUES (1,'Yes');
INSERT INTO `affection_status` VALUES (2,'No');
INSERT INTO `affection_status` VALUES (3,'Carrier');
/*!40000 ALTER TABLE `affection_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disease`
--

DROP TABLE IF EXISTS `disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disease` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `CUSTOM_FIELD_GROUP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `study`.`custom_field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disease`
--

LOCK TABLES `disease` WRITE;
/*!40000 ALTER TABLE `disease` DISABLE KEYS */;
/*!40000 ALTER TABLE `disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disease_custom_fields`
--

DROP TABLE IF EXISTS `disease_custom_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disease_custom_fields` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_ID` int(11) NOT NULL,
  `DISEASE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CUSTOM_F_ID` (`CUSTOM_FIELD_ID`),
  KEY `FK_DISEASE_D_ID` (`DISEASE_ID`),
  CONSTRAINT `FK_CUSTOM_F_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `study`.`custom_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_DISEASE_D_ID` FOREIGN KEY (`DISEASE_ID`) REFERENCES `disease` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disease_custom_fields`
--

LOCK TABLES `disease_custom_fields` WRITE;
/*!40000 ALTER TABLE `disease_custom_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `disease_custom_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gene`
--

DROP TABLE IF EXISTS `gene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  CONSTRAINT `FK_GENE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gene`
--

LOCK TABLES `gene` WRITE;
/*!40000 ALTER TABLE `gene` DISABLE KEYS */;
/*!40000 ALTER TABLE `gene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gene_disease`
--

DROP TABLE IF EXISTS `gene_disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene_disease` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GENE_ID` int(11) NOT NULL,
  `DISEASE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_GENE_ID` (`GENE_ID`),
  KEY `FK_DISEASE_ID` (`DISEASE_ID`),
  CONSTRAINT `FK_DISEASE_G_ID` FOREIGN KEY (`DISEASE_ID`) REFERENCES `disease` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_GENE_D_ID` FOREIGN KEY (`GENE_ID`) REFERENCES `gene` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gene_disease`
--

LOCK TABLES `gene_disease` WRITE;
/*!40000 ALTER TABLE `gene_disease` DISABLE KEYS */;
/*!40000 ALTER TABLE `gene_disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mutation`
--

DROP TABLE IF EXISTS `mutation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mutation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINKSUBJECTSTUDY_ID` int(11) NOT NULL,
  `DISEASE_ID` int(11) NOT NULL,
  `SCREENED` tinyint(1) NOT NULL,
  `TESTED` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DISEASE_ID` (`DISEASE_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_ID` (`LINKSUBJECTSTUDY_ID`),
  CONSTRAINT `FK_MUT_DISEASE_ID` FOREIGN KEY (`DISEASE_ID`) REFERENCES `disease` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_MUT_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINKSUBJECTSTUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mutation`
--

LOCK TABLES `mutation` WRITE;
/*!40000 ALTER TABLE `mutation` DISABLE KEYS */;
/*!40000 ALTER TABLE `mutation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GENE_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_GENE_ID` (`GENE_ID`),
  CONSTRAINT `FK_POS_GENE_ID` FOREIGN KEY (`GENE_ID`) REFERENCES `gene` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `geno`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `geno` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `geno`;

--
-- Table structure for table `command`
--

DROP TABLE IF EXISTS `command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `command` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `LOCATION` varchar(255) DEFAULT NULL,
  `SERVER_URL` varchar(255) DEFAULT NULL,
  `INPUT_FILE_FORMAT` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_FORMAT` varchar(255) DEFAULT NULL COMMENT 'may need error output too i guess or alt output',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `command`
--

LOCK TABLES `command` WRITE;
/*!40000 ALTER TABLE `command` DISABLE KEYS */;
/*!40000 ALTER TABLE `command` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lss_pipeline`
--

DROP TABLE IF EXISTS `lss_pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lss_pipeline` (
  `ID` int(11) NOT NULL,
  `PIPELINE_ID` int(11) NOT NULL,
  `LSS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_lss_pipeline_pipeline_idx` (`PIPELINE_ID`),
  KEY `fk_lss_pipeline_lss_idx` (`LSS_ID`),
  CONSTRAINT `fk_lss_pipeline_lss` FOREIGN KEY (`LSS_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lss_pipeline_pipeline` FOREIGN KEY (`PIPELINE_ID`) REFERENCES `pipeline` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lss_pipeline`
--

LOCK TABLES `lss_pipeline` WRITE;
/*!40000 ALTER TABLE `lss_pipeline` DISABLE KEYS */;
/*!40000 ALTER TABLE `lss_pipeline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pipeline`
--

DROP TABLE IF EXISTS `pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pipeline` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pipeline`
--

LOCK TABLES `pipeline` WRITE;
/*!40000 ALTER TABLE `pipeline` DISABLE KEYS */;
/*!40000 ALTER TABLE `pipeline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pipeline_template`
--

DROP TABLE IF EXISTS `pipeline_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pipeline_template` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  `IS_TEMPLATE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pipeline_template`
--

LOCK TABLES `pipeline_template` WRITE;
/*!40000 ALTER TABLE `pipeline_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `pipeline_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process`
--

DROP TABLE IF EXISTS `process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PIPELINE_ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  `COMMAND_ID` int(11) DEFAULT NULL COMMENT 'The command is the task/program that will perform the process/transform',
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_process_command_idx` (`COMMAND_ID`),
  KEY `fk_process_pipeline_idx` (`PIPELINE_ID`),
  CONSTRAINT `fk_process_command` FOREIGN KEY (`COMMAND_ID`) REFERENCES `command` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_process_pipeline` FOREIGN KEY (`PIPELINE_ID`) REFERENCES `lss_pipeline` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process`
--

LOCK TABLES `process` WRITE;
/*!40000 ALTER TABLE `process` DISABLE KEYS */;
/*!40000 ALTER TABLE `process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_input`
--

DROP TABLE IF EXISTS `process_input`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_input` (
  `ID` int(11) NOT NULL,
  `PROCESS_ID` int(11) NOT NULL,
  `INPUT_FILE_LOCATION` varchar(255) DEFAULT NULL,
  `INPUT_FILE_HASH` varchar(255) DEFAULT NULL,
  `INPUT_FILE_TYPE` varchar(255) DEFAULT NULL,
  `INPUT_KEPT` tinyint(1) DEFAULT NULL,
  `INPUT_SERVER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_process_input_process_idx` (`PROCESS_ID`),
  CONSTRAINT `fk_process_input_process` FOREIGN KEY (`PROCESS_ID`) REFERENCES `process` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_input`
--

LOCK TABLES `process_input` WRITE;
/*!40000 ALTER TABLE `process_input` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_input` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_output`
--

DROP TABLE IF EXISTS `process_output`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_output` (
  `ID` int(11) NOT NULL,
  `PROCESS_ID` int(11) NOT NULL,
  `OUTPUT_FILE_LOCATION` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_HASH` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_TYPE` varchar(255) DEFAULT NULL,
  `OUTPUT_KEPT` tinyint(1) DEFAULT NULL,
  `OUTPUT_SERVER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_process_output_process_idx` (`PROCESS_ID`),
  CONSTRAINT `fk_process_output_process` FOREIGN KEY (`PROCESS_ID`) REFERENCES `process` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_output`
--

LOCK TABLES `process_output` WRITE;
/*!40000 ALTER TABLE `process_output` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_output` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `lims`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `lims` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `lims`;

--
-- Table structure for table `access_request`
--

DROP TABLE IF EXISTS `access_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `REQUEST_DATE` datetime NOT NULL,
  `REQUIRED_DATE` datetime DEFAULT NULL,
  `COMMENTS` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `access_request`
--

LOCK TABLES `access_request` WRITE;
/*!40000 ALTER TABLE `access_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `access_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `barcode_label`
--

DROP TABLE IF EXISTS `barcode_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `BARCODE_PRINTER_ID` int(11) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `LABEL_PREFIX` text NOT NULL,
  `LABEL_SUFFIX` text NOT NULL,
  `VERSION` int(11) NOT NULL,
  `BARCODE_PRINTER_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_label_study` (`STUDY_ID`),
  KEY `fk_barcode_label_printer` (`BARCODE_PRINTER_ID`),
  KEY `fk_barcode_label_barcode_printer` (`BARCODE_PRINTER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode_label`
--

LOCK TABLES `barcode_label` WRITE;
/*!40000 ALTER TABLE `barcode_label` DISABLE KEYS */;
/*!40000 ALTER TABLE `barcode_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `barcode_label_data`
--

DROP TABLE IF EXISTS `barcode_label_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BARCODE_LABEL_ID` int(11) NOT NULL,
  `COMMAND` varchar(50) NOT NULL,
  `X_COORD` int(11) NOT NULL,
  `Y_COORD` int(11) NOT NULL,
  `P1` varchar(50) DEFAULT NULL,
  `P2` varchar(50) DEFAULT NULL,
  `P3` varchar(50) DEFAULT NULL,
  `P4` varchar(50) DEFAULT NULL,
  `P5` varchar(50) DEFAULT NULL,
  `P6` varchar(50) DEFAULT NULL,
  `P7` varchar(50) DEFAULT NULL,
  `P8` varchar(50) DEFAULT NULL,
  `QUOTE_LEFT` varchar(5) DEFAULT NULL,
  `DATA` varchar(50) DEFAULT NULL,
  `QUOTE_RIGHT` varchar(5) DEFAULT NULL,
  `LINE_FEED` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_label_data_label` (`BARCODE_LABEL_ID`),
  KEY `fk_barcode_label_data_1` (`BARCODE_LABEL_ID`),
  CONSTRAINT `fk_barcode_label_data_1` FOREIGN KEY (`BARCODE_LABEL_ID`) REFERENCES `barcode_label` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode_label_data`
--

LOCK TABLES `barcode_label_data` WRITE;
/*!40000 ALTER TABLE `barcode_label_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `barcode_label_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bio_sampletype`
--

DROP TABLE IF EXISTS `bio_sampletype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bio_sampletype` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL DEFAULT '0',
  `SAMPLETYPE` varchar(50) DEFAULT NULL,
  `SAMPLESUBTYPE` varchar(50) DEFAULT NULL,
  `ORDER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bio_sampletype`
--

LOCK TABLES `bio_sampletype` WRITE;
/*!40000 ALTER TABLE `bio_sampletype` DISABLE KEYS */;
INSERT INTO `bio_sampletype` VALUES (1,'Blood / EDTA-purple top (10mL)','Blood','Blood',1);
INSERT INTO `bio_sampletype` VALUES (2,'Blood / EDTA-purple or pink top (5mL)','Blood','Blood',2);
INSERT INTO `bio_sampletype` VALUES (3,'Blood / PAXrna tube','Blood','Blood',3);
INSERT INTO `bio_sampletype` VALUES (4,'Blood / Heparin-Green top','Blood','Blood',4);
INSERT INTO `bio_sampletype` VALUES (5,'Blood / Gel separator tube - Red or White top','Blood','Blood',5);
INSERT INTO `bio_sampletype` VALUES (6,'Saliva / Oragene Kit (for DNA)','Saliva','Saliva',6);
INSERT INTO `bio_sampletype` VALUES (7,'Saliva / for Metagenomics (NOT Oragene)','Saliva','Saliva',7);
INSERT INTO `bio_sampletype` VALUES (8,'Urine sample','Tissue','Urine',8);
INSERT INTO `bio_sampletype` VALUES (9,'Stool sample','Tissue','Stol',9);
INSERT INTO `bio_sampletype` VALUES (10,'Skin Biopsy','Tissue','Skin',10);
INSERT INTO `bio_sampletype` VALUES (11,'Tears','Tissue','Tears',11);
INSERT INTO `bio_sampletype` VALUES (12,'Tissue - Vitreous','Tissue','Vitreous',12);
INSERT INTO `bio_sampletype` VALUES (13,'Tissue - Aqueous','Tissue','Aqueous',13);
INSERT INTO `bio_sampletype` VALUES (20,'Plasma - separated neat','Blood','Plasma',20);
INSERT INTO `bio_sampletype` VALUES (21,'Plasma - separated Protease inhibitor','Blood','Plasma',21);
INSERT INTO `bio_sampletype` VALUES (22,'Plasma - separated RNA inhibitor','Blood','Plasma',22);
INSERT INTO `bio_sampletype` VALUES (23,'Serum - separated neat','Blood','Serum',23);
INSERT INTO `bio_sampletype` VALUES (24,'Serum - separated Protease inhibitor','Blood','Serum',24);
INSERT INTO `bio_sampletype` VALUES (25,'Serum - separated RNA inhibitor','Blood','Serum',25);
INSERT INTO `bio_sampletype` VALUES (26,'Isolated CD4 cells','Blood','Blood cells',26);
INSERT INTO `bio_sampletype` VALUES (27,'Isolated CD8 cells','Blood','Blood cells',27);
INSERT INTO `bio_sampletype` VALUES (28,'Isolated WBCs','Blood','Blood cells',28);
INSERT INTO `bio_sampletype` VALUES (30,'extracted DNA','DNA','DNA',30);
INSERT INTO `bio_sampletype` VALUES (31,'extracted Protein','Protein','Protein',31);
INSERT INTO `bio_sampletype` VALUES (32,'extracted RNA','RNA','RNA',32);
INSERT INTO `bio_sampletype` VALUES (50,'Tissue - Globe','Tissue','Globe',50);
INSERT INTO `bio_sampletype` VALUES (51,'Tissue - Orbital Fat','Tissue','Orbital Fat',51);
INSERT INTO `bio_sampletype` VALUES (52,'Tissue - Iris','Tissue','Iris',52);
INSERT INTO `bio_sampletype` VALUES (53,'Tissue - Lens','Tissue','Lens',53);
INSERT INTO `bio_sampletype` VALUES (54,'Tissue - Pterygium','Tissue','Pterygium',54);
INSERT INTO `bio_sampletype` VALUES (55,'Tissue - RPE / Choroid','Tissue','RPE/Choroid',55);
INSERT INTO `bio_sampletype` VALUES (56,'Tissue - Neurosensory Retina','Tissue','Retina',56);
INSERT INTO `bio_sampletype` VALUES (57,'Serum - Blood / Gel separator tube - Red or Gold  ','Blood','Bood',58);
INSERT INTO `bio_sampletype` VALUES (99,'Fibroblasts','Cells','Fibroblast',99);
INSERT INTO `bio_sampletype` VALUES (100,'Embryoid bodies','Cells','Embryoid bodies',100);
INSERT INTO `bio_sampletype` VALUES (101,'iPSC','Cells','iPSC',101);
INSERT INTO `bio_sampletype` VALUES (102,'Derived Cardiomyoctyes','Cells','Cardiomyocytes',102);
INSERT INTO `bio_sampletype` VALUES (103,'Derived Lens','Cells','Lens',103);
INSERT INTO `bio_sampletype` VALUES (104,'Derived Neurons','Cells','Neurons',104);
INSERT INTO `bio_sampletype` VALUES (105,'Derived RGC','Cells','RGCs',105);
INSERT INTO `bio_sampletype` VALUES (106,'Derived RPE','Cells','RPE',106);
INSERT INTO `bio_sampletype` VALUES (199,'Tissue - FFPE sample','Tissue','FFPE',199);
INSERT INTO `bio_sampletype` VALUES (200,'Other','Other','Other',200);
INSERT INTO `bio_sampletype` VALUES (201,'Blood / Buffy Coat','Blood','Blood',6);
INSERT INTO `bio_sampletype` VALUES (202,'Tissue - Unspecified','Tissue','Unspecified',57);
INSERT INTO `bio_sampletype` VALUES (203,'Tissue - Optic Nerve','Tissue','Optic Nerve',56);
/*!40000 ALTER TABLE `bio_sampletype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bio_transaction`
--

DROP TABLE IF EXISTS `bio_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bio_transaction` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `TRANSACTION_DATE` datetime DEFAULT NULL,
  `QUANTITY` decimal(16,10) DEFAULT NULL,
  `RECORDER` varchar(255) DEFAULT NULL,
  `REASON` text,
  `STATUS_ID` int(11) DEFAULT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  `UNIT_ID` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_BIOTRANSACTION_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  KEY `FK_BIOTRANSACTION_STATUS_ID` (`STATUS_ID`),
  KEY `FK_BIOTRANSACTION_REQUEST_ID` (`REQUEST_ID`),
  KEY `UNIT_ID` (`UNIT_ID`),
  CONSTRAINT `FK_BIOTRANSACTION_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_REQUEST_ID` FOREIGN KEY (`REQUEST_ID`) REFERENCES `access_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_STATUS_ID` FOREIGN KEY (`STATUS_ID`) REFERENCES `bio_transaction_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bio_transaction`
--

LOCK TABLES `bio_transaction` WRITE;
/*!40000 ALTER TABLE `bio_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `bio_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bio_transaction_status`
--

DROP TABLE IF EXISTS `bio_transaction_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bio_transaction_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bio_transaction_status`
--

LOCK TABLES `bio_transaction_status` WRITE;
/*!40000 ALTER TABLE `bio_transaction_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `bio_transaction_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollection`
--

DROP TABLE IF EXISTS `biocollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOCOLLECTION_UID` varchar(50) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `COLLECTIONDATE` datetime DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `COMMENTS` text,
  `HOSPITAL` varchar(50) DEFAULT NULL,
  `SURGERYDATE` datetime DEFAULT NULL,
  `DIAG_CATEGORY` text,
  `REF_DOCTOR` varchar(50) DEFAULT NULL,
  `PATIENTAGE` int(11) DEFAULT NULL,
  `DISCHARGEDATE` datetime DEFAULT NULL,
  `HOSPITAL_UR` varchar(50) DEFAULT NULL,
  `DIAG_DATE` datetime DEFAULT NULL,
  `COLLECTIONGROUP_ID` int(11) DEFAULT NULL,
  `EPISODE_NUM` varchar(50) DEFAULT NULL,
  `EPISODE_DESC` varchar(50) DEFAULT NULL,
  `COLLECTIONGROUP` varchar(50) DEFAULT NULL,
  `TISSUETYPE` varchar(50) DEFAULT NULL,
  `TISSUECLASS` varchar(50) DEFAULT NULL,
  `PATHLABNO` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_collection_name_idx` (`NAME`),
  KEY `fk_collection_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_collection_study` (`STUDY_ID`),
  KEY `fk_collection_biocollection_uid_idx` (`BIOCOLLECTION_UID`),
  CONSTRAINT `fk_collection_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollection`
--

LOCK TABLES `biocollection` WRITE;
/*!40000 ALTER TABLE `biocollection` DISABLE KEYS */;
/*!40000 ALTER TABLE `biocollection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollection_custom_field_data`
--

DROP TABLE IF EXISTS `biocollection_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIO_COLLECTION_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY (`ID`),
  KEY `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` (`BIO_COLLECTION_ID`),
  KEY `FK_BIOCOLCFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` FOREIGN KEY (`BIO_COLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOCOLCFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollection_custom_field_data`
--

LOCK TABLES `biocollection_custom_field_data` WRITE;
/*!40000 ALTER TABLE `biocollection_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `biocollection_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollectionuid_padchar`
--

DROP TABLE IF EXISTS `biocollectionuid_padchar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_padchar` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollectionuid_padchar`
--

LOCK TABLES `biocollectionuid_padchar` WRITE;
/*!40000 ALTER TABLE `biocollectionuid_padchar` DISABLE KEYS */;
INSERT INTO `biocollectionuid_padchar` VALUES (1,'1');
INSERT INTO `biocollectionuid_padchar` VALUES (2,'2');
INSERT INTO `biocollectionuid_padchar` VALUES (3,'3');
INSERT INTO `biocollectionuid_padchar` VALUES (4,'4');
INSERT INTO `biocollectionuid_padchar` VALUES (5,'5');
INSERT INTO `biocollectionuid_padchar` VALUES (6,'6');
INSERT INTO `biocollectionuid_padchar` VALUES (7,'7');
INSERT INTO `biocollectionuid_padchar` VALUES (8,'8');
INSERT INTO `biocollectionuid_padchar` VALUES (9,'9');
INSERT INTO `biocollectionuid_padchar` VALUES (10,'10');
/*!40000 ALTER TABLE `biocollectionuid_padchar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollectionuid_sequence`
--

DROP TABLE IF EXISTS `biocollectionuid_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollectionuid_sequence`
--

LOCK TABLES `biocollectionuid_sequence` WRITE;
/*!40000 ALTER TABLE `biocollectionuid_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `biocollectionuid_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollectionuid_template`
--

DROP TABLE IF EXISTS `biocollectionuid_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `BIOCOLLECTIONUID_PREFIX` varchar(45) DEFAULT NULL,
  `BIOCOLLECTIONUID_TOKEN_ID` int(11) DEFAULT NULL,
  `BIOCOLLECTIONUID_PADCHAR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `STUDY_ID_UNIQUE` (`STUDY_ID`),
  KEY `fk_biocollectionuid_template_study` (`STUDY_ID`),
  CONSTRAINT `fk_biocollectionuid_template_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollectionuid_template`
--

LOCK TABLES `biocollectionuid_template` WRITE;
/*!40000 ALTER TABLE `biocollectionuid_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `biocollectionuid_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollectionuid_token`
--

DROP TABLE IF EXISTS `biocollectionuid_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollectionuid_token`
--

LOCK TABLES `biocollectionuid_token` WRITE;
/*!40000 ALTER TABLE `biocollectionuid_token` DISABLE KEYS */;
INSERT INTO `biocollectionuid_token` VALUES (1,'-');
INSERT INTO `biocollectionuid_token` VALUES (2,'@');
INSERT INTO `biocollectionuid_token` VALUES (3,'#');
INSERT INTO `biocollectionuid_token` VALUES (4,':');
INSERT INTO `biocollectionuid_token` VALUES (5,'*');
INSERT INTO `biocollectionuid_token` VALUES (6,'|');
INSERT INTO `biocollectionuid_token` VALUES (7,'_');
INSERT INTO `biocollectionuid_token` VALUES (8,'+');
/*!40000 ALTER TABLE `biocollectionuid_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen`
--

DROP TABLE IF EXISTS `biospecimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_UID` varchar(50) CHARACTER SET latin1 NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `SAMPLETYPE_ID` int(11) NOT NULL,
  `BIOCOLLECTION_ID` int(11) NOT NULL,
  `SUBSTUDY_ID` int(11) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `PARENTID` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  `OLDPARENT_ID` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `OTHERID` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `BIOSPECIMEN_STORAGE_ID` int(11) DEFAULT NULL,
  `SAMPLE_TIME` time DEFAULT NULL,
  `PROCESSED_DATE` datetime DEFAULT NULL,
  `SAMPLE_DATE` datetime DEFAULT NULL,
  `SAMPLETYPE` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `SAMPLESUBTYPE` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `PROCESSED_TIME` time DEFAULT NULL,
  `DEPTH` int(11) DEFAULT '1',
  `BIOSPECIMEN_GRADE_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_SPECIES_ID` int(11) DEFAULT '1',
  `QTY_COLLECTED` double DEFAULT NULL,
  `QTY_REMOVED` double DEFAULT NULL,
  `COMMENTS` text CHARACTER SET latin1,
  `QUANTITY` decimal(16,10) DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT '0',
  `TREATMENT_TYPE_ID` int(11) NOT NULL,
  `BARCODED` tinyint(1) NOT NULL DEFAULT '0',
  `BIOSPECIMEN_QUALITY_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_STATUS_ID` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `CONCENTRATION` float DEFAULT NULL,
  `BIOSPECIMEN_PROTOCOL_ID` int(11) DEFAULT NULL,
  `PURITY` float DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_UID`,`STUDY_ID`,`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_biospecimen_study` (`STUDY_ID`),
  KEY `fk_biospecimen_treatment_type_id` (`TREATMENT_TYPE_ID`),
  KEY `fk_biospecimen_unit` (`UNIT_ID`),
  KEY `fk_biospecimen_quality` (`BIOSPECIMEN_QUALITY_ID`),
  KEY `fk_biospecimen_anticoagulant` (`BIOSPECIMEN_ANTICOAGULANT_ID`),
  KEY `fk_biospecimen_status` (`BIOSPECIMEN_STATUS_ID`),
  KEY `fk_biospecimen_storage` (`BIOSPECIMEN_STORAGE_ID`) USING BTREE,
  KEY `fk_biospecimen_species` (`BIOSPECIMEN_SPECIES_ID`) USING BTREE,
  KEY `fk_biospecimen_biocollection` (`BIOCOLLECTION_ID`),
  KEY `fk_biospecimen_biospecimen` (`PARENT_ID`),
  KEY `fk_biospecimen_parent_id` (`PARENT_ID`) USING BTREE,
  KEY `fk_biospecimen_old_id` (`OLD_ID`) USING BTREE,
  KEY `fk_biospecimen_subject` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  KEY `fk_biospecimen_protocol` (`BIOSPECIMEN_PROTOCOL_ID`),
  KEY `fk_biospecimen_sampletype_idx` (`SAMPLETYPE_ID`),
  CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biocollection` FOREIGN KEY (`BIOCOLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biospecimen` FOREIGN KEY (`PARENT_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_protocol` FOREIGN KEY (`BIOSPECIMEN_PROTOCOL_ID`) REFERENCES `biospecimen_protocol` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_sampletype` FOREIGN KEY (`SAMPLETYPE_ID`) REFERENCES `bio_sampletype` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_biospecimen_treatment_type_id` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen`
--

LOCK TABLES `biospecimen` WRITE;
/*!40000 ALTER TABLE `biospecimen` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_anticoagulant`
--

DROP TABLE IF EXISTS `biospecimen_anticoagulant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_anticoagulant` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_anticoagulant`
--

LOCK TABLES `biospecimen_anticoagulant` WRITE;
/*!40000 ALTER TABLE `biospecimen_anticoagulant` DISABLE KEYS */;
INSERT INTO `biospecimen_anticoagulant` VALUES (1,'N/A');
INSERT INTO `biospecimen_anticoagulant` VALUES (2,'EDTA');
INSERT INTO `biospecimen_anticoagulant` VALUES (3,'Lithium Heparin');
INSERT INTO `biospecimen_anticoagulant` VALUES (4,'Sodium Citrate');
INSERT INTO `biospecimen_anticoagulant` VALUES (5,'ACD');
/*!40000 ALTER TABLE `biospecimen_anticoagulant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_copy`
--

DROP TABLE IF EXISTS `biospecimen_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_copy` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_UID` varchar(50) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `SAMPLETYPE_ID` int(11) NOT NULL,
  `BIOCOLLECTION_ID` int(11) NOT NULL,
  `SUBSTUDY_ID` int(11) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `PARENTID` varchar(50) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  `OLDPARENT_ID` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `OTHERID` varchar(50) DEFAULT NULL,
  `BIOSPECIMEN_STORAGE_ID` int(11) DEFAULT NULL,
  `SAMPLE_TIME` time DEFAULT NULL,
  `PROCESSED_DATE` datetime DEFAULT NULL,
  `SAMPLE_DATE` datetime DEFAULT NULL,
  `SAMPLETYPE` varchar(255) DEFAULT NULL,
  `SAMPLESUBTYPE` varchar(255) DEFAULT NULL,
  `PROCESSED_TIME` time DEFAULT NULL,
  `DEPTH` int(11) DEFAULT '1',
  `BIOSPECIMEN_GRADE_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_SPECIES_ID` int(11) DEFAULT '1',
  `QTY_COLLECTED` double DEFAULT NULL,
  `QTY_REMOVED` double DEFAULT NULL,
  `COMMENTS` text,
  `QUANTITY` decimal(16,10) DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT '0',
  `TREATMENT_TYPE_ID` int(11) NOT NULL,
  `BARCODED` tinyint(1) NOT NULL DEFAULT '0',
  `BIOSPECIMEN_QUALITY_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_STATUS_ID` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `CONCENTRATION` float DEFAULT NULL,
  `BIOSPECIMEN_PROTOCOL_ID` int(11) DEFAULT NULL,
  `PURITY` float DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_study` (`STUDY_ID`),
  KEY `fk_biospecimen_treatment_type_id` (`TREATMENT_TYPE_ID`),
  KEY `fk_biospecimen_unit` (`UNIT_ID`),
  KEY `fk_biospecimen_quality` (`BIOSPECIMEN_QUALITY_ID`),
  KEY `fk_biospecimen_anticoagulant` (`BIOSPECIMEN_ANTICOAGULANT_ID`),
  KEY `fk_biospecimen_status` (`BIOSPECIMEN_STATUS_ID`),
  KEY `fk_biospecimen_storage` (`BIOSPECIMEN_STORAGE_ID`) USING BTREE,
  KEY `fk_biospecimen_species` (`BIOSPECIMEN_SPECIES_ID`) USING BTREE,
  KEY `fk_biospecimen_biocollection` (`BIOCOLLECTION_ID`),
  KEY `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_UID`),
  KEY `fk_biospecimen_biospecimen` (`PARENT_ID`),
  KEY `fk_biospecimen_parent_id` (`PARENT_ID`) USING BTREE,
  KEY `fk_biospecimen_old_id` (`OLD_ID`) USING BTREE,
  KEY `fk_biospecimen_subject` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  KEY `fk_biospecimen_protocol` (`BIOSPECIMEN_PROTOCOL_ID`),
  KEY `fk_biospecimen_sampletype_idx` (`SAMPLETYPE_ID`),
  CONSTRAINT `biospecimen_copy_ibfk_1` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_10` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `biospecimen_copy_ibfk_11` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_12` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `biospecimen_copy_ibfk_2` FOREIGN KEY (`BIOCOLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_3` FOREIGN KEY (`PARENT_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_4` FOREIGN KEY (`BIOSPECIMEN_PROTOCOL_ID`) REFERENCES `biospecimen_protocol` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_5` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_6` FOREIGN KEY (`SAMPLETYPE_ID`) REFERENCES `bio_sampletype` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_7` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_8` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `biospecimen_copy_ibfk_9` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_copy`
--

LOCK TABLES `biospecimen_copy` WRITE;
/*!40000 ALTER TABLE `biospecimen_copy` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimen_copy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_custom_field_data`
--

DROP TABLE IF EXISTS `biospecimen_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FK_BIOSPECFDATA_BIOSPECIMENID_CFDID` (`BIOSPECIMEN_ID`,`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_BIOSPECFDATA_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_custom_field_data`
--

LOCK TABLES `biospecimen_custom_field_data` WRITE;
/*!40000 ALTER TABLE `biospecimen_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimen_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_grade`
--

DROP TABLE IF EXISTS `biospecimen_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_grade` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_grade`
--

LOCK TABLES `biospecimen_grade` WRITE;
/*!40000 ALTER TABLE `biospecimen_grade` DISABLE KEYS */;
INSERT INTO `biospecimen_grade` VALUES (1,'Extracted');
INSERT INTO `biospecimen_grade` VALUES (2,'Precipitated');
INSERT INTO `biospecimen_grade` VALUES (3,'Immediate');
INSERT INTO `biospecimen_grade` VALUES (4,'Delay < 1 hr');
INSERT INTO `biospecimen_grade` VALUES (5,'Delay < 1  ');
INSERT INTO `biospecimen_grade` VALUES (6,'N/A');
INSERT INTO `biospecimen_grade` VALUES (7,'Undiffrentiated');
INSERT INTO `biospecimen_grade` VALUES (9,'Not stated');
INSERT INTO `biospecimen_grade` VALUES (10,'Poor');
INSERT INTO `biospecimen_grade` VALUES (18,'Undiffrent');
/*!40000 ALTER TABLE `biospecimen_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_protocol`
--

DROP TABLE IF EXISTS `biospecimen_protocol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_protocol` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_protocol`
--

LOCK TABLES `biospecimen_protocol` WRITE;
/*!40000 ALTER TABLE `biospecimen_protocol` DISABLE KEYS */;
INSERT INTO `biospecimen_protocol` VALUES (1,'Unknown');
INSERT INTO `biospecimen_protocol` VALUES (2,'Qiagen');
INSERT INTO `biospecimen_protocol` VALUES (3,'RNA');
INSERT INTO `biospecimen_protocol` VALUES (4,'Oragene');
INSERT INTO `biospecimen_protocol` VALUES (5,'Phenol/chloroform');
INSERT INTO `biospecimen_protocol` VALUES (6,'Bead');
INSERT INTO `biospecimen_protocol` VALUES (7,'Column');
INSERT INTO `biospecimen_protocol` VALUES (8,'Salting out');
INSERT INTO `biospecimen_protocol` VALUES (9,'Machery-Nagel');
INSERT INTO `biospecimen_protocol` VALUES (10,'Reliaprep');
/*!40000 ALTER TABLE `biospecimen_protocol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_quality`
--

DROP TABLE IF EXISTS `biospecimen_quality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_quality` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_quality`
--

LOCK TABLES `biospecimen_quality` WRITE;
/*!40000 ALTER TABLE `biospecimen_quality` DISABLE KEYS */;
INSERT INTO `biospecimen_quality` VALUES (1,'Fresh');
INSERT INTO `biospecimen_quality` VALUES (2,'Frozen short term (<6mths)');
INSERT INTO `biospecimen_quality` VALUES (3,'Frozen long term (>6mths)');
/*!40000 ALTER TABLE `biospecimen_quality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_species`
--

DROP TABLE IF EXISTS `biospecimen_species`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_species` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_species`
--

LOCK TABLES `biospecimen_species` WRITE;
/*!40000 ALTER TABLE `biospecimen_species` DISABLE KEYS */;
INSERT INTO `biospecimen_species` VALUES (1,'Human');
/*!40000 ALTER TABLE `biospecimen_species` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_status`
--

DROP TABLE IF EXISTS `biospecimen_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_status`
--

LOCK TABLES `biospecimen_status` WRITE;
/*!40000 ALTER TABLE `biospecimen_status` DISABLE KEYS */;
INSERT INTO `biospecimen_status` VALUES (1,'New');
INSERT INTO `biospecimen_status` VALUES (2,'Archived');
/*!40000 ALTER TABLE `biospecimen_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_storage`
--

DROP TABLE IF EXISTS `biospecimen_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_storage` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `SIZE` double DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_storage_unit` (`UNIT_ID`),
  CONSTRAINT `fk_biospecimen_storage_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_storage`
--

LOCK TABLES `biospecimen_storage` WRITE;
/*!40000 ALTER TABLE `biospecimen_storage` DISABLE KEYS */;
INSERT INTO `biospecimen_storage` VALUES (1,'Unsure',NULL,NULL);
INSERT INTO `biospecimen_storage` VALUES (2,'2.0ml tube',2,17);
INSERT INTO `biospecimen_storage` VALUES (3,'1.5ml tube',1.5,17);
INSERT INTO `biospecimen_storage` VALUES (4,'0.5ml tube',0.5,17);
INSERT INTO `biospecimen_storage` VALUES (5,'1.2ml tube',1.2,17);
INSERT INTO `biospecimen_storage` VALUES (6,'10ml tube',10,17);
INSERT INTO `biospecimen_storage` VALUES (7,'15ml Falcontube',15,17);
INSERT INTO `biospecimen_storage` VALUES (8,'50ml Falcontube',50,17);
INSERT INTO `biospecimen_storage` VALUES (9,'1-well plate',NULL,NULL);
INSERT INTO `biospecimen_storage` VALUES (10,'48-well plate',NULL,NULL);
INSERT INTO `biospecimen_storage` VALUES (11,'96-well plate',NULL,NULL);
INSERT INTO `biospecimen_storage` VALUES (12,'6-well plate',NULL,NULL);
INSERT INTO `biospecimen_storage` VALUES (13,'Parrafin Block',NULL,NULL);
INSERT INTO `biospecimen_storage` VALUES (14,'Not Stored',NULL,NULL);
/*!40000 ALTER TABLE `biospecimen_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimenuid_padchar`
--

DROP TABLE IF EXISTS `biospecimenuid_padchar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_padchar` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimenuid_padchar`
--

LOCK TABLES `biospecimenuid_padchar` WRITE;
/*!40000 ALTER TABLE `biospecimenuid_padchar` DISABLE KEYS */;
INSERT INTO `biospecimenuid_padchar` VALUES (1,'1');
INSERT INTO `biospecimenuid_padchar` VALUES (2,'2');
INSERT INTO `biospecimenuid_padchar` VALUES (3,'3');
INSERT INTO `biospecimenuid_padchar` VALUES (4,'4');
INSERT INTO `biospecimenuid_padchar` VALUES (5,'5');
INSERT INTO `biospecimenuid_padchar` VALUES (6,'6');
INSERT INTO `biospecimenuid_padchar` VALUES (7,'7');
INSERT INTO `biospecimenuid_padchar` VALUES (8,'8');
INSERT INTO `biospecimenuid_padchar` VALUES (9,'9');
INSERT INTO `biospecimenuid_padchar` VALUES (10,'10');
/*!40000 ALTER TABLE `biospecimenuid_padchar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimenuid_sequence`
--

DROP TABLE IF EXISTS `biospecimenuid_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimenuid_sequence`
--

LOCK TABLES `biospecimenuid_sequence` WRITE;
/*!40000 ALTER TABLE `biospecimenuid_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimenuid_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimenuid_template`
--

DROP TABLE IF EXISTS `biospecimenuid_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `BIOSPECIMENUID_PREFIX` varchar(45) DEFAULT NULL,
  `BIOSPECIMENUID_TOKEN_ID` int(11) DEFAULT NULL,
  `BIOSPECIMENUID_PADCHAR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `fk_study_study` (`STUDY_ID`) USING BTREE,
  KEY `fk_study_biospecimenuid_padchar` (`BIOSPECIMENUID_PADCHAR_ID`),
  KEY `fk_study_biospecimenuid_token` (`BIOSPECIMENUID_TOKEN_ID`),
  CONSTRAINT `fk_study_biospecimenuid_padchar` FOREIGN KEY (`BIOSPECIMENUID_PADCHAR_ID`) REFERENCES `biospecimenuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_biospecimenuid_token` FOREIGN KEY (`BIOSPECIMENUID_TOKEN_ID`) REFERENCES `biospecimenuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimenuid_template`
--

LOCK TABLES `biospecimenuid_template` WRITE;
/*!40000 ALTER TABLE `biospecimenuid_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimenuid_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimenuid_token`
--

DROP TABLE IF EXISTS `biospecimenuid_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimenuid_token`
--

LOCK TABLES `biospecimenuid_token` WRITE;
/*!40000 ALTER TABLE `biospecimenuid_token` DISABLE KEYS */;
INSERT INTO `biospecimenuid_token` VALUES (1,'-');
INSERT INTO `biospecimenuid_token` VALUES (2,'@');
INSERT INTO `biospecimenuid_token` VALUES (3,'#');
INSERT INTO `biospecimenuid_token` VALUES (4,':');
INSERT INTO `biospecimenuid_token` VALUES (5,'*');
INSERT INTO `biospecimenuid_token` VALUES (6,'|');
INSERT INTO `biospecimenuid_token` VALUES (7,'_');
INSERT INTO `biospecimenuid_token` VALUES (8,'+');
/*!40000 ALTER TABLE `biospecimenuid_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cell_status`
--

DROP TABLE IF EXISTS `cell_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cell_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cell_status`
--

LOCK TABLES `cell_status` WRITE;
/*!40000 ALTER TABLE `cell_status` DISABLE KEYS */;
INSERT INTO `cell_status` VALUES (1,'Empty','Cell is empty and available');
INSERT INTO `cell_status` VALUES (2,'Used','Cell is used and unavailable');
INSERT INTO `cell_status` VALUES (3,'Held','Cell is held for allocation');
/*!40000 ALTER TABLE `cell_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_box`
--

DROP TABLE IF EXISTS `inv_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_box` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `NAME` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `NOOFCOL` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `RACK_ID` int(11) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `NOOFROW` int(11) NOT NULL,
  `COLNOTYPE_ID` int(11) NOT NULL,
  `ROWNOTYPE_ID` int(11) NOT NULL,
  `TRANSFER_ID` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`RACK_ID`),
  UNIQUE KEY `UNIQUE_BOX_NAME` (`RACK_ID`,`NAME`),
  KEY `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`),
  KEY `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`),
  KEY `fk_inv_box_rack_idx` (`RACK_ID`),
  CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rack` FOREIGN KEY (`RACK_ID`) REFERENCES `inv_rack` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_box`
--

LOCK TABLES `inv_box` WRITE;
/*!40000 ALTER TABLE `inv_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_cell`
--

DROP TABLE IF EXISTS `inv_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_cell` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BOX_ID` int(11) NOT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `ROWNO` int(11) DEFAULT NULL,
  `COLNO` int(11) DEFAULT NULL,
  `STATUS` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `BIOSPECIMEN_ID` int(11) DEFAULT NULL,
  `BIOSPECIMENKEY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  UNIQUE KEY `BOX_ID` (`BOX_ID`,`ROWNO`,`COLNO`),
  KEY `fk_inv_cell_box_idx` (`BOX_ID`) USING BTREE,
  KEY `fk_inv_cell_biospecimen_idx` (`BIOSPECIMEN_ID`) USING BTREE,
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_cell`
--

LOCK TABLES `inv_cell` WRITE;
/*!40000 ALTER TABLE `inv_cell` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_col_row_type`
--

DROP TABLE IF EXISTS `inv_col_row_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_col_row_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_col_row_type`
--

LOCK TABLES `inv_col_row_type` WRITE;
/*!40000 ALTER TABLE `inv_col_row_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_col_row_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_freezer`
--

DROP TABLE IF EXISTS `inv_freezer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_freezer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `LOCATION` text CHARACTER SET latin1,
  `STATUS` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `SITE_ID` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `LASTSERVICENOTE` text CHARACTER SET latin1,
  `NAME` varchar(50) CHARACTER SET latin1 NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DECOMMISSIONDATE` datetime DEFAULT NULL,
  `COMMISSIONDATE` datetime DEFAULT NULL,
  `LASTSERVICEDATE` datetime DEFAULT NULL,
  `DESCRIPTION` text CHARACTER SET latin1,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_freezer_site` (`SITE_ID`,`NAME`),
  UNIQUE KEY `UNIQUE_FREEZER_NAME` (`SITE_ID`,`NAME`),
  KEY `fk_inv_freezer_site` (`SITE_ID`),
  CONSTRAINT `fk_inv_freezer_site` FOREIGN KEY (`SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_freezer`
--

LOCK TABLES `inv_freezer` WRITE;
/*!40000 ALTER TABLE `inv_freezer` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_freezer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_rack`
--

DROP TABLE IF EXISTS `inv_rack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_rack` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FREEZER_ID` int(11) NOT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `NAME` varchar(50) CHARACTER SET latin1 NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DESCRIPTION` text CHARACTER SET latin1,
  `CAPACITY` int(11) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_rack_name_freezer` (`FREEZER_ID`,`NAME`),
  UNIQUE KEY `UNIQUE_RACK_NAME` (`FREEZER_ID`,`NAME`),
  KEY `fk_inv_freezer_tray_idx` (`FREEZER_ID`) USING BTREE,
  CONSTRAINT `FK_inv_rack_inv_freezer` FOREIGN KEY (`FREEZER_ID`) REFERENCES `inv_freezer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_rack`
--

LOCK TABLES `inv_rack` WRITE;
/*!40000 ALTER TABLE `inv_rack` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_rack` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_site`
--

DROP TABLE IF EXISTS `inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `CONTACT` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `ADDRESS` text CHARACTER SET latin1,
  `NAME` varchar(50) CHARACTER SET latin1 NOT NULL,
  `PHONE` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `LDAP_GROUP` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNIQUE_SITE_NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_site`
--

LOCK TABLES `inv_site` WRITE;
/*!40000 ALTER TABLE `inv_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_inv_site`
--

DROP TABLE IF EXISTS `study_inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_inv_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `INV_SITE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_study_inv_site` (`STUDY_ID`,`INV_SITE_ID`),
  KEY `fk_study_inv_site_study` (`STUDY_ID`),
  KEY `fk_study_inv_site_inv_site` (`INV_SITE_ID`),
  CONSTRAINT `fk_study_inv_site_inv_site` FOREIGN KEY (`INV_SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_inv_site`
--

LOCK TABLES `study_inv_site` WRITE;
/*!40000 ALTER TABLE `study_inv_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `study_inv_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treatment_type`
--

DROP TABLE IF EXISTS `treatment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treatment_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treatment_type`
--

LOCK TABLES `treatment_type` WRITE;
/*!40000 ALTER TABLE `treatment_type` DISABLE KEYS */;
INSERT INTO `treatment_type` VALUES (8,'70% Alcohol Fixed');
INSERT INTO `treatment_type` VALUES (11,'Dispase');
INSERT INTO `treatment_type` VALUES (7,'Ficoll prep');
INSERT INTO `treatment_type` VALUES (6,'Formalin Fixed');
INSERT INTO `treatment_type` VALUES (1,'Frozen');
INSERT INTO `treatment_type` VALUES (3,'RNA Later');
INSERT INTO `treatment_type` VALUES (5,'RNA later, then Formalin Fixed');
INSERT INTO `treatment_type` VALUES (4,'RNA later, then Snap Frozen');
INSERT INTO `treatment_type` VALUES (2,'Tissue Cultured');
INSERT INTO `treatment_type` VALUES (9,'TRIS EDTA');
INSERT INTO `treatment_type` VALUES (-1,'Unknown');
INSERT INTO `treatment_type` VALUES (10,'Unprocessed');
/*!40000 ALTER TABLE `treatment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  `FACTOR` double NOT NULL DEFAULT '1',
  `ORDER` bigint(20) NOT NULL DEFAULT '1',
  `TYPE` enum('VOLUME','MASS','TIME','TEMPERATURE','DISTANCE','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (1,'mL',NULL,1000,4,'VOLUME');
INSERT INTO `unit` VALUES (2,'ul','microlitre',1,1,'VOLUME');
INSERT INTO `unit` VALUES (3,'units',NULL,1,1,'UNKNOWN');
INSERT INTO `unit` VALUES (4,'ug','nanograms',1,1,'MASS');
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pheno` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `pheno`;

--
-- Table structure for table `pheno_collection`
--

DROP TABLE IF EXISTS `pheno_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `QUESTIONNAIRE_STATUS_ID` int(11) NOT NULL,
  `RECORD_DATE` datetime NOT NULL,
  `CUSTOM_FIELD_GROUP_ID` int(11) NOT NULL,
  `REVIEWED_DATE` date DEFAULT NULL,
  `REVIEWED_BY_ID` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` (`LINK_SUBJECT_STUDY_ID`),
  KEY `FK_PHENO_QUESTIONNAIRE_STATUS_ID` (`QUESTIONNAIRE_STATUS_ID`),
  KEY `FK_PHENO_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_REVIEWED_BY_ARK_USER_ID` (`REVIEWED_BY_ID`),
  CONSTRAINT `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `study`.`custom_field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_QUESTIONNAIRE_STATUS_ID` FOREIGN KEY (`QUESTIONNAIRE_STATUS_ID`) REFERENCES `questionnaire_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REVIEWED_BY_ARK_USER_ID` FOREIGN KEY (`REVIEWED_BY_ID`) REFERENCES `study`.`ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_collection`
--

LOCK TABLES `pheno_collection` WRITE;
/*!40000 ALTER TABLE `pheno_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pheno_data`
--

DROP TABLE IF EXISTS `pheno_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
  `PHENO_COLLECTION_ID` int(11) DEFAULT NULL,
  `DATE_DATA_VALUE` date DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` text,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY (`ID`),
  KEY `FK_PHENO_DATA_CFD_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_PHENO_DATA_PHENO_COLLECTION_ID` (`PHENO_COLLECTION_ID`),
  CONSTRAINT `FK_PHENO_DATA_CFD_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_DATA_PHENO_COLLECTION_ID` FOREIGN KEY (`PHENO_COLLECTION_ID`) REFERENCES `pheno_collection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_data`
--

LOCK TABLES `pheno_data` WRITE;
/*!40000 ALTER TABLE `pheno_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pheno_dataset_category`
--

DROP TABLE IF EXISTS `pheno_dataset_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_dataset_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `ORDER_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_name` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_CUSTOMFIELDCATEGORY_ARK_FUNCTION_ID_idx` (`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOMFIELDCATEGORY_PARENT_ID` (`PARENT_ID`),
  CONSTRAINT `FK_CUSTOMFIELDCATEGORY_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELDCATEGORY_PARENT_ID` FOREIGN KEY (`PARENT_ID`) REFERENCES `pheno_dataset_category` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_dataset_category`
--

LOCK TABLES `pheno_dataset_category` WRITE;
/*!40000 ALTER TABLE `pheno_dataset_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_dataset_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pheno_dataset_field`
--

DROP TABLE IF EXISTS `pheno_dataset_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_dataset_field` (
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
  `HAS_DATA` tinyint(4) NOT NULL DEFAULT '0',
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_dataset_field`
--

LOCK TABLES `pheno_dataset_field` WRITE;
/*!40000 ALTER TABLE `pheno_dataset_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_dataset_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pheno_dataset_field_display`
--

DROP TABLE IF EXISTS `pheno_dataset_field_display`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_dataset_field_display` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PHENO_DATASET_FIELD_ID` int(11) NOT NULL,
  `PHENO_DATASET_FIELD_GROUP_ID` int(11) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `REQUIRED` int(11) DEFAULT NULL,
  `REQUIRED_MESSAGE` varchar(45) DEFAULT NULL,
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_PHENO_DATASET_FIELD_GROUP_ID` (`PHENO_DATASET_FIELD_GROUP_ID`),
  KEY `FK_PHENO_DATASET_FIELD_ID` (`PHENO_DATASET_FIELD_ID`),
  CONSTRAINT `FK_PHENO_DATASET_FIELD_GROUP_ID` FOREIGN KEY (`PHENO_DATASET_FIELD_GROUP_ID`) REFERENCES `pheno_dataset_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PHENO_DATASET_FIELD_ID` FOREIGN KEY (`PHENO_DATASET_FIELD_ID`) REFERENCES `pheno_dataset_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_dataset_field_display`
--

LOCK TABLES `pheno_dataset_field_display` WRITE;
/*!40000 ALTER TABLE `pheno_dataset_field_display` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_dataset_field_display` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pheno_dataset_field_group`
--

DROP TABLE IF EXISTS `pheno_dataset_field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_dataset_field_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `PUBLISHED` tinyint(1) DEFAULT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_PHENO_DATASET_FIELD_GROUP_STUDY_ID` (`STUDY_ID`),
  KEY `FK_PHENO_DATASET_FIELD_GROUP_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_PHENO_DATASET_FIELD_GROUP_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_DATASET_FIELD_GROUP_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_dataset_field_group`
--

LOCK TABLES `pheno_dataset_field_group` WRITE;
/*!40000 ALTER TABLE `pheno_dataset_field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_dataset_field_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire_status`
--

DROP TABLE IF EXISTS `questionnaire_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questionnaire_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire_status`
--

LOCK TABLES `questionnaire_status` WRITE;
/*!40000 ALTER TABLE `questionnaire_status` DISABLE KEYS */;
INSERT INTO `questionnaire_status` VALUES (1,'In Progress','The Questionnaire is being provided with data and not yet completed.');
INSERT INTO `questionnaire_status` VALUES (2,'Data Entry Completed','Questionnaire data entry is completed and awaiting review.');
INSERT INTO `questionnaire_status` VALUES (3,'Review Ok','The Questionnaire data was reviewed successfully and questionnaire is now locked from further modification.');
INSERT INTO `questionnaire_status` VALUES (4,'Review Failed','The Questionnaire data failed review and is needs to be revisited for data correction.');
INSERT INTO `questionnaire_status` VALUES (5,'Uploaded From File','The Questionnaire data has been update from file, with no further action taken since then.');
/*!40000 ALTER TABLE `questionnaire_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'CREATED');
INSERT INTO `status` VALUES (2,'ACTIVE');
INSERT INTO `status` VALUES (3,'DISPLAYED');
INSERT INTO `status` VALUES (4,'EXPIRED');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `UPLOAD_REPORT` longblob,
  `UPLOAD_TYPE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE,
  KEY `fk_upload_study` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `pheno/del';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload`
--

LOCK TABLES `upload` WRITE;
/*!40000 ALTER TABLE `upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `reporting`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `reporting` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `reporting`;

--
-- Table structure for table `biocollection_field`
--

DROP TABLE IF EXISTS `biocollection_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biocollection_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `biocollection_field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollection_field`
--

LOCK TABLES `biocollection_field` WRITE;
/*!40000 ALTER TABLE `biocollection_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `biocollection_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biocollection_field_search`
--

DROP TABLE IF EXISTS `biocollection_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOCOLLECTION_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_bcfs_bcf_s` (`BIOCOLLECTION_FIELD_ID`,`SEARCH_ID`) USING BTREE,
  KEY `fk_dfs_biocollection_field` (`BIOCOLLECTION_FIELD_ID`),
  KEY `fk_dfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='many2many join biocollection_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollection_field_search`
--

LOCK TABLES `biocollection_field_search` WRITE;
/*!40000 ALTER TABLE `biocollection_field_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `biocollection_field_search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_field`
--

DROP TABLE IF EXISTS `biospecimen_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `FILTERABLE` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `biospecimen_field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_field`
--

LOCK TABLES `biospecimen_field` WRITE;
/*!40000 ALTER TABLE `biospecimen_field` DISABLE KEYS */;
INSERT INTO `biospecimen_field` VALUES (1,'Biospecimen','biospecimenUid','BiospecimenUID',1,1);
INSERT INTO `biospecimen_field` VALUES (2,'Biospecimen','sampleType','Sample Type',4,1);
INSERT INTO `biospecimen_field` VALUES (3,'Biospecimen','sampleDate','Sample Date',3,1);
INSERT INTO `biospecimen_field` VALUES (4,'Biospecimen','sampleTime','Sample Time',3,1);
INSERT INTO `biospecimen_field` VALUES (5,'Biospecimen','processedDate','Processed Date',3,1);
INSERT INTO `biospecimen_field` VALUES (6,'Biospecimen','processedTime','Processed Time',3,1);
INSERT INTO `biospecimen_field` VALUES (7,'Biospecimen','quantity','Quantity',1,1);
INSERT INTO `biospecimen_field` VALUES (8,'Biospecimen','concentration','Concentration',1,1);
INSERT INTO `biospecimen_field` VALUES (9,'Biospecimen','purity','Purity',1,1);
INSERT INTO `biospecimen_field` VALUES (10,'Biospecimen','site','Site',1,0);
INSERT INTO `biospecimen_field` VALUES (16,'Biospecimen','freezer','Freezer',1,0);
INSERT INTO `biospecimen_field` VALUES (17,'Biospecimen','rack','Rack',1,0);
INSERT INTO `biospecimen_field` VALUES (18,'Biospecimen','box','Box',1,0);
INSERT INTO `biospecimen_field` VALUES (19,'Biospecimen','column','Column',1,0);
INSERT INTO `biospecimen_field` VALUES (20,'Biospecimen','row','Row',1,0);
INSERT INTO `biospecimen_field` VALUES (21,'Biospecimen','storedIn','Stored In',4,1);
INSERT INTO `biospecimen_field` VALUES (22,'Biospecimen','grade','Grade',4,1);
INSERT INTO `biospecimen_field` VALUES (23,'Biospecimen','comments','Comments',1,1);
INSERT INTO `biospecimen_field` VALUES (24,'Biospecimen','unit','Unit',1,1);
INSERT INTO `biospecimen_field` VALUES (25,'Biospecimen','treatmentType','Treatment Type',4,1);
INSERT INTO `biospecimen_field` VALUES (26,'Biospecimen','quality','Quality',1,1);
INSERT INTO `biospecimen_field` VALUES (27,'Biospecimen','anticoag','Anticoagulant',4,1);
INSERT INTO `biospecimen_field` VALUES (28,'Biospecimen','status','Status',4,1);
INSERT INTO `biospecimen_field` VALUES (29,'Biospecimen','biospecimenProtocol','Protocol',4,1);
INSERT INTO `biospecimen_field` VALUES (30,'Biospecimen','amount','Amount',1,1);
INSERT INTO `biospecimen_field` VALUES (31,'BioCollection','biocollectionUid','BiocollectionUid',4,1);
INSERT INTO `biospecimen_field` VALUES (32,'BioCollection','name','Biocollection Name',4,1);
INSERT INTO `biospecimen_field` VALUES (33,'Biospecimen','parentUID','Parent UID',1,1);
INSERT INTO `biospecimen_field` VALUES (34,'Biospecimen','parentUID','Parent UID',1,1);
/*!40000 ALTER TABLE `biospecimen_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen_field_search`
--

DROP TABLE IF EXISTS `biospecimen_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_bsfs_df_s` (`BIOSPECIMEN_FIELD_ID`,`SEARCH_ID`) USING BTREE,
  KEY `fk_bsfs_biospecimen_field` (`BIOSPECIMEN_FIELD_ID`) USING BTREE,
  KEY `fk_bsfs_search` (`SEARCH_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='many2many join biospecimen_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_field_search`
--

LOCK TABLES `biospecimen_field_search` WRITE;
/*!40000 ALTER TABLE `biospecimen_field_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimen_field_search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_status_field`
--

DROP TABLE IF EXISTS `consent_status_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_status_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `FILTERABLE` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_consent_status_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `consent_status_field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_status_field`
--

LOCK TABLES `consent_status_field` WRITE;
/*!40000 ALTER TABLE `consent_status_field` DISABLE KEYS */;
INSERT INTO `consent_status_field` VALUES (1,'StudyComp','name','Study Component Name',1,1);
INSERT INTO `consent_status_field` VALUES (2,'Consent','studyComponentStatus','Study Component Status',1,1);
INSERT INTO `consent_status_field` VALUES (3,'Consent','consentDate','Consent Date',3,1);
INSERT INTO `consent_status_field` VALUES (4,'Consent','consentedBy','Consented By',1,1);
/*!40000 ALTER TABLE `consent_status_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_status_field_search`
--

DROP TABLE IF EXISTS `consent_status_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_status_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONSENT_STATUS_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_dfs_df_s` (`CONSENT_STATUS_FIELD_ID`,`SEARCH_ID`),
  KEY `fk_dfs_demographic_field` (`CONSENT_STATUS_FIELD_ID`),
  KEY `fk_dfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='many2many join demographic_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_status_field_search`
--

LOCK TABLES `consent_status_field_search` WRITE;
/*!40000 ALTER TABLE `consent_status_field_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `consent_status_field_search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_display_search`
--

DROP TABLE IF EXISTS `custom_field_display_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_display_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_cfds_cfd_search` (`CUSTOM_FIELD_DISPLAY_ID`,`SEARCH_ID`),
  KEY `fk_cfds_custom_field_display` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `fk_cfds_search` (`SEARCH_ID`),
  CONSTRAINT `custom_field_display_search_ibfk_1` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `custom_field_display_search_ibfk_2` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='many2many join custom_field_display and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_display_search`
--

LOCK TABLES `custom_field_display_search` WRITE;
/*!40000 ALTER TABLE `custom_field_display_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_display_search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `demographic_field`
--

DROP TABLE IF EXISTS `demographic_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demographic_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `FILTERABLE` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_demographic_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `demographic_field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demographic_field`
--

LOCK TABLES `demographic_field` WRITE;
/*!40000 ALTER TABLE `demographic_field` DISABLE KEYS */;
INSERT INTO `demographic_field` VALUES (5,'Person','firstName','First Name',1,1);
INSERT INTO `demographic_field` VALUES (6,'LinkSubjectStudy','consentDate','Consent Date',3,1);
INSERT INTO `demographic_field` VALUES (7,'LinkSubjectStudy','subjectUID','Subject UID',1,1);
INSERT INTO `demographic_field` VALUES (8,'Address','postCode','Post Code',1,1);
INSERT INTO `demographic_field` VALUES (9,'Person','lastName','Last Name',1,1);
INSERT INTO `demographic_field` VALUES (10,'Person','genderType','Sex',4,1);
INSERT INTO `demographic_field` VALUES (11,'Person','vitalStatus','Vital Status',4,1);
INSERT INTO `demographic_field` VALUES (13,'Person','maritalStatus','Marital Status',4,1);
INSERT INTO `demographic_field` VALUES (14,'Person','dateOfBirth','DOB',3,1);
INSERT INTO `demographic_field` VALUES (15,'Person','dateOfDeath','Date of Death',3,1);
INSERT INTO `demographic_field` VALUES (16,'Person','causeOfDeath','Cause of Death',1,1);
INSERT INTO `demographic_field` VALUES (17,'Person','preferredEmail','Preferred Email',1,1);
INSERT INTO `demographic_field` VALUES (18,'Person','otherEmail','Other Email',1,1);
INSERT INTO `demographic_field` VALUES (19,'Person','dateLastKnownAlive','Last Known Alive',3,1);
INSERT INTO `demographic_field` VALUES (20,'Address','addressLine1','Building Name/Unit',1,1);
INSERT INTO `demographic_field` VALUES (21,'Address','streetAddress','Street Address',1,1);
INSERT INTO `demographic_field` VALUES (22,'Address','city','City',1,1);
INSERT INTO `demographic_field` VALUES (23,'Address','country','Country',4,1);
INSERT INTO `demographic_field` VALUES (24,'Address','state','State',1,1);
INSERT INTO `demographic_field` VALUES (25,'Address','otherState','Other State',1,1);
INSERT INTO `demographic_field` VALUES (26,'Address','addressStatus','Address Status',4,1);
INSERT INTO `demographic_field` VALUES (27,'Address','addressType','Address Type',4,1);
INSERT INTO `demographic_field` VALUES (28,'Phone','phoneNumber','Phone Number',1,0);
INSERT INTO `demographic_field` VALUES (29,'Person','titleType','Title',4,1);
INSERT INTO `demographic_field` VALUES (30,'Person','maritalStatus','Marital Status',4,1);
INSERT INTO `demographic_field` VALUES (31,'Person','contactMethod','Contact Method',4,1);
INSERT INTO `demographic_field` VALUES (32,'LinkSubjectStudy','subjectStatus','Status',4,1);
INSERT INTO `demographic_field` VALUES (33,'LinkSubjectStudy','consentStatus','Consent Status',4,1);
INSERT INTO `demographic_field` VALUES (34,'LinkSubjectStudy','consentType','Consent Type',4,1);
INSERT INTO `demographic_field` VALUES (35,'LinkSubjectStudy','consentDownloaded','Consent Downloaded',4,1);
INSERT INTO `demographic_field` VALUES (36,'LinkSubjectStudy','consentToPassiveDataGathering','Consent To Passive Data Gathering',4,1);
INSERT INTO `demographic_field` VALUES (37,'LinkSubjectStudy','consentToActiveContact','Consent To Active Contact',4,1);
INSERT INTO `demographic_field` VALUES (38,'LinkSubjectStudy','consentToUseData','Consent To Use Data',4,1);
INSERT INTO `demographic_field` VALUES (39,'LinkSubjectStudy','heardAboutStudy','Heard About Study',1,1);
INSERT INTO `demographic_field` VALUES (40,'LinkSubjectStudy','comment','Subject Comments',1,1);
INSERT INTO `demographic_field` VALUES (41,'Person','otherEmailStatus','Other Email Status',4,1);
INSERT INTO `demographic_field` VALUES (42,'Person','preferredEmailStatus','PreferredEmailStatus',4,1);
INSERT INTO `demographic_field` VALUES (43,'Address','dateReceived','Date Address Received',3,1);
INSERT INTO `demographic_field` VALUES (44,'Address','comments','Address Comments',1,1);
INSERT INTO `demographic_field` VALUES (45,'Address','source','Address Source',1,1);
INSERT INTO `demographic_field` VALUES (46,'Phone','phoneType','Phone Type',4,0);
INSERT INTO `demographic_field` VALUES (47,'Phone','areaCode','Area Code',1,0);
INSERT INTO `demographic_field` VALUES (48,'Phone','phoneStatus','Phone Status',4,0);
INSERT INTO `demographic_field` VALUES (49,'Phone','source','Phone Source',1,0);
INSERT INTO `demographic_field` VALUES (50,'Phone','dateReceived','Date Received',1,0);
INSERT INTO `demographic_field` VALUES (51,'Phone','silentMode','Silent Mode',4,0);
INSERT INTO `demographic_field` VALUES (52,'Phone','comment','Phone Comment',1,0);
INSERT INTO `demographic_field` VALUES (53,'OtherID','otherID','Other ID',1,1);
INSERT INTO `demographic_field` VALUES (54,'OtherID','otherID_Source','Other ID Source',1,1);
INSERT INTO `demographic_field` VALUES (55,'Phone','validFrom','Phone Valid From',3,1);
INSERT INTO `demographic_field` VALUES (56,'Phone','validTo','Phone Valid To',3,1);
INSERT INTO `demographic_field` VALUES (57,'Address','validFrom','Address Valid From',3,1);
INSERT INTO `demographic_field` VALUES (58,'Address','validTo','Address Valid To',3,1);
INSERT INTO `demographic_field` VALUES (59,'LinkSubjectTwin','secondSubject','Twin Subject UID ',1,1);
INSERT INTO `demographic_field` VALUES (60,'LinkSubjectTwin','twinType','TwinType',1,1);
/*!40000 ALTER TABLE `demographic_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `demographic_field_search`
--

DROP TABLE IF EXISTS `demographic_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demographic_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEMOGRAPHIC_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_dfs_df_s` (`DEMOGRAPHIC_FIELD_ID`,`SEARCH_ID`),
  KEY `fk_dfs_demographic_field` (`DEMOGRAPHIC_FIELD_ID`),
  KEY `fk_dfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='many2many join demographic_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demographic_field_search`
--

LOCK TABLES `demographic_field_search` WRITE;
/*!40000 ALTER TABLE `demographic_field_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `demographic_field_search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_filter`
--

DROP TABLE IF EXISTS `query_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_filter` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEMOGRAPHIC_FIELD_ID` int(11) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
  `VALUE` varchar(512) DEFAULT NULL,
  `SECOND_VALUE` varchar(512) DEFAULT NULL,
  `OPERATOR` varchar(256) DEFAULT NULL,
  `PREFIX` varchar(56) DEFAULT NULL,
  `BIOSPECIMEN_FIELD_ID` int(11) DEFAULT NULL,
  `BIOCOLLECTION_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_FIELD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_qf_df_idx` (`DEMOGRAPHIC_FIELD_ID`),
  KEY `fk_qf_cfd_idx` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `fk_qf_bsf_idx` (`BIOSPECIMEN_FIELD_ID`),
  KEY `fk_qf_bcf_idx` (`BIOCOLLECTION_FIELD_ID`),
  KEY `fk_query_filter_1_idx` (`SEARCH_ID`),
  KEY `fk_qf_csf_idx` (`CONSENT_STATUS_FIELD_ID`),
  CONSTRAINT `query_filter_ibfk_1` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_ibfk_2` FOREIGN KEY (`BIOCOLLECTION_FIELD_ID`) REFERENCES `biocollection_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_ibfk_3` FOREIGN KEY (`BIOSPECIMEN_FIELD_ID`) REFERENCES `biospecimen_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_ibfk_4` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `query_filter_ibfk_5` FOREIGN KEY (`DEMOGRAPHIC_FIELD_ID`) REFERENCES `demographic_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_filter`
--

LOCK TABLES `query_filter` WRITE;
/*!40000 ALTER TABLE `query_filter` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_filter_grouping`
--

DROP TABLE IF EXISTS `query_filter_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_filter_grouping` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_GROUPING_ID` int(11) NOT NULL,
  `LEFT_FILTER_ID` int(11) NOT NULL,
  `JOIN_TO_NEXT_FILTER` varchar(56) DEFAULT NULL,
  `PRECEDENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_qfg_qg_idx` (`PARENT_GROUPING_ID`),
  KEY `fk_qfg_qf_idx` (`LEFT_FILTER_ID`),
  CONSTRAINT `query_filter_grouping_ibfk_1` FOREIGN KEY (`LEFT_FILTER_ID`) REFERENCES `query_filter` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_grouping_ibfk_2` FOREIGN KEY (`PARENT_GROUPING_ID`) REFERENCES `query_grouping` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_filter_grouping`
--

LOCK TABLES `query_filter_grouping` WRITE;
/*!40000 ALTER TABLE `query_filter_grouping` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_filter_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_grouping`
--

DROP TABLE IF EXISTS `query_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_grouping` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_grouping`
--

LOCK TABLES `query_grouping` WRITE;
/*!40000 ALTER TABLE `query_grouping` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_grouping_grouping`
--

DROP TABLE IF EXISTS `query_grouping_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_grouping_grouping` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_GROUPING_ID` int(11) NOT NULL,
  `LEFT_GROUPING_ID` int(11) NOT NULL,
  `JOIN_TO_NEXT_FILTER` varchar(56) DEFAULT NULL,
  `PRECEDENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_qgg_parent_grouping_idx` (`PARENT_GROUPING_ID`),
  KEY `fk_qgg_left_grouping_idx` (`LEFT_GROUPING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_grouping_grouping`
--

LOCK TABLES `query_grouping_grouping` WRITE;
/*!40000 ALTER TABLE `query_grouping_grouping` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_grouping_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_output_format`
--

DROP TABLE IF EXISTS `report_output_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_output_format` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_output_format`
--

LOCK TABLES `report_output_format` WRITE;
/*!40000 ALTER TABLE `report_output_format` DISABLE KEYS */;
INSERT INTO `report_output_format` VALUES (1,'PDF','Portable Document Format (compatible with Adobe Reader)');
INSERT INTO `report_output_format` VALUES (2,'CSV','Comma Separated Value (compatible with Excel)');
/*!40000 ALTER TABLE `report_output_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_template`
--

DROP TABLE IF EXISTS `report_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1024) DEFAULT NULL,
  `TEMPLATE_PATH` varchar(255) NOT NULL,
  `MODULE_ID` int(11) DEFAULT NULL,
  `FUNCTION_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  KEY `FK_REPORTTEMPLATE_ARKMODULE` (`MODULE_ID`),
  KEY `FK_REPORTTEMPLATE_ARKFUNCTION` (`FUNCTION_ID`),
  CONSTRAINT `report_template_ibfk_1` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `report_template_ibfk_2` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_template`
--

LOCK TABLES `report_template` WRITE;
/*!40000 ALTER TABLE `report_template` DISABLE KEYS */;
INSERT INTO `report_template` VALUES (1,'Study Summary','An overview of subject information for a given study.','StudySummaryReport.jrxml',1,23);
INSERT INTO `report_template` VALUES (2,'Study-level Consent','Detailed subject information for study-level consent.','ConsentDetailsReport.jrxml',2,24);
INSERT INTO `report_template` VALUES (3,'Study Component Consent','Detailed subject information with respect to study component consent.','ConsentDetailsReport.jrxml',2,25);
INSERT INTO `report_template` VALUES (4,'Dataset Field Details','A listing of phenotype fields comprising a dataset definition.','DataDictionaryReport.jrxml',3,26);
INSERT INTO `report_template` VALUES (5,'Study User Roles','A listing of user roles and permissions for a given study.','StudyUserRolePermissions.jrxml',1,33);
INSERT INTO `report_template` VALUES (6,'Researcher Costs','A listing of invoiced billable item costs for a researcher.','ResearcherCostReport.jrxml',8,62);
INSERT INTO `report_template` VALUES (7,'Detailed Costs','A listing of invoiced billable item costs, grouped by type, for a researcher.','ResearcherDetailCostReport.jrxml',8,62);
INSERT INTO `report_template` VALUES (8,'Study Costs','A listing of invoiced billable item costs, grouped by type, for a study.','StudyDetailCostReport.jrxml',8,62);
INSERT INTO `report_template` VALUES (9,'Biospecimen Summary','An overview of biospecimen information for a given study.','BiospecimenSummaryReport.jrxml',5,19);
INSERT INTO `report_template` VALUES (10,'Biospecimen Details','Detailed biospecimen information, including inventory, for a given study.','BiospecimenDetailReport.jrxml',5,19);
/*!40000 ALTER TABLE `report_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search`
--

DROP TABLE IF EXISTS `search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `TOP_LEVEL_GROUPING_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `STATUS` varchar(45) DEFAULT NULL,
  `STARTTIME` datetime DEFAULT NULL,
  `FINISHTIME` datetime DEFAULT NULL,
  `INCLUDE_GENO` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `fk_search_query_grouping` (`TOP_LEVEL_GROUPING_ID`),
  KEY `search_ibfk_1` (`STUDY_ID`),
  CONSTRAINT `search_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search`
--

LOCK TABLES `search` WRITE;
/*!40000 ALTER TABLE `search` DISABLE KEYS */;
/*!40000 ALTER TABLE `search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_payload`
--

DROP TABLE IF EXISTS `search_payload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_payload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAYLOAD` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_payload`
--

LOCK TABLES `search_payload` WRITE;
/*!40000 ALTER TABLE `search_payload` DISABLE KEYS */;
/*!40000 ALTER TABLE `search_payload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_result`
--

DROP TABLE IF EXISTS `search_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_result` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEARCH_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `CHECKSUM` varchar(50) DEFAULT NULL,
  `USER_ID` varchar(50) DEFAULT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `SEARCH_PAYLOAD_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `ID` (`ID`) USING BTREE,
  KEY `fk_search_result_search` (`SEARCH_ID`) USING BTREE,
  KEY `fk_upload_payload` (`SEARCH_PAYLOAD_ID`) USING BTREE,
  CONSTRAINT `fk_search_result_search` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `search_result_ibfk_1` FOREIGN KEY (`SEARCH_PAYLOAD_ID`) REFERENCES `search_payload` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `search_result_ibfk_2` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_result`
--

LOCK TABLES `search_result` WRITE;
/*!40000 ALTER TABLE `search_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `search_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_subject`
--

DROP TABLE IF EXISTS `search_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_subject` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEARCH_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_search_subject_1_idx` (`SEARCH_ID`) USING BTREE,
  KEY `fk_search_subject_2` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  CONSTRAINT `search_subject_ibfk_1` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `search_subject_ibfk_2` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_subject`
--

LOCK TABLES `search_subject` WRITE;
/*!40000 ALTER TABLE `search_subject` DISABLE KEYS */;
/*!40000 ALTER TABLE `search_subject` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Current Database: `study`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `study` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `study`;

--
-- Table structure for table `action_type`
--

DROP TABLE IF EXISTS `action_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `action_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `action_type`
--

LOCK TABLES `action_type` WRITE;
/*!40000 ALTER TABLE `action_type` DISABLE KEYS */;
INSERT INTO `action_type` VALUES (1,'CREATED',NULL);
INSERT INTO `action_type` VALUES (2,'UPDATED',NULL);
INSERT INTO `action_type` VALUES (3,'DELETED',NULL);
INSERT INTO `action_type` VALUES (4,'ARCHIVED',NULL);
/*!40000 ALTER TABLE `action_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ADDRESS_LINE_1` varchar(255) DEFAULT NULL,
  `STREET_ADDRESS` varchar(255) DEFAULT NULL,
  `CITY` varchar(45) DEFAULT NULL,
  `STATE_ID` int(11) DEFAULT NULL,
  `POST_CODE` varchar(10) DEFAULT NULL,
  `COUNTRY_ID` int(11) DEFAULT NULL,
  `ADDRESS_STATUS_ID` int(11) DEFAULT NULL,
  `ADDRESS_TYPE_ID` int(11) DEFAULT NULL,
  `OTHER_STATE` varchar(45) DEFAULT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENTS` text,
  `PREFERRED_MAILING_ADDRESS` int(11) DEFAULT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `VALID_FROM` date DEFAULT NULL,
  `VALID_TO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_address_country` (`COUNTRY_ID`) USING BTREE,
  KEY `fk_address_person` (`PERSON_ID`) USING BTREE,
  KEY `fk_address_address_type` (`ADDRESS_TYPE_ID`) USING BTREE,
  KEY `fk_address_status` (`ADDRESS_STATUS_ID`) USING BTREE,
  KEY `fk_address_preferred_mailing_address_id` (`PREFERRED_MAILING_ADDRESS`) USING BTREE,
  KEY `fk_address_state` (`STATE_ID`) USING BTREE,
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_address_type` FOREIGN KEY (`ADDRESS_TYPE_ID`) REFERENCES `address_type` (`ID`),
  CONSTRAINT `fk_address_country` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_state` FOREIGN KEY (`STATE_ID`) REFERENCES `state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_status` FOREIGN KEY (`ADDRESS_STATUS_ID`) REFERENCES `address_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_TYPE_ID`) REFER `study/addre';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address_status`
--

DROP TABLE IF EXISTS `address_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address_status`
--

LOCK TABLES `address_status` WRITE;
/*!40000 ALTER TABLE `address_status` DISABLE KEYS */;
INSERT INTO `address_status` VALUES (1,'Current');
INSERT INTO `address_status` VALUES (2,'Current - Alternative');
INSERT INTO `address_status` VALUES (3,'Current - Under Investigation');
INSERT INTO `address_status` VALUES (5,'Incorrect address');
INSERT INTO `address_status` VALUES (4,'Valid past address');
/*!40000 ALTER TABLE `address_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address_type`
--

DROP TABLE IF EXISTS `address_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address_type`
--

LOCK TABLES `address_type` WRITE;
/*!40000 ALTER TABLE `address_type` DISABLE KEYS */;
INSERT INTO `address_type` VALUES (1,'Residential',NULL);
INSERT INTO `address_type` VALUES (2,'Work',NULL);
INSERT INTO `address_type` VALUES (3,'Postal',NULL);
/*!40000 ALTER TABLE `address_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_function`
--

DROP TABLE IF EXISTS `ark_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_function` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `ARK_FUNCTION_TYPE_ID` int(11) NOT NULL,
  `RESOURCE_KEY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ARK_FUNCTION_ARK_FUNCTION_TYPE_ID` (`ARK_FUNCTION_TYPE_ID`),
  CONSTRAINT `FK_ARK_FUNCTION_ARK_FUNCTION_TYPE_ID` FOREIGN KEY (`ARK_FUNCTION_TYPE_ID`) REFERENCES `ark_function_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_function`
--

LOCK TABLES `ark_function` WRITE;
/*!40000 ALTER TABLE `ark_function` DISABLE KEYS */;
INSERT INTO `ark_function` VALUES (1,'STUDY','Study Management usecase. This is represented via the Study Detail Tab under the main Study  Tab.',1,'tab.module.study.details');
INSERT INTO `ark_function` VALUES (2,'STUDY_COMPONENT','Study Component usecase. This is represented via the StudyComponent Tab under the main Study  Tab.',1,'tab.module.study.components');
INSERT INTO `ark_function` VALUES (3,'MY_DETAIL','Edit My details usecase, this is represented via My Detail tab.',1,'tab.module.mydetails');
INSERT INTO `ark_function` VALUES (4,'USER','User management usecase. This is represented via the User Tab under the main Study  Tab.',1,'tab.module.user.management');
INSERT INTO `ark_function` VALUES (5,'SUBJECT','Subject management usecase. This is represented via the Subject Tab under the main Study  Tab.',1,'tab.module.subject.detail');
INSERT INTO `ark_function` VALUES (6,'PHONE','Manage phone usecase. This is represented via the Phone tab under the main Study  Tab.',1,'tab.module.person.phone');
INSERT INTO `ark_function` VALUES (7,'ADDRESS','Manage Address',1,'tab.module.person.address');
INSERT INTO `ark_function` VALUES (8,'ATTACHMENT','Manage Consent and Component attachments. This is represented via the Attachment tab under Subject Main tab.',1,'tab.module.subject.subjectFile');
INSERT INTO `ark_function` VALUES (9,'CONSENT','Manage Subject Consents. This is represented via the Consent tab under the main Study  Tab.',1,'tab.module.subject.consent');
INSERT INTO `ark_function` VALUES (10,'STUDY_DATA_UPLOAD','All the study related data uploads.Demographic,Pedigree,Subject attachment,Consent Data',1,'tab.module.study.studyDataUpload');
INSERT INTO `ark_function` VALUES (11,'SUBJECT_CUSTOM_FIELD','Manage Custom Fields for Subjects.',1,'tab.module.subject.subjectcustomfield');
INSERT INTO `ark_function` VALUES (12,'DATA_DICTIONARY','Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.field');
INSERT INTO `ark_function` VALUES (13,'DATA_DICTIONARY_UPLOAD','Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldUpload');
INSERT INTO `ark_function` VALUES (14,'PHENO_COLLECTION','Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.collection');
INSERT INTO `ark_function` VALUES (15,'FIELD_DATA','Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldData');
INSERT INTO `ark_function` VALUES (16,'FIELD_DATA_UPLOAD','Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.phenoUpload');
INSERT INTO `ark_function` VALUES (17,'LIMS_SUBJECT','LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.',1,'tab.module.lims.subject.detail');
INSERT INTO `ark_function` VALUES (18,'LIMS_COLLECTION','LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.',1,'tab.module.lims.collection');
INSERT INTO `ark_function` VALUES (19,'BIOSPECIMEN','LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.',1,'tab.module.lims.biospecimen');
INSERT INTO `ark_function` VALUES (20,'INVENTORY','LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.',1,'tab.module.lims.inventory');
INSERT INTO `ark_function` VALUES (21,'CORRESPONDENCE','',1,'tab.module.subject.correspondence');
INSERT INTO `ark_function` VALUES (22,'SUMMARY','Phenotypic Summary.',1,'tab.module.phenotypic.summary');
INSERT INTO `ark_function` VALUES (23,'REPORT_STUDYSUMARY','Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>',2,NULL);
INSERT INTO `ark_function` VALUES (24,'REPORT_STUDYLEVELCONSENT','Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.',2,NULL);
INSERT INTO `ark_function` VALUES (25,'REPORT_STUDYCOMPCONSENT','Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.',2,NULL);
INSERT INTO `ark_function` VALUES (26,'REPORT_PHENOFIELDDETAILS','Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic data set.',2,NULL);
INSERT INTO `ark_function` VALUES (27,'GENO_COLLECTION','Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu',1,'tab.module.geno.collection');
INSERT INTO `ark_function` VALUES (28,'ROLE_POLICY_TEMPLATE','Allows CRUD operations on the ark_role_policy_template table for the Ark application',1,'tab.module.admin.rolePolicyTemplate');
INSERT INTO `ark_function` VALUES (29,'MODULE','Allows CRUD operations on the ark_module table for the Ark application',1,'tab.module.admin.module');
INSERT INTO `ark_function` VALUES (30,'FUNCTION','Allows CRUD operations on the ark_function table for the Ark application',1,'tab.module.admin.function');
INSERT INTO `ark_function` VALUES (33,'REPORT_STUDY_USER_ROLE_PERMISSIONS','Study User Role Permissions Report lists all user role and permissions for the study in context.',2,NULL);
INSERT INTO `ark_function` VALUES (34,'SUBJECT_CUSTOM_DATA','Data entry for Subject Custom Fields.',1,'tab.module.subject.subjectcustomdata');
INSERT INTO `ark_function` VALUES (36,'LIMS_COLLECTION_CUSTOM_DATA','Data entry for LIMS collection Custom Fields.',1,'tab.module.lims.collectioncustomdata');
INSERT INTO `ark_function` VALUES (38,'BIOSPECIMEN_CUSTOM_DATA','Data entry for Biospecimen Custom Fields.',1,'tab.module.lims.biospecimencustomdata');
INSERT INTO `ark_function` VALUES (41,'BIOSPECIMENUID_TEMPLATE','Manage BiospecimenUid templates for the study,',1,'tab.module.lims.biospecimenuidtemplate');
INSERT INTO `ark_function` VALUES (42,'BARCODE_LABEL','Manage barcode label definitions the study,',1,'tab.module.lims.barcodelabel');
INSERT INTO `ark_function` VALUES (43,'BARCODE_PRINTER','Manage barcode printers for the study,',1,'tab.module.lims.barcodeprinter');
INSERT INTO `ark_function` VALUES (44,'MODULE_FUNCTION','Allows CRUD operations on the ark_module_function table for the Ark application',1,'tab.module.admin.modulefunction');
INSERT INTO `ark_function` VALUES (45,'ROLE','Allows CRUD operations on user roles',1,'tab.module.admin.role');
INSERT INTO `ark_function` VALUES (46,'MODULE_ROLE','Allows CRUD operations on module_role table',1,'tab.module.admin.modulerole');
INSERT INTO `ark_function` VALUES (47,'BIOSPECIMEN_UPLOAD','Uploader for bispecimens',1,'tab.module.lims.biospecimenUpload');
INSERT INTO `ark_function` VALUES (48,'SUBJECT_CUSTOM_FIELD_UPLOAD','Uploader for Subject Custom Fields',1,'tab.module.subject.subjectCustomFieldUpload');
INSERT INTO `ark_function` VALUES (58,'BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD','Uploader for both Biospecimen and Biocollection Custom Fields',1,'tab.module.lims.bioupload');
INSERT INTO `ark_function` VALUES (59,'RESEARCHER','Researcher tab',1,'tab.module.work.researcher');
INSERT INTO `ark_function` VALUES (60,'BILLABLE_ITEM_TYPE','Billable item type tab',1,'tab.module.work.billableitemtype');
INSERT INTO `ark_function` VALUES (61,'WORK_REQUEST','Work Request tab',1,'tab.module.work.workrequest');
INSERT INTO `ark_function` VALUES (62,'BILLABLE_ITEM','Billable Item Tab',1,'tab.module.work.billableitem');
INSERT INTO `ark_function` VALUES (63,'DATA_EXTRACTION','Advanced search for data extraction',1,'tab.reporting.dataextraction');
INSERT INTO `ark_function` VALUES (64,'PEDIGREE','Pedigree visualization',1,'tab.module.subject.pedigree');
INSERT INTO `ark_function` VALUES (65,'GENO_TABLE',NULL,1,'tab.module.geno.table');
INSERT INTO `ark_function` VALUES (66,'GENE',NULL,1,'tab.module.disease.gene');
INSERT INTO `ark_function` VALUES (68,'DISEASE',NULL,1,'tab.module.disease.disease');
INSERT INTO `ark_function` VALUES (69,'CONTACT','Manage the phone and address details.',1,'tab.module.person.contact');
INSERT INTO `ark_function` VALUES (70,'MICRO_SERVICE','Define service access points',1,'tab.module.genomics.microservice');
INSERT INTO `ark_function` VALUES (71,'CONTACT','Manage the phone and address details.',1,'tab.module.person.contact');
INSERT INTO `ark_function` VALUES (72,'DISEASE_CUSTOM_FIELDS',NULL,1,'tab.module.disease.customfield');
INSERT INTO `ark_function` VALUES (73,'DISEASE_AFFECTION',NULL,1,'tab.module.disease.affection');
INSERT INTO `ark_function` VALUES (74,'DATA_CENTER','Declare the available data repos per service',1,'tab.module.genomics.datacenter');
INSERT INTO `ark_function` VALUES (75,'AUDIT',NULL,1,'tab.module.admin.audit');
INSERT INTO `ark_function` VALUES (76,'COMPUTATION','Declare the computational resources for analysis',1,'tab.module.genomics.computation');
INSERT INTO `ark_function` VALUES (77,'SUBJECT_CUSTOM_FIELD_CATEGORY','Manage Custom Fields Category for Subjects.',1,'tab.module.subject.subjectcustomfieldcategory');
INSERT INTO `ark_function` VALUES (78,'ANALYSIS','Initiate the analysis based of available computational and data sources',1,'tab.module.genomics.analysis');
INSERT INTO `ark_function` VALUES (79,'LIMS_CUSTOM_FIELD_CATEGORY','Manage Custom Fields Category for LIMS.',1,'tab.module.lims.limscustomfieldcategory');
INSERT INTO `ark_function` VALUES (80,'LIMS_CUSTOM_FIELD','Manage Custom Fields for LIMS.',1,'tab.module.lims.limscustomfield');
INSERT INTO `ark_function` VALUES (81,'LIMS_CUSTOM_FIELD_UPLOAD','Manage Custom Field Upload for LIMS.',1,'tab.module.lims.limscustomfieldupload');
INSERT INTO `ark_function` VALUES (82,'DATASET_CATEGORY','Phenotypic Data set category use case. This is represented by the Dataset Category tab, under the main Phenotypic(Dataset) Tab.',1,'tab.module.phenotypic.category');
INSERT INTO `ark_function` VALUES (83,'DATASET_CATEGORY_UPLOAD','Phenotypic Data set category upload use case. This is represented by the Dataset Category tab, under the main Phenotypic(Dataset) Tab.',1,'tab.module.phenotypic.categoryupload');
INSERT INTO `ark_function` VALUES (84,'GLOBAL_BIOSPECIMEN_SEARCH',NULL,1,'tab.module.lims.global.biospecimen.search');
/*!40000 ALTER TABLE `ark_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_function_type`
--

DROP TABLE IF EXISTS `ark_function_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_function_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COMMENT='Determines the type of function as a Report or Non-Report fu';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_function_type`
--

LOCK TABLES `ark_function_type` WRITE;
/*!40000 ALTER TABLE `ark_function_type` DISABLE KEYS */;
INSERT INTO `ark_function_type` VALUES (1,'NON-REPORT','A function that is not a report.');
INSERT INTO `ark_function_type` VALUES (2,'REPORT',' A function that is a report.');
/*!40000 ALTER TABLE `ark_function_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_module`
--

DROP TABLE IF EXISTS `ark_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_module` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `ENABLED` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  UNIQUE KEY `ARK_MODULE_NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module`
--

LOCK TABLES `ark_module` WRITE;
/*!40000 ALTER TABLE `ark_module` DISABLE KEYS */;
INSERT INTO `ark_module` VALUES (1,'Study',NULL,1);
INSERT INTO `ark_module` VALUES (2,'Subject',NULL,1);
INSERT INTO `ark_module` VALUES (3,'Datasets',NULL,1);
INSERT INTO `ark_module` VALUES (4,'Genotypic',NULL,1);
INSERT INTO `ark_module` VALUES (5,'LIMS',NULL,1);
INSERT INTO `ark_module` VALUES (6,'Reporting',NULL,1);
INSERT INTO `ark_module` VALUES (8,'Work Tracking','Work Tracking Module',1);
INSERT INTO `ark_module` VALUES (9,'Admin',NULL,1);
INSERT INTO `ark_module` VALUES (10,'Disease',NULL,1);
INSERT INTO `ark_module` VALUES (11,'Genomics','Genomics Module',0);
/*!40000 ALTER TABLE `ark_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_module_function`
--

DROP TABLE IF EXISTS `ark_module_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_module_function` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARK_MODULE_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `FUNCTION_SEQUENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` (`ARK_MODULE_ID`),
  KEY `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=248 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module_function`
--

LOCK TABLES `ark_module_function` WRITE;
/*!40000 ALTER TABLE `ark_module_function` DISABLE KEYS */;
INSERT INTO `ark_module_function` VALUES (27,4,27,1);
INSERT INTO `ark_module_function` VALUES (88,9,29,1);
INSERT INTO `ark_module_function` VALUES (89,9,30,2);
INSERT INTO `ark_module_function` VALUES (90,9,44,3);
INSERT INTO `ark_module_function` VALUES (91,9,45,4);
INSERT INTO `ark_module_function` VALUES (92,9,46,5);
INSERT INTO `ark_module_function` VALUES (93,9,28,6);
INSERT INTO `ark_module_function` VALUES (120,8,59,1);
INSERT INTO `ark_module_function` VALUES (121,8,60,2);
INSERT INTO `ark_module_function` VALUES (122,8,61,3);
INSERT INTO `ark_module_function` VALUES (123,8,62,4);
INSERT INTO `ark_module_function` VALUES (142,6,63,1);
INSERT INTO `ark_module_function` VALUES (170,3,26,1);
INSERT INTO `ark_module_function` VALUES (171,3,12,3);
INSERT INTO `ark_module_function` VALUES (172,3,13,4);
INSERT INTO `ark_module_function` VALUES (173,3,14,5);
INSERT INTO `ark_module_function` VALUES (174,3,16,7);
INSERT INTO `ark_module_function` VALUES (175,1,23,1);
INSERT INTO `ark_module_function` VALUES (176,1,1,2);
INSERT INTO `ark_module_function` VALUES (177,1,2,3);
INSERT INTO `ark_module_function` VALUES (178,1,4,4);
INSERT INTO `ark_module_function` VALUES (179,1,11,6);
INSERT INTO `ark_module_function` VALUES (180,1,48,7);
INSERT INTO `ark_module_function` VALUES (181,1,10,8);
INSERT INTO `ark_module_function` VALUES (182,1,25,8);
INSERT INTO `ark_module_function` VALUES (210,2,24,1);
INSERT INTO `ark_module_function` VALUES (211,2,25,2);
INSERT INTO `ark_module_function` VALUES (212,2,5,3);
INSERT INTO `ark_module_function` VALUES (213,2,34,4);
INSERT INTO `ark_module_function` VALUES (214,2,69,5);
INSERT INTO `ark_module_function` VALUES (216,2,8,7);
INSERT INTO `ark_module_function` VALUES (217,2,9,8);
INSERT INTO `ark_module_function` VALUES (218,2,21,9);
INSERT INTO `ark_module_function` VALUES (219,2,15,10);
INSERT INTO `ark_module_function` VALUES (220,2,17,11);
INSERT INTO `ark_module_function` VALUES (221,2,19,12);
INSERT INTO `ark_module_function` VALUES (222,2,64,13);
INSERT INTO `ark_module_function` VALUES (223,10,66,1);
INSERT INTO `ark_module_function` VALUES (225,10,68,1);
INSERT INTO `ark_module_function` VALUES (226,11,70,1);
INSERT INTO `ark_module_function` VALUES (227,10,72,1);
INSERT INTO `ark_module_function` VALUES (228,10,73,1);
INSERT INTO `ark_module_function` VALUES (229,2,73,13);
INSERT INTO `ark_module_function` VALUES (230,11,74,2);
INSERT INTO `ark_module_function` VALUES (231,9,75,7);
INSERT INTO `ark_module_function` VALUES (232,11,76,3);
INSERT INTO `ark_module_function` VALUES (233,1,77,5);
INSERT INTO `ark_module_function` VALUES (234,11,78,4);
INSERT INTO `ark_module_function` VALUES (238,3,82,1);
INSERT INTO `ark_module_function` VALUES (239,3,83,2);
INSERT INTO `ark_module_function` VALUES (240,5,20,1);
INSERT INTO `ark_module_function` VALUES (241,5,47,2);
INSERT INTO `ark_module_function` VALUES (242,5,79,3);
INSERT INTO `ark_module_function` VALUES (243,5,80,4);
INSERT INTO `ark_module_function` VALUES (244,5,81,5);
INSERT INTO `ark_module_function` VALUES (245,5,43,6);
INSERT INTO `ark_module_function` VALUES (246,5,42,7);
INSERT INTO `ark_module_function` VALUES (247,5,84,8);
/*!40000 ALTER TABLE `ark_module_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_module_role`
--

DROP TABLE IF EXISTS `ark_module_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_module_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARK_MODULE_ID` int(11) NOT NULL,
  `ARK_ROLE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ARK_MODULE_ID` (`ARK_MODULE_ID`),
  KEY `FK_ARK_ROLE_ID` (`ARK_ROLE_ID`),
  CONSTRAINT `fk_ark_module_role_1` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_ark_module_role_2` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module_role`
--

LOCK TABLES `ark_module_role` WRITE;
/*!40000 ALTER TABLE `ark_module_role` DISABLE KEYS */;
INSERT INTO `ark_module_role` VALUES (3,2,4);
INSERT INTO `ark_module_role` VALUES (4,2,5);
INSERT INTO `ark_module_role` VALUES (5,2,6);
INSERT INTO `ark_module_role` VALUES (6,3,7);
INSERT INTO `ark_module_role` VALUES (7,3,8);
INSERT INTO `ark_module_role` VALUES (8,5,9);
INSERT INTO `ark_module_role` VALUES (9,5,10);
INSERT INTO `ark_module_role` VALUES (10,4,11);
INSERT INTO `ark_module_role` VALUES (11,5,12);
INSERT INTO `ark_module_role` VALUES (12,3,13);
INSERT INTO `ark_module_role` VALUES (22,8,2);
INSERT INTO `ark_module_role` VALUES (23,8,15);
INSERT INTO `ark_module_role` VALUES (24,8,16);
INSERT INTO `ark_module_role` VALUES (25,8,17);
INSERT INTO `ark_module_role` VALUES (26,6,18);
INSERT INTO `ark_module_role` VALUES (27,6,19);
INSERT INTO `ark_module_role` VALUES (28,6,20);
INSERT INTO `ark_module_role` VALUES (35,1,2);
INSERT INTO `ark_module_role` VALUES (36,1,3);
INSERT INTO `ark_module_role` VALUES (37,4,21);
INSERT INTO `ark_module_role` VALUES (38,4,22);
INSERT INTO `ark_module_role` VALUES (39,11,23);
INSERT INTO `ark_module_role` VALUES (40,11,24);
INSERT INTO `ark_module_role` VALUES (41,11,25);
INSERT INTO `ark_module_role` VALUES (42,10,26);
INSERT INTO `ark_module_role` VALUES (43,10,27);
INSERT INTO `ark_module_role` VALUES (44,10,28);
/*!40000 ALTER TABLE `ark_module_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_permission`
--

DROP TABLE IF EXISTS `ark_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_permission` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_permission`
--

LOCK TABLES `ark_permission` WRITE;
/*!40000 ALTER TABLE `ark_permission` DISABLE KEYS */;
INSERT INTO `ark_permission` VALUES (1,'CREATE',NULL);
INSERT INTO `ark_permission` VALUES (2,'READ',NULL);
INSERT INTO `ark_permission` VALUES (3,'UPDATE',NULL);
INSERT INTO `ark_permission` VALUES (4,'DELETE',NULL);
/*!40000 ALTER TABLE `ark_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_role`
--

DROP TABLE IF EXISTS `ark_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_role`
--

LOCK TABLES `ark_role` WRITE;
/*!40000 ALTER TABLE `ark_role` DISABLE KEYS */;
INSERT INTO `ark_role` VALUES (1,'Super Administrator','Highest level user of the ARK system');
INSERT INTO `ark_role` VALUES (2,'Study Administrator',NULL);
INSERT INTO `ark_role` VALUES (3,'Study Read-Only user',NULL);
INSERT INTO `ark_role` VALUES (4,'Subject Administrator',NULL);
INSERT INTO `ark_role` VALUES (5,'Subject Data Manager',NULL);
INSERT INTO `ark_role` VALUES (6,'Subject Read-Only user',NULL);
INSERT INTO `ark_role` VALUES (7,'Pheno Read-Only user',NULL);
INSERT INTO `ark_role` VALUES (8,'Pheno Data Manager',NULL);
INSERT INTO `ark_role` VALUES (9,'LIMS Read-Only user',NULL);
INSERT INTO `ark_role` VALUES (10,'LIMS Data Manager',NULL);
INSERT INTO `ark_role` VALUES (11,'Geno Read-Only User',NULL);
INSERT INTO `ark_role` VALUES (12,'LIMS Administrator',NULL);
INSERT INTO `ark_role` VALUES (13,'Pheno Administrator',NULL);
INSERT INTO `ark_role` VALUES (14,'New Role',NULL);
INSERT INTO `ark_role` VALUES (15,'Work Tracking Administrator','Work Tracking Administrator');
INSERT INTO `ark_role` VALUES (16,'Work Tracking Data Manager','Work Tracking Data Manager');
INSERT INTO `ark_role` VALUES (17,'Work Tracking Read-Only User','Work Tracking Read-Only User');
INSERT INTO `ark_role` VALUES (18,'Reporting Administrator','Reporting Administrator');
INSERT INTO `ark_role` VALUES (19,'Reporting Data Manager','Reporting Data Manager');
INSERT INTO `ark_role` VALUES (20,'Reporting Read-Only User','Reporting Read-Only User');
INSERT INTO `ark_role` VALUES (21,'Geno Administrator',NULL);
INSERT INTO `ark_role` VALUES (22,'Geno Data Manager',NULL);
INSERT INTO `ark_role` VALUES (23,'Genomics Administrator',NULL);
INSERT INTO `ark_role` VALUES (24,'Genomics Data Manager',NULL);
INSERT INTO `ark_role` VALUES (25,'Genomics Read-Only user',NULL);
INSERT INTO `ark_role` VALUES (26,'Disease Administrator',NULL);
INSERT INTO `ark_role` VALUES (27,'Disease Data Manager',NULL);
INSERT INTO `ark_role` VALUES (28,'Disease Read-Only user',NULL);
/*!40000 ALTER TABLE `ark_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_role_policy_template`
--

DROP TABLE IF EXISTS `ark_role_policy_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_role_policy_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARK_ROLE_ID` int(11) NOT NULL,
  `ARK_MODULE_ID` int(11) DEFAULT NULL,
  `ARK_FUNCTION_ID` int(11) DEFAULT NULL,
  `ARK_PERMISSION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ROLE_TMPLT_ARK_ROLE_ID` (`ARK_ROLE_ID`),
  KEY `FK_ROLE_TMPLT_ARK_MODULE_ID` (`ARK_MODULE_ID`),
  KEY `FK_ROLE_TMPLT_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  KEY `FK_ROLE_TMPLT_ARK_PRMSN_ID` (`ARK_PERMISSION_ID`),
  CONSTRAINT `FK_ROLE_TMPLT_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_PRMSN_ID` FOREIGN KEY (`ARK_PERMISSION_ID`) REFERENCES `ark_permission` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_ROLE_ID` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=890 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_role_policy_template`
--

LOCK TABLES `ark_role_policy_template` WRITE;
/*!40000 ALTER TABLE `ark_role_policy_template` DISABLE KEYS */;
INSERT INTO `ark_role_policy_template` VALUES (1,1,NULL,NULL,1);
INSERT INTO `ark_role_policy_template` VALUES (2,1,NULL,NULL,2);
INSERT INTO `ark_role_policy_template` VALUES (3,1,NULL,NULL,3);
INSERT INTO `ark_role_policy_template` VALUES (4,1,NULL,NULL,4);
INSERT INTO `ark_role_policy_template` VALUES (7,2,1,2,1);
INSERT INTO `ark_role_policy_template` VALUES (8,2,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (9,2,1,2,3);
INSERT INTO `ark_role_policy_template` VALUES (12,2,1,4,1);
INSERT INTO `ark_role_policy_template` VALUES (13,2,1,4,2);
INSERT INTO `ark_role_policy_template` VALUES (14,2,1,4,3);
INSERT INTO `ark_role_policy_template` VALUES (15,3,1,1,2);
INSERT INTO `ark_role_policy_template` VALUES (16,3,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (20,4,2,5,1);
INSERT INTO `ark_role_policy_template` VALUES (21,4,2,5,2);
INSERT INTO `ark_role_policy_template` VALUES (22,4,2,5,3);
INSERT INTO `ark_role_policy_template` VALUES (23,4,2,6,1);
INSERT INTO `ark_role_policy_template` VALUES (24,4,2,6,2);
INSERT INTO `ark_role_policy_template` VALUES (25,4,2,6,3);
INSERT INTO `ark_role_policy_template` VALUES (26,4,2,6,4);
INSERT INTO `ark_role_policy_template` VALUES (27,4,2,7,1);
INSERT INTO `ark_role_policy_template` VALUES (28,4,2,7,2);
INSERT INTO `ark_role_policy_template` VALUES (29,4,2,7,3);
INSERT INTO `ark_role_policy_template` VALUES (30,4,2,7,4);
INSERT INTO `ark_role_policy_template` VALUES (31,4,2,8,1);
INSERT INTO `ark_role_policy_template` VALUES (32,4,2,8,2);
INSERT INTO `ark_role_policy_template` VALUES (33,4,2,8,3);
INSERT INTO `ark_role_policy_template` VALUES (34,4,2,8,4);
INSERT INTO `ark_role_policy_template` VALUES (35,4,2,9,1);
INSERT INTO `ark_role_policy_template` VALUES (36,4,2,9,2);
INSERT INTO `ark_role_policy_template` VALUES (37,4,2,9,3);
INSERT INTO `ark_role_policy_template` VALUES (38,4,2,9,4);
INSERT INTO `ark_role_policy_template` VALUES (48,4,2,10,1);
INSERT INTO `ark_role_policy_template` VALUES (49,4,2,10,2);
INSERT INTO `ark_role_policy_template` VALUES (50,4,2,10,3);
INSERT INTO `ark_role_policy_template` VALUES (51,4,2,11,1);
INSERT INTO `ark_role_policy_template` VALUES (52,4,2,11,2);
INSERT INTO `ark_role_policy_template` VALUES (53,4,2,11,3);
INSERT INTO `ark_role_policy_template` VALUES (64,6,2,5,2);
INSERT INTO `ark_role_policy_template` VALUES (65,6,2,6,2);
INSERT INTO `ark_role_policy_template` VALUES (66,6,2,7,2);
INSERT INTO `ark_role_policy_template` VALUES (67,6,2,8,2);
INSERT INTO `ark_role_policy_template` VALUES (68,6,2,9,2);
INSERT INTO `ark_role_policy_template` VALUES (69,6,2,34,2);
INSERT INTO `ark_role_policy_template` VALUES (79,9,5,17,2);
INSERT INTO `ark_role_policy_template` VALUES (80,9,5,18,2);
INSERT INTO `ark_role_policy_template` VALUES (81,9,5,19,2);
INSERT INTO `ark_role_policy_template` VALUES (82,9,5,20,2);
INSERT INTO `ark_role_policy_template` VALUES (83,7,3,12,2);
INSERT INTO `ark_role_policy_template` VALUES (84,7,3,13,2);
INSERT INTO `ark_role_policy_template` VALUES (85,7,3,14,2);
INSERT INTO `ark_role_policy_template` VALUES (86,7,3,15,2);
INSERT INTO `ark_role_policy_template` VALUES (87,7,3,16,2);
INSERT INTO `ark_role_policy_template` VALUES (92,2,1,23,2);
INSERT INTO `ark_role_policy_template` VALUES (93,3,1,23,2);
INSERT INTO `ark_role_policy_template` VALUES (94,4,2,24,2);
INSERT INTO `ark_role_policy_template` VALUES (95,5,2,24,2);
INSERT INTO `ark_role_policy_template` VALUES (96,6,2,24,2);
INSERT INTO `ark_role_policy_template` VALUES (98,5,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (99,6,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (100,7,3,26,2);
INSERT INTO `ark_role_policy_template` VALUES (104,11,4,65,2);
INSERT INTO `ark_role_policy_template` VALUES (107,6,2,21,2);
INSERT INTO `ark_role_policy_template` VALUES (112,4,2,21,1);
INSERT INTO `ark_role_policy_template` VALUES (113,4,2,21,2);
INSERT INTO `ark_role_policy_template` VALUES (114,4,2,21,3);
INSERT INTO `ark_role_policy_template` VALUES (115,4,2,21,4);
INSERT INTO `ark_role_policy_template` VALUES (116,12,5,17,1);
INSERT INTO `ark_role_policy_template` VALUES (117,12,5,17,2);
INSERT INTO `ark_role_policy_template` VALUES (118,12,5,17,3);
INSERT INTO `ark_role_policy_template` VALUES (119,12,5,17,4);
INSERT INTO `ark_role_policy_template` VALUES (120,12,5,18,1);
INSERT INTO `ark_role_policy_template` VALUES (121,12,5,18,2);
INSERT INTO `ark_role_policy_template` VALUES (122,12,5,18,3);
INSERT INTO `ark_role_policy_template` VALUES (123,12,5,18,4);
INSERT INTO `ark_role_policy_template` VALUES (124,12,5,19,1);
INSERT INTO `ark_role_policy_template` VALUES (125,12,5,19,2);
INSERT INTO `ark_role_policy_template` VALUES (126,12,5,19,3);
INSERT INTO `ark_role_policy_template` VALUES (127,12,5,19,4);
INSERT INTO `ark_role_policy_template` VALUES (128,12,5,20,1);
INSERT INTO `ark_role_policy_template` VALUES (129,12,5,20,2);
INSERT INTO `ark_role_policy_template` VALUES (130,12,5,20,3);
INSERT INTO `ark_role_policy_template` VALUES (131,12,5,20,4);
INSERT INTO `ark_role_policy_template` VALUES (133,13,3,13,1);
INSERT INTO `ark_role_policy_template` VALUES (138,13,3,26,2);
INSERT INTO `ark_role_policy_template` VALUES (142,1,9,28,1);
INSERT INTO `ark_role_policy_template` VALUES (143,1,9,28,2);
INSERT INTO `ark_role_policy_template` VALUES (144,1,9,28,3);
INSERT INTO `ark_role_policy_template` VALUES (145,1,9,28,4);
INSERT INTO `ark_role_policy_template` VALUES (146,13,3,22,2);
INSERT INTO `ark_role_policy_template` VALUES (147,7,3,22,2);
INSERT INTO `ark_role_policy_template` VALUES (148,13,3,12,1);
INSERT INTO `ark_role_policy_template` VALUES (149,13,3,12,2);
INSERT INTO `ark_role_policy_template` VALUES (184,4,2,34,1);
INSERT INTO `ark_role_policy_template` VALUES (185,4,2,34,2);
INSERT INTO `ark_role_policy_template` VALUES (186,4,2,34,3);
INSERT INTO `ark_role_policy_template` VALUES (187,4,2,34,4);
INSERT INTO `ark_role_policy_template` VALUES (195,12,5,36,1);
INSERT INTO `ark_role_policy_template` VALUES (196,12,5,36,2);
INSERT INTO `ark_role_policy_template` VALUES (197,12,5,36,3);
INSERT INTO `ark_role_policy_template` VALUES (198,12,5,36,4);
INSERT INTO `ark_role_policy_template` VALUES (203,9,5,36,2);
INSERT INTO `ark_role_policy_template` VALUES (207,12,5,38,1);
INSERT INTO `ark_role_policy_template` VALUES (208,12,5,38,2);
INSERT INTO `ark_role_policy_template` VALUES (209,12,5,38,3);
INSERT INTO `ark_role_policy_template` VALUES (210,12,5,38,4);
INSERT INTO `ark_role_policy_template` VALUES (215,9,5,38,2);
INSERT INTO `ark_role_policy_template` VALUES (233,3,1,3,2);
INSERT INTO `ark_role_policy_template` VALUES (246,14,1,23,2);
INSERT INTO `ark_role_policy_template` VALUES (247,14,1,1,2);
INSERT INTO `ark_role_policy_template` VALUES (248,14,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (249,14,1,4,2);
INSERT INTO `ark_role_policy_template` VALUES (250,2,1,23,2);
INSERT INTO `ark_role_policy_template` VALUES (252,2,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (253,2,1,4,2);
INSERT INTO `ark_role_policy_template` VALUES (254,3,1,23,2);
INSERT INTO `ark_role_policy_template` VALUES (255,3,1,1,2);
INSERT INTO `ark_role_policy_template` VALUES (256,3,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (257,3,1,4,2);
INSERT INTO `ark_role_policy_template` VALUES (258,4,1,23,2);
INSERT INTO `ark_role_policy_template` VALUES (259,4,1,1,2);
INSERT INTO `ark_role_policy_template` VALUES (260,4,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (261,4,1,4,2);
INSERT INTO `ark_role_policy_template` VALUES (262,2,1,33,2);
INSERT INTO `ark_role_policy_template` VALUES (263,13,3,15,1);
INSERT INTO `ark_role_policy_template` VALUES (264,13,3,15,2);
INSERT INTO `ark_role_policy_template` VALUES (265,13,3,15,3);
INSERT INTO `ark_role_policy_template` VALUES (266,13,3,15,4);
INSERT INTO `ark_role_policy_template` VALUES (267,13,3,14,1);
INSERT INTO `ark_role_policy_template` VALUES (268,13,3,14,2);
INSERT INTO `ark_role_policy_template` VALUES (269,13,3,14,3);
INSERT INTO `ark_role_policy_template` VALUES (270,13,3,14,4);
INSERT INTO `ark_role_policy_template` VALUES (283,12,5,47,1);
INSERT INTO `ark_role_policy_template` VALUES (284,12,5,47,2);
INSERT INTO `ark_role_policy_template` VALUES (285,12,5,47,3);
INSERT INTO `ark_role_policy_template` VALUES (286,12,5,47,4);
INSERT INTO `ark_role_policy_template` VALUES (287,2,1,1,1);
INSERT INTO `ark_role_policy_template` VALUES (288,2,1,1,2);
INSERT INTO `ark_role_policy_template` VALUES (289,2,1,1,3);
INSERT INTO `ark_role_policy_template` VALUES (290,12,5,42,1);
INSERT INTO `ark_role_policy_template` VALUES (291,12,5,42,2);
INSERT INTO `ark_role_policy_template` VALUES (292,12,5,42,3);
INSERT INTO `ark_role_policy_template` VALUES (293,12,5,42,4);
INSERT INTO `ark_role_policy_template` VALUES (294,12,5,43,1);
INSERT INTO `ark_role_policy_template` VALUES (295,12,5,43,2);
INSERT INTO `ark_role_policy_template` VALUES (296,12,5,43,3);
INSERT INTO `ark_role_policy_template` VALUES (297,12,5,43,4);
INSERT INTO `ark_role_policy_template` VALUES (298,4,2,48,1);
INSERT INTO `ark_role_policy_template` VALUES (299,4,2,48,2);
INSERT INTO `ark_role_policy_template` VALUES (300,4,2,48,3);
INSERT INTO `ark_role_policy_template` VALUES (301,4,2,48,4);
INSERT INTO `ark_role_policy_template` VALUES (310,12,5,58,1);
INSERT INTO `ark_role_policy_template` VALUES (311,12,5,58,2);
INSERT INTO `ark_role_policy_template` VALUES (312,12,5,58,3);
INSERT INTO `ark_role_policy_template` VALUES (313,12,5,58,4);
INSERT INTO `ark_role_policy_template` VALUES (318,2,8,60,1);
INSERT INTO `ark_role_policy_template` VALUES (319,2,8,60,2);
INSERT INTO `ark_role_policy_template` VALUES (320,2,8,60,3);
INSERT INTO `ark_role_policy_template` VALUES (321,2,8,60,4);
INSERT INTO `ark_role_policy_template` VALUES (322,2,8,61,1);
INSERT INTO `ark_role_policy_template` VALUES (323,2,8,61,2);
INSERT INTO `ark_role_policy_template` VALUES (324,2,8,61,3);
INSERT INTO `ark_role_policy_template` VALUES (325,2,8,61,4);
INSERT INTO `ark_role_policy_template` VALUES (326,2,8,62,1);
INSERT INTO `ark_role_policy_template` VALUES (327,2,8,62,2);
INSERT INTO `ark_role_policy_template` VALUES (328,2,8,62,3);
INSERT INTO `ark_role_policy_template` VALUES (329,2,8,62,4);
INSERT INTO `ark_role_policy_template` VALUES (330,15,8,59,1);
INSERT INTO `ark_role_policy_template` VALUES (331,15,8,59,2);
INSERT INTO `ark_role_policy_template` VALUES (332,15,8,59,3);
INSERT INTO `ark_role_policy_template` VALUES (333,15,8,59,4);
INSERT INTO `ark_role_policy_template` VALUES (334,15,8,60,1);
INSERT INTO `ark_role_policy_template` VALUES (335,15,8,60,2);
INSERT INTO `ark_role_policy_template` VALUES (336,15,8,60,3);
INSERT INTO `ark_role_policy_template` VALUES (337,15,8,60,4);
INSERT INTO `ark_role_policy_template` VALUES (338,15,8,61,1);
INSERT INTO `ark_role_policy_template` VALUES (339,15,8,61,2);
INSERT INTO `ark_role_policy_template` VALUES (340,15,8,61,3);
INSERT INTO `ark_role_policy_template` VALUES (341,15,8,61,4);
INSERT INTO `ark_role_policy_template` VALUES (342,15,8,62,1);
INSERT INTO `ark_role_policy_template` VALUES (343,15,8,62,2);
INSERT INTO `ark_role_policy_template` VALUES (344,15,8,62,3);
INSERT INTO `ark_role_policy_template` VALUES (345,15,8,62,4);
INSERT INTO `ark_role_policy_template` VALUES (346,16,8,59,1);
INSERT INTO `ark_role_policy_template` VALUES (347,16,8,59,2);
INSERT INTO `ark_role_policy_template` VALUES (348,16,8,59,3);
INSERT INTO `ark_role_policy_template` VALUES (349,16,8,60,1);
INSERT INTO `ark_role_policy_template` VALUES (350,16,8,60,2);
INSERT INTO `ark_role_policy_template` VALUES (351,16,8,60,3);
INSERT INTO `ark_role_policy_template` VALUES (352,16,8,61,1);
INSERT INTO `ark_role_policy_template` VALUES (353,16,8,61,2);
INSERT INTO `ark_role_policy_template` VALUES (354,16,8,61,3);
INSERT INTO `ark_role_policy_template` VALUES (355,16,8,62,1);
INSERT INTO `ark_role_policy_template` VALUES (356,16,8,62,2);
INSERT INTO `ark_role_policy_template` VALUES (357,16,8,62,3);
INSERT INTO `ark_role_policy_template` VALUES (358,17,8,59,2);
INSERT INTO `ark_role_policy_template` VALUES (359,17,8,60,2);
INSERT INTO `ark_role_policy_template` VALUES (360,17,8,61,2);
INSERT INTO `ark_role_policy_template` VALUES (361,17,8,62,2);
INSERT INTO `ark_role_policy_template` VALUES (370,20,6,63,2);
INSERT INTO `ark_role_policy_template` VALUES (371,2,1,10,2);
INSERT INTO `ark_role_policy_template` VALUES (372,2,1,10,1);
INSERT INTO `ark_role_policy_template` VALUES (373,2,1,10,3);
INSERT INTO `ark_role_policy_template` VALUES (382,10,5,20,1);
INSERT INTO `ark_role_policy_template` VALUES (383,10,5,20,2);
INSERT INTO `ark_role_policy_template` VALUES (384,10,5,20,3);
INSERT INTO `ark_role_policy_template` VALUES (408,4,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (420,5,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (432,6,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (444,7,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (456,8,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (468,13,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (480,13,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (492,8,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (504,7,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (516,4,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (528,5,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (540,6,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (552,18,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (585,4,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (597,5,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (609,6,2,25,2);
INSERT INTO `ark_role_policy_template` VALUES (610,2,8,59,1);
INSERT INTO `ark_role_policy_template` VALUES (611,2,8,59,2);
INSERT INTO `ark_role_policy_template` VALUES (612,2,8,59,3);
INSERT INTO `ark_role_policy_template` VALUES (613,2,8,59,4);
INSERT INTO `ark_role_policy_template` VALUES (643,10,5,43,1);
INSERT INTO `ark_role_policy_template` VALUES (644,10,5,43,2);
INSERT INTO `ark_role_policy_template` VALUES (645,10,5,43,3);
INSERT INTO `ark_role_policy_template` VALUES (646,13,3,16,1);
INSERT INTO `ark_role_policy_template` VALUES (647,13,3,16,2);
INSERT INTO `ark_role_policy_template` VALUES (648,13,3,16,3);
INSERT INTO `ark_role_policy_template` VALUES (649,13,3,16,4);
INSERT INTO `ark_role_policy_template` VALUES (656,2,1,3,2);
INSERT INTO `ark_role_policy_template` VALUES (657,2,1,3,3);
INSERT INTO `ark_role_policy_template` VALUES (658,18,6,63,1);
INSERT INTO `ark_role_policy_template` VALUES (659,18,6,63,2);
INSERT INTO `ark_role_policy_template` VALUES (660,18,6,63,3);
INSERT INTO `ark_role_policy_template` VALUES (661,18,6,63,4);
INSERT INTO `ark_role_policy_template` VALUES (665,21,4,65,3);
INSERT INTO `ark_role_policy_template` VALUES (667,22,4,65,2);
INSERT INTO `ark_role_policy_template` VALUES (668,22,4,65,3);
INSERT INTO `ark_role_policy_template` VALUES (669,21,4,65,1);
INSERT INTO `ark_role_policy_template` VALUES (670,21,4,65,2);
INSERT INTO `ark_role_policy_template` VALUES (671,21,4,65,3);
INSERT INTO `ark_role_policy_template` VALUES (672,21,4,65,4);
INSERT INTO `ark_role_policy_template` VALUES (673,1,4,65,1);
INSERT INTO `ark_role_policy_template` VALUES (675,1,4,65,2);
INSERT INTO `ark_role_policy_template` VALUES (676,1,4,65,3);
INSERT INTO `ark_role_policy_template` VALUES (677,1,4,65,4);
INSERT INTO `ark_role_policy_template` VALUES (678,5,2,5,1);
INSERT INTO `ark_role_policy_template` VALUES (679,5,2,5,2);
INSERT INTO `ark_role_policy_template` VALUES (680,5,2,5,3);
INSERT INTO `ark_role_policy_template` VALUES (681,5,2,6,1);
INSERT INTO `ark_role_policy_template` VALUES (682,5,2,6,2);
INSERT INTO `ark_role_policy_template` VALUES (683,5,2,6,3);
INSERT INTO `ark_role_policy_template` VALUES (684,5,2,7,1);
INSERT INTO `ark_role_policy_template` VALUES (685,5,2,7,2);
INSERT INTO `ark_role_policy_template` VALUES (686,5,2,7,3);
INSERT INTO `ark_role_policy_template` VALUES (687,5,2,8,1);
INSERT INTO `ark_role_policy_template` VALUES (688,5,2,8,2);
INSERT INTO `ark_role_policy_template` VALUES (689,5,2,8,3);
INSERT INTO `ark_role_policy_template` VALUES (690,5,2,9,1);
INSERT INTO `ark_role_policy_template` VALUES (691,5,2,9,2);
INSERT INTO `ark_role_policy_template` VALUES (692,5,2,9,3);
INSERT INTO `ark_role_policy_template` VALUES (693,5,2,10,1);
INSERT INTO `ark_role_policy_template` VALUES (694,5,2,10,2);
INSERT INTO `ark_role_policy_template` VALUES (695,5,2,10,3);
INSERT INTO `ark_role_policy_template` VALUES (696,5,2,21,1);
INSERT INTO `ark_role_policy_template` VALUES (697,5,2,21,2);
INSERT INTO `ark_role_policy_template` VALUES (698,5,2,21,3);
INSERT INTO `ark_role_policy_template` VALUES (699,5,2,34,1);
INSERT INTO `ark_role_policy_template` VALUES (700,5,2,34,2);
INSERT INTO `ark_role_policy_template` VALUES (701,5,2,34,3);
INSERT INTO `ark_role_policy_template` VALUES (702,10,5,17,1);
INSERT INTO `ark_role_policy_template` VALUES (703,10,5,17,2);
INSERT INTO `ark_role_policy_template` VALUES (704,10,5,17,3);
INSERT INTO `ark_role_policy_template` VALUES (705,10,5,18,1);
INSERT INTO `ark_role_policy_template` VALUES (706,10,5,18,2);
INSERT INTO `ark_role_policy_template` VALUES (707,10,5,18,3);
INSERT INTO `ark_role_policy_template` VALUES (708,10,5,19,1);
INSERT INTO `ark_role_policy_template` VALUES (709,10,5,19,2);
INSERT INTO `ark_role_policy_template` VALUES (710,10,5,19,3);
INSERT INTO `ark_role_policy_template` VALUES (711,10,5,36,1);
INSERT INTO `ark_role_policy_template` VALUES (712,10,5,36,2);
INSERT INTO `ark_role_policy_template` VALUES (713,10,5,36,3);
INSERT INTO `ark_role_policy_template` VALUES (714,10,5,38,1);
INSERT INTO `ark_role_policy_template` VALUES (715,10,5,38,2);
INSERT INTO `ark_role_policy_template` VALUES (716,10,5,38,3);
INSERT INTO `ark_role_policy_template` VALUES (721,10,5,42,1);
INSERT INTO `ark_role_policy_template` VALUES (722,10,5,42,2);
INSERT INTO `ark_role_policy_template` VALUES (723,10,5,42,3);
INSERT INTO `ark_role_policy_template` VALUES (724,10,5,47,1);
INSERT INTO `ark_role_policy_template` VALUES (725,10,5,47,2);
INSERT INTO `ark_role_policy_template` VALUES (726,10,5,47,3);
INSERT INTO `ark_role_policy_template` VALUES (727,19,6,63,1);
INSERT INTO `ark_role_policy_template` VALUES (728,19,6,63,2);
INSERT INTO `ark_role_policy_template` VALUES (729,19,6,63,3);
INSERT INTO `ark_role_policy_template` VALUES (730,23,11,70,1);
INSERT INTO `ark_role_policy_template` VALUES (731,23,11,70,2);
INSERT INTO `ark_role_policy_template` VALUES (732,23,11,70,3);
INSERT INTO `ark_role_policy_template` VALUES (733,23,11,70,4);
INSERT INTO `ark_role_policy_template` VALUES (734,24,11,70,1);
INSERT INTO `ark_role_policy_template` VALUES (735,24,11,70,2);
INSERT INTO `ark_role_policy_template` VALUES (736,24,11,70,3);
INSERT INTO `ark_role_policy_template` VALUES (737,25,11,70,2);
INSERT INTO `ark_role_policy_template` VALUES (739,4,2,69,1);
INSERT INTO `ark_role_policy_template` VALUES (740,4,2,69,2);
INSERT INTO `ark_role_policy_template` VALUES (741,4,2,69,3);
INSERT INTO `ark_role_policy_template` VALUES (742,4,2,69,4);
INSERT INTO `ark_role_policy_template` VALUES (744,5,2,69,1);
INSERT INTO `ark_role_policy_template` VALUES (745,5,2,69,2);
INSERT INTO `ark_role_policy_template` VALUES (746,5,2,69,3);
INSERT INTO `ark_role_policy_template` VALUES (747,6,2,69,2);
INSERT INTO `ark_role_policy_template` VALUES (748,26,10,68,1);
INSERT INTO `ark_role_policy_template` VALUES (749,26,10,68,2);
INSERT INTO `ark_role_policy_template` VALUES (750,26,10,68,3);
INSERT INTO `ark_role_policy_template` VALUES (751,26,10,68,4);
INSERT INTO `ark_role_policy_template` VALUES (752,27,10,68,1);
INSERT INTO `ark_role_policy_template` VALUES (753,27,10,68,2);
INSERT INTO `ark_role_policy_template` VALUES (754,27,10,68,3);
INSERT INTO `ark_role_policy_template` VALUES (755,28,10,68,2);
INSERT INTO `ark_role_policy_template` VALUES (756,26,10,72,1);
INSERT INTO `ark_role_policy_template` VALUES (757,26,10,72,2);
INSERT INTO `ark_role_policy_template` VALUES (758,26,10,72,3);
INSERT INTO `ark_role_policy_template` VALUES (759,26,10,72,4);
INSERT INTO `ark_role_policy_template` VALUES (760,27,10,72,1);
INSERT INTO `ark_role_policy_template` VALUES (761,27,10,72,2);
INSERT INTO `ark_role_policy_template` VALUES (762,27,10,72,3);
INSERT INTO `ark_role_policy_template` VALUES (763,28,10,72,2);
INSERT INTO `ark_role_policy_template` VALUES (764,26,10,73,1);
INSERT INTO `ark_role_policy_template` VALUES (765,26,10,73,2);
INSERT INTO `ark_role_policy_template` VALUES (766,26,10,73,3);
INSERT INTO `ark_role_policy_template` VALUES (767,26,10,73,4);
INSERT INTO `ark_role_policy_template` VALUES (768,27,10,73,1);
INSERT INTO `ark_role_policy_template` VALUES (769,27,10,73,2);
INSERT INTO `ark_role_policy_template` VALUES (770,27,10,73,3);
INSERT INTO `ark_role_policy_template` VALUES (771,28,10,73,2);
INSERT INTO `ark_role_policy_template` VALUES (776,8,3,12,1);
INSERT INTO `ark_role_policy_template` VALUES (777,8,3,12,2);
INSERT INTO `ark_role_policy_template` VALUES (778,8,3,12,3);
INSERT INTO `ark_role_policy_template` VALUES (779,8,3,12,4);
INSERT INTO `ark_role_policy_template` VALUES (780,8,3,13,1);
INSERT INTO `ark_role_policy_template` VALUES (781,8,3,13,2);
INSERT INTO `ark_role_policy_template` VALUES (782,8,3,13,3);
INSERT INTO `ark_role_policy_template` VALUES (783,8,3,13,4);
INSERT INTO `ark_role_policy_template` VALUES (784,8,3,14,1);
INSERT INTO `ark_role_policy_template` VALUES (785,8,3,14,2);
INSERT INTO `ark_role_policy_template` VALUES (786,8,3,14,3);
INSERT INTO `ark_role_policy_template` VALUES (787,8,3,14,4);
INSERT INTO `ark_role_policy_template` VALUES (791,8,3,26,1);
INSERT INTO `ark_role_policy_template` VALUES (792,8,3,26,2);
INSERT INTO `ark_role_policy_template` VALUES (793,8,3,26,3);
INSERT INTO `ark_role_policy_template` VALUES (794,8,3,22,1);
INSERT INTO `ark_role_policy_template` VALUES (795,8,3,22,2);
INSERT INTO `ark_role_policy_template` VALUES (796,8,3,22,3);
INSERT INTO `ark_role_policy_template` VALUES (797,8,3,15,1);
INSERT INTO `ark_role_policy_template` VALUES (798,8,3,15,2);
INSERT INTO `ark_role_policy_template` VALUES (799,8,3,15,3);
INSERT INTO `ark_role_policy_template` VALUES (800,8,3,15,4);
INSERT INTO `ark_role_policy_template` VALUES (801,8,3,16,1);
INSERT INTO `ark_role_policy_template` VALUES (802,8,3,16,2);
INSERT INTO `ark_role_policy_template` VALUES (803,8,3,16,3);
INSERT INTO `ark_role_policy_template` VALUES (804,8,3,16,4);
INSERT INTO `ark_role_policy_template` VALUES (805,2,1,77,1);
INSERT INTO `ark_role_policy_template` VALUES (806,2,1,77,2);
INSERT INTO `ark_role_policy_template` VALUES (807,2,1,77,3);
INSERT INTO `ark_role_policy_template` VALUES (808,2,1,77,4);
INSERT INTO `ark_role_policy_template` VALUES (809,3,1,77,2);
INSERT INTO `ark_role_policy_template` VALUES (810,2,1,48,1);
INSERT INTO `ark_role_policy_template` VALUES (811,2,1,48,2);
INSERT INTO `ark_role_policy_template` VALUES (812,2,1,48,3);
INSERT INTO `ark_role_policy_template` VALUES (813,2,1,48,4);
INSERT INTO `ark_role_policy_template` VALUES (814,3,1,48,2);
INSERT INTO `ark_role_policy_template` VALUES (815,23,11,78,1);
INSERT INTO `ark_role_policy_template` VALUES (816,23,11,78,2);
INSERT INTO `ark_role_policy_template` VALUES (817,23,11,78,3);
INSERT INTO `ark_role_policy_template` VALUES (818,23,11,78,4);
INSERT INTO `ark_role_policy_template` VALUES (819,24,11,78,1);
INSERT INTO `ark_role_policy_template` VALUES (820,24,11,78,2);
INSERT INTO `ark_role_policy_template` VALUES (821,24,11,78,3);
INSERT INTO `ark_role_policy_template` VALUES (822,25,11,78,2);
INSERT INTO `ark_role_policy_template` VALUES (823,23,11,76,1);
INSERT INTO `ark_role_policy_template` VALUES (824,23,11,76,2);
INSERT INTO `ark_role_policy_template` VALUES (825,23,11,76,3);
INSERT INTO `ark_role_policy_template` VALUES (826,23,11,76,4);
INSERT INTO `ark_role_policy_template` VALUES (827,24,11,76,1);
INSERT INTO `ark_role_policy_template` VALUES (828,24,11,76,2);
INSERT INTO `ark_role_policy_template` VALUES (829,24,11,76,3);
INSERT INTO `ark_role_policy_template` VALUES (830,25,11,76,2);
INSERT INTO `ark_role_policy_template` VALUES (831,23,11,74,1);
INSERT INTO `ark_role_policy_template` VALUES (832,23,11,74,2);
INSERT INTO `ark_role_policy_template` VALUES (833,23,11,74,3);
INSERT INTO `ark_role_policy_template` VALUES (834,23,11,74,4);
INSERT INTO `ark_role_policy_template` VALUES (835,24,11,74,1);
INSERT INTO `ark_role_policy_template` VALUES (836,24,11,74,2);
INSERT INTO `ark_role_policy_template` VALUES (837,24,11,74,3);
INSERT INTO `ark_role_policy_template` VALUES (838,25,11,74,2);
INSERT INTO `ark_role_policy_template` VALUES (839,23,11,78,1);
INSERT INTO `ark_role_policy_template` VALUES (840,23,11,78,2);
INSERT INTO `ark_role_policy_template` VALUES (841,23,11,78,3);
INSERT INTO `ark_role_policy_template` VALUES (842,23,11,78,4);
INSERT INTO `ark_role_policy_template` VALUES (843,24,11,78,1);
INSERT INTO `ark_role_policy_template` VALUES (844,24,11,78,2);
INSERT INTO `ark_role_policy_template` VALUES (845,24,11,78,3);
INSERT INTO `ark_role_policy_template` VALUES (846,25,11,78,2);
INSERT INTO `ark_role_policy_template` VALUES (847,23,11,76,1);
INSERT INTO `ark_role_policy_template` VALUES (848,23,11,76,2);
INSERT INTO `ark_role_policy_template` VALUES (849,23,11,76,3);
INSERT INTO `ark_role_policy_template` VALUES (850,23,11,76,4);
INSERT INTO `ark_role_policy_template` VALUES (851,24,11,76,1);
INSERT INTO `ark_role_policy_template` VALUES (852,24,11,76,2);
INSERT INTO `ark_role_policy_template` VALUES (853,24,11,76,3);
INSERT INTO `ark_role_policy_template` VALUES (854,25,11,76,2);
INSERT INTO `ark_role_policy_template` VALUES (855,23,11,74,1);
INSERT INTO `ark_role_policy_template` VALUES (856,23,11,74,2);
INSERT INTO `ark_role_policy_template` VALUES (857,23,11,74,3);
INSERT INTO `ark_role_policy_template` VALUES (858,23,11,74,4);
INSERT INTO `ark_role_policy_template` VALUES (859,24,11,74,1);
INSERT INTO `ark_role_policy_template` VALUES (860,24,11,74,2);
INSERT INTO `ark_role_policy_template` VALUES (861,24,11,74,3);
INSERT INTO `ark_role_policy_template` VALUES (862,25,11,74,2);
INSERT INTO `ark_role_policy_template` VALUES (863,12,5,79,1);
INSERT INTO `ark_role_policy_template` VALUES (864,12,5,79,2);
INSERT INTO `ark_role_policy_template` VALUES (865,12,5,79,3);
INSERT INTO `ark_role_policy_template` VALUES (866,12,5,80,1);
INSERT INTO `ark_role_policy_template` VALUES (867,12,5,80,2);
INSERT INTO `ark_role_policy_template` VALUES (868,12,5,80,3);
INSERT INTO `ark_role_policy_template` VALUES (869,12,5,81,1);
INSERT INTO `ark_role_policy_template` VALUES (870,12,5,81,2);
INSERT INTO `ark_role_policy_template` VALUES (871,12,5,81,3);
INSERT INTO `ark_role_policy_template` VALUES (872,4,1,1,1);
INSERT INTO `ark_role_policy_template` VALUES (873,4,1,1,2);
INSERT INTO `ark_role_policy_template` VALUES (874,4,1,1,3);
INSERT INTO `ark_role_policy_template` VALUES (875,4,1,2,1);
INSERT INTO `ark_role_policy_template` VALUES (876,4,1,2,2);
INSERT INTO `ark_role_policy_template` VALUES (877,4,1,2,3);
INSERT INTO `ark_role_policy_template` VALUES (878,4,1,77,1);
INSERT INTO `ark_role_policy_template` VALUES (879,4,1,77,2);
INSERT INTO `ark_role_policy_template` VALUES (880,4,1,77,3);
INSERT INTO `ark_role_policy_template` VALUES (881,4,1,11,1);
INSERT INTO `ark_role_policy_template` VALUES (882,4,1,11,2);
INSERT INTO `ark_role_policy_template` VALUES (883,4,1,11,3);
INSERT INTO `ark_role_policy_template` VALUES (884,4,1,48,1);
INSERT INTO `ark_role_policy_template` VALUES (885,4,1,48,2);
INSERT INTO `ark_role_policy_template` VALUES (886,4,1,48,3);
INSERT INTO `ark_role_policy_template` VALUES (887,9,5,84,2);
INSERT INTO `ark_role_policy_template` VALUES (888,10,5,84,2);
INSERT INTO `ark_role_policy_template` VALUES (889,12,5,84,2);
/*!40000 ALTER TABLE `ark_role_policy_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_user`
--

DROP TABLE IF EXISTS `ark_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LDAP_USER_NAME` varchar(500) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_user`
--

LOCK TABLES `ark_user` WRITE;
/*!40000 ALTER TABLE `ark_user` DISABLE KEYS */;
INSERT INTO `ark_user` VALUES (1,'arksuperuser@ark.org.au');
/*!40000 ALTER TABLE `ark_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ark_user_role`
--

DROP TABLE IF EXISTS `ark_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ark_user_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARK_USER_ID` int(11) NOT NULL,
  `ARK_ROLE_ID` int(11) NOT NULL,
  `ARK_MODULE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ARK_USER_ID` (`ARK_USER_ID`),
  KEY `FK_ARK_ROLE_ID` (`ARK_ROLE_ID`),
  KEY `FK_ARK_MODULE_ID` (`ARK_MODULE_ID`),
  KEY `FK_ARK_USER_ROLE_STUDY_ID` (`STUDY_ID`),
  CONSTRAINT `FK_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_ARK_ROLE_ID` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_USER_ID` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_USER_ROLE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_user_role`
--

LOCK TABLES `ark_user_role` WRITE;
/*!40000 ALTER TABLE `ark_user_role` DISABLE KEYS */;
INSERT INTO `ark_user_role` VALUES (1,1,1,1,NULL);
INSERT INTO `ark_user_role` VALUES (2,1,1,2,NULL);
INSERT INTO `ark_user_role` VALUES (3,1,1,3,NULL);
INSERT INTO `ark_user_role` VALUES (4,1,1,4,NULL);
INSERT INTO `ark_user_role` VALUES (5,1,1,5,NULL);
INSERT INTO `ark_user_role` VALUES (6,1,1,9,NULL);
INSERT INTO `ark_user_role` VALUES (7,1,1,8,NULL);
/*!40000 ALTER TABLE `ark_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_history`
--

DROP TABLE IF EXISTS `audit_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_STATUS_ID` int(11) DEFAULT '0',
  `DATE_TIME` datetime DEFAULT NULL,
  `ACTION_TYPE` varchar(50) NOT NULL,
  `ARK_USER_ID` varchar(255) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `ENTITY_TYPE` varchar(50) NOT NULL,
  `ENTITY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `AUDIT_HISTORY_STUDY_STATUS_FK` (`STUDY_STATUS_ID`) USING BTREE,
  KEY `AUDIT_HISTORY_ENTITY_ID` (`ENTITY_ID`) USING BTREE,
  KEY `AUDIT_HISTORY_ACTION_TYPE` (`ACTION_TYPE`) USING BTREE,
  KEY `AUDIT_HISTORY_ENTITY_TYPE` (`ENTITY_TYPE`) USING BTREE,
  CONSTRAINT `audit_history_ibfk_1` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`),
  CONSTRAINT `audit_history_ibfk_3` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ACTION_TYPE_ID`) REFER `study/action';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_history`
--

LOCK TABLES `audit_history` WRITE;
/*!40000 ALTER TABLE `audit_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent`
--

DROP TABLE IF EXISTS `consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  `CONSENT_STATUS_ID` int(11) NOT NULL,
  `CONSENT_TYPE_ID` int(11) NOT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENTED_BY` varchar(100) DEFAULT NULL,
  `COMMENTS` varchar(500) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `RECEIVED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_study` (`STUDY_ID`) USING BTREE,
  KEY `fk_study_component` (`STUDY_COMP_ID`) USING BTREE,
  KEY `fk_study_comp_status` (`STUDY_COMP_STATUS_ID`) USING BTREE,
  KEY `fk_consent_status` (`CONSENT_STATUS_ID`) USING BTREE,
  KEY `fk_consent_type` (`CONSENT_TYPE_ID`) USING BTREE,
  KEY `fk_consent_downloaded` (`CONSENT_DOWNLOADED_ID`) USING BTREE,
  KEY `fk_subject` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  CONSTRAINT `fk_consent_downloaded` FOREIGN KEY (`CONSENT_DOWNLOADED_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_status` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_type` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_study_component` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_comp_status` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent`
--

LOCK TABLES `consent` WRITE;
/*!40000 ALTER TABLE `consent` DISABLE KEYS */;
/*!40000 ALTER TABLE `consent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_answer`
--

DROP TABLE IF EXISTS `consent_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_answer`
--

LOCK TABLES `consent_answer` WRITE;
/*!40000 ALTER TABLE `consent_answer` DISABLE KEYS */;
INSERT INTO `consent_answer` VALUES (1,'YES');
INSERT INTO `consent_answer` VALUES (2,'NO');
/*!40000 ALTER TABLE `consent_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_file`
--

DROP TABLE IF EXISTS `consent_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONSENT_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_consent_file_consent` (`CONSENT_ID`) USING BTREE,
  CONSTRAINT `fk_upload_consent` FOREIGN KEY (`CONSENT_ID`) REFERENCES `consent` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_file`
--

LOCK TABLES `consent_file` WRITE;
/*!40000 ALTER TABLE `consent_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `consent_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_option`
--

DROP TABLE IF EXISTS `consent_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_option` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_option`
--

LOCK TABLES `consent_option` WRITE;
/*!40000 ALTER TABLE `consent_option` DISABLE KEYS */;
INSERT INTO `consent_option` VALUES (1,'Yes');
INSERT INTO `consent_option` VALUES (2,'No');
INSERT INTO `consent_option` VALUES (3,'Pending');
INSERT INTO `consent_option` VALUES (4,'Unavailable');
INSERT INTO `consent_option` VALUES (5,'Limited');
INSERT INTO `consent_option` VALUES (6,'Revoked');
/*!40000 ALTER TABLE `consent_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_section`
--

DROP TABLE IF EXISTS `consent_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_section` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE,
  KEY `fk_consent_section_1` (`STUDY_ID`) USING BTREE,
  KEY `fk_consent_section_2` (`STUDY_COMP_ID`) USING BTREE,
  CONSTRAINT `fk_consent_section_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_section_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_section`
--

LOCK TABLES `consent_section` WRITE;
/*!40000 ALTER TABLE `consent_section` DISABLE KEYS */;
/*!40000 ALTER TABLE `consent_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_status`
--

DROP TABLE IF EXISTS `consent_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_status`
--

LOCK TABLES `consent_status` WRITE;
/*!40000 ALTER TABLE `consent_status` DISABLE KEYS */;
INSERT INTO `consent_status` VALUES (1,'Consented','Subject Consented');
INSERT INTO `consent_status` VALUES (2,'Not Consented','Subject Not Consented');
INSERT INTO `consent_status` VALUES (3,'Ineligible','Ineligible');
INSERT INTO `consent_status` VALUES (4,'Refused','Refused');
INSERT INTO `consent_status` VALUES (5,'Withdrawn','Withdrawn');
INSERT INTO `consent_status` VALUES (6,'Pending','Pending');
/*!40000 ALTER TABLE `consent_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consent_type`
--

DROP TABLE IF EXISTS `consent_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_type`
--

LOCK TABLES `consent_type` WRITE;
/*!40000 ALTER TABLE `consent_type` DISABLE KEYS */;
INSERT INTO `consent_type` VALUES (1,'Hard Copy','Physical Paper based document.');
INSERT INTO `consent_type` VALUES (2,'Electronic','A scanned equivalent of a hard copy that is available as a download via an application.');
INSERT INTO `consent_type` VALUES (3,'Verbal',NULL);
/*!40000 ALTER TABLE `consent_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `correspondence_direction_type`
--

DROP TABLE IF EXISTS `correspondence_direction_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_direction_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correspondence_direction_type`
--

LOCK TABLES `correspondence_direction_type` WRITE;
/*!40000 ALTER TABLE `correspondence_direction_type` DISABLE KEYS */;
INSERT INTO `correspondence_direction_type` VALUES (1,'Incoming',NULL);
INSERT INTO `correspondence_direction_type` VALUES (2,'Outgoing',NULL);
/*!40000 ALTER TABLE `correspondence_direction_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `correspondence_mode_type`
--

DROP TABLE IF EXISTS `correspondence_mode_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_mode_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correspondence_mode_type`
--

LOCK TABLES `correspondence_mode_type` WRITE;
/*!40000 ALTER TABLE `correspondence_mode_type` DISABLE KEYS */;
INSERT INTO `correspondence_mode_type` VALUES (1,'Mail',NULL);
INSERT INTO `correspondence_mode_type` VALUES (2,'Fax',NULL);
INSERT INTO `correspondence_mode_type` VALUES (3,'Email',NULL);
INSERT INTO `correspondence_mode_type` VALUES (4,'Telephone',NULL);
INSERT INTO `correspondence_mode_type` VALUES (5,'Face to face',NULL);
INSERT INTO `correspondence_mode_type` VALUES (6,'Not applicable',NULL);
/*!40000 ALTER TABLE `correspondence_mode_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `correspondence_outcome_type`
--

DROP TABLE IF EXISTS `correspondence_outcome_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_outcome_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(496) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correspondence_outcome_type`
--

LOCK TABLES `correspondence_outcome_type` WRITE;
/*!40000 ALTER TABLE `correspondence_outcome_type` DISABLE KEYS */;
INSERT INTO `correspondence_outcome_type` VALUES (1,'Sent',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (2,'Received',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (3,'Return to sender',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (4,'Engaged',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (5,'No answer',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (6,'Contact made',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (7,'Message given to person',NULL);
INSERT INTO `correspondence_outcome_type` VALUES (8,'Not applicable',NULL);
/*!40000 ALTER TABLE `correspondence_outcome_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `correspondences`
--

DROP TABLE IF EXISTS `correspondences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondences` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `ARK_USER_ID` int(11) DEFAULT NULL,
  `DATE` date DEFAULT NULL,
  `TIME` varchar(255) DEFAULT NULL,
  `REASON` varchar(4096) DEFAULT NULL,
  `MODE_TYPE_ID` int(11) DEFAULT NULL,
  `DIRECTION_TYPE_ID` int(11) DEFAULT NULL,
  `OUTCOME_TYPE_ID` int(11) DEFAULT NULL,
  `DETAILS` varchar(4096) DEFAULT NULL,
  `COMMENTS` varchar(4096) DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(255) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `BILLABLE_ITEM_ID` int(11) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) DEFAULT NULL,
  `ATTACHMENT_CHECKSUM` varchar(50) DEFAULT NULL,
  `ATTACHMENT_FILE_ID` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `mode_type` (`MODE_TYPE_ID`) USING BTREE,
  KEY `direction_type` (`DIRECTION_TYPE_ID`) USING BTREE,
  KEY `outcome_type` (`OUTCOME_TYPE_ID`) USING BTREE,
  KEY `correspondences_study_id` (`STUDY_ID`) USING BTREE,
  KEY `correspondences_person_id` (`PERSON_ID`),
  KEY `fk_correspondences_ark_user` (`ARK_USER_ID`),
  CONSTRAINT `correspondences_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `correspondence_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_correspondences_ark_user` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correspondences`
--

LOCK TABLES `correspondences` WRITE;
/*!40000 ALTER TABLE `correspondences` DISABLE KEYS */;
/*!40000 ALTER TABLE `correspondences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'LEGACY ID, \nkeep table structures similar\n',
  `NAME` varchar(100) NOT NULL COMMENT 'Common / \nColloquial Name',
  `COUNTRY_CODE` varchar(2) NOT NULL COMMENT 'Official as used in local match ups, unique\n',
  `ALPHA_3_CODE` varchar(45) NOT NULL,
  `NUMERIC_CODE` varchar(45) NOT NULL,
  `OFFICIAL_NAME` varchar(45) NOT NULL COMMENT 'Correct Name, Probably not used often\n',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'Australia','AU','AUS','036','');
INSERT INTO `country` VALUES (2,'United Kingdom','GB','GBR','826','United Kingdom of Great Britain and Northern ');
INSERT INTO `country` VALUES (3,'Canada','CA','CAN','124','');
INSERT INTO `country` VALUES (4,'Afghanistan','AF','AFG','004','Islamic Republic of Afghanistan');
INSERT INTO `country` VALUES (5,'Aland Islands','AX','ALA','248','Åland Islands');
INSERT INTO `country` VALUES (6,'Albania','AL','ALB','008','Republic of Albania');
INSERT INTO `country` VALUES (7,'Algeria','DZ','DZA','012','People\'s Democratic Republic of Algeria');
INSERT INTO `country` VALUES (8,'American Samoa','AS','ASM','016','');
INSERT INTO `country` VALUES (9,'Andorra','AD','AND','020','Principality of Andorra');
INSERT INTO `country` VALUES (10,'Angola','AO','AGO','024','Republic of Angola');
INSERT INTO `country` VALUES (11,'Anguilla','AI','AIA','660','');
INSERT INTO `country` VALUES (12,'Antarctica','AQ','ATA','010','');
INSERT INTO `country` VALUES (13,'Antigua and Barbuda','AG','ATG','028','');
INSERT INTO `country` VALUES (14,'Argentina','AR','ARG','032','Argentine Republic');
INSERT INTO `country` VALUES (15,'Armenia','AM','ARM','051','Republic of Armenia');
INSERT INTO `country` VALUES (16,'Aruba','AW','ABW','533','');
INSERT INTO `country` VALUES (18,'Austria','AT','AUT','040','Republic of Austria');
INSERT INTO `country` VALUES (19,'Azerbaijan','AZ','AZE','031','Republic of Azerbaijan');
INSERT INTO `country` VALUES (20,'Bahamas','BS','BHS','044','Commonwealth of the Bahamas');
INSERT INTO `country` VALUES (21,'Bahrain','BH','BHR','048','Kingdom of Bahrain');
INSERT INTO `country` VALUES (22,'Bangladesh','BD','BGD','050','People\'s Republic of Bangladesh');
INSERT INTO `country` VALUES (23,'Barbados','BB','BRB','052','');
INSERT INTO `country` VALUES (24,'Belarus','BY','BLR','112','Republic of Belarus');
INSERT INTO `country` VALUES (25,'Belgium','BE','BEL','056','Kingdom of Belgium');
INSERT INTO `country` VALUES (26,'Belize','BZ','BLZ','084','');
INSERT INTO `country` VALUES (27,'Benin','BJ','BEN','204','Republic of Benin');
INSERT INTO `country` VALUES (28,'Bermuda','BM','BMU','060','');
INSERT INTO `country` VALUES (29,'Bhutan','BT','BTN','064','Kingdom of Bhutan');
INSERT INTO `country` VALUES (30,'Bolivia','BO','BOL','068','Republic of Bolivia');
INSERT INTO `country` VALUES (31,'BONAIRE, SAINT EUSTATIUS AND SABA','BQ','','','');
INSERT INTO `country` VALUES (32,'Bosnia and Herzegovina','BA','BIH','070','Republic of Bosnia and Herzegovina');
INSERT INTO `country` VALUES (33,'Botswana','BW','BWA','072','Republic of Botswana');
INSERT INTO `country` VALUES (34,'Bouvet Island','BV','BVT','074','');
INSERT INTO `country` VALUES (35,'Brazil','BR','BRA','076','Federative Republic of Brazil');
INSERT INTO `country` VALUES (36,'British Indian Ocean Territory','IO','IOT','086','');
INSERT INTO `country` VALUES (37,'Brunei Darussalam','BN','BRN','096','');
INSERT INTO `country` VALUES (38,'Bulgaria','BG','BGR','100','Republic of Bulgaria');
INSERT INTO `country` VALUES (39,'Burkina Faso','BF','BFA','854','');
INSERT INTO `country` VALUES (40,'Burundi','BI','BDI','108','Republic of Burundi');
INSERT INTO `country` VALUES (41,'Cambodia','KH','KHM','116','Kingdom of Cambodia');
INSERT INTO `country` VALUES (42,'Cameroon','CM','CMR','120','Republic of Cameroon');
INSERT INTO `country` VALUES (44,'Cape Verde','CV','CPV','132','Republic of Cape Verde');
INSERT INTO `country` VALUES (45,'Cayman Islands','KY','CYM','136','');
INSERT INTO `country` VALUES (46,'Central African Republic','CF','CAF','140','');
INSERT INTO `country` VALUES (47,'Chad','TD','TCD','148','Republic of Chad');
INSERT INTO `country` VALUES (48,'Chile','CL','CHL','152','Republic of Chile');
INSERT INTO `country` VALUES (49,'China','CN','CHN','156','People\'s Republic of China');
INSERT INTO `country` VALUES (50,'Christmas Island','CX','CXR','162','');
INSERT INTO `country` VALUES (51,'Cocos (Keeling) Islands','CC','CCK','166','');
INSERT INTO `country` VALUES (52,'Colombia','CO','COL','170','Republic of Colombia');
INSERT INTO `country` VALUES (53,'Comoros','KM','COM','174','Union of the Comoros');
INSERT INTO `country` VALUES (54,'Congo','CG','COG','178','Republic of the Congo');
INSERT INTO `country` VALUES (55,'Congo, The Democratic Republic of the','CD','COD','180','');
INSERT INTO `country` VALUES (56,'Cook Islands','CK','COK','184','');
INSERT INTO `country` VALUES (57,'Costa Rica','CR','CRI','188','Republic of Costa Rica');
INSERT INTO `country` VALUES (58,'Côte d\'Ivoire','CI','CIV','384','Republic of Côte d\'Ivoire');
INSERT INTO `country` VALUES (59,'Croatia','HR','HRV','191','Republic of Croatia');
INSERT INTO `country` VALUES (60,'Cuba','CU','CUB','192','Republic of Cuba');
INSERT INTO `country` VALUES (61,'CURACAO','CW','','','');
INSERT INTO `country` VALUES (62,'Cyprus','CY','CYP','196','Republic of Cyprus');
INSERT INTO `country` VALUES (63,'Czech Republic','CZ','CZE','203','');
INSERT INTO `country` VALUES (64,'Denmark','DK','DNK','208','Kingdom of Denmark');
INSERT INTO `country` VALUES (65,'Djibouti','DJ','DJI','262','Republic of Djibouti');
INSERT INTO `country` VALUES (66,'Dominica','DM','DMA','212','Commonwealth of Dominica');
INSERT INTO `country` VALUES (67,'Dominican Republic','DO','DOM','214','');
INSERT INTO `country` VALUES (68,'Ecuador','EC','ECU','218','Republic of Ecuador');
INSERT INTO `country` VALUES (69,'Egypt','EG','EGY','818','Arab Republic of Egypt');
INSERT INTO `country` VALUES (70,'El Salvador','SV','SLV','222','Republic of El Salvador');
INSERT INTO `country` VALUES (71,'Equatorial Guinea','GQ','GNQ','226','Republic of Equatorial Guinea');
INSERT INTO `country` VALUES (72,'Eritrea','ER','ERI','232','');
INSERT INTO `country` VALUES (73,'Estonia','EE','EST','233','Republic of Estonia');
INSERT INTO `country` VALUES (74,'Ethiopia','ET','ETH','231','Federal Democratic Republic of Ethiopia');
INSERT INTO `country` VALUES (75,'Falkland Islands (Malvinas)','FK','FLK','238','');
INSERT INTO `country` VALUES (76,'Faroe Islands','FO','FRO','234','');
INSERT INTO `country` VALUES (77,'Fiji','FJ','FJI','242','Republic of the Fiji Islands');
INSERT INTO `country` VALUES (78,'Finland','FI','FIN','246','Republic of Finland');
INSERT INTO `country` VALUES (79,'France','FR','FRA','250','French Republic');
INSERT INTO `country` VALUES (80,'French Guiana','GF','GUF','254','');
INSERT INTO `country` VALUES (81,'French Polynesia','PF','PYF','258','');
INSERT INTO `country` VALUES (82,'French Southern Territories','TF','ATF','260','');
INSERT INTO `country` VALUES (83,'Gabon','GA','GAB','266','Gabonese Republic');
INSERT INTO `country` VALUES (84,'Gambia','GM','GMB','270','Republic of the Gambia');
INSERT INTO `country` VALUES (85,'Georgia','GE','GEO','268','');
INSERT INTO `country` VALUES (86,'Germany','DE','DEU','276','Federal Republic of Germany');
INSERT INTO `country` VALUES (87,'Ghana','GH','GHA','288','Republic of Ghana');
INSERT INTO `country` VALUES (88,'Gibraltar','GI','GIB','292','');
INSERT INTO `country` VALUES (89,'Greece','GR','GRC','300','Hellenic Republic');
INSERT INTO `country` VALUES (90,'Greenland','GL','GRL','304','');
INSERT INTO `country` VALUES (91,'Grenada','GD','GRD','308','');
INSERT INTO `country` VALUES (92,'Guadeloupe','GP','GLP','312','');
INSERT INTO `country` VALUES (93,'Guam','GU','GUM','316','');
INSERT INTO `country` VALUES (94,'Guatemala','GT','GTM','320','Republic of Guatemala');
INSERT INTO `country` VALUES (95,'Guernsey','GG','GGY','831','');
INSERT INTO `country` VALUES (96,'Guinea','GN','GIN','324','Republic of Guinea');
INSERT INTO `country` VALUES (97,'Guinea-Bissau','GW','GNB','624','Republic of Guinea-Bissau');
INSERT INTO `country` VALUES (98,'Guyana','GY','GUY','328','Republic of Guyana');
INSERT INTO `country` VALUES (99,'Haiti','HT','HTI','332','Republic of Haiti');
INSERT INTO `country` VALUES (100,'Heard Island and McDonald Islands','HM','HMD','334','');
INSERT INTO `country` VALUES (101,'Holy See (Vatican City State)','VA','VAT','336','');
INSERT INTO `country` VALUES (102,'Honduras','HN','HND','340','Republic of Honduras');
INSERT INTO `country` VALUES (103,'Hong Kong','HK','HKG','344','Hong Kong Special Administrative Region of Ch');
INSERT INTO `country` VALUES (104,'Hungary','HU','HUN','348','Republic of Hungary');
INSERT INTO `country` VALUES (105,'Iceland','IS','ISL','352','Republic of Iceland');
INSERT INTO `country` VALUES (106,'India','IN','IND','356','Republic of India');
INSERT INTO `country` VALUES (107,'Indonesia','ID','IDN','360','Republic of Indonesia');
INSERT INTO `country` VALUES (108,'Iran, Islamic Republic of','IR','IRN','364','Islamic Republic of Iran');
INSERT INTO `country` VALUES (109,'Iraq','IQ','IRQ','368','Republic of Iraq');
INSERT INTO `country` VALUES (110,'Ireland','IE','IRL','372','');
INSERT INTO `country` VALUES (111,'Isle of Man','IM','IMN','833','');
INSERT INTO `country` VALUES (112,'Israel','IL','ISR','376','State of Israel');
INSERT INTO `country` VALUES (113,'Italy','IT','ITA','380','Italian Republic');
INSERT INTO `country` VALUES (114,'Jamaica','JM','JAM','388','');
INSERT INTO `country` VALUES (115,'Japan','JP','JPN','392','');
INSERT INTO `country` VALUES (116,'Jersey','JE','JEY','832','');
INSERT INTO `country` VALUES (117,'Jordan','JO','JOR','400','Hashemite Kingdom of Jordan');
INSERT INTO `country` VALUES (118,'Kazakhstan','KZ','KAZ','398','Republic of Kazakhstan');
INSERT INTO `country` VALUES (119,'Kenya','KE','KEN','404','Republic of Kenya');
INSERT INTO `country` VALUES (120,'Kiribati','KI','KIR','296','Republic of Kiribati');
INSERT INTO `country` VALUES (121,'Korea, Democratic People\'s Republic of','KP','PRK','408','Democratic People\'s Republic of Korea');
INSERT INTO `country` VALUES (122,'Korea, Republic of','KR','KOR','410','');
INSERT INTO `country` VALUES (123,'Kuwait','KW','KWT','414','State of Kuwait');
INSERT INTO `country` VALUES (124,'Kyrgyzstan','KG','KGZ','417','Kyrgyz Republic');
INSERT INTO `country` VALUES (125,'Lao People\'s Democratic Republic','LA','LAO','418','');
INSERT INTO `country` VALUES (126,'Latvia','LV','LVA','428','Republic of Latvia');
INSERT INTO `country` VALUES (127,'Lebanon','LB','LBN','422','Lebanese Republic');
INSERT INTO `country` VALUES (128,'Lesotho','LS','LSO','426','Kingdom of Lesotho');
INSERT INTO `country` VALUES (129,'Liberia','LR','LBR','430','Republic of Liberia');
INSERT INTO `country` VALUES (130,'Libyan Arab Jamahiriya','LY','LBY','434','Socialist People\'s Libyan Arab Jamahiriya');
INSERT INTO `country` VALUES (131,'Liechtenstein','LI','LIE','438','Principality of Liechtenstein');
INSERT INTO `country` VALUES (132,'Lithuania','LT','LTU','440','Republic of Lithuania');
INSERT INTO `country` VALUES (133,'Luxembourg','LU','LUX','442','Grand Duchy of Luxembourg');
INSERT INTO `country` VALUES (134,'Macao','MO','MAC','446','Macao Special Administrative Region of China');
INSERT INTO `country` VALUES (135,'Macedonia, Republic of','MK','MKD','807','The Former Yugoslav Republic of Macedonia');
INSERT INTO `country` VALUES (136,'Madagascar','MG','MDG','450','Republic of Madagascar');
INSERT INTO `country` VALUES (137,'Malawi','MW','MWI','454','Republic of Malawi');
INSERT INTO `country` VALUES (138,'Malaysia','MY','MYS','458','');
INSERT INTO `country` VALUES (139,'Maldives','MV','MDV','462','Republic of Maldives');
INSERT INTO `country` VALUES (140,'Mali','ML','MLI','466','Republic of Mali');
INSERT INTO `country` VALUES (141,'Malta','MT','MLT','470','Republic of Malta');
INSERT INTO `country` VALUES (142,'Marshall Islands','MH','MHL','584','Republic of the Marshall Islands');
INSERT INTO `country` VALUES (143,'Martinique','MQ','MTQ','474','');
INSERT INTO `country` VALUES (144,'Mauritania','MR','MRT','478','Islamic Republic of Mauritania');
INSERT INTO `country` VALUES (145,'Mauritius','MU','MUS','480','Republic of Mauritius');
INSERT INTO `country` VALUES (146,'Mayotte','YT','MYT','175','');
INSERT INTO `country` VALUES (147,'Mexico','MX','MEX','484','United Mexican States');
INSERT INTO `country` VALUES (148,'Micronesia, Federated States of','FM','FSM','583','Federated States of Micronesia');
INSERT INTO `country` VALUES (149,'Moldova','MD','MDA','498','Republic of Moldova');
INSERT INTO `country` VALUES (150,'Monaco','MC','MCO','492','Principality of Monaco');
INSERT INTO `country` VALUES (151,'Mongolia','MN','MNG','496','');
INSERT INTO `country` VALUES (152,'Montenegro','ME','MNE','499','Montenegro');
INSERT INTO `country` VALUES (153,'Montserrat','MS','MSR','500','');
INSERT INTO `country` VALUES (154,'Morocco','MA','MAR','504','Kingdom of Morocco');
INSERT INTO `country` VALUES (155,'Mozambique','MZ','MOZ','508','Republic of Mozambique');
INSERT INTO `country` VALUES (156,'Myanmar','MM','MMR','104','Union of Myanmar');
INSERT INTO `country` VALUES (157,'Namibia','NA','NAM','516','Republic of Namibia');
INSERT INTO `country` VALUES (158,'Nauru','NR','NRU','520','Republic of Nauru');
INSERT INTO `country` VALUES (159,'Nepal','NP','NPL','524','Kingdom of Nepal');
INSERT INTO `country` VALUES (160,'Netherlands','NL','NLD','528','Kingdom of the Netherlands');
INSERT INTO `country` VALUES (161,'New Caledonia','NC','NCL','540','');
INSERT INTO `country` VALUES (162,'New Zealand','NZ','NZL','554','');
INSERT INTO `country` VALUES (163,'Nicaragua','NI','NIC','558','Republic of Nicaragua');
INSERT INTO `country` VALUES (164,'Niger','NE','NER','562','Republic of the Niger');
INSERT INTO `country` VALUES (165,'Nigeria','NG','NGA','566','Federal Republic of Nigeria');
INSERT INTO `country` VALUES (166,'Niue','NU','NIU','570','Republic of Niue');
INSERT INTO `country` VALUES (167,'Norfolk Island','NF','NFK','574','');
INSERT INTO `country` VALUES (168,'Northern Mariana Islands','MP','MNP','580','Commonwealth of the Northern Mariana Islands');
INSERT INTO `country` VALUES (169,'Norway','NO','NOR','578','Kingdom of Norway');
INSERT INTO `country` VALUES (170,'Oman','OM','OMN','512','Sultanate of Oman');
INSERT INTO `country` VALUES (171,'Pakistan','PK','PAK','586','Islamic Republic of Pakistan');
INSERT INTO `country` VALUES (172,'Palau','PW','PLW','585','Republic of Palau');
INSERT INTO `country` VALUES (173,'Palestinian Territory, Occupied','PS','PSE','275','Occupied Palestinian Territory');
INSERT INTO `country` VALUES (174,'Panama','PA','PAN','591','Republic of Panama');
INSERT INTO `country` VALUES (175,'Papua New Guinea','PG','PNG','598','');
INSERT INTO `country` VALUES (176,'Paraguay','PY','PRY','600','Republic of Paraguay');
INSERT INTO `country` VALUES (177,'Peru','PE','PER','604','Republic of Peru');
INSERT INTO `country` VALUES (178,'Philippines','PH','PHL','608','Republic of the Philippines');
INSERT INTO `country` VALUES (179,'Pitcairn','PN','PCN','612','');
INSERT INTO `country` VALUES (180,'Poland','PL','POL','616','Republic of Poland');
INSERT INTO `country` VALUES (181,'Portugal','PT','PRT','620','Portuguese Republic');
INSERT INTO `country` VALUES (182,'Puerto Rico','PR','PRI','630','');
INSERT INTO `country` VALUES (183,'Qatar','QA','QAT','634','State of Qatar');
INSERT INTO `country` VALUES (184,'Reunion','RE','REU','638','');
INSERT INTO `country` VALUES (185,'Romania','RO','ROU','642','');
INSERT INTO `country` VALUES (186,'Russian Federation','RU','RUS','643','');
INSERT INTO `country` VALUES (187,'Rwanda','RW','RWA','646','Rwandese Republic');
INSERT INTO `country` VALUES (188,'Saint Barthélemy','BL','BLM','652','');
INSERT INTO `country` VALUES (189,'Saint Helena','SH','SHN','654','SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA');
INSERT INTO `country` VALUES (190,'Saint Kitts and Nevis','KN','KNA','659','');
INSERT INTO `country` VALUES (191,'Saint Lucia','LC','LCA','662','');
INSERT INTO `country` VALUES (192,'Saint Martin (French part)','MF','MAF','663','');
INSERT INTO `country` VALUES (193,'Saint Pierre and Miquelon','PM','SPM','666','');
INSERT INTO `country` VALUES (194,'Saint Vincent and the Grenadines','VC','VCT','670','');
INSERT INTO `country` VALUES (195,'Samoa','WS','WSM','882','Independent State of Samoa');
INSERT INTO `country` VALUES (196,'San Marino','SM','SMR','674','Republic of San Marino');
INSERT INTO `country` VALUES (197,'Sao Tome and Principe','ST','STP','678','Democratic Republic of Sao Tome and Principe');
INSERT INTO `country` VALUES (198,'Saudi Arabia','SA','SAU','682','Kingdom of Saudi Arabia');
INSERT INTO `country` VALUES (199,'Senegal','SN','SEN','686','Republic of Senegal');
INSERT INTO `country` VALUES (200,'Serbia','RS','SRB','688','Republic of Serbia');
INSERT INTO `country` VALUES (201,'Seychelles','SC','SYC','690','Republic of Seychelles');
INSERT INTO `country` VALUES (202,'Sierra Leone','SL','SLE','694','Republic of Sierra Leone');
INSERT INTO `country` VALUES (203,'Singapore','SG','SGP','702','Republic of Singapore');
INSERT INTO `country` VALUES (204,'SINT MAARTEN (DUTCH PART)','SX','','','');
INSERT INTO `country` VALUES (205,'Slovakia','SK','SVK','703','Slovak Republic');
INSERT INTO `country` VALUES (206,'Slovenia','SI','SVN','705','Republic of Slovenia');
INSERT INTO `country` VALUES (207,'Solomon Islands','SB','SLB','090','');
INSERT INTO `country` VALUES (208,'Somalia','SO','SOM','706','Somali Republic');
INSERT INTO `country` VALUES (209,'South Africa','ZA','ZAF','710','Republic of South Africa');
INSERT INTO `country` VALUES (210,'South Georgia and the South Sandwich Islands','GS','SGS','239','');
INSERT INTO `country` VALUES (211,'Spain','ES','ESP','724','Kingdom of Spain');
INSERT INTO `country` VALUES (212,'Sri Lanka','LK','LKA','144','Democratic Socialist Republic of Sri Lanka');
INSERT INTO `country` VALUES (213,'Sudan','SD','SDN','736','Republic of the Sudan');
INSERT INTO `country` VALUES (214,'Suriname','SR','SUR','740','Republic of Suriname');
INSERT INTO `country` VALUES (215,'Svalbard and Jan Mayen','SJ','SJM','744','');
INSERT INTO `country` VALUES (216,'Swaziland','SZ','SWZ','748','Kingdom of Swaziland');
INSERT INTO `country` VALUES (217,'Sweden','SE','SWE','752','Kingdom of Sweden');
INSERT INTO `country` VALUES (218,'Switzerland','CH','CHE','756','Swiss Confederation');
INSERT INTO `country` VALUES (219,'Syrian Arab Republic','SY','SYR','760','');
INSERT INTO `country` VALUES (220,'Taiwan, Province of China','TW','TWN','158','Taiwan, Province of China');
INSERT INTO `country` VALUES (221,'Tajikistan','TJ','TJK','762','Republic of Tajikistan');
INSERT INTO `country` VALUES (222,'Tanzania, United Republic of','TZ','TZA','834','United Republic of Tanzania');
INSERT INTO `country` VALUES (223,'Thailand','TH','THA','764','Kingdom of Thailand');
INSERT INTO `country` VALUES (224,'Timor-Leste','TL','TLS','626','Democratic Republic of Timor-Leste');
INSERT INTO `country` VALUES (225,'Togo','TG','TGO','768','Togolese Republic');
INSERT INTO `country` VALUES (226,'Tokelau','TK','TKL','772','');
INSERT INTO `country` VALUES (227,'Tonga','TO','TON','776','Kingdom of Tonga');
INSERT INTO `country` VALUES (228,'Trinidad and Tobago','TT','TTO','780','Republic of Trinidad and Tobago');
INSERT INTO `country` VALUES (229,'Tunisia','TN','TUN','788','Republic of Tunisia');
INSERT INTO `country` VALUES (230,'Turkey','TR','TUR','792','Republic of Turkey');
INSERT INTO `country` VALUES (231,'Turkmenistan','TM','TKM','795','');
INSERT INTO `country` VALUES (232,'Turks and Caicos Islands','TC','TCA','796','');
INSERT INTO `country` VALUES (233,'Tuvalu','TV','TUV','798','');
INSERT INTO `country` VALUES (234,'Uganda','UG','UGA','800','Republic of Uganda');
INSERT INTO `country` VALUES (235,'Ukraine','UA','UKR','804','');
INSERT INTO `country` VALUES (236,'United Arab Emirates','AE','ARE','784','');
INSERT INTO `country` VALUES (238,'United States','US','USA','840','United States of America');
INSERT INTO `country` VALUES (239,'United States Minor Outlying Islands','UM','UMI','581','');
INSERT INTO `country` VALUES (240,'Uruguay','UY','URY','858','Eastern Republic of Uruguay');
INSERT INTO `country` VALUES (241,'Uzbekistan','UZ','UZB','860','Republic of Uzbekistan');
INSERT INTO `country` VALUES (242,'Vanuatu','VU','VUT','548','Republic of Vanuatu');
INSERT INTO `country` VALUES (243,'VATICAN CITY STATE','VA','','','');
INSERT INTO `country` VALUES (244,'Venezuela','VE','VEN','862','Bolivarian Republic of Venezuela');
INSERT INTO `country` VALUES (245,'Viet Nam','VN','VNM','704','Socialist Republic of Viet Nam');
INSERT INTO `country` VALUES (246,'Virgin Islands, British','VG','VGB','092','British Virgin Islands');
INSERT INTO `country` VALUES (247,'Virgin Islands, U.S.','VI','VIR','850','Virgin Islands of the United States');
INSERT INTO `country` VALUES (248,'Wallis and Futuna','WF','WLF','876','');
INSERT INTO `country` VALUES (249,'Western Sahara','EH','ESH','732','');
INSERT INTO `country` VALUES (250,'Yemen','YE','YEM','887','Republic of Yemen');
INSERT INTO `country` VALUES (251,'Zambia','ZM','ZMB','894','Republic of Zambia');
INSERT INTO `country` VALUES (252,'Zimbabwe','ZW','ZWE','716','Republic of Zimbabwe');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `csv_blob`
--

DROP TABLE IF EXISTS `csv_blob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csv_blob` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CSV_BLOB` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table containing BLOB references of CSV files for import/upl';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `csv_blob`
--

LOCK TABLES `csv_blob` WRITE;
/*!40000 ALTER TABLE `csv_blob` DISABLE KEYS */;
/*!40000 ALTER TABLE `csv_blob` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field`
--

DROP TABLE IF EXISTS `custom_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `FIELD_TYPE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `UNIT_TYPE_ID` int(11) DEFAULT NULL,
  `MIN_VALUE` varchar(45) DEFAULT NULL,
  `MAX_VALUE` varchar(45) DEFAULT NULL,
  `ENCODED_VALUES` text,
  `MISSING_VALUE` varchar(45) DEFAULT NULL,
  `HAS_DATA` tinyint(4) NOT NULL DEFAULT '0',
  `CUSTOM_FIELD_LABEL` varchar(255) DEFAULT NULL,
  `DEFAULT_VALUE` text,
  `UNIT_TYPE_IN_TEXT` varchar(20) DEFAULT NULL,
  `CATEGORY_ID` int(11) DEFAULT NULL,
  `CUSTOM_FIELD_TYPE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_UNIT_TYPE_ID` (`UNIT_TYPE_ID`),
  KEY `FK_CUSTOMFIELD_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOMFIELD_FIELD_TYPE_ID` (`FIELD_TYPE_ID`),
  KEY `FK_CUSTOM_FIELD_CATEGORY_idx` (`CATEGORY_ID`),
  KEY `FK_CUSTOMFIELD_CUSTOM_FIELD_TYPE_ID_idx` (`CUSTOM_FIELD_TYPE_ID`),
  CONSTRAINT `FK_CUSTOMFIELD_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELD_CUSTOM_FIELD_TYPE_ID` FOREIGN KEY (`CUSTOM_FIELD_TYPE_ID`) REFERENCES `custom_field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELD_FIELD_TYPE_ID` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_CATEGORY` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_CUSTOM_FIELD_CATEGORY_ID` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `custom_field_category` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_UNIT_TYPE_ID` FOREIGN KEY (`UNIT_TYPE_ID`) REFERENCES `unit_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field`
--

LOCK TABLES `custom_field` WRITE;
/*!40000 ALTER TABLE `custom_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_category`
--

DROP TABLE IF EXISTS `custom_field_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `CUSTOM_FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `ORDER_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_CUSTOMFIELDCATEGORY_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOMFIELDCATEGORY_PARENT_ID` (`PARENT_ID`),
  KEY `FK_CUSTOMFIELD_CUSTOM_FIELD_TYPE_ID_idx` (`CUSTOM_FIELD_TYPE_ID`),
  CONSTRAINT `FK_CUSTOMFIELDCATEGORY_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELDCATEGORY_CUSTOM_FIELD_TYPE_ID` FOREIGN KEY (`CUSTOM_FIELD_TYPE_ID`) REFERENCES `custom_field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELDCATEGORY_PARENT_ID` FOREIGN KEY (`PARENT_ID`) REFERENCES `custom_field_category` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELDCATEGORY_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_category`
--

LOCK TABLES `custom_field_category` WRITE;
/*!40000 ALTER TABLE `custom_field_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_category_upload`
--

DROP TABLE IF EXISTS `custom_field_category_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_category_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_CATEGORY_ID` int(11) NOT NULL,
  `UPLOAD_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CFCU_CUSTOM_FIELD_CATEGORY_ID` (`CUSTOM_FIELD_CATEGORY_ID`),
  KEY `FK_CFCU_UPLOAD_ID` (`UPLOAD_ID`),
  CONSTRAINT `FK_CFCU_CUSTOM_FIELD_CATEGORY_ID` FOREIGN KEY (`CUSTOM_FIELD_CATEGORY_ID`) REFERENCES `custom_field_category` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFCU_UPLOAD_ID` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_category_upload`
--

LOCK TABLES `custom_field_category_upload` WRITE;
/*!40000 ALTER TABLE `custom_field_category_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_category_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_display`
--

DROP TABLE IF EXISTS `custom_field_display`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_display` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_GROUP_ID` int(11) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `REQUIRED` int(11) DEFAULT NULL,
  `REQUIRED_MESSAGE` varchar(45) DEFAULT NULL,
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `custom_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_display`
--

LOCK TABLES `custom_field_display` WRITE;
/*!40000 ALTER TABLE `custom_field_display` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_display` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_group`
--

DROP TABLE IF EXISTS `custom_field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `PUBLISHED` tinyint(1) DEFAULT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_STUDY_ID` (`STUDY_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_group`
--

LOCK TABLES `custom_field_group` WRITE;
/*!40000 ALTER TABLE `custom_field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_type`
--

DROP TABLE IF EXISTS `custom_field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARK_MODULE_ID` int(11) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_custom_field_type_1_idx` (`ARK_MODULE_ID`),
  CONSTRAINT `fk_custom_field_type_1` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_type`
--

LOCK TABLES `custom_field_type` WRITE;
/*!40000 ALTER TABLE `custom_field_type` DISABLE KEYS */;
INSERT INTO `custom_field_type` VALUES (1,1,'SUBJECT','Subject custom field to store the subject data');
INSERT INTO `custom_field_type` VALUES (2,1,'FAMILY','Family custom field to store the family data');
INSERT INTO `custom_field_type` VALUES (3,5,'BIOSPECIMEN','LIMS Biospecimen details');
INSERT INTO `custom_field_type` VALUES (4,5,'BIOCOLLECTION','LIMS BioCollection details');
/*!40000 ALTER TABLE `custom_field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_upload`
--

DROP TABLE IF EXISTS `custom_field_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_ID` int(11) NOT NULL,
  `UPLOAD_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CFU_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  KEY `FK_CFU_UPLOAD_ID` (`UPLOAD_ID`),
  CONSTRAINT `FK_CFU_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFU_UPLOAD_ID` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_upload`
--

LOCK TABLES `custom_field_upload` WRITE;
/*!40000 ALTER TABLE `custom_field_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delimiter_type`
--

DROP TABLE IF EXISTS `delimiter_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delimiter_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `DELIMITER_CHARACTER` varchar(1) NOT NULL DEFAULT ',',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delimiter_type`
--

LOCK TABLES `delimiter_type` WRITE;
/*!40000 ALTER TABLE `delimiter_type` DISABLE KEYS */;
INSERT INTO `delimiter_type` VALUES (1,'COMMA','Comma',',');
INSERT INTO `delimiter_type` VALUES (2,'TAB','Tab character','	');
INSERT INTO `delimiter_type` VALUES (3,'PIPE','Pipe character','|');
INSERT INTO `delimiter_type` VALUES (4,'COLON','Colon character',':');
INSERT INTO `delimiter_type` VALUES (5,'AT SYMBOL','At characer','@');
/*!40000 ALTER TABLE `delimiter_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain_type`
--

DROP TABLE IF EXISTS `domain_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain_type`
--

LOCK TABLES `domain_type` WRITE;
/*!40000 ALTER TABLE `domain_type` DISABLE KEYS */;
INSERT INTO `domain_type` VALUES (1,'STUDY',NULL);
INSERT INTO `domain_type` VALUES (2,'STUDY COMPONENT',NULL);
/*!40000 ALTER TABLE `domain_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_account`
--

DROP TABLE IF EXISTS `email_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_account` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `PRIMARY_ACCOUNT` int(11) DEFAULT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `EMAIL_ACCOUNT_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMAIL_ACCOUNT_PER_FK1` (`PERSON_ID`) USING BTREE,
  KEY `EMAIL_ACCOUNT_EMA_FK1` (`EMAIL_ACCOUNT_TYPE_ID`) USING BTREE,
  CONSTRAINT `email_account_ibfk_1` FOREIGN KEY (`EMAIL_ACCOUNT_TYPE_ID`) REFERENCES `email_account_type` (`ID`),
  CONSTRAINT `email_account_ibfk_2` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`EMAIL_ACCOUNT_TYPE_ID`) REFER `study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_account`
--

LOCK TABLES `email_account` WRITE;
/*!40000 ALTER TABLE `email_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_account_type`
--

DROP TABLE IF EXISTS `email_account_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_account_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_account_type`
--

LOCK TABLES `email_account_type` WRITE;
/*!40000 ALTER TABLE `email_account_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_account_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_status`
--

DROP TABLE IF EXISTS `email_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_status` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Status (something like  Unverified, Verified, Bounced, Unknown)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_status`
--

LOCK TABLES `email_status` WRITE;
/*!40000 ALTER TABLE `email_status` DISABLE KEYS */;
INSERT INTO `email_status` VALUES (0,'Unknown','A default where status has not been specified');
INSERT INTO `email_status` VALUES (1,'Verified','Verified as a valid email');
INSERT INTO `email_status` VALUES (2,'Unverified','Not verified as a valid email address');
INSERT INTO `email_status` VALUES (3,'Bounced','An email to this address bounced');
/*!40000 ALTER TABLE `email_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity_type`
--

DROP TABLE IF EXISTS `entity_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_type`
--

LOCK TABLES `entity_type` WRITE;
/*!40000 ALTER TABLE `entity_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `entity_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `family_custom_field_data`
--

DROP TABLE IF EXISTS `family_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `family_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FAMILY_UID` varchar(8) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_family_custom_field_custom_field_display_idx` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `fk_family_custom_field_data_study_id_idx` (`STUDY_ID`),
  CONSTRAINT `fk_family_custom_field_custom_field_display` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_family_custom_field_data_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `family_custom_field_data`
--

LOCK TABLES `family_custom_field_data` WRITE;
/*!40000 ALTER TABLE `family_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `family_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `VISIBLE` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` VALUES (1,'CHARACTER',1);
INSERT INTO `field_type` VALUES (2,'NUMBER',1);
INSERT INTO `field_type` VALUES (3,'DATE',1);
INSERT INTO `field_type` VALUES (4,'LOOKUP',0);
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_format`
--

DROP TABLE IF EXISTS `file_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_format` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` VALUES (1,'CSV','Comma separated values');
INSERT INTO `file_format` VALUES (2,'TXT','Tab separated text file');
INSERT INTO `file_format` VALUES (3,'XLS','Excel Spreadsheet');
INSERT INTO `file_format` VALUES (4,'PED','Pedigree format');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gender_type`
--

DROP TABLE IF EXISTS `gender_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gender_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gender_type`
--

LOCK TABLES `gender_type` WRITE;
/*!40000 ALTER TABLE `gender_type` DISABLE KEYS */;
INSERT INTO `gender_type` VALUES (0,'Unknown',NULL);
INSERT INTO `gender_type` VALUES (1,'Male',NULL);
INSERT INTO `gender_type` VALUES (2,'Female',NULL);
/*!40000 ALTER TABLE `gender_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_site_contact`
--

DROP TABLE IF EXISTS `link_site_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_site_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_SITE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_SITE_CONTACT_FK1` (`PERSON_ID`) USING BTREE,
  KEY `LINK_SITE_CONTACT_STUDY_SITE_FK` (`STUDY_SITE_ID`) USING BTREE,
  CONSTRAINT `link_site_contact_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_site_contact_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_site_contact`
--

LOCK TABLES `link_site_contact` WRITE;
/*!40000 ALTER TABLE `link_site_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_site_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_study_arkmodule`
--

DROP TABLE IF EXISTS `link_study_arkmodule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_arkmodule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_MODULE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_LINK_STUDY_ARKMODULE_STUDY_ID` (`STUDY_ID`),
  KEY `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` (`ARK_MODULE_ID`),
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_study_arkmodule`
--

LOCK TABLES `link_study_arkmodule` WRITE;
/*!40000 ALTER TABLE `link_study_arkmodule` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_study_arkmodule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_study_studycomp`
--

DROP TABLE IF EXISTS `link_study_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_studycomp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LSSC_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `LSSC_STUDY_COMP_FK` (`STUDY_COMP_ID`) USING BTREE,
  KEY `LSSC_STUDY_COMP_STATUS_FK` (`STUDY_COMP_STATUS_ID`) USING BTREE,
  CONSTRAINT `link_study_studycomp_ibfk_1` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`),
  CONSTRAINT `link_study_studycomp_ibfk_2` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`),
  CONSTRAINT `link_study_studycomp_ibfk_3` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_COMP_ID`) REFER `study/study_c';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_study_studycomp`
--

LOCK TABLES `link_study_studycomp` WRITE;
/*!40000 ALTER TABLE `link_study_studycomp` DISABLE KEYS */;
INSERT INTO `link_study_studycomp` VALUES (1,4,15,1);
/*!40000 ALTER TABLE `link_study_studycomp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_study_studysite`
--

DROP TABLE IF EXISTS `link_study_studysite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_studysite` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_SITE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_STUDY_STUDYSITE_STUDY_SITE_FK` (`STUDY_SITE_ID`) USING BTREE,
  KEY `LINK_STUDY_STUDYSITE_STUDY_FK` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `link_study_studysite_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `link_study_studysite_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_study_studysite`
--

LOCK TABLES `link_study_studysite` WRITE;
/*!40000 ALTER TABLE `link_study_studysite` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_study_studysite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_study_substudy`
--

DROP TABLE IF EXISTS `link_study_substudy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_substudy` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `SUB_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_STUDY_SUBSTUDY_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK` (`SUB_STUDY_ID`) USING BTREE,
  CONSTRAINT `link_study_substudy_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `link_study_substudy_ibfk_2` FOREIGN KEY (`SUB_STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_study_substudy`
--

LOCK TABLES `link_study_substudy` WRITE;
/*!40000 ALTER TABLE `link_study_substudy` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_study_substudy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_subject_contact`
--

DROP TABLE IF EXISTS `link_subject_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_CONTACT_ID` int(11) DEFAULT NULL,
  `PERSON_SUBJECT_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `RELATIONSHIP_ID` int(11) DEFAULT NULL,
  `FAMILY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_SUBJECT_CONTACT_PERSON_FK` (`PERSON_CONTACT_ID`) USING BTREE,
  KEY `LINK_SUBJECT_CONTACT_PERSON_SUBJECT_FK` (`PERSON_SUBJECT_ID`) USING BTREE,
  KEY `LINK_SUBJECT_CONTACT_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `LINK_SUBJECT_CONTACT_RELATIONSHIP_FK` (`RELATIONSHIP_ID`) USING BTREE,
  CONSTRAINT `link_subject_contact_ibfk_1` FOREIGN KEY (`PERSON_CONTACT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_2` FOREIGN KEY (`PERSON_SUBJECT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_3` FOREIGN KEY (`RELATIONSHIP_ID`) REFERENCES `relationship` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_4` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_CONTACT_ID`) REFER `study/per';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_subject_contact`
--

LOCK TABLES `link_subject_contact` WRITE;
/*!40000 ALTER TABLE `link_subject_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_subject_pedigree`
--

DROP TABLE IF EXISTS `link_subject_pedigree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_pedigree` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) DEFAULT NULL,
  `RELATIVE_ID` int(11) DEFAULT NULL,
  `RELATIONSHIP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_link_subject_pedigree_subject_in_context_fk_idx` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_link_subject_pedigree_parent_relative_fk_idx` (`RELATIVE_ID`),
  KEY `fk_link_subject_pedigree_relationship_fk_idx` (`RELATIONSHIP_ID`),
  CONSTRAINT `fk_link_subject_pedigree_parent_relative_fk` FOREIGN KEY (`RELATIVE_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_link_subject_pedigree_relationship_fk` FOREIGN KEY (`RELATIONSHIP_ID`) REFERENCES `relationship` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_link_subject_pedigree_subject_in_context_fk` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_subject_pedigree`
--

LOCK TABLES `link_subject_pedigree` WRITE;
/*!40000 ALTER TABLE `link_subject_pedigree` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_pedigree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_subject_study`
--

DROP TABLE IF EXISTS `link_subject_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_study` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `SUBJECT_STATUS_ID` int(11) DEFAULT '1',
  `SUBJECT_UID` varchar(50) NOT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_ID` int(11) DEFAULT '1',
  `CONSENT_TYPE_ID` int(11) DEFAULT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `HEARD_ABOUT_STUDY` varchar(500) DEFAULT NULL,
  `COMMENTS` varchar(1000) DEFAULT NULL,
  `CONSENT_DOWNLOADED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`SUBJECT_UID`),
  UNIQUE KEY `UQ_STUDY_ID_SUBJECT_UID` (`STUDY_ID`,`SUBJECT_UID`) USING BTREE,
  KEY `FK_LINK_SUBJECT_STUDY_PERSON_FK` (`PERSON_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` (`SUBJECT_STATUS_ID`),
  KEY `FK_LINK_SBJT_STUDY_CNS_ACT_CNCT` (`CONSENT_TO_ACTIVE_CONTACT_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CNS_PASS_DATA` (`CONSENT_TO_PASSIVE_DATA_GATHERING_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CNS_USE_DATA` (`CONSENT_TO_USE_DATA_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_UID` (`SUBJECT_UID`) USING BTREE,
  KEY `FK_LINK_SUBJECT_STUDY_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `FK_LINK_SUBJECT_STUDY_CONSENT_STATUS_ID` (`CONSENT_STATUS_ID`) USING BTREE,
  KEY `FK_LINK_SUBJECT_STUDY_CONSENT_TYPE_ID` (`CONSENT_TYPE_ID`) USING BTREE,
  KEY `FK_CONSENT_DOWNLOADED_YES_NO` (`CONSENT_DOWNLOADED`),
  CONSTRAINT `FK_CONSENT_DOWNLOADED_YES_NO` FOREIGN KEY (`CONSENT_DOWNLOADED`) REFERENCES `yes_no` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_ACT_CTNT` FOREIGN KEY (`CONSENT_TO_ACTIVE_CONTACT_ID`) REFERENCES `consent_option` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_PASS_DATA` FOREIGN KEY (`CONSENT_TO_PASSIVE_DATA_GATHERING_ID`) REFERENCES `consent_option` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_USE_DATA` FOREIGN KEY (`CONSENT_TO_USE_DATA_ID`) REFERENCES `consent_option` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CONSENT_STATUS_ID` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CONSENT_TYPE_ID` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STUDY_FK` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` FOREIGN KEY (`SUBJECT_STATUS_ID`) REFERENCES `subject_status` (`ID`),
  CONSTRAINT `link_subject_study_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_subject_study`
--

LOCK TABLES `link_subject_study` WRITE;
/*!40000 ALTER TABLE `link_subject_study` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_study` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_subject_studycomp`
--

DROP TABLE IF EXISTS `link_subject_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_studycomp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_SUBJECT_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_SSC_PERSON_FK` (`PERSON_SUBJECT_ID`) USING BTREE,
  KEY `LINK_SSC_STUDY_COMP_FK` (`STUDY_COMP_ID`) USING BTREE,
  KEY `LINK_SUBJECT_STUDYCOM_FK3` (`STUDY_ID`) USING BTREE,
  KEY `LINK_SSC_STUDY_COMP_STATUS_FK` (`STUDY_COMP_STATUS_ID`) USING BTREE,
  CONSTRAINT `link_subject_studycomp_ibfk_1` FOREIGN KEY (`PERSON_SUBJECT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_3` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_4` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_SUBJECT_ID`) REFER `study/per';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_subject_studycomp`
--

LOCK TABLES `link_subject_studycomp` WRITE;
/*!40000 ALTER TABLE `link_subject_studycomp` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_studycomp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_subject_twin`
--

DROP TABLE IF EXISTS `link_subject_twin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_twin` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_SUBJECT` int(11) NOT NULL,
  `SECOND_SUBJECT` int(11) NOT NULL,
  `TWIN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_link_subject_twin_first_subject_fk_idx` (`FIRST_SUBJECT`),
  KEY `fk_link_subject_twin_second_subject_fk_idx` (`SECOND_SUBJECT`),
  KEY `fk_link_subject_twin_type_fk_idx` (`TWIN_TYPE_ID`),
  CONSTRAINT `fk_link_subject_twin_first_subject_fk` FOREIGN KEY (`FIRST_SUBJECT`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_link_subject_twin_second_subject_fk` FOREIGN KEY (`SECOND_SUBJECT`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_link_subject_twin_type_fk` FOREIGN KEY (`TWIN_TYPE_ID`) REFERENCES `twin_type` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_subject_twin`
--

LOCK TABLES `link_subject_twin` WRITE;
/*!40000 ALTER TABLE `link_subject_twin` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_twin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marital_status`
--

DROP TABLE IF EXISTS `marital_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marital_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marital_status`
--

LOCK TABLES `marital_status` WRITE;
/*!40000 ALTER TABLE `marital_status` DISABLE KEYS */;
INSERT INTO `marital_status` VALUES (1,'Married',NULL);
INSERT INTO `marital_status` VALUES (2,'Single',NULL);
INSERT INTO `marital_status` VALUES (3,'Divorced',NULL);
INSERT INTO `marital_status` VALUES (4,'Unknown',NULL);
/*!40000 ALTER TABLE `marital_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measurement_type`
--

DROP TABLE IF EXISTS `measurement_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measurement_type` (
  `ID` int(11) NOT NULL,
  `VALUE` varchar(64) DEFAULT NULL,
  `DESCRIPTION` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measurement_type`
--

LOCK TABLES `measurement_type` WRITE;
/*!40000 ALTER TABLE `measurement_type` DISABLE KEYS */;
INSERT INTO `measurement_type` VALUES (1,'Distance',NULL);
INSERT INTO `measurement_type` VALUES (2,'Volume',NULL);
INSERT INTO `measurement_type` VALUES (3,'Time',NULL);
INSERT INTO `measurement_type` VALUES (4,'Weight',NULL);
INSERT INTO `measurement_type` VALUES (5,'Weight per Volume',NULL);
INSERT INTO `measurement_type` VALUES (6,'Distance per Time',NULL);
INSERT INTO `measurement_type` VALUES (7,'Percentage or Fraction',NULL);
INSERT INTO `measurement_type` VALUES (999,'Other',NULL);
/*!40000 ALTER TABLE `measurement_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otherid`
--

DROP TABLE IF EXISTS `otherid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `otherid` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PersonID` int(11) NOT NULL,
  `OtherID` varchar(100) NOT NULL,
  `OtherID_Source` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otherid`
--

LOCK TABLES `otherid` WRITE;
/*!40000 ALTER TABLE `otherid` DISABLE KEYS */;
/*!40000 ALTER TABLE `otherid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `padding_character`
--

DROP TABLE IF EXISTS `padding_character`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `padding_character` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `padding_character`
--

LOCK TABLES `padding_character` WRITE;
/*!40000 ALTER TABLE `padding_character` DISABLE KEYS */;
/*!40000 ALTER TABLE `padding_character` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payload`
--

DROP TABLE IF EXISTS `payload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAYLOAD` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='This is a simple table for storing LOBs and an id to represent them.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payload`
--

LOCK TABLES `payload` WRITE;
/*!40000 ALTER TABLE `payload` DISABLE KEYS */;
/*!40000 ALTER TABLE `payload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `PREFERRED_NAME` varchar(150) DEFAULT NULL,
  `GENDER_TYPE_ID` int(11) NOT NULL,
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `DATE_OF_DEATH` date DEFAULT NULL,
  `REGISTRATION_DATE` date DEFAULT NULL,
  `CAUSE_OF_DEATH` varchar(255) DEFAULT NULL,
  `VITAL_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `TITLE_TYPE_ID` int(11) DEFAULT '0',
  `MARITAL_STATUS_ID` int(11) DEFAULT NULL,
  `PERSON_CONTACT_METHOD_ID` int(11) DEFAULT NULL,
  `PREFERRED_EMAIL` varchar(150) DEFAULT NULL,
  `OTHER_EMAIL` varchar(45) DEFAULT NULL,
  `DATE_LAST_KNOWN_ALIVE` date DEFAULT NULL,
  `OTHER_ID` int(11) DEFAULT NULL,
  `OTHER_EMAIL_STATUS` int(11) DEFAULT '0',
  `PREFERRED_EMAIL_STATUS` int(11) DEFAULT '0',
  `CURRENT_OR_DEATH_AGE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PERSON_GENDER_TYPE_FK` (`GENDER_TYPE_ID`) USING BTREE,
  KEY `PERSON_VITAL_STATUS_FK` (`VITAL_STATUS_ID`) USING BTREE,
  KEY `PERSON_TITLE_TYPE_FK` (`TITLE_TYPE_ID`) USING BTREE,
  KEY `fk_person_person_contact_method` (`PERSON_CONTACT_METHOD_ID`),
  KEY `fk_person_marital_status` (`MARITAL_STATUS_ID`),
  KEY `fk_person_other_email_status` (`OTHER_EMAIL_STATUS`),
  KEY `fk_person_preferred_email_status` (`PREFERRED_EMAIL_STATUS`),
  CONSTRAINT `fk_person_gender_type` FOREIGN KEY (`GENDER_TYPE_ID`) REFERENCES `gender_type` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_person_marital_status` FOREIGN KEY (`MARITAL_STATUS_ID`) REFERENCES `marital_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_other_email_status` FOREIGN KEY (`OTHER_EMAIL_STATUS`) REFERENCES `email_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_person_contact_method` FOREIGN KEY (`PERSON_CONTACT_METHOD_ID`) REFERENCES `person_contact_method` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_preferred_email_status` FOREIGN KEY (`PREFERRED_EMAIL_STATUS`) REFERENCES `email_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_title_type` FOREIGN KEY (`TITLE_TYPE_ID`) REFERENCES `title_type` (`ID`),
  CONSTRAINT `fk_person_vital_status` FOREIGN KEY (`VITAL_STATUS_ID`) REFERENCES `vital_status` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`GENDER_TYPE_ID`) REFER `study/gender';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_contact_method`
--

DROP TABLE IF EXISTS `person_contact_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_contact_method` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `NAME` (`NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_contact_method`
--

LOCK TABLES `person_contact_method` WRITE;
/*!40000 ALTER TABLE `person_contact_method` DISABLE KEYS */;
INSERT INTO `person_contact_method` VALUES (3,'Email');
INSERT INTO `person_contact_method` VALUES (1,'Home telephone');
INSERT INTO `person_contact_method` VALUES (2,'Mobile telephone');
INSERT INTO `person_contact_method` VALUES (4,'Post');
/*!40000 ALTER TABLE `person_contact_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_lastname_history`
--

DROP TABLE IF EXISTS `person_lastname_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_lastname_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `LASTNAME` varchar(50) NOT NULL,
  `date_inserted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `SURNAME` (`LASTNAME`) USING BTREE,
  KEY `PERSON_ID` (`PERSON_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_lastname_history`
--

LOCK TABLES `person_lastname_history` WRITE;
/*!40000 ALTER TABLE `person_lastname_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_lastname_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(10) DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `PHONE_TYPE_ID` int(11) DEFAULT NULL,
  `PHONE_STATUS_ID` int(11) DEFAULT NULL,
  `SOURCE` varchar(500) DEFAULT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENT` varchar(1000) DEFAULT NULL,
  `SILENT` int(11) DEFAULT NULL,
  `PREFERRED_PHONE_NUMBER` int(11) NOT NULL,
  `VALID_FROM` date DEFAULT NULL,
  `VALID_TO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `AREA_CODE_2` (`AREA_CODE`,`PHONE_NUMBER`,`PHONE_TYPE_ID`,`PERSON_ID`),
  KEY `PHONE_PHONE_TYPE_FK` (`PHONE_TYPE_ID`) USING BTREE,
  KEY `PHONE_PERSON_FK` (`PERSON_ID`) USING BTREE,
  KEY `phone_ibfk_3` (`PHONE_STATUS_ID`),
  KEY `phone_ibfk_4` (`SILENT`),
  CONSTRAINT `phone_ibfk_2` FOREIGN KEY (`PHONE_TYPE_ID`) REFERENCES `phone_type` (`ID`),
  CONSTRAINT `phone_ibfk_3` FOREIGN KEY (`PHONE_STATUS_ID`) REFERENCES `phone_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_4` FOREIGN KEY (`SILENT`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_5` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_status`
--

DROP TABLE IF EXISTS `phone_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_status`
--

LOCK TABLES `phone_status` WRITE;
/*!40000 ALTER TABLE `phone_status` DISABLE KEYS */;
INSERT INTO `phone_status` VALUES (0,'Unknown','Status not known, this will be the default if no status provided');
INSERT INTO `phone_status` VALUES (1,'Current',NULL);
INSERT INTO `phone_status` VALUES (2,'Current Alternative',NULL);
INSERT INTO `phone_status` VALUES (3,'Current Under Investigation',NULL);
INSERT INTO `phone_status` VALUES (4,'Valid Past',NULL);
INSERT INTO `phone_status` VALUES (5,'Incorrect or Disconnected',NULL);
/*!40000 ALTER TABLE `phone_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_type`
--

DROP TABLE IF EXISTS `phone_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_type`
--

LOCK TABLES `phone_type` WRITE;
/*!40000 ALTER TABLE `phone_type` DISABLE KEYS */;
INSERT INTO `phone_type` VALUES (1,'Mobile','Mobile  Cell Phones');
INSERT INTO `phone_type` VALUES (2,'Home','Land Home Phone');
INSERT INTO `phone_type` VALUES (3,'Work','Land Phone at Office');
/*!40000 ALTER TABLE `phone_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_answer`
--

DROP TABLE IF EXISTS `question_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ANSWER` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_answer`
--

LOCK TABLES `question_answer` WRITE;
/*!40000 ALTER TABLE `question_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registration_status`
--

DROP TABLE IF EXISTS `registration_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registration_status` (
  `ID` int(11) NOT NULL,
  `REGISTRATION_STATUS` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registration_status`
--

LOCK TABLES `registration_status` WRITE;
/*!40000 ALTER TABLE `registration_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `registration_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship`
--

DROP TABLE IF EXISTS `relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship`
--

LOCK TABLES `relationship` WRITE;
/*!40000 ALTER TABLE `relationship` DISABLE KEYS */;
INSERT INTO `relationship` VALUES (0,'Father','Father');
INSERT INTO `relationship` VALUES (1,'Mother','Mother');
/*!40000 ALTER TABLE `relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `role_policy`
--

DROP TABLE IF EXISTS `role_policy`;
/*!50001 DROP VIEW IF EXISTS `role_policy`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `role_policy` (
  `Role` tinyint NOT NULL,
  `Module` tinyint NOT NULL,
  `FunctionGroup` tinyint NOT NULL,
  `Permission` tinyint NOT NULL,
  `Function` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COUNTRY_ID` int(11) NOT NULL,
  `TYPE` varchar(45) NOT NULL COMMENT 'what they call a ''state'', ''province'' , etc\ncan be multiple for a country\neg; au has state and territory',
  `NAME` varchar(255) NOT NULL,
  `CODE` varchar(45) NOT NULL,
  `SHORT_NAME` varchar(56) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_country_id` (`COUNTRY_ID`) USING BTREE,
  CONSTRAINT `fk_country_id` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4552 DEFAULT CHARSET=latin1 COMMENT='A link table that associates a country and its respective st';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` VALUES (1,1,'State','Western Australia','AU-WA','WA');
INSERT INTO `state` VALUES (2,1,'State','New South Wales','AU-NSW','NSW');
INSERT INTO `state` VALUES (3,1,'State','Victoria','AU-VIC','VIC');
INSERT INTO `state` VALUES (4,1,'Territory','Australian Capital Territory','AU-ACT','ACT');
INSERT INTO `state` VALUES (5,1,'Territory','Northern Territory','AU-NT','NT');
INSERT INTO `state` VALUES (6,1,'State','Queensland','AU-QLD','QLD');
INSERT INTO `state` VALUES (7,3,'','','','Alberta');
INSERT INTO `state` VALUES (8,2,'','','','Bedfordshire');
INSERT INTO `state` VALUES (9,2,'','','','Berkshire');
INSERT INTO `state` VALUES (10,1,'State','Tasmania','AU-TAS','TAS');
INSERT INTO `state` VALUES (11,1,'State','South Australia','AU-SA','SA');
INSERT INTO `state` VALUES (12,9,'Parish','Andorra la Vella','AD-07','');
INSERT INTO `state` VALUES (13,9,'Parish','Canillo','AD-02','');
INSERT INTO `state` VALUES (14,9,'Parish','Encamp','AD-03','');
INSERT INTO `state` VALUES (15,9,'Parish','Escaldes-Engordany','AD-08','');
INSERT INTO `state` VALUES (16,9,'Parish','La Massana','AD-04','');
INSERT INTO `state` VALUES (17,9,'Parish','Ordino','AD-05','');
INSERT INTO `state` VALUES (18,9,'Parish','Sant Julià de Lòria','AD-06','');
INSERT INTO `state` VALUES (19,236,'Emirate','Ab? ?aby [Abu Dhabi]','AE-AZ','');
INSERT INTO `state` VALUES (20,236,'Emirate','\'Ajm?n','AE-AJ','');
INSERT INTO `state` VALUES (21,236,'Emirate','Al Fujayrah','AE-FU','');
INSERT INTO `state` VALUES (22,236,'Emirate','Ash Sh?riqah','AE-SH','');
INSERT INTO `state` VALUES (23,236,'Emirate','Dubayy','AE-DU','');
INSERT INTO `state` VALUES (24,236,'Emirate','Ra’s al Khaymah','AE-RK','');
INSERT INTO `state` VALUES (25,236,'Emirate','Umm al Qaywayn','AE-UQ','');
INSERT INTO `state` VALUES (26,4,'Province','Badakhsh?n','AF-BDS','');
INSERT INTO `state` VALUES (27,4,'Province','B?dgh?s','AF-BDG','');
INSERT INTO `state` VALUES (28,4,'Province','Baghl?n','AF-BGL','');
INSERT INTO `state` VALUES (29,4,'Province','Balkh','AF-BAL','');
INSERT INTO `state` VALUES (30,4,'Province','B?m??n','AF-BAM','');
INSERT INTO `state` VALUES (31,4,'Province','D?ykond?','AF-DAY','');
INSERT INTO `state` VALUES (32,4,'Province','Far?h','AF-FRA','');
INSERT INTO `state` VALUES (33,4,'Province','F?ry?b','AF-FYB','');
INSERT INTO `state` VALUES (34,4,'Province','Ghazn?','AF-GHA','');
INSERT INTO `state` VALUES (35,4,'Province','Ghowr','AF-GHO','');
INSERT INTO `state` VALUES (36,4,'Province','Helmand','AF-HEL','');
INSERT INTO `state` VALUES (37,4,'Province','Her?t','AF-HER','');
INSERT INTO `state` VALUES (38,4,'Province','Jowzj?n','AF-JOW','');
INSERT INTO `state` VALUES (39,4,'Province','K?bul [K?bol]','AF-KAB','');
INSERT INTO `state` VALUES (40,4,'Province','Kandah?r','AF-KAN','');
INSERT INTO `state` VALUES (41,4,'Province','K?p?s?','AF-KAP','');
INSERT INTO `state` VALUES (42,4,'Province','Khowst','AF-KHO','');
INSERT INTO `state` VALUES (43,4,'Province','Konar [Kunar]','AF-KNR','');
INSERT INTO `state` VALUES (44,4,'Province','Kondoz [Kunduz]','AF-KDZ','');
INSERT INTO `state` VALUES (45,4,'Province','Laghm?n','AF-LAG','');
INSERT INTO `state` VALUES (46,4,'Province','Lowgar','AF-LOW','');
INSERT INTO `state` VALUES (47,4,'Province','Nangrah?r [Nangarh?r]','AF-NAN','');
INSERT INTO `state` VALUES (48,4,'Province','N?mr?z','AF-NIM','');
INSERT INTO `state` VALUES (49,4,'Province','N?rest?n','AF-NUR','');
INSERT INTO `state` VALUES (50,4,'Province','Or?zg?n [Ur?zg?n]','AF-ORU','');
INSERT INTO `state` VALUES (51,4,'Province','Panjsh?r','AF-PAN','');
INSERT INTO `state` VALUES (52,4,'Province','Pakt??','AF-PIA','');
INSERT INTO `state` VALUES (53,4,'Province','Pakt?k?','AF-PKA','');
INSERT INTO `state` VALUES (54,4,'Province','Parw?n','AF-PAR','');
INSERT INTO `state` VALUES (55,4,'Province','Samang?n','AF-SAM','');
INSERT INTO `state` VALUES (56,4,'Province','Sar-e Pol','AF-SAR','');
INSERT INTO `state` VALUES (57,4,'Province','Takh?r','AF-TAK','');
INSERT INTO `state` VALUES (58,4,'Province','Wardak [Wardag]','AF-WAR','');
INSERT INTO `state` VALUES (59,4,'Province','Z?bol [Z?bul]','AF-ZAB','');
INSERT INTO `state` VALUES (60,13,'Parish','Saint George','AG-03','');
INSERT INTO `state` VALUES (61,13,'Parish','Saint John','AG-04','');
INSERT INTO `state` VALUES (62,13,'Parish','Saint Mary','AG-05','');
INSERT INTO `state` VALUES (63,13,'Parish','Saint Paul','AG-06','');
INSERT INTO `state` VALUES (64,13,'Parish','Saint Peter','AG-07','');
INSERT INTO `state` VALUES (65,13,'Parish','Saint Philip','AG-08','');
INSERT INTO `state` VALUES (66,13,'Dependency','Barbuda','AG-10','');
INSERT INTO `state` VALUES (67,6,'County','Berat','AL 1','');
INSERT INTO `state` VALUES (68,6,'County','Dibër','AL 9','');
INSERT INTO `state` VALUES (69,6,'County','Durrës','AL 2','');
INSERT INTO `state` VALUES (70,6,'County','Elbasan','AL 3','');
INSERT INTO `state` VALUES (71,6,'County','Fier','AL 4','');
INSERT INTO `state` VALUES (72,6,'County','Gjirokastër','AL 5','');
INSERT INTO `state` VALUES (73,6,'County','Korçë','AL 6','');
INSERT INTO `state` VALUES (74,6,'County','Kukës','AL 7','');
INSERT INTO `state` VALUES (75,6,'County','Lezhë','AL 8','');
INSERT INTO `state` VALUES (76,6,'County','Shkodër','AL 10','');
INSERT INTO `state` VALUES (77,6,'County','Tiranë','AL 11','');
INSERT INTO `state` VALUES (78,6,'County','Vlorë','AL 12','');
INSERT INTO `state` VALUES (79,6,'District','Berat','AL-BR','');
INSERT INTO `state` VALUES (80,6,'District','Bulqizë','AL-BU','');
INSERT INTO `state` VALUES (81,6,'District','Delvinë','AL-DL','');
INSERT INTO `state` VALUES (82,6,'District','Devoll','AL-DV','');
INSERT INTO `state` VALUES (83,6,'District','Dibër','AL-DI','');
INSERT INTO `state` VALUES (84,6,'District','Durrës','AL-DR','');
INSERT INTO `state` VALUES (85,6,'District','Elbasan','AL-EL','');
INSERT INTO `state` VALUES (86,6,'District','Fier','AL-FR','');
INSERT INTO `state` VALUES (87,6,'District','Gramsh','AL-GR','');
INSERT INTO `state` VALUES (88,6,'District','Gjirokastër','AL-GJ','');
INSERT INTO `state` VALUES (89,6,'District','Has','AL-HA','');
INSERT INTO `state` VALUES (90,6,'District','Kavajë','AL-KA','');
INSERT INTO `state` VALUES (91,6,'District','Kolonjë','AL-ER','');
INSERT INTO `state` VALUES (92,6,'District','Korçë','AL-KO','');
INSERT INTO `state` VALUES (93,6,'District','Krujë','AL-KR','');
INSERT INTO `state` VALUES (94,6,'District','Kuçovë','AL-KC','');
INSERT INTO `state` VALUES (95,6,'District','Kukës','AL-KU','');
INSERT INTO `state` VALUES (96,6,'District','Kurbin','AL-KB','');
INSERT INTO `state` VALUES (97,6,'District','Lezhë','AL-LE','');
INSERT INTO `state` VALUES (98,6,'District','Librazhd','AL-LB','');
INSERT INTO `state` VALUES (99,6,'District','Lushnjë','AL-LU','');
INSERT INTO `state` VALUES (100,6,'District','Malësi e Madhe','AL-MM','');
INSERT INTO `state` VALUES (101,6,'District','Mallakastër','AL-MK','');
INSERT INTO `state` VALUES (102,6,'District','Mat','AL-MT','');
INSERT INTO `state` VALUES (103,6,'District','Mirditë','AL-MR','');
INSERT INTO `state` VALUES (104,6,'District','Peqin','AL-PQ','');
INSERT INTO `state` VALUES (105,6,'District','Përmet','AL-PR','');
INSERT INTO `state` VALUES (106,6,'District','Pogradec','AL-PG','');
INSERT INTO `state` VALUES (107,6,'District','Pukë','AL-PU','');
INSERT INTO `state` VALUES (108,6,'District','Sarandë','AL-SR','');
INSERT INTO `state` VALUES (109,6,'District','Skrapar','AL-SK','');
INSERT INTO `state` VALUES (110,6,'District','Shkodër','AL-SH','');
INSERT INTO `state` VALUES (111,6,'District','Tepelenë','AL-TE','');
INSERT INTO `state` VALUES (112,6,'District','Tiranë','AL-TR','');
INSERT INTO `state` VALUES (113,6,'District','Tropojë','AL-TP','');
INSERT INTO `state` VALUES (114,6,'District','Vlorë','AL-VL','');
INSERT INTO `state` VALUES (115,15,'Province','Erevan','AM-ER','');
INSERT INTO `state` VALUES (116,15,'Province','Aragacotn','AM-AG','');
INSERT INTO `state` VALUES (117,15,'Province','Ararat','AM-AR','');
INSERT INTO `state` VALUES (118,15,'Province','Armavir','AM-AV','');
INSERT INTO `state` VALUES (119,15,'Province','Gegarkunik\'','AM-GR','');
INSERT INTO `state` VALUES (120,15,'Province','Kotayk\'','AM-KT','');
INSERT INTO `state` VALUES (121,15,'Province','Lory','AM-LO','');
INSERT INTO `state` VALUES (122,15,'Province','Sirak','AM-SH','');
INSERT INTO `state` VALUES (123,15,'Province','Syunik\'','AM-SU','');
INSERT INTO `state` VALUES (124,15,'Province','Tavus','AM-TV','');
INSERT INTO `state` VALUES (125,15,'Province','Vayoc Jor','AM-VD','');
INSERT INTO `state` VALUES (126,10,'Province','Bengo','AO-BGO','');
INSERT INTO `state` VALUES (127,10,'Province','Benguela','AO-BGU','');
INSERT INTO `state` VALUES (128,10,'Province','Bié','AO-BIE','');
INSERT INTO `state` VALUES (129,10,'Province','Cabinda','AO-CAB','');
INSERT INTO `state` VALUES (130,10,'Province','Cuando-Cubango','AO-CCU','');
INSERT INTO `state` VALUES (131,10,'Province','Cuanza Norte','AO-CNO','');
INSERT INTO `state` VALUES (132,10,'Province','Cuanza Sul','AO-CUS','');
INSERT INTO `state` VALUES (133,10,'Province','Cunene','AO-CNN','');
INSERT INTO `state` VALUES (134,10,'Province','Huambo','AO-HUA','');
INSERT INTO `state` VALUES (135,10,'Province','Huíla','AO-HUI','');
INSERT INTO `state` VALUES (136,10,'Province','Luanda','AO-LUA','');
INSERT INTO `state` VALUES (137,10,'Province','Lunda Norte','AO-LNO','');
INSERT INTO `state` VALUES (138,10,'Province','Lunda Sul','AO-LSU','');
INSERT INTO `state` VALUES (139,10,'Province','Malange','AO-MAL','');
INSERT INTO `state` VALUES (140,10,'Province','Moxico','AO-MOX','');
INSERT INTO `state` VALUES (141,10,'Province','Namibe','AO-NAM','');
INSERT INTO `state` VALUES (142,10,'Province','Uíge','AO-UIG','');
INSERT INTO `state` VALUES (143,10,'Province','Zaire','AO-ZAI','');
INSERT INTO `state` VALUES (144,14,'Province','Capital federal','AR-C','');
INSERT INTO `state` VALUES (145,14,'Province','Buenos Aires','AR-B','');
INSERT INTO `state` VALUES (146,14,'Province','Catamarca','AR-K','');
INSERT INTO `state` VALUES (147,14,'Province','Cordoba','AR-X','');
INSERT INTO `state` VALUES (148,14,'Province','Corrientes','AR-W','');
INSERT INTO `state` VALUES (149,14,'Province','Chaco','AR-H','');
INSERT INTO `state` VALUES (150,14,'Province','Chubut','AR-U','');
INSERT INTO `state` VALUES (151,14,'Province','Entre Rios','AR-E','');
INSERT INTO `state` VALUES (152,14,'Province','Formosa','AR-P','');
INSERT INTO `state` VALUES (153,14,'Province','Jujuy','AR-Y','');
INSERT INTO `state` VALUES (154,14,'Province','La Pampa','AR-L','');
INSERT INTO `state` VALUES (155,14,'Province','Mendoza','AR-M','');
INSERT INTO `state` VALUES (156,14,'Province','Misiones','AR-N','');
INSERT INTO `state` VALUES (157,14,'Province','Neuquen','AR-Q','');
INSERT INTO `state` VALUES (158,14,'Province','Rio Negro','AR-R','');
INSERT INTO `state` VALUES (159,14,'Province','Salta','AR-A','');
INSERT INTO `state` VALUES (160,14,'Province','San Juan','AR-J','');
INSERT INTO `state` VALUES (161,14,'Province','San Luis','AR-D','');
INSERT INTO `state` VALUES (162,14,'Province','Santa Cruz','AR-Z','');
INSERT INTO `state` VALUES (163,14,'Province','Santa Fe','AR-S','');
INSERT INTO `state` VALUES (164,14,'Province','Santiago del Estero','AR-G','');
INSERT INTO `state` VALUES (165,14,'Province','Tierra del Fuego','AR-V','');
INSERT INTO `state` VALUES (166,14,'Province','Tucuman','AR-T','');
INSERT INTO `state` VALUES (167,18,'State','Burgenland','AT-1','');
INSERT INTO `state` VALUES (168,18,'State','Kärnten','AT-2','');
INSERT INTO `state` VALUES (169,18,'State','Niederösterreich','AT-3','');
INSERT INTO `state` VALUES (170,18,'State','Oberösterreich','AT-4','');
INSERT INTO `state` VALUES (171,18,'State','Salzburg','AT-5','');
INSERT INTO `state` VALUES (172,18,'State','Steiermark','AT-6','');
INSERT INTO `state` VALUES (173,18,'State','Tirol','AT-7','');
INSERT INTO `state` VALUES (174,18,'State','Vorarlberg','AT-8','');
INSERT INTO `state` VALUES (175,18,'State','Wien','AT-9','');
INSERT INTO `state` VALUES (176,19,'Autonomous republic','Naxç?van','AZ NX','');
INSERT INTO `state` VALUES (177,19,'City','?li Bayraml?','AZ-AB','');
INSERT INTO `state` VALUES (178,19,'City','Bak?','AZ-BA','');
INSERT INTO `state` VALUES (179,19,'City','G?nc?','AZ-GA','');
INSERT INTO `state` VALUES (180,19,'City','L?nk?ran','AZ-LA','');
INSERT INTO `state` VALUES (181,19,'City','Ming?çevir','AZ-MI','');
INSERT INTO `state` VALUES (182,19,'City','Naftalan','AZ-NA','');
INSERT INTO `state` VALUES (183,19,'City','??ki','AZ-SA','');
INSERT INTO `state` VALUES (184,19,'City','Sumqay?t','AZ-SM','');
INSERT INTO `state` VALUES (185,19,'City','?u?a','AZ-SS','');
INSERT INTO `state` VALUES (186,19,'City','Xank?ndi','AZ-XA','');
INSERT INTO `state` VALUES (187,19,'City','Yevlax','AZ-YE','');
INSERT INTO `state` VALUES (188,19,'Rayon','Ab?eron','AZ-ABS','');
INSERT INTO `state` VALUES (189,19,'Rayon','A?cab?di','AZ-AGC','');
INSERT INTO `state` VALUES (190,19,'Rayon','A?dam','AZ-AGM','');
INSERT INTO `state` VALUES (191,19,'Rayon','A?da?','AZ-AGS','');
INSERT INTO `state` VALUES (192,19,'Rayon','A?stafa','AZ-AGA','');
INSERT INTO `state` VALUES (193,19,'Rayon','A?su','AZ-AGU','');
INSERT INTO `state` VALUES (194,19,'Rayon','Astara','AZ-AST','');
INSERT INTO `state` VALUES (195,19,'Rayon','Bab?k','AZ-BAB','');
INSERT INTO `state` VALUES (196,19,'Rayon','Balak?n','AZ-BAL','');
INSERT INTO `state` VALUES (197,19,'Rayon','B?rd?','AZ-BAR','');
INSERT INTO `state` VALUES (198,19,'Rayon','Beyl?qan','AZ-BEY','');
INSERT INTO `state` VALUES (199,19,'Rayon','Bil?suvar','AZ-BIL','');
INSERT INTO `state` VALUES (200,19,'Rayon','C?bray?l','AZ-CAB','');
INSERT INTO `state` VALUES (201,19,'Rayon','C?lilabab','AZ-CAL','');
INSERT INTO `state` VALUES (202,19,'Rayon','Culfa','AZ-CUL','');
INSERT INTO `state` VALUES (203,19,'Rayon','Da?k?s?n','AZ-DAS','');
INSERT INTO `state` VALUES (204,19,'Rayon','D?v?çi','AZ-DAV','');
INSERT INTO `state` VALUES (205,19,'Rayon','Füzuli','AZ-FUZ','');
INSERT INTO `state` VALUES (206,19,'Rayon','G?d?b?y','AZ-GAD','');
INSERT INTO `state` VALUES (207,19,'Rayon','Goranboy','AZ-GOR','');
INSERT INTO `state` VALUES (208,19,'Rayon','Göyçay','AZ-GOY','');
INSERT INTO `state` VALUES (209,19,'Rayon','Hac?qabul','AZ-HAC','');
INSERT INTO `state` VALUES (210,19,'Rayon','?mi?li','AZ-IMI','');
INSERT INTO `state` VALUES (211,19,'Rayon','?smay?ll?','AZ-ISM','');
INSERT INTO `state` VALUES (212,19,'Rayon','K?lb?c?r','AZ-KAL','');
INSERT INTO `state` VALUES (213,19,'Rayon','Kürd?mir','AZ-KUR','');
INSERT INTO `state` VALUES (214,19,'Rayon','Laç?n','AZ-LAC','');
INSERT INTO `state` VALUES (215,19,'Rayon','L?nk?ran','AZ-LAN','');
INSERT INTO `state` VALUES (216,19,'Rayon','Lerik','AZ-LER','');
INSERT INTO `state` VALUES (217,19,'Rayon','Masall?','AZ-MAS','');
INSERT INTO `state` VALUES (218,19,'Rayon','Neftçala','AZ-NEF','');
INSERT INTO `state` VALUES (219,19,'Rayon','O?uz','AZ-OGU','');
INSERT INTO `state` VALUES (220,19,'Rayon','Ordubad','AZ-ORD','');
INSERT INTO `state` VALUES (221,19,'Rayon','Q?b?l?','AZ-QAB','');
INSERT INTO `state` VALUES (222,19,'Rayon','Qax','AZ-QAX','');
INSERT INTO `state` VALUES (223,19,'Rayon','Qazax','AZ-QAZ','');
INSERT INTO `state` VALUES (224,19,'Rayon','Qobustan','AZ-QOB','');
INSERT INTO `state` VALUES (225,19,'Rayon','Quba','AZ-QBA','');
INSERT INTO `state` VALUES (226,19,'Rayon','Qubadl?','AZ-QBI','');
INSERT INTO `state` VALUES (227,19,'Rayon','Qusar','AZ-QUS','');
INSERT INTO `state` VALUES (228,19,'Rayon','Saatl?','AZ-SAT','');
INSERT INTO `state` VALUES (229,19,'Rayon','Sabirabad','AZ-SAB','');
INSERT INTO `state` VALUES (230,19,'Rayon','S?d?r?k','AZ-SAD','');
INSERT INTO `state` VALUES (231,19,'Rayon','?ahbuz','AZ-SAH','');
INSERT INTO `state` VALUES (232,19,'Rayon','??ki','AZ-SAK','');
INSERT INTO `state` VALUES (233,19,'Rayon','Salyan','AZ-SAL','');
INSERT INTO `state` VALUES (234,19,'Rayon','?amax?','AZ-SMI','');
INSERT INTO `state` VALUES (235,19,'Rayon','??mkir','AZ-SKR','');
INSERT INTO `state` VALUES (236,19,'Rayon','Samux','AZ-SMX','');
INSERT INTO `state` VALUES (237,19,'Rayon','??rur','AZ-SAR','');
INSERT INTO `state` VALUES (238,19,'Rayon','Siy?z?n','AZ-SIY','');
INSERT INTO `state` VALUES (239,19,'Rayon','?u?a','AZ-SUS','');
INSERT INTO `state` VALUES (240,19,'Rayon','T?rt?r','AZ-TAR','');
INSERT INTO `state` VALUES (241,19,'Rayon','Tovuz','AZ-TOV','');
INSERT INTO `state` VALUES (242,19,'Rayon','Ucar','AZ-UCA','');
INSERT INTO `state` VALUES (243,19,'Rayon','Xaçmaz','AZ-XAC','');
INSERT INTO `state` VALUES (244,19,'Rayon','Xanlar','AZ-XAN','');
INSERT INTO `state` VALUES (245,19,'Rayon','X?z?','AZ-XIZ','');
INSERT INTO `state` VALUES (246,19,'Rayon','Xocal?','AZ-XCI','');
INSERT INTO `state` VALUES (247,19,'Rayon','Xocav?nd','AZ-XVD','');
INSERT INTO `state` VALUES (248,19,'Rayon','Yard?ml?','AZ-YAR','');
INSERT INTO `state` VALUES (249,19,'Rayon','Yevlax','AZ-YEV','');
INSERT INTO `state` VALUES (250,19,'Rayon','Z?ngilan','AZ-ZAN','');
INSERT INTO `state` VALUES (251,19,'Rayon','Zaqatala','AZ-ZAQ','');
INSERT INTO `state` VALUES (252,19,'Rayon','Z?rdab','AZ-ZAR','');
INSERT INTO `state` VALUES (253,32,'Entity','Federacija Bosna i Hercegovina','BA-BIH','');
INSERT INTO `state` VALUES (254,32,'Entity','Republika Srpska','BA-SRP','');
INSERT INTO `state` VALUES (255,23,'Parish','Christ Church','BB-01','');
INSERT INTO `state` VALUES (256,23,'Parish','Saint Andrew','BB-02','');
INSERT INTO `state` VALUES (257,23,'Parish','Saint George','BB-03','');
INSERT INTO `state` VALUES (258,23,'Parish','Saint James','BB-04','');
INSERT INTO `state` VALUES (259,23,'Parish','Saint John','BB-05','');
INSERT INTO `state` VALUES (260,23,'Parish','Saint Joseph','BB-06','');
INSERT INTO `state` VALUES (261,23,'Parish','Saint Lucy','BB-07','');
INSERT INTO `state` VALUES (262,23,'Parish','Saint Michael','BB-08','');
INSERT INTO `state` VALUES (263,23,'Parish','Saint Peter','BB-09','');
INSERT INTO `state` VALUES (264,23,'Parish','Saint Philip','BB-10','');
INSERT INTO `state` VALUES (265,23,'Parish','Saint Thomas','BB-11','');
INSERT INTO `state` VALUES (266,22,'Division','Barisal bibhag','BD 1','');
INSERT INTO `state` VALUES (267,22,'Division','Chittagong bibhag','BD 2','');
INSERT INTO `state` VALUES (268,22,'Division','Dhaka bibhag','BD 3','');
INSERT INTO `state` VALUES (269,22,'Division','Khulna bibhag','BD 4','');
INSERT INTO `state` VALUES (270,22,'Division','Rajshahi bibhag','BD 5','');
INSERT INTO `state` VALUES (271,22,'Division','Sylhet bibhag','BD 6','');
INSERT INTO `state` VALUES (272,22,'District','Bagerhat zila','BD-05','');
INSERT INTO `state` VALUES (273,22,'District','Bandarban zila','BD-01','');
INSERT INTO `state` VALUES (274,22,'District','Barguna zila','BD-02','');
INSERT INTO `state` VALUES (275,22,'District','Barisal zila','BD-06','');
INSERT INTO `state` VALUES (276,22,'District','Bhola zila','BD-07','');
INSERT INTO `state` VALUES (277,22,'District','Bogra zila','BD-03','');
INSERT INTO `state` VALUES (278,22,'District','Brahmanbaria zila','BD-04','');
INSERT INTO `state` VALUES (279,22,'District','Chandpur zila','BD-09','');
INSERT INTO `state` VALUES (280,22,'District','Chittagong zila','BD-10','');
INSERT INTO `state` VALUES (281,22,'District','Chuadanga zila','BD-12','');
INSERT INTO `state` VALUES (282,22,'District','Comilla zila','BD-08','');
INSERT INTO `state` VALUES (283,22,'District','Cox\'s Bazar zila','BD-11','');
INSERT INTO `state` VALUES (284,22,'District','Dhaka zila','BD-13','');
INSERT INTO `state` VALUES (285,22,'District','Dinajpur zila','BD-14','');
INSERT INTO `state` VALUES (286,22,'District','Faridpur zila','BD-15','');
INSERT INTO `state` VALUES (287,22,'District','Feni zila','BD-16','');
INSERT INTO `state` VALUES (288,22,'District','Gaibandha zila','BD-19','');
INSERT INTO `state` VALUES (289,22,'District','Gazipur zila','BD-18','');
INSERT INTO `state` VALUES (290,22,'District','Gopalganj zila','BD-17','');
INSERT INTO `state` VALUES (291,22,'District','Habiganj zila','BD-20','');
INSERT INTO `state` VALUES (292,22,'District','Jaipurhat zila','BD-24','');
INSERT INTO `state` VALUES (293,22,'District','Jamalpur zila','BD-21','');
INSERT INTO `state` VALUES (294,22,'District','Jessore zila','BD-22','');
INSERT INTO `state` VALUES (295,22,'District','Jhalakati zila','BD-25','');
INSERT INTO `state` VALUES (296,22,'District','Jhenaidah zila','BD-23','');
INSERT INTO `state` VALUES (297,22,'District','Khagrachari zila','BD-29','');
INSERT INTO `state` VALUES (298,22,'District','Khulna zila','BD-27','');
INSERT INTO `state` VALUES (299,22,'District','Kishorganj zila','BD-26','');
INSERT INTO `state` VALUES (300,22,'District','Kurigram zila','BD-28','');
INSERT INTO `state` VALUES (301,22,'District','Kushtia zila','BD-30','');
INSERT INTO `state` VALUES (302,22,'District','Lakshmipur zila','BD-31','');
INSERT INTO `state` VALUES (303,22,'District','Lalmonirhat zila','BD-32','');
INSERT INTO `state` VALUES (304,22,'District','Madaripur zila','BD-36','');
INSERT INTO `state` VALUES (305,22,'District','Magura zila','BD-37','');
INSERT INTO `state` VALUES (306,22,'District','Manikganj zila','BD-33','');
INSERT INTO `state` VALUES (307,22,'District','Meherpur zila','BD-39','');
INSERT INTO `state` VALUES (308,22,'District','Moulvibazar zila','BD-38','');
INSERT INTO `state` VALUES (309,22,'District','Munshiganj zila','BD-35','');
INSERT INTO `state` VALUES (310,22,'District','Mymensingh zila','BD-34','');
INSERT INTO `state` VALUES (311,22,'District','Naogaon zila','BD-48','');
INSERT INTO `state` VALUES (312,22,'District','Narail zila','BD-43','');
INSERT INTO `state` VALUES (313,22,'District','Narayanganj zila','BD-40','');
INSERT INTO `state` VALUES (314,22,'District','Narsingdi zila','BD-42','');
INSERT INTO `state` VALUES (315,22,'District','Natore zila','BD-44','');
INSERT INTO `state` VALUES (316,22,'District','Nawabganj zila','BD-45','');
INSERT INTO `state` VALUES (317,22,'District','Netrakona zila','BD-41','');
INSERT INTO `state` VALUES (318,22,'District','Nilphamari zila','BD-46','');
INSERT INTO `state` VALUES (319,22,'District','Noakhali zila','BD-47','');
INSERT INTO `state` VALUES (320,22,'District','Pabna zila','BD-49','');
INSERT INTO `state` VALUES (321,22,'District','Panchagarh zila','BD-52','');
INSERT INTO `state` VALUES (322,22,'District','Patuakhali zila','BD-51','');
INSERT INTO `state` VALUES (323,22,'District','Pirojpur zila','BD-50','');
INSERT INTO `state` VALUES (324,22,'District','Rajbari zila','BD-53','');
INSERT INTO `state` VALUES (325,22,'District','Rajshahi zila','BD-54','');
INSERT INTO `state` VALUES (326,22,'District','Rangamati zila','BD-56','');
INSERT INTO `state` VALUES (327,22,'District','Rangpur zila','BD-55','');
INSERT INTO `state` VALUES (328,22,'District','Satkhira zila','BD-58','');
INSERT INTO `state` VALUES (329,22,'District','Shariatpur zila','BD-62','');
INSERT INTO `state` VALUES (330,22,'District','Sherpur zila','BD-57','');
INSERT INTO `state` VALUES (331,22,'District','Sirajganj zila','BD-59','');
INSERT INTO `state` VALUES (332,22,'District','Sunamganj zila','BD-61','');
INSERT INTO `state` VALUES (333,22,'District','Sylhet zila','BD-60','');
INSERT INTO `state` VALUES (334,22,'District','Tangail zila','BD-63','');
INSERT INTO `state` VALUES (335,22,'District','Thakurgaon zila','BD-64','');
INSERT INTO `state` VALUES (336,25,'Province','Antwerpen','BE-VAN','');
INSERT INTO `state` VALUES (337,25,'Province','Brabant Wallon','BE-WBR','');
INSERT INTO `state` VALUES (338,25,'Province','Brussels-Capital Region','BE-BRU','');
INSERT INTO `state` VALUES (339,25,'Province','Hainaut','BE-WHT','');
INSERT INTO `state` VALUES (340,25,'Province','Liege','BE-WLG','');
INSERT INTO `state` VALUES (341,25,'Province','Limburg','BE-VLI','');
INSERT INTO `state` VALUES (342,25,'Province','Luxembourg','BE-WLX','');
INSERT INTO `state` VALUES (343,25,'Province','Namur','BE-WNA','');
INSERT INTO `state` VALUES (344,25,'Province','Oost-Vlaanderen','BE-VOV','');
INSERT INTO `state` VALUES (345,25,'Province','Vlaams-Brabant','BE-VBR','');
INSERT INTO `state` VALUES (346,25,'Province','West-Vlaanderen','BE-VWV','');
INSERT INTO `state` VALUES (347,39,'Province','Balé','BF-BAL','');
INSERT INTO `state` VALUES (348,39,'Province','Bam','BF-BAM','');
INSERT INTO `state` VALUES (349,39,'Province','Banwa','BF-BAN','');
INSERT INTO `state` VALUES (350,39,'Province','Bazèga','BF-BAZ','');
INSERT INTO `state` VALUES (351,39,'Province','Bougouriba','BF-BGR','');
INSERT INTO `state` VALUES (352,39,'Province','Boulgou','BF-BLG','');
INSERT INTO `state` VALUES (353,39,'Province','Boulkiemdé','BF-BLK','');
INSERT INTO `state` VALUES (354,39,'Province','Comoé','BF-COM','');
INSERT INTO `state` VALUES (355,39,'Province','Ganzourgou','BF-GAN','');
INSERT INTO `state` VALUES (356,39,'Province','Gnagna','BF-GNA','');
INSERT INTO `state` VALUES (357,39,'Province','Gourma','BF-GOU','');
INSERT INTO `state` VALUES (358,39,'Province','Houet','BF-HOU','');
INSERT INTO `state` VALUES (359,39,'Province','Ioba','BF-IOB','');
INSERT INTO `state` VALUES (360,39,'Province','Kadiogo','BF-KAD','');
INSERT INTO `state` VALUES (361,39,'Province','Kénédougou','BF-KEN','');
INSERT INTO `state` VALUES (362,39,'Province','Komondjari','BF-KMD','');
INSERT INTO `state` VALUES (363,39,'Province','Kompienga','BF-KMP','');
INSERT INTO `state` VALUES (364,39,'Province','Kossi','BF-KOS','');
INSERT INTO `state` VALUES (365,39,'Province','Koulpélogo','BF-KOP','');
INSERT INTO `state` VALUES (366,39,'Province','Kouritenga','BF-KOT','');
INSERT INTO `state` VALUES (367,39,'Province','Kourwéogo','BF-KOW','');
INSERT INTO `state` VALUES (368,39,'Province','Léraba','BF-LER','');
INSERT INTO `state` VALUES (369,39,'Province','Loroum','BF-LOR','');
INSERT INTO `state` VALUES (370,39,'Province','Mouhoun','BF-MOU','');
INSERT INTO `state` VALUES (371,39,'Province','Naouri','BF-NAO','');
INSERT INTO `state` VALUES (372,39,'Province','Namentenga','BF-NAM','');
INSERT INTO `state` VALUES (373,39,'Province','Nayala','BF-NAY','');
INSERT INTO `state` VALUES (374,39,'Province','Noumbiel','BF-NOU','');
INSERT INTO `state` VALUES (375,39,'Province','Oubritenga','BF-OUB','');
INSERT INTO `state` VALUES (376,39,'Province','Oudalan','BF-OUD','');
INSERT INTO `state` VALUES (377,39,'Province','Passoré','BF-PAS','');
INSERT INTO `state` VALUES (378,39,'Province','Poni','BF-PON','');
INSERT INTO `state` VALUES (379,39,'Province','Sanguié','BF-SNG','');
INSERT INTO `state` VALUES (380,39,'Province','Sanmatenga','BF-SMT','');
INSERT INTO `state` VALUES (381,39,'Province','Séno','BF-SEN','');
INSERT INTO `state` VALUES (382,39,'Province','Sissili','BF-SIS','');
INSERT INTO `state` VALUES (383,39,'Province','Soum','BF-SOM','');
INSERT INTO `state` VALUES (384,39,'Province','Sourou','BF-SOR','');
INSERT INTO `state` VALUES (385,39,'Province','Tapoa','BF-TAP','');
INSERT INTO `state` VALUES (386,39,'Province','Tui','BF-TUI','');
INSERT INTO `state` VALUES (387,39,'Province','Yagha','BF-YAG','');
INSERT INTO `state` VALUES (388,39,'Province','Yatenga','BF-YAT','');
INSERT INTO `state` VALUES (389,39,'Province','Ziro','BF-ZIR','');
INSERT INTO `state` VALUES (390,39,'Province','Zondoma','BF-ZON','');
INSERT INTO `state` VALUES (391,39,'Province','Zoundwéogo','BF-ZOU','');
INSERT INTO `state` VALUES (392,38,'Region','Blagoevgrad','BG-01','');
INSERT INTO `state` VALUES (393,38,'Region','Burgas','BG-02','');
INSERT INTO `state` VALUES (394,38,'Region','Dobrich','BG-08','');
INSERT INTO `state` VALUES (395,38,'Region','Gabrovo','BG-07','');
INSERT INTO `state` VALUES (396,38,'Region','Haskovo','BG-26','');
INSERT INTO `state` VALUES (397,38,'Region','Kardzhali','BG-09','');
INSERT INTO `state` VALUES (398,38,'Region','Kyustendil','BG-10','');
INSERT INTO `state` VALUES (399,38,'Region','Lovech','BG-11','');
INSERT INTO `state` VALUES (400,38,'Region','Montana','BG-12','');
INSERT INTO `state` VALUES (401,38,'Region','Pazardzhik','BG-13','');
INSERT INTO `state` VALUES (402,38,'Region','Pernik','BG-14','');
INSERT INTO `state` VALUES (403,38,'Region','Pleven','BG-15','');
INSERT INTO `state` VALUES (404,38,'Region','Plovdiv','BG-16','');
INSERT INTO `state` VALUES (405,38,'Region','Razgrad','BG-17','');
INSERT INTO `state` VALUES (406,38,'Region','Ruse','BG-18','');
INSERT INTO `state` VALUES (407,38,'Region','Shumen','BG-27','');
INSERT INTO `state` VALUES (408,38,'Region','Silistra','BG-19','');
INSERT INTO `state` VALUES (409,38,'Region','Sliven','BG-20','');
INSERT INTO `state` VALUES (410,38,'Region','Smolyan','BG-21','');
INSERT INTO `state` VALUES (411,38,'Region','Sofia','BG-23','');
INSERT INTO `state` VALUES (412,38,'Region','Sofia-Grad','BG-22','');
INSERT INTO `state` VALUES (413,38,'Region','Stara Zagora','BG-24','');
INSERT INTO `state` VALUES (414,38,'Region','Targovishte','BG-25','');
INSERT INTO `state` VALUES (415,38,'Region','Varna','BG-03','');
INSERT INTO `state` VALUES (416,38,'Region','Veliko Tarnovo','BG-04','');
INSERT INTO `state` VALUES (417,38,'Region','Vidin','BG-05','');
INSERT INTO `state` VALUES (418,38,'Region','Vratsa','BG-06','');
INSERT INTO `state` VALUES (419,38,'Region','Yambol','BG-28','');
INSERT INTO `state` VALUES (420,21,'Governorate','Al Man?mah (Al ‘??imah)','BH-13','');
INSERT INTO `state` VALUES (421,21,'Governorate','Al Jan?b?yah','BH-14','');
INSERT INTO `state` VALUES (422,21,'Governorate','Al Mu?arraq','BH-15','');
INSERT INTO `state` VALUES (423,21,'Governorate','Al Wus?á','BH-16','');
INSERT INTO `state` VALUES (424,21,'Governorate','Ash Sham?l?yah','BH-17','');
INSERT INTO `state` VALUES (425,40,'Province','Bubanza','BI-BB','');
INSERT INTO `state` VALUES (426,40,'Province','Bujumbura','BI-BJ','');
INSERT INTO `state` VALUES (427,40,'Province','Bururi','BI-BR','');
INSERT INTO `state` VALUES (428,40,'Province','Cankuzo','BI-CA','');
INSERT INTO `state` VALUES (429,40,'Province','Cibitoke','BI-CI','');
INSERT INTO `state` VALUES (430,40,'Province','Gitega','BI-GI','');
INSERT INTO `state` VALUES (431,40,'Province','Karuzi','BI-KR','');
INSERT INTO `state` VALUES (432,40,'Province','Kayanza','BI-KY','');
INSERT INTO `state` VALUES (433,40,'Province','Kirundo','BI-KI','');
INSERT INTO `state` VALUES (434,40,'Province','Makamba','BI-MA','');
INSERT INTO `state` VALUES (435,40,'Province','Muramvya','BI-MU','');
INSERT INTO `state` VALUES (436,40,'Province','Mwaro','BI-MW','');
INSERT INTO `state` VALUES (437,40,'Province','Ngozi','BI-NG','');
INSERT INTO `state` VALUES (438,40,'Province','Rutana','BI-RT','');
INSERT INTO `state` VALUES (439,40,'Province','Ruyigi','BI-RY','');
INSERT INTO `state` VALUES (440,27,'Department','Alibori','BJ-AL','');
INSERT INTO `state` VALUES (441,27,'Department','Atakora','BJ-AK','');
INSERT INTO `state` VALUES (442,27,'Department','Atlantique','BJ-AQ','');
INSERT INTO `state` VALUES (443,27,'Department','Borgou','BJ-BO','');
INSERT INTO `state` VALUES (444,27,'Department','Collines','BJ-CO','');
INSERT INTO `state` VALUES (445,27,'Department','Donga','BJ-DO','');
INSERT INTO `state` VALUES (446,27,'Department','Kouffo','BJ-KO','');
INSERT INTO `state` VALUES (447,27,'Department','Littoral','BJ-LI','');
INSERT INTO `state` VALUES (448,27,'Department','Mono','BJ-MO','');
INSERT INTO `state` VALUES (449,27,'Department','Ouémé','BJ-OU','');
INSERT INTO `state` VALUES (450,27,'Department','Plateau','BJ-PL','');
INSERT INTO `state` VALUES (451,27,'Department','Zou','BJ-ZO','');
INSERT INTO `state` VALUES (452,37,'District','Belait','BN-BE','');
INSERT INTO `state` VALUES (453,37,'District','Brunei-Muara','BN-BM','');
INSERT INTO `state` VALUES (454,37,'District','Temburong','BN-TE','');
INSERT INTO `state` VALUES (455,37,'District','Tutong','BN-TU','');
INSERT INTO `state` VALUES (456,30,'Department','Cochabamba','BO-C','');
INSERT INTO `state` VALUES (457,30,'Department','Chuquisaca','BO-H','');
INSERT INTO `state` VALUES (458,30,'Department','El Beni','BO-B','');
INSERT INTO `state` VALUES (459,30,'Department','La Paz','BO-L','');
INSERT INTO `state` VALUES (460,30,'Department','Oruro','BO-O','');
INSERT INTO `state` VALUES (461,30,'Department','Pando','BO-N','');
INSERT INTO `state` VALUES (462,30,'Department','Potosí','BO-P','');
INSERT INTO `state` VALUES (463,30,'Department','Santa Cruz','BO-S','');
INSERT INTO `state` VALUES (464,30,'Department','Tarija','BO-T','');
INSERT INTO `state` VALUES (465,35,'State','Acre','BR-AC','');
INSERT INTO `state` VALUES (466,35,'State','Alagoas','BR-AL','');
INSERT INTO `state` VALUES (467,35,'State','Amazonas','BR-AM','');
INSERT INTO `state` VALUES (468,35,'State','Amapá','BR-AP','');
INSERT INTO `state` VALUES (469,35,'State','Bahia','BR-BA','');
INSERT INTO `state` VALUES (470,35,'State','Ceará','BR-CE','');
INSERT INTO `state` VALUES (471,35,'State','Espírito Santo','BR-ES','');
INSERT INTO `state` VALUES (472,35,'State','Fernando de Noronha','BR-FN','');
INSERT INTO `state` VALUES (473,35,'State','Goiás','BR-GO','');
INSERT INTO `state` VALUES (474,35,'State','Maranhão','BR-MA','');
INSERT INTO `state` VALUES (475,35,'State','Minas Gerais','BR-MG','');
INSERT INTO `state` VALUES (476,35,'State','Mato Grosso do Sul','BR-MS','');
INSERT INTO `state` VALUES (477,35,'State','Mato Grosso','BR-MT','');
INSERT INTO `state` VALUES (478,35,'State','Pará','BR-PA','');
INSERT INTO `state` VALUES (479,35,'State','Paraíba','BR-PB','');
INSERT INTO `state` VALUES (480,35,'State','Pernambuco','BR-PE','');
INSERT INTO `state` VALUES (481,35,'State','Piauí','BR-PI','');
INSERT INTO `state` VALUES (482,35,'State','Paraná','BR-PR','');
INSERT INTO `state` VALUES (483,35,'State','Rio de Janeiro','BR-RJ','');
INSERT INTO `state` VALUES (484,35,'State','Rio Grande do Norte','BR-RN','');
INSERT INTO `state` VALUES (485,35,'State','Rondônia','BR-RO','');
INSERT INTO `state` VALUES (486,35,'State','Roraima','BR-RR','');
INSERT INTO `state` VALUES (487,35,'State','Rio Grande do Sul','BR-RS','');
INSERT INTO `state` VALUES (488,35,'State','Santa Catarina','BR-SC','');
INSERT INTO `state` VALUES (489,35,'State','Sergipe','BR-SE','');
INSERT INTO `state` VALUES (490,35,'State','Sâo Paulo','BR-SP','');
INSERT INTO `state` VALUES (491,35,'State','Tocantins','BR-TO','');
INSERT INTO `state` VALUES (492,35,'Federal District','Distrito Federal','BR-DF','');
INSERT INTO `state` VALUES (493,20,'District','Acklins and Crooked Islands','BS-AC','');
INSERT INTO `state` VALUES (494,20,'District','Bimini','BS-BI','');
INSERT INTO `state` VALUES (495,20,'District','Cat Island','BS-CI','');
INSERT INTO `state` VALUES (496,20,'District','Exuma','BS-EX','');
INSERT INTO `state` VALUES (497,20,'District','Freeport','BS-FP','');
INSERT INTO `state` VALUES (498,20,'District','Fresh Creek','BS-FC','');
INSERT INTO `state` VALUES (499,20,'District','Governor\'s Harbour','BS-GH','');
INSERT INTO `state` VALUES (500,20,'District','Green Turtle Cay','BS-GT','');
INSERT INTO `state` VALUES (501,20,'District','Harbour Island','BS-HI','');
INSERT INTO `state` VALUES (502,20,'District','High Rock','BS-HR','');
INSERT INTO `state` VALUES (503,20,'District','Inagua','BS-IN','');
INSERT INTO `state` VALUES (504,20,'District','Kemps Bay','BS-KB','');
INSERT INTO `state` VALUES (505,20,'District','Long Island','BS-LI','');
INSERT INTO `state` VALUES (506,20,'District','Marsh Harbour','BS-MH','');
INSERT INTO `state` VALUES (507,20,'District','Mayaguana','BS-MG','');
INSERT INTO `state` VALUES (508,20,'District','New Providence','BS-NP','');
INSERT INTO `state` VALUES (509,20,'District','Nicholls Town and Berry Islands','BS-NB','');
INSERT INTO `state` VALUES (510,20,'District','Ragged Island','BS-RI','');
INSERT INTO `state` VALUES (511,20,'District','Rock Sound','BS-RS','');
INSERT INTO `state` VALUES (512,20,'District','Sandy Point','BS-SP','');
INSERT INTO `state` VALUES (513,20,'District','San Salvador and Rum Cay','BS-SR','');
INSERT INTO `state` VALUES (514,29,'District','Bumthang','BT-33','');
INSERT INTO `state` VALUES (515,29,'District','Chhukha','BT-12','');
INSERT INTO `state` VALUES (516,29,'District','Dagana','BT-22','');
INSERT INTO `state` VALUES (517,29,'District','Gasa','BT-GA','');
INSERT INTO `state` VALUES (518,29,'District','Ha','BT-13','');
INSERT INTO `state` VALUES (519,29,'District','Lhuentse','BT-44','');
INSERT INTO `state` VALUES (520,29,'District','Monggar','BT-42','');
INSERT INTO `state` VALUES (521,29,'District','Paro','BT-11','');
INSERT INTO `state` VALUES (522,29,'District','Pemagatshel','BT-43','');
INSERT INTO `state` VALUES (523,29,'District','Punakha','BT-23','');
INSERT INTO `state` VALUES (524,29,'District','Samdrup Jongkha','BT-45','');
INSERT INTO `state` VALUES (525,29,'District','Samtee','BT-14','');
INSERT INTO `state` VALUES (526,29,'District','Sarpang','BT-31','');
INSERT INTO `state` VALUES (527,29,'District','Thimphu','BT-15','');
INSERT INTO `state` VALUES (528,29,'District','Trashigang','BT-41','');
INSERT INTO `state` VALUES (529,29,'District','Trashi Yangtse','BT-TY','');
INSERT INTO `state` VALUES (530,29,'District','Trongsa','BT-32','');
INSERT INTO `state` VALUES (531,29,'District','Tsirang','BT-21','');
INSERT INTO `state` VALUES (532,29,'District','Wangdue Phodrang','BT-24','');
INSERT INTO `state` VALUES (533,29,'District','Zhemgang','BT-34','');
INSERT INTO `state` VALUES (534,33,'District','Central','BW-CE','');
INSERT INTO `state` VALUES (535,33,'District','Ghanzi','BW-GH','');
INSERT INTO `state` VALUES (536,33,'District','Kgalagadi','BW-KG','');
INSERT INTO `state` VALUES (537,33,'District','Kgatleng','BW-KL','');
INSERT INTO `state` VALUES (538,33,'District','Kweneng','BW-KW','');
INSERT INTO `state` VALUES (539,33,'District','Ngamiland','BW-NG','');
INSERT INTO `state` VALUES (540,33,'District','North-East','BW-NE','');
INSERT INTO `state` VALUES (541,33,'District','North-West (Botswana)','BW-NW','');
INSERT INTO `state` VALUES (542,33,'District','South-East','BW-SE','');
INSERT INTO `state` VALUES (543,33,'District','Southern (Botswana)','BW-SO','');
INSERT INTO `state` VALUES (544,24,'Oblast','Brèsckaja voblasc\'','BY-BR','');
INSERT INTO `state` VALUES (545,24,'Oblast','Homel\'skaja voblasc\'','BY-HO','');
INSERT INTO `state` VALUES (546,24,'Oblast','Hrodzenskaja voblasc\'','BY-HR','');
INSERT INTO `state` VALUES (547,24,'Oblast','Mahilëuskaja voblasc\'','BY-MA','');
INSERT INTO `state` VALUES (548,24,'Oblast','Minskaja voblasc\'','BY-MI','');
INSERT INTO `state` VALUES (549,24,'Oblast','Vicebskaja voblasc\'','BY-VI','');
INSERT INTO `state` VALUES (550,26,'District','Belize','BZ-BZ','');
INSERT INTO `state` VALUES (551,26,'District','Cayo','BZ-CY','');
INSERT INTO `state` VALUES (552,26,'District','Corozal','BZ-CZL','');
INSERT INTO `state` VALUES (553,26,'District','Orange Walk','BZ-OW','');
INSERT INTO `state` VALUES (554,26,'District','Stann Creek','BZ-SC','');
INSERT INTO `state` VALUES (555,26,'District','Toledo','BZ-TOL','');
INSERT INTO `state` VALUES (556,3,'Province','Alberta','CA-AB','');
INSERT INTO `state` VALUES (557,3,'Province','British Columbia','CA-BC','');
INSERT INTO `state` VALUES (558,3,'Province','Manitoba','CA-MB','');
INSERT INTO `state` VALUES (559,3,'Province','New Brunswick','CA-NB','');
INSERT INTO `state` VALUES (560,3,'Province','Newfoundland and Labrador','CA-NL','');
INSERT INTO `state` VALUES (561,3,'Province','Nova Scotia','CA-NS','');
INSERT INTO `state` VALUES (562,3,'Province','Ontario','CA-ON','');
INSERT INTO `state` VALUES (563,3,'Province','Prince Edward Island','CA-PE','');
INSERT INTO `state` VALUES (564,3,'Province','Quebec','CA-QC','');
INSERT INTO `state` VALUES (565,3,'Province','Saskatchewan','CA-SK','');
INSERT INTO `state` VALUES (566,3,'Territory','Northwest Territories','CA-NT','');
INSERT INTO `state` VALUES (567,3,'Territory','Nunavut','CA-NU','');
INSERT INTO `state` VALUES (568,3,'Territory','Yukon Territory','CA-YT','');
INSERT INTO `state` VALUES (569,55,'City','Kinshasa','CD-KN','');
INSERT INTO `state` VALUES (570,55,'Province','Bandundu','CD-BN','');
INSERT INTO `state` VALUES (571,55,'Province','Bas-Congo','CD-BC','');
INSERT INTO `state` VALUES (572,55,'Province','Équateur','CD-EQ','');
INSERT INTO `state` VALUES (573,55,'Province','Haut-Congo','CD-HC','');
INSERT INTO `state` VALUES (574,55,'Province','Kasai-Occidental','CD-KW','');
INSERT INTO `state` VALUES (575,55,'Province','Kasai-Oriental','CD-KE','');
INSERT INTO `state` VALUES (576,55,'Province','Katanga','CD-KA','');
INSERT INTO `state` VALUES (577,55,'Province','Maniema','CD-MA','');
INSERT INTO `state` VALUES (578,55,'Province','Nord-Kivu','CD-NK','');
INSERT INTO `state` VALUES (579,55,'Province','Orientale','CD-OR','');
INSERT INTO `state` VALUES (580,55,'Province','Sud-Kivu','CD-SK','');
INSERT INTO `state` VALUES (581,46,'Prefecture','Bamingui-Bangoran','CF-BB','');
INSERT INTO `state` VALUES (582,46,'Prefecture','Basse-Kotto','CF-BK','');
INSERT INTO `state` VALUES (583,46,'Prefecture','Haute-Kotto','CF-HK','');
INSERT INTO `state` VALUES (584,46,'Prefecture','Haut-Mbomou','CF-HM','');
INSERT INTO `state` VALUES (585,46,'Prefecture','Kémo','CF-KG','');
INSERT INTO `state` VALUES (586,46,'Prefecture','Lobaye','CF-LB','');
INSERT INTO `state` VALUES (587,46,'Prefecture','Mambéré-Kadéï','CF-HS','');
INSERT INTO `state` VALUES (588,46,'Prefecture','Mbomou','CF-MB','');
INSERT INTO `state` VALUES (589,46,'Prefecture','Nana-Mambéré','CF-NM','');
INSERT INTO `state` VALUES (590,46,'Prefecture','Ombella-M\'poko','CF-MP','');
INSERT INTO `state` VALUES (591,46,'Prefecture','Ouaka','CF-UK','');
INSERT INTO `state` VALUES (592,46,'Prefecture','Ouham','CF-AC','');
INSERT INTO `state` VALUES (593,46,'Prefecture','Ouham-Pendé','CF-OP','');
INSERT INTO `state` VALUES (594,46,'Prefecture','Vakaga','CF-VR','');
INSERT INTO `state` VALUES (595,46,'Economic Prefecture','Nana-Grébizi','CF-KB','');
INSERT INTO `state` VALUES (596,46,'Economic Prefecture','Sangha-Mbaéré','CF-SE','');
INSERT INTO `state` VALUES (597,46,'Autonomous Commune','Bangui','CF-BGF','');
INSERT INTO `state` VALUES (598,54,'Region','Bouenza','CG-11','');
INSERT INTO `state` VALUES (599,54,'Region','Cuvette','CG-8','');
INSERT INTO `state` VALUES (600,54,'Region','Cuvette-Ouest','CG-15','');
INSERT INTO `state` VALUES (601,54,'Region','Kouilou','CG-5','');
INSERT INTO `state` VALUES (602,54,'Region','Lékoumou','CG-2','');
INSERT INTO `state` VALUES (603,54,'Region','Likouala','CG-7','');
INSERT INTO `state` VALUES (604,54,'Region','Niari','CG-9','');
INSERT INTO `state` VALUES (605,54,'Region','Plateaux','CG-14','');
INSERT INTO `state` VALUES (606,54,'Region','Pool','CG-12','');
INSERT INTO `state` VALUES (607,54,'Region','Sangha','CG-13','');
INSERT INTO `state` VALUES (608,54,'Capital District','Brazzaville','CG-BZV','');
INSERT INTO `state` VALUES (609,218,'Canton','Aargau','CH-AG','');
INSERT INTO `state` VALUES (610,218,'Canton','Appenzell Innerrhoden','CH-AI','');
INSERT INTO `state` VALUES (611,218,'Canton','Appenzell Ausserrhoden','CH-AR','');
INSERT INTO `state` VALUES (612,218,'Canton','Bern','CH-BE','');
INSERT INTO `state` VALUES (613,218,'Canton','Basel-Landschaft','CH-BL','');
INSERT INTO `state` VALUES (614,218,'Canton','Basel-Stadt','CH-BS','');
INSERT INTO `state` VALUES (615,218,'Canton','Fribourg','CH-FR','');
INSERT INTO `state` VALUES (616,218,'Canton','Genève','CH-GE','');
INSERT INTO `state` VALUES (617,218,'Canton','Glarus','CH-GL','');
INSERT INTO `state` VALUES (618,218,'Canton','Graubünden','CH-GR','');
INSERT INTO `state` VALUES (619,218,'Canton','Jura','CH-JU','');
INSERT INTO `state` VALUES (620,218,'Canton','Luzern','CH-LU','');
INSERT INTO `state` VALUES (621,218,'Canton','Neuchâtel','CH-NE','');
INSERT INTO `state` VALUES (622,218,'Canton','Nidwalden','CH-NW','');
INSERT INTO `state` VALUES (623,218,'Canton','Obwalden','CH-OW','');
INSERT INTO `state` VALUES (624,218,'Canton','Sankt Gallen','CH-SG','');
INSERT INTO `state` VALUES (625,218,'Canton','Schaffhausen','CH-SH','');
INSERT INTO `state` VALUES (626,218,'Canton','Solothurn','CH-SO','');
INSERT INTO `state` VALUES (627,218,'Canton','Schwyz','CH-SZ','');
INSERT INTO `state` VALUES (628,218,'Canton','Thurgau','CH-TG','');
INSERT INTO `state` VALUES (629,218,'Canton','Ticino','CH-TI','');
INSERT INTO `state` VALUES (630,218,'Canton','Uri','CH-UR','');
INSERT INTO `state` VALUES (631,218,'Canton','Vaud','CH-VD','');
INSERT INTO `state` VALUES (632,218,'Canton','Valais','CH-VS','');
INSERT INTO `state` VALUES (633,218,'Canton','Zug','CH-ZG','');
INSERT INTO `state` VALUES (634,218,'Canton','Zürich','CH-ZH','');
INSERT INTO `state` VALUES (635,58,'Region','18 Montagnes (Région des)','CI-06','');
INSERT INTO `state` VALUES (636,58,'Region','Agnébi (Région de l\')','CI-16','');
INSERT INTO `state` VALUES (637,58,'Region','Bafing (Région du)','CI-17','');
INSERT INTO `state` VALUES (638,58,'Region','Bas-Sassandra (Région du)','CI-09','');
INSERT INTO `state` VALUES (639,58,'Region','Denguélé (Région du)','CI-10','');
INSERT INTO `state` VALUES (640,58,'Region','Fromager (Région du)','CI-18','');
INSERT INTO `state` VALUES (641,58,'Region','Haut-Sassandra (Région du)','CI-02','');
INSERT INTO `state` VALUES (642,58,'Region','Lacs (Région des)','CI-07','');
INSERT INTO `state` VALUES (643,58,'Region','Lagunes (Région des)','CI-01','');
INSERT INTO `state` VALUES (644,58,'Region','Marahoué (Région de la)','CI-12','');
INSERT INTO `state` VALUES (645,58,'Region','Moyen-Cavally (Région du)','CI-19','');
INSERT INTO `state` VALUES (646,58,'Region','Moyen-Comoé (Région du)','CI-05','');
INSERT INTO `state` VALUES (647,58,'Region','Nzi-Comoé (Région)','CI-11','');
INSERT INTO `state` VALUES (648,58,'Region','Savanes (Région des)','CI-03','');
INSERT INTO `state` VALUES (649,58,'Region','Sud-Bandama (Région du)','CI-15','');
INSERT INTO `state` VALUES (650,58,'Region','Sud-Comoé (Région du)','CI-13','');
INSERT INTO `state` VALUES (651,58,'Region','Vallée du Bandama (Région de la)','CI-04','');
INSERT INTO `state` VALUES (652,58,'Region','Worodouqou (Région du)','CI-14','');
INSERT INTO `state` VALUES (653,58,'Region','Zanzan (Région du)','CI-08','');
INSERT INTO `state` VALUES (654,48,'Region','Aisén del General Carlos Ibáñez del Campo','CL-AI','');
INSERT INTO `state` VALUES (655,48,'Region','Antofagasta','CL-AN','');
INSERT INTO `state` VALUES (656,48,'Region','Araucanía','CL-AR','');
INSERT INTO `state` VALUES (657,48,'Region','Atacama','CL-AT','');
INSERT INTO `state` VALUES (658,48,'Region','Bío-Bío','CL-BI','');
INSERT INTO `state` VALUES (659,48,'Region','Coquimbo','CL-CO','');
INSERT INTO `state` VALUES (660,48,'Region','Libertador General Bernardo O\'Higgins','CL-LI','');
INSERT INTO `state` VALUES (661,48,'Region','Los Lagos','CL-LL','');
INSERT INTO `state` VALUES (662,48,'Region','Magallanes y Antártica Chilena','CL-MA','');
INSERT INTO `state` VALUES (663,48,'Region','Maule','CL-ML','');
INSERT INTO `state` VALUES (664,48,'Region','Región Metropolitana de Santiago','CL-RM','');
INSERT INTO `state` VALUES (665,48,'Region','Tarapacá','CL-TA','');
INSERT INTO `state` VALUES (666,48,'Region','Valparaíso','CL-VS','');
INSERT INTO `state` VALUES (667,42,'Province','Adamaoua','CM-AD','');
INSERT INTO `state` VALUES (668,42,'Province','Centre','CM-CE','');
INSERT INTO `state` VALUES (669,42,'Province','East','CM-ES','');
INSERT INTO `state` VALUES (670,42,'Province','Far North','CM-EN','');
INSERT INTO `state` VALUES (671,42,'Province','Littoral','CM-LT','');
INSERT INTO `state` VALUES (672,42,'Province','North','CM-NO','');
INSERT INTO `state` VALUES (673,42,'Province','North-West (Cameroon)','CM-NW','');
INSERT INTO `state` VALUES (674,42,'Province','South','CM-SU','');
INSERT INTO `state` VALUES (675,42,'Province','South-West','CM-SW','');
INSERT INTO `state` VALUES (676,42,'Province','West','CM-OU','');
INSERT INTO `state` VALUES (677,49,'Municipality','Beijing','CN-11','');
INSERT INTO `state` VALUES (678,49,'Municipality','Chongqing','CN-50','');
INSERT INTO `state` VALUES (679,49,'Municipality','Shanghai','CN-31','');
INSERT INTO `state` VALUES (680,49,'Municipality','Tianjin','CN-12','');
INSERT INTO `state` VALUES (681,49,'Province','Anhui','CN-34','');
INSERT INTO `state` VALUES (682,49,'Province','Fujian','CN-35','');
INSERT INTO `state` VALUES (683,49,'Province','Gansu','CN-62','');
INSERT INTO `state` VALUES (684,49,'Province','Guangdong','CN-44','');
INSERT INTO `state` VALUES (685,49,'Province','Guizhou','CN-52','');
INSERT INTO `state` VALUES (686,49,'Province','Hainan','CN-46','');
INSERT INTO `state` VALUES (687,49,'Province','Hebei','CN-13','');
INSERT INTO `state` VALUES (688,49,'Province','Heilongjiang','CN-23','');
INSERT INTO `state` VALUES (689,49,'Province','Henan','CN-41','');
INSERT INTO `state` VALUES (690,49,'Province','Hubei','CN-42','');
INSERT INTO `state` VALUES (691,49,'Province','Hunan','CN-43','');
INSERT INTO `state` VALUES (692,49,'Province','Jiangsu','CN-32','');
INSERT INTO `state` VALUES (693,49,'Province','Jiangxi','CN-36','');
INSERT INTO `state` VALUES (694,49,'Province','Jilin','CN-22','');
INSERT INTO `state` VALUES (695,49,'Province','Liaoning','CN-21','');
INSERT INTO `state` VALUES (696,49,'Province','Qinghai','CN-63','');
INSERT INTO `state` VALUES (697,49,'Province','Shaanxi','CN-61','');
INSERT INTO `state` VALUES (698,49,'Province','Shandong','CN-37','');
INSERT INTO `state` VALUES (699,49,'Province','Shanxi','CN-14','');
INSERT INTO `state` VALUES (700,49,'Province','Sichuan','CN-51','');
INSERT INTO `state` VALUES (701,49,'Province','Taiwan','CN-71','');
INSERT INTO `state` VALUES (702,49,'Province','Yunnan','CN-53','');
INSERT INTO `state` VALUES (703,49,'Province','Zhejiang','CN-33','');
INSERT INTO `state` VALUES (704,49,'Autonomous region','Guangxi','CN-45','');
INSERT INTO `state` VALUES (705,49,'Autonomous region','Nei Mongol','CN-15','');
INSERT INTO `state` VALUES (706,49,'Autonomous region','Ningxia','CN-64','');
INSERT INTO `state` VALUES (707,49,'Autonomous region','Xinjiang','CN-65','');
INSERT INTO `state` VALUES (708,49,'Autonomous region','Xizang','CN-54','');
INSERT INTO `state` VALUES (709,49,'Special administrative region','Xianggang (Hong-Kong)','CN-91','');
INSERT INTO `state` VALUES (710,49,'Special administrative region','Aomen (Macau)','CN-92','');
INSERT INTO `state` VALUES (711,52,'Capital district','Distrito Capital de Bogotá','CO-DC','');
INSERT INTO `state` VALUES (712,52,'Department','Amazonas','CO-AMA','');
INSERT INTO `state` VALUES (713,52,'Department','Antioquia','CO-ANT','');
INSERT INTO `state` VALUES (714,52,'Department','Arauca','CO-ARA','');
INSERT INTO `state` VALUES (715,52,'Department','Atlántico','CO-ATL','');
INSERT INTO `state` VALUES (716,52,'Department','Bolívar','CO-BOL','');
INSERT INTO `state` VALUES (717,52,'Department','Boyacá','CO-BOY','');
INSERT INTO `state` VALUES (718,52,'Department','Caldas','CO-CAL','');
INSERT INTO `state` VALUES (719,52,'Department','Caquetá','CO-CAQ','');
INSERT INTO `state` VALUES (720,52,'Department','Casanare','CO-CAS','');
INSERT INTO `state` VALUES (721,52,'Department','Cauca','CO-CAU','');
INSERT INTO `state` VALUES (722,52,'Department','Cesar','CO-CES','');
INSERT INTO `state` VALUES (723,52,'Department','Chocó','CO-CHO','');
INSERT INTO `state` VALUES (724,52,'Department','Córdoba','CO-COR','');
INSERT INTO `state` VALUES (725,52,'Department','Cundinamarca','CO-CUN','');
INSERT INTO `state` VALUES (726,52,'Department','Guainía','CO-GUA','');
INSERT INTO `state` VALUES (727,52,'Department','Guaviare','CO-GUV','');
INSERT INTO `state` VALUES (728,52,'Department','Huila','CO-HUI','');
INSERT INTO `state` VALUES (729,52,'Department','La Guajira','CO-LAG','');
INSERT INTO `state` VALUES (730,52,'Department','Magdalena','CO-MAG','');
INSERT INTO `state` VALUES (731,52,'Department','Meta','CO-MET','');
INSERT INTO `state` VALUES (732,52,'Department','Nariño','CO-NAR','');
INSERT INTO `state` VALUES (733,52,'Department','Norte de Santander','CO-NSA','');
INSERT INTO `state` VALUES (734,52,'Department','Putumayo','CO-PUT','');
INSERT INTO `state` VALUES (735,52,'Department','Quindío','CO-QUI','');
INSERT INTO `state` VALUES (736,52,'Department','Risaralda','CO-RIS','');
INSERT INTO `state` VALUES (737,52,'Department','San Andrés, Providencia y Santa Catalina','CO-SAP','');
INSERT INTO `state` VALUES (738,52,'Department','Santander','CO-SAN','');
INSERT INTO `state` VALUES (739,52,'Department','Sucre','CO-SUC','');
INSERT INTO `state` VALUES (740,52,'Department','Tolima','CO-TOL','');
INSERT INTO `state` VALUES (741,52,'Department','Valle del Cauca','CO-VAC','');
INSERT INTO `state` VALUES (742,52,'Department','Vaupés','CO-VAU','');
INSERT INTO `state` VALUES (743,52,'Department','Vichada','CO-VID','');
INSERT INTO `state` VALUES (744,57,'Province','Alajuela','CR-A','');
INSERT INTO `state` VALUES (745,57,'Province','Cartago','CR-C','');
INSERT INTO `state` VALUES (746,57,'Province','Guanacaste','CR-G','');
INSERT INTO `state` VALUES (747,57,'Province','Heredia','CR-H','');
INSERT INTO `state` VALUES (748,57,'Province','Limón','CR-L','');
INSERT INTO `state` VALUES (749,57,'Province','Puntarenas','CR-P','');
INSERT INTO `state` VALUES (750,57,'Province','San José','CR-SJ','');
INSERT INTO `state` VALUES (751,60,'Province','Camagüey','CU-09','');
INSERT INTO `state` VALUES (752,60,'Province','Ciego de Ávila','CU-08','');
INSERT INTO `state` VALUES (753,60,'Province','Cienfuegos','CU-06','');
INSERT INTO `state` VALUES (754,60,'Province','Ciudad de La Habana','CU-03','');
INSERT INTO `state` VALUES (755,60,'Province','Granma','CU-12','');
INSERT INTO `state` VALUES (756,60,'Province','Guantánamo','CU-14','');
INSERT INTO `state` VALUES (757,60,'Province','Holguín','CU-11','');
INSERT INTO `state` VALUES (758,60,'Province','La Habana','CU-02','');
INSERT INTO `state` VALUES (759,60,'Province','Las Tunas','CU-10','');
INSERT INTO `state` VALUES (760,60,'Province','Matanzas','CU-04','');
INSERT INTO `state` VALUES (761,60,'Province','Pinar del Rio','CU-01','');
INSERT INTO `state` VALUES (762,60,'Province','Sancti Spíritus','CU-07','');
INSERT INTO `state` VALUES (763,60,'Province','Santiago de Cuba','CU-13','');
INSERT INTO `state` VALUES (764,60,'Province','Villa Clara','CU-05','');
INSERT INTO `state` VALUES (765,60,'Special municipality','Isla de la Juventud','CU-99','');
INSERT INTO `state` VALUES (766,44,'District','Ilhas de Barlavento','CV B','');
INSERT INTO `state` VALUES (767,44,'District','Ilhas de Sotavento','CV S','');
INSERT INTO `state` VALUES (768,44,'Municipality','Boa Vista','CV-BV','');
INSERT INTO `state` VALUES (769,44,'Municipality','Brava','CV-BR','');
INSERT INTO `state` VALUES (770,44,'Municipality','Calheta de São Miguel','CV-CS','');
INSERT INTO `state` VALUES (771,44,'Municipality','Maio','CV-MA','');
INSERT INTO `state` VALUES (772,44,'Municipality','Mosteiros','CV-MO','');
INSERT INTO `state` VALUES (773,44,'Municipality','Paúl','CV-PA','');
INSERT INTO `state` VALUES (774,44,'Municipality','Porto Novo','CV-PN','');
INSERT INTO `state` VALUES (775,44,'Municipality','Praia','CV-PR','');
INSERT INTO `state` VALUES (776,44,'Municipality','Ribeira Grande','CV-RG','');
INSERT INTO `state` VALUES (777,44,'Municipality','Sal','CV-SL','');
INSERT INTO `state` VALUES (778,44,'Municipality','Santa Catarina','CV-CA','');
INSERT INTO `state` VALUES (779,44,'Municipality','Santa Cruz','CV-CR','');
INSERT INTO `state` VALUES (780,44,'Municipality','São Domingos','CV-SD','');
INSERT INTO `state` VALUES (781,44,'Municipality','São Filipe','CV-SF','');
INSERT INTO `state` VALUES (782,44,'Municipality','São Nicolau','CV-SN','');
INSERT INTO `state` VALUES (783,44,'Municipality','São Vicente','CV-SV','');
INSERT INTO `state` VALUES (784,44,'Municipality','Tarrafal','CV-TA','');
INSERT INTO `state` VALUES (785,62,'District','Ammóchostos','CY-04','');
INSERT INTO `state` VALUES (786,62,'District','Kerýneia','CY-06','');
INSERT INTO `state` VALUES (787,62,'District','Lárnaka','CY-03','');
INSERT INTO `state` VALUES (788,62,'District','Lefkosía','CY-01','');
INSERT INTO `state` VALUES (789,62,'District','Lemesós','CY-02','');
INSERT INTO `state` VALUES (790,62,'District','Páfos','CY-05','');
INSERT INTO `state` VALUES (791,63,'Region','Jiho?eský kraj','CZ JC','');
INSERT INTO `state` VALUES (792,63,'Region','Jihomoravský kraj','CZ JM','');
INSERT INTO `state` VALUES (793,63,'Region','Karlovarský kraj','CZ KA','');
INSERT INTO `state` VALUES (794,63,'Region','Královéhradecký kraj','CZ KR','');
INSERT INTO `state` VALUES (795,63,'Region','Liberecký kraj','CZ LI','');
INSERT INTO `state` VALUES (796,63,'Region','Moravskoslezský kraj','CZ MO','');
INSERT INTO `state` VALUES (797,63,'Region','Olomoucký kraj','CZ OL','');
INSERT INTO `state` VALUES (798,63,'Region','Pardubický kraj','CZ PA','');
INSERT INTO `state` VALUES (799,63,'Region','Plze?ský kraj','CZ PL','');
INSERT INTO `state` VALUES (800,63,'Region','Praha, hlavní m?sto','CZ PR','');
INSERT INTO `state` VALUES (801,63,'Region','St?edo?eský kraj','CZ ST','');
INSERT INTO `state` VALUES (802,63,'Region','Ústecký kraj','CZ US','');
INSERT INTO `state` VALUES (803,63,'Region','Vyso?ina','CZ VY','');
INSERT INTO `state` VALUES (804,63,'Region','Zlínský kraj','CZ ZL','');
INSERT INTO `state` VALUES (805,63,'district','Benešov','CZ-201','');
INSERT INTO `state` VALUES (806,63,'district','Beroun','CZ-202','');
INSERT INTO `state` VALUES (807,63,'district','Kladno','CZ-203','');
INSERT INTO `state` VALUES (808,63,'district','Kolín','CZ-204','');
INSERT INTO `state` VALUES (809,63,'district','Kutná Hora','CZ-205','');
INSERT INTO `state` VALUES (810,63,'district','M?lník','CZ-206','');
INSERT INTO `state` VALUES (811,63,'district','Mladá Boleslav','CZ-207','');
INSERT INTO `state` VALUES (812,63,'district','Nymburk','CZ-208','');
INSERT INTO `state` VALUES (813,63,'district','Praha-východ','CZ-209','');
INSERT INTO `state` VALUES (814,63,'district','Praha-západ','CZ-20A','');
INSERT INTO `state` VALUES (815,63,'district','P?íbram','CZ-20B','');
INSERT INTO `state` VALUES (816,63,'district','Rakovník','CZ-20C','');
INSERT INTO `state` VALUES (817,63,'district','?eské Bud?jovice','CZ-311','');
INSERT INTO `state` VALUES (818,63,'district','?eský Krumlov','CZ-312','');
INSERT INTO `state` VALUES (819,63,'district','Jind?ich?v Hradec','CZ-313','');
INSERT INTO `state` VALUES (820,63,'district','Písek','CZ-314','');
INSERT INTO `state` VALUES (821,63,'district','Prachatice','CZ-315','');
INSERT INTO `state` VALUES (822,63,'district','Strakonice','CZ-316','');
INSERT INTO `state` VALUES (823,63,'district','Tábor','CZ-317','');
INSERT INTO `state` VALUES (824,63,'district','Domažlice','CZ-321','');
INSERT INTO `state` VALUES (825,63,'district','Klatovy','CZ-322','');
INSERT INTO `state` VALUES (826,63,'district','Plze?-m?sto','CZ-323','');
INSERT INTO `state` VALUES (827,63,'district','Plze?-jih','CZ-324','');
INSERT INTO `state` VALUES (828,63,'district','Plze?-sever','CZ-325','');
INSERT INTO `state` VALUES (829,63,'district','Rokycany','CZ-326','');
INSERT INTO `state` VALUES (830,63,'district','Tachov','CZ-327','');
INSERT INTO `state` VALUES (831,63,'district','Cheb','CZ-411','');
INSERT INTO `state` VALUES (832,63,'district','Karlovy Vary','CZ-412','');
INSERT INTO `state` VALUES (833,63,'district','Sokolov','CZ-413','');
INSERT INTO `state` VALUES (834,63,'district','D??ín','CZ-421','');
INSERT INTO `state` VALUES (835,63,'district','Chomutov','CZ-422','');
INSERT INTO `state` VALUES (836,63,'district','Litom??ice','CZ-423','');
INSERT INTO `state` VALUES (837,63,'district','Louny','CZ-424','');
INSERT INTO `state` VALUES (838,63,'district','Most','CZ-425','');
INSERT INTO `state` VALUES (839,63,'district','Teplice','CZ-426','');
INSERT INTO `state` VALUES (840,63,'district','Ústí nad Labem','CZ-427','');
INSERT INTO `state` VALUES (841,63,'district','?eská Lípa','CZ-511','');
INSERT INTO `state` VALUES (842,63,'district','Jablonec nad Nisou','CZ-512','');
INSERT INTO `state` VALUES (843,63,'district','Liberec','CZ-513','');
INSERT INTO `state` VALUES (844,63,'district','Semily','CZ-514','');
INSERT INTO `state` VALUES (845,63,'district','Hradec Králové','CZ-521','');
INSERT INTO `state` VALUES (846,63,'district','Ji?ín','CZ-522','');
INSERT INTO `state` VALUES (847,63,'district','Náchod','CZ-523','');
INSERT INTO `state` VALUES (848,63,'district','Rychnov nad Kn?žnou','CZ-524','');
INSERT INTO `state` VALUES (849,63,'district','Trutnov','CZ-525','');
INSERT INTO `state` VALUES (850,63,'district','Chrudim','CZ-531','');
INSERT INTO `state` VALUES (851,63,'district','Pardubice','CZ-532','');
INSERT INTO `state` VALUES (852,63,'district','Svitavy','CZ-533','');
INSERT INTO `state` VALUES (853,63,'district','Ústí nad Orlicí','CZ-534','');
INSERT INTO `state` VALUES (854,63,'district','Blansko','CZ-621','');
INSERT INTO `state` VALUES (855,63,'district','Brno-m?sto','CZ-622','');
INSERT INTO `state` VALUES (856,63,'district','Brno-venkov','CZ-623','');
INSERT INTO `state` VALUES (857,63,'district','B?eclav','CZ-624','');
INSERT INTO `state` VALUES (858,63,'district','Hodonín','CZ-625','');
INSERT INTO `state` VALUES (859,63,'district','Vyškov','CZ-626','');
INSERT INTO `state` VALUES (860,63,'district','Znojmo','CZ-627','');
INSERT INTO `state` VALUES (861,63,'district','Jeseník','CZ-711','');
INSERT INTO `state` VALUES (862,63,'district','Olomouc','CZ-712','');
INSERT INTO `state` VALUES (863,63,'district','Prost?jov','CZ-713','');
INSERT INTO `state` VALUES (864,63,'district','P?erov','CZ-714','');
INSERT INTO `state` VALUES (865,63,'district','Šumperk','CZ-715','');
INSERT INTO `state` VALUES (866,63,'district','Krom??íž','CZ-721','');
INSERT INTO `state` VALUES (867,63,'district','Uherské Hradišt?','CZ-722','');
INSERT INTO `state` VALUES (868,63,'district','Vsetín','CZ-723','');
INSERT INTO `state` VALUES (869,63,'district','Zlín','CZ-724','');
INSERT INTO `state` VALUES (870,63,'district','Bruntál','CZ-801','');
INSERT INTO `state` VALUES (871,63,'district','Frýdek - Místek','CZ-802','');
INSERT INTO `state` VALUES (872,63,'district','Karviná','CZ-803','');
INSERT INTO `state` VALUES (873,63,'district','Nový Ji?ín','CZ-804','');
INSERT INTO `state` VALUES (874,63,'district','Opava','CZ-805','');
INSERT INTO `state` VALUES (875,63,'district','Ostrava - m?sto','CZ-806','');
INSERT INTO `state` VALUES (876,63,'district','Praha 1','CZ-101','');
INSERT INTO `state` VALUES (877,63,'district','Praha 2','CZ-102','');
INSERT INTO `state` VALUES (878,63,'district','Praha 3','CZ-103','');
INSERT INTO `state` VALUES (879,63,'district','Praha 4','CZ-104','');
INSERT INTO `state` VALUES (880,63,'district','Praha 5','CZ-105','');
INSERT INTO `state` VALUES (881,63,'district','Praha 6','CZ-106','');
INSERT INTO `state` VALUES (882,63,'district','Praha 7','CZ-107','');
INSERT INTO `state` VALUES (883,63,'district','Praha 8','CZ-108','');
INSERT INTO `state` VALUES (884,63,'district','Praha 9','CZ-109','');
INSERT INTO `state` VALUES (885,63,'district','Praha 10','CZ-10A','');
INSERT INTO `state` VALUES (886,63,'district','Praha 11','CZ-10B','');
INSERT INTO `state` VALUES (887,63,'district','Praha 12','CZ-10C','');
INSERT INTO `state` VALUES (888,63,'district','Praha 13','CZ-10D','');
INSERT INTO `state` VALUES (889,63,'district','Praha 14','CZ-10E','');
INSERT INTO `state` VALUES (890,63,'district','Praha 15','CZ-10F','');
INSERT INTO `state` VALUES (891,63,'district','Havlí?k?v Brod','CZ-611','');
INSERT INTO `state` VALUES (892,63,'district','Jihlava','CZ-612','');
INSERT INTO `state` VALUES (893,63,'district','Pelh?imov','CZ-613','');
INSERT INTO `state` VALUES (894,63,'district','T?ebí?','CZ-614','');
INSERT INTO `state` VALUES (895,63,'district','Žd’ár nad Sázavou','CZ-615','');
INSERT INTO `state` VALUES (896,86,'State','Baden-Württemberg','DE-BW','');
INSERT INTO `state` VALUES (897,86,'State','Bayern','DE-BY','');
INSERT INTO `state` VALUES (898,86,'State','Bremen','DE-HB','');
INSERT INTO `state` VALUES (899,86,'State','Hamburg','DE-HH','');
INSERT INTO `state` VALUES (900,86,'State','Hessen','DE-HE','');
INSERT INTO `state` VALUES (901,86,'State','Niedersachsen','DE-NI','');
INSERT INTO `state` VALUES (902,86,'State','Nordrhein-Westfalen','DE-NW','');
INSERT INTO `state` VALUES (903,86,'State','Rheinland-Pfalz','DE-RP','');
INSERT INTO `state` VALUES (904,86,'State','Saarland','DE-SL','');
INSERT INTO `state` VALUES (905,86,'State','Schleswig-Holstein','DE-SH','');
INSERT INTO `state` VALUES (906,86,'State','Berlin','DE-BE','');
INSERT INTO `state` VALUES (907,86,'State','Brandenburg','DE-BB','');
INSERT INTO `state` VALUES (908,86,'State','Mecklenburg-Vorpommern','DE-MV','');
INSERT INTO `state` VALUES (909,86,'State','Sachsen','DE-SN','');
INSERT INTO `state` VALUES (910,86,'State','Sachsen-Anhalt','DE-ST','');
INSERT INTO `state` VALUES (911,86,'State','Thüringen','DE-TH','');
INSERT INTO `state` VALUES (912,65,'Region','Ali Sabieh','DJ-AS','');
INSERT INTO `state` VALUES (913,65,'Region','Arta','DJ-AR','');
INSERT INTO `state` VALUES (914,65,'Region','Dikhil','DJ-DI','');
INSERT INTO `state` VALUES (915,65,'Region','Obock','DJ-OB','');
INSERT INTO `state` VALUES (916,65,'Region','Tadjourah','DJ-TA','');
INSERT INTO `state` VALUES (917,65,'City','Djibouti','DJ-DJ','');
INSERT INTO `state` VALUES (918,64,'County','Copenhagen','DK-015','');
INSERT INTO `state` VALUES (919,64,'County','Frederiksborg','DK-020','');
INSERT INTO `state` VALUES (920,64,'County','Roskilde','DK-025','');
INSERT INTO `state` VALUES (921,64,'County','Western Zealand','DK-030','');
INSERT INTO `state` VALUES (922,64,'County','Storstrøm','DK-035','');
INSERT INTO `state` VALUES (923,64,'County','Bornholm','DK-040','');
INSERT INTO `state` VALUES (924,64,'County','Funen','DK-042','');
INSERT INTO `state` VALUES (925,64,'County','Southern Jutland','DK-050','');
INSERT INTO `state` VALUES (926,64,'County','Ribe','DK-055','');
INSERT INTO `state` VALUES (927,64,'County','Vejle','DK-060','');
INSERT INTO `state` VALUES (928,64,'County','Ringkøbing','DK-065','');
INSERT INTO `state` VALUES (929,64,'County','Aarhus','DK-070','');
INSERT INTO `state` VALUES (930,64,'County','Viborg','DK-076','');
INSERT INTO `state` VALUES (931,64,'County','Northern Jutland','DK-080','');
INSERT INTO `state` VALUES (932,64,'Municipality','Frederikaberg municipality','DK-147','');
INSERT INTO `state` VALUES (933,64,'Municipality','Copenhagen municipality','DK-101','');
INSERT INTO `state` VALUES (934,66,'Parish','Saint Andrew','DM-02','');
INSERT INTO `state` VALUES (935,66,'Parish','Saint David','DM-03','');
INSERT INTO `state` VALUES (936,66,'Parish','Saint George','DM-04','');
INSERT INTO `state` VALUES (937,66,'Parish','Saint John','DM-05','');
INSERT INTO `state` VALUES (938,66,'Parish','Saint Joseph','DM-06','');
INSERT INTO `state` VALUES (939,66,'Parish','Saint Luke','DM-07','');
INSERT INTO `state` VALUES (940,66,'Parish','Saint Mark','DM-08','');
INSERT INTO `state` VALUES (941,66,'Parish','Saint Patrick','DM-09','');
INSERT INTO `state` VALUES (942,66,'Parish','Saint Paul','DM-10','');
INSERT INTO `state` VALUES (943,66,'Parish','Saint Peter','DM-01','');
INSERT INTO `state` VALUES (944,67,'District','Distrito Nacional (Santo Domingo)','DO-01','');
INSERT INTO `state` VALUES (945,67,'Province','Azua','DO-02','');
INSERT INTO `state` VALUES (946,67,'Province','Bahoruco','DO-03','');
INSERT INTO `state` VALUES (947,67,'Province','Barahona','DO-04','');
INSERT INTO `state` VALUES (948,67,'Province','Dajabón','DO-05','');
INSERT INTO `state` VALUES (949,67,'Province','Duarte','DO-06','');
INSERT INTO `state` VALUES (950,67,'Province','El Seybo [El Seibo]','DO-08','');
INSERT INTO `state` VALUES (951,67,'Province','Espaillat','DO-09','');
INSERT INTO `state` VALUES (952,67,'Province','Hato Mayor','DO-30','');
INSERT INTO `state` VALUES (953,67,'Province','Independencia','DO-10','');
INSERT INTO `state` VALUES (954,67,'Province','La Altagracia','DO-11','');
INSERT INTO `state` VALUES (955,67,'Province','La Estrelleta [Elías Piña]','DO-07','');
INSERT INTO `state` VALUES (956,67,'Province','La Romana','DO-12','');
INSERT INTO `state` VALUES (957,67,'Province','La Vega','DO-13','');
INSERT INTO `state` VALUES (958,67,'Province','María Trinidad Sánchez','DO-14','');
INSERT INTO `state` VALUES (959,67,'Province','Monseñor Nouel','DO-28','');
INSERT INTO `state` VALUES (960,67,'Province','Monte Cristi','DO-15','');
INSERT INTO `state` VALUES (961,67,'Province','Monte Plata','DO-29','');
INSERT INTO `state` VALUES (962,67,'Province','Pedernales','DO-16','');
INSERT INTO `state` VALUES (963,67,'Province','Peravia','DO-17','');
INSERT INTO `state` VALUES (964,67,'Province','Puerto Plata','DO-18','');
INSERT INTO `state` VALUES (965,67,'Province','Salcedo','DO-19','');
INSERT INTO `state` VALUES (966,67,'Province','Samaná','DO-20','');
INSERT INTO `state` VALUES (967,67,'Province','San Cristóbal','DO-21','');
INSERT INTO `state` VALUES (968,67,'Province','San Juan','DO-22','');
INSERT INTO `state` VALUES (969,67,'Province','San Pedro de Macorís','DO-23','');
INSERT INTO `state` VALUES (970,67,'Province','Sánchez Ramírez','DO-24','');
INSERT INTO `state` VALUES (971,67,'Province','Santiago','DO-25','');
INSERT INTO `state` VALUES (972,67,'Province','Santiago Rodríguez','DO-26','');
INSERT INTO `state` VALUES (973,67,'Province','Valverde','DO-27','');
INSERT INTO `state` VALUES (974,7,'Province','Adrar','DZ-01','');
INSERT INTO `state` VALUES (975,7,'Province','Aïn Defla','DZ-44','');
INSERT INTO `state` VALUES (976,7,'Province','Aïn Témouchent','DZ-46','');
INSERT INTO `state` VALUES (977,7,'Province','Alger','DZ-16','');
INSERT INTO `state` VALUES (978,7,'Province','Annaba','DZ-23','');
INSERT INTO `state` VALUES (979,7,'Province','Batna','DZ-05','');
INSERT INTO `state` VALUES (980,7,'Province','Béchar','DZ-08','');
INSERT INTO `state` VALUES (981,7,'Province','Béjaïa','DZ-06','');
INSERT INTO `state` VALUES (982,7,'Province','Biskra','DZ-07','');
INSERT INTO `state` VALUES (983,7,'Province','Blida','DZ-09','');
INSERT INTO `state` VALUES (984,7,'Province','Bordj Bou Arréridj','DZ-34','');
INSERT INTO `state` VALUES (985,7,'Province','Bouira','DZ-10','');
INSERT INTO `state` VALUES (986,7,'Province','Boumerdès','DZ-35','');
INSERT INTO `state` VALUES (987,7,'Province','Chlef','DZ-02','');
INSERT INTO `state` VALUES (988,7,'Province','Constantine','DZ-25','');
INSERT INTO `state` VALUES (989,7,'Province','Djelfa','DZ-17','');
INSERT INTO `state` VALUES (990,7,'Province','El Bayadh','DZ-32','');
INSERT INTO `state` VALUES (991,7,'Province','El Oued','DZ-39','');
INSERT INTO `state` VALUES (992,7,'Province','El Tarf','DZ-36','');
INSERT INTO `state` VALUES (993,7,'Province','Ghardaïa','DZ-47','');
INSERT INTO `state` VALUES (994,7,'Province','Guelma','DZ-24','');
INSERT INTO `state` VALUES (995,7,'Province','Illizi','DZ-33','');
INSERT INTO `state` VALUES (996,7,'Province','Jijel','DZ-18','');
INSERT INTO `state` VALUES (997,7,'Province','Khenchela','DZ-40','');
INSERT INTO `state` VALUES (998,7,'Province','Laghouat','DZ-03','');
INSERT INTO `state` VALUES (999,7,'Province','Mascara','DZ-29','');
INSERT INTO `state` VALUES (1000,7,'Province','Médéa','DZ-26','');
INSERT INTO `state` VALUES (1001,7,'Province','Mila','DZ-43','');
INSERT INTO `state` VALUES (1002,7,'Province','Mostaganem','DZ-27','');
INSERT INTO `state` VALUES (1003,7,'Province','Msila','DZ-28','');
INSERT INTO `state` VALUES (1004,7,'Province','Naama','DZ-45','');
INSERT INTO `state` VALUES (1005,7,'Province','Oran','DZ-31','');
INSERT INTO `state` VALUES (1006,7,'Province','Ouargla','DZ-30','');
INSERT INTO `state` VALUES (1007,7,'Province','Oum el Bouaghi','DZ-04','');
INSERT INTO `state` VALUES (1008,7,'Province','Relizane','DZ-48','');
INSERT INTO `state` VALUES (1009,7,'Province','Saïda','DZ-20','');
INSERT INTO `state` VALUES (1010,7,'Province','Sétif','DZ-19','');
INSERT INTO `state` VALUES (1011,7,'Province','Sidi Bel Abbès','DZ-22','');
INSERT INTO `state` VALUES (1012,7,'Province','Skikda','DZ-21','');
INSERT INTO `state` VALUES (1013,7,'Province','Souk Ahras','DZ-41','');
INSERT INTO `state` VALUES (1014,7,'Province','Tamanghasset','DZ-11','');
INSERT INTO `state` VALUES (1015,7,'Province','Tébessa','DZ-12','');
INSERT INTO `state` VALUES (1016,7,'Province','Tiaret','DZ-14','');
INSERT INTO `state` VALUES (1017,7,'Province','Tindouf','DZ-37','');
INSERT INTO `state` VALUES (1018,7,'Province','Tipaza','DZ-42','');
INSERT INTO `state` VALUES (1019,7,'Province','Tissemsilt','DZ-38','');
INSERT INTO `state` VALUES (1020,7,'Province','Tizi Ouzou','DZ-15','');
INSERT INTO `state` VALUES (1021,7,'Province','Tlemcen','DZ-13','');
INSERT INTO `state` VALUES (1022,68,'Province','Azuay','EC-A','');
INSERT INTO `state` VALUES (1023,68,'Province','Bolívar','EC-B','');
INSERT INTO `state` VALUES (1024,68,'Province','Cañar','EC-F','');
INSERT INTO `state` VALUES (1025,68,'Province','Carchi','EC-C','');
INSERT INTO `state` VALUES (1026,68,'Province','Cotopaxi','EC-X','');
INSERT INTO `state` VALUES (1027,68,'Province','Chimborazo','EC-H','');
INSERT INTO `state` VALUES (1028,68,'Province','El Oro','EC-O','');
INSERT INTO `state` VALUES (1029,68,'Province','Esmeraldas','EC-E','');
INSERT INTO `state` VALUES (1030,68,'Province','Galápagos','EC-W','');
INSERT INTO `state` VALUES (1031,68,'Province','Guayas','EC-G','');
INSERT INTO `state` VALUES (1032,68,'Province','Imbabura','EC-I','');
INSERT INTO `state` VALUES (1033,68,'Province','Loja','EC-L','');
INSERT INTO `state` VALUES (1034,68,'Province','Los Ríos','EC-R','');
INSERT INTO `state` VALUES (1035,68,'Province','Manabí','EC-M','');
INSERT INTO `state` VALUES (1036,68,'Province','Morona-Santiago','EC-S','');
INSERT INTO `state` VALUES (1037,68,'Province','Napo','EC-N','');
INSERT INTO `state` VALUES (1038,68,'Province','Orellana','EC-D','');
INSERT INTO `state` VALUES (1039,68,'Province','Pastaza','EC-Y','');
INSERT INTO `state` VALUES (1040,68,'Province','Pichincha','EC-P','');
INSERT INTO `state` VALUES (1041,68,'Province','Sucumbíos','EC-U','');
INSERT INTO `state` VALUES (1042,68,'Province','Tungurahua','EC-T','');
INSERT INTO `state` VALUES (1043,68,'Province','Zamora-Chinchipe','EC-Z','');
INSERT INTO `state` VALUES (1044,73,'County','Harjumaa','EE-37','');
INSERT INTO `state` VALUES (1045,73,'County','Hiiumaa','EE-39','');
INSERT INTO `state` VALUES (1046,73,'County','Ida-Virumaa','EE-44','');
INSERT INTO `state` VALUES (1047,73,'County','Jõgevamaa','EE-49','');
INSERT INTO `state` VALUES (1048,73,'County','Järvamaa','EE-51','');
INSERT INTO `state` VALUES (1049,73,'County','Läänemaa','EE-57','');
INSERT INTO `state` VALUES (1050,73,'County','Lääne-Virumaa','EE-59','');
INSERT INTO `state` VALUES (1051,73,'County','Põlvamaa','EE-65','');
INSERT INTO `state` VALUES (1052,73,'County','Pärnumaa','EE-67','');
INSERT INTO `state` VALUES (1053,73,'County','Raplamaa','EE-70','');
INSERT INTO `state` VALUES (1054,73,'County','Saaremaa','EE-74','');
INSERT INTO `state` VALUES (1055,73,'County','Tartumaa','EE-78','');
INSERT INTO `state` VALUES (1056,73,'County','Valgamaa','EE-82','');
INSERT INTO `state` VALUES (1057,73,'County','Viljandimaa','EE-84','');
INSERT INTO `state` VALUES (1058,73,'County','Võrumaa','EE-86','');
INSERT INTO `state` VALUES (1059,69,'Governorate','Ad Daqahl?yah','EG-DK','');
INSERT INTO `state` VALUES (1060,69,'Governorate','Al Bahr al Ahmar','EG-BA','');
INSERT INTO `state` VALUES (1061,69,'Governorate','Al Buhayrah','EG-BH','');
INSERT INTO `state` VALUES (1062,69,'Governorate','Al Fayy?m','EG-FYM','');
INSERT INTO `state` VALUES (1063,69,'Governorate','Al Gharb?yah','EG-GH','');
INSERT INTO `state` VALUES (1064,69,'Governorate','Al Iskandar?yah','EG-ALX','');
INSERT INTO `state` VALUES (1065,69,'Governorate','Al Ism?`?l?yah','EG-IS','');
INSERT INTO `state` VALUES (1066,69,'Governorate','Al J?zah','EG-GZ','');
INSERT INTO `state` VALUES (1067,69,'Governorate','Al Min?f?yah','EG-MNF','');
INSERT INTO `state` VALUES (1068,69,'Governorate','Al Miny?','EG-MN','');
INSERT INTO `state` VALUES (1069,69,'Governorate','Al Q?hirah','EG-C','');
INSERT INTO `state` VALUES (1070,69,'Governorate','Al Qaly?b?yah','EG-KB','');
INSERT INTO `state` VALUES (1071,69,'Governorate','Al W?d? al Jad?d','EG-WAD','');
INSERT INTO `state` VALUES (1072,69,'Governorate','Ash Sharq?yah','EG-SHR','');
INSERT INTO `state` VALUES (1073,69,'Governorate','As Suways','EG-SUZ','');
INSERT INTO `state` VALUES (1074,69,'Governorate','Asw?n','EG-ASN','');
INSERT INTO `state` VALUES (1075,69,'Governorate','Asy?t','EG-AST','');
INSERT INTO `state` VALUES (1076,69,'Governorate','Ban? Suwayf','EG-BNS','');
INSERT INTO `state` VALUES (1077,69,'Governorate','B?r Sa`?d','EG-PTS','');
INSERT INTO `state` VALUES (1078,69,'Governorate','Dumy?t','EG-DT','');
INSERT INTO `state` VALUES (1079,69,'Governorate','Jan?b S?n?\'','EG-JS','');
INSERT INTO `state` VALUES (1080,69,'Governorate','Kafr ash Shaykh','EG-KFS','');
INSERT INTO `state` VALUES (1081,69,'Governorate','Matr?h','EG-MT','');
INSERT INTO `state` VALUES (1082,69,'Governorate','Qin?','EG-KN','');
INSERT INTO `state` VALUES (1083,69,'Governorate','Shamal S?n?\'','EG-SIN','');
INSERT INTO `state` VALUES (1084,69,'Governorate','S?h?j','EG-SHG','');
INSERT INTO `state` VALUES (1085,72,'Province','Anseba','ER-AN','');
INSERT INTO `state` VALUES (1086,72,'Province','Debub','ER-DU','');
INSERT INTO `state` VALUES (1087,72,'Province','Debubawi Keyih Bahri [Debub-Keih-Bahri]','ER-DK','');
INSERT INTO `state` VALUES (1088,72,'Province','Gash-Barka','ER-GB','');
INSERT INTO `state` VALUES (1089,72,'Province','Maakel [Maekel]','ER-MA','');
INSERT INTO `state` VALUES (1090,72,'Province','Semenawi Keyih Bahri [Semien-Keih-Bahri]','ER-SK','');
INSERT INTO `state` VALUES (1091,211,'Autonomous communities','Andalucía','ES AN','');
INSERT INTO `state` VALUES (1092,211,'Autonomous communities','Aragón','ES AR','');
INSERT INTO `state` VALUES (1093,211,'Autonomous communities','Asturias, Principado de','ES O','');
INSERT INTO `state` VALUES (1094,211,'Autonomous communities','Canarias','ES CN','');
INSERT INTO `state` VALUES (1095,211,'Autonomous communities','Cantabria','ES S','');
INSERT INTO `state` VALUES (1096,211,'Autonomous communities','Castilla-La Mancha','ES CM','');
INSERT INTO `state` VALUES (1097,211,'Autonomous communities','Castilla y León','ES CL','');
INSERT INTO `state` VALUES (1098,211,'Autonomous communities','Cataluña','ES CT','');
INSERT INTO `state` VALUES (1099,211,'Autonomous communities','Extremadura','ES EX','');
INSERT INTO `state` VALUES (1100,211,'Autonomous communities','Galicia','ES GA','');
INSERT INTO `state` VALUES (1101,211,'Autonomous communities','Illes Balears','ES IB','');
INSERT INTO `state` VALUES (1102,211,'Autonomous communities','La Rioja','ES LO','');
INSERT INTO `state` VALUES (1103,211,'Autonomous communities','Madrid, Comunidad de','ES M','');
INSERT INTO `state` VALUES (1104,211,'Autonomous communities','Murcia, Región de','ES MU','');
INSERT INTO `state` VALUES (1105,211,'Autonomous communities','Navarra, Comunidad Foral de','ES NA','');
INSERT INTO `state` VALUES (1106,211,'Autonomous communities','País Vasco','ES PV','');
INSERT INTO `state` VALUES (1107,211,'Autonomous communities','Valenciana, Comunidad','ES VC','');
INSERT INTO `state` VALUES (1108,211,'Province','Álava','ES-VI','');
INSERT INTO `state` VALUES (1109,211,'Province','Albacete','ES-AB','');
INSERT INTO `state` VALUES (1110,211,'Province','Alicante','ES-A','');
INSERT INTO `state` VALUES (1111,211,'Province','Almería','ES-AL','');
INSERT INTO `state` VALUES (1112,211,'Province','Asturias','ES-O','');
INSERT INTO `state` VALUES (1113,211,'Province','Ávila','ES-AV','');
INSERT INTO `state` VALUES (1114,211,'Province','Badajoz','ES-BA','');
INSERT INTO `state` VALUES (1115,211,'Province','Baleares','ES-IB','');
INSERT INTO `state` VALUES (1116,211,'Province','Barcelona','ES-B','');
INSERT INTO `state` VALUES (1117,211,'Province','Burgos','ES-BU','');
INSERT INTO `state` VALUES (1118,211,'Province','Cáceres','ES-CC','');
INSERT INTO `state` VALUES (1119,211,'Province','Cádiz','ES-CA','');
INSERT INTO `state` VALUES (1120,211,'Province','Cantabria','ES-S','');
INSERT INTO `state` VALUES (1121,211,'Province','Castellón','ES-CS','');
INSERT INTO `state` VALUES (1122,211,'Province','Ciudad Real','ES-CR','');
INSERT INTO `state` VALUES (1123,211,'Province','Córdoba','ES-CO','');
INSERT INTO `state` VALUES (1124,211,'Province','Cuenca','ES-CU','');
INSERT INTO `state` VALUES (1125,211,'Province','Girona','ES-GI','');
INSERT INTO `state` VALUES (1126,211,'Province','Granada','ES-GR','');
INSERT INTO `state` VALUES (1127,211,'Province','Guadalajara','ES-GU','');
INSERT INTO `state` VALUES (1128,211,'Province','Guipúzcoa','ES-SS','');
INSERT INTO `state` VALUES (1129,211,'Province','Huelva','ES-H','');
INSERT INTO `state` VALUES (1130,211,'Province','Huesca','ES-HU','');
INSERT INTO `state` VALUES (1131,211,'Province','Jaén','ES-J','');
INSERT INTO `state` VALUES (1132,211,'Province','A Coruña','ES-C','');
INSERT INTO `state` VALUES (1133,211,'Province','La Rioja','ES-LO','');
INSERT INTO `state` VALUES (1134,211,'Province','Las Palmas','ES-GC','');
INSERT INTO `state` VALUES (1135,211,'Province','León','ES-LE','');
INSERT INTO `state` VALUES (1136,211,'Province','Lleida','ES-L','');
INSERT INTO `state` VALUES (1137,211,'Province','Lugo','ES-LU','');
INSERT INTO `state` VALUES (1138,211,'Province','Madrid','ES-M','');
INSERT INTO `state` VALUES (1139,211,'Province','Málaga','ES-MA','');
INSERT INTO `state` VALUES (1140,211,'Province','Murcia','ES-MU','');
INSERT INTO `state` VALUES (1141,211,'Province','Navarra','ES-NA','');
INSERT INTO `state` VALUES (1142,211,'Province','Ourense','ES-OR','');
INSERT INTO `state` VALUES (1143,211,'Province','Palencia','ES-P','');
INSERT INTO `state` VALUES (1144,211,'Province','Pontevedra','ES-PO','');
INSERT INTO `state` VALUES (1145,211,'Province','Salamanca','ES-SA','');
INSERT INTO `state` VALUES (1146,211,'Province','Santa Cruz de Tenerife','ES-TF','');
INSERT INTO `state` VALUES (1147,211,'Province','Segovia','ES-SG','');
INSERT INTO `state` VALUES (1148,211,'Province','Sevilla','ES-SE','');
INSERT INTO `state` VALUES (1149,211,'Province','Soria','ES-SO','');
INSERT INTO `state` VALUES (1150,211,'Province','Tarragona','ES-T','');
INSERT INTO `state` VALUES (1151,211,'Province','Teruel','ES-TE','');
INSERT INTO `state` VALUES (1152,211,'Province','Toledo','ES-TO','');
INSERT INTO `state` VALUES (1153,211,'Province','Valencia','ES-V','');
INSERT INTO `state` VALUES (1154,211,'Province','Valladolid','ES-VA','');
INSERT INTO `state` VALUES (1155,211,'Province','Vizcaya','ES-BI','');
INSERT INTO `state` VALUES (1156,211,'Province','Zamora','ES-ZA','');
INSERT INTO `state` VALUES (1157,211,'Province','Zaragoza','ES-Z','');
INSERT INTO `state` VALUES (1158,211,'Autonomous city','Ceuta','ES-CE','');
INSERT INTO `state` VALUES (1159,211,'Autonomous city','Melilla','ES-ML','');
INSERT INTO `state` VALUES (1160,74,'Administration','?d?s ?beba','ET-AA','');
INSERT INTO `state` VALUES (1161,74,'Administration','Dir? Dawa','ET-DD','');
INSERT INTO `state` VALUES (1162,74,'State','?far','ET-AF','');
INSERT INTO `state` VALUES (1163,74,'State','?mara','ET-AM','');
INSERT INTO `state` VALUES (1164,74,'State','B?nshangul Gumuz','ET-BE','');
INSERT INTO `state` VALUES (1165,74,'State','Gamb?la Hizboch','ET-GA','');
INSERT INTO `state` VALUES (1166,74,'State','H?rer? Hizb','ET-HA','');
INSERT INTO `state` VALUES (1167,74,'State','Orom?ya','ET-OR','');
INSERT INTO `state` VALUES (1168,74,'State','Sumal?','ET-SO','');
INSERT INTO `state` VALUES (1169,74,'State','Tigray','ET-TI','');
INSERT INTO `state` VALUES (1170,74,'State','YeDebub Bih?roch Bih?reseboch na Hizboch','ET-SN','');
INSERT INTO `state` VALUES (1171,78,'Province','Ahvenanmaan lääni','FI-AL','');
INSERT INTO `state` VALUES (1172,78,'Province','Etelä-Suomen lääni','FI-ES','');
INSERT INTO `state` VALUES (1173,78,'Province','Itä-Suomen lääni','FI-IS','');
INSERT INTO `state` VALUES (1174,78,'Province','Lapin lääni','FI-LL','');
INSERT INTO `state` VALUES (1175,78,'Province','Länsi-Suomen lääni','FI-LS','');
INSERT INTO `state` VALUES (1176,78,'Province','Oulun lääni','FI-OL','');
INSERT INTO `state` VALUES (1177,77,'Division','Central','FJ-C','');
INSERT INTO `state` VALUES (1178,77,'Division','Eastern','FJ-E','');
INSERT INTO `state` VALUES (1179,77,'Division','Northern','FJ-N','');
INSERT INTO `state` VALUES (1180,77,'Division','Western','FJ-W','');
INSERT INTO `state` VALUES (1181,77,'Dependency','Rotuma','FJ-R','');
INSERT INTO `state` VALUES (1182,148,'State','Chuuk','FM-TRK','');
INSERT INTO `state` VALUES (1183,148,'State','Kosrae','FM-KSA','');
INSERT INTO `state` VALUES (1184,148,'State','Pohnpei','FM-PNI','');
INSERT INTO `state` VALUES (1185,148,'State','Yap','FM-YAP','');
INSERT INTO `state` VALUES (1186,79,'Metropolitan region','Alsace','FR A','');
INSERT INTO `state` VALUES (1187,79,'Metropolitan region','Aquitaine','FR B','');
INSERT INTO `state` VALUES (1188,79,'Metropolitan region','Auvergne','FR C','');
INSERT INTO `state` VALUES (1189,79,'Metropolitan region','Basse-Normandie','FR P','');
INSERT INTO `state` VALUES (1190,79,'Metropolitan region','Bourgogne','FR D','');
INSERT INTO `state` VALUES (1191,79,'Metropolitan region','Bretagne','FR E','');
INSERT INTO `state` VALUES (1192,79,'Metropolitan region','Centre','FR F','');
INSERT INTO `state` VALUES (1193,79,'Metropolitan region','Champagne-Ardenne','FR G','');
INSERT INTO `state` VALUES (1194,79,'Metropolitan region','Corse','FR H','');
INSERT INTO `state` VALUES (1195,79,'Metropolitan region','Franche-Comté','FR I','');
INSERT INTO `state` VALUES (1196,79,'Metropolitan region','Haute-Normandie','FR Q','');
INSERT INTO `state` VALUES (1197,79,'Metropolitan region','Île-de-France','FR J','');
INSERT INTO `state` VALUES (1198,79,'Metropolitan region','Languedoc-Roussillon','FR K','');
INSERT INTO `state` VALUES (1199,79,'Metropolitan region','Limousin','FR L','');
INSERT INTO `state` VALUES (1200,79,'Metropolitan region','Lorraine','FR M','');
INSERT INTO `state` VALUES (1201,79,'Metropolitan region','Midi-Pyrénées','FR N','');
INSERT INTO `state` VALUES (1202,79,'Metropolitan region','Nord - Pas-de-Calais','FR O','');
INSERT INTO `state` VALUES (1203,79,'Metropolitan region','Pays de la Loire','FR R','');
INSERT INTO `state` VALUES (1204,79,'Metropolitan region','Picardie','FR S','');
INSERT INTO `state` VALUES (1205,79,'Metropolitan region','Poitou-Charentes','FR T','');
INSERT INTO `state` VALUES (1206,79,'Metropolitan region','Provence-Alpes-Côte d\'Azur','FR U','');
INSERT INTO `state` VALUES (1207,79,'Metropolitan region','Rhône-Alpes','FR V','');
INSERT INTO `state` VALUES (1208,79,'Overseas region/department','Guadeloupe','FR GP','');
INSERT INTO `state` VALUES (1209,79,'Overseas region/department','Guyane','FR GF','');
INSERT INTO `state` VALUES (1210,79,'Overseas region/department','Martinique','FR MQ','');
INSERT INTO `state` VALUES (1211,79,'Overseas region/department','Réunion','FR RE','');
INSERT INTO `state` VALUES (1212,79,'Metropolitan department','Ain','FR-01','');
INSERT INTO `state` VALUES (1213,79,'Metropolitan department','Aisne','FR-02','');
INSERT INTO `state` VALUES (1214,79,'Metropolitan department','Allier','FR-03','');
INSERT INTO `state` VALUES (1215,79,'Metropolitan department','Alpes-de-Haute-Provence','FR-04','');
INSERT INTO `state` VALUES (1216,79,'Metropolitan department','Alpes-Maritimes','FR-06','');
INSERT INTO `state` VALUES (1217,79,'Metropolitan department','Ardèche','FR-07','');
INSERT INTO `state` VALUES (1218,79,'Metropolitan department','Ardennes','FR-08','');
INSERT INTO `state` VALUES (1219,79,'Metropolitan department','Ariège','FR-09','');
INSERT INTO `state` VALUES (1220,79,'Metropolitan department','Aube','FR-10','');
INSERT INTO `state` VALUES (1221,79,'Metropolitan department','Aude','FR-11','');
INSERT INTO `state` VALUES (1222,79,'Metropolitan department','Aveyron','FR-12','');
INSERT INTO `state` VALUES (1223,79,'Metropolitan department','Bas-Rhin','FR-67','');
INSERT INTO `state` VALUES (1224,79,'Metropolitan department','Bouches-du-Rhône','FR-13','');
INSERT INTO `state` VALUES (1225,79,'Metropolitan department','Calvados','FR-14','');
INSERT INTO `state` VALUES (1226,79,'Metropolitan department','Cantal','FR-15','');
INSERT INTO `state` VALUES (1227,79,'Metropolitan department','Charente','FR-16','');
INSERT INTO `state` VALUES (1228,79,'Metropolitan department','Charente-Maritime','FR-17','');
INSERT INTO `state` VALUES (1229,79,'Metropolitan department','Cher','FR-18','');
INSERT INTO `state` VALUES (1230,79,'Metropolitan department','Corrèze','FR-19','');
INSERT INTO `state` VALUES (1231,79,'Metropolitan department','Corse-du-Sud','FR-2A','');
INSERT INTO `state` VALUES (1232,79,'Metropolitan department','Côte-d\'Or','FR-21','');
INSERT INTO `state` VALUES (1233,79,'Metropolitan department','Côtes-d\'Armor','FR-22','');
INSERT INTO `state` VALUES (1234,79,'Metropolitan department','Creuse','FR-23','');
INSERT INTO `state` VALUES (1235,79,'Metropolitan department','Deux-Sèvres','FR-79','');
INSERT INTO `state` VALUES (1236,79,'Metropolitan department','Dordogne','FR-24','');
INSERT INTO `state` VALUES (1237,79,'Metropolitan department','Doubs','FR-25','');
INSERT INTO `state` VALUES (1238,79,'Metropolitan department','Drôme','FR-26','');
INSERT INTO `state` VALUES (1239,79,'Metropolitan department','Essonne','FR-91','');
INSERT INTO `state` VALUES (1240,79,'Metropolitan department','Eure','FR-27','');
INSERT INTO `state` VALUES (1241,79,'Metropolitan department','Eure-et-Loir','FR-28','');
INSERT INTO `state` VALUES (1242,79,'Metropolitan department','Finistère','FR-29','');
INSERT INTO `state` VALUES (1243,79,'Metropolitan department','Gard','FR-30','');
INSERT INTO `state` VALUES (1244,79,'Metropolitan department','Gers','FR-32','');
INSERT INTO `state` VALUES (1245,79,'Metropolitan department','Gironde','FR-33','');
INSERT INTO `state` VALUES (1246,79,'Metropolitan department','Haute-Corse','FR-2B','');
INSERT INTO `state` VALUES (1247,79,'Metropolitan department','Haute-Garonne','FR-31','');
INSERT INTO `state` VALUES (1248,79,'Metropolitan department','Haute-Loire','FR-43','');
INSERT INTO `state` VALUES (1249,79,'Metropolitan department','Haute-Marne','FR-52','');
INSERT INTO `state` VALUES (1250,79,'Metropolitan department','Hautes-Alpes','FR-05','');
INSERT INTO `state` VALUES (1251,79,'Metropolitan department','Haute-Saône','FR-70','');
INSERT INTO `state` VALUES (1252,79,'Metropolitan department','Haute-Savoie','FR-74','');
INSERT INTO `state` VALUES (1253,79,'Metropolitan department','Hautes-Pyrénées','FR-65','');
INSERT INTO `state` VALUES (1254,79,'Metropolitan department','Haute-Vienne','FR-87','');
INSERT INTO `state` VALUES (1255,79,'Metropolitan department','Haut-Rhin','FR-68','');
INSERT INTO `state` VALUES (1256,79,'Metropolitan department','Hauts-de-Seine','FR-92','');
INSERT INTO `state` VALUES (1257,79,'Metropolitan department','Hérault','FR-34','');
INSERT INTO `state` VALUES (1258,79,'Metropolitan department','Ille-et-Vilaine','FR-35','');
INSERT INTO `state` VALUES (1259,79,'Metropolitan department','Indre','FR-36','');
INSERT INTO `state` VALUES (1260,79,'Metropolitan department','Indre-et-Loire','FR-37','');
INSERT INTO `state` VALUES (1261,79,'Metropolitan department','Isère','FR-38','');
INSERT INTO `state` VALUES (1262,79,'Metropolitan department','Jura','FR-39','');
INSERT INTO `state` VALUES (1263,79,'Metropolitan department','Landes','FR-40','');
INSERT INTO `state` VALUES (1264,79,'Metropolitan department','Loir-et-Cher','FR-41','');
INSERT INTO `state` VALUES (1265,79,'Metropolitan department','Loire','FR-42','');
INSERT INTO `state` VALUES (1266,79,'Metropolitan department','Loire-Atlantique','FR-44','');
INSERT INTO `state` VALUES (1267,79,'Metropolitan department','Loiret','FR-45','');
INSERT INTO `state` VALUES (1268,79,'Metropolitan department','Lot','FR-46','');
INSERT INTO `state` VALUES (1269,79,'Metropolitan department','Lot-et-Garonne','FR-47','');
INSERT INTO `state` VALUES (1270,79,'Metropolitan department','Lozère','FR-48','');
INSERT INTO `state` VALUES (1271,79,'Metropolitan department','Maine-et-Loire','FR-49','');
INSERT INTO `state` VALUES (1272,79,'Metropolitan department','Manche','FR-50','');
INSERT INTO `state` VALUES (1273,79,'Metropolitan department','Marne','FR-51','');
INSERT INTO `state` VALUES (1274,79,'Metropolitan department','Mayenne','FR-53','');
INSERT INTO `state` VALUES (1275,79,'Metropolitan department','Meurthe-et-Moselle','FR-54','');
INSERT INTO `state` VALUES (1276,79,'Metropolitan department','Meuse','FR-55','');
INSERT INTO `state` VALUES (1277,79,'Metropolitan department','Morbihan','FR-56','');
INSERT INTO `state` VALUES (1278,79,'Metropolitan department','Moselle','FR-57','');
INSERT INTO `state` VALUES (1279,79,'Metropolitan department','Nièvre','FR-58','');
INSERT INTO `state` VALUES (1280,79,'Metropolitan department','Nord','FR-59','');
INSERT INTO `state` VALUES (1281,79,'Metropolitan department','Oise','FR-60','');
INSERT INTO `state` VALUES (1282,79,'Metropolitan department','Orne','FR-61','');
INSERT INTO `state` VALUES (1283,79,'Metropolitan department','Paris','FR-75','');
INSERT INTO `state` VALUES (1284,79,'Metropolitan department','Pas-de-Calais','FR-62','');
INSERT INTO `state` VALUES (1285,79,'Metropolitan department','Puy-de-Dôme','FR-63','');
INSERT INTO `state` VALUES (1286,79,'Metropolitan department','Pyrénées-Atlantiques','FR-64','');
INSERT INTO `state` VALUES (1287,79,'Metropolitan department','Pyrénées-Orientales','FR-66','');
INSERT INTO `state` VALUES (1288,79,'Metropolitan department','Rhône','FR-69','');
INSERT INTO `state` VALUES (1289,79,'Metropolitan department','Saône-et-Loire','FR-71','');
INSERT INTO `state` VALUES (1290,79,'Metropolitan department','Sarthe','FR-72','');
INSERT INTO `state` VALUES (1291,79,'Metropolitan department','Savoie','FR-73','');
INSERT INTO `state` VALUES (1292,79,'Metropolitan department','Seine-et-Marne','FR-77','');
INSERT INTO `state` VALUES (1293,79,'Metropolitan department','Seine-Maritime','FR-76','');
INSERT INTO `state` VALUES (1294,79,'Metropolitan department','Seine-Saint-Denis','FR-93','');
INSERT INTO `state` VALUES (1295,79,'Metropolitan department','Somme','FR-80','');
INSERT INTO `state` VALUES (1296,79,'Metropolitan department','Tarn','FR-81','');
INSERT INTO `state` VALUES (1297,79,'Metropolitan department','Tarn-et-Garonne','FR-82','');
INSERT INTO `state` VALUES (1298,79,'Metropolitan department','Territoire de Belfort','FR-90','');
INSERT INTO `state` VALUES (1299,79,'Metropolitan department','Val-de-Marne','FR-94','');
INSERT INTO `state` VALUES (1300,79,'Metropolitan department','Val d\'Oise','FR-95','');
INSERT INTO `state` VALUES (1301,79,'Metropolitan department','Var','FR-83','');
INSERT INTO `state` VALUES (1302,79,'Metropolitan department','Vaucluse','FR-84','');
INSERT INTO `state` VALUES (1303,79,'Metropolitan department','Vendée','FR-85','');
INSERT INTO `state` VALUES (1304,79,'Metropolitan department','Vienne','FR-86','');
INSERT INTO `state` VALUES (1305,79,'Metropolitan department','Vosges','FR-88','');
INSERT INTO `state` VALUES (1306,79,'Metropolitan department','Yonne','FR-89','');
INSERT INTO `state` VALUES (1307,79,'Metropolitan department','Yvelines','FR-78','');
INSERT INTO `state` VALUES (1308,79,'Dependency','Clipperton','FR-CP','');
INSERT INTO `state` VALUES (1309,79,'Overseas territorial collectivity','Saint-Barthélemy','FR-BL','');
INSERT INTO `state` VALUES (1310,79,'Overseas territorial collectivity','Saint-Martin','FR-MF','');
INSERT INTO `state` VALUES (1311,79,'Overseas territorial collectivity','Nouvelle-Calédonie','FR-NC','');
INSERT INTO `state` VALUES (1312,79,'Overseas territorial collectivity','Polynésie française','FR-PF','');
INSERT INTO `state` VALUES (1313,79,'Overseas territorial collectivity','Saint-Pierre-et-Miquelon','FR-PM','');
INSERT INTO `state` VALUES (1314,79,'Overseas territorial collectivity','Terres australes françaises','FR-TF','');
INSERT INTO `state` VALUES (1315,79,'Overseas territorial collectivity','Wallis et Futuna','FR-WF','');
INSERT INTO `state` VALUES (1316,79,'Overseas territorial collectivity','Mayotte','FR-YT','');
INSERT INTO `state` VALUES (1317,2,'Country','England','GB ENG','');
INSERT INTO `state` VALUES (1318,2,'Country','Scotland','GB SCT','');
INSERT INTO `state` VALUES (1319,2,'Province','Northern Ireland','GB NIR','');
INSERT INTO `state` VALUES (1320,2,'Principality','Wales','GB WLS','');
INSERT INTO `state` VALUES (1321,2,'Included for completeness','England and Wales','GB EAW','');
INSERT INTO `state` VALUES (1322,2,'Included for completeness','Great Britain','GB GBN','');
INSERT INTO `state` VALUES (1323,2,'Included for completeness','United Kingdom','GB UKM','');
INSERT INTO `state` VALUES (1324,2,'Two-tier county','Bedfordshire','GB-BDF','');
INSERT INTO `state` VALUES (1325,2,'Two-tier county','Buckinghamshire','GB-BKM','');
INSERT INTO `state` VALUES (1326,2,'Two-tier county','Cambridgeshire','GB-CAM','');
INSERT INTO `state` VALUES (1327,2,'Two-tier county','Cheshire','GB-CHS','');
INSERT INTO `state` VALUES (1328,2,'Two-tier county','Cornwall','GB-CON','');
INSERT INTO `state` VALUES (1329,2,'Two-tier county','Cumbria','GB-CMA','');
INSERT INTO `state` VALUES (1330,2,'Two-tier county','Derbyshire','GB-DBY','');
INSERT INTO `state` VALUES (1331,2,'Two-tier county','Devon','GB-DEV','');
INSERT INTO `state` VALUES (1332,2,'Two-tier county','Dorset','GB-DOR','');
INSERT INTO `state` VALUES (1333,2,'Two-tier county','Durham','GB-DUR','');
INSERT INTO `state` VALUES (1334,2,'Two-tier county','East Sussex','GB-ESX','');
INSERT INTO `state` VALUES (1335,2,'Two-tier county','Essex','GB-ESS','');
INSERT INTO `state` VALUES (1336,2,'Two-tier county','Gloucestershire','GB-GLS','');
INSERT INTO `state` VALUES (1337,2,'Two-tier county','Hampshire','GB-HAM','');
INSERT INTO `state` VALUES (1338,2,'Two-tier county','Hertfordshire','GB-HRT','');
INSERT INTO `state` VALUES (1339,2,'Two-tier county','Kent','GB-KEN','');
INSERT INTO `state` VALUES (1340,2,'Two-tier county','Lancashire','GB-LAN','');
INSERT INTO `state` VALUES (1341,2,'Two-tier county','Leicestershire','GB-LEC','');
INSERT INTO `state` VALUES (1342,2,'Two-tier county','Lincolnshire','GB-LIN','');
INSERT INTO `state` VALUES (1343,2,'Two-tier county','Norfolk','GB-NFK','');
INSERT INTO `state` VALUES (1344,2,'Two-tier county','North Yorkshire','GB-NYK','');
INSERT INTO `state` VALUES (1345,2,'Two-tier county','Northamptonshire','GB-NTH','');
INSERT INTO `state` VALUES (1346,2,'Two-tier county','Northumbarland','GB-NBL','');
INSERT INTO `state` VALUES (1347,2,'Two-tier county','Nottinghamshire','GB-NTT','');
INSERT INTO `state` VALUES (1348,2,'Two-tier county','Oxfordshire','GB-OXF','');
INSERT INTO `state` VALUES (1349,2,'Two-tier county','Somerset','GB-SOM','');
INSERT INTO `state` VALUES (1350,2,'Two-tier county','Staffordshire','GB-STS','');
INSERT INTO `state` VALUES (1351,2,'Two-tier county','Suffolk','GB-SFK','');
INSERT INTO `state` VALUES (1352,2,'Two-tier county','Surrey','GB-SRY','');
INSERT INTO `state` VALUES (1353,2,'Two-tier county','West Sussex','GB-WSX','');
INSERT INTO `state` VALUES (1354,2,'Two-tier county','Wiltshire','GB-WIL','');
INSERT INTO `state` VALUES (1355,2,'Two-tier county','Worcestershire','GB-WOR','');
INSERT INTO `state` VALUES (1356,2,'London borough','Barking and Dagenham','GB-BDG','');
INSERT INTO `state` VALUES (1357,2,'London borough','Barnet','GB-BNE','');
INSERT INTO `state` VALUES (1358,2,'London borough','Bexley','GB-BEX','');
INSERT INTO `state` VALUES (1359,2,'London borough','Brent','GB-BEN','');
INSERT INTO `state` VALUES (1360,2,'London borough','Bromley','GB-BRY','');
INSERT INTO `state` VALUES (1361,2,'London borough','Camden','GB-CMD','');
INSERT INTO `state` VALUES (1362,2,'London borough','Croydon','GB-CRY','');
INSERT INTO `state` VALUES (1363,2,'London borough','Ealing','GB-EAL','');
INSERT INTO `state` VALUES (1364,2,'London borough','Enfield','GB-ENF','');
INSERT INTO `state` VALUES (1365,2,'London borough','Greenwich','GB-GRE','');
INSERT INTO `state` VALUES (1366,2,'London borough','Hackney','GB-HCK','');
INSERT INTO `state` VALUES (1367,2,'London borough','Hammersmith and Fulham','GB-HMF','');
INSERT INTO `state` VALUES (1368,2,'London borough','Haringey','GB-HRY','');
INSERT INTO `state` VALUES (1369,2,'London borough','Harrow','GB-HRW','');
INSERT INTO `state` VALUES (1370,2,'London borough','Havering','GB-HAV','');
INSERT INTO `state` VALUES (1371,2,'London borough','Hillingdon','GB-HIL','');
INSERT INTO `state` VALUES (1372,2,'London borough','Hounslow','GB-HNS','');
INSERT INTO `state` VALUES (1373,2,'London borough','Islington','GB-ISL','');
INSERT INTO `state` VALUES (1374,2,'London borough','Kensington and Chelsea','GB-KEC','');
INSERT INTO `state` VALUES (1375,2,'London borough','Kingston upon Thames','GB-KTT','');
INSERT INTO `state` VALUES (1376,2,'London borough','Lambeth','GB-LBH','');
INSERT INTO `state` VALUES (1377,2,'London borough','Lewisham','GB-LEW','');
INSERT INTO `state` VALUES (1378,2,'London borough','Merton','GB-MRT','');
INSERT INTO `state` VALUES (1379,2,'London borough','Newham','GB-NWM','');
INSERT INTO `state` VALUES (1380,2,'London borough','Redbridge','GB-RDB','');
INSERT INTO `state` VALUES (1381,2,'London borough','Richmond upon Thames','GB-RIC','');
INSERT INTO `state` VALUES (1382,2,'London borough','Southwark','GB-SWK','');
INSERT INTO `state` VALUES (1383,2,'London borough','Sutton','GB-STN','');
INSERT INTO `state` VALUES (1384,2,'London borough','Tower Hamlets','GB-TWH','');
INSERT INTO `state` VALUES (1385,2,'London borough','Waltham Forest','GB-WFT','');
INSERT INTO `state` VALUES (1386,2,'London borough','Wandsworth','GB-WND','');
INSERT INTO `state` VALUES (1387,2,'London borough','Westminster','GB-WSM','');
INSERT INTO `state` VALUES (1388,2,'Metropolitan district','Barnsley','GB-BNS','');
INSERT INTO `state` VALUES (1389,2,'Metropolitan district','Birmingham','GB-BIR','');
INSERT INTO `state` VALUES (1390,2,'Metropolitan district','Bolton','GB-BOL','');
INSERT INTO `state` VALUES (1391,2,'Metropolitan district','Bradford','GB-BRD','');
INSERT INTO `state` VALUES (1392,2,'Metropolitan district','Bury','GB-BUR','');
INSERT INTO `state` VALUES (1393,2,'Metropolitan district','Calderdale','GB-CLD','');
INSERT INTO `state` VALUES (1394,2,'Metropolitan district','Coventry','GB-COV','');
INSERT INTO `state` VALUES (1395,2,'Metropolitan district','Doncaster','GB-DNC','');
INSERT INTO `state` VALUES (1396,2,'Metropolitan district','Dudley','GB-DUD','');
INSERT INTO `state` VALUES (1397,2,'Metropolitan district','Gateshead','GB-GAT','');
INSERT INTO `state` VALUES (1398,2,'Metropolitan district','Kirklees','GB-KIR','');
INSERT INTO `state` VALUES (1399,2,'Metropolitan district','Knowsley','GB-KWL','');
INSERT INTO `state` VALUES (1400,2,'Metropolitan district','Leeds','GB-LDS','');
INSERT INTO `state` VALUES (1401,2,'Metropolitan district','Liverpool','GB-LIV','');
INSERT INTO `state` VALUES (1402,2,'Metropolitan district','Manchester','GB-MAN','');
INSERT INTO `state` VALUES (1403,2,'Metropolitan district','Newcastle upon Tyne','GB-NET','');
INSERT INTO `state` VALUES (1404,2,'Metropolitan district','North Tyneside','GB-NTY','');
INSERT INTO `state` VALUES (1405,2,'Metropolitan district','Oldham','GB-OLD','');
INSERT INTO `state` VALUES (1406,2,'Metropolitan district','Rochdale','GB-RCH','');
INSERT INTO `state` VALUES (1407,2,'Metropolitan district','Rotherham','GB-ROT','');
INSERT INTO `state` VALUES (1408,2,'Metropolitan district','Salford','GB-SLF','');
INSERT INTO `state` VALUES (1409,2,'Metropolitan district','Sandwell','GB-SAW','');
INSERT INTO `state` VALUES (1410,2,'Metropolitan district','Sefton','GB-SFT','');
INSERT INTO `state` VALUES (1411,2,'Metropolitan district','Sheffield','GB-SHF','');
INSERT INTO `state` VALUES (1412,2,'Metropolitan district','Solihull','GB-SOL','');
INSERT INTO `state` VALUES (1413,2,'Metropolitan district','South Tyneside','GB-STY','');
INSERT INTO `state` VALUES (1414,2,'Metropolitan district','St. Helens','GB-SHN','');
INSERT INTO `state` VALUES (1415,2,'Metropolitan district','Stockport','GB-SKP','');
INSERT INTO `state` VALUES (1416,2,'Metropolitan district','Sunderland','GB-SND','');
INSERT INTO `state` VALUES (1417,2,'Metropolitan district','Tameside','GB-TAM','');
INSERT INTO `state` VALUES (1418,2,'Metropolitan district','Trafford','GB-TRF','');
INSERT INTO `state` VALUES (1419,2,'Metropolitan district','Wakefield','GB-WKF','');
INSERT INTO `state` VALUES (1420,2,'Metropolitan district','Walsall','GB-WLL','');
INSERT INTO `state` VALUES (1421,2,'Metropolitan district','Wigan','GB-WGN','');
INSERT INTO `state` VALUES (1422,2,'Metropolitan district','Wirral','GB-WRL','');
INSERT INTO `state` VALUES (1423,2,'Metropolitan district','Wolverhampton','GB-WLV','');
INSERT INTO `state` VALUES (1424,2,'City corporation','London, City of','GB-LND','');
INSERT INTO `state` VALUES (1425,2,'Division','Aberdeen City','GB-ABE','');
INSERT INTO `state` VALUES (1426,2,'Division','Aberdeenshire','GB-ABD','');
INSERT INTO `state` VALUES (1427,2,'Division','Angus','GB-ANS','');
INSERT INTO `state` VALUES (1428,2,'Division','Antrim','GB-ANT','');
INSERT INTO `state` VALUES (1429,2,'Division','Ards','GB-ARD','');
INSERT INTO `state` VALUES (1430,2,'Division','Argyll and Bute','GB-AGB','');
INSERT INTO `state` VALUES (1431,2,'Division','Armagh','GB-ARM','');
INSERT INTO `state` VALUES (1432,2,'Division','Ballymena','GB-BLA','');
INSERT INTO `state` VALUES (1433,2,'Division','Ballymoney','GB-BLY','');
INSERT INTO `state` VALUES (1434,2,'Division','Banbridge','GB-BNB','');
INSERT INTO `state` VALUES (1435,2,'Division','Bath and North East Somerset','GB-BAS','');
INSERT INTO `state` VALUES (1436,2,'Division','Belfast','GB-BFS','');
INSERT INTO `state` VALUES (1437,2,'Division','Blackburn with Darwen','GB-BBD','');
INSERT INTO `state` VALUES (1438,2,'Division','Blackpool','GB-BPL','');
INSERT INTO `state` VALUES (1439,2,'Division','Blaenau Gwent','GB-BGW','');
INSERT INTO `state` VALUES (1440,2,'Division','Bournemouth','GB-BMH','');
INSERT INTO `state` VALUES (1441,2,'Division','Bracknell Forest','GB-BRC','');
INSERT INTO `state` VALUES (1442,2,'Division','Bridgend','GB-BGE','');
INSERT INTO `state` VALUES (1443,2,'Division','Brighton and Hove','GB-BNH','');
INSERT INTO `state` VALUES (1444,2,'Division','Bristol, City of','GB-BST','');
INSERT INTO `state` VALUES (1445,2,'Division','Caerphilly','GB-CAY','');
INSERT INTO `state` VALUES (1446,2,'Division','Cardiff','GB-CRF','');
INSERT INTO `state` VALUES (1447,2,'Division','Carmarthenshire','GB-CMN','');
INSERT INTO `state` VALUES (1448,2,'Division','Carrickfergus','GB-CKF','');
INSERT INTO `state` VALUES (1449,2,'Division','Castlereagh','GB-CSR','');
INSERT INTO `state` VALUES (1450,2,'Division','Ceredigion','GB-CGN','');
INSERT INTO `state` VALUES (1451,2,'Division','Clackmannanshire','GB-CLK','');
INSERT INTO `state` VALUES (1452,2,'Division','Coleraine','GB-CLR','');
INSERT INTO `state` VALUES (1453,2,'Division','Conwy','GB-CWY','');
INSERT INTO `state` VALUES (1454,2,'Division','Cookstown','GB-CKT','');
INSERT INTO `state` VALUES (1455,2,'Division','Craigavon','GB-CGV','');
INSERT INTO `state` VALUES (1456,2,'Division','Darlington','GB-DAL','');
INSERT INTO `state` VALUES (1457,2,'Division','Denbighshire','GB-DEN','');
INSERT INTO `state` VALUES (1458,2,'Division','Derby','GB-DER','');
INSERT INTO `state` VALUES (1459,2,'Division','Derry','GB-DRY','');
INSERT INTO `state` VALUES (1460,2,'Division','Down','GB-DOW','');
INSERT INTO `state` VALUES (1461,2,'Division','Dumfries and Galloway','GB-DGY','');
INSERT INTO `state` VALUES (1462,2,'Division','Dundee City','GB-DND','');
INSERT INTO `state` VALUES (1463,2,'Division','Dungannon','GB-DGN','');
INSERT INTO `state` VALUES (1464,2,'Division','East Ayrshire','GB-EAY','');
INSERT INTO `state` VALUES (1465,2,'Division','East Dunbartonshire','GB-EDU','');
INSERT INTO `state` VALUES (1466,2,'Division','East Lothian','GB-ELN','');
INSERT INTO `state` VALUES (1467,2,'Division','East Renfrewshire','GB-ERW','');
INSERT INTO `state` VALUES (1468,2,'Division','East Riding of Yorkshire','GB-ERY','');
INSERT INTO `state` VALUES (1469,2,'Division','Edinburgh, City of','GB-EDH','');
INSERT INTO `state` VALUES (1470,2,'Division','Eilean Siar','GB-ELS','');
INSERT INTO `state` VALUES (1471,2,'Division','Falkirk','GB-FAL','');
INSERT INTO `state` VALUES (1472,2,'Division','Fermanagh','GB-FER','');
INSERT INTO `state` VALUES (1473,2,'Division','Fife','GB-FIF','');
INSERT INTO `state` VALUES (1474,2,'Division','Flintshire','GB-FLN','');
INSERT INTO `state` VALUES (1475,2,'Division','Glasgow City','GB-GLG','');
INSERT INTO `state` VALUES (1476,2,'Division','Gwynedd','GB-GWN','');
INSERT INTO `state` VALUES (1477,2,'Division','Halton','GB-HAL','');
INSERT INTO `state` VALUES (1478,2,'Division','Hartlepool','GB-HPL','');
INSERT INTO `state` VALUES (1479,2,'Division','Herefordshire, County of','GB-HEF','');
INSERT INTO `state` VALUES (1480,2,'Division','Highland','GB-HED','');
INSERT INTO `state` VALUES (1481,2,'Division','Inverclyde','GB-IVC','');
INSERT INTO `state` VALUES (1482,2,'Division','Isle of Anglesey','GB-AGY','');
INSERT INTO `state` VALUES (1483,2,'Division','Isle of Wight','GB-IOW','');
INSERT INTO `state` VALUES (1484,2,'Division','Isles of Scilly','GB-IOS','');
INSERT INTO `state` VALUES (1485,2,'Division','Kingston upon Hull, City of','GB-KHL','');
INSERT INTO `state` VALUES (1486,2,'Division','Larne','GB-LRN','');
INSERT INTO `state` VALUES (1487,2,'Division','Leicester','GB-LCE','');
INSERT INTO `state` VALUES (1488,2,'Division','Limavady','GB-LMV','');
INSERT INTO `state` VALUES (1489,2,'Division','Lisburn','GB-LSB','');
INSERT INTO `state` VALUES (1490,2,'Division','Luton','GB-LUT','');
INSERT INTO `state` VALUES (1491,2,'Division','Magherafelt','GB-MFT','');
INSERT INTO `state` VALUES (1492,2,'Division','Medway','GB-MDW','');
INSERT INTO `state` VALUES (1493,2,'Division','Merthyr Tydfil','GB-MTY','');
INSERT INTO `state` VALUES (1494,2,'Division','Middlesbrough','GB-MDB','');
INSERT INTO `state` VALUES (1495,2,'Division','Midlothian','GB-MLN','');
INSERT INTO `state` VALUES (1496,2,'Division','Milton Keynes','GB-MIK','');
INSERT INTO `state` VALUES (1497,2,'Division','Monmouthshire','GB-MON','');
INSERT INTO `state` VALUES (1498,2,'Division','Moray','GB-MRY','');
INSERT INTO `state` VALUES (1499,2,'Division','Moyle','GB-MYL','');
INSERT INTO `state` VALUES (1500,2,'Division','Neath Port Talbot','GB-NTL','');
INSERT INTO `state` VALUES (1501,2,'Division','Newport','GB-NWP','');
INSERT INTO `state` VALUES (1502,2,'Division','Newry and Mourne','GB-NYM','');
INSERT INTO `state` VALUES (1503,2,'Division','Newtownabbey','GB-NTA','');
INSERT INTO `state` VALUES (1504,2,'Division','North Ayrshire','GB-NAY','');
INSERT INTO `state` VALUES (1505,2,'Division','North Down','GB-NDN','');
INSERT INTO `state` VALUES (1506,2,'Division','North East Lincolnshire','GB-NEL','');
INSERT INTO `state` VALUES (1507,2,'Division','North Lanarkshire','GB-NLK','');
INSERT INTO `state` VALUES (1508,2,'Division','North Lincolnshire','GB-NLN','');
INSERT INTO `state` VALUES (1509,2,'Division','North Somerset','GB-NSM','');
INSERT INTO `state` VALUES (1510,2,'Division','Nottingham','GB-NGM','');
INSERT INTO `state` VALUES (1511,2,'Division','Omagh','GB-OMH','');
INSERT INTO `state` VALUES (1512,2,'Division','Orkney Islands','GB-ORR','');
INSERT INTO `state` VALUES (1513,2,'Division','Pembrokeshire','GB-PEM','');
INSERT INTO `state` VALUES (1514,2,'Division','Perth and Kinross','GB-PKN','');
INSERT INTO `state` VALUES (1515,2,'Division','Peterborough','GB-PTE','');
INSERT INTO `state` VALUES (1516,2,'Division','Plymouth','GB-PLY','');
INSERT INTO `state` VALUES (1517,2,'Division','Poole','GB-POL','');
INSERT INTO `state` VALUES (1518,2,'Division','Portsmouth','GB-POR','');
INSERT INTO `state` VALUES (1519,2,'Division','Powys','GB-POW','');
INSERT INTO `state` VALUES (1520,2,'Division','Reading','GB-RDG','');
INSERT INTO `state` VALUES (1521,2,'Division','Redcar and Cleveland','GB-RCC','');
INSERT INTO `state` VALUES (1522,2,'Division','Renfrewshire','GB-RFW','');
INSERT INTO `state` VALUES (1523,2,'Division','Rhondda, Cynon, Taff','GB-RCT','');
INSERT INTO `state` VALUES (1524,2,'Division','Rutland','GB-RUT','');
INSERT INTO `state` VALUES (1525,2,'Division','Scottish Borders, The','GB-SCB','');
INSERT INTO `state` VALUES (1526,2,'Division','Shetland Islands','GB-ZET','');
INSERT INTO `state` VALUES (1527,2,'Division','Shropshire','GB-SHR','');
INSERT INTO `state` VALUES (1528,2,'Division','Slough','GB-SLG','');
INSERT INTO `state` VALUES (1529,2,'Division','South Ayrshire','GB-SAY','');
INSERT INTO `state` VALUES (1530,2,'Division','South Gloucestershire','GB-SGC','');
INSERT INTO `state` VALUES (1531,2,'Division','South Lanarkshire','GB-SLK','');
INSERT INTO `state` VALUES (1532,2,'Division','Southampton','GB-STH','');
INSERT INTO `state` VALUES (1533,2,'Division','Southend-on-Sea','GB-SOS','');
INSERT INTO `state` VALUES (1534,2,'Division','Stirling','GB-STG','');
INSERT INTO `state` VALUES (1535,2,'Division','Stockton-on-Tees','GB-STT','');
INSERT INTO `state` VALUES (1536,2,'Division','Stoke-on-Trent','GB-STE','');
INSERT INTO `state` VALUES (1537,2,'Division','Strabane','GB-STB','');
INSERT INTO `state` VALUES (1538,2,'Division','Swansea','GB-SWA','');
INSERT INTO `state` VALUES (1539,2,'Division','Swindon','GB-SWD','');
INSERT INTO `state` VALUES (1540,2,'Division','Telford and Wrekin','GB-TFW','');
INSERT INTO `state` VALUES (1541,2,'Division','Thurrock','GB-THR','');
INSERT INTO `state` VALUES (1542,2,'Division','Torbay','GB-TOB','');
INSERT INTO `state` VALUES (1543,2,'Division','Torfaen','GB-TOF','');
INSERT INTO `state` VALUES (1544,2,'Division','Vale of Glamorgan, The','GB-VGL','');
INSERT INTO `state` VALUES (1545,2,'Division','Warrington','GB-WRT','');
INSERT INTO `state` VALUES (1546,2,'Division','Warwickshire','GB-WAR','');
INSERT INTO `state` VALUES (1547,2,'Division','West Berkshire','GB-WBX','');
INSERT INTO `state` VALUES (1548,2,'Division','West Dunbartonshire','GB-WDU','');
INSERT INTO `state` VALUES (1549,2,'Division','West Lothian','GB-WLN','');
INSERT INTO `state` VALUES (1550,2,'Division','Windsor and Maidenhead','GB-WNM','');
INSERT INTO `state` VALUES (1551,2,'Division','Wokingham','GB-WOK','');
INSERT INTO `state` VALUES (1552,2,'Division','Wrexham','GB-WRX','');
INSERT INTO `state` VALUES (1553,2,'Division','York','GB-YOR','');
INSERT INTO `state` VALUES (1554,91,'Parish','Saint Andrew','GD-01','');
INSERT INTO `state` VALUES (1555,91,'Parish','Saint David','GD-02','');
INSERT INTO `state` VALUES (1556,91,'Parish','Saint George','GD-03','');
INSERT INTO `state` VALUES (1557,91,'Parish','Saint John','GD-04','');
INSERT INTO `state` VALUES (1558,91,'Parish','Saint Mark','GD-05','');
INSERT INTO `state` VALUES (1559,91,'Parish','Saint Patrick','GD-06','');
INSERT INTO `state` VALUES (1560,91,'Dependency','Southern Grenadine Islands','GD-10','');
INSERT INTO `state` VALUES (1561,85,'Autonomous republic','Abkhazia','GE-AB','');
INSERT INTO `state` VALUES (1562,85,'Autonomous republic','Ajaria','GE-AJ','');
INSERT INTO `state` VALUES (1563,85,'City','T’bilisi','GE-TB','');
INSERT INTO `state` VALUES (1564,85,'Region','Guria','GE-GU','');
INSERT INTO `state` VALUES (1565,85,'Region','Imeret’i','GE-IM','');
INSERT INTO `state` VALUES (1566,85,'Region','Kakhet’i','GE-KA','');
INSERT INTO `state` VALUES (1567,85,'Region','K’vemo K’art’li','GE-KK','');
INSERT INTO `state` VALUES (1568,85,'Region','Mts’khet’a-Mt’ianet’i','GE-MM','');
INSERT INTO `state` VALUES (1569,85,'Region','Racha-Lech’khumi-K’vemo Svanet’i','GE-RL','');
INSERT INTO `state` VALUES (1570,85,'Region','Samegrelo-Zemo Svanet’i','GE-SZ','');
INSERT INTO `state` VALUES (1571,85,'Region','Samts’khe-Javakhet’i','GE-SJ','');
INSERT INTO `state` VALUES (1572,85,'Region','Shida K’art’li','GE-SK','');
INSERT INTO `state` VALUES (1573,87,'Region','Ashanti','GH-AH','');
INSERT INTO `state` VALUES (1574,87,'Region','Brong-Ahafo','GH-BA','');
INSERT INTO `state` VALUES (1575,87,'Region','Central','GH-CP','');
INSERT INTO `state` VALUES (1576,87,'Region','Eastern','GH-EP','');
INSERT INTO `state` VALUES (1577,87,'Region','Greater Accra','GH-AA','');
INSERT INTO `state` VALUES (1578,87,'Region','Northern','GH-NP','');
INSERT INTO `state` VALUES (1579,87,'Region','Upper East','GH-UE','');
INSERT INTO `state` VALUES (1580,87,'Region','Upper West','GH-UW','');
INSERT INTO `state` VALUES (1581,87,'Region','Volta','GH-TV','');
INSERT INTO `state` VALUES (1582,87,'Region','Western','GH-WP','');
INSERT INTO `state` VALUES (1583,84,'Division','Lower River','GM-L','');
INSERT INTO `state` VALUES (1584,84,'Division','Central River','GM-M','');
INSERT INTO `state` VALUES (1585,84,'Division','North Bank','GM-N','');
INSERT INTO `state` VALUES (1586,84,'Division','Upper River','GM-U','');
INSERT INTO `state` VALUES (1587,84,'Division','Western','GM-W','');
INSERT INTO `state` VALUES (1588,84,'City','Banjul','GM-B','');
INSERT INTO `state` VALUES (1589,96,'Governorate','Boké, Gouvernorat de','GN B','');
INSERT INTO `state` VALUES (1590,96,'Governorate','Faranah, Gouvernorat de','GN F','');
INSERT INTO `state` VALUES (1591,96,'Governorate','Kankan, Gouvernorat de','GN K','');
INSERT INTO `state` VALUES (1592,96,'Governorate','Kindia, Gouvernorat de','GN D','');
INSERT INTO `state` VALUES (1593,96,'Governorate','Labé, Gouvernorat de','GN L','');
INSERT INTO `state` VALUES (1594,96,'Governorate','Mamou, Gouvernorat de','GN M','');
INSERT INTO `state` VALUES (1595,96,'Governorate','Nzérékoré, Gouvernorat de','GN N','');
INSERT INTO `state` VALUES (1596,96,'City','Conakry','GN C','');
INSERT INTO `state` VALUES (1597,96,'Prefecture','Beyla','GN-BE','');
INSERT INTO `state` VALUES (1598,96,'Prefecture','Boffa','GN-BF','');
INSERT INTO `state` VALUES (1599,96,'Prefecture','Boké','GN-BK','');
INSERT INTO `state` VALUES (1600,96,'Prefecture','Coyah','GN-CO','');
INSERT INTO `state` VALUES (1601,96,'Prefecture','Dabola','GN-DB','');
INSERT INTO `state` VALUES (1602,96,'Prefecture','Dalaba','GN-DL','');
INSERT INTO `state` VALUES (1603,96,'Prefecture','Dinguiraye','GN-DI','');
INSERT INTO `state` VALUES (1604,96,'Prefecture','Dubréka','GN-DU','');
INSERT INTO `state` VALUES (1605,96,'Prefecture','Faranah','GN-FA','');
INSERT INTO `state` VALUES (1606,96,'Prefecture','Forécariah','GN-FO','');
INSERT INTO `state` VALUES (1607,96,'Prefecture','Fria','GN-FR','');
INSERT INTO `state` VALUES (1608,96,'Prefecture','Gaoual','GN-GA','');
INSERT INTO `state` VALUES (1609,96,'Prefecture','Guékédou','GN-GU','');
INSERT INTO `state` VALUES (1610,96,'Prefecture','Kankan','GN-KA','');
INSERT INTO `state` VALUES (1611,96,'Prefecture','Kérouané','GN-KE','');
INSERT INTO `state` VALUES (1612,96,'Prefecture','Kindia','GN-KD','');
INSERT INTO `state` VALUES (1613,96,'Prefecture','Kissidougou','GN-KS','');
INSERT INTO `state` VALUES (1614,96,'Prefecture','Koubia','GN-KB','');
INSERT INTO `state` VALUES (1615,96,'Prefecture','Koundara','GN-KN','');
INSERT INTO `state` VALUES (1616,96,'Prefecture','Kouroussa','GN-KO','');
INSERT INTO `state` VALUES (1617,96,'Prefecture','Labé','GN-LA','');
INSERT INTO `state` VALUES (1618,96,'Prefecture','Lélouma','GN-LE','');
INSERT INTO `state` VALUES (1619,96,'Prefecture','Lola','GN-LO','');
INSERT INTO `state` VALUES (1620,96,'Prefecture','Macenta','GN-MC','');
INSERT INTO `state` VALUES (1621,96,'Prefecture','Mali','GN-ML','');
INSERT INTO `state` VALUES (1622,96,'Prefecture','Mamou','GN-MM','');
INSERT INTO `state` VALUES (1623,96,'Prefecture','Mandiana','GN-MD','');
INSERT INTO `state` VALUES (1624,96,'Prefecture','Nzérékoré','GN-NZ','');
INSERT INTO `state` VALUES (1625,96,'Prefecture','Pita','GN-PI','');
INSERT INTO `state` VALUES (1626,96,'Prefecture','Siguiri','GN-SI','');
INSERT INTO `state` VALUES (1627,96,'Prefecture','Télimélé','GN-TE','');
INSERT INTO `state` VALUES (1628,96,'Prefecture','Tougué','GN-TO','');
INSERT INTO `state` VALUES (1629,96,'Prefecture','Yomou','GN-YO','');
INSERT INTO `state` VALUES (1630,71,'Province','Región Continental','GQ-C','');
INSERT INTO `state` VALUES (1631,71,'Province','Región Insular','GQ-I','');
INSERT INTO `state` VALUES (1632,71,'Province','Annobón','GQ-AN','');
INSERT INTO `state` VALUES (1633,71,'Province','Bioko Norte','GQ-BN','');
INSERT INTO `state` VALUES (1634,71,'Province','Bioko Sur','GQ-BS','');
INSERT INTO `state` VALUES (1635,71,'Province','Centro Sur','GQ-CS','');
INSERT INTO `state` VALUES (1636,71,'Province','Kié-Ntem','GQ-KN','');
INSERT INTO `state` VALUES (1637,71,'Province','Litoral','GQ-LI','');
INSERT INTO `state` VALUES (1638,71,'Province','Wele-Nzás','GQ-WN','');
INSERT INTO `state` VALUES (1639,89,'Region','Periféreia Anatolikís Makedonías kai Thrákis','GR I','');
INSERT INTO `state` VALUES (1640,89,'Region','Periféreia Kentrikís Makedonías','GR II','');
INSERT INTO `state` VALUES (1641,89,'Region','Periféreia Dytikís Makedonías','GR III','');
INSERT INTO `state` VALUES (1642,89,'Region','Periféreia Ipeírou','GR IV','');
INSERT INTO `state` VALUES (1643,89,'Region','Periféreia Thessalías','GR V','');
INSERT INTO `state` VALUES (1644,89,'Region','Periféreia Ioníon Níson','GR VI','');
INSERT INTO `state` VALUES (1645,89,'Region','Periféreia Dytikís Elládas','GR VII','');
INSERT INTO `state` VALUES (1646,89,'Region','Periféreia Stereás Elládas','GR VIII','');
INSERT INTO `state` VALUES (1647,89,'Region','Periféreia Attikís','GR IX','');
INSERT INTO `state` VALUES (1648,89,'Region','Periféreia Peloponnísou','GR X','');
INSERT INTO `state` VALUES (1649,89,'Region','Periféreia Voreíou Aigaíou','GR XI','');
INSERT INTO `state` VALUES (1650,89,'Region','Periféreia Notíou Aigaíou','GR XII','');
INSERT INTO `state` VALUES (1651,89,'Region','Periféreia Krítis','GR XIII','');
INSERT INTO `state` VALUES (1652,89,'Autonomous monastic state','Ágion Óros','GR-69','');
INSERT INTO `state` VALUES (1653,89,'Prefecture','Nomós Athinón','GR-A1','');
INSERT INTO `state` VALUES (1654,89,'Prefecture','Nomós Anatolikís Attikís','GR-A2','');
INSERT INTO `state` VALUES (1655,89,'Prefecture','Nomós Peiraiós','GR-A3','');
INSERT INTO `state` VALUES (1656,89,'Prefecture','Nomós Dytikís Attikís','GR-A4','');
INSERT INTO `state` VALUES (1657,89,'Prefecture','Nomós Aitoloakarnanías','GR-01','');
INSERT INTO `state` VALUES (1658,89,'Prefecture','Nomós Voiotías','GR-03','');
INSERT INTO `state` VALUES (1659,89,'Prefecture','Nomós Évvoias','GR-04','');
INSERT INTO `state` VALUES (1660,89,'Prefecture','Nomós Evrytanías','GR-05','');
INSERT INTO `state` VALUES (1661,89,'Prefecture','Nomós Fthiótidas','GR-06','');
INSERT INTO `state` VALUES (1662,89,'Prefecture','Nomós Fokídas','GR-07','');
INSERT INTO `state` VALUES (1663,89,'Prefecture','Nomós Argolídas','GR-11','');
INSERT INTO `state` VALUES (1664,89,'Prefecture','Nomós Arkadías','GR-12','');
INSERT INTO `state` VALUES (1665,89,'Prefecture','Nomós Acha?as','GR-13','');
INSERT INTO `state` VALUES (1666,89,'Prefecture','Nomós Ileías','GR-14','');
INSERT INTO `state` VALUES (1667,89,'Prefecture','Nomós Korinthías','GR-15','');
INSERT INTO `state` VALUES (1668,89,'Prefecture','Nomós Lakonías','GR-16','');
INSERT INTO `state` VALUES (1669,89,'Prefecture','Nomós Messinías','GR-17','');
INSERT INTO `state` VALUES (1670,89,'Prefecture','Nomós Zakýnthoy','GR-21','');
INSERT INTO `state` VALUES (1671,89,'Prefecture','Nomós Kérkyras','GR-22','');
INSERT INTO `state` VALUES (1672,89,'Prefecture','Nomós Kefaloniás kai Ithákis','GR-23','');
INSERT INTO `state` VALUES (1673,89,'Prefecture','Nomós Lefkádas','GR-24','');
INSERT INTO `state` VALUES (1674,89,'Prefecture','Nomós Ártas','GR-31','');
INSERT INTO `state` VALUES (1675,89,'Prefecture','Nomós Thesprotías','GR-32','');
INSERT INTO `state` VALUES (1676,89,'Prefecture','Nomós Ioannínon','GR-33','');
INSERT INTO `state` VALUES (1677,89,'Prefecture','Nomós Prévezas','GR-34','');
INSERT INTO `state` VALUES (1678,89,'Prefecture','Nomós Kardítsas','GR-41','');
INSERT INTO `state` VALUES (1679,89,'Prefecture','Nomós Lárissas','GR-42','');
INSERT INTO `state` VALUES (1680,89,'Prefecture','Nomós Magnisías','GR-43','');
INSERT INTO `state` VALUES (1681,89,'Prefecture','Nomós Trikálon','GR-44','');
INSERT INTO `state` VALUES (1682,89,'Prefecture','Nomós Grevenón','GR-51','');
INSERT INTO `state` VALUES (1683,89,'Prefecture','Nomós Drámas','GR-52','');
INSERT INTO `state` VALUES (1684,89,'Prefecture','Nomós Imathías','GR-53','');
INSERT INTO `state` VALUES (1685,89,'Prefecture','Nomós Thessaloníkis','GR-54','');
INSERT INTO `state` VALUES (1686,89,'Prefecture','Nomós Kaválas','GR-55','');
INSERT INTO `state` VALUES (1687,89,'Prefecture','Nomós Kastoriás','GR-56','');
INSERT INTO `state` VALUES (1688,89,'Prefecture','Nomós Kilkís','GR-57','');
INSERT INTO `state` VALUES (1689,89,'Prefecture','Nomós Kozánis','GR-58','');
INSERT INTO `state` VALUES (1690,89,'Prefecture','Nomós Péllas','GR-59','');
INSERT INTO `state` VALUES (1691,89,'Prefecture','Nomós Pierías','GR-61','');
INSERT INTO `state` VALUES (1692,89,'Prefecture','Nomós Serrón','GR-62','');
INSERT INTO `state` VALUES (1693,89,'Prefecture','Nomós Flórinas','GR-63','');
INSERT INTO `state` VALUES (1694,89,'Prefecture','Nomós Chalkidikís','GR-64','');
INSERT INTO `state` VALUES (1695,89,'Prefecture','Nomós Évroy','GR-71','');
INSERT INTO `state` VALUES (1696,89,'Prefecture','Nomós Xánthis','GR-72','');
INSERT INTO `state` VALUES (1697,89,'Prefecture','Nomós Rodópis','GR-73','');
INSERT INTO `state` VALUES (1698,89,'Prefecture','Nomós Dodekanísoy','GR-81','');
INSERT INTO `state` VALUES (1699,89,'Prefecture','Nomós Kykládon','GR-82','');
INSERT INTO `state` VALUES (1700,89,'Prefecture','Nomós Lésboy','GR-83','');
INSERT INTO `state` VALUES (1701,89,'Prefecture','Nomós Sámoy','GR-84','');
INSERT INTO `state` VALUES (1702,89,'Prefecture','Nomós Chíoy','GR-85','');
INSERT INTO `state` VALUES (1703,89,'Prefecture','Nomós Irakleíoy','GR-91','');
INSERT INTO `state` VALUES (1704,89,'Prefecture','Nomós Lasithíoy','GR-92','');
INSERT INTO `state` VALUES (1705,89,'Prefecture','Nomós Rethýmnoy','GR-93','');
INSERT INTO `state` VALUES (1706,89,'Prefecture','Nomós Chaníon','GR-94','');
INSERT INTO `state` VALUES (1707,94,'Department','Alta Verapaz','GT-AV','');
INSERT INTO `state` VALUES (1708,94,'Department','Baja Verapaz','GT-BV','');
INSERT INTO `state` VALUES (1709,94,'Department','Chimaltenango','GT-CM','');
INSERT INTO `state` VALUES (1710,94,'Department','Chiquimula','GT-CQ','');
INSERT INTO `state` VALUES (1711,94,'Department','El Progreso','GT-PR','');
INSERT INTO `state` VALUES (1712,94,'Department','Escuintla','GT-ES','');
INSERT INTO `state` VALUES (1713,94,'Department','Guatemala','GT-GU','');
INSERT INTO `state` VALUES (1714,94,'Department','Huehuetenango','GT-HU','');
INSERT INTO `state` VALUES (1715,94,'Department','Izabal','GT-IZ','');
INSERT INTO `state` VALUES (1716,94,'Department','Jalapa','GT-JA','');
INSERT INTO `state` VALUES (1717,94,'Department','Jutiapa','GT-JU','');
INSERT INTO `state` VALUES (1718,94,'Department','Petén','GT-PE','');
INSERT INTO `state` VALUES (1719,94,'Department','Quetzaltenango','GT-QZ','');
INSERT INTO `state` VALUES (1720,94,'Department','Quiché','GT-QC','');
INSERT INTO `state` VALUES (1721,94,'Department','Retalhuleu','GT-RE','');
INSERT INTO `state` VALUES (1722,94,'Department','Sacatepéquez','GT-SA','');
INSERT INTO `state` VALUES (1723,94,'Department','San Marcos','GT-SM','');
INSERT INTO `state` VALUES (1724,94,'Department','Santa Rosa','GT-SR','');
INSERT INTO `state` VALUES (1725,94,'Department','Sololá','GT-SO','');
INSERT INTO `state` VALUES (1726,94,'Department','Suchitepéquez','GT-SU','');
INSERT INTO `state` VALUES (1727,94,'Department','Totonicapán','GT-TO','');
INSERT INTO `state` VALUES (1728,94,'Department','Zacapa','GT-ZA','');
INSERT INTO `state` VALUES (1729,97,'Region','Bafatá','GW-BA','');
INSERT INTO `state` VALUES (1730,97,'Region','Biombo','GW-BM','');
INSERT INTO `state` VALUES (1731,97,'Region','Bolama','GW-BL','');
INSERT INTO `state` VALUES (1732,97,'Region','Cacheu','GW-CA','');
INSERT INTO `state` VALUES (1733,97,'Region','Gabú','GW-GA','');
INSERT INTO `state` VALUES (1734,97,'Region','Oio','GW-OI','');
INSERT INTO `state` VALUES (1735,97,'Region','Quinara','GW-QU','');
INSERT INTO `state` VALUES (1736,97,'Region','Tombali','GW-TO','');
INSERT INTO `state` VALUES (1737,97,'Autonomous sector','Bissau','GW-BS','');
INSERT INTO `state` VALUES (1738,98,'Region','Barima-Waini','GY-BA','');
INSERT INTO `state` VALUES (1739,98,'Region','Cuyuni-Mazaruni','GY-CU','');
INSERT INTO `state` VALUES (1740,98,'Region','Demerara-Mahaica','GY-DE','');
INSERT INTO `state` VALUES (1741,98,'Region','East Berbice-Corentyne','GY-EB','');
INSERT INTO `state` VALUES (1742,98,'Region','Essequibo Islands-West Demerara','GY-ES','');
INSERT INTO `state` VALUES (1743,98,'Region','Mahaica-Berbice','GY-MA','');
INSERT INTO `state` VALUES (1744,98,'Region','Pomeroon-Supenaam','GY-PM','');
INSERT INTO `state` VALUES (1745,98,'Region','Potaro-Siparuni','GY-PT','');
INSERT INTO `state` VALUES (1746,98,'Region','Upper Demerara-Berbice','GY-UD','');
INSERT INTO `state` VALUES (1747,98,'Region','Upper Takutu-Upper Essequibo','GY-UT','');
INSERT INTO `state` VALUES (1748,102,'Department','Atlántida','HN-AT','');
INSERT INTO `state` VALUES (1749,102,'Department','Colón','HN-CL','');
INSERT INTO `state` VALUES (1750,102,'Department','Comayagua','HN-CM','');
INSERT INTO `state` VALUES (1751,102,'Department','Copán','HN-CP','');
INSERT INTO `state` VALUES (1752,102,'Department','Cortés','HN-CR','');
INSERT INTO `state` VALUES (1753,102,'Department','Choluteca','HN-CH','');
INSERT INTO `state` VALUES (1754,102,'Department','El Paraíso','HN-EP','');
INSERT INTO `state` VALUES (1755,102,'Department','Francisco Morazán','HN-FM','');
INSERT INTO `state` VALUES (1756,102,'Department','Gracias a Dios','HN-GD','');
INSERT INTO `state` VALUES (1757,102,'Department','Intibucá','HN-IN','');
INSERT INTO `state` VALUES (1758,102,'Department','Islas de la Bahía','HN-IB','');
INSERT INTO `state` VALUES (1759,102,'Department','La Paz','HN-LP','');
INSERT INTO `state` VALUES (1760,102,'Department','Lempira','HN-LE','');
INSERT INTO `state` VALUES (1761,102,'Department','Ocotepeque','HN-OC','');
INSERT INTO `state` VALUES (1762,102,'Department','Olancho','HN-OL','');
INSERT INTO `state` VALUES (1763,102,'Department','Santa Bárbara','HN-SB','');
INSERT INTO `state` VALUES (1764,102,'Department','Valle','HN-VA','');
INSERT INTO `state` VALUES (1765,102,'Department','Yoro','HN-YO','');
INSERT INTO `state` VALUES (1766,59,'City','Grad Zagreb','HR-21','');
INSERT INTO `state` VALUES (1767,59,'County','Bjelovarsko-bilogorska županija','HR-07','');
INSERT INTO `state` VALUES (1768,59,'County','Brodsko-posavska županija','HR-12','');
INSERT INTO `state` VALUES (1769,59,'County','Dubrova?ko-neretvanska županija','HR-19','');
INSERT INTO `state` VALUES (1770,59,'County','Istarska županija','HR-18','');
INSERT INTO `state` VALUES (1771,59,'County','Karlova?ka županija','HR-04','');
INSERT INTO `state` VALUES (1772,59,'County','Koprivni?ko-križeva?ka županija','HR-06','');
INSERT INTO `state` VALUES (1773,59,'County','Krapinsko-zagorska županija','HR-02','');
INSERT INTO `state` VALUES (1774,59,'County','Li?ko-senjska županija','HR-09','');
INSERT INTO `state` VALUES (1775,59,'County','Me?imurska županija','HR-20','');
INSERT INTO `state` VALUES (1776,59,'County','Osje?ko-baranjska županija','HR-14','');
INSERT INTO `state` VALUES (1777,59,'County','Požeško-slavonska županija','HR-11','');
INSERT INTO `state` VALUES (1778,59,'County','Primorsko-goranska županija','HR-08','');
INSERT INTO `state` VALUES (1779,59,'County','Sisa?ko-moslava?ka županija','HR-03','');
INSERT INTO `state` VALUES (1780,59,'County','Splitsko-dalmatinska županija','HR-17','');
INSERT INTO `state` VALUES (1781,59,'County','Šibensko-kninska županija','HR-15','');
INSERT INTO `state` VALUES (1782,59,'County','Varaždinska županija','HR-05','');
INSERT INTO `state` VALUES (1783,59,'County','Viroviti?ko-podravska županija','HR-10','');
INSERT INTO `state` VALUES (1784,59,'County','Vukovarsko-srijemska županija','HR-16','');
INSERT INTO `state` VALUES (1785,59,'County','Zadarska županija','HR-13','');
INSERT INTO `state` VALUES (1786,59,'County','Zagreba?ka županija','HR-01','');
INSERT INTO `state` VALUES (1787,99,'Department','Artibonite','HT-AR','');
INSERT INTO `state` VALUES (1788,99,'Department','Centre','HT-CE','');
INSERT INTO `state` VALUES (1789,99,'Department','Grande-Anse','HT-GA','');
INSERT INTO `state` VALUES (1790,99,'Department','Nord','HT-ND','');
INSERT INTO `state` VALUES (1791,99,'Department','Nord-Est','HT-NE','');
INSERT INTO `state` VALUES (1792,99,'Department','Nord-Ouest','HT-NO','');
INSERT INTO `state` VALUES (1793,99,'Department','Ouest','HT-OU','');
INSERT INTO `state` VALUES (1794,99,'Department','Sud','HT-SD','');
INSERT INTO `state` VALUES (1795,99,'Department','Sud-Est','HT-SE','');
INSERT INTO `state` VALUES (1796,104,'County','Bács-Kiskun','HU-BK','');
INSERT INTO `state` VALUES (1797,104,'County','Baranya','HU-BA','');
INSERT INTO `state` VALUES (1798,104,'County','Békés','HU-BE','');
INSERT INTO `state` VALUES (1799,104,'County','Borsod-Abaúj-Zemplén','HU-BZ','');
INSERT INTO `state` VALUES (1800,104,'County','Csongrád','HU-CS','');
INSERT INTO `state` VALUES (1801,104,'County','Fejér','HU-FE','');
INSERT INTO `state` VALUES (1802,104,'County','Gy?r-Moson-Sopron','HU-GS','');
INSERT INTO `state` VALUES (1803,104,'County','Hajdú-Bihar','HU-HB','');
INSERT INTO `state` VALUES (1804,104,'County','Heves','HU-HE','');
INSERT INTO `state` VALUES (1805,104,'County','Jász-Nagykun-Szolnok','HU-JN','');
INSERT INTO `state` VALUES (1806,104,'County','Komárom-Esztergom','HU-KE','');
INSERT INTO `state` VALUES (1807,104,'County','Nógrád','HU-NO','');
INSERT INTO `state` VALUES (1808,104,'County','Pest','HU-PE','');
INSERT INTO `state` VALUES (1809,104,'County','Somogy','HU-SO','');
INSERT INTO `state` VALUES (1810,104,'County','Szabolcs-Szatmár-Bereg','HU-SZ','');
INSERT INTO `state` VALUES (1811,104,'County','Tolna','HU-TO','');
INSERT INTO `state` VALUES (1812,104,'County','Vas','HU-VA','');
INSERT INTO `state` VALUES (1813,104,'County','Veszprém (county)','HU-VE','');
INSERT INTO `state` VALUES (1814,104,'County','Zala','HU-ZA','');
INSERT INTO `state` VALUES (1815,104,'City with county rights','Békéscsaba','HU-BC','');
INSERT INTO `state` VALUES (1816,104,'City with county rights','Debrecen','HU-DE','');
INSERT INTO `state` VALUES (1817,104,'City with county rights','Dunaújváros','HU-DU','');
INSERT INTO `state` VALUES (1818,104,'City with county rights','Eger','HU-EG','');
INSERT INTO `state` VALUES (1819,104,'City with county rights','Gy?r','HU-GY','');
INSERT INTO `state` VALUES (1820,104,'City with county rights','Hódmez?vásárhely','HU-HV','');
INSERT INTO `state` VALUES (1821,104,'City with county rights','Kaposvár','HU-KV','');
INSERT INTO `state` VALUES (1822,104,'City with county rights','Kecskemét','HU-KM','');
INSERT INTO `state` VALUES (1823,104,'City with county rights','Miskolc','HU-MI','');
INSERT INTO `state` VALUES (1824,104,'City with county rights','Nagykanizsa','HU-NK','');
INSERT INTO `state` VALUES (1825,104,'City with county rights','Nyíregyháza','HU-NY','');
INSERT INTO `state` VALUES (1826,104,'City with county rights','Pécs','HU-PS','');
INSERT INTO `state` VALUES (1827,104,'City with county rights','Salgótarján','HU-ST','');
INSERT INTO `state` VALUES (1828,104,'City with county rights','Sopron','HU-SN','');
INSERT INTO `state` VALUES (1829,104,'City with county rights','Szeged','HU-SD','');
INSERT INTO `state` VALUES (1830,104,'City with county rights','Székesfehérvár','HU-SF','');
INSERT INTO `state` VALUES (1831,104,'City with county rights','Szekszárd','HU-SS','');
INSERT INTO `state` VALUES (1832,104,'City with county rights','Szolnok','HU-SK','');
INSERT INTO `state` VALUES (1833,104,'City with county rights','Szombathely','HU-SH','');
INSERT INTO `state` VALUES (1834,104,'City with county rights','Tatabánya','HU-TB','');
INSERT INTO `state` VALUES (1835,104,'City with county rights','Veszprém','HU-VM','');
INSERT INTO `state` VALUES (1836,104,'City with county rights','Zalaegerszeg','HU-ZE','');
INSERT INTO `state` VALUES (1837,104,'Capital city','Budapest','HU-BU','');
INSERT INTO `state` VALUES (1838,107,'Geographical unit','Papua','ID IJ','');
INSERT INTO `state` VALUES (1839,107,'Geographical unit','Jawa','ID JW','');
INSERT INTO `state` VALUES (1840,107,'Geographical unit','Kalimantan','ID KA','');
INSERT INTO `state` VALUES (1841,107,'Geographical unit','Maluku','ID MA','');
INSERT INTO `state` VALUES (1842,107,'Geographical unit','Nusa Tenggara','ID NU','');
INSERT INTO `state` VALUES (1843,107,'Geographical unit','Sulawesi','ID SL','');
INSERT INTO `state` VALUES (1844,107,'Geographical unit','Sumatera','ID SM','');
INSERT INTO `state` VALUES (1845,107,'Autonomous Province','Aceh','ID-AC','');
INSERT INTO `state` VALUES (1846,107,'Province','Bali','ID-BA','');
INSERT INTO `state` VALUES (1847,107,'Province','Bangka Belitung','ID-BB','');
INSERT INTO `state` VALUES (1848,107,'Province','Banten','ID-BT','');
INSERT INTO `state` VALUES (1849,107,'Province','Bengkulu','ID-BE','');
INSERT INTO `state` VALUES (1850,107,'Province','Gorontalo','ID-GO','');
INSERT INTO `state` VALUES (1851,107,'Province','Jambi','ID-JA','');
INSERT INTO `state` VALUES (1852,107,'Province','Jawa Barat','ID-JB','');
INSERT INTO `state` VALUES (1853,107,'Province','Jawa Tengah','ID-JT','');
INSERT INTO `state` VALUES (1854,107,'Province','Jawa Timur','ID-JI','');
INSERT INTO `state` VALUES (1855,107,'Province','Kalimantan Barat','ID-KB','');
INSERT INTO `state` VALUES (1856,107,'Province','Kalimantan Tengah','ID-KT','');
INSERT INTO `state` VALUES (1857,107,'Province','Kalimantan Selatan','ID-KS','');
INSERT INTO `state` VALUES (1858,107,'Province','Kalimantan Timur','ID-KI','');
INSERT INTO `state` VALUES (1859,107,'Province','Kepulauan Riau','ID-KR','');
INSERT INTO `state` VALUES (1860,107,'Province','Lampung','ID-LA','');
INSERT INTO `state` VALUES (1861,107,'Province','Maluku','ID-MA','');
INSERT INTO `state` VALUES (1862,107,'Province','Maluku Utara','ID-MU','');
INSERT INTO `state` VALUES (1863,107,'Province','Nusa Tenggara Barat','ID-NB','');
INSERT INTO `state` VALUES (1864,107,'Province','Nusa Tenggara Timur','ID-NT','');
INSERT INTO `state` VALUES (1865,107,'Province','Papua','ID-PA','');
INSERT INTO `state` VALUES (1866,107,'Province','Riau','ID-RI','');
INSERT INTO `state` VALUES (1867,107,'Province','Sulawesi Barat','ID-SR','');
INSERT INTO `state` VALUES (1868,107,'Province','Sulawesi Selatan','ID-SN','');
INSERT INTO `state` VALUES (1869,107,'Province','Sulawesi Tengah','ID-ST','');
INSERT INTO `state` VALUES (1870,107,'Province','Sulawesi Tenggara','ID-SG','');
INSERT INTO `state` VALUES (1871,107,'Province','Sulawesi Utara','ID-SA','');
INSERT INTO `state` VALUES (1872,107,'Province','Sumatra Barat','ID-SB','');
INSERT INTO `state` VALUES (1873,107,'Province','Sumatra Selatan','ID-SS','');
INSERT INTO `state` VALUES (1874,107,'Province','Sumatera Utara','ID-SU','');
INSERT INTO `state` VALUES (1875,107,'Special District','Jakarta Raya','ID-JK','');
INSERT INTO `state` VALUES (1876,107,'Special Region','Yogyakarta','ID-YO','');
INSERT INTO `state` VALUES (1877,110,'Province','Connacht','IE C','');
INSERT INTO `state` VALUES (1878,110,'Province','Leinster','IE L','');
INSERT INTO `state` VALUES (1879,110,'Province','Munster','IE M','');
INSERT INTO `state` VALUES (1880,110,'Province','Ulster','IE U','');
INSERT INTO `state` VALUES (1881,110,'County','Cork','IE-C','');
INSERT INTO `state` VALUES (1882,110,'County','Clare','IE-CE','');
INSERT INTO `state` VALUES (1883,110,'County','Cavan','IE-CN','');
INSERT INTO `state` VALUES (1884,110,'County','Carlow','IE-CW','');
INSERT INTO `state` VALUES (1885,110,'County','Dublin','IE-D','');
INSERT INTO `state` VALUES (1886,110,'County','Donegal','IE-DL','');
INSERT INTO `state` VALUES (1887,110,'County','Galway','IE-G','');
INSERT INTO `state` VALUES (1888,110,'County','Kildare','IE-KE','');
INSERT INTO `state` VALUES (1889,110,'County','Kilkenny','IE-KK','');
INSERT INTO `state` VALUES (1890,110,'County','Kerry','IE-KY','');
INSERT INTO `state` VALUES (1891,110,'County','Longford','IE-LD','');
INSERT INTO `state` VALUES (1892,110,'County','Louth','IE-LH','');
INSERT INTO `state` VALUES (1893,110,'County','Limerick','IE-LK','');
INSERT INTO `state` VALUES (1894,110,'County','Leitrim','IE-LM','');
INSERT INTO `state` VALUES (1895,110,'County','Laois','IE-LS','');
INSERT INTO `state` VALUES (1896,110,'County','Meath','IE-MH','');
INSERT INTO `state` VALUES (1897,110,'County','Monaghan','IE-MN','');
INSERT INTO `state` VALUES (1898,110,'County','Mayo','IE-MO','');
INSERT INTO `state` VALUES (1899,110,'County','Offaly','IE-OY','');
INSERT INTO `state` VALUES (1900,110,'County','Roscommon','IE-RN','');
INSERT INTO `state` VALUES (1901,110,'County','Sligo','IE-SO','');
INSERT INTO `state` VALUES (1902,110,'County','Tipperary','IE-TA','');
INSERT INTO `state` VALUES (1903,110,'County','Waterford','IE-WD','');
INSERT INTO `state` VALUES (1904,110,'County','Westmeath','IE-WH','');
INSERT INTO `state` VALUES (1905,110,'County','Wicklow','IE-WW','');
INSERT INTO `state` VALUES (1906,110,'County','Wexford','IE-WX','');
INSERT INTO `state` VALUES (1907,112,'District','HaDarom','IL-D','');
INSERT INTO `state` VALUES (1908,112,'District','HaMerkaz','IL-M','');
INSERT INTO `state` VALUES (1909,112,'District','HaZafon','IL-Z','');
INSERT INTO `state` VALUES (1910,112,'District','Hefa','IL-HA','');
INSERT INTO `state` VALUES (1911,112,'District','Tel-Aviv','IL-TA','');
INSERT INTO `state` VALUES (1912,112,'District','Yerushalayim Al Quds','IL-JM','');
INSERT INTO `state` VALUES (1913,106,'State','Andhra Pradesh','IN-AP','');
INSERT INTO `state` VALUES (1914,106,'State','Arun?chal Pradesh','IN-AR','');
INSERT INTO `state` VALUES (1915,106,'State','Assam','IN-AS','');
INSERT INTO `state` VALUES (1916,106,'State','Bih?r','IN-BR','');
INSERT INTO `state` VALUES (1917,106,'State','Chhatt?sgarh','IN-CT','');
INSERT INTO `state` VALUES (1918,106,'State','Goa','IN-GA','');
INSERT INTO `state` VALUES (1919,106,'State','Gujar?t','IN-GJ','');
INSERT INTO `state` VALUES (1920,106,'State','Hary?na','IN-HR','');
INSERT INTO `state` VALUES (1921,106,'State','Him?chal Pradesh','IN-HP','');
INSERT INTO `state` VALUES (1922,106,'State','Jammu and Kashm?r','IN-JK','');
INSERT INTO `state` VALUES (1923,106,'State','Jharkhand','IN-JH','');
INSERT INTO `state` VALUES (1924,106,'State','Karn?taka','IN-KA','');
INSERT INTO `state` VALUES (1925,106,'State','Kerala','IN-KL','');
INSERT INTO `state` VALUES (1926,106,'State','Madhya Pradesh','IN-MP','');
INSERT INTO `state` VALUES (1927,106,'State','Mah?r?shtra','IN-MH','');
INSERT INTO `state` VALUES (1928,106,'State','Manipur','IN-MN','');
INSERT INTO `state` VALUES (1929,106,'State','Megh?laya','IN-ML','');
INSERT INTO `state` VALUES (1930,106,'State','Mizoram','IN-MZ','');
INSERT INTO `state` VALUES (1931,106,'State','N?g?land','IN-NL','');
INSERT INTO `state` VALUES (1932,106,'State','Orissa','IN-OR','');
INSERT INTO `state` VALUES (1933,106,'State','Punjab','IN-PB','');
INSERT INTO `state` VALUES (1934,106,'State','R?jasth?n','IN-RJ','');
INSERT INTO `state` VALUES (1935,106,'State','Sikkim','IN-SK','');
INSERT INTO `state` VALUES (1936,106,'State','Tamil N?du','IN-TN','');
INSERT INTO `state` VALUES (1937,106,'State','Tripura','IN-TR','');
INSERT INTO `state` VALUES (1938,106,'State','Uttaranchal','IN-UL','');
INSERT INTO `state` VALUES (1939,106,'State','Uttar Pradesh','IN-UP','');
INSERT INTO `state` VALUES (1940,106,'State','West Bengal','IN-WB','');
INSERT INTO `state` VALUES (1941,106,'Union territory','Andaman and Nicobar Islands','IN-AN','');
INSERT INTO `state` VALUES (1942,106,'Union territory','Chand?garh','IN-CH','');
INSERT INTO `state` VALUES (1943,106,'Union territory','D?dra and Nagar Haveli','IN-DN','');
INSERT INTO `state` VALUES (1944,106,'Union territory','Dam?n and Diu','IN-DD','');
INSERT INTO `state` VALUES (1945,106,'Union territory','Delhi','IN-DL','');
INSERT INTO `state` VALUES (1946,106,'Union territory','Lakshadweep','IN-LD','');
INSERT INTO `state` VALUES (1947,106,'Union territory','Pondicherry','IN-PY','');
INSERT INTO `state` VALUES (1948,109,'Governorate','Al Anbar','IQ-AN','');
INSERT INTO `state` VALUES (1949,109,'Governorate','Al Basrah','IQ-BA','');
INSERT INTO `state` VALUES (1950,109,'Governorate','Al Muthanna','IQ-MU','');
INSERT INTO `state` VALUES (1951,109,'Governorate','Al Qadisiyah','IQ-QA','');
INSERT INTO `state` VALUES (1952,109,'Governorate','An Najef','IQ-NA','');
INSERT INTO `state` VALUES (1953,109,'Governorate','Arbil','IQ-AR','');
INSERT INTO `state` VALUES (1954,109,'Governorate','As Sulaymaniyah','IQ-SW','');
INSERT INTO `state` VALUES (1955,109,'Governorate','At Ta\'mim','IQ-TS','');
INSERT INTO `state` VALUES (1956,109,'Governorate','Babil','IQ-BB','');
INSERT INTO `state` VALUES (1957,109,'Governorate','Baghdad','IQ-BG','');
INSERT INTO `state` VALUES (1958,109,'Governorate','Dahuk','IQ-DA','');
INSERT INTO `state` VALUES (1959,109,'Governorate','Dhi Qar','IQ-DQ','');
INSERT INTO `state` VALUES (1960,109,'Governorate','Diyala','IQ-DI','');
INSERT INTO `state` VALUES (1961,109,'Governorate','Karbala\'','IQ-KA','');
INSERT INTO `state` VALUES (1962,109,'Governorate','Maysan','IQ-MA','');
INSERT INTO `state` VALUES (1963,109,'Governorate','Ninawa','IQ-NI','');
INSERT INTO `state` VALUES (1964,109,'Governorate','Salah ad Din','IQ-SD','');
INSERT INTO `state` VALUES (1965,109,'Governorate','Wasit','IQ-WA','');
INSERT INTO `state` VALUES (1966,108,'Province','Ardab?l','IR-03','');
INSERT INTO `state` VALUES (1967,108,'Province','?zarb?yj?n-e Gharb?','IR-02','');
INSERT INTO `state` VALUES (1968,108,'Province','?zarb?yj?n-e Sharq?','IR-01','');
INSERT INTO `state` VALUES (1969,108,'Province','B?shehr','IR-06','');
INSERT INTO `state` VALUES (1970,108,'Province','Chah?r Mah?ll va Bakht??r?','IR-08','');
INSERT INTO `state` VALUES (1971,108,'Province','E?fah?n','IR-04','');
INSERT INTO `state` VALUES (1972,108,'Province','F?rs','IR-14','');
INSERT INTO `state` VALUES (1973,108,'Province','G?l?n','IR-19','');
INSERT INTO `state` VALUES (1974,108,'Province','Golest?n','IR-27','');
INSERT INTO `state` VALUES (1975,108,'Province','Hamad?n','IR-24','');
INSERT INTO `state` VALUES (1976,108,'Province','Hormozg?n','IR-23','');
INSERT INTO `state` VALUES (1977,108,'Province','?l?m','IR-05','');
INSERT INTO `state` VALUES (1978,108,'Province','Kerm?n','IR-15','');
INSERT INTO `state` VALUES (1979,108,'Province','Kerm?nsh?h','IR-17','');
INSERT INTO `state` VALUES (1980,108,'Province','Khor?s?n-e Jan?b?','IR-29','');
INSERT INTO `state` VALUES (1981,108,'Province','Khor?s?n-e Razav?','IR-30','');
INSERT INTO `state` VALUES (1982,108,'Province','Khor?s?n-e Shem?l?','IR-31','');
INSERT INTO `state` VALUES (1983,108,'Province','Kh?zest?n','IR-10','');
INSERT INTO `state` VALUES (1984,108,'Province','Kohg?l?yeh va B?yer Ahmad','IR-18','');
INSERT INTO `state` VALUES (1985,108,'Province','Kordest?n','IR-16','');
INSERT INTO `state` VALUES (1986,108,'Province','Lorest?n','IR-20','');
INSERT INTO `state` VALUES (1987,108,'Province','Markaz?','IR-22','');
INSERT INTO `state` VALUES (1988,108,'Province','M?zandar?n','IR-21','');
INSERT INTO `state` VALUES (1989,108,'Province','Qazv?n','IR-28','');
INSERT INTO `state` VALUES (1990,108,'Province','Qom','IR-26','');
INSERT INTO `state` VALUES (1991,108,'Province','Semn?n','IR-12','');
INSERT INTO `state` VALUES (1992,108,'Province','S?st?n va Bal?chest?n','IR-13','');
INSERT INTO `state` VALUES (1993,108,'Province','Tehr?n','IR-07','');
INSERT INTO `state` VALUES (1994,108,'Province','Yazd','IR-25','');
INSERT INTO `state` VALUES (1995,108,'Province','Zanj?n','IR-11','');
INSERT INTO `state` VALUES (1996,105,'Region','Austurland','IS-7','');
INSERT INTO `state` VALUES (1997,105,'Region','Höfuðborgarsvæðið','IS-1','');
INSERT INTO `state` VALUES (1998,105,'Region','Norðurland eystra','IS-6','');
INSERT INTO `state` VALUES (1999,105,'Region','Norðurland vestra','IS-5','');
INSERT INTO `state` VALUES (2000,105,'Region','Suðurland','IS-8','');
INSERT INTO `state` VALUES (2001,105,'Region','Suðurnes','IS-2','');
INSERT INTO `state` VALUES (2002,105,'Region','Vestfirðir','IS-4','');
INSERT INTO `state` VALUES (2003,105,'Region','Vesturland','IS-3','');
INSERT INTO `state` VALUES (2004,105,'City','Reykjavík','IS-0','');
INSERT INTO `state` VALUES (2005,113,'Region','Abruzzo','IT 65','');
INSERT INTO `state` VALUES (2006,113,'Region','Basilicata','IT 77','');
INSERT INTO `state` VALUES (2007,113,'Region','Calabria','IT 78','');
INSERT INTO `state` VALUES (2008,113,'Region','Campania','IT 72','');
INSERT INTO `state` VALUES (2009,113,'Region','Emilia-Romagna','IT 45','');
INSERT INTO `state` VALUES (2010,113,'Region','Friuli-Venezia Giulia','IT 36','');
INSERT INTO `state` VALUES (2011,113,'Region','Lazio','IT 62','');
INSERT INTO `state` VALUES (2012,113,'Region','Liguria','IT 42','');
INSERT INTO `state` VALUES (2013,113,'Region','Lombardia','IT 25','');
INSERT INTO `state` VALUES (2014,113,'Region','Marche','IT 57','');
INSERT INTO `state` VALUES (2015,113,'Region','Molise','IT 67','');
INSERT INTO `state` VALUES (2016,113,'Region','Piemonte','IT 21','');
INSERT INTO `state` VALUES (2017,113,'Region','Puglia','IT 75','');
INSERT INTO `state` VALUES (2018,113,'Region','Sardegna','IT 88','');
INSERT INTO `state` VALUES (2019,113,'Region','Sicilia','IT 82','');
INSERT INTO `state` VALUES (2020,113,'Region','Toscana','IT 52','');
INSERT INTO `state` VALUES (2021,113,'Region','Trentino-Alto Adige','IT 32','');
INSERT INTO `state` VALUES (2022,113,'Region','Umbria','IT 55','');
INSERT INTO `state` VALUES (2023,113,'Region','Valle d\'Aosta','IT 23','');
INSERT INTO `state` VALUES (2024,113,'Region','Veneto','IT 34','');
INSERT INTO `state` VALUES (2025,113,'Province','Agrigento','IT-AG','');
INSERT INTO `state` VALUES (2026,113,'Province','Alessandria','IT-AL','');
INSERT INTO `state` VALUES (2027,113,'Province','Ancona','IT-AN','');
INSERT INTO `state` VALUES (2028,113,'Province','Aosta','IT-AO','');
INSERT INTO `state` VALUES (2029,113,'Province','Arezzo','IT-AR','');
INSERT INTO `state` VALUES (2030,113,'Province','Ascoli Piceno','IT-AP','');
INSERT INTO `state` VALUES (2031,113,'Province','Asti','IT-AT','');
INSERT INTO `state` VALUES (2032,113,'Province','Avellino','IT-AV','');
INSERT INTO `state` VALUES (2033,113,'Province','Bari','IT-BA','');
INSERT INTO `state` VALUES (2034,113,'Province','Belluno','IT-BL','');
INSERT INTO `state` VALUES (2035,113,'Province','Benevento','IT-BN','');
INSERT INTO `state` VALUES (2036,113,'Province','Bergamo','IT-BG','');
INSERT INTO `state` VALUES (2037,113,'Province','Biella','IT-BI','');
INSERT INTO `state` VALUES (2038,113,'Province','Bologna','IT-BO','');
INSERT INTO `state` VALUES (2039,113,'Province','Bolzano','IT-BZ','');
INSERT INTO `state` VALUES (2040,113,'Province','Brescia','IT-BS','');
INSERT INTO `state` VALUES (2041,113,'Province','Brindisi','IT-BR','');
INSERT INTO `state` VALUES (2042,113,'Province','Cagliari','IT-CA','');
INSERT INTO `state` VALUES (2043,113,'Province','Caltanissetta','IT-CL','');
INSERT INTO `state` VALUES (2044,113,'Province','Campobasso','IT-CB','');
INSERT INTO `state` VALUES (2045,113,'Province','Carbonia-Iglesias','IT-CI','');
INSERT INTO `state` VALUES (2046,113,'Province','Caserta','IT-CE','');
INSERT INTO `state` VALUES (2047,113,'Province','Catania','IT-CT','');
INSERT INTO `state` VALUES (2048,113,'Province','Catanzaro','IT-CZ','');
INSERT INTO `state` VALUES (2049,113,'Province','Chieti','IT-CH','');
INSERT INTO `state` VALUES (2050,113,'Province','Como','IT-CO','');
INSERT INTO `state` VALUES (2051,113,'Province','Cosenza','IT-CS','');
INSERT INTO `state` VALUES (2052,113,'Province','Cremona','IT-CR','');
INSERT INTO `state` VALUES (2053,113,'Province','Crotone','IT-KR','');
INSERT INTO `state` VALUES (2054,113,'Province','Cuneo','IT-CN','');
INSERT INTO `state` VALUES (2055,113,'Province','Enna','IT-EN','');
INSERT INTO `state` VALUES (2056,113,'Province','Ferrara','IT-FE','');
INSERT INTO `state` VALUES (2057,113,'Province','Firenze','IT-FI','');
INSERT INTO `state` VALUES (2058,113,'Province','Foggia','IT-FG','');
INSERT INTO `state` VALUES (2059,113,'Province','Forlì','IT-FO','');
INSERT INTO `state` VALUES (2060,113,'Province','Frosinone','IT-FR','');
INSERT INTO `state` VALUES (2061,113,'Province','Genova','IT-GE','');
INSERT INTO `state` VALUES (2062,113,'Province','Gorizia','IT-GO','');
INSERT INTO `state` VALUES (2063,113,'Province','Grosseto','IT-GR','');
INSERT INTO `state` VALUES (2064,113,'Province','Imperia','IT-IM','');
INSERT INTO `state` VALUES (2065,113,'Province','Isernia','IT-IS','');
INSERT INTO `state` VALUES (2066,113,'Province','La Spezia','IT-SP','');
INSERT INTO `state` VALUES (2067,113,'Province','L\'Aquila','IT-AQ','');
INSERT INTO `state` VALUES (2068,113,'Province','Latina','IT-LT','');
INSERT INTO `state` VALUES (2069,113,'Province','Lecce','IT-LE','');
INSERT INTO `state` VALUES (2070,113,'Province','Lecco','IT-LC','');
INSERT INTO `state` VALUES (2071,113,'Province','Livorno','IT-LI','');
INSERT INTO `state` VALUES (2072,113,'Province','Lodi','IT-LO','');
INSERT INTO `state` VALUES (2073,113,'Province','Lucca','IT-LU','');
INSERT INTO `state` VALUES (2074,113,'Province','Macerata','IT-SC','');
INSERT INTO `state` VALUES (2075,113,'Province','Mantova','IT-MN','');
INSERT INTO `state` VALUES (2076,113,'Province','Massa-Carrara','IT-MS','');
INSERT INTO `state` VALUES (2077,113,'Province','Matera','IT-MT','');
INSERT INTO `state` VALUES (2078,113,'Province','Medio Campidano','IT-VS','');
INSERT INTO `state` VALUES (2079,113,'Province','Messina','IT-ME','');
INSERT INTO `state` VALUES (2080,113,'Province','Milano','IT-MI','');
INSERT INTO `state` VALUES (2081,113,'Province','Modena','IT-MO','');
INSERT INTO `state` VALUES (2082,113,'Province','Napoli','IT-NA','');
INSERT INTO `state` VALUES (2083,113,'Province','Novara','IT-NO','');
INSERT INTO `state` VALUES (2084,113,'Province','Nuoro','IT-NU','');
INSERT INTO `state` VALUES (2085,113,'Province','Ogliastra','IT-OG','');
INSERT INTO `state` VALUES (2086,113,'Province','Olbia-Tempio','IT-OT','');
INSERT INTO `state` VALUES (2087,113,'Province','Oristano','IT-OR','');
INSERT INTO `state` VALUES (2088,113,'Province','Padova','IT-PD','');
INSERT INTO `state` VALUES (2089,113,'Province','Palermo','IT-PA','');
INSERT INTO `state` VALUES (2090,113,'Province','Parma','IT-PR','');
INSERT INTO `state` VALUES (2091,113,'Province','Pavia','IT-PV','');
INSERT INTO `state` VALUES (2092,113,'Province','Perugia','IT-PG','');
INSERT INTO `state` VALUES (2093,113,'Province','Pesaro e Urbino','IT-PS','');
INSERT INTO `state` VALUES (2094,113,'Province','Pescara','IT-PE','');
INSERT INTO `state` VALUES (2095,113,'Province','Piacenza','IT-PC','');
INSERT INTO `state` VALUES (2096,113,'Province','Pisa','IT-PI','');
INSERT INTO `state` VALUES (2097,113,'Province','Pistoia','IT-PT','');
INSERT INTO `state` VALUES (2098,113,'Province','Pordenone','IT-PN','');
INSERT INTO `state` VALUES (2099,113,'Province','Potenza','IT-PZ','');
INSERT INTO `state` VALUES (2100,113,'Province','Prato','IT-PO','');
INSERT INTO `state` VALUES (2101,113,'Province','Ragusa','IT-RG','');
INSERT INTO `state` VALUES (2102,113,'Province','Ravenna','IT-RA','');
INSERT INTO `state` VALUES (2103,113,'Province','Reggio Calabria','IT-RC','');
INSERT INTO `state` VALUES (2104,113,'Province','Reggio Emilia','IT-RE','');
INSERT INTO `state` VALUES (2105,113,'Province','Rieti','IT-RI','');
INSERT INTO `state` VALUES (2106,113,'Province','Rimini','IT-RN','');
INSERT INTO `state` VALUES (2107,113,'Province','Roma','IT-RM','');
INSERT INTO `state` VALUES (2108,113,'Province','Rovigo','IT-RO','');
INSERT INTO `state` VALUES (2109,113,'Province','Salerno','IT-SA','');
INSERT INTO `state` VALUES (2110,113,'Province','Sassari','IT-SS','');
INSERT INTO `state` VALUES (2111,113,'Province','Savona','IT-SV','');
INSERT INTO `state` VALUES (2112,113,'Province','Siena','IT-SI','');
INSERT INTO `state` VALUES (2113,113,'Province','Siracusa','IT-SR','');
INSERT INTO `state` VALUES (2114,113,'Province','Sondrio','IT-SO','');
INSERT INTO `state` VALUES (2115,113,'Province','Taranto','IT-TA','');
INSERT INTO `state` VALUES (2116,113,'Province','Teramo','IT-TE','');
INSERT INTO `state` VALUES (2117,113,'Province','Terni','IT-TR','');
INSERT INTO `state` VALUES (2118,113,'Province','Torino','IT-TO','');
INSERT INTO `state` VALUES (2119,113,'Province','Trapani','IT-TP','');
INSERT INTO `state` VALUES (2120,113,'Province','Trento','IT-TN','');
INSERT INTO `state` VALUES (2121,113,'Province','Treviso','IT-TV','');
INSERT INTO `state` VALUES (2122,113,'Province','Trieste','IT-TS','');
INSERT INTO `state` VALUES (2123,113,'Province','Udine','IT-UD','');
INSERT INTO `state` VALUES (2124,113,'Province','Varese','IT-VA','');
INSERT INTO `state` VALUES (2125,113,'Province','Venezia','IT-VE','');
INSERT INTO `state` VALUES (2126,113,'Province','Verbano-Cusio-Ossola','IT-VB','');
INSERT INTO `state` VALUES (2127,113,'Province','Vercelli','IT-VC','');
INSERT INTO `state` VALUES (2128,113,'Province','Verona','IT-VR','');
INSERT INTO `state` VALUES (2129,113,'Province','Vibo Valentia','IT-VV','');
INSERT INTO `state` VALUES (2130,113,'Province','Vicenza','IT-VI','');
INSERT INTO `state` VALUES (2131,113,'Province','Viterbo','IT-VT','');
INSERT INTO `state` VALUES (2132,114,'Parish','Clarendon','JM-13','');
INSERT INTO `state` VALUES (2133,114,'Parish','Hanover','JM-09','');
INSERT INTO `state` VALUES (2134,114,'Parish','Kingston','JM-01','');
INSERT INTO `state` VALUES (2135,114,'Parish','Manchester','JM-12','');
INSERT INTO `state` VALUES (2136,114,'Parish','Portland','JM-04','');
INSERT INTO `state` VALUES (2137,114,'Parish','Saint Andrew','JM-02','');
INSERT INTO `state` VALUES (2138,114,'Parish','Saint Ann','JM-06','');
INSERT INTO `state` VALUES (2139,114,'Parish','Saint Catherine','JM-14','');
INSERT INTO `state` VALUES (2140,114,'Parish','Saint Elizabeth','JM-11','');
INSERT INTO `state` VALUES (2141,114,'Parish','Saint James','JM-08','');
INSERT INTO `state` VALUES (2142,114,'Parish','Saint Mary','JM-05','');
INSERT INTO `state` VALUES (2143,114,'Parish','Saint Thomas','JM-03','');
INSERT INTO `state` VALUES (2144,114,'Parish','Trelawny','JM-07','');
INSERT INTO `state` VALUES (2145,114,'Parish','Westmoreland','JM-10','');
INSERT INTO `state` VALUES (2146,117,'Governorate','`Ajlun','JO-AJ','');
INSERT INTO `state` VALUES (2147,117,'Governorate','Al `Aqabah','JO-AQ','');
INSERT INTO `state` VALUES (2148,117,'Governorate','Al Balq?\'','JO-BA','');
INSERT INTO `state` VALUES (2149,117,'Governorate','Al Karak','JO-KA','');
INSERT INTO `state` VALUES (2150,117,'Governorate','Al Mafraq','JO-MA','');
INSERT INTO `state` VALUES (2151,117,'Governorate','Amman','JO-AM','');
INSERT INTO `state` VALUES (2152,117,'Governorate','A? ?af?lah','JO-AT','');
INSERT INTO `state` VALUES (2153,117,'Governorate','Az Zarq?\'','JO-AZ','');
INSERT INTO `state` VALUES (2154,117,'Governorate','Irbid','JO-JR','');
INSERT INTO `state` VALUES (2155,117,'Governorate','Jarash','JO-JA','');
INSERT INTO `state` VALUES (2156,117,'Governorate','Ma`?n','JO-MN','');
INSERT INTO `state` VALUES (2157,117,'Governorate','M?dab?','JO-MD','');
INSERT INTO `state` VALUES (2158,115,'Prefecture','Aichi','JP-23','');
INSERT INTO `state` VALUES (2159,115,'Prefecture','Akita','JP-05','');
INSERT INTO `state` VALUES (2160,115,'Prefecture','Aomori','JP-02','');
INSERT INTO `state` VALUES (2161,115,'Prefecture','Chiba','JP-12','');
INSERT INTO `state` VALUES (2162,115,'Prefecture','Ehime','JP-38','');
INSERT INTO `state` VALUES (2163,115,'Prefecture','Fukui','JP-18','');
INSERT INTO `state` VALUES (2164,115,'Prefecture','Fukuoka','JP-40','');
INSERT INTO `state` VALUES (2165,115,'Prefecture','Fukushima','JP-07','');
INSERT INTO `state` VALUES (2166,115,'Prefecture','Gifu','JP-21','');
INSERT INTO `state` VALUES (2167,115,'Prefecture','Gunma','JP-10','');
INSERT INTO `state` VALUES (2168,115,'Prefecture','Hiroshima','JP-34','');
INSERT INTO `state` VALUES (2169,115,'Prefecture','Hokkaido','JP-01','');
INSERT INTO `state` VALUES (2170,115,'Prefecture','Hyogo','JP-28','');
INSERT INTO `state` VALUES (2171,115,'Prefecture','Ibaraki','JP-08','');
INSERT INTO `state` VALUES (2172,115,'Prefecture','Ishikawa','JP-17','');
INSERT INTO `state` VALUES (2173,115,'Prefecture','Iwate','JP-03','');
INSERT INTO `state` VALUES (2174,115,'Prefecture','Kagawa','JP-37','');
INSERT INTO `state` VALUES (2175,115,'Prefecture','Kagoshima','JP-46','');
INSERT INTO `state` VALUES (2176,115,'Prefecture','Kanagawa','JP-14','');
INSERT INTO `state` VALUES (2177,115,'Prefecture','Kochi','JP-39','');
INSERT INTO `state` VALUES (2178,115,'Prefecture','Kumamoto','JP-43','');
INSERT INTO `state` VALUES (2179,115,'Prefecture','Kyoto','JP-26','');
INSERT INTO `state` VALUES (2180,115,'Prefecture','Mie','JP-24','');
INSERT INTO `state` VALUES (2181,115,'Prefecture','Miyagi','JP-04','');
INSERT INTO `state` VALUES (2182,115,'Prefecture','Miyazaki','JP-45','');
INSERT INTO `state` VALUES (2183,115,'Prefecture','Nagano','JP-20','');
INSERT INTO `state` VALUES (2184,115,'Prefecture','Nagasaki','JP-42','');
INSERT INTO `state` VALUES (2185,115,'Prefecture','Nara','JP-29','');
INSERT INTO `state` VALUES (2186,115,'Prefecture','Niigata','JP-15','');
INSERT INTO `state` VALUES (2187,115,'Prefecture','Oita','JP-44','');
INSERT INTO `state` VALUES (2188,115,'Prefecture','Okayama','JP-33','');
INSERT INTO `state` VALUES (2189,115,'Prefecture','Okinawa','JP-47','');
INSERT INTO `state` VALUES (2190,115,'Prefecture','Osaka','JP-27','');
INSERT INTO `state` VALUES (2191,115,'Prefecture','Saga','JP-41','');
INSERT INTO `state` VALUES (2192,115,'Prefecture','Saitama','JP-11','');
INSERT INTO `state` VALUES (2193,115,'Prefecture','Shiga','JP-25','');
INSERT INTO `state` VALUES (2194,115,'Prefecture','Shimane','JP-32','');
INSERT INTO `state` VALUES (2195,115,'Prefecture','Shizuoka','JP-22','');
INSERT INTO `state` VALUES (2196,115,'Prefecture','Tochigi','JP-09','');
INSERT INTO `state` VALUES (2197,115,'Prefecture','Tokushima','JP-36','');
INSERT INTO `state` VALUES (2198,115,'Prefecture','Tokyo','JP-13','');
INSERT INTO `state` VALUES (2199,115,'Prefecture','Tottori','JP-31','');
INSERT INTO `state` VALUES (2200,115,'Prefecture','Toyama','JP-16','');
INSERT INTO `state` VALUES (2201,115,'Prefecture','Wakayama','JP-30','');
INSERT INTO `state` VALUES (2202,115,'Prefecture','Yamagata','JP-06','');
INSERT INTO `state` VALUES (2203,115,'Prefecture','Yamaguchi','JP-35','');
INSERT INTO `state` VALUES (2204,115,'Prefecture','Yamanashi','JP-19','');
INSERT INTO `state` VALUES (2205,119,'Province','Nairobi Municipality','KE-110','');
INSERT INTO `state` VALUES (2206,119,'Province','Central','KE-200','');
INSERT INTO `state` VALUES (2207,119,'Province','Coast','KE-300','');
INSERT INTO `state` VALUES (2208,119,'Province','Eastern','KE-400','');
INSERT INTO `state` VALUES (2209,119,'Province','North-Eastern Kaskazini Mashariki','KE-500','');
INSERT INTO `state` VALUES (2210,119,'Province','Rift Valley','KE-700','');
INSERT INTO `state` VALUES (2211,119,'Province','Western Magharibi','KE-900','');
INSERT INTO `state` VALUES (2212,124,'City','Bishkek','KG-GB','');
INSERT INTO `state` VALUES (2213,124,'Region','Batken','KG-B','');
INSERT INTO `state` VALUES (2214,124,'Region','Chü','KG-C','');
INSERT INTO `state` VALUES (2215,124,'Region','Jalal-Abad','KG-J','');
INSERT INTO `state` VALUES (2216,124,'Region','Naryn','KG-N','');
INSERT INTO `state` VALUES (2217,124,'Region','Osh','KG-O','');
INSERT INTO `state` VALUES (2218,124,'Region','Talas','KG-T','');
INSERT INTO `state` VALUES (2219,124,'Region','Ysyk-Köl','KG-Y','');
INSERT INTO `state` VALUES (2220,41,'Autonomous municipality','Krong Kaeb','KH-23','');
INSERT INTO `state` VALUES (2221,41,'Autonomous municipality','Krong Pailin','KH-24','');
INSERT INTO `state` VALUES (2222,41,'Autonomous municipality','Krong Preah Sihanouk','KH-18','');
INSERT INTO `state` VALUES (2223,41,'Autonomous municipality','Phnom Penh','KH-12','');
INSERT INTO `state` VALUES (2224,41,'Province','Battambang','KH-2','');
INSERT INTO `state` VALUES (2225,41,'Province','Banteay Mean Chey','KH-1','');
INSERT INTO `state` VALUES (2226,41,'Province','Kampong Cham','KH-3','');
INSERT INTO `state` VALUES (2227,41,'Province','Kampong Chhnang','KH-4','');
INSERT INTO `state` VALUES (2228,41,'Province','Kampong Speu','KH-5','');
INSERT INTO `state` VALUES (2229,41,'Province','Kampong Thom','KH-6','');
INSERT INTO `state` VALUES (2230,41,'Province','Kampot','KH-7','');
INSERT INTO `state` VALUES (2231,41,'Province','Kandal','KH-8','');
INSERT INTO `state` VALUES (2232,41,'Province','Kach Kong','KH-9','');
INSERT INTO `state` VALUES (2233,41,'Province','Krachoh','KH-10','');
INSERT INTO `state` VALUES (2234,41,'Province','Mondol Kiri','KH-11','');
INSERT INTO `state` VALUES (2235,41,'Province','Otdar Mean Chey','KH-22','');
INSERT INTO `state` VALUES (2236,41,'Province','Pousaat','KH-15','');
INSERT INTO `state` VALUES (2237,41,'Province','Preah Vihear','KH-13','');
INSERT INTO `state` VALUES (2238,41,'Province','Prey Veaeng','KH-14','');
INSERT INTO `state` VALUES (2239,41,'Province','Rotanak Kiri','KH-16','');
INSERT INTO `state` VALUES (2240,41,'Province','Siem Reab','KH-17','');
INSERT INTO `state` VALUES (2241,41,'Province','Stueng Traeng','KH-19','');
INSERT INTO `state` VALUES (2242,41,'Province','Svaay Rieng','KH-20','');
INSERT INTO `state` VALUES (2243,41,'Province','Taakaev','KH-21','');
INSERT INTO `state` VALUES (2244,120,'Island group','Gilbert Islands','KI-G','');
INSERT INTO `state` VALUES (2245,120,'Island group','Line Islands','KI-L','');
INSERT INTO `state` VALUES (2246,120,'Island group','Phoenix Islands','KI-P','');
INSERT INTO `state` VALUES (2247,190,'State','Saint Kitts','KN K','');
INSERT INTO `state` VALUES (2248,190,'State','Nevis','KN N','');
INSERT INTO `state` VALUES (2249,190,'Parish','Christ Church Nichola Town','KN-01','');
INSERT INTO `state` VALUES (2250,190,'Parish','Saint Anne Sandy Point','KN-02','');
INSERT INTO `state` VALUES (2251,190,'Parish','Saint George Basseterre','KN-03','');
INSERT INTO `state` VALUES (2252,190,'Parish','Saint George Gingerland','KN-04','');
INSERT INTO `state` VALUES (2253,190,'Parish','Saint James Windward','KN-05','');
INSERT INTO `state` VALUES (2254,190,'Parish','Saint John Capisterre','KN-06','');
INSERT INTO `state` VALUES (2255,190,'Parish','Saint John Figtree','KN-07','');
INSERT INTO `state` VALUES (2256,190,'Parish','Saint Mary Cayon','KN-08','');
INSERT INTO `state` VALUES (2257,190,'Parish','Saint Paul Capisterre','KN-09','');
INSERT INTO `state` VALUES (2258,190,'Parish','Saint Paul Charlestown','KN-10','');
INSERT INTO `state` VALUES (2259,190,'Parish','Saint Peter Basseterre','KN-11','');
INSERT INTO `state` VALUES (2260,190,'Parish','Saint Thomas Lowland','KN-12','');
INSERT INTO `state` VALUES (2261,190,'Parish','Saint Thomas Middle Island','KN-13','');
INSERT INTO `state` VALUES (2262,190,'Parish','Trinity Palmetto Point','KN-15','');
INSERT INTO `state` VALUES (2263,53,'Autonomous island','Anjouan Ndzouani','KM-A','');
INSERT INTO `state` VALUES (2264,53,'Autonomous island','Grande Comore Ngazidja','KM-G','');
INSERT INTO `state` VALUES (2265,53,'Autonomous island','Mohéli Moili','KM-M','');
INSERT INTO `state` VALUES (2266,121,'Province','Chagang-do','KP-CHA','');
INSERT INTO `state` VALUES (2267,121,'Province','Hamgyongbuk-do','KP-HAB','');
INSERT INTO `state` VALUES (2268,121,'Province','Hamgyongnam-do','KP-HAN','');
INSERT INTO `state` VALUES (2269,121,'Province','Hwanghaebuk-do','KP-HWB','');
INSERT INTO `state` VALUES (2270,121,'Province','Hwanghaenam-do','KP-HWN','');
INSERT INTO `state` VALUES (2271,121,'Province','Kangwon-do','KP-KAN','');
INSERT INTO `state` VALUES (2272,121,'Province','Pyonganbuk-do','KP-PYB','');
INSERT INTO `state` VALUES (2273,121,'Province','Pyongannam-do','KP-PYN','');
INSERT INTO `state` VALUES (2274,121,'Province','Yanggang-do','KP-YAN','');
INSERT INTO `state` VALUES (2275,121,'Special city','Kaesong-si','KP-KAE','');
INSERT INTO `state` VALUES (2276,121,'Special city','Najin Sonbong-si','KP-NAJ','');
INSERT INTO `state` VALUES (2277,121,'Special city','Nampo-si','KP-NAM','');
INSERT INTO `state` VALUES (2278,121,'Special city','Pyongyang-si','KP-PYO','');
INSERT INTO `state` VALUES (2279,122,'Capital Metropolitan City','Seoul Teugbyeolsi','KR-11','');
INSERT INTO `state` VALUES (2280,122,'Metropolitan cities','Busan Gwang\'yeogsi','KR-26','');
INSERT INTO `state` VALUES (2281,122,'Metropolitan cities','Daegu Gwang\'yeogsi','KR-27','');
INSERT INTO `state` VALUES (2282,122,'Metropolitan cities','Daejeon Gwang\'yeogsi','KR-30','');
INSERT INTO `state` VALUES (2283,122,'Metropolitan cities','Gwangju Gwang\'yeogsi','KR-29','');
INSERT INTO `state` VALUES (2284,122,'Metropolitan cities','Incheon Gwang\'yeogsi','KR-28','');
INSERT INTO `state` VALUES (2285,122,'Metropolitan cities','Ulsan Gwang\'yeogsi','KR-31','');
INSERT INTO `state` VALUES (2286,122,'Province','Chungcheongbukdo','KR-43','');
INSERT INTO `state` VALUES (2287,122,'Province','Chungcheongnamdo','KR-44','');
INSERT INTO `state` VALUES (2288,122,'Province','Gang\'weondo','KR-42','');
INSERT INTO `state` VALUES (2289,122,'Province','Gyeonggido','KR-41','');
INSERT INTO `state` VALUES (2290,122,'Province','Gyeongsangbukdo','KR-47','');
INSERT INTO `state` VALUES (2291,122,'Province','Gyeongsangnamdo','KR-48','');
INSERT INTO `state` VALUES (2292,122,'Province','Jejudo','KR-49','');
INSERT INTO `state` VALUES (2293,122,'Province','Jeonrabukdo','KR-45','');
INSERT INTO `state` VALUES (2294,122,'Province','Jeonranamdo','KR-46','');
INSERT INTO `state` VALUES (2295,123,'Governorate','Al Ahmadi','KW-AH','');
INSERT INTO `state` VALUES (2296,123,'Governorate','Al Farw?n?yah','KW-FA','');
INSERT INTO `state` VALUES (2297,123,'Governorate','Al Jahrah','KW-JA','');
INSERT INTO `state` VALUES (2298,123,'Governorate','Al Kuwayt','KW-KU','');
INSERT INTO `state` VALUES (2299,123,'Governorate','Hawall?','KW-HA','');
INSERT INTO `state` VALUES (2300,118,'City','Almaty','KZ-ALA','');
INSERT INTO `state` VALUES (2301,118,'City','Astana','KZ-AST','');
INSERT INTO `state` VALUES (2302,118,'Region','Almaty oblysy','KZ-ALM','');
INSERT INTO `state` VALUES (2303,118,'Region','Aqmola oblysy','KZ-AKM','');
INSERT INTO `state` VALUES (2304,118,'Region','Aqtöbe oblysy','KZ-AKT','');
INSERT INTO `state` VALUES (2305,118,'Region','Atyra? oblysy','KZ-ATY','');
INSERT INTO `state` VALUES (2306,118,'Region','Batys Quzaqstan oblysy','KZ-ZAP','');
INSERT INTO `state` VALUES (2307,118,'Region','Mangghysta? oblysy','KZ-MAN','');
INSERT INTO `state` VALUES (2308,118,'Region','Ongtüstik Qazaqstan oblysy','KZ-YUZ','');
INSERT INTO `state` VALUES (2309,118,'Region','Pavlodar oblysy','KZ-PAV','');
INSERT INTO `state` VALUES (2310,118,'Region','Qaraghandy oblysy','KZ-KAR','');
INSERT INTO `state` VALUES (2311,118,'Region','Qostanay oblysy','KZ-KUS','');
INSERT INTO `state` VALUES (2312,118,'Region','Qyzylorda oblysy','KZ-KZY','');
INSERT INTO `state` VALUES (2313,118,'Region','Shyghys Qazaqstan oblysy','KZ-VOS','');
INSERT INTO `state` VALUES (2314,118,'Region','Soltüstik Quzaqstan oblysy','KZ-SEV','');
INSERT INTO `state` VALUES (2315,118,'Region','Zhambyl oblysy','KZ-ZHA','');
INSERT INTO `state` VALUES (2316,125,'Prefecture','Vientiane','LA-VT','');
INSERT INTO `state` VALUES (2317,125,'Province','Attapu','LA-AT','');
INSERT INTO `state` VALUES (2318,125,'Province','Bokèo','LA-BK','');
INSERT INTO `state` VALUES (2319,125,'Province','Bolikhamxai','LA-BL','');
INSERT INTO `state` VALUES (2320,125,'Province','Champasak','LA-CH','');
INSERT INTO `state` VALUES (2321,125,'Province','Houaphan','LA-HO','');
INSERT INTO `state` VALUES (2322,125,'Province','Khammouan','LA-KH','');
INSERT INTO `state` VALUES (2323,125,'Province','Louang Namtha','LA-LM','');
INSERT INTO `state` VALUES (2324,125,'Province','Louangphabang','LA-LP','');
INSERT INTO `state` VALUES (2325,125,'Province','Oudômxai','LA-OU','');
INSERT INTO `state` VALUES (2326,125,'Province','Phôngsali','LA-PH','');
INSERT INTO `state` VALUES (2327,125,'Province','Salavan','LA-SL','');
INSERT INTO `state` VALUES (2328,125,'Province','Savannakhét','LA-SV','');
INSERT INTO `state` VALUES (2329,125,'Province','Vientiane','LA-VI','');
INSERT INTO `state` VALUES (2330,125,'Province','Xaignabouli','LA-XA','');
INSERT INTO `state` VALUES (2331,125,'Province','Xékong','LA-XE','');
INSERT INTO `state` VALUES (2332,125,'Province','Xiangkhoang','LA-XI','');
INSERT INTO `state` VALUES (2333,125,'Special zone','Xiasômboun','LA-XN','');
INSERT INTO `state` VALUES (2334,131,'Commune','Balzers','LI-01','');
INSERT INTO `state` VALUES (2335,131,'Commune','Eschen','LI-02','');
INSERT INTO `state` VALUES (2336,131,'Commune','Gamprin','LI-03','');
INSERT INTO `state` VALUES (2337,131,'Commune','Mauren','LI-04','');
INSERT INTO `state` VALUES (2338,131,'Commune','Planken','LI-05','');
INSERT INTO `state` VALUES (2339,131,'Commune','Ruggell','LI-06','');
INSERT INTO `state` VALUES (2340,131,'Commune','Schaan','LI-07','');
INSERT INTO `state` VALUES (2341,131,'Commune','Schellenberg','LI-08','');
INSERT INTO `state` VALUES (2342,131,'Commune','Triesen','LI-09','');
INSERT INTO `state` VALUES (2343,131,'Commune','Triesenberg','LI-10','');
INSERT INTO `state` VALUES (2344,131,'Commune','Vaduz','LI-11','');
INSERT INTO `state` VALUES (2345,127,'Governorate','Aakkâr','LB-AK','');
INSERT INTO `state` VALUES (2346,127,'Governorate','Baalbek-Hermel','LB-BH','');
INSERT INTO `state` VALUES (2347,127,'Governorate','Béqaa','LB-BI','');
INSERT INTO `state` VALUES (2348,127,'Governorate','Beyrouth','LB-BA','');
INSERT INTO `state` VALUES (2349,127,'Governorate','Liban-Nord','LB-AS','');
INSERT INTO `state` VALUES (2350,127,'Governorate','Liban-Sud','LB-JA','');
INSERT INTO `state` VALUES (2351,127,'Governorate','Mont-Liban','LB-JL','');
INSERT INTO `state` VALUES (2352,127,'Governorate','Nabatîyé','LB-NA','');
INSERT INTO `state` VALUES (2353,212,'District','Ampara','LK-52','');
INSERT INTO `state` VALUES (2354,212,'District','Anuradhapura','LK-71','');
INSERT INTO `state` VALUES (2355,212,'District','Badulla','LK-81','');
INSERT INTO `state` VALUES (2356,212,'District','Batticaloa','LK-51','');
INSERT INTO `state` VALUES (2357,212,'District','Colombo','LK-11','');
INSERT INTO `state` VALUES (2358,212,'District','Galle','LK-31','');
INSERT INTO `state` VALUES (2359,212,'District','Gampaha','LK-12','');
INSERT INTO `state` VALUES (2360,212,'District','Hambantota','LK-33','');
INSERT INTO `state` VALUES (2361,212,'District','Jaffna','LK-41','');
INSERT INTO `state` VALUES (2362,212,'District','Kalutara','LK-13','');
INSERT INTO `state` VALUES (2363,212,'District','Kandy','LK-21','');
INSERT INTO `state` VALUES (2364,212,'District','Kegalla','LK-92','');
INSERT INTO `state` VALUES (2365,212,'District','Kilinochchi','LK-42','');
INSERT INTO `state` VALUES (2366,212,'District','Kurunegala','LK-61','');
INSERT INTO `state` VALUES (2367,212,'District','Mannar','LK-43','');
INSERT INTO `state` VALUES (2368,212,'District','Matale','LK-22','');
INSERT INTO `state` VALUES (2369,212,'District','Matara','LK-32','');
INSERT INTO `state` VALUES (2370,212,'District','Monaragala','LK-82','');
INSERT INTO `state` VALUES (2371,212,'District','Mullaittivu','LK-45','');
INSERT INTO `state` VALUES (2372,212,'District','Nuwara Eliya','LK-23','');
INSERT INTO `state` VALUES (2373,212,'District','Polonnaruwa','LK-72','');
INSERT INTO `state` VALUES (2374,212,'District','Puttalum','LK-62','');
INSERT INTO `state` VALUES (2375,212,'District','Ratnapura','LK-91','');
INSERT INTO `state` VALUES (2376,212,'District','Trincomalee','LK-53','');
INSERT INTO `state` VALUES (2377,212,'District','Vavuniya','LK-44','');
INSERT INTO `state` VALUES (2378,129,'County','Bomi','LR-BM','');
INSERT INTO `state` VALUES (2379,129,'County','Bong','LR-BG','');
INSERT INTO `state` VALUES (2380,129,'County','Grand Bassa','LR-GB','');
INSERT INTO `state` VALUES (2381,129,'County','Grand Cape Mount','LR-CM','');
INSERT INTO `state` VALUES (2382,129,'County','Grand Gedeh','LR-GG','');
INSERT INTO `state` VALUES (2383,129,'County','Grand Kru','LR-GK','');
INSERT INTO `state` VALUES (2384,129,'County','Lofa','LR-LO','');
INSERT INTO `state` VALUES (2385,129,'County','Margibi','LR-MG','');
INSERT INTO `state` VALUES (2386,129,'County','Maryland','LR-MY','');
INSERT INTO `state` VALUES (2387,129,'County','Montserrado','LR-MO','');
INSERT INTO `state` VALUES (2388,129,'County','Nimba','LR-NI','');
INSERT INTO `state` VALUES (2389,129,'County','Rivercess','LR-RI','');
INSERT INTO `state` VALUES (2390,129,'County','Sinoe','LR-SI','');
INSERT INTO `state` VALUES (2391,128,'District','Berea','LS-D','');
INSERT INTO `state` VALUES (2392,128,'District','Butha-Buthe','LS-B','');
INSERT INTO `state` VALUES (2393,128,'District','Leribe','LS-C','');
INSERT INTO `state` VALUES (2394,128,'District','Mafeteng','LS-E','');
INSERT INTO `state` VALUES (2395,128,'District','Maseru','LS-A','');
INSERT INTO `state` VALUES (2396,128,'District','Mohale\'s Hoek','LS-F','');
INSERT INTO `state` VALUES (2397,128,'District','Mokhotlong','LS-J','');
INSERT INTO `state` VALUES (2398,128,'District','Qacha\'s Nek','LS-H','');
INSERT INTO `state` VALUES (2399,128,'District','Quthing','LS-G','');
INSERT INTO `state` VALUES (2400,128,'District','Thaba-Tseka','LS-K','');
INSERT INTO `state` VALUES (2401,132,'County','Alytaus Apskritis','LT-AL','');
INSERT INTO `state` VALUES (2402,132,'County','Kauno Apskritis','LT-KU','');
INSERT INTO `state` VALUES (2403,132,'County','Klaip?dos Apskritis','LT-KL','');
INSERT INTO `state` VALUES (2404,132,'County','Marijampol?s Apskritis','LT-MR','');
INSERT INTO `state` VALUES (2405,132,'County','Panev?žio Apskritis','LT-PN','');
INSERT INTO `state` VALUES (2406,132,'County','Šiauli? Apskritis','LT-SA','');
INSERT INTO `state` VALUES (2407,132,'County','Tauragés Apskritis','LT-TA','');
INSERT INTO `state` VALUES (2408,132,'County','Telši? Apskritis','LT-TE','');
INSERT INTO `state` VALUES (2409,132,'County','Utenos Apskritis','LT-UT','');
INSERT INTO `state` VALUES (2410,132,'County','Vilniaus Apskritis','LT-VL','');
INSERT INTO `state` VALUES (2411,133,'District','Diekirch','LU-D','');
INSERT INTO `state` VALUES (2412,133,'District','Grevenmacher','LU-G','');
INSERT INTO `state` VALUES (2413,133,'District','Luxembourg','LU-L','');
INSERT INTO `state` VALUES (2414,126,'District','Aizkraukle','LV-AI','');
INSERT INTO `state` VALUES (2415,126,'District','Al?ksne','LV-AL','');
INSERT INTO `state` VALUES (2416,126,'District','Balvi','LV-BL','');
INSERT INTO `state` VALUES (2417,126,'District','Bauska','LV-BU','');
INSERT INTO `state` VALUES (2418,126,'District','C?sis','LV-CE','');
INSERT INTO `state` VALUES (2419,126,'District','Daugavpils','LV-DA','');
INSERT INTO `state` VALUES (2420,126,'District','Dobele','LV-DO','');
INSERT INTO `state` VALUES (2421,126,'District','Gulbene','LV-GU','');
INSERT INTO `state` VALUES (2422,126,'District','J?kabpils','LV-JK','');
INSERT INTO `state` VALUES (2423,126,'District','Jelgava','LV-JL','');
INSERT INTO `state` VALUES (2424,126,'District','Kr?slava','LV-KR','');
INSERT INTO `state` VALUES (2425,126,'District','Kuld?ga','LV-KU','');
INSERT INTO `state` VALUES (2426,126,'District','Liep?ja','LV-LE','');
INSERT INTO `state` VALUES (2427,126,'District','Limbaži','LV-LM','');
INSERT INTO `state` VALUES (2428,126,'District','Ludza','LV-LU','');
INSERT INTO `state` VALUES (2429,126,'District','Madona','LV-MA','');
INSERT INTO `state` VALUES (2430,126,'District','Ogre','LV-OG','');
INSERT INTO `state` VALUES (2431,126,'District','Prei?i','LV-PR','');
INSERT INTO `state` VALUES (2432,126,'District','R?zekne','LV-RE','');
INSERT INTO `state` VALUES (2433,126,'District','R?ga','LV-RI','');
INSERT INTO `state` VALUES (2434,126,'District','Saldus','LV-SA','');
INSERT INTO `state` VALUES (2435,126,'District','Talsi','LV-TA','');
INSERT INTO `state` VALUES (2436,126,'District','Tukums','LV-TU','');
INSERT INTO `state` VALUES (2437,126,'District','Valka','LV-VK','');
INSERT INTO `state` VALUES (2438,126,'District','Valmiera','LV-VM','');
INSERT INTO `state` VALUES (2439,126,'District','Ventspils','LV-VE','');
INSERT INTO `state` VALUES (2440,126,'City','Daugavpils','LV-DGV','');
INSERT INTO `state` VALUES (2441,126,'City','Jelgava','LV-JEL','');
INSERT INTO `state` VALUES (2442,126,'City','J?rmala','LV-JUR','');
INSERT INTO `state` VALUES (2443,126,'City','Liep?ja','LV-LPX','');
INSERT INTO `state` VALUES (2444,126,'City','R?zekne','LV-REZ','');
INSERT INTO `state` VALUES (2445,126,'City','R?ga','LV-RIX','');
INSERT INTO `state` VALUES (2446,126,'City','Ventspils','LV-VEN','');
INSERT INTO `state` VALUES (2447,130,'Municipality','Ajd?biy?','LY-AJ','');
INSERT INTO `state` VALUES (2448,130,'Municipality','Al Bu?n?n','LY-BU','');
INSERT INTO `state` VALUES (2449,130,'Municipality','Al ?iz?m al Akh?ar','LY-HZ','');
INSERT INTO `state` VALUES (2450,130,'Municipality','Al Jabal al Akh?ar','LY-JA','');
INSERT INTO `state` VALUES (2451,130,'Municipality','Al Jif?rah','LY-JI','');
INSERT INTO `state` VALUES (2452,130,'Municipality','Al Jufrah','LY-JU','');
INSERT INTO `state` VALUES (2453,130,'Municipality','Al Kufrah','LY-KF','');
INSERT INTO `state` VALUES (2454,130,'Municipality','Al Marj','LY-MJ','');
INSERT INTO `state` VALUES (2455,130,'Municipality','Al Marqab','LY-MB','');
INSERT INTO `state` VALUES (2456,130,'Municipality','Al Qa?r?n','LY-QT','');
INSERT INTO `state` VALUES (2457,130,'Municipality','Al Qubbah','LY-QB','');
INSERT INTO `state` VALUES (2458,130,'Municipality','Al W??ah','LY-WA','');
INSERT INTO `state` VALUES (2459,130,'Municipality','An Nuqa? al Khams','LY-NQ','');
INSERT INTO `state` VALUES (2460,130,'Municipality','Ash Sh??i\'','LY-SH','');
INSERT INTO `state` VALUES (2461,130,'Municipality','Az Z?wiyah','LY-ZA','');
INSERT INTO `state` VALUES (2462,130,'Municipality','Bangh?z?','LY-BA','');
INSERT INTO `state` VALUES (2463,130,'Municipality','Ban? Wal?d','LY-BW','');
INSERT INTO `state` VALUES (2464,130,'Municipality','Darnah','LY-DR','');
INSERT INTO `state` VALUES (2465,130,'Municipality','Ghad?mis','LY-GD','');
INSERT INTO `state` VALUES (2466,130,'Municipality','Ghary?n','LY-GR','');
INSERT INTO `state` VALUES (2467,130,'Municipality','Gh?t','LY-GT','');
INSERT INTO `state` VALUES (2468,130,'Municipality','Jaghb?b','LY-JB','');
INSERT INTO `state` VALUES (2469,130,'Municipality','Mi?r?tah','LY-MI','');
INSERT INTO `state` VALUES (2470,130,'Municipality','Mizdah','LY-MZ','');
INSERT INTO `state` VALUES (2471,130,'Municipality','Murzuq','LY-MQ','');
INSERT INTO `state` VALUES (2472,130,'Municipality','N?l?t','LY-NL','');
INSERT INTO `state` VALUES (2473,130,'Municipality','Sabh?','LY-SB','');
INSERT INTO `state` VALUES (2474,130,'Municipality','?abr?tah ?urm?n','LY-SS','');
INSERT INTO `state` VALUES (2475,130,'Municipality','Surt','LY-SR','');
INSERT INTO `state` VALUES (2476,130,'Municipality','T?j?r?\' wa an Naw??? al Arb??','LY-TN','');
INSERT INTO `state` VALUES (2477,130,'Municipality','?ar?bulus','LY-TB','');
INSERT INTO `state` VALUES (2478,130,'Municipality','Tarh?nah-Masall?tah','LY-TM','');
INSERT INTO `state` VALUES (2479,130,'Municipality','W?d? al ?ay?t','LY-WD','');
INSERT INTO `state` VALUES (2480,130,'Municipality','Yafran-J?d?','LY-YJ','');
INSERT INTO `state` VALUES (2481,154,'Economic region','Chaouia-Ouardigha','MA 09','');
INSERT INTO `state` VALUES (2482,154,'Economic region','Doukhala-Abda','MA 10','');
INSERT INTO `state` VALUES (2483,154,'Economic region','Fès-Boulemane','MA 05','');
INSERT INTO `state` VALUES (2484,154,'Economic region','Gharb-Chrarda-Beni Hssen','MA 02','');
INSERT INTO `state` VALUES (2485,154,'Economic region','Grand Casablanca','MA 08','');
INSERT INTO `state` VALUES (2486,154,'Economic region','Guelmim-Es Smara','MA 14','');
INSERT INTO `state` VALUES (2487,154,'Economic region','Laâyoune-Boujdour-Sakia el Hamra','MA 15','');
INSERT INTO `state` VALUES (2488,154,'Economic region','L\'Oriental','MA 04','');
INSERT INTO `state` VALUES (2489,154,'Economic region','Marrakech-Tensift-Al Haouz','MA 11','');
INSERT INTO `state` VALUES (2490,154,'Economic region','Meknès-Tafilalet','MA 06','');
INSERT INTO `state` VALUES (2491,154,'Economic region','Oued ed Dahab-Lagouira','MA 16','');
INSERT INTO `state` VALUES (2492,154,'Economic region','Rabat-Salé-Zemmour-Zaer','MA 07','');
INSERT INTO `state` VALUES (2493,154,'Economic region','Sous-Massa-Draa','MA 13','');
INSERT INTO `state` VALUES (2494,154,'Economic region','Tadla-Azilal','MA 12','');
INSERT INTO `state` VALUES (2495,154,'Economic region','Tanger-Tétouan','MA 01','');
INSERT INTO `state` VALUES (2496,154,'Economic region','Taza-Al Hoceima-Taounate','MA 03','');
INSERT INTO `state` VALUES (2497,154,'Province','Agadir','MA-AGD','');
INSERT INTO `state` VALUES (2498,154,'Province','Aït Baha','MA-BAH','');
INSERT INTO `state` VALUES (2499,154,'Province','Aït Melloul','MA-MEL','');
INSERT INTO `state` VALUES (2500,154,'Province','Al Haouz','MA-HAO','');
INSERT INTO `state` VALUES (2501,154,'Province','Al Hoceïma','MA-HOC','');
INSERT INTO `state` VALUES (2502,154,'Province','Assa-Zag','MA-ASZ','');
INSERT INTO `state` VALUES (2503,154,'Province','Azilal','MA-AZI','');
INSERT INTO `state` VALUES (2504,154,'Province','Beni Mellal','MA-BEM','');
INSERT INTO `state` VALUES (2505,154,'Province','Ben Sllmane','MA-BES','');
INSERT INTO `state` VALUES (2506,154,'Province','Berkane','MA-BER','');
INSERT INTO `state` VALUES (2507,154,'Province','Boujdour (EH)','MA-BOD','');
INSERT INTO `state` VALUES (2508,154,'Province','Boulemane','MA-BOM','');
INSERT INTO `state` VALUES (2509,154,'Province','Casablanca [Dar el Beïda]','MA-CAS','');
INSERT INTO `state` VALUES (2510,154,'Province','Chefchaouene','MA-CHE','');
INSERT INTO `state` VALUES (2511,154,'Province','Chichaoua','MA-CHI','');
INSERT INTO `state` VALUES (2512,154,'Province','El Hajeb','MA-HAJ','');
INSERT INTO `state` VALUES (2513,154,'Province','El Jadida','MA-JDI','');
INSERT INTO `state` VALUES (2514,154,'Province','Errachidia','MA-ERR','');
INSERT INTO `state` VALUES (2515,154,'Province','Essaouira','MA-ESI','');
INSERT INTO `state` VALUES (2516,154,'Province','Es Smara (EH)','MA-ESM','');
INSERT INTO `state` VALUES (2517,154,'Province','Fès','MA-FES','');
INSERT INTO `state` VALUES (2518,154,'Province','Figuig','MA-FIG','');
INSERT INTO `state` VALUES (2519,154,'Province','Guelmim','MA-GUE','');
INSERT INTO `state` VALUES (2520,154,'Province','Ifrane','MA-IFR','');
INSERT INTO `state` VALUES (2521,154,'Province','Jerada','MA-JRA','');
INSERT INTO `state` VALUES (2522,154,'Province','Kelaat Sraghna','MA-KES','');
INSERT INTO `state` VALUES (2523,154,'Province','Kénitra','MA-KEN','');
INSERT INTO `state` VALUES (2524,154,'Province','Khemisaet','MA-KHE','');
INSERT INTO `state` VALUES (2525,154,'Province','Khenifra','MA-KHN','');
INSERT INTO `state` VALUES (2526,154,'Province','Khouribga','MA-KHO','');
INSERT INTO `state` VALUES (2527,154,'Province','Laâyoune (EH)','MA-LAA','');
INSERT INTO `state` VALUES (2528,154,'Province','Larache','MA-LAP','');
INSERT INTO `state` VALUES (2529,154,'Province','Marrakech','MA-MAR','');
INSERT INTO `state` VALUES (2530,154,'Province','Meknsès','MA-MEK','');
INSERT INTO `state` VALUES (2531,154,'Province','Nador','MA-NAD','');
INSERT INTO `state` VALUES (2532,154,'Province','Ouarzazate','MA-OUA','');
INSERT INTO `state` VALUES (2533,154,'Province','Oued ed Dahab (EH)','MA-OUD','');
INSERT INTO `state` VALUES (2534,154,'Province','Oujda','MA-OUJ','');
INSERT INTO `state` VALUES (2535,154,'Province','Rabat-Salé','MA-RBA','');
INSERT INTO `state` VALUES (2536,154,'Province','Safi','MA-SAF','');
INSERT INTO `state` VALUES (2537,154,'Province','Sefrou','MA-SEF','');
INSERT INTO `state` VALUES (2538,154,'Province','Settat','MA-SET','');
INSERT INTO `state` VALUES (2539,154,'Province','Sidl Kacem','MA-SIK','');
INSERT INTO `state` VALUES (2540,154,'Province','Tanger','MA-TNG','');
INSERT INTO `state` VALUES (2541,154,'Province','Tan-Tan','MA-TNT','');
INSERT INTO `state` VALUES (2542,154,'Province','Taounate','MA-TAO','');
INSERT INTO `state` VALUES (2543,154,'Province','Taroudannt','MA-TAR','');
INSERT INTO `state` VALUES (2544,154,'Province','Tata','MA-TAT','');
INSERT INTO `state` VALUES (2545,154,'Province','Taza','MA-TAZ','');
INSERT INTO `state` VALUES (2546,154,'Province','Tétouan','MA-TET','');
INSERT INTO `state` VALUES (2547,154,'Province','Tiznit','MA-TIZ','');
INSERT INTO `state` VALUES (2548,149,'Autonomous territory','G?g?uzia, Unitate Teritorial? Autonom?','MD-GA','');
INSERT INTO `state` VALUES (2549,149,'City','Chi?in?u','MD-CU','');
INSERT INTO `state` VALUES (2550,149,'District','B?l?i','MD-BA','');
INSERT INTO `state` VALUES (2551,149,'District','Cahul','MD-CA','');
INSERT INTO `state` VALUES (2552,149,'District','Chi?in?u','MD-CH','');
INSERT INTO `state` VALUES (2553,149,'District','Edine?','MD-ED','');
INSERT INTO `state` VALUES (2554,149,'District','L?pu?na','MD-LA','');
INSERT INTO `state` VALUES (2555,149,'District','Orhei','MD-OR','');
INSERT INTO `state` VALUES (2556,149,'District','Soroca','MD-SO','');
INSERT INTO `state` VALUES (2557,149,'District','Taraclia','MD-TA','');
INSERT INTO `state` VALUES (2558,149,'District','Tighina','MD-TI','');
INSERT INTO `state` VALUES (2559,149,'District','Ungheni','MD-UN','');
INSERT INTO `state` VALUES (2560,149,'Territorial unit','Stînga Nistrului, unitatea teritorial? din','MD-SN','');
INSERT INTO `state` VALUES (2561,152,'Municipality','Andrijevica','ME-01','');
INSERT INTO `state` VALUES (2562,152,'Municipality','Bar','ME-02','');
INSERT INTO `state` VALUES (2563,152,'Municipality','Berane','ME-03','');
INSERT INTO `state` VALUES (2564,152,'Municipality','Bijelo Polje','ME-04','');
INSERT INTO `state` VALUES (2565,152,'Municipality','Budva','ME-05','');
INSERT INTO `state` VALUES (2566,152,'Municipality','Cetinje','ME-06','');
INSERT INTO `state` VALUES (2567,152,'Municipality','Danilovgrad','ME-07','');
INSERT INTO `state` VALUES (2568,152,'Municipality','Herceg-Novi','ME-08','');
INSERT INTO `state` VALUES (2569,152,'Municipality','Kolašin','ME-09','');
INSERT INTO `state` VALUES (2570,152,'Municipality','Kotor','ME-10','');
INSERT INTO `state` VALUES (2571,152,'Municipality','Mojkovac','ME-11','');
INSERT INTO `state` VALUES (2572,152,'Municipality','Nikši?','ME-12','');
INSERT INTO `state` VALUES (2573,152,'Municipality','Plav','ME-13','');
INSERT INTO `state` VALUES (2574,152,'Municipality','Pljevlja','ME-14','');
INSERT INTO `state` VALUES (2575,152,'Municipality','Plužine','ME-15','');
INSERT INTO `state` VALUES (2576,152,'Municipality','Podgorica','ME-16','');
INSERT INTO `state` VALUES (2577,152,'Municipality','Rožaje','ME-17','');
INSERT INTO `state` VALUES (2578,152,'Municipality','Šavnik','ME-18','');
INSERT INTO `state` VALUES (2579,152,'Municipality','Tivat','ME-19','');
INSERT INTO `state` VALUES (2580,152,'Municipality','Ulcinj','ME-20','');
INSERT INTO `state` VALUES (2581,152,'Municipality','Žabljak','ME-21','');
INSERT INTO `state` VALUES (2582,136,'Autonomous province','Antananarivo','MG-T','');
INSERT INTO `state` VALUES (2583,136,'Autonomous province','Antsiranana','MG-D','');
INSERT INTO `state` VALUES (2584,136,'Autonomous province','Fianarantsoa','MG-F','');
INSERT INTO `state` VALUES (2585,136,'Autonomous province','Mahajanga','MG-M','');
INSERT INTO `state` VALUES (2586,136,'Autonomous province','Toamasina','MG-A','');
INSERT INTO `state` VALUES (2587,136,'Autonomous province','Toliara','MG-U','');
INSERT INTO `state` VALUES (2588,142,'Municipality','Ailinglapalap','MH-ALL','');
INSERT INTO `state` VALUES (2589,142,'Municipality','Ailuk','MH-ALK','');
INSERT INTO `state` VALUES (2590,142,'Municipality','Arno','MH-ARN','');
INSERT INTO `state` VALUES (2591,142,'Municipality','Aur','MH-AUR','');
INSERT INTO `state` VALUES (2592,142,'Municipality','Ebon','MH-EBO','');
INSERT INTO `state` VALUES (2593,142,'Municipality','Eniwetok','MH-ENI','');
INSERT INTO `state` VALUES (2594,142,'Municipality','Jaluit','MH-JAL','');
INSERT INTO `state` VALUES (2595,142,'Municipality','Kili','MH-KIL','');
INSERT INTO `state` VALUES (2596,142,'Municipality','Kwajalein','MH-KWA','');
INSERT INTO `state` VALUES (2597,142,'Municipality','Lae','MH-LAE','');
INSERT INTO `state` VALUES (2598,142,'Municipality','Lib','MH-LIB','');
INSERT INTO `state` VALUES (2599,142,'Municipality','Likiep','MH-LIK','');
INSERT INTO `state` VALUES (2600,142,'Municipality','Majuro','MH-MAJ','');
INSERT INTO `state` VALUES (2601,142,'Municipality','Maloelap','MH-MAL','');
INSERT INTO `state` VALUES (2602,142,'Municipality','Mejit','MH-MEJ','');
INSERT INTO `state` VALUES (2603,142,'Municipality','Mili','MH-MIL','');
INSERT INTO `state` VALUES (2604,142,'Municipality','Namorik','MH-NMK','');
INSERT INTO `state` VALUES (2605,142,'Municipality','Namu','MH-NMU','');
INSERT INTO `state` VALUES (2606,142,'Municipality','Rongelap','MH-RON','');
INSERT INTO `state` VALUES (2607,142,'Municipality','Ujae','MH-UJA','');
INSERT INTO `state` VALUES (2608,142,'Municipality','Ujelang','MH-UJL','');
INSERT INTO `state` VALUES (2609,142,'Municipality','Utirik','MH-UTI','');
INSERT INTO `state` VALUES (2610,142,'Municipality','Wotho','MH-WTN','');
INSERT INTO `state` VALUES (2611,142,'Municipality','Wotje','MH-WTJ','');
INSERT INTO `state` VALUES (2612,135,'Municipality','Aerodrom','MK-01','');
INSERT INTO `state` VALUES (2613,135,'Municipality','Ara?inovo','MK-02','');
INSERT INTO `state` VALUES (2614,135,'Municipality','Berovo','MK-03','');
INSERT INTO `state` VALUES (2615,135,'Municipality','Bitola','MK-04','');
INSERT INTO `state` VALUES (2616,135,'Municipality','Bogdanci','MK-05','');
INSERT INTO `state` VALUES (2617,135,'Municipality','Bogovinje','MK-06','');
INSERT INTO `state` VALUES (2618,135,'Municipality','Bosilovo','MK-07','');
INSERT INTO `state` VALUES (2619,135,'Municipality','Brvenica','MK-08','');
INSERT INTO `state` VALUES (2620,135,'Municipality','Butel','MK-09','');
INSERT INTO `state` VALUES (2621,135,'Municipality','Centar','MK-77','');
INSERT INTO `state` VALUES (2622,135,'Municipality','Centar Župa','MK-78','');
INSERT INTO `state` VALUES (2623,135,'Municipality','?air','MK-79','');
INSERT INTO `state` VALUES (2624,135,'Municipality','?aška','MK-80','');
INSERT INTO `state` VALUES (2625,135,'Municipality','?ešinovo-Obleševo','MK-81','');
INSERT INTO `state` VALUES (2626,135,'Municipality','?u?er Sandevo','MK-82','');
INSERT INTO `state` VALUES (2627,135,'Municipality','Debar','MK-21','');
INSERT INTO `state` VALUES (2628,135,'Municipality','Debarca','MK-22','');
INSERT INTO `state` VALUES (2629,135,'Municipality','Del?evo','MK-23','');
INSERT INTO `state` VALUES (2630,135,'Municipality','Demir Hisar','MK-25','');
INSERT INTO `state` VALUES (2631,135,'Municipality','Demir Kapija','MK-24','');
INSERT INTO `state` VALUES (2632,135,'Municipality','Dojran','MK-26','');
INSERT INTO `state` VALUES (2633,135,'Municipality','Dolneni','MK-27','');
INSERT INTO `state` VALUES (2634,135,'Municipality','Drugovo','MK-28','');
INSERT INTO `state` VALUES (2635,135,'Municipality','Gazi Baba','MK-17','');
INSERT INTO `state` VALUES (2636,135,'Municipality','Gevgelija','MK-18','');
INSERT INTO `state` VALUES (2637,135,'Municipality','Gjor?e Petrov','MK-29','');
INSERT INTO `state` VALUES (2638,135,'Municipality','Gostivar','MK-19','');
INSERT INTO `state` VALUES (2639,135,'Municipality','Gradsko','MK-20','');
INSERT INTO `state` VALUES (2640,135,'Municipality','Ilinden','MK-34','');
INSERT INTO `state` VALUES (2641,135,'Municipality','Jegunovce','MK-35','');
INSERT INTO `state` VALUES (2642,135,'Municipality','Karbinci','MK-37','');
INSERT INTO `state` VALUES (2643,135,'Municipality','Karpoš','MK-38','');
INSERT INTO `state` VALUES (2644,135,'Municipality','Kavadarci','MK-36','');
INSERT INTO `state` VALUES (2645,135,'Municipality','Ki?evo','MK-40','');
INSERT INTO `state` VALUES (2646,135,'Municipality','Kisela Voda','MK-39','');
INSERT INTO `state` VALUES (2647,135,'Municipality','Ko?ani','MK-42','');
INSERT INTO `state` VALUES (2648,135,'Municipality','Kon?e','MK-41','');
INSERT INTO `state` VALUES (2649,135,'Municipality','Kratovo','MK-43','');
INSERT INTO `state` VALUES (2650,135,'Municipality','Kriva Palanka','MK-44','');
INSERT INTO `state` VALUES (2651,135,'Municipality','Krivogaštani','MK-45','');
INSERT INTO `state` VALUES (2652,135,'Municipality','Kruševo','MK-46','');
INSERT INTO `state` VALUES (2653,135,'Municipality','Kumanovo','MK-47','');
INSERT INTO `state` VALUES (2654,135,'Municipality','Lipkovo','MK-48','');
INSERT INTO `state` VALUES (2655,135,'Municipality','Lozovo','MK-49','');
INSERT INTO `state` VALUES (2656,135,'Municipality','Makedonska Kamenica','MK-51','');
INSERT INTO `state` VALUES (2657,135,'Municipality','Makedonski Brod','MK-52','');
INSERT INTO `state` VALUES (2658,135,'Municipality','Mavrovo-i-Rostuša','MK-50','');
INSERT INTO `state` VALUES (2659,135,'Municipality','Mogila','MK-53','');
INSERT INTO `state` VALUES (2660,135,'Municipality','Negotino','MK-54','');
INSERT INTO `state` VALUES (2661,135,'Municipality','Novaci','MK-55','');
INSERT INTO `state` VALUES (2662,135,'Municipality','Novo Selo','MK-56','');
INSERT INTO `state` VALUES (2663,135,'Municipality','Ohrid','MK-58','');
INSERT INTO `state` VALUES (2664,135,'Municipality','Oslomej','MK-57','');
INSERT INTO `state` VALUES (2665,135,'Municipality','Peh?evo','MK-60','');
INSERT INTO `state` VALUES (2666,135,'Municipality','Petrovec','MK-59','');
INSERT INTO `state` VALUES (2667,135,'Municipality','Plasnica','MK-61','');
INSERT INTO `state` VALUES (2668,135,'Municipality','Prilep','MK-62','');
INSERT INTO `state` VALUES (2669,135,'Municipality','Probištip','MK-63','');
INSERT INTO `state` VALUES (2670,135,'Municipality','Radoviš','MK-64','');
INSERT INTO `state` VALUES (2671,135,'Municipality','Rankovce','MK-65','');
INSERT INTO `state` VALUES (2672,135,'Municipality','Resen','MK-66','');
INSERT INTO `state` VALUES (2673,135,'Municipality','Rosoman','MK-67','');
INSERT INTO `state` VALUES (2674,135,'Municipality','Saraj','MK-68','');
INSERT INTO `state` VALUES (2675,135,'Municipality','Štip','MK-83','');
INSERT INTO `state` VALUES (2676,135,'Municipality','Šuto Orizari','MK-84','');
INSERT INTO `state` VALUES (2677,135,'Municipality','Sopište','MK-70','');
INSERT INTO `state` VALUES (2678,135,'Municipality','Staro Nagori?ane','MK-71','');
INSERT INTO `state` VALUES (2679,135,'Municipality','Struga','MK-72','');
INSERT INTO `state` VALUES (2680,135,'Municipality','Strumica','MK-73','');
INSERT INTO `state` VALUES (2681,135,'Municipality','Studeni?ani','MK-74','');
INSERT INTO `state` VALUES (2682,135,'Municipality','Sveti Nikole','MK-69','');
INSERT INTO `state` VALUES (2683,135,'Municipality','Tearce','MK-75','');
INSERT INTO `state` VALUES (2684,135,'Municipality','Tetovo','MK-76','');
INSERT INTO `state` VALUES (2685,135,'Municipality','Valandovo','MK-10','');
INSERT INTO `state` VALUES (2686,135,'Municipality','Vasilevo','MK-11','');
INSERT INTO `state` VALUES (2687,135,'Municipality','Veles','MK-13','');
INSERT INTO `state` VALUES (2688,135,'Municipality','Vev?ani','MK-12','');
INSERT INTO `state` VALUES (2689,135,'Municipality','Vinica','MK-14','');
INSERT INTO `state` VALUES (2690,135,'Municipality','Vraneštica','MK-15','');
INSERT INTO `state` VALUES (2691,135,'Municipality','Vrap?ište','MK-16','');
INSERT INTO `state` VALUES (2692,135,'Municipality','Zajas','MK-31','');
INSERT INTO `state` VALUES (2693,135,'Municipality','Zelenikovo','MK-32','');
INSERT INTO `state` VALUES (2694,135,'Municipality','Želino','MK-30','');
INSERT INTO `state` VALUES (2695,135,'Municipality','Zrnovci','MK-33','');
INSERT INTO `state` VALUES (2696,140,'District','Bamako','ML-BK0','');
INSERT INTO `state` VALUES (2697,140,'Region','Gao','ML-7','');
INSERT INTO `state` VALUES (2698,140,'Region','Kayes','ML-1','');
INSERT INTO `state` VALUES (2699,140,'Region','Kidal','ML-8','');
INSERT INTO `state` VALUES (2700,140,'Region','Koulikoro','ML-2','');
INSERT INTO `state` VALUES (2701,140,'Region','Mopti','ML-5','');
INSERT INTO `state` VALUES (2702,140,'Region','Ségou','ML-4','');
INSERT INTO `state` VALUES (2703,140,'Region','Sikasso','ML-3','');
INSERT INTO `state` VALUES (2704,140,'Region','Tombouctou','ML-6','');
INSERT INTO `state` VALUES (2705,156,'Division','Ayeyarwady','MM-07','');
INSERT INTO `state` VALUES (2706,156,'Division','Bago','MM-02','');
INSERT INTO `state` VALUES (2707,156,'Division','Magway','MM-03','');
INSERT INTO `state` VALUES (2708,156,'Division','Mandalay','MM-04','');
INSERT INTO `state` VALUES (2709,156,'Division','Sagaing','MM-01','');
INSERT INTO `state` VALUES (2710,156,'Division','Tanintharyi','MM-05','');
INSERT INTO `state` VALUES (2711,156,'Division','Yangon','MM-06','');
INSERT INTO `state` VALUES (2712,156,'State','Chin','MM-14','');
INSERT INTO `state` VALUES (2713,156,'State','Kachin','MM-11','');
INSERT INTO `state` VALUES (2714,156,'State','Kayah','MM-12','');
INSERT INTO `state` VALUES (2715,156,'State','Kayin','MM-13','');
INSERT INTO `state` VALUES (2716,156,'State','Mon','MM-15','');
INSERT INTO `state` VALUES (2717,156,'State','Rakhine','MM-16','');
INSERT INTO `state` VALUES (2718,156,'State','Shan','MM-17','');
INSERT INTO `state` VALUES (2719,151,'Province','Arhangay','MN-073','');
INSERT INTO `state` VALUES (2720,151,'Province','Bayanhongor','MN-069','');
INSERT INTO `state` VALUES (2721,151,'Province','Bayan-Ölgiy','MN-071','');
INSERT INTO `state` VALUES (2722,151,'Province','Bulgan','MN-067','');
INSERT INTO `state` VALUES (2723,151,'Province','Dornod','MN-061','');
INSERT INTO `state` VALUES (2724,151,'Province','Dornogovi','MN-063','');
INSERT INTO `state` VALUES (2725,151,'Province','Dundgovi','MN-059','');
INSERT INTO `state` VALUES (2726,151,'Province','Dzavhan','MN-057','');
INSERT INTO `state` VALUES (2727,151,'Province','Govi-Altay','MN-065','');
INSERT INTO `state` VALUES (2728,151,'Province','Hentiy','MN-039','');
INSERT INTO `state` VALUES (2729,151,'Province','Hovd','MN-043','');
INSERT INTO `state` VALUES (2730,151,'Province','Hövsgöl','MN-041','');
INSERT INTO `state` VALUES (2731,151,'Province','Ömnögovi','MN-053','');
INSERT INTO `state` VALUES (2732,151,'Province','Övörhangay','MN-055','');
INSERT INTO `state` VALUES (2733,151,'Province','Selenge','MN-049','');
INSERT INTO `state` VALUES (2734,151,'Province','Sühbaatar','MN-051','');
INSERT INTO `state` VALUES (2735,151,'Province','Töv','MN-047','');
INSERT INTO `state` VALUES (2736,151,'Province','Uvs','MN-046','');
INSERT INTO `state` VALUES (2737,151,'Municipality','Ulanbaatar','MN-1','');
INSERT INTO `state` VALUES (2738,151,'Municipality','Darhan uul','MN-037','');
INSERT INTO `state` VALUES (2739,151,'Municipality','Govi-Sumber','MN-064','');
INSERT INTO `state` VALUES (2740,151,'Municipality','Orhon','MN-035','');
INSERT INTO `state` VALUES (2741,144,'District','Nouakchott','MR-NKC','');
INSERT INTO `state` VALUES (2742,144,'Region','Adrar','MR-07','');
INSERT INTO `state` VALUES (2743,144,'Region','Assaba','MR-03','');
INSERT INTO `state` VALUES (2744,144,'Region','Brakna','MR-05','');
INSERT INTO `state` VALUES (2745,144,'Region','Dakhlet Nouadhibou','MR-08','');
INSERT INTO `state` VALUES (2746,144,'Region','Gorgol','MR-04','');
INSERT INTO `state` VALUES (2747,144,'Region','Guidimaka','MR-10','');
INSERT INTO `state` VALUES (2748,144,'Region','Hodh ech Chargui','MR-01','');
INSERT INTO `state` VALUES (2749,144,'Region','Hodh el Charbi','MR-02','');
INSERT INTO `state` VALUES (2750,144,'Region','Inchiri','MR-12','');
INSERT INTO `state` VALUES (2751,144,'Region','Tagant','MR-09','');
INSERT INTO `state` VALUES (2752,144,'Region','Tiris Zemmour','MR-11','');
INSERT INTO `state` VALUES (2753,144,'Region','Trarza','MR-06','');
INSERT INTO `state` VALUES (2754,141,'Local council','Attard','MT-01','');
INSERT INTO `state` VALUES (2755,141,'Local council','Balzan','MT-02','');
INSERT INTO `state` VALUES (2756,141,'Local council','Birgu','MT-03','');
INSERT INTO `state` VALUES (2757,141,'Local council','Birkirkara','MT-04','');
INSERT INTO `state` VALUES (2758,141,'Local council','Bir?ebbu?a','MT-05','');
INSERT INTO `state` VALUES (2759,141,'Local council','Bormla','MT-06','');
INSERT INTO `state` VALUES (2760,141,'Local council','Dingli','MT-07','');
INSERT INTO `state` VALUES (2761,141,'Local council','Fgura','MT-08','');
INSERT INTO `state` VALUES (2762,141,'Local council','Floriana','MT-09','');
INSERT INTO `state` VALUES (2763,141,'Local council','Fontana','MT-10','');
INSERT INTO `state` VALUES (2764,141,'Local council','Gudja','MT-11','');
INSERT INTO `state` VALUES (2765,141,'Local council','G?ira','MT-12','');
INSERT INTO `state` VALUES (2766,141,'Local council','G?ajnsielem','MT-13','');
INSERT INTO `state` VALUES (2767,141,'Local council','G?arb','MT-14','');
INSERT INTO `state` VALUES (2768,141,'Local council','G?arg?ur','MT-15','');
INSERT INTO `state` VALUES (2769,141,'Local council','G?asri','MT-16','');
INSERT INTO `state` VALUES (2770,141,'Local council','G?axaq','MT-17','');
INSERT INTO `state` VALUES (2771,141,'Local council','?amrun','MT-18','');
INSERT INTO `state` VALUES (2772,141,'Local council','Iklin','MT-19','');
INSERT INTO `state` VALUES (2773,141,'Local council','Isla','MT-20','');
INSERT INTO `state` VALUES (2774,141,'Local council','Kalkara','MT-21','');
INSERT INTO `state` VALUES (2775,141,'Local council','Ker?em','MT-22','');
INSERT INTO `state` VALUES (2776,141,'Local council','Kirkop','MT-23','');
INSERT INTO `state` VALUES (2777,141,'Local council','Lija','MT-24','');
INSERT INTO `state` VALUES (2778,141,'Local council','Luqa','MT-25','');
INSERT INTO `state` VALUES (2779,141,'Local council','Marsa','MT-26','');
INSERT INTO `state` VALUES (2780,141,'Local council','Marsaskala','MT-27','');
INSERT INTO `state` VALUES (2781,141,'Local council','Marsaxlokk','MT-28','');
INSERT INTO `state` VALUES (2782,141,'Local council','Mdina','MT-29','');
INSERT INTO `state` VALUES (2783,141,'Local council','Mellie?a','MT-30','');
INSERT INTO `state` VALUES (2784,141,'Local council','M?arr','MT-31','');
INSERT INTO `state` VALUES (2785,141,'Local council','Mosta','MT-32','');
INSERT INTO `state` VALUES (2786,141,'Local council','Mqabba','MT-33','');
INSERT INTO `state` VALUES (2787,141,'Local council','Msida','MT-34','');
INSERT INTO `state` VALUES (2788,141,'Local council','Mtarfa','MT-35','');
INSERT INTO `state` VALUES (2789,141,'Local council','Munxar','MT-36','');
INSERT INTO `state` VALUES (2790,141,'Local council','Nadur','MT-37','');
INSERT INTO `state` VALUES (2791,141,'Local council','Naxxar','MT-38','');
INSERT INTO `state` VALUES (2792,141,'Local council','Paola','MT-39','');
INSERT INTO `state` VALUES (2793,141,'Local council','Pembroke','MT-40','');
INSERT INTO `state` VALUES (2794,141,'Local council','Pietà','MT-41','');
INSERT INTO `state` VALUES (2795,141,'Local council','Qala','MT-42','');
INSERT INTO `state` VALUES (2796,141,'Local council','Qormi','MT-43','');
INSERT INTO `state` VALUES (2797,141,'Local council','Qrendi','MT-44','');
INSERT INTO `state` VALUES (2798,141,'Local council','Rabat G?awdex','MT-45','');
INSERT INTO `state` VALUES (2799,141,'Local council','Rabat Malta','MT-46','');
INSERT INTO `state` VALUES (2800,141,'Local council','Safi','MT-47','');
INSERT INTO `state` VALUES (2801,141,'Local council','San ?iljan','MT-48','');
INSERT INTO `state` VALUES (2802,141,'Local council','San ?wann','MT-49','');
INSERT INTO `state` VALUES (2803,141,'Local council','San Lawrenz','MT-50','');
INSERT INTO `state` VALUES (2804,141,'Local council','San Pawl il-Ba?ar','MT-51','');
INSERT INTO `state` VALUES (2805,141,'Local council','Sannat','MT-52','');
INSERT INTO `state` VALUES (2806,141,'Local council','Santa Lu?ija','MT-53','');
INSERT INTO `state` VALUES (2807,141,'Local council','Santa Venera','MT-54','');
INSERT INTO `state` VALUES (2808,141,'Local council','Si??iewi','MT-55','');
INSERT INTO `state` VALUES (2809,141,'Local council','Sliema','MT-56','');
INSERT INTO `state` VALUES (2810,141,'Local council','Swieqi','MT-57','');
INSERT INTO `state` VALUES (2811,141,'Local council','Ta’ Xbiex','MT-58','');
INSERT INTO `state` VALUES (2812,141,'Local council','Tarxien','MT-59','');
INSERT INTO `state` VALUES (2813,141,'Local council','Valletta','MT-60','');
INSERT INTO `state` VALUES (2814,141,'Local council','Xag?ra','MT-61','');
INSERT INTO `state` VALUES (2815,141,'Local council','Xewkija','MT-62','');
INSERT INTO `state` VALUES (2816,141,'Local council','Xg?ajra','MT-63','');
INSERT INTO `state` VALUES (2817,141,'Local council','?abbar','MT-64','');
INSERT INTO `state` VALUES (2818,141,'Local council','?ebbu? G?awdex','MT-65','');
INSERT INTO `state` VALUES (2819,141,'Local council','?ebbu? Malta','MT-66','');
INSERT INTO `state` VALUES (2820,141,'Local council','?ejtun','MT-67','');
INSERT INTO `state` VALUES (2821,141,'Local council','?urrieq','MT-68','');
INSERT INTO `state` VALUES (2822,145,'City','Beau Bassin-Rose Hill','MU-BR','');
INSERT INTO `state` VALUES (2823,145,'City','Curepipe','MU-CU','');
INSERT INTO `state` VALUES (2824,145,'City','Port Louis','MU-PU','');
INSERT INTO `state` VALUES (2825,145,'City','Quatre Bornes','MU-QB','');
INSERT INTO `state` VALUES (2826,145,'City','Vacoas-Phoenix','MU-VP','');
INSERT INTO `state` VALUES (2827,145,'Dependency','Agalega Islands','MU-AG','');
INSERT INTO `state` VALUES (2828,145,'Dependency','Cargados Carajos Shoals','MU-CC','');
INSERT INTO `state` VALUES (2829,145,'Dependency','Rodrigues Island','MU-RO','');
INSERT INTO `state` VALUES (2830,145,'District','Black River','MU-BL','');
INSERT INTO `state` VALUES (2831,145,'District','Flacq','MU-FL','');
INSERT INTO `state` VALUES (2832,145,'District','Grand Port','MU-GP','');
INSERT INTO `state` VALUES (2833,145,'District','Moka','MU-MO','');
INSERT INTO `state` VALUES (2834,145,'District','Pamplemousses','MU-PA','');
INSERT INTO `state` VALUES (2835,145,'District','Plaines Wilhems','MU-PW','');
INSERT INTO `state` VALUES (2836,145,'District','Port Louis','MU-PL','');
INSERT INTO `state` VALUES (2837,145,'District','Rivière du Rempart','MU-RP','');
INSERT INTO `state` VALUES (2838,145,'District','Savanne','MU-SA','');
INSERT INTO `state` VALUES (2839,139,'City','Male','MV-MLE','');
INSERT INTO `state` VALUES (2840,139,'Atoll','Alif','MV-02','');
INSERT INTO `state` VALUES (2841,139,'Atoll','Baa','MV-20','');
INSERT INTO `state` VALUES (2842,139,'Atoll','Dhaalu','MV-17','');
INSERT INTO `state` VALUES (2843,139,'Atoll','Faafu','MV-14','');
INSERT INTO `state` VALUES (2844,139,'Atoll','Gaafu Aliff','MV-27','');
INSERT INTO `state` VALUES (2845,139,'Atoll','Gaafu Daalu','MV-28','');
INSERT INTO `state` VALUES (2846,139,'Atoll','Gnaviyani','MV-29','');
INSERT INTO `state` VALUES (2847,139,'Atoll','Haa Alif','MV-07','');
INSERT INTO `state` VALUES (2848,139,'Atoll','Haa Dhaalu','MV-23','');
INSERT INTO `state` VALUES (2849,139,'Atoll','Kaafu','MV-26','');
INSERT INTO `state` VALUES (2850,139,'Atoll','Laamu','MV-05','');
INSERT INTO `state` VALUES (2851,139,'Atoll','Lhaviyani','MV-03','');
INSERT INTO `state` VALUES (2852,139,'Atoll','Meemu','MV-12','');
INSERT INTO `state` VALUES (2853,139,'Atoll','Noonu','MV-25','');
INSERT INTO `state` VALUES (2854,139,'Atoll','Raa','MV-13','');
INSERT INTO `state` VALUES (2855,139,'Atoll','Seenu','MV-01','');
INSERT INTO `state` VALUES (2856,139,'Atoll','Shaviyani','MV-24','');
INSERT INTO `state` VALUES (2857,139,'Atoll','Thaa','MV-08','');
INSERT INTO `state` VALUES (2858,139,'Atoll','Vaavu','MV-04','');
INSERT INTO `state` VALUES (2859,137,'Region','Central','MW C','');
INSERT INTO `state` VALUES (2860,137,'Region','Northern','MW N','');
INSERT INTO `state` VALUES (2861,137,'Region','Southern (Malawi)','MW S','');
INSERT INTO `state` VALUES (2862,137,'District','Balaka','MW-BA','');
INSERT INTO `state` VALUES (2863,137,'District','Blantyre','MW-BL','');
INSERT INTO `state` VALUES (2864,137,'District','Chikwawa','MW-CK','');
INSERT INTO `state` VALUES (2865,137,'District','Chiradzulu','MW-CR','');
INSERT INTO `state` VALUES (2866,137,'District','Chitipa','MW-CT','');
INSERT INTO `state` VALUES (2867,137,'District','Dedza','MW-DE','');
INSERT INTO `state` VALUES (2868,137,'District','Dowa','MW-DO','');
INSERT INTO `state` VALUES (2869,137,'District','Karonga','MW-KR','');
INSERT INTO `state` VALUES (2870,137,'District','Kasungu','MW-KS','');
INSERT INTO `state` VALUES (2871,137,'District','Likoma Island','MW-LK','');
INSERT INTO `state` VALUES (2872,137,'District','Lilongwe','MW-LI','');
INSERT INTO `state` VALUES (2873,137,'District','Machinga','MW-MH','');
INSERT INTO `state` VALUES (2874,137,'District','Mangochi','MW-MG','');
INSERT INTO `state` VALUES (2875,137,'District','Mchinji','MW-MC','');
INSERT INTO `state` VALUES (2876,137,'District','Mulanje','MW-MU','');
INSERT INTO `state` VALUES (2877,137,'District','Mwanza','MW-MW','');
INSERT INTO `state` VALUES (2878,137,'District','Mzimba','MW-MZ','');
INSERT INTO `state` VALUES (2879,137,'District','Nkhata Bay','MW-NB','');
INSERT INTO `state` VALUES (2880,137,'District','Nkhotakota','MW-NK','');
INSERT INTO `state` VALUES (2881,137,'District','Nsanje','MW-NS','');
INSERT INTO `state` VALUES (2882,137,'District','Ntcheu','MW-NU','');
INSERT INTO `state` VALUES (2883,137,'District','Ntchisi','MW-NI','');
INSERT INTO `state` VALUES (2884,137,'District','Phalombe','MW-PH','');
INSERT INTO `state` VALUES (2885,137,'District','Rumphi','MW-RU','');
INSERT INTO `state` VALUES (2886,137,'District','Salima','MW-SA','');
INSERT INTO `state` VALUES (2887,137,'District','Thyolo','MW-TH','');
INSERT INTO `state` VALUES (2888,137,'District','Zomba','MW-ZO','');
INSERT INTO `state` VALUES (2889,147,'Federal district','Distrito Federal','MX-DIF','');
INSERT INTO `state` VALUES (2890,147,'State','Aguascalientes','MX-AGU','');
INSERT INTO `state` VALUES (2891,147,'State','Baja California','MX-BCN','');
INSERT INTO `state` VALUES (2892,147,'State','Baja California Sur','MX-BCS','');
INSERT INTO `state` VALUES (2893,147,'State','Campeche','MX-CAM','');
INSERT INTO `state` VALUES (2894,147,'State','Coahuila','MX-COA','');
INSERT INTO `state` VALUES (2895,147,'State','Colima','MX-COL','');
INSERT INTO `state` VALUES (2896,147,'State','Chiapas','MX-CHP','');
INSERT INTO `state` VALUES (2897,147,'State','Chihuahua','MX-CHH','');
INSERT INTO `state` VALUES (2898,147,'State','Durango','MX-DUR','');
INSERT INTO `state` VALUES (2899,147,'State','Guanajuato','MX-GUA','');
INSERT INTO `state` VALUES (2900,147,'State','Guerrero','MX-GRO','');
INSERT INTO `state` VALUES (2901,147,'State','Hidalgo','MX-HID','');
INSERT INTO `state` VALUES (2902,147,'State','Jalisco','MX-JAL','');
INSERT INTO `state` VALUES (2903,147,'State','México','MX-MEX','');
INSERT INTO `state` VALUES (2904,147,'State','Michoacán','MX-MIC','');
INSERT INTO `state` VALUES (2905,147,'State','Morelos','MX-MOR','');
INSERT INTO `state` VALUES (2906,147,'State','Nayarit','MX-NAY','');
INSERT INTO `state` VALUES (2907,147,'State','Nuevo León','MX-NLE','');
INSERT INTO `state` VALUES (2908,147,'State','Oaxaca','MX-OAX','');
INSERT INTO `state` VALUES (2909,147,'State','Puebla','MX-PUE','');
INSERT INTO `state` VALUES (2910,147,'State','Querétaro','MX-QUE','');
INSERT INTO `state` VALUES (2911,147,'State','Quintana Roo','MX-ROO','');
INSERT INTO `state` VALUES (2912,147,'State','San Luis Potosí','MX-SLP','');
INSERT INTO `state` VALUES (2913,147,'State','Sinaloa','MX-SIN','');
INSERT INTO `state` VALUES (2914,147,'State','Sonora','MX-SON','');
INSERT INTO `state` VALUES (2915,147,'State','Tabasco','MX-TAB','');
INSERT INTO `state` VALUES (2916,147,'State','Tamaulipas','MX-TAM','');
INSERT INTO `state` VALUES (2917,147,'State','Tlaxcala','MX-TLA','');
INSERT INTO `state` VALUES (2918,147,'State','Veracruz','MX-VER','');
INSERT INTO `state` VALUES (2919,147,'State','Yucatán','MX-YUC','');
INSERT INTO `state` VALUES (2920,147,'State','Zacatecas','MX-ZAC','');
INSERT INTO `state` VALUES (2921,138,'Federal Territories','Wilayah Persekutuan Kuala Lumpur','MY-14','');
INSERT INTO `state` VALUES (2922,138,'Federal Territories','Wilayah Persekutuan Labuan','MY-15','');
INSERT INTO `state` VALUES (2923,138,'Federal Territories','Wilayah Persekutuan Putrajaya','MY-16','');
INSERT INTO `state` VALUES (2924,138,'State','Johor','MY-01','');
INSERT INTO `state` VALUES (2925,138,'State','Kedah','MY-02','');
INSERT INTO `state` VALUES (2926,138,'State','Kelantan','MY-03','');
INSERT INTO `state` VALUES (2927,138,'State','Melaka','MY-04','');
INSERT INTO `state` VALUES (2928,138,'State','Negeri Sembilan','MY-05','');
INSERT INTO `state` VALUES (2929,138,'State','Pahang','MY-06','');
INSERT INTO `state` VALUES (2930,138,'State','Perak','MY-08','');
INSERT INTO `state` VALUES (2931,138,'State','Perlis','MY-09','');
INSERT INTO `state` VALUES (2932,138,'State','Pulau Pinang','MY-07','');
INSERT INTO `state` VALUES (2933,138,'State','Sabah','MY-12','');
INSERT INTO `state` VALUES (2934,138,'State','Sarawak','MY-13','');
INSERT INTO `state` VALUES (2935,138,'State','Selangor','MY-10','');
INSERT INTO `state` VALUES (2936,138,'State','Terengganu','MY-11','');
INSERT INTO `state` VALUES (2937,155,'City','Maputo (city)','MZ-MPM','');
INSERT INTO `state` VALUES (2938,155,'Province','Cabo Delgado','MZ-P','');
INSERT INTO `state` VALUES (2939,155,'Province','Gaza','MZ-G','');
INSERT INTO `state` VALUES (2940,155,'Province','Inhambane','MZ-I','');
INSERT INTO `state` VALUES (2941,155,'Province','Manica','MZ-B','');
INSERT INTO `state` VALUES (2942,155,'Province','Maputo','MZ-L','');
INSERT INTO `state` VALUES (2943,155,'Province','Numpula','MZ-N','');
INSERT INTO `state` VALUES (2944,155,'Province','Niassa','MZ-A','');
INSERT INTO `state` VALUES (2945,155,'Province','Sofala','MZ-S','');
INSERT INTO `state` VALUES (2946,155,'Province','Tete','MZ-T','');
INSERT INTO `state` VALUES (2947,155,'Province','Zambezia','MZ-Q','');
INSERT INTO `state` VALUES (2948,157,'Region','Caprivi','NA-CA','');
INSERT INTO `state` VALUES (2949,157,'Region','Erongo','NA-ER','');
INSERT INTO `state` VALUES (2950,157,'Region','Hardap','NA-HA','');
INSERT INTO `state` VALUES (2951,157,'Region','Karas','NA-KA','');
INSERT INTO `state` VALUES (2952,157,'Region','Khomas','NA-KH','');
INSERT INTO `state` VALUES (2953,157,'Region','Kunene','NA-KU','');
INSERT INTO `state` VALUES (2954,157,'Region','Ohangwena','NA-OW','');
INSERT INTO `state` VALUES (2955,157,'Region','Okavango','NA-OK','');
INSERT INTO `state` VALUES (2956,157,'Region','Omaheke','NA-OH','');
INSERT INTO `state` VALUES (2957,157,'Region','Omusati','NA-OS','');
INSERT INTO `state` VALUES (2958,157,'Region','Oshana','NA-ON','');
INSERT INTO `state` VALUES (2959,157,'Region','Oshikoto','NA-OT','');
INSERT INTO `state` VALUES (2960,157,'Region','Otjozondjupa','NA-OD','');
INSERT INTO `state` VALUES (2961,164,'Capital District','Niamey','NE-8','');
INSERT INTO `state` VALUES (2962,164,'Department','Agadez','NE-1','');
INSERT INTO `state` VALUES (2963,164,'Department','Diffa','NE-2','');
INSERT INTO `state` VALUES (2964,164,'Department','Dosso','NE-3','');
INSERT INTO `state` VALUES (2965,164,'Department','Maradi','NE-4','');
INSERT INTO `state` VALUES (2966,164,'Department','Tahoua','NE-5','');
INSERT INTO `state` VALUES (2967,164,'Department','Tillabéri','NE-6','');
INSERT INTO `state` VALUES (2968,164,'Department','Zinder','NE-7','');
INSERT INTO `state` VALUES (2969,165,'Capital Territory','Abuja Capital Territory','NG-FC','');
INSERT INTO `state` VALUES (2970,165,'State','Abia','NG-AB','');
INSERT INTO `state` VALUES (2971,165,'State','Adamawa','NG-AD','');
INSERT INTO `state` VALUES (2972,165,'State','Akwa Ibom','NG-AK','');
INSERT INTO `state` VALUES (2973,165,'State','Anambra','NG-AN','');
INSERT INTO `state` VALUES (2974,165,'State','Bauchi','NG-BA','');
INSERT INTO `state` VALUES (2975,165,'State','Bayelsa','NG-BY','');
INSERT INTO `state` VALUES (2976,165,'State','Benue','NG-BE','');
INSERT INTO `state` VALUES (2977,165,'State','Borno','NG-BO','');
INSERT INTO `state` VALUES (2978,165,'State','Cross River','NG-CR','');
INSERT INTO `state` VALUES (2979,165,'State','Delta','NG-DE','');
INSERT INTO `state` VALUES (2980,165,'State','Ebonyi','NG-EB','');
INSERT INTO `state` VALUES (2981,165,'State','Edo','NG-ED','');
INSERT INTO `state` VALUES (2982,165,'State','Ekiti','NG-EK','');
INSERT INTO `state` VALUES (2983,165,'State','Enugu','NG-EN','');
INSERT INTO `state` VALUES (2984,165,'State','Gombe','NG-GO','');
INSERT INTO `state` VALUES (2985,165,'State','Imo','NG-IM','');
INSERT INTO `state` VALUES (2986,165,'State','Jigawa','NG-JI','');
INSERT INTO `state` VALUES (2987,165,'State','Kaduna','NG-KD','');
INSERT INTO `state` VALUES (2988,165,'State','Kano','NG-KN','');
INSERT INTO `state` VALUES (2989,165,'State','Katsina','NG-KT','');
INSERT INTO `state` VALUES (2990,165,'State','Kebbi','NG-KE','');
INSERT INTO `state` VALUES (2991,165,'State','Kogi','NG-KO','');
INSERT INTO `state` VALUES (2992,165,'State','Kwara','NG-KW','');
INSERT INTO `state` VALUES (2993,165,'State','Lagos','NG-LA','');
INSERT INTO `state` VALUES (2994,165,'State','Nassarawa','NG-NA','');
INSERT INTO `state` VALUES (2995,165,'State','Niger','NG-NI','');
INSERT INTO `state` VALUES (2996,165,'State','Ogun','NG-OG','');
INSERT INTO `state` VALUES (2997,165,'State','Ondo','NG-ON','');
INSERT INTO `state` VALUES (2998,165,'State','Osun','NG-OS','');
INSERT INTO `state` VALUES (2999,165,'State','Oyo','NG-OY','');
INSERT INTO `state` VALUES (3000,165,'State','Plateau','NG-PL','');
INSERT INTO `state` VALUES (3001,165,'State','Rivers','NG-RI','');
INSERT INTO `state` VALUES (3002,165,'State','Sokoto','NG-SO','');
INSERT INTO `state` VALUES (3003,165,'State','Taraba','NG-TA','');
INSERT INTO `state` VALUES (3004,165,'State','Yobe','NG-YO','');
INSERT INTO `state` VALUES (3005,165,'State','Zamfara','NG-ZA','');
INSERT INTO `state` VALUES (3006,163,'Department','Boaco','NI-BO','');
INSERT INTO `state` VALUES (3007,163,'Department','Carazo','NI-CA','');
INSERT INTO `state` VALUES (3008,163,'Department','Chinandega','NI-CI','');
INSERT INTO `state` VALUES (3009,163,'Department','Chontales','NI-CO','');
INSERT INTO `state` VALUES (3010,163,'Department','Estelí','NI-ES','');
INSERT INTO `state` VALUES (3011,163,'Department','Granada','NI-GR','');
INSERT INTO `state` VALUES (3012,163,'Department','Jinotega','NI-JI','');
INSERT INTO `state` VALUES (3013,163,'Department','León','NI-LE','');
INSERT INTO `state` VALUES (3014,163,'Department','Madriz','NI-MD','');
INSERT INTO `state` VALUES (3015,163,'Department','Managua','NI-MN','');
INSERT INTO `state` VALUES (3016,163,'Department','Masaya','NI-MS','');
INSERT INTO `state` VALUES (3017,163,'Department','Matagalpa','NI-MT','');
INSERT INTO `state` VALUES (3018,163,'Department','Nueva Segovia','NI-NS','');
INSERT INTO `state` VALUES (3019,163,'Department','Río San Juan','NI-SJ','');
INSERT INTO `state` VALUES (3020,163,'Department','Rivas','NI-RI','');
INSERT INTO `state` VALUES (3021,163,'Autonomous Region','Atlántico Norte','NI-AN','');
INSERT INTO `state` VALUES (3022,163,'Autonomous Region','Atlántico Sur','NI-AS','');
INSERT INTO `state` VALUES (3023,160,'Province','Drenthe','NL-DR','');
INSERT INTO `state` VALUES (3024,160,'Province','Flevoland','NL-FL','');
INSERT INTO `state` VALUES (3025,160,'Province','Friesland','NL-FR','');
INSERT INTO `state` VALUES (3026,160,'Province','Gelderland','NL-GE','');
INSERT INTO `state` VALUES (3027,160,'Province','Groningen','NL-GR','');
INSERT INTO `state` VALUES (3028,160,'Province','Limburg','NL-LI','');
INSERT INTO `state` VALUES (3029,160,'Province','Noord-Brabant','NL-NB','');
INSERT INTO `state` VALUES (3030,160,'Province','Noord-Holland','NL-NH','');
INSERT INTO `state` VALUES (3031,160,'Province','Overijssel','NL-OV','');
INSERT INTO `state` VALUES (3032,160,'Province','Utrecht','NL-UT','');
INSERT INTO `state` VALUES (3033,160,'Province','Zeeland','NL-ZE','');
INSERT INTO `state` VALUES (3034,160,'Province','Zuid-Holland','NL-ZH','');
INSERT INTO `state` VALUES (3035,169,'County','Akershus','NO-02','');
INSERT INTO `state` VALUES (3036,169,'County','Aust-Agder','NO-09','');
INSERT INTO `state` VALUES (3037,169,'County','Buskerud','NO-06','');
INSERT INTO `state` VALUES (3038,169,'County','Finnmark','NO-20','');
INSERT INTO `state` VALUES (3039,169,'County','Hedmark','NO-04','');
INSERT INTO `state` VALUES (3040,169,'County','Hordaland','NO-12','');
INSERT INTO `state` VALUES (3041,169,'County','Møre og Romsdal','NO-15','');
INSERT INTO `state` VALUES (3042,169,'County','Nordland','NO-18','');
INSERT INTO `state` VALUES (3043,169,'County','Nord-Trøndelag','NO-17','');
INSERT INTO `state` VALUES (3044,169,'County','Oppland','NO-05','');
INSERT INTO `state` VALUES (3045,169,'County','Oslo','NO-03','');
INSERT INTO `state` VALUES (3046,169,'County','Rogaland','NO-11','');
INSERT INTO `state` VALUES (3047,169,'County','Sogn og Fjordane','NO-14','');
INSERT INTO `state` VALUES (3048,169,'County','Sør-Trøndelag','NO-16','');
INSERT INTO `state` VALUES (3049,169,'County','Telemark','NO-08','');
INSERT INTO `state` VALUES (3050,169,'County','Troms','NO-19','');
INSERT INTO `state` VALUES (3051,169,'County','Vest-Agder','NO-10','');
INSERT INTO `state` VALUES (3052,169,'County','Vestfold','NO-07','');
INSERT INTO `state` VALUES (3053,169,'County','Østfold','NO-01','');
INSERT INTO `state` VALUES (3054,169,'County','Jan Mayen','NO-22','');
INSERT INTO `state` VALUES (3055,169,'County','Svalbard','NO-21','');
INSERT INTO `state` VALUES (3056,158,'District','Aiwo','NR-01','');
INSERT INTO `state` VALUES (3057,158,'District','Anabar','NR-02','');
INSERT INTO `state` VALUES (3058,158,'District','Anetan','NR-03','');
INSERT INTO `state` VALUES (3059,158,'District','Anibare','NR-04','');
INSERT INTO `state` VALUES (3060,158,'District','Baiti','NR-05','');
INSERT INTO `state` VALUES (3061,158,'District','Boe','NR-06','');
INSERT INTO `state` VALUES (3062,158,'District','Buada','NR-07','');
INSERT INTO `state` VALUES (3063,158,'District','Denigomodu','NR-08','');
INSERT INTO `state` VALUES (3064,158,'District','Ewa','NR-09','');
INSERT INTO `state` VALUES (3065,158,'District','Ijuw','NR-10','');
INSERT INTO `state` VALUES (3066,158,'District','Meneng','NR-11','');
INSERT INTO `state` VALUES (3067,158,'District','Nibok','NR-12','');
INSERT INTO `state` VALUES (3068,158,'District','Uaboe','NR-13','');
INSERT INTO `state` VALUES (3069,158,'District','Yaren','NR-14','');
INSERT INTO `state` VALUES (3070,162,'Regional council','Auckland','NZ-AUK','');
INSERT INTO `state` VALUES (3071,162,'Regional council','Bay of Plenty','NZ-BOP','');
INSERT INTO `state` VALUES (3072,162,'Regional council','Canterbury','NZ-CAN','');
INSERT INTO `state` VALUES (3073,162,'Regional council','Hawkes Bay','NZ-HKB','');
INSERT INTO `state` VALUES (3074,162,'Regional council','Manawatu-Wanganui','NZ-MWT','');
INSERT INTO `state` VALUES (3075,162,'Regional council','Northland','NZ-NTL','');
INSERT INTO `state` VALUES (3076,162,'Regional council','Otago','NZ-OTA','');
INSERT INTO `state` VALUES (3077,162,'Regional council','Southland','NZ-STL','');
INSERT INTO `state` VALUES (3078,162,'Regional council','Taranaki','NZ-TKI','');
INSERT INTO `state` VALUES (3079,162,'Regional council','Waikato','NZ-WKO','');
INSERT INTO `state` VALUES (3080,162,'Regional council','Wellington','NZ-WGN','');
INSERT INTO `state` VALUES (3081,162,'Regional council','West Coast','NZ-WTC','');
INSERT INTO `state` VALUES (3082,162,'Unitary authority','Gisborne','NZ-GIS','');
INSERT INTO `state` VALUES (3083,162,'Unitary authority','Marlborough','NZ-MBH','');
INSERT INTO `state` VALUES (3084,162,'Unitary authority','Nelson','NZ-NSN','');
INSERT INTO `state` VALUES (3085,162,'Unitary authority','Tasman','NZ-TAS','');
INSERT INTO `state` VALUES (3086,170,'Region','Ad Dakhillyah','OM-DA','');
INSERT INTO `state` VALUES (3087,170,'Region','Al Batinah','OM-BA','');
INSERT INTO `state` VALUES (3088,170,'Region','Al Wusta','OM-WU','');
INSERT INTO `state` VALUES (3089,170,'Region','Ash Sharqlyah','OM-SH','');
INSERT INTO `state` VALUES (3090,170,'Region','Az Zahirah','OM-ZA','');
INSERT INTO `state` VALUES (3091,170,'Governorate','Al Janblyah','OM-JA','');
INSERT INTO `state` VALUES (3092,170,'Governorate','Masqat','OM-MA','');
INSERT INTO `state` VALUES (3093,170,'Governorate','Musandam','OM-MU','');
INSERT INTO `state` VALUES (3094,174,'Province','Bocas del Toro','PA-1','');
INSERT INTO `state` VALUES (3095,174,'Province','Coclé','PA-2','');
INSERT INTO `state` VALUES (3096,174,'Province','Colón','PA-3','');
INSERT INTO `state` VALUES (3097,174,'Province','Chiriquí','PA-4','');
INSERT INTO `state` VALUES (3098,174,'Province','Darién','PA-5','');
INSERT INTO `state` VALUES (3099,174,'Province','Herrera','PA-6','');
INSERT INTO `state` VALUES (3100,174,'Province','Los Santos','PA-7','');
INSERT INTO `state` VALUES (3101,174,'Province','Panamá','PA-8','');
INSERT INTO `state` VALUES (3102,174,'Province','Veraguas','PA-9','');
INSERT INTO `state` VALUES (3103,174,'Province','Kuna Yala','PA-0','');
INSERT INTO `state` VALUES (3104,177,'Region','El Callao','PE-CAL','');
INSERT INTO `state` VALUES (3105,177,'Region','Amazonas','PE-AMA','');
INSERT INTO `state` VALUES (3106,177,'Region','Ancash','PE-ANC','');
INSERT INTO `state` VALUES (3107,177,'Region','Apurímac','PE-APU','');
INSERT INTO `state` VALUES (3108,177,'Region','Arequipa','PE-ARE','');
INSERT INTO `state` VALUES (3109,177,'Region','Ayacucho','PE-AYA','');
INSERT INTO `state` VALUES (3110,177,'Region','Cajamarca','PE-CAJ','');
INSERT INTO `state` VALUES (3111,177,'Region','Cusco','PE-CUS','');
INSERT INTO `state` VALUES (3112,177,'Region','Huancavelica','PE-HUV','');
INSERT INTO `state` VALUES (3113,177,'Region','Huánuco','PE-HUC','');
INSERT INTO `state` VALUES (3114,177,'Region','Ica','PE-ICA','');
INSERT INTO `state` VALUES (3115,177,'Region','Junín','PE-JUN','');
INSERT INTO `state` VALUES (3116,177,'Region','La Libertad','PE-LAL','');
INSERT INTO `state` VALUES (3117,177,'Region','Lambayeque','PE-LAM','');
INSERT INTO `state` VALUES (3118,177,'Region','Lima','PE-LIM','');
INSERT INTO `state` VALUES (3119,177,'Region','Loreto','PE-LOR','');
INSERT INTO `state` VALUES (3120,177,'Region','Madre de Dios','PE-MDD','');
INSERT INTO `state` VALUES (3121,177,'Region','Moquegua','PE-MOQ','');
INSERT INTO `state` VALUES (3122,177,'Region','Pasco','PE-PAS','');
INSERT INTO `state` VALUES (3123,177,'Region','Piura','PE-PIU','');
INSERT INTO `state` VALUES (3124,177,'Region','Puno','PE-PUN','');
INSERT INTO `state` VALUES (3125,177,'Region','San Martín','PE-SAM','');
INSERT INTO `state` VALUES (3126,177,'Region','Tacna','PE-TAC','');
INSERT INTO `state` VALUES (3127,177,'Region','Tumbes','PE-TUM','');
INSERT INTO `state` VALUES (3128,177,'Region','Ucayali','PE-UCA','');
INSERT INTO `state` VALUES (3129,175,'District','National Capital District (Port Moresby)','PG-NCD','');
INSERT INTO `state` VALUES (3130,175,'Province','Central','PG-CPM','');
INSERT INTO `state` VALUES (3131,175,'Province','Chimbu','PG-CPK','');
INSERT INTO `state` VALUES (3132,175,'Province','Eastern Highlands','PG-EHG','');
INSERT INTO `state` VALUES (3133,175,'Province','East New Britain','PG-EBR','');
INSERT INTO `state` VALUES (3134,175,'Province','East Sepik','PG-ESW','');
INSERT INTO `state` VALUES (3135,175,'Province','Enga','PG-EPW','');
INSERT INTO `state` VALUES (3136,175,'Province','Gulf','PG-GPK','');
INSERT INTO `state` VALUES (3137,175,'Province','Madang','PG-MPM','');
INSERT INTO `state` VALUES (3138,175,'Province','Manus','PG-MRL','');
INSERT INTO `state` VALUES (3139,175,'Province','Milne Bay','PG-MBA','');
INSERT INTO `state` VALUES (3140,175,'Province','Morobe','PG-MPL','');
INSERT INTO `state` VALUES (3141,175,'Province','New Ireland','PG-NIK','');
INSERT INTO `state` VALUES (3142,175,'Province','Northern','PG-NPP','');
INSERT INTO `state` VALUES (3143,175,'Province','North Solomons','PG-NSA','');
INSERT INTO `state` VALUES (3144,175,'Province','Sandaun','PG-SAN','');
INSERT INTO `state` VALUES (3145,175,'Province','Southern Highlands','PG-SHM','');
INSERT INTO `state` VALUES (3146,175,'Province','Western','PG-WPD','');
INSERT INTO `state` VALUES (3147,175,'Province','Western Highlands','PG-WHM','');
INSERT INTO `state` VALUES (3148,175,'Province','West New Britain','PG-WBK','');
INSERT INTO `state` VALUES (3149,178,'Region','Autonomous Region in Muslim Mindanao (ARMM)','PH 14','');
INSERT INTO `state` VALUES (3150,178,'Region','Bicol','PH 05','');
INSERT INTO `state` VALUES (3151,178,'Region','Cagayan Valley','PH 02','');
INSERT INTO `state` VALUES (3152,178,'Region','CARAGA','PH 13','');
INSERT INTO `state` VALUES (3153,178,'Region','Central Luzon','PH 03','');
INSERT INTO `state` VALUES (3154,178,'Region','Central Mindanao','PH 12','');
INSERT INTO `state` VALUES (3155,178,'Region','Central Visayas','PH 07','');
INSERT INTO `state` VALUES (3156,178,'Region','Cordillera Administrative Region (CAR)','PH 15','');
INSERT INTO `state` VALUES (3157,178,'Region','Eastern Visayas','PH 08','');
INSERT INTO `state` VALUES (3158,178,'Region','Ilocos','PH 01','');
INSERT INTO `state` VALUES (3159,178,'Region','National Capital Region (Manila)','PH 00','');
INSERT INTO `state` VALUES (3160,178,'Region','Northern Mindanao','PH 10','');
INSERT INTO `state` VALUES (3161,178,'Region','Southern Mindanao','PH 11','');
INSERT INTO `state` VALUES (3162,178,'Region','Southern Tagalog','PH 04','');
INSERT INTO `state` VALUES (3163,178,'Region','Western Mindanao','PH 09','');
INSERT INTO `state` VALUES (3164,178,'Region','Western Visayas','PH 06','');
INSERT INTO `state` VALUES (3165,178,'Province','Abra','PH-ABR','');
INSERT INTO `state` VALUES (3166,178,'Province','Agusan del Norte','PH-AGN','');
INSERT INTO `state` VALUES (3167,178,'Province','Agusan del Sur','PH-AGS','');
INSERT INTO `state` VALUES (3168,178,'Province','Aklan','PH-AKL','');
INSERT INTO `state` VALUES (3169,178,'Province','Albay','PH-ALB','');
INSERT INTO `state` VALUES (3170,178,'Province','Antique','PH-ANT','');
INSERT INTO `state` VALUES (3171,178,'Province','Apayao','PH-APA','');
INSERT INTO `state` VALUES (3172,178,'Province','Aurora','PH-AUR','');
INSERT INTO `state` VALUES (3173,178,'Province','Basilan','PH-BAS','');
INSERT INTO `state` VALUES (3174,178,'Province','Batasn','PH-BAN','');
INSERT INTO `state` VALUES (3175,178,'Province','Batanes','PH-BTN','');
INSERT INTO `state` VALUES (3176,178,'Province','Batangas','PH-BTG','');
INSERT INTO `state` VALUES (3177,178,'Province','Benguet','PH-BEN','');
INSERT INTO `state` VALUES (3178,178,'Province','Biliran','PH-BIL','');
INSERT INTO `state` VALUES (3179,178,'Province','Bohol','PH-BOH','');
INSERT INTO `state` VALUES (3180,178,'Province','Bukidnon','PH-BUK','');
INSERT INTO `state` VALUES (3181,178,'Province','Bulacan','PH-BUL','');
INSERT INTO `state` VALUES (3182,178,'Province','Cagayan','PH-CAG','');
INSERT INTO `state` VALUES (3183,178,'Province','Camarines Norte','PH-CAN','');
INSERT INTO `state` VALUES (3184,178,'Province','Camarines Sur','PH-CAS','');
INSERT INTO `state` VALUES (3185,178,'Province','Camiguin','PH-CAM','');
INSERT INTO `state` VALUES (3186,178,'Province','Capiz','PH-CAP','');
INSERT INTO `state` VALUES (3187,178,'Province','Catanduanes','PH-CAT','');
INSERT INTO `state` VALUES (3188,178,'Province','Cavite','PH-CAV','');
INSERT INTO `state` VALUES (3189,178,'Province','Cebu','PH-CEB','');
INSERT INTO `state` VALUES (3190,178,'Province','Compostela Valley','PH-COM','');
INSERT INTO `state` VALUES (3191,178,'Province','Davao del Norte','PH-DAV','');
INSERT INTO `state` VALUES (3192,178,'Province','Davao del Sur','PH-DAS','');
INSERT INTO `state` VALUES (3193,178,'Province','Davao Oriental','PH-DAO','');
INSERT INTO `state` VALUES (3194,178,'Province','Eastern Samar','PH-EAS','');
INSERT INTO `state` VALUES (3195,178,'Province','Guimaras','PH-GUI','');
INSERT INTO `state` VALUES (3196,178,'Province','Ifugao','PH-IFU','');
INSERT INTO `state` VALUES (3197,178,'Province','Ilocos Norte','PH-ILN','');
INSERT INTO `state` VALUES (3198,178,'Province','Ilocos Sur','PH-ILS','');
INSERT INTO `state` VALUES (3199,178,'Province','Iloilo','PH-ILI','');
INSERT INTO `state` VALUES (3200,178,'Province','Isabela','PH-ISA','');
INSERT INTO `state` VALUES (3201,178,'Province','Kalinga-Apayso','PH-KAL','');
INSERT INTO `state` VALUES (3202,178,'Province','Laguna','PH-LAG','');
INSERT INTO `state` VALUES (3203,178,'Province','Lanao del Norte','PH-LAN','');
INSERT INTO `state` VALUES (3204,178,'Province','Lanao del Sur','PH-LAS','');
INSERT INTO `state` VALUES (3205,178,'Province','La Union','PH-LUN','');
INSERT INTO `state` VALUES (3206,178,'Province','Leyte','PH-LEY','');
INSERT INTO `state` VALUES (3207,178,'Province','Maguindanao','PH-MAG','');
INSERT INTO `state` VALUES (3208,178,'Province','Marinduque','PH-MAD','');
INSERT INTO `state` VALUES (3209,178,'Province','Masbate','PH-MAS','');
INSERT INTO `state` VALUES (3210,178,'Province','Mindoro Occidental','PH-MDC','');
INSERT INTO `state` VALUES (3211,178,'Province','Mindoro Oriental','PH-MDR','');
INSERT INTO `state` VALUES (3212,178,'Province','Misamis Occidental','PH-MSC','');
INSERT INTO `state` VALUES (3213,178,'Province','Misamis Oriental','PH-MSR','');
INSERT INTO `state` VALUES (3214,178,'Province','Mountain Province','PH-MOU','');
INSERT INTO `state` VALUES (3215,178,'Province','Negroe Occidental','PH-NEC','');
INSERT INTO `state` VALUES (3216,178,'Province','Negros Oriental','PH-NER','');
INSERT INTO `state` VALUES (3217,178,'Province','North Cotabato','PH-NCO','');
INSERT INTO `state` VALUES (3218,178,'Province','Northern Samar','PH-NSA','');
INSERT INTO `state` VALUES (3219,178,'Province','Nueva Ecija','PH-NUE','');
INSERT INTO `state` VALUES (3220,178,'Province','Nueva Vizcaya','PH-NUV','');
INSERT INTO `state` VALUES (3221,178,'Province','Palawan','PH-PLW','');
INSERT INTO `state` VALUES (3222,178,'Province','Pampanga','PH-PAM','');
INSERT INTO `state` VALUES (3223,178,'Province','Pangasinan','PH-PAN','');
INSERT INTO `state` VALUES (3224,178,'Province','Quezon','PH-QUE','');
INSERT INTO `state` VALUES (3225,178,'Province','Quirino','PH-QUI','');
INSERT INTO `state` VALUES (3226,178,'Province','Rizal','PH-RIZ','');
INSERT INTO `state` VALUES (3227,178,'Province','Romblon','PH-ROM','');
INSERT INTO `state` VALUES (3228,178,'Province','Sarangani','PH-SAR','');
INSERT INTO `state` VALUES (3229,178,'Province','Siquijor','PH-SIG','');
INSERT INTO `state` VALUES (3230,178,'Province','Sorsogon','PH-SOR','');
INSERT INTO `state` VALUES (3231,178,'Province','South Cotabato','PH-SCO','');
INSERT INTO `state` VALUES (3232,178,'Province','Southern Leyte','PH-SLE','');
INSERT INTO `state` VALUES (3233,178,'Province','Sultan Kudarat','PH-SUK','');
INSERT INTO `state` VALUES (3234,178,'Province','Sulu','PH-SLU','');
INSERT INTO `state` VALUES (3235,178,'Province','Surigao del Norte','PH-SUN','');
INSERT INTO `state` VALUES (3236,178,'Province','Surigao del Sur','PH-SUR','');
INSERT INTO `state` VALUES (3237,178,'Province','Tarlac','PH-TAR','');
INSERT INTO `state` VALUES (3238,178,'Province','Tawi-Tawi','PH-TAW','');
INSERT INTO `state` VALUES (3239,178,'Province','Western Samar','PH-WSA','');
INSERT INTO `state` VALUES (3240,178,'Province','Zambales','PH-ZMB','');
INSERT INTO `state` VALUES (3241,178,'Province','Zamboanga del Norte','PH-ZAN','');
INSERT INTO `state` VALUES (3242,178,'Province','Zamboanga del Sur','PH-ZAS','');
INSERT INTO `state` VALUES (3243,178,'Province','Zamboanga Sibiguey','PH-ZSI','');
INSERT INTO `state` VALUES (3244,171,'Capital territory','Islamabad','PK-IS','');
INSERT INTO `state` VALUES (3245,171,'Province','Balochistan','PK-BA','');
INSERT INTO `state` VALUES (3246,171,'Province','North-West Frontier','PK-NW','');
INSERT INTO `state` VALUES (3247,171,'Province','Punjab','PK-PB','');
INSERT INTO `state` VALUES (3248,171,'Province','Sindh','PK-SD','');
INSERT INTO `state` VALUES (3249,171,'Area','Federally Administered Tribal Areas','PK-TA','');
INSERT INTO `state` VALUES (3250,171,'Area','Azad Rashmir','PK-JK','');
INSERT INTO `state` VALUES (3251,171,'Area','Northern Areas','PK-NA','');
INSERT INTO `state` VALUES (3252,180,'Province','Dolno?l?skie','PL-DS','');
INSERT INTO `state` VALUES (3253,180,'Province','Kujawsko-pomorskie','PL-KP','');
INSERT INTO `state` VALUES (3254,180,'Province','Lubelskie','PL-LU','');
INSERT INTO `state` VALUES (3255,180,'Province','Lubuskie','PL-LB','');
INSERT INTO `state` VALUES (3256,180,'Province','?ódzkie','PL-LD','');
INSERT INTO `state` VALUES (3257,180,'Province','Ma?opolskie','PL-MA','');
INSERT INTO `state` VALUES (3258,180,'Province','Mazowieckie','PL-MZ','');
INSERT INTO `state` VALUES (3259,180,'Province','Opolskie','PL-OP','');
INSERT INTO `state` VALUES (3260,180,'Province','Podkarpackie','PL-PK','');
INSERT INTO `state` VALUES (3261,180,'Province','Podlaskie','PL-PD','');
INSERT INTO `state` VALUES (3262,180,'Province','Pomorskie','PL-PM','');
INSERT INTO `state` VALUES (3263,180,'Province','?l?skie','PL-SL','');
INSERT INTO `state` VALUES (3264,180,'Province','?wi?tokrzyskie','PL-SK','');
INSERT INTO `state` VALUES (3265,180,'Province','Warmi?sko-mazurskie','PL-WN','');
INSERT INTO `state` VALUES (3266,180,'Province','Wielkopolskie','PL-WP','');
INSERT INTO `state` VALUES (3267,180,'Province','Zachodniopomorskie','PL-ZP','');
INSERT INTO `state` VALUES (3268,181,'District','Aveiro','PT-01','');
INSERT INTO `state` VALUES (3269,181,'District','Beja','PT-02','');
INSERT INTO `state` VALUES (3270,181,'District','Braga','PT-03','');
INSERT INTO `state` VALUES (3271,181,'District','Bragança','PT-04','');
INSERT INTO `state` VALUES (3272,181,'District','Castelo Branco','PT-05','');
INSERT INTO `state` VALUES (3273,181,'District','Coimbra','PT-06','');
INSERT INTO `state` VALUES (3274,181,'District','Évora','PT-07','');
INSERT INTO `state` VALUES (3275,181,'District','Faro','PT-08','');
INSERT INTO `state` VALUES (3276,181,'District','Guarda','PT-09','');
INSERT INTO `state` VALUES (3277,181,'District','Leiria','PT-10','');
INSERT INTO `state` VALUES (3278,181,'District','Lisboa','PT-11','');
INSERT INTO `state` VALUES (3279,181,'District','Portalegre','PT-12','');
INSERT INTO `state` VALUES (3280,181,'District','Porto','PT-13','');
INSERT INTO `state` VALUES (3281,181,'District','Santarém','PT-14','');
INSERT INTO `state` VALUES (3282,181,'District','Setúbal','PT-15','');
INSERT INTO `state` VALUES (3283,181,'District','Viana do Castelo','PT-16','');
INSERT INTO `state` VALUES (3284,181,'District','Vila Real','PT-17','');
INSERT INTO `state` VALUES (3285,181,'District','Viseu','PT-18','');
INSERT INTO `state` VALUES (3286,181,'Autonomous region','Região Autónoma dos Açores','PT-20','');
INSERT INTO `state` VALUES (3287,181,'Autonomous region','Região Autónoma da Madeira','PT-30','');
INSERT INTO `state` VALUES (3288,172,'State','Aimeliik','PW-002','');
INSERT INTO `state` VALUES (3289,172,'State','Airai','PW-004','');
INSERT INTO `state` VALUES (3290,172,'State','Angaur','PW-010','');
INSERT INTO `state` VALUES (3291,172,'State','Hatobohei','PW-050','');
INSERT INTO `state` VALUES (3292,172,'State','Kayangel','PW-100','');
INSERT INTO `state` VALUES (3293,172,'State','Koror','PW-150','');
INSERT INTO `state` VALUES (3294,172,'State','Melekeok','PW-212','');
INSERT INTO `state` VALUES (3295,172,'State','Ngaraard','PW-214','');
INSERT INTO `state` VALUES (3296,172,'State','Ngarchelong','PW-218','');
INSERT INTO `state` VALUES (3297,172,'State','Ngardmau','PW-222','');
INSERT INTO `state` VALUES (3298,172,'State','Ngatpang','PW-224','');
INSERT INTO `state` VALUES (3299,172,'State','Ngchesar','PW-226','');
INSERT INTO `state` VALUES (3300,172,'State','Ngeremlengui','PW-227','');
INSERT INTO `state` VALUES (3301,172,'State','Ngiwal','PW-228','');
INSERT INTO `state` VALUES (3302,172,'State','Peleliu','PW-350','');
INSERT INTO `state` VALUES (3303,172,'State','Sonsorol','PW-370','');
INSERT INTO `state` VALUES (3304,176,'Capital district','Asunción','PY-ASU','');
INSERT INTO `state` VALUES (3305,176,'Department','Alto Paraguay','PY-16','');
INSERT INTO `state` VALUES (3306,176,'Department','Alto Paraná','PY-10','');
INSERT INTO `state` VALUES (3307,176,'Department','Amambay','PY-13','');
INSERT INTO `state` VALUES (3308,176,'Department','Boquerón','PY-19','');
INSERT INTO `state` VALUES (3309,176,'Department','Caaguazú','PY-5','');
INSERT INTO `state` VALUES (3310,176,'Department','Caazapá','PY-6','');
INSERT INTO `state` VALUES (3311,176,'Department','Canindeyú','PY-14','');
INSERT INTO `state` VALUES (3312,176,'Department','Central','PY-11','');
INSERT INTO `state` VALUES (3313,176,'Department','Concepción','PY-1','');
INSERT INTO `state` VALUES (3314,176,'Department','Cordillera','PY-3','');
INSERT INTO `state` VALUES (3315,176,'Department','Guairá','PY-4','');
INSERT INTO `state` VALUES (3316,176,'Department','Itapúa','PY-7','');
INSERT INTO `state` VALUES (3317,176,'Department','Misiones','PY-8','');
INSERT INTO `state` VALUES (3318,176,'Department','Ñeembucú','PY-12','');
INSERT INTO `state` VALUES (3319,176,'Department','Paraguarí','PY-9','');
INSERT INTO `state` VALUES (3320,176,'Department','Presidente Hayes','PY-15','');
INSERT INTO `state` VALUES (3321,176,'Department','San Pedro','PY-2','');
INSERT INTO `state` VALUES (3322,183,'Municipality','Ad Dawhah','QA-DA','');
INSERT INTO `state` VALUES (3323,183,'Municipality','Al Ghuwayriyah','QA-GH','');
INSERT INTO `state` VALUES (3324,183,'Municipality','Al Jumayliyah','QA-JU','');
INSERT INTO `state` VALUES (3325,183,'Municipality','Al Khawr','QA-KH','');
INSERT INTO `state` VALUES (3326,183,'Municipality','Al Wakrah','QA-WA','');
INSERT INTO `state` VALUES (3327,183,'Municipality','Ar Rayyan','QA-RA','');
INSERT INTO `state` VALUES (3328,183,'Municipality','Jariyan al Batnah','QA-JB','');
INSERT INTO `state` VALUES (3329,183,'Municipality','Madinat ash Shamal','QA-MS','');
INSERT INTO `state` VALUES (3330,183,'Municipality','Umm Salal','QA-US','');
INSERT INTO `state` VALUES (3331,185,'Department','Alba','RO-AB','');
INSERT INTO `state` VALUES (3332,185,'Department','Arad','RO-AR','');
INSERT INTO `state` VALUES (3333,185,'Department','Arge?','RO-AG','');
INSERT INTO `state` VALUES (3334,185,'Department','Bac?u','RO-BC','');
INSERT INTO `state` VALUES (3335,185,'Department','Bihor','RO-BH','');
INSERT INTO `state` VALUES (3336,185,'Department','Bistri?a-N?s?ud','RO-BN','');
INSERT INTO `state` VALUES (3337,185,'Department','Boto?ani','RO-BT','');
INSERT INTO `state` VALUES (3338,185,'Department','Bra?ov','RO-BV','');
INSERT INTO `state` VALUES (3339,185,'Department','Br?ila','RO-BR','');
INSERT INTO `state` VALUES (3340,185,'Department','Buz?u','RO-BZ','');
INSERT INTO `state` VALUES (3341,185,'Department','Cara?-Severin','RO-CS','');
INSERT INTO `state` VALUES (3342,185,'Department','C?l?ra?i','RO-CL','');
INSERT INTO `state` VALUES (3343,185,'Department','Cluj','RO-CJ','');
INSERT INTO `state` VALUES (3344,185,'Department','Constan?a','RO-CT','');
INSERT INTO `state` VALUES (3345,185,'Department','Covasna','RO-CV','');
INSERT INTO `state` VALUES (3346,185,'Department','Dâmbovi?a','RO-DB','');
INSERT INTO `state` VALUES (3347,185,'Department','Dolj','RO-DJ','');
INSERT INTO `state` VALUES (3348,185,'Department','Gala?i','RO-GL','');
INSERT INTO `state` VALUES (3349,185,'Department','Giurgiu','RO-GR','');
INSERT INTO `state` VALUES (3350,185,'Department','Gorj','RO-GJ','');
INSERT INTO `state` VALUES (3351,185,'Department','Harghita','RO-HR','');
INSERT INTO `state` VALUES (3352,185,'Department','Hunedoara','RO-HD','');
INSERT INTO `state` VALUES (3353,185,'Department','Ialomi?a','RO-IL','');
INSERT INTO `state` VALUES (3354,185,'Department','Ia?i','RO-IS','');
INSERT INTO `state` VALUES (3355,185,'Department','Ilfov','RO-IF','');
INSERT INTO `state` VALUES (3356,185,'Department','Maramure?','RO-MM','');
INSERT INTO `state` VALUES (3357,185,'Department','Mehedin?i','RO-MH','');
INSERT INTO `state` VALUES (3358,185,'Department','Mure?','RO-MS','');
INSERT INTO `state` VALUES (3359,185,'Department','Neam?','RO-NT','');
INSERT INTO `state` VALUES (3360,185,'Department','Olt','RO-OT','');
INSERT INTO `state` VALUES (3361,185,'Department','Prahova','RO-PH','');
INSERT INTO `state` VALUES (3362,185,'Department','Satu Mare','RO-SM','');
INSERT INTO `state` VALUES (3363,185,'Department','S?laj','RO-SJ','');
INSERT INTO `state` VALUES (3364,185,'Department','Sibiu','RO-SB','');
INSERT INTO `state` VALUES (3365,185,'Department','Suceava','RO-SV','');
INSERT INTO `state` VALUES (3366,185,'Department','Teleorman','RO-TR','');
INSERT INTO `state` VALUES (3367,185,'Department','Timi?','RO-TM','');
INSERT INTO `state` VALUES (3368,185,'Department','Tulcea','RO-TL','');
INSERT INTO `state` VALUES (3369,185,'Department','Vaslui','RO-VS','');
INSERT INTO `state` VALUES (3370,185,'Department','Vâlcea','RO-VL','');
INSERT INTO `state` VALUES (3371,185,'Department','Vrancea','RO-VN','');
INSERT INTO `state` VALUES (3372,185,'Municipality','Bucure?ti','RO-B','');
INSERT INTO `state` VALUES (3373,200,'City','Beograd','RS-00','');
INSERT INTO `state` VALUES (3374,200,'Autonomous province','Kosovo-Metohija','RS KM','');
INSERT INTO `state` VALUES (3375,200,'Autonomous province','Vojvodina','RS VO','');
INSERT INTO `state` VALUES (3376,200,'District','Severna Ba?ka','RS-01','');
INSERT INTO `state` VALUES (3377,200,'District','Južna Ba?ka','RS-06','');
INSERT INTO `state` VALUES (3378,200,'District','Zapadna Ba?ka','RS-05','');
INSERT INTO `state` VALUES (3379,200,'District','Severni Banat','RS-03','');
INSERT INTO `state` VALUES (3380,200,'District','Srednji Banat','RS-02','');
INSERT INTO `state` VALUES (3381,200,'District','Južni Banat','RS-04','');
INSERT INTO `state` VALUES (3382,200,'District','Bor','RS-14','');
INSERT INTO `state` VALUES (3383,200,'District','Brani?evo','RS-11','');
INSERT INTO `state` VALUES (3384,200,'District','Jablanica','RS-23','');
INSERT INTO `state` VALUES (3385,200,'District','Kolubara','RS-09','');
INSERT INTO `state` VALUES (3386,200,'District','Kosovo','RS-25','');
INSERT INTO `state` VALUES (3387,200,'District','Kosovska Mitrovica','RS-28','');
INSERT INTO `state` VALUES (3388,200,'District','Kosovo-Pomoravlje','RS-29','');
INSERT INTO `state` VALUES (3389,200,'District','Ma?va','RS-08','');
INSERT INTO `state` VALUES (3390,200,'District','Moravica','RS-17','');
INSERT INTO `state` VALUES (3391,200,'District','Nišava','RS-20','');
INSERT INTO `state` VALUES (3392,200,'District','P?inja','RS-24','');
INSERT INTO `state` VALUES (3393,200,'District','Pe?','RS-26','');
INSERT INTO `state` VALUES (3394,200,'District','Pirot','RS-22','');
INSERT INTO `state` VALUES (3395,200,'District','Podunavlje','RS-10','');
INSERT INTO `state` VALUES (3396,200,'District','Pomoravlje','RS-13','');
INSERT INTO `state` VALUES (3397,200,'District','Prizren','RS-27','');
INSERT INTO `state` VALUES (3398,200,'District','Rasina','RS-19','');
INSERT INTO `state` VALUES (3399,200,'District','Raška','RS-18','');
INSERT INTO `state` VALUES (3400,200,'District','Srem','RS-07','');
INSERT INTO `state` VALUES (3401,200,'District','Šumadija','RS-12','');
INSERT INTO `state` VALUES (3402,200,'District','Toplica','RS-21','');
INSERT INTO `state` VALUES (3403,200,'District','Zaje?ar','RS-15','');
INSERT INTO `state` VALUES (3404,200,'District','Zlatibor','RS-16','');
INSERT INTO `state` VALUES (3405,186,'Republic','Adygeya, Respublika','RU-AD','');
INSERT INTO `state` VALUES (3406,186,'Republic','Altay, Respublika','RU-AL','');
INSERT INTO `state` VALUES (3407,186,'Republic','Bashkortostan, Respublika','RU-BA','');
INSERT INTO `state` VALUES (3408,186,'Republic','Buryatiya, Respublika','RU-BU','');
INSERT INTO `state` VALUES (3409,186,'Republic','Chechenskaya Respublika','RU-CE','');
INSERT INTO `state` VALUES (3410,186,'Republic','Chuvashskaya Respublika','RU-CU','');
INSERT INTO `state` VALUES (3411,186,'Republic','Dagestan, Respublika','RU-DA','');
INSERT INTO `state` VALUES (3412,186,'Republic','Respublika Ingushetiya','RU-IN','');
INSERT INTO `state` VALUES (3413,186,'Republic','Kabardino-Balkarskaya Respublika','RU-KB','');
INSERT INTO `state` VALUES (3414,186,'Republic','Kalmykiya, Respublika','RU-KL','');
INSERT INTO `state` VALUES (3415,186,'Republic','Karachayevo-Cherkesskaya Respublika','RU-KC','');
INSERT INTO `state` VALUES (3416,186,'Republic','Kareliya, Respublika','RU-KR','');
INSERT INTO `state` VALUES (3417,186,'Republic','Khakasiya, Respublika','RU-KK','');
INSERT INTO `state` VALUES (3418,186,'Republic','Komi, Respublika','RU-KO','');
INSERT INTO `state` VALUES (3419,186,'Republic','Mariy El, Respublika','RU-ME','');
INSERT INTO `state` VALUES (3420,186,'Republic','Mordoviya, Respublika','RU-MO','');
INSERT INTO `state` VALUES (3421,186,'Republic','Sakha, Respublika [Yakutiya]','RU-SA','');
INSERT INTO `state` VALUES (3422,186,'Republic','Severnaya Osetiya-Alaniya, Respublika','RU-SE','');
INSERT INTO `state` VALUES (3423,186,'Republic','Tatarstan, Respublika','RU-TA','');
INSERT INTO `state` VALUES (3424,186,'Republic','Tyva, Respublika [Tuva]','RU-TY','');
INSERT INTO `state` VALUES (3425,186,'Republic','Udmurtskaya Respublika','RU-UD','');
INSERT INTO `state` VALUES (3426,186,'Administrative Territory','Altayskiy kray','RU-ALT','');
INSERT INTO `state` VALUES (3427,186,'Administrative Territory','Kamchatskiy kray','RU-KAM','');
INSERT INTO `state` VALUES (3428,186,'Administrative Territory','Khabarovskiy kray','RU-KHA','');
INSERT INTO `state` VALUES (3429,186,'Administrative Territory','Krasnodarskiy kray','RU-KDA','');
INSERT INTO `state` VALUES (3430,186,'Administrative Territory','Krasnoyarskiy kray','RU-KYA','');
INSERT INTO `state` VALUES (3431,186,'Administrative Territory','Permskiy kray','RU-PER','');
INSERT INTO `state` VALUES (3432,186,'Administrative Territory','Primorskiy kray','RU-PRI','');
INSERT INTO `state` VALUES (3433,186,'Administrative Territory','Stavropol\'skiy kray','RU-STA','');
INSERT INTO `state` VALUES (3434,186,'Administrative Region','Amurskaya oblast\'','RU-AMU','');
INSERT INTO `state` VALUES (3435,186,'Administrative Region','Arkhangel\'skaya oblast\'','RU-ARK','');
INSERT INTO `state` VALUES (3436,186,'Administrative Region','Astrakhanskaya oblast\'','RU-AST','');
INSERT INTO `state` VALUES (3437,186,'Administrative Region','Belgorodskaya oblast\'','RU-BEL','');
INSERT INTO `state` VALUES (3438,186,'Administrative Region','Bryanskaya oblast\'','RU-BRY','');
INSERT INTO `state` VALUES (3439,186,'Administrative Region','Chelyabinskaya oblast\'','RU-CHE','');
INSERT INTO `state` VALUES (3440,186,'Administrative Region','Chitinskaya oblast\'','RU-CHI','');
INSERT INTO `state` VALUES (3441,186,'Administrative Region','Irkutiskaya oblast\'','RU-IRK','');
INSERT INTO `state` VALUES (3442,186,'Administrative Region','Ivanovskaya oblast\'','RU-IVA','');
INSERT INTO `state` VALUES (3443,186,'Administrative Region','Kaliningradskaya oblast\'','RU-KGD','');
INSERT INTO `state` VALUES (3444,186,'Administrative Region','Kaluzhskaya oblast\'','RU-KLU','');
INSERT INTO `state` VALUES (3445,186,'Administrative Region','Kemerovskaya oblast\'','RU-KEM','');
INSERT INTO `state` VALUES (3446,186,'Administrative Region','Kirovskaya oblast\'','RU-KIR','');
INSERT INTO `state` VALUES (3447,186,'Administrative Region','Kostromskaya oblast\'','RU-KOS','');
INSERT INTO `state` VALUES (3448,186,'Administrative Region','Kurganskaya oblast\'','RU-KGN','');
INSERT INTO `state` VALUES (3449,186,'Administrative Region','Kurskaya oblast\'','RU-KRS','');
INSERT INTO `state` VALUES (3450,186,'Administrative Region','Leningradskaya oblast\'','RU-LEN','');
INSERT INTO `state` VALUES (3451,186,'Administrative Region','Lipetskaya oblast\'','RU-LIP','');
INSERT INTO `state` VALUES (3452,186,'Administrative Region','Magadanskaya oblast\'','RU-MAG','');
INSERT INTO `state` VALUES (3453,186,'Administrative Region','Moskovskaya oblast\'','RU-MOS','');
INSERT INTO `state` VALUES (3454,186,'Administrative Region','Murmanskaya oblast\'','RU-MUR','');
INSERT INTO `state` VALUES (3455,186,'Administrative Region','Nizhegorodskaya oblast\'','RU-NIZ','');
INSERT INTO `state` VALUES (3456,186,'Administrative Region','Novgorodskaya oblast\'','RU-NGR','');
INSERT INTO `state` VALUES (3457,186,'Administrative Region','Novosibirskaya oblast\'','RU-NVS','');
INSERT INTO `state` VALUES (3458,186,'Administrative Region','Omskaya oblast\'','RU-OMS','');
INSERT INTO `state` VALUES (3459,186,'Administrative Region','Orenburgskaya oblast\'','RU-ORE','');
INSERT INTO `state` VALUES (3460,186,'Administrative Region','Orlovskaya oblast\'','RU-ORL','');
INSERT INTO `state` VALUES (3461,186,'Administrative Region','Penzenskaya oblast\'','RU-PNZ','');
INSERT INTO `state` VALUES (3462,186,'Administrative Region','Pskovskaya oblast\'','RU-PSK','');
INSERT INTO `state` VALUES (3463,186,'Administrative Region','Rostovskaya oblast\'','RU-ROS','');
INSERT INTO `state` VALUES (3464,186,'Administrative Region','Ryazanskaya oblast\'','RU-RYA','');
INSERT INTO `state` VALUES (3465,186,'Administrative Region','Sakhalinskaya oblast\'','RU-SAK','');
INSERT INTO `state` VALUES (3466,186,'Administrative Region','Samaraskaya oblast\'','RU-SAM','');
INSERT INTO `state` VALUES (3467,186,'Administrative Region','Saratovskaya oblast\'','RU-SAR','');
INSERT INTO `state` VALUES (3468,186,'Administrative Region','Smolenskaya oblast\'','RU-SMO','');
INSERT INTO `state` VALUES (3469,186,'Administrative Region','Sverdlovskaya oblast\'','RU-SVE','');
INSERT INTO `state` VALUES (3470,186,'Administrative Region','Tambovskaya oblast\'','RU-TAM','');
INSERT INTO `state` VALUES (3471,186,'Administrative Region','Tomskaya oblast\'','RU-TOM','');
INSERT INTO `state` VALUES (3472,186,'Administrative Region','Tul\'skaya oblast\'','RU-TUL','');
INSERT INTO `state` VALUES (3473,186,'Administrative Region','Tverskaya oblast\'','RU-TVE','');
INSERT INTO `state` VALUES (3474,186,'Administrative Region','Tyumenskaya oblast\'','RU-TYU','');
INSERT INTO `state` VALUES (3475,186,'Administrative Region','Ul\'yanovskaya oblast\'','RU-ULY','');
INSERT INTO `state` VALUES (3476,186,'Administrative Region','Vladimirskaya oblast\'','RU-VLA','');
INSERT INTO `state` VALUES (3477,186,'Administrative Region','Volgogradskaya oblast\'','RU-VGG','');
INSERT INTO `state` VALUES (3478,186,'Administrative Region','Vologodskaya oblast\'','RU-VLG','');
INSERT INTO `state` VALUES (3479,186,'Administrative Region','Voronezhskaya oblast\'','RU-VOR','');
INSERT INTO `state` VALUES (3480,186,'Administrative Region','Yaroslavskaya oblast\'','RU-YAR','');
INSERT INTO `state` VALUES (3481,186,'Autonomous City','Moskva','RU-MOW','');
INSERT INTO `state` VALUES (3482,186,'Autonomous City','Sankt-Peterburg','RU-SPE','');
INSERT INTO `state` VALUES (3483,186,'Autonomous Region','Yevreyskaya avtonomnaya oblast\'','RU-YEV','');
INSERT INTO `state` VALUES (3484,186,'Autonomous District','Aginsky Buryatskiy avtonomnyy okrug','RU-AGB','');
INSERT INTO `state` VALUES (3485,186,'Autonomous District','Chukotskiy avtonomnyy okrug','RU-CHU','');
INSERT INTO `state` VALUES (3486,186,'Autonomous District','Khanty-Mansiysky avtonomnyy okrug-Yugra','RU-KHM','');
INSERT INTO `state` VALUES (3487,186,'Autonomous District','Nenetskiy avtonomnyy okrug','RU-NEN','');
INSERT INTO `state` VALUES (3488,186,'Autonomous District','Ust\'-Ordynskiy Buryatskiy avtonomnyy okrug','RU-UOB','');
INSERT INTO `state` VALUES (3489,186,'Autonomous District','Yamalo-Nenetskiy avtonomnyy okrug','RU-YAN','');
INSERT INTO `state` VALUES (3490,187,'Town council','Ville de Kigali','RW-01','');
INSERT INTO `state` VALUES (3491,187,'Province','Est','RW-02','');
INSERT INTO `state` VALUES (3492,187,'Province','Nord','RW-03','');
INSERT INTO `state` VALUES (3493,187,'Province','Ouest','RW-04','');
INSERT INTO `state` VALUES (3494,187,'Province','Sud','RW-05','');
INSERT INTO `state` VALUES (3495,198,'Province','Al B?hah','SA-11','');
INSERT INTO `state` VALUES (3496,198,'Province','Al ?ud?d ash Sham?liyah','SA-08','');
INSERT INTO `state` VALUES (3497,198,'Province','Al Jawf','SA-12','');
INSERT INTO `state` VALUES (3498,198,'Province','Al Mad?nah','SA-03','');
INSERT INTO `state` VALUES (3499,198,'Province','Al Qa??m','SA-05','');
INSERT INTO `state` VALUES (3500,198,'Province','Ar Riy??','SA-01','');
INSERT INTO `state` VALUES (3501,198,'Province','Ash Sharq?yah','SA-04','');
INSERT INTO `state` VALUES (3502,198,'Province','`As?r','SA-14','');
INSERT INTO `state` VALUES (3503,198,'Province','??\'il','SA-06','');
INSERT INTO `state` VALUES (3504,198,'Province','J?zan','SA-09','');
INSERT INTO `state` VALUES (3505,198,'Province','Makkah','SA-02','');
INSERT INTO `state` VALUES (3506,198,'Province','Najr?n','SA-10','');
INSERT INTO `state` VALUES (3507,198,'Province','Tab?k','SA-07','');
INSERT INTO `state` VALUES (3508,207,'Capital territory','Capital Territory (Honiara)','SB-CT','');
INSERT INTO `state` VALUES (3509,207,'Province','Central','SB-CE','');
INSERT INTO `state` VALUES (3510,207,'Province','Choiseul','SB-CH','');
INSERT INTO `state` VALUES (3511,207,'Province','Guadalcanal','SB-GU','');
INSERT INTO `state` VALUES (3512,207,'Province','Isabel','SB-IS','');
INSERT INTO `state` VALUES (3513,207,'Province','Makira','SB-MK','');
INSERT INTO `state` VALUES (3514,207,'Province','Malaita','SB-ML','');
INSERT INTO `state` VALUES (3515,207,'Province','Rennell and Bellona','SB-RB','');
INSERT INTO `state` VALUES (3516,207,'Province','Temotu','SB-TE','');
INSERT INTO `state` VALUES (3517,207,'Province','Western','SB-WE','');
INSERT INTO `state` VALUES (3518,201,'District','Anse aux Pins','SC-01','');
INSERT INTO `state` VALUES (3519,201,'District','Anse Boileau','SC-02','');
INSERT INTO `state` VALUES (3520,201,'District','Anse Étoile','SC-03','');
INSERT INTO `state` VALUES (3521,201,'District','Anse Louis','SC-04','');
INSERT INTO `state` VALUES (3522,201,'District','Anse Royale','SC-05','');
INSERT INTO `state` VALUES (3523,201,'District','Baie Lazare','SC-06','');
INSERT INTO `state` VALUES (3524,201,'District','Baie Sainte Anne','SC-07','');
INSERT INTO `state` VALUES (3525,201,'District','Beau Vallon','SC-08','');
INSERT INTO `state` VALUES (3526,201,'District','Bel Air','SC-09','');
INSERT INTO `state` VALUES (3527,201,'District','Bel Ombre','SC-10','');
INSERT INTO `state` VALUES (3528,201,'District','Cascade','SC-11','');
INSERT INTO `state` VALUES (3529,201,'District','Glacis','SC-12','');
INSERT INTO `state` VALUES (3530,201,'District','Grand\' Anse (Mahé)','SC-13','');
INSERT INTO `state` VALUES (3531,201,'District','Grand\' Anse (Praslin)','SC-14','');
INSERT INTO `state` VALUES (3532,201,'District','La Digue','SC-15','');
INSERT INTO `state` VALUES (3533,201,'District','La Rivière Anglaise','SC-16','');
INSERT INTO `state` VALUES (3534,201,'District','Mont Buxton','SC-17','');
INSERT INTO `state` VALUES (3535,201,'District','Mont Fleuri','SC-18','');
INSERT INTO `state` VALUES (3536,201,'District','Plaisance','SC-19','');
INSERT INTO `state` VALUES (3537,201,'District','Pointe La Rue','SC-20','');
INSERT INTO `state` VALUES (3538,201,'District','Port Glaud','SC-21','');
INSERT INTO `state` VALUES (3539,201,'District','Saint Louis','SC-22','');
INSERT INTO `state` VALUES (3540,201,'District','Takamaka','SC-23','');
INSERT INTO `state` VALUES (3541,213,'state','Al Ba?r al A?mar','SD-26','');
INSERT INTO `state` VALUES (3542,213,'state','Al Bu?ayr?t','SD-18','');
INSERT INTO `state` VALUES (3543,213,'state','Al Jaz?rah','SD-07','');
INSERT INTO `state` VALUES (3544,213,'state','Al Khar??m','SD-03','');
INSERT INTO `state` VALUES (3545,213,'state','Al Qa??rif','SD-06','');
INSERT INTO `state` VALUES (3546,213,'state','Al Wa?dah','SD-22','');
INSERT INTO `state` VALUES (3547,213,'state','An N?l','SD-04','');
INSERT INTO `state` VALUES (3548,213,'state','An N?l al Abya?','SD-08','');
INSERT INTO `state` VALUES (3549,213,'state','An N?l al Azraq','SD-24','');
INSERT INTO `state` VALUES (3550,213,'state','Ash Sham?l?yah','SD-01','');
INSERT INTO `state` VALUES (3551,213,'state','A‘?l? an N?l','SD-23','');
INSERT INTO `state` VALUES (3552,213,'state','Ba?r al Jabal','SD-17','');
INSERT INTO `state` VALUES (3553,213,'state','Gharb al Istiw?\'?yah','SD-16','');
INSERT INTO `state` VALUES (3554,213,'state','Gharb Ba?r al Ghaz?l','SD-14','');
INSERT INTO `state` VALUES (3555,213,'state','Gharb D?rf?r','SD-12','');
INSERT INTO `state` VALUES (3556,213,'state','Jan?b D?rf?r','SD-11','');
INSERT INTO `state` VALUES (3557,213,'state','Jan?b Kurduf?n','SD-13','');
INSERT INTO `state` VALUES (3558,213,'state','J?nqal?','SD-20','');
INSERT INTO `state` VALUES (3559,213,'state','Kassal?','SD-05','');
INSERT INTO `state` VALUES (3560,213,'state','Sham?l Ba?r al Ghaz?l','SD-15','');
INSERT INTO `state` VALUES (3561,213,'state','Sham?l D?rf?r','SD-02','');
INSERT INTO `state` VALUES (3562,213,'state','Sham?l Kurduf?n','SD-09','');
INSERT INTO `state` VALUES (3563,213,'state','Sharq al Istiw?\'?yah','SD-19','');
INSERT INTO `state` VALUES (3564,213,'state','Sinn?r','SD-25','');
INSERT INTO `state` VALUES (3565,213,'state','W?r?b','SD-21','');
INSERT INTO `state` VALUES (3566,217,'County','Blekinge län','SE-K','');
INSERT INTO `state` VALUES (3567,217,'County','Dalarnas län','SE-W','');
INSERT INTO `state` VALUES (3568,217,'County','Gotlands län','SE-I','');
INSERT INTO `state` VALUES (3569,217,'County','Gävleborgs län','SE-X','');
INSERT INTO `state` VALUES (3570,217,'County','Hallands län','SE-N','');
INSERT INTO `state` VALUES (3571,217,'County','Jämtlande län','SE-Z','');
INSERT INTO `state` VALUES (3572,217,'County','Jönköpings län','SE-F','');
INSERT INTO `state` VALUES (3573,217,'County','Kalmar län','SE-H','');
INSERT INTO `state` VALUES (3574,217,'County','Kronobergs län','SE-G','');
INSERT INTO `state` VALUES (3575,217,'County','Norrbottens län','SE-BD','');
INSERT INTO `state` VALUES (3576,217,'County','Skåne län','SE-M','');
INSERT INTO `state` VALUES (3577,217,'County','Stockholms län','SE-AB','');
INSERT INTO `state` VALUES (3578,217,'County','Södermanlands län','SE-D','');
INSERT INTO `state` VALUES (3579,217,'County','Uppsala län','SE-C','');
INSERT INTO `state` VALUES (3580,217,'County','Värmlands län','SE-S','');
INSERT INTO `state` VALUES (3581,217,'County','Västerbottens län','SE-AC','');
INSERT INTO `state` VALUES (3582,217,'County','Västernorrlands län','SE-Y','');
INSERT INTO `state` VALUES (3583,217,'County','Västmanlands län','SE-U','');
INSERT INTO `state` VALUES (3584,217,'County','Västra Götalands län','SE-Q','');
INSERT INTO `state` VALUES (3585,217,'County','Örebro län','SE-T','');
INSERT INTO `state` VALUES (3586,217,'County','Östergötlands län','SE-E','');
INSERT INTO `state` VALUES (3587,203,'district','Central Singapore','SG-01','');
INSERT INTO `state` VALUES (3588,203,'district','North East','SG-02','');
INSERT INTO `state` VALUES (3589,203,'district','North West','SG-03','');
INSERT INTO `state` VALUES (3590,203,'district','South East','SG-04','');
INSERT INTO `state` VALUES (3591,203,'district','South West','SG-05','');
INSERT INTO `state` VALUES (3592,189,'Dependency','Saint Helena','SH-SH','');
INSERT INTO `state` VALUES (3593,189,'Dependency','Tristan da Cunha','SH-TA','');
INSERT INTO `state` VALUES (3594,189,'Administrative area','Ascension','SH-AC','');
INSERT INTO `state` VALUES (3595,206,'Municipalities','Ajdovš?ina','SI-001','');
INSERT INTO `state` VALUES (3596,206,'Municipalities','Beltinci','SI-002','');
INSERT INTO `state` VALUES (3597,206,'Municipalities','Benedikt','SI-148','');
INSERT INTO `state` VALUES (3598,206,'Municipalities','Bistrica ob Sotli','SI-149','');
INSERT INTO `state` VALUES (3599,206,'Municipalities','Bled','SI-003','');
INSERT INTO `state` VALUES (3600,206,'Municipalities','Bloke','SI-150','');
INSERT INTO `state` VALUES (3601,206,'Municipalities','Bohinj','SI-004','');
INSERT INTO `state` VALUES (3602,206,'Municipalities','Borovnica','SI-005','');
INSERT INTO `state` VALUES (3603,206,'Municipalities','Bovec','SI-006','');
INSERT INTO `state` VALUES (3604,206,'Municipalities','Braslov?e','SI-151','');
INSERT INTO `state` VALUES (3605,206,'Municipalities','Brda','SI-007','');
INSERT INTO `state` VALUES (3606,206,'Municipalities','Brežice','SI-009','');
INSERT INTO `state` VALUES (3607,206,'Municipalities','Brezovica','SI-008','');
INSERT INTO `state` VALUES (3608,206,'Municipalities','Cankova','SI-152','');
INSERT INTO `state` VALUES (3609,206,'Municipalities','Celje','SI-011','');
INSERT INTO `state` VALUES (3610,206,'Municipalities','Cerklje na Gorenjskem','SI-012','');
INSERT INTO `state` VALUES (3611,206,'Municipalities','Cerknica','SI-013','');
INSERT INTO `state` VALUES (3612,206,'Municipalities','Cerkno','SI-014','');
INSERT INTO `state` VALUES (3613,206,'Municipalities','Cerkvenjak','SI-153','');
INSERT INTO `state` VALUES (3614,206,'Municipalities','?renšovci','SI-015','');
INSERT INTO `state` VALUES (3615,206,'Municipalities','?rna na Koroškem','SI-016','');
INSERT INTO `state` VALUES (3616,206,'Municipalities','?rnomelj','SI-017','');
INSERT INTO `state` VALUES (3617,206,'Municipalities','Destrnik','SI-018','');
INSERT INTO `state` VALUES (3618,206,'Municipalities','Diva?a','SI-019','');
INSERT INTO `state` VALUES (3619,206,'Municipalities','Dobje','SI-154','');
INSERT INTO `state` VALUES (3620,206,'Municipalities','Dobrepolje','SI-020','');
INSERT INTO `state` VALUES (3621,206,'Municipalities','Dobrna','SI-155','');
INSERT INTO `state` VALUES (3622,206,'Municipalities','Dobrova-Polhov Gradec','SI-021','');
INSERT INTO `state` VALUES (3623,206,'Municipalities','Dobrovnik/Dobronak','SI-156','');
INSERT INTO `state` VALUES (3624,206,'Municipalities','Dol pri Ljubljani','SI-022','');
INSERT INTO `state` VALUES (3625,206,'Municipalities','Dolenjske Toplice','SI-157','');
INSERT INTO `state` VALUES (3626,206,'Municipalities','Domžale','SI-023','');
INSERT INTO `state` VALUES (3627,206,'Municipalities','Dornava','SI-024','');
INSERT INTO `state` VALUES (3628,206,'Municipalities','Dravograd','SI-025','');
INSERT INTO `state` VALUES (3629,206,'Municipalities','Duplek','SI-026','');
INSERT INTO `state` VALUES (3630,206,'Municipalities','Gorenja vas-Poljane','SI-027','');
INSERT INTO `state` VALUES (3631,206,'Municipalities','Gorišnica','SI-028','');
INSERT INTO `state` VALUES (3632,206,'Municipalities','Gornja Radgona','SI-029','');
INSERT INTO `state` VALUES (3633,206,'Municipalities','Gornji Grad','SI-030','');
INSERT INTO `state` VALUES (3634,206,'Municipalities','Gornji Petrovci','SI-031','');
INSERT INTO `state` VALUES (3635,206,'Municipalities','Grad','SI-158','');
INSERT INTO `state` VALUES (3636,206,'Municipalities','Grosuplje','SI-032','');
INSERT INTO `state` VALUES (3637,206,'Municipalities','Hajdina','SI-159','');
INSERT INTO `state` VALUES (3638,206,'Municipalities','Ho?e-Slivnica','SI-160','');
INSERT INTO `state` VALUES (3639,206,'Municipalities','Hodoš/Hodos','SI-161','');
INSERT INTO `state` VALUES (3640,206,'Municipalities','Horjul','SI-162','');
INSERT INTO `state` VALUES (3641,206,'Municipalities','Hrastnik','SI-034','');
INSERT INTO `state` VALUES (3642,206,'Municipalities','Hrpelje-Kozina','SI-035','');
INSERT INTO `state` VALUES (3643,206,'Municipalities','Idrija','SI-036','');
INSERT INTO `state` VALUES (3644,206,'Municipalities','Ig','SI-037','');
INSERT INTO `state` VALUES (3645,206,'Municipalities','Ilirska Bistrica','SI-038','');
INSERT INTO `state` VALUES (3646,206,'Municipalities','Ivan?na Gorica','SI-039','');
INSERT INTO `state` VALUES (3647,206,'Municipalities','Izola/Isola','SI-040','');
INSERT INTO `state` VALUES (3648,206,'Municipalities','Jesenice','SI-041','');
INSERT INTO `state` VALUES (3649,206,'Municipalities','Jezersko','SI-163','');
INSERT INTO `state` VALUES (3650,206,'Municipalities','Juršinci','SI-042','');
INSERT INTO `state` VALUES (3651,206,'Municipalities','Kamnik','SI-043','');
INSERT INTO `state` VALUES (3652,206,'Municipalities','Kanal','SI-044','');
INSERT INTO `state` VALUES (3653,206,'Municipalities','Kidri?evo','SI-045','');
INSERT INTO `state` VALUES (3654,206,'Municipalities','Kobarid','SI-046','');
INSERT INTO `state` VALUES (3655,206,'Municipalities','Kobilje','SI-047','');
INSERT INTO `state` VALUES (3656,206,'Municipalities','Ko?evje','SI-048','');
INSERT INTO `state` VALUES (3657,206,'Municipalities','Komen','SI-049','');
INSERT INTO `state` VALUES (3658,206,'Municipalities','Komenda','SI-164','');
INSERT INTO `state` VALUES (3659,206,'Municipalities','Koper/Capodistria','SI-050','');
INSERT INTO `state` VALUES (3660,206,'Municipalities','Kostel','SI-165','');
INSERT INTO `state` VALUES (3661,206,'Municipalities','Kozje','SI-051','');
INSERT INTO `state` VALUES (3662,206,'Municipalities','Kranj','SI-052','');
INSERT INTO `state` VALUES (3663,206,'Municipalities','Kranjska Gora','SI-053','');
INSERT INTO `state` VALUES (3664,206,'Municipalities','Križevci','SI-166','');
INSERT INTO `state` VALUES (3665,206,'Municipalities','Krško','SI-054','');
INSERT INTO `state` VALUES (3666,206,'Municipalities','Kungota','SI-055','');
INSERT INTO `state` VALUES (3667,206,'Municipalities','Kuzma','SI-056','');
INSERT INTO `state` VALUES (3668,206,'Municipalities','Laško','SI-057','');
INSERT INTO `state` VALUES (3669,206,'Municipalities','Lenart','SI-058','');
INSERT INTO `state` VALUES (3670,206,'Municipalities','Lendava/Lendva','SI-059','');
INSERT INTO `state` VALUES (3671,206,'Municipalities','Litija','SI-060','');
INSERT INTO `state` VALUES (3672,206,'Municipalities','Ljubljana','SI-061','');
INSERT INTO `state` VALUES (3673,206,'Municipalities','Ljubno','SI-062','');
INSERT INTO `state` VALUES (3674,206,'Municipalities','Ljutomer','SI-063','');
INSERT INTO `state` VALUES (3675,206,'Municipalities','Logatec','SI-064','');
INSERT INTO `state` VALUES (3676,206,'Municipalities','Loška dolina','SI-065','');
INSERT INTO `state` VALUES (3677,206,'Municipalities','Loški Potok','SI-066','');
INSERT INTO `state` VALUES (3678,206,'Municipalities','Lovrenc na Pohorju','SI-167','');
INSERT INTO `state` VALUES (3679,206,'Municipalities','Lu?e','SI-067','');
INSERT INTO `state` VALUES (3680,206,'Municipalities','Lukovica','SI-068','');
INSERT INTO `state` VALUES (3681,206,'Municipalities','Majšperk','SI-069','');
INSERT INTO `state` VALUES (3682,206,'Municipalities','Maribor','SI-070','');
INSERT INTO `state` VALUES (3683,206,'Municipalities','Markovci','SI-168','');
INSERT INTO `state` VALUES (3684,206,'Municipalities','Medvode','SI-071','');
INSERT INTO `state` VALUES (3685,206,'Municipalities','Mengeš','SI-072','');
INSERT INTO `state` VALUES (3686,206,'Municipalities','Metlika','SI-073','');
INSERT INTO `state` VALUES (3687,206,'Municipalities','Mežica','SI-074','');
INSERT INTO `state` VALUES (3688,206,'Municipalities','Miklavž na Dravskem polju','SI-169','');
INSERT INTO `state` VALUES (3689,206,'Municipalities','Miren-Kostanjevica','SI-075','');
INSERT INTO `state` VALUES (3690,206,'Municipalities','Mirna Pe?','SI-170','');
INSERT INTO `state` VALUES (3691,206,'Municipalities','Mislinja','SI-076','');
INSERT INTO `state` VALUES (3692,206,'Municipalities','Morav?e','SI-077','');
INSERT INTO `state` VALUES (3693,206,'Municipalities','Moravske Toplice','SI-078','');
INSERT INTO `state` VALUES (3694,206,'Municipalities','Mozirje','SI-079','');
INSERT INTO `state` VALUES (3695,206,'Municipalities','Murska Sobota','SI-080','');
INSERT INTO `state` VALUES (3696,206,'Municipalities','Muta','SI-081','');
INSERT INTO `state` VALUES (3697,206,'Municipalities','Naklo','SI-082','');
INSERT INTO `state` VALUES (3698,206,'Municipalities','Nazarje','SI-083','');
INSERT INTO `state` VALUES (3699,206,'Municipalities','Nova Gorica','SI-084','');
INSERT INTO `state` VALUES (3700,206,'Municipalities','Novo mesto','SI-085','');
INSERT INTO `state` VALUES (3701,206,'Municipalities','Odranci','SI-086','');
INSERT INTO `state` VALUES (3702,206,'Municipalities','Oplotnica','SI-171','');
INSERT INTO `state` VALUES (3703,206,'Municipalities','Ormož','SI-087','');
INSERT INTO `state` VALUES (3704,206,'Municipalities','Osilnica','SI-088','');
INSERT INTO `state` VALUES (3705,206,'Municipalities','Pesnica','SI-089','');
INSERT INTO `state` VALUES (3706,206,'Municipalities','Piran/Pirano','SI-090','');
INSERT INTO `state` VALUES (3707,206,'Municipalities','Pivka','SI-091','');
INSERT INTO `state` VALUES (3708,206,'Municipalities','Pod?etrtek','SI-092','');
INSERT INTO `state` VALUES (3709,206,'Municipalities','Podlehnik','SI-172','');
INSERT INTO `state` VALUES (3710,206,'Municipalities','Podvelka','SI-093','');
INSERT INTO `state` VALUES (3711,206,'Municipalities','Polzela','SI-173','');
INSERT INTO `state` VALUES (3712,206,'Municipalities','Postojna','SI-094','');
INSERT INTO `state` VALUES (3713,206,'Municipalities','Prebold','SI-174','');
INSERT INTO `state` VALUES (3714,206,'Municipalities','Preddvor','SI-095','');
INSERT INTO `state` VALUES (3715,206,'Municipalities','Prevalje','SI-175','');
INSERT INTO `state` VALUES (3716,206,'Municipalities','Ptuj','SI-096','');
INSERT INTO `state` VALUES (3717,206,'Municipalities','Puconci','SI-097','');
INSERT INTO `state` VALUES (3718,206,'Municipalities','Ra?e-Fram','SI-098','');
INSERT INTO `state` VALUES (3719,206,'Municipalities','Rade?e','SI-099','');
INSERT INTO `state` VALUES (3720,206,'Municipalities','Radenci','SI-100','');
INSERT INTO `state` VALUES (3721,206,'Municipalities','Radlje ob Dravi','SI-101','');
INSERT INTO `state` VALUES (3722,206,'Municipalities','Radovljica','SI-102','');
INSERT INTO `state` VALUES (3723,206,'Municipalities','Ravne na Koroškem','SI-103','');
INSERT INTO `state` VALUES (3724,206,'Municipalities','Razkrižje','SI-176','');
INSERT INTO `state` VALUES (3725,206,'Municipalities','Ribnica','SI-104','');
INSERT INTO `state` VALUES (3726,206,'Municipalities','Ribnica na Pohorju','SI-177','');
INSERT INTO `state` VALUES (3727,206,'Municipalities','Rogaška Slatina','SI-106','');
INSERT INTO `state` VALUES (3728,206,'Municipalities','Rogašovci','SI-105','');
INSERT INTO `state` VALUES (3729,206,'Municipalities','Rogatec','SI-107','');
INSERT INTO `state` VALUES (3730,206,'Municipalities','Ruše','SI-108','');
INSERT INTO `state` VALUES (3731,206,'Municipalities','Šalovci','SI-033','');
INSERT INTO `state` VALUES (3732,206,'Municipalities','Selnica ob Dravi','SI-178','');
INSERT INTO `state` VALUES (3733,206,'Municipalities','Semi?','SI-109','');
INSERT INTO `state` VALUES (3734,206,'Municipalities','Šempeter-Vrtojba','SI-183','');
INSERT INTO `state` VALUES (3735,206,'Municipalities','Šen?ur','SI-117','');
INSERT INTO `state` VALUES (3736,206,'Municipalities','Šentilj','SI-118','');
INSERT INTO `state` VALUES (3737,206,'Municipalities','Šentjernej','SI-119','');
INSERT INTO `state` VALUES (3738,206,'Municipalities','Šentjur pri Celju','SI-120','');
INSERT INTO `state` VALUES (3739,206,'Municipalities','Sevnica','SI-110','');
INSERT INTO `state` VALUES (3740,206,'Municipalities','Sežana','SI-111','');
INSERT INTO `state` VALUES (3741,206,'Municipalities','Škocjan','SI-121','');
INSERT INTO `state` VALUES (3742,206,'Municipalities','Škofja Loka','SI-122','');
INSERT INTO `state` VALUES (3743,206,'Municipalities','Škofljica','SI-123','');
INSERT INTO `state` VALUES (3744,206,'Municipalities','Slovenj Gradec','SI-112','');
INSERT INTO `state` VALUES (3745,206,'Municipalities','Slovenska Bistrica','SI-113','');
INSERT INTO `state` VALUES (3746,206,'Municipalities','Slovenske Konjice','SI-114','');
INSERT INTO `state` VALUES (3747,206,'Municipalities','Šmarje pri Jelšah','SI-124','');
INSERT INTO `state` VALUES (3748,206,'Municipalities','Šmartno ob Paki','SI-125','');
INSERT INTO `state` VALUES (3749,206,'Municipalities','Šmartno pri Litiji','SI-194','');
INSERT INTO `state` VALUES (3750,206,'Municipalities','Sodražica','SI-179','');
INSERT INTO `state` VALUES (3751,206,'Municipalities','Sol?ava','SI-180','');
INSERT INTO `state` VALUES (3752,206,'Municipalities','Šoštanj','SI-126','');
INSERT INTO `state` VALUES (3753,206,'Municipalities','Starše','SI-115','');
INSERT INTO `state` VALUES (3754,206,'Municipalities','Štore','SI-127','');
INSERT INTO `state` VALUES (3755,206,'Municipalities','Sveta Ana','SI-181','');
INSERT INTO `state` VALUES (3756,206,'Municipalities','Sveti Andraž v Slovenskih goricah','SI-182','');
INSERT INTO `state` VALUES (3757,206,'Municipalities','Sveti Jurij','SI-116','');
INSERT INTO `state` VALUES (3758,206,'Municipalities','Tabor','SI-184','');
INSERT INTO `state` VALUES (3759,206,'Municipalities','Tišina','SI-010','');
INSERT INTO `state` VALUES (3760,206,'Municipalities','Tolmin','SI-128','');
INSERT INTO `state` VALUES (3761,206,'Municipalities','Trbovlje','SI-129','');
INSERT INTO `state` VALUES (3762,206,'Municipalities','Trebnje','SI-130','');
INSERT INTO `state` VALUES (3763,206,'Municipalities','Trnovska vas','SI-185','');
INSERT INTO `state` VALUES (3764,206,'Municipalities','Trži?','SI-131','');
INSERT INTO `state` VALUES (3765,206,'Municipalities','Trzin','SI-186','');
INSERT INTO `state` VALUES (3766,206,'Municipalities','Turniš?e','SI-132','');
INSERT INTO `state` VALUES (3767,206,'Municipalities','Velenje','SI-133','');
INSERT INTO `state` VALUES (3768,206,'Municipalities','Velika Polana','SI-187','');
INSERT INTO `state` VALUES (3769,206,'Municipalities','Velike Laš?e','SI-134','');
INSERT INTO `state` VALUES (3770,206,'Municipalities','Veržej','SI-188','');
INSERT INTO `state` VALUES (3771,206,'Municipalities','Videm','SI-135','');
INSERT INTO `state` VALUES (3772,206,'Municipalities','Vipava','SI-136','');
INSERT INTO `state` VALUES (3773,206,'Municipalities','Vitanje','SI-137','');
INSERT INTO `state` VALUES (3774,206,'Municipalities','Vodice','SI-138','');
INSERT INTO `state` VALUES (3775,206,'Municipalities','Vojnik','SI-139','');
INSERT INTO `state` VALUES (3776,206,'Municipalities','Vransko','SI-189','');
INSERT INTO `state` VALUES (3777,206,'Municipalities','Vrhnika','SI-140','');
INSERT INTO `state` VALUES (3778,206,'Municipalities','Vuzenica','SI-141','');
INSERT INTO `state` VALUES (3779,206,'Municipalities','Zagorje ob Savi','SI-142','');
INSERT INTO `state` VALUES (3780,206,'Municipalities','Žalec','SI-190','');
INSERT INTO `state` VALUES (3781,206,'Municipalities','Zavr?','SI-143','');
INSERT INTO `state` VALUES (3782,206,'Municipalities','Železniki','SI-146','');
INSERT INTO `state` VALUES (3783,206,'Municipalities','Žetale','SI-191','');
INSERT INTO `state` VALUES (3784,206,'Municipalities','Žiri','SI-147','');
INSERT INTO `state` VALUES (3785,206,'Municipalities','Žirovnica','SI-192','');
INSERT INTO `state` VALUES (3786,206,'Municipalities','Zre?e','SI-144','');
INSERT INTO `state` VALUES (3787,206,'Municipalities','Žužemberk','SI-193','');
INSERT INTO `state` VALUES (3788,205,'Region','Banskobystrický kraj','SK-BC','');
INSERT INTO `state` VALUES (3789,205,'Region','Bratislavský kraj','SK-BL','');
INSERT INTO `state` VALUES (3790,205,'Region','Košický kraj','SK-KI','');
INSERT INTO `state` VALUES (3791,205,'Region','Nitriansky kraj','SK-NJ','');
INSERT INTO `state` VALUES (3792,205,'Region','Prešovský kraj','SK-PV','');
INSERT INTO `state` VALUES (3793,205,'Region','Tren?iansky kraj','SK-TC','');
INSERT INTO `state` VALUES (3794,205,'Region','Trnavský kraj','SK-TA','');
INSERT INTO `state` VALUES (3795,205,'Region','Žilinský kraj','SK-ZI','');
INSERT INTO `state` VALUES (3796,202,'Area','Western Area (Freetown)','SL-W','');
INSERT INTO `state` VALUES (3797,202,'Province','Eastern','SL-E','');
INSERT INTO `state` VALUES (3798,202,'Province','Northern','SL-N','');
INSERT INTO `state` VALUES (3799,202,'Province','Southern (Sierra Leone)','SL-S','');
INSERT INTO `state` VALUES (3800,196,'Municipalities','Acquaviva','SM-01','');
INSERT INTO `state` VALUES (3801,196,'Municipalities','Borgo Maggiore','SM-06','');
INSERT INTO `state` VALUES (3802,196,'Municipalities','Chiesanuova','SM-02','');
INSERT INTO `state` VALUES (3803,196,'Municipalities','Domagnano','SM-03','');
INSERT INTO `state` VALUES (3804,196,'Municipalities','Faetano','SM-04','');
INSERT INTO `state` VALUES (3805,196,'Municipalities','Fiorentino','SM-05','');
INSERT INTO `state` VALUES (3806,196,'Municipalities','Montegiardino','SM-08','');
INSERT INTO `state` VALUES (3807,196,'Municipalities','San Marino','SM-07','');
INSERT INTO `state` VALUES (3808,196,'Municipalities','Serravalle','SM-09','');
INSERT INTO `state` VALUES (3809,199,'Region','Dakar','SN-DK','');
INSERT INTO `state` VALUES (3810,199,'Region','Diourbel','SN-DB','');
INSERT INTO `state` VALUES (3811,199,'Region','Fatick','SN-FK','');
INSERT INTO `state` VALUES (3812,199,'Region','Kaolack','SN-KL','');
INSERT INTO `state` VALUES (3813,199,'Region','Kolda','SN-KD','');
INSERT INTO `state` VALUES (3814,199,'Region','Louga','SN-LG','');
INSERT INTO `state` VALUES (3815,199,'Region','Matam','SN-MT','');
INSERT INTO `state` VALUES (3816,199,'Region','Saint-Louis','SN-SL','');
INSERT INTO `state` VALUES (3817,199,'Region','Tambacounda','SN-TC','');
INSERT INTO `state` VALUES (3818,199,'Region','Thiès','SN-TH','');
INSERT INTO `state` VALUES (3819,199,'Region','Ziguinchor','SN-ZG','');
INSERT INTO `state` VALUES (3820,208,'Region','Awdal','SO-AW','');
INSERT INTO `state` VALUES (3821,208,'Region','Bakool','SO-BK','');
INSERT INTO `state` VALUES (3822,208,'Region','Banaadir','SO-BN','');
INSERT INTO `state` VALUES (3823,208,'Region','Bari','SO-BR','');
INSERT INTO `state` VALUES (3824,208,'Region','Bay','SO-BY','');
INSERT INTO `state` VALUES (3825,208,'Region','Galguduud','SO-GA','');
INSERT INTO `state` VALUES (3826,208,'Region','Gedo','SO-GE','');
INSERT INTO `state` VALUES (3827,208,'Region','Hiirsan','SO-HI','');
INSERT INTO `state` VALUES (3828,208,'Region','Jubbada Dhexe','SO-JD','');
INSERT INTO `state` VALUES (3829,208,'Region','Jubbada Hoose','SO-JH','');
INSERT INTO `state` VALUES (3830,208,'Region','Mudug','SO-MU','');
INSERT INTO `state` VALUES (3831,208,'Region','Nugaal','SO-NU','');
INSERT INTO `state` VALUES (3832,208,'Region','Saneag','SO-SA','');
INSERT INTO `state` VALUES (3833,208,'Region','Shabeellaha Dhexe','SO-SD','');
INSERT INTO `state` VALUES (3834,208,'Region','Shabeellaha Hoose','SO-SH','');
INSERT INTO `state` VALUES (3835,208,'Region','Sool','SO-SO','');
INSERT INTO `state` VALUES (3836,208,'Region','Togdheer','SO-TO','');
INSERT INTO `state` VALUES (3837,208,'Region','Woqooyi Galbeed','SO-WO','');
INSERT INTO `state` VALUES (3838,214,'District','Brokopondo','SR-BR','');
INSERT INTO `state` VALUES (3839,214,'District','Commewijne','SR-CM','');
INSERT INTO `state` VALUES (3840,214,'District','Coronie','SR-CR','');
INSERT INTO `state` VALUES (3841,214,'District','Marowijne','SR-MA','');
INSERT INTO `state` VALUES (3842,214,'District','Nickerie','SR-NI','');
INSERT INTO `state` VALUES (3843,214,'District','Para','SR-PR','');
INSERT INTO `state` VALUES (3844,214,'District','Paramaribo','SR-PM','');
INSERT INTO `state` VALUES (3845,214,'District','Saramacca','SR-SA','');
INSERT INTO `state` VALUES (3846,214,'District','Sipaliwini','SR-SI','');
INSERT INTO `state` VALUES (3847,214,'District','Wanica','SR-WA','');
INSERT INTO `state` VALUES (3848,197,'Municipality','Príncipe','ST-P','');
INSERT INTO `state` VALUES (3849,197,'Municipality','São Tomé','ST-S','');
INSERT INTO `state` VALUES (3850,70,'Department','Ahuachapán','SV-AH','');
INSERT INTO `state` VALUES (3851,70,'Department','Cabañas','SV-CA','');
INSERT INTO `state` VALUES (3852,70,'Department','Cuscatlán','SV-CU','');
INSERT INTO `state` VALUES (3853,70,'Department','Chalatenango','SV-CH','');
INSERT INTO `state` VALUES (3854,70,'Department','La Libertad','SV-LI','');
INSERT INTO `state` VALUES (3855,70,'Department','La Paz','SV-PA','');
INSERT INTO `state` VALUES (3856,70,'Department','La Unión','SV-UN','');
INSERT INTO `state` VALUES (3857,70,'Department','Morazán','SV-MO','');
INSERT INTO `state` VALUES (3858,70,'Department','San Miguel','SV-SM','');
INSERT INTO `state` VALUES (3859,70,'Department','San Salvador','SV-SS','');
INSERT INTO `state` VALUES (3860,70,'Department','Santa Ana','SV-SA','');
INSERT INTO `state` VALUES (3861,70,'Department','San Vicente','SV-SV','');
INSERT INTO `state` VALUES (3862,70,'Department','Sonsonate','SV-SO','');
INSERT INTO `state` VALUES (3863,70,'Department','Usulután','SV-US','');
INSERT INTO `state` VALUES (3864,219,'Governorate','Al Hasakah','SY-HA','');
INSERT INTO `state` VALUES (3865,219,'Governorate','Al Ladhiqiyah','SY-LA','');
INSERT INTO `state` VALUES (3866,219,'Governorate','Al Qunaytirah','SY-QU','');
INSERT INTO `state` VALUES (3867,219,'Governorate','Ar Raqqah','SY-RA','');
INSERT INTO `state` VALUES (3868,219,'Governorate','As Suwayda\'','SY-SU','');
INSERT INTO `state` VALUES (3869,219,'Governorate','Dar\'a','SY-DR','');
INSERT INTO `state` VALUES (3870,219,'Governorate','Dayr az Zawr','SY-DY','');
INSERT INTO `state` VALUES (3871,219,'Governorate','Dimashq','SY-DI','');
INSERT INTO `state` VALUES (3872,219,'Governorate','Halab','SY-HL','');
INSERT INTO `state` VALUES (3873,219,'Governorate','Hamah','SY-HM','');
INSERT INTO `state` VALUES (3874,219,'Governorate','Homs','SY-HI','');
INSERT INTO `state` VALUES (3875,219,'Governorate','Idlib','SY-ID','');
INSERT INTO `state` VALUES (3876,219,'Governorate','Rif Dimashq','SY-RD','');
INSERT INTO `state` VALUES (3877,219,'Governorate','Tartus','SY-TA','');
INSERT INTO `state` VALUES (3878,216,'District','Hhohho','SZ-HH','');
INSERT INTO `state` VALUES (3879,216,'District','Lubombo','SZ-LU','');
INSERT INTO `state` VALUES (3880,216,'District','Manzini','SZ-MA','');
INSERT INTO `state` VALUES (3881,216,'District','Shiselweni','SZ-SH','');
INSERT INTO `state` VALUES (3882,47,'Region','Batha','TD-BA','');
INSERT INTO `state` VALUES (3883,47,'Region','Borkou-Ennedi-Tibesti','TD-BET','');
INSERT INTO `state` VALUES (3884,47,'Region','Chari-Baguirmi','TD-CB','');
INSERT INTO `state` VALUES (3885,47,'Region','Guéra','TD-GR','');
INSERT INTO `state` VALUES (3886,47,'Region','Hadjer Lamis','TD-HL','');
INSERT INTO `state` VALUES (3887,47,'Region','Kanem','TD-KA','');
INSERT INTO `state` VALUES (3888,47,'Region','Lac','TD-LC','');
INSERT INTO `state` VALUES (3889,47,'Region','Logone-Occidental','TD-LO','');
INSERT INTO `state` VALUES (3890,47,'Region','Logone-Oriental','TD-LR','');
INSERT INTO `state` VALUES (3891,47,'Region','Mandoul','TD-MA','');
INSERT INTO `state` VALUES (3892,47,'Region','Mayo-Kébbi-Est','TD-ME','');
INSERT INTO `state` VALUES (3893,47,'Region','Mayo-Kébbi-Ouest','TD-MO','');
INSERT INTO `state` VALUES (3894,47,'Region','Moyen-Chari','TD-MC','');
INSERT INTO `state` VALUES (3895,47,'Region','Ndjamena','TD-ND','');
INSERT INTO `state` VALUES (3896,47,'Region','Ouaddaï','TD-OD','');
INSERT INTO `state` VALUES (3897,47,'Region','Salamat','TD-SA','');
INSERT INTO `state` VALUES (3898,47,'Region','Tandjilé','TD-TA','');
INSERT INTO `state` VALUES (3899,47,'Region','Wadi Fira','TD-WF','');
INSERT INTO `state` VALUES (3900,225,'Region','Région du Centre','TG-C','');
INSERT INTO `state` VALUES (3901,225,'Region','Région de la Kara','TG-K','');
INSERT INTO `state` VALUES (3902,225,'Region','Région Maritime','TG-M','');
INSERT INTO `state` VALUES (3903,225,'Region','Région des Plateaux','TG-P','');
INSERT INTO `state` VALUES (3904,225,'Region','Région des Savannes','TG-S','');
INSERT INTO `state` VALUES (3905,223,'Municipality','Krung Thep Maha Nakhon Bangkok','TH-10','');
INSERT INTO `state` VALUES (3906,223,'Province','Phatthaya','TH-S','');
INSERT INTO `state` VALUES (3907,223,'Province','Amnat Charoen','TH-37','');
INSERT INTO `state` VALUES (3908,223,'Province','Ang Thong','TH-15','');
INSERT INTO `state` VALUES (3909,223,'Province','Buri Ram','TH-31','');
INSERT INTO `state` VALUES (3910,223,'Province','Chachoengsao','TH-24','');
INSERT INTO `state` VALUES (3911,223,'Province','Chai Nat','TH-18','');
INSERT INTO `state` VALUES (3912,223,'Province','Chaiyaphum','TH-36','');
INSERT INTO `state` VALUES (3913,223,'Province','Chanthaburi','TH-22','');
INSERT INTO `state` VALUES (3914,223,'Province','Chiang Mai','TH-50','');
INSERT INTO `state` VALUES (3915,223,'Province','Chiang Rai','TH-57','');
INSERT INTO `state` VALUES (3916,223,'Province','Chon Buri','TH-20','');
INSERT INTO `state` VALUES (3917,223,'Province','Chumphon','TH-86','');
INSERT INTO `state` VALUES (3918,223,'Province','Kalasin','TH-46','');
INSERT INTO `state` VALUES (3919,223,'Province','Kamphaeng Phet','TH-62','');
INSERT INTO `state` VALUES (3920,223,'Province','Kanchanaburi','TH-71','');
INSERT INTO `state` VALUES (3921,223,'Province','Khon Kaen','TH-40','');
INSERT INTO `state` VALUES (3922,223,'Province','Krabi','TH-81','');
INSERT INTO `state` VALUES (3923,223,'Province','Lampang','TH-52','');
INSERT INTO `state` VALUES (3924,223,'Province','Lamphun','TH-51','');
INSERT INTO `state` VALUES (3925,223,'Province','Loei','TH-42','');
INSERT INTO `state` VALUES (3926,223,'Province','Lop Buri','TH-16','');
INSERT INTO `state` VALUES (3927,223,'Province','Mae Hong Son','TH-58','');
INSERT INTO `state` VALUES (3928,223,'Province','Maha Sarakham','TH-44','');
INSERT INTO `state` VALUES (3929,223,'Province','Mukdahan','TH-49','');
INSERT INTO `state` VALUES (3930,223,'Province','Nakhon Nayok','TH-26','');
INSERT INTO `state` VALUES (3931,223,'Province','Nakhon Pathom','TH-73','');
INSERT INTO `state` VALUES (3932,223,'Province','Nakhon Phanom','TH-48','');
INSERT INTO `state` VALUES (3933,223,'Province','Nakhon Ratchasima','TH-30','');
INSERT INTO `state` VALUES (3934,223,'Province','Nakhon Sawan','TH-60','');
INSERT INTO `state` VALUES (3935,223,'Province','Nakhon Si Thammarat','TH-80','');
INSERT INTO `state` VALUES (3936,223,'Province','Nan','TH-55','');
INSERT INTO `state` VALUES (3937,223,'Province','Narathiwat','TH-96','');
INSERT INTO `state` VALUES (3938,223,'Province','Nong Bua Lam Phu','TH-39','');
INSERT INTO `state` VALUES (3939,223,'Province','Nong Khai','TH-43','');
INSERT INTO `state` VALUES (3940,223,'Province','Nonthaburi','TH-12','');
INSERT INTO `state` VALUES (3941,223,'Province','Pathum Thani','TH-13','');
INSERT INTO `state` VALUES (3942,223,'Province','Pattani','TH-94','');
INSERT INTO `state` VALUES (3943,223,'Province','Phangnga','TH-82','');
INSERT INTO `state` VALUES (3944,223,'Province','Phatthalung','TH-93','');
INSERT INTO `state` VALUES (3945,223,'Province','Phayao','TH-56','');
INSERT INTO `state` VALUES (3946,223,'Province','Phetchabun','TH-67','');
INSERT INTO `state` VALUES (3947,223,'Province','Phetchaburi','TH-76','');
INSERT INTO `state` VALUES (3948,223,'Province','Phichit','TH-66','');
INSERT INTO `state` VALUES (3949,223,'Province','Phitsanulok','TH-65','');
INSERT INTO `state` VALUES (3950,223,'Province','Phrae','TH-54','');
INSERT INTO `state` VALUES (3951,223,'Province','Phra Nakhon Si Ayutthaya','TH-14','');
INSERT INTO `state` VALUES (3952,223,'Province','Phuket','TH-83','');
INSERT INTO `state` VALUES (3953,223,'Province','Prachin Buri','TH-25','');
INSERT INTO `state` VALUES (3954,223,'Province','Prachuap Khiri Khan','TH-77','');
INSERT INTO `state` VALUES (3955,223,'Province','Ranong','TH-85','');
INSERT INTO `state` VALUES (3956,223,'Province','Ratchaburi','TH-70','');
INSERT INTO `state` VALUES (3957,223,'Province','Rayong','TH-21','');
INSERT INTO `state` VALUES (3958,223,'Province','Roi Et','TH-45','');
INSERT INTO `state` VALUES (3959,223,'Province','Sa Kaeo','TH-27','');
INSERT INTO `state` VALUES (3960,223,'Province','Sakon Nakhon','TH-47','');
INSERT INTO `state` VALUES (3961,223,'Province','Samut Prakan','TH-11','');
INSERT INTO `state` VALUES (3962,223,'Province','Samut Sakhon','TH-74','');
INSERT INTO `state` VALUES (3963,223,'Province','Samut Songkhram','TH-75','');
INSERT INTO `state` VALUES (3964,223,'Province','Saraburi','TH-19','');
INSERT INTO `state` VALUES (3965,223,'Province','Satun','TH-91','');
INSERT INTO `state` VALUES (3966,223,'Province','Sing Buri','TH-17','');
INSERT INTO `state` VALUES (3967,223,'Province','Si Sa Ket','TH-33','');
INSERT INTO `state` VALUES (3968,223,'Province','Songkhla','TH-90','');
INSERT INTO `state` VALUES (3969,223,'Province','Sukhothai','TH-64','');
INSERT INTO `state` VALUES (3970,223,'Province','Suphan Buri','TH-72','');
INSERT INTO `state` VALUES (3971,223,'Province','Surat Thani','TH-84','');
INSERT INTO `state` VALUES (3972,223,'Province','Surin','TH-32','');
INSERT INTO `state` VALUES (3973,223,'Province','Tak','TH-63','');
INSERT INTO `state` VALUES (3974,223,'Province','Trang','TH-92','');
INSERT INTO `state` VALUES (3975,223,'Province','Trat','TH-23','');
INSERT INTO `state` VALUES (3976,223,'Province','Ubon Ratchathani','TH-34','');
INSERT INTO `state` VALUES (3977,223,'Province','Udon Thani','TH-41','');
INSERT INTO `state` VALUES (3978,223,'Province','Uthai Thani','TH-61','');
INSERT INTO `state` VALUES (3979,223,'Province','Uttaradit','TH-53','');
INSERT INTO `state` VALUES (3980,223,'Province','Yala','TH-95','');
INSERT INTO `state` VALUES (3981,223,'Province','Yasothon','TH-35','');
INSERT INTO `state` VALUES (3982,221,'Autonomous region','Gorno-Badakhshan','TJ-GB','');
INSERT INTO `state` VALUES (3983,221,'Region','Khatlon','TJ-KT','');
INSERT INTO `state` VALUES (3984,221,'Region','Sughd','TJ-SU','');
INSERT INTO `state` VALUES (3985,224,'District','Aileu','TL-AL','');
INSERT INTO `state` VALUES (3986,224,'District','Ainaro','TL-AN','');
INSERT INTO `state` VALUES (3987,224,'District','Baucau','TL-BA','');
INSERT INTO `state` VALUES (3988,224,'District','Bobonaro','TL-BO','');
INSERT INTO `state` VALUES (3989,224,'District','Cova Lima','TL-CO','');
INSERT INTO `state` VALUES (3990,224,'District','Dili','TL-DI','');
INSERT INTO `state` VALUES (3991,224,'District','Ermera','TL-ER','');
INSERT INTO `state` VALUES (3992,224,'District','Lautem','TL-LA','');
INSERT INTO `state` VALUES (3993,224,'District','Liquiça','TL-LI','');
INSERT INTO `state` VALUES (3994,224,'District','Manatuto','TL-MT','');
INSERT INTO `state` VALUES (3995,224,'District','Manufahi','TL-MF','');
INSERT INTO `state` VALUES (3996,224,'District','Oecussi','TL-OE','');
INSERT INTO `state` VALUES (3997,224,'District','Viqueque','TL-VI','');
INSERT INTO `state` VALUES (3998,231,'Region','Ahal','TM-A','');
INSERT INTO `state` VALUES (3999,231,'Region','Balkan','TM-B','');
INSERT INTO `state` VALUES (4000,231,'Region','Da?oguz','TM-D','');
INSERT INTO `state` VALUES (4001,231,'Region','Lebap','TM-L','');
INSERT INTO `state` VALUES (4002,231,'Region','Mary','TM-M','');
INSERT INTO `state` VALUES (4003,229,'Governorate','Béja','TN-31','');
INSERT INTO `state` VALUES (4004,229,'Governorate','Ben Arous','TN-13','');
INSERT INTO `state` VALUES (4005,229,'Governorate','Bizerte','TN-23','');
INSERT INTO `state` VALUES (4006,229,'Governorate','Gabès','TN-81','');
INSERT INTO `state` VALUES (4007,229,'Governorate','Gafsa','TN-71','');
INSERT INTO `state` VALUES (4008,229,'Governorate','Jendouba','TN-32','');
INSERT INTO `state` VALUES (4009,229,'Governorate','Kairouan','TN-41','');
INSERT INTO `state` VALUES (4010,229,'Governorate','Kasserine','TN-42','');
INSERT INTO `state` VALUES (4011,229,'Governorate','Kebili','TN-73','');
INSERT INTO `state` VALUES (4012,229,'Governorate','L\'Ariana','TN-12','');
INSERT INTO `state` VALUES (4013,229,'Governorate','Le Kef','TN-33','');
INSERT INTO `state` VALUES (4014,229,'Governorate','Mahdia','TN-53','');
INSERT INTO `state` VALUES (4015,229,'Governorate','La Manouba','TN-14','');
INSERT INTO `state` VALUES (4016,229,'Governorate','Medenine','TN-82','');
INSERT INTO `state` VALUES (4017,229,'Governorate','Monastir','TN-52','');
INSERT INTO `state` VALUES (4018,229,'Governorate','Nabeul','TN-21','');
INSERT INTO `state` VALUES (4019,229,'Governorate','Sfax','TN-61','');
INSERT INTO `state` VALUES (4020,229,'Governorate','Sidi Bouzid','TN-43','');
INSERT INTO `state` VALUES (4021,229,'Governorate','Siliana','TN-34','');
INSERT INTO `state` VALUES (4022,229,'Governorate','Sousse','TN-51','');
INSERT INTO `state` VALUES (4023,229,'Governorate','Tataouine','TN-83','');
INSERT INTO `state` VALUES (4024,229,'Governorate','Tozeur','TN-72','');
INSERT INTO `state` VALUES (4025,229,'Governorate','Tunis','TN-11','');
INSERT INTO `state` VALUES (4026,229,'Governorate','Zaghouan','TN-22','');
INSERT INTO `state` VALUES (4027,227,'Division','\'Eua','TO-01','');
INSERT INTO `state` VALUES (4028,227,'Division','Ha\'apai','TO-02','');
INSERT INTO `state` VALUES (4029,227,'Division','Niuas','TO-03','');
INSERT INTO `state` VALUES (4030,227,'Division','Tongatapu','TO-04','');
INSERT INTO `state` VALUES (4031,227,'Division','Vava\'u','TO-05','');
INSERT INTO `state` VALUES (4032,230,'Province','Adana','TR-01','');
INSERT INTO `state` VALUES (4033,230,'Province','Ad?yaman','TR-02','');
INSERT INTO `state` VALUES (4034,230,'Province','Afyon','TR-03','');
INSERT INTO `state` VALUES (4035,230,'Province','A?r?','TR-04','');
INSERT INTO `state` VALUES (4036,230,'Province','Aksaray','TR-68','');
INSERT INTO `state` VALUES (4037,230,'Province','Amasya','TR-05','');
INSERT INTO `state` VALUES (4038,230,'Province','Ankara','TR-06','');
INSERT INTO `state` VALUES (4039,230,'Province','Antalya','TR-07','');
INSERT INTO `state` VALUES (4040,230,'Province','Ardahan','TR-75','');
INSERT INTO `state` VALUES (4041,230,'Province','Artvin','TR-08','');
INSERT INTO `state` VALUES (4042,230,'Province','Ayd?n','TR-09','');
INSERT INTO `state` VALUES (4043,230,'Province','Bal?kesir','TR-10','');
INSERT INTO `state` VALUES (4044,230,'Province','Bart?n','TR-74','');
INSERT INTO `state` VALUES (4045,230,'Province','Batman','TR-72','');
INSERT INTO `state` VALUES (4046,230,'Province','Bayburt','TR-69','');
INSERT INTO `state` VALUES (4047,230,'Province','Bilecik','TR-11','');
INSERT INTO `state` VALUES (4048,230,'Province','Bingöl','TR-12','');
INSERT INTO `state` VALUES (4049,230,'Province','Bitlis','TR-13','');
INSERT INTO `state` VALUES (4050,230,'Province','Bolu','TR-14','');
INSERT INTO `state` VALUES (4051,230,'Province','Burdur','TR-15','');
INSERT INTO `state` VALUES (4052,230,'Province','Bursa','TR-16','');
INSERT INTO `state` VALUES (4053,230,'Province','Çanakkale','TR-17','');
INSERT INTO `state` VALUES (4054,230,'Province','Çank?r?','TR-18','');
INSERT INTO `state` VALUES (4055,230,'Province','Çorum','TR-19','');
INSERT INTO `state` VALUES (4056,230,'Province','Denizli','TR-20','');
INSERT INTO `state` VALUES (4057,230,'Province','Diyarbak?r','TR-21','');
INSERT INTO `state` VALUES (4058,230,'Province','Düzce','TR-81','');
INSERT INTO `state` VALUES (4059,230,'Province','Edirne','TR-22','');
INSERT INTO `state` VALUES (4060,230,'Province','Elaz??','TR-23','');
INSERT INTO `state` VALUES (4061,230,'Province','Erzincan','TR-24','');
INSERT INTO `state` VALUES (4062,230,'Province','Erzurum','TR-25','');
INSERT INTO `state` VALUES (4063,230,'Province','Eski?ehir','TR-26','');
INSERT INTO `state` VALUES (4064,230,'Province','Gaziantep','TR-27','');
INSERT INTO `state` VALUES (4065,230,'Province','Giresun','TR-28','');
INSERT INTO `state` VALUES (4066,230,'Province','Gümü?hane','TR-29','');
INSERT INTO `state` VALUES (4067,230,'Province','Hakkâri','TR-30','');
INSERT INTO `state` VALUES (4068,230,'Province','Hatay','TR-31','');
INSERT INTO `state` VALUES (4069,230,'Province','I?d?r','TR-76','');
INSERT INTO `state` VALUES (4070,230,'Province','Isparta','TR-32','');
INSERT INTO `state` VALUES (4071,230,'Province','?çel','TR-33','');
INSERT INTO `state` VALUES (4072,230,'Province','?stanbul','TR-34','');
INSERT INTO `state` VALUES (4073,230,'Province','?zmir','TR-35','');
INSERT INTO `state` VALUES (4074,230,'Province','Kahramanmara?','TR-46','');
INSERT INTO `state` VALUES (4075,230,'Province','Karabük','TR-78','');
INSERT INTO `state` VALUES (4076,230,'Province','Karaman','TR-70','');
INSERT INTO `state` VALUES (4077,230,'Province','Kars','TR-36','');
INSERT INTO `state` VALUES (4078,230,'Province','Kastamonu','TR-37','');
INSERT INTO `state` VALUES (4079,230,'Province','Kayseri','TR-38','');
INSERT INTO `state` VALUES (4080,230,'Province','K?r?kkale','TR-71','');
INSERT INTO `state` VALUES (4081,230,'Province','K?rklareli','TR-39','');
INSERT INTO `state` VALUES (4082,230,'Province','K?r?ehir','TR-40','');
INSERT INTO `state` VALUES (4083,230,'Province','Kilis','TR-79','');
INSERT INTO `state` VALUES (4084,230,'Province','Kocaeli','TR-41','');
INSERT INTO `state` VALUES (4085,230,'Province','Konya','TR-42','');
INSERT INTO `state` VALUES (4086,230,'Province','Kütahya','TR-43','');
INSERT INTO `state` VALUES (4087,230,'Province','Malatya','TR-44','');
INSERT INTO `state` VALUES (4088,230,'Province','Manisa','TR-45','');
INSERT INTO `state` VALUES (4089,230,'Province','Mardin','TR-47','');
INSERT INTO `state` VALUES (4090,230,'Province','Mu?la','TR-48','');
INSERT INTO `state` VALUES (4091,230,'Province','Mu?','TR-49','');
INSERT INTO `state` VALUES (4092,230,'Province','Nev?ehir','TR-50','');
INSERT INTO `state` VALUES (4093,230,'Province','Ni?de','TR-51','');
INSERT INTO `state` VALUES (4094,230,'Province','Ordu','TR-52','');
INSERT INTO `state` VALUES (4095,230,'Province','Osmaniye','TR-80','');
INSERT INTO `state` VALUES (4096,230,'Province','Rize','TR-53','');
INSERT INTO `state` VALUES (4097,230,'Province','Sakarya','TR-54','');
INSERT INTO `state` VALUES (4098,230,'Province','Samsun','TR-55','');
INSERT INTO `state` VALUES (4099,230,'Province','Siirt','TR-56','');
INSERT INTO `state` VALUES (4100,230,'Province','Sinop','TR-57','');
INSERT INTO `state` VALUES (4101,230,'Province','Sivas','TR-58','');
INSERT INTO `state` VALUES (4102,230,'Province','?anl?urfa','TR-63','');
INSERT INTO `state` VALUES (4103,230,'Province','??rnak','TR-73','');
INSERT INTO `state` VALUES (4104,230,'Province','Tekirda?','TR-59','');
INSERT INTO `state` VALUES (4105,230,'Province','Tokat','TR-60','');
INSERT INTO `state` VALUES (4106,230,'Province','Trabzon','TR-61','');
INSERT INTO `state` VALUES (4107,230,'Province','Tunceli','TR-62','');
INSERT INTO `state` VALUES (4108,230,'Province','U?ak','TR-64','');
INSERT INTO `state` VALUES (4109,230,'Province','Van','TR-65','');
INSERT INTO `state` VALUES (4110,230,'Province','Yalova','TR-77','');
INSERT INTO `state` VALUES (4111,230,'Province','Yozgat','TR-66','');
INSERT INTO `state` VALUES (4112,230,'Province','Zonguldak','TR-67','');
INSERT INTO `state` VALUES (4113,228,'Region','Couva-Tabaquite-Talparo','TT-CTT','');
INSERT INTO `state` VALUES (4114,228,'Region','Diego Martin','TT-DMN','');
INSERT INTO `state` VALUES (4115,228,'Region','Eastern Tobago','TT-ETO','');
INSERT INTO `state` VALUES (4116,228,'Region','Penal-Debe','TT-PED','');
INSERT INTO `state` VALUES (4117,228,'Region','Princes Town','TT-PRT','');
INSERT INTO `state` VALUES (4118,228,'Region','Rio Claro-Mayaro','TT-RCM','');
INSERT INTO `state` VALUES (4119,228,'Region','Sangre Grande','TT-SGE','');
INSERT INTO `state` VALUES (4120,228,'Region','San Juan-Laventille','TT-SJL','');
INSERT INTO `state` VALUES (4121,228,'Region','Siparia','TT-SIP','');
INSERT INTO `state` VALUES (4122,228,'Region','Tunapuna-Piarco','TT-TUP','');
INSERT INTO `state` VALUES (4123,228,'Region','Western Tobago','TT-WTO','');
INSERT INTO `state` VALUES (4124,228,'Borough','Arima','TT-ARI','');
INSERT INTO `state` VALUES (4125,228,'Borough','Chaguanas','TT-CHA','');
INSERT INTO `state` VALUES (4126,228,'Borough','Point Fortin','TT-PTF','');
INSERT INTO `state` VALUES (4127,228,'City','Port of Spain','TT-POS','');
INSERT INTO `state` VALUES (4128,228,'City','San Fernando','TT-SFO','');
INSERT INTO `state` VALUES (4129,233,'Town council','Funafuti','TV-FUN','');
INSERT INTO `state` VALUES (4130,233,'Island council','Nanumanga','TV-NMG','');
INSERT INTO `state` VALUES (4131,233,'Island council','Nanumea','TV-NMA','');
INSERT INTO `state` VALUES (4132,233,'Island council','Niutao','TV-NIT','');
INSERT INTO `state` VALUES (4133,233,'Island council','Nui','TV-NIU','');
INSERT INTO `state` VALUES (4134,233,'Island council','Nukufetau','TV-NKF','');
INSERT INTO `state` VALUES (4135,233,'Island council','Nukulaelae','TV-NKL','');
INSERT INTO `state` VALUES (4136,233,'Island council','Vaitupu','TV-VAI','');
INSERT INTO `state` VALUES (4137,220,'District','Changhua','TW-CHA','');
INSERT INTO `state` VALUES (4138,220,'District','Chiayi','TW-CYQ','');
INSERT INTO `state` VALUES (4139,220,'District','Hsinchu','TW-HSQ','');
INSERT INTO `state` VALUES (4140,220,'District','Hualien','TW-HUA','');
INSERT INTO `state` VALUES (4141,220,'District','Ilan','TW-ILA','');
INSERT INTO `state` VALUES (4142,220,'District','Kaohsiung','TW-KHQ','');
INSERT INTO `state` VALUES (4143,220,'District','Miaoli','TW-MIA','');
INSERT INTO `state` VALUES (4144,220,'District','Nantou','TW-NAN','');
INSERT INTO `state` VALUES (4145,220,'District','Penghu','TW-PEN','');
INSERT INTO `state` VALUES (4146,220,'District','Pingtung','TW-PIF','');
INSERT INTO `state` VALUES (4147,220,'District','Taichung','TW-TXQ','');
INSERT INTO `state` VALUES (4148,220,'District','Tainan','TW-TNQ','');
INSERT INTO `state` VALUES (4149,220,'District','Taipei','TW-TPQ','');
INSERT INTO `state` VALUES (4150,220,'District','Taitung','TW-TTT','');
INSERT INTO `state` VALUES (4151,220,'District','Taoyuan','TW-TAO','');
INSERT INTO `state` VALUES (4152,220,'District','Yunlin','TW-YUN','');
INSERT INTO `state` VALUES (4153,220,'Municipality','Chiay City','TW-CYI','');
INSERT INTO `state` VALUES (4154,220,'Municipality','Hsinchui City','TW-HSZ','');
INSERT INTO `state` VALUES (4155,220,'Municipality','Keelung City','TW-KEE','');
INSERT INTO `state` VALUES (4156,220,'Municipality','Taichung City','TW-TXG','');
INSERT INTO `state` VALUES (4157,220,'Municipality','Tainan City','TW-TNN','');
INSERT INTO `state` VALUES (4158,220,'Special Municipality','Kaohsiung City','TW-KHH','');
INSERT INTO `state` VALUES (4159,220,'Special Municipality','Taipei City','TW-TPE','');
INSERT INTO `state` VALUES (4160,222,'Region','Arusha','TZ-01','');
INSERT INTO `state` VALUES (4161,222,'Region','Dar-es-Salaam','TZ-02','');
INSERT INTO `state` VALUES (4162,222,'Region','Dodoma','TZ-03','');
INSERT INTO `state` VALUES (4163,222,'Region','Iringa','TZ-04','');
INSERT INTO `state` VALUES (4164,222,'Region','Kagera','TZ-05','');
INSERT INTO `state` VALUES (4165,222,'Region','Kaskazini Pemba','TZ-06','');
INSERT INTO `state` VALUES (4166,222,'Region','Kaskazini Unguja','TZ-07','');
INSERT INTO `state` VALUES (4167,222,'Region','Kigoma','TZ-08','');
INSERT INTO `state` VALUES (4168,222,'Region','Kilimanjaro','TZ-09','');
INSERT INTO `state` VALUES (4169,222,'Region','Kusini Pemba','TZ-10','');
INSERT INTO `state` VALUES (4170,222,'Region','Kusini Unguja','TZ-11','');
INSERT INTO `state` VALUES (4171,222,'Region','Lindi','TZ-12','');
INSERT INTO `state` VALUES (4172,222,'Region','Manyara','TZ-26','');
INSERT INTO `state` VALUES (4173,222,'Region','Mara','TZ-13','');
INSERT INTO `state` VALUES (4174,222,'Region','Mbeya','TZ-14','');
INSERT INTO `state` VALUES (4175,222,'Region','Mjini Magharibi','TZ-15','');
INSERT INTO `state` VALUES (4176,222,'Region','Morogoro','TZ-16','');
INSERT INTO `state` VALUES (4177,222,'Region','Mtwara','TZ-17','');
INSERT INTO `state` VALUES (4178,222,'Region','Mwanza','TZ-18','');
INSERT INTO `state` VALUES (4179,222,'Region','Pwani','TZ-19','');
INSERT INTO `state` VALUES (4180,222,'Region','Rukwa','TZ-20','');
INSERT INTO `state` VALUES (4181,222,'Region','Ruvuma','TZ-21','');
INSERT INTO `state` VALUES (4182,222,'Region','Shinyanga','TZ-22','');
INSERT INTO `state` VALUES (4183,222,'Region','Singida','TZ-23','');
INSERT INTO `state` VALUES (4184,222,'Region','Tabora','TZ-24','');
INSERT INTO `state` VALUES (4185,222,'Region','Tanga','TZ-25','');
INSERT INTO `state` VALUES (4186,235,'Province','Cherkas\'ka Oblast\'','UA-71','');
INSERT INTO `state` VALUES (4187,235,'Province','Chernihivs\'ka Oblast\'','UA-74','');
INSERT INTO `state` VALUES (4188,235,'Province','Chernivets\'ka Oblast\'','UA-77','');
INSERT INTO `state` VALUES (4189,235,'Province','Dnipropetrovs\'ka Oblast\'','UA-12','');
INSERT INTO `state` VALUES (4190,235,'Province','Donets\'ka Oblast\'','UA-14','');
INSERT INTO `state` VALUES (4191,235,'Province','Ivano-Frankivs\'ka Oblast\'','UA-26','');
INSERT INTO `state` VALUES (4192,235,'Province','Kharkivs\'ka Oblast\'','UA-63','');
INSERT INTO `state` VALUES (4193,235,'Province','Khersons\'ka Oblast\'','UA-65','');
INSERT INTO `state` VALUES (4194,235,'Province','Khmel\'nyts\'ka Oblast\'','UA-68','');
INSERT INTO `state` VALUES (4195,235,'Province','Kirovohrads\'ka Oblast\'','UA-35','');
INSERT INTO `state` VALUES (4196,235,'Province','Kyïvs\'ka Oblast\'','UA-32','');
INSERT INTO `state` VALUES (4197,235,'Province','Luhans\'ka Oblast\'','UA-09','');
INSERT INTO `state` VALUES (4198,235,'Province','L\'vivs\'ka Oblast\'','UA-46','');
INSERT INTO `state` VALUES (4199,235,'Province','Mykolaïvs\'ka Oblast\'','UA-48','');
INSERT INTO `state` VALUES (4200,235,'Province','Odes\'ka Oblast\'','UA-51','');
INSERT INTO `state` VALUES (4201,235,'Province','Poltavs\'ka Oblast\'','UA-53','');
INSERT INTO `state` VALUES (4202,235,'Province','Rivnens\'ka Oblast\'','UA-56','');
INSERT INTO `state` VALUES (4203,235,'Province','Sums \'ka Oblast\'','UA-59','');
INSERT INTO `state` VALUES (4204,235,'Province','Ternopil\'s\'ka Oblast\'','UA-61','');
INSERT INTO `state` VALUES (4205,235,'Province','Vinnyts\'ka Oblast\'','UA-05','');
INSERT INTO `state` VALUES (4206,235,'Province','Volyns\'ka Oblast\'','UA-07','');
INSERT INTO `state` VALUES (4207,235,'Province','Zakarpats\'ka Oblast\'','UA-21','');
INSERT INTO `state` VALUES (4208,235,'Province','Zaporiz\'ka Oblast\'','UA-23','');
INSERT INTO `state` VALUES (4209,235,'Province','Zhytomyrs\'ka Oblast\'','UA-18','');
INSERT INTO `state` VALUES (4210,235,'Autonomous republic','Respublika Krym','UA-43','');
INSERT INTO `state` VALUES (4211,235,'City','Kyïvs\'ka mis\'ka rada','UA-30','');
INSERT INTO `state` VALUES (4212,235,'City','Sevastopol','UA-40','');
INSERT INTO `state` VALUES (4213,234,'Geographical region','Central','UG C','');
INSERT INTO `state` VALUES (4214,234,'Geographical region','Eastern','UG E','');
INSERT INTO `state` VALUES (4215,234,'Geographical region','Northern','UG N','');
INSERT INTO `state` VALUES (4216,234,'Geographical region','Western','UG W','');
INSERT INTO `state` VALUES (4217,234,'District','Abim','UG-317','');
INSERT INTO `state` VALUES (4218,234,'District','Adjumani','UG-301','');
INSERT INTO `state` VALUES (4219,234,'District','Amolatar','UG-314','');
INSERT INTO `state` VALUES (4220,234,'District','Amuria','UG-216','');
INSERT INTO `state` VALUES (4221,234,'District','Amuru','UG-319','');
INSERT INTO `state` VALUES (4222,234,'District','Apac','UG-302','');
INSERT INTO `state` VALUES (4223,234,'District','Arua','UG-303','');
INSERT INTO `state` VALUES (4224,234,'District','Budaka','UG-217','');
INSERT INTO `state` VALUES (4225,234,'District','Bugiri','UG-201','');
INSERT INTO `state` VALUES (4226,234,'District','Bukwa','UG-218','');
INSERT INTO `state` VALUES (4227,234,'District','Bulisa','UG-419','');
INSERT INTO `state` VALUES (4228,234,'District','Bundibugyo','UG-401','');
INSERT INTO `state` VALUES (4229,234,'District','Bushenyi','UG-402','');
INSERT INTO `state` VALUES (4230,234,'District','Busia','UG-202','');
INSERT INTO `state` VALUES (4231,234,'District','Butaleja','UG-219','');
INSERT INTO `state` VALUES (4232,234,'District','Dokolo','UG-318','');
INSERT INTO `state` VALUES (4233,234,'District','Gulu','UG-304','');
INSERT INTO `state` VALUES (4234,234,'District','Hoima','UG-403','');
INSERT INTO `state` VALUES (4235,234,'District','Ibanda','UG-416','');
INSERT INTO `state` VALUES (4236,234,'District','Iganga','UG-203','');
INSERT INTO `state` VALUES (4237,234,'District','Isingiro','UG-417','');
INSERT INTO `state` VALUES (4238,234,'District','Jinja','UG-204','');
INSERT INTO `state` VALUES (4239,234,'District','Kaabong','UG-315','');
INSERT INTO `state` VALUES (4240,234,'District','Kabale','UG-404','');
INSERT INTO `state` VALUES (4241,234,'District','Kabarole','UG-405','');
INSERT INTO `state` VALUES (4242,234,'District','Kaberamaido','UG-213','');
INSERT INTO `state` VALUES (4243,234,'District','Kalangala','UG-101','');
INSERT INTO `state` VALUES (4244,234,'District','Kaliro','UG-220','');
INSERT INTO `state` VALUES (4245,234,'District','Kampala','UG-102','');
INSERT INTO `state` VALUES (4246,234,'District','Kamuli','UG-205','');
INSERT INTO `state` VALUES (4247,234,'District','Kamwenge','UG-413','');
INSERT INTO `state` VALUES (4248,234,'District','Kanungu','UG-414','');
INSERT INTO `state` VALUES (4249,234,'District','Kapchorwa','UG-206','');
INSERT INTO `state` VALUES (4250,234,'District','Kasese','UG-406','');
INSERT INTO `state` VALUES (4251,234,'District','Katakwi','UG-207','');
INSERT INTO `state` VALUES (4252,234,'District','Kayunga','UG-112','');
INSERT INTO `state` VALUES (4253,234,'District','Kibaale','UG-407','');
INSERT INTO `state` VALUES (4254,234,'District','Kiboga','UG-103','');
INSERT INTO `state` VALUES (4255,234,'District','Kiruhura','UG-418','');
INSERT INTO `state` VALUES (4256,234,'District','Kisoro','UG-408','');
INSERT INTO `state` VALUES (4257,234,'District','Kitgum','UG-305','');
INSERT INTO `state` VALUES (4258,234,'District','Koboko','UG-316','');
INSERT INTO `state` VALUES (4259,234,'District','Kotido','UG-306','');
INSERT INTO `state` VALUES (4260,234,'District','Kumi','UG-208','');
INSERT INTO `state` VALUES (4261,234,'District','Kyenjojo','UG-415','');
INSERT INTO `state` VALUES (4262,234,'District','Lira','UG-307','');
INSERT INTO `state` VALUES (4263,234,'District','Luwero','UG-104','');
INSERT INTO `state` VALUES (4264,234,'District','Manafwa','UG-221','');
INSERT INTO `state` VALUES (4265,234,'District','Maracha','UG-320','');
INSERT INTO `state` VALUES (4266,234,'District','Masaka','UG-105','');
INSERT INTO `state` VALUES (4267,234,'District','Masindi','UG-409','');
INSERT INTO `state` VALUES (4268,234,'District','Mayuge','UG-214','');
INSERT INTO `state` VALUES (4269,234,'District','Mbale','UG-209','');
INSERT INTO `state` VALUES (4270,234,'District','Mbarara','UG-410','');
INSERT INTO `state` VALUES (4271,234,'District','Mityana','UG-114','');
INSERT INTO `state` VALUES (4272,234,'District','Moroto','UG-308','');
INSERT INTO `state` VALUES (4273,234,'District','Moyo','UG-309','');
INSERT INTO `state` VALUES (4274,234,'District','Mpigi','UG-106','');
INSERT INTO `state` VALUES (4275,234,'District','Mubende','UG-107','');
INSERT INTO `state` VALUES (4276,234,'District','Mukono','UG-108','');
INSERT INTO `state` VALUES (4277,234,'District','Nakapiripirit','UG-311','');
INSERT INTO `state` VALUES (4278,234,'District','Nakaseke','UG-115','');
INSERT INTO `state` VALUES (4279,234,'District','Nakasongola','UG-109','');
INSERT INTO `state` VALUES (4280,234,'District','Namutumba','UG-222','');
INSERT INTO `state` VALUES (4281,234,'District','Nebbi','UG-310','');
INSERT INTO `state` VALUES (4282,234,'District','Ntungamo','UG-411','');
INSERT INTO `state` VALUES (4283,234,'District','Oyam','UG-321','');
INSERT INTO `state` VALUES (4284,234,'District','Pader','UG-312','');
INSERT INTO `state` VALUES (4285,234,'District','Pallisa','UG-210','');
INSERT INTO `state` VALUES (4286,234,'District','Rakai','UG-110','');
INSERT INTO `state` VALUES (4287,234,'District','Rukungiri','UG-412','');
INSERT INTO `state` VALUES (4288,234,'District','Sembabule','UG-111','');
INSERT INTO `state` VALUES (4289,234,'District','Sironko','UG-215','');
INSERT INTO `state` VALUES (4290,234,'District','Soroti','UG-211','');
INSERT INTO `state` VALUES (4291,234,'District','Tororo','UG-212','');
INSERT INTO `state` VALUES (4292,234,'District','Wakiso','UG-113','');
INSERT INTO `state` VALUES (4293,234,'District','Yumbe','UG-313','');
INSERT INTO `state` VALUES (4294,239,'Territory','Baker Island','UM-81','');
INSERT INTO `state` VALUES (4295,239,'Territory','Howland Island','UM-84','');
INSERT INTO `state` VALUES (4296,239,'Territory','Jarvis Island','UM-86','');
INSERT INTO `state` VALUES (4297,239,'Territory','Johnston Atoll','UM-67','');
INSERT INTO `state` VALUES (4298,239,'Territory','Kingman Reef','UM-89','');
INSERT INTO `state` VALUES (4299,239,'Territory','Midway Islands','UM-71','');
INSERT INTO `state` VALUES (4300,239,'Territory','Navassa Island','UM-76','');
INSERT INTO `state` VALUES (4301,239,'Territory','Palmyra Atoll','UM-95','');
INSERT INTO `state` VALUES (4302,239,'Territory','Wake Island','UM-79','');
INSERT INTO `state` VALUES (4303,238,'State','Alabama','US-AL','');
INSERT INTO `state` VALUES (4304,238,'State','Alaska','US-AK','');
INSERT INTO `state` VALUES (4305,238,'State','Arizona','US-AZ','');
INSERT INTO `state` VALUES (4306,238,'State','Arkansas','US-AR','');
INSERT INTO `state` VALUES (4307,238,'State','California','US-CA','');
INSERT INTO `state` VALUES (4308,238,'State','Colorado','US-CO','');
INSERT INTO `state` VALUES (4309,238,'State','Connecticut','US-CT','');
INSERT INTO `state` VALUES (4310,238,'State','Delaware','US-DE','');
INSERT INTO `state` VALUES (4311,238,'State','Florida','US-FL','');
INSERT INTO `state` VALUES (4312,238,'State','Georgia','US-GA','');
INSERT INTO `state` VALUES (4313,238,'State','Hawaii','US-HI','');
INSERT INTO `state` VALUES (4314,238,'State','Idaho','US-ID','');
INSERT INTO `state` VALUES (4315,238,'State','Illinois','US-IL','');
INSERT INTO `state` VALUES (4316,238,'State','Indiana','US-IN','');
INSERT INTO `state` VALUES (4317,238,'State','Iowa','US-IA','');
INSERT INTO `state` VALUES (4318,238,'State','Kansas','US-KS','');
INSERT INTO `state` VALUES (4319,238,'State','Kentucky','US-KY','');
INSERT INTO `state` VALUES (4320,238,'State','Louisiana','US-LA','');
INSERT INTO `state` VALUES (4321,238,'State','Maine','US-ME','');
INSERT INTO `state` VALUES (4322,238,'State','Maryland','US-MD','');
INSERT INTO `state` VALUES (4323,238,'State','Massachusetts','US-MA','');
INSERT INTO `state` VALUES (4324,238,'State','Michigan','US-MI','');
INSERT INTO `state` VALUES (4325,238,'State','Minnesota','US-MN','');
INSERT INTO `state` VALUES (4326,238,'State','Mississippi','US-MS','');
INSERT INTO `state` VALUES (4327,238,'State','Missouri','US-MO','');
INSERT INTO `state` VALUES (4328,238,'State','Montana','US-MT','');
INSERT INTO `state` VALUES (4329,238,'State','Nebraska','US-NE','');
INSERT INTO `state` VALUES (4330,238,'State','Nevada','US-NV','');
INSERT INTO `state` VALUES (4331,238,'State','New Hampshire','US-NH','');
INSERT INTO `state` VALUES (4332,238,'State','New Jersey','US-NJ','');
INSERT INTO `state` VALUES (4333,238,'State','New Mexico','US-NM','');
INSERT INTO `state` VALUES (4334,238,'State','New York','US-NY','');
INSERT INTO `state` VALUES (4335,238,'State','North Carolina','US-NC','');
INSERT INTO `state` VALUES (4336,238,'State','North Dakota','US-ND','');
INSERT INTO `state` VALUES (4337,238,'State','Ohio','US-OH','');
INSERT INTO `state` VALUES (4338,238,'State','Oklahoma','US-OK','');
INSERT INTO `state` VALUES (4339,238,'State','Oregon','US-OR','');
INSERT INTO `state` VALUES (4340,238,'State','Pennsylvania','US-PA','');
INSERT INTO `state` VALUES (4341,238,'State','Rhode Island','US-RI','');
INSERT INTO `state` VALUES (4342,238,'State','South Carolina','US-SC','');
INSERT INTO `state` VALUES (4343,238,'State','South Dakota','US-SD','');
INSERT INTO `state` VALUES (4344,238,'State','Tennessee','US-TN','');
INSERT INTO `state` VALUES (4345,238,'State','Texas','US-TX','');
INSERT INTO `state` VALUES (4346,238,'State','Utah','US-UT','');
INSERT INTO `state` VALUES (4347,238,'State','Vermont','US-VT','');
INSERT INTO `state` VALUES (4348,238,'State','Virginia','US-VA','');
INSERT INTO `state` VALUES (4349,238,'State','Washington','US-WA','');
INSERT INTO `state` VALUES (4350,238,'State','West Virginia','US-WV','');
INSERT INTO `state` VALUES (4351,238,'State','Wisconsin','US-WI','');
INSERT INTO `state` VALUES (4352,238,'State','Wyoming','US-WY','');
INSERT INTO `state` VALUES (4353,238,'District','District of Columbia','US-DC','');
INSERT INTO `state` VALUES (4354,238,'Outlying area','American Samoa','US-AS','');
INSERT INTO `state` VALUES (4355,238,'Outlying area','Guam','US-GU','');
INSERT INTO `state` VALUES (4356,238,'Outlying area','Northern Mariana Islands','US-MP','');
INSERT INTO `state` VALUES (4357,238,'Outlying area','Puerto Rico','US-PR','');
INSERT INTO `state` VALUES (4358,238,'Outlying area','United States Minor Outlying Islands','US-UM','');
INSERT INTO `state` VALUES (4359,238,'Outlying area','Virgin Islands','US-VI','');
INSERT INTO `state` VALUES (4360,240,'Department','Artigas','UY-AR','');
INSERT INTO `state` VALUES (4361,240,'Department','Canelones','UY-CA','');
INSERT INTO `state` VALUES (4362,240,'Department','Cerro Largo','UY-CL','');
INSERT INTO `state` VALUES (4363,240,'Department','Colonia','UY-CO','');
INSERT INTO `state` VALUES (4364,240,'Department','Durazno','UY-DU','');
INSERT INTO `state` VALUES (4365,240,'Department','Flores','UY-FS','');
INSERT INTO `state` VALUES (4366,240,'Department','Florida','UY-FD','');
INSERT INTO `state` VALUES (4367,240,'Department','Lavalleja','UY-LA','');
INSERT INTO `state` VALUES (4368,240,'Department','Maldonado','UY-MA','');
INSERT INTO `state` VALUES (4369,240,'Department','Montevideo','UY-MO','');
INSERT INTO `state` VALUES (4370,240,'Department','Paysandú','UY-PA','');
INSERT INTO `state` VALUES (4371,240,'Department','Río Negro','UY-RN','');
INSERT INTO `state` VALUES (4372,240,'Department','Rivera','UY-RV','');
INSERT INTO `state` VALUES (4373,240,'Department','Rocha','UY-RO','');
INSERT INTO `state` VALUES (4374,240,'Department','Salto','UY-SA','');
INSERT INTO `state` VALUES (4375,240,'Department','San José','UY-SJ','');
INSERT INTO `state` VALUES (4376,240,'Department','Soriano','UY-SO','');
INSERT INTO `state` VALUES (4377,240,'Department','Tacuarembó','UY-TA','');
INSERT INTO `state` VALUES (4378,240,'Department','Treinta y Tres','UY-TT','');
INSERT INTO `state` VALUES (4379,241,'City','Toshkent','UZ-TK','');
INSERT INTO `state` VALUES (4380,241,'Region','Andijon','UZ-AN','');
INSERT INTO `state` VALUES (4381,241,'Region','Buxoro','UZ-BU','');
INSERT INTO `state` VALUES (4382,241,'Region','Farg\'ona','UZ-FA','');
INSERT INTO `state` VALUES (4383,241,'Region','Jizzax','UZ-JI','');
INSERT INTO `state` VALUES (4384,241,'Region','Namangan','UZ-NG','');
INSERT INTO `state` VALUES (4385,241,'Region','Navoiy','UZ-NW','');
INSERT INTO `state` VALUES (4386,241,'Region','Qashqadaryo','UZ-QA','');
INSERT INTO `state` VALUES (4387,241,'Region','Samarqand','UZ-SA','');
INSERT INTO `state` VALUES (4388,241,'Region','Sirdaryo','UZ-SI','');
INSERT INTO `state` VALUES (4389,241,'Region','Surxondaryo','UZ-SU','');
INSERT INTO `state` VALUES (4390,241,'Region','Toshkent','UZ-TO','');
INSERT INTO `state` VALUES (4391,241,'Region','Xorazm','UZ-XO','');
INSERT INTO `state` VALUES (4392,241,'Republic','Qoraqalpog\'iston Respublikasi','UZ-QR','');
INSERT INTO `state` VALUES (4393,194,'Parish','Charlotte','VC-01','');
INSERT INTO `state` VALUES (4394,194,'Parish','Grenadines','VC-06','');
INSERT INTO `state` VALUES (4395,194,'Parish','Saint Andrew','VC-02','');
INSERT INTO `state` VALUES (4396,194,'Parish','Saint David','VC-03','');
INSERT INTO `state` VALUES (4397,194,'Parish','Saint George','VC-04','');
INSERT INTO `state` VALUES (4398,194,'Parish','Saint Patrick','VC-05','');
INSERT INTO `state` VALUES (4399,244,'Federal Dependency','Dependencias Federales','VE-W','');
INSERT INTO `state` VALUES (4400,244,'Federal District','Distrito Federal','VE-A','');
INSERT INTO `state` VALUES (4401,244,'State','Amazonas','VE-Z','');
INSERT INTO `state` VALUES (4402,244,'State','Anzoátegui','VE-B','');
INSERT INTO `state` VALUES (4403,244,'State','Apure','VE-C','');
INSERT INTO `state` VALUES (4404,244,'State','Aragua','VE-D','');
INSERT INTO `state` VALUES (4405,244,'State','Barinas','VE-E','');
INSERT INTO `state` VALUES (4406,244,'State','Bolívar','VE-F','');
INSERT INTO `state` VALUES (4407,244,'State','Carabobo','VE-G','');
INSERT INTO `state` VALUES (4408,244,'State','Cojedes','VE-H','');
INSERT INTO `state` VALUES (4409,244,'State','Delta Amacuro','VE-Y','');
INSERT INTO `state` VALUES (4410,244,'State','Falcón','VE-I','');
INSERT INTO `state` VALUES (4411,244,'State','Guárico','VE-J','');
INSERT INTO `state` VALUES (4412,244,'State','Lara','VE-K','');
INSERT INTO `state` VALUES (4413,244,'State','Mérida','VE-L','');
INSERT INTO `state` VALUES (4414,244,'State','Miranda','VE-M','');
INSERT INTO `state` VALUES (4415,244,'State','Monagas','VE-N','');
INSERT INTO `state` VALUES (4416,244,'State','Nueva Esparta','VE-O','');
INSERT INTO `state` VALUES (4417,244,'State','Portuguesa','VE-P','');
INSERT INTO `state` VALUES (4418,244,'State','Sucre','VE-R','');
INSERT INTO `state` VALUES (4419,244,'State','Táchira','VE-S','');
INSERT INTO `state` VALUES (4420,244,'State','Trujillo','VE-T','');
INSERT INTO `state` VALUES (4421,244,'State','Vargas','VE-X','');
INSERT INTO `state` VALUES (4422,244,'State','Yaracuy','VE-U','');
INSERT INTO `state` VALUES (4423,244,'State','Zulia','VE-V','');
INSERT INTO `state` VALUES (4424,245,'Province','An Giang','VN-44','');
INSERT INTO `state` VALUES (4425,245,'Province','Bà R?a - V?ng Tàu','VN-43','');
INSERT INTO `state` VALUES (4426,245,'Province','B?c K?n','VN-53','');
INSERT INTO `state` VALUES (4427,245,'Province','B?c Giang','VN-54','');
INSERT INTO `state` VALUES (4428,245,'Province','B?c Liêu','VN-55','');
INSERT INTO `state` VALUES (4429,245,'Province','B?c Ninh','VN-56','');
INSERT INTO `state` VALUES (4430,245,'Province','B?n Tre','VN-50','');
INSERT INTO `state` VALUES (4431,245,'Province','Bình ??nh','VN-31','');
INSERT INTO `state` VALUES (4432,245,'Province','Bình D??ng','VN-57','');
INSERT INTO `state` VALUES (4433,245,'Province','Bình Ph??c','VN-58','');
INSERT INTO `state` VALUES (4434,245,'Province','Bình Thu?n','VN-40','');
INSERT INTO `state` VALUES (4435,245,'Province','Cà Mau','VN-59','');
INSERT INTO `state` VALUES (4436,245,'Province','C?n Th?','VN-48','');
INSERT INTO `state` VALUES (4437,245,'Province','Cao B?ng','VN-04','');
INSERT INTO `state` VALUES (4438,245,'Province','?à N?ng, thành ph?','VN-60','');
INSERT INTO `state` VALUES (4439,245,'Province','??c L?k','VN-33','');
INSERT INTO `state` VALUES (4440,245,'Province','??k Nông','VN-72','');
INSERT INTO `state` VALUES (4441,245,'Province','?i?n Biên','VN-71','');
INSERT INTO `state` VALUES (4442,245,'Province','??ng Nai','VN-39','');
INSERT INTO `state` VALUES (4443,245,'Province','??ng Tháp','VN-45','');
INSERT INTO `state` VALUES (4444,245,'Province','Gia Lai','VN-30','');
INSERT INTO `state` VALUES (4445,245,'Province','Hà Giang','VN-03','');
INSERT INTO `state` VALUES (4446,245,'Province','Hà Nam','VN-63','');
INSERT INTO `state` VALUES (4447,245,'Province','Hà N?i, th? ?ô','VN-64','');
INSERT INTO `state` VALUES (4448,245,'Province','Hà Tây','VN-15','');
INSERT INTO `state` VALUES (4449,245,'Province','Hà T?nh','VN-23','');
INSERT INTO `state` VALUES (4450,245,'Province','H?i Duong','VN-61','');
INSERT INTO `state` VALUES (4451,245,'Province','H?i Phòng, thành ph?','VN-62','');
INSERT INTO `state` VALUES (4452,245,'Province','H?u Giang','VN-73','');
INSERT INTO `state` VALUES (4453,245,'Province','Hoà Bình','VN-14','');
INSERT INTO `state` VALUES (4454,245,'Province','H? Chí Minh, thành ph? [Sài Gòn]','VN-65','');
INSERT INTO `state` VALUES (4455,245,'Province','H?ng Yên','VN-66','');
INSERT INTO `state` VALUES (4456,245,'Province','Khánh Hòa','VN-34','');
INSERT INTO `state` VALUES (4457,245,'Province','Kiên Giang','VN-47','');
INSERT INTO `state` VALUES (4458,245,'Province','Kon Tum','VN-28','');
INSERT INTO `state` VALUES (4459,245,'Province','Lai Châu','VN-01','');
INSERT INTO `state` VALUES (4460,245,'Province','Lâm ??ng','VN-35','');
INSERT INTO `state` VALUES (4461,245,'Province','L?ng S?n','VN-09','');
INSERT INTO `state` VALUES (4462,245,'Province','Lào Cai','VN-02','');
INSERT INTO `state` VALUES (4463,245,'Province','Long An','VN-41','');
INSERT INTO `state` VALUES (4464,245,'Province','Nam ??nh','VN-67','');
INSERT INTO `state` VALUES (4465,245,'Province','Ngh? An','VN-22','');
INSERT INTO `state` VALUES (4466,245,'Province','Ninh Bình','VN-18','');
INSERT INTO `state` VALUES (4467,245,'Province','Ninh Thu?n','VN-36','');
INSERT INTO `state` VALUES (4468,245,'Province','Phú Th?','VN-68','');
INSERT INTO `state` VALUES (4469,245,'Province','Phú Yên','VN-32','');
INSERT INTO `state` VALUES (4470,245,'Province','Qu?ng Bình','VN-24','');
INSERT INTO `state` VALUES (4471,245,'Province','Qu?ng Nam','VN-27','');
INSERT INTO `state` VALUES (4472,245,'Province','Qu?ng Ngãi','VN-29','');
INSERT INTO `state` VALUES (4473,245,'Province','Qu?ng Ninh','VN-13','');
INSERT INTO `state` VALUES (4474,245,'Province','Qu?ng Tr?','VN-25','');
INSERT INTO `state` VALUES (4475,245,'Province','Sóc Tr?ng','VN-52','');
INSERT INTO `state` VALUES (4476,245,'Province','S?n La','VN-05','');
INSERT INTO `state` VALUES (4477,245,'Province','Tây Ninh','VN-37','');
INSERT INTO `state` VALUES (4478,245,'Province','Thái Bình','VN-20','');
INSERT INTO `state` VALUES (4479,245,'Province','Thái Nguyên','VN-69','');
INSERT INTO `state` VALUES (4480,245,'Province','Thanh Hóa','VN-21','');
INSERT INTO `state` VALUES (4481,245,'Province','Th?a Thiên-Hu?','VN-26','');
INSERT INTO `state` VALUES (4482,245,'Province','Ti?n Giang','VN-46','');
INSERT INTO `state` VALUES (4483,245,'Province','Trà Vinh','VN-51','');
INSERT INTO `state` VALUES (4484,245,'Province','Tuyên Quang','VN-07','');
INSERT INTO `state` VALUES (4485,245,'Province','V?nh Long','VN-49','');
INSERT INTO `state` VALUES (4486,245,'Province','V?nh Phúc','VN-70','');
INSERT INTO `state` VALUES (4487,245,'Province','Yên Bái','VN-06','');
INSERT INTO `state` VALUES (4488,242,'Province','Malampa','VU-MAP','');
INSERT INTO `state` VALUES (4489,242,'Province','Pénama','VU-PAM','');
INSERT INTO `state` VALUES (4490,242,'Province','Sanma','VU-SAM','');
INSERT INTO `state` VALUES (4491,242,'Province','Shéfa','VU-SEE','');
INSERT INTO `state` VALUES (4492,242,'Province','Taféa','VU-TAE','');
INSERT INTO `state` VALUES (4493,242,'Province','Torba','VU-TOB','');
INSERT INTO `state` VALUES (4494,195,'District','A\'ana','WS-AA','');
INSERT INTO `state` VALUES (4495,195,'District','Aiga-i-le-Tai','WS-AL','');
INSERT INTO `state` VALUES (4496,195,'District','Atua','WS-AT','');
INSERT INTO `state` VALUES (4497,195,'District','Fa\'asaleleaga','WS-FA','');
INSERT INTO `state` VALUES (4498,195,'District','Gaga\'emauga','WS-GE','');
INSERT INTO `state` VALUES (4499,195,'District','Gagaifomauga','WS-GI','');
INSERT INTO `state` VALUES (4500,195,'District','Palauli','WS-PA','');
INSERT INTO `state` VALUES (4501,195,'District','Satupa\'itea','WS-SA','');
INSERT INTO `state` VALUES (4502,195,'District','Tuamasaga','WS-TU','');
INSERT INTO `state` VALUES (4503,195,'District','Va\'a-o-Fonoti','WS-VF','');
INSERT INTO `state` VALUES (4504,195,'District','Vaisigano','WS-VS','');
INSERT INTO `state` VALUES (4505,250,'Governorate','Aby?n','YE-AB','');
INSERT INTO `state` VALUES (4506,250,'Governorate','\'Adan','YE-AD','');
INSERT INTO `state` VALUES (4507,250,'Governorate','A? ??li\'','YE-DA','');
INSERT INTO `state` VALUES (4508,250,'Governorate','Al Bay??\'','YE-BA','');
INSERT INTO `state` VALUES (4509,250,'Governorate','Al ?udaydah','YE-MU','');
INSERT INTO `state` VALUES (4510,250,'Governorate','Al Jawf','YE-JA','');
INSERT INTO `state` VALUES (4511,250,'Governorate','Al Mahrah','YE-MR','');
INSERT INTO `state` VALUES (4512,250,'Governorate','Al Ma?w?t','YE-MW','');
INSERT INTO `state` VALUES (4513,250,'Governorate','\'Amr?n','YE-AM','');
INSERT INTO `state` VALUES (4514,250,'Governorate','Dham?r','YE-DH','');
INSERT INTO `state` VALUES (4515,250,'Governorate','?a?ramawt','YE-HD','');
INSERT INTO `state` VALUES (4516,250,'Governorate','?ajjah','YE-HJ','');
INSERT INTO `state` VALUES (4517,250,'Governorate','Ibb','YE-IB','');
INSERT INTO `state` VALUES (4518,250,'Governorate','La?ij','YE-LA','');
INSERT INTO `state` VALUES (4519,250,'Governorate','Ma\'rib','YE-MA','');
INSERT INTO `state` VALUES (4520,250,'Governorate','?a\'dah','YE-SD','');
INSERT INTO `state` VALUES (4521,250,'Governorate','?an\'?\'','YE-SN','');
INSERT INTO `state` VALUES (4522,250,'Governorate','Shabwah','YE-SH','');
INSERT INTO `state` VALUES (4523,250,'Governorate','T?\'izz','YE-TA','');
INSERT INTO `state` VALUES (4524,209,'Province','Eastern Cape','ZA-EC','');
INSERT INTO `state` VALUES (4525,209,'Province','Free State','ZA-FS','');
INSERT INTO `state` VALUES (4526,209,'Province','Gauteng','ZA-GT','');
INSERT INTO `state` VALUES (4527,209,'Province','Kwazulu-Natal','ZA-NL','');
INSERT INTO `state` VALUES (4528,209,'Province','Limpopo','ZA-LP','');
INSERT INTO `state` VALUES (4529,209,'Province','Mpumalanga','ZA-MP','');
INSERT INTO `state` VALUES (4530,209,'Province','Northern Cape','ZA-NC','');
INSERT INTO `state` VALUES (4531,209,'Province','North-West (South Africa)','ZA-NW','');
INSERT INTO `state` VALUES (4532,209,'Province','Western Cape','ZA-WC','');
INSERT INTO `state` VALUES (4533,251,'Province','Central','ZM-02','');
INSERT INTO `state` VALUES (4534,251,'Province','Copperbelt','ZM-08','');
INSERT INTO `state` VALUES (4535,251,'Province','Eastern','ZM-03','');
INSERT INTO `state` VALUES (4536,251,'Province','Luapula','ZM-04','');
INSERT INTO `state` VALUES (4537,251,'Province','Lusaka','ZM-09','');
INSERT INTO `state` VALUES (4538,251,'Province','Northern','ZM-05','');
INSERT INTO `state` VALUES (4539,251,'Province','North-Western','ZM-06','');
INSERT INTO `state` VALUES (4540,251,'Province','Southern (Zambia)','ZM-07','');
INSERT INTO `state` VALUES (4541,251,'Province','Western','ZM-01','');
INSERT INTO `state` VALUES (4542,252,'City','Bulawayo','ZW-BU','');
INSERT INTO `state` VALUES (4543,252,'City','Harare','ZW-HA','');
INSERT INTO `state` VALUES (4544,252,'Province','Manicaland','ZW-MA','');
INSERT INTO `state` VALUES (4545,252,'Province','Mashonaland Central','ZW-MC','');
INSERT INTO `state` VALUES (4546,252,'Province','Mashonaland East','ZW-ME','');
INSERT INTO `state` VALUES (4547,252,'Province','Mashonaland West','ZW-MW','');
INSERT INTO `state` VALUES (4548,252,'Province','Masvingo','ZW-MV','');
INSERT INTO `state` VALUES (4549,252,'Province','Matabeleland North','ZW-MN','');
INSERT INTO `state` VALUES (4550,252,'Province','Matabeleland South','ZW-MS','');
INSERT INTO `state` VALUES (4551,252,'Province','Midlands','ZW-MI','');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study`
--

DROP TABLE IF EXISTS `study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(150) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DATE_OF_APPLICATION` date DEFAULT NULL,
  `ESTIMATED_YEAR_OF_COMPLETION` int(11) DEFAULT NULL,
  `CHIEF_INVESTIGATOR` varchar(50) DEFAULT NULL,
  `CO_INVESTIGATOR` varchar(50) DEFAULT NULL,
  `AUTO_GENERATE_SUBJECTUID` int(11) NOT NULL,
  `SUBJECTUID_START` int(11) DEFAULT NULL,
  `STUDY_STATUS_ID` int(11) NOT NULL,
  `SUBJECTUID_PREFIX` varchar(20) DEFAULT NULL,
  `CONTACT_PERSON` varchar(50) DEFAULT NULL,
  `CONTACT_PERSON_PHONE` varchar(20) DEFAULT NULL,
  `LDAP_GROUP_NAME` varchar(100) DEFAULT NULL,
  `AUTO_CONSENT` int(11) DEFAULT NULL,
  `SUB_STUDY_BIOSPECIMEN_PREFIX` varchar(20) DEFAULT NULL,
  `STUDY_LOGO` blob,
  `FILENAME` varchar(255) DEFAULT NULL,
  `SUBJECTUID_TOKEN_ID` int(11) DEFAULT NULL,
  `SUBJECTUID_PADCHAR_ID` int(11) DEFAULT NULL,
  `SUBJECT_KEY_PREFIX` varchar(45) DEFAULT NULL,
  `SUBJECT_KEY_START` varchar(45) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `AUTO_GENERATE_BIOSPECIMENUID` tinyint(4) NOT NULL DEFAULT '0',
  `AUTO_GENERATE_BIOCOLLECTIONUID` tinyint(4) NOT NULL DEFAULT '0',
  `SAVE_TO_PARENT` tinyint(1) NOT NULL DEFAULT '0',
  `SAVE_TO_PARENT_LOCKED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `STUDY_STUDY_STATUS_FK1` (`STUDY_STATUS_ID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE,
  KEY `fk_study_subjectuid_padchar` (`SUBJECTUID_PADCHAR_ID`),
  KEY `fk_study_subjectuid_token` (`SUBJECTUID_TOKEN_ID`),
  KEY `fk_study_study` (`PARENT_ID`),
  CONSTRAINT `fk_study_study` FOREIGN KEY (`PARENT_ID`) REFERENCES `study` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_study_status` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`),
  CONSTRAINT `fk_study_subjectuid_padchar` FOREIGN KEY (`SUBJECTUID_PADCHAR_ID`) REFERENCES `subjectuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_subjectuid_token` FOREIGN KEY (`SUBJECTUID_TOKEN_ID`) REFERENCES `subjectuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_STATUS_ID`) REFER `study/study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study`
--

LOCK TABLES `study` WRITE;
/*!40000 ALTER TABLE `study` DISABLE KEYS */;
/*!40000 ALTER TABLE `study` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_comp`
--

DROP TABLE IF EXISTS `study_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`),
  KEY `STUDY_COMP_STUDY_FK` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `study_comp_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_comp`
--

LOCK TABLES `study_comp` WRITE;
/*!40000 ALTER TABLE `study_comp` DISABLE KEYS */;
/*!40000 ALTER TABLE `study_comp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_comp_status`
--

DROP TABLE IF EXISTS `study_comp_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comp_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_comp_status`
--

LOCK TABLES `study_comp_status` WRITE;
/*!40000 ALTER TABLE `study_comp_status` DISABLE KEYS */;
INSERT INTO `study_comp_status` VALUES (1,'Completed',NULL);
INSERT INTO `study_comp_status` VALUES (2,'Not Completed',NULL);
INSERT INTO `study_comp_status` VALUES (3,'Not Needed',NULL);
INSERT INTO `study_comp_status` VALUES (4,'Not Available',NULL);
INSERT INTO `study_comp_status` VALUES (5,'Pending',NULL);
INSERT INTO `study_comp_status` VALUES (6,'Received',NULL);
INSERT INTO `study_comp_status` VALUES (7,'Requested',NULL);
INSERT INTO `study_comp_status` VALUES (8,'Refused',NULL);
INSERT INTO `study_comp_status` VALUES (9,'Unknown',NULL);
/*!40000 ALTER TABLE `study_comp_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_consent_question`
--

DROP TABLE IF EXISTS `study_consent_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_consent_question` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION` varchar(700) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `DATA_TYPE_ID` int(11) DEFAULT NULL,
  `DISCRETE_VALUES` varchar(45) DEFAULT NULL,
  `FIELD_POSITION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_study_consent_question_1` (`STUDY_ID`) USING BTREE,
  KEY `fk_study_consent_question_2` (`DATA_TYPE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_consent_question`
--

LOCK TABLES `study_consent_question` WRITE;
/*!40000 ALTER TABLE `study_consent_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `study_consent_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_pedigree_config`
--

DROP TABLE IF EXISTS `study_pedigree_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_pedigree_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `study_id` int(11) DEFAULT NULL,
  `custom_field_id` int(11) DEFAULT NULL,
  `dob_allowed` tinyint(4) DEFAULT NULL,
  `status_allowed` tinyint(4) DEFAULT NULL,
  `age_allowed` tinyint(4) DEFAULT NULL,
  `family_id` int(11) DEFAULT NULL,
  `INBREED_ALLOWED` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_study_pedigree_config_1_idx` (`study_id`),
  KEY `fk_study_pedigree_config_custom_field_idx` (`custom_field_id`),
  KEY `fk_study_pedigree_config_family_id_idx` (`family_id`),
  CONSTRAINT `fk_study_pedigree_config_custom_field` FOREIGN KEY (`custom_field_id`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_pedigree_config_family_id` FOREIGN KEY (`family_id`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_pedigree_config_study` FOREIGN KEY (`study_id`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_pedigree_config`
--

LOCK TABLES `study_pedigree_config` WRITE;
/*!40000 ALTER TABLE `study_pedigree_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `study_pedigree_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_site`
--

DROP TABLE IF EXISTS `study_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ADDRESS_ID` int(11) NOT NULL,
  `DOMAIN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STUDY_SITE_ADDRES_FK1` (`ADDRESS_ID`) USING BTREE,
  KEY `STUDY_SITE_DOMAIN_TYPE_FK` (`DOMAIN_TYPE_ID`) USING BTREE,
  CONSTRAINT `study_site_ibfk_1` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `study_site_ibfk_2` FOREIGN KEY (`DOMAIN_TYPE_ID`) REFERENCES `domain_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_ID`) REFER `study/address`(`';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_site`
--

LOCK TABLES `study_site` WRITE;
/*!40000 ALTER TABLE `study_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `study_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_status`
--

DROP TABLE IF EXISTS `study_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_status`
--

LOCK TABLES `study_status` WRITE;
/*!40000 ALTER TABLE `study_status` DISABLE KEYS */;
INSERT INTO `study_status` VALUES (1,'Active',NULL);
INSERT INTO `study_status` VALUES (2,'Discussion',NULL);
INSERT INTO `study_status` VALUES (3,'EOI',NULL);
INSERT INTO `study_status` VALUES (4,'Full Application',NULL);
INSERT INTO `study_status` VALUES (5,'Ethics',NULL);
INSERT INTO `study_status` VALUES (6,'Dispute Recorded',NULL);
INSERT INTO `study_status` VALUES (7,'Approved',NULL);
INSERT INTO `study_status` VALUES (8,'Active-Recruiting',NULL);
INSERT INTO `study_status` VALUES (9,'Active-Ongoing Programme',NULL);
INSERT INTO `study_status` VALUES (10,'Active-Data Analysis',NULL);
INSERT INTO `study_status` VALUES (11,'Active-Writing Up',NULL);
INSERT INTO `study_status` VALUES (12,'Unsuccessful Funding',NULL);
INSERT INTO `study_status` VALUES (13,'EOI-Rejected',NULL);
INSERT INTO `study_status` VALUES (14,'EOI-Abandoned',NULL);
INSERT INTO `study_status` VALUES (15,'Archive',NULL);
/*!40000 ALTER TABLE `study_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `study_user_role_permission_view`
--

DROP TABLE IF EXISTS `study_user_role_permission_view`;
/*!50001 DROP VIEW IF EXISTS `study_user_role_permission_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `study_user_role_permission_view` (
  `studyName` tinyint NOT NULL,
  `userName` tinyint NOT NULL,
  `roleName` tinyint NOT NULL,
  `moduleName` tinyint NOT NULL,
  `create` tinyint NOT NULL,
  `read` tinyint NOT NULL,
  `update` tinyint NOT NULL,
  `delete` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subject_custom_field_data`
--

DROP TABLE IF EXISTS `subject_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UQ_SCFD_CFD_LSS` (`LINK_SUBJECT_STUDY_ID`,`CUSTOM_FIELD_DISPLAY_ID`) USING BTREE,
  KEY `FK_CFD_LINK_SUBJECT_STUDY_ID` (`LINK_SUBJECT_STUDY_ID`),
  KEY `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_custom_field_data`
--

LOCK TABLES `subject_custom_field_data` WRITE;
/*!40000 ALTER TABLE `subject_custom_field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_custom_field_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject_file`
--

DROP TABLE IF EXISTS `subject_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) DEFAULT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  `IS_CONSENT_FILE` tinyint(4) NOT NULL DEFAULT '0',
  `FILE_ID` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_subject_file_subject` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  KEY `fk_subject_file_study_comp` (`STUDY_COMP_ID`) USING BTREE,
  CONSTRAINT `fk_subject_file_study_comp` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_file_subject` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_file`
--

LOCK TABLES `subject_file` WRITE;
/*!40000 ALTER TABLE `subject_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject_status`
--

DROP TABLE IF EXISTS `subject_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_status`
--

LOCK TABLES `subject_status` WRITE;
/*!40000 ALTER TABLE `subject_status` DISABLE KEYS */;
INSERT INTO `subject_status` VALUES (1,'Subject',NULL);
INSERT INTO `subject_status` VALUES (2,'Prospect',NULL);
INSERT INTO `subject_status` VALUES (3,'Withdrawn Subject',NULL);
INSERT INTO `subject_status` VALUES (4,'Archive',NULL);
INSERT INTO `subject_status` VALUES (5,'Inactive',NULL);
/*!40000 ALTER TABLE `subject_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject_study_consent`
--

DROP TABLE IF EXISTS `subject_study_consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_study_consent` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `STUDY_CONSENT_QUESTION_ID` int(11) DEFAULT NULL,
  `STATUS` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_subject_study_consent_1` (`STUDY_ID`) USING BTREE,
  KEY `fk_subject_study_consent_2` (`SUBJECT_ID`) USING BTREE,
  KEY `fk_subject_study_consent_3` (`STUDY_CONSENT_QUESTION_ID`) USING BTREE,
  CONSTRAINT `fk_subject_study_consent_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_study_consent_2` FOREIGN KEY (`SUBJECT_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_study_consent_3` FOREIGN KEY (`STUDY_CONSENT_QUESTION_ID`) REFERENCES `study_consent_question` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_study_consent`
--

LOCK TABLES `subject_study_consent` WRITE;
/*!40000 ALTER TABLE `subject_study_consent` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_study_consent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjectuid_padchar`
--

DROP TABLE IF EXISTS `subjectuid_padchar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_padchar` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjectuid_padchar`
--

LOCK TABLES `subjectuid_padchar` WRITE;
/*!40000 ALTER TABLE `subjectuid_padchar` DISABLE KEYS */;
INSERT INTO `subjectuid_padchar` VALUES (1,'1');
INSERT INTO `subjectuid_padchar` VALUES (2,'2');
INSERT INTO `subjectuid_padchar` VALUES (3,'3');
INSERT INTO `subjectuid_padchar` VALUES (4,'4');
INSERT INTO `subjectuid_padchar` VALUES (5,'5');
INSERT INTO `subjectuid_padchar` VALUES (6,'6');
INSERT INTO `subjectuid_padchar` VALUES (7,'7');
INSERT INTO `subjectuid_padchar` VALUES (8,'8');
INSERT INTO `subjectuid_padchar` VALUES (9,'9');
INSERT INTO `subjectuid_padchar` VALUES (10,'10');
/*!40000 ALTER TABLE `subjectuid_padchar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjectuid_sequence`
--

DROP TABLE IF EXISTS `subjectuid_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjectuid_sequence`
--

LOCK TABLES `subjectuid_sequence` WRITE;
/*!40000 ALTER TABLE `subjectuid_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `subjectuid_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjectuid_token`
--

DROP TABLE IF EXISTS `subjectuid_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjectuid_token`
--

LOCK TABLES `subjectuid_token` WRITE;
/*!40000 ALTER TABLE `subjectuid_token` DISABLE KEYS */;
INSERT INTO `subjectuid_token` VALUES (1,'-');
INSERT INTO `subjectuid_token` VALUES (2,'@');
INSERT INTO `subjectuid_token` VALUES (3,'#');
INSERT INTO `subjectuid_token` VALUES (4,':');
INSERT INTO `subjectuid_token` VALUES (5,'*');
INSERT INTO `subjectuid_token` VALUES (6,'|');
INSERT INTO `subjectuid_token` VALUES (7,'_');
INSERT INTO `subjectuid_token` VALUES (8,'+');
/*!40000 ALTER TABLE `subjectuid_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `title_type`
--

DROP TABLE IF EXISTS `title_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `title_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `title_type`
--

LOCK TABLES `title_type` WRITE;
/*!40000 ALTER TABLE `title_type` DISABLE KEYS */;
INSERT INTO `title_type` VALUES (0,'Unknown',NULL);
INSERT INTO `title_type` VALUES (1,'Br',NULL);
INSERT INTO `title_type` VALUES (2,'Capt','Captain');
INSERT INTO `title_type` VALUES (3,'Col',NULL);
INSERT INTO `title_type` VALUES (4,'Cpl',NULL);
INSERT INTO `title_type` VALUES (5,'Dean',NULL);
INSERT INTO `title_type` VALUES (6,'Dr',NULL);
INSERT INTO `title_type` VALUES (7,'Fr',NULL);
INSERT INTO `title_type` VALUES (8,'Lac',NULL);
INSERT INTO `title_type` VALUES (9,'Major',NULL);
INSERT INTO `title_type` VALUES (10,'Miss',NULL);
INSERT INTO `title_type` VALUES (11,'Mr',NULL);
INSERT INTO `title_type` VALUES (12,'Mrs',NULL);
INSERT INTO `title_type` VALUES (13,'Ms',NULL);
INSERT INTO `title_type` VALUES (14,'Past',NULL);
INSERT INTO `title_type` VALUES (15,'Prof',NULL);
INSERT INTO `title_type` VALUES (16,'Pstr',NULL);
INSERT INTO `title_type` VALUES (17,'Rev',NULL);
INSERT INTO `title_type` VALUES (18,'Sir',NULL);
INSERT INTO `title_type` VALUES (19,'Sr',NULL);
INSERT INTO `title_type` VALUES (20,'Lady',NULL);
INSERT INTO `title_type` VALUES (21,'Sen','Senator');
INSERT INTO `title_type` VALUES (22,'Hons','Hons');
/*!40000 ALTER TABLE `title_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `twin_type`
--

DROP TABLE IF EXISTS `twin_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `twin_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `twin_type`
--

LOCK TABLES `twin_type` WRITE;
/*!40000 ALTER TABLE `twin_type` DISABLE KEYS */;
INSERT INTO `twin_type` VALUES (1,'MZ','Monozygotic');
INSERT INTO `twin_type` VALUES (2,'DZ','Dizygotic');
/*!40000 ALTER TABLE `twin_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit_type`
--

DROP TABLE IF EXISTS `unit_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARK_FUNCTION_ID` int(11) DEFAULT NULL,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `MEASUREMENT_TYPE_ID` int(11) DEFAULT NULL,
  `DISPLAY_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_ARK_FUNCTION_UNIQUE` (`NAME`,`ARK_FUNCTION_ID`),
  KEY `FK_UNIT_TYPE_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_UNIT_TYPE_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit_type`
--

LOCK TABLES `unit_type` WRITE;
/*!40000 ALTER TABLE `unit_type` DISABLE KEYS */;
INSERT INTO `unit_type` VALUES (1,NULL,'mm','Millimetres (mm)',1,1);
INSERT INTO `unit_type` VALUES (2,NULL,'cm','Centimetres (cm)',1,2);
INSERT INTO `unit_type` VALUES (3,NULL,'m','Metres (m)',1,4);
INSERT INTO `unit_type` VALUES (4,NULL,'g','Grams (g)',4,1);
INSERT INTO `unit_type` VALUES (5,NULL,'kg','Kilograms (kg)',4,2);
INSERT INTO `unit_type` VALUES (6,NULL,'L','Litres (L)',2,1);
INSERT INTO `unit_type` VALUES (7,NULL,'Days','Days',3,6);
INSERT INTO `unit_type` VALUES (8,NULL,'Months','Months',3,7);
INSERT INTO `unit_type` VALUES (9,NULL,'Years','Years',3,8);
INSERT INTO `unit_type` VALUES (10,NULL,'hrs','Hours (hrs)',3,5);
INSERT INTO `unit_type` VALUES (11,NULL,'min','Minutes (min)',3,4);
INSERT INTO `unit_type` VALUES (12,NULL,'s','Seconds (s)',3,1);
INSERT INTO `unit_type` VALUES (13,18,'ug/L','ug/L',5,1);
INSERT INTO `unit_type` VALUES (14,18,'bpm','bpm',999,NULL);
INSERT INTO `unit_type` VALUES (15,18,'g/L','g/L',5,4);
INSERT INTO `unit_type` VALUES (16,18,'fL','fL',999,NULL);
INSERT INTO `unit_type` VALUES (17,18,'feet','feet',1,3);
INSERT INTO `unit_type` VALUES (18,18,'IU/L','IU/L',999,NULL);
INSERT INTO `unit_type` VALUES (19,18,'U','U',999,NULL);
INSERT INTO `unit_type` VALUES (20,18,'Age','Age',3,9);
INSERT INTO `unit_type` VALUES (21,18,'m/L','m/L',999,NULL);
INSERT INTO `unit_type` VALUES (22,18,'pg','pg',999,NULL);
INSERT INTO `unit_type` VALUES (23,18,'pred','pred',999,NULL);
INSERT INTO `unit_type` VALUES (24,18,'Gy','Gy',999,NULL);
INSERT INTO `unit_type` VALUES (25,18,'%','%',7,1);
INSERT INTO `unit_type` VALUES (26,18,'mS','mS',999,NULL);
INSERT INTO `unit_type` VALUES (27,18,'mm/hr','mm/hr',6,1);
INSERT INTO `unit_type` VALUES (28,18,'mg/dl','mg/dl',5,2);
INSERT INTO `unit_type` VALUES (29,18,'mn','mn',999,NULL);
INSERT INTO `unit_type` VALUES (30,18,'mg/L','mg/L',5,3);
INSERT INTO `unit_type` VALUES (31,18,'kgm2','kgm2',999,NULL);
INSERT INTO `unit_type` VALUES (32,18,'mm Hg','mm Hg',999,NULL);
INSERT INTO `unit_type` VALUES (33,18,'kg/m2','kg/m2',5,6);
INSERT INTO `unit_type` VALUES (34,18,'Pipes','Pipes',999,NULL);
INSERT INTO `unit_type` VALUES (35,18,'S','S',3,2);
INSERT INTO `unit_type` VALUES (36,18,'mm/hg','mm/hg',999,NULL);
INSERT INTO `unit_type` VALUES (37,19,'ug/L','ug/L',5,1);
INSERT INTO `unit_type` VALUES (38,19,'bpm','bpm',999,NULL);
INSERT INTO `unit_type` VALUES (39,19,'g/L','g/L',5,4);
INSERT INTO `unit_type` VALUES (40,19,'fL','fL',999,NULL);
INSERT INTO `unit_type` VALUES (41,19,'feet','feet',1,3);
INSERT INTO `unit_type` VALUES (42,19,'IU/L','IU/L',999,NULL);
INSERT INTO `unit_type` VALUES (43,19,'U','U',999,NULL);
INSERT INTO `unit_type` VALUES (44,19,'Age','Age',3,10);
INSERT INTO `unit_type` VALUES (45,19,'m/L','m/L',999,NULL);
INSERT INTO `unit_type` VALUES (46,19,'pg','pg',999,NULL);
INSERT INTO `unit_type` VALUES (47,19,'pred','pred',999,NULL);
INSERT INTO `unit_type` VALUES (48,19,'Gy','Gy',999,NULL);
INSERT INTO `unit_type` VALUES (49,19,'%','%',7,1);
INSERT INTO `unit_type` VALUES (50,19,'mS','mS',999,NULL);
INSERT INTO `unit_type` VALUES (51,19,'mm/hr','mm/hr',6,1);
INSERT INTO `unit_type` VALUES (52,19,'mg/dl','mg/dl',5,2);
INSERT INTO `unit_type` VALUES (53,19,'mn','mn',999,NULL);
INSERT INTO `unit_type` VALUES (54,19,'mg/L','mg/L',5,4);
INSERT INTO `unit_type` VALUES (55,19,'kgm2','kgm2',999,NULL);
INSERT INTO `unit_type` VALUES (56,19,'mm Hg','mm Hg',999,NULL);
INSERT INTO `unit_type` VALUES (57,19,'kg/m2','kg/m2',5,5);
INSERT INTO `unit_type` VALUES (58,19,'Pipes','Pipes',999,NULL);
INSERT INTO `unit_type` VALUES (59,19,'S','S',3,3);
INSERT INTO `unit_type` VALUES (60,19,'mm/hg','mm/hg',999,NULL);
/*!40000 ALTER TABLE `unit_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `UPLOAD_REPORT` longblob,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `UPLOAD_TYPE_ID` int(11) DEFAULT NULL,
  `PAYLOAD_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) NOT NULL,
  `UPLOAD_LEVEL_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  KEY `fk_upload_study` (`STUDY_ID`),
  KEY `fk_upload_ark_function_id` (`ARK_FUNCTION_ID`),
  KEY `fk_upload_payload` (`PAYLOAD_ID`),
  KEY `fk_upload_status` (`STATUS_ID`),
  KEY `fk_upload_upload_level_idx` (`UPLOAD_LEVEL_ID`),
  CONSTRAINT `fk_upload_ark_function_id` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_payload` FOREIGN KEY (`PAYLOAD_ID`) REFERENCES `payload` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `upload_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_upload_upload_level` FOREIGN KEY (`UPLOAD_LEVEL_ID`) REFERENCES `upload_level` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `study/del';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload`
--

LOCK TABLES `upload` WRITE;
/*!40000 ALTER TABLE `upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload_error`
--

DROP TABLE IF EXISTS `upload_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_error` (
  `ID` int(11) NOT NULL,
  `UPLOAD_ID` int(11) DEFAULT NULL,
  `ERROR_MSG` varchar(256) DEFAULT NULL,
  `ROW_NUMBER` int(11) DEFAULT NULL,
  `ORIGINAL_ROW_DATA` text,
  PRIMARY KEY (`ID`),
  KEY `FK_UPLOAD_ERROR_UPLOAD` (`UPLOAD_ID`),
  CONSTRAINT `FK_UPLOAD_ERROR_UPLOAD` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload_error`
--

LOCK TABLES `upload_error` WRITE;
/*!40000 ALTER TABLE `upload_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload_level`
--

DROP TABLE IF EXISTS `upload_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload_level`
--

LOCK TABLES `upload_level` WRITE;
/*!40000 ALTER TABLE `upload_level` DISABLE KEYS */;
INSERT INTO `upload_level` VALUES (1,'Field','Field level custom field upload. ');
INSERT INTO `upload_level` VALUES (2,'Category','Category level custom field upload.');
/*!40000 ALTER TABLE `upload_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload_status`
--

DROP TABLE IF EXISTS `upload_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_status` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `SHORT_MESSAGE` varchar(128) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)',
  `LONG_MESSAGE` varchar(256) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `name_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload_status`
--

LOCK TABLES `upload_status` WRITE;
/*!40000 ALTER TABLE `upload_status` DISABLE KEYS */;
INSERT INTO `upload_status` VALUES (-3,'ERROR_ON_DATA_IMPORT','Error while importing data','While the file passed validation, an error occured during the import of data.  Please contact your system administrator.');
INSERT INTO `upload_status` VALUES (-2,'ERROR_IN_DATA_VALIDATION','Error while validating data','Error while validating data, prior to uploading');
INSERT INTO `upload_status` VALUES (-1,'ERROR_IN_FILE_VALIDATION','Error validation file','Error in file format or header values.');
INSERT INTO `upload_status` VALUES (0,'STATUS_NOT_DEFINED','Status not defined','Status not defined.  This may predate our adding status to uploads');
INSERT INTO `upload_status` VALUES (1,'AWAITING_VALIDATION','Awaiting Validation','Successfully uploaded to our server, awaiting validation and upload to fields');
INSERT INTO `upload_status` VALUES (2,'VALIDATED','Successfully validated','Successfully validated.  Awaiting upload into fields');
INSERT INTO `upload_status` VALUES (3,'COMPLETED','Successfully completed','Successfully completed upload');
/*!40000 ALTER TABLE `upload_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload_type`
--

DROP TABLE IF EXISTS `upload_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ARK_MODULE_ID` int(11) DEFAULT NULL,
  `ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_type_ark_module` (`ARK_MODULE_ID`),
  CONSTRAINT `fk_upload_type_ark_module` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COMMENT='Reference table to describe the type of an upload';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload_type`
--

LOCK TABLES `upload_type` WRITE;
/*!40000 ALTER TABLE `upload_type` DISABLE KEYS */;
INSERT INTO `upload_type` VALUES (1,'Biospecimen','Biospecimen deails',5,3);
INSERT INTO `upload_type` VALUES (2,'Subject Demographic Data',NULL,2,NULL);
INSERT INTO `upload_type` VALUES (3,'Study-specific (custom) Data',NULL,2,NULL);
INSERT INTO `upload_type` VALUES (4,'Custom Data Sets','Custom Data Sets formerly known as Phenotypic Custom Data',3,NULL);
INSERT INTO `upload_type` VALUES (5,'Biocollection','Biocollection deails',5,1);
INSERT INTO `upload_type` VALUES (6,'Subject Consent Data',NULL,2,NULL);
INSERT INTO `upload_type` VALUES (7,'Pedigree Data','Pedigree Data associated with the subjects',2,NULL);
INSERT INTO `upload_type` VALUES (8,'Biospecimen Inventory','Upload the locations of Biospecimen only',5,2);
INSERT INTO `upload_type` VALUES (9,'Subject Attachment Data','Upload the subject attachments and consent attachments',2,NULL);
/*!40000 ALTER TABLE `upload_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vital_status`
--

DROP TABLE IF EXISTS `vital_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vital_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vital_status`
--

LOCK TABLES `vital_status` WRITE;
/*!40000 ALTER TABLE `vital_status` DISABLE KEYS */;
INSERT INTO `vital_status` VALUES (0,'Unknown',NULL);
INSERT INTO `vital_status` VALUES (1,'Alive',NULL);
INSERT INTO `vital_status` VALUES (2,'Deceased',NULL);
/*!40000 ALTER TABLE `vital_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yes_no`
--

DROP TABLE IF EXISTS `yes_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `yes_no` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yes_no`
--

LOCK TABLES `yes_no` WRITE;
/*!40000 ALTER TABLE `yes_no` DISABLE KEYS */;
INSERT INTO `yes_no` VALUES (1,'Yes');
INSERT INTO `yes_no` VALUES (2,'No');
/*!40000 ALTER TABLE `yes_no` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `admin`
--

USE `admin`;

--
-- Current Database: `audit`
--

USE `audit`;

--
-- Current Database: `config`
--

USE `config`;

--
-- Current Database: `disease`
--

USE `disease`;

--
-- Current Database: `geno`
--

USE `geno`;

--
-- Current Database: `lims`
--

USE `lims`;

--
-- Current Database: `pheno`
--

USE `pheno`;

--
-- Current Database: `reporting`
--

USE `reporting`;

--
-- Current Database: `study`
--

USE `study`;

--
-- Final view structure for view `role_policy`
--

/*!50001 DROP TABLE IF EXISTS `role_policy`*/;
/*!50001 DROP VIEW IF EXISTS `role_policy`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `role_policy` AS select `ar`.`NAME` AS `Role`,`am`.`NAME` AS `Module`,`af`.`NAME` AS `FunctionGroup`,`ap`.`NAME` AS `Permission`,`af`.`DESCRIPTION` AS `Function` from ((((`ark_role_policy_template` `arpt` join `ark_role` `ar` on((`arpt`.`ARK_ROLE_ID` = `ar`.`ID`))) join `ark_permission` `ap` on((`arpt`.`ARK_PERMISSION_ID` = `ap`.`ID`))) left join `ark_module` `am` on((`arpt`.`ARK_MODULE_ID` = `am`.`ID`))) left join `ark_function` `af` on((`arpt`.`ARK_FUNCTION_ID` = `af`.`ID`))) order by `ar`.`ID`,`af`.`ID`,`ap`.`ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `study_user_role_permission_view`
--

/*!50001 DROP TABLE IF EXISTS `study_user_role_permission_view`*/;
/*!50001 DROP VIEW IF EXISTS `study_user_role_permission_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `study_user_role_permission_view` AS select distinct `study`.`NAME` AS `studyName`,`ark_user`.`LDAP_USER_NAME` AS `userName`,`ark_role`.`NAME` AS `roleName`,`ark_module`.`NAME` AS `moduleName`,max(if((`arpt`.`ARK_PERMISSION_ID` = 1),_utf8'Y',_utf8'N')) AS `create`,max(if((`arpt`.`ARK_PERMISSION_ID` = 2),_utf8'Y',_utf8'N')) AS `read`,max(if((`arpt`.`ARK_PERMISSION_ID` = 3),_utf8'Y',_utf8'N')) AS `update`,max(if((`arpt`.`ARK_PERMISSION_ID` = 4),_utf8'Y',_utf8'N')) AS `delete` from ((((((`ark_role_policy_template` `arpt` join `ark_role`) join `ark_user_role`) join `ark_user`) join `ark_module`) join `ark_permission` `ap`) join `study`) where ((`arpt`.`ARK_ROLE_ID` = `ark_role`.`ID`) and (`arpt`.`ARK_MODULE_ID` = `ark_module`.`ID`) and (`arpt`.`ARK_PERMISSION_ID` = `ap`.`ID`) and (`arpt`.`ARK_ROLE_ID` = `ark_user_role`.`ARK_ROLE_ID`) and (`arpt`.`ARK_MODULE_ID` = `ark_user_role`.`ARK_MODULE_ID`) and (`ark_user_role`.`ARK_ROLE_ID` = `ark_role`.`ID`) and (`ark_user_role`.`ARK_MODULE_ID` = `ark_module`.`ID`) and (`ark_user_role`.`ARK_USER_ID` = `ark_user`.`ID`) and (`ark_user_role`.`STUDY_ID` = `study`.`ID`)) group by `study`.`NAME`,`ark_user`.`LDAP_USER_NAME`,`ark_role`.`NAME`,`ark_module`.`NAME` order by `ark_user_role`.`STUDY_ID`,`ark_user`.`LDAP_USER_NAME`,`ark_role`.`ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-08 11:10:13
