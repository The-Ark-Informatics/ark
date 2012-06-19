/* MySQL script to create the Ark database schema and default reference tables */;

/* Creating the schemas */
CREATE DATABASE /*!32312 IF NOT EXISTS*/ audit /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ study /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ pheno /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ geno /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ lims /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ reporting /*!40100 DEFAULT CHARACTER SET latin1 */;


USE audit;

-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: audit
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
  `CONSENT_DATE` date NOT NULL,
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE study;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: study
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
  `COUNTRY_STATE_ID` int(11) DEFAULT NULL,
  `POST_CODE` varchar(10) DEFAULT NULL,
  `COUNTRY_ID` int(11) DEFAULT NULL,
  `ADDRESS_STATUS_ID` int(11) DEFAULT NULL,
  `ADDRESS_TYPE_ID` int(11) NOT NULL,
  `OTHER_STATE` varchar(45) DEFAULT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENTS` text,
  `PREFERRED_MAILING_ADDRESS` int(11) NOT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`,`ADDRESS_TYPE_ID`,`PERSON_ID`),
  KEY `fk_address_country` (`COUNTRY_ID`) USING BTREE,
  KEY `fk_address_state` (`COUNTRY_STATE_ID`) USING BTREE,
  KEY `fk_address_person` (`PERSON_ID`) USING BTREE,
  KEY `fk_address_address_type` (`ADDRESS_TYPE_ID`) USING BTREE,
  KEY `fk_address_status` (`ADDRESS_STATUS_ID`) USING BTREE,
  KEY `fk_address_preferred_mailing_address_id` (`PREFERRED_MAILING_ADDRESS`) USING BTREE,
  CONSTRAINT `fk_address_address_type` FOREIGN KEY (`ADDRESS_TYPE_ID`) REFERENCES `address_type` (`ID`),
  CONSTRAINT `fk_address_country` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_person` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_state` FOREIGN KEY (`COUNTRY_STATE_ID`) REFERENCES `country_state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_status` FOREIGN KEY (`ADDRESS_STATUS_ID`) REFERENCES `address_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_TYPE_ID`) REFER `study/addre';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Determines the type of function as a Report or Non-Report fu';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  UNIQUE KEY `ARK_MODULE_NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `fk_ark_module_role_1` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ark_module_role_2` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_ROLE_TMPLT_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_PRMSN_ID` FOREIGN KEY (`ARK_PERMISSION_ID`) REFERENCES `ark_permission` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_ROLE_ID` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_ROLE_ID` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_USER_ID` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_USER_ROLE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `CONSENT_DATE` date NOT NULL,
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
  CONSTRAINT `fk_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_component` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_comp_status` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `consent_option`
--

DROP TABLE IF EXISTS `consent_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_option` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_section`
--

DROP TABLE IF EXISTS `consent_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_section` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(500) NOT NULL,
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_attachment`
--

DROP TABLE IF EXISTS `correspondence_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_attachment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  PRIMARY KEY (`ID`),
  KEY `correspondence` (`CORRESPONDENCE_ID`) USING BTREE,
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CORRESPONDENCE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `AUDIT_DATE` date NOT NULL,
  `AUDIT_TIME` varchar(255) NOT NULL,
  `STATUS_TYPE_ID` int(11) DEFAULT NULL,
  `STUDY_MANAGER` varchar(255) DEFAULT NULL,
  `CORRESPONDENCE_DATE` date DEFAULT NULL,
  `CORRESPONDENCE_TIME` varchar(255) DEFAULT NULL,
  `REASON` varchar(4096) DEFAULT NULL,
  `MODE_TYPE_ID` int(11) DEFAULT NULL,
  `DIRECTION_TYPE_ID` int(11) DEFAULT NULL,
  `OUTCOME_TYPE_ID` int(11) DEFAULT NULL,
  `DETAILS` varchar(4096) DEFAULT NULL,
  `COMMENTS` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `corres` (`CORRESPONDENCE_ID`) USING BTREE,
  KEY `correspondence_audit_correspondence_id` (`CORRESPONDENCE_ID`) USING BTREE,
  KEY `correspondence_audit_status_type_id` (`STATUS_TYPE_ID`) USING BTREE,
  KEY `correspondence_audit_mode_type_id` (`MODE_TYPE_ID`) USING BTREE,
  KEY `correspondence_audit_direction_type_id` (`DIRECTION_TYPE_ID`) USING BTREE,
  KEY `correspondence_audit_outcome_type_id` (`OUTCOME_TYPE_ID`) USING BTREE,
  KEY `correspondence_audit_study_id` (`STUDY_ID`) USING BTREE,
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CORRESPONDENCE_AUDIT_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  PRIMARY KEY (`ID`),
  KEY `correspondence_audit_attachment_correspondence_audit_id` (`CORRESPONDENCE_AUDIT_ID`) USING BTREE,
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_status_type`
--

DROP TABLE IF EXISTS `correspondence_status_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_status_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondences`
--

DROP TABLE IF EXISTS `correspondences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondences` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STATUS_TYPE_ID` int(11) DEFAULT NULL,
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
  PRIMARY KEY (`ID`),
  KEY `status_type` (`STATUS_TYPE_ID`) USING BTREE,
  KEY `mode_type` (`MODE_TYPE_ID`) USING BTREE,
  KEY `direction_type` (`DIRECTION_TYPE_ID`) USING BTREE,
  KEY `outcome_type` (`OUTCOME_TYPE_ID`) USING BTREE,
  KEY `correspondences_study_id` (`STUDY_ID`) USING BTREE,
  KEY `correspondences_person_id` (`PERSON_ID`),
  KEY `fk_correspondences_ark_user` (`ARK_USER_ID`),
  CONSTRAINT `correspondences_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_status_type_id` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `correspondence_status_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_correspondences_ark_user` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `COUNTRY_CODE` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country_state`
--

DROP TABLE IF EXISTS `country_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country_state` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COUNTRY_ID` int(11) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_country_id` (`COUNTRY_ID`) USING BTREE,
  CONSTRAINT `fk_country_id` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='A link table that associates a country and its respective st';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_UNIT_TYPE_ID` (`UNIT_TYPE_ID`),
  KEY `FK_CUSTOMFIELD_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOMFIELD_FIELD_TYPE_ID` (`FIELD_TYPE_ID`),
  CONSTRAINT `FK_CUSTOMFIELD_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELD_FIELD_TYPE_ID` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_UNIT_TYPE_ID` FOREIGN KEY (`UNIT_TYPE_ID`) REFERENCES `unit_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `custom_field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_CFU_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CFU_UPLOAD_ID` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`,`PERSON_ID`,`EMAIL_ACCOUNT_TYPE_ID`),
  KEY `EMAIL_ACCOUNT_PER_FK1` (`PERSON_ID`) USING BTREE,
  KEY `EMAIL_ACCOUNT_EMA_FK1` (`EMAIL_ACCOUNT_TYPE_ID`) USING BTREE,
  CONSTRAINT `email_account_ibfk_1` FOREIGN KEY (`EMAIL_ACCOUNT_TYPE_ID`) REFERENCES `email_account_type` (`ID`),
  CONSTRAINT `email_account_ibfk_2` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`EMAIL_ACCOUNT_TYPE_ID`) REFER `study';
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
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_correspondence_audit_person`
--

DROP TABLE IF EXISTS `link_correspondence_audit_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_correspondence_audit_person` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CORRESPONDENCE_AUDIT_ID` int(11) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `correspondence_audit_people_person_id` (`PERSON_ID`) USING BTREE,
  KEY `correspondence_audit_people_correspondence_audit_id` (`CORRESPONDENCE_AUDIT_ID`) USING BTREE,
  CONSTRAINT `correspondence_audit_people_correspondence_audit_id` FOREIGN KEY (`CORRESPONDENCE_AUDIT_ID`) REFERENCES `correspondence_audit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondence_audit_people_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`,`PERSON_ID`,`STUDY_SITE_ID`),
  KEY `LINK_SITE_CONTACT_FK1` (`PERSON_ID`) USING BTREE,
  KEY `LINK_SITE_CONTACT_STUDY_SITE_FK` (`STUDY_SITE_ID`) USING BTREE,
  CONSTRAINT `link_site_contact_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_site_contact_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`,`STUDY_ID`,`STUDY_COMP_STATUS_ID`,`STUDY_COMP_ID`),
  KEY `LSSC_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `LSSC_STUDY_COMP_FK` (`STUDY_COMP_ID`) USING BTREE,
  KEY `LSSC_STUDY_COMP_STATUS_FK` (`STUDY_COMP_STATUS_ID`) USING BTREE,
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_SITE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`STUDY_SITE_ID`,`STUDY_ID`),
  KEY `LINK_STUDY_STUDYSITE_STUDY_SITE_FK` (`STUDY_SITE_ID`) USING BTREE,
  KEY `LINK_STUDY_STUDYSITE_STUDY_FK` (`STUDY_ID`) USING BTREE,
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `SUB_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`SUB_STUDY_ID`),
  KEY `LINK_STUDY_SUBSTUDY_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK` (`SUB_STUDY_ID`) USING BTREE,
  CONSTRAINT `link_study_substudy_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `link_study_substudy_ibfk_2` FOREIGN KEY (`SUB_STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`,`STUDY_ID`),
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
-- Table structure for table `link_subject_study`
--

