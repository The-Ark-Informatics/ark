-- MySQL dump 10.11
--
-- Host: localhost    Database: lims
-- USE lims;
-- ------------------------------------------------------
-- Server version	5.0.77

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appointments` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `PURPOSE` text,
  `NOTIFY` varchar(100) default NULL,
  `PATIENT_ID` int(11) NOT NULL,
  `TIME` datetime default NULL,
  `DATE` datetime NOT NULL,
  `ALERT_DATE` datetime default NULL,
  `SURVEY_ID` int(11) default NULL,
  `SENT_TIMESTAMP` varchar(55) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `ATTACHEDBY` text NOT NULL,
  `FILE_NAME` varchar(50) default NULL,
  `COMMENTS` text,
  `DOMAIN` varchar(50) NOT NULL,
  `NA` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `barcodeformat`
--

DROP TABLE IF EXISTS `barcodeformat`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `barcodeformat` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) default NULL,
  `FIELDNAME` varchar(100) default NULL,
  `CONST` varchar(100) default NULL,
  `TYPE` int(11) default NULL,
  `LENGTH` int(11) default NULL,
  `FORMAT` varchar(50) default NULL,
  `ORDER` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_barcode_format_idx` (`STUDY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `barcodeid_engine`
--

DROP TABLE IF EXISTS `barcodeid_engine`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `barcodeid_engine` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `CLASS` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_barcodeid_engine_study_idx` (`STUDY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bio_sampletype`
--

DROP TABLE IF EXISTS `bio_sampletype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bio_sampletype` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL default '0',
  `SAMPLETYPE` varchar(50) default NULL,
  `SAMPLESUBTYPE` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bio_transaction`
--

DROP TABLE IF EXISTS `bio_transaction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bio_transaction` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) default NULL,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `TREATMENT` varchar(255) default NULL,
  `SOURCESTUDY_ID` int(11) default NULL,
  `UNIT` varchar(50) default NULL,
  `DELIVERYDATE` datetime default NULL,
  `FIXATIONTIME` varchar(50) default NULL,
  `TRANSACTIONDATE` datetime NOT NULL,
  `QUANTITY` double NOT NULL,
  `OWNER` varchar(255) default NULL,
  `REASON` text,
  `STATUS` varchar(50) default NULL,
  `STUDY` varchar(50) default NULL,
  `COLLABORATOR` varchar(255) default NULL,
  `RECORDER` varchar(255) default NULL,
  `DESTINATION` varchar(255) default NULL,
  `ACTION` varchar(50) default NULL,
  `TYPE` varchar(100) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_bio_transactions_biospecimen_idx` (`BIOSPECIMEN_ID`),
  CONSTRAINT `fk_bio_transactions_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata`
--

DROP TABLE IF EXISTS `biodata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata` (
  `ID` int(11) NOT NULL auto_increment,
  `DOMAIN_ID` int(11) default NULL,
  `FIELD_ID` int(11) NOT NULL,
  `DATE_COLLECTED` datetime NOT NULL,
  `STRING_VALUE` text,
  `NUMBER_VALUE` int(11) default NULL,
  `DATE_VALUE` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_biodata_domain_idx` (`DOMAIN_ID`),
  KEY `fk_biodata_field_idx` (`FIELD_ID`),
  CONSTRAINT `fk_biodata_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `biodata_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_criteria`
--

DROP TABLE IF EXISTS `biodata_criteria`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_criteria` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) default NULL,
  `DOMAIN` varchar(50) default NULL,
  `FIELD` varchar(50) default NULL,
  `VALUE` varchar(255) default NULL,
  `DESCRIPTION` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_field`
--

DROP TABLE IF EXISTS `biodata_field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_field` (
  `ID` int(11) NOT NULL auto_increment,
  `TYPE_ID` int(11) NOT NULL,
  `FORMAT` varchar(20) default NULL,
  `COLUMNNAME` varchar(50) NOT NULL,
  `UNIT_ID` int(11) default NULL,
  `LOVTYPE` varchar(20) default NULL,
  `DOMAIN` varchar(50) default NULL,
  `FIELDNAME` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_field_group`
--

DROP TABLE IF EXISTS `biodata_field_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_field_group` (
  `ID` int(11) NOT NULL auto_increment,
  `FIELD_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `POSITION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_biodata_field_group_field_idx` (`FIELD_ID`),
  KEY `fk_biodata_field_group_group_idx` (`GROUP_ID`),
  CONSTRAINT `fk_biodata_field_group_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `biodata_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biodata_field_group_group` FOREIGN KEY (`GROUP_ID`) REFERENCES `biodata_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_field_lov`
--

DROP TABLE IF EXISTS `biodata_field_lov`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_field_lov` (
  `ID` int(11) NOT NULL auto_increment,
  `LIST_ID` int(11) NOT NULL,
  `VALUE` varchar(50) NOT NULL,
  `ORDER` int(11) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_group`
--

DROP TABLE IF EXISTS `biodata_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_group` (
  `ID` int(11) NOT NULL auto_increment,
  `GROUP_NAME` varchar(100) NOT NULL,
  `DOMAIN` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_group_criteria`
--

DROP TABLE IF EXISTS `biodata_group_criteria`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_group_criteria` (
  `ID` int(11) NOT NULL auto_increment,
  `CRITERIA_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_biodata_group_criteria_criteria_idx` (`CRITERIA_ID`),
  KEY `fk_biodata_group_criteria_group_idx` (`GROUP_ID`),
  CONSTRAINT `fk_biodata_group_criteria` FOREIGN KEY (`CRITERIA_ID`) REFERENCES `biodata_criteria` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biodata_group_group` FOREIGN KEY (`GROUP_ID`) REFERENCES `biodata_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_lov_list`
--

DROP TABLE IF EXISTS `biodata_lov_list`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_lov_list` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_type`
--

DROP TABLE IF EXISTS `biodata_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biodata_unit`
--

DROP TABLE IF EXISTS `biodata_unit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biodata_unit` (
  `ID` int(11) NOT NULL auto_increment,
  `UNITNAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen`
--

DROP TABLE IF EXISTS `biospecimen`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen` (
  `ID` int(11) NOT NULL auto_increment,
  `BIOSPECIMEN_ID` varchar(50) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) default NULL,
  `COLLECTION_ID` int(11) default NULL,
  `SUBSTUDY_ID` int(11) default NULL,
  `PARENT_ID` int(11) default NULL,
  `PARENTID` varchar(50) default NULL,
  `OLD_ID` int(11) default NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `OTHERID` varchar(50) default NULL,
  `STORED_IN` varchar(50) default NULL,
  `SAMPLE_TIME` datetime default NULL,
  `GRADE` varchar(255) default NULL,
  `CELL_ID` int(11) default NULL,
  `DEPTH` int(11) default NULL,
  `SAMPLEDATE` datetime default NULL,
  `EXTRACTED_TIME` datetime default NULL,
  `LOCATION` varchar(255) default NULL,
  `SAMPLETYPE_ID` int(11) NOT NULL,
  `SAMPLETYPE` varchar(255) default NULL,
  `SAMPLESUBTYPE` varchar(255) default NULL,
  `SUBTYPEDESC` varchar(255) default NULL,
  `SPECIES` varchar(255) default NULL,
  `QTY_COLLECTED` double default NULL,
  `DATEEXTRACTED` datetime default NULL,
  `QTY_REMOVED` double default NULL,
  `GESTAT` double default NULL,
  `COMMENTS` text,
  `DATEDISTRIBUTED` datetime default NULL,
  `COLLABORATOR` varchar(100) default NULL,
  `DNACONC` double default NULL,
  `PURITY` double default NULL,
  `ANTICOAG` varchar(100) default NULL,
  `PROTOCOL` varchar(100) default NULL,
  `DNA_BANK` int(11) default NULL,
  `QUANTITY` int(11) default NULL,
  `UNITS` varchar(10) default NULL,
  `QUALITY` varchar(100) default NULL,
  `WITHDRAWN` int(11) default NULL,
  `STATUS` varchar(20) default NULL,
  `TREATMENT` varchar(50) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_biospecimen_collection` (`COLLECTION_ID`),
  KEY `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_ID`),
  KEY `fk_biospecimen_study` (`STUDY_ID`),
  KEY `fk_biospecimen_inv_cell` (`CELL_ID`),
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_inv_cell` FOREIGN KEY (`CELL_ID`) REFERENCES `inv_cell` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `collection` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `COLLECTIONDATE` datetime default NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `COMMENTS` text,
  `HOSPITAL` varchar(50) default NULL,
  `SURGERYDATE` datetime default NULL,
  `DIAG_CATEGORY` varchar(50) default NULL,
  `REF_DOCTOR` varchar(50) default NULL,
  `PATIENTAGE` int(11) default NULL,
  `DISCHARGEDATE` datetime default NULL,
  `HOSPITAL_UR` varchar(50) default NULL,
  `DIAG_DATE` datetime default NULL,
  `COLLECTIONGROUP_ID` int(11) default NULL,
  `EPISODE_NUM` varchar(50) default NULL,
  `EPISODE_DESC` varchar(50) default NULL,
  `COLLECTIONGROUP` varchar(50) default NULL,
  `TISSUETYPE` varchar(50) default NULL,
  `TISSUECLASS` varchar(50) default NULL,
  `PATHLABNO` varchar(50) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_collection_idx` (`NAME`),
  KEY `fk_collection_name_idx` (`NAME`),
  KEY `fk_collection_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_collection_study` (`STUDY_ID`),
  CONSTRAINT `fk_collection_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `flag`
--

DROP TABLE IF EXISTS `flag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `flag` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `DOMAIN` varchar(50) NOT NULL,
  `REFERENCE_ID` int(11) NOT NULL,
  `USER` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `group` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `GROUP_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `ACTIVITY_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_box`
--

DROP TABLE IF EXISTS `inv_box`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_box` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `NOOFCOL` int(11) NOT NULL,
  `COLNOTYPE` varchar(15) NOT NULL,
  `CAPACITY` int(11) default NULL,
  `NAME` varchar(50) default NULL,
  `TRAY_ID` int(11) NOT NULL,
  `AVAILABLE` int(11) default NULL,
  `NOOFROW` int(11) NOT NULL,
  `ROWNOTYPE` varchar(15) NOT NULL,
  `TRANSFER_ID` int(11) default NULL,
  `TYPE` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_inv_tray_box_idx` (`TRAY_ID`),
  CONSTRAINT `fk_inv_tray_box` FOREIGN KEY (`TRAY_ID`) REFERENCES `inv_tray` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_cell`
--

DROP TABLE IF EXISTS `inv_cell`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_cell` (
  `ID` int(11) NOT NULL auto_increment,
  `BOX_ID` int(11) NOT NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `ROWNO` int(11) default NULL,
  `COLNO` int(11) default NULL,
  `STATUS` varchar(50) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_inv_cell_tray_idx` (`BOX_ID`),
  CONSTRAINT `fk_inv_cell_tray` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_site`
--

DROP TABLE IF EXISTS `inv_site`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_site` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `CONTACT` varchar(50) default NULL,
  `ADDRESS` text,
  `NAME` varchar(50) NOT NULL,
  `PHONE` varchar(50) default NULL,
  `LDAP_GROUP` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_tank`
--

DROP TABLE IF EXISTS `inv_tank`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_tank` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `LOCATION` text,
  `STATUS` varchar(50) default NULL,
  `SITE_ID` int(11) NOT NULL,
  `CAPACITY` int(11) default NULL,
  `LASTSERVICENOTE` text,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) default NULL,
  `DECOMMISSIONDATE` datetime default NULL,
  `COMMISSIONDATE` datetime default NULL,
  `LASTSERVICEDATE` datetime default NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`ID`),
  KEY `fk_inv_tank_site` (`SITE_ID`),
  CONSTRAINT `fk_inv_tank_site` FOREIGN KEY (`SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_tray`
--

DROP TABLE IF EXISTS `inv_tray`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_tray` (
  `ID` int(11) NOT NULL auto_increment,
  `TANK_ID` int(11) NOT NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) default NULL,
  `DESCRIPTION` text,
  `CAPACITY` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_inv_box_tank_idx` (`TANK_ID`),
  CONSTRAINT `fk_inv_box_tank` FOREIGN KEY (`TANK_ID`) REFERENCES `inv_tank` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `listofvalues`
--

DROP TABLE IF EXISTS `listofvalues`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `listofvalues` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) default NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `TYPE` varchar(100) default NULL,
  `VALUE` varchar(100) default NULL,
  `SORTORDER` int(11) default NULL,
  `GROUP_ID` int(11) default NULL,
  `DESCRIPTION` varchar(100) default NULL,
  `PARENTTYPE` text,
  `PARENTVALUE` text,
  `ISEDITABLE` int(11) NOT NULL,
  `LANGUAGE` varchar(20) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2839 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `listofvalues_description`
--

DROP TABLE IF EXISTS `listofvalues_description`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `listofvalues_description` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `TYPE` varchar(255) NOT NULL,
  `DESCRIPTION` text NOT NULL,
  `DESC_ID` varchar(55) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `note` (
  `ID` int(11) NOT NULL auto_increment,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `NAME` varchar(100) default NULL,
  `ELEMENT_ID` int(11) NOT NULL,
  `TYPE` varchar(50) default NULL,
  `FILENAME` varchar(50) default NULL,
  `DOMAIN` varchar(50) default NULL,
  `DESCRIPTION` text,
  `DATE` datetime NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `samplecode`
--

DROP TABLE IF EXISTS `samplecode`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `samplecode` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `SAMPLETYPE` varchar(100) NOT NULL,
  `SAMPLESUBTYPE` varchar(50) default NULL,
  `SAMPLETYPE_ID` int(11) default NULL,
  `CODE` varchar(4) default NULL,
  `ORDER` int(11) default NULL,
  `CHILDCODE` varchar(4) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-11  5:40:07
