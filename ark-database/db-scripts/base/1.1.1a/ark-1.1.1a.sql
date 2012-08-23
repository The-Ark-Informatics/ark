/* MySQL script to create the Ark database schema and default reference tables */;

/* Creating the schemas */
CREATE DATABASE /*!32312 IF NOT EXISTS*/ audit /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ study /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ pheno /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ geno /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ lims /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ reporting /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ admin /*!40100 DEFAULT CHARACTER SET latin1 */;


USE audit;

-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: audit
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENTED_BY` varchar(100) DEFAULT NULL,
  `COMMENTS` varchar(500) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `RECEIVED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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

-- Dump completed on 2012-08-23 17:09:15

USE study;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: study
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
  `STATE_ID` int(11) DEFAULT NULL,
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
  KEY `fk_address_person` (`PERSON_ID`) USING BTREE,
  KEY `fk_address_address_type` (`ADDRESS_TYPE_ID`) USING BTREE,
  KEY `fk_address_status` (`ADDRESS_STATUS_ID`) USING BTREE,
  KEY `fk_address_preferred_mailing_address_id` (`PREFERRED_MAILING_ADDRESS`) USING BTREE,
  KEY `fk_address_state` (`STATE_ID`) USING BTREE,
  CONSTRAINT `fk_address_address_type` FOREIGN KEY (`ADDRESS_TYPE_ID`) REFERENCES `address_type` (`ID`),
  CONSTRAINT `fk_address_country` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_person` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_state` FOREIGN KEY (`STATE_ID`) REFERENCES `state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_status` FOREIGN KEY (`ADDRESS_STATUS_ID`) REFERENCES `address_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_TYPE_ID`) REFER `study/addre';
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
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  CONSTRAINT `fk_ark_module_role_1` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
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
  CONSTRAINT `FK_ROLE_TMPLT_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
  CONSTRAINT `FK_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ACTION_TYPE_ID`) REFER `study/action';
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
-- Table structure for table `correspondences`
--

DROP TABLE IF EXISTS `correspondences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondences` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
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
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_correspondences_ark_user` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  CONSTRAINT `FK_CUSTOM_FIELD_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_UNIT_TYPE_ID` FOREIGN KEY (`UNIT_TYPE_ID`) REFERENCES `unit_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `custom_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  CONSTRAINT `FK_CFU_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFU_UPLOAD_ID` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  CONSTRAINT `link_study_substudy_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
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
  PRIMARY KEY (`ID`),
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
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_PERSON_FK` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STUDY_FK` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` FOREIGN KEY (`SUBJECT_STATUS_ID`) REFERENCES `subject_status` (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
-- Table structure for table `payload`
--

DROP TABLE IF EXISTS `payload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payload` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='This is a simple table for storing LObs and an id to represe';
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
  `OTHER_ID` int(11) DEFAULT NULL,
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`GENDER_TYPE_ID`) REFER `study/gender';
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
  `date_inserted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `SURNAME` (`LASTNAME`) USING BTREE,
  KEY `PERSON_ID` (`PERSON_ID`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
  `PHONE_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `SOURCE` varchar(500) DEFAULT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENT` varchar(1000) DEFAULT NULL,
  `SILENT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`,`PHONE_NUMBER`,`PERSON_ID`,`PHONE_TYPE_ID`),
  UNIQUE KEY `AREA_CODE_2` (`AREA_CODE`,`PHONE_NUMBER`,`PHONE_TYPE_ID`,`PERSON_ID`),
  KEY `PHONE_PHONE_TYPE_FK` (`PHONE_TYPE_ID`) USING BTREE,
  KEY `PHONE_PERSON_FK` (`PERSON_ID`) USING BTREE,
  KEY `phone_ibfk_3` (`PHONE_STATUS_ID`),
  KEY `phone_ibfk_4` (`SILENT`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_2` FOREIGN KEY (`PHONE_TYPE_ID`) REFERENCES `phone_type` (`ID`),
  CONSTRAINT `phone_ibfk_3` FOREIGN KEY (`PHONE_STATUS_ID`) REFERENCES `phone_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_4` FOREIGN KEY (`SILENT`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='A link table that associates a country and its respective st';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_STATUS_ID`) REFER `study/study';
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
  CONSTRAINT `study_comp_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
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
  CONSTRAINT `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  CONSTRAINT `fk_subject_file_subject` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `UPLOAD_REPORT` longblob,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `UPLOAD_TYPE_ID` int(11) DEFAULT NULL,
  `PAYLOAD_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  KEY `fk_upload_ark_function_id` (`ARK_FUNCTION_ID`),
  KEY `fk_upload_study` (`STUDY_ID`) USING BTREE,
  KEY `fk_upload_1` (`UPLOAD_TYPE_ID`),
  KEY `fk_upload_payload` (`PAYLOAD_ID`),
  KEY `fk_upload_status` (`STATUS_ID`),
  CONSTRAINT `fk_upload_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `upload_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_ark_function_id` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_payload` FOREIGN KEY (`PAYLOAD_ID`) REFERENCES `payload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `study/del';
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `upload_type`
--

DROP TABLE IF EXISTS `upload_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ARK_MODULE_ID` int(11) NOT NULL DEFAULT '2',
  PRIMARY KEY (`ID`),
  KEY `fk_upload_type_ark_module` (`ARK_MODULE_ID`),
  CONSTRAINT `fk_upload_type_ark_module` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Reference table to describe the type of an upload';
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

-- Dump completed on 2012-08-23 17:09:16

USE pheno;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: pheno
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
  CONSTRAINT `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PHENO_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `study`.`custom_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PHENO_QUESTIONNAIRE_STATUS_ID` FOREIGN KEY (`QUESTIONNAIRE_STATUS_ID`) REFERENCES `questionnaire_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REVIEWED_BY_ARK_USER_ID` FOREIGN KEY (`REVIEWED_BY_ID`) REFERENCES `study`.`ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `pheno/del';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-23 17:09:16

USE geno;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: geno
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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

-- Dump completed on 2012-08-23 17:09:16

USE lims;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: lims
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
-- Table structure for table `barcode_label_template`
--

DROP TABLE IF EXISTS `barcode_label_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label_template` (
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='NOTE: the name of the printer MUST match the shared name';
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
  `TRANSACTION_DATE` datetime DEFAULT NULL,
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  KEY `fk_collection_idx` (`NAME`),
  KEY `fk_collection_name_idx` (`NAME`),
  KEY `fk_collection_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_collection_study` (`STUDY_ID`),
  CONSTRAINT `fk_collection_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `QUANTITY` double DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT NULL,
  `TREATMENT_TYPE_ID` int(11) NOT NULL,
  `BARCODED` tinyint(1) NOT NULL DEFAULT '0',
  `BIOSPECIMEN_QUALITY_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_STATUS_ID` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `CONCENTRATION` float DEFAULT NULL,
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
  KEY `fk_biospecimen_old_id` (`OLD_ID`),
  KEY `fk_biospecimen_subject` (`LINK_SUBJECT_STUDY_ID`),
  CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biocollection` FOREIGN KEY (`BIOCOLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biospecimen` FOREIGN KEY (`PARENT_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_biospecimen_treatment_type_id` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`),
  KEY `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`),
  KEY `fk_inv_box_rack_idx` (`RACK_ID`),
  CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rack` FOREIGN KEY (`RACK_ID`) REFERENCES `inv_rack` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `BIOSPECIMENKEY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_cell_box_idx` (`BOX_ID`) USING BTREE,
  KEY `fk_inv_cell_biospecimen_idx` (`BIOSPECIMEN_ID`) USING BTREE,
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_freezer_tray_idx` (`FREEZER_ID`) USING BTREE,
  CONSTRAINT `FK_inv_rack_inv_freezer` FOREIGN KEY (`FREEZER_ID`) REFERENCES `inv_freezer` (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
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

-- Dump completed on 2012-08-23 17:09:16

USE reporting;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: reporting
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
  CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_REPORTTEMPLATE_ARKFUNCTION` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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

-- Dump completed on 2012-08-23 17:09:16

USE admin;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: admin
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
  `ITEM_COST` double DEFAULT NULL,
  `TOTAL_COST` double DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(45) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `TOTAL_GST` double DEFAULT NULL,
  `GST` double DEFAULT NULL,
  `GST_ALLOW` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_bilable_item_billable_type` (`BILLABLE_TYPE`),
  KEY `fk_bilable_item_request_id` (`REQUEST_ID`),
  KEY `fk_bilable_item_status` (`STATUS_ID`),
  CONSTRAINT `fk_bilable_item_billable_type` FOREIGN KEY (`BILLABLE_TYPE`) REFERENCES `billable_item_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bilable_item_request_id` FOREIGN KEY (`REQUEST_ID`) REFERENCES `work_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `OFFICE_PHONE` varchar(12) DEFAULT NULL,
  `MOBILE` varchar(12) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `FAX` varchar(12) DEFAULT NULL,
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`),
  KEY `fk_work_request_researcher_id` (`RESEARCHER_ID`),
  KEY `fk_work_request_status_id` (`STATUS_ID`),
  CONSTRAINT `fk_work_request_researcher_id` FOREIGN KEY (`RESEARCHER_ID`) REFERENCES `researcher` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_work_request_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `work_request_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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

-- Dump completed on 2012-08-23 17:09:17
/* Reference/lookup data */

USE study;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: study
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
INSERT INTO `ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (1,'STUDY','Study Management  usecase. This is represented via the Study Detail Tab under the main Study  Tab. ',1,'tab.module.study.details'),(2,'STUDY_COMPONENT','Study Component usecase. This is represented via the StudyComponent Tab under the main Study  Tab. ',1,'tab.module.study.components'),(3,'MY_DETAIL','Edit My details usecase, this is represented via My Detail tab.',1,'tab.module.mydetails'),(4,'USER','User management usecase. This is represented via the User Tab under the main Study  Tab.',1,'tab.module.user.management'),(5,'SUBJECT','Subject management usecase. This is represented via the Subject Tab under the main Study  Tab.',1,'tab.module.subject.detail'),(6,'PHONE','Manage phone usecase. This is represented via the Phone tab under the main Study  Tab.',1,'tab.module.person.phone'),(7,'ADDRESS','Manage Address',1,'tab.module.person.address'),(8,'ATTACHMENT','Manage Consent and Component attachments. This is represented via the Attachment tab under Subject Main tab.',1,'tab.module.subject.subjectFile'),(9,'CONSENT','Manage Subject Consents. This is represented via the Consent tab under the main Study  Tab.',1,'tab.module.subject.consent'),(10,'SUBJECT_UPLOAD','Bulk upload of Subjects.',1,'tab.module.subject.subjectUpload'),(11,'SUBJECT_CUSTOM_FIELD','Manage Custom Fields for Subjects.',1,'tab.module.subject.subjectcustomfield'),(12,'DATA_DICTIONARY','Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.field'),(13,'DATA_DICTIONARY_UPLOAD','Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldUpload'),(14,'PHENO_COLLECTION','Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.collection'),(15,'FIELD_DATA','Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldData'),(16,'FIELD_DATA_UPLOAD','Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.phenoUpload'),(17,'LIMS_SUBJECT','LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.',1,'tab.module.lims.subject.detail'),(18,'LIMS_COLLECTION','LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.',1,'tab.module.lims.collection'),(19,'BIOSPECIMEN','LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.',1,'tab.module.lims.biospecimen'),(20,'INVENTORY','LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.',1,'tab.module.lims.inventory'),(21,'CORRESPONDENCE','',1,'tab.module.subject.correspondence'),(22,'SUMMARY','Phenotypic Summary.',1,'tab.module.phenotypic.summary'),(23,'REPORT_STUDYSUMARY','Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>',2,NULL),(24,'REPORT_STUDYLEVELCONSENT','Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.',2,NULL),(25,'REPORT_STUDYCOMPCONSENT','Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.',2,NULL),(26,'REPORT_PHENOFIELDDETAILS','Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic data set.',2,NULL),(27,'GENO_COLLECTION','Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu',1,'tab.module.geno.collection'),(28,'ROLE_POLICY_TEMPLATE','Allows CRUD operations on the ark_role_policy_template table for the Ark application',1,'tab.module.admin.rolePolicyTemplate'),(29,'MODULE','Allows CRUD operations on the ark_module table for the Ark application',1,'tab.module.admin.module'),(30,'FUNCTION','Allows CRUD operations on the ark_function table for the Ark application',1,'tab.module.admin.function'),(33,'REPORT_STUDY_USER_ROLE_PERMISSIONS','Study User Role Permissions Report lists all user role and permissions for the study in context.',2,NULL),(34,'SUBJECT_CUSTOM_DATA','Data entry for Subject Custom Fields.',1,'tab.module.subject.subjectcustomdata'),(35,'LIMS_COLLECTION_CUSTOM_FIELD','Manage Custom Fields for LIMS collections.',1,'tab.module.lims.collectioncustomfield'),(36,'LIMS_COLLECTION_CUSTOM_DATA','Data entry for LIMS collection Custom Fields.',1,'tab.module.lims.collectioncustomdata'),(37,'BIOSPECIMEN_CUSTOM_FIELD','Manage Custom Fields for Biospecimens.',1,'tab.module.lims.biospecimencustomfield'),(38,'BIOSPECIMEN_CUSTOM_DATA','Data entry for Biospecimen Custom Fields.',1,'tab.module.lims.biospecimencustomdata'),(41,'BIOSPECIMENUID_TEMPLATE','Manage BiospecimenUid templates for the study,',1,'tab.module.lims.biospecimenuidtemplate'),(42,'BARCODE_LABEL','Manage barcode label definitions the study,',1,'tab.module.lims.barcodelabel'),(43,'BARCODE_PRINTER','Manage barcode printers for the study,',1,'tab.module.lims.barcodeprinter'),(44,'MODULE_FUNCTION','Allows CRUD operations on the ark_module_function table for the Ark application',1,'tab.module.admin.modulefunction'),(45,'ROLE','Allows CRUD operations on users roles',1,'tab.module.admin.role'),(46,'MODULE_ROLE','Allows CRUD operations on module_role table',1,'tab.module.admin.modulerole'),(47,'BIOSPECIMEN_UPLOAD','Uploader for bispecimens',1,'tab.module.lims.biospecimenUpload'),(50,'SUBJECT_CUSTOM_FIELD_UPLOAD','Uploader for Subject Custom Fields',1,'tab.module.subject.subjectCustomFieldUpload'),(51,'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD','Uploader for BioCollection Custom Fields',1,'tab.module.lims.bioCollectionCustomFieldUpload'),(52,'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD','Uploader for Biospecimen Custom Fields',1,'tab.module.lims.biospecimenCustomFieldUpload'),(54,'RESEARCHER','Researcher use case',1,'tab.module.work.researcher'),(55,'BILLABLE_ITEM_TYPE','Billable item type use case',1,'tab.module.work.billableitemtype'),(56,'WORK_REQUEST','Work Request tab',1,'tab.module.work.workrequest'),(57,'BILLABLE_ITEM','Billable Item Tab',1,'tab.module.work.billableitem'),(58,'BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD','Uploader for both Biospecimen and Biocollection Custom Fields',1,'tab.module.lims.bioupload');
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
INSERT INTO `ark_module` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Study','study'),(2,'Subject',NULL),(3,'Phenotypic',NULL),(4,'Genotypic',NULL),(5,'LIMS',NULL),(6,'Reporting',NULL),(8,'Work Tracking','Work Tracking Module'),(9,'Admin',NULL);
/*!40000 ALTER TABLE `ark_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module_function`
--

LOCK TABLES `ark_module_function` WRITE;
/*!40000 ALTER TABLE `ark_module_function` DISABLE KEYS */;
INSERT INTO `ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (1,1,1,1),(2,1,2,2),(4,1,4,3),(23,1,23,NULL),(27,4,27,1),(88,9,29,1),(89,9,30,2),(90,9,44,3),(91,9,45,4),(92,9,46,5),(93,9,28,6),(117,3,26,1),(118,3,12,2),(119,3,13,3),(120,3,14,4),(121,3,15,5),(122,3,16,6),(159,8,54,1),(160,8,55,2),(161,8,56,3),(162,8,57,4),(210,2,24,1),(211,2,25,2),(212,2,5,3),(213,2,34,4),(214,2,6,5),(215,2,7,6),(216,2,8,7),(217,2,9,8),(218,2,21,9),(219,2,10,10),(220,2,11,11),(221,2,50,12),(246,5,17,1),(247,5,19,2),(248,5,20,3),(249,5,35,4),(250,5,37,5),(251,5,51,6),(252,5,52,7),(253,5,47,8),(254,5,42,9),(255,5,43,10);
/*!40000 ALTER TABLE `ark_module_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module_role`
--

LOCK TABLES `ark_module_role` WRITE;
/*!40000 ALTER TABLE `ark_module_role` DISABLE KEYS */;
INSERT INTO `ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES (3,2,4),(4,2,5),(5,2,6),(6,3,7),(7,3,8),(8,5,9),(9,5,10),(10,4,11),(11,5,12),(12,3,13),(20,1,2),(21,1,3),(24,8,2);
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
INSERT INTO `ark_role` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Super Administrator','Highest level user of the ARK system!'),(2,'Study Administrator',NULL),(3,'Study Read-Only user',NULL),(4,'Subject Administrator',NULL),(5,'Subject Data Manager',NULL),(6,'Subject Read-Only user',NULL),(7,'Pheno Read-Only user',NULL),(8,'Pheno Data Manager',NULL),(9,'LIMS Read-Only user',NULL),(10,'LIMS Data Manager',NULL),(11,'Geno Read-Only User',NULL),(12,'LIMS Administrator',NULL),(13,'Pheno Administrator',NULL),(14,'New Role',NULL);
/*!40000 ALTER TABLE `ark_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_role_policy_template`
--

LOCK TABLES `ark_role_policy_template` WRITE;
/*!40000 ALTER TABLE `ark_role_policy_template` DISABLE KEYS */;
INSERT INTO `ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (1,1,NULL,NULL,1),(2,1,NULL,NULL,2),(3,1,NULL,NULL,3),(4,1,NULL,NULL,4),(7,2,1,2,1),(8,2,1,2,2),(9,2,1,2,3),(10,2,1,3,2),(11,2,1,3,3),(15,3,1,1,2),(16,3,1,2,2),(20,4,2,5,1),(21,4,2,5,2),(22,4,2,5,3),(23,4,2,6,1),(24,4,2,6,2),(25,4,2,6,3),(26,4,2,6,4),(27,4,2,7,1),(28,4,2,7,2),(29,4,2,7,3),(30,4,2,7,4),(31,4,2,8,1),(32,4,2,8,2),(33,4,2,8,3),(34,4,2,8,4),(35,4,2,9,1),(36,4,2,9,2),(37,4,2,9,3),(38,4,2,9,4),(39,5,2,5,2),(40,5,2,6,2),(41,5,2,6,3),(42,5,2,7,2),(43,5,2,7,3),(44,5,2,8,2),(45,5,2,8,3),(46,5,2,9,2),(47,5,2,9,3),(48,4,2,10,1),(49,4,2,10,2),(50,4,2,10,3),(51,4,2,11,1),(52,4,2,11,2),(53,4,2,11,3),(54,5,2,5,1),(55,5,2,5,3),(56,5,2,6,1),(57,5,2,6,4),(58,5,2,7,1),(59,5,2,7,4),(60,5,2,8,1),(61,5,2,8,4),(62,5,2,9,1),(63,5,2,9,4),(64,6,2,5,2),(65,6,2,6,2),(66,6,2,7,2),(67,6,2,8,2),(68,6,2,9,2),(69,6,2,34,2),(70,8,3,12,2),(71,8,3,13,2),(74,8,3,16,1),(75,10,5,17,3),(76,10,5,18,3),(77,10,5,19,3),(78,10,5,20,3),(79,9,5,17,2),(80,9,5,18,2),(81,9,5,19,2),(82,9,5,20,2),(83,7,3,12,2),(84,7,3,13,2),(85,7,3,14,2),(86,7,3,15,2),(87,7,3,16,2),(88,5,2,10,1),(89,8,3,22,2),(91,10,5,17,4),(92,2,1,23,2),(93,3,1,23,2),(94,4,2,24,2),(95,5,2,24,2),(96,6,2,24,2),(98,5,2,25,2),(99,6,2,25,2),(100,7,3,26,2),(101,8,3,26,2),(102,10,5,17,4),(103,10,5,19,4),(104,11,4,27,2),(106,8,3,14,2),(107,6,2,21,2),(108,5,2,21,1),(109,5,2,21,2),(110,5,2,21,3),(111,5,2,21,4),(112,4,2,21,1),(113,4,2,21,2),(114,4,2,21,3),(115,4,2,21,4),(116,12,5,17,1),(117,12,5,17,2),(118,12,5,17,3),(119,12,5,17,4),(120,12,5,18,1),(121,12,5,18,2),(122,12,5,18,3),(123,12,5,18,4),(124,12,5,19,1),(125,12,5,19,2),(126,12,5,19,3),(127,12,5,19,4),(128,12,5,20,1),(129,12,5,20,2),(130,12,5,20,3),(131,12,5,20,4),(133,13,3,13,1),(136,13,3,16,1),(138,13,3,26,2),(142,1,9,28,1),(143,1,9,28,2),(144,1,9,28,3),(145,1,9,28,4),(146,13,3,22,2),(147,7,3,22,2),(148,13,3,12,1),(149,13,3,12,2),(184,4,2,34,1),(185,4,2,34,2),(186,4,2,34,3),(187,4,2,34,4),(188,5,2,34,1),(189,5,2,34,2),(190,5,2,34,3),(191,5,2,34,4),(192,12,5,35,1),(193,12,5,35,2),(194,12,5,35,3),(195,12,5,36,1),(196,12,5,36,2),(197,12,5,36,3),(198,12,5,36,4),(199,10,5,36,1),(200,10,5,36,2),(201,10,5,36,3),(202,10,5,36,4),(203,9,5,36,2),(204,12,5,37,1),(205,12,5,37,2),(206,12,5,37,3),(207,12,5,38,1),(208,12,5,38,2),(209,12,5,38,3),(210,12,5,38,4),(211,10,5,38,1),(212,10,5,38,2),(213,10,5,38,3),(214,10,5,38,4),(215,9,5,38,2),(228,8,3,15,1),(229,8,3,15,2),(230,8,3,15,3),(233,3,1,3,2),(246,14,1,23,2),(247,14,1,1,2),(248,14,1,2,2),(249,14,1,4,2),(250,2,1,23,2),(252,2,1,2,2),(254,3,1,23,2),(255,3,1,1,2),(256,3,1,2,2),(257,3,1,4,2),(258,4,1,23,2),(259,4,1,1,2),(260,4,1,2,2),(261,4,1,4,2),(262,2,1,33,2),(263,13,3,15,1),(264,13,3,15,2),(265,13,3,15,3),(266,13,3,15,4),(267,13,3,14,1),(268,13,3,14,2),(269,13,3,14,3),(270,13,3,14,4),(279,10,5,47,1),(280,10,5,47,2),(281,10,5,47,3),(282,10,5,47,4),(283,12,5,47,1),(284,12,5,47,2),(285,12,5,47,3),(286,12,5,47,4),(287,2,1,1,1),(288,2,1,1,2),(289,2,1,1,3),(290,12,5,NULL,4),(295,12,5,51,1),(296,12,5,51,2),(297,12,5,51,3),(298,12,5,51,4),(299,12,5,52,1),(300,12,5,52,2),(301,12,5,52,3),(302,12,5,52,4),(303,4,2,50,1),(304,4,2,50,2),(305,4,2,50,3),(306,4,2,50,4),(308,12,5,42,1),(309,12,5,42,2),(310,12,5,42,3),(311,12,5,42,4),(312,2,1,4,1),(313,2,1,4,2),(314,2,1,4,3),(315,2,1,4,4),(316,12,5,58,1),(317,12,5,58,2),(318,12,5,58,3),(319,12,5,58,4),(329,2,8,55,1),(330,2,8,55,2),(331,2,8,55,3),(332,2,8,55,4),(333,2,8,56,1),(334,2,8,56,2),(335,2,8,56,3),(336,2,8,56,4),(337,2,8,57,1),(338,2,8,57,2),(339,2,8,57,3),(340,2,8,57,4),(341,2,8,54,1),(342,2,8,54,2),(343,2,8,54,3),(344,2,8,54,4);
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
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` (`ID`, `NAME`, `COUNTRY_CODE`, `ALPHA_3_CODE`, `NUMERIC_CODE`, `OFFICIAL_NAME`) VALUES (1,'Australia','AU','AUS','036',''),(2,'United Kingdom','GB','GBR','826','United Kingdom of Great Britain and Northern '),(3,'Canada','CA','CAN','124',''),(4,'Afghanistan','AF','AFG','004','Islamic Republic of Afghanistan'),(5,'Åland Islands','AX','ALA','248',''),(6,'Albania','AL','ALB','008','Republic of Albania'),(7,'Algeria','DZ','DZA','012','People\'s Democratic Republic of Algeria'),(8,'American Samoa','AS','ASM','016',''),(9,'Andorra','AD','AND','020','Principality of Andorra'),(10,'Angola','AO','AGO','024','Republic of Angola'),(11,'Anguilla','AI','AIA','660',''),(12,'Antarctica','AQ','ATA','010',''),(13,'Antigua and Barbuda','AG','ATG','028',''),(14,'Argentina','AR','ARG','032','Argentine Republic'),(15,'Armenia','AM','ARM','051','Republic of Armenia'),(16,'Aruba','AW','ABW','533',''),(18,'Austria','AT','AUT','040','Republic of Austria'),(19,'Azerbaijan','AZ','AZE','031','Republic of Azerbaijan'),(20,'Bahamas','BS','BHS','044','Commonwealth of the Bahamas'),(21,'Bahrain','BH','BHR','048','Kingdom of Bahrain'),(22,'Bangladesh','BD','BGD','050','People\'s Republic of Bangladesh'),(23,'Barbados','BB','BRB','052',''),(24,'Belarus','BY','BLR','112','Republic of Belarus'),(25,'Belgium','BE','BEL','056','Kingdom of Belgium'),(26,'Belize','BZ','BLZ','084',''),(27,'Benin','BJ','BEN','204','Republic of Benin'),(28,'Bermuda','BM','BMU','060',''),(29,'Bhutan','BT','BTN','064','Kingdom of Bhutan'),(30,'Bolivia','BO','BOL','068','Republic of Bolivia'),(31,'BONAIRE, SAINT EUSTATIUS AND SABA','BQ','','',''),(32,'Bosnia and Herzegovina','BA','BIH','070','Republic of Bosnia and Herzegovina'),(33,'Botswana','BW','BWA','072','Republic of Botswana'),(34,'Bouvet Island','BV','BVT','074',''),(35,'Brazil','BR','BRA','076','Federative Republic of Brazil'),(36,'British Indian Ocean Territory','IO','IOT','086',''),(37,'Brunei Darussalam','BN','BRN','096',''),(38,'Bulgaria','BG','BGR','100','Republic of Bulgaria'),(39,'Burkina Faso','BF','BFA','854',''),(40,'Burundi','BI','BDI','108','Republic of Burundi'),(41,'Cambodia','KH','KHM','116','Kingdom of Cambodia'),(42,'Cameroon','CM','CMR','120','Republic of Cameroon'),(44,'Cape Verde','CV','CPV','132','Republic of Cape Verde'),(45,'Cayman Islands','KY','CYM','136',''),(46,'Central African Republic','CF','CAF','140',''),(47,'Chad','TD','TCD','148','Republic of Chad'),(48,'Chile','CL','CHL','152','Republic of Chile'),(49,'China','CN','CHN','156','People\'s Republic of China'),(50,'Christmas Island','CX','CXR','162',''),(51,'Cocos (Keeling) Islands','CC','CCK','166',''),(52,'Colombia','CO','COL','170','Republic of Colombia'),(53,'Comoros','KM','COM','174','Union of the Comoros'),(54,'Congo','CG','COG','178','Republic of the Congo'),(55,'Congo, The Democratic Republic of the','CD','COD','180',''),(56,'Cook Islands','CK','COK','184',''),(57,'Costa Rica','CR','CRI','188','Republic of Costa Rica'),(58,'Côte d\'Ivoire','CI','CIV','384','Republic of Côte d\'Ivoire'),(59,'Croatia','HR','HRV','191','Republic of Croatia'),(60,'Cuba','CU','CUB','192','Republic of Cuba'),(61,'CURACAO','CW','','',''),(62,'Cyprus','CY','CYP','196','Republic of Cyprus'),(63,'Czech Republic','CZ','CZE','203',''),(64,'Denmark','DK','DNK','208','Kingdom of Denmark'),(65,'Djibouti','DJ','DJI','262','Republic of Djibouti'),(66,'Dominica','DM','DMA','212','Commonwealth of Dominica'),(67,'Dominican Republic','DO','DOM','214',''),(68,'Ecuador','EC','ECU','218','Republic of Ecuador'),(69,'Egypt','EG','EGY','818','Arab Republic of Egypt'),(70,'El Salvador','SV','SLV','222','Republic of El Salvador'),(71,'Equatorial Guinea','GQ','GNQ','226','Republic of Equatorial Guinea'),(72,'Eritrea','ER','ERI','232',''),(73,'Estonia','EE','EST','233','Republic of Estonia'),(74,'Ethiopia','ET','ETH','231','Federal Democratic Republic of Ethiopia'),(75,'Falkland Islands (Malvinas)','FK','FLK','238',''),(76,'Faroe Islands','FO','FRO','234',''),(77,'Fiji','FJ','FJI','242','Republic of the Fiji Islands'),(78,'Finland','FI','FIN','246','Republic of Finland'),(79,'France','FR','FRA','250','French Republic'),(80,'French Guiana','GF','GUF','254',''),(81,'French Polynesia','PF','PYF','258',''),(82,'French Southern Territories','TF','ATF','260',''),(83,'Gabon','GA','GAB','266','Gabonese Republic'),(84,'Gambia','GM','GMB','270','Republic of the Gambia'),(85,'Georgia','GE','GEO','268',''),(86,'Germany','DE','DEU','276','Federal Republic of Germany'),(87,'Ghana','GH','GHA','288','Republic of Ghana'),(88,'Gibraltar','GI','GIB','292',''),(89,'Greece','GR','GRC','300','Hellenic Republic'),(90,'Greenland','GL','GRL','304',''),(91,'Grenada','GD','GRD','308',''),(92,'Guadeloupe','GP','GLP','312',''),(93,'Guam','GU','GUM','316',''),(94,'Guatemala','GT','GTM','320','Republic of Guatemala'),(95,'Guernsey','GG','GGY','831',''),(96,'Guinea','GN','GIN','324','Republic of Guinea'),(97,'Guinea-Bissau','GW','GNB','624','Republic of Guinea-Bissau'),(98,'Guyana','GY','GUY','328','Republic of Guyana'),(99,'Haiti','HT','HTI','332','Republic of Haiti'),(100,'Heard Island and McDonald Islands','HM','HMD','334',''),(101,'Holy See (Vatican City State)','VA','VAT','336',''),(102,'Honduras','HN','HND','340','Republic of Honduras'),(103,'Hong Kong','HK','HKG','344','Hong Kong Special Administrative Region of Ch'),(104,'Hungary','HU','HUN','348','Republic of Hungary'),(105,'Iceland','IS','ISL','352','Republic of Iceland'),(106,'India','IN','IND','356','Republic of India'),(107,'Indonesia','ID','IDN','360','Republic of Indonesia'),(108,'Iran, Islamic Republic of','IR','IRN','364','Islamic Republic of Iran'),(109,'Iraq','IQ','IRQ','368','Republic of Iraq'),(110,'Ireland','IE','IRL','372',''),(111,'Isle of Man','IM','IMN','833',''),(112,'Israel','IL','ISR','376','State of Israel'),(113,'Italy','IT','ITA','380','Italian Republic'),(114,'Jamaica','JM','JAM','388',''),(115,'Japan','JP','JPN','392',''),(116,'Jersey','JE','JEY','832',''),(117,'Jordan','JO','JOR','400','Hashemite Kingdom of Jordan'),(118,'Kazakhstan','KZ','KAZ','398','Republic of Kazakhstan'),(119,'Kenya','KE','KEN','404','Republic of Kenya'),(120,'Kiribati','KI','KIR','296','Republic of Kiribati'),(121,'Korea, Democratic People\'s Republic of','KP','PRK','408','Democratic People\'s Republic of Korea'),(122,'Korea, Republic of','KR','KOR','410',''),(123,'Kuwait','KW','KWT','414','State of Kuwait'),(124,'Kyrgyzstan','KG','KGZ','417','Kyrgyz Republic'),(125,'Lao People\'s Democratic Republic','LA','LAO','418',''),(126,'Latvia','LV','LVA','428','Republic of Latvia'),(127,'Lebanon','LB','LBN','422','Lebanese Republic'),(128,'Lesotho','LS','LSO','426','Kingdom of Lesotho'),(129,'Liberia','LR','LBR','430','Republic of Liberia'),(130,'Libyan Arab Jamahiriya','LY','LBY','434','Socialist People\'s Libyan Arab Jamahiriya'),(131,'Liechtenstein','LI','LIE','438','Principality of Liechtenstein'),(132,'Lithuania','LT','LTU','440','Republic of Lithuania'),(133,'Luxembourg','LU','LUX','442','Grand Duchy of Luxembourg'),(134,'Macao','MO','MAC','446','Macao Special Administrative Region of China'),(135,'Macedonia, Republic of','MK','MKD','807','The Former Yugoslav Republic of Macedonia'),(136,'Madagascar','MG','MDG','450','Republic of Madagascar'),(137,'Malawi','MW','MWI','454','Republic of Malawi'),(138,'Malaysia','MY','MYS','458',''),(139,'Maldives','MV','MDV','462','Republic of Maldives'),(140,'Mali','ML','MLI','466','Republic of Mali'),(141,'Malta','MT','MLT','470','Republic of Malta'),(142,'Marshall Islands','MH','MHL','584','Republic of the Marshall Islands'),(143,'Martinique','MQ','MTQ','474',''),(144,'Mauritania','MR','MRT','478','Islamic Republic of Mauritania'),(145,'Mauritius','MU','MUS','480','Republic of Mauritius'),(146,'Mayotte','YT','MYT','175',''),(147,'Mexico','MX','MEX','484','United Mexican States'),(148,'Micronesia, Federated States of','FM','FSM','583','Federated States of Micronesia'),(149,'Moldova','MD','MDA','498','Republic of Moldova'),(150,'Monaco','MC','MCO','492','Principality of Monaco'),(151,'Mongolia','MN','MNG','496',''),(152,'Montenegro','ME','MNE','499','Montenegro'),(153,'Montserrat','MS','MSR','500',''),(154,'Morocco','MA','MAR','504','Kingdom of Morocco'),(155,'Mozambique','MZ','MOZ','508','Republic of Mozambique'),(156,'Myanmar','MM','MMR','104','Union of Myanmar'),(157,'Namibia','NA','NAM','516','Republic of Namibia'),(158,'Nauru','NR','NRU','520','Republic of Nauru'),(159,'Nepal','NP','NPL','524','Kingdom of Nepal'),(160,'Netherlands','NL','NLD','528','Kingdom of the Netherlands'),(161,'New Caledonia','NC','NCL','540',''),(162,'New Zealand','NZ','NZL','554',''),(163,'Nicaragua','NI','NIC','558','Republic of Nicaragua'),(164,'Niger','NE','NER','562','Republic of the Niger'),(165,'Nigeria','NG','NGA','566','Federal Republic of Nigeria'),(166,'Niue','NU','NIU','570','Republic of Niue'),(167,'Norfolk Island','NF','NFK','574',''),(168,'Northern Mariana Islands','MP','MNP','580','Commonwealth of the Northern Mariana Islands'),(169,'Norway','NO','NOR','578','Kingdom of Norway'),(170,'Oman','OM','OMN','512','Sultanate of Oman'),(171,'Pakistan','PK','PAK','586','Islamic Republic of Pakistan'),(172,'Palau','PW','PLW','585','Republic of Palau'),(173,'Palestinian Territory, Occupied','PS','PSE','275','Occupied Palestinian Territory'),(174,'Panama','PA','PAN','591','Republic of Panama'),(175,'Papua New Guinea','PG','PNG','598',''),(176,'Paraguay','PY','PRY','600','Republic of Paraguay'),(177,'Peru','PE','PER','604','Republic of Peru'),(178,'Philippines','PH','PHL','608','Republic of the Philippines'),(179,'Pitcairn','PN','PCN','612',''),(180,'Poland','PL','POL','616','Republic of Poland'),(181,'Portugal','PT','PRT','620','Portuguese Republic'),(182,'Puerto Rico','PR','PRI','630',''),(183,'Qatar','QA','QAT','634','State of Qatar'),(184,'Reunion','RE','REU','638',''),(185,'Romania','RO','ROU','642',''),(186,'Russian Federation','RU','RUS','643',''),(187,'Rwanda','RW','RWA','646','Rwandese Republic'),(188,'Saint Barthélemy','BL','BLM','652',''),(189,'Saint Helena','SH','SHN','654','SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA'),(190,'Saint Kitts and Nevis','KN','KNA','659',''),(191,'Saint Lucia','LC','LCA','662',''),(192,'Saint Martin (French part)','MF','MAF','663',''),(193,'Saint Pierre and Miquelon','PM','SPM','666',''),(194,'Saint Vincent and the Grenadines','VC','VCT','670',''),(195,'Samoa','WS','WSM','882','Independent State of Samoa'),(196,'San Marino','SM','SMR','674','Republic of San Marino'),(197,'Sao Tome and Principe','ST','STP','678','Democratic Republic of Sao Tome and Principe'),(198,'Saudi Arabia','SA','SAU','682','Kingdom of Saudi Arabia'),(199,'Senegal','SN','SEN','686','Republic of Senegal'),(200,'Serbia','RS','SRB','688','Republic of Serbia'),(201,'Seychelles','SC','SYC','690','Republic of Seychelles'),(202,'Sierra Leone','SL','SLE','694','Republic of Sierra Leone'),(203,'Singapore','SG','SGP','702','Republic of Singapore'),(204,'SINT MAARTEN (DUTCH PART)','SX','','',''),(205,'Slovakia','SK','SVK','703','Slovak Republic'),(206,'Slovenia','SI','SVN','705','Republic of Slovenia'),(207,'Solomon Islands','SB','SLB','090',''),(208,'Somalia','SO','SOM','706','Somali Republic'),(209,'South Africa','ZA','ZAF','710','Republic of South Africa'),(210,'South Georgia and the South Sandwich Islands','GS','SGS','239',''),(211,'Spain','ES','ESP','724','Kingdom of Spain'),(212,'Sri Lanka','LK','LKA','144','Democratic Socialist Republic of Sri Lanka'),(213,'Sudan','SD','SDN','736','Republic of the Sudan'),(214,'Suriname','SR','SUR','740','Republic of Suriname'),(215,'Svalbard and Jan Mayen','SJ','SJM','744',''),(216,'Swaziland','SZ','SWZ','748','Kingdom of Swaziland'),(217,'Sweden','SE','SWE','752','Kingdom of Sweden'),(218,'Switzerland','CH','CHE','756','Swiss Confederation'),(219,'Syrian Arab Republic','SY','SYR','760',''),(220,'Taiwan, Province of China','TW','TWN','158','Taiwan, Province of China'),(221,'Tajikistan','TJ','TJK','762','Republic of Tajikistan'),(222,'Tanzania, United Republic of','TZ','TZA','834','United Republic of Tanzania'),(223,'Thailand','TH','THA','764','Kingdom of Thailand'),(224,'Timor-Leste','TL','TLS','626','Democratic Republic of Timor-Leste'),(225,'Togo','TG','TGO','768','Togolese Republic'),(226,'Tokelau','TK','TKL','772',''),(227,'Tonga','TO','TON','776','Kingdom of Tonga'),(228,'Trinidad and Tobago','TT','TTO','780','Republic of Trinidad and Tobago'),(229,'Tunisia','TN','TUN','788','Republic of Tunisia'),(230,'Turkey','TR','TUR','792','Republic of Turkey'),(231,'Turkmenistan','TM','TKM','795',''),(232,'Turks and Caicos Islands','TC','TCA','796',''),(233,'Tuvalu','TV','TUV','798',''),(234,'Uganda','UG','UGA','800','Republic of Uganda'),(235,'Ukraine','UA','UKR','804',''),(236,'United Arab Emirates','AE','ARE','784',''),(238,'United States','US','USA','840','United States of America'),(239,'United States Minor Outlying Islands','UM','UMI','581',''),(240,'Uruguay','UY','URY','858','Eastern Republic of Uruguay'),(241,'Uzbekistan','UZ','UZB','860','Republic of Uzbekistan'),(242,'Vanuatu','VU','VUT','548','Republic of Vanuatu'),(243,'VATICAN CITY STATE','VA','','',''),(244,'Venezuela','VE','VEN','862','Bolivarian Republic of Venezuela'),(245,'Viet Nam','VN','VNM','704','Socialist Republic of Viet Nam'),(246,'Virgin Islands, British','VG','VGB','092','British Virgin Islands'),(247,'Virgin Islands, U.S.','VI','VIR','850','Virgin Islands of the United States'),(248,'Wallis and Futuna','WF','WLF','876',''),(249,'Western Sahara','EH','ESH','732',''),(250,'Yemen','YE','YEM','887','Republic of Yemen'),(251,'Zambia','ZM','ZMB','894','Republic of Zambia'),(252,'Zimbabwe','ZW','ZWE','716','Republic of Zimbabwe');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` (`ID`, `COUNTRY_ID`, `TYPE`, `NAME`, `CODE`, `SHORT_NAME`) VALUES (1,1,'State','Western Australia','AU-WA','WA'),(2,1,'State','New South Wales','AU-NSW','NSW'),(3,1,'State','Victoria','AU-VIC','VIC'),(4,1,'Territory','Australian Capital Territory','AU-ACT','ACT'),(5,1,'Territory','Northern Territory','AU-NT','NT'),(6,1,'State','Queensland','AU-QLD','QLD'),(10,1,'State','Tasmania','AU-TAS','TAS'),(11,1,'State','South Australia','AU-SA','SA'),(18578,9,'Parish','Andorra la Vella','AD-07',''),(18579,9,'Parish','Canillo','AD-02',''),(18580,9,'Parish','Encamp','AD-03',''),(18581,9,'Parish','Escaldes-Engordany','AD-08',''),(18582,9,'Parish','La Massana','AD-04',''),(18583,9,'Parish','Ordino','AD-05',''),(18584,9,'Parish','Sant Julià de Lòria','AD-06',''),(18585,236,'Emirate','Ab? ?aby [Abu Dhabi]','AE-AZ',''),(18586,236,'Emirate','\'Ajm?n','AE-AJ',''),(18587,236,'Emirate','Al Fujayrah','AE-FU',''),(18588,236,'Emirate','Ash Sh?riqah','AE-SH',''),(18589,236,'Emirate','Dubayy','AE-DU',''),(18590,236,'Emirate','Ra’s al Khaymah','AE-RK',''),(18591,236,'Emirate','Umm al Qaywayn','AE-UQ',''),(18592,4,'Province','Badakhsh?n','AF-BDS',''),(18593,4,'Province','B?dgh?s','AF-BDG',''),(18594,4,'Province','Baghl?n','AF-BGL',''),(18595,4,'Province','Balkh','AF-BAL',''),(18596,4,'Province','B?m??n','AF-BAM',''),(18597,4,'Province','D?ykond?','AF-DAY',''),(18598,4,'Province','Far?h','AF-FRA',''),(18599,4,'Province','F?ry?b','AF-FYB',''),(18600,4,'Province','Ghazn?','AF-GHA',''),(18601,4,'Province','Ghowr','AF-GHO',''),(18602,4,'Province','Helmand','AF-HEL',''),(18603,4,'Province','Her?t','AF-HER',''),(18604,4,'Province','Jowzj?n','AF-JOW',''),(18605,4,'Province','K?bul [K?bol]','AF-KAB',''),(18606,4,'Province','Kandah?r','AF-KAN',''),(18607,4,'Province','K?p?s?','AF-KAP',''),(18608,4,'Province','Khowst','AF-KHO',''),(18609,4,'Province','Konar [Kunar]','AF-KNR',''),(18610,4,'Province','Kondoz [Kunduz]','AF-KDZ',''),(18611,4,'Province','Laghm?n','AF-LAG',''),(18612,4,'Province','Lowgar','AF-LOW',''),(18613,4,'Province','Nangrah?r [Nangarh?r]','AF-NAN',''),(18614,4,'Province','N?mr?z','AF-NIM',''),(18615,4,'Province','N?rest?n','AF-NUR',''),(18616,4,'Province','Or?zg?n [Ur?zg?n]','AF-ORU',''),(18617,4,'Province','Panjsh?r','AF-PAN',''),(18618,4,'Province','Pakt??','AF-PIA',''),(18619,4,'Province','Pakt?k?','AF-PKA',''),(18620,4,'Province','Parw?n','AF-PAR',''),(18621,4,'Province','Samang?n','AF-SAM',''),(18622,4,'Province','Sar-e Pol','AF-SAR',''),(18623,4,'Province','Takh?r','AF-TAK',''),(18624,4,'Province','Wardak [Wardag]','AF-WAR',''),(18625,4,'Province','Z?bol [Z?bul]','AF-ZAB',''),(18626,13,'Parish','Saint George','AG-03',''),(18627,13,'Parish','Saint John','AG-04',''),(18628,13,'Parish','Saint Mary','AG-05',''),(18629,13,'Parish','Saint Paul','AG-06',''),(18630,13,'Parish','Saint Peter','AG-07',''),(18631,13,'Parish','Saint Philip','AG-08',''),(18632,13,'Dependency','Barbuda','AG-10',''),(18633,6,'County','Berat','AL 1',''),(18634,6,'County','Dibër','AL 9',''),(18635,6,'County','Durrës','AL 2',''),(18636,6,'County','Elbasan','AL 3',''),(18637,6,'County','Fier','AL 4',''),(18638,6,'County','Gjirokastër','AL 5',''),(18639,6,'County','Korçë','AL 6',''),(18640,6,'County','Kukës','AL 7',''),(18641,6,'County','Lezhë','AL 8',''),(18642,6,'County','Shkodër','AL 10',''),(18643,6,'County','Tiranë','AL 11',''),(18644,6,'County','Vlorë','AL 12',''),(18645,6,'District','Berat','AL-BR',''),(18646,6,'District','Bulqizë','AL-BU',''),(18647,6,'District','Delvinë','AL-DL',''),(18648,6,'District','Devoll','AL-DV',''),(18649,6,'District','Dibër','AL-DI',''),(18650,6,'District','Durrës','AL-DR',''),(18651,6,'District','Elbasan','AL-EL',''),(18652,6,'District','Fier','AL-FR',''),(18653,6,'District','Gramsh','AL-GR',''),(18654,6,'District','Gjirokastër','AL-GJ',''),(18655,6,'District','Has','AL-HA',''),(18656,6,'District','Kavajë','AL-KA',''),(18657,6,'District','Kolonjë','AL-ER',''),(18658,6,'District','Korçë','AL-KO',''),(18659,6,'District','Krujë','AL-KR',''),(18660,6,'District','Kuçovë','AL-KC',''),(18661,6,'District','Kukës','AL-KU',''),(18662,6,'District','Kurbin','AL-KB',''),(18663,6,'District','Lezhë','AL-LE',''),(18664,6,'District','Librazhd','AL-LB',''),(18665,6,'District','Lushnjë','AL-LU',''),(18666,6,'District','Malësi e Madhe','AL-MM',''),(18667,6,'District','Mallakastër','AL-MK',''),(18668,6,'District','Mat','AL-MT',''),(18669,6,'District','Mirditë','AL-MR',''),(18670,6,'District','Peqin','AL-PQ',''),(18671,6,'District','Përmet','AL-PR',''),(18672,6,'District','Pogradec','AL-PG',''),(18673,6,'District','Pukë','AL-PU',''),(18674,6,'District','Sarandë','AL-SR',''),(18675,6,'District','Skrapar','AL-SK',''),(18676,6,'District','Shkodër','AL-SH',''),(18677,6,'District','Tepelenë','AL-TE',''),(18678,6,'District','Tiranë','AL-TR',''),(18679,6,'District','Tropojë','AL-TP',''),(18680,6,'District','Vlorë','AL-VL',''),(18681,15,'Province','Erevan','AM-ER',''),(18682,15,'Province','Aragacotn','AM-AG',''),(18683,15,'Province','Ararat','AM-AR',''),(18684,15,'Province','Armavir','AM-AV',''),(18685,15,'Province','Gegarkunik\'','AM-GR',''),(18686,15,'Province','Kotayk\'','AM-KT',''),(18687,15,'Province','Lory','AM-LO',''),(18688,15,'Province','Sirak','AM-SH',''),(18689,15,'Province','Syunik\'','AM-SU',''),(18690,15,'Province','Tavus','AM-TV',''),(18691,15,'Province','Vayoc Jor','AM-VD',''),(18692,10,'Province','Bengo','AO-BGO',''),(18693,10,'Province','Benguela','AO-BGU',''),(18694,10,'Province','Bié','AO-BIE',''),(18695,10,'Province','Cabinda','AO-CAB',''),(18696,10,'Province','Cuando-Cubango','AO-CCU',''),(18697,10,'Province','Cuanza Norte','AO-CNO',''),(18698,10,'Province','Cuanza Sul','AO-CUS',''),(18699,10,'Province','Cunene','AO-CNN',''),(18700,10,'Province','Huambo','AO-HUA',''),(18701,10,'Province','Huíla','AO-HUI',''),(18702,10,'Province','Luanda','AO-LUA',''),(18703,10,'Province','Lunda Norte','AO-LNO',''),(18704,10,'Province','Lunda Sul','AO-LSU',''),(18705,10,'Province','Malange','AO-MAL',''),(18706,10,'Province','Moxico','AO-MOX',''),(18707,10,'Province','Namibe','AO-NAM',''),(18708,10,'Province','Uíge','AO-UIG',''),(18709,10,'Province','Zaire','AO-ZAI',''),(18710,14,'Province','Capital federal','AR-C',''),(18711,14,'Province','Buenos Aires','AR-B',''),(18712,14,'Province','Catamarca','AR-K',''),(18713,14,'Province','Cordoba','AR-X',''),(18714,14,'Province','Corrientes','AR-W',''),(18715,14,'Province','Chaco','AR-H',''),(18716,14,'Province','Chubut','AR-U',''),(18717,14,'Province','Entre Rios','AR-E',''),(18718,14,'Province','Formosa','AR-P',''),(18719,14,'Province','Jujuy','AR-Y',''),(18720,14,'Province','La Pampa','AR-L',''),(18721,14,'Province','Mendoza','AR-M',''),(18722,14,'Province','Misiones','AR-N',''),(18723,14,'Province','Neuquen','AR-Q',''),(18724,14,'Province','Rio Negro','AR-R',''),(18725,14,'Province','Salta','AR-A',''),(18726,14,'Province','San Juan','AR-J',''),(18727,14,'Province','San Luis','AR-D',''),(18728,14,'Province','Santa Cruz','AR-Z',''),(18729,14,'Province','Santa Fe','AR-S',''),(18730,14,'Province','Santiago del Estero','AR-G',''),(18731,14,'Province','Tierra del Fuego','AR-V',''),(18732,14,'Province','Tucuman','AR-T',''),(18733,18,'State','Burgenland','AT-1',''),(18734,18,'State','Kärnten','AT-2',''),(18735,18,'State','Niederösterreich','AT-3',''),(18736,18,'State','Oberösterreich','AT-4',''),(18737,18,'State','Salzburg','AT-5',''),(18738,18,'State','Steiermark','AT-6',''),(18739,18,'State','Tirol','AT-7',''),(18740,18,'State','Vorarlberg','AT-8',''),(18741,18,'State','Wien','AT-9',''),(18750,19,'Autonomous republic','Naxç?van','AZ NX',''),(18751,19,'City','?li Bayraml?','AZ-AB',''),(18752,19,'City','Bak?','AZ-BA',''),(18753,19,'City','G?nc?','AZ-GA',''),(18754,19,'City','L?nk?ran','AZ-LA',''),(18755,19,'City','Ming?çevir','AZ-MI',''),(18756,19,'City','Naftalan','AZ-NA',''),(18757,19,'City','??ki','AZ-SA',''),(18758,19,'City','Sumqay?t','AZ-SM',''),(18759,19,'City','?u?a','AZ-SS',''),(18760,19,'City','Xank?ndi','AZ-XA',''),(18761,19,'City','Yevlax','AZ-YE',''),(18762,19,'Rayon','Ab?eron','AZ-ABS',''),(18763,19,'Rayon','A?cab?di','AZ-AGC',''),(18764,19,'Rayon','A?dam','AZ-AGM',''),(18765,19,'Rayon','A?da?','AZ-AGS',''),(18766,19,'Rayon','A?stafa','AZ-AGA',''),(18767,19,'Rayon','A?su','AZ-AGU',''),(18768,19,'Rayon','Astara','AZ-AST',''),(18769,19,'Rayon','Bab?k','AZ-BAB',''),(18770,19,'Rayon','Balak?n','AZ-BAL',''),(18771,19,'Rayon','B?rd?','AZ-BAR',''),(18772,19,'Rayon','Beyl?qan','AZ-BEY',''),(18773,19,'Rayon','Bil?suvar','AZ-BIL',''),(18774,19,'Rayon','C?bray?l','AZ-CAB',''),(18775,19,'Rayon','C?lilabab','AZ-CAL',''),(18776,19,'Rayon','Culfa','AZ-CUL',''),(18777,19,'Rayon','Da?k?s?n','AZ-DAS',''),(18778,19,'Rayon','D?v?çi','AZ-DAV',''),(18779,19,'Rayon','Füzuli','AZ-FUZ',''),(18780,19,'Rayon','G?d?b?y','AZ-GAD',''),(18781,19,'Rayon','Goranboy','AZ-GOR',''),(18782,19,'Rayon','Göyçay','AZ-GOY',''),(18783,19,'Rayon','Hac?qabul','AZ-HAC',''),(18784,19,'Rayon','?mi?li','AZ-IMI',''),(18785,19,'Rayon','?smay?ll?','AZ-ISM',''),(18786,19,'Rayon','K?lb?c?r','AZ-KAL',''),(18787,19,'Rayon','Kürd?mir','AZ-KUR',''),(18788,19,'Rayon','Laç?n','AZ-LAC',''),(18789,19,'Rayon','L?nk?ran','AZ-LAN',''),(18790,19,'Rayon','Lerik','AZ-LER',''),(18791,19,'Rayon','Masall?','AZ-MAS',''),(18792,19,'Rayon','Neftçala','AZ-NEF',''),(18793,19,'Rayon','O?uz','AZ-OGU',''),(18794,19,'Rayon','Ordubad','AZ-ORD',''),(18795,19,'Rayon','Q?b?l?','AZ-QAB',''),(18796,19,'Rayon','Qax','AZ-QAX',''),(18797,19,'Rayon','Qazax','AZ-QAZ',''),(18798,19,'Rayon','Qobustan','AZ-QOB',''),(18799,19,'Rayon','Quba','AZ-QBA',''),(18800,19,'Rayon','Qubadl?','AZ-QBI',''),(18801,19,'Rayon','Qusar','AZ-QUS',''),(18802,19,'Rayon','Saatl?','AZ-SAT',''),(18803,19,'Rayon','Sabirabad','AZ-SAB',''),(18804,19,'Rayon','S?d?r?k','AZ-SAD',''),(18805,19,'Rayon','?ahbuz','AZ-SAH',''),(18806,19,'Rayon','??ki','AZ-SAK',''),(18807,19,'Rayon','Salyan','AZ-SAL',''),(18808,19,'Rayon','?amax?','AZ-SMI',''),(18809,19,'Rayon','??mkir','AZ-SKR',''),(18810,19,'Rayon','Samux','AZ-SMX',''),(18811,19,'Rayon','??rur','AZ-SAR',''),(18812,19,'Rayon','Siy?z?n','AZ-SIY',''),(18813,19,'Rayon','?u?a','AZ-SUS',''),(18814,19,'Rayon','T?rt?r','AZ-TAR',''),(18815,19,'Rayon','Tovuz','AZ-TOV',''),(18816,19,'Rayon','Ucar','AZ-UCA',''),(18817,19,'Rayon','Xaçmaz','AZ-XAC',''),(18818,19,'Rayon','Xanlar','AZ-XAN',''),(18819,19,'Rayon','X?z?','AZ-XIZ',''),(18820,19,'Rayon','Xocal?','AZ-XCI',''),(18821,19,'Rayon','Xocav?nd','AZ-XVD',''),(18822,19,'Rayon','Yard?ml?','AZ-YAR',''),(18823,19,'Rayon','Yevlax','AZ-YEV',''),(18824,19,'Rayon','Z?ngilan','AZ-ZAN',''),(18825,19,'Rayon','Zaqatala','AZ-ZAQ',''),(18826,19,'Rayon','Z?rdab','AZ-ZAR',''),(18827,32,'Entity','Federacija Bosna i Hercegovina','BA-BIH',''),(18828,32,'Entity','Republika Srpska','BA-SRP',''),(18829,23,'Parish','Christ Church','BB-01',''),(18830,23,'Parish','Saint Andrew','BB-02',''),(18831,23,'Parish','Saint George','BB-03',''),(18832,23,'Parish','Saint James','BB-04',''),(18833,23,'Parish','Saint John','BB-05',''),(18834,23,'Parish','Saint Joseph','BB-06',''),(18835,23,'Parish','Saint Lucy','BB-07',''),(18836,23,'Parish','Saint Michael','BB-08',''),(18837,23,'Parish','Saint Peter','BB-09',''),(18838,23,'Parish','Saint Philip','BB-10',''),(18839,23,'Parish','Saint Thomas','BB-11',''),(18840,22,'Division','Barisal bibhag','BD 1',''),(18841,22,'Division','Chittagong bibhag','BD 2',''),(18842,22,'Division','Dhaka bibhag','BD 3',''),(18843,22,'Division','Khulna bibhag','BD 4',''),(18844,22,'Division','Rajshahi bibhag','BD 5',''),(18845,22,'Division','Sylhet bibhag','BD 6',''),(18846,22,'District','Bagerhat zila','BD-05',''),(18847,22,'District','Bandarban zila','BD-01',''),(18848,22,'District','Barguna zila','BD-02',''),(18849,22,'District','Barisal zila','BD-06',''),(18850,22,'District','Bhola zila','BD-07',''),(18851,22,'District','Bogra zila','BD-03',''),(18852,22,'District','Brahmanbaria zila','BD-04',''),(18853,22,'District','Chandpur zila','BD-09',''),(18854,22,'District','Chittagong zila','BD-10',''),(18855,22,'District','Chuadanga zila','BD-12',''),(18856,22,'District','Comilla zila','BD-08',''),(18857,22,'District','Cox\'s Bazar zila','BD-11',''),(18858,22,'District','Dhaka zila','BD-13',''),(18859,22,'District','Dinajpur zila','BD-14',''),(18860,22,'District','Faridpur zila','BD-15',''),(18861,22,'District','Feni zila','BD-16',''),(18862,22,'District','Gaibandha zila','BD-19',''),(18863,22,'District','Gazipur zila','BD-18',''),(18864,22,'District','Gopalganj zila','BD-17',''),(18865,22,'District','Habiganj zila','BD-20',''),(18866,22,'District','Jaipurhat zila','BD-24',''),(18867,22,'District','Jamalpur zila','BD-21',''),(18868,22,'District','Jessore zila','BD-22',''),(18869,22,'District','Jhalakati zila','BD-25',''),(18870,22,'District','Jhenaidah zila','BD-23',''),(18871,22,'District','Khagrachari zila','BD-29',''),(18872,22,'District','Khulna zila','BD-27',''),(18873,22,'District','Kishorganj zila','BD-26',''),(18874,22,'District','Kurigram zila','BD-28',''),(18875,22,'District','Kushtia zila','BD-30',''),(18876,22,'District','Lakshmipur zila','BD-31',''),(18877,22,'District','Lalmonirhat zila','BD-32',''),(18878,22,'District','Madaripur zila','BD-36',''),(18879,22,'District','Magura zila','BD-37',''),(18880,22,'District','Manikganj zila','BD-33',''),(18881,22,'District','Meherpur zila','BD-39',''),(18882,22,'District','Moulvibazar zila','BD-38',''),(18883,22,'District','Munshiganj zila','BD-35',''),(18884,22,'District','Mymensingh zila','BD-34',''),(18885,22,'District','Naogaon zila','BD-48',''),(18886,22,'District','Narail zila','BD-43',''),(18887,22,'District','Narayanganj zila','BD-40',''),(18888,22,'District','Narsingdi zila','BD-42',''),(18889,22,'District','Natore zila','BD-44',''),(18890,22,'District','Nawabganj zila','BD-45',''),(18891,22,'District','Netrakona zila','BD-41',''),(18892,22,'District','Nilphamari zila','BD-46',''),(18893,22,'District','Noakhali zila','BD-47',''),(18894,22,'District','Pabna zila','BD-49',''),(18895,22,'District','Panchagarh zila','BD-52',''),(18896,22,'District','Patuakhali zila','BD-51',''),(18897,22,'District','Pirojpur zila','BD-50',''),(18898,22,'District','Rajbari zila','BD-53',''),(18899,22,'District','Rajshahi zila','BD-54',''),(18900,22,'District','Rangamati zila','BD-56',''),(18901,22,'District','Rangpur zila','BD-55',''),(18902,22,'District','Satkhira zila','BD-58',''),(18903,22,'District','Shariatpur zila','BD-62',''),(18904,22,'District','Sherpur zila','BD-57',''),(18905,22,'District','Sirajganj zila','BD-59',''),(18906,22,'District','Sunamganj zila','BD-61',''),(18907,22,'District','Sylhet zila','BD-60',''),(18908,22,'District','Tangail zila','BD-63',''),(18909,22,'District','Thakurgaon zila','BD-64',''),(18910,25,'Province','Antwerpen','BE-VAN',''),(18911,25,'Province','Brabant Wallon','BE-WBR',''),(18912,25,'Province','Brussels-Capital Region','BE-BRU',''),(18913,25,'Province','Hainaut','BE-WHT',''),(18914,25,'Province','Liege','BE-WLG',''),(18915,25,'Province','Limburg','BE-VLI',''),(18916,25,'Province','Luxembourg','BE-WLX',''),(18917,25,'Province','Namur','BE-WNA',''),(18918,25,'Province','Oost-Vlaanderen','BE-VOV',''),(18919,25,'Province','Vlaams-Brabant','BE-VBR',''),(18920,25,'Province','West-Vlaanderen','BE-VWV',''),(18921,39,'Province','Balé','BF-BAL',''),(18922,39,'Province','Bam','BF-BAM',''),(18923,39,'Province','Banwa','BF-BAN',''),(18924,39,'Province','Bazèga','BF-BAZ',''),(18925,39,'Province','Bougouriba','BF-BGR',''),(18926,39,'Province','Boulgou','BF-BLG',''),(18927,39,'Province','Boulkiemdé','BF-BLK',''),(18928,39,'Province','Comoé','BF-COM',''),(18929,39,'Province','Ganzourgou','BF-GAN',''),(18930,39,'Province','Gnagna','BF-GNA',''),(18931,39,'Province','Gourma','BF-GOU',''),(18932,39,'Province','Houet','BF-HOU',''),(18933,39,'Province','Ioba','BF-IOB',''),(18934,39,'Province','Kadiogo','BF-KAD',''),(18935,39,'Province','Kénédougou','BF-KEN',''),(18936,39,'Province','Komondjari','BF-KMD',''),(18937,39,'Province','Kompienga','BF-KMP',''),(18938,39,'Province','Kossi','BF-KOS',''),(18939,39,'Province','Koulpélogo','BF-KOP',''),(18940,39,'Province','Kouritenga','BF-KOT',''),(18941,39,'Province','Kourwéogo','BF-KOW',''),(18942,39,'Province','Léraba','BF-LER',''),(18943,39,'Province','Loroum','BF-LOR',''),(18944,39,'Province','Mouhoun','BF-MOU',''),(18945,39,'Province','Naouri','BF-NAO',''),(18946,39,'Province','Namentenga','BF-NAM',''),(18947,39,'Province','Nayala','BF-NAY',''),(18948,39,'Province','Noumbiel','BF-NOU',''),(18949,39,'Province','Oubritenga','BF-OUB',''),(18950,39,'Province','Oudalan','BF-OUD',''),(18951,39,'Province','Passoré','BF-PAS',''),(18952,39,'Province','Poni','BF-PON',''),(18953,39,'Province','Sanguié','BF-SNG',''),(18954,39,'Province','Sanmatenga','BF-SMT',''),(18955,39,'Province','Séno','BF-SEN',''),(18956,39,'Province','Sissili','BF-SIS',''),(18957,39,'Province','Soum','BF-SOM',''),(18958,39,'Province','Sourou','BF-SOR',''),(18959,39,'Province','Tapoa','BF-TAP',''),(18960,39,'Province','Tui','BF-TUI',''),(18961,39,'Province','Yagha','BF-YAG',''),(18962,39,'Province','Yatenga','BF-YAT',''),(18963,39,'Province','Ziro','BF-ZIR',''),(18964,39,'Province','Zondoma','BF-ZON',''),(18965,39,'Province','Zoundwéogo','BF-ZOU',''),(18966,38,'Region','Blagoevgrad','BG-01',''),(18967,38,'Region','Burgas','BG-02',''),(18968,38,'Region','Dobrich','BG-08',''),(18969,38,'Region','Gabrovo','BG-07',''),(18970,38,'Region','Haskovo','BG-26',''),(18971,38,'Region','Kardzhali','BG-09',''),(18972,38,'Region','Kyustendil','BG-10',''),(18973,38,'Region','Lovech','BG-11',''),(18974,38,'Region','Montana','BG-12',''),(18975,38,'Region','Pazardzhik','BG-13',''),(18976,38,'Region','Pernik','BG-14',''),(18977,38,'Region','Pleven','BG-15',''),(18978,38,'Region','Plovdiv','BG-16',''),(18979,38,'Region','Razgrad','BG-17',''),(18980,38,'Region','Ruse','BG-18',''),(18981,38,'Region','Shumen','BG-27',''),(18982,38,'Region','Silistra','BG-19',''),(18983,38,'Region','Sliven','BG-20',''),(18984,38,'Region','Smolyan','BG-21',''),(18985,38,'Region','Sofia','BG-23',''),(18986,38,'Region','Sofia-Grad','BG-22',''),(18987,38,'Region','Stara Zagora','BG-24',''),(18988,38,'Region','Targovishte','BG-25',''),(18989,38,'Region','Varna','BG-03',''),(18990,38,'Region','Veliko Tarnovo','BG-04',''),(18991,38,'Region','Vidin','BG-05',''),(18992,38,'Region','Vratsa','BG-06',''),(18993,38,'Region','Yambol','BG-28',''),(18994,21,'Governorate','Al Man?mah (Al ‘??imah)','BH-13',''),(18995,21,'Governorate','Al Jan?b?yah','BH-14',''),(18996,21,'Governorate','Al Mu?arraq','BH-15',''),(18997,21,'Governorate','Al Wus?á','BH-16',''),(18998,21,'Governorate','Ash Sham?l?yah','BH-17',''),(18999,40,'Province','Bubanza','BI-BB',''),(19000,40,'Province','Bujumbura','BI-BJ',''),(19001,40,'Province','Bururi','BI-BR',''),(19002,40,'Province','Cankuzo','BI-CA',''),(19003,40,'Province','Cibitoke','BI-CI',''),(19004,40,'Province','Gitega','BI-GI',''),(19005,40,'Province','Karuzi','BI-KR',''),(19006,40,'Province','Kayanza','BI-KY',''),(19007,40,'Province','Kirundo','BI-KI',''),(19008,40,'Province','Makamba','BI-MA',''),(19009,40,'Province','Muramvya','BI-MU',''),(19010,40,'Province','Mwaro','BI-MW',''),(19011,40,'Province','Ngozi','BI-NG',''),(19012,40,'Province','Rutana','BI-RT',''),(19013,40,'Province','Ruyigi','BI-RY',''),(19014,27,'Department','Alibori','BJ-AL',''),(19015,27,'Department','Atakora','BJ-AK',''),(19016,27,'Department','Atlantique','BJ-AQ',''),(19017,27,'Department','Borgou','BJ-BO',''),(19018,27,'Department','Collines','BJ-CO',''),(19019,27,'Department','Donga','BJ-DO',''),(19020,27,'Department','Kouffo','BJ-KO',''),(19021,27,'Department','Littoral','BJ-LI',''),(19022,27,'Department','Mono','BJ-MO',''),(19023,27,'Department','Ouémé','BJ-OU',''),(19024,27,'Department','Plateau','BJ-PL',''),(19025,27,'Department','Zou','BJ-ZO',''),(19026,37,'District','Belait','BN-BE',''),(19027,37,'District','Brunei-Muara','BN-BM',''),(19028,37,'District','Temburong','BN-TE',''),(19029,37,'District','Tutong','BN-TU',''),(19030,30,'Department','Cochabamba','BO-C',''),(19031,30,'Department','Chuquisaca','BO-H',''),(19032,30,'Department','El Beni','BO-B',''),(19033,30,'Department','La Paz','BO-L',''),(19034,30,'Department','Oruro','BO-O',''),(19035,30,'Department','Pando','BO-N',''),(19036,30,'Department','Potosí','BO-P',''),(19037,30,'Department','Santa Cruz','BO-S',''),(19038,30,'Department','Tarija','BO-T',''),(19039,35,'State','Acre','BR-AC',''),(19040,35,'State','Alagoas','BR-AL',''),(19041,35,'State','Amazonas','BR-AM',''),(19042,35,'State','Amapá','BR-AP',''),(19043,35,'State','Bahia','BR-BA',''),(19044,35,'State','Ceará','BR-CE',''),(19045,35,'State','Espírito Santo','BR-ES',''),(19046,35,'State','Fernando de Noronha','BR-FN',''),(19047,35,'State','Goiás','BR-GO',''),(19048,35,'State','Maranhão','BR-MA',''),(19049,35,'State','Minas Gerais','BR-MG',''),(19050,35,'State','Mato Grosso do Sul','BR-MS',''),(19051,35,'State','Mato Grosso','BR-MT',''),(19052,35,'State','Pará','BR-PA',''),(19053,35,'State','Paraíba','BR-PB',''),(19054,35,'State','Pernambuco','BR-PE',''),(19055,35,'State','Piauí','BR-PI',''),(19056,35,'State','Paraná','BR-PR',''),(19057,35,'State','Rio de Janeiro','BR-RJ',''),(19058,35,'State','Rio Grande do Norte','BR-RN',''),(19059,35,'State','Rondônia','BR-RO',''),(19060,35,'State','Roraima','BR-RR',''),(19061,35,'State','Rio Grande do Sul','BR-RS',''),(19062,35,'State','Santa Catarina','BR-SC',''),(19063,35,'State','Sergipe','BR-SE',''),(19064,35,'State','Sâo Paulo','BR-SP',''),(19065,35,'State','Tocantins','BR-TO',''),(19066,35,'Federal District','Distrito Federal','BR-DF',''),(19067,20,'District','Acklins and Crooked Islands','BS-AC',''),(19068,20,'District','Bimini','BS-BI',''),(19069,20,'District','Cat Island','BS-CI',''),(19070,20,'District','Exuma','BS-EX',''),(19071,20,'District','Freeport','BS-FP',''),(19072,20,'District','Fresh Creek','BS-FC',''),(19073,20,'District','Governor\'s Harbour','BS-GH',''),(19074,20,'District','Green Turtle Cay','BS-GT',''),(19075,20,'District','Harbour Island','BS-HI',''),(19076,20,'District','High Rock','BS-HR',''),(19077,20,'District','Inagua','BS-IN',''),(19078,20,'District','Kemps Bay','BS-KB',''),(19079,20,'District','Long Island','BS-LI',''),(19080,20,'District','Marsh Harbour','BS-MH',''),(19081,20,'District','Mayaguana','BS-MG',''),(19082,20,'District','New Providence','BS-NP',''),(19083,20,'District','Nicholls Town and Berry Islands','BS-NB',''),(19084,20,'District','Ragged Island','BS-RI',''),(19085,20,'District','Rock Sound','BS-RS',''),(19086,20,'District','Sandy Point','BS-SP',''),(19087,20,'District','San Salvador and Rum Cay','BS-SR',''),(19088,29,'District','Bumthang','BT-33',''),(19089,29,'District','Chhukha','BT-12',''),(19090,29,'District','Dagana','BT-22',''),(19091,29,'District','Gasa','BT-GA',''),(19092,29,'District','Ha','BT-13',''),(19093,29,'District','Lhuentse','BT-44',''),(19094,29,'District','Monggar','BT-42',''),(19095,29,'District','Paro','BT-11',''),(19096,29,'District','Pemagatshel','BT-43',''),(19097,29,'District','Punakha','BT-23',''),(19098,29,'District','Samdrup Jongkha','BT-45',''),(19099,29,'District','Samtee','BT-14',''),(19100,29,'District','Sarpang','BT-31',''),(19101,29,'District','Thimphu','BT-15',''),(19102,29,'District','Trashigang','BT-41',''),(19103,29,'District','Trashi Yangtse','BT-TY',''),(19104,29,'District','Trongsa','BT-32',''),(19105,29,'District','Tsirang','BT-21',''),(19106,29,'District','Wangdue Phodrang','BT-24',''),(19107,29,'District','Zhemgang','BT-34',''),(19108,33,'District','Central','BW-CE',''),(19109,33,'District','Ghanzi','BW-GH',''),(19110,33,'District','Kgalagadi','BW-KG',''),(19111,33,'District','Kgatleng','BW-KL',''),(19112,33,'District','Kweneng','BW-KW',''),(19113,33,'District','Ngamiland','BW-NG',''),(19114,33,'District','North-East','BW-NE',''),(19115,33,'District','North-West (Botswana)','BW-NW',''),(19116,33,'District','South-East','BW-SE',''),(19117,33,'District','Southern (Botswana)','BW-SO',''),(19118,24,'Oblast','Brèsckaja voblasc\'','BY-BR',''),(19119,24,'Oblast','Homel\'skaja voblasc\'','BY-HO',''),(19120,24,'Oblast','Hrodzenskaja voblasc\'','BY-HR',''),(19121,24,'Oblast','Mahilëuskaja voblasc\'','BY-MA',''),(19122,24,'Oblast','Minskaja voblasc\'','BY-MI',''),(19123,24,'Oblast','Vicebskaja voblasc\'','BY-VI',''),(19124,26,'District','Belize','BZ-BZ',''),(19125,26,'District','Cayo','BZ-CY',''),(19126,26,'District','Corozal','BZ-CZL',''),(19127,26,'District','Orange Walk','BZ-OW',''),(19128,26,'District','Stann Creek','BZ-SC',''),(19129,26,'District','Toledo','BZ-TOL',''),(19130,3,'Province','Alberta','CA-AB',''),(19131,3,'Province','British Columbia','CA-BC',''),(19132,3,'Province','Manitoba','CA-MB',''),(19133,3,'Province','New Brunswick','CA-NB',''),(19134,3,'Province','Newfoundland and Labrador','CA-NL',''),(19135,3,'Province','Nova Scotia','CA-NS',''),(19136,3,'Province','Ontario','CA-ON',''),(19137,3,'Province','Prince Edward Island','CA-PE',''),(19138,3,'Province','Quebec','CA-QC',''),(19139,3,'Province','Saskatchewan','CA-SK',''),(19140,3,'Territory','Northwest Territories','CA-NT',''),(19141,3,'Territory','Nunavut','CA-NU',''),(19142,3,'Territory','Yukon Territory','CA-YT',''),(19143,55,'City','Kinshasa','CD-KN',''),(19144,55,'Province','Bandundu','CD-BN',''),(19145,55,'Province','Bas-Congo','CD-BC',''),(19146,55,'Province','Équateur','CD-EQ',''),(19147,55,'Province','Haut-Congo','CD-HC',''),(19148,55,'Province','Kasai-Occidental','CD-KW',''),(19149,55,'Province','Kasai-Oriental','CD-KE',''),(19150,55,'Province','Katanga','CD-KA',''),(19151,55,'Province','Maniema','CD-MA',''),(19152,55,'Province','Nord-Kivu','CD-NK',''),(19153,55,'Province','Orientale','CD-OR',''),(19154,55,'Province','Sud-Kivu','CD-SK',''),(19155,46,'Prefecture','Bamingui-Bangoran','CF-BB',''),(19156,46,'Prefecture','Basse-Kotto','CF-BK',''),(19157,46,'Prefecture','Haute-Kotto','CF-HK',''),(19158,46,'Prefecture','Haut-Mbomou','CF-HM',''),(19159,46,'Prefecture','Kémo','CF-KG',''),(19160,46,'Prefecture','Lobaye','CF-LB',''),(19161,46,'Prefecture','Mambéré-Kadéï','CF-HS',''),(19162,46,'Prefecture','Mbomou','CF-MB',''),(19163,46,'Prefecture','Nana-Mambéré','CF-NM',''),(19164,46,'Prefecture','Ombella-M\'poko','CF-MP',''),(19165,46,'Prefecture','Ouaka','CF-UK',''),(19166,46,'Prefecture','Ouham','CF-AC',''),(19167,46,'Prefecture','Ouham-Pendé','CF-OP',''),(19168,46,'Prefecture','Vakaga','CF-VR',''),(19169,46,'Economic Prefecture','Nana-Grébizi','CF-KB',''),(19170,46,'Economic Prefecture','Sangha-Mbaéré','CF-SE',''),(19171,46,'Autonomous Commune','Bangui','CF-BGF',''),(19172,54,'Region','Bouenza','CG-11',''),(19173,54,'Region','Cuvette','CG-8',''),(19174,54,'Region','Cuvette-Ouest','CG-15',''),(19175,54,'Region','Kouilou','CG-5',''),(19176,54,'Region','Lékoumou','CG-2',''),(19177,54,'Region','Likouala','CG-7',''),(19178,54,'Region','Niari','CG-9',''),(19179,54,'Region','Plateaux','CG-14',''),(19180,54,'Region','Pool','CG-12',''),(19181,54,'Region','Sangha','CG-13',''),(19182,54,'Capital District','Brazzaville','CG-BZV',''),(19183,218,'Canton','Aargau','CH-AG',''),(19184,218,'Canton','Appenzell Innerrhoden','CH-AI',''),(19185,218,'Canton','Appenzell Ausserrhoden','CH-AR',''),(19186,218,'Canton','Bern','CH-BE',''),(19187,218,'Canton','Basel-Landschaft','CH-BL',''),(19188,218,'Canton','Basel-Stadt','CH-BS',''),(19189,218,'Canton','Fribourg','CH-FR',''),(19190,218,'Canton','Genève','CH-GE',''),(19191,218,'Canton','Glarus','CH-GL',''),(19192,218,'Canton','Graubünden','CH-GR',''),(19193,218,'Canton','Jura','CH-JU',''),(19194,218,'Canton','Luzern','CH-LU',''),(19195,218,'Canton','Neuchâtel','CH-NE',''),(19196,218,'Canton','Nidwalden','CH-NW',''),(19197,218,'Canton','Obwalden','CH-OW',''),(19198,218,'Canton','Sankt Gallen','CH-SG',''),(19199,218,'Canton','Schaffhausen','CH-SH',''),(19200,218,'Canton','Solothurn','CH-SO',''),(19201,218,'Canton','Schwyz','CH-SZ',''),(19202,218,'Canton','Thurgau','CH-TG',''),(19203,218,'Canton','Ticino','CH-TI',''),(19204,218,'Canton','Uri','CH-UR',''),(19205,218,'Canton','Vaud','CH-VD',''),(19206,218,'Canton','Valais','CH-VS',''),(19207,218,'Canton','Zug','CH-ZG',''),(19208,218,'Canton','Zürich','CH-ZH',''),(19209,58,'Region','18 Montagnes (Région des)','CI-06',''),(19210,58,'Region','Agnébi (Région de l\')','CI-16',''),(19211,58,'Region','Bafing (Région du)','CI-17',''),(19212,58,'Region','Bas-Sassandra (Région du)','CI-09',''),(19213,58,'Region','Denguélé (Région du)','CI-10',''),(19214,58,'Region','Fromager (Région du)','CI-18',''),(19215,58,'Region','Haut-Sassandra (Région du)','CI-02',''),(19216,58,'Region','Lacs (Région des)','CI-07',''),(19217,58,'Region','Lagunes (Région des)','CI-01',''),(19218,58,'Region','Marahoué (Région de la)','CI-12',''),(19219,58,'Region','Moyen-Cavally (Région du)','CI-19',''),(19220,58,'Region','Moyen-Comoé (Région du)','CI-05',''),(19221,58,'Region','Nzi-Comoé (Région)','CI-11',''),(19222,58,'Region','Savanes (Région des)','CI-03',''),(19223,58,'Region','Sud-Bandama (Région du)','CI-15',''),(19224,58,'Region','Sud-Comoé (Région du)','CI-13',''),(19225,58,'Region','Vallée du Bandama (Région de la)','CI-04',''),(19226,58,'Region','Worodouqou (Région du)','CI-14',''),(19227,58,'Region','Zanzan (Région du)','CI-08',''),(19228,48,'Region','Aisén del General Carlos Ibáñez del Campo','CL-AI',''),(19229,48,'Region','Antofagasta','CL-AN',''),(19230,48,'Region','Araucanía','CL-AR',''),(19231,48,'Region','Atacama','CL-AT',''),(19232,48,'Region','Bío-Bío','CL-BI',''),(19233,48,'Region','Coquimbo','CL-CO',''),(19234,48,'Region','Libertador General Bernardo O\'Higgins','CL-LI',''),(19235,48,'Region','Los Lagos','CL-LL',''),(19236,48,'Region','Magallanes y Antártica Chilena','CL-MA',''),(19237,48,'Region','Maule','CL-ML',''),(19238,48,'Region','Región Metropolitana de Santiago','CL-RM',''),(19239,48,'Region','Tarapacá','CL-TA',''),(19240,48,'Region','Valparaíso','CL-VS',''),(19241,42,'Province','Adamaoua','CM-AD',''),(19242,42,'Province','Centre','CM-CE',''),(19243,42,'Province','East','CM-ES',''),(19244,42,'Province','Far North','CM-EN',''),(19245,42,'Province','Littoral','CM-LT',''),(19246,42,'Province','North','CM-NO',''),(19247,42,'Province','North-West (Cameroon)','CM-NW',''),(19248,42,'Province','South','CM-SU',''),(19249,42,'Province','South-West','CM-SW',''),(19250,42,'Province','West','CM-OU',''),(19251,49,'Municipality','Beijing','CN-11',''),(19252,49,'Municipality','Chongqing','CN-50',''),(19253,49,'Municipality','Shanghai','CN-31',''),(19254,49,'Municipality','Tianjin','CN-12',''),(19255,49,'Province','Anhui','CN-34',''),(19256,49,'Province','Fujian','CN-35',''),(19257,49,'Province','Gansu','CN-62',''),(19258,49,'Province','Guangdong','CN-44',''),(19259,49,'Province','Guizhou','CN-52',''),(19260,49,'Province','Hainan','CN-46',''),(19261,49,'Province','Hebei','CN-13',''),(19262,49,'Province','Heilongjiang','CN-23',''),(19263,49,'Province','Henan','CN-41',''),(19264,49,'Province','Hubei','CN-42',''),(19265,49,'Province','Hunan','CN-43',''),(19266,49,'Province','Jiangsu','CN-32',''),(19267,49,'Province','Jiangxi','CN-36',''),(19268,49,'Province','Jilin','CN-22',''),(19269,49,'Province','Liaoning','CN-21',''),(19270,49,'Province','Qinghai','CN-63',''),(19271,49,'Province','Shaanxi','CN-61',''),(19272,49,'Province','Shandong','CN-37',''),(19273,49,'Province','Shanxi','CN-14',''),(19274,49,'Province','Sichuan','CN-51',''),(19275,49,'Province','Taiwan','CN-71',''),(19276,49,'Province','Yunnan','CN-53',''),(19277,49,'Province','Zhejiang','CN-33',''),(19278,49,'Autonomous region','Guangxi','CN-45',''),(19279,49,'Autonomous region','Nei Mongol','CN-15',''),(19280,49,'Autonomous region','Ningxia','CN-64',''),(19281,49,'Autonomous region','Xinjiang','CN-65',''),(19282,49,'Autonomous region','Xizang','CN-54',''),(19283,49,'Special administrative region','Xianggang (Hong-Kong)','CN-91',''),(19284,49,'Special administrative region','Aomen (Macau)','CN-92',''),(19285,52,'Capital district','Distrito Capital de Bogotá','CO-DC',''),(19286,52,'Department','Amazonas','CO-AMA',''),(19287,52,'Department','Antioquia','CO-ANT',''),(19288,52,'Department','Arauca','CO-ARA',''),(19289,52,'Department','Atlántico','CO-ATL',''),(19290,52,'Department','Bolívar','CO-BOL',''),(19291,52,'Department','Boyacá','CO-BOY',''),(19292,52,'Department','Caldas','CO-CAL',''),(19293,52,'Department','Caquetá','CO-CAQ',''),(19294,52,'Department','Casanare','CO-CAS',''),(19295,52,'Department','Cauca','CO-CAU',''),(19296,52,'Department','Cesar','CO-CES',''),(19297,52,'Department','Chocó','CO-CHO',''),(19298,52,'Department','Córdoba','CO-COR',''),(19299,52,'Department','Cundinamarca','CO-CUN',''),(19300,52,'Department','Guainía','CO-GUA',''),(19301,52,'Department','Guaviare','CO-GUV',''),(19302,52,'Department','Huila','CO-HUI',''),(19303,52,'Department','La Guajira','CO-LAG',''),(19304,52,'Department','Magdalena','CO-MAG',''),(19305,52,'Department','Meta','CO-MET',''),(19306,52,'Department','Nariño','CO-NAR',''),(19307,52,'Department','Norte de Santander','CO-NSA',''),(19308,52,'Department','Putumayo','CO-PUT',''),(19309,52,'Department','Quindío','CO-QUI',''),(19310,52,'Department','Risaralda','CO-RIS',''),(19311,52,'Department','San Andrés, Providencia y Santa Catalina','CO-SAP',''),(19312,52,'Department','Santander','CO-SAN',''),(19313,52,'Department','Sucre','CO-SUC',''),(19314,52,'Department','Tolima','CO-TOL',''),(19315,52,'Department','Valle del Cauca','CO-VAC',''),(19316,52,'Department','Vaupés','CO-VAU',''),(19317,52,'Department','Vichada','CO-VID',''),(19318,57,'Province','Alajuela','CR-A',''),(19319,57,'Province','Cartago','CR-C',''),(19320,57,'Province','Guanacaste','CR-G',''),(19321,57,'Province','Heredia','CR-H',''),(19322,57,'Province','Limón','CR-L',''),(19323,57,'Province','Puntarenas','CR-P',''),(19324,57,'Province','San José','CR-SJ',''),(19325,60,'Province','Camagüey','CU-09',''),(19326,60,'Province','Ciego de Ávila','CU-08',''),(19327,60,'Province','Cienfuegos','CU-06',''),(19328,60,'Province','Ciudad de La Habana','CU-03',''),(19329,60,'Province','Granma','CU-12',''),(19330,60,'Province','Guantánamo','CU-14',''),(19331,60,'Province','Holguín','CU-11',''),(19332,60,'Province','La Habana','CU-02',''),(19333,60,'Province','Las Tunas','CU-10',''),(19334,60,'Province','Matanzas','CU-04',''),(19335,60,'Province','Pinar del Rio','CU-01',''),(19336,60,'Province','Sancti Spíritus','CU-07',''),(19337,60,'Province','Santiago de Cuba','CU-13',''),(19338,60,'Province','Villa Clara','CU-05',''),(19339,60,'Special municipality','Isla de la Juventud','CU-99',''),(19340,44,'District','Ilhas de Barlavento','CV B',''),(19341,44,'District','Ilhas de Sotavento','CV S',''),(19342,44,'Municipality','Boa Vista','CV-BV',''),(19343,44,'Municipality','Brava','CV-BR',''),(19344,44,'Municipality','Calheta de São Miguel','CV-CS',''),(19345,44,'Municipality','Maio','CV-MA',''),(19346,44,'Municipality','Mosteiros','CV-MO',''),(19347,44,'Municipality','Paúl','CV-PA',''),(19348,44,'Municipality','Porto Novo','CV-PN',''),(19349,44,'Municipality','Praia','CV-PR',''),(19350,44,'Municipality','Ribeira Grande','CV-RG',''),(19351,44,'Municipality','Sal','CV-SL',''),(19352,44,'Municipality','Santa Catarina','CV-CA',''),(19353,44,'Municipality','Santa Cruz','CV-CR',''),(19354,44,'Municipality','São Domingos','CV-SD',''),(19355,44,'Municipality','São Filipe','CV-SF',''),(19356,44,'Municipality','São Nicolau','CV-SN',''),(19357,44,'Municipality','São Vicente','CV-SV',''),(19358,44,'Municipality','Tarrafal','CV-TA',''),(19359,62,'District','Ammóchostos','CY-04',''),(19360,62,'District','Kerýneia','CY-06',''),(19361,62,'District','Lárnaka','CY-03',''),(19362,62,'District','Lefkosía','CY-01',''),(19363,62,'District','Lemesós','CY-02',''),(19364,62,'District','Páfos','CY-05',''),(19365,63,'Region','Jiho?eský kraj','CZ JC',''),(19366,63,'Region','Jihomoravský kraj','CZ JM',''),(19367,63,'Region','Karlovarský kraj','CZ KA',''),(19368,63,'Region','Královéhradecký kraj','CZ KR',''),(19369,63,'Region','Liberecký kraj','CZ LI',''),(19370,63,'Region','Moravskoslezský kraj','CZ MO',''),(19371,63,'Region','Olomoucký kraj','CZ OL',''),(19372,63,'Region','Pardubický kraj','CZ PA',''),(19373,63,'Region','Plze?ský kraj','CZ PL',''),(19374,63,'Region','Praha, hlavní m?sto','CZ PR',''),(19375,63,'Region','St?edo?eský kraj','CZ ST',''),(19376,63,'Region','Ústecký kraj','CZ US',''),(19377,63,'Region','Vyso?ina','CZ VY',''),(19378,63,'Region','Zlínský kraj','CZ ZL',''),(19379,63,'district','Benešov','CZ-201',''),(19380,63,'district','Beroun','CZ-202',''),(19381,63,'district','Kladno','CZ-203',''),(19382,63,'district','Kolín','CZ-204',''),(19383,63,'district','Kutná Hora','CZ-205',''),(19384,63,'district','M?lník','CZ-206',''),(19385,63,'district','Mladá Boleslav','CZ-207',''),(19386,63,'district','Nymburk','CZ-208',''),(19387,63,'district','Praha-východ','CZ-209',''),(19388,63,'district','Praha-západ','CZ-20A',''),(19389,63,'district','P?íbram','CZ-20B',''),(19390,63,'district','Rakovník','CZ-20C',''),(19391,63,'district','?eské Bud?jovice','CZ-311',''),(19392,63,'district','?eský Krumlov','CZ-312',''),(19393,63,'district','Jind?ich?v Hradec','CZ-313',''),(19394,63,'district','Písek','CZ-314',''),(19395,63,'district','Prachatice','CZ-315',''),(19396,63,'district','Strakonice','CZ-316',''),(19397,63,'district','Tábor','CZ-317',''),(19398,63,'district','Domažlice','CZ-321',''),(19399,63,'district','Klatovy','CZ-322',''),(19400,63,'district','Plze?-m?sto','CZ-323',''),(19401,63,'district','Plze?-jih','CZ-324',''),(19402,63,'district','Plze?-sever','CZ-325',''),(19403,63,'district','Rokycany','CZ-326',''),(19404,63,'district','Tachov','CZ-327',''),(19405,63,'district','Cheb','CZ-411',''),(19406,63,'district','Karlovy Vary','CZ-412',''),(19407,63,'district','Sokolov','CZ-413',''),(19408,63,'district','D??ín','CZ-421',''),(19409,63,'district','Chomutov','CZ-422',''),(19410,63,'district','Litom??ice','CZ-423',''),(19411,63,'district','Louny','CZ-424',''),(19412,63,'district','Most','CZ-425',''),(19413,63,'district','Teplice','CZ-426',''),(19414,63,'district','Ústí nad Labem','CZ-427',''),(19415,63,'district','?eská Lípa','CZ-511',''),(19416,63,'district','Jablonec nad Nisou','CZ-512',''),(19417,63,'district','Liberec','CZ-513',''),(19418,63,'district','Semily','CZ-514',''),(19419,63,'district','Hradec Králové','CZ-521',''),(19420,63,'district','Ji?ín','CZ-522',''),(19421,63,'district','Náchod','CZ-523',''),(19422,63,'district','Rychnov nad Kn?žnou','CZ-524',''),(19423,63,'district','Trutnov','CZ-525',''),(19424,63,'district','Chrudim','CZ-531',''),(19425,63,'district','Pardubice','CZ-532',''),(19426,63,'district','Svitavy','CZ-533',''),(19427,63,'district','Ústí nad Orlicí','CZ-534',''),(19428,63,'district','Blansko','CZ-621',''),(19429,63,'district','Brno-m?sto','CZ-622',''),(19430,63,'district','Brno-venkov','CZ-623',''),(19431,63,'district','B?eclav','CZ-624',''),(19432,63,'district','Hodonín','CZ-625',''),(19433,63,'district','Vyškov','CZ-626',''),(19434,63,'district','Znojmo','CZ-627',''),(19435,63,'district','Jeseník','CZ-711',''),(19436,63,'district','Olomouc','CZ-712',''),(19437,63,'district','Prost?jov','CZ-713',''),(19438,63,'district','P?erov','CZ-714',''),(19439,63,'district','Šumperk','CZ-715',''),(19440,63,'district','Krom??íž','CZ-721',''),(19441,63,'district','Uherské Hradišt?','CZ-722',''),(19442,63,'district','Vsetín','CZ-723',''),(19443,63,'district','Zlín','CZ-724',''),(19444,63,'district','Bruntál','CZ-801',''),(19445,63,'district','Frýdek - Místek','CZ-802',''),(19446,63,'district','Karviná','CZ-803',''),(19447,63,'district','Nový Ji?ín','CZ-804',''),(19448,63,'district','Opava','CZ-805',''),(19449,63,'district','Ostrava - m?sto','CZ-806',''),(19450,63,'district','Praha 1','CZ-101',''),(19451,63,'district','Praha 2','CZ-102',''),(19452,63,'district','Praha 3','CZ-103',''),(19453,63,'district','Praha 4','CZ-104',''),(19454,63,'district','Praha 5','CZ-105',''),(19455,63,'district','Praha 6','CZ-106',''),(19456,63,'district','Praha 7','CZ-107',''),(19457,63,'district','Praha 8','CZ-108',''),(19458,63,'district','Praha 9','CZ-109',''),(19459,63,'district','Praha 10','CZ-10A',''),(19460,63,'district','Praha 11','CZ-10B',''),(19461,63,'district','Praha 12','CZ-10C',''),(19462,63,'district','Praha 13','CZ-10D',''),(19463,63,'district','Praha 14','CZ-10E',''),(19464,63,'district','Praha 15','CZ-10F',''),(19465,63,'district','Havlí?k?v Brod','CZ-611',''),(19466,63,'district','Jihlava','CZ-612',''),(19467,63,'district','Pelh?imov','CZ-613',''),(19468,63,'district','T?ebí?','CZ-614',''),(19469,63,'district','Žd’ár nad Sázavou','CZ-615',''),(19470,86,'State','Baden-Württemberg','DE-BW',''),(19471,86,'State','Bayern','DE-BY',''),(19472,86,'State','Bremen','DE-HB',''),(19473,86,'State','Hamburg','DE-HH',''),(19474,86,'State','Hessen','DE-HE',''),(19475,86,'State','Niedersachsen','DE-NI',''),(19476,86,'State','Nordrhein-Westfalen','DE-NW',''),(19477,86,'State','Rheinland-Pfalz','DE-RP',''),(19478,86,'State','Saarland','DE-SL',''),(19479,86,'State','Schleswig-Holstein','DE-SH',''),(19480,86,'State','Berlin','DE-BE',''),(19481,86,'State','Brandenburg','DE-BB',''),(19482,86,'State','Mecklenburg-Vorpommern','DE-MV',''),(19483,86,'State','Sachsen','DE-SN',''),(19484,86,'State','Sachsen-Anhalt','DE-ST',''),(19485,86,'State','Thüringen','DE-TH',''),(19486,65,'Region','Ali Sabieh','DJ-AS',''),(19487,65,'Region','Arta','DJ-AR',''),(19488,65,'Region','Dikhil','DJ-DI',''),(19489,65,'Region','Obock','DJ-OB',''),(19490,65,'Region','Tadjourah','DJ-TA',''),(19491,65,'City','Djibouti','DJ-DJ',''),(19492,64,'County','Copenhagen','DK-015',''),(19493,64,'County','Frederiksborg','DK-020',''),(19494,64,'County','Roskilde','DK-025',''),(19495,64,'County','Western Zealand','DK-030',''),(19496,64,'County','Storstrøm','DK-035',''),(19497,64,'County','Bornholm','DK-040',''),(19498,64,'County','Funen','DK-042',''),(19499,64,'County','Southern Jutland','DK-050',''),(19500,64,'County','Ribe','DK-055',''),(19501,64,'County','Vejle','DK-060',''),(19502,64,'County','Ringkøbing','DK-065',''),(19503,64,'County','Aarhus','DK-070',''),(19504,64,'County','Viborg','DK-076',''),(19505,64,'County','Northern Jutland','DK-080',''),(19506,64,'Municipality','Frederikaberg municipality','DK-147',''),(19507,64,'Municipality','Copenhagen municipality','DK-101',''),(19508,66,'Parish','Saint Andrew','DM-02',''),(19509,66,'Parish','Saint David','DM-03',''),(19510,66,'Parish','Saint George','DM-04',''),(19511,66,'Parish','Saint John','DM-05',''),(19512,66,'Parish','Saint Joseph','DM-06',''),(19513,66,'Parish','Saint Luke','DM-07',''),(19514,66,'Parish','Saint Mark','DM-08',''),(19515,66,'Parish','Saint Patrick','DM-09',''),(19516,66,'Parish','Saint Paul','DM-10',''),(19517,66,'Parish','Saint Peter','DM-01',''),(19518,67,'District','Distrito Nacional (Santo Domingo)','DO-01',''),(19519,67,'Province','Azua','DO-02',''),(19520,67,'Province','Bahoruco','DO-03',''),(19521,67,'Province','Barahona','DO-04',''),(19522,67,'Province','Dajabón','DO-05',''),(19523,67,'Province','Duarte','DO-06',''),(19524,67,'Province','El Seybo [El Seibo]','DO-08',''),(19525,67,'Province','Espaillat','DO-09',''),(19526,67,'Province','Hato Mayor','DO-30',''),(19527,67,'Province','Independencia','DO-10',''),(19528,67,'Province','La Altagracia','DO-11',''),(19529,67,'Province','La Estrelleta [Elías Piña]','DO-07',''),(19530,67,'Province','La Romana','DO-12',''),(19531,67,'Province','La Vega','DO-13',''),(19532,67,'Province','María Trinidad Sánchez','DO-14',''),(19533,67,'Province','Monseñor Nouel','DO-28',''),(19534,67,'Province','Monte Cristi','DO-15',''),(19535,67,'Province','Monte Plata','DO-29',''),(19536,67,'Province','Pedernales','DO-16',''),(19537,67,'Province','Peravia','DO-17',''),(19538,67,'Province','Puerto Plata','DO-18',''),(19539,67,'Province','Salcedo','DO-19',''),(19540,67,'Province','Samaná','DO-20',''),(19541,67,'Province','San Cristóbal','DO-21',''),(19542,67,'Province','San Juan','DO-22',''),(19543,67,'Province','San Pedro de Macorís','DO-23',''),(19544,67,'Province','Sánchez Ramírez','DO-24',''),(19545,67,'Province','Santiago','DO-25',''),(19546,67,'Province','Santiago Rodríguez','DO-26',''),(19547,67,'Province','Valverde','DO-27',''),(19548,7,'Province','Adrar','DZ-01',''),(19549,7,'Province','Aïn Defla','DZ-44',''),(19550,7,'Province','Aïn Témouchent','DZ-46',''),(19551,7,'Province','Alger','DZ-16',''),(19552,7,'Province','Annaba','DZ-23',''),(19553,7,'Province','Batna','DZ-05',''),(19554,7,'Province','Béchar','DZ-08',''),(19555,7,'Province','Béjaïa','DZ-06',''),(19556,7,'Province','Biskra','DZ-07',''),(19557,7,'Province','Blida','DZ-09',''),(19558,7,'Province','Bordj Bou Arréridj','DZ-34',''),(19559,7,'Province','Bouira','DZ-10',''),(19560,7,'Province','Boumerdès','DZ-35',''),(19561,7,'Province','Chlef','DZ-02',''),(19562,7,'Province','Constantine','DZ-25',''),(19563,7,'Province','Djelfa','DZ-17',''),(19564,7,'Province','El Bayadh','DZ-32',''),(19565,7,'Province','El Oued','DZ-39',''),(19566,7,'Province','El Tarf','DZ-36',''),(19567,7,'Province','Ghardaïa','DZ-47',''),(19568,7,'Province','Guelma','DZ-24',''),(19569,7,'Province','Illizi','DZ-33',''),(19570,7,'Province','Jijel','DZ-18',''),(19571,7,'Province','Khenchela','DZ-40',''),(19572,7,'Province','Laghouat','DZ-03',''),(19573,7,'Province','Mascara','DZ-29',''),(19574,7,'Province','Médéa','DZ-26',''),(19575,7,'Province','Mila','DZ-43',''),(19576,7,'Province','Mostaganem','DZ-27',''),(19577,7,'Province','Msila','DZ-28',''),(19578,7,'Province','Naama','DZ-45',''),(19579,7,'Province','Oran','DZ-31',''),(19580,7,'Province','Ouargla','DZ-30',''),(19581,7,'Province','Oum el Bouaghi','DZ-04',''),(19582,7,'Province','Relizane','DZ-48',''),(19583,7,'Province','Saïda','DZ-20',''),(19584,7,'Province','Sétif','DZ-19',''),(19585,7,'Province','Sidi Bel Abbès','DZ-22',''),(19586,7,'Province','Skikda','DZ-21',''),(19587,7,'Province','Souk Ahras','DZ-41',''),(19588,7,'Province','Tamanghasset','DZ-11',''),(19589,7,'Province','Tébessa','DZ-12',''),(19590,7,'Province','Tiaret','DZ-14',''),(19591,7,'Province','Tindouf','DZ-37',''),(19592,7,'Province','Tipaza','DZ-42',''),(19593,7,'Province','Tissemsilt','DZ-38',''),(19594,7,'Province','Tizi Ouzou','DZ-15',''),(19595,7,'Province','Tlemcen','DZ-13',''),(19596,68,'Province','Azuay','EC-A',''),(19597,68,'Province','Bolívar','EC-B',''),(19598,68,'Province','Cañar','EC-F',''),(19599,68,'Province','Carchi','EC-C',''),(19600,68,'Province','Cotopaxi','EC-X',''),(19601,68,'Province','Chimborazo','EC-H',''),(19602,68,'Province','El Oro','EC-O',''),(19603,68,'Province','Esmeraldas','EC-E',''),(19604,68,'Province','Galápagos','EC-W',''),(19605,68,'Province','Guayas','EC-G',''),(19606,68,'Province','Imbabura','EC-I',''),(19607,68,'Province','Loja','EC-L',''),(19608,68,'Province','Los Ríos','EC-R',''),(19609,68,'Province','Manabí','EC-M',''),(19610,68,'Province','Morona-Santiago','EC-S',''),(19611,68,'Province','Napo','EC-N',''),(19612,68,'Province','Orellana','EC-D',''),(19613,68,'Province','Pastaza','EC-Y',''),(19614,68,'Province','Pichincha','EC-P',''),(19615,68,'Province','Sucumbíos','EC-U',''),(19616,68,'Province','Tungurahua','EC-T',''),(19617,68,'Province','Zamora-Chinchipe','EC-Z',''),(19618,73,'County','Harjumaa','EE-37',''),(19619,73,'County','Hiiumaa','EE-39',''),(19620,73,'County','Ida-Virumaa','EE-44',''),(19621,73,'County','Jõgevamaa','EE-49',''),(19622,73,'County','Järvamaa','EE-51',''),(19623,73,'County','Läänemaa','EE-57',''),(19624,73,'County','Lääne-Virumaa','EE-59',''),(19625,73,'County','Põlvamaa','EE-65',''),(19626,73,'County','Pärnumaa','EE-67',''),(19627,73,'County','Raplamaa','EE-70',''),(19628,73,'County','Saaremaa','EE-74',''),(19629,73,'County','Tartumaa','EE-78',''),(19630,73,'County','Valgamaa','EE-82',''),(19631,73,'County','Viljandimaa','EE-84',''),(19632,73,'County','Võrumaa','EE-86',''),(19633,69,'Governorate','Ad Daqahl?yah','EG-DK',''),(19634,69,'Governorate','Al Bahr al Ahmar','EG-BA',''),(19635,69,'Governorate','Al Buhayrah','EG-BH',''),(19636,69,'Governorate','Al Fayy?m','EG-FYM',''),(19637,69,'Governorate','Al Gharb?yah','EG-GH',''),(19638,69,'Governorate','Al Iskandar?yah','EG-ALX',''),(19639,69,'Governorate','Al Ism?`?l?yah','EG-IS',''),(19640,69,'Governorate','Al J?zah','EG-GZ',''),(19641,69,'Governorate','Al Min?f?yah','EG-MNF',''),(19642,69,'Governorate','Al Miny?','EG-MN',''),(19643,69,'Governorate','Al Q?hirah','EG-C',''),(19644,69,'Governorate','Al Qaly?b?yah','EG-KB',''),(19645,69,'Governorate','Al W?d? al Jad?d','EG-WAD',''),(19646,69,'Governorate','Ash Sharq?yah','EG-SHR',''),(19647,69,'Governorate','As Suways','EG-SUZ',''),(19648,69,'Governorate','Asw?n','EG-ASN',''),(19649,69,'Governorate','Asy?t','EG-AST',''),(19650,69,'Governorate','Ban? Suwayf','EG-BNS',''),(19651,69,'Governorate','B?r Sa`?d','EG-PTS',''),(19652,69,'Governorate','Dumy?t','EG-DT',''),(19653,69,'Governorate','Jan?b S?n?\'','EG-JS',''),(19654,69,'Governorate','Kafr ash Shaykh','EG-KFS',''),(19655,69,'Governorate','Matr?h','EG-MT',''),(19656,69,'Governorate','Qin?','EG-KN',''),(19657,69,'Governorate','Shamal S?n?\'','EG-SIN',''),(19658,69,'Governorate','S?h?j','EG-SHG',''),(19659,72,'Province','Anseba','ER-AN',''),(19660,72,'Province','Debub','ER-DU',''),(19661,72,'Province','Debubawi Keyih Bahri [Debub-Keih-Bahri]','ER-DK',''),(19662,72,'Province','Gash-Barka','ER-GB',''),(19663,72,'Province','Maakel [Maekel]','ER-MA',''),(19664,72,'Province','Semenawi Keyih Bahri [Semien-Keih-Bahri]','ER-SK',''),(19665,211,'Autonomous communities','Andalucía','ES AN',''),(19666,211,'Autonomous communities','Aragón','ES AR',''),(19667,211,'Autonomous communities','Asturias, Principado de','ES O',''),(19668,211,'Autonomous communities','Canarias','ES CN',''),(19669,211,'Autonomous communities','Cantabria','ES S',''),(19670,211,'Autonomous communities','Castilla-La Mancha','ES CM',''),(19671,211,'Autonomous communities','Castilla y León','ES CL',''),(19672,211,'Autonomous communities','Cataluña','ES CT',''),(19673,211,'Autonomous communities','Extremadura','ES EX',''),(19674,211,'Autonomous communities','Galicia','ES GA',''),(19675,211,'Autonomous communities','Illes Balears','ES IB',''),(19676,211,'Autonomous communities','La Rioja','ES LO',''),(19677,211,'Autonomous communities','Madrid, Comunidad de','ES M',''),(19678,211,'Autonomous communities','Murcia, Región de','ES MU',''),(19679,211,'Autonomous communities','Navarra, Comunidad Foral de','ES NA',''),(19680,211,'Autonomous communities','País Vasco','ES PV',''),(19681,211,'Autonomous communities','Valenciana, Comunidad','ES VC',''),(19682,211,'Province','Álava','ES-VI',''),(19683,211,'Province','Albacete','ES-AB',''),(19684,211,'Province','Alicante','ES-A',''),(19685,211,'Province','Almería','ES-AL',''),(19686,211,'Province','Asturias','ES-O',''),(19687,211,'Province','Ávila','ES-AV',''),(19688,211,'Province','Badajoz','ES-BA',''),(19689,211,'Province','Baleares','ES-IB',''),(19690,211,'Province','Barcelona','ES-B',''),(19691,211,'Province','Burgos','ES-BU',''),(19692,211,'Province','Cáceres','ES-CC',''),(19693,211,'Province','Cádiz','ES-CA',''),(19694,211,'Province','Cantabria','ES-S',''),(19695,211,'Province','Castellón','ES-CS',''),(19696,211,'Province','Ciudad Real','ES-CR',''),(19697,211,'Province','Córdoba','ES-CO',''),(19698,211,'Province','Cuenca','ES-CU',''),(19699,211,'Province','Girona','ES-GI',''),(19700,211,'Province','Granada','ES-GR',''),(19701,211,'Province','Guadalajara','ES-GU',''),(19702,211,'Province','Guipúzcoa','ES-SS',''),(19703,211,'Province','Huelva','ES-H',''),(19704,211,'Province','Huesca','ES-HU',''),(19705,211,'Province','Jaén','ES-J',''),(19706,211,'Province','A Coruña','ES-C',''),(19707,211,'Province','La Rioja','ES-LO',''),(19708,211,'Province','Las Palmas','ES-GC',''),(19709,211,'Province','León','ES-LE',''),(19710,211,'Province','Lleida','ES-L',''),(19711,211,'Province','Lugo','ES-LU',''),(19712,211,'Province','Madrid','ES-M',''),(19713,211,'Province','Málaga','ES-MA',''),(19714,211,'Province','Murcia','ES-MU',''),(19715,211,'Province','Navarra','ES-NA',''),(19716,211,'Province','Ourense','ES-OR',''),(19717,211,'Province','Palencia','ES-P',''),(19718,211,'Province','Pontevedra','ES-PO',''),(19719,211,'Province','Salamanca','ES-SA',''),(19720,211,'Province','Santa Cruz de Tenerife','ES-TF',''),(19721,211,'Province','Segovia','ES-SG',''),(19722,211,'Province','Sevilla','ES-SE',''),(19723,211,'Province','Soria','ES-SO',''),(19724,211,'Province','Tarragona','ES-T',''),(19725,211,'Province','Teruel','ES-TE',''),(19726,211,'Province','Toledo','ES-TO',''),(19727,211,'Province','Valencia','ES-V',''),(19728,211,'Province','Valladolid','ES-VA',''),(19729,211,'Province','Vizcaya','ES-BI',''),(19730,211,'Province','Zamora','ES-ZA',''),(19731,211,'Province','Zaragoza','ES-Z',''),(19732,211,'Autonomous city','Ceuta','ES-CE',''),(19733,211,'Autonomous city','Melilla','ES-ML',''),(19734,74,'Administration','?d?s ?beba','ET-AA',''),(19735,74,'Administration','Dir? Dawa','ET-DD',''),(19736,74,'State','?far','ET-AF',''),(19737,74,'State','?mara','ET-AM',''),(19738,74,'State','B?nshangul Gumuz','ET-BE',''),(19739,74,'State','Gamb?la Hizboch','ET-GA',''),(19740,74,'State','H?rer? Hizb','ET-HA',''),(19741,74,'State','Orom?ya','ET-OR',''),(19742,74,'State','Sumal?','ET-SO',''),(19743,74,'State','Tigray','ET-TI',''),(19744,74,'State','YeDebub Bih?roch Bih?reseboch na Hizboch','ET-SN',''),(19745,78,'Province','Ahvenanmaan lääni','FI-AL',''),(19746,78,'Province','Etelä-Suomen lääni','FI-ES',''),(19747,78,'Province','Itä-Suomen lääni','FI-IS',''),(19748,78,'Province','Lapin lääni','FI-LL',''),(19749,78,'Province','Länsi-Suomen lääni','FI-LS',''),(19750,78,'Province','Oulun lääni','FI-OL',''),(19751,77,'Division','Central','FJ-C',''),(19752,77,'Division','Eastern','FJ-E',''),(19753,77,'Division','Northern','FJ-N',''),(19754,77,'Division','Western','FJ-W',''),(19755,77,'Dependency','Rotuma','FJ-R',''),(19756,148,'State','Chuuk','FM-TRK',''),(19757,148,'State','Kosrae','FM-KSA',''),(19758,148,'State','Pohnpei','FM-PNI',''),(19759,148,'State','Yap','FM-YAP',''),(19760,79,'Metropolitan region','Alsace','FR A',''),(19761,79,'Metropolitan region','Aquitaine','FR B',''),(19762,79,'Metropolitan region','Auvergne','FR C',''),(19763,79,'Metropolitan region','Basse-Normandie','FR P',''),(19764,79,'Metropolitan region','Bourgogne','FR D',''),(19765,79,'Metropolitan region','Bretagne','FR E',''),(19766,79,'Metropolitan region','Centre','FR F',''),(19767,79,'Metropolitan region','Champagne-Ardenne','FR G',''),(19768,79,'Metropolitan region','Corse','FR H',''),(19769,79,'Metropolitan region','Franche-Comté','FR I',''),(19770,79,'Metropolitan region','Haute-Normandie','FR Q',''),(19771,79,'Metropolitan region','Île-de-France','FR J',''),(19772,79,'Metropolitan region','Languedoc-Roussillon','FR K',''),(19773,79,'Metropolitan region','Limousin','FR L',''),(19774,79,'Metropolitan region','Lorraine','FR M',''),(19775,79,'Metropolitan region','Midi-Pyrénées','FR N',''),(19776,79,'Metropolitan region','Nord - Pas-de-Calais','FR O',''),(19777,79,'Metropolitan region','Pays de la Loire','FR R',''),(19778,79,'Metropolitan region','Picardie','FR S',''),(19779,79,'Metropolitan region','Poitou-Charentes','FR T',''),(19780,79,'Metropolitan region','Provence-Alpes-Côte d\'Azur','FR U',''),(19781,79,'Metropolitan region','Rhône-Alpes','FR V',''),(19782,79,'Overseas region/department','Guadeloupe','FR GP',''),(19783,79,'Overseas region/department','Guyane','FR GF',''),(19784,79,'Overseas region/department','Martinique','FR MQ',''),(19785,79,'Overseas region/department','Réunion','FR RE',''),(19786,79,'Metropolitan department','Ain','FR-01',''),(19787,79,'Metropolitan department','Aisne','FR-02',''),(19788,79,'Metropolitan department','Allier','FR-03',''),(19789,79,'Metropolitan department','Alpes-de-Haute-Provence','FR-04',''),(19790,79,'Metropolitan department','Alpes-Maritimes','FR-06',''),(19791,79,'Metropolitan department','Ardèche','FR-07',''),(19792,79,'Metropolitan department','Ardennes','FR-08',''),(19793,79,'Metropolitan department','Ariège','FR-09',''),(19794,79,'Metropolitan department','Aube','FR-10',''),(19795,79,'Metropolitan department','Aude','FR-11',''),(19796,79,'Metropolitan department','Aveyron','FR-12',''),(19797,79,'Metropolitan department','Bas-Rhin','FR-67',''),(19798,79,'Metropolitan department','Bouches-du-Rhône','FR-13',''),(19799,79,'Metropolitan department','Calvados','FR-14',''),(19800,79,'Metropolitan department','Cantal','FR-15',''),(19801,79,'Metropolitan department','Charente','FR-16',''),(19802,79,'Metropolitan department','Charente-Maritime','FR-17',''),(19803,79,'Metropolitan department','Cher','FR-18',''),(19804,79,'Metropolitan department','Corrèze','FR-19',''),(19805,79,'Metropolitan department','Corse-du-Sud','FR-2A',''),(19806,79,'Metropolitan department','Côte-d\'Or','FR-21',''),(19807,79,'Metropolitan department','Côtes-d\'Armor','FR-22',''),(19808,79,'Metropolitan department','Creuse','FR-23',''),(19809,79,'Metropolitan department','Deux-Sèvres','FR-79',''),(19810,79,'Metropolitan department','Dordogne','FR-24',''),(19811,79,'Metropolitan department','Doubs','FR-25',''),(19812,79,'Metropolitan department','Drôme','FR-26',''),(19813,79,'Metropolitan department','Essonne','FR-91',''),(19814,79,'Metropolitan department','Eure','FR-27',''),(19815,79,'Metropolitan department','Eure-et-Loir','FR-28',''),(19816,79,'Metropolitan department','Finistère','FR-29',''),(19817,79,'Metropolitan department','Gard','FR-30',''),(19818,79,'Metropolitan department','Gers','FR-32',''),(19819,79,'Metropolitan department','Gironde','FR-33',''),(19820,79,'Metropolitan department','Haute-Corse','FR-2B',''),(19821,79,'Metropolitan department','Haute-Garonne','FR-31',''),(19822,79,'Metropolitan department','Haute-Loire','FR-43',''),(19823,79,'Metropolitan department','Haute-Marne','FR-52',''),(19824,79,'Metropolitan department','Hautes-Alpes','FR-05',''),(19825,79,'Metropolitan department','Haute-Saône','FR-70',''),(19826,79,'Metropolitan department','Haute-Savoie','FR-74',''),(19827,79,'Metropolitan department','Hautes-Pyrénées','FR-65',''),(19828,79,'Metropolitan department','Haute-Vienne','FR-87',''),(19829,79,'Metropolitan department','Haut-Rhin','FR-68',''),(19830,79,'Metropolitan department','Hauts-de-Seine','FR-92',''),(19831,79,'Metropolitan department','Hérault','FR-34',''),(19832,79,'Metropolitan department','Ille-et-Vilaine','FR-35',''),(19833,79,'Metropolitan department','Indre','FR-36',''),(19834,79,'Metropolitan department','Indre-et-Loire','FR-37',''),(19835,79,'Metropolitan department','Isère','FR-38',''),(19836,79,'Metropolitan department','Jura','FR-39',''),(19837,79,'Metropolitan department','Landes','FR-40',''),(19838,79,'Metropolitan department','Loir-et-Cher','FR-41',''),(19839,79,'Metropolitan department','Loire','FR-42',''),(19840,79,'Metropolitan department','Loire-Atlantique','FR-44',''),(19841,79,'Metropolitan department','Loiret','FR-45',''),(19842,79,'Metropolitan department','Lot','FR-46',''),(19843,79,'Metropolitan department','Lot-et-Garonne','FR-47',''),(19844,79,'Metropolitan department','Lozère','FR-48',''),(19845,79,'Metropolitan department','Maine-et-Loire','FR-49',''),(19846,79,'Metropolitan department','Manche','FR-50',''),(19847,79,'Metropolitan department','Marne','FR-51',''),(19848,79,'Metropolitan department','Mayenne','FR-53',''),(19849,79,'Metropolitan department','Meurthe-et-Moselle','FR-54',''),(19850,79,'Metropolitan department','Meuse','FR-55',''),(19851,79,'Metropolitan department','Morbihan','FR-56',''),(19852,79,'Metropolitan department','Moselle','FR-57',''),(19853,79,'Metropolitan department','Nièvre','FR-58',''),(19854,79,'Metropolitan department','Nord','FR-59',''),(19855,79,'Metropolitan department','Oise','FR-60',''),(19856,79,'Metropolitan department','Orne','FR-61',''),(19857,79,'Metropolitan department','Paris','FR-75',''),(19858,79,'Metropolitan department','Pas-de-Calais','FR-62',''),(19859,79,'Metropolitan department','Puy-de-Dôme','FR-63',''),(19860,79,'Metropolitan department','Pyrénées-Atlantiques','FR-64',''),(19861,79,'Metropolitan department','Pyrénées-Orientales','FR-66',''),(19862,79,'Metropolitan department','Rhône','FR-69',''),(19863,79,'Metropolitan department','Saône-et-Loire','FR-71',''),(19864,79,'Metropolitan department','Sarthe','FR-72',''),(19865,79,'Metropolitan department','Savoie','FR-73',''),(19866,79,'Metropolitan department','Seine-et-Marne','FR-77',''),(19867,79,'Metropolitan department','Seine-Maritime','FR-76',''),(19868,79,'Metropolitan department','Seine-Saint-Denis','FR-93',''),(19869,79,'Metropolitan department','Somme','FR-80',''),(19870,79,'Metropolitan department','Tarn','FR-81',''),(19871,79,'Metropolitan department','Tarn-et-Garonne','FR-82',''),(19872,79,'Metropolitan department','Territoire de Belfort','FR-90',''),(19873,79,'Metropolitan department','Val-de-Marne','FR-94',''),(19874,79,'Metropolitan department','Val d\'Oise','FR-95',''),(19875,79,'Metropolitan department','Var','FR-83',''),(19876,79,'Metropolitan department','Vaucluse','FR-84',''),(19877,79,'Metropolitan department','Vendée','FR-85',''),(19878,79,'Metropolitan department','Vienne','FR-86',''),(19879,79,'Metropolitan department','Vosges','FR-88',''),(19880,79,'Metropolitan department','Yonne','FR-89',''),(19881,79,'Metropolitan department','Yvelines','FR-78',''),(19882,79,'Dependency','Clipperton','FR-CP',''),(19883,79,'Overseas territorial collectivity','Saint-Barthélemy','FR-BL',''),(19884,79,'Overseas territorial collectivity','Saint-Martin','FR-MF',''),(19885,79,'Overseas territorial collectivity','Nouvelle-Calédonie','FR-NC',''),(19886,79,'Overseas territorial collectivity','Polynésie française','FR-PF',''),(19887,79,'Overseas territorial collectivity','Saint-Pierre-et-Miquelon','FR-PM',''),(19888,79,'Overseas territorial collectivity','Terres australes françaises','FR-TF',''),(19889,79,'Overseas territorial collectivity','Wallis et Futuna','FR-WF',''),(19890,79,'Overseas territorial collectivity','Mayotte','FR-YT',''),(19891,2,'Country','England','GB ENG',''),(19892,2,'Country','Scotland','GB SCT',''),(19893,2,'Province','Northern Ireland','GB NIR',''),(19894,2,'Principality','Wales','GB WLS',''),(19895,2,'Included for completeness','England and Wales','GB EAW',''),(19896,2,'Included for completeness','Great Britain','GB GBN',''),(19897,2,'Included for completeness','United Kingdom','GB UKM',''),(19898,2,'Two-tier county','Bedfordshire','GB-BDF',''),(19899,2,'Two-tier county','Buckinghamshire','GB-BKM',''),(19900,2,'Two-tier county','Cambridgeshire','GB-CAM',''),(19901,2,'Two-tier county','Cheshire','GB-CHS',''),(19902,2,'Two-tier county','Cornwall','GB-CON',''),(19903,2,'Two-tier county','Cumbria','GB-CMA',''),(19904,2,'Two-tier county','Derbyshire','GB-DBY',''),(19905,2,'Two-tier county','Devon','GB-DEV',''),(19906,2,'Two-tier county','Dorset','GB-DOR',''),(19907,2,'Two-tier county','Durham','GB-DUR',''),(19908,2,'Two-tier county','East Sussex','GB-ESX',''),(19909,2,'Two-tier county','Essex','GB-ESS',''),(19910,2,'Two-tier county','Gloucestershire','GB-GLS',''),(19911,2,'Two-tier county','Hampshire','GB-HAM',''),(19912,2,'Two-tier county','Hertfordshire','GB-HRT',''),(19913,2,'Two-tier county','Kent','GB-KEN',''),(19914,2,'Two-tier county','Lancashire','GB-LAN',''),(19915,2,'Two-tier county','Leicestershire','GB-LEC',''),(19916,2,'Two-tier county','Lincolnshire','GB-LIN',''),(19917,2,'Two-tier county','Norfolk','GB-NFK',''),(19918,2,'Two-tier county','North Yorkshire','GB-NYK',''),(19919,2,'Two-tier county','Northamptonshire','GB-NTH',''),(19920,2,'Two-tier county','Northumbarland','GB-NBL',''),(19921,2,'Two-tier county','Nottinghamshire','GB-NTT',''),(19922,2,'Two-tier county','Oxfordshire','GB-OXF',''),(19923,2,'Two-tier county','Somerset','GB-SOM',''),(19924,2,'Two-tier county','Staffordshire','GB-STS',''),(19925,2,'Two-tier county','Suffolk','GB-SFK',''),(19926,2,'Two-tier county','Surrey','GB-SRY',''),(19927,2,'Two-tier county','West Sussex','GB-WSX',''),(19928,2,'Two-tier county','Wiltshire','GB-WIL',''),(19929,2,'Two-tier county','Worcestershire','GB-WOR',''),(19930,2,'London borough','Barking and Dagenham','GB-BDG',''),(19931,2,'London borough','Barnet','GB-BNE',''),(19932,2,'London borough','Bexley','GB-BEX',''),(19933,2,'London borough','Brent','GB-BEN',''),(19934,2,'London borough','Bromley','GB-BRY',''),(19935,2,'London borough','Camden','GB-CMD',''),(19936,2,'London borough','Croydon','GB-CRY',''),(19937,2,'London borough','Ealing','GB-EAL',''),(19938,2,'London borough','Enfield','GB-ENF',''),(19939,2,'London borough','Greenwich','GB-GRE',''),(19940,2,'London borough','Hackney','GB-HCK',''),(19941,2,'London borough','Hammersmith and Fulham','GB-HMF',''),(19942,2,'London borough','Haringey','GB-HRY',''),(19943,2,'London borough','Harrow','GB-HRW',''),(19944,2,'London borough','Havering','GB-HAV',''),(19945,2,'London borough','Hillingdon','GB-HIL',''),(19946,2,'London borough','Hounslow','GB-HNS',''),(19947,2,'London borough','Islington','GB-ISL',''),(19948,2,'London borough','Kensington and Chelsea','GB-KEC',''),(19949,2,'London borough','Kingston upon Thames','GB-KTT',''),(19950,2,'London borough','Lambeth','GB-LBH',''),(19951,2,'London borough','Lewisham','GB-LEW',''),(19952,2,'London borough','Merton','GB-MRT',''),(19953,2,'London borough','Newham','GB-NWM',''),(19954,2,'London borough','Redbridge','GB-RDB',''),(19955,2,'London borough','Richmond upon Thames','GB-RIC',''),(19956,2,'London borough','Southwark','GB-SWK',''),(19957,2,'London borough','Sutton','GB-STN',''),(19958,2,'London borough','Tower Hamlets','GB-TWH',''),(19959,2,'London borough','Waltham Forest','GB-WFT',''),(19960,2,'London borough','Wandsworth','GB-WND',''),(19961,2,'London borough','Westminster','GB-WSM',''),(19962,2,'Metropolitan district','Barnsley','GB-BNS',''),(19963,2,'Metropolitan district','Birmingham','GB-BIR',''),(19964,2,'Metropolitan district','Bolton','GB-BOL',''),(19965,2,'Metropolitan district','Bradford','GB-BRD',''),(19966,2,'Metropolitan district','Bury','GB-BUR',''),(19967,2,'Metropolitan district','Calderdale','GB-CLD',''),(19968,2,'Metropolitan district','Coventry','GB-COV',''),(19969,2,'Metropolitan district','Doncaster','GB-DNC',''),(19970,2,'Metropolitan district','Dudley','GB-DUD',''),(19971,2,'Metropolitan district','Gateshead','GB-GAT',''),(19972,2,'Metropolitan district','Kirklees','GB-KIR',''),(19973,2,'Metropolitan district','Knowsley','GB-KWL',''),(19974,2,'Metropolitan district','Leeds','GB-LDS',''),(19975,2,'Metropolitan district','Liverpool','GB-LIV',''),(19976,2,'Metropolitan district','Manchester','GB-MAN',''),(19977,2,'Metropolitan district','Newcastle upon Tyne','GB-NET',''),(19978,2,'Metropolitan district','North Tyneside','GB-NTY',''),(19979,2,'Metropolitan district','Oldham','GB-OLD',''),(19980,2,'Metropolitan district','Rochdale','GB-RCH',''),(19981,2,'Metropolitan district','Rotherham','GB-ROT',''),(19982,2,'Metropolitan district','Salford','GB-SLF',''),(19983,2,'Metropolitan district','Sandwell','GB-SAW',''),(19984,2,'Metropolitan district','Sefton','GB-SFT',''),(19985,2,'Metropolitan district','Sheffield','GB-SHF',''),(19986,2,'Metropolitan district','Solihull','GB-SOL',''),(19987,2,'Metropolitan district','South Tyneside','GB-STY',''),(19988,2,'Metropolitan district','St. Helens','GB-SHN',''),(19989,2,'Metropolitan district','Stockport','GB-SKP',''),(19990,2,'Metropolitan district','Sunderland','GB-SND',''),(19991,2,'Metropolitan district','Tameside','GB-TAM',''),(19992,2,'Metropolitan district','Trafford','GB-TRF',''),(19993,2,'Metropolitan district','Wakefield','GB-WKF',''),(19994,2,'Metropolitan district','Walsall','GB-WLL',''),(19995,2,'Metropolitan district','Wigan','GB-WGN',''),(19996,2,'Metropolitan district','Wirral','GB-WRL',''),(19997,2,'Metropolitan district','Wolverhampton','GB-WLV',''),(19998,2,'City corporation','London, City of','GB-LND',''),(19999,2,'Division','Aberdeen City','GB-ABE',''),(20000,2,'Division','Aberdeenshire','GB-ABD',''),(20001,2,'Division','Angus','GB-ANS',''),(20002,2,'Division','Antrim','GB-ANT',''),(20003,2,'Division','Ards','GB-ARD',''),(20004,2,'Division','Argyll and Bute','GB-AGB',''),(20005,2,'Division','Armagh','GB-ARM',''),(20006,2,'Division','Ballymena','GB-BLA',''),(20007,2,'Division','Ballymoney','GB-BLY',''),(20008,2,'Division','Banbridge','GB-BNB',''),(20009,2,'Division','Bath and North East Somerset','GB-BAS',''),(20010,2,'Division','Belfast','GB-BFS',''),(20011,2,'Division','Blackburn with Darwen','GB-BBD',''),(20012,2,'Division','Blackpool','GB-BPL',''),(20013,2,'Division','Blaenau Gwent','GB-BGW',''),(20014,2,'Division','Bournemouth','GB-BMH',''),(20015,2,'Division','Bracknell Forest','GB-BRC',''),(20016,2,'Division','Bridgend','GB-BGE',''),(20017,2,'Division','Brighton and Hove','GB-BNH',''),(20018,2,'Division','Bristol, City of','GB-BST',''),(20019,2,'Division','Caerphilly','GB-CAY',''),(20020,2,'Division','Cardiff','GB-CRF',''),(20021,2,'Division','Carmarthenshire','GB-CMN',''),(20022,2,'Division','Carrickfergus','GB-CKF',''),(20023,2,'Division','Castlereagh','GB-CSR',''),(20024,2,'Division','Ceredigion','GB-CGN',''),(20025,2,'Division','Clackmannanshire','GB-CLK',''),(20026,2,'Division','Coleraine','GB-CLR',''),(20027,2,'Division','Conwy','GB-CWY',''),(20028,2,'Division','Cookstown','GB-CKT',''),(20029,2,'Division','Craigavon','GB-CGV',''),(20030,2,'Division','Darlington','GB-DAL',''),(20031,2,'Division','Denbighshire','GB-DEN',''),(20032,2,'Division','Derby','GB-DER',''),(20033,2,'Division','Derry','GB-DRY',''),(20034,2,'Division','Down','GB-DOW',''),(20035,2,'Division','Dumfries and Galloway','GB-DGY',''),(20036,2,'Division','Dundee City','GB-DND',''),(20037,2,'Division','Dungannon','GB-DGN',''),(20038,2,'Division','East Ayrshire','GB-EAY',''),(20039,2,'Division','East Dunbartonshire','GB-EDU',''),(20040,2,'Division','East Lothian','GB-ELN',''),(20041,2,'Division','East Renfrewshire','GB-ERW',''),(20042,2,'Division','East Riding of Yorkshire','GB-ERY',''),(20043,2,'Division','Edinburgh, City of','GB-EDH',''),(20044,2,'Division','Eilean Siar','GB-ELS',''),(20045,2,'Division','Falkirk','GB-FAL',''),(20046,2,'Division','Fermanagh','GB-FER',''),(20047,2,'Division','Fife','GB-FIF',''),(20048,2,'Division','Flintshire','GB-FLN',''),(20049,2,'Division','Glasgow City','GB-GLG',''),(20050,2,'Division','Gwynedd','GB-GWN',''),(20051,2,'Division','Halton','GB-HAL',''),(20052,2,'Division','Hartlepool','GB-HPL',''),(20053,2,'Division','Herefordshire, County of','GB-HEF',''),(20054,2,'Division','Highland','GB-HED',''),(20055,2,'Division','Inverclyde','GB-IVC',''),(20056,2,'Division','Isle of Anglesey','GB-AGY',''),(20057,2,'Division','Isle of Wight','GB-IOW',''),(20058,2,'Division','Isles of Scilly','GB-IOS',''),(20059,2,'Division','Kingston upon Hull, City of','GB-KHL',''),(20060,2,'Division','Larne','GB-LRN',''),(20061,2,'Division','Leicester','GB-LCE',''),(20062,2,'Division','Limavady','GB-LMV',''),(20063,2,'Division','Lisburn','GB-LSB',''),(20064,2,'Division','Luton','GB-LUT',''),(20065,2,'Division','Magherafelt','GB-MFT',''),(20066,2,'Division','Medway','GB-MDW',''),(20067,2,'Division','Merthyr Tydfil','GB-MTY',''),(20068,2,'Division','Middlesbrough','GB-MDB',''),(20069,2,'Division','Midlothian','GB-MLN',''),(20070,2,'Division','Milton Keynes','GB-MIK',''),(20071,2,'Division','Monmouthshire','GB-MON',''),(20072,2,'Division','Moray','GB-MRY',''),(20073,2,'Division','Moyle','GB-MYL',''),(20074,2,'Division','Neath Port Talbot','GB-NTL',''),(20075,2,'Division','Newport','GB-NWP',''),(20076,2,'Division','Newry and Mourne','GB-NYM',''),(20077,2,'Division','Newtownabbey','GB-NTA',''),(20078,2,'Division','North Ayrshire','GB-NAY',''),(20079,2,'Division','North Down','GB-NDN',''),(20080,2,'Division','North East Lincolnshire','GB-NEL',''),(20081,2,'Division','North Lanarkshire','GB-NLK',''),(20082,2,'Division','North Lincolnshire','GB-NLN',''),(20083,2,'Division','North Somerset','GB-NSM',''),(20084,2,'Division','Nottingham','GB-NGM',''),(20085,2,'Division','Omagh','GB-OMH',''),(20086,2,'Division','Orkney Islands','GB-ORR',''),(20087,2,'Division','Pembrokeshire','GB-PEM',''),(20088,2,'Division','Perth and Kinross','GB-PKN',''),(20089,2,'Division','Peterborough','GB-PTE',''),(20090,2,'Division','Plymouth','GB-PLY',''),(20091,2,'Division','Poole','GB-POL',''),(20092,2,'Division','Portsmouth','GB-POR',''),(20093,2,'Division','Powys','GB-POW',''),(20094,2,'Division','Reading','GB-RDG',''),(20095,2,'Division','Redcar and Cleveland','GB-RCC',''),(20096,2,'Division','Renfrewshire','GB-RFW',''),(20097,2,'Division','Rhondda, Cynon, Taff','GB-RCT',''),(20098,2,'Division','Rutland','GB-RUT',''),(20099,2,'Division','Scottish Borders, The','GB-SCB',''),(20100,2,'Division','Shetland Islands','GB-ZET',''),(20101,2,'Division','Shropshire','GB-SHR',''),(20102,2,'Division','Slough','GB-SLG',''),(20103,2,'Division','South Ayrshire','GB-SAY',''),(20104,2,'Division','South Gloucestershire','GB-SGC',''),(20105,2,'Division','South Lanarkshire','GB-SLK',''),(20106,2,'Division','Southampton','GB-STH',''),(20107,2,'Division','Southend-on-Sea','GB-SOS',''),(20108,2,'Division','Stirling','GB-STG',''),(20109,2,'Division','Stockton-on-Tees','GB-STT',''),(20110,2,'Division','Stoke-on-Trent','GB-STE',''),(20111,2,'Division','Strabane','GB-STB',''),(20112,2,'Division','Swansea','GB-SWA',''),(20113,2,'Division','Swindon','GB-SWD',''),(20114,2,'Division','Telford and Wrekin','GB-TFW',''),(20115,2,'Division','Thurrock','GB-THR',''),(20116,2,'Division','Torbay','GB-TOB',''),(20117,2,'Division','Torfaen','GB-TOF',''),(20118,2,'Division','Vale of Glamorgan, The','GB-VGL',''),(20119,2,'Division','Warrington','GB-WRT',''),(20120,2,'Division','Warwickshire','GB-WAR',''),(20121,2,'Division','West Berkshire','GB-WBX',''),(20122,2,'Division','West Dunbartonshire','GB-WDU',''),(20123,2,'Division','West Lothian','GB-WLN',''),(20124,2,'Division','Windsor and Maidenhead','GB-WNM',''),(20125,2,'Division','Wokingham','GB-WOK',''),(20126,2,'Division','Wrexham','GB-WRX',''),(20127,2,'Division','York','GB-YOR',''),(20128,91,'Parish','Saint Andrew','GD-01',''),(20129,91,'Parish','Saint David','GD-02',''),(20130,91,'Parish','Saint George','GD-03',''),(20131,91,'Parish','Saint John','GD-04',''),(20132,91,'Parish','Saint Mark','GD-05',''),(20133,91,'Parish','Saint Patrick','GD-06',''),(20134,91,'Dependency','Southern Grenadine Islands','GD-10',''),(20135,85,'Autonomous republic','Abkhazia','GE-AB',''),(20136,85,'Autonomous republic','Ajaria','GE-AJ',''),(20137,85,'City','T’bilisi','GE-TB',''),(20138,85,'Region','Guria','GE-GU',''),(20139,85,'Region','Imeret’i','GE-IM',''),(20140,85,'Region','Kakhet’i','GE-KA',''),(20141,85,'Region','K’vemo K’art’li','GE-KK',''),(20142,85,'Region','Mts’khet’a-Mt’ianet’i','GE-MM',''),(20143,85,'Region','Racha-Lech’khumi-K’vemo Svanet’i','GE-RL',''),(20144,85,'Region','Samegrelo-Zemo Svanet’i','GE-SZ',''),(20145,85,'Region','Samts’khe-Javakhet’i','GE-SJ',''),(20146,85,'Region','Shida K’art’li','GE-SK',''),(20147,87,'Region','Ashanti','GH-AH',''),(20148,87,'Region','Brong-Ahafo','GH-BA',''),(20149,87,'Region','Central','GH-CP',''),(20150,87,'Region','Eastern','GH-EP',''),(20151,87,'Region','Greater Accra','GH-AA',''),(20152,87,'Region','Northern','GH-NP',''),(20153,87,'Region','Upper East','GH-UE',''),(20154,87,'Region','Upper West','GH-UW',''),(20155,87,'Region','Volta','GH-TV',''),(20156,87,'Region','Western','GH-WP',''),(20157,84,'Division','Lower River','GM-L',''),(20158,84,'Division','Central River','GM-M',''),(20159,84,'Division','North Bank','GM-N',''),(20160,84,'Division','Upper River','GM-U',''),(20161,84,'Division','Western','GM-W',''),(20162,84,'City','Banjul','GM-B',''),(20163,96,'Governorate','Boké, Gouvernorat de','GN B',''),(20164,96,'Governorate','Faranah, Gouvernorat de','GN F',''),(20165,96,'Governorate','Kankan, Gouvernorat de','GN K',''),(20166,96,'Governorate','Kindia, Gouvernorat de','GN D',''),(20167,96,'Governorate','Labé, Gouvernorat de','GN L',''),(20168,96,'Governorate','Mamou, Gouvernorat de','GN M',''),(20169,96,'Governorate','Nzérékoré, Gouvernorat de','GN N',''),(20170,96,'City','Conakry','GN C',''),(20171,96,'Prefecture','Beyla','GN-BE',''),(20172,96,'Prefecture','Boffa','GN-BF',''),(20173,96,'Prefecture','Boké','GN-BK',''),(20174,96,'Prefecture','Coyah','GN-CO',''),(20175,96,'Prefecture','Dabola','GN-DB',''),(20176,96,'Prefecture','Dalaba','GN-DL',''),(20177,96,'Prefecture','Dinguiraye','GN-DI',''),(20178,96,'Prefecture','Dubréka','GN-DU',''),(20179,96,'Prefecture','Faranah','GN-FA',''),(20180,96,'Prefecture','Forécariah','GN-FO',''),(20181,96,'Prefecture','Fria','GN-FR',''),(20182,96,'Prefecture','Gaoual','GN-GA',''),(20183,96,'Prefecture','Guékédou','GN-GU',''),(20184,96,'Prefecture','Kankan','GN-KA',''),(20185,96,'Prefecture','Kérouané','GN-KE',''),(20186,96,'Prefecture','Kindia','GN-KD',''),(20187,96,'Prefecture','Kissidougou','GN-KS',''),(20188,96,'Prefecture','Koubia','GN-KB',''),(20189,96,'Prefecture','Koundara','GN-KN',''),(20190,96,'Prefecture','Kouroussa','GN-KO',''),(20191,96,'Prefecture','Labé','GN-LA',''),(20192,96,'Prefecture','Lélouma','GN-LE',''),(20193,96,'Prefecture','Lola','GN-LO',''),(20194,96,'Prefecture','Macenta','GN-MC',''),(20195,96,'Prefecture','Mali','GN-ML',''),(20196,96,'Prefecture','Mamou','GN-MM',''),(20197,96,'Prefecture','Mandiana','GN-MD',''),(20198,96,'Prefecture','Nzérékoré','GN-NZ',''),(20199,96,'Prefecture','Pita','GN-PI',''),(20200,96,'Prefecture','Siguiri','GN-SI',''),(20201,96,'Prefecture','Télimélé','GN-TE',''),(20202,96,'Prefecture','Tougué','GN-TO',''),(20203,96,'Prefecture','Yomou','GN-YO',''),(20204,71,'Province','Región Continental','GQ-C',''),(20205,71,'Province','Región Insular','GQ-I',''),(20206,71,'Province','Annobón','GQ-AN',''),(20207,71,'Province','Bioko Norte','GQ-BN',''),(20208,71,'Province','Bioko Sur','GQ-BS',''),(20209,71,'Province','Centro Sur','GQ-CS',''),(20210,71,'Province','Kié-Ntem','GQ-KN',''),(20211,71,'Province','Litoral','GQ-LI',''),(20212,71,'Province','Wele-Nzás','GQ-WN',''),(20213,89,'Region','Periféreia Anatolikís Makedonías kai Thrákis','GR I',''),(20214,89,'Region','Periféreia Kentrikís Makedonías','GR II',''),(20215,89,'Region','Periféreia Dytikís Makedonías','GR III',''),(20216,89,'Region','Periféreia Ipeírou','GR IV',''),(20217,89,'Region','Periféreia Thessalías','GR V',''),(20218,89,'Region','Periféreia Ioníon Níson','GR VI',''),(20219,89,'Region','Periféreia Dytikís Elládas','GR VII',''),(20220,89,'Region','Periféreia Stereás Elládas','GR VIII',''),(20221,89,'Region','Periféreia Attikís','GR IX',''),(20222,89,'Region','Periféreia Peloponnísou','GR X',''),(20223,89,'Region','Periféreia Voreíou Aigaíou','GR XI',''),(20224,89,'Region','Periféreia Notíou Aigaíou','GR XII',''),(20225,89,'Region','Periféreia Krítis','GR XIII',''),(20226,89,'Autonomous monastic state','Ágion Óros','GR-69',''),(20227,89,'Prefecture','Nomós Athinón','GR-A1',''),(20228,89,'Prefecture','Nomós Anatolikís Attikís','GR-A2',''),(20229,89,'Prefecture','Nomós Peiraiós','GR-A3',''),(20230,89,'Prefecture','Nomós Dytikís Attikís','GR-A4',''),(20231,89,'Prefecture','Nomós Aitoloakarnanías','GR-01',''),(20232,89,'Prefecture','Nomós Voiotías','GR-03',''),(20233,89,'Prefecture','Nomós Évvoias','GR-04',''),(20234,89,'Prefecture','Nomós Evrytanías','GR-05',''),(20235,89,'Prefecture','Nomós Fthiótidas','GR-06',''),(20236,89,'Prefecture','Nomós Fokídas','GR-07',''),(20237,89,'Prefecture','Nomós Argolídas','GR-11',''),(20238,89,'Prefecture','Nomós Arkadías','GR-12',''),(20239,89,'Prefecture','Nomós Acha?as','GR-13',''),(20240,89,'Prefecture','Nomós Ileías','GR-14',''),(20241,89,'Prefecture','Nomós Korinthías','GR-15',''),(20242,89,'Prefecture','Nomós Lakonías','GR-16',''),(20243,89,'Prefecture','Nomós Messinías','GR-17',''),(20244,89,'Prefecture','Nomós Zakýnthoy','GR-21',''),(20245,89,'Prefecture','Nomós Kérkyras','GR-22',''),(20246,89,'Prefecture','Nomós Kefaloniás kai Ithákis','GR-23',''),(20247,89,'Prefecture','Nomós Lefkádas','GR-24',''),(20248,89,'Prefecture','Nomós Ártas','GR-31',''),(20249,89,'Prefecture','Nomós Thesprotías','GR-32',''),(20250,89,'Prefecture','Nomós Ioannínon','GR-33',''),(20251,89,'Prefecture','Nomós Prévezas','GR-34',''),(20252,89,'Prefecture','Nomós Kardítsas','GR-41',''),(20253,89,'Prefecture','Nomós Lárissas','GR-42',''),(20254,89,'Prefecture','Nomós Magnisías','GR-43',''),(20255,89,'Prefecture','Nomós Trikálon','GR-44',''),(20256,89,'Prefecture','Nomós Grevenón','GR-51',''),(20257,89,'Prefecture','Nomós Drámas','GR-52',''),(20258,89,'Prefecture','Nomós Imathías','GR-53',''),(20259,89,'Prefecture','Nomós Thessaloníkis','GR-54',''),(20260,89,'Prefecture','Nomós Kaválas','GR-55',''),(20261,89,'Prefecture','Nomós Kastoriás','GR-56',''),(20262,89,'Prefecture','Nomós Kilkís','GR-57',''),(20263,89,'Prefecture','Nomós Kozánis','GR-58',''),(20264,89,'Prefecture','Nomós Péllas','GR-59',''),(20265,89,'Prefecture','Nomós Pierías','GR-61',''),(20266,89,'Prefecture','Nomós Serrón','GR-62',''),(20267,89,'Prefecture','Nomós Flórinas','GR-63',''),(20268,89,'Prefecture','Nomós Chalkidikís','GR-64',''),(20269,89,'Prefecture','Nomós Évroy','GR-71',''),(20270,89,'Prefecture','Nomós Xánthis','GR-72',''),(20271,89,'Prefecture','Nomós Rodópis','GR-73',''),(20272,89,'Prefecture','Nomós Dodekanísoy','GR-81',''),(20273,89,'Prefecture','Nomós Kykládon','GR-82',''),(20274,89,'Prefecture','Nomós Lésboy','GR-83',''),(20275,89,'Prefecture','Nomós Sámoy','GR-84',''),(20276,89,'Prefecture','Nomós Chíoy','GR-85',''),(20277,89,'Prefecture','Nomós Irakleíoy','GR-91',''),(20278,89,'Prefecture','Nomós Lasithíoy','GR-92',''),(20279,89,'Prefecture','Nomós Rethýmnoy','GR-93',''),(20280,89,'Prefecture','Nomós Chaníon','GR-94',''),(20281,94,'Department','Alta Verapaz','GT-AV',''),(20282,94,'Department','Baja Verapaz','GT-BV',''),(20283,94,'Department','Chimaltenango','GT-CM',''),(20284,94,'Department','Chiquimula','GT-CQ',''),(20285,94,'Department','El Progreso','GT-PR',''),(20286,94,'Department','Escuintla','GT-ES',''),(20287,94,'Department','Guatemala','GT-GU',''),(20288,94,'Department','Huehuetenango','GT-HU',''),(20289,94,'Department','Izabal','GT-IZ',''),(20290,94,'Department','Jalapa','GT-JA',''),(20291,94,'Department','Jutiapa','GT-JU',''),(20292,94,'Department','Petén','GT-PE',''),(20293,94,'Department','Quetzaltenango','GT-QZ',''),(20294,94,'Department','Quiché','GT-QC',''),(20295,94,'Department','Retalhuleu','GT-RE',''),(20296,94,'Department','Sacatepéquez','GT-SA',''),(20297,94,'Department','San Marcos','GT-SM',''),(20298,94,'Department','Santa Rosa','GT-SR',''),(20299,94,'Department','Sololá','GT-SO',''),(20300,94,'Department','Suchitepéquez','GT-SU',''),(20301,94,'Department','Totonicapán','GT-TO',''),(20302,94,'Department','Zacapa','GT-ZA',''),(20303,97,'Region','Bafatá','GW-BA',''),(20304,97,'Region','Biombo','GW-BM',''),(20305,97,'Region','Bolama','GW-BL',''),(20306,97,'Region','Cacheu','GW-CA',''),(20307,97,'Region','Gabú','GW-GA',''),(20308,97,'Region','Oio','GW-OI',''),(20309,97,'Region','Quinara','GW-QU',''),(20310,97,'Region','Tombali','GW-TO',''),(20311,97,'Autonomous sector','Bissau','GW-BS',''),(20312,98,'Region','Barima-Waini','GY-BA',''),(20313,98,'Region','Cuyuni-Mazaruni','GY-CU',''),(20314,98,'Region','Demerara-Mahaica','GY-DE',''),(20315,98,'Region','East Berbice-Corentyne','GY-EB',''),(20316,98,'Region','Essequibo Islands-West Demerara','GY-ES',''),(20317,98,'Region','Mahaica-Berbice','GY-MA',''),(20318,98,'Region','Pomeroon-Supenaam','GY-PM',''),(20319,98,'Region','Potaro-Siparuni','GY-PT',''),(20320,98,'Region','Upper Demerara-Berbice','GY-UD',''),(20321,98,'Region','Upper Takutu-Upper Essequibo','GY-UT',''),(20322,102,'Department','Atlántida','HN-AT',''),(20323,102,'Department','Colón','HN-CL',''),(20324,102,'Department','Comayagua','HN-CM',''),(20325,102,'Department','Copán','HN-CP',''),(20326,102,'Department','Cortés','HN-CR',''),(20327,102,'Department','Choluteca','HN-CH',''),(20328,102,'Department','El Paraíso','HN-EP',''),(20329,102,'Department','Francisco Morazán','HN-FM',''),(20330,102,'Department','Gracias a Dios','HN-GD',''),(20331,102,'Department','Intibucá','HN-IN',''),(20332,102,'Department','Islas de la Bahía','HN-IB',''),(20333,102,'Department','La Paz','HN-LP',''),(20334,102,'Department','Lempira','HN-LE',''),(20335,102,'Department','Ocotepeque','HN-OC',''),(20336,102,'Department','Olancho','HN-OL',''),(20337,102,'Department','Santa Bárbara','HN-SB',''),(20338,102,'Department','Valle','HN-VA',''),(20339,102,'Department','Yoro','HN-YO',''),(20340,59,'City','Grad Zagreb','HR-21',''),(20341,59,'County','Bjelovarsko-bilogorska županija','HR-07',''),(20342,59,'County','Brodsko-posavska županija','HR-12',''),(20343,59,'County','Dubrova?ko-neretvanska županija','HR-19',''),(20344,59,'County','Istarska županija','HR-18',''),(20345,59,'County','Karlova?ka županija','HR-04',''),(20346,59,'County','Koprivni?ko-križeva?ka županija','HR-06',''),(20347,59,'County','Krapinsko-zagorska županija','HR-02',''),(20348,59,'County','Li?ko-senjska županija','HR-09',''),(20349,59,'County','Me?imurska županija','HR-20',''),(20350,59,'County','Osje?ko-baranjska županija','HR-14',''),(20351,59,'County','Požeško-slavonska županija','HR-11',''),(20352,59,'County','Primorsko-goranska županija','HR-08',''),(20353,59,'County','Sisa?ko-moslava?ka županija','HR-03',''),(20354,59,'County','Splitsko-dalmatinska županija','HR-17',''),(20355,59,'County','Šibensko-kninska županija','HR-15',''),(20356,59,'County','Varaždinska županija','HR-05',''),(20357,59,'County','Viroviti?ko-podravska županija','HR-10',''),(20358,59,'County','Vukovarsko-srijemska županija','HR-16',''),(20359,59,'County','Zadarska županija','HR-13',''),(20360,59,'County','Zagreba?ka županija','HR-01',''),(20361,99,'Department','Artibonite','HT-AR',''),(20362,99,'Department','Centre','HT-CE',''),(20363,99,'Department','Grande-Anse','HT-GA',''),(20364,99,'Department','Nord','HT-ND',''),(20365,99,'Department','Nord-Est','HT-NE',''),(20366,99,'Department','Nord-Ouest','HT-NO',''),(20367,99,'Department','Ouest','HT-OU',''),(20368,99,'Department','Sud','HT-SD',''),(20369,99,'Department','Sud-Est','HT-SE',''),(20370,104,'County','Bács-Kiskun','HU-BK',''),(20371,104,'County','Baranya','HU-BA',''),(20372,104,'County','Békés','HU-BE',''),(20373,104,'County','Borsod-Abaúj-Zemplén','HU-BZ',''),(20374,104,'County','Csongrád','HU-CS',''),(20375,104,'County','Fejér','HU-FE',''),(20376,104,'County','Gy?r-Moson-Sopron','HU-GS',''),(20377,104,'County','Hajdú-Bihar','HU-HB',''),(20378,104,'County','Heves','HU-HE',''),(20379,104,'County','Jász-Nagykun-Szolnok','HU-JN',''),(20380,104,'County','Komárom-Esztergom','HU-KE',''),(20381,104,'County','Nógrád','HU-NO',''),(20382,104,'County','Pest','HU-PE',''),(20383,104,'County','Somogy','HU-SO',''),(20384,104,'County','Szabolcs-Szatmár-Bereg','HU-SZ',''),(20385,104,'County','Tolna','HU-TO',''),(20386,104,'County','Vas','HU-VA',''),(20387,104,'County','Veszprém (county)','HU-VE',''),(20388,104,'County','Zala','HU-ZA',''),(20389,104,'City with county rights','Békéscsaba','HU-BC',''),(20390,104,'City with county rights','Debrecen','HU-DE',''),(20391,104,'City with county rights','Dunaújváros','HU-DU',''),(20392,104,'City with county rights','Eger','HU-EG',''),(20393,104,'City with county rights','Gy?r','HU-GY',''),(20394,104,'City with county rights','Hódmez?vásárhely','HU-HV',''),(20395,104,'City with county rights','Kaposvár','HU-KV',''),(20396,104,'City with county rights','Kecskemét','HU-KM',''),(20397,104,'City with county rights','Miskolc','HU-MI',''),(20398,104,'City with county rights','Nagykanizsa','HU-NK',''),(20399,104,'City with county rights','Nyíregyháza','HU-NY',''),(20400,104,'City with county rights','Pécs','HU-PS',''),(20401,104,'City with county rights','Salgótarján','HU-ST',''),(20402,104,'City with county rights','Sopron','HU-SN',''),(20403,104,'City with county rights','Szeged','HU-SD',''),(20404,104,'City with county rights','Székesfehérvár','HU-SF',''),(20405,104,'City with county rights','Szekszárd','HU-SS',''),(20406,104,'City with county rights','Szolnok','HU-SK',''),(20407,104,'City with county rights','Szombathely','HU-SH',''),(20408,104,'City with county rights','Tatabánya','HU-TB',''),(20409,104,'City with county rights','Veszprém','HU-VM',''),(20410,104,'City with county rights','Zalaegerszeg','HU-ZE',''),(20411,104,'Capital city','Budapest','HU-BU',''),(20412,107,'Geographical unit','Papua','ID IJ',''),(20413,107,'Geographical unit','Jawa','ID JW',''),(20414,107,'Geographical unit','Kalimantan','ID KA',''),(20415,107,'Geographical unit','Maluku','ID MA',''),(20416,107,'Geographical unit','Nusa Tenggara','ID NU',''),(20417,107,'Geographical unit','Sulawesi','ID SL',''),(20418,107,'Geographical unit','Sumatera','ID SM',''),(20419,107,'Autonomous Province','Aceh','ID-AC',''),(20420,107,'Province','Bali','ID-BA',''),(20421,107,'Province','Bangka Belitung','ID-BB',''),(20422,107,'Province','Banten','ID-BT',''),(20423,107,'Province','Bengkulu','ID-BE',''),(20424,107,'Province','Gorontalo','ID-GO',''),(20425,107,'Province','Jambi','ID-JA',''),(20426,107,'Province','Jawa Barat','ID-JB',''),(20427,107,'Province','Jawa Tengah','ID-JT',''),(20428,107,'Province','Jawa Timur','ID-JI',''),(20429,107,'Province','Kalimantan Barat','ID-KB',''),(20430,107,'Province','Kalimantan Tengah','ID-KT',''),(20431,107,'Province','Kalimantan Selatan','ID-KS',''),(20432,107,'Province','Kalimantan Timur','ID-KI',''),(20433,107,'Province','Kepulauan Riau','ID-KR',''),(20434,107,'Province','Lampung','ID-LA',''),(20435,107,'Province','Maluku','ID-MA',''),(20436,107,'Province','Maluku Utara','ID-MU',''),(20437,107,'Province','Nusa Tenggara Barat','ID-NB',''),(20438,107,'Province','Nusa Tenggara Timur','ID-NT',''),(20439,107,'Province','Papua','ID-PA',''),(20440,107,'Province','Riau','ID-RI',''),(20441,107,'Province','Sulawesi Barat','ID-SR',''),(20442,107,'Province','Sulawesi Selatan','ID-SN',''),(20443,107,'Province','Sulawesi Tengah','ID-ST',''),(20444,107,'Province','Sulawesi Tenggara','ID-SG',''),(20445,107,'Province','Sulawesi Utara','ID-SA',''),(20446,107,'Province','Sumatra Barat','ID-SB',''),(20447,107,'Province','Sumatra Selatan','ID-SS',''),(20448,107,'Province','Sumatera Utara','ID-SU',''),(20449,107,'Special District','Jakarta Raya','ID-JK',''),(20450,107,'Special Region','Yogyakarta','ID-YO',''),(20451,110,'Province','Connacht','IE C',''),(20452,110,'Province','Leinster','IE L',''),(20453,110,'Province','Munster','IE M',''),(20454,110,'Province','Ulster','IE U',''),(20455,110,'County','Cork','IE-C',''),(20456,110,'County','Clare','IE-CE',''),(20457,110,'County','Cavan','IE-CN',''),(20458,110,'County','Carlow','IE-CW',''),(20459,110,'County','Dublin','IE-D',''),(20460,110,'County','Donegal','IE-DL',''),(20461,110,'County','Galway','IE-G',''),(20462,110,'County','Kildare','IE-KE',''),(20463,110,'County','Kilkenny','IE-KK',''),(20464,110,'County','Kerry','IE-KY',''),(20465,110,'County','Longford','IE-LD',''),(20466,110,'County','Louth','IE-LH',''),(20467,110,'County','Limerick','IE-LK',''),(20468,110,'County','Leitrim','IE-LM',''),(20469,110,'County','Laois','IE-LS',''),(20470,110,'County','Meath','IE-MH',''),(20471,110,'County','Monaghan','IE-MN',''),(20472,110,'County','Mayo','IE-MO',''),(20473,110,'County','Offaly','IE-OY',''),(20474,110,'County','Roscommon','IE-RN',''),(20475,110,'County','Sligo','IE-SO',''),(20476,110,'County','Tipperary','IE-TA',''),(20477,110,'County','Waterford','IE-WD',''),(20478,110,'County','Westmeath','IE-WH',''),(20479,110,'County','Wicklow','IE-WW',''),(20480,110,'County','Wexford','IE-WX',''),(20481,112,'District','HaDarom','IL-D',''),(20482,112,'District','HaMerkaz','IL-M',''),(20483,112,'District','HaZafon','IL-Z',''),(20484,112,'District','Hefa','IL-HA',''),(20485,112,'District','Tel-Aviv','IL-TA',''),(20486,112,'District','Yerushalayim Al Quds','IL-JM',''),(20487,106,'State','Andhra Pradesh','IN-AP',''),(20488,106,'State','Arun?chal Pradesh','IN-AR',''),(20489,106,'State','Assam','IN-AS',''),(20490,106,'State','Bih?r','IN-BR',''),(20491,106,'State','Chhatt?sgarh','IN-CT',''),(20492,106,'State','Goa','IN-GA',''),(20493,106,'State','Gujar?t','IN-GJ',''),(20494,106,'State','Hary?na','IN-HR',''),(20495,106,'State','Him?chal Pradesh','IN-HP',''),(20496,106,'State','Jammu and Kashm?r','IN-JK',''),(20497,106,'State','Jharkhand','IN-JH',''),(20498,106,'State','Karn?taka','IN-KA',''),(20499,106,'State','Kerala','IN-KL',''),(20500,106,'State','Madhya Pradesh','IN-MP',''),(20501,106,'State','Mah?r?shtra','IN-MH',''),(20502,106,'State','Manipur','IN-MN',''),(20503,106,'State','Megh?laya','IN-ML',''),(20504,106,'State','Mizoram','IN-MZ',''),(20505,106,'State','N?g?land','IN-NL',''),(20506,106,'State','Orissa','IN-OR',''),(20507,106,'State','Punjab','IN-PB',''),(20508,106,'State','R?jasth?n','IN-RJ',''),(20509,106,'State','Sikkim','IN-SK',''),(20510,106,'State','Tamil N?du','IN-TN',''),(20511,106,'State','Tripura','IN-TR',''),(20512,106,'State','Uttaranchal','IN-UL',''),(20513,106,'State','Uttar Pradesh','IN-UP',''),(20514,106,'State','West Bengal','IN-WB',''),(20515,106,'Union territory','Andaman and Nicobar Islands','IN-AN',''),(20516,106,'Union territory','Chand?garh','IN-CH',''),(20517,106,'Union territory','D?dra and Nagar Haveli','IN-DN',''),(20518,106,'Union territory','Dam?n and Diu','IN-DD',''),(20519,106,'Union territory','Delhi','IN-DL',''),(20520,106,'Union territory','Lakshadweep','IN-LD',''),(20521,106,'Union territory','Pondicherry','IN-PY',''),(20522,109,'Governorate','Al Anbar','IQ-AN',''),(20523,109,'Governorate','Al Basrah','IQ-BA',''),(20524,109,'Governorate','Al Muthanna','IQ-MU',''),(20525,109,'Governorate','Al Qadisiyah','IQ-QA',''),(20526,109,'Governorate','An Najef','IQ-NA',''),(20527,109,'Governorate','Arbil','IQ-AR',''),(20528,109,'Governorate','As Sulaymaniyah','IQ-SW',''),(20529,109,'Governorate','At Ta\'mim','IQ-TS',''),(20530,109,'Governorate','Babil','IQ-BB',''),(20531,109,'Governorate','Baghdad','IQ-BG',''),(20532,109,'Governorate','Dahuk','IQ-DA',''),(20533,109,'Governorate','Dhi Qar','IQ-DQ',''),(20534,109,'Governorate','Diyala','IQ-DI',''),(20535,109,'Governorate','Karbala\'','IQ-KA',''),(20536,109,'Governorate','Maysan','IQ-MA',''),(20537,109,'Governorate','Ninawa','IQ-NI',''),(20538,109,'Governorate','Salah ad Din','IQ-SD',''),(20539,109,'Governorate','Wasit','IQ-WA',''),(20540,108,'Province','Ardab?l','IR-03',''),(20541,108,'Province','?zarb?yj?n-e Gharb?','IR-02',''),(20542,108,'Province','?zarb?yj?n-e Sharq?','IR-01',''),(20543,108,'Province','B?shehr','IR-06',''),(20544,108,'Province','Chah?r Mah?ll va Bakht??r?','IR-08',''),(20545,108,'Province','E?fah?n','IR-04',''),(20546,108,'Province','F?rs','IR-14',''),(20547,108,'Province','G?l?n','IR-19',''),(20548,108,'Province','Golest?n','IR-27',''),(20549,108,'Province','Hamad?n','IR-24',''),(20550,108,'Province','Hormozg?n','IR-23',''),(20551,108,'Province','?l?m','IR-05',''),(20552,108,'Province','Kerm?n','IR-15',''),(20553,108,'Province','Kerm?nsh?h','IR-17',''),(20554,108,'Province','Khor?s?n-e Jan?b?','IR-29',''),(20555,108,'Province','Khor?s?n-e Razav?','IR-30',''),(20556,108,'Province','Khor?s?n-e Shem?l?','IR-31',''),(20557,108,'Province','Kh?zest?n','IR-10',''),(20558,108,'Province','Kohg?l?yeh va B?yer Ahmad','IR-18',''),(20559,108,'Province','Kordest?n','IR-16',''),(20560,108,'Province','Lorest?n','IR-20',''),(20561,108,'Province','Markaz?','IR-22',''),(20562,108,'Province','M?zandar?n','IR-21',''),(20563,108,'Province','Qazv?n','IR-28',''),(20564,108,'Province','Qom','IR-26',''),(20565,108,'Province','Semn?n','IR-12',''),(20566,108,'Province','S?st?n va Bal?chest?n','IR-13',''),(20567,108,'Province','Tehr?n','IR-07',''),(20568,108,'Province','Yazd','IR-25',''),(20569,108,'Province','Zanj?n','IR-11',''),(20570,105,'Region','Austurland','IS-7',''),(20571,105,'Region','Höfuðborgarsvæðið','IS-1',''),(20572,105,'Region','Norðurland eystra','IS-6',''),(20573,105,'Region','Norðurland vestra','IS-5',''),(20574,105,'Region','Suðurland','IS-8',''),(20575,105,'Region','Suðurnes','IS-2',''),(20576,105,'Region','Vestfirðir','IS-4',''),(20577,105,'Region','Vesturland','IS-3',''),(20578,105,'City','Reykjavík','IS-0',''),(20579,113,'Region','Abruzzo','IT 65',''),(20580,113,'Region','Basilicata','IT 77',''),(20581,113,'Region','Calabria','IT 78',''),(20582,113,'Region','Campania','IT 72',''),(20583,113,'Region','Emilia-Romagna','IT 45',''),(20584,113,'Region','Friuli-Venezia Giulia','IT 36',''),(20585,113,'Region','Lazio','IT 62',''),(20586,113,'Region','Liguria','IT 42',''),(20587,113,'Region','Lombardia','IT 25',''),(20588,113,'Region','Marche','IT 57',''),(20589,113,'Region','Molise','IT 67',''),(20590,113,'Region','Piemonte','IT 21',''),(20591,113,'Region','Puglia','IT 75',''),(20592,113,'Region','Sardegna','IT 88',''),(20593,113,'Region','Sicilia','IT 82',''),(20594,113,'Region','Toscana','IT 52',''),(20595,113,'Region','Trentino-Alto Adige','IT 32',''),(20596,113,'Region','Umbria','IT 55',''),(20597,113,'Region','Valle d\'Aosta','IT 23',''),(20598,113,'Region','Veneto','IT 34',''),(20599,113,'Province','Agrigento','IT-AG',''),(20600,113,'Province','Alessandria','IT-AL',''),(20601,113,'Province','Ancona','IT-AN',''),(20602,113,'Province','Aosta','IT-AO',''),(20603,113,'Province','Arezzo','IT-AR',''),(20604,113,'Province','Ascoli Piceno','IT-AP',''),(20605,113,'Province','Asti','IT-AT',''),(20606,113,'Province','Avellino','IT-AV',''),(20607,113,'Province','Bari','IT-BA',''),(20608,113,'Province','Belluno','IT-BL',''),(20609,113,'Province','Benevento','IT-BN',''),(20610,113,'Province','Bergamo','IT-BG',''),(20611,113,'Province','Biella','IT-BI',''),(20612,113,'Province','Bologna','IT-BO',''),(20613,113,'Province','Bolzano','IT-BZ',''),(20614,113,'Province','Brescia','IT-BS',''),(20615,113,'Province','Brindisi','IT-BR',''),(20616,113,'Province','Cagliari','IT-CA',''),(20617,113,'Province','Caltanissetta','IT-CL',''),(20618,113,'Province','Campobasso','IT-CB',''),(20619,113,'Province','Carbonia-Iglesias','IT-CI',''),(20620,113,'Province','Caserta','IT-CE',''),(20621,113,'Province','Catania','IT-CT',''),(20622,113,'Province','Catanzaro','IT-CZ',''),(20623,113,'Province','Chieti','IT-CH',''),(20624,113,'Province','Como','IT-CO',''),(20625,113,'Province','Cosenza','IT-CS',''),(20626,113,'Province','Cremona','IT-CR',''),(20627,113,'Province','Crotone','IT-KR',''),(20628,113,'Province','Cuneo','IT-CN',''),(20629,113,'Province','Enna','IT-EN',''),(20630,113,'Province','Ferrara','IT-FE',''),(20631,113,'Province','Firenze','IT-FI',''),(20632,113,'Province','Foggia','IT-FG',''),(20633,113,'Province','Forlì','IT-FO',''),(20634,113,'Province','Frosinone','IT-FR',''),(20635,113,'Province','Genova','IT-GE',''),(20636,113,'Province','Gorizia','IT-GO',''),(20637,113,'Province','Grosseto','IT-GR',''),(20638,113,'Province','Imperia','IT-IM',''),(20639,113,'Province','Isernia','IT-IS',''),(20640,113,'Province','La Spezia','IT-SP',''),(20641,113,'Province','L\'Aquila','IT-AQ',''),(20642,113,'Province','Latina','IT-LT',''),(20643,113,'Province','Lecce','IT-LE',''),(20644,113,'Province','Lecco','IT-LC',''),(20645,113,'Province','Livorno','IT-LI',''),(20646,113,'Province','Lodi','IT-LO',''),(20647,113,'Province','Lucca','IT-LU',''),(20648,113,'Province','Macerata','IT-SC',''),(20649,113,'Province','Mantova','IT-MN',''),(20650,113,'Province','Massa-Carrara','IT-MS',''),(20651,113,'Province','Matera','IT-MT',''),(20652,113,'Province','Medio Campidano','IT-VS',''),(20653,113,'Province','Messina','IT-ME',''),(20654,113,'Province','Milano','IT-MI',''),(20655,113,'Province','Modena','IT-MO',''),(20656,113,'Province','Napoli','IT-NA',''),(20657,113,'Province','Novara','IT-NO',''),(20658,113,'Province','Nuoro','IT-NU',''),(20659,113,'Province','Ogliastra','IT-OG',''),(20660,113,'Province','Olbia-Tempio','IT-OT',''),(20661,113,'Province','Oristano','IT-OR',''),(20662,113,'Province','Padova','IT-PD',''),(20663,113,'Province','Palermo','IT-PA',''),(20664,113,'Province','Parma','IT-PR',''),(20665,113,'Province','Pavia','IT-PV',''),(20666,113,'Province','Perugia','IT-PG',''),(20667,113,'Province','Pesaro e Urbino','IT-PS',''),(20668,113,'Province','Pescara','IT-PE',''),(20669,113,'Province','Piacenza','IT-PC',''),(20670,113,'Province','Pisa','IT-PI',''),(20671,113,'Province','Pistoia','IT-PT',''),(20672,113,'Province','Pordenone','IT-PN',''),(20673,113,'Province','Potenza','IT-PZ',''),(20674,113,'Province','Prato','IT-PO',''),(20675,113,'Province','Ragusa','IT-RG',''),(20676,113,'Province','Ravenna','IT-RA',''),(20677,113,'Province','Reggio Calabria','IT-RC',''),(20678,113,'Province','Reggio Emilia','IT-RE',''),(20679,113,'Province','Rieti','IT-RI',''),(20680,113,'Province','Rimini','IT-RN',''),(20681,113,'Province','Roma','IT-RM',''),(20682,113,'Province','Rovigo','IT-RO',''),(20683,113,'Province','Salerno','IT-SA',''),(20684,113,'Province','Sassari','IT-SS',''),(20685,113,'Province','Savona','IT-SV',''),(20686,113,'Province','Siena','IT-SI',''),(20687,113,'Province','Siracusa','IT-SR',''),(20688,113,'Province','Sondrio','IT-SO',''),(20689,113,'Province','Taranto','IT-TA',''),(20690,113,'Province','Teramo','IT-TE',''),(20691,113,'Province','Terni','IT-TR',''),(20692,113,'Province','Torino','IT-TO',''),(20693,113,'Province','Trapani','IT-TP',''),(20694,113,'Province','Trento','IT-TN',''),(20695,113,'Province','Treviso','IT-TV',''),(20696,113,'Province','Trieste','IT-TS',''),(20697,113,'Province','Udine','IT-UD',''),(20698,113,'Province','Varese','IT-VA',''),(20699,113,'Province','Venezia','IT-VE',''),(20700,113,'Province','Verbano-Cusio-Ossola','IT-VB',''),(20701,113,'Province','Vercelli','IT-VC',''),(20702,113,'Province','Verona','IT-VR',''),(20703,113,'Province','Vibo Valentia','IT-VV',''),(20704,113,'Province','Vicenza','IT-VI',''),(20705,113,'Province','Viterbo','IT-VT',''),(20706,114,'Parish','Clarendon','JM-13',''),(20707,114,'Parish','Hanover','JM-09',''),(20708,114,'Parish','Kingston','JM-01',''),(20709,114,'Parish','Manchester','JM-12',''),(20710,114,'Parish','Portland','JM-04',''),(20711,114,'Parish','Saint Andrew','JM-02',''),(20712,114,'Parish','Saint Ann','JM-06',''),(20713,114,'Parish','Saint Catherine','JM-14',''),(20714,114,'Parish','Saint Elizabeth','JM-11',''),(20715,114,'Parish','Saint James','JM-08',''),(20716,114,'Parish','Saint Mary','JM-05',''),(20717,114,'Parish','Saint Thomas','JM-03',''),(20718,114,'Parish','Trelawny','JM-07',''),(20719,114,'Parish','Westmoreland','JM-10',''),(20720,117,'Governorate','`Ajlun','JO-AJ',''),(20721,117,'Governorate','Al `Aqabah','JO-AQ',''),(20722,117,'Governorate','Al Balq?\'','JO-BA',''),(20723,117,'Governorate','Al Karak','JO-KA',''),(20724,117,'Governorate','Al Mafraq','JO-MA',''),(20725,117,'Governorate','Amman','JO-AM',''),(20726,117,'Governorate','A? ?af?lah','JO-AT',''),(20727,117,'Governorate','Az Zarq?\'','JO-AZ',''),(20728,117,'Governorate','Irbid','JO-JR',''),(20729,117,'Governorate','Jarash','JO-JA',''),(20730,117,'Governorate','Ma`?n','JO-MN',''),(20731,117,'Governorate','M?dab?','JO-MD',''),(20732,115,'Prefecture','Aichi','JP-23',''),(20733,115,'Prefecture','Akita','JP-05',''),(20734,115,'Prefecture','Aomori','JP-02',''),(20735,115,'Prefecture','Chiba','JP-12',''),(20736,115,'Prefecture','Ehime','JP-38',''),(20737,115,'Prefecture','Fukui','JP-18',''),(20738,115,'Prefecture','Fukuoka','JP-40',''),(20739,115,'Prefecture','Fukushima','JP-07',''),(20740,115,'Prefecture','Gifu','JP-21',''),(20741,115,'Prefecture','Gunma','JP-10',''),(20742,115,'Prefecture','Hiroshima','JP-34',''),(20743,115,'Prefecture','Hokkaido','JP-01',''),(20744,115,'Prefecture','Hyogo','JP-28',''),(20745,115,'Prefecture','Ibaraki','JP-08',''),(20746,115,'Prefecture','Ishikawa','JP-17',''),(20747,115,'Prefecture','Iwate','JP-03',''),(20748,115,'Prefecture','Kagawa','JP-37',''),(20749,115,'Prefecture','Kagoshima','JP-46',''),(20750,115,'Prefecture','Kanagawa','JP-14',''),(20751,115,'Prefecture','Kochi','JP-39',''),(20752,115,'Prefecture','Kumamoto','JP-43',''),(20753,115,'Prefecture','Kyoto','JP-26',''),(20754,115,'Prefecture','Mie','JP-24',''),(20755,115,'Prefecture','Miyagi','JP-04',''),(20756,115,'Prefecture','Miyazaki','JP-45',''),(20757,115,'Prefecture','Nagano','JP-20',''),(20758,115,'Prefecture','Nagasaki','JP-42',''),(20759,115,'Prefecture','Nara','JP-29',''),(20760,115,'Prefecture','Niigata','JP-15',''),(20761,115,'Prefecture','Oita','JP-44',''),(20762,115,'Prefecture','Okayama','JP-33',''),(20763,115,'Prefecture','Okinawa','JP-47',''),(20764,115,'Prefecture','Osaka','JP-27',''),(20765,115,'Prefecture','Saga','JP-41',''),(20766,115,'Prefecture','Saitama','JP-11',''),(20767,115,'Prefecture','Shiga','JP-25',''),(20768,115,'Prefecture','Shimane','JP-32',''),(20769,115,'Prefecture','Shizuoka','JP-22',''),(20770,115,'Prefecture','Tochigi','JP-09',''),(20771,115,'Prefecture','Tokushima','JP-36',''),(20772,115,'Prefecture','Tokyo','JP-13',''),(20773,115,'Prefecture','Tottori','JP-31',''),(20774,115,'Prefecture','Toyama','JP-16',''),(20775,115,'Prefecture','Wakayama','JP-30',''),(20776,115,'Prefecture','Yamagata','JP-06',''),(20777,115,'Prefecture','Yamaguchi','JP-35',''),(20778,115,'Prefecture','Yamanashi','JP-19',''),(20779,119,'Province','Nairobi Municipality','KE-110',''),(20780,119,'Province','Central','KE-200',''),(20781,119,'Province','Coast','KE-300',''),(20782,119,'Province','Eastern','KE-400',''),(20783,119,'Province','North-Eastern Kaskazini Mashariki','KE-500',''),(20784,119,'Province','Rift Valley','KE-700',''),(20785,119,'Province','Western Magharibi','KE-900',''),(20786,124,'City','Bishkek','KG-GB',''),(20787,124,'Region','Batken','KG-B',''),(20788,124,'Region','Chü','KG-C',''),(20789,124,'Region','Jalal-Abad','KG-J',''),(20790,124,'Region','Naryn','KG-N',''),(20791,124,'Region','Osh','KG-O',''),(20792,124,'Region','Talas','KG-T',''),(20793,124,'Region','Ysyk-Köl','KG-Y',''),(20794,41,'Autonomous municipality','Krong Kaeb','KH-23',''),(20795,41,'Autonomous municipality','Krong Pailin','KH-24',''),(20796,41,'Autonomous municipality','Krong Preah Sihanouk','KH-18',''),(20797,41,'Autonomous municipality','Phnom Penh','KH-12',''),(20798,41,'Province','Battambang','KH-2',''),(20799,41,'Province','Banteay Mean Chey','KH-1',''),(20800,41,'Province','Kampong Cham','KH-3',''),(20801,41,'Province','Kampong Chhnang','KH-4',''),(20802,41,'Province','Kampong Speu','KH-5',''),(20803,41,'Province','Kampong Thom','KH-6',''),(20804,41,'Province','Kampot','KH-7',''),(20805,41,'Province','Kandal','KH-8',''),(20806,41,'Province','Kach Kong','KH-9',''),(20807,41,'Province','Krachoh','KH-10',''),(20808,41,'Province','Mondol Kiri','KH-11',''),(20809,41,'Province','Otdar Mean Chey','KH-22',''),(20810,41,'Province','Pousaat','KH-15',''),(20811,41,'Province','Preah Vihear','KH-13',''),(20812,41,'Province','Prey Veaeng','KH-14',''),(20813,41,'Province','Rotanak Kiri','KH-16',''),(20814,41,'Province','Siem Reab','KH-17',''),(20815,41,'Province','Stueng Traeng','KH-19',''),(20816,41,'Province','Svaay Rieng','KH-20',''),(20817,41,'Province','Taakaev','KH-21',''),(20818,120,'Island group','Gilbert Islands','KI-G',''),(20819,120,'Island group','Line Islands','KI-L',''),(20820,120,'Island group','Phoenix Islands','KI-P',''),(20821,190,'State','Saint Kitts','KN K',''),(20822,190,'State','Nevis','KN N',''),(20823,190,'Parish','Christ Church Nichola Town','KN-01',''),(20824,190,'Parish','Saint Anne Sandy Point','KN-02',''),(20825,190,'Parish','Saint George Basseterre','KN-03',''),(20826,190,'Parish','Saint George Gingerland','KN-04',''),(20827,190,'Parish','Saint James Windward','KN-05',''),(20828,190,'Parish','Saint John Capisterre','KN-06',''),(20829,190,'Parish','Saint John Figtree','KN-07',''),(20830,190,'Parish','Saint Mary Cayon','KN-08',''),(20831,190,'Parish','Saint Paul Capisterre','KN-09',''),(20832,190,'Parish','Saint Paul Charlestown','KN-10',''),(20833,190,'Parish','Saint Peter Basseterre','KN-11',''),(20834,190,'Parish','Saint Thomas Lowland','KN-12',''),(20835,190,'Parish','Saint Thomas Middle Island','KN-13',''),(20836,190,'Parish','Trinity Palmetto Point','KN-15',''),(20837,53,'Autonomous island','Anjouan Ndzouani','KM-A',''),(20838,53,'Autonomous island','Grande Comore Ngazidja','KM-G',''),(20839,53,'Autonomous island','Mohéli Moili','KM-M',''),(20840,121,'Province','Chagang-do','KP-CHA',''),(20841,121,'Province','Hamgyongbuk-do','KP-HAB',''),(20842,121,'Province','Hamgyongnam-do','KP-HAN',''),(20843,121,'Province','Hwanghaebuk-do','KP-HWB',''),(20844,121,'Province','Hwanghaenam-do','KP-HWN',''),(20845,121,'Province','Kangwon-do','KP-KAN',''),(20846,121,'Province','Pyonganbuk-do','KP-PYB',''),(20847,121,'Province','Pyongannam-do','KP-PYN',''),(20848,121,'Province','Yanggang-do','KP-YAN',''),(20849,121,'Special city','Kaesong-si','KP-KAE',''),(20850,121,'Special city','Najin Sonbong-si','KP-NAJ',''),(20851,121,'Special city','Nampo-si','KP-NAM',''),(20852,121,'Special city','Pyongyang-si','KP-PYO',''),(20853,122,'Capital Metropolitan City','Seoul Teugbyeolsi','KR-11',''),(20854,122,'Metropolitan cities','Busan Gwang\'yeogsi','KR-26',''),(20855,122,'Metropolitan cities','Daegu Gwang\'yeogsi','KR-27',''),(20856,122,'Metropolitan cities','Daejeon Gwang\'yeogsi','KR-30',''),(20857,122,'Metropolitan cities','Gwangju Gwang\'yeogsi','KR-29',''),(20858,122,'Metropolitan cities','Incheon Gwang\'yeogsi','KR-28',''),(20859,122,'Metropolitan cities','Ulsan Gwang\'yeogsi','KR-31',''),(20860,122,'Province','Chungcheongbukdo','KR-43',''),(20861,122,'Province','Chungcheongnamdo','KR-44',''),(20862,122,'Province','Gang\'weondo','KR-42',''),(20863,122,'Province','Gyeonggido','KR-41',''),(20864,122,'Province','Gyeongsangbukdo','KR-47',''),(20865,122,'Province','Gyeongsangnamdo','KR-48',''),(20866,122,'Province','Jejudo','KR-49',''),(20867,122,'Province','Jeonrabukdo','KR-45',''),(20868,122,'Province','Jeonranamdo','KR-46',''),(20869,123,'Governorate','Al Ahmadi','KW-AH',''),(20870,123,'Governorate','Al Farw?n?yah','KW-FA',''),(20871,123,'Governorate','Al Jahrah','KW-JA',''),(20872,123,'Governorate','Al Kuwayt','KW-KU',''),(20873,123,'Governorate','Hawall?','KW-HA',''),(20874,118,'City','Almaty','KZ-ALA',''),(20875,118,'City','Astana','KZ-AST',''),(20876,118,'Region','Almaty oblysy','KZ-ALM',''),(20877,118,'Region','Aqmola oblysy','KZ-AKM',''),(20878,118,'Region','Aqtöbe oblysy','KZ-AKT',''),(20879,118,'Region','Atyra? oblysy','KZ-ATY',''),(20880,118,'Region','Batys Quzaqstan oblysy','KZ-ZAP',''),(20881,118,'Region','Mangghysta? oblysy','KZ-MAN',''),(20882,118,'Region','Ongtüstik Qazaqstan oblysy','KZ-YUZ',''),(20883,118,'Region','Pavlodar oblysy','KZ-PAV',''),(20884,118,'Region','Qaraghandy oblysy','KZ-KAR',''),(20885,118,'Region','Qostanay oblysy','KZ-KUS',''),(20886,118,'Region','Qyzylorda oblysy','KZ-KZY',''),(20887,118,'Region','Shyghys Qazaqstan oblysy','KZ-VOS',''),(20888,118,'Region','Soltüstik Quzaqstan oblysy','KZ-SEV',''),(20889,118,'Region','Zhambyl oblysy','KZ-ZHA',''),(20890,125,'Prefecture','Vientiane','LA-VT',''),(20891,125,'Province','Attapu','LA-AT',''),(20892,125,'Province','Bokèo','LA-BK',''),(20893,125,'Province','Bolikhamxai','LA-BL',''),(20894,125,'Province','Champasak','LA-CH',''),(20895,125,'Province','Houaphan','LA-HO',''),(20896,125,'Province','Khammouan','LA-KH',''),(20897,125,'Province','Louang Namtha','LA-LM',''),(20898,125,'Province','Louangphabang','LA-LP',''),(20899,125,'Province','Oudômxai','LA-OU',''),(20900,125,'Province','Phôngsali','LA-PH',''),(20901,125,'Province','Salavan','LA-SL',''),(20902,125,'Province','Savannakhét','LA-SV',''),(20903,125,'Province','Vientiane','LA-VI',''),(20904,125,'Province','Xaignabouli','LA-XA',''),(20905,125,'Province','Xékong','LA-XE',''),(20906,125,'Province','Xiangkhoang','LA-XI',''),(20907,125,'Special zone','Xiasômboun','LA-XN',''),(20908,131,'Commune','Balzers','LI-01',''),(20909,131,'Commune','Eschen','LI-02',''),(20910,131,'Commune','Gamprin','LI-03',''),(20911,131,'Commune','Mauren','LI-04',''),(20912,131,'Commune','Planken','LI-05',''),(20913,131,'Commune','Ruggell','LI-06',''),(20914,131,'Commune','Schaan','LI-07',''),(20915,131,'Commune','Schellenberg','LI-08',''),(20916,131,'Commune','Triesen','LI-09',''),(20917,131,'Commune','Triesenberg','LI-10',''),(20918,131,'Commune','Vaduz','LI-11',''),(20919,127,'Governorate','Aakkâr','LB-AK',''),(20920,127,'Governorate','Baalbek-Hermel','LB-BH',''),(20921,127,'Governorate','Béqaa','LB-BI',''),(20922,127,'Governorate','Beyrouth','LB-BA',''),(20923,127,'Governorate','Liban-Nord','LB-AS',''),(20924,127,'Governorate','Liban-Sud','LB-JA',''),(20925,127,'Governorate','Mont-Liban','LB-JL',''),(20926,127,'Governorate','Nabatîyé','LB-NA',''),(20927,212,'District','Ampara','LK-52',''),(20928,212,'District','Anuradhapura','LK-71',''),(20929,212,'District','Badulla','LK-81',''),(20930,212,'District','Batticaloa','LK-51',''),(20931,212,'District','Colombo','LK-11',''),(20932,212,'District','Galle','LK-31',''),(20933,212,'District','Gampaha','LK-12',''),(20934,212,'District','Hambantota','LK-33',''),(20935,212,'District','Jaffna','LK-41',''),(20936,212,'District','Kalutara','LK-13',''),(20937,212,'District','Kandy','LK-21',''),(20938,212,'District','Kegalla','LK-92',''),(20939,212,'District','Kilinochchi','LK-42',''),(20940,212,'District','Kurunegala','LK-61',''),(20941,212,'District','Mannar','LK-43',''),(20942,212,'District','Matale','LK-22',''),(20943,212,'District','Matara','LK-32',''),(20944,212,'District','Monaragala','LK-82',''),(20945,212,'District','Mullaittivu','LK-45',''),(20946,212,'District','Nuwara Eliya','LK-23',''),(20947,212,'District','Polonnaruwa','LK-72',''),(20948,212,'District','Puttalum','LK-62',''),(20949,212,'District','Ratnapura','LK-91',''),(20950,212,'District','Trincomalee','LK-53',''),(20951,212,'District','Vavuniya','LK-44',''),(20952,129,'County','Bomi','LR-BM',''),(20953,129,'County','Bong','LR-BG',''),(20954,129,'County','Grand Bassa','LR-GB',''),(20955,129,'County','Grand Cape Mount','LR-CM',''),(20956,129,'County','Grand Gedeh','LR-GG',''),(20957,129,'County','Grand Kru','LR-GK',''),(20958,129,'County','Lofa','LR-LO',''),(20959,129,'County','Margibi','LR-MG',''),(20960,129,'County','Maryland','LR-MY',''),(20961,129,'County','Montserrado','LR-MO',''),(20962,129,'County','Nimba','LR-NI',''),(20963,129,'County','Rivercess','LR-RI',''),(20964,129,'County','Sinoe','LR-SI',''),(20965,128,'District','Berea','LS-D',''),(20966,128,'District','Butha-Buthe','LS-B',''),(20967,128,'District','Leribe','LS-C',''),(20968,128,'District','Mafeteng','LS-E',''),(20969,128,'District','Maseru','LS-A',''),(20970,128,'District','Mohale\'s Hoek','LS-F',''),(20971,128,'District','Mokhotlong','LS-J',''),(20972,128,'District','Qacha\'s Nek','LS-H',''),(20973,128,'District','Quthing','LS-G',''),(20974,128,'District','Thaba-Tseka','LS-K',''),(20975,132,'County','Alytaus Apskritis','LT-AL',''),(20976,132,'County','Kauno Apskritis','LT-KU',''),(20977,132,'County','Klaip?dos Apskritis','LT-KL',''),(20978,132,'County','Marijampol?s Apskritis','LT-MR',''),(20979,132,'County','Panev?žio Apskritis','LT-PN',''),(20980,132,'County','Šiauli? Apskritis','LT-SA',''),(20981,132,'County','Tauragés Apskritis','LT-TA',''),(20982,132,'County','Telši? Apskritis','LT-TE',''),(20983,132,'County','Utenos Apskritis','LT-UT',''),(20984,132,'County','Vilniaus Apskritis','LT-VL',''),(20985,133,'District','Diekirch','LU-D',''),(20986,133,'District','Grevenmacher','LU-G',''),(20987,133,'District','Luxembourg','LU-L',''),(20988,126,'District','Aizkraukle','LV-AI',''),(20989,126,'District','Al?ksne','LV-AL',''),(20990,126,'District','Balvi','LV-BL',''),(20991,126,'District','Bauska','LV-BU',''),(20992,126,'District','C?sis','LV-CE',''),(20993,126,'District','Daugavpils','LV-DA',''),(20994,126,'District','Dobele','LV-DO',''),(20995,126,'District','Gulbene','LV-GU',''),(20996,126,'District','J?kabpils','LV-JK',''),(20997,126,'District','Jelgava','LV-JL',''),(20998,126,'District','Kr?slava','LV-KR',''),(20999,126,'District','Kuld?ga','LV-KU',''),(21000,126,'District','Liep?ja','LV-LE',''),(21001,126,'District','Limbaži','LV-LM',''),(21002,126,'District','Ludza','LV-LU',''),(21003,126,'District','Madona','LV-MA',''),(21004,126,'District','Ogre','LV-OG',''),(21005,126,'District','Prei?i','LV-PR',''),(21006,126,'District','R?zekne','LV-RE',''),(21007,126,'District','R?ga','LV-RI',''),(21008,126,'District','Saldus','LV-SA',''),(21009,126,'District','Talsi','LV-TA',''),(21010,126,'District','Tukums','LV-TU',''),(21011,126,'District','Valka','LV-VK',''),(21012,126,'District','Valmiera','LV-VM',''),(21013,126,'District','Ventspils','LV-VE',''),(21014,126,'City','Daugavpils','LV-DGV',''),(21015,126,'City','Jelgava','LV-JEL',''),(21016,126,'City','J?rmala','LV-JUR',''),(21017,126,'City','Liep?ja','LV-LPX',''),(21018,126,'City','R?zekne','LV-REZ',''),(21019,126,'City','R?ga','LV-RIX',''),(21020,126,'City','Ventspils','LV-VEN',''),(21021,130,'Municipality','Ajd?biy?','LY-AJ',''),(21022,130,'Municipality','Al Bu?n?n','LY-BU',''),(21023,130,'Municipality','Al ?iz?m al Akh?ar','LY-HZ',''),(21024,130,'Municipality','Al Jabal al Akh?ar','LY-JA',''),(21025,130,'Municipality','Al Jif?rah','LY-JI',''),(21026,130,'Municipality','Al Jufrah','LY-JU',''),(21027,130,'Municipality','Al Kufrah','LY-KF',''),(21028,130,'Municipality','Al Marj','LY-MJ',''),(21029,130,'Municipality','Al Marqab','LY-MB',''),(21030,130,'Municipality','Al Qa?r?n','LY-QT',''),(21031,130,'Municipality','Al Qubbah','LY-QB',''),(21032,130,'Municipality','Al W??ah','LY-WA',''),(21033,130,'Municipality','An Nuqa? al Khams','LY-NQ',''),(21034,130,'Municipality','Ash Sh??i\'','LY-SH',''),(21035,130,'Municipality','Az Z?wiyah','LY-ZA',''),(21036,130,'Municipality','Bangh?z?','LY-BA',''),(21037,130,'Municipality','Ban? Wal?d','LY-BW',''),(21038,130,'Municipality','Darnah','LY-DR',''),(21039,130,'Municipality','Ghad?mis','LY-GD',''),(21040,130,'Municipality','Ghary?n','LY-GR',''),(21041,130,'Municipality','Gh?t','LY-GT',''),(21042,130,'Municipality','Jaghb?b','LY-JB',''),(21043,130,'Municipality','Mi?r?tah','LY-MI',''),(21044,130,'Municipality','Mizdah','LY-MZ',''),(21045,130,'Municipality','Murzuq','LY-MQ',''),(21046,130,'Municipality','N?l?t','LY-NL',''),(21047,130,'Municipality','Sabh?','LY-SB',''),(21048,130,'Municipality','?abr?tah ?urm?n','LY-SS',''),(21049,130,'Municipality','Surt','LY-SR',''),(21050,130,'Municipality','T?j?r?\' wa an Naw??? al Arb??','LY-TN',''),(21051,130,'Municipality','?ar?bulus','LY-TB',''),(21052,130,'Municipality','Tarh?nah-Masall?tah','LY-TM',''),(21053,130,'Municipality','W?d? al ?ay?t','LY-WD',''),(21054,130,'Municipality','Yafran-J?d?','LY-YJ',''),(21055,154,'Economic region','Chaouia-Ouardigha','MA 09',''),(21056,154,'Economic region','Doukhala-Abda','MA 10',''),(21057,154,'Economic region','Fès-Boulemane','MA 05',''),(21058,154,'Economic region','Gharb-Chrarda-Beni Hssen','MA 02',''),(21059,154,'Economic region','Grand Casablanca','MA 08',''),(21060,154,'Economic region','Guelmim-Es Smara','MA 14',''),(21061,154,'Economic region','Laâyoune-Boujdour-Sakia el Hamra','MA 15',''),(21062,154,'Economic region','L\'Oriental','MA 04',''),(21063,154,'Economic region','Marrakech-Tensift-Al Haouz','MA 11',''),(21064,154,'Economic region','Meknès-Tafilalet','MA 06',''),(21065,154,'Economic region','Oued ed Dahab-Lagouira','MA 16',''),(21066,154,'Economic region','Rabat-Salé-Zemmour-Zaer','MA 07',''),(21067,154,'Economic region','Sous-Massa-Draa','MA 13',''),(21068,154,'Economic region','Tadla-Azilal','MA 12',''),(21069,154,'Economic region','Tanger-Tétouan','MA 01',''),(21070,154,'Economic region','Taza-Al Hoceima-Taounate','MA 03',''),(21071,154,'Province','Agadir','MA-AGD',''),(21072,154,'Province','Aït Baha','MA-BAH',''),(21073,154,'Province','Aït Melloul','MA-MEL',''),(21074,154,'Province','Al Haouz','MA-HAO',''),(21075,154,'Province','Al Hoceïma','MA-HOC',''),(21076,154,'Province','Assa-Zag','MA-ASZ',''),(21077,154,'Province','Azilal','MA-AZI',''),(21078,154,'Province','Beni Mellal','MA-BEM',''),(21079,154,'Province','Ben Sllmane','MA-BES',''),(21080,154,'Province','Berkane','MA-BER',''),(21081,154,'Province','Boujdour (EH)','MA-BOD',''),(21082,154,'Province','Boulemane','MA-BOM',''),(21083,154,'Province','Casablanca [Dar el Beïda]','MA-CAS',''),(21084,154,'Province','Chefchaouene','MA-CHE',''),(21085,154,'Province','Chichaoua','MA-CHI',''),(21086,154,'Province','El Hajeb','MA-HAJ',''),(21087,154,'Province','El Jadida','MA-JDI',''),(21088,154,'Province','Errachidia','MA-ERR',''),(21089,154,'Province','Essaouira','MA-ESI',''),(21090,154,'Province','Es Smara (EH)','MA-ESM',''),(21091,154,'Province','Fès','MA-FES',''),(21092,154,'Province','Figuig','MA-FIG',''),(21093,154,'Province','Guelmim','MA-GUE',''),(21094,154,'Province','Ifrane','MA-IFR',''),(21095,154,'Province','Jerada','MA-JRA',''),(21096,154,'Province','Kelaat Sraghna','MA-KES',''),(21097,154,'Province','Kénitra','MA-KEN',''),(21098,154,'Province','Khemisaet','MA-KHE',''),(21099,154,'Province','Khenifra','MA-KHN',''),(21100,154,'Province','Khouribga','MA-KHO',''),(21101,154,'Province','Laâyoune (EH)','MA-LAA',''),(21102,154,'Province','Larache','MA-LAP',''),(21103,154,'Province','Marrakech','MA-MAR',''),(21104,154,'Province','Meknsès','MA-MEK',''),(21105,154,'Province','Nador','MA-NAD',''),(21106,154,'Province','Ouarzazate','MA-OUA',''),(21107,154,'Province','Oued ed Dahab (EH)','MA-OUD',''),(21108,154,'Province','Oujda','MA-OUJ',''),(21109,154,'Province','Rabat-Salé','MA-RBA',''),(21110,154,'Province','Safi','MA-SAF',''),(21111,154,'Province','Sefrou','MA-SEF',''),(21112,154,'Province','Settat','MA-SET',''),(21113,154,'Province','Sidl Kacem','MA-SIK',''),(21114,154,'Province','Tanger','MA-TNG',''),(21115,154,'Province','Tan-Tan','MA-TNT',''),(21116,154,'Province','Taounate','MA-TAO',''),(21117,154,'Province','Taroudannt','MA-TAR',''),(21118,154,'Province','Tata','MA-TAT',''),(21119,154,'Province','Taza','MA-TAZ',''),(21120,154,'Province','Tétouan','MA-TET',''),(21121,154,'Province','Tiznit','MA-TIZ',''),(21122,149,'Autonomous territory','G?g?uzia, Unitate Teritorial? Autonom?','MD-GA',''),(21123,149,'City','Chi?in?u','MD-CU',''),(21124,149,'District','B?l?i','MD-BA',''),(21125,149,'District','Cahul','MD-CA',''),(21126,149,'District','Chi?in?u','MD-CH',''),(21127,149,'District','Edine?','MD-ED',''),(21128,149,'District','L?pu?na','MD-LA',''),(21129,149,'District','Orhei','MD-OR',''),(21130,149,'District','Soroca','MD-SO',''),(21131,149,'District','Taraclia','MD-TA',''),(21132,149,'District','Tighina','MD-TI',''),(21133,149,'District','Ungheni','MD-UN',''),(21134,149,'Territorial unit','Stînga Nistrului, unitatea teritorial? din','MD-SN',''),(21135,152,'Municipality','Andrijevica','ME-01',''),(21136,152,'Municipality','Bar','ME-02',''),(21137,152,'Municipality','Berane','ME-03',''),(21138,152,'Municipality','Bijelo Polje','ME-04',''),(21139,152,'Municipality','Budva','ME-05',''),(21140,152,'Municipality','Cetinje','ME-06',''),(21141,152,'Municipality','Danilovgrad','ME-07',''),(21142,152,'Municipality','Herceg-Novi','ME-08',''),(21143,152,'Municipality','Kolašin','ME-09',''),(21144,152,'Municipality','Kotor','ME-10',''),(21145,152,'Municipality','Mojkovac','ME-11',''),(21146,152,'Municipality','Nikši?','ME-12',''),(21147,152,'Municipality','Plav','ME-13',''),(21148,152,'Municipality','Pljevlja','ME-14',''),(21149,152,'Municipality','Plužine','ME-15',''),(21150,152,'Municipality','Podgorica','ME-16',''),(21151,152,'Municipality','Rožaje','ME-17',''),(21152,152,'Municipality','Šavnik','ME-18',''),(21153,152,'Municipality','Tivat','ME-19',''),(21154,152,'Municipality','Ulcinj','ME-20',''),(21155,152,'Municipality','Žabljak','ME-21',''),(21156,136,'Autonomous province','Antananarivo','MG-T',''),(21157,136,'Autonomous province','Antsiranana','MG-D',''),(21158,136,'Autonomous province','Fianarantsoa','MG-F',''),(21159,136,'Autonomous province','Mahajanga','MG-M',''),(21160,136,'Autonomous province','Toamasina','MG-A',''),(21161,136,'Autonomous province','Toliara','MG-U',''),(21162,142,'Municipality','Ailinglapalap','MH-ALL',''),(21163,142,'Municipality','Ailuk','MH-ALK',''),(21164,142,'Municipality','Arno','MH-ARN',''),(21165,142,'Municipality','Aur','MH-AUR',''),(21166,142,'Municipality','Ebon','MH-EBO',''),(21167,142,'Municipality','Eniwetok','MH-ENI',''),(21168,142,'Municipality','Jaluit','MH-JAL',''),(21169,142,'Municipality','Kili','MH-KIL',''),(21170,142,'Municipality','Kwajalein','MH-KWA',''),(21171,142,'Municipality','Lae','MH-LAE',''),(21172,142,'Municipality','Lib','MH-LIB',''),(21173,142,'Municipality','Likiep','MH-LIK',''),(21174,142,'Municipality','Majuro','MH-MAJ',''),(21175,142,'Municipality','Maloelap','MH-MAL',''),(21176,142,'Municipality','Mejit','MH-MEJ',''),(21177,142,'Municipality','Mili','MH-MIL',''),(21178,142,'Municipality','Namorik','MH-NMK',''),(21179,142,'Municipality','Namu','MH-NMU',''),(21180,142,'Municipality','Rongelap','MH-RON',''),(21181,142,'Municipality','Ujae','MH-UJA',''),(21182,142,'Municipality','Ujelang','MH-UJL',''),(21183,142,'Municipality','Utirik','MH-UTI',''),(21184,142,'Municipality','Wotho','MH-WTN',''),(21185,142,'Municipality','Wotje','MH-WTJ',''),(21186,135,'Municipality','Aerodrom','MK-01',''),(21187,135,'Municipality','Ara?inovo','MK-02',''),(21188,135,'Municipality','Berovo','MK-03',''),(21189,135,'Municipality','Bitola','MK-04',''),(21190,135,'Municipality','Bogdanci','MK-05',''),(21191,135,'Municipality','Bogovinje','MK-06',''),(21192,135,'Municipality','Bosilovo','MK-07',''),(21193,135,'Municipality','Brvenica','MK-08',''),(21194,135,'Municipality','Butel','MK-09',''),(21195,135,'Municipality','Centar','MK-77',''),(21196,135,'Municipality','Centar Župa','MK-78',''),(21197,135,'Municipality','?air','MK-79',''),(21198,135,'Municipality','?aška','MK-80',''),(21199,135,'Municipality','?ešinovo-Obleševo','MK-81',''),(21200,135,'Municipality','?u?er Sandevo','MK-82',''),(21201,135,'Municipality','Debar','MK-21',''),(21202,135,'Municipality','Debarca','MK-22',''),(21203,135,'Municipality','Del?evo','MK-23',''),(21204,135,'Municipality','Demir Hisar','MK-25',''),(21205,135,'Municipality','Demir Kapija','MK-24',''),(21206,135,'Municipality','Dojran','MK-26',''),(21207,135,'Municipality','Dolneni','MK-27',''),(21208,135,'Municipality','Drugovo','MK-28',''),(21209,135,'Municipality','Gazi Baba','MK-17',''),(21210,135,'Municipality','Gevgelija','MK-18',''),(21211,135,'Municipality','Gjor?e Petrov','MK-29',''),(21212,135,'Municipality','Gostivar','MK-19',''),(21213,135,'Municipality','Gradsko','MK-20',''),(21214,135,'Municipality','Ilinden','MK-34',''),(21215,135,'Municipality','Jegunovce','MK-35',''),(21216,135,'Municipality','Karbinci','MK-37',''),(21217,135,'Municipality','Karpoš','MK-38',''),(21218,135,'Municipality','Kavadarci','MK-36',''),(21219,135,'Municipality','Ki?evo','MK-40',''),(21220,135,'Municipality','Kisela Voda','MK-39',''),(21221,135,'Municipality','Ko?ani','MK-42',''),(21222,135,'Municipality','Kon?e','MK-41',''),(21223,135,'Municipality','Kratovo','MK-43',''),(21224,135,'Municipality','Kriva Palanka','MK-44',''),(21225,135,'Municipality','Krivogaštani','MK-45',''),(21226,135,'Municipality','Kruševo','MK-46',''),(21227,135,'Municipality','Kumanovo','MK-47',''),(21228,135,'Municipality','Lipkovo','MK-48',''),(21229,135,'Municipality','Lozovo','MK-49',''),(21230,135,'Municipality','Makedonska Kamenica','MK-51',''),(21231,135,'Municipality','Makedonski Brod','MK-52',''),(21232,135,'Municipality','Mavrovo-i-Rostuša','MK-50',''),(21233,135,'Municipality','Mogila','MK-53',''),(21234,135,'Municipality','Negotino','MK-54',''),(21235,135,'Municipality','Novaci','MK-55',''),(21236,135,'Municipality','Novo Selo','MK-56',''),(21237,135,'Municipality','Ohrid','MK-58',''),(21238,135,'Municipality','Oslomej','MK-57',''),(21239,135,'Municipality','Peh?evo','MK-60',''),(21240,135,'Municipality','Petrovec','MK-59',''),(21241,135,'Municipality','Plasnica','MK-61',''),(21242,135,'Municipality','Prilep','MK-62',''),(21243,135,'Municipality','Probištip','MK-63',''),(21244,135,'Municipality','Radoviš','MK-64',''),(21245,135,'Municipality','Rankovce','MK-65',''),(21246,135,'Municipality','Resen','MK-66',''),(21247,135,'Municipality','Rosoman','MK-67',''),(21248,135,'Municipality','Saraj','MK-68',''),(21249,135,'Municipality','Štip','MK-83',''),(21250,135,'Municipality','Šuto Orizari','MK-84',''),(21251,135,'Municipality','Sopište','MK-70',''),(21252,135,'Municipality','Staro Nagori?ane','MK-71',''),(21253,135,'Municipality','Struga','MK-72',''),(21254,135,'Municipality','Strumica','MK-73',''),(21255,135,'Municipality','Studeni?ani','MK-74',''),(21256,135,'Municipality','Sveti Nikole','MK-69',''),(21257,135,'Municipality','Tearce','MK-75',''),(21258,135,'Municipality','Tetovo','MK-76',''),(21259,135,'Municipality','Valandovo','MK-10',''),(21260,135,'Municipality','Vasilevo','MK-11',''),(21261,135,'Municipality','Veles','MK-13',''),(21262,135,'Municipality','Vev?ani','MK-12',''),(21263,135,'Municipality','Vinica','MK-14',''),(21264,135,'Municipality','Vraneštica','MK-15',''),(21265,135,'Municipality','Vrap?ište','MK-16',''),(21266,135,'Municipality','Zajas','MK-31',''),(21267,135,'Municipality','Zelenikovo','MK-32',''),(21268,135,'Municipality','Želino','MK-30',''),(21269,135,'Municipality','Zrnovci','MK-33',''),(21270,140,'District','Bamako','ML-BK0',''),(21271,140,'Region','Gao','ML-7',''),(21272,140,'Region','Kayes','ML-1',''),(21273,140,'Region','Kidal','ML-8',''),(21274,140,'Region','Koulikoro','ML-2',''),(21275,140,'Region','Mopti','ML-5',''),(21276,140,'Region','Ségou','ML-4',''),(21277,140,'Region','Sikasso','ML-3',''),(21278,140,'Region','Tombouctou','ML-6',''),(21279,156,'Division','Ayeyarwady','MM-07',''),(21280,156,'Division','Bago','MM-02',''),(21281,156,'Division','Magway','MM-03',''),(21282,156,'Division','Mandalay','MM-04',''),(21283,156,'Division','Sagaing','MM-01',''),(21284,156,'Division','Tanintharyi','MM-05',''),(21285,156,'Division','Yangon','MM-06',''),(21286,156,'State','Chin','MM-14',''),(21287,156,'State','Kachin','MM-11',''),(21288,156,'State','Kayah','MM-12',''),(21289,156,'State','Kayin','MM-13',''),(21290,156,'State','Mon','MM-15',''),(21291,156,'State','Rakhine','MM-16',''),(21292,156,'State','Shan','MM-17',''),(21293,151,'Province','Arhangay','MN-073',''),(21294,151,'Province','Bayanhongor','MN-069',''),(21295,151,'Province','Bayan-Ölgiy','MN-071',''),(21296,151,'Province','Bulgan','MN-067',''),(21297,151,'Province','Dornod','MN-061',''),(21298,151,'Province','Dornogovi','MN-063',''),(21299,151,'Province','Dundgovi','MN-059',''),(21300,151,'Province','Dzavhan','MN-057',''),(21301,151,'Province','Govi-Altay','MN-065',''),(21302,151,'Province','Hentiy','MN-039',''),(21303,151,'Province','Hovd','MN-043',''),(21304,151,'Province','Hövsgöl','MN-041',''),(21305,151,'Province','Ömnögovi','MN-053',''),(21306,151,'Province','Övörhangay','MN-055',''),(21307,151,'Province','Selenge','MN-049',''),(21308,151,'Province','Sühbaatar','MN-051',''),(21309,151,'Province','Töv','MN-047',''),(21310,151,'Province','Uvs','MN-046',''),(21311,151,'Municipality','Ulanbaatar','MN-1',''),(21312,151,'Municipality','Darhan uul','MN-037',''),(21313,151,'Municipality','Govi-Sumber','MN-064',''),(21314,151,'Municipality','Orhon','MN-035',''),(21315,144,'District','Nouakchott','MR-NKC',''),(21316,144,'Region','Adrar','MR-07',''),(21317,144,'Region','Assaba','MR-03',''),(21318,144,'Region','Brakna','MR-05',''),(21319,144,'Region','Dakhlet Nouadhibou','MR-08',''),(21320,144,'Region','Gorgol','MR-04',''),(21321,144,'Region','Guidimaka','MR-10',''),(21322,144,'Region','Hodh ech Chargui','MR-01',''),(21323,144,'Region','Hodh el Charbi','MR-02',''),(21324,144,'Region','Inchiri','MR-12',''),(21325,144,'Region','Tagant','MR-09',''),(21326,144,'Region','Tiris Zemmour','MR-11',''),(21327,144,'Region','Trarza','MR-06',''),(21328,141,'Local council','Attard','MT-01',''),(21329,141,'Local council','Balzan','MT-02',''),(21330,141,'Local council','Birgu','MT-03',''),(21331,141,'Local council','Birkirkara','MT-04',''),(21332,141,'Local council','Bir?ebbu?a','MT-05',''),(21333,141,'Local council','Bormla','MT-06',''),(21334,141,'Local council','Dingli','MT-07',''),(21335,141,'Local council','Fgura','MT-08',''),(21336,141,'Local council','Floriana','MT-09',''),(21337,141,'Local council','Fontana','MT-10',''),(21338,141,'Local council','Gudja','MT-11',''),(21339,141,'Local council','G?ira','MT-12',''),(21340,141,'Local council','G?ajnsielem','MT-13',''),(21341,141,'Local council','G?arb','MT-14',''),(21342,141,'Local council','G?arg?ur','MT-15',''),(21343,141,'Local council','G?asri','MT-16',''),(21344,141,'Local council','G?axaq','MT-17',''),(21345,141,'Local council','?amrun','MT-18',''),(21346,141,'Local council','Iklin','MT-19',''),(21347,141,'Local council','Isla','MT-20',''),(21348,141,'Local council','Kalkara','MT-21',''),(21349,141,'Local council','Ker?em','MT-22',''),(21350,141,'Local council','Kirkop','MT-23',''),(21351,141,'Local council','Lija','MT-24',''),(21352,141,'Local council','Luqa','MT-25',''),(21353,141,'Local council','Marsa','MT-26',''),(21354,141,'Local council','Marsaskala','MT-27',''),(21355,141,'Local council','Marsaxlokk','MT-28',''),(21356,141,'Local council','Mdina','MT-29',''),(21357,141,'Local council','Mellie?a','MT-30',''),(21358,141,'Local council','M?arr','MT-31',''),(21359,141,'Local council','Mosta','MT-32',''),(21360,141,'Local council','Mqabba','MT-33',''),(21361,141,'Local council','Msida','MT-34',''),(21362,141,'Local council','Mtarfa','MT-35',''),(21363,141,'Local council','Munxar','MT-36',''),(21364,141,'Local council','Nadur','MT-37',''),(21365,141,'Local council','Naxxar','MT-38',''),(21366,141,'Local council','Paola','MT-39',''),(21367,141,'Local council','Pembroke','MT-40',''),(21368,141,'Local council','Pietà','MT-41',''),(21369,141,'Local council','Qala','MT-42',''),(21370,141,'Local council','Qormi','MT-43',''),(21371,141,'Local council','Qrendi','MT-44',''),(21372,141,'Local council','Rabat G?awdex','MT-45',''),(21373,141,'Local council','Rabat Malta','MT-46',''),(21374,141,'Local council','Safi','MT-47',''),(21375,141,'Local council','San ?iljan','MT-48',''),(21376,141,'Local council','San ?wann','MT-49',''),(21377,141,'Local council','San Lawrenz','MT-50',''),(21378,141,'Local council','San Pawl il-Ba?ar','MT-51',''),(21379,141,'Local council','Sannat','MT-52',''),(21380,141,'Local council','Santa Lu?ija','MT-53',''),(21381,141,'Local council','Santa Venera','MT-54',''),(21382,141,'Local council','Si??iewi','MT-55',''),(21383,141,'Local council','Sliema','MT-56',''),(21384,141,'Local council','Swieqi','MT-57',''),(21385,141,'Local council','Ta’ Xbiex','MT-58',''),(21386,141,'Local council','Tarxien','MT-59',''),(21387,141,'Local council','Valletta','MT-60',''),(21388,141,'Local council','Xag?ra','MT-61',''),(21389,141,'Local council','Xewkija','MT-62',''),(21390,141,'Local council','Xg?ajra','MT-63',''),(21391,141,'Local council','?abbar','MT-64',''),(21392,141,'Local council','?ebbu? G?awdex','MT-65',''),(21393,141,'Local council','?ebbu? Malta','MT-66',''),(21394,141,'Local council','?ejtun','MT-67',''),(21395,141,'Local council','?urrieq','MT-68',''),(21396,145,'City','Beau Bassin-Rose Hill','MU-BR',''),(21397,145,'City','Curepipe','MU-CU',''),(21398,145,'City','Port Louis','MU-PU',''),(21399,145,'City','Quatre Bornes','MU-QB',''),(21400,145,'City','Vacoas-Phoenix','MU-VP',''),(21401,145,'Dependency','Agalega Islands','MU-AG',''),(21402,145,'Dependency','Cargados Carajos Shoals','MU-CC',''),(21403,145,'Dependency','Rodrigues Island','MU-RO',''),(21404,145,'District','Black River','MU-BL',''),(21405,145,'District','Flacq','MU-FL',''),(21406,145,'District','Grand Port','MU-GP',''),(21407,145,'District','Moka','MU-MO',''),(21408,145,'District','Pamplemousses','MU-PA',''),(21409,145,'District','Plaines Wilhems','MU-PW',''),(21410,145,'District','Port Louis','MU-PL',''),(21411,145,'District','Rivière du Rempart','MU-RP',''),(21412,145,'District','Savanne','MU-SA',''),(21413,139,'City','Male','MV-MLE',''),(21414,139,'Atoll','Alif','MV-02',''),(21415,139,'Atoll','Baa','MV-20',''),(21416,139,'Atoll','Dhaalu','MV-17',''),(21417,139,'Atoll','Faafu','MV-14',''),(21418,139,'Atoll','Gaafu Aliff','MV-27',''),(21419,139,'Atoll','Gaafu Daalu','MV-28',''),(21420,139,'Atoll','Gnaviyani','MV-29',''),(21421,139,'Atoll','Haa Alif','MV-07',''),(21422,139,'Atoll','Haa Dhaalu','MV-23',''),(21423,139,'Atoll','Kaafu','MV-26',''),(21424,139,'Atoll','Laamu','MV-05',''),(21425,139,'Atoll','Lhaviyani','MV-03',''),(21426,139,'Atoll','Meemu','MV-12',''),(21427,139,'Atoll','Noonu','MV-25',''),(21428,139,'Atoll','Raa','MV-13',''),(21429,139,'Atoll','Seenu','MV-01',''),(21430,139,'Atoll','Shaviyani','MV-24',''),(21431,139,'Atoll','Thaa','MV-08',''),(21432,139,'Atoll','Vaavu','MV-04',''),(21433,137,'Region','Central','MW C',''),(21434,137,'Region','Northern','MW N',''),(21435,137,'Region','Southern (Malawi)','MW S',''),(21436,137,'District','Balaka','MW-BA',''),(21437,137,'District','Blantyre','MW-BL',''),(21438,137,'District','Chikwawa','MW-CK',''),(21439,137,'District','Chiradzulu','MW-CR',''),(21440,137,'District','Chitipa','MW-CT',''),(21441,137,'District','Dedza','MW-DE',''),(21442,137,'District','Dowa','MW-DO',''),(21443,137,'District','Karonga','MW-KR',''),(21444,137,'District','Kasungu','MW-KS',''),(21445,137,'District','Likoma Island','MW-LK',''),(21446,137,'District','Lilongwe','MW-LI',''),(21447,137,'District','Machinga','MW-MH',''),(21448,137,'District','Mangochi','MW-MG',''),(21449,137,'District','Mchinji','MW-MC',''),(21450,137,'District','Mulanje','MW-MU',''),(21451,137,'District','Mwanza','MW-MW',''),(21452,137,'District','Mzimba','MW-MZ',''),(21453,137,'District','Nkhata Bay','MW-NB',''),(21454,137,'District','Nkhotakota','MW-NK',''),(21455,137,'District','Nsanje','MW-NS',''),(21456,137,'District','Ntcheu','MW-NU',''),(21457,137,'District','Ntchisi','MW-NI',''),(21458,137,'District','Phalombe','MW-PH',''),(21459,137,'District','Rumphi','MW-RU',''),(21460,137,'District','Salima','MW-SA',''),(21461,137,'District','Thyolo','MW-TH',''),(21462,137,'District','Zomba','MW-ZO',''),(21463,147,'Federal district','Distrito Federal','MX-DIF',''),(21464,147,'State','Aguascalientes','MX-AGU',''),(21465,147,'State','Baja California','MX-BCN',''),(21466,147,'State','Baja California Sur','MX-BCS',''),(21467,147,'State','Campeche','MX-CAM',''),(21468,147,'State','Coahuila','MX-COA',''),(21469,147,'State','Colima','MX-COL',''),(21470,147,'State','Chiapas','MX-CHP',''),(21471,147,'State','Chihuahua','MX-CHH',''),(21472,147,'State','Durango','MX-DUR',''),(21473,147,'State','Guanajuato','MX-GUA',''),(21474,147,'State','Guerrero','MX-GRO',''),(21475,147,'State','Hidalgo','MX-HID',''),(21476,147,'State','Jalisco','MX-JAL',''),(21477,147,'State','México','MX-MEX',''),(21478,147,'State','Michoacán','MX-MIC',''),(21479,147,'State','Morelos','MX-MOR',''),(21480,147,'State','Nayarit','MX-NAY',''),(21481,147,'State','Nuevo León','MX-NLE',''),(21482,147,'State','Oaxaca','MX-OAX',''),(21483,147,'State','Puebla','MX-PUE',''),(21484,147,'State','Querétaro','MX-QUE',''),(21485,147,'State','Quintana Roo','MX-ROO',''),(21486,147,'State','San Luis Potosí','MX-SLP',''),(21487,147,'State','Sinaloa','MX-SIN',''),(21488,147,'State','Sonora','MX-SON',''),(21489,147,'State','Tabasco','MX-TAB',''),(21490,147,'State','Tamaulipas','MX-TAM',''),(21491,147,'State','Tlaxcala','MX-TLA',''),(21492,147,'State','Veracruz','MX-VER',''),(21493,147,'State','Yucatán','MX-YUC',''),(21494,147,'State','Zacatecas','MX-ZAC',''),(21495,138,'Federal Territories','Wilayah Persekutuan Kuala Lumpur','MY-14',''),(21496,138,'Federal Territories','Wilayah Persekutuan Labuan','MY-15',''),(21497,138,'Federal Territories','Wilayah Persekutuan Putrajaya','MY-16',''),(21498,138,'State','Johor','MY-01',''),(21499,138,'State','Kedah','MY-02',''),(21500,138,'State','Kelantan','MY-03',''),(21501,138,'State','Melaka','MY-04',''),(21502,138,'State','Negeri Sembilan','MY-05',''),(21503,138,'State','Pahang','MY-06',''),(21504,138,'State','Perak','MY-08',''),(21505,138,'State','Perlis','MY-09',''),(21506,138,'State','Pulau Pinang','MY-07',''),(21507,138,'State','Sabah','MY-12',''),(21508,138,'State','Sarawak','MY-13',''),(21509,138,'State','Selangor','MY-10',''),(21510,138,'State','Terengganu','MY-11',''),(21511,155,'City','Maputo (city)','MZ-MPM',''),(21512,155,'Province','Cabo Delgado','MZ-P',''),(21513,155,'Province','Gaza','MZ-G',''),(21514,155,'Province','Inhambane','MZ-I',''),(21515,155,'Province','Manica','MZ-B',''),(21516,155,'Province','Maputo','MZ-L',''),(21517,155,'Province','Numpula','MZ-N',''),(21518,155,'Province','Niassa','MZ-A',''),(21519,155,'Province','Sofala','MZ-S',''),(21520,155,'Province','Tete','MZ-T',''),(21521,155,'Province','Zambezia','MZ-Q',''),(21522,157,'Region','Caprivi','NA-CA',''),(21523,157,'Region','Erongo','NA-ER',''),(21524,157,'Region','Hardap','NA-HA',''),(21525,157,'Region','Karas','NA-KA',''),(21526,157,'Region','Khomas','NA-KH',''),(21527,157,'Region','Kunene','NA-KU',''),(21528,157,'Region','Ohangwena','NA-OW',''),(21529,157,'Region','Okavango','NA-OK',''),(21530,157,'Region','Omaheke','NA-OH',''),(21531,157,'Region','Omusati','NA-OS',''),(21532,157,'Region','Oshana','NA-ON',''),(21533,157,'Region','Oshikoto','NA-OT',''),(21534,157,'Region','Otjozondjupa','NA-OD',''),(21535,164,'Capital District','Niamey','NE-8',''),(21536,164,'Department','Agadez','NE-1',''),(21537,164,'Department','Diffa','NE-2',''),(21538,164,'Department','Dosso','NE-3',''),(21539,164,'Department','Maradi','NE-4',''),(21540,164,'Department','Tahoua','NE-5',''),(21541,164,'Department','Tillabéri','NE-6',''),(21542,164,'Department','Zinder','NE-7',''),(21543,165,'Capital Territory','Abuja Capital Territory','NG-FC',''),(21544,165,'State','Abia','NG-AB',''),(21545,165,'State','Adamawa','NG-AD',''),(21546,165,'State','Akwa Ibom','NG-AK',''),(21547,165,'State','Anambra','NG-AN',''),(21548,165,'State','Bauchi','NG-BA',''),(21549,165,'State','Bayelsa','NG-BY',''),(21550,165,'State','Benue','NG-BE',''),(21551,165,'State','Borno','NG-BO',''),(21552,165,'State','Cross River','NG-CR',''),(21553,165,'State','Delta','NG-DE',''),(21554,165,'State','Ebonyi','NG-EB',''),(21555,165,'State','Edo','NG-ED',''),(21556,165,'State','Ekiti','NG-EK',''),(21557,165,'State','Enugu','NG-EN',''),(21558,165,'State','Gombe','NG-GO',''),(21559,165,'State','Imo','NG-IM',''),(21560,165,'State','Jigawa','NG-JI',''),(21561,165,'State','Kaduna','NG-KD',''),(21562,165,'State','Kano','NG-KN',''),(21563,165,'State','Katsina','NG-KT',''),(21564,165,'State','Kebbi','NG-KE',''),(21565,165,'State','Kogi','NG-KO',''),(21566,165,'State','Kwara','NG-KW',''),(21567,165,'State','Lagos','NG-LA',''),(21568,165,'State','Nassarawa','NG-NA',''),(21569,165,'State','Niger','NG-NI',''),(21570,165,'State','Ogun','NG-OG',''),(21571,165,'State','Ondo','NG-ON',''),(21572,165,'State','Osun','NG-OS',''),(21573,165,'State','Oyo','NG-OY',''),(21574,165,'State','Plateau','NG-PL',''),(21575,165,'State','Rivers','NG-RI',''),(21576,165,'State','Sokoto','NG-SO',''),(21577,165,'State','Taraba','NG-TA',''),(21578,165,'State','Yobe','NG-YO',''),(21579,165,'State','Zamfara','NG-ZA',''),(21580,163,'Department','Boaco','NI-BO',''),(21581,163,'Department','Carazo','NI-CA',''),(21582,163,'Department','Chinandega','NI-CI',''),(21583,163,'Department','Chontales','NI-CO',''),(21584,163,'Department','Estelí','NI-ES',''),(21585,163,'Department','Granada','NI-GR',''),(21586,163,'Department','Jinotega','NI-JI',''),(21587,163,'Department','León','NI-LE',''),(21588,163,'Department','Madriz','NI-MD',''),(21589,163,'Department','Managua','NI-MN',''),(21590,163,'Department','Masaya','NI-MS',''),(21591,163,'Department','Matagalpa','NI-MT',''),(21592,163,'Department','Nueva Segovia','NI-NS',''),(21593,163,'Department','Río San Juan','NI-SJ',''),(21594,163,'Department','Rivas','NI-RI',''),(21595,163,'Autonomous Region','Atlántico Norte','NI-AN',''),(21596,163,'Autonomous Region','Atlántico Sur','NI-AS',''),(21597,160,'Province','Drenthe','NL-DR',''),(21598,160,'Province','Flevoland','NL-FL',''),(21599,160,'Province','Friesland','NL-FR',''),(21600,160,'Province','Gelderland','NL-GE',''),(21601,160,'Province','Groningen','NL-GR',''),(21602,160,'Province','Limburg','NL-LI',''),(21603,160,'Province','Noord-Brabant','NL-NB',''),(21604,160,'Province','Noord-Holland','NL-NH',''),(21605,160,'Province','Overijssel','NL-OV',''),(21606,160,'Province','Utrecht','NL-UT',''),(21607,160,'Province','Zeeland','NL-ZE',''),(21608,160,'Province','Zuid-Holland','NL-ZH',''),(21609,169,'County','Akershus','NO-02',''),(21610,169,'County','Aust-Agder','NO-09',''),(21611,169,'County','Buskerud','NO-06',''),(21612,169,'County','Finnmark','NO-20',''),(21613,169,'County','Hedmark','NO-04',''),(21614,169,'County','Hordaland','NO-12',''),(21615,169,'County','Møre og Romsdal','NO-15',''),(21616,169,'County','Nordland','NO-18',''),(21617,169,'County','Nord-Trøndelag','NO-17',''),(21618,169,'County','Oppland','NO-05',''),(21619,169,'County','Oslo','NO-03',''),(21620,169,'County','Rogaland','NO-11',''),(21621,169,'County','Sogn og Fjordane','NO-14',''),(21622,169,'County','Sør-Trøndelag','NO-16',''),(21623,169,'County','Telemark','NO-08',''),(21624,169,'County','Troms','NO-19',''),(21625,169,'County','Vest-Agder','NO-10',''),(21626,169,'County','Vestfold','NO-07',''),(21627,169,'County','Østfold','NO-01',''),(21628,169,'County','Jan Mayen','NO-22',''),(21629,169,'County','Svalbard','NO-21',''),(21630,158,'District','Aiwo','NR-01',''),(21631,158,'District','Anabar','NR-02',''),(21632,158,'District','Anetan','NR-03',''),(21633,158,'District','Anibare','NR-04',''),(21634,158,'District','Baiti','NR-05',''),(21635,158,'District','Boe','NR-06',''),(21636,158,'District','Buada','NR-07',''),(21637,158,'District','Denigomodu','NR-08',''),(21638,158,'District','Ewa','NR-09',''),(21639,158,'District','Ijuw','NR-10',''),(21640,158,'District','Meneng','NR-11',''),(21641,158,'District','Nibok','NR-12',''),(21642,158,'District','Uaboe','NR-13',''),(21643,158,'District','Yaren','NR-14',''),(21644,162,'Regional council','Auckland','NZ-AUK',''),(21645,162,'Regional council','Bay of Plenty','NZ-BOP',''),(21646,162,'Regional council','Canterbury','NZ-CAN',''),(21647,162,'Regional council','Hawkes Bay','NZ-HKB',''),(21648,162,'Regional council','Manawatu-Wanganui','NZ-MWT',''),(21649,162,'Regional council','Northland','NZ-NTL',''),(21650,162,'Regional council','Otago','NZ-OTA',''),(21651,162,'Regional council','Southland','NZ-STL',''),(21652,162,'Regional council','Taranaki','NZ-TKI',''),(21653,162,'Regional council','Waikato','NZ-WKO',''),(21654,162,'Regional council','Wellington','NZ-WGN',''),(21655,162,'Regional council','West Coast','NZ-WTC',''),(21656,162,'Unitary authority','Gisborne','NZ-GIS',''),(21657,162,'Unitary authority','Marlborough','NZ-MBH',''),(21658,162,'Unitary authority','Nelson','NZ-NSN',''),(21659,162,'Unitary authority','Tasman','NZ-TAS',''),(21660,170,'Region','Ad Dakhillyah','OM-DA',''),(21661,170,'Region','Al Batinah','OM-BA',''),(21662,170,'Region','Al Wusta','OM-WU',''),(21663,170,'Region','Ash Sharqlyah','OM-SH',''),(21664,170,'Region','Az Zahirah','OM-ZA',''),(21665,170,'Governorate','Al Janblyah','OM-JA',''),(21666,170,'Governorate','Masqat','OM-MA',''),(21667,170,'Governorate','Musandam','OM-MU',''),(21668,174,'Province','Bocas del Toro','PA-1',''),(21669,174,'Province','Coclé','PA-2',''),(21670,174,'Province','Colón','PA-3',''),(21671,174,'Province','Chiriquí','PA-4',''),(21672,174,'Province','Darién','PA-5',''),(21673,174,'Province','Herrera','PA-6',''),(21674,174,'Province','Los Santos','PA-7',''),(21675,174,'Province','Panamá','PA-8',''),(21676,174,'Province','Veraguas','PA-9',''),(21677,174,'Province','Kuna Yala','PA-0',''),(21678,177,'Region','El Callao','PE-CAL',''),(21679,177,'Region','Amazonas','PE-AMA',''),(21680,177,'Region','Ancash','PE-ANC',''),(21681,177,'Region','Apurímac','PE-APU',''),(21682,177,'Region','Arequipa','PE-ARE',''),(21683,177,'Region','Ayacucho','PE-AYA',''),(21684,177,'Region','Cajamarca','PE-CAJ',''),(21685,177,'Region','Cusco','PE-CUS',''),(21686,177,'Region','Huancavelica','PE-HUV',''),(21687,177,'Region','Huánuco','PE-HUC',''),(21688,177,'Region','Ica','PE-ICA',''),(21689,177,'Region','Junín','PE-JUN',''),(21690,177,'Region','La Libertad','PE-LAL',''),(21691,177,'Region','Lambayeque','PE-LAM',''),(21692,177,'Region','Lima','PE-LIM',''),(21693,177,'Region','Loreto','PE-LOR',''),(21694,177,'Region','Madre de Dios','PE-MDD',''),(21695,177,'Region','Moquegua','PE-MOQ',''),(21696,177,'Region','Pasco','PE-PAS',''),(21697,177,'Region','Piura','PE-PIU',''),(21698,177,'Region','Puno','PE-PUN',''),(21699,177,'Region','San Martín','PE-SAM',''),(21700,177,'Region','Tacna','PE-TAC',''),(21701,177,'Region','Tumbes','PE-TUM',''),(21702,177,'Region','Ucayali','PE-UCA',''),(21703,175,'District','National Capital District (Port Moresby)','PG-NCD',''),(21704,175,'Province','Central','PG-CPM',''),(21705,175,'Province','Chimbu','PG-CPK',''),(21706,175,'Province','Eastern Highlands','PG-EHG',''),(21707,175,'Province','East New Britain','PG-EBR',''),(21708,175,'Province','East Sepik','PG-ESW',''),(21709,175,'Province','Enga','PG-EPW',''),(21710,175,'Province','Gulf','PG-GPK',''),(21711,175,'Province','Madang','PG-MPM',''),(21712,175,'Province','Manus','PG-MRL',''),(21713,175,'Province','Milne Bay','PG-MBA',''),(21714,175,'Province','Morobe','PG-MPL',''),(21715,175,'Province','New Ireland','PG-NIK',''),(21716,175,'Province','Northern','PG-NPP',''),(21717,175,'Province','North Solomons','PG-NSA',''),(21718,175,'Province','Sandaun','PG-SAN',''),(21719,175,'Province','Southern Highlands','PG-SHM',''),(21720,175,'Province','Western','PG-WPD',''),(21721,175,'Province','Western Highlands','PG-WHM',''),(21722,175,'Province','West New Britain','PG-WBK',''),(21723,178,'Region','Autonomous Region in Muslim Mindanao (ARMM)','PH 14',''),(21724,178,'Region','Bicol','PH 05',''),(21725,178,'Region','Cagayan Valley','PH 02',''),(21726,178,'Region','CARAGA','PH 13',''),(21727,178,'Region','Central Luzon','PH 03',''),(21728,178,'Region','Central Mindanao','PH 12',''),(21729,178,'Region','Central Visayas','PH 07',''),(21730,178,'Region','Cordillera Administrative Region (CAR)','PH 15',''),(21731,178,'Region','Eastern Visayas','PH 08',''),(21732,178,'Region','Ilocos','PH 01',''),(21733,178,'Region','National Capital Region (Manila)','PH 00',''),(21734,178,'Region','Northern Mindanao','PH 10',''),(21735,178,'Region','Southern Mindanao','PH 11',''),(21736,178,'Region','Southern Tagalog','PH 04',''),(21737,178,'Region','Western Mindanao','PH 09',''),(21738,178,'Region','Western Visayas','PH 06',''),(21739,178,'Province','Abra','PH-ABR',''),(21740,178,'Province','Agusan del Norte','PH-AGN',''),(21741,178,'Province','Agusan del Sur','PH-AGS',''),(21742,178,'Province','Aklan','PH-AKL',''),(21743,178,'Province','Albay','PH-ALB',''),(21744,178,'Province','Antique','PH-ANT',''),(21745,178,'Province','Apayao','PH-APA',''),(21746,178,'Province','Aurora','PH-AUR',''),(21747,178,'Province','Basilan','PH-BAS',''),(21748,178,'Province','Batasn','PH-BAN',''),(21749,178,'Province','Batanes','PH-BTN',''),(21750,178,'Province','Batangas','PH-BTG',''),(21751,178,'Province','Benguet','PH-BEN',''),(21752,178,'Province','Biliran','PH-BIL',''),(21753,178,'Province','Bohol','PH-BOH',''),(21754,178,'Province','Bukidnon','PH-BUK',''),(21755,178,'Province','Bulacan','PH-BUL',''),(21756,178,'Province','Cagayan','PH-CAG',''),(21757,178,'Province','Camarines Norte','PH-CAN',''),(21758,178,'Province','Camarines Sur','PH-CAS',''),(21759,178,'Province','Camiguin','PH-CAM',''),(21760,178,'Province','Capiz','PH-CAP',''),(21761,178,'Province','Catanduanes','PH-CAT',''),(21762,178,'Province','Cavite','PH-CAV',''),(21763,178,'Province','Cebu','PH-CEB',''),(21764,178,'Province','Compostela Valley','PH-COM',''),(21765,178,'Province','Davao del Norte','PH-DAV',''),(21766,178,'Province','Davao del Sur','PH-DAS',''),(21767,178,'Province','Davao Oriental','PH-DAO',''),(21768,178,'Province','Eastern Samar','PH-EAS',''),(21769,178,'Province','Guimaras','PH-GUI',''),(21770,178,'Province','Ifugao','PH-IFU',''),(21771,178,'Province','Ilocos Norte','PH-ILN',''),(21772,178,'Province','Ilocos Sur','PH-ILS',''),(21773,178,'Province','Iloilo','PH-ILI',''),(21774,178,'Province','Isabela','PH-ISA',''),(21775,178,'Province','Kalinga-Apayso','PH-KAL',''),(21776,178,'Province','Laguna','PH-LAG',''),(21777,178,'Province','Lanao del Norte','PH-LAN',''),(21778,178,'Province','Lanao del Sur','PH-LAS',''),(21779,178,'Province','La Union','PH-LUN',''),(21780,178,'Province','Leyte','PH-LEY',''),(21781,178,'Province','Maguindanao','PH-MAG',''),(21782,178,'Province','Marinduque','PH-MAD',''),(21783,178,'Province','Masbate','PH-MAS',''),(21784,178,'Province','Mindoro Occidental','PH-MDC',''),(21785,178,'Province','Mindoro Oriental','PH-MDR',''),(21786,178,'Province','Misamis Occidental','PH-MSC',''),(21787,178,'Province','Misamis Oriental','PH-MSR',''),(21788,178,'Province','Mountain Province','PH-MOU',''),(21789,178,'Province','Negroe Occidental','PH-NEC',''),(21790,178,'Province','Negros Oriental','PH-NER',''),(21791,178,'Province','North Cotabato','PH-NCO',''),(21792,178,'Province','Northern Samar','PH-NSA',''),(21793,178,'Province','Nueva Ecija','PH-NUE',''),(21794,178,'Province','Nueva Vizcaya','PH-NUV',''),(21795,178,'Province','Palawan','PH-PLW',''),(21796,178,'Province','Pampanga','PH-PAM',''),(21797,178,'Province','Pangasinan','PH-PAN',''),(21798,178,'Province','Quezon','PH-QUE',''),(21799,178,'Province','Quirino','PH-QUI',''),(21800,178,'Province','Rizal','PH-RIZ',''),(21801,178,'Province','Romblon','PH-ROM',''),(21802,178,'Province','Sarangani','PH-SAR',''),(21803,178,'Province','Siquijor','PH-SIG',''),(21804,178,'Province','Sorsogon','PH-SOR',''),(21805,178,'Province','South Cotabato','PH-SCO',''),(21806,178,'Province','Southern Leyte','PH-SLE',''),(21807,178,'Province','Sultan Kudarat','PH-SUK',''),(21808,178,'Province','Sulu','PH-SLU',''),(21809,178,'Province','Surigao del Norte','PH-SUN',''),(21810,178,'Province','Surigao del Sur','PH-SUR',''),(21811,178,'Province','Tarlac','PH-TAR',''),(21812,178,'Province','Tawi-Tawi','PH-TAW',''),(21813,178,'Province','Western Samar','PH-WSA',''),(21814,178,'Province','Zambales','PH-ZMB',''),(21815,178,'Province','Zamboanga del Norte','PH-ZAN',''),(21816,178,'Province','Zamboanga del Sur','PH-ZAS',''),(21817,178,'Province','Zamboanga Sibiguey','PH-ZSI',''),(21818,171,'Capital territory','Islamabad','PK-IS',''),(21819,171,'Province','Balochistan','PK-BA',''),(21820,171,'Province','North-West Frontier','PK-NW',''),(21821,171,'Province','Punjab','PK-PB',''),(21822,171,'Province','Sindh','PK-SD',''),(21823,171,'Area','Federally Administered Tribal Areas','PK-TA',''),(21824,171,'Area','Azad Rashmir','PK-JK',''),(21825,171,'Area','Northern Areas','PK-NA',''),(21826,180,'Province','Dolno?l?skie','PL-DS',''),(21827,180,'Province','Kujawsko-pomorskie','PL-KP',''),(21828,180,'Province','Lubelskie','PL-LU',''),(21829,180,'Province','Lubuskie','PL-LB',''),(21830,180,'Province','?ódzkie','PL-LD',''),(21831,180,'Province','Ma?opolskie','PL-MA',''),(21832,180,'Province','Mazowieckie','PL-MZ',''),(21833,180,'Province','Opolskie','PL-OP',''),(21834,180,'Province','Podkarpackie','PL-PK',''),(21835,180,'Province','Podlaskie','PL-PD',''),(21836,180,'Province','Pomorskie','PL-PM',''),(21837,180,'Province','?l?skie','PL-SL',''),(21838,180,'Province','?wi?tokrzyskie','PL-SK',''),(21839,180,'Province','Warmi?sko-mazurskie','PL-WN',''),(21840,180,'Province','Wielkopolskie','PL-WP',''),(21841,180,'Province','Zachodniopomorskie','PL-ZP',''),(21842,181,'District','Aveiro','PT-01',''),(21843,181,'District','Beja','PT-02',''),(21844,181,'District','Braga','PT-03',''),(21845,181,'District','Bragança','PT-04',''),(21846,181,'District','Castelo Branco','PT-05',''),(21847,181,'District','Coimbra','PT-06',''),(21848,181,'District','Évora','PT-07',''),(21849,181,'District','Faro','PT-08',''),(21850,181,'District','Guarda','PT-09',''),(21851,181,'District','Leiria','PT-10',''),(21852,181,'District','Lisboa','PT-11',''),(21853,181,'District','Portalegre','PT-12',''),(21854,181,'District','Porto','PT-13',''),(21855,181,'District','Santarém','PT-14',''),(21856,181,'District','Setúbal','PT-15',''),(21857,181,'District','Viana do Castelo','PT-16',''),(21858,181,'District','Vila Real','PT-17',''),(21859,181,'District','Viseu','PT-18',''),(21860,181,'Autonomous region','Região Autónoma dos Açores','PT-20',''),(21861,181,'Autonomous region','Região Autónoma da Madeira','PT-30',''),(21862,172,'State','Aimeliik','PW-002',''),(21863,172,'State','Airai','PW-004',''),(21864,172,'State','Angaur','PW-010',''),(21865,172,'State','Hatobohei','PW-050',''),(21866,172,'State','Kayangel','PW-100',''),(21867,172,'State','Koror','PW-150',''),(21868,172,'State','Melekeok','PW-212',''),(21869,172,'State','Ngaraard','PW-214',''),(21870,172,'State','Ngarchelong','PW-218',''),(21871,172,'State','Ngardmau','PW-222',''),(21872,172,'State','Ngatpang','PW-224',''),(21873,172,'State','Ngchesar','PW-226',''),(21874,172,'State','Ngeremlengui','PW-227',''),(21875,172,'State','Ngiwal','PW-228',''),(21876,172,'State','Peleliu','PW-350',''),(21877,172,'State','Sonsorol','PW-370',''),(21878,176,'Capital district','Asunción','PY-ASU',''),(21879,176,'Department','Alto Paraguay','PY-16',''),(21880,176,'Department','Alto Paraná','PY-10',''),(21881,176,'Department','Amambay','PY-13',''),(21882,176,'Department','Boquerón','PY-19',''),(21883,176,'Department','Caaguazú','PY-5',''),(21884,176,'Department','Caazapá','PY-6',''),(21885,176,'Department','Canindeyú','PY-14',''),(21886,176,'Department','Central','PY-11',''),(21887,176,'Department','Concepción','PY-1',''),(21888,176,'Department','Cordillera','PY-3',''),(21889,176,'Department','Guairá','PY-4',''),(21890,176,'Department','Itapúa','PY-7',''),(21891,176,'Department','Misiones','PY-8',''),(21892,176,'Department','Ñeembucú','PY-12',''),(21893,176,'Department','Paraguarí','PY-9',''),(21894,176,'Department','Presidente Hayes','PY-15',''),(21895,176,'Department','San Pedro','PY-2',''),(21896,183,'Municipality','Ad Dawhah','QA-DA',''),(21897,183,'Municipality','Al Ghuwayriyah','QA-GH',''),(21898,183,'Municipality','Al Jumayliyah','QA-JU',''),(21899,183,'Municipality','Al Khawr','QA-KH',''),(21900,183,'Municipality','Al Wakrah','QA-WA',''),(21901,183,'Municipality','Ar Rayyan','QA-RA',''),(21902,183,'Municipality','Jariyan al Batnah','QA-JB',''),(21903,183,'Municipality','Madinat ash Shamal','QA-MS',''),(21904,183,'Municipality','Umm Salal','QA-US',''),(21905,185,'Department','Alba','RO-AB',''),(21906,185,'Department','Arad','RO-AR',''),(21907,185,'Department','Arge?','RO-AG',''),(21908,185,'Department','Bac?u','RO-BC',''),(21909,185,'Department','Bihor','RO-BH',''),(21910,185,'Department','Bistri?a-N?s?ud','RO-BN',''),(21911,185,'Department','Boto?ani','RO-BT',''),(21912,185,'Department','Bra?ov','RO-BV',''),(21913,185,'Department','Br?ila','RO-BR',''),(21914,185,'Department','Buz?u','RO-BZ',''),(21915,185,'Department','Cara?-Severin','RO-CS',''),(21916,185,'Department','C?l?ra?i','RO-CL',''),(21917,185,'Department','Cluj','RO-CJ',''),(21918,185,'Department','Constan?a','RO-CT',''),(21919,185,'Department','Covasna','RO-CV',''),(21920,185,'Department','Dâmbovi?a','RO-DB',''),(21921,185,'Department','Dolj','RO-DJ',''),(21922,185,'Department','Gala?i','RO-GL',''),(21923,185,'Department','Giurgiu','RO-GR',''),(21924,185,'Department','Gorj','RO-GJ',''),(21925,185,'Department','Harghita','RO-HR',''),(21926,185,'Department','Hunedoara','RO-HD',''),(21927,185,'Department','Ialomi?a','RO-IL',''),(21928,185,'Department','Ia?i','RO-IS',''),(21929,185,'Department','Ilfov','RO-IF',''),(21930,185,'Department','Maramure?','RO-MM',''),(21931,185,'Department','Mehedin?i','RO-MH',''),(21932,185,'Department','Mure?','RO-MS',''),(21933,185,'Department','Neam?','RO-NT',''),(21934,185,'Department','Olt','RO-OT',''),(21935,185,'Department','Prahova','RO-PH',''),(21936,185,'Department','Satu Mare','RO-SM',''),(21937,185,'Department','S?laj','RO-SJ',''),(21938,185,'Department','Sibiu','RO-SB',''),(21939,185,'Department','Suceava','RO-SV',''),(21940,185,'Department','Teleorman','RO-TR',''),(21941,185,'Department','Timi?','RO-TM',''),(21942,185,'Department','Tulcea','RO-TL',''),(21943,185,'Department','Vaslui','RO-VS',''),(21944,185,'Department','Vâlcea','RO-VL',''),(21945,185,'Department','Vrancea','RO-VN',''),(21946,185,'Municipality','Bucure?ti','RO-B',''),(21947,200,'City','Beograd','RS-00',''),(21948,200,'Autonomous province','Kosovo-Metohija','RS KM',''),(21949,200,'Autonomous province','Vojvodina','RS VO',''),(21950,200,'District','Severna Ba?ka','RS-01',''),(21951,200,'District','Južna Ba?ka','RS-06',''),(21952,200,'District','Zapadna Ba?ka','RS-05',''),(21953,200,'District','Severni Banat','RS-03',''),(21954,200,'District','Srednji Banat','RS-02',''),(21955,200,'District','Južni Banat','RS-04',''),(21956,200,'District','Bor','RS-14',''),(21957,200,'District','Brani?evo','RS-11',''),(21958,200,'District','Jablanica','RS-23',''),(21959,200,'District','Kolubara','RS-09',''),(21960,200,'District','Kosovo','RS-25',''),(21961,200,'District','Kosovska Mitrovica','RS-28',''),(21962,200,'District','Kosovo-Pomoravlje','RS-29',''),(21963,200,'District','Ma?va','RS-08',''),(21964,200,'District','Moravica','RS-17',''),(21965,200,'District','Nišava','RS-20',''),(21966,200,'District','P?inja','RS-24',''),(21967,200,'District','Pe?','RS-26',''),(21968,200,'District','Pirot','RS-22',''),(21969,200,'District','Podunavlje','RS-10',''),(21970,200,'District','Pomoravlje','RS-13',''),(21971,200,'District','Prizren','RS-27',''),(21972,200,'District','Rasina','RS-19',''),(21973,200,'District','Raška','RS-18',''),(21974,200,'District','Srem','RS-07',''),(21975,200,'District','Šumadija','RS-12',''),(21976,200,'District','Toplica','RS-21',''),(21977,200,'District','Zaje?ar','RS-15',''),(21978,200,'District','Zlatibor','RS-16',''),(21979,186,'Republic','Adygeya, Respublika','RU-AD',''),(21980,186,'Republic','Altay, Respublika','RU-AL',''),(21981,186,'Republic','Bashkortostan, Respublika','RU-BA',''),(21982,186,'Republic','Buryatiya, Respublika','RU-BU',''),(21983,186,'Republic','Chechenskaya Respublika','RU-CE',''),(21984,186,'Republic','Chuvashskaya Respublika','RU-CU',''),(21985,186,'Republic','Dagestan, Respublika','RU-DA',''),(21986,186,'Republic','Respublika Ingushetiya','RU-IN',''),(21987,186,'Republic','Kabardino-Balkarskaya Respublika','RU-KB',''),(21988,186,'Republic','Kalmykiya, Respublika','RU-KL',''),(21989,186,'Republic','Karachayevo-Cherkesskaya Respublika','RU-KC',''),(21990,186,'Republic','Kareliya, Respublika','RU-KR',''),(21991,186,'Republic','Khakasiya, Respublika','RU-KK',''),(21992,186,'Republic','Komi, Respublika','RU-KO',''),(21993,186,'Republic','Mariy El, Respublika','RU-ME',''),(21994,186,'Republic','Mordoviya, Respublika','RU-MO',''),(21995,186,'Republic','Sakha, Respublika [Yakutiya]','RU-SA',''),(21996,186,'Republic','Severnaya Osetiya-Alaniya, Respublika','RU-SE',''),(21997,186,'Republic','Tatarstan, Respublika','RU-TA',''),(21998,186,'Republic','Tyva, Respublika [Tuva]','RU-TY',''),(21999,186,'Republic','Udmurtskaya Respublika','RU-UD',''),(22000,186,'Administrative Territory','Altayskiy kray','RU-ALT',''),(22001,186,'Administrative Territory','Kamchatskiy kray','RU-KAM',''),(22002,186,'Administrative Territory','Khabarovskiy kray','RU-KHA',''),(22003,186,'Administrative Territory','Krasnodarskiy kray','RU-KDA',''),(22004,186,'Administrative Territory','Krasnoyarskiy kray','RU-KYA',''),(22005,186,'Administrative Territory','Permskiy kray','RU-PER',''),(22006,186,'Administrative Territory','Primorskiy kray','RU-PRI',''),(22007,186,'Administrative Territory','Stavropol\'skiy kray','RU-STA',''),(22008,186,'Administrative Region','Amurskaya oblast\'','RU-AMU',''),(22009,186,'Administrative Region','Arkhangel\'skaya oblast\'','RU-ARK',''),(22010,186,'Administrative Region','Astrakhanskaya oblast\'','RU-AST',''),(22011,186,'Administrative Region','Belgorodskaya oblast\'','RU-BEL',''),(22012,186,'Administrative Region','Bryanskaya oblast\'','RU-BRY',''),(22013,186,'Administrative Region','Chelyabinskaya oblast\'','RU-CHE',''),(22014,186,'Administrative Region','Chitinskaya oblast\'','RU-CHI',''),(22015,186,'Administrative Region','Irkutiskaya oblast\'','RU-IRK',''),(22016,186,'Administrative Region','Ivanovskaya oblast\'','RU-IVA',''),(22017,186,'Administrative Region','Kaliningradskaya oblast\'','RU-KGD',''),(22018,186,'Administrative Region','Kaluzhskaya oblast\'','RU-KLU',''),(22019,186,'Administrative Region','Kemerovskaya oblast\'','RU-KEM',''),(22020,186,'Administrative Region','Kirovskaya oblast\'','RU-KIR',''),(22021,186,'Administrative Region','Kostromskaya oblast\'','RU-KOS',''),(22022,186,'Administrative Region','Kurganskaya oblast\'','RU-KGN',''),(22023,186,'Administrative Region','Kurskaya oblast\'','RU-KRS',''),(22024,186,'Administrative Region','Leningradskaya oblast\'','RU-LEN',''),(22025,186,'Administrative Region','Lipetskaya oblast\'','RU-LIP',''),(22026,186,'Administrative Region','Magadanskaya oblast\'','RU-MAG',''),(22027,186,'Administrative Region','Moskovskaya oblast\'','RU-MOS',''),(22028,186,'Administrative Region','Murmanskaya oblast\'','RU-MUR',''),(22029,186,'Administrative Region','Nizhegorodskaya oblast\'','RU-NIZ',''),(22030,186,'Administrative Region','Novgorodskaya oblast\'','RU-NGR',''),(22031,186,'Administrative Region','Novosibirskaya oblast\'','RU-NVS',''),(22032,186,'Administrative Region','Omskaya oblast\'','RU-OMS',''),(22033,186,'Administrative Region','Orenburgskaya oblast\'','RU-ORE',''),(22034,186,'Administrative Region','Orlovskaya oblast\'','RU-ORL',''),(22035,186,'Administrative Region','Penzenskaya oblast\'','RU-PNZ',''),(22036,186,'Administrative Region','Pskovskaya oblast\'','RU-PSK',''),(22037,186,'Administrative Region','Rostovskaya oblast\'','RU-ROS',''),(22038,186,'Administrative Region','Ryazanskaya oblast\'','RU-RYA',''),(22039,186,'Administrative Region','Sakhalinskaya oblast\'','RU-SAK',''),(22040,186,'Administrative Region','Samaraskaya oblast\'','RU-SAM',''),(22041,186,'Administrative Region','Saratovskaya oblast\'','RU-SAR',''),(22042,186,'Administrative Region','Smolenskaya oblast\'','RU-SMO',''),(22043,186,'Administrative Region','Sverdlovskaya oblast\'','RU-SVE',''),(22044,186,'Administrative Region','Tambovskaya oblast\'','RU-TAM',''),(22045,186,'Administrative Region','Tomskaya oblast\'','RU-TOM',''),(22046,186,'Administrative Region','Tul\'skaya oblast\'','RU-TUL',''),(22047,186,'Administrative Region','Tverskaya oblast\'','RU-TVE',''),(22048,186,'Administrative Region','Tyumenskaya oblast\'','RU-TYU',''),(22049,186,'Administrative Region','Ul\'yanovskaya oblast\'','RU-ULY',''),(22050,186,'Administrative Region','Vladimirskaya oblast\'','RU-VLA',''),(22051,186,'Administrative Region','Volgogradskaya oblast\'','RU-VGG',''),(22052,186,'Administrative Region','Vologodskaya oblast\'','RU-VLG',''),(22053,186,'Administrative Region','Voronezhskaya oblast\'','RU-VOR',''),(22054,186,'Administrative Region','Yaroslavskaya oblast\'','RU-YAR',''),(22055,186,'Autonomous City','Moskva','RU-MOW',''),(22056,186,'Autonomous City','Sankt-Peterburg','RU-SPE',''),(22057,186,'Autonomous Region','Yevreyskaya avtonomnaya oblast\'','RU-YEV',''),(22058,186,'Autonomous District','Aginsky Buryatskiy avtonomnyy okrug','RU-AGB',''),(22059,186,'Autonomous District','Chukotskiy avtonomnyy okrug','RU-CHU',''),(22060,186,'Autonomous District','Khanty-Mansiysky avtonomnyy okrug-Yugra','RU-KHM',''),(22061,186,'Autonomous District','Nenetskiy avtonomnyy okrug','RU-NEN',''),(22062,186,'Autonomous District','Ust\'-Ordynskiy Buryatskiy avtonomnyy okrug','RU-UOB',''),(22063,186,'Autonomous District','Yamalo-Nenetskiy avtonomnyy okrug','RU-YAN',''),(22064,187,'Town council','Ville de Kigali','RW-01',''),(22065,187,'Province','Est','RW-02',''),(22066,187,'Province','Nord','RW-03',''),(22067,187,'Province','Ouest','RW-04',''),(22068,187,'Province','Sud','RW-05',''),(22069,198,'Province','Al B?hah','SA-11',''),(22070,198,'Province','Al ?ud?d ash Sham?liyah','SA-08',''),(22071,198,'Province','Al Jawf','SA-12',''),(22072,198,'Province','Al Mad?nah','SA-03',''),(22073,198,'Province','Al Qa??m','SA-05',''),(22074,198,'Province','Ar Riy??','SA-01',''),(22075,198,'Province','Ash Sharq?yah','SA-04',''),(22076,198,'Province','`As?r','SA-14',''),(22077,198,'Province','??\'il','SA-06',''),(22078,198,'Province','J?zan','SA-09',''),(22079,198,'Province','Makkah','SA-02',''),(22080,198,'Province','Najr?n','SA-10',''),(22081,198,'Province','Tab?k','SA-07',''),(22082,207,'Capital territory','Capital Territory (Honiara)','SB-CT',''),(22083,207,'Province','Central','SB-CE',''),(22084,207,'Province','Choiseul','SB-CH',''),(22085,207,'Province','Guadalcanal','SB-GU',''),(22086,207,'Province','Isabel','SB-IS',''),(22087,207,'Province','Makira','SB-MK',''),(22088,207,'Province','Malaita','SB-ML',''),(22089,207,'Province','Rennell and Bellona','SB-RB',''),(22090,207,'Province','Temotu','SB-TE',''),(22091,207,'Province','Western','SB-WE',''),(22092,201,'District','Anse aux Pins','SC-01',''),(22093,201,'District','Anse Boileau','SC-02',''),(22094,201,'District','Anse Étoile','SC-03',''),(22095,201,'District','Anse Louis','SC-04',''),(22096,201,'District','Anse Royale','SC-05',''),(22097,201,'District','Baie Lazare','SC-06',''),(22098,201,'District','Baie Sainte Anne','SC-07',''),(22099,201,'District','Beau Vallon','SC-08',''),(22100,201,'District','Bel Air','SC-09',''),(22101,201,'District','Bel Ombre','SC-10',''),(22102,201,'District','Cascade','SC-11',''),(22103,201,'District','Glacis','SC-12',''),(22104,201,'District','Grand\' Anse (Mahé)','SC-13',''),(22105,201,'District','Grand\' Anse (Praslin)','SC-14',''),(22106,201,'District','La Digue','SC-15',''),(22107,201,'District','La Rivière Anglaise','SC-16',''),(22108,201,'District','Mont Buxton','SC-17',''),(22109,201,'District','Mont Fleuri','SC-18',''),(22110,201,'District','Plaisance','SC-19',''),(22111,201,'District','Pointe La Rue','SC-20',''),(22112,201,'District','Port Glaud','SC-21',''),(22113,201,'District','Saint Louis','SC-22',''),(22114,201,'District','Takamaka','SC-23',''),(22115,213,'state','Al Ba?r al A?mar','SD-26',''),(22116,213,'state','Al Bu?ayr?t','SD-18',''),(22117,213,'state','Al Jaz?rah','SD-07',''),(22118,213,'state','Al Khar??m','SD-03',''),(22119,213,'state','Al Qa??rif','SD-06',''),(22120,213,'state','Al Wa?dah','SD-22',''),(22121,213,'state','An N?l','SD-04',''),(22122,213,'state','An N?l al Abya?','SD-08',''),(22123,213,'state','An N?l al Azraq','SD-24',''),(22124,213,'state','Ash Sham?l?yah','SD-01',''),(22125,213,'state','A‘?l? an N?l','SD-23',''),(22126,213,'state','Ba?r al Jabal','SD-17',''),(22127,213,'state','Gharb al Istiw?\'?yah','SD-16',''),(22128,213,'state','Gharb Ba?r al Ghaz?l','SD-14',''),(22129,213,'state','Gharb D?rf?r','SD-12',''),(22130,213,'state','Jan?b D?rf?r','SD-11',''),(22131,213,'state','Jan?b Kurduf?n','SD-13',''),(22132,213,'state','J?nqal?','SD-20',''),(22133,213,'state','Kassal?','SD-05',''),(22134,213,'state','Sham?l Ba?r al Ghaz?l','SD-15',''),(22135,213,'state','Sham?l D?rf?r','SD-02',''),(22136,213,'state','Sham?l Kurduf?n','SD-09',''),(22137,213,'state','Sharq al Istiw?\'?yah','SD-19',''),(22138,213,'state','Sinn?r','SD-25',''),(22139,213,'state','W?r?b','SD-21',''),(22140,217,'County','Blekinge län','SE-K',''),(22141,217,'County','Dalarnas län','SE-W',''),(22142,217,'County','Gotlands län','SE-I',''),(22143,217,'County','Gävleborgs län','SE-X',''),(22144,217,'County','Hallands län','SE-N',''),(22145,217,'County','Jämtlande län','SE-Z',''),(22146,217,'County','Jönköpings län','SE-F',''),(22147,217,'County','Kalmar län','SE-H',''),(22148,217,'County','Kronobergs län','SE-G',''),(22149,217,'County','Norrbottens län','SE-BD',''),(22150,217,'County','Skåne län','SE-M',''),(22151,217,'County','Stockholms län','SE-AB',''),(22152,217,'County','Södermanlands län','SE-D',''),(22153,217,'County','Uppsala län','SE-C',''),(22154,217,'County','Värmlands län','SE-S',''),(22155,217,'County','Västerbottens län','SE-AC',''),(22156,217,'County','Västernorrlands län','SE-Y',''),(22157,217,'County','Västmanlands län','SE-U',''),(22158,217,'County','Västra Götalands län','SE-Q',''),(22159,217,'County','Örebro län','SE-T',''),(22160,217,'County','Östergötlands län','SE-E',''),(22161,203,'district','Central Singapore','SG-01',''),(22162,203,'district','North East','SG-02',''),(22163,203,'district','North West','SG-03',''),(22164,203,'district','South East','SG-04',''),(22165,203,'district','South West','SG-05',''),(22166,189,'Dependency','Saint Helena','SH-SH',''),(22167,189,'Dependency','Tristan da Cunha','SH-TA',''),(22168,189,'Administrative area','Ascension','SH-AC',''),(22169,206,'Municipalities','Ajdovš?ina','SI-001',''),(22170,206,'Municipalities','Beltinci','SI-002',''),(22171,206,'Municipalities','Benedikt','SI-148',''),(22172,206,'Municipalities','Bistrica ob Sotli','SI-149',''),(22173,206,'Municipalities','Bled','SI-003',''),(22174,206,'Municipalities','Bloke','SI-150',''),(22175,206,'Municipalities','Bohinj','SI-004',''),(22176,206,'Municipalities','Borovnica','SI-005',''),(22177,206,'Municipalities','Bovec','SI-006',''),(22178,206,'Municipalities','Braslov?e','SI-151',''),(22179,206,'Municipalities','Brda','SI-007',''),(22180,206,'Municipalities','Brežice','SI-009',''),(22181,206,'Municipalities','Brezovica','SI-008',''),(22182,206,'Municipalities','Cankova','SI-152',''),(22183,206,'Municipalities','Celje','SI-011',''),(22184,206,'Municipalities','Cerklje na Gorenjskem','SI-012',''),(22185,206,'Municipalities','Cerknica','SI-013',''),(22186,206,'Municipalities','Cerkno','SI-014',''),(22187,206,'Municipalities','Cerkvenjak','SI-153',''),(22188,206,'Municipalities','?renšovci','SI-015',''),(22189,206,'Municipalities','?rna na Koroškem','SI-016',''),(22190,206,'Municipalities','?rnomelj','SI-017',''),(22191,206,'Municipalities','Destrnik','SI-018',''),(22192,206,'Municipalities','Diva?a','SI-019',''),(22193,206,'Municipalities','Dobje','SI-154',''),(22194,206,'Municipalities','Dobrepolje','SI-020',''),(22195,206,'Municipalities','Dobrna','SI-155',''),(22196,206,'Municipalities','Dobrova-Polhov Gradec','SI-021',''),(22197,206,'Municipalities','Dobrovnik/Dobronak','SI-156',''),(22198,206,'Municipalities','Dol pri Ljubljani','SI-022',''),(22199,206,'Municipalities','Dolenjske Toplice','SI-157',''),(22200,206,'Municipalities','Domžale','SI-023',''),(22201,206,'Municipalities','Dornava','SI-024',''),(22202,206,'Municipalities','Dravograd','SI-025',''),(22203,206,'Municipalities','Duplek','SI-026',''),(22204,206,'Municipalities','Gorenja vas-Poljane','SI-027',''),(22205,206,'Municipalities','Gorišnica','SI-028',''),(22206,206,'Municipalities','Gornja Radgona','SI-029',''),(22207,206,'Municipalities','Gornji Grad','SI-030',''),(22208,206,'Municipalities','Gornji Petrovci','SI-031',''),(22209,206,'Municipalities','Grad','SI-158',''),(22210,206,'Municipalities','Grosuplje','SI-032',''),(22211,206,'Municipalities','Hajdina','SI-159',''),(22212,206,'Municipalities','Ho?e-Slivnica','SI-160',''),(22213,206,'Municipalities','Hodoš/Hodos','SI-161',''),(22214,206,'Municipalities','Horjul','SI-162',''),(22215,206,'Municipalities','Hrastnik','SI-034',''),(22216,206,'Municipalities','Hrpelje-Kozina','SI-035',''),(22217,206,'Municipalities','Idrija','SI-036',''),(22218,206,'Municipalities','Ig','SI-037',''),(22219,206,'Municipalities','Ilirska Bistrica','SI-038',''),(22220,206,'Municipalities','Ivan?na Gorica','SI-039',''),(22221,206,'Municipalities','Izola/Isola','SI-040',''),(22222,206,'Municipalities','Jesenice','SI-041',''),(22223,206,'Municipalities','Jezersko','SI-163',''),(22224,206,'Municipalities','Juršinci','SI-042',''),(22225,206,'Municipalities','Kamnik','SI-043',''),(22226,206,'Municipalities','Kanal','SI-044',''),(22227,206,'Municipalities','Kidri?evo','SI-045',''),(22228,206,'Municipalities','Kobarid','SI-046',''),(22229,206,'Municipalities','Kobilje','SI-047',''),(22230,206,'Municipalities','Ko?evje','SI-048',''),(22231,206,'Municipalities','Komen','SI-049',''),(22232,206,'Municipalities','Komenda','SI-164',''),(22233,206,'Municipalities','Koper/Capodistria','SI-050',''),(22234,206,'Municipalities','Kostel','SI-165',''),(22235,206,'Municipalities','Kozje','SI-051',''),(22236,206,'Municipalities','Kranj','SI-052',''),(22237,206,'Municipalities','Kranjska Gora','SI-053',''),(22238,206,'Municipalities','Križevci','SI-166',''),(22239,206,'Municipalities','Krško','SI-054',''),(22240,206,'Municipalities','Kungota','SI-055',''),(22241,206,'Municipalities','Kuzma','SI-056',''),(22242,206,'Municipalities','Laško','SI-057',''),(22243,206,'Municipalities','Lenart','SI-058',''),(22244,206,'Municipalities','Lendava/Lendva','SI-059',''),(22245,206,'Municipalities','Litija','SI-060',''),(22246,206,'Municipalities','Ljubljana','SI-061',''),(22247,206,'Municipalities','Ljubno','SI-062',''),(22248,206,'Municipalities','Ljutomer','SI-063',''),(22249,206,'Municipalities','Logatec','SI-064',''),(22250,206,'Municipalities','Loška dolina','SI-065',''),(22251,206,'Municipalities','Loški Potok','SI-066',''),(22252,206,'Municipalities','Lovrenc na Pohorju','SI-167',''),(22253,206,'Municipalities','Lu?e','SI-067',''),(22254,206,'Municipalities','Lukovica','SI-068',''),(22255,206,'Municipalities','Majšperk','SI-069',''),(22256,206,'Municipalities','Maribor','SI-070',''),(22257,206,'Municipalities','Markovci','SI-168',''),(22258,206,'Municipalities','Medvode','SI-071',''),(22259,206,'Municipalities','Mengeš','SI-072',''),(22260,206,'Municipalities','Metlika','SI-073',''),(22261,206,'Municipalities','Mežica','SI-074',''),(22262,206,'Municipalities','Miklavž na Dravskem polju','SI-169',''),(22263,206,'Municipalities','Miren-Kostanjevica','SI-075',''),(22264,206,'Municipalities','Mirna Pe?','SI-170',''),(22265,206,'Municipalities','Mislinja','SI-076',''),(22266,206,'Municipalities','Morav?e','SI-077',''),(22267,206,'Municipalities','Moravske Toplice','SI-078',''),(22268,206,'Municipalities','Mozirje','SI-079',''),(22269,206,'Municipalities','Murska Sobota','SI-080',''),(22270,206,'Municipalities','Muta','SI-081',''),(22271,206,'Municipalities','Naklo','SI-082',''),(22272,206,'Municipalities','Nazarje','SI-083',''),(22273,206,'Municipalities','Nova Gorica','SI-084',''),(22274,206,'Municipalities','Novo mesto','SI-085',''),(22275,206,'Municipalities','Odranci','SI-086',''),(22276,206,'Municipalities','Oplotnica','SI-171',''),(22277,206,'Municipalities','Ormož','SI-087',''),(22278,206,'Municipalities','Osilnica','SI-088',''),(22279,206,'Municipalities','Pesnica','SI-089',''),(22280,206,'Municipalities','Piran/Pirano','SI-090',''),(22281,206,'Municipalities','Pivka','SI-091',''),(22282,206,'Municipalities','Pod?etrtek','SI-092',''),(22283,206,'Municipalities','Podlehnik','SI-172',''),(22284,206,'Municipalities','Podvelka','SI-093',''),(22285,206,'Municipalities','Polzela','SI-173',''),(22286,206,'Municipalities','Postojna','SI-094',''),(22287,206,'Municipalities','Prebold','SI-174',''),(22288,206,'Municipalities','Preddvor','SI-095',''),(22289,206,'Municipalities','Prevalje','SI-175',''),(22290,206,'Municipalities','Ptuj','SI-096',''),(22291,206,'Municipalities','Puconci','SI-097',''),(22292,206,'Municipalities','Ra?e-Fram','SI-098',''),(22293,206,'Municipalities','Rade?e','SI-099',''),(22294,206,'Municipalities','Radenci','SI-100',''),(22295,206,'Municipalities','Radlje ob Dravi','SI-101',''),(22296,206,'Municipalities','Radovljica','SI-102',''),(22297,206,'Municipalities','Ravne na Koroškem','SI-103',''),(22298,206,'Municipalities','Razkrižje','SI-176',''),(22299,206,'Municipalities','Ribnica','SI-104',''),(22300,206,'Municipalities','Ribnica na Pohorju','SI-177',''),(22301,206,'Municipalities','Rogaška Slatina','SI-106',''),(22302,206,'Municipalities','Rogašovci','SI-105',''),(22303,206,'Municipalities','Rogatec','SI-107',''),(22304,206,'Municipalities','Ruše','SI-108',''),(22305,206,'Municipalities','Šalovci','SI-033',''),(22306,206,'Municipalities','Selnica ob Dravi','SI-178',''),(22307,206,'Municipalities','Semi?','SI-109',''),(22308,206,'Municipalities','Šempeter-Vrtojba','SI-183',''),(22309,206,'Municipalities','Šen?ur','SI-117',''),(22310,206,'Municipalities','Šentilj','SI-118',''),(22311,206,'Municipalities','Šentjernej','SI-119',''),(22312,206,'Municipalities','Šentjur pri Celju','SI-120',''),(22313,206,'Municipalities','Sevnica','SI-110',''),(22314,206,'Municipalities','Sežana','SI-111',''),(22315,206,'Municipalities','Škocjan','SI-121',''),(22316,206,'Municipalities','Škofja Loka','SI-122',''),(22317,206,'Municipalities','Škofljica','SI-123',''),(22318,206,'Municipalities','Slovenj Gradec','SI-112',''),(22319,206,'Municipalities','Slovenska Bistrica','SI-113',''),(22320,206,'Municipalities','Slovenske Konjice','SI-114',''),(22321,206,'Municipalities','Šmarje pri Jelšah','SI-124',''),(22322,206,'Municipalities','Šmartno ob Paki','SI-125',''),(22323,206,'Municipalities','Šmartno pri Litiji','SI-194',''),(22324,206,'Municipalities','Sodražica','SI-179',''),(22325,206,'Municipalities','Sol?ava','SI-180',''),(22326,206,'Municipalities','Šoštanj','SI-126',''),(22327,206,'Municipalities','Starše','SI-115',''),(22328,206,'Municipalities','Štore','SI-127',''),(22329,206,'Municipalities','Sveta Ana','SI-181',''),(22330,206,'Municipalities','Sveti Andraž v Slovenskih goricah','SI-182',''),(22331,206,'Municipalities','Sveti Jurij','SI-116',''),(22332,206,'Municipalities','Tabor','SI-184',''),(22333,206,'Municipalities','Tišina','SI-010',''),(22334,206,'Municipalities','Tolmin','SI-128',''),(22335,206,'Municipalities','Trbovlje','SI-129',''),(22336,206,'Municipalities','Trebnje','SI-130',''),(22337,206,'Municipalities','Trnovska vas','SI-185',''),(22338,206,'Municipalities','Trži?','SI-131',''),(22339,206,'Municipalities','Trzin','SI-186',''),(22340,206,'Municipalities','Turniš?e','SI-132',''),(22341,206,'Municipalities','Velenje','SI-133',''),(22342,206,'Municipalities','Velika Polana','SI-187',''),(22343,206,'Municipalities','Velike Laš?e','SI-134',''),(22344,206,'Municipalities','Veržej','SI-188',''),(22345,206,'Municipalities','Videm','SI-135',''),(22346,206,'Municipalities','Vipava','SI-136',''),(22347,206,'Municipalities','Vitanje','SI-137',''),(22348,206,'Municipalities','Vodice','SI-138',''),(22349,206,'Municipalities','Vojnik','SI-139',''),(22350,206,'Municipalities','Vransko','SI-189',''),(22351,206,'Municipalities','Vrhnika','SI-140',''),(22352,206,'Municipalities','Vuzenica','SI-141',''),(22353,206,'Municipalities','Zagorje ob Savi','SI-142',''),(22354,206,'Municipalities','Žalec','SI-190',''),(22355,206,'Municipalities','Zavr?','SI-143',''),(22356,206,'Municipalities','Železniki','SI-146',''),(22357,206,'Municipalities','Žetale','SI-191',''),(22358,206,'Municipalities','Žiri','SI-147',''),(22359,206,'Municipalities','Žirovnica','SI-192',''),(22360,206,'Municipalities','Zre?e','SI-144',''),(22361,206,'Municipalities','Žužemberk','SI-193',''),(22362,205,'Region','Banskobystrický kraj','SK-BC',''),(22363,205,'Region','Bratislavský kraj','SK-BL',''),(22364,205,'Region','Košický kraj','SK-KI',''),(22365,205,'Region','Nitriansky kraj','SK-NJ',''),(22366,205,'Region','Prešovský kraj','SK-PV',''),(22367,205,'Region','Tren?iansky kraj','SK-TC',''),(22368,205,'Region','Trnavský kraj','SK-TA',''),(22369,205,'Region','Žilinský kraj','SK-ZI',''),(22370,202,'Area','Western Area (Freetown)','SL-W',''),(22371,202,'Province','Eastern','SL-E',''),(22372,202,'Province','Northern','SL-N',''),(22373,202,'Province','Southern (Sierra Leone)','SL-S',''),(22374,196,'Municipalities','Acquaviva','SM-01',''),(22375,196,'Municipalities','Borgo Maggiore','SM-06',''),(22376,196,'Municipalities','Chiesanuova','SM-02',''),(22377,196,'Municipalities','Domagnano','SM-03',''),(22378,196,'Municipalities','Faetano','SM-04',''),(22379,196,'Municipalities','Fiorentino','SM-05',''),(22380,196,'Municipalities','Montegiardino','SM-08',''),(22381,196,'Municipalities','San Marino','SM-07',''),(22382,196,'Municipalities','Serravalle','SM-09',''),(22383,199,'Region','Dakar','SN-DK',''),(22384,199,'Region','Diourbel','SN-DB',''),(22385,199,'Region','Fatick','SN-FK',''),(22386,199,'Region','Kaolack','SN-KL',''),(22387,199,'Region','Kolda','SN-KD',''),(22388,199,'Region','Louga','SN-LG',''),(22389,199,'Region','Matam','SN-MT',''),(22390,199,'Region','Saint-Louis','SN-SL',''),(22391,199,'Region','Tambacounda','SN-TC',''),(22392,199,'Region','Thiès','SN-TH',''),(22393,199,'Region','Ziguinchor','SN-ZG',''),(22394,208,'Region','Awdal','SO-AW',''),(22395,208,'Region','Bakool','SO-BK',''),(22396,208,'Region','Banaadir','SO-BN',''),(22397,208,'Region','Bari','SO-BR',''),(22398,208,'Region','Bay','SO-BY',''),(22399,208,'Region','Galguduud','SO-GA',''),(22400,208,'Region','Gedo','SO-GE',''),(22401,208,'Region','Hiirsan','SO-HI',''),(22402,208,'Region','Jubbada Dhexe','SO-JD',''),(22403,208,'Region','Jubbada Hoose','SO-JH',''),(22404,208,'Region','Mudug','SO-MU',''),(22405,208,'Region','Nugaal','SO-NU',''),(22406,208,'Region','Saneag','SO-SA',''),(22407,208,'Region','Shabeellaha Dhexe','SO-SD',''),(22408,208,'Region','Shabeellaha Hoose','SO-SH',''),(22409,208,'Region','Sool','SO-SO',''),(22410,208,'Region','Togdheer','SO-TO',''),(22411,208,'Region','Woqooyi Galbeed','SO-WO',''),(22412,214,'District','Brokopondo','SR-BR',''),(22413,214,'District','Commewijne','SR-CM',''),(22414,214,'District','Coronie','SR-CR',''),(22415,214,'District','Marowijne','SR-MA',''),(22416,214,'District','Nickerie','SR-NI',''),(22417,214,'District','Para','SR-PR',''),(22418,214,'District','Paramaribo','SR-PM',''),(22419,214,'District','Saramacca','SR-SA',''),(22420,214,'District','Sipaliwini','SR-SI',''),(22421,214,'District','Wanica','SR-WA',''),(22422,197,'Municipality','Príncipe','ST-P',''),(22423,197,'Municipality','São Tomé','ST-S',''),(22424,70,'Department','Ahuachapán','SV-AH',''),(22425,70,'Department','Cabañas','SV-CA',''),(22426,70,'Department','Cuscatlán','SV-CU',''),(22427,70,'Department','Chalatenango','SV-CH',''),(22428,70,'Department','La Libertad','SV-LI',''),(22429,70,'Department','La Paz','SV-PA',''),(22430,70,'Department','La Unión','SV-UN',''),(22431,70,'Department','Morazán','SV-MO',''),(22432,70,'Department','San Miguel','SV-SM',''),(22433,70,'Department','San Salvador','SV-SS',''),(22434,70,'Department','Santa Ana','SV-SA',''),(22435,70,'Department','San Vicente','SV-SV',''),(22436,70,'Department','Sonsonate','SV-SO',''),(22437,70,'Department','Usulután','SV-US',''),(22438,219,'Governorate','Al Hasakah','SY-HA',''),(22439,219,'Governorate','Al Ladhiqiyah','SY-LA',''),(22440,219,'Governorate','Al Qunaytirah','SY-QU',''),(22441,219,'Governorate','Ar Raqqah','SY-RA',''),(22442,219,'Governorate','As Suwayda\'','SY-SU',''),(22443,219,'Governorate','Dar\'a','SY-DR',''),(22444,219,'Governorate','Dayr az Zawr','SY-DY',''),(22445,219,'Governorate','Dimashq','SY-DI',''),(22446,219,'Governorate','Halab','SY-HL',''),(22447,219,'Governorate','Hamah','SY-HM',''),(22448,219,'Governorate','Homs','SY-HI',''),(22449,219,'Governorate','Idlib','SY-ID',''),(22450,219,'Governorate','Rif Dimashq','SY-RD',''),(22451,219,'Governorate','Tartus','SY-TA',''),(22452,216,'District','Hhohho','SZ-HH',''),(22453,216,'District','Lubombo','SZ-LU',''),(22454,216,'District','Manzini','SZ-MA',''),(22455,216,'District','Shiselweni','SZ-SH',''),(22456,47,'Region','Batha','TD-BA',''),(22457,47,'Region','Borkou-Ennedi-Tibesti','TD-BET',''),(22458,47,'Region','Chari-Baguirmi','TD-CB',''),(22459,47,'Region','Guéra','TD-GR',''),(22460,47,'Region','Hadjer Lamis','TD-HL',''),(22461,47,'Region','Kanem','TD-KA',''),(22462,47,'Region','Lac','TD-LC',''),(22463,47,'Region','Logone-Occidental','TD-LO',''),(22464,47,'Region','Logone-Oriental','TD-LR',''),(22465,47,'Region','Mandoul','TD-MA',''),(22466,47,'Region','Mayo-Kébbi-Est','TD-ME',''),(22467,47,'Region','Mayo-Kébbi-Ouest','TD-MO',''),(22468,47,'Region','Moyen-Chari','TD-MC',''),(22469,47,'Region','Ndjamena','TD-ND',''),(22470,47,'Region','Ouaddaï','TD-OD',''),(22471,47,'Region','Salamat','TD-SA',''),(22472,47,'Region','Tandjilé','TD-TA',''),(22473,47,'Region','Wadi Fira','TD-WF',''),(22474,225,'Region','Région du Centre','TG-C',''),(22475,225,'Region','Région de la Kara','TG-K',''),(22476,225,'Region','Région Maritime','TG-M',''),(22477,225,'Region','Région des Plateaux','TG-P',''),(22478,225,'Region','Région des Savannes','TG-S',''),(22479,223,'Municipality','Krung Thep Maha Nakhon Bangkok','TH-10',''),(22480,223,'Province','Phatthaya','TH-S',''),(22481,223,'Province','Amnat Charoen','TH-37',''),(22482,223,'Province','Ang Thong','TH-15',''),(22483,223,'Province','Buri Ram','TH-31',''),(22484,223,'Province','Chachoengsao','TH-24',''),(22485,223,'Province','Chai Nat','TH-18',''),(22486,223,'Province','Chaiyaphum','TH-36',''),(22487,223,'Province','Chanthaburi','TH-22',''),(22488,223,'Province','Chiang Mai','TH-50',''),(22489,223,'Province','Chiang Rai','TH-57',''),(22490,223,'Province','Chon Buri','TH-20',''),(22491,223,'Province','Chumphon','TH-86',''),(22492,223,'Province','Kalasin','TH-46',''),(22493,223,'Province','Kamphaeng Phet','TH-62',''),(22494,223,'Province','Kanchanaburi','TH-71',''),(22495,223,'Province','Khon Kaen','TH-40',''),(22496,223,'Province','Krabi','TH-81',''),(22497,223,'Province','Lampang','TH-52',''),(22498,223,'Province','Lamphun','TH-51',''),(22499,223,'Province','Loei','TH-42',''),(22500,223,'Province','Lop Buri','TH-16',''),(22501,223,'Province','Mae Hong Son','TH-58',''),(22502,223,'Province','Maha Sarakham','TH-44',''),(22503,223,'Province','Mukdahan','TH-49',''),(22504,223,'Province','Nakhon Nayok','TH-26',''),(22505,223,'Province','Nakhon Pathom','TH-73',''),(22506,223,'Province','Nakhon Phanom','TH-48',''),(22507,223,'Province','Nakhon Ratchasima','TH-30',''),(22508,223,'Province','Nakhon Sawan','TH-60',''),(22509,223,'Province','Nakhon Si Thammarat','TH-80',''),(22510,223,'Province','Nan','TH-55',''),(22511,223,'Province','Narathiwat','TH-96',''),(22512,223,'Province','Nong Bua Lam Phu','TH-39',''),(22513,223,'Province','Nong Khai','TH-43',''),(22514,223,'Province','Nonthaburi','TH-12',''),(22515,223,'Province','Pathum Thani','TH-13',''),(22516,223,'Province','Pattani','TH-94',''),(22517,223,'Province','Phangnga','TH-82',''),(22518,223,'Province','Phatthalung','TH-93',''),(22519,223,'Province','Phayao','TH-56',''),(22520,223,'Province','Phetchabun','TH-67',''),(22521,223,'Province','Phetchaburi','TH-76',''),(22522,223,'Province','Phichit','TH-66',''),(22523,223,'Province','Phitsanulok','TH-65',''),(22524,223,'Province','Phrae','TH-54',''),(22525,223,'Province','Phra Nakhon Si Ayutthaya','TH-14',''),(22526,223,'Province','Phuket','TH-83',''),(22527,223,'Province','Prachin Buri','TH-25',''),(22528,223,'Province','Prachuap Khiri Khan','TH-77',''),(22529,223,'Province','Ranong','TH-85',''),(22530,223,'Province','Ratchaburi','TH-70',''),(22531,223,'Province','Rayong','TH-21',''),(22532,223,'Province','Roi Et','TH-45',''),(22533,223,'Province','Sa Kaeo','TH-27',''),(22534,223,'Province','Sakon Nakhon','TH-47',''),(22535,223,'Province','Samut Prakan','TH-11',''),(22536,223,'Province','Samut Sakhon','TH-74',''),(22537,223,'Province','Samut Songkhram','TH-75',''),(22538,223,'Province','Saraburi','TH-19',''),(22539,223,'Province','Satun','TH-91',''),(22540,223,'Province','Sing Buri','TH-17',''),(22541,223,'Province','Si Sa Ket','TH-33',''),(22542,223,'Province','Songkhla','TH-90',''),(22543,223,'Province','Sukhothai','TH-64',''),(22544,223,'Province','Suphan Buri','TH-72',''),(22545,223,'Province','Surat Thani','TH-84',''),(22546,223,'Province','Surin','TH-32',''),(22547,223,'Province','Tak','TH-63',''),(22548,223,'Province','Trang','TH-92',''),(22549,223,'Province','Trat','TH-23',''),(22550,223,'Province','Ubon Ratchathani','TH-34',''),(22551,223,'Province','Udon Thani','TH-41',''),(22552,223,'Province','Uthai Thani','TH-61',''),(22553,223,'Province','Uttaradit','TH-53',''),(22554,223,'Province','Yala','TH-95',''),(22555,223,'Province','Yasothon','TH-35',''),(22556,221,'Autonomous region','Gorno-Badakhshan','TJ-GB',''),(22557,221,'Region','Khatlon','TJ-KT',''),(22558,221,'Region','Sughd','TJ-SU',''),(22559,224,'District','Aileu','TL-AL',''),(22560,224,'District','Ainaro','TL-AN',''),(22561,224,'District','Baucau','TL-BA',''),(22562,224,'District','Bobonaro','TL-BO',''),(22563,224,'District','Cova Lima','TL-CO',''),(22564,224,'District','Dili','TL-DI',''),(22565,224,'District','Ermera','TL-ER',''),(22566,224,'District','Lautem','TL-LA',''),(22567,224,'District','Liquiça','TL-LI',''),(22568,224,'District','Manatuto','TL-MT',''),(22569,224,'District','Manufahi','TL-MF',''),(22570,224,'District','Oecussi','TL-OE',''),(22571,224,'District','Viqueque','TL-VI',''),(22572,231,'Region','Ahal','TM-A',''),(22573,231,'Region','Balkan','TM-B',''),(22574,231,'Region','Da?oguz','TM-D',''),(22575,231,'Region','Lebap','TM-L',''),(22576,231,'Region','Mary','TM-M',''),(22577,229,'Governorate','Béja','TN-31',''),(22578,229,'Governorate','Ben Arous','TN-13',''),(22579,229,'Governorate','Bizerte','TN-23',''),(22580,229,'Governorate','Gabès','TN-81',''),(22581,229,'Governorate','Gafsa','TN-71',''),(22582,229,'Governorate','Jendouba','TN-32',''),(22583,229,'Governorate','Kairouan','TN-41',''),(22584,229,'Governorate','Kasserine','TN-42',''),(22585,229,'Governorate','Kebili','TN-73',''),(22586,229,'Governorate','L\'Ariana','TN-12',''),(22587,229,'Governorate','Le Kef','TN-33',''),(22588,229,'Governorate','Mahdia','TN-53',''),(22589,229,'Governorate','La Manouba','TN-14',''),(22590,229,'Governorate','Medenine','TN-82',''),(22591,229,'Governorate','Monastir','TN-52',''),(22592,229,'Governorate','Nabeul','TN-21',''),(22593,229,'Governorate','Sfax','TN-61',''),(22594,229,'Governorate','Sidi Bouzid','TN-43',''),(22595,229,'Governorate','Siliana','TN-34',''),(22596,229,'Governorate','Sousse','TN-51',''),(22597,229,'Governorate','Tataouine','TN-83',''),(22598,229,'Governorate','Tozeur','TN-72',''),(22599,229,'Governorate','Tunis','TN-11',''),(22600,229,'Governorate','Zaghouan','TN-22',''),(22601,227,'Division','\'Eua','TO-01',''),(22602,227,'Division','Ha\'apai','TO-02',''),(22603,227,'Division','Niuas','TO-03',''),(22604,227,'Division','Tongatapu','TO-04',''),(22605,227,'Division','Vava\'u','TO-05',''),(22606,230,'Province','Adana','TR-01',''),(22607,230,'Province','Ad?yaman','TR-02',''),(22608,230,'Province','Afyon','TR-03',''),(22609,230,'Province','A?r?','TR-04',''),(22610,230,'Province','Aksaray','TR-68',''),(22611,230,'Province','Amasya','TR-05',''),(22612,230,'Province','Ankara','TR-06',''),(22613,230,'Province','Antalya','TR-07',''),(22614,230,'Province','Ardahan','TR-75',''),(22615,230,'Province','Artvin','TR-08',''),(22616,230,'Province','Ayd?n','TR-09',''),(22617,230,'Province','Bal?kesir','TR-10',''),(22618,230,'Province','Bart?n','TR-74',''),(22619,230,'Province','Batman','TR-72',''),(22620,230,'Province','Bayburt','TR-69',''),(22621,230,'Province','Bilecik','TR-11',''),(22622,230,'Province','Bingöl','TR-12',''),(22623,230,'Province','Bitlis','TR-13',''),(22624,230,'Province','Bolu','TR-14',''),(22625,230,'Province','Burdur','TR-15',''),(22626,230,'Province','Bursa','TR-16',''),(22627,230,'Province','Çanakkale','TR-17',''),(22628,230,'Province','Çank?r?','TR-18',''),(22629,230,'Province','Çorum','TR-19',''),(22630,230,'Province','Denizli','TR-20',''),(22631,230,'Province','Diyarbak?r','TR-21',''),(22632,230,'Province','Düzce','TR-81',''),(22633,230,'Province','Edirne','TR-22',''),(22634,230,'Province','Elaz??','TR-23',''),(22635,230,'Province','Erzincan','TR-24',''),(22636,230,'Province','Erzurum','TR-25',''),(22637,230,'Province','Eski?ehir','TR-26',''),(22638,230,'Province','Gaziantep','TR-27',''),(22639,230,'Province','Giresun','TR-28',''),(22640,230,'Province','Gümü?hane','TR-29',''),(22641,230,'Province','Hakkâri','TR-30',''),(22642,230,'Province','Hatay','TR-31',''),(22643,230,'Province','I?d?r','TR-76',''),(22644,230,'Province','Isparta','TR-32',''),(22645,230,'Province','?çel','TR-33',''),(22646,230,'Province','?stanbul','TR-34',''),(22647,230,'Province','?zmir','TR-35',''),(22648,230,'Province','Kahramanmara?','TR-46',''),(22649,230,'Province','Karabük','TR-78',''),(22650,230,'Province','Karaman','TR-70',''),(22651,230,'Province','Kars','TR-36',''),(22652,230,'Province','Kastamonu','TR-37',''),(22653,230,'Province','Kayseri','TR-38',''),(22654,230,'Province','K?r?kkale','TR-71',''),(22655,230,'Province','K?rklareli','TR-39',''),(22656,230,'Province','K?r?ehir','TR-40',''),(22657,230,'Province','Kilis','TR-79',''),(22658,230,'Province','Kocaeli','TR-41',''),(22659,230,'Province','Konya','TR-42',''),(22660,230,'Province','Kütahya','TR-43',''),(22661,230,'Province','Malatya','TR-44',''),(22662,230,'Province','Manisa','TR-45',''),(22663,230,'Province','Mardin','TR-47',''),(22664,230,'Province','Mu?la','TR-48',''),(22665,230,'Province','Mu?','TR-49',''),(22666,230,'Province','Nev?ehir','TR-50',''),(22667,230,'Province','Ni?de','TR-51',''),(22668,230,'Province','Ordu','TR-52',''),(22669,230,'Province','Osmaniye','TR-80',''),(22670,230,'Province','Rize','TR-53',''),(22671,230,'Province','Sakarya','TR-54',''),(22672,230,'Province','Samsun','TR-55',''),(22673,230,'Province','Siirt','TR-56',''),(22674,230,'Province','Sinop','TR-57',''),(22675,230,'Province','Sivas','TR-58',''),(22676,230,'Province','?anl?urfa','TR-63',''),(22677,230,'Province','??rnak','TR-73',''),(22678,230,'Province','Tekirda?','TR-59',''),(22679,230,'Province','Tokat','TR-60',''),(22680,230,'Province','Trabzon','TR-61',''),(22681,230,'Province','Tunceli','TR-62',''),(22682,230,'Province','U?ak','TR-64',''),(22683,230,'Province','Van','TR-65',''),(22684,230,'Province','Yalova','TR-77',''),(22685,230,'Province','Yozgat','TR-66',''),(22686,230,'Province','Zonguldak','TR-67',''),(22687,228,'Region','Couva-Tabaquite-Talparo','TT-CTT',''),(22688,228,'Region','Diego Martin','TT-DMN',''),(22689,228,'Region','Eastern Tobago','TT-ETO',''),(22690,228,'Region','Penal-Debe','TT-PED',''),(22691,228,'Region','Princes Town','TT-PRT',''),(22692,228,'Region','Rio Claro-Mayaro','TT-RCM',''),(22693,228,'Region','Sangre Grande','TT-SGE',''),(22694,228,'Region','San Juan-Laventille','TT-SJL',''),(22695,228,'Region','Siparia','TT-SIP',''),(22696,228,'Region','Tunapuna-Piarco','TT-TUP',''),(22697,228,'Region','Western Tobago','TT-WTO',''),(22698,228,'Borough','Arima','TT-ARI',''),(22699,228,'Borough','Chaguanas','TT-CHA',''),(22700,228,'Borough','Point Fortin','TT-PTF',''),(22701,228,'City','Port of Spain','TT-POS',''),(22702,228,'City','San Fernando','TT-SFO',''),(22703,233,'Town council','Funafuti','TV-FUN',''),(22704,233,'Island council','Nanumanga','TV-NMG',''),(22705,233,'Island council','Nanumea','TV-NMA',''),(22706,233,'Island council','Niutao','TV-NIT',''),(22707,233,'Island council','Nui','TV-NIU',''),(22708,233,'Island council','Nukufetau','TV-NKF',''),(22709,233,'Island council','Nukulaelae','TV-NKL',''),(22710,233,'Island council','Vaitupu','TV-VAI',''),(22711,220,'District','Changhua','TW-CHA',''),(22712,220,'District','Chiayi','TW-CYQ',''),(22713,220,'District','Hsinchu','TW-HSQ',''),(22714,220,'District','Hualien','TW-HUA',''),(22715,220,'District','Ilan','TW-ILA',''),(22716,220,'District','Kaohsiung','TW-KHQ',''),(22717,220,'District','Miaoli','TW-MIA',''),(22718,220,'District','Nantou','TW-NAN',''),(22719,220,'District','Penghu','TW-PEN',''),(22720,220,'District','Pingtung','TW-PIF',''),(22721,220,'District','Taichung','TW-TXQ',''),(22722,220,'District','Tainan','TW-TNQ',''),(22723,220,'District','Taipei','TW-TPQ',''),(22724,220,'District','Taitung','TW-TTT',''),(22725,220,'District','Taoyuan','TW-TAO',''),(22726,220,'District','Yunlin','TW-YUN',''),(22727,220,'Municipality','Chiay City','TW-CYI',''),(22728,220,'Municipality','Hsinchui City','TW-HSZ',''),(22729,220,'Municipality','Keelung City','TW-KEE',''),(22730,220,'Municipality','Taichung City','TW-TXG',''),(22731,220,'Municipality','Tainan City','TW-TNN',''),(22732,220,'Special Municipality','Kaohsiung City','TW-KHH',''),(22733,220,'Special Municipality','Taipei City','TW-TPE',''),(22734,222,'Region','Arusha','TZ-01',''),(22735,222,'Region','Dar-es-Salaam','TZ-02',''),(22736,222,'Region','Dodoma','TZ-03',''),(22737,222,'Region','Iringa','TZ-04',''),(22738,222,'Region','Kagera','TZ-05',''),(22739,222,'Region','Kaskazini Pemba','TZ-06',''),(22740,222,'Region','Kaskazini Unguja','TZ-07',''),(22741,222,'Region','Kigoma','TZ-08',''),(22742,222,'Region','Kilimanjaro','TZ-09',''),(22743,222,'Region','Kusini Pemba','TZ-10',''),(22744,222,'Region','Kusini Unguja','TZ-11',''),(22745,222,'Region','Lindi','TZ-12',''),(22746,222,'Region','Manyara','TZ-26',''),(22747,222,'Region','Mara','TZ-13',''),(22748,222,'Region','Mbeya','TZ-14',''),(22749,222,'Region','Mjini Magharibi','TZ-15',''),(22750,222,'Region','Morogoro','TZ-16',''),(22751,222,'Region','Mtwara','TZ-17',''),(22752,222,'Region','Mwanza','TZ-18',''),(22753,222,'Region','Pwani','TZ-19',''),(22754,222,'Region','Rukwa','TZ-20',''),(22755,222,'Region','Ruvuma','TZ-21',''),(22756,222,'Region','Shinyanga','TZ-22',''),(22757,222,'Region','Singida','TZ-23',''),(22758,222,'Region','Tabora','TZ-24',''),(22759,222,'Region','Tanga','TZ-25',''),(22760,235,'Province','Cherkas\'ka Oblast\'','UA-71',''),(22761,235,'Province','Chernihivs\'ka Oblast\'','UA-74',''),(22762,235,'Province','Chernivets\'ka Oblast\'','UA-77',''),(22763,235,'Province','Dnipropetrovs\'ka Oblast\'','UA-12',''),(22764,235,'Province','Donets\'ka Oblast\'','UA-14',''),(22765,235,'Province','Ivano-Frankivs\'ka Oblast\'','UA-26',''),(22766,235,'Province','Kharkivs\'ka Oblast\'','UA-63',''),(22767,235,'Province','Khersons\'ka Oblast\'','UA-65',''),(22768,235,'Province','Khmel\'nyts\'ka Oblast\'','UA-68',''),(22769,235,'Province','Kirovohrads\'ka Oblast\'','UA-35',''),(22770,235,'Province','Kyïvs\'ka Oblast\'','UA-32',''),(22771,235,'Province','Luhans\'ka Oblast\'','UA-09',''),(22772,235,'Province','L\'vivs\'ka Oblast\'','UA-46',''),(22773,235,'Province','Mykolaïvs\'ka Oblast\'','UA-48',''),(22774,235,'Province','Odes\'ka Oblast\'','UA-51',''),(22775,235,'Province','Poltavs\'ka Oblast\'','UA-53',''),(22776,235,'Province','Rivnens\'ka Oblast\'','UA-56',''),(22777,235,'Province','Sums \'ka Oblast\'','UA-59',''),(22778,235,'Province','Ternopil\'s\'ka Oblast\'','UA-61',''),(22779,235,'Province','Vinnyts\'ka Oblast\'','UA-05',''),(22780,235,'Province','Volyns\'ka Oblast\'','UA-07',''),(22781,235,'Province','Zakarpats\'ka Oblast\'','UA-21',''),(22782,235,'Province','Zaporiz\'ka Oblast\'','UA-23',''),(22783,235,'Province','Zhytomyrs\'ka Oblast\'','UA-18',''),(22784,235,'Autonomous republic','Respublika Krym','UA-43',''),(22785,235,'City','Kyïvs\'ka mis\'ka rada','UA-30',''),(22786,235,'City','Sevastopol','UA-40',''),(22787,234,'Geographical region','Central','UG C',''),(22788,234,'Geographical region','Eastern','UG E',''),(22789,234,'Geographical region','Northern','UG N',''),(22790,234,'Geographical region','Western','UG W',''),(22791,234,'District','Abim','UG-317',''),(22792,234,'District','Adjumani','UG-301',''),(22793,234,'District','Amolatar','UG-314',''),(22794,234,'District','Amuria','UG-216',''),(22795,234,'District','Amuru','UG-319',''),(22796,234,'District','Apac','UG-302',''),(22797,234,'District','Arua','UG-303',''),(22798,234,'District','Budaka','UG-217',''),(22799,234,'District','Bugiri','UG-201',''),(22800,234,'District','Bukwa','UG-218',''),(22801,234,'District','Bulisa','UG-419',''),(22802,234,'District','Bundibugyo','UG-401',''),(22803,234,'District','Bushenyi','UG-402',''),(22804,234,'District','Busia','UG-202',''),(22805,234,'District','Butaleja','UG-219',''),(22806,234,'District','Dokolo','UG-318',''),(22807,234,'District','Gulu','UG-304',''),(22808,234,'District','Hoima','UG-403',''),(22809,234,'District','Ibanda','UG-416',''),(22810,234,'District','Iganga','UG-203',''),(22811,234,'District','Isingiro','UG-417',''),(22812,234,'District','Jinja','UG-204',''),(22813,234,'District','Kaabong','UG-315',''),(22814,234,'District','Kabale','UG-404',''),(22815,234,'District','Kabarole','UG-405',''),(22816,234,'District','Kaberamaido','UG-213',''),(22817,234,'District','Kalangala','UG-101',''),(22818,234,'District','Kaliro','UG-220',''),(22819,234,'District','Kampala','UG-102',''),(22820,234,'District','Kamuli','UG-205',''),(22821,234,'District','Kamwenge','UG-413',''),(22822,234,'District','Kanungu','UG-414',''),(22823,234,'District','Kapchorwa','UG-206',''),(22824,234,'District','Kasese','UG-406',''),(22825,234,'District','Katakwi','UG-207',''),(22826,234,'District','Kayunga','UG-112',''),(22827,234,'District','Kibaale','UG-407',''),(22828,234,'District','Kiboga','UG-103',''),(22829,234,'District','Kiruhura','UG-418',''),(22830,234,'District','Kisoro','UG-408',''),(22831,234,'District','Kitgum','UG-305',''),(22832,234,'District','Koboko','UG-316',''),(22833,234,'District','Kotido','UG-306',''),(22834,234,'District','Kumi','UG-208',''),(22835,234,'District','Kyenjojo','UG-415',''),(22836,234,'District','Lira','UG-307',''),(22837,234,'District','Luwero','UG-104',''),(22838,234,'District','Manafwa','UG-221',''),(22839,234,'District','Maracha','UG-320',''),(22840,234,'District','Masaka','UG-105',''),(22841,234,'District','Masindi','UG-409',''),(22842,234,'District','Mayuge','UG-214',''),(22843,234,'District','Mbale','UG-209',''),(22844,234,'District','Mbarara','UG-410',''),(22845,234,'District','Mityana','UG-114',''),(22846,234,'District','Moroto','UG-308',''),(22847,234,'District','Moyo','UG-309',''),(22848,234,'District','Mpigi','UG-106',''),(22849,234,'District','Mubende','UG-107',''),(22850,234,'District','Mukono','UG-108',''),(22851,234,'District','Nakapiripirit','UG-311',''),(22852,234,'District','Nakaseke','UG-115',''),(22853,234,'District','Nakasongola','UG-109',''),(22854,234,'District','Namutumba','UG-222',''),(22855,234,'District','Nebbi','UG-310',''),(22856,234,'District','Ntungamo','UG-411',''),(22857,234,'District','Oyam','UG-321',''),(22858,234,'District','Pader','UG-312',''),(22859,234,'District','Pallisa','UG-210',''),(22860,234,'District','Rakai','UG-110',''),(22861,234,'District','Rukungiri','UG-412',''),(22862,234,'District','Sembabule','UG-111',''),(22863,234,'District','Sironko','UG-215',''),(22864,234,'District','Soroti','UG-211',''),(22865,234,'District','Tororo','UG-212',''),(22866,234,'District','Wakiso','UG-113',''),(22867,234,'District','Yumbe','UG-313',''),(22868,239,'Territory','Baker Island','UM-81',''),(22869,239,'Territory','Howland Island','UM-84',''),(22870,239,'Territory','Jarvis Island','UM-86',''),(22871,239,'Territory','Johnston Atoll','UM-67',''),(22872,239,'Territory','Kingman Reef','UM-89',''),(22873,239,'Territory','Midway Islands','UM-71',''),(22874,239,'Territory','Navassa Island','UM-76',''),(22875,239,'Territory','Palmyra Atoll','UM-95',''),(22876,239,'Territory','Wake Island','UM-79',''),(22877,238,'State','Alabama','US-AL',''),(22878,238,'State','Alaska','US-AK',''),(22879,238,'State','Arizona','US-AZ',''),(22880,238,'State','Arkansas','US-AR',''),(22881,238,'State','California','US-CA',''),(22882,238,'State','Colorado','US-CO',''),(22883,238,'State','Connecticut','US-CT',''),(22884,238,'State','Delaware','US-DE',''),(22885,238,'State','Florida','US-FL',''),(22886,238,'State','Georgia','US-GA',''),(22887,238,'State','Hawaii','US-HI',''),(22888,238,'State','Idaho','US-ID',''),(22889,238,'State','Illinois','US-IL',''),(22890,238,'State','Indiana','US-IN',''),(22891,238,'State','Iowa','US-IA',''),(22892,238,'State','Kansas','US-KS',''),(22893,238,'State','Kentucky','US-KY',''),(22894,238,'State','Louisiana','US-LA',''),(22895,238,'State','Maine','US-ME',''),(22896,238,'State','Maryland','US-MD',''),(22897,238,'State','Massachusetts','US-MA',''),(22898,238,'State','Michigan','US-MI',''),(22899,238,'State','Minnesota','US-MN',''),(22900,238,'State','Mississippi','US-MS',''),(22901,238,'State','Missouri','US-MO',''),(22902,238,'State','Montana','US-MT',''),(22903,238,'State','Nebraska','US-NE',''),(22904,238,'State','Nevada','US-NV',''),(22905,238,'State','New Hampshire','US-NH',''),(22906,238,'State','New Jersey','US-NJ',''),(22907,238,'State','New Mexico','US-NM',''),(22908,238,'State','New York','US-NY',''),(22909,238,'State','North Carolina','US-NC',''),(22910,238,'State','North Dakota','US-ND',''),(22911,238,'State','Ohio','US-OH',''),(22912,238,'State','Oklahoma','US-OK',''),(22913,238,'State','Oregon','US-OR',''),(22914,238,'State','Pennsylvania','US-PA',''),(22915,238,'State','Rhode Island','US-RI',''),(22916,238,'State','South Carolina','US-SC',''),(22917,238,'State','South Dakota','US-SD',''),(22918,238,'State','Tennessee','US-TN',''),(22919,238,'State','Texas','US-TX',''),(22920,238,'State','Utah','US-UT',''),(22921,238,'State','Vermont','US-VT',''),(22922,238,'State','Virginia','US-VA',''),(22923,238,'State','Washington','US-WA',''),(22924,238,'State','West Virginia','US-WV',''),(22925,238,'State','Wisconsin','US-WI',''),(22926,238,'State','Wyoming','US-WY',''),(22927,238,'District','District of Columbia','US-DC',''),(22928,238,'Outlying area','American Samoa','US-AS',''),(22929,238,'Outlying area','Guam','US-GU',''),(22930,238,'Outlying area','Northern Mariana Islands','US-MP',''),(22931,238,'Outlying area','Puerto Rico','US-PR',''),(22932,238,'Outlying area','United States Minor Outlying Islands','US-UM',''),(22933,238,'Outlying area','Virgin Islands','US-VI',''),(22934,240,'Department','Artigas','UY-AR',''),(22935,240,'Department','Canelones','UY-CA',''),(22936,240,'Department','Cerro Largo','UY-CL',''),(22937,240,'Department','Colonia','UY-CO',''),(22938,240,'Department','Durazno','UY-DU',''),(22939,240,'Department','Flores','UY-FS',''),(22940,240,'Department','Florida','UY-FD',''),(22941,240,'Department','Lavalleja','UY-LA',''),(22942,240,'Department','Maldonado','UY-MA',''),(22943,240,'Department','Montevideo','UY-MO',''),(22944,240,'Department','Paysandú','UY-PA',''),(22945,240,'Department','Río Negro','UY-RN',''),(22946,240,'Department','Rivera','UY-RV',''),(22947,240,'Department','Rocha','UY-RO',''),(22948,240,'Department','Salto','UY-SA',''),(22949,240,'Department','San José','UY-SJ',''),(22950,240,'Department','Soriano','UY-SO',''),(22951,240,'Department','Tacuarembó','UY-TA',''),(22952,240,'Department','Treinta y Tres','UY-TT',''),(22953,241,'City','Toshkent','UZ-TK',''),(22954,241,'Region','Andijon','UZ-AN',''),(22955,241,'Region','Buxoro','UZ-BU',''),(22956,241,'Region','Farg\'ona','UZ-FA',''),(22957,241,'Region','Jizzax','UZ-JI',''),(22958,241,'Region','Namangan','UZ-NG',''),(22959,241,'Region','Navoiy','UZ-NW',''),(22960,241,'Region','Qashqadaryo','UZ-QA',''),(22961,241,'Region','Samarqand','UZ-SA',''),(22962,241,'Region','Sirdaryo','UZ-SI',''),(22963,241,'Region','Surxondaryo','UZ-SU',''),(22964,241,'Region','Toshkent','UZ-TO',''),(22965,241,'Region','Xorazm','UZ-XO',''),(22966,241,'Republic','Qoraqalpog\'iston Respublikasi','UZ-QR',''),(22967,194,'Parish','Charlotte','VC-01',''),(22968,194,'Parish','Grenadines','VC-06',''),(22969,194,'Parish','Saint Andrew','VC-02',''),(22970,194,'Parish','Saint David','VC-03',''),(22971,194,'Parish','Saint George','VC-04',''),(22972,194,'Parish','Saint Patrick','VC-05',''),(22973,244,'Federal Dependency','Dependencias Federales','VE-W',''),(22974,244,'Federal District','Distrito Federal','VE-A',''),(22975,244,'State','Amazonas','VE-Z',''),(22976,244,'State','Anzoátegui','VE-B',''),(22977,244,'State','Apure','VE-C',''),(22978,244,'State','Aragua','VE-D',''),(22979,244,'State','Barinas','VE-E',''),(22980,244,'State','Bolívar','VE-F',''),(22981,244,'State','Carabobo','VE-G',''),(22982,244,'State','Cojedes','VE-H',''),(22983,244,'State','Delta Amacuro','VE-Y',''),(22984,244,'State','Falcón','VE-I',''),(22985,244,'State','Guárico','VE-J',''),(22986,244,'State','Lara','VE-K',''),(22987,244,'State','Mérida','VE-L',''),(22988,244,'State','Miranda','VE-M',''),(22989,244,'State','Monagas','VE-N',''),(22990,244,'State','Nueva Esparta','VE-O',''),(22991,244,'State','Portuguesa','VE-P',''),(22992,244,'State','Sucre','VE-R',''),(22993,244,'State','Táchira','VE-S',''),(22994,244,'State','Trujillo','VE-T',''),(22995,244,'State','Vargas','VE-X',''),(22996,244,'State','Yaracuy','VE-U',''),(22997,244,'State','Zulia','VE-V',''),(22998,245,'Province','An Giang','VN-44',''),(22999,245,'Province','Bà R?a - V?ng Tàu','VN-43',''),(23000,245,'Province','B?c K?n','VN-53',''),(23001,245,'Province','B?c Giang','VN-54',''),(23002,245,'Province','B?c Liêu','VN-55',''),(23003,245,'Province','B?c Ninh','VN-56',''),(23004,245,'Province','B?n Tre','VN-50',''),(23005,245,'Province','Bình ??nh','VN-31',''),(23006,245,'Province','Bình D??ng','VN-57',''),(23007,245,'Province','Bình Ph??c','VN-58',''),(23008,245,'Province','Bình Thu?n','VN-40',''),(23009,245,'Province','Cà Mau','VN-59',''),(23010,245,'Province','C?n Th?','VN-48',''),(23011,245,'Province','Cao B?ng','VN-04',''),(23012,245,'Province','?à N?ng, thành ph?','VN-60',''),(23013,245,'Province','??c L?k','VN-33',''),(23014,245,'Province','??k Nông','VN-72',''),(23015,245,'Province','?i?n Biên','VN-71',''),(23016,245,'Province','??ng Nai','VN-39',''),(23017,245,'Province','??ng Tháp','VN-45',''),(23018,245,'Province','Gia Lai','VN-30',''),(23019,245,'Province','Hà Giang','VN-03',''),(23020,245,'Province','Hà Nam','VN-63',''),(23021,245,'Province','Hà N?i, th? ?ô','VN-64',''),(23022,245,'Province','Hà Tây','VN-15',''),(23023,245,'Province','Hà T?nh','VN-23',''),(23024,245,'Province','H?i Duong','VN-61',''),(23025,245,'Province','H?i Phòng, thành ph?','VN-62',''),(23026,245,'Province','H?u Giang','VN-73',''),(23027,245,'Province','Hoà Bình','VN-14',''),(23028,245,'Province','H? Chí Minh, thành ph? [Sài Gòn]','VN-65',''),(23029,245,'Province','H?ng Yên','VN-66',''),(23030,245,'Province','Khánh Hòa','VN-34',''),(23031,245,'Province','Kiên Giang','VN-47',''),(23032,245,'Province','Kon Tum','VN-28',''),(23033,245,'Province','Lai Châu','VN-01',''),(23034,245,'Province','Lâm ??ng','VN-35',''),(23035,245,'Province','L?ng S?n','VN-09',''),(23036,245,'Province','Lào Cai','VN-02',''),(23037,245,'Province','Long An','VN-41',''),(23038,245,'Province','Nam ??nh','VN-67',''),(23039,245,'Province','Ngh? An','VN-22',''),(23040,245,'Province','Ninh Bình','VN-18',''),(23041,245,'Province','Ninh Thu?n','VN-36',''),(23042,245,'Province','Phú Th?','VN-68',''),(23043,245,'Province','Phú Yên','VN-32',''),(23044,245,'Province','Qu?ng Bình','VN-24',''),(23045,245,'Province','Qu?ng Nam','VN-27',''),(23046,245,'Province','Qu?ng Ngãi','VN-29',''),(23047,245,'Province','Qu?ng Ninh','VN-13',''),(23048,245,'Province','Qu?ng Tr?','VN-25',''),(23049,245,'Province','Sóc Tr?ng','VN-52',''),(23050,245,'Province','S?n La','VN-05',''),(23051,245,'Province','Tây Ninh','VN-37',''),(23052,245,'Province','Thái Bình','VN-20',''),(23053,245,'Province','Thái Nguyên','VN-69',''),(23054,245,'Province','Thanh Hóa','VN-21',''),(23055,245,'Province','Th?a Thiên-Hu?','VN-26',''),(23056,245,'Province','Ti?n Giang','VN-46',''),(23057,245,'Province','Trà Vinh','VN-51',''),(23058,245,'Province','Tuyên Quang','VN-07',''),(23059,245,'Province','V?nh Long','VN-49',''),(23060,245,'Province','V?nh Phúc','VN-70',''),(23061,245,'Province','Yên Bái','VN-06',''),(23062,242,'Province','Malampa','VU-MAP',''),(23063,242,'Province','Pénama','VU-PAM',''),(23064,242,'Province','Sanma','VU-SAM',''),(23065,242,'Province','Shéfa','VU-SEE',''),(23066,242,'Province','Taféa','VU-TAE',''),(23067,242,'Province','Torba','VU-TOB',''),(23068,195,'District','A\'ana','WS-AA',''),(23069,195,'District','Aiga-i-le-Tai','WS-AL',''),(23070,195,'District','Atua','WS-AT',''),(23071,195,'District','Fa\'asaleleaga','WS-FA',''),(23072,195,'District','Gaga\'emauga','WS-GE',''),(23073,195,'District','Gagaifomauga','WS-GI',''),(23074,195,'District','Palauli','WS-PA',''),(23075,195,'District','Satupa\'itea','WS-SA',''),(23076,195,'District','Tuamasaga','WS-TU',''),(23077,195,'District','Va\'a-o-Fonoti','WS-VF',''),(23078,195,'District','Vaisigano','WS-VS',''),(23079,250,'Governorate','Aby?n','YE-AB',''),(23080,250,'Governorate','\'Adan','YE-AD',''),(23081,250,'Governorate','A? ??li\'','YE-DA',''),(23082,250,'Governorate','Al Bay??\'','YE-BA',''),(23083,250,'Governorate','Al ?udaydah','YE-MU',''),(23084,250,'Governorate','Al Jawf','YE-JA',''),(23085,250,'Governorate','Al Mahrah','YE-MR',''),(23086,250,'Governorate','Al Ma?w?t','YE-MW',''),(23087,250,'Governorate','\'Amr?n','YE-AM',''),(23088,250,'Governorate','Dham?r','YE-DH',''),(23089,250,'Governorate','?a?ramawt','YE-HD',''),(23090,250,'Governorate','?ajjah','YE-HJ',''),(23091,250,'Governorate','Ibb','YE-IB',''),(23092,250,'Governorate','La?ij','YE-LA',''),(23093,250,'Governorate','Ma\'rib','YE-MA',''),(23094,250,'Governorate','?a\'dah','YE-SD',''),(23095,250,'Governorate','?an\'?\'','YE-SN',''),(23096,250,'Governorate','Shabwah','YE-SH',''),(23097,250,'Governorate','T?\'izz','YE-TA',''),(23098,209,'Province','Eastern Cape','ZA-EC',''),(23099,209,'Province','Free State','ZA-FS',''),(23100,209,'Province','Gauteng','ZA-GT',''),(23101,209,'Province','Kwazulu-Natal','ZA-NL',''),(23102,209,'Province','Limpopo','ZA-LP',''),(23103,209,'Province','Mpumalanga','ZA-MP',''),(23104,209,'Province','Northern Cape','ZA-NC',''),(23105,209,'Province','North-West (South Africa)','ZA-NW',''),(23106,209,'Province','Western Cape','ZA-WC',''),(23107,251,'Province','Central','ZM-02',''),(23108,251,'Province','Copperbelt','ZM-08',''),(23109,251,'Province','Eastern','ZM-03',''),(23110,251,'Province','Luapula','ZM-04',''),(23111,251,'Province','Lusaka','ZM-09',''),(23112,251,'Province','Northern','ZM-05',''),(23113,251,'Province','North-Western','ZM-06',''),(23114,251,'Province','Southern (Zambia)','ZM-07',''),(23115,251,'Province','Western','ZM-01',''),(23116,252,'City','Bulawayo','ZW-BU',''),(23117,252,'City','Harare','ZW-HA',''),(23118,252,'Province','Manicaland','ZW-MA',''),(23119,252,'Province','Mashonaland Central','ZW-MC',''),(23120,252,'Province','Mashonaland East','ZW-ME',''),(23121,252,'Province','Mashonaland West','ZW-MW',''),(23122,252,'Province','Masvingo','ZW-MV',''),(23123,252,'Province','Matabeleland North','ZW-MN',''),(23124,252,'Province','Matabeleland South','ZW-MS',''),(23125,252,'Province','Midlands','ZW-MI','');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
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
INSERT INTO `phone_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0,'Unknown','Status not known, this will be the default if no status provided'),(1,'Current',NULL),(2,'Current Alternative',NULL),(3,'Current Under Investigation',NULL),(4,'Valid Past',NULL),(5,'Incorrect or Disconnected',NULL);
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
-- Dumping data for table `upload_type`
--

LOCK TABLES `upload_type` WRITE;
/*!40000 ALTER TABLE `upload_type` DISABLE KEYS */;
INSERT INTO `upload_type` (`ID`, `NAME`, `DESCRIPTION`, `ARK_MODULE_ID`) VALUES (0,'Biospecimen Custom Data','Custom Data to be associated with a biospecimen',5),(1,'Subject Demographic Data',NULL,2),(2,'Study-specific (custom) Data',NULL,2),(3,'Custom Data Sets','Custom Data Sets formerly known as Phenotypic Custom Data',3),(4,'Biocollection Custom Data','Custom Data to be associated with a biospecimen',5);
/*!40000 ALTER TABLE `upload_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `upload_status`
--

LOCK TABLES `upload_status` WRITE;
/*!40000 ALTER TABLE `upload_status` DISABLE KEYS */;
INSERT INTO `upload_status` (`ID`, `NAME`, `SHORT_MESSAGE`, `LONG_MESSAGE`) VALUES (-3,'ERROR_ON_DATA_IMPORT','Error while importing data','While the file passed validation, an error occured during the import of data.  Please contact your system administrator.'),(-2,'ERROR_IN_DATA_VALIDATION','Error while validating data','Error while validating data, prior to uploading'),(-1,'ERROR_IN_FILE_VALIDATION','Error validation file','Error in file format or header values.'),(0,'STATUS_NOT_DEFINED','Status not defined','Status not defined.  This may predate our adding status to uploads'),(1,'AWAITING_VALIDATION','Awaiting Validation','Successfully uploaded to our server, awaiting validation and upload to fields'),(2,'VALIDATED','Successfully validated','Successfully validated.  Awaiting upload into fields'),(3,'COMPLETED','Successfully completed','Successfully completed upload');
/*!40000 ALTER TABLE `upload_status` ENABLE KEYS */;
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
INSERT INTO `consent_option` (`ID`, `NAME`) VALUES (1,'Yes'),(2,'No'),(3,'Pending'),(4,'Unavailable'),(5,'Limited'),(6,'Revoked');
/*!40000 ALTER TABLE `consent_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `measurement_type`
--

LOCK TABLES `measurement_type` WRITE;
/*!40000 ALTER TABLE `measurement_type` DISABLE KEYS */;
INSERT INTO `measurement_type` (`ID`, `VALUE`, `DESCRIPTION`) VALUES (1,'Distance',NULL),(2,'Volume',NULL),(3,'Time',NULL),(4,'Weight',NULL),(5,'Weight per Volume',NULL),(6,'Distance per Time',NULL),(7,'Percentage or Fraction',NULL),(999,'Other',NULL);
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

-- Dump completed on 2012-08-23 17:09:17

USE pheno;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: pheno
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
-- Dumping data for table `questionnaire_status`
--

LOCK TABLES `questionnaire_status` WRITE;
/*!40000 ALTER TABLE `questionnaire_status` DISABLE KEYS */;
INSERT INTO `questionnaire_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'In Progress','The Questionnaire is being provided with data and not yet completed.'),(2,'Data Entry Completed','Questionnaire data entry is completed and awaiting review.'),(3,'Review Ok','The Questionnaire data was reviewed successfully and questionnaire is now locked from further modification.'),(4,'Review Failed','The Questionnaire data failed review and is needs to be revisited for data correction.'),(5,'Uploaded From File','The Questionnaire data has been update from file, with no further action taken since then.');
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

-- Dump completed on 2012-08-23 17:09:17

USE geno;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: geno
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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

-- Dump completed on 2012-08-23 17:09:17

USE lims;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: lims
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
INSERT INTO `bio_sampletype` (`ID`, `NAME`, `SAMPLETYPE`, `SAMPLESUBTYPE`) VALUES (0,'Unknown','Unknown',NULL),(1,'Blood / Blood','Blood','Blood'),(2,'Blood / Buffy Coat','Blood','Buffy Coat'),(3,'Blood / Buffy Coat (ACD)','Blood','Buffy Coat (ACD)'),(4,'Blood / Buffy Coat (EDTA)','Blood','Buffy Coat (EDTA)'),(5,'Blood / Buffy Coat (LH)','Blood','Buffy Coat (LH)'),(6,'Blood / Cord blood','Blood','Cord blood'),(7,'Blood / EDTA Blood','Blood','EDTA Blood'),(8,'Blood / Frozen Lymphocytes (F)','Blood','Frozen Lymphocytes (F)'),(9,'Blood / Lithium Heparin','Blood','Lithium Heparin'),(10,'Blood / Mothers Blood','Blood','Mothers Blood'),(11,'Blood / Plasma','Blood','Plasma'),(12,'Blood / Plasma (ACD)','Blood','Plasma (ACD)'),(13,'Blood / Plasma (EDTA)','Blood','Plasma (EDTA)'),(14,'Blood / Plasma (LH)','Blood','Plasma (LH)'),(15,'Blood / Protein from TL','Blood','Protein from TL'),(16,'Blood / Red Blood Cells','Blood','Red Blood Cells'),(17,'Blood / Serum','Blood','Serum'),(18,'Blood / Transformed lymphoblasts (T)','Blood','Transformed lymphoblasts (T)'),(19,'Blood / Unprocessed','Blood','Unprocessed'),(20,'Blood / Whole Blood','Blood','Whole Blood'),(21,'Blood / Whole Blood (EDTA)','Blood','Whole Blood (EDTA)'),(22,'Blood / Whole Blood (LH)','Blood','Whole Blood (LH)'),(23,'Nucleic Acid / Buccal Swab','Nucleic Acid','Buccal Swab'),(24,'Nucleic Acid / DNA','Nucleic Acid','DNA'),(25,'Nucleic Acid / DNA from BC','Nucleic Acid','DNA from BC'),(26,'Nucleic Acid / DNA from TL','Nucleic Acid','DNA from TL'),(27,'Nucleic Acid / DNA from Tissue','Nucleic Acid','DNA from Tissue'),(28,'Nucleic Acid / Paxgene RNA','Nucleic Acid','Paxgene RNA'),(29,'Nucleic Acid / RNA','Nucleic Acid','RNA'),(30,'Nucleic Acid / Saliva','Nucleic Acid','Saliva'),(31,'Protein from TL','Protein from TL',NULL),(32,'Saliva / Buccal Swab','Saliva','Buccal Swab'),(33,'Saliva / Buccal Swab (SB)','Saliva','Buccal Swab (SB)'),(34,'Saliva / Oragene (OS)','Saliva','Oragene (OS)'),(35,'Saliva / Throat Swab','Saliva','Throat Swab'),(36,'Saliva','Saliva',NULL),(37,'Tissue / Anus','Tissue','Anus'),(38,'Tissue / Appendix','Tissue','Appendix'),(39,'Tissue / Brain','Tissue','Brain'),(40,'Tissue / Breast','Tissue','Breast'),(41,'Tissue / Breast,Lt','Tissue','Breast,Lt'),(42,'Tissue / Breast,Rt','Tissue','Breast,Rt'),(43,'Tissue / Caecum','Tissue','Caecum'),(44,'Tissue / Colon','Tissue','Colon'),(45,'Tissue / Colon, ascending','Tissue','Colon, ascending'),(46,'Tissue / Colon, descending','Tissue','Colon, descending'),(47,'Tissue / Colon, hepatic flexure','Tissue','Colon, hepatic flexure'),(48,'Tissue / Colon, nos','Tissue','Colon, nos'),(49,'Tissue / Colon, sigmoid','Tissue','Colon, sigmoid'),(50,'Tissue / Colon, spenic flexure','Tissue','Colon, spenic flexure'),(51,'Tissue / Colon, splenic flexure','Tissue','Colon, splenic flexure'),(52,'Tissue / Colon, transverse','Tissue','Colon, transverse'),(53,'Tissue / Descending Colon','Tissue','Descending Colon'),(54,'Tissue / Duodenum','Tissue','Duodenum'),(55,'Tissue / Endometrium','Tissue','Endometrium'),(56,'Tissue / Ileum','Tissue','Ileum'),(57,'Tissue / Left Tube','Tissue','Left Tube'),(58,'Tissue / Liver','Tissue','Liver'),(59,'Tissue / Lung','Tissue','Lung'),(60,'Tissue / Lymph Node','Tissue','Lymph Node'),(61,'Tissue / Mesentary','Tissue','Mesentary'),(62,'Tissue / Oesophagus','Tissue','Oesophagus'),(63,'Tissue / Omentum','Tissue','Omentum'),(64,'Tissue / Ovarian Cyst L','Tissue','Ovarian Cyst L'),(65,'Tissue / Ovarian L','Tissue','Ovarian L'),(66,'Tissue / Ovarian R','Tissue','Ovarian R'),(67,'Tissue / Ovary','Tissue','Ovary'),(68,'Tissue / Pancreas','Tissue','Pancreas'),(69,'Tissue / Peritoneum, pelvic','Tissue','Peritoneum, pelvic'),(70,'Tissue / Placenta','Tissue','Placenta'),(71,'Tissue / Rectal Peritoneal Mass','Tissue','Rectal Peritoneal Mass'),(72,'Tissue / Recto-sigmoid','Tissue','Recto-sigmoid'),(73,'Tissue / Rectum','Tissue','Rectum'),(74,'Tissue / Right Tube','Tissue','Right Tube'),(75,'Tissue / Small Bowel','Tissue','Small Bowel'),(76,'Tissue / Spleen','Tissue','Spleen'),(77,'Tissue / Stomach','Tissue','Stomach'),(78,'Tissue / Tissue','Tissue','Tissue'),(79,'Tissue / Uterus','Tissue','Uterus'),(80,'Tissue / Uterus, endometrium','Tissue','Uterus, endometrium'),(81,'Urine / Urine','Urine','Urine'),(82,'Urine','Urine',NULL),(83,'Blood / Peripheral Blood Ficolled','Blood','Peripheral Blood Ficolled'),(84,'Tissue / Bone Marrow Slide','Tissue','Bone Marrow Slide'),(85,'Tissue / Bone Marrow','Tissue','Bone Marrow'),(86,'Tissue / Bone Marrow Ficolled','Tissue','Bone Marrow Ficolled'),(87,'Tissue / Bone Marrow Buffy Coat','Tissue','Bone Marrow Buffy Coat'),(88,'Tissue / Bone Marrow or Peripheral Blood ','Tissue','Bone Marrow or Peripheral Blood '),(89,'Blood / Peripheral blood','Blood','Peripheral blood'),(90,'Tissue / Bone Marrow or Peripheral Bloo','Tissue','Bone Marrow or Peripheral Bloo'),(91,'Stem Cell Harvest','Stem Cell Harvest','Stem Cell Harvest'),(92,'Central Spinal Fluid (CSF)','Central Spinal Fluid (CSF)','Central Spinal Fluid (CSF)'),(93,'Tissue / Bone Marrow Stored Whole','Tissue','Bone Marrow Stored Whole'),(94,'Blood / Peripheral Blood Buffy Coat','Blood','Peripheral Blood Buffy Coat'),(95,'Blood / Cord Blood Ficolled','Blood','Cord Blood Ficolled'),(96,'Blood / Peripheral Blood Stored Whole','Blood','Peripheral Blood Stored Whole'),(97,'Tumour Tissue','Tumour Tissue','Tumour Tissue'),(98,'Tissue / Bone Marrow Lysed','Tissue','Bone Marrow Lysed'),(99,'Tissue / Bone Marrow DNA','Tissue','Bone Marrow DNA'),(100,'Blood / Peripheral Blood Lysed','Blood','Peripheral Blood Lysed'),(101,'Unknown','Unknown','Unknown'),(102,'Buccal Swab','Buccal Swab','Buccal Swab'),(103,'RNA','RNA','RNA'),(104,'Tissue / Bone Marrow Trephine','Tissue','Bone Marrow Trephine'),(105,'Tissue','Tissue','Tissue'),(106,'Skin','Skin','Skin'),(107,'Serum','Serum','Serum'),(108,'Pericardial Fluid','Pericardial Fluid','Pericardial Fluid'),(109,'Slide','Slide','Slide'),(110,'Blood / Peripheral Blood Slide','Blood','Peripheral Blood Slide'),(111,'Cytospin','Cytospin','Cytospin'),(112,'Nucleic Acid / Granulocyte DNA','Nucleic Acid','Granulocyte DNA'),(113,'Plasma','Plasma','Plasma'),(114,'Nucleic Acid / Neutrophil DNA','Nucleic Acid','Neutrophil DNA'),(115,'Nucleic Acid / G-DNA','Nucleic Acid','G-DNA'),(116,'Tissue / Bone Marrow RNA','Tissue','Bone Marrow RNA'),(117,'Nucleic Acid / Tumour Tissue cDNA','Nucleic Acid','Tumour Tissue cDNA'),(118,'Nucleic Acid / cDNA','Nucleic Acid','cDNA'),(119,'Tumour Tissue RNA','Tumour Tissue RNA','Tumour Tissue RNA'),(120,'Blood / Peripheral Blood DNA','Blood','Peripheral Blood DNA'),(121,'Nucleic Acid / Tumour Tissue DNA','Nucleic Acid','Tumour Tissue DNA'),(122,'Pleural fluid','Pleural fluid','Pleural fluid');
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
INSERT INTO `barcode_label` (`ID`, `STUDY_ID`, `BARCODE_PRINTER_ID`, `NAME`, `DESCRIPTION`, `LABEL_PREFIX`, `LABEL_SUFFIX`, `VERSION`, `BARCODE_PRINTER_NAME`) VALUES (1,NULL,1,'zebra biospecimen','Generic Zebra Biospecimen Label','D14\nN','P1',1,NULL),(2,NULL,1,'zebra biocollection','Generic Zebra BioCollection label','D15\nN','P1',1,NULL),(3,NULL,2,'straw barcode','Generic Brady Straw Biospecimen label','DIRECTION 0\nREFERENCE 0,0\nCLS','PRINT 1,1',1,NULL),(4,NULL,1,'zebra biospecimen','Generic Zebra Biospecimen Label v2','D14\nN','P1',2,NULL),(5,NULL,1,'straw barcode','Generic Zebra Straw Label','D14\nN','P1',3,NULL),(23,68,53,'zebra biospecimen',NULL,'D14\nN','P1',1,'Zebra'),(30,68,53,'zebra biocollection',NULL,'D15\nN','P1',1,'Zebra'),(32,2,1,'zebra biocollection',NULL,'D15\nN','P1',1,NULL),(34,2,1,'zebra biospecimen',NULL,'D14\nN','P1',1,NULL),(35,1,1,'zebra biospecimen','v2','D14\nN','P1',2,'Zebra'),(38,32,17,'zebra biospecimen',NULL,'D14\nN','P1',2,NULL),(40,1,2,'straw barcode',NULL,'D14\nN','P1',3,'brady_bbp_11'),(41,1,1,'zebra biospecimen',NULL,'D14\nN','P1',1,'Zebra'),(42,24,13,'zebra biospecimen',NULL,'D14\nN','P1',1,NULL);
/*!40000 ALTER TABLE `barcode_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `barcode_label_data`
--

LOCK TABLES `barcode_label_data` WRITE;
/*!40000 ALTER TABLE `barcode_label_data` DISABLE KEYS */;
INSERT INTO `barcode_label_data` (`ID`, `BARCODE_LABEL_ID`, `COMMAND`, `X_COORD`, `Y_COORD`, `P1`, `P2`, `P3`, `P4`, `P5`, `P6`, `P7`, `P8`, `QUOTE_LEFT`, `DATA`, `QUOTE_RIGHT`, `LINE_FEED`) VALUES (42,1,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(43,1,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(44,1,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(45,1,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(46,1,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(47,1,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(48,2,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ID: {$subjectUid} Family ID: ${familyId}','\"','\n'),(49,2,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ASRB No: ${asrbno}','\"','\n'),(50,2,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Collection Date: ${collectionDate}','\"','\n'),(51,2,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Researcher: ${refDoctor}','\"','\n'),(52,2,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','DOB: ${dateOfBirth} Sex: {$sex}','\"','\n'),(53,3,'BARCODE',25,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(54,3,'TEXT',25,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(55,3,'TEXT',25,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(56,3,'BARCODE',250,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(57,3,'TEXT',250,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(58,3,'TEXT',250,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(66,23,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(68,23,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(70,23,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(71,23,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(77,30,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ID: {$subjectUid} Family ID: ${familyId}','\"','\n'),(78,30,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ASRB No: ${asrbno}','\"','\n'),(79,30,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Collection Date: ${collectionDate}','\"','\n'),(80,30,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Researcher: ${refDoctor}','\"','\n'),(81,30,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','DOB: ${dateOfBirth} Sex: {$sex}','\"','\n'),(88,32,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ID: {$subjectUid} Family ID: ${familyId}','\"','\n'),(89,32,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ASRB No: ${asrbno}','\"','\n'),(90,32,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Collection Date: ${collectionDate}','\"','\n'),(91,32,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Researcher: ${refDoctor}','\"','\n'),(92,32,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','DOB: ${dateOfBirth} Sex: {$sex}','\"','\n'),(93,4,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(94,4,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(95,4,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(96,4,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n'),(97,34,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(98,34,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(99,34,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(100,34,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n'),(101,35,'b',193,8,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(102,35,'b',100,8,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(103,35,'A',250,10,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(104,35,'A',250,30,'0','1','1','1','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n'),(111,5,'B',242,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(112,5,'B',-80,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(113,5,'A',250,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(114,5,'A',-80,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(119,38,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(120,38,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(121,38,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(122,38,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n'),(127,40,'B',242,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(128,40,'B',-80,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(129,40,'A',250,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(130,40,'A',-80,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(131,35,'A',250,50,'0','1','1','1','N',NULL,NULL,NULL,'\"','${sampleType}','\"',NULL),(132,41,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(133,41,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(134,41,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(135,41,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(136,41,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(137,41,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(138,42,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(139,42,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(140,42,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(141,42,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(142,42,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(143,42,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n');
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

-- Dump completed on 2012-08-23 17:09:17

USE reporting;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: reporting
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
INSERT INTO `report_template` (`ID`, `NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) VALUES (1,'Study Summary Report','This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>','StudySummaryReport.jrxml',1,23),(2,'Study-level Consent Details Report','This report lists detailed subject information for a particular study based on their consent status at the study-level.','ConsentDetailsReport.jrxml',2,24),(3,'Study Component Consent Details Report','This report lists detailed subject information for a particular study based on their consent status for a specific study component.','ConsentDetailsReport.jrxml',2,25),(4,'Phenotypic Field Details Report (Data Dictionary)','This report lists detailed field information for a particular study based on their associated phenotypic collection.','DataDictionaryReport.jrxml',3,26),(5,'Study User Role Permissions Report','This report lists all user role and permissions for the study in context.','StudyUserRolePermissions.jrxml',1,33),(6,'Work Researcher Cost Report','This report lists the total billable item type costs realated to a researcher','ResearcherCostReport.jrxml',8,57),(7,'Work Researcher Detail Cost Report','This report lists the individual billable item costs group by the billable item type realated to a researcher','ResearcherDetailCostReport.jrxml',8,57),(8,'Work Study Detail Cost Report','This report lists the individual billable item costs group by the billable item type related to context study','StudyDetailCostReport.jrxml',8,57);
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

-- Dump completed on 2012-08-23 17:09:17

USE admin;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: ark-database    Database: admin
-- ------------------------------------------------------
-- Server version	5.1.63-0ubuntu0.10.04.1

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
-- Dumping data for table `billable_item_type_status`
--

LOCK TABLES `billable_item_type_status` WRITE;
/*!40000 ALTER TABLE `billable_item_type_status` DISABLE KEYS */;
INSERT INTO `billable_item_type_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'ACTIVE','Active record'),(2,'INACTIVE','Inactive record');
/*!40000 ALTER TABLE `billable_item_type_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `billing_type`
--

LOCK TABLES `billing_type` WRITE;
/*!40000 ALTER TABLE `billing_type` DISABLE KEYS */;
INSERT INTO `billing_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'EFT',NULL),(2,'CHEQUE',NULL),(3,'CASH',NULL);
/*!40000 ALTER TABLE `billing_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `researcher_role`
--

LOCK TABLES `researcher_role` WRITE;
/*!40000 ALTER TABLE `researcher_role` DISABLE KEYS */;
INSERT INTO `researcher_role` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Chief Investigator',NULL),(2,'Assoc Investigator',NULL),(3,'Other Investigator',NULL),(4,'Research Assistant',NULL);
/*!40000 ALTER TABLE `researcher_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `researcher_status`
--

LOCK TABLES `researcher_status` WRITE;
/*!40000 ALTER TABLE `researcher_status` DISABLE KEYS */;
INSERT INTO `researcher_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Active',NULL),(2,'Inactive','');
/*!40000 ALTER TABLE `researcher_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `work_request_status`
--

LOCK TABLES `work_request_status` WRITE;
/*!40000 ALTER TABLE `work_request_status` DISABLE KEYS */;
INSERT INTO `work_request_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Not Commenced',''),(2,'Commenced',''),(3,'Completed','');
/*!40000 ALTER TABLE `work_request_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-23 17:09:17
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