DROP TABLE IF EXISTS `link_subject_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_study` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `SUBJECT_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `SUBJECT_UID` varchar(50) NOT NULL,
  `AMDRF_ID` int(11) DEFAULT NULL,
  `STUDY_APPROACH_DATE` date DEFAULT NULL,
  `SEND_NEWS_LETTER` int(11) DEFAULT NULL,
  `YEAR_OF_FIRST_MAMOGRAM` int(11) DEFAULT NULL,
  `YEAR_OF_RECENT_MAMOGRAM` int(11) DEFAULT NULL,
  `TOTAL_MAMOGRAMS` int(11) DEFAULT NULL,
  `SITE_STREET_ADDRESS` varchar(255) DEFAULT NULL,
  `SITE_CITY` varchar(255) DEFAULT NULL,
  `SITE_POST` varchar(45) DEFAULT NULL,
  `COUNTRY_ID` int(11) DEFAULT NULL,
  `STATE_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_ID` int(11) DEFAULT NULL,
  `CONSENT_TYPE_ID` int(11) DEFAULT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `OTHER_STATE` varchar(255) DEFAULT NULL,
  `HEARD_ABOUT_STUDY` varchar(500) DEFAULT NULL,
  `COMMENTS` varchar(1000) DEFAULT NULL,
  `CONSENT_DOWNLOADED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`SUBJECT_UID`),
  UNIQUE KEY `UQ_STUDY_ID_SUBJECT_UID` (`STUDY_ID`,`SUBJECT_UID`) USING BTREE,
  KEY `FK_LINK_SUBJECT_STUDY_PERSON_FK` (`PERSON_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` (`SUBJECT_STATUS_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_STATE` (`STATE_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_COUNTRY` (`COUNTRY_ID`),
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
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_COUNTRY` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_PERSON_FK` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STATE` FOREIGN KEY (`STATE_ID`) REFERENCES `country_state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STUDY_FK` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` FOREIGN KEY (`SUBJECT_STATUS_ID`) REFERENCES `subject_status` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`,`PERSON_SUBJECT_ID`,`STUDY_COMP_ID`,`STUDY_ID`,`STUDY_COMP_STATUS_ID`),
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `GENDER_TYPE_ID` int(11) NOT NULL DEFAULT '0',
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `DATE_OF_DEATH` date DEFAULT NULL,
  `REGISTRATION_DATE` date DEFAULT NULL,
  `CAUSE_OF_DEATH` varchar(255) DEFAULT NULL,
  `VITAL_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `TITLE_TYPE_ID` int(11) NOT NULL DEFAULT '0',
  `MARITAL_STATUS_ID` int(11) DEFAULT NULL,
  `PERSON_CONTACT_METHOD_ID` int(11) DEFAULT NULL,
  `PREFERRED_EMAIL` varchar(150) DEFAULT NULL,
  `OTHER_EMAIL` varchar(45) DEFAULT NULL,
  `DATE_LAST_KNOWN_ALIVE` date DEFAULT NULL,
  PRIMARY KEY (`ID`,`VITAL_STATUS_ID`,`TITLE_TYPE_ID`,`GENDER_TYPE_ID`),
  KEY `PERSON_GENDER_TYPE_FK` (`GENDER_TYPE_ID`) USING BTREE,
  KEY `PERSON_VITAL_STATUS_FK` (`VITAL_STATUS_ID`) USING BTREE,
  KEY `PERSON_TITLE_TYPE_FK` (`TITLE_TYPE_ID`) USING BTREE,
  KEY `fk_person_person_contact_method` (`PERSON_CONTACT_METHOD_ID`),
  KEY `fk_person_marital_status` (`MARITAL_STATUS_ID`),
  CONSTRAINT `fk_person_gender_type` FOREIGN KEY (`GENDER_TYPE_ID`) REFERENCES `gender_type` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_person_marital_status` FOREIGN KEY (`MARITAL_STATUS_ID`) REFERENCES `marital_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_person_contact_method` FOREIGN KEY (`PERSON_CONTACT_METHOD_ID`) REFERENCES `person_contact_method` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_title_type` FOREIGN KEY (`TITLE_TYPE_ID`) REFERENCES `title_type` (`ID`),
  CONSTRAINT `fk_person_vital_status` FOREIGN KEY (`VITAL_STATUS_ID`) REFERENCES `vital_status` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`GENDER_TYPE_ID`) REFER `study/gender';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`),
  KEY `SURNAME` (`LASTNAME`) USING BTREE,
  KEY `PERSON_ID` (`PERSON_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(10) DEFAULT NULL,
  `PHONE_NUMBER` varchar(10) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `PHONE_TYPE_ID` int(11) NOT NULL,
  `PHONE_STATUS_ID` int(11) DEFAULT NULL,
  `SOURCE` varchar(500) DEFAULT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENT` varchar(1000) DEFAULT NULL,
  `SILENT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`,`PHONE_NUMBER`,`PERSON_ID`,`PHONE_TYPE_ID`),
  UNIQUE KEY `AREA_CODE` (`AREA_CODE`,`PHONE_NUMBER`,`PERSON_ID`) USING BTREE,
  UNIQUE KEY `AREA_CODE_2` (`AREA_CODE`,`PHONE_NUMBER`,`PHONE_TYPE_ID`,`PERSON_ID`),
  KEY `PHONE_PHONE_TYPE_FK` (`PHONE_TYPE_ID`) USING BTREE,
  KEY `PHONE_PERSON_FK` (`PERSON_ID`) USING BTREE,
  KEY `phone_ibfk_3` (`PHONE_STATUS_ID`),
  KEY `phone_ibfk_4` (`SILENT`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_2` FOREIGN KEY (`PHONE_TYPE_ID`) REFERENCES `phone_type` (`ID`),
  CONSTRAINT `phone_ibfk_3` FOREIGN KEY (`PHONE_STATUS_ID`) REFERENCES `phone_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_4` FOREIGN KEY (`SILENT`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Temporary table structure for view `role_policy`
--

DROP TABLE IF EXISTS `role_policy`;
/*!50001 DROP VIEW IF EXISTS `role_policy`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `role_policy` (
  `Role` varchar(255),
  `Module` varchar(255),
  `FunctionGroup` varchar(255),
  `Permission` varchar(255),
  `Function` varchar(1000)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

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
  PRIMARY KEY (`ID`,`STUDY_STATUS_ID`),
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
  PRIMARY KEY (`ID`,`STUDY_ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`),
  KEY `STUDY_COMP_STUDY_FK` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `study_comp_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`,`ADDRESS_ID`,`DOMAIN_TYPE_ID`),
  KEY `STUDY_SITE_ADDRES_FK1` (`ADDRESS_ID`) USING BTREE,
  KEY `STUDY_SITE_DOMAIN_TYPE_FK` (`DOMAIN_TYPE_ID`) USING BTREE,
  CONSTRAINT `study_site_ibfk_1` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `study_site_ibfk_2` FOREIGN KEY (`DOMAIN_TYPE_ID`) REFERENCES `domain_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_ID`) REFER `study/address`(`';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `study_user_role_permission_view`
--

DROP TABLE IF EXISTS `study_user_role_permission_view`;
/*!50001 DROP VIEW IF EXISTS `study_user_role_permission_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `study_user_role_permission_view` (
  `studyName` varchar(150),
  `userName` varchar(500),
  `roleName` varchar(255),
  `moduleName` varchar(255),
  `create` varchar(1),
  `read` varchar(1),
  `update` varchar(1),
  `delete` varchar(1)
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
  KEY `FK_CFD_LINK_SUBJECT_STUDY_ID` (`LINK_SUBJECT_STUDY_ID`),
  KEY `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  PRIMARY KEY (`ID`),
  KEY `fk_subject_file_subject` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  KEY `fk_subject_file_study_comp` (`STUDY_COMP_ID`) USING BTREE,
  CONSTRAINT `fk_subject_file_study_comp` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_file_subject` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `subjectuid_padchar`
--

DROP TABLE IF EXISTS `subjectuid_padchar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_padchar` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `subjectuid_token`
--

DROP TABLE IF EXISTS `subjectuid_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `UPLOAD_REPORT` longblob,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  KEY `fk_upload_study` (`STUDY_ID`),
  KEY `fk_upload_ark_function_id` (`ARK_FUNCTION_ID`),
  CONSTRAINT `fk_upload_ark_function_id` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `study/del';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!50013 DEFINER=`arkadmin`@`127.0.0.%` SQL SECURITY DEFINER */
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
/*!50013 DEFINER=`arkadmin`@`127.0.0.%` SQL SECURITY DEFINER */
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

-- Dump completed on 2012-04-04 11:30:53

USE pheno;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: pheno
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `STATUS_ID` int(11) NOT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_collection_status` (`STATUS_ID`) USING BTREE,
  KEY `fk_collection_study` (`STUDY_ID`),
  CONSTRAINT `fk_collection_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STATUS_ID`) REFER `pheno/status`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_upload`
--

DROP TABLE IF EXISTS `collection_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UPLOAD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_collection_upload_file_upload` (`UPLOAD_ID`) USING BTREE,
  KEY `fk_collection_upload_collection` (`COLLECTION_ID`) USING BTREE,
  CONSTRAINT `fk_collection_upload_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_collection_upload_upload` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `DELIMITER_CHARACTER` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `FIELD_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `UNITS` varchar(50) DEFAULT NULL,
  `SEQ_NUM` int(11) DEFAULT NULL,
  `MIN_VALUE` varchar(100) DEFAULT NULL,
  `MAX_VALUE` varchar(100) DEFAULT NULL,
  `ENCODED_VALUES` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `QUALITY_CONTROL_STATUS` int(11) NOT NULL DEFAULT '1',
  `MISSING_VALUE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`,`FIELD_TYPE_ID`),
  KEY `fk_field_field_type` (`FIELD_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE,
  KEY `NAME` (`NAME`) USING BTREE,
  CONSTRAINT `field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_TYPE_ID`) REFER `pheno/field_t';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_collection`
--

DROP TABLE IF EXISTS `field_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_field_collection_study` (`STUDY_ID`) USING BTREE,
  KEY `fk_field_collection_field` (`FIELD_ID`) USING BTREE,
  KEY `fk_field_collection_collection` (`COLLECTION_ID`) USING BTREE,
  CONSTRAINT `fk_field_collection_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_field_collection_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_data`
--

DROP TABLE IF EXISTS `field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COLLECTION_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `DATE_COLLECTED` datetime NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `VALUE` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `PASSED_QUALITY_CONTROL` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_field_data_collection` (`COLLECTION_ID`),
  KEY `fk_field_data_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_field_data_field` (`FIELD_ID`),
  CONSTRAINT `fk_field_data_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_field_data_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_field_data_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_data_log`
--

DROP TABLE IF EXISTS `field_data_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_data_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIELD_DATA_ID` int(11) NOT NULL,
  `COMMENT` varchar(50) NOT NULL,
  `VALUE` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_field_group`
--

DROP TABLE IF EXISTS `field_field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_field_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIELD_GROUP_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`FIELD_GROUP_ID`,`FIELD_ID`),
  KEY `fk_field_groups_field_group1` (`FIELD_GROUP_ID`) USING BTREE,
  KEY `fk_field_groups_field` (`FIELD_ID`) USING BTREE,
  CONSTRAINT `field_field_group_ibfk_1` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `field_field_group_ibfk_2` FOREIGN KEY (`FIELD_GROUP_ID`) REFERENCES `field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_ID`) REFER `pheno/field`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_group`
--

DROP TABLE IF EXISTS `field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `STUDY_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_group_upload`
--

DROP TABLE IF EXISTS `field_group_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_group_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UPLOAD_ID` int(11) NOT NULL,
  `FIELD_GROUP_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_field_group_upload_upload` (`UPLOAD_ID`) USING BTREE,
  KEY `fk_field_group_upload_field_field_group` (`FIELD_GROUP_ID`) USING BTREE,
  CONSTRAINT `field_group_upload_ibfk_1` FOREIGN KEY (`FIELD_GROUP_ID`) REFERENCES `field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `field_group_upload_ibfk_2` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_GROUP_ID`) REFER `pheno/field_';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `field_summary`
--

DROP TABLE IF EXISTS `field_summary`;
/*!50001 DROP VIEW IF EXISTS `field_summary`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `field_summary` (
  `study_id` int(11),
  `fields` bigint(21),
  `fields_with_data` bigint(21)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `field_upload`
--

DROP TABLE IF EXISTS `field_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UPLOAD_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_field_groups_field` (`FIELD_ID`) USING BTREE,
  KEY `fk_field_upload_upload` (`UPLOAD_ID`) USING BTREE,
  CONSTRAINT `fk_field_upload_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_field_upload_upload` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_ID`) REFER `pheno/field`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `field_upload_v`
--

DROP TABLE IF EXISTS `field_upload_v`;
/*!50001 DROP VIEW IF EXISTS `field_upload_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `field_upload_v` (
  `ID` int(11),
  `STUDY_ID` int(11),
  `FILE_FORMAT_ID` int(11),
  `DELIMITER_TYPE_ID` int(11),
  `FILENAME` text,
  `PAYLOAD` longblob,
  `CHECKSUM` varchar(50),
  `USER_ID` varchar(50),
  `INSERT_TIME` datetime,
  `UPDATE_USER_ID` varchar(50),
  `UPDATE_TIME` datetime,
  `START_TIME` datetime,
  `FINISH_TIME` datetime,
  `UPLOAD_REPORT` longblob
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pheno_collection`
--

DROP TABLE IF EXISTS `pheno_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
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
  CONSTRAINT `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `study`.`custom_field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_QUESTIONNAIRE_STATUS_ID` FOREIGN KEY (`QUESTIONNAIRE_STATUS_ID`) REFERENCES `questionnaire_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REVIEWED_BY_ARK_USER_ID` FOREIGN KEY (`REVIEWED_BY_ID`) REFERENCES `study`.`ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Final view structure for view `field_summary`
--

/*!50001 DROP TABLE IF EXISTS `field_summary`*/;
/*!50001 DROP VIEW IF EXISTS `field_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`192.168.113.%` SQL SECURITY DEFINER */
/*!50001 VIEW `field_summary` AS select `f`.`STUDY_ID` AS `study_id`,count(`f`.`ID`) AS `fields`,(select count(distinct `fd`.`FIELD_ID`) AS `count(distinct ``fd``.``FIELD_ID``)` from (`field_data` `fd` join `field_collection` `fc`) where ((`fd`.`COLLECTION_ID` = `fc`.`COLLECTION_ID`) and (`fc`.`STUDY_ID` = `f`.`STUDY_ID`))) AS `fields_with_data` from `field` `f` group by `f`.`STUDY_ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `field_upload_v`
--

/*!50001 DROP TABLE IF EXISTS `field_upload_v`*/;
/*!50001 DROP VIEW IF EXISTS `field_upload_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`192.168.113.%` SQL SECURITY DEFINER */
/*!50001 VIEW `field_upload_v` AS select distinct `upload`.`ID` AS `ID`,`upload`.`STUDY_ID` AS `STUDY_ID`,`upload`.`FILE_FORMAT_ID` AS `FILE_FORMAT_ID`,`upload`.`DELIMITER_TYPE_ID` AS `DELIMITER_TYPE_ID`,`upload`.`FILENAME` AS `FILENAME`,`upload`.`PAYLOAD` AS `PAYLOAD`,`upload`.`CHECKSUM` AS `CHECKSUM`,`upload`.`USER_ID` AS `USER_ID`,`upload`.`INSERT_TIME` AS `INSERT_TIME`,`upload`.`UPDATE_USER_ID` AS `UPDATE_USER_ID`,`upload`.`UPDATE_TIME` AS `UPDATE_TIME`,`upload`.`START_TIME` AS `START_TIME`,`upload`.`FINISH_TIME` AS `FINISH_TIME`,`upload`.`UPLOAD_REPORT` AS `UPLOAD_REPORT` from (`upload` join `field_upload`) where (`upload`.`ID` = `field_upload`.`UPLOAD_ID`) */;
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

-- Dump completed on 2012-04-04 11:30:53

USE geno;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: geno
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` text,
  `STATUS_ID` int(11) NOT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `EXPIRY_DATE` datetime DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`STATUS_ID`),
  KEY `fk_collection_status` (`STATUS_ID`) USING BTREE,
  CONSTRAINT `collection_ibfk_1` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`STATUS_ID`) REFER `geno/STATUS`(`ID`';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_import`
--

DROP TABLE IF EXISTS `collection_import`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection_import` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COLLECTION_ID` int(11) NOT NULL,
  `MARKER_GROUP_ID` int(11) NOT NULL,
  `IMPORT_TYPE_ID` int(11) NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`COLLECTION_ID`,`MARKER_GROUP_ID`,`IMPORT_TYPE_ID`),
  KEY `fk_collection_import_collection` (`COLLECTION_ID`) USING BTREE,
  KEY `fk_collection_import_import_type` (`IMPORT_TYPE_ID`) USING BTREE,
  KEY `fk_collection_import_marker_group` (`MARKER_GROUP_ID`) USING BTREE,
  CONSTRAINT `collection_import_ibfk_1` FOREIGN KEY (`MARKER_GROUP_ID`) REFERENCES `marker_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `collection_import_ibfk_3` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `collection_import_ibfk_4` FOREIGN KEY (`IMPORT_TYPE_ID`) REFERENCES `import_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `decode_mask`
--

DROP TABLE IF EXISTS `decode_mask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `decode_mask` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIT_POSITION` int(11) NOT NULL,
  `MARKER_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`MARKER_ID`,`COLLECTION_ID`),
  KEY `fk_decode_mask_marker` (`MARKER_ID`) USING BTREE,
  KEY `fk_decode_mask_collection` (`COLLECTION_ID`) USING BTREE,
  CONSTRAINT `decode_mask_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `decode_mask_ibfk_2` FOREIGN KEY (`MARKER_ID`) REFERENCES `marker` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delimiter_type`
--

DROP TABLE IF EXISTS `delimiter_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delimiter_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `encoded_data`
--

DROP TABLE IF EXISTS `encoded_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encoded_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `ENCODED_BIT1` longblob,
  `ENCODED_BIT2` longblob,
  PRIMARY KEY (`ID`,`SUBJECT_ID`,`COLLECTION_ID`),
  KEY `fk_encoded_data_collection` (`COLLECTION_ID`) USING BTREE,
  CONSTRAINT `encoded_data_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_format`
--

DROP TABLE IF EXISTS `file_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_format` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `import_type`
--

DROP TABLE IF EXISTS `import_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `import_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marker`
--

