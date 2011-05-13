-- MySQL dump 10.13  Distrib 5.1.56, for apple-darwin10.3.0 (i386)
--
-- Host: 192.168.3.253    Database: study
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
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `action_type`
--

DROP TABLE IF EXISTS `action_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `action_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ID` int(11) NOT NULL auto_increment,
  `STREET_ADDRESS` varchar(255) default NULL,
  `CITY` varchar(45) default NULL,
  `COUNTRY_STATE_ID` int(11) default NULL,
  `POST_CODE` varchar(10) default NULL,
  `COUNTRY_ID` int(11) default NULL,
  `ADDRESS_STATUS_ID` int(11) default NULL,
  `ADDRESS_TYPE_ID` int(11) NOT NULL,
  `OTHER_STATE` varchar(45) default NULL,
  `PERSON_ID` int(11) NOT NULL,
  `DATE_RECEIVED` varchar(45) default NULL,
  `COMMENTS` text,
  `PREFERRED_MAILING_ADDRESS_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`,`ADDRESS_TYPE_ID`,`PERSON_ID`),
  KEY `fk_address_country` USING BTREE (`COUNTRY_ID`),
  KEY `fk_address_state` USING BTREE (`COUNTRY_STATE_ID`),
  KEY `fk_address_person` USING BTREE (`PERSON_ID`),
  KEY `fk_address_address_type` USING BTREE (`ADDRESS_TYPE_ID`),
  KEY `fk_address_status` USING BTREE (`ADDRESS_STATUS_ID`),
  KEY `fk_address_preferred_mailing_address_id` USING BTREE (`PREFERRED_MAILING_ADDRESS_ID`),
  CONSTRAINT `fk_address_address_type` FOREIGN KEY (`ADDRESS_TYPE_ID`) REFERENCES `address_type` (`ID`),
  CONSTRAINT `fk_address_country` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_person` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_preferred_mailing_address_id` FOREIGN KEY (`PREFERRED_MAILING_ADDRESS_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_state` FOREIGN KEY (`COUNTRY_STATE_ID`) REFERENCES `country_state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_status` FOREIGN KEY (`ADDRESS_STATUS_ID`) REFERENCES `address_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_TYPE_ID`) REFER `study/addre';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address_status`
--

DROP TABLE IF EXISTS `address_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` USING BTREE (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address_type`
--

DROP TABLE IF EXISTS `address_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_history`
--

DROP TABLE IF EXISTS `audit_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_history` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_STATUS_ID` int(11) NOT NULL default '0',
  `DATE_TIME` datetime default NULL,
  `ACTION_TYPE` varchar(50) NOT NULL,
  `ARK_USER_ID` varchar(255) default NULL,
  `COMMENT` varchar(255) default NULL,
  `ENTITY_TYPE` varchar(50) NOT NULL,
  `ENTITY_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`,`STUDY_STATUS_ID`,`ACTION_TYPE`,`ENTITY_TYPE`),
  KEY `AUDIT_HISTORY_STUDY_STATUS_FK` USING BTREE (`STUDY_STATUS_ID`),
  KEY `AUDIT_HISTORY_ENTITY_ID` USING BTREE (`ENTITY_ID`),
  KEY `AUDIT_HISTORY_ACTION_TYPE` USING BTREE (`ACTION_TYPE`),
  KEY `AUDIT_HISTORY_ENTITY_TYPE` USING BTREE (`ENTITY_TYPE`),
  CONSTRAINT `audit_history_ibfk_3` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ACTION_TYPE_ID`) REFER `study/action';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent`
--

