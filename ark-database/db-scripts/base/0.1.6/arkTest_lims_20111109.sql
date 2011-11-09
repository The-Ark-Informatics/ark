-- MySQL dump 10.11
--
-- Host: localhost    Database: lims
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
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access_request`
--

DROP TABLE IF EXISTS `access_request`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `access_request` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `REQUEST_DATE` datetime NOT NULL,
  `REQUIRED_DATE` datetime default NULL,
  `COMMENTS` varchar(2000) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
-- Table structure for table `barcode_command`
--

DROP TABLE IF EXISTS `barcode_command`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `barcode_command` (
  `ID` int(11) NOT NULL auto_increment,
  `PRINTER_ID` int(11) NOT NULL,
  `COMMAND` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  `MEMORY` varchar(45) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `barcode_label`
--

DROP TABLE IF EXISTS `barcode_label`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `barcode_label` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `BARCODE_PRINTER_ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `LABEL_PREFIX` text NOT NULL,
  `LABEL_SUFFIX` text NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_barcode_label_study` (`STUDY_ID`),
  KEY `fk_barcode_label_printer` (`BARCODE_PRINTER_ID`),
  KEY `fk_barcode_label_barcode_printer` (`BARCODE_PRINTER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `barcode_label_data`
--

DROP TABLE IF EXISTS `barcode_label_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `barcode_label_data` (
  `ID` int(11) NOT NULL auto_increment,
  `BARCODE_LABEL_ID` int(11) NOT NULL,
  `COMMAND` varchar(50) NOT NULL,
  `X_COORD` int(11) NOT NULL,
  `Y_COORD` int(11) NOT NULL,
  `P1` varchar(50) default NULL,
  `P2` varchar(50) default NULL,
  `P3` varchar(50) default NULL,
  `P4` varchar(50) default NULL,
  `P5` varchar(50) default NULL,
  `P6` varchar(50) default NULL,
  `P7` varchar(50) default NULL,
  `P8` varchar(50) default NULL,
  `QUOTE_LEFT` varchar(5) default NULL,
  `DATA` varchar(50) default NULL,
  `QUOTE_RIGHT` varchar(5) default NULL,
  `LINE_FEED` varchar(5) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_barcode_label_data_label` (`BARCODE_LABEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `barcode_printer`
--

DROP TABLE IF EXISTS `barcode_printer`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `barcode_printer` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `LOCATION` varchar(100) NOT NULL,
  `HOST` varchar(100) NOT NULL,
  `PORT` int(11) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='NOTE: the name of the printer MUST match the shared name';
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bio_transaction`
--

DROP TABLE IF EXISTS `bio_transaction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bio_transaction` (
  `ID` int(11) NOT NULL auto_increment,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `TRANSACTION_DATE` datetime NOT NULL,
  `QUANTITY` double NOT NULL,
  `RECORDER` varchar(255) default NULL,
  `REASON` text,
  `STATUS_ID` int(11) default NULL,
  `REQUEST_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_BIOTRANSACTION_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  KEY `FK_BIOTRANSACTION_STATUS_ID` (`STATUS_ID`),
  KEY `FK_BIOTRANSACTION_REQUEST_ID` (`REQUEST_ID`),
  CONSTRAINT `FK_BIOTRANSACTION_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_REQUEST_ID` FOREIGN KEY (`REQUEST_ID`) REFERENCES `access_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_STATUS_ID` FOREIGN KEY (`STATUS_ID`) REFERENCES `bio_transaction_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bio_transaction_status`
--

DROP TABLE IF EXISTS `bio_transaction_status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bio_transaction_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biocollection_custom_field_data`
--

DROP TABLE IF EXISTS `biocollection_custom_field_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biocollection_custom_field_data` (
  `ID` int(11) NOT NULL auto_increment,
  `BIO_COLLECTION_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime default NULL,
  `NUMBER_DATA_VALUE` double default NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY  (`ID`),
  KEY `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` (`BIO_COLLECTION_ID`),
  KEY `FK_BIOCOLCFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` FOREIGN KEY (`BIO_COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOCOLCFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `SAMPLETYPE_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `SUBSTUDY_ID` int(11) default NULL,
  `PARENT_ID` int(11) default NULL,
  `PARENTID` varchar(50) default NULL,
  `OLD_ID` int(11) default NULL,
  `DELETED` int(11) default NULL,
  `TIMESTAMP` varchar(55) default NULL,
  `OTHERID` varchar(50) default NULL,
  `BIOSPECIMEN_STORAGE_ID` int(11) default NULL,
  `SAMPLE_TIME` time default NULL,
  `PROCESSED_DATE` datetime default NULL,
  `SAMPLE_DATE` datetime default NULL,
  `SAMPLETYPE` varchar(255) default NULL,
  `SAMPLESUBTYPE` varchar(255) default NULL,
  `PROCESSED_TIME` time default NULL,
  `DEPTH` int(11) default '1',
  `BIOSPECIMEN_GRADE_ID` int(11) default NULL,
  `BIOSPECIMEN_SPECIES_ID` int(11) default '1',
  `QTY_COLLECTED` double default NULL,
  `QTY_REMOVED` double default NULL,
  `COMMENTS` text,
  `QUANTITY` double default NULL,
  `UNIT_ID` int(11) default NULL,
  `TREATMENT_TYPE_ID` int(11) NOT NULL,
  `BARCODED` tinyint(1) NOT NULL default '0',
  `BIOSPECIMEN_QUALITY_ID` int(11) default NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` int(11) default NULL,
  `BIOSPECIMEN_STATUS_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_biospecimen_collection` (`COLLECTION_ID`),
  KEY `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_ID`),
  KEY `fk_biospecimen_study` (`STUDY_ID`),
  KEY `fk_biospecimen_treatment_type_id` (`TREATMENT_TYPE_ID`),
  KEY `fk_biospecimen_unit` (`UNIT_ID`),
  KEY `fk_biospecimen_quality` (`BIOSPECIMEN_QUALITY_ID`),
  KEY `fk_biospecimen_anticoagulant` (`BIOSPECIMEN_ANTICOAGULANT_ID`),
  KEY `fk_biospecimen_status` (`BIOSPECIMEN_STATUS_ID`),
  KEY `fk_biospecimen_storage` USING BTREE (`BIOSPECIMEN_STORAGE_ID`),
  KEY `fk_biospecimen_species` USING BTREE (`BIOSPECIMEN_SPECIES_ID`),
  CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_treatment_type_id` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_anticoagulant`
--

DROP TABLE IF EXISTS `biospecimen_anticoagulant`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_anticoagulant` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_custom_field_data`
--

DROP TABLE IF EXISTS `biospecimen_custom_field_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_custom_field_data` (
  `ID` int(11) NOT NULL auto_increment,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime default NULL,
  `NUMBER_DATA_VALUE` double default NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY  (`ID`),
  KEY `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_BIOSPECFDATA_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  CONSTRAINT `FK_BIOSPECFDATA_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_grade`
--

DROP TABLE IF EXISTS `biospecimen_grade`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_grade` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_quality`
--

DROP TABLE IF EXISTS `biospecimen_quality`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_quality` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_species`
--

DROP TABLE IF EXISTS `biospecimen_species`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_species` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_status`
--

DROP TABLE IF EXISTS `biospecimen_status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimen_storage`
--

DROP TABLE IF EXISTS `biospecimen_storage`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimen_storage` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `SIZE` double default NULL,
  `UNIT_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_biospecimen_storage_unit` (`UNIT_ID`),
  CONSTRAINT `fk_biospecimen_storage_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimenuid_padchar`
--

DROP TABLE IF EXISTS `biospecimenuid_padchar`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimenuid_padchar` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimenuid_sequence`
--

DROP TABLE IF EXISTS `biospecimenuid_sequence`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimenuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL default '0',
  `INSERT_LOCK` int(11) NOT NULL default '0',
  PRIMARY KEY  (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimenuid_template`
--

DROP TABLE IF EXISTS `biospecimenuid_template`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimenuid_template` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `BIOSPECIMENUID_PREFIX` varchar(45) default NULL,
  `BIOSPECIMENUID_TOKEN_ID` int(11) default NULL,
  `BIOSPECIMENUID_PADCHAR_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_study_study` (`STUDY_ID`),
  KEY `fk_study_biospecimenuid_padchar` (`BIOSPECIMENUID_PADCHAR_ID`),
  KEY `fk_study_biospecimenuid_token` (`BIOSPECIMENUID_TOKEN_ID`),
  CONSTRAINT `fk_study_biospecimenuid_padchar` FOREIGN KEY (`BIOSPECIMENUID_PADCHAR_ID`) REFERENCES `biospecimenuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_biospecimenuid_token` FOREIGN KEY (`BIOSPECIMENUID_TOKEN_ID`) REFERENCES `biospecimenuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `biospecimenuid_token`
--

DROP TABLE IF EXISTS `biospecimenuid_token`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `biospecimenuid_token` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `cell_status`
--

DROP TABLE IF EXISTS `cell_status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `cell_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `NAME` varchar(50) default NULL,
  `NOOFCOL` int(11) NOT NULL,
  `CAPACITY` int(11) default NULL,
  `TRAY_ID` int(11) NOT NULL,
  `AVAILABLE` int(11) default NULL,
  `NOOFROW` int(11) NOT NULL,
  `COLNOTYPE_ID` int(11) NOT NULL,
  `ROWNOTYPE_ID` int(11) NOT NULL,
  `TRANSFER_ID` int(11) default NULL,
  `TYPE` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`),
  KEY `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`),
  KEY `fk_inv_box_tray_idx` (`TRAY_ID`),
  CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_tray` FOREIGN KEY (`TRAY_ID`) REFERENCES `inv_tray` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `BIOSPECIMEN_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_inv_cell_box_idx` USING BTREE (`BOX_ID`),
  KEY `fk_inv_cell_biospecimen_idx` USING BTREE (`BIOSPECIMEN_ID`),
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_col_row_type`
--

DROP TABLE IF EXISTS `inv_col_row_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_col_row_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
  `STUDY_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_INV_SITE_STUDY` USING BTREE (`STUDY_ID`),
  CONSTRAINT `FK_INV_SITE_STUDY` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  KEY `fk_inv_tank_tray_idx` USING BTREE (`TANK_ID`),
  CONSTRAINT `fk_inv_tray_tank` FOREIGN KEY (`TANK_ID`) REFERENCES `inv_tank` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inv_type`
--

DROP TABLE IF EXISTS `inv_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inv_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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

--
-- Table structure for table `study_inv_site`
--

DROP TABLE IF EXISTS `study_inv_site`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `study_inv_site` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `INV_SITE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_study_inv_site_study` (`STUDY_ID`),
  KEY `fk_study_inv_site_inv_site` (`INV_SITE_ID`),
  CONSTRAINT `fk_study_inv_site_inv_site` FOREIGN KEY (`INV_SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `treatment_type`
--

DROP TABLE IF EXISTS `treatment_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `treatment_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `unit` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(45) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-11-09  9:16:47