DROP TABLE IF EXISTS `marker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marker` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MARKER_GROUP_ID` int(11) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `DESCRIPTION` text,
  `CHROMOSOME` varchar(50) NOT NULL,
  `POSITION` decimal(65,30) DEFAULT NULL,
  `GENE` varchar(100) DEFAULT NULL,
  `MAJOR_ALLELE` varchar(10) DEFAULT NULL,
  `MINOR_ALLELE` varchar(10) DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`MARKER_GROUP_ID`),
  KEY `fk_marker_marker_group` (`MARKER_GROUP_ID`) USING BTREE,
  CONSTRAINT `marker_ibfk_1` FOREIGN KEY (`MARKER_GROUP_ID`) REFERENCES `marker_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_GROUP_ID`) REFER `geno/MARKER';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marker_group`
--

DROP TABLE IF EXISTS `marker_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marker_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `MARKER_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `DESCRIPTION` text,
  `VISIBLE` decimal(1,0) DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`MARKER_TYPE_ID`),
  KEY `fk_marker_group_marker_type` (`MARKER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  CONSTRAINT `marker_group_ibfk_1` FOREIGN KEY (`MARKER_TYPE_ID`) REFERENCES `marker_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_TYPE_ID`) REFER `geno/MARKER_';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marker_meta_data`
--

DROP TABLE IF EXISTS `marker_meta_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marker_meta_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `META_DATA_ID` int(11) NOT NULL,
  `MARKER_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`META_DATA_ID`,`MARKER_ID`),
  KEY `fk_marker_meta_data_marker` (`MARKER_ID`) USING BTREE,
  KEY `fk_marker_meta_data_meta_data` (`META_DATA_ID`) USING BTREE,
  CONSTRAINT `marker_meta_data_ibfk_1` FOREIGN KEY (`MARKER_ID`) REFERENCES `marker` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `marker_meta_data_ibfk_2` FOREIGN KEY (`META_DATA_ID`) REFERENCES `meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_ID`) REFER `geno/MARKER`(`ID`';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marker_type`
--

DROP TABLE IF EXISTS `marker_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marker_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meta_data`
--

DROP TABLE IF EXISTS `meta_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `META_DATA_FIELD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `VALUE` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`META_DATA_FIELD_ID`,`COLLECTION_ID`),
  KEY `fk_meta_data_meta_data_field` (`META_DATA_FIELD_ID`) USING BTREE,
  KEY `fk_meta_data_collection` (`COLLECTION_ID`) USING BTREE,
  CONSTRAINT `meta_data_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `meta_data_ibfk_2` FOREIGN KEY (`META_DATA_FIELD_ID`) REFERENCES `meta_data_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meta_data_field`
--

DROP TABLE IF EXISTS `meta_data_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_data_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `META_DATA_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `UNITS` varchar(50) DEFAULT NULL,
  `SEQ_NUM` decimal(65,30) DEFAULT NULL,
  `MIN_VALUE` varchar(100) DEFAULT NULL,
  `MAX_VALUE` varchar(100) DEFAULT NULL,
  `DISCRETE_VALUES` varchar(100) DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`META_DATA_TYPE_ID`),
  KEY `fk_meta_data_field_md_type` (`META_DATA_TYPE_ID`) USING BTREE,
  CONSTRAINT `meta_data_field_ibfk_1` FOREIGN KEY (`META_DATA_TYPE_ID`) REFERENCES `meta_data_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`META_DATA_TYPE_ID`) REFER `geno/META';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meta_data_type`
--

DROP TABLE IF EXISTS `meta_data_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_data_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_marker_meta_data`
--

DROP TABLE IF EXISTS `subject_marker_meta_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_marker_meta_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_ID` int(11) NOT NULL,
  `META_DATA_ID` int(11) NOT NULL,
  `MARKER_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`SUBJECT_ID`,`META_DATA_ID`,`MARKER_ID`),
  KEY `fk_subject_marker_meta_data_meta_data` (`META_DATA_ID`) USING BTREE,
  KEY `fk_subject_marker_meta_data_marker` (`MARKER_ID`) USING BTREE,
  CONSTRAINT `subject_marker_meta_data_ibfk_1` FOREIGN KEY (`MARKER_ID`) REFERENCES `marker` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `subject_marker_meta_data_ibfk_2` FOREIGN KEY (`META_DATA_ID`) REFERENCES `meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_ID`) REFER `geno/MARKER`(`ID`';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_meta_data`
--

DROP TABLE IF EXISTS `subject_meta_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_meta_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_ID` int(11) NOT NULL,
  `META_DATA_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`SUBJECT_ID`,`META_DATA_ID`),
  KEY `fk_subject_meta_data_meta_data` (`META_DATA_ID`) USING BTREE,
  CONSTRAINT `subject_meta_data_ibfk_1` FOREIGN KEY (`META_DATA_ID`) REFERENCES `meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`META_DATA_ID`) REFER `geno/META_DATA';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob,
  `CHECKSUM` varchar(20) DEFAULT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`FILE_FORMAT_ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  CONSTRAINT `upload_ibfk_1` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`FILE_FORMAT_ID`) REFER `geno/FILE_FO';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload_collection`
--

DROP TABLE IF EXISTS `upload_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UPLOAD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) DEFAULT NULL,
  `INSERT_TIME` datetime DEFAULT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`COLLECTION_ID`,`UPLOAD_ID`),
  KEY `fk_upload_collection_collection` (`COLLECTION_ID`) USING BTREE,
  KEY `fk_upload_collection_upload` (`UPLOAD_ID`) USING BTREE,
  CONSTRAINT `upload_collection_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `upload_collection_ibfk_2` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload_marker_group`
--

DROP TABLE IF EXISTS `upload_marker_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_marker_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UPLOAD_ID` int(11) NOT NULL,
  `MARKER_GROUP_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime DEFAULT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`,`UPLOAD_ID`,`MARKER_GROUP_ID`),
  KEY `fk_upload_marker_group_upload` (`UPLOAD_ID`) USING BTREE,
  KEY `fk_upload_marker_group_marker_group` (`MARKER_GROUP_ID`) USING BTREE,
  CONSTRAINT `upload_marker_group_ibfk_1` FOREIGN KEY (`MARKER_GROUP_ID`) REFERENCES `marker_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `upload_marker_group_ibfk_2` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_GROUP_ID`) REFER `geno/MARKER';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE lims;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: lims
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointments` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `PURPOSE` text,
  `NOTIFY` varchar(100) DEFAULT NULL,
  `PATIENT_ID` int(11) NOT NULL,
  `TIME` datetime DEFAULT NULL,
  `DATE` datetime NOT NULL,
  `ALERT_DATE` datetime DEFAULT NULL,
  `SURVEY_ID` int(11) DEFAULT NULL,
  `SENT_TIMESTAMP` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `ATTACHEDBY` text NOT NULL,
  `FILE_NAME` varchar(50) DEFAULT NULL,
  `COMMENTS` text,
  `DOMAIN` varchar(50) NOT NULL,
  `NA` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode_command`
--

DROP TABLE IF EXISTS `barcode_command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_command` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRINTER_ID` int(11) NOT NULL,
  `COMMAND` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  `MEMORY` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode_label`
--