DROP TABLE IF EXISTS `consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `SUBJECT_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) default NULL,
  `CONSENT_STATUS_ID` int(11) NOT NULL,
  `CONSENT_TYPE_ID` int(11) NOT NULL,
  `CONSENT_DATE` date NOT NULL,
  `CONSENTED_BY` varchar(100) default NULL,
  `COMMENTS` varchar(500) default NULL,
  `REQUESTED_DATE` date default NULL,
  `RECEIVED_DATE` date default NULL,
  `COMPLETED_DATE` date default NULL,
  `CONSENT_DOWNLOADED_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_study` USING BTREE (`STUDY_ID`),
  KEY `fk_subject` USING BTREE (`SUBJECT_ID`),
  KEY `fk_study_component` USING BTREE (`STUDY_COMP_ID`),
  KEY `fk_study_comp_status` USING BTREE (`STUDY_COMP_STATUS_ID`),
  KEY `fk_consent_status` USING BTREE (`CONSENT_STATUS_ID`),
  KEY `fk_consent_type` USING BTREE (`CONSENT_TYPE_ID`),
  KEY `fk_consent_downloaded` USING BTREE (`CONSENT_DOWNLOADED_ID`),
  CONSTRAINT `fk_consent_downloaded` FOREIGN KEY (`CONSENT_DOWNLOADED_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_status` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_type` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_component` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_comp_status` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject` FOREIGN KEY (`SUBJECT_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_answer`
--

DROP TABLE IF EXISTS `consent_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_answer` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_file`
--

DROP TABLE IF EXISTS `consent_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_file` (
  `ID` int(11) NOT NULL auto_increment,
  `CONSENT_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_consent_file_consent` USING BTREE (`CONSENT_ID`),
  CONSTRAINT `fk_upload_consent` FOREIGN KEY (`CONSENT_ID`) REFERENCES `consent` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_section`
--

DROP TABLE IF EXISTS `consent_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_section` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(500) NOT NULL,
  `DESCRIPTION` varchar(1000) default NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` USING BTREE (`NAME`),
  KEY `fk_consent_section_1` USING BTREE (`STUDY_ID`),
  KEY `fk_consent_section_2` USING BTREE (`STUDY_COMP_ID`),
  CONSTRAINT `fk_consent_section_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_section_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_status`
--

DROP TABLE IF EXISTS `consent_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1000) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` USING BTREE (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_type`
--

DROP TABLE IF EXISTS `consent_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1000) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence`
--

DROP TABLE IF EXISTS `correspondence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence` (
  `ID` int(11) NOT NULL auto_increment,
  `DATE_OF_CORRESPONDENCE` date default NULL,
  `SUMMARY` varchar(255) default NULL,
  `CORRESPONDENCE_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`CORRESPONDENCE_TYPE_ID`),
  KEY `CORRESPONDENCE_CO_TYPE_FK` USING BTREE (`CORRESPONDENCE_TYPE_ID`),
  CONSTRAINT `correspondence_ibfk_1` FOREIGN KEY (`CORRESPONDENCE_TYPE_ID`) REFERENCES `correspondence_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`CORRESPONDENCE_TYPE_ID`) REFER `stud';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_attachment`
--

DROP TABLE IF EXISTS `correspondence_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `SIZE` int(10) unsigned NOT NULL,
  `CONTENTS` blob NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `correspondence` USING BTREE (`CORRESPONDENCE_ID`),
  CONSTRAINT `correspondence_attachment_correspondence_id` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondences` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_audit`
--

DROP TABLE IF EXISTS `correspondence_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_audit` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) default NULL,
  `AUDIT_DATE` date NOT NULL,
  `AUDIT_TIME` varchar(255) NOT NULL,
  `STATUS_TYPE_ID` int(11) default NULL,
  `STUDY_MANAGER` varchar(255) default NULL,
  `CORRESPONDENCE_DATE` date default NULL,
  `CORRESPONDENCE_TIME` varchar(255) default NULL,
  `REASON` varchar(4096) default NULL,
  `MODE_TYPE_ID` int(11) default NULL,
  `DIRECTION_TYPE_ID` int(11) default NULL,
  `OUTCOME_TYPE_ID` int(11) default NULL,
  `DETAILS` varchar(4096) default NULL,
  `COMMENTS` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `corres` USING BTREE (`CORRESPONDENCE_ID`),
  KEY `correspondence_audit_correspondence_id` USING BTREE (`CORRESPONDENCE_ID`),
  KEY `correspondence_audit_status_type_id` USING BTREE (`STATUS_TYPE_ID`),
  KEY `correspondence_audit_mode_type_id` USING BTREE (`MODE_TYPE_ID`),
  KEY `correspondence_audit_direction_type_id` USING BTREE (`DIRECTION_TYPE_ID`),
  KEY `correspondence_audit_outcome_type_id` USING BTREE (`OUTCOME_TYPE_ID`),
  KEY `correspondence_audit_study_id` USING BTREE (`STUDY_ID`),
  CONSTRAINT `correspondence_audit_correspondence_id` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondences` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_status_type_id` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `correspondence_status_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_audit_attachment`
--

DROP TABLE IF EXISTS `correspondence_audit_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_audit_attachment` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_AUDIT_ID` int(11) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `SIZE` int(10) unsigned NOT NULL,
  `CONTENTS` blob NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `correspondence_audit_attachment_correspondence_audit_id` USING BTREE (`CORRESPONDENCE_AUDIT_ID`),
  CONSTRAINT `correspondence_audit_attachment_correspondence_audit_id` FOREIGN KEY (`CORRESPONDENCE_AUDIT_ID`) REFERENCES `correspondence_audit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_direction_type`
--

DROP TABLE IF EXISTS `correspondence_direction_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_direction_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_mode_type`
--

DROP TABLE IF EXISTS `correspondence_mode_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_mode_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_outcome_type`
--

DROP TABLE IF EXISTS `correspondence_outcome_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_outcome_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(496) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_status_type`
--

DROP TABLE IF EXISTS `correspondence_status_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_status_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_type`
--

DROP TABLE IF EXISTS `correspondence_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `TYPE_DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondences`
--

DROP TABLE IF EXISTS `correspondences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondences` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `STATUS_TYPE_ID` int(11) default NULL,
  `STUDY_MANAGER` varchar(255) default NULL,
  `DATE` date default NULL,
  `TIME` varchar(255) default NULL,
  `REASON` varchar(4096) default NULL,
  `MODE_TYPE_ID` int(11) default NULL,
  `DIRECTION_TYPE_ID` int(11) default NULL,
  `OUTCOME_TYPE_ID` int(11) default NULL,
  `DETAILS` varchar(4096) default NULL,
  `COMMENTS` varchar(4096) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `status_type` USING BTREE (`STATUS_TYPE_ID`),
  KEY `mode_type` USING BTREE (`MODE_TYPE_ID`),
  KEY `direction_type` USING BTREE (`DIRECTION_TYPE_ID`),
  KEY `outcome_type` USING BTREE (`OUTCOME_TYPE_ID`),
  KEY `correspondences_study_id` USING BTREE (`STUDY_ID`),
  CONSTRAINT `correspondences_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_status_type_id` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `correspondence_status_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(250) default NULL,
  `COUNTRY_CODE` varchar(2) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` USING BTREE (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country_state`
--

DROP TABLE IF EXISTS `country_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country_state` (
  `ID` int(11) NOT NULL auto_increment,
  `COUNTRY_ID` int(11) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_country_id` USING BTREE (`COUNTRY_ID`),
  CONSTRAINT `fk_country_id` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COMMENT='A link table that associates a country and its respective st';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_consent_answer`
--

DROP TABLE IF EXISTS `custom_consent_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_consent_answer` (
  `ID` int(11) NOT NULL auto_increment,
  `CUSTOM_CONSENT_QUESTION_ID` int(11) default NULL,
  `CONSENT_ANSWER_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_custom_consent_answer_1` USING BTREE (`CUSTOM_CONSENT_QUESTION_ID`),
  KEY `fk_custom_consent_answer_2` USING BTREE (`CONSENT_ANSWER_ID`),
  CONSTRAINT `fk_custom_consent_answer_1` FOREIGN KEY (`CUSTOM_CONSENT_QUESTION_ID`) REFERENCES `custom_consent_question` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_custom_consent_answer_2` FOREIGN KEY (`CONSENT_ANSWER_ID`) REFERENCES `question_answer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_consent_question`
--

DROP TABLE IF EXISTS `custom_consent_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_consent_question` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(500) NOT NULL,
  `DATA_TYPE_ID` int(11) NOT NULL,
  `DESCRETE_VALUE` varchar(45) default NULL,
  `POSITION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` USING BTREE (`NAME`),
  KEY `fk_custom_consent_question_1` USING BTREE (`DATA_TYPE_ID`),
  CONSTRAINT `fk_custom_consent_question_1` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `data_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_type`
--

DROP TABLE IF EXISTS `data_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `DOCUMENT_CONTENT` blob NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`STUDY_COMP_ID`,`CORRESPONDENCE_ID`),
  KEY `DOCUMENT_CORRESPONDENCE_FK` USING BTREE (`CORRESPONDENCE_ID`),
  KEY `DOCUMENT_STUDY_COMP_FK` USING BTREE (`STUDY_COMP_ID`),
  CONSTRAINT `document_ibfk_1` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondence` (`ID`),
  CONSTRAINT `document_ibfk_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`CORRESPONDENCE_ID`) REFER `study/cor';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `domain_type`
--

DROP TABLE IF EXISTS `domain_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email_account_type`
--

DROP TABLE IF EXISTS `email_account_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_account_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Table structure for table `email_account`
--

DROP TABLE IF EXISTS `email_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_account` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `PRIMARY_ACCOUNT` int(11) default NULL,
  `PERSON_ID` int(11) NOT NULL,
  `EMAIL_ACCOUNT_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`PERSON_ID`,`EMAIL_ACCOUNT_TYPE_ID`),
  KEY `EMAIL_ACCOUNT_PER_FK1` USING BTREE (`PERSON_ID`),
  KEY `EMAIL_ACCOUNT_EMA_FK1` USING BTREE (`EMAIL_ACCOUNT_TYPE_ID`),
  CONSTRAINT `email_account_ibfk_1` FOREIGN KEY (`EMAIL_ACCOUNT_TYPE_ID`) REFERENCES `email_account_type` (`ID`),
  CONSTRAINT `email_account_ibfk_2` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`EMAIL_ACCOUNT_TYPE_ID`) REFER `study';
/*!40101 SET character_set_client = @saved_cs_client */;


/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entity_type`
--

DROP TABLE IF EXISTS `entity_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gender_type`
--

DROP TABLE IF EXISTS `gender_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gender_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_correspondence_audit_person`
--

DROP TABLE IF EXISTS `link_correspondence_audit_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_correspondence_audit_person` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_AUDIT_ID` int(11) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `correspondence_audit_people_person_id` USING BTREE (`PERSON_ID`),
  KEY `correspondence_audit_people_correspondence_audit_id` USING BTREE (`CORRESPONDENCE_AUDIT_ID`),
  CONSTRAINT `correspondence_audit_people_correspondence_audit_id` FOREIGN KEY (`CORRESPONDENCE_AUDIT_ID`) REFERENCES `correspondence_audit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_people_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_correspondence_person`
--

DROP TABLE IF EXISTS `link_correspondence_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_correspondence_person` (
  `ID` int(11) NOT NULL auto_increment,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `correspondence_id` USING BTREE (`CORRESPONDENCE_ID`),
  KEY `people_id` USING BTREE (`PERSON_ID`),
  KEY `correspondence_people_person_id` USING BTREE (`PERSON_ID`),
  CONSTRAINT `correspondence_people_correspondence_id` FOREIGN KEY (`CORRESPONDENCE_ID`) REFERENCES `correspondences` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_people_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_site`
--

DROP TABLE IF EXISTS `study_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_site` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `ADDRESS_ID` int(11) NOT NULL,
  `DOMAIN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`ADDRESS_ID`,`DOMAIN_TYPE_ID`),
  KEY `STUDY_SITE_ADDRES_FK1` USING BTREE (`ADDRESS_ID`),
  KEY `STUDY_SITE_DOMAIN_TYPE_FK` USING BTREE (`DOMAIN_TYPE_ID`),
  CONSTRAINT `study_site_ibfk_1` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `study_site_ibfk_2` FOREIGN KEY (`DOMAIN_TYPE_ID`) REFERENCES `domain_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_ID`) REFER `study/address`(`';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_site_contact`
--
DROP TABLE IF EXISTS `link_site_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_site_contact` (
  `ID` int(11) NOT NULL auto_increment,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_SITE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`PERSON_ID`,`STUDY_SITE_ID`),
  KEY `LINK_SITE_CONTACT_FK1` USING BTREE (`PERSON_ID`),
  KEY `LINK_SITE_CONTACT_STUDY_SITE_FK` USING BTREE (`STUDY_SITE_ID`),
  CONSTRAINT `link_site_contact_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_site_contact_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_studycomp`
--

DROP TABLE IF EXISTS `link_study_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_studycomp` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`STUDY_COMP_STATUS_ID`,`STUDY_COMP_ID`),
  KEY `LSSC_STUDY_FK` USING BTREE (`STUDY_ID`),
  KEY `LSSC_STUDY_COMP_FK` USING BTREE (`STUDY_COMP_ID`),
  KEY `LSSC_STUDY_COMP_STATUS_FK` USING BTREE (`STUDY_COMP_STATUS_ID`),
  CONSTRAINT `link_study_studycomp_ibfk_1` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`),
  CONSTRAINT `link_study_studycomp_ibfk_2` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`),
  CONSTRAINT `link_study_studycomp_ibfk_3` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_COMP_ID`) REFER `study/study_c';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_studysite`