DROP TABLE IF EXISTS `barcode_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `BARCODE_PRINTER_ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `LABEL_PREFIX` text NOT NULL,
  `LABEL_SUFFIX` text NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_label_study` (`STUDY_ID`),
  KEY `fk_barcode_label_printer` (`BARCODE_PRINTER_ID`),
  KEY `fk_barcode_label_barcode_printer` (`BARCODE_PRINTER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode_printer`
--

DROP TABLE IF EXISTS `barcode_printer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_printer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `LOCATION` varchar(100) NOT NULL,
  `HOST` varchar(100) NOT NULL,
  `PORT` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='NOTE: the name of the printer MUST match the shared name';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcodeformat`
--

DROP TABLE IF EXISTS `barcodeformat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcodeformat` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `FIELDNAME` varchar(100) DEFAULT NULL,
  `CONST` varchar(100) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `LENGTH` int(11) DEFAULT NULL,
  `FORMAT` varchar(50) DEFAULT NULL,
  `ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_format_idx` (`STUDY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcodeid_engine`
--

DROP TABLE IF EXISTS `barcodeid_engine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcodeid_engine` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `CLASS` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcodeid_engine_study_idx` (`STUDY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bio_transaction`
--

DROP TABLE IF EXISTS `bio_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bio_transaction` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `TRANSACTION_DATE` datetime NOT NULL,
  `QUANTITY` double NOT NULL,
  `RECORDER` varchar(255) DEFAULT NULL,
  `REASON` text,
  `STATUS_ID` int(11) DEFAULT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_BIOTRANSACTION_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  KEY `FK_BIOTRANSACTION_STATUS_ID` (`STATUS_ID`),
  KEY `FK_BIOTRANSACTION_REQUEST_ID` (`REQUEST_ID`),
  CONSTRAINT `FK_BIOTRANSACTION_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_REQUEST_ID` FOREIGN KEY (`REQUEST_ID`) REFERENCES `access_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_STATUS_ID` FOREIGN KEY (`STATUS_ID`) REFERENCES `bio_transaction_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biocollection`
--

DROP TABLE IF EXISTS `biocollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `COLLECTIONDATE` datetime DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `COMMENTS` text,
  `HOSPITAL` varchar(50) DEFAULT NULL,
  `SURGERYDATE` datetime DEFAULT NULL,
  `DIAG_CATEGORY` varchar(50) DEFAULT NULL,
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
  KEY `fk_collection_idx` (`NAME`),
  KEY `fk_collection_name_idx` (`NAME`),
  KEY `fk_collection_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_collection_study` (`STUDY_ID`),
  CONSTRAINT `fk_collection_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `biocollectionuid_token`
--

DROP TABLE IF EXISTS `biocollectionuid_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata`
--

DROP TABLE IF EXISTS `biodata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DOMAIN_ID` int(11) DEFAULT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `DATE_COLLECTED` datetime NOT NULL,
  `STRING_VALUE` text,
  `NUMBER_VALUE` int(11) DEFAULT NULL,
  `DATE_VALUE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biodata_domain_idx` (`DOMAIN_ID`),
  KEY `fk_biodata_field_idx` (`FIELD_ID`),
  CONSTRAINT `fk_biodata_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `biodata_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_criteria`
--

DROP TABLE IF EXISTS `biodata_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_criteria` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `DOMAIN` varchar(50) DEFAULT NULL,
  `FIELD` varchar(50) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_field`
--

DROP TABLE IF EXISTS `biodata_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_ID` int(11) NOT NULL,
  `FORMAT` varchar(20) DEFAULT NULL,
  `COLUMNNAME` varchar(50) NOT NULL,
  `UNIT_ID` int(11) DEFAULT NULL,
  `LOVTYPE` varchar(20) DEFAULT NULL,
  `DOMAIN` varchar(50) DEFAULT NULL,
  `FIELDNAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_field_group`
--

DROP TABLE IF EXISTS `biodata_field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_field_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIELD_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `POSITION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biodata_field_group_field_idx` (`FIELD_ID`),
  KEY `fk_biodata_field_group_group_idx` (`GROUP_ID`),
  CONSTRAINT `fk_biodata_field_group_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `biodata_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biodata_field_group_group` FOREIGN KEY (`GROUP_ID`) REFERENCES `biodata_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_field_lov`
--

DROP TABLE IF EXISTS `biodata_field_lov`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_field_lov` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LIST_ID` int(11) NOT NULL,
  `VALUE` varchar(50) NOT NULL,
  `ORDER` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_group`
--

DROP TABLE IF EXISTS `biodata_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` varchar(100) NOT NULL,
  `DOMAIN` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_group_criteria`
--

DROP TABLE IF EXISTS `biodata_group_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_group_criteria` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CRITERIA_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biodata_group_criteria_criteria_idx` (`CRITERIA_ID`),
  KEY `fk_biodata_group_criteria_group_idx` (`GROUP_ID`),
  CONSTRAINT `fk_biodata_group_criteria` FOREIGN KEY (`CRITERIA_ID`) REFERENCES `biodata_criteria` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biodata_group_group` FOREIGN KEY (`GROUP_ID`) REFERENCES `biodata_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_lov_list`
--

DROP TABLE IF EXISTS `biodata_lov_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_lov_list` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_type`
--

DROP TABLE IF EXISTS `biodata_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biodata_unit`
--

DROP TABLE IF EXISTS `biodata_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biodata_unit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UNITNAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimen`
--

DROP TABLE IF EXISTS `biospecimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen` (
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
  `DELETED` int(11) DEFAULT NULL,
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
  `QUANTITY` double DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT NULL,
  `TREATMENT_TYPE_ID` int(11) NOT NULL,
  `BARCODED` tinyint(1) NOT NULL DEFAULT '0',
  `BIOSPECIMEN_QUALITY_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_STATUS_ID` int(11) DEFAULT NULL,
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
  CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biocollection` FOREIGN KEY (`BIOCOLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biospecimen` FOREIGN KEY (`PARENT_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_treatment_type_id` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_BIOSPECFDATA_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  CONSTRAINT `FK_BIOSPECFDATA_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `biospecimenuid_token`
--

DROP TABLE IF EXISTS `biospecimenuid_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flag`
--

DROP TABLE IF EXISTS `flag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flag` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `DOMAIN` varchar(50) NOT NULL,
  `REFERENCE_ID` int(11) NOT NULL,
  `USER` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `ACTIVITY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_box`
--

DROP TABLE IF EXISTS `inv_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_box` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `NOOFCOL` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `RACK_ID` int(11) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `NOOFROW` int(11) NOT NULL,
  `COLNOTYPE_ID` int(11) NOT NULL,
  `ROWNOTYPE_ID` int(11) NOT NULL,
  `TRANSFER_ID` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`),
  KEY `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`),
  KEY `fk_inv_box_rack_idx` (`RACK_ID`),
  CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rack` FOREIGN KEY (`RACK_ID`) REFERENCES `inv_rack` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `ROWNO` int(11) DEFAULT NULL,
  `COLNO` int(11) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `BIOSPECIMEN_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_cell_box_idx` (`BOX_ID`) USING BTREE,
  KEY `fk_inv_cell_biospecimen_idx` (`BIOSPECIMEN_ID`) USING BTREE,
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_col_row_type`
--

DROP TABLE IF EXISTS `inv_col_row_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_col_row_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_freezer`
--

DROP TABLE IF EXISTS `inv_freezer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_freezer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `LOCATION` text,
  `STATUS` varchar(50) DEFAULT NULL,
  `SITE_ID` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `LASTSERVICENOTE` text,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DECOMMISSIONDATE` datetime DEFAULT NULL,
  `COMMISSIONDATE` datetime DEFAULT NULL,
  `LASTSERVICEDATE` datetime DEFAULT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_freezer_site` (`SITE_ID`),
  CONSTRAINT `fk_inv_freezer_site` FOREIGN KEY (`SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DESCRIPTION` text,
  `CAPACITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_freezer_tray_idx` (`FREEZER_ID`) USING BTREE,
  CONSTRAINT `FK_inv_rack_inv_freezer` FOREIGN KEY (`FREEZER_ID`) REFERENCES `inv_freezer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_site`
--

DROP TABLE IF EXISTS `inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `CONTACT` varchar(50) DEFAULT NULL,
  `ADDRESS` text,
  `NAME` varchar(50) NOT NULL,
  `PHONE` varchar(50) DEFAULT NULL,
  `LDAP_GROUP` varchar(50) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INV_SITE_STUDY` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `FK_INV_SITE_STUDY` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_type`
--

DROP TABLE IF EXISTS `inv_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `listofvalues`
--

DROP TABLE IF EXISTS `listofvalues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `listofvalues` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `TYPE` varchar(100) DEFAULT NULL,
  `VALUE` varchar(100) DEFAULT NULL,
  `SORTORDER` int(11) DEFAULT NULL,
  `GROUP_ID` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `PARENTTYPE` text,
  `PARENTVALUE` text,
  `ISEDITABLE` int(11) NOT NULL,
  `LANGUAGE` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `listofvalues_description`
--

DROP TABLE IF EXISTS `listofvalues_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `listofvalues_description` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `TYPE` varchar(255) NOT NULL,
  `DESCRIPTION` text NOT NULL,
  `DESC_ID` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `ELEMENT_ID` int(11) NOT NULL,
  `TYPE` varchar(50) DEFAULT NULL,
  `FILENAME` varchar(50) DEFAULT NULL,
  `DOMAIN` varchar(50) DEFAULT NULL,
  `DESCRIPTION` text,
  `DATE` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samplecode`
--

DROP TABLE IF EXISTS `samplecode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplecode` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `SAMPLETYPE` varchar(100) NOT NULL,
  `SAMPLESUBTYPE` varchar(50) DEFAULT NULL,
  `SAMPLETYPE_ID` int(11) DEFAULT NULL,
  `CODE` varchar(4) DEFAULT NULL,
  `ORDER` int(11) DEFAULT NULL,
  `CHILDCODE` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `fk_study_inv_site_study` (`STUDY_ID`),
  KEY `fk_study_inv_site_inv_site` (`INV_SITE_ID`),
  CONSTRAINT `fk_study_inv_site_inv_site` FOREIGN KEY (`INV_SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE reporting;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: reporting
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `FK_REPORTTEMPLATE_ARKFUNCTION` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53
/* Reference/lookup data */

USE study;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: study
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Dumping data for table `action_type`
--

LOCK TABLES `action_type` WRITE;
/*!40000 ALTER TABLE `action_type` DISABLE KEYS */;
INSERT INTO `action_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'CREATED',NULL),(2,'UPDATED',NULL),(3,'DELETED',NULL),(4,'ARCHIVED',NULL);
/*!40000 ALTER TABLE `action_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `address_status`
--

LOCK TABLES `address_status` WRITE;
/*!40000 ALTER TABLE `address_status` DISABLE KEYS */;
INSERT INTO `address_status` (`ID`, `NAME`) VALUES (1,'Current'),(2,'Current - Alternative'),(3,'Current - Under Investigation'),(5,'Incorrect address'),(4,'Valid past address');
/*!40000 ALTER TABLE `address_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `address_type`
--

LOCK TABLES `address_type` WRITE;
/*!40000 ALTER TABLE `address_type` DISABLE KEYS */;
INSERT INTO `address_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'RESIDENTIAL',NULL),(2,'WORK',NULL),(3,'POSTAL',NULL);
/*!40000 ALTER TABLE `address_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_function`
--

LOCK TABLES `ark_function` WRITE;
/*!40000 ALTER TABLE `ark_function` DISABLE KEYS */;
INSERT INTO `ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (1,'STUDY','Study Management usecase. This is represented via the Study Detail Tab under the main Study  Tab. ',1,'tab.module.study.details'),(2,'STUDY_COMPONENT','Study Component usecase. This is represented via the StudyComponent Tab under the main Study  Tab. ',1,'tab.module.study.components'),(3,'MY_DETAIL','Edit My details usecase, this is represented via My Detail tab.',1,'tab.module.mydetails'),(4,'USER','User management usecase. This is represented via the User Tab under the main Study  Tab.',1,'tab.module.user.management'),(5,'SUBJECT','Subject management usecase. This is represented via the Subject Tab under the main Study  Tab.',1,'tab.module.subject.detail'),(6,'PHONE','Manage phone usecase. This is represented via the Phone tab under the main Study  Tab.',1,'tab.module.person.phone'),(7,'ADDRESS','Manage Address',1,'tab.module.person.address'),(8,'ATTACHMENT','Manage Consent and Component attachments. This is represented via the Attachment tab under Subject Main tab.',1,'tab.module.subject.subjectFile'),(9,'CONSENT','Manage Subject Consents. This is represented via the Consent tab under the main Study  Tab.',1,'tab.module.subject.consent'),(10,'SUBJECT_UPLOAD','Bulk upload of Subjects.',1,'tab.module.subject.subjectUpload'),(11,'SUBJECT_CUSTOM_FIELD','Manage Custom Fields for Subjects.',1,'tab.module.subject.subjectcustomfield'),(12,'DATA_DICTIONARY','Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.field'),(13,'DATA_DICTIONARY_UPLOAD','Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldUpload'),(14,'PHENO_COLLECTION','Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.collection'),(15,'FIELD_DATA','Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldData'),(16,'FIELD_DATA_UPLOAD','Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.phenoUpload'),(17,'LIMS_SUBJECT','LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.',1,'tab.module.lims.subject.detail'),(18,'LIMS_COLLECTION','LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.',1,'tab.module.lims.collection'),(19,'BIOSPECIMEN','LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.',1,'tab.module.lims.biospecimen'),(20,'INVENTORY','LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.',1,'tab.module.lims.inventory'),(21,'CORRESPONDENCE','',1,'tab.module.subject.correspondence'),(22,'SUMMARY','Phenotypic Summary.',1,'tab.module.phenotypic.summary'),(23,'REPORT_STUDYSUMARY','Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>',2,NULL),(24,'REPORT_STUDYLEVELCONSENT','Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.',2,NULL),(25,'REPORT_STUDYCOMPCONSENT','Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.',2,NULL),(26,'REPORT_PHENOFIELDDETAILS','Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic collection.',2,NULL),(27,'GENO_COLLECTION','Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu',1,'tab.module.geno.collection'),(28,'ROLE_POLICY_TEMPLATE','Allows CRUD operations on the ark_role_policy_template table for the Ark application',1,'tab.module.admin.rolePolicyTemplate'),(29,'MODULE','Allows CRUD operations on the ark_module table for the Ark application',1,'tab.module.admin.module'),(30,'FUNCTION','Allows CRUD operations on the ark_function table for the Ark application',1,'tab.module.admin.function'),(33,'REPORT_STUDY_USER_ROLE_PERMISSIONS','Study User Role Permissions Report lists all user role and permissions for the study in context.',2,NULL),(34,'SUBJECT_CUSTOM_DATA','Data entry for Subject Custom Fields.',1,'tab.module.subject.subjectcustomdata'),(35,'LIMS_COLLECTION_CUSTOM_FIELD','Manage Custom Fields for LIMS collections.',1,'tab.module.lims.collectioncustomfield'),(36,'LIMS_COLLECTION_CUSTOM_DATA','Data entry for LIMS collection Custom Fields.',1,'tab.module.lims.collectioncustomdata'),(37,'BIOSPECIMEN_CUSTOM_FIELD','Manage Custom Fields for Biospecimens.',1,'tab.module.lims.biospecimencustomfield'),(38,'BIOSPECIMEN_CUSTOM_DATA','Data entry for Biospecimen Custom Fields.',1,'tab.module.lims.biospecimencustomdata'),(41,'BIOSPECIMENUID_TEMPLATE','Manage BiospecimenUid templates for the study,',1,'tab.module.lims.biospecimenuidtemplate'),(42,'BARCODE_LABEL','Manage barcode label definitions the study,',1,'tab.module.lims.barcodelabel'),(43,'BARCODE_PRINTER','Manage barcode printers for the study,',1,'tab.module.lims.barcodeprinter'),(44,'MODULE_FUNCTION','Allows CRUD operations on the ark_module_function table for the Ark application',1,'tab.module.admin.modulefunction'),(45,'ROLE','Allows CRUD operations on user roles',1,'tab.module.admin.role'),(46,'MODULE_ROLE','Allows CRUD operations on module_role table',1,'tab.module.admin.modulerole'),(47,'BIOSPECIMEN_UPLOAD','Uploader for bispecimens',1,'tab.module.lims.biospecimenUpload');
/*!40000 ALTER TABLE `ark_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_function_type`
--

LOCK TABLES `ark_function_type` WRITE;
/*!40000 ALTER TABLE `ark_function_type` DISABLE KEYS */;
INSERT INTO `ark_function_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'NON-REPORT','A function that is not a report.'),(2,'REPORT',' A function that is a report.');
/*!40000 ALTER TABLE `ark_function_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module`
--

LOCK TABLES `ark_module` WRITE;
/*!40000 ALTER TABLE `ark_module` DISABLE KEYS */;
INSERT INTO `ark_module` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Study',NULL),(2,'Subject',NULL),(3,'Phenotypic',NULL),(4,'Genotypic',NULL),(5,'LIMS',NULL),(6,'Reporting',NULL),(7,'Admin',NULL);
/*!40000 ALTER TABLE `ark_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module_function`
--

LOCK TABLES `ark_module_function` WRITE;
/*!40000 ALTER TABLE `ark_module_function` DISABLE KEYS */;
INSERT INTO `ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (1,1,1,1),(2,1,2,2),(4,1,4,3),(5,2,5,1),(6,2,6,3),(7,2,7,4),(8,2,8,5),(9,2,9,6),(10,2,10,8),(11,2,11,9),(12,2,21,7),(13,3,12,2),(14,3,13,3),(15,3,14,4),(16,3,15,5),(23,1,23,NULL),(24,2,24,NULL),(25,2,25,NULL),(26,3,26,NULL),(27,4,27,1),(31,2,34,2),(88,7,29,1),(89,7,30,2),(90,7,44,3),(91,7,45,4),(92,7,46,5),(93,7,28,6),(109,5,17,1),(110,5,19,2),(111,5,20,3),(112,5,47,4),(113,5,35,5),(114,5,37,6),(115,5,43,7),(116,5,42,8);
/*!40000 ALTER TABLE `ark_module_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module_role`
--

LOCK TABLES `ark_module_role` WRITE;
/*!40000 ALTER TABLE `ark_module_role` DISABLE KEYS */;
INSERT INTO `ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES (3,2,4),(4,2,5),(5,2,6),(6,3,7),(7,3,8),(8,5,9),(9,5,10),(10,4,11),(11,5,12),(12,3,13),(20,1,2),(21,1,3);
/*!40000 ALTER TABLE `ark_module_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_permission`
--

LOCK TABLES `ark_permission` WRITE;
/*!40000 ALTER TABLE `ark_permission` DISABLE KEYS */;
INSERT INTO `ark_permission` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'CREATE',NULL),(2,'READ',NULL),(3,'UPDATE',NULL),(4,'DELETE',NULL);
/*!40000 ALTER TABLE `ark_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_role`
--

LOCK TABLES `ark_role` WRITE;
/*!40000 ALTER TABLE `ark_role` DISABLE KEYS */;
INSERT INTO `ark_role` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Super Administrator','Highest level user of the ARK system'),(2,'Study Administrator',NULL),(3,'Study Read-Only user',NULL),(4,'Subject Administrator',NULL),(5,'Subject Data Manager',NULL),(6,'Subject Read-Only user',NULL),(7,'Pheno Read-Only user',NULL),(8,'Pheno Data Manager',NULL),(9,'LIMS Read-Only user',NULL),(10,'LIMS Data Manager',NULL),(11,'Geno Read-Only User',NULL),(12,'LIMS Administrator',NULL),(13,'Pheno Administrator',NULL),(14,'New Role',NULL);
/*!40000 ALTER TABLE `ark_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_role_policy_template`
--

LOCK TABLES `ark_role_policy_template` WRITE;
/*!40000 ALTER TABLE `ark_role_policy_template` DISABLE KEYS */;
INSERT INTO `ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (1,1,NULL,NULL,1),(2,1,NULL,NULL,2),(3,1,NULL,NULL,3),(4,1,NULL,NULL,4),(7,2,1,2,1),(8,2,1,2,2),(9,2,1,2,3),(10,2,1,3,2),(11,2,1,3,3),(12,2,1,4,1),(13,2,1,4,2),(14,2,1,4,3),(15,3,1,1,2),(16,3,1,2,2),(20,4,2,5,1),(21,4,2,5,2),(22,4,2,5,3),(23,4,2,6,1),(24,4,2,6,2),(25,4,2,6,3),(26,4,2,6,4),(27,4,2,7,1),(28,4,2,7,2),(29,4,2,7,3),(30,4,2,7,4),(31,4,2,8,1),(32,4,2,8,2),(33,4,2,8,3),(34,4,2,8,4),(35,4,2,9,1),(36,4,2,9,2),(37,4,2,9,3),(38,4,2,9,4),(39,5,2,5,2),(40,5,2,6,2),(41,5,2,6,3),(42,5,2,7,2),(43,5,2,7,3),(44,5,2,8,2),(45,5,2,8,3),(46,5,2,9,2),(47,5,2,9,3),(48,4,2,10,1),(49,4,2,10,2),(50,4,2,10,3),(51,4,2,11,1),(52,4,2,11,2),(53,4,2,11,3),(54,5,2,5,1),(55,5,2,5,3),(56,5,2,6,1),(57,5,2,6,4),(58,5,2,7,1),(59,5,2,7,4),(60,5,2,8,1),(61,5,2,8,4),(62,5,2,9,1),(63,5,2,9,4),(64,6,2,5,2),(65,6,2,6,2),(66,6,2,7,2),(67,6,2,8,2),(68,6,2,9,2),(69,6,2,34,2),(70,8,3,12,2),(71,8,3,13,2),(74,8,3,16,1),(75,10,5,17,3),(76,10,5,18,3),(77,10,5,19,3),(78,10,5,20,3),(79,9,5,17,2),(80,9,5,18,2),(81,9,5,19,2),(82,9,5,20,2),(83,7,3,12,2),(84,7,3,13,2),(85,7,3,14,2),(86,7,3,15,2),(87,7,3,16,2),(88,5,2,10,1),(89,8,3,22,2),(91,10,5,17,4),(92,2,1,23,2),(93,3,1,23,2),(94,4,2,24,2),(95,5,2,24,2),(96,6,2,24,2),(98,5,2,25,2),(99,6,2,25,2),(100,7,3,26,2),(101,8,3,26,2),(102,10,5,17,4),(103,10,5,19,4),(104,11,4,27,2),(106,8,3,14,2),(107,6,2,21,2),(108,5,2,21,1),(109,5,2,21,2),(110,5,2,21,3),(111,5,2,21,4),(112,4,2,21,1),(113,4,2,21,2),(114,4,2,21,3),(115,4,2,21,4),(116,12,5,17,1),(117,12,5,17,2),(118,12,5,17,3),(119,12,5,17,4),(120,12,5,18,1),(121,12,5,18,2),(122,12,5,18,3),(123,12,5,18,4),(124,12,5,19,1),(125,12,5,19,2),(126,12,5,19,3),(127,12,5,19,4),(128,12,5,20,1),(129,12,5,20,2),(130,12,5,20,3),(131,12,5,20,4),(133,13,3,13,1),(136,13,3,16,1),(138,13,3,26,2),(142,1,7,28,1),(143,1,7,28,2),(144,1,7,28,3),(145,1,7,28,4),(146,13,3,22,2),(147,7,3,22,2),(148,13,3,12,1),(149,13,3,12,2),(184,4,2,34,1),(185,4,2,34,2),(186,4,2,34,3),(187,4,2,34,4),(188,5,2,34,1),(189,5,2,34,2),(190,5,2,34,3),(191,5,2,34,4),(192,12,5,35,1),(193,12,5,35,2),(194,12,5,35,3),(195,12,5,36,1),(196,12,5,36,2),(197,12,5,36,3),(198,12,5,36,4),(199,10,5,36,1),(200,10,5,36,2),(201,10,5,36,3),(202,10,5,36,4),(203,9,5,36,2),(204,12,5,37,1),(205,12,5,37,2),(206,12,5,37,3),(207,12,5,38,1),(208,12,5,38,2),(209,12,5,38,3),(210,12,5,38,4),(211,10,5,38,1),(212,10,5,38,2),(213,10,5,38,3),(214,10,5,38,4),(215,9,5,38,2),(228,8,3,15,1),(229,8,3,15,2),(230,8,3,15,3),(233,3,1,3,2),(246,14,1,23,2),(247,14,1,1,2),(248,14,1,2,2),(249,14,1,4,2),(250,2,1,23,2),(252,2,1,2,2),(253,2,1,4,2),(254,3,1,23,2),(255,3,1,1,2),(256,3,1,2,2),(257,3,1,4,2),(258,4,1,23,2),(259,4,1,1,2),(260,4,1,2,2),(261,4,1,4,2),(262,2,1,33,2),(263,13,3,15,1),(264,13,3,15,2),(265,13,3,15,3),(266,13,3,15,4),(267,13,3,14,1),(268,13,3,14,2),(269,13,3,14,3),(270,13,3,14,4),(279,10,5,47,1),(280,10,5,47,2),(281,10,5,47,3),(282,10,5,47,4),(283,12,5,47,1),(284,12,5,47,2),(285,12,5,47,3),(286,12,5,47,4),(287,2,1,1,1),(288,2,1,1,2),(289,2,1,1,3);
/*!40000 ALTER TABLE `ark_role_policy_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `consent_answer`
--

LOCK TABLES `consent_answer` WRITE;
/*!40000 ALTER TABLE `consent_answer` DISABLE KEYS */;
INSERT INTO `consent_answer` (`ID`, `NAME`) VALUES (1,'YES'),(2,'NO');
/*!40000 ALTER TABLE `consent_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `consent_status`
--

LOCK TABLES `consent_status` WRITE;
/*!40000 ALTER TABLE `consent_status` DISABLE KEYS */;
INSERT INTO `consent_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Consented',NULL),(2,'Not Consented',NULL),(3,'Ineligible',NULL),(4,'Refused',NULL),(5,'Withdrawn',NULL),(6,'Pending',NULL);
/*!40000 ALTER TABLE `consent_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `consent_type`
--

LOCK TABLES `consent_type` WRITE;
/*!40000 ALTER TABLE `consent_type` DISABLE KEYS */;
INSERT INTO `consent_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Hard Copy','Physical Paper based document.'),(2,'Electronic','A scanned equivalent of a hard copy that is available as a download via an application.');
/*!40000 ALTER TABLE `consent_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `correspondence_direction_type`
--

LOCK TABLES `correspondence_direction_type` WRITE;
/*!40000 ALTER TABLE `correspondence_direction_type` DISABLE KEYS */;
INSERT INTO `correspondence_direction_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Incoming',NULL),(2,'Outgoing',NULL);
/*!40000 ALTER TABLE `correspondence_direction_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `correspondence_mode_type`
--

LOCK TABLES `correspondence_mode_type` WRITE;
/*!40000 ALTER TABLE `correspondence_mode_type` DISABLE KEYS */;
INSERT INTO `correspondence_mode_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Mail',NULL),(2,'Fax',NULL),(3,'Email',NULL),(4,'Telephone',NULL),(5,'Face to face',NULL),(6,'Not applicable',NULL);
/*!40000 ALTER TABLE `correspondence_mode_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `correspondence_outcome_type`
--

LOCK TABLES `correspondence_outcome_type` WRITE;
/*!40000 ALTER TABLE `correspondence_outcome_type` DISABLE KEYS */;
INSERT INTO `correspondence_outcome_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Sent',NULL),(2,'Received',NULL),(3,'Return to sender',NULL),(4,'Engaged',NULL),(5,'No answer',NULL),(6,'Contact made',NULL),(7,'Message given to person',NULL),(8,'Not applicable',NULL);
/*!40000 ALTER TABLE `correspondence_outcome_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `correspondence_status_type`
--

LOCK TABLES `correspondence_status_type` WRITE;
/*!40000 ALTER TABLE `correspondence_status_type` DISABLE KEYS */;
INSERT INTO `correspondence_status_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'On call',NULL),(2,'Archived',NULL);
/*!40000 ALTER TABLE `correspondence_status_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` (`ID`, `NAME`, `DESCRIPTION`, `COUNTRY_CODE`) VALUES (1,'AUSTRALIA',NULL,'AU'),(2,'UNITED KINGDOM',NULL,NULL),(3,'CANADA',NULL,NULL),(4,'AFGHANISTAN',NULL,'AF'),(5,'ALAND ISLANDS',NULL,'AX'),(6,'ALBANIA',NULL,'AL'),(7,'ALGERIA',NULL,'DZ'),(8,'AMERICAN SAMOA',NULL,'AS'),(9,'ANDORRA',NULL,'AD'),(10,'ANGOLA',NULL,'AO'),(11,'ANGUILLA',NULL,'AI'),(12,'ANTARCTICA',NULL,'AQ'),(13,'ANTIGUA AND BARBUDA',NULL,'AG'),(14,'ARGENTINA',NULL,'AR'),(15,'ARMENIA',NULL,'AM'),(16,'ARUBA',NULL,'AW'),(18,'AUSTRIA',NULL,'AT'),(19,'AZERBAIJAN',NULL,'AZ'),(20,'BAHAMAS',NULL,'BS'),(21,'BAHRAIN',NULL,'BH'),(22,'BANGLADESH',NULL,'BD'),(23,'BARBADOS',NULL,'BB'),(24,'BELARUS',NULL,'BY'),(25,'BELGIUM',NULL,'BE'),(26,'BELIZE',NULL,'BZ'),(27,'BENIN',NULL,'BJ'),(28,'BERMUDA',NULL,'BM'),(29,'BHUTAN',NULL,'BT'),(30,'BOLIVIA, PLURINATIONAL STATE OF',NULL,'BO'),(31,'BONAIRE, SAINT EUSTATIUS AND SABA',NULL,'BQ'),(32,'BOSNIA AND HERZEGOVINA',NULL,'BA'),(33,'BOTSWANA',NULL,'BW'),(34,'BOUVET ISLAND',NULL,'BV'),(35,'BRAZIL',NULL,'BR'),(36,'BRITISH INDIAN OCEAN TERRITORY',NULL,'IO'),(37,'BRUNEI DARUSSALAM',NULL,'BN'),(38,'BULGARIA',NULL,'BG'),(39,'BURKINA FASO',NULL,'BF'),(40,'BURUNDI',NULL,'BI'),(41,'CAMBODIA',NULL,'KH'),(42,'CAMEROON',NULL,'CM'),(44,'CAPE VERDE',NULL,'CV'),(45,'CAYMAN ISLANDS',NULL,'KY'),(46,'CENTRAL AFRICAN REPUBLIC',NULL,'CF'),(47,'CHAD',NULL,'TD'),(48,'CHILE',NULL,'CL'),(49,'CHINA',NULL,'CN'),(50,'CHRISTMAS ISLAND',NULL,'CX'),(51,'COCOS (KEELING) ISLANDS',NULL,'CC'),(52,'COLOMBIA',NULL,'CO'),(53,'COMOROS',NULL,'KM'),(54,'CONGO',NULL,'CG'),(55,'CONGO, THE DEMOCRATIC REPUBLIC OF THE',NULL,'CD'),(56,'COOK ISLANDS',NULL,'CK'),(57,'COSTA RICA',NULL,'CR'),(58,'COTE D\'\'IVOIRE',NULL,'CI'),(59,'CROATIA',NULL,'HR'),(60,'CUBA',NULL,'CU'),(61,'CURACAO',NULL,'CW'),(62,'CYPRUS',NULL,'CY'),(63,'CZECH REPUBLIC',NULL,'CZ'),(64,'DENMARK',NULL,'DK'),(65,'DJIBOUTI',NULL,'DJ'),(66,'DOMINICA',NULL,'DM'),(67,'DOMINICAN REPUBLIC',NULL,'DO'),(68,'ECUADOR',NULL,'EC'),(69,'EGYPT',NULL,'EG'),(70,'EL SALVADOR',NULL,'SV'),(71,'EQUATORIAL GUINEA',NULL,'GQ'),(72,'ERITREA',NULL,'ER'),(73,'ESTONIA',NULL,'EE'),(74,'ETHIOPIA',NULL,'ET'),(75,'FALKLAND ISLANDS (MALVINAS)',NULL,'FK'),(76,'FAROE ISLANDS',NULL,'FO'),(77,'FIJI',NULL,'FJ'),(78,'FINLAND',NULL,'FI'),(79,'FRANCE',NULL,'FR'),(80,'FRENCH GUIANA',NULL,'GF'),(81,'FRENCH POLYNESIA',NULL,'PF'),(82,'FRENCH SOUTHERN TERRITORIES',NULL,'TF'),(83,'GABON',NULL,'GA'),(84,'GAMBIA',NULL,'GM'),(85,'GEORGIA',NULL,'GE'),(86,'GERMANY',NULL,'DE'),(87,'GHANA',NULL,'GH'),(88,'GIBRALTAR',NULL,'GI'),(89,'GREECE',NULL,'GR'),(90,'GREENLAND',NULL,'GL'),(91,'GRENADA',NULL,'GD'),(92,'GUADELOUPE',NULL,'GP'),(93,'GUAM',NULL,'GU'),(94,'GUATEMALA',NULL,'GT'),(95,'GUERNSEY',NULL,'GG'),(96,'GUINEA',NULL,'GN'),(97,'GUINEA-BISSAU',NULL,'GW'),(98,'GUYANA',NULL,'GY'),(99,'HAITI',NULL,'HT'),(100,'HEARD ISLAND AND MCDONALD ISLANDS',NULL,'HM'),(101,'HOLY SEE (VATICAN CITY STATE)',NULL,'VA'),(102,'HONDURAS',NULL,'HN'),(103,'HONG KONG',NULL,'HK'),(104,'HUNGARY',NULL,'HU'),(105,'ICELAND',NULL,'IS'),(106,'INDIA',NULL,'IN'),(107,'INDONESIA',NULL,'ID'),(108,'IRAN, ISLAMIC REPUBLIC OF',NULL,'IR'),(109,'IRAQ',NULL,'IQ'),(110,'IRELAND',NULL,'IE'),(111,'ISLE OF MAN',NULL,'IM'),(112,'ISRAEL',NULL,'IL'),(113,'ITALY',NULL,'IT'),(114,'JAMAICA',NULL,'JM'),(115,'JAPAN',NULL,'JP'),(116,'JERSEY',NULL,'JE'),(117,'JORDAN',NULL,'JO'),(118,'KAZAKHSTAN',NULL,'KZ'),(119,'KENYA',NULL,'KE'),(120,'KIRIBATI',NULL,'KI'),(121,'KOREA, DEMOCRATIC PEOPLE\'S REPUBLIC OF',NULL,'KP'),(122,'KOREA, REPUBLIC OF',NULL,'KR'),(123,'KUWAIT',NULL,'KW'),(124,'KYRGYZSTAN',NULL,'KG'),(125,'LAO PEOPLE\'S DEMOCRATIC REPUBLIC',NULL,'LA'),(126,'LATVIA',NULL,'LV'),(127,'LEBANON',NULL,'LB'),(128,'LESOTHO',NULL,'LS'),(129,'LIBERIA',NULL,'LR'),(130,'LIBYAN ARAB JAMAHIRIYA',NULL,'LY'),(131,'LIECHTENSTEIN',NULL,'LI'),(132,'LITHUANIA',NULL,'LT'),(133,'LUXEMBOURG',NULL,'LU'),(134,'MACAO',NULL,'MO'),(135,'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF',NULL,'MK'),(136,'MADAGASCAR',NULL,'MG'),(137,'MALAWI',NULL,'MW'),(138,'MALAYSIA',NULL,'MY'),(139,'MALDIVES',NULL,'MV'),(140,'MALI',NULL,'ML'),(141,'MALTA',NULL,'MT'),(142,'MARSHALL ISLANDS',NULL,'MH'),(143,'MARTINIQUE',NULL,'MQ'),(144,'MAURITANIA',NULL,'MR'),(145,'MAURITIUS',NULL,'MU'),(146,'MAYOTTE',NULL,'YT'),(147,'MEXICO',NULL,'MX'),(148,'MICRONESIA, FEDERATED STATES OF',NULL,'FM'),(149,'MOLDOVA, REPUBLIC OF',NULL,'MD'),(150,'MONACO',NULL,'MC'),(151,'MONGOLIA',NULL,'MN'),(152,'MONTENEGRO',NULL,'ME'),(153,'MONTSERRAT',NULL,'MS'),(154,'MOROCCO',NULL,'MA'),(155,'MOZAMBIQUE',NULL,'MZ'),(156,'MYANMAR',NULL,'MM'),(157,'NAMIBIA',NULL,'NA'),(158,'NAURU',NULL,'NR'),(159,'NEPAL',NULL,'NP'),(160,'NETHERLANDS',NULL,'NL'),(161,'NEW CALEDONIA',NULL,'NC'),(162,'NEW ZEALAND',NULL,'NZ'),(163,'NICARAGUA',NULL,'NI'),(164,'NIGER',NULL,'NE'),(165,'NIGERIA',NULL,'NG'),(166,'NIUE',NULL,'NU'),(167,'NORFOLK ISLAND',NULL,'NF'),(168,'NORTHERN MARIANA ISLANDS',NULL,'MP'),(169,'NORWAY',NULL,'NO'),(170,'OMAN',NULL,'OM'),(171,'PAKISTAN',NULL,'PK'),(172,'PALAU',NULL,'PW'),(173,'PALESTINIAN TERRITORY, OCCUPIED',NULL,'PS'),(174,'PANAMA',NULL,'PA'),(175,'PAPUA NEW GUINEA',NULL,'PG'),(176,'PARAGUAY',NULL,'PY'),(177,'PERU',NULL,'PE'),(178,'PHILIPPINES',NULL,'PH'),(179,'PITCAIRN',NULL,'PN'),(180,'POLAND',NULL,'PL'),(181,'PORTUGAL',NULL,'PT'),(182,'PUERTO RICO',NULL,'PR'),(183,'QATAR',NULL,'QA'),(184,'REUNION',NULL,'RE'),(185,'ROMANIA',NULL,'RO'),(186,'RUSSIAN FEDERATION',NULL,'RU'),(187,'RWANDA',NULL,'RW'),(188,'SAINT BARTHELEMY',NULL,'BL'),(189,'SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA',NULL,'SH'),(190,'SAINT KITTS AND NEVIS',NULL,'KN'),(191,'SAINT LUCIA',NULL,'LC'),(192,'SAINT MARTIN (FRENCH PART)',NULL,'MF'),(193,'SAINT PIERRE AND MIQUELON',NULL,'PM'),(194,'SAINT VINCENT AND THE GRENADINES',NULL,'VC'),(195,'SAMOA',NULL,'WS'),(196,'SAN MARINO',NULL,'SM'),(197,'SAO TOME AND PRINCIPE',NULL,'ST'),(198,'SAUDI ARABIA',NULL,'SA'),(199,'SENEGAL',NULL,'SN'),(200,'SERBIA',NULL,'RS'),(201,'SEYCHELLES',NULL,'SC'),(202,'SIERRA LEONE',NULL,'SL'),(203,'SINGAPORE',NULL,'SG'),(204,'SINT MAARTEN (DUTCH PART)',NULL,'SX'),(205,'SLOVAKIA',NULL,'SK'),(206,'SLOVENIA',NULL,'SI'),(207,'SOLOMON ISLANDS',NULL,'SB'),(208,'SOMALIA',NULL,'SO'),(209,'SOUTH AFRICA',NULL,'ZA'),(210,'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS',NULL,'GS'),(211,'SPAIN',NULL,'ES'),(212,'SRI LANKA',NULL,'LK'),(213,'SUDAN',NULL,'SD'),(214,'SURINAME',NULL,'SR'),(215,'SVALBARD AND JAN MAYEN',NULL,'SJ'),(216,'SWAZILAND',NULL,'SZ'),(217,'SWEDEN',NULL,'SE'),(218,'SWITZERLAND',NULL,'CH'),(219,'SYRIAN ARAB REPUBLIC',NULL,'SY'),(220,'TAIWAN, PROVINCE OF CHINA',NULL,'TW'),(221,'TAJIKISTAN',NULL,'TJ'),(222,'TANZANIA, UNITED REPUBLIC OF',NULL,'TZ'),(223,'THAILAND',NULL,'TH'),(224,'TIMOR-LESTE',NULL,'TL'),(225,'TOGO',NULL,'TG'),(226,'TOKELAU',NULL,'TK'),(227,'TONGA',NULL,'TO'),(228,'TRINIDAD AND TOBAGO',NULL,'TT'),(229,'TUNISIA',NULL,'TN'),(230,'TURKEY',NULL,'TR'),(231,'TURKMENISTAN',NULL,'TM'),(232,'TURKS AND CAICOS ISLANDS',NULL,'TC'),(233,'TUVALU',NULL,'TV'),(234,'UGANDA',NULL,'UG'),(235,'UKRAINE',NULL,'UA'),(236,'UNITED ARAB EMIRATES',NULL,'AE'),(238,'UNITED STATES',NULL,'US'),(239,'UNITED STATES MINOR OUTLYING ISLANDS',NULL,'UM'),(240,'URUGUAY',NULL,'UY'),(241,'UZBEKISTAN',NULL,'UZ'),(242,'VANUATU',NULL,'VU'),(243,'VATICAN CITY STATE',NULL,'VA'),(244,'VENEZUELA, BOLIVARIAN REPUBLIC OF',NULL,'VE'),(245,'VIET NAM',NULL,'VN'),(246,'VIRGIN ISLANDS, BRITISH',NULL,'VG'),(247,'VIRGIN ISLANDS, U.S.',NULL,'VI'),(248,'WALLIS AND FUTUNA',NULL,'WF'),(249,'WESTERN SAHARA',NULL,'EH'),(250,'YEMEN',NULL,'YE'),(251,'ZAMBIA',NULL,'ZM'),(252,'ZIMBABWE',NULL,'ZW');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `country_state`
--

LOCK TABLES `country_state` WRITE;
/*!40000 ALTER TABLE `country_state` DISABLE KEYS */;
INSERT INTO `country_state` (`ID`, `COUNTRY_ID`, `STATE`) VALUES (1,1,'WA'),(2,1,'NSW'),(3,1,'VIC'),(4,1,'ACT'),(5,1,'NT'),(6,1,'QLD'),(7,3,'Alberta'),(8,2,'Bedfordshire'),(9,2,'Berkshire'),(10,1,'TAS'),(11,1,'SA');
/*!40000 ALTER TABLE `country_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `delimiter_type`
--

LOCK TABLES `delimiter_type` WRITE;
/*!40000 ALTER TABLE `delimiter_type` DISABLE KEYS */;
INSERT INTO `delimiter_type` (`ID`, `NAME`, `DESCRIPTION`, `DELIMITER_CHARACTER`) VALUES (1,'COMMA','Comma',','),(2,'TAB','Tab character','	'),(3,'PIPE','Pipe character','|'),(4,'COLON','Colon character',':'),(5,'AT SYMBOL','At characer','@');
/*!40000 ALTER TABLE `delimiter_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `domain_type`
--

LOCK TABLES `domain_type` WRITE;
/*!40000 ALTER TABLE `domain_type` DISABLE KEYS */;
INSERT INTO `domain_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'STUDY',NULL),(2,'STUDY COMPONENT',NULL);
/*!40000 ALTER TABLE `domain_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `email_account_type`
--

LOCK TABLES `email_account_type` WRITE;
/*!40000 ALTER TABLE `email_account_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_account_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `entity_type`
--

LOCK TABLES `entity_type` WRITE;
/*!40000 ALTER TABLE `entity_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `entity_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` (`ID`, `NAME`) VALUES (1,'CHARACTER'),(2,'NUMBER'),(3,'DATE');
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'CSV','Comma separated values'),(2,'TXT','Tab separated text file'),(3,'XLS','Excel Spreadsheet');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gender_type`
--

LOCK TABLES `gender_type` WRITE;
/*!40000 ALTER TABLE `gender_type` DISABLE KEYS */;
INSERT INTO `gender_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0,'Unknown',NULL),(1,'Male',NULL),(2,'Female',NULL);
/*!40000 ALTER TABLE `gender_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `marital_status`
--

LOCK TABLES `marital_status` WRITE;
/*!40000 ALTER TABLE `marital_status` DISABLE KEYS */;
INSERT INTO `marital_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Married',NULL),(2,'Single',NULL),(3,'Divorced',NULL),(4,'Unknown',NULL);
/*!40000 ALTER TABLE `marital_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `person_contact_method`
--

LOCK TABLES `person_contact_method` WRITE;
/*!40000 ALTER TABLE `person_contact_method` DISABLE KEYS */;
INSERT INTO `person_contact_method` (`ID`, `NAME`) VALUES (3,'Email'),(1,'Home telephone'),(2,'Mobile telephone'),(4,'Post');
/*!40000 ALTER TABLE `person_contact_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `phone_status`
--

LOCK TABLES `phone_status` WRITE;
/*!40000 ALTER TABLE `phone_status` DISABLE KEYS */;
INSERT INTO `phone_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Current',NULL),(2,'Current Alternative',NULL),(3,'Current Under Investigation',NULL),(4,'Valid Past',NULL),(5,'Incorrect or Disconnected',NULL);
/*!40000 ALTER TABLE `phone_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `phone_type`
--

LOCK TABLES `phone_type` WRITE;
/*!40000 ALTER TABLE `phone_type` DISABLE KEYS */;
INSERT INTO `phone_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Mobile','Mobile  Cell Phones'),(2,'Home','Land Home Phone'),(3,'Work','Land Phone at Office');
/*!40000 ALTER TABLE `phone_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `registration_status`
--

LOCK TABLES `registration_status` WRITE;
/*!40000 ALTER TABLE `registration_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `registration_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `relationship`
--

LOCK TABLES `relationship` WRITE;
/*!40000 ALTER TABLE `relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `study_comp_status`
--

LOCK TABLES `study_comp_status` WRITE;
/*!40000 ALTER TABLE `study_comp_status` DISABLE KEYS */;
INSERT INTO `study_comp_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Completed',NULL),(2,'Not Completed',NULL),(3,'Not Needed',NULL),(4,'Not Available',NULL),(5,'Pending',NULL),(6,'Received',NULL),(7,'Requested',NULL),(8,'Refused',NULL),(9,'Unknown',NULL);
/*!40000 ALTER TABLE `study_comp_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `study_status`
--

LOCK TABLES `study_status` WRITE;
/*!40000 ALTER TABLE `study_status` DISABLE KEYS */;
INSERT INTO `study_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Active',NULL),(2,'Discussion',NULL),(3,'EOI',NULL),(4,'Full Application',NULL),(5,'Ethics',NULL),(6,'Dispute Recorded',NULL),(7,'Approved',NULL),(8,'Active-Recruiting',NULL),(9,'Active-Ongoing Programme',NULL),(10,'Active-Data Analysis',NULL),(11,'Active-Writing Up',NULL),(12,'Unsuccessful Funding',NULL),(13,'EOI-Rejected',NULL),(14,'EOI-Abandoned',NULL),(15,'Archive',NULL);
/*!40000 ALTER TABLE `study_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `subject_status`
--

LOCK TABLES `subject_status` WRITE;
/*!40000 ALTER TABLE `subject_status` DISABLE KEYS */;
INSERT INTO `subject_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Subject',NULL),(2,'Prospect',NULL),(3,'Withdrawn Subject',NULL),(4,'Archive',NULL);
/*!40000 ALTER TABLE `subject_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `subjectuid_padchar`
--

LOCK TABLES `subjectuid_padchar` WRITE;
/*!40000 ALTER TABLE `subjectuid_padchar` DISABLE KEYS */;
INSERT INTO `subjectuid_padchar` (`ID`, `NAME`) VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');
/*!40000 ALTER TABLE `subjectuid_padchar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `subjectuid_token`
--

LOCK TABLES `subjectuid_token` WRITE;
/*!40000 ALTER TABLE `subjectuid_token` DISABLE KEYS */;
INSERT INTO `subjectuid_token` (`ID`, `NAME`) VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');
/*!40000 ALTER TABLE `subjectuid_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `title_type`
--

LOCK TABLES `title_type` WRITE;
/*!40000 ALTER TABLE `title_type` DISABLE KEYS */;
INSERT INTO `title_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0,'Unknown',NULL),(1,'Br',NULL),(2,'Capt','Captain'),(3,'Col',NULL),(4,'Cpl',NULL),(5,'Dean',NULL),(6,'Dr',NULL),(7,'Fr',NULL),(8,'Lac',NULL),(9,'Major',NULL),(10,'Miss',NULL),(11,'Mr',NULL),(12,'Mrs',NULL),(13,'Ms',NULL),(14,'Past',NULL),(15,'Prof',NULL),(16,'Pstr',NULL),(17,'Rev',NULL),(18,'Sir',NULL),(19,'Sr',NULL),(20,'Lady',NULL);
/*!40000 ALTER TABLE `title_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `unit_type`
--

LOCK TABLES `unit_type` WRITE;
/*!40000 ALTER TABLE `unit_type` DISABLE KEYS */;
INSERT INTO `unit_type` (`ID`, `ARK_FUNCTION_ID`, `NAME`, `DESCRIPTION`, `MEASUREMENT_TYPE_ID`, `DISPLAY_ORDER`) VALUES (1,NULL,'mm','Millimetres (mm)',1,1),(2,NULL,'cm','Centimetres (cm)',1,2),(3,NULL,'m','Metres (m)',1,4),(4,NULL,'g','Grams (g)',4,1),(5,NULL,'kg','Kilograms (kg)',4,2),(6,NULL,'L','Litres (L)',2,1),(7,NULL,'Days','Days',3,6),(8,NULL,'Months','Months',3,7),(9,NULL,'Years','Years',3,8),(10,NULL,'hrs','Hours (hrs)',3,5),(11,NULL,'min','Minutes (min)',3,4),(12,NULL,'s','Seconds (s)',3,1),(13,18,'ug/L','ug/L',5,1),(14,18,'bpm','bpm',999,NULL),(15,18,'g/L','g/L',5,4),(16,18,'fL','fL',999,NULL),(17,18,'feet','feet',1,3),(18,18,'IU/L','IU/L',999,NULL),(19,18,'U','U',999,NULL),(20,18,'Age','Age',3,9),(21,18,'m/L','m/L',999,NULL),(22,18,'pg','pg',999,NULL),(23,18,'pred','pred',999,NULL),(24,18,'Gy','Gy',999,NULL),(25,18,'%','%',7,1),(26,18,'mS','mS',999,NULL),(27,18,'mm/hr','mm/hr',6,1),(28,18,'mg/dl','mg/dl',5,2),(29,18,'mn','mn',999,NULL),(30,18,'mg/L','mg/L',5,3),(31,18,'kgm2','kgm2',999,NULL),(32,18,'mm Hg','mm Hg',999,NULL),(33,18,'kg/m2','kg/m2',5,6),(34,18,'Pipes','Pipes',999,NULL),(35,18,'S','S',3,2),(36,18,'mm/hg','mm/hg',999,NULL),(37,19,'ug/L','ug/L',5,1),(38,19,'bpm','bpm',999,NULL),(39,19,'g/L','g/L',5,4),(40,19,'fL','fL',999,NULL),(41,19,'feet','feet',1,3),(42,19,'IU/L','IU/L',999,NULL),(43,19,'U','U',999,NULL),(44,19,'Age','Age',3,10),(45,19,'m/L','m/L',999,NULL),(46,19,'pg','pg',999,NULL),(47,19,'pred','pred',999,NULL),(48,19,'Gy','Gy',999,NULL),(49,19,'%','%',7,1),(50,19,'mS','mS',999,NULL),(51,19,'mm/hr','mm/hr',6,1),(52,19,'mg/dl','mg/dl',5,2),(53,19,'mn','mn',999,NULL),(54,19,'mg/L','mg/L',5,4),(55,19,'kgm2','kgm2',999,NULL),(56,19,'mm Hg','mm Hg',999,NULL),(57,19,'kg/m2','kg/m2',5,5),(58,19,'Pipes','Pipes',999,NULL),(59,19,'S','S',3,3),(60,19,'mm/hg','mm/hg',999,NULL);
/*!40000 ALTER TABLE `unit_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `vital_status`
--

LOCK TABLES `vital_status` WRITE;
/*!40000 ALTER TABLE `vital_status` DISABLE KEYS */;
INSERT INTO `vital_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0,'Unknown',NULL),(1,'Alive',NULL),(2,'Deceased',NULL);
/*!40000 ALTER TABLE `vital_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `yes_no`
--

LOCK TABLES `yes_no` WRITE;
/*!40000 ALTER TABLE `yes_no` DISABLE KEYS */;
INSERT INTO `yes_no` (`ID`, `NAME`) VALUES (1,'Yes'),(2,'No');
/*!40000 ALTER TABLE `yes_no` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `consent_option`
--

LOCK TABLES `consent_option` WRITE;
/*!40000 ALTER TABLE `consent_option` DISABLE KEYS */;
INSERT INTO `consent_option` (`ID`, `NAME`) VALUES (1,'Yes'),(2,'No'),(3,'Pending'),(4,'Unavailable');
/*!40000 ALTER TABLE `consent_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `measurement_type`
--

LOCK TABLES `measurement_type` WRITE;
/*!40000 ALTER TABLE `measurement_type` DISABLE KEYS */;
INSERT INTO `measurement_type` (`ID`, `VALUE`, `DESCRIPTION`) VALUES (999,'Other',NULL),(1,'Distance',NULL),(2,'Volume',NULL),(3,'Time',NULL),(4,'Weight',NULL),(5,'Weight per Volume',NULL),(6,'Distance per Time',NULL),(7,'Percentage or Fraction',NULL);
/*!40000 ALTER TABLE `measurement_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE pheno;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: pheno
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Dumping data for table `delimiter_type`
--

LOCK TABLES `delimiter_type` WRITE;
/*!40000 ALTER TABLE `delimiter_type` DISABLE KEYS */;
INSERT INTO `delimiter_type` (`ID`, `NAME`, `DESCRIPTION`, `DELIMITER_CHARACTER`) VALUES (1,'COMMA','Comma',','),(2,'TAB','Tab character','	'),(3,'PIPE','Pipe character','|'),(4,'COLON','Colon character',':'),(5,'AT SYMBOL','At character','@');
/*!40000 ALTER TABLE `delimiter_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` (`ID`, `NAME`) VALUES (1,'CHARACTER'),(2,'NUMBER'),(3,'DATE');
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'CSV','Comma separated values'),(2,'TXT','Tab separated text file'),(3,'XLS','Excel Spreadsheet');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `questionnaire_status`
--

LOCK TABLES `questionnaire_status` WRITE;
/*!40000 ALTER TABLE `questionnaire_status` DISABLE KEYS */;
INSERT INTO `questionnaire_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'In Progress','The Questionnaire is being provided with data and not yet completed.'),(2,'Data Entry Completed','Questionnaire data entry is completed and awaiting review.'),(3,'Review Ok','The Questionnaire data was reviewed successfully and questionnaire is now locked from further modification.'),(4,'Review Failed','The Questionnaire data failed review and is needs to be revisited for data correction.');
/*!40000 ALTER TABLE `questionnaire_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`ID`, `NAME`) VALUES (1,'CREATED'),(2,'ACTIVE'),(3,'DISPLAYED'),(4,'EXPIRED');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE geno;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: geno
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Dumping data for table `delimiter_type`
--

LOCK TABLES `delimiter_type` WRITE;
/*!40000 ALTER TABLE `delimiter_type` DISABLE KEYS */;
INSERT INTO `delimiter_type` (`ID`, `NAME`) VALUES (1,'Tab'),(2,'Comma');
/*!40000 ALTER TABLE `delimiter_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` (`ID`, `NAME`) VALUES (1,'PED'),(2,'MAP');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `import_type`
--

LOCK TABLES `import_type` WRITE;
/*!40000 ALTER TABLE `import_type` DISABLE KEYS */;
INSERT INTO `import_type` (`ID`, `NAME`) VALUES (1,'Raw'),(2,'Imputed');
/*!40000 ALTER TABLE `import_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `marker_type`
--

LOCK TABLES `marker_type` WRITE;
/*!40000 ALTER TABLE `marker_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `marker_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meta_data_type`
--

LOCK TABLES `meta_data_type` WRITE;
/*!40000 ALTER TABLE `meta_data_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_data_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`ID`, `NAME`) VALUES (1,'Created'),(2,'Active'),(3,'Disabled'),(4,'Expired');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE lims;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: lims
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Dumping data for table `bio_sampletype`
--

LOCK TABLES `bio_sampletype` WRITE;
/*!40000 ALTER TABLE `bio_sampletype` DISABLE KEYS */;
INSERT INTO `bio_sampletype` (`ID`, `NAME`, `SAMPLETYPE`, `SAMPLESUBTYPE`) VALUES (1,'Blood / Blood','Blood','Blood'),(2,'Blood / Buffy Coat','Blood','Buffy Coat'),(3,'Blood / Buffy Coat (ACD)','Blood','Buffy Coat (ACD)'),(4,'Blood / Buffy Coat (EDTA)','Blood','Buffy Coat (EDTA)'),(5,'Blood / Buffy Coat (LH)','Blood','Buffy Coat (LH)'),(6,'Blood / Cord blood','Blood','Cord blood'),(7,'Blood / EDTA Blood','Blood','EDTA Blood'),(8,'Blood / Frozen Lymphocytes (F)','Blood','Frozen Lymphocytes (F)'),(9,'Blood / Lithium Heparin','Blood','Lithium Heparin'),(10,'Blood / Mothers Blood','Blood','Mothers Blood'),(11,'Blood / Plasma','Blood','Plasma'),(12,'Blood / Plasma (ACD)','Blood','Plasma (ACD)'),(13,'Blood / Plasma (EDTA)','Blood','Plasma (EDTA)'),(14,'Blood / Plasma (LH)','Blood','Plasma (LH)'),(15,'Blood / Protein from TL','Blood','Protein from TL'),(16,'Blood / Red Blood Cells','Blood','Red Blood Cells'),(17,'Blood / Serum','Blood','Serum'),(18,'Blood / Transformed lymphoblasts (T)','Blood','Transformed lymphoblasts (T)'),(19,'Blood / Unprocessed','Blood','Unprocessed'),(20,'Blood / Whole Blood','Blood','Whole Blood'),(21,'Blood / Whole Blood (EDTA)','Blood','Whole Blood (EDTA)'),(22,'Blood / Whole Blood (LH)','Blood','Whole Blood (LH)'),(23,'Nucleic Acid / Buccal Swab','Nucleic Acid','Buccal Swab'),(24,'Nucleic Acid / DNA','Nucleic Acid','DNA'),(25,'Nucleic Acid / DNA from BC','Nucleic Acid','DNA from BC'),(26,'Nucleic Acid / DNA from TL','Nucleic Acid','DNA from TL'),(27,'Nucleic Acid / DNA from Tissue','Nucleic Acid','DNA from Tissue'),(28,'Nucleic Acid / Paxgene RNA','Nucleic Acid','Paxgene RNA'),(29,'Nucleic Acid / RNA','Nucleic Acid','RNA'),(30,'Nucleic Acid / Saliva','Nucleic Acid','Saliva'),(31,'Protein from TL','Protein from TL',NULL),(32,'Saliva / Buccal Swab','Saliva','Buccal Swab'),(33,'Saliva / Buccal Swab (SB)','Saliva','Buccal Swab (SB)'),(34,'Saliva / Oragene (OS)','Saliva','Oragene (OS)'),(35,'Saliva / Throat Swab','Saliva','Throat Swab'),(36,'Saliva','Saliva',NULL),(37,'Tissue / Anus','Tissue','Anus'),(38,'Tissue / Appendix','Tissue','Appendix'),(39,'Tissue / Brain','Tissue','Brain'),(40,'Tissue / Breast','Tissue','Breast'),(41,'Tissue / Breast,Lt','Tissue','Breast,Lt'),(42,'Tissue / Breast,Rt','Tissue','Breast,Rt'),(43,'Tissue / Caecum','Tissue','Caecum'),(44,'Tissue / Colon','Tissue','Colon'),(45,'Tissue / Colon, ascending','Tissue','Colon, ascending'),(46,'Tissue / Colon, descending','Tissue','Colon, descending'),(47,'Tissue / Colon, hepatic flexure','Tissue','Colon, hepatic flexure'),(48,'Tissue / Colon, nos','Tissue','Colon, nos'),(49,'Tissue / Colon, sigmoid','Tissue','Colon, sigmoid'),(50,'Tissue / Colon, spenic flexure','Tissue','Colon, spenic flexure'),(51,'Tissue / Colon, splenic flexure','Tissue','Colon, splenic flexure'),(52,'Tissue / Colon, transverse','Tissue','Colon, transverse'),(53,'Tissue / Descending Colon','Tissue','Descending Colon'),(54,'Tissue / Duodenum','Tissue','Duodenum'),(55,'Tissue / Endometrium','Tissue','Endometrium'),(56,'Tissue / Ileum','Tissue','Ileum'),(57,'Tissue / Left Tube','Tissue','Left Tube'),(58,'Tissue / Liver','Tissue','Liver'),(59,'Tissue / Lung','Tissue','Lung'),(60,'Tissue / Lymph Node','Tissue','Lymph Node'),(61,'Tissue / Mesentary','Tissue','Mesentary'),(62,'Tissue / Oesophagus','Tissue','Oesophagus'),(63,'Tissue / Omentum','Tissue','Omentum'),(64,'Tissue / Ovarian Cyst L','Tissue','Ovarian Cyst L'),(65,'Tissue / Ovarian L','Tissue','Ovarian L'),(66,'Tissue / Ovarian R','Tissue','Ovarian R'),(67,'Tissue / Ovary','Tissue','Ovary'),(68,'Tissue / Pancreas','Tissue','Pancreas'),(69,'Tissue / Peritoneum, pelvic','Tissue','Peritoneum, pelvic'),(70,'Tissue / Placenta','Tissue','Placenta'),(71,'Tissue / Rectal Peritoneal Mass','Tissue','Rectal Peritoneal Mass'),(72,'Tissue / Recto-sigmoid','Tissue','Recto-sigmoid'),(73,'Tissue / Rectum','Tissue','Rectum'),(74,'Tissue / Right Tube','Tissue','Right Tube'),(75,'Tissue / Small Bowel','Tissue','Small Bowel'),(76,'Tissue / Spleen','Tissue','Spleen'),(77,'Tissue / Stomach','Tissue','Stomach'),(78,'Tissue / Tissue','Tissue','Tissue'),(79,'Tissue / Uterus','Tissue','Uterus'),(80,'Tissue / Uterus, endometrium','Tissue','Uterus, endometrium'),(81,'Urine / Urine','Urine','Urine'),(82,'Urine','Urine',NULL);
/*!40000 ALTER TABLE `bio_sampletype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bio_transaction_status`
--

LOCK TABLES `bio_transaction_status` WRITE;
/*!40000 ALTER TABLE `bio_transaction_status` DISABLE KEYS */;
INSERT INTO `bio_transaction_status` (`ID`, `NAME`) VALUES (5,'Aliquoted'),(3,'Completed'),(1,'Initial Quantity'),(2,'Pending'),(4,'Processed');
/*!40000 ALTER TABLE `bio_transaction_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimen_anticoagulant`
--

LOCK TABLES `biospecimen_anticoagulant` WRITE;
/*!40000 ALTER TABLE `biospecimen_anticoagulant` DISABLE KEYS */;
INSERT INTO `biospecimen_anticoagulant` (`ID`, `NAME`) VALUES (1,'N/A'),(2,'EDTA'),(3,'Lithium Heparin'),(4,'Sodium Citrate');
/*!40000 ALTER TABLE `biospecimen_anticoagulant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimen_grade`
--

LOCK TABLES `biospecimen_grade` WRITE;
/*!40000 ALTER TABLE `biospecimen_grade` DISABLE KEYS */;
INSERT INTO `biospecimen_grade` (`ID`, `NAME`) VALUES (1,'Extracted'),(2,'Precipitated');
/*!40000 ALTER TABLE `biospecimen_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimen_quality`
--

LOCK TABLES `biospecimen_quality` WRITE;
/*!40000 ALTER TABLE `biospecimen_quality` DISABLE KEYS */;
INSERT INTO `biospecimen_quality` (`ID`, `NAME`) VALUES (1,'Fresh'),(2,'Frozen short term (<6mths)'),(3,'Frozen long term (>6mths)');
/*!40000 ALTER TABLE `biospecimen_quality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimen_species`
--

LOCK TABLES `biospecimen_species` WRITE;
/*!40000 ALTER TABLE `biospecimen_species` DISABLE KEYS */;
INSERT INTO `biospecimen_species` (`ID`, `NAME`) VALUES (1,'Human'),(2,'Baboon'),(3,'Cat'),(4,'Cow'),(5,'Dog'),(6,'Goat'),(7,'Mouse'),(8,'Pig'),(9,'Rabbit'),(10,'Rat'),(11,'Sheep');
/*!40000 ALTER TABLE `biospecimen_species` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimen_status`
--

LOCK TABLES `biospecimen_status` WRITE;
/*!40000 ALTER TABLE `biospecimen_status` DISABLE KEYS */;
INSERT INTO `biospecimen_status` (`ID`, `NAME`) VALUES (1,'New'),(2,'Archived');
/*!40000 ALTER TABLE `biospecimen_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimen_storage`
--

LOCK TABLES `biospecimen_storage` WRITE;
/*!40000 ALTER TABLE `biospecimen_storage` DISABLE KEYS */;
INSERT INTO `biospecimen_storage` (`ID`, `NAME`, `SIZE`, `UNIT_ID`) VALUES (1,'0.5ml',0.5,17),(2,'1.5ml',1.5,17),(3,'10ml tube',10,17),(4,'2ml tube',2,17),(5,'2ml',2,17),(6,'50ml tube',50,17),(7,'96 well plate',NULL,NULL),(8,'Large tube',NULL,NULL),(9,'Parrafin Block',NULL,NULL);
/*!40000 ALTER TABLE `biospecimen_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biocollectionuid_padchar`
--

LOCK TABLES `biocollectionuid_padchar` WRITE;
/*!40000 ALTER TABLE `biocollectionuid_padchar` DISABLE KEYS */;
INSERT INTO `biocollectionuid_padchar` (`ID`, `NAME`) VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');
/*!40000 ALTER TABLE `biocollectionuid_padchar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biocollectionuid_token`
--

LOCK TABLES `biocollectionuid_token` WRITE;
/*!40000 ALTER TABLE `biocollectionuid_token` DISABLE KEYS */;
INSERT INTO `biocollectionuid_token` (`ID`, `NAME`) VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');
/*!40000 ALTER TABLE `biocollectionuid_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimenuid_padchar`
--

LOCK TABLES `biospecimenuid_padchar` WRITE;
/*!40000 ALTER TABLE `biospecimenuid_padchar` DISABLE KEYS */;
INSERT INTO `biospecimenuid_padchar` (`ID`, `NAME`) VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');
/*!40000 ALTER TABLE `biospecimenuid_padchar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `biospecimenuid_token`
--

LOCK TABLES `biospecimenuid_token` WRITE;
/*!40000 ALTER TABLE `biospecimenuid_token` DISABLE KEYS */;
INSERT INTO `biospecimenuid_token` (`ID`, `NAME`) VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');
/*!40000 ALTER TABLE `biospecimenuid_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cell_status`
--

LOCK TABLES `cell_status` WRITE;
/*!40000 ALTER TABLE `cell_status` DISABLE KEYS */;
INSERT INTO `cell_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Empty','Cell is empty and available'),(2,'Used','Cell is used and unavailable'),(3,'Held','Cell is held for allocation');
/*!40000 ALTER TABLE `cell_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `barcode_label`
--