--

DROP TABLE IF EXISTS `link_study_studysite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_studysite` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_SITE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`STUDY_SITE_ID`,`STUDY_ID`),
  KEY `LINK_STUDY_STUDYSITE_STUDY_SITE_FK` USING BTREE (`STUDY_SITE_ID`),
  KEY `LINK_STUDY_STUDYSITE_STUDY_FK` USING BTREE (`STUDY_ID`),
  CONSTRAINT `link_study_studysite_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `link_study_studysite_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_substudy`
--

DROP TABLE IF EXISTS `link_study_substudy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_substudy` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `SUB_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`SUB_STUDY_ID`),
  KEY `LINK_STUDY_SUBSTUDY_STUDY_FK` USING BTREE (`STUDY_ID`),
  KEY `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK` USING BTREE (`SUB_STUDY_ID`),
  CONSTRAINT `link_study_substudy_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `link_study_substudy_ibfk_2` FOREIGN KEY (`SUB_STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `relationship`
--

DROP TABLE IF EXISTS `relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_contact`
--

DROP TABLE IF EXISTS `link_subject_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_contact` (
  `ID` int(11) NOT NULL auto_increment,
  `PERSON_CONTACT_ID` int(11) default NULL,
  `PERSON_SUBJECT_ID` int(11) default NULL,
  `STUDY_ID` int(11) NOT NULL,
  `RELATIONSHIP_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`),
  KEY `LINK_SUBJECT_CONTACT_PERSON_FK` USING BTREE (`PERSON_CONTACT_ID`),
  KEY `LINK_SUBJECT_CONTACT_PERSON_SUBJECT_FK` USING BTREE (`PERSON_SUBJECT_ID`),
  KEY `LINK_SUBJECT_CONTACT_STUDY_FK` USING BTREE (`STUDY_ID`),
  KEY `LINK_SUBJECT_CONTACT_RELATIONSHIP_FK` USING BTREE (`RELATIONSHIP_ID`),
  CONSTRAINT `link_subject_contact_ibfk_1` FOREIGN KEY (`PERSON_CONTACT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_2` FOREIGN KEY (`PERSON_SUBJECT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_3` FOREIGN KEY (`RELATIONSHIP_ID`) REFERENCES `relationship` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_4` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_CONTACT_ID`) REFER `study/per';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_study`
--

DROP TABLE IF EXISTS `link_subject_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_study` (
  `ID` int(11) NOT NULL auto_increment,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `SUBJECT_STATUS_ID` int(11) NOT NULL,
  `SUBJECT_UID` varchar(50) NOT NULL,
  `AMDRIF_ID` int(11) default NULL,
  `STUDY_APPROACH_DATE` date default NULL,
  `SEND_NEWS_LETTER` int(11) default NULL,
  `YEAR_OF_FIRST_MAMOGRAM` int(11) default NULL,
  `YEAR_OF_RECENT_MAMOGRAM` int(11) default NULL,
  `TOTAL_MAMOGRAMS` int(11) default NULL,
  `SITE_STREET_ADDRESS` varchar(255) default NULL,
  `SITE_CITY` varchar(255) default NULL,
  `SITE_POST` varchar(45) default NULL,
  `COUNTRY_ID` int(11) default NULL,
  `STATE_ID` int(11) default NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) default NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) default NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) default NULL,
  `CONSENT_STATUS_ID` int(11) default NULL,
  `CONSENT_TYPE_ID` int(11) default NULL,
  `CONSENT_DATE` date default NULL,
  `OTHER_STATE` varchar(255) default NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`SUBJECT_UID`),
  UNIQUE KEY `UQ_STUDY_ID_SUBJECT_UID` USING BTREE (`STUDY_ID`,`SUBJECT_UID`),
  KEY `FK_LINK_SUBJECT_STUDY_PERSON_FK` (`PERSON_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` (`SUBJECT_STATUS_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_STATE` (`STATE_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_COUNTRY` (`COUNTRY_ID`),
  KEY `FK_LINK_SBJT_STUDY_CNS_ACT_CNCT` (`CONSENT_TO_ACTIVE_CONTACT_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CNS_PASS_DATA` (`CONSENT_TO_PASSIVE_DATA_GATHERING_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CNS_USE_DATA` (`CONSENT_TO_USE_DATA_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_UID` USING BTREE (`SUBJECT_UID`),
  KEY `FK_LINK_SUBJECT_STUDY_STUDY_FK` USING BTREE (`STUDY_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CONSENT_STATUS_ID` USING BTREE (`CONSENT_STATUS_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CONSENT_TYPE_ID` USING BTREE (`CONSENT_TYPE_ID`),
  CONSTRAINT `FK_LINK_SBJT_STUDY_CNS_ACT_CNCT` FOREIGN KEY (`CONSENT_TO_ACTIVE_CONTACT_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_PASS_DATA` FOREIGN KEY (`CONSENT_TO_PASSIVE_DATA_GATHERING_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_USE_DATA` FOREIGN KEY (`CONSENT_TO_USE_DATA_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CONSENT_STATUS_ID` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CONSENT_TYPE_ID` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_COUNTRY` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_PERSON_FK` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STATE` FOREIGN KEY (`STATE_ID`) REFERENCES `country_state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STUDY_FK` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` FOREIGN KEY (`SUBJECT_STATUS_ID`) REFERENCES `subject_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_studycomp`
--

DROP TABLE IF EXISTS `link_subject_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_studycomp` (
  `ID` int(11) NOT NULL auto_increment,
  `PERSON_SUBJECT_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`PERSON_SUBJECT_ID`,`STUDY_COMP_ID`,`STUDY_ID`,`STUDY_COMP_STATUS_ID`),
  KEY `LINK_SSC_PERSON_FK` USING BTREE (`PERSON_SUBJECT_ID`),
  KEY `LINK_SSC_STUDY_COMP_FK` USING BTREE (`STUDY_COMP_ID`),
  KEY `LINK_SUBJECT_STUDYCOM_FK3` USING BTREE (`STUDY_ID`),
  KEY `LINK_SSC_STUDY_COMP_STATUS_FK` USING BTREE (`STUDY_COMP_STATUS_ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_1` FOREIGN KEY (`PERSON_SUBJECT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_3` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_4` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_SUBJECT_ID`) REFER `study/per';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marital_status`
--

DROP TABLE IF EXISTS `marital_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marital_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `ID` int(11) NOT NULL auto_increment,
  `FIRST_NAME` varchar(50) default NULL,
  `MIDDLE_NAME` varchar(50) default NULL,
  `LAST_NAME` varchar(50) default NULL,
  `PREFERRED_NAME` varchar(150) default NULL,
  `GENDER_TYPE_ID` int(11) NOT NULL,
  `DATE_OF_BIRTH` date default NULL,
  `DATE_OF_DEATH` date default NULL,
  `REGISTRATION_DATE` date default NULL,
  `CAUSE_OF_DEATH` varchar(255) default NULL,
  `VITAL_STATUS_ID` int(11) NOT NULL,
  `TITLE_TYPE_ID` int(11) NOT NULL default '0',
  `MARITAL_STATUS_ID` int(11) default NULL,
  `PERSON_CONTACT_METHOD_ID` int(11) default NULL,
  `PREFERRED_EMAIL` varchar(150) default NULL,
  `OTHER_EMAIL` varchar(45) default NULL,
  PRIMARY KEY  (`ID`,`VITAL_STATUS_ID`,`TITLE_TYPE_ID`,`GENDER_TYPE_ID`),
  KEY `PERSON_GENDER_TYPE_FK` USING BTREE (`GENDER_TYPE_ID`),
  KEY `PERSON_VITAL_STATUS_FK` USING BTREE (`VITAL_STATUS_ID`),
  KEY `PERSON_TITLE_TYPE_FK` USING BTREE (`TITLE_TYPE_ID`),
  KEY `fk_person_person_contact_method` (`PERSON_CONTACT_METHOD_ID`),
  KEY `fk_person_marital_status` (`MARITAL_STATUS_ID`),
  CONSTRAINT `fk_person_gender_type` FOREIGN KEY (`GENDER_TYPE_ID`) REFERENCES `gender_type` (`ID`),
  CONSTRAINT `fk_person_marital_status` FOREIGN KEY (`MARITAL_STATUS_ID`) REFERENCES `marital_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_person_contact_method` FOREIGN KEY (`PERSON_CONTACT_METHOD_ID`) REFERENCES `person_contact_method` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_title_type` FOREIGN KEY (`TITLE_TYPE_ID`) REFERENCES `title_type` (`ID`),
  CONSTRAINT `fk_person_vital_status` FOREIGN KEY (`VITAL_STATUS_ID`) REFERENCES `vital_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`GENDER_TYPE_ID`) REFER `study/gender';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_contact_method`
--

DROP TABLE IF EXISTS `person_contact_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_contact_method` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `NAME` USING BTREE (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_lastname_history`
--

DROP TABLE IF EXISTS `person_lastname_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_lastname_history` (
  `ID` int(11) NOT NULL auto_increment,
  `PERSON_ID` int(11) NOT NULL,
  `LASTNAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `SURNAME` USING BTREE (`LASTNAME`),
  KEY `PERSON_ID` USING BTREE (`PERSON_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `phone_type`
--

DROP TABLE IF EXISTS `phone_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `ID` int(11) NOT NULL auto_increment,
  `AREA_CODE` varchar(10) default NULL,
  `PHONE_NUMBER` varchar(10) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `PHONE_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`PHONE_NUMBER`,`PERSON_ID`,`PHONE_TYPE_ID`),
  KEY `PHONE_PHONE_TYPE_FK` USING BTREE (`PHONE_TYPE_ID`),
  KEY `PHONE_PERSON_FK` USING BTREE (`PERSON_ID`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_2` FOREIGN KEY (`PHONE_TYPE_ID`) REFERENCES `phone_type` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `question_answer`
--

DROP TABLE IF EXISTS `question_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_answer` (
  `ID` int(11) NOT NULL auto_increment,
  `ANSWER` varchar(45) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registration_status`
--

DROP TABLE IF EXISTS `registration_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registration_status` (
  `ID` int(11) NOT NULL,
  `REGISTRATION_STATUS` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `study`
--

DROP TABLE IF EXISTS `study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(150) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `DATE_OF_APPLICATION` date default NULL,
  `ESTIMATED_YEAR_OF_COMPLETION` int(11) default NULL,
  `CHIEF_INVESTIGATOR` varchar(50) default NULL,
  `CO_INVESTIGATOR` varchar(50) default NULL,
  `AUTO_GENERATE_SUBJECT_UID` int(11) NOT NULL,
  `SUBJECT_UID_START` int(11) default NULL,
  `STUDY_STATUS_ID` int(11) NOT NULL,
  `SUBJECT_KEY_PREFIX` varchar(20) default NULL,
  `CONTACT_PERSON` varchar(50) default NULL,
  `CONTACT_PERSON_PHONE` varchar(20) default NULL,
  `LDAP_GROUP_NAME` varchar(100) default NULL,
  `AUTO_CONSENT` int(11) default NULL,
  `SUB_STUDY_BIOSPECIMEN_PREFIX` varchar(20) default NULL,
  `STUDY_LOGO` blob,
  `FILENAME` varchar(255) default NULL,
  PRIMARY KEY  (`ID`,`STUDY_STATUS_ID`),
  KEY `STUDY_STUDY_STATUS_FK1` USING BTREE (`STUDY_STATUS_ID`),
  KEY `ID` USING BTREE (`ID`),
  CONSTRAINT `study_ibfk_1` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_STATUS_ID`) REFER `study/study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_comp`
--

DROP TABLE IF EXISTS `study_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comp` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `STUDY_ID` int(11) NOT NULL,
  `KEYWORD` varchar(255) default NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`),
  KEY `STUDY_COMP_STUDY_FK` USING BTREE (`STUDY_ID`),
  CONSTRAINT `study_comp_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_comp_status`
--

DROP TABLE IF EXISTS `study_comp_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comp_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_consent_question`
--

DROP TABLE IF EXISTS `study_consent_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_consent_question` (
  `ID` int(11) NOT NULL auto_increment,
  `QUESTION` varchar(700) default NULL,
  `STUDY_ID` int(11) default NULL,
  `DATA_TYPE_ID` int(11) default NULL,
  `DISCRETE_VALUES` varchar(45) default NULL,
  `FIELD_POSITION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_study_consent_question_1` USING BTREE (`STUDY_ID`),
  KEY `fk_study_consent_question_2` USING BTREE (`DATA_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `study_status`
--

DROP TABLE IF EXISTS `study_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_cust_fld_dat`
--

DROP TABLE IF EXISTS `subject_cust_fld_dat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_cust_fld_dat` (
  `ID` int(11) NOT NULL auto_increment,
  `FIELD_DATA` text,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `SUBJECT_CUSTM_FLD_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`LINK_SUBJECT_STUDY_ID`,`SUBJECT_CUSTM_FLD_ID`),
  KEY `SCFD_LINK_SUBJECT_STUDY_FK` USING BTREE (`LINK_SUBJECT_STUDY_ID`),
  KEY `SCFD_SUBJECT_CUSTOM_FIELD_FK` USING BTREE (`SUBJECT_CUSTM_FLD_ID`),
  CONSTRAINT `subject_cust_fld_dat_ibfk_1` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`),
  CONSTRAINT `subject_cust_fld_dat_ibfk_2` FOREIGN KEY (`SUBJECT_CUSTM_FLD_ID`) REFERENCES `subject_custm_fld` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`LINK_SUBJECT_STUDY_ID`) REFER `study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_custm_fld`
--

DROP TABLE IF EXISTS `subject_custm_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_custm_fld` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `DATA_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `MIN_VALUE` varchar(100) default NULL,
  `MAX_VALUE` varchar(100) default NULL,
  `DISCRETE_VALUES` varchar(1000) default NULL,
  `FIELD_POSITION` int(11) NOT NULL,
  `FIELD_TITLE` varchar(255) NOT NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`DATA_TYPE_ID`),
  KEY `SUBJECT_CUSTM_FLD_DATA_TYPE_FK` USING BTREE (`DATA_TYPE_ID`),
  KEY `SUBJECT_CUSTM_FLD_STUDY_FK` USING BTREE (`STUDY_ID`),
  CONSTRAINT `subject_custm_fld_ibfk_1` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `data_type` (`ID`),
  CONSTRAINT `subject_custm_fld_ibfk_2` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DATA_TYPE_ID`) REFER `study/data_typ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_file`
--

DROP TABLE IF EXISTS `subject_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_file` (
  `ID` int(11) NOT NULL auto_increment,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) default NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_subject_file_subject` USING BTREE (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_subject_file_study_comp` USING BTREE (`STUDY_COMP_ID`),
  CONSTRAINT `fk_subject_file_study_comp` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_file_subject` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_status`
--

DROP TABLE IF EXISTS `subject_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_study_consent`
--

DROP TABLE IF EXISTS `subject_study_consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_study_consent` (
  `ID` int(11) NOT NULL auto_increment,
  `SUBJECT_ID` int(11) default NULL,
  `STUDY_ID` int(11) default NULL,
  `STUDY_CONSENT_QUESTION_ID` int(11) default NULL,
  `STATUS` varchar(45) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_subject_study_consent_1` USING BTREE (`STUDY_ID`),
  KEY `fk_subject_study_consent_2` USING BTREE (`SUBJECT_ID`),
  KEY `fk_subject_study_consent_3` USING BTREE (`STUDY_CONSENT_QUESTION_ID`),
  CONSTRAINT `fk_subject_study_consent_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_study_consent_2` FOREIGN KEY (`SUBJECT_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_study_consent_3` FOREIGN KEY (`STUDY_CONSENT_QUESTION_ID`) REFERENCES `study_consent_question` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `title_type`
--

DROP TABLE IF EXISTS `title_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `title_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vital_status`
--

DROP TABLE IF EXISTS `vital_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vital_status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `yes_no`
--

DROP TABLE IF EXISTS `yes_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `yes_no` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(3) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-03-22 11:19:42