LOCK TABLES `barcode_label` WRITE;
/*!40000 ALTER TABLE `barcode_label` DISABLE KEYS */;
INSERT INTO `barcode_label` (`ID`, `STUDY_ID`, `BARCODE_PRINTER_ID`, `NAME`, `DESCRIPTION`, `LABEL_PREFIX`, `LABEL_SUFFIX`, `VERSION`) VALUES (1,NULL,1,'zebra biospecimen','Generic Zebra Biospecimen Label','D14\nN','P1',1),(2,NULL,1,'zebra bioCollection','Generic Zebra BioCollection label','D15\nN','P1',1),(3,NULL,2,'straw barcode','Generic Brady Straw Biospecimen label','DIRECTION 0\nREFERENCE 0,0\nCLS','PRINT 1,1',1),(4,NULL,1,'zebra biospecimen v2','Generic Zebra Biospecimen Label v2','D14\nN','P1',2),(5,NULL,1,'straw barcode','Generic Zebra Straw Label','D14\nN','P1',3);
/*!40000 ALTER TABLE `barcode_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `barcode_label_data`
--

LOCK TABLES `barcode_label_data` WRITE;
/*!40000 ALTER TABLE `barcode_label_data` DISABLE KEYS */;
INSERT INTO `barcode_label_data` (`ID`, `BARCODE_LABEL_ID`, `COMMAND`, `X_COORD`, `Y_COORD`, `P1`, `P2`, `P3`, `P4`, `P5`, `P6`, `P7`, `P8`, `QUOTE_LEFT`, `DATA`, `QUOTE_RIGHT`, `LINE_FEED`) VALUES (42,1,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(43,1,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(44,1,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(45,1,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(46,1,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(47,1,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(48,2,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ID: {$subjectUid} Family ID: ${familyId}','\"','\n'),(49,2,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ASRB No: ${asrbno}','\"','\n'),(50,2,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Collection Date: ${collectionDate}','\"','\n'),(51,2,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Researcher: ${refDoctor}','\"','\n'),(52,2,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','DOB: ${dateOfBirth} Sex: {$sex}','\"','\n'),(53,3,'BARCODE',25,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(54,3,'TEXT',25,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(55,3,'TEXT',25,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(56,3,'BARCODE',250,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(57,3,'TEXT',250,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(58,3,'TEXT',250,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(93,4,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(94,4,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(95,4,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(96,4,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n'),(111,5,'B',242,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(112,5,'B',-80,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(113,5,'A',250,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(114,5,'A',-80,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n');
/*!40000 ALTER TABLE `barcode_label_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inv_col_row_type`
--

LOCK TABLES `inv_col_row_type` WRITE;
/*!40000 ALTER TABLE `inv_col_row_type` DISABLE KEYS */;
INSERT INTO `inv_col_row_type` (`ID`, `NAME`) VALUES (1,'Numeric'),(2,'Alphabet');
/*!40000 ALTER TABLE `inv_col_row_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `treatment_type`
--

LOCK TABLES `treatment_type` WRITE;
/*!40000 ALTER TABLE `treatment_type` DISABLE KEYS */;
INSERT INTO `treatment_type` (`ID`, `NAME`) VALUES (4,'70% Alcohol Fixed'),(2,'Formalin Fixed'),(1,'Frozen'),(5,'RN later'),(6,'RNA later, then Formalin Fixed'),(7,'RNA later, then Snap Frozen'),(3,'Tissue Cultured'),(8,'Unprocessed');
/*!40000 ALTER TABLE `treatment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'mm',NULL),(2,'ug/L',NULL),(3,'Years',NULL),(4,'mµ/l',NULL),(5,'bpm',NULL),(6,'g/L',NULL),(7,'fL',NULL),(8,'feet',NULL),(9,'IU/L',NULL),(10,'kg',NULL),(11,'U',NULL),(12,'µV',NULL),(13,'Days',NULL),(14,'mg/l',NULL),(15,'Age',NULL),(16,'cm',NULL),(17,'mL',NULL),(18,'Iµ/mL',NULL),(19,'pg',NULL),(20,'row 2',NULL),(21,'grams',NULL),(22,'pred',NULL),(23,'Gy',NULL),(24,'Hours',NULL),(25,'µ/L',NULL),(26,'Mins',NULL),(27,'%',NULL),(28,'mS',NULL),(29,'mm/hr',NULL),(30,'mg/dl',NULL),(31,'mn',NULL),(33,'mg/L',NULL),(34,'kgm2',NULL),(35,'mm Hg',NULL),(36,'kg/m2',NULL),(37,'Pipes',NULL),(38,'L',NULL),(39,'S',NULL),(40,'m',NULL),(41,'fl',NULL),(42,'hours',NULL),(43,'mm/hg',NULL);
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53

USE reporting;
-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: reporting
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.10.04.1

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
-- Dumping data for table `report_output_format`
--

LOCK TABLES `report_output_format` WRITE;
/*!40000 ALTER TABLE `report_output_format` DISABLE KEYS */;
INSERT INTO `report_output_format` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'PDF','Portable Document Format (compatible with Adobe Reader)'),(2,'CSV','Comma Separated Value (compatible with Excel)');
/*!40000 ALTER TABLE `report_output_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `report_template`
--

LOCK TABLES `report_template` WRITE;
/*!40000 ALTER TABLE `report_template` DISABLE KEYS */;
INSERT INTO `report_template` (`ID`, `NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) VALUES (1,'Study Summary Report','This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>','StudySummaryReport.jrxml',1,23),(2,'Study-level Consent Details Report','This report lists detailed subject information for a particular study based on their consent status at the study-level.','ConsentDetailsReport.jrxml',2,24),(3,'Study Component Consent Details Report','This report lists detailed subject information for a particular study based on their consent status for a specific study component.','ConsentDetailsReport.jrxml',2,25),(4,'Phenotypic Field Details Report (Data Dictionary)','This report lists detailed field information for a particular study based on their associated phenotypic collection.','DataDictionaryReport.jrxml',3,26),(5,'Study User Role Permissions Report','This report lists all user role and permissions for the study in context.','StudyUserRolePermissions.jrxml',1,33);
/*!40000 ALTER TABLE `report_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-04 11:30:53
/* Initialise the super user in the database */

USE study;
-- Insert first Super User as a valid account (replace the value for 'LDAP_USER_NAME' if necessary)
INSERT INTO study.ark_user (ID, LDAP_USER_NAME) VALUES (1, 'arksuperuser@ark.org.au');
-- Set up the permissions for the first Super User (ark_role_id = 1)
INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (1,1,1,1,NULL);
INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (2,1,1,2,NULL);
INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (3,1,1,3,NULL);
INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (4,1,1,4,NULL);
INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (5,1,1,5,NULL);
-- NB: ark_module_id = 6 (Reporting) omitted, because reporting relies on permissions defined in other modules.
INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (7,1,1,7,NULL);
