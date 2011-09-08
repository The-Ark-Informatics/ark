-- MySQL dump 10.13  Distrib 5.1.41, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: study
-- ------------------------------------------------------
-- Server version	5.1.41-3ubuntu12.10

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
INSERT INTO `action_type` VALUES (1,'CREATED',NULL),(2,'UPDATED',NULL),(3,'DELETED',NULL),(4,'ARCHIVED',NULL);
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
INSERT INTO `address_status` VALUES (1,'Current'),(2,'Current - Alternative'),(3,'Current - Under Investigation'),(5,'Incorrect address'),(4,'Valid past address');
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
INSERT INTO `address_type` VALUES (1,'HOME',NULL),(2,'WORK',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_function`
--

LOCK TABLES `ark_function` WRITE;
/*!40000 ALTER TABLE `ark_function` DISABLE KEYS */;
INSERT INTO `ark_function` VALUES (1,'STUDY','Study Management  usecase. This is represented via the Study Detail Tab under the main Study  Tab. ',1,'tab.module.study.details'),(2,'STUDY_COMPONENT','Study Component usecase.This is represented via the StudyComponent Tab under the main Study  Tab. ',1,'tab.module.study.components'),(3,'MY_DETAIL','Edit My details usecase, this is represented via My Detail tab.',1,'tab.module.mydetails'),(4,'USER','User management usecase. This is represented via the User Tab under the main Study  Tab.',1,'tab.module.user.management'),(5,'SUBJECT','Subject management usecase. This is represented via the Subject Tab under the main Study  Tab.',1,'tab.module.subject.detail'),(6,'PHONE','Manage phone usecase. This is represented via the Phone tab under the main Study  Tab.',1,'tab.module.person.phone'),(7,'ADDRESS','Manage Address',1,'tab.module.person.address'),(8,'ATTACHMENT','Manage Consent and Component attachments. This is represented via the Attachment tab under Subject Main tab.',1,'tab.module.subject.subjectFile'),(9,'CONSENT','Manage Subject Consents. This is represented via the Consent tab under the main Study  Tab.',1,'tab.module.subject.consent'),(10,'SUBJECT_UPLOAD','Bulk upload of Subjects.',1,'tab.module.subject.subjectUpload'),(11,'SUBJECT_CUSTOM_FIELD','Manage Custom Fields for Subjects.',1,'tab.module.subject.subjectcustomfield'),(12,'DATA_DICTIONARY','Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.field'),(13,'DATA_DICTIONARY_UPLOAD','Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldUpload'),(14,'PHENO_COLLECTION','Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.collection'),(15,'FIELD_DATA','Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldData'),(16,'FIELD_DATA_UPLOAD','Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.phenoUpload'),(17,'LIMS_SUBJECT','LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.',1,'tab.module.lims.subject.detail'),(18,'LIMS_COLLECTION','LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.',1,'tab.module.lims.collection'),(19,'BIOSPECIMEN','LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.',1,'tab.module.lims.biospecimen'),(20,'INVENTORY','LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.',1,'tab.module.lims.inventory'),(21,'CORRESPONDENCE','',1,'tab.module.subject.correspondence'),(22,'SUMMARY','Phenotypic Summary.',1,'tab.module.phenotypic.summary'),(23,'REPORT_STUDYSUMARY','Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>',2,NULL),(24,'REPORT_STUDYLEVELCONSENT','Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.',2,NULL),(25,'REPORT_STUDYCOMPCONSENT','Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.',2,NULL),(26,'REPORT_PHENOFIELDDETAILS','Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic collection.',2,NULL),(27,'GENO_COLLECTION','Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu',1,'tab.module.geno.collection'),(28,'ROLE_POLICY_TEMPLATE','Allows CRUD operations on the ark_role_policy_template table for the Ark application',1,'tab.module.admin.rolePolicyTemplate'),(29,'MODULE','Allows CRUD operations on the ark_module table for the Ark application',1,'tab.module.admin.module'),(30,'FUNCTION','Allows CRUD operations on the ark_function table for the Ark application',1,'tab.module.admin.function'),(33,'REPORT_STUDY_USER_ROLE_PERMISSIONS','Study User Role Permissions Report lists all user role and permissions for the study in context.',2,NULL),(34,'SUBJECT_CUSTOM_DATA','Data entry for Subject Custom Fields.',1,'tab.module.subject.subjectcustomdata'),(35,'LIMS_COLLECTION_CUSTOM_FIELD','Manage Custom Fields for LIMS collections.',1,'tab.module.lims.collectioncustomfield'),(36,'LIMS_COLLECTION_CUSTOM_DATA','Data entry for LIMS collection Custom Fields.',1,'tab.module.lims.collectioncustomdata'),(37,'BIOSPECIMEN_CUSTOM_FIELD','Manage Custom Fields for Biospecimens.',1,'tab.module.lims.biospecimencustomfield'),(38,'BIOSPECIMEN_CUSTOM_DATA','Data entry for Biospecimen Custom Fields.',1,'tab.module.lims.biospecimencustomdata');
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
INSERT INTO `ark_function_type` VALUES (1,'NON-REPORT','A function that is not a report.'),(2,'REPORT',' A function that is a report.');
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
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  UNIQUE KEY `ARK_MODULE_NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module`
--

LOCK TABLES `ark_module` WRITE;
/*!40000 ALTER TABLE `ark_module` DISABLE KEYS */;
INSERT INTO `ark_module` VALUES (1,'Study',NULL),(2,'Subject',NULL),(3,'Phenotypic',NULL),(4,'Genotypic',NULL),(5,'LIMS',NULL),(6,'Reporting',NULL),(7,'Admin',NULL);
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
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module_function`
--

LOCK TABLES `ark_module_function` WRITE;
/*!40000 ALTER TABLE `ark_module_function` DISABLE KEYS */;
INSERT INTO `ark_module_function` VALUES (1,1,1,1),(2,1,2,2),(3,1,3,4),(4,1,4,3),(5,2,5,1),(6,2,6,3),(7,2,7,4),(8,2,8,5),(9,2,9,6),(10,2,10,8),(11,2,11,9),(12,2,21,7),(13,3,12,2),(14,3,13,3),(15,3,14,4),(16,3,15,5),(17,3,16,6),(19,5,18,2),(20,5,19,2),(21,5,20,3),(22,3,22,NULL),(23,1,23,NULL),(24,2,24,NULL),(25,2,25,NULL),(26,3,26,NULL),(27,4,27,1),(28,7,28,3),(29,7,29,1),(30,7,30,2),(31,2,34,2),(34,5,35,6),(35,5,37,7);
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
  CONSTRAINT `fk_ark_module_role_1` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ark_module_role_2` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module_role`
--

LOCK TABLES `ark_module_role` WRITE;
/*!40000 ALTER TABLE `ark_module_role` DISABLE KEYS */;
INSERT INTO `ark_module_role` VALUES (1,1,2),(2,1,3),(3,2,4),(4,2,5),(5,2,6),(6,3,7),(7,3,8),(8,5,9),(9,5,10),(10,4,11),(11,5,12),(12,3,13);
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
INSERT INTO `ark_permission` VALUES (1,'CREATE',NULL),(2,'READ',NULL),(3,'UPDATE',NULL),(4,'DELETE',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_role`
--

LOCK TABLES `ark_role` WRITE;
/*!40000 ALTER TABLE `ark_role` DISABLE KEYS */;
INSERT INTO `ark_role` VALUES (1,'Super Administrator',NULL),(2,'Study Administrator',NULL),(3,'Study Read-Only user',NULL),(4,'Subject Administrator',NULL),(5,'Subject Data Manager',NULL),(6,'Subject Read-Only user',NULL),(7,'Pheno Read-Only user',NULL),(8,'Pheno Data Manager',NULL),(9,'LIMS Read-Only user',NULL),(10,'LIMS Data Manager',NULL),(11,'Geno Read-Only User',NULL),(12,'LIMS Administrator',NULL),(13,'Pheno Administrator',NULL);
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
  CONSTRAINT `FK_ROLE_TMPLT_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_PRMSN_ID` FOREIGN KEY (`ARK_PERMISSION_ID`) REFERENCES `ark_permission` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_ROLE_ID` FOREIGN KEY (`ARK_ROLE_ID`) REFERENCES `ark_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_role_policy_template`
--

LOCK TABLES `ark_role_policy_template` WRITE;
/*!40000 ALTER TABLE `ark_role_policy_template` DISABLE KEYS */;
INSERT INTO `ark_role_policy_template` VALUES (1,1,NULL,NULL,1),(2,1,NULL,NULL,2),(3,1,NULL,NULL,3),(4,1,NULL,NULL,4),(5,2,1,1,2),(6,2,1,1,3),(7,2,1,2,1),(8,2,1,2,2),(9,2,1,2,3),(10,2,1,3,2),(11,2,1,3,3),(12,2,1,4,1),(13,2,1,4,2),(14,2,1,4,3),(15,3,1,1,2),(16,3,1,2,2),(18,3,1,3,2),(19,3,1,3,3),(20,4,2,5,1),(21,4,2,5,2),(22,4,2,5,3),(23,4,2,6,1),(24,4,2,6,2),(25,4,2,6,3),(26,4,2,6,4),(27,4,2,7,1),(28,4,2,7,2),(29,4,2,7,3),(30,4,2,7,4),(31,4,2,8,1),(32,4,2,8,2),(33,4,2,8,3),(34,4,2,8,4),(35,4,2,9,1),(36,4,2,9,2),(37,4,2,9,3),(38,4,2,9,4),(39,5,2,5,2),(40,5,2,6,2),(41,5,2,6,3),(42,5,2,7,2),(43,5,2,7,3),(44,5,2,8,2),(45,5,2,8,3),(46,5,2,9,2),(47,5,2,9,3),(48,4,2,10,1),(49,4,2,10,2),(50,4,2,10,3),(51,4,2,11,1),(52,4,2,11,2),(53,4,2,11,3),(54,5,2,5,1),(55,5,2,5,3),(56,5,2,6,1),(57,5,2,6,4),(58,5,2,7,1),(59,5,2,7,4),(60,5,2,8,1),(61,5,2,8,4),(62,5,2,9,1),(63,5,2,9,4),(64,6,2,5,2),(65,6,2,6,2),(66,6,2,7,2),(67,6,2,8,2),(68,6,2,9,2),(69,6,2,34,2),(70,8,3,12,2),(71,8,3,13,2),(73,8,3,15,2),(74,8,3,16,1),(75,10,5,17,3),(76,10,5,18,3),(77,10,5,19,3),(78,10,5,20,3),(79,9,5,17,2),(80,9,5,18,2),(81,9,5,19,2),(82,9,5,20,2),(83,7,3,12,2),(84,7,3,13,2),(85,7,3,14,2),(86,7,3,15,2),(87,7,3,16,2),(88,5,2,10,1),(89,8,3,22,2),(90,7,3,22,3),(91,10,5,17,4),(92,2,1,23,2),(93,3,1,23,2),(94,4,2,24,2),(95,5,2,24,2),(96,6,2,24,2),(97,4,2,25,2),(98,5,2,25,2),(99,6,2,25,2),(100,7,3,26,2),(101,8,3,26,2),(102,10,5,17,4),(103,10,5,19,4),(104,11,4,27,2),(106,8,3,14,2),(107,6,2,21,2),(108,5,2,21,1),(109,5,2,21,2),(110,5,2,21,3),(111,5,2,21,4),(112,4,2,21,1),(113,4,2,21,2),(114,4,2,21,3),(115,4,2,21,4),(116,12,5,17,1),(117,12,5,17,2),(118,12,5,17,3),(119,12,5,17,4),(120,12,5,18,1),(121,12,5,18,2),(122,12,5,18,3),(123,12,5,18,4),(124,12,5,19,1),(125,12,5,19,2),(126,12,5,19,3),(127,12,5,19,4),(128,12,5,20,1),(129,12,5,20,2),(130,12,5,20,3),(131,12,5,20,4),(132,13,3,12,1),(133,13,3,13,1),(134,13,3,14,1),(135,13,3,15,3),(136,13,3,16,1),(137,13,3,22,4),(138,13,3,26,2),(139,13,3,14,3),(140,13,3,14,2),(141,8,3,15,3),(184,4,2,34,1),(185,4,2,34,2),(186,4,2,34,3),(187,4,2,34,4),(188,5,2,34,1),(189,5,2,34,2),(190,5,2,34,3),(191,5,2,34,4),(192,12,5,35,1),(193,12,5,35,2),(194,12,5,35,3),(195,12,5,36,1),(196,12,5,36,2),(197,12,5,36,3),(198,12,5,36,4),(199,10,5,36,1),(200,10,5,36,2),(201,10,5,36,3),(202,10,5,36,4),(203,9,5,36,2),(204,12,5,37,1),(205,12,5,37,2),(206,12,5,37,3),(207,12,5,38,1),(208,12,5,38,2),(209,12,5,38,3),(210,12,5,38,4),(211,10,5,38,1),(212,10,5,38,2),(213,10,5,38,3),(214,10,5,38,4),(215,9,5,38,2);
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
INSERT INTO `ark_user` VALUES (1,'replace_with_ldapname');
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
  CONSTRAINT `FK_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
INSERT INTO `ark_user_role` VALUES (1,1,1,1,NULL),(2,1,1,2,NULL),(3,1,1,3,NULL),(4,1,1,4,NULL),(5,1,1,5,NULL),(6,1,1,6,NULL),(7,1,1,7,NULL);
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
-- Table structure for table `consent`
--

DROP TABLE IF EXISTS `consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `SUBJECT_ID` int(11) NOT NULL,
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
  KEY `fk_subject` (`SUBJECT_ID`) USING BTREE,
  KEY `fk_study_component` (`STUDY_COMP_ID`) USING BTREE,
  KEY `fk_study_comp_status` (`STUDY_COMP_STATUS_ID`) USING BTREE,
  KEY `fk_consent_status` (`CONSENT_STATUS_ID`) USING BTREE,
  KEY `fk_consent_type` (`CONSENT_TYPE_ID`) USING BTREE,
  KEY `fk_consent_downloaded` (`CONSENT_DOWNLOADED_ID`) USING BTREE,
  CONSTRAINT `fk_consent_downloaded` FOREIGN KEY (`CONSENT_DOWNLOADED_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_status` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_type` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_component` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_comp_status` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject` FOREIGN KEY (`SUBJECT_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
INSERT INTO `consent_answer` VALUES (1,'YES'),(2,'NO');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_status`
--

LOCK TABLES `consent_status` WRITE;
/*!40000 ALTER TABLE `consent_status` DISABLE KEYS */;
INSERT INTO `consent_status` VALUES (1,'Consented',NULL),(2,'Not Consented',NULL),(3,'Ineligible',NULL),(4,'Refused',NULL),(5,'Withdrawn',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_type`
--

LOCK TABLES `consent_type` WRITE;
/*!40000 ALTER TABLE `consent_type` DISABLE KEYS */;
INSERT INTO `consent_type` VALUES (1,'Hard Copy','Physical Paper based document.'),(2,'Electronic','A scanned equivalent of a hard copy that is available as a download via an application.');
/*!40000 ALTER TABLE `consent_type` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `correspondence_attachment`
--

LOCK TABLES `correspondence_attachment` WRITE;
/*!40000 ALTER TABLE `correspondence_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `correspondence_attachment` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `correspondence_audit`
--

LOCK TABLES `correspondence_audit` WRITE;
/*!40000 ALTER TABLE `correspondence_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `correspondence_audit` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `correspondence_audit_attachment`
--

LOCK TABLES `correspondence_audit_attachment` WRITE;
/*!40000 ALTER TABLE `correspondence_audit_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `correspondence_audit_attachment` ENABLE KEYS */;
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
INSERT INTO `correspondence_direction_type` VALUES (1,'Incoming',NULL),(2,'Outgoing',NULL);
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
INSERT INTO `correspondence_mode_type` VALUES (1,'Mail',NULL),(2,'Fax',NULL),(3,'Email',NULL),(4,'Telephone',NULL),(5,'Face to face',NULL),(6,'Not applicable',NULL);
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
INSERT INTO `correspondence_outcome_type` VALUES (1,'Sent',NULL),(2,'Received',NULL),(3,'Return to sender',NULL),(4,'Engaged',NULL),(5,'No answer',NULL),(6,'Contact made',NULL),(7,'Message given to person',NULL),(8,'Not applicable',NULL);
/*!40000 ALTER TABLE `correspondence_outcome_type` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correspondence_status_type`
--

LOCK TABLES `correspondence_status_type` WRITE;
/*!40000 ALTER TABLE `correspondence_status_type` DISABLE KEYS */;
INSERT INTO `correspondence_status_type` VALUES (1,'On call',NULL),(2,'Archived',NULL);
/*!40000 ALTER TABLE `correspondence_status_type` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `fk_correspondences_ark_user` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_status_type_id` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `correspondence_status_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `COUNTRY_CODE` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'AUSTRALIA',NULL,'AU'),(2,'UNITED KINGDOM',NULL,NULL),(3,'CANADA',NULL,NULL),(4,'AFGHANISTAN',NULL,'AF'),(5,'ALAND ISLANDS',NULL,'AX'),(6,'ALBANIA',NULL,'AL'),(7,'ALGERIA',NULL,'DZ'),(8,'AMERICAN SAMOA',NULL,'AS'),(9,'ANDORRA',NULL,'AD'),(10,'ANGOLA',NULL,'AO'),(11,'ANGUILLA',NULL,'AI'),(12,'ANTARCTICA',NULL,'AQ'),(13,'ANTIGUA AND BARBUDA',NULL,'AG'),(14,'ARGENTINA',NULL,'AR'),(15,'ARMENIA',NULL,'AM'),(16,'ARUBA',NULL,'AW'),(18,'AUSTRIA',NULL,'AT'),(19,'AZERBAIJAN',NULL,'AZ'),(20,'BAHAMAS',NULL,'BS'),(21,'BAHRAIN',NULL,'BH'),(22,'BANGLADESH',NULL,'BD'),(23,'BARBADOS',NULL,'BB'),(24,'BELARUS',NULL,'BY'),(25,'BELGIUM',NULL,'BE'),(26,'BELIZE',NULL,'BZ'),(27,'BENIN',NULL,'BJ'),(28,'BERMUDA',NULL,'BM'),(29,'BHUTAN',NULL,'BT'),(30,'BOLIVIA, PLURINATIONAL STATE OF',NULL,'BO'),(31,'BONAIRE, SAINT EUSTATIUS AND SABA',NULL,'BQ'),(32,'BOSNIA AND HERZEGOVINA',NULL,'BA'),(33,'BOTSWANA',NULL,'BW'),(34,'BOUVET ISLAND',NULL,'BV'),(35,'BRAZIL',NULL,'BR'),(36,'BRITISH INDIAN OCEAN TERRITORY',NULL,'IO'),(37,'BRUNEI DARUSSALAM',NULL,'BN'),(38,'BULGARIA',NULL,'BG'),(39,'BURKINA FASO',NULL,'BF'),(40,'BURUNDI',NULL,'BI'),(41,'CAMBODIA',NULL,'KH'),(42,'CAMEROON',NULL,'CM'),(44,'CAPE VERDE',NULL,'CV'),(45,'CAYMAN ISLANDS',NULL,'KY'),(46,'CENTRAL AFRICAN REPUBLIC',NULL,'CF'),(47,'CHAD',NULL,'TD'),(48,'CHILE',NULL,'CL'),(49,'CHINA',NULL,'CN'),(50,'CHRISTMAS ISLAND',NULL,'CX'),(51,'COCOS (KEELING) ISLANDS',NULL,'CC'),(52,'COLOMBIA',NULL,'CO'),(53,'COMOROS',NULL,'KM'),(54,'CONGO',NULL,'CG'),(55,'CONGO, THE DEMOCRATIC REPUBLIC OF THE',NULL,'CD'),(56,'COOK ISLANDS',NULL,'CK'),(57,'COSTA RICA',NULL,'CR'),(58,'COTE D\'\'IVOIRE',NULL,'CI'),(59,'CROATIA',NULL,'HR'),(60,'CUBA',NULL,'CU'),(61,'CURACAO',NULL,'CW'),(62,'CYPRUS',NULL,'CY'),(63,'CZECH REPUBLIC',NULL,'CZ'),(64,'DENMARK',NULL,'DK'),(65,'DJIBOUTI',NULL,'DJ'),(66,'DOMINICA',NULL,'DM'),(67,'DOMINICAN REPUBLIC',NULL,'DO'),(68,'ECUADOR',NULL,'EC'),(69,'EGYPT',NULL,'EG'),(70,'EL SALVADOR',NULL,'SV'),(71,'EQUATORIAL GUINEA',NULL,'GQ'),(72,'ERITREA',NULL,'ER'),(73,'ESTONIA',NULL,'EE'),(74,'ETHIOPIA',NULL,'ET'),(75,'FALKLAND ISLANDS (MALVINAS)',NULL,'FK'),(76,'FAROE ISLANDS',NULL,'FO'),(77,'FIJI',NULL,'FJ'),(78,'FINLAND',NULL,'FI'),(79,'FRANCE',NULL,'FR'),(80,'FRENCH GUIANA',NULL,'GF'),(81,'FRENCH POLYNESIA',NULL,'PF'),(82,'FRENCH SOUTHERN TERRITORIES',NULL,'TF'),(83,'GABON',NULL,'GA'),(84,'GAMBIA',NULL,'GM'),(85,'GEORGIA',NULL,'GE'),(86,'GERMANY',NULL,'DE'),(87,'GHANA',NULL,'GH'),(88,'GIBRALTAR',NULL,'GI'),(89,'GREECE',NULL,'GR'),(90,'GREENLAND',NULL,'GL'),(91,'GRENADA',NULL,'GD'),(92,'GUADELOUPE',NULL,'GP'),(93,'GUAM',NULL,'GU'),(94,'GUATEMALA',NULL,'GT'),(95,'GUERNSEY',NULL,'GG'),(96,'GUINEA',NULL,'GN'),(97,'GUINEA-BISSAU',NULL,'GW'),(98,'GUYANA',NULL,'GY'),(99,'HAITI',NULL,'HT'),(100,'HEARD ISLAND AND MCDONALD ISLANDS',NULL,'HM'),(101,'HOLY SEE (VATICAN CITY STATE)',NULL,'VA'),(102,'HONDURAS',NULL,'HN'),(103,'HONG KONG',NULL,'HK'),(104,'HUNGARY',NULL,'HU'),(105,'ICELAND',NULL,'IS'),(106,'INDIA',NULL,'IN'),(107,'INDONESIA',NULL,'ID'),(108,'IRAN, ISLAMIC REPUBLIC OF',NULL,'IR'),(109,'IRAQ',NULL,'IQ'),(110,'IRELAND',NULL,'IE'),(111,'ISLE OF MAN',NULL,'IM'),(112,'ISRAEL',NULL,'IL'),(113,'ITALY',NULL,'IT'),(114,'JAMAICA',NULL,'JM'),(115,'JAPAN',NULL,'JP'),(116,'JERSEY',NULL,'JE'),(117,'JORDAN',NULL,'JO'),(118,'KAZAKHSTAN',NULL,'KZ'),(119,'KENYA',NULL,'KE'),(120,'KIRIBATI',NULL,'KI'),(121,'KOREA, DEMOCRATIC PEOPLE\'S REPUBLIC OF',NULL,'KP'),(122,'KOREA, REPUBLIC OF',NULL,'KR'),(123,'KUWAIT',NULL,'KW'),(124,'KYRGYZSTAN',NULL,'KG'),(125,'LAO PEOPLE\'S DEMOCRATIC REPUBLIC',NULL,'LA'),(126,'LATVIA',NULL,'LV'),(127,'LEBANON',NULL,'LB'),(128,'LESOTHO',NULL,'LS'),(129,'LIBERIA',NULL,'LR'),(130,'LIBYAN ARAB JAMAHIRIYA',NULL,'LY'),(131,'LIECHTENSTEIN',NULL,'LI'),(132,'LITHUANIA',NULL,'LT'),(133,'LUXEMBOURG',NULL,'LU'),(134,'MACAO',NULL,'MO'),(135,'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF',NULL,'MK'),(136,'MADAGASCAR',NULL,'MG'),(137,'MALAWI',NULL,'MW'),(138,'MALAYSIA',NULL,'MY'),(139,'MALDIVES',NULL,'MV'),(140,'MALI',NULL,'ML'),(141,'MALTA',NULL,'MT'),(142,'MARSHALL ISLANDS',NULL,'MH'),(143,'MARTINIQUE',NULL,'MQ'),(144,'MAURITANIA',NULL,'MR'),(145,'MAURITIUS',NULL,'MU'),(146,'MAYOTTE',NULL,'YT'),(147,'MEXICO',NULL,'MX'),(148,'MICRONESIA, FEDERATED STATES OF',NULL,'FM'),(149,'MOLDOVA, REPUBLIC OF',NULL,'MD'),(150,'MONACO',NULL,'MC'),(151,'MONGOLIA',NULL,'MN'),(152,'MONTENEGRO',NULL,'ME'),(153,'MONTSERRAT',NULL,'MS'),(154,'MOROCCO',NULL,'MA'),(155,'MOZAMBIQUE',NULL,'MZ'),(156,'MYANMAR',NULL,'MM'),(157,'NAMIBIA',NULL,'NA'),(158,'NAURU',NULL,'NR'),(159,'NEPAL',NULL,'NP'),(160,'NETHERLANDS',NULL,'NL'),(161,'NEW CALEDONIA',NULL,'NC'),(162,'NEW ZEALAND',NULL,'NZ'),(163,'NICARAGUA',NULL,'NI'),(164,'NIGER',NULL,'NE'),(165,'NIGERIA',NULL,'NG'),(166,'NIUE',NULL,'NU'),(167,'NORFOLK ISLAND',NULL,'NF'),(168,'NORTHERN MARIANA ISLANDS',NULL,'MP'),(169,'NORWAY',NULL,'NO'),(170,'OMAN',NULL,'OM'),(171,'PAKISTAN',NULL,'PK'),(172,'PALAU',NULL,'PW'),(173,'PALESTINIAN TERRITORY, OCCUPIED',NULL,'PS'),(174,'PANAMA',NULL,'PA'),(175,'PAPUA NEW GUINEA',NULL,'PG'),(176,'PARAGUAY',NULL,'PY'),(177,'PERU',NULL,'PE'),(178,'PHILIPPINES',NULL,'PH'),(179,'PITCAIRN',NULL,'PN'),(180,'POLAND',NULL,'PL'),(181,'PORTUGAL',NULL,'PT'),(182,'PUERTO RICO',NULL,'PR'),(183,'QATAR',NULL,'QA'),(184,'REUNION',NULL,'RE'),(185,'ROMANIA',NULL,'RO'),(186,'RUSSIAN FEDERATION',NULL,'RU'),(187,'RWANDA',NULL,'RW'),(188,'SAINT BARTHELEMY',NULL,'BL'),(189,'SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA',NULL,'SH'),(190,'SAINT KITTS AND NEVIS',NULL,'KN'),(191,'SAINT LUCIA',NULL,'LC'),(192,'SAINT MARTIN (FRENCH PART)',NULL,'MF'),(193,'SAINT PIERRE AND MIQUELON',NULL,'PM'),(194,'SAINT VINCENT AND THE GRENADINES',NULL,'VC'),(195,'SAMOA',NULL,'WS'),(196,'SAN MARINO',NULL,'SM'),(197,'SAO TOME AND PRINCIPE',NULL,'ST'),(198,'SAUDI ARABIA',NULL,'SA'),(199,'SENEGAL',NULL,'SN'),(200,'SERBIA',NULL,'RS'),(201,'SEYCHELLES',NULL,'SC'),(202,'SIERRA LEONE',NULL,'SL'),(203,'SINGAPORE',NULL,'SG'),(204,'SINT MAARTEN (DUTCH PART)',NULL,'SX'),(205,'SLOVAKIA',NULL,'SK'),(206,'SLOVENIA',NULL,'SI'),(207,'SOLOMON ISLANDS',NULL,'SB'),(208,'SOMALIA',NULL,'SO'),(209,'SOUTH AFRICA',NULL,'ZA'),(210,'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS',NULL,'GS'),(211,'SPAIN',NULL,'ES'),(212,'SRI LANKA',NULL,'LK'),(213,'SUDAN',NULL,'SD'),(214,'SURINAME',NULL,'SR'),(215,'SVALBARD AND JAN MAYEN',NULL,'SJ'),(216,'SWAZILAND',NULL,'SZ'),(217,'SWEDEN',NULL,'SE'),(218,'SWITZERLAND',NULL,'CH'),(219,'SYRIAN ARAB REPUBLIC',NULL,'SY'),(220,'TAIWAN, PROVINCE OF CHINA',NULL,'TW'),(221,'TAJIKISTAN',NULL,'TJ'),(222,'TANZANIA, UNITED REPUBLIC OF',NULL,'TZ'),(223,'THAILAND',NULL,'TH'),(224,'TIMOR-LESTE',NULL,'TL'),(225,'TOGO',NULL,'TG'),(226,'TOKELAU',NULL,'TK'),(227,'TONGA',NULL,'TO'),(228,'TRINIDAD AND TOBAGO',NULL,'TT'),(229,'TUNISIA',NULL,'TN'),(230,'TURKEY',NULL,'TR'),(231,'TURKMENISTAN',NULL,'TM'),(232,'TURKS AND CAICOS ISLANDS',NULL,'TC'),(233,'TUVALU',NULL,'TV'),(234,'UGANDA',NULL,'UG'),(235,'UKRAINE',NULL,'UA'),(236,'UNITED ARAB EMIRATES',NULL,'AE'),(238,'UNITED STATES',NULL,'US'),(239,'UNITED STATES MINOR OUTLYING ISLANDS',NULL,'UM'),(240,'URUGUAY',NULL,'UY'),(241,'UZBEKISTAN',NULL,'UZ'),(242,'VANUATU',NULL,'VU'),(243,'VATICAN CITY STATE',NULL,'VA'),(244,'VENEZUELA, BOLIVARIAN REPUBLIC OF',NULL,'VE'),(245,'VIET NAM',NULL,'VN'),(246,'VIRGIN ISLANDS, BRITISH',NULL,'VG'),(247,'VIRGIN ISLANDS, U.S.',NULL,'VI'),(248,'WALLIS AND FUTUNA',NULL,'WF'),(249,'WESTERN SAHARA',NULL,'EH'),(250,'YEMEN',NULL,'YE'),(251,'ZAMBIA',NULL,'ZM'),(252,'ZIMBABWE',NULL,'ZW');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='A link table that associates a country and its respective st';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country_state`
--

LOCK TABLES `country_state` WRITE;
/*!40000 ALTER TABLE `country_state` DISABLE KEYS */;
INSERT INTO `country_state` VALUES (1,1,'WA'),(2,1,'NSW'),(3,1,'VIC'),(4,1,'ACT'),(5,1,'NT'),(6,1,'QLD'),(7,3,'Alberta'),(8,2,'Bedfordshire'),(9,2,'Berkshire'),(10,1,'TAS'),(11,1,'SA');
/*!40000 ALTER TABLE `country_state` ENABLE KEYS */;
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
-- Table structure for table `custom_consent_answer`
--

DROP TABLE IF EXISTS `custom_consent_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_consent_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_CONSENT_QUESTION_ID` int(11) DEFAULT NULL,
  `CONSENT_ANSWER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_custom_consent_answer_1` (`CUSTOM_CONSENT_QUESTION_ID`) USING BTREE,
  KEY `fk_custom_consent_answer_2` (`CONSENT_ANSWER_ID`) USING BTREE,
  CONSTRAINT `fk_custom_consent_answer_1` FOREIGN KEY (`CUSTOM_CONSENT_QUESTION_ID`) REFERENCES `custom_consent_question` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_custom_consent_answer_2` FOREIGN KEY (`CONSENT_ANSWER_ID`) REFERENCES `question_answer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_consent_answer`
--

LOCK TABLES `custom_consent_answer` WRITE;
/*!40000 ALTER TABLE `custom_consent_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_consent_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_consent_question`
--

DROP TABLE IF EXISTS `custom_consent_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_consent_question` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(500) NOT NULL,
  `DATA_TYPE_ID` int(11) NOT NULL,
  `DESCRETE_VALUE` varchar(45) DEFAULT NULL,
  `POSITION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`) USING BTREE,
  KEY `fk_custom_consent_question_1` (`DATA_TYPE_ID`) USING BTREE,
  CONSTRAINT `fk_custom_consent_question_1` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `data_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_consent_question`
--

LOCK TABLES `custom_consent_question` WRITE;
/*!40000 ALTER TABLE `custom_consent_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_consent_question` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_UNIT_TYPE_ID` (`UNIT_TYPE_ID`),
  KEY `FK_CUSTOMFIELD_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_CUSTOMFIELD_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `custom_field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  PRIMARY KEY (`ID`)
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
-- Table structure for table `data_type`
--

DROP TABLE IF EXISTS `data_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_type`
--

LOCK TABLES `data_type` WRITE;
/*!40000 ALTER TABLE `data_type` DISABLE KEYS */;
INSERT INTO `data_type` VALUES (1,'TEXT',NULL),(2,'NUMBER',NULL),(3,'DATE',NULL);
/*!40000 ALTER TABLE `data_type` ENABLE KEYS */;
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
INSERT INTO `delimiter_type` VALUES (1,'COMMA','Comma',','),(2,'TAB','Tab character','	'),(3,'PIPE','Pipe character','|'),(4,'COLON','Colon character',':'),(5,'AT SYMBOL','At characer','@');
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
INSERT INTO `domain_type` VALUES (1,'STUDY',NULL),(2,'STUDY COMPONENT',NULL);
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
  PRIMARY KEY (`ID`,`PERSON_ID`,`EMAIL_ACCOUNT_TYPE_ID`),
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
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` VALUES (1,'CHARACTER'),(2,'NUMBER'),(3,'DATE');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` VALUES (1,'CSV','Comma separated values'),(2,'TXT','Tab separated text file'),(3,'XLS','Excel Spreadsheet');
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
INSERT INTO `gender_type` VALUES (0,'Unknown',NULL),(1,'Male',NULL),(2,'Female',NULL);
/*!40000 ALTER TABLE `gender_type` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `link_correspondence_audit_person`
--

LOCK TABLES `link_correspondence_audit_person` WRITE;
/*!40000 ALTER TABLE `link_correspondence_audit_person` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_correspondence_audit_person` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`,`PERSON_ID`,`STUDY_SITE_ID`),
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
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
-- Dumping data for table `link_study_studycomp`
--

LOCK TABLES `link_study_studycomp` WRITE;
/*!40000 ALTER TABLE `link_study_studycomp` DISABLE KEYS */;
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
  PRIMARY KEY (`ID`,`STUDY_SITE_ID`,`STUDY_ID`),
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
  PRIMARY KEY (`ID`,`STUDY_ID`,`SUB_STUDY_ID`),
  KEY `LINK_STUDY_SUBSTUDY_STUDY_FK` (`STUDY_ID`) USING BTREE,
  KEY `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK` (`SUB_STUDY_ID`) USING BTREE,
  CONSTRAINT `link_study_substudy_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
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
-- Dumping data for table `link_subject_contact`
--

LOCK TABLES `link_subject_contact` WRITE;
/*!40000 ALTER TABLE `link_subject_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_contact` ENABLE KEYS */;
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
  KEY `fk_link_subject_study_1` (`CONSENT_DOWNLOADED`),
  CONSTRAINT `fk_link_subject_study_1` FOREIGN KEY (`CONSENT_DOWNLOADED`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
-- Dumping data for table `link_subject_studycomp`
--

LOCK TABLES `link_subject_studycomp` WRITE;
/*!40000 ALTER TABLE `link_subject_studycomp` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_subject_studycomp` ENABLE KEYS */;
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
INSERT INTO `marital_status` VALUES (1,'Married',NULL),(2,'Single',NULL),(3,'Divorced',NULL),(4,'Unknown',NULL);
/*!40000 ALTER TABLE `marital_status` ENABLE KEYS */;
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
INSERT INTO `person_contact_method` VALUES (3,'Email'),(1,'Home telephone'),(2,'Mobile telephone'),(4,'Post');
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
INSERT INTO `phone_status` VALUES (1,'Current',NULL),(2,'Current Alternative',NULL),(3,'Current Under Investigation',NULL),(4,'Valid Past',NULL),(5,'Incorrect or Disconnected',NULL);
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
INSERT INTO `phone_type` VALUES (1,'Mobile','Mobile  Cell Phones'),(2,'Home','Land Home Phone'),(3,'Work','Land Phone at Office');
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
  PRIMARY KEY (`ID`,`STUDY_STATUS_ID`),
  KEY `STUDY_STUDY_STATUS_FK1` (`STUDY_STATUS_ID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE,
  KEY `fk_study_subjectuid_padchar` (`SUBJECTUID_PADCHAR_ID`),
  KEY `fk_study_subjectuid_token` (`SUBJECTUID_TOKEN_ID`),
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
  PRIMARY KEY (`ID`,`STUDY_ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`),
  KEY `STUDY_COMP_STUDY_FK` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `study_comp_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
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
INSERT INTO `study_comp_status` VALUES (1,'Completed',NULL),(2,'Not Completed',NULL),(3,'Not Needed',NULL),(4,'Not Available',NULL),(5,'Pending',NULL),(6,'Received',NULL),(7,'Requested',NULL),(8,'Refused',NULL),(9,'Unknown',NULL);
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
INSERT INTO `study_status` VALUES (1,'Active',NULL),(2,'Discussion',NULL),(3,'EOI',NULL),(4,'Full Application',NULL),(5,'Ethics',NULL),(6,'Dispute Recorded',NULL),(7,'Approved',NULL),(8,'Active-Recruiting',NULL),(9,'Active-Ongoing Programme',NULL),(10,'Active-Data Analysis',NULL),(11,'Active-Writing Up',NULL),(12,'Unsuccessful Funding',NULL),(13,'EOI-Rejected',NULL),(14,'EOI-Abandoned',NULL),(15,'Archive',NULL);
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
-- Table structure for table `subject_cust_fld_dat`
--

DROP TABLE IF EXISTS `subject_cust_fld_dat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_cust_fld_dat` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIELD_DATA` text,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `SUBJECT_CUSTM_FLD_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`LINK_SUBJECT_STUDY_ID`,`SUBJECT_CUSTM_FLD_ID`),
  KEY `SCFD_LINK_SUBJECT_STUDY_FK` (`LINK_SUBJECT_STUDY_ID`) USING BTREE,
  KEY `SCFD_SUBJECT_CUSTOM_FIELD_FK` (`SUBJECT_CUSTM_FLD_ID`) USING BTREE,
  CONSTRAINT `subject_cust_fld_dat_ibfk_1` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`),
  CONSTRAINT `subject_cust_fld_dat_ibfk_2` FOREIGN KEY (`SUBJECT_CUSTM_FLD_ID`) REFERENCES `subject_custm_fld` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`LINK_SUBJECT_STUDY_ID`) REFER `study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_cust_fld_dat`
--

LOCK TABLES `subject_cust_fld_dat` WRITE;
/*!40000 ALTER TABLE `subject_cust_fld_dat` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_cust_fld_dat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject_custm_fld`
--

DROP TABLE IF EXISTS `subject_custm_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_custm_fld` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `DATA_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `MIN_VALUE` varchar(100) DEFAULT NULL,
  `MAX_VALUE` varchar(100) DEFAULT NULL,
  `DISCRETE_VALUES` varchar(1000) DEFAULT NULL,
  `FIELD_POSITION` int(11) NOT NULL,
  `FIELD_TITLE` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`,`STUDY_ID`,`DATA_TYPE_ID`),
  KEY `SUBJECT_CUSTM_FLD_DATA_TYPE_FK` (`DATA_TYPE_ID`) USING BTREE,
  KEY `SUBJECT_CUSTM_FLD_STUDY_FK` (`STUDY_ID`) USING BTREE,
  CONSTRAINT `subject_custm_fld_ibfk_1` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `data_type` (`ID`),
  CONSTRAINT `subject_custm_fld_ibfk_2` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DATA_TYPE_ID`) REFER `study/data_typ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_custm_fld`
--

LOCK TABLES `subject_custm_fld` WRITE;
/*!40000 ALTER TABLE `subject_custm_fld` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_custm_fld` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_status`
--

LOCK TABLES `subject_status` WRITE;
/*!40000 ALTER TABLE `subject_status` DISABLE KEYS */;
INSERT INTO `subject_status` VALUES (1,'Subject',NULL),(2,'Prospect',NULL),(3,'Withdrawn Subject',NULL),(4,'Archive',NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjectuid_padchar`
--

LOCK TABLES `subjectuid_padchar` WRITE;
/*!40000 ALTER TABLE `subjectuid_padchar` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjectuid_token`
--

LOCK TABLES `subjectuid_token` WRITE;
/*!40000 ALTER TABLE `subjectuid_token` DISABLE KEYS */;
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
INSERT INTO `title_type` VALUES (0,'Unknown',NULL),(1,'Br',NULL),(2,'Capt','Captain'),(3,'Col',NULL),(4,'Cpl',NULL),(5,'Dean',NULL),(6,'Dr',NULL),(7,'Fr',NULL),(8,'Lac',NULL),(9,'Major',NULL),(10,'Miss',NULL),(11,'Mr',NULL),(12,'Mrs',NULL),(13,'Ms.',NULL),(14,'Past',NULL),(15,'Prof',NULL),(16,'Pstr',NULL),(17,'Rev',NULL),(18,'Sir',NULL),(19,'Sr',NULL);
/*!40000 ALTER TABLE `title_type` ENABLE KEYS */;
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
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_ARK_FUNCTION_UNIQUE` (`NAME`,`ARK_FUNCTION_ID`),
  KEY `FK_UNIT_TYPE_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_UNIT_TYPE_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit_type`
--

LOCK TABLES `unit_type` WRITE;
/*!40000 ALTER TABLE `unit_type` DISABLE KEYS */;
INSERT INTO `unit_type` VALUES (63,NULL,'mm','Millimetres'),(64,NULL,'cm','Centimetres'),(65,NULL,'m','Metres'),(66,NULL,'g','Grams'),(67,NULL,'kg','Kilograms'),(68,NULL,'L','Litres'),(69,NULL,'Days',''),(70,NULL,'Months',''),(71,NULL,'Years',''),(72,NULL,'hrs','Hours'),(73,NULL,'min','Minutes'),(74,NULL,'s','Seconds'),(75,18,'ug/L',NULL),(76,18,'bpm',NULL),(77,18,'g/L',NULL),(78,18,'fL',NULL),(79,18,'feet',NULL),(80,18,'IU/L',NULL),(81,18,'U',NULL),(82,18,'Age',NULL),(83,18,'m/L',NULL),(84,18,'pg',NULL),(85,18,'pred',NULL),(86,18,'Gy',NULL),(87,18,'%',NULL),(88,18,'mS',NULL),(89,18,'mm/hr',NULL),(90,18,'mg/dl',NULL),(91,18,'mn',NULL),(92,18,'mg/L',NULL),(93,18,'kgm2',NULL),(94,18,'mm Hg',NULL),(95,18,'kg/m2',NULL),(96,18,'Pipes',NULL),(97,18,'S',NULL),(98,18,'mm/hg',NULL),(99,19,'ug/L',NULL),(100,19,'bpm',NULL),(101,19,'g/L',NULL),(102,19,'fL',NULL),(103,19,'feet',NULL),(104,19,'IU/L',NULL),(105,19,'U',NULL),(106,19,'Age',NULL),(107,19,'m/L',NULL),(108,19,'pg',NULL),(109,19,'pred',NULL),(110,19,'Gy',NULL),(111,19,'%',NULL),(112,19,'mS',NULL),(113,19,'mm/hr',NULL),(114,19,'mg/dl',NULL),(115,19,'mn',NULL),(116,19,'mg/L',NULL),(117,19,'kgm2',NULL),(118,19,'mm Hg',NULL),(119,19,'kg/m2',NULL),(120,19,'Pipes',NULL),(121,19,'S',NULL),(122,19,'mm/hg',NULL);
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
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `UPLOAD_REPORT` longblob,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  KEY `fk_upload_study` (`STUDY_ID`),
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
INSERT INTO `vital_status` VALUES (0,'Unknown',NULL),(1,'Alive',NULL),(2,'Deceased',NULL);
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
INSERT INTO `yes_no` VALUES (1,'Yes'),(2,'No');
/*!40000 ALTER TABLE `yes_no` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `pheno`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pheno` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `pheno`;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STATUS_ID`) REFER `pheno/status`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection_upload`
--

LOCK TABLES `collection_upload` WRITE;
/*!40000 ALTER TABLE `collection_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection_upload` ENABLE KEYS */;
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
  `DELIMITER_CHARACTER` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delimiter_type`
--

LOCK TABLES `delimiter_type` WRITE;
/*!40000 ALTER TABLE `delimiter_type` DISABLE KEYS */;
INSERT INTO `delimiter_type` VALUES (1,'COMMA','Comma',','),(2,'TAB','Tab character','	'),(3,'PIPE','Pipe character','|'),(4,'COLON','Colon character',':'),(5,'AT SYMBOL','At character','@');
/*!40000 ALTER TABLE `delimiter_type` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_TYPE_ID`) REFER `pheno/field_t';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_collection`
--

LOCK TABLES `field_collection` WRITE;
/*!40000 ALTER TABLE `field_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_collection` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `fk_field_data_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_field_data_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_data`
--

LOCK TABLES `field_data` WRITE;
/*!40000 ALTER TABLE `field_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_data` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `field_data_log`
--

LOCK TABLES `field_data_log` WRITE;
/*!40000 ALTER TABLE `field_data_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_data_log` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `field_field_group`
--

LOCK TABLES `field_field_group` WRITE;
/*!40000 ALTER TABLE `field_field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_field_group` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `field_group`
--

LOCK TABLES `field_group` WRITE;
/*!40000 ALTER TABLE `field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_group` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `field_group_upload`
--

LOCK TABLES `field_group_upload` WRITE;
/*!40000 ALTER TABLE `field_group_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_group_upload` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` VALUES (1,'CHARACTER'),(2,'NUMBER'),(3,'DATE');
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_ID`) REFER `pheno/field`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_upload`
--

LOCK TABLES `field_upload` WRITE;
/*!40000 ALTER TABLE `field_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_upload` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` VALUES (1,'CSV','Comma separated values'),(2,'TXT','Tab separated text file'),(3,'XLS','Excel Spreadsheet');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
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
INSERT INTO `status` VALUES (1,'CREATED'),(2,'ACTIVE'),(3,'DISPLAYED'),(4,'EXPIRED');
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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `pheno/del';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload`
--

LOCK TABLES `upload` WRITE;
/*!40000 ALTER TABLE `upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `geno`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `geno` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `geno`;

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
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `collection_import`
--

LOCK TABLES `collection_import` WRITE;
/*!40000 ALTER TABLE `collection_import` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection_import` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `decode_mask`
--

LOCK TABLES `decode_mask` WRITE;
/*!40000 ALTER TABLE `decode_mask` DISABLE KEYS */;
/*!40000 ALTER TABLE `decode_mask` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delimiter_type`
--

LOCK TABLES `delimiter_type` WRITE;
/*!40000 ALTER TABLE `delimiter_type` DISABLE KEYS */;
INSERT INTO `delimiter_type` VALUES (1,'Tab'),(2,'Comma');
/*!40000 ALTER TABLE `delimiter_type` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `encoded_data`
--

LOCK TABLES `encoded_data` WRITE;
/*!40000 ALTER TABLE `encoded_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `encoded_data` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` VALUES (1,'PED'),(2,'MAP');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_type`
--

LOCK TABLES `import_type` WRITE;
/*!40000 ALTER TABLE `import_type` DISABLE KEYS */;
INSERT INTO `import_type` VALUES (1,'Raw'),(2,'Imputed');
/*!40000 ALTER TABLE `import_type` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `marker`
--

LOCK TABLES `marker` WRITE;
/*!40000 ALTER TABLE `marker` DISABLE KEYS */;
/*!40000 ALTER TABLE `marker` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `marker_group`
--

LOCK TABLES `marker_group` WRITE;
/*!40000 ALTER TABLE `marker_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `marker_group` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `marker_meta_data`
--

LOCK TABLES `marker_meta_data` WRITE;
/*!40000 ALTER TABLE `marker_meta_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `marker_meta_data` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `marker_type`
--

LOCK TABLES `marker_type` WRITE;
/*!40000 ALTER TABLE `marker_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `marker_type` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `meta_data`
--

LOCK TABLES `meta_data` WRITE;
/*!40000 ALTER TABLE `meta_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_data` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `meta_data_field`
--

LOCK TABLES `meta_data_field` WRITE;
/*!40000 ALTER TABLE `meta_data_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_data_field` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `meta_data_type`
--

LOCK TABLES `meta_data_type` WRITE;
/*!40000 ALTER TABLE `meta_data_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_data_type` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'Created'),(2,'Active'),(3,'Disabled'),(4,'Expired');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `subject_marker_meta_data`
--

LOCK TABLES `subject_marker_meta_data` WRITE;
/*!40000 ALTER TABLE `subject_marker_meta_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_marker_meta_data` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `subject_meta_data`
--

LOCK TABLES `subject_meta_data` WRITE;
/*!40000 ALTER TABLE `subject_meta_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject_meta_data` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `upload`
--

LOCK TABLES `upload` WRITE;
/*!40000 ALTER TABLE `upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `upload_collection`
--

LOCK TABLES `upload_collection` WRITE;
/*!40000 ALTER TABLE `upload_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload_collection` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Dumping data for table `upload_marker_group`
--

LOCK TABLES `upload_marker_group` WRITE;
/*!40000 ALTER TABLE `upload_marker_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `upload_marker_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `lims`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `lims` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `lims`;

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
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `barcodeformat`
--

LOCK TABLES `barcodeformat` WRITE;
/*!40000 ALTER TABLE `barcodeformat` DISABLE KEYS */;
/*!40000 ALTER TABLE `barcodeformat` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `barcodeid_engine`
--

LOCK TABLES `barcodeid_engine` WRITE;
/*!40000 ALTER TABLE `barcodeid_engine` DISABLE KEYS */;
/*!40000 ALTER TABLE `barcodeid_engine` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bio_sampletype`
--

LOCK TABLES `bio_sampletype` WRITE;
/*!40000 ALTER TABLE `bio_sampletype` DISABLE KEYS */;
INSERT INTO `bio_sampletype` VALUES (1,'Blood / Blood','Blood','Blood'),(2,'Blood / Buffy Coat','Blood','Buffy Coat'),(3,'Blood / Buffy Coat (ACD)','Blood','Buffy Coat (ACD)'),(4,'Blood / Buffy Coat (EDTA)','Blood','Buffy Coat (EDTA)'),(5,'Blood / Buffy Coat (LH)','Blood','Buffy Coat (LH)'),(6,'Blood / Cord blood','Blood','Cord blood'),(7,'Blood / EDTA Blood','Blood','EDTA Blood'),(8,'Blood / Frozen Lymphocytes (F)','Blood','Frozen Lymphocytes (F)'),(9,'Blood / Lithium Heparin','Blood','Lithium Heparin'),(10,'Blood / Mothers Blood','Blood','Mothers Blood'),(11,'Blood / Plasma','Blood','Plasma'),(12,'Blood / Plasma (ACD)','Blood','Plasma (ACD)'),(13,'Blood / Plasma (EDTA)','Blood','Plasma (EDTA)'),(14,'Blood / Plasma (LH)','Blood','Plasma (LH)'),(15,'Blood / Protein from TL','Blood','Protein from TL'),(16,'Blood / Red Blood Cells','Blood','Red Blood Cells'),(17,'Blood / Serum','Blood','Serum'),(18,'Blood / Transformed lymphoblasts (T)','Blood','Transformed lymphoblasts (T)'),(19,'Blood / Unprocessed','Blood','Unprocessed'),(20,'Blood / Whole Blood','Blood','Whole Blood'),(21,'Blood / Whole Blood (EDTA)','Blood','Whole Blood (EDTA)'),(22,'Blood / Whole Blood (LH)','Blood','Whole Blood (LH)'),(23,'Nucleic Acid / Buccal Swab','Nucleic Acid','Buccal Swab'),(24,'Nucleic Acid / DNA','Nucleic Acid','DNA'),(25,'Nucleic Acid / DNA from BC','Nucleic Acid','DNA from BC'),(26,'Nucleic Acid / DNA from TL','Nucleic Acid','DNA from TL'),(27,'Nucleic Acid / DNA from Tissue','Nucleic Acid','DNA from Tissue'),(28,'Nucleic Acid / Paxgene RNA','Nucleic Acid','Paxgene RNA'),(29,'Nucleic Acid / RNA','Nucleic Acid','RNA'),(30,'Nucleic Acid / Saliva','Nucleic Acid','Saliva'),(31,'Protein from TL','Protein from TL',NULL),(32,'Saliva / Buccal Swab','Saliva','Buccal Swab'),(33,'Saliva / Buccal Swab (SB)','Saliva','Buccal Swab (SB)'),(34,'Saliva / Oragene (OS)','Saliva','Oragene (OS)'),(35,'Saliva / Throat Swab','Saliva','Throat Swab'),(36,'Saliva','Saliva',NULL),(37,'Tissue / Anus','Tissue','Anus'),(38,'Tissue / Appendix','Tissue','Appendix'),(39,'Tissue / Brain','Tissue','Brain'),(40,'Tissue / Breast','Tissue','Breast'),(41,'Tissue / Breast,Lt','Tissue','Breast,Lt'),(42,'Tissue / Breast,Rt','Tissue','Breast,Rt'),(43,'Tissue / Caecum','Tissue','Caecum'),(44,'Tissue / Colon','Tissue','Colon'),(45,'Tissue / Colon, ascending','Tissue','Colon, ascending'),(46,'Tissue / Colon, descending','Tissue','Colon, descending'),(47,'Tissue / Colon, hepatic flexure','Tissue','Colon, hepatic flexure'),(48,'Tissue / Colon, nos','Tissue','Colon, nos'),(49,'Tissue / Colon, sigmoid','Tissue','Colon, sigmoid'),(50,'Tissue / Colon, spenic flexure','Tissue','Colon, spenic flexure'),(51,'Tissue / Colon, splenic flexure','Tissue','Colon, splenic flexure'),(52,'Tissue / Colon, transverse','Tissue','Colon, transverse'),(53,'Tissue / Descending Colon','Tissue','Descending Colon'),(54,'Tissue / Duodenum','Tissue','Duodenum'),(55,'Tissue / Endometrium','Tissue','Endometrium'),(56,'Tissue / Ileum','Tissue','Ileum'),(57,'Tissue / Left Tube','Tissue','Left Tube'),(58,'Tissue / Liver','Tissue','Liver'),(59,'Tissue / Lung','Tissue','Lung'),(60,'Tissue / Lymph Node','Tissue','Lymph Node'),(61,'Tissue / Mesentary','Tissue','Mesentary'),(62,'Tissue / Oesophagus','Tissue','Oesophagus'),(63,'Tissue / Omentum','Tissue','Omentum'),(64,'Tissue / Ovarian Cyst L','Tissue','Ovarian Cyst L'),(65,'Tissue / Ovarian L','Tissue','Ovarian L'),(66,'Tissue / Ovarian R','Tissue','Ovarian R'),(67,'Tissue / Ovary','Tissue','Ovary'),(68,'Tissue / Pancreas','Tissue','Pancreas'),(69,'Tissue / Peritoneum, pelvic','Tissue','Peritoneum, pelvic'),(70,'Tissue / Placenta','Tissue','Placenta'),(71,'Tissue / Rectal Peritoneal Mass','Tissue','Rectal Peritoneal Mass'),(72,'Tissue / Recto-sigmoid','Tissue','Recto-sigmoid'),(73,'Tissue / Rectum','Tissue','Rectum'),(74,'Tissue / Right Tube','Tissue','Right Tube'),(75,'Tissue / Small Bowel','Tissue','Small Bowel'),(76,'Tissue / Spleen','Tissue','Spleen'),(77,'Tissue / Stomach','Tissue','Stomach'),(78,'Tissue / Tissue','Tissue','Tissue'),(79,'Tissue / Uterus','Tissue','Uterus'),(80,'Tissue / Uterus, endometrium','Tissue','Uterus, endometrium'),(81,'Urine / Urine','Urine','Urine'),(82,'Urine','Urine',NULL);
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
  `STUDY_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `TREATMENT` varchar(255) DEFAULT NULL,
  `SOURCESTUDY_ID` int(11) DEFAULT NULL,
  `UNIT` varchar(50) DEFAULT NULL,
  `DELIVERYDATE` datetime DEFAULT NULL,
  `FIXATIONTIME` varchar(50) DEFAULT NULL,
  `TRANSACTIONDATE` datetime NOT NULL,
  `QUANTITY` double NOT NULL,
  `OWNER` varchar(255) DEFAULT NULL,
  `REASON` text,
  `STATUS` varchar(50) DEFAULT NULL,
  `STUDY` varchar(50) DEFAULT NULL,
  `COLLABORATOR` varchar(255) DEFAULT NULL,
  `RECORDER` varchar(255) DEFAULT NULL,
  `DESTINATION` varchar(255) DEFAULT NULL,
  `ACTION` varchar(50) DEFAULT NULL,
  `TYPE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_bio_transactions_biospecimen_idx` (`BIOSPECIMEN_ID`),
  CONSTRAINT `fk_bio_transactions_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  CONSTRAINT `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` FOREIGN KEY (`BIO_COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
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
-- Dumping data for table `biodata`
--

LOCK TABLES `biodata` WRITE;
/*!40000 ALTER TABLE `biodata` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_criteria`
--

LOCK TABLES `biodata_criteria` WRITE;
/*!40000 ALTER TABLE `biodata_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_criteria` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_field`
--

LOCK TABLES `biodata_field` WRITE;
/*!40000 ALTER TABLE `biodata_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_field` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_field_group`
--

LOCK TABLES `biodata_field_group` WRITE;
/*!40000 ALTER TABLE `biodata_field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_field_group` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_field_lov`
--

LOCK TABLES `biodata_field_lov` WRITE;
/*!40000 ALTER TABLE `biodata_field_lov` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_field_lov` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_group`
--

LOCK TABLES `biodata_group` WRITE;
/*!40000 ALTER TABLE `biodata_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_group` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_group_criteria`
--

LOCK TABLES `biodata_group_criteria` WRITE;
/*!40000 ALTER TABLE `biodata_group_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_group_criteria` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_lov_list`
--

LOCK TABLES `biodata_lov_list` WRITE;
/*!40000 ALTER TABLE `biodata_lov_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_lov_list` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_type`
--

LOCK TABLES `biodata_type` WRITE;
/*!40000 ALTER TABLE `biodata_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_type` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `biodata_unit`
--

LOCK TABLES `biodata_unit` WRITE;
/*!40000 ALTER TABLE `biodata_unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `biodata_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biospecimen`
--

DROP TABLE IF EXISTS `biospecimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_ID` varchar(50) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) DEFAULT NULL,
  `COLLECTION_ID` int(11) DEFAULT NULL,
  `SUBSTUDY_ID` int(11) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `PARENTID` varchar(50) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `OTHERID` varchar(50) DEFAULT NULL,
  `STORED_IN` varchar(50) DEFAULT NULL,
  `SAMPLE_TIME` datetime DEFAULT NULL,
  `GRADE` varchar(255) DEFAULT NULL,
  `DEPTH` int(11) DEFAULT NULL,
  `SAMPLEDATE` datetime DEFAULT NULL,
  `EXTRACTED_TIME` datetime DEFAULT NULL,
  `LOCATION` varchar(255) DEFAULT NULL,
  `SAMPLETYPE_ID` int(11) NOT NULL,
  `SAMPLETYPE` varchar(255) DEFAULT NULL,
  `SAMPLESUBTYPE` varchar(255) DEFAULT NULL,
  `SUBTYPEDESC` varchar(255) DEFAULT NULL,
  `SPECIES` varchar(255) DEFAULT NULL,
  `QTY_COLLECTED` double DEFAULT NULL,
  `DATEEXTRACTED` datetime DEFAULT NULL,
  `QTY_REMOVED` double DEFAULT NULL,
  `GESTAT` double DEFAULT NULL,
  `COMMENTS` text,
  `DATEDISTRIBUTED` datetime DEFAULT NULL,
  `COLLABORATOR` varchar(100) DEFAULT NULL,
  `DNACONC` double DEFAULT NULL,
  `PURITY` double DEFAULT NULL,
  `ANTICOAG` varchar(100) DEFAULT NULL,
  `PROTOCOL` varchar(100) DEFAULT NULL,
  `DNA_BANK` int(11) DEFAULT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `UNITS` varchar(100) DEFAULT NULL,
  `QUALITY` varchar(100) DEFAULT NULL,
  `WITHDRAWN` int(11) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `TREATMENT` varchar(50) DEFAULT NULL,
  `BARCODED` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_collection` (`COLLECTION_ID`),
  KEY `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_ID`),
  KEY `fk_biospecimen_study` (`STUDY_ID`),
  CONSTRAINT `fk_biospecimen_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen`
--

LOCK TABLES `biospecimen` WRITE;
/*!40000 ALTER TABLE `biospecimen` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimen` ENABLE KEYS */;
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
  KEY `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_BIOSPECFDATA_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
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
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `flag`
--

LOCK TABLES `flag` WRITE;
/*!40000 ALTER TABLE `flag` DISABLE KEYS */;
/*!40000 ALTER TABLE `flag` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
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
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `NOOFCOL` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `TRAY_ID` int(11) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `NOOFROW` int(11) NOT NULL,
  `COLNOTYPE_ID` int(11) NOT NULL,
  `ROWNOTYPE_ID` int(11) NOT NULL,
  `TRANSFER_ID` int(11) DEFAULT NULL,
  `TYPE` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`),
  KEY `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`),
  KEY `fk_inv_box_tray_idx` (`TRAY_ID`),
  CONSTRAINT `fk_inv_box_tray` FOREIGN KEY (`TRAY_ID`) REFERENCES `inv_tray` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `ROWNO` int(11) DEFAULT NULL,
  `COLNO` int(11) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `BIOSPECIMEN_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_cell_box_idx` (`BOX_ID`) USING BTREE,
  KEY `fk_inv_cell_biospecimen_idx` (`BIOSPECIMEN_ID`) USING BTREE,
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_col_row_type`
--

LOCK TABLES `inv_col_row_type` WRITE;
/*!40000 ALTER TABLE `inv_col_row_type` DISABLE KEYS */;
INSERT INTO `inv_col_row_type` VALUES (1,'Numeric'),(2,'Alphabet');
/*!40000 ALTER TABLE `inv_col_row_type` ENABLE KEYS */;
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
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `CONTACT` varchar(50) DEFAULT NULL,
  `ADDRESS` text,
  `NAME` varchar(50) NOT NULL,
  `PHONE` varchar(50) DEFAULT NULL,
  `LDAP_GROUP` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_site`
--

LOCK TABLES `inv_site` WRITE;
/*!40000 ALTER TABLE `inv_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_tank`
--

DROP TABLE IF EXISTS `inv_tank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_tank` (
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
  KEY `fk_inv_tank_site` (`SITE_ID`),
  CONSTRAINT `fk_inv_tank_site` FOREIGN KEY (`SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_tank`
--

LOCK TABLES `inv_tank` WRITE;
/*!40000 ALTER TABLE `inv_tank` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_tank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_tray`
--

DROP TABLE IF EXISTS `inv_tray`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_tray` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TANK_ID` int(11) NOT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DESCRIPTION` text,
  `CAPACITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_tray_tank_idx` (`TANK_ID`) USING BTREE,
  CONSTRAINT `fk_inv_tray_tank` FOREIGN KEY (`TANK_ID`) REFERENCES `inv_tank` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_tray`
--

LOCK TABLES `inv_tray` WRITE;
/*!40000 ALTER TABLE `inv_tray` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_tray` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `inv_type`
--

LOCK TABLES `inv_type` WRITE;
/*!40000 ALTER TABLE `inv_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_type` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=2839 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listofvalues`
--

LOCK TABLES `listofvalues` WRITE;
/*!40000 ALTER TABLE `listofvalues` DISABLE KEYS */;
INSERT INTO `listofvalues` VALUES (3,0,0,'Wed Jan 07 16:39:34.427940 2004 AUSEST','BIOSPECLOCATION','Bldg 1 Room 1',1,1,NULL,NULL,NULL,0,'ENG'),(4,0,0,'Wed Jan 07 16:39:34.470940 2004 AUSEST','BIOSPECLOCATION','Bldg 1 Room 2',2,1,NULL,NULL,NULL,0,'ENG'),(5,0,0,'Wed Jan 07 16:39:34.512940 2004 AUSEST','BIOSPECLOCATION','Bldg 2 Room 1',3,1,NULL,NULL,NULL,0,'ENG'),(6,0,0,'Wed Jan 07 16:39:34.554940 2004 AUSEST','BIOSPECLOCATION','Bldg 2 Room 2',4,1,NULL,NULL,NULL,0,'ENG'),(7,0,0,'Wed Jan 07 16:39:34.597940 2004 AUSEST','BIOSPECFLUIDSUBTYPES','Maternal Serum',1,1,NULL,NULL,NULL,0,'ENG'),(8,0,0,'Wed Jan 07 16:39:34.640940 2004 AUSEST','BIOSPECFLUIDSUBTYPES','Cords Blood',2,1,NULL,NULL,NULL,0,'ENG'),(9,0,0,'Wed Jan 07 16:39:34.682940 2004 AUSEST','BIOSPECFLUIDSUBTYPES','Amniotic Fluid',3,1,NULL,NULL,NULL,0,'ENG'),(10,0,0,'Wed Jan 07 16:39:34.726940 2004 AUSEST','BIOSPECTISSUESUBTYPES','Placenta',1,1,NULL,NULL,NULL,0,'ENG'),(11,0,0,'Wed Jan 07 16:39:34.769940 2004 AUSEST','BIOSPECTISSUESUBTYPES','Decidua',2,1,NULL,NULL,NULL,0,'ENG'),(12,0,0,'Wed Jan 07 16:39:34.811940 2004 AUSEST','BIOSPECTISSUESUBTYPES','Umbilical Cord',3,1,NULL,NULL,NULL,0,'ENG'),(13,0,0,'Wed Jan 07 16:39:34.854940 2004 AUSEST','BIOSPECTISSUESUBTYPES','Myometrim',4,1,NULL,NULL,NULL,0,'ENG'),(14,0,0,'Wed Jan 07 16:39:34.896940 2004 AUSEST','BIOSPECTISSUESUBTYPES','Fetal Membrane',5,1,NULL,NULL,NULL,0,'ENG'),(15,0,0,'Wed Jan 07 16:39:34.939940 2004 AUSEST','BIOSPECTISSUESUBTYPES','Membrane',6,1,NULL,NULL,NULL,0,'ENG'),(16,0,0,'Tue Jan 13 09:36:11.759220 2004 EST','BIOSPECTREATMENTS','Formalin Fixed',2,1,NULL,NULL,NULL,0,'ENG'),(17,0,0,'Tue Jan 13 09:36:07.844262 2004 EST','BIOSPECTREATMENTS','Frozen',1,1,NULL,NULL,NULL,0,'ENG'),(18,0,0,'Wed Jan 07 16:39:35.068940 2004 AUSEST','BIOSPECTREATMENTS','Tissue Culture',3,1,NULL,NULL,NULL,0,'ENG'),(19,0,0,'Wed Jan 07 16:39:35.110940 2004 AUSEST','BIOSPECAMNIO','AMA',1,1,NULL,NULL,NULL,0,'ENG'),(20,0,0,'Wed Jan 07 16:39:35.153940 2004 AUSEST','BIOSPECAMNIO','Abnormal 1st Trimester USS',2,1,NULL,NULL,NULL,0,'ENG'),(21,0,0,'Wed Jan 07 16:39:35.195940 2004 AUSEST','BIOSPECAMNIO','Abnormal 2nd Trimester USS',3,1,NULL,NULL,NULL,0,'ENG'),(22,0,0,'Wed Jan 07 16:39:35.238940 2004 AUSEST','BIOSPECAMNIO','Abnormal MSS',4,1,NULL,NULL,NULL,0,'ENG'),(23,0,0,'Wed Jan 07 16:39:35.281940 2004 AUSEST','BIOSPECKARYOTYPE','XX',1,1,NULL,NULL,NULL,0,'ENG'),(24,0,0,'Wed Jan 07 16:39:35.323940 2004 AUSEST','BIOSPECKARYOTYPE','XY',2,1,NULL,NULL,NULL,0,'ENG'),(25,0,0,'Wed Jan 07 16:39:35.438940 2004 AUSEST','BIOSPECKARYOTYPE','Other',3,1,NULL,NULL,NULL,0,'ENG'),(26,0,0,'Wed Jan 07 16:39:35.480940 2004 AUSEST','EXPERIMENT_ATTACHMENT','Notes',2,1,'Research Notes',NULL,NULL,0,'ENG'),(27,0,0,'Wed Jan 07 16:39:35.523940 2004 AUSEST','EXPERIMENT_ATTACHMENT','Data',1,1,'Microarray Data',NULL,NULL,0,'ENG'),(28,0,0,'Wed Jan 07 16:39:35.566940 2004 AUSEST','EXPERIMENT_ATTACHMENT','Slide',3,1,'Graphic Slides',NULL,NULL,0,'ENG'),(29,0,0,'Wed Jan 07 16:39:35.608940 2004 AUSEST','ETHNICITY','White Australian',1,1,'White Australian',NULL,NULL,0,'ENG'),(30,0,0,'Wed Jan 07 16:39:35.652940 2004 AUSEST','ETHNICITY','European',2,1,'European',NULL,NULL,0,'ENG'),(31,0,0,'Wed Jan 07 16:39:35.695940 2004 AUSEST','ETHNICITY','New Zealand',3,1,'New Zealand',NULL,NULL,0,'ENG'),(32,0,0,'Wed Jan 07 16:39:35.737940 2004 AUSEST','ETHNICITY','Vietnamese',4,1,'Vietnamese',NULL,NULL,0,'ENG'),(33,0,0,'Wed Jan 07 16:39:35.780940 2004 AUSEST','ETHNICITY','Cambodian',5,1,'Cambodian',NULL,NULL,0,'ENG'),(34,0,0,'Wed Jan 07 16:39:35.822940 2004 AUSEST','ETHNICITY','Chinese',6,1,'Chinese',NULL,NULL,0,'ENG'),(35,0,0,'Wed Jan 07 16:39:35.864940 2004 AUSEST','ETHNICITY','Hong Kongese',7,1,'Hong Kongese',NULL,NULL,0,'ENG'),(36,0,0,'Wed Jan 07 16:39:35.908940 2004 AUSEST','ETHNICITY','Thailand',8,1,'Thailand',NULL,NULL,0,'ENG'),(37,0,0,'Wed Jan 07 16:39:35.951940 2004 AUSEST','ETHNICITY','Sri Lankan',9,1,'Sri Lankan',NULL,NULL,0,'ENG'),(38,0,0,'Wed Jan 07 16:39:35.993940 2004 AUSEST','ETHNICITY','Indian',10,1,'Indian',NULL,NULL,0,'ENG'),(39,0,0,'Wed Jan 07 16:39:36.037940 2004 AUSEST','ETHNICITY','Pakistani',11,1,'Pakistani',NULL,NULL,0,'ENG'),(40,0,0,'Wed Jan 07 16:39:36.079940 2004 AUSEST','ETHNICITY','Nepalese',12,1,'Nepalese',NULL,NULL,0,'ENG'),(41,0,0,'Wed Jan 07 16:39:36.121940 2004 AUSEST','ETHNICITY','Afghanistan',13,1,'Afghanistan',NULL,NULL,0,'ENG'),(42,0,0,'Wed Jan 07 16:39:36.164940 2004 AUSEST','ETHNICITY','Indonesia',14,1,'Indonesia',NULL,NULL,0,'ENG'),(43,0,0,'Wed Jan 07 16:39:36.206940 2004 AUSEST','ETHNICITY','East Timorese',15,1,'East Timorese',NULL,NULL,0,'ENG'),(44,0,0,'Wed Jan 07 16:39:36.249940 2004 AUSEST','ETHNICITY','Timorese',16,1,'Timorese',NULL,NULL,0,'ENG'),(45,0,0,'Wed Jan 07 16:39:36.293940 2004 AUSEST','ETHNICITY','Middle Eastern',17,1,'Middle Eastern',NULL,NULL,0,'ENG'),(46,0,0,'Wed Jan 07 16:39:36.370940 2004 AUSEST','ETHNICITY','Israel',18,1,'Israel',NULL,NULL,0,'ENG'),(47,0,0,'Wed Jan 07 16:39:36.448940 2004 AUSEST','ETHNICITY','Egyptian',19,1,'Egyptian',NULL,NULL,0,'ENG'),(48,0,0,'Wed Jan 07 16:39:36.492940 2004 AUSEST','ETHNICITY','TURKISH',20,1,'TURKISH',NULL,NULL,0,'ENG'),(49,0,0,'Wed Jan 07 16:39:36.535940 2004 AUSEST','ETHNICITY','South/Central American',21,1,'South/Central American',NULL,NULL,0,'ENG'),(50,0,0,'Wed Jan 07 16:39:36.576940 2004 AUSEST','ETHNICITY','Philippino',22,1,'Philippino',NULL,NULL,0,'ENG'),(51,0,0,'Wed Jan 07 16:39:36.620940 2004 AUSEST','ETHNICITY','Korean',23,1,'Korean',NULL,NULL,0,'ENG'),(52,0,0,'Wed Jan 07 16:39:36.663940 2004 AUSEST','ETHNICITY','Japanese',24,1,'Japanese',NULL,NULL,0,'ENG'),(53,0,0,'Wed Jan 07 16:39:36.705940 2004 AUSEST','ETHNICITY','Aboriginal',25,1,'Aboriginal',NULL,NULL,0,'ENG'),(54,0,0,'Wed Jan 07 16:39:36.748940 2004 AUSEST','ETHNICITY','Maori',26,1,'Maori',NULL,NULL,0,'ENG'),(55,0,0,'Wed Jan 07 16:39:36.791940 2004 AUSEST','ETHNICITY','Torres Strait Islander',27,1,'Torres Strait Islander',NULL,NULL,0,'ENG'),(56,0,0,'Wed Jan 07 16:39:36.833940 2004 AUSEST','ETHNICITY','Cook Islander',28,1,'Cook Islander',NULL,NULL,0,'ENG'),(57,0,0,'Wed Jan 07 16:39:36.876940 2004 AUSEST','ETHNICITY','Samoa',29,1,'Samoa',NULL,NULL,0,'ENG'),(58,0,0,'Wed Jan 07 16:39:36.919940 2004 AUSEST','ETHNICITY','ASIAN',30,1,'ASIAN',NULL,NULL,0,'ENG'),(59,0,0,'Wed Jan 07 16:39:36.962940 2004 AUSEST','ETHNICITY','AFRICAN',31,1,'AFRICAN',NULL,NULL,0,'ENG'),(60,0,0,'Wed Jan 07 16:39:37.005940 2004 AUSEST','ETHNICITY','STH. AFRICAN',32,1,'STH. AFRICAN',NULL,NULL,0,'ENG'),(61,0,0,'Wed Jan 07 16:39:37.047940 2004 AUSEST','ETHNICITY','Mauritus',33,1,'Mauritus',NULL,NULL,0,'ENG'),(62,0,0,'Wed Jan 07 16:39:37.091940 2004 AUSEST','ETHNICITY','Laos',34,1,'Laos',NULL,NULL,0,'ENG'),(63,0,0,'Wed Jan 07 16:39:37.132940 2004 AUSEST','ETHNICITY','MALAYSIA',35,1,'MALAYSIA',NULL,NULL,0,'ENG'),(64,0,0,'Wed Jan 07 16:39:37.175940 2004 AUSEST','ETHNICITY','Taiwan',36,1,'Taiwan',NULL,NULL,0,'ENG'),(65,0,0,'Wed Jan 07 16:39:37.218940 2004 AUSEST','ETHNICITY','Fiji',37,1,'Fiji',NULL,NULL,0,'ENG'),(66,0,0,'Wed Jan 07 16:39:37.261940 2004 AUSEST','ETHNICITY','Tahiti',38,1,'Tahiti',NULL,NULL,0,'ENG'),(67,0,0,'Wed Jan 07 16:39:37.303940 2004 AUSEST','ETHNICITY','Other',39,1,'Other',NULL,NULL,0,'ENG'),(68,0,0,'Wed Jan 07 16:39:37.367940 2004 AUSEST','ETHNICITY','Poland',40,1,'Poland',NULL,NULL,0,'ENG'),(69,0,0,'Wed Jan 07 16:39:37.460940 2004 AUSEST','ETHNICITY','Armania',41,1,'Armania',NULL,NULL,0,'ENG'),(70,0,0,'Wed Jan 07 16:39:37.502940 2004 AUSEST','ETHNICITY','Jordon',42,1,'Jordon',NULL,NULL,0,'ENG'),(71,0,0,'Wed Jan 07 16:39:37.544940 2004 AUSEST','ETHNICITY','Greek',43,1,'Greek',NULL,NULL,0,'ENG'),(72,0,0,'Wed Jan 07 16:39:37.588940 2004 AUSEST','ETHNICITY','Canadian',44,1,'Canadian',NULL,NULL,0,'ENG'),(73,0,0,'Wed Jan 07 16:39:37.631940 2004 AUSEST','ETHNICITY','USA',45,1,'USA',NULL,NULL,0,'ENG'),(74,0,0,'Wed Jan 07 16:39:37.673940 2004 AUSEST','ETHNICITY','Ireland',46,1,'Ireland',NULL,NULL,0,'ENG'),(75,0,0,'Wed Jan 07 16:39:37.717940 2004 AUSEST','ETHNICITY','Burma',47,1,'Burma',NULL,NULL,0,'ENG'),(76,0,0,'Wed Jan 07 16:39:37.759940 2004 AUSEST','ETHNICITY','Ethiopian',48,1,'Ethiopian',NULL,NULL,0,'ENG'),(77,0,0,'Wed Jan 07 16:39:37.802940 2004 AUSEST','ETHNICITY','Iraq',49,1,'Iraq',NULL,NULL,0,'ENG'),(78,0,0,'Wed Jan 07 16:39:37.844940 2004 AUSEST','ETHNICITY','Macedonia',50,1,'Macedonia',NULL,NULL,0,'ENG'),(79,0,0,'Wed Jan 07 16:39:37.887940 2004 AUSEST','ETHNICITY','Lebanon',51,1,'Lebanon',NULL,NULL,0,'ENG'),(80,0,0,'Wed Jan 07 16:39:37.930940 2004 AUSEST','ETHNICITY','Singapore',52,1,'Singapore',NULL,NULL,0,'ENG'),(81,0,0,'Wed Jan 07 16:39:37.973940 2004 AUSEST','ETHNICITY','Chile',53,1,'Chile',NULL,NULL,0,'ENG'),(82,0,0,'Wed Jan 07 16:39:38.015940 2004 AUSEST','ETHNICITY','Slavic',54,1,'Slavic',NULL,NULL,0,'ENG'),(83,0,0,'Wed Jan 07 16:39:38.059940 2004 AUSEST','ETHNICITY','Brunei',55,1,'Brunei',NULL,NULL,0,'ENG'),(84,0,0,'Wed Jan 07 16:39:38.101940 2004 AUSEST','ETHNICITY','United Kingdom',56,1,'United Kingdom',NULL,NULL,0,'ENG'),(85,0,0,'Wed Jan 07 16:39:38.143940 2004 AUSEST','ETHNICITY','Yugoslavia',57,1,'Yugoslavia',NULL,NULL,0,'ENG'),(86,0,0,'Wed Jan 07 16:39:38.186940 2004 AUSEST','ETHNICITY','Bosnia',58,1,'Bosnia',NULL,NULL,0,'ENG'),(87,0,0,'Wed Jan 07 16:39:38.229940 2004 AUSEST','ETHNICITY','West Indies',59,1,'West Indies',NULL,NULL,0,'ENG'),(88,0,0,'Wed Jan 07 16:39:38.271940 2004 AUSEST','ETHNICITY','Kuwait',60,1,'Kuwait',NULL,NULL,0,'ENG'),(89,0,0,'Wed Jan 07 16:39:38.314940 2004 AUSEST','ETHNICITY','Algeria',61,1,'Algeria',NULL,NULL,0,'ENG'),(90,0,0,'Wed Jan 07 16:39:38.431940 2004 AUSEST','ETHNICITY','Singapore',62,1,'Singapore',NULL,NULL,0,'ENG'),(91,0,0,'Wed Jan 07 16:39:38.470940 2004 AUSEST','ETHNICITY','Russia',63,1,'Russia',NULL,NULL,0,'ENG'),(92,0,0,'Wed Jan 07 16:39:38.513940 2004 AUSEST','ETHNICITY','Argentina',64,1,'Argentina',NULL,NULL,0,'ENG'),(93,0,0,'Wed Jan 07 16:39:38.556940 2004 AUSEST','ETHNICITY','Ukraine',65,1,'Ukraine',NULL,NULL,0,'ENG'),(94,0,0,'Wed Jan 07 16:39:38.598940 2004 AUSEST','ETHNICITY','Syria',66,1,'Syria',NULL,NULL,0,'ENG'),(95,0,0,'Wed Jan 07 16:39:38.641940 2004 AUSEST','ETHNICITY','Croatia',67,1,'Croatia',NULL,NULL,0,'ENG'),(96,0,0,'Wed Jan 07 16:39:38.683940 2004 AUSEST','ETHNICITY','German',68,1,'German',NULL,NULL,0,'ENG'),(97,0,0,'Wed Jan 07 16:39:38.726940 2004 AUSEST','ETHNICITY','Scottish',69,1,'Scottish',NULL,NULL,0,'ENG'),(98,0,0,'Wed Jan 07 16:39:38.769940 2004 AUSEST','ETHNICITY','Italian',70,1,'Italian',NULL,NULL,0,'ENG'),(99,0,0,'Wed Jan 07 16:39:38.813940 2004 AUSEST','ETHNICITY','Swiss',71,1,'Swiss',NULL,NULL,0,'ENG'),(100,0,0,'Wed Jan 07 16:39:38.855940 2004 AUSEST','ETHNICITY','Dutch',72,1,'Dutch',NULL,NULL,0,'ENG'),(101,0,0,'Wed Jan 07 16:39:38.898940 2004 AUSEST','ETHNICITY','Maltese',73,1,'Maltese',NULL,NULL,0,'ENG'),(102,0,0,'Wed Jan 14 18:37:41.378316 2004 EST','STATE','Victoria',2,1,'Victoria',NULL,NULL,0,'ENG'),(103,0,0,'Wed Jan 14 18:37:39.392095 2004 EST','STATE','New South Wales',1,1,'New South Wales',NULL,NULL,0,'ENG'),(104,0,0,'Wed Jan 07 16:39:39.026940 2004 AUSEST','STATE','Queensland',3,1,'Queensland',NULL,NULL,0,'ENG'),(105,0,0,'Wed Jan 07 16:39:39.068940 2004 AUSEST','STATE','Northern Territory',4,1,'Northern Territory',NULL,NULL,0,'ENG'),(106,0,0,'Wed Jan 07 16:39:39.110940 2004 AUSEST','STATE','South Australia',5,1,'South Australia',NULL,NULL,0,'ENG'),(107,0,0,'Wed Jan 07 16:39:39.154940 2004 AUSEST','STATE','Tasmania',6,1,'Tasmania',NULL,NULL,0,'ENG'),(108,0,0,'Wed Jan 07 16:39:39.197940 2004 AUSEST','STATE','Western Australia',7,1,'Western Australia',NULL,NULL,0,'ENG'),(109,0,0,'Wed Jan 07 16:39:39.239940 2004 AUSEST','STATE','Australian Capital Territory',8,1,'Australian Capital Territory',NULL,NULL,0,'ENG'),(110,0,0,'Wed Jan 07 16:39:39.282940 2004 AUSEST','COUNTRY','Australia',1,1,'Australia',NULL,NULL,0,'ENG'),(111,0,0,'Wed Jan 07 16:39:39.325940 2004 AUSEST','STATUS','New',1,1,'Active',NULL,NULL,0,'ENG'),(112,0,0,'Wed Jan 07 16:39:39.407940 2004 AUSEST','STATUS','Archived',2,1,'Inactive',NULL,NULL,0,'ENG'),(114,0,0,'Wed Jan 07 16:39:39.481940 2004 AUSEST','CONTACTPLACE','Home',1,1,'Home',NULL,NULL,0,'ENG'),(115,0,0,'Wed Jan 07 16:39:39.525940 2004 AUSEST','CONTACTPLACE','Work',2,1,'Work',NULL,NULL,0,'ENG'),(116,0,0,'Wed Jan 07 16:39:39.567940 2004 AUSEST','YESNOLIST','Yes',1,1,'Yes',NULL,NULL,0,'ENG'),(117,0,0,'Wed Jan 07 16:39:39.610940 2004 AUSEST','YESNOLIST','No',2,1,'No',NULL,NULL,0,'ENG'),(118,0,0,'Wed Jan 07 16:39:39.653940 2004 AUSEST','PREP.OUTCOME','livebirth',1,1,'livebirth',NULL,NULL,0,'ENG'),(119,0,0,'Wed Jan 07 16:39:39.694940 2004 AUSEST','PREP.OUTCOME','stillbirth',2,1,'stillbirth',NULL,NULL,0,'ENG'),(120,0,0,'Wed Jan 07 16:39:39.737940 2004 AUSEST','PREP.OUTCOME','miscarriage',3,1,'miscarriage',NULL,NULL,0,'ENG'),(121,0,0,'Wed Jan 07 16:39:39.780940 2004 AUSEST','PREP.OUTCOME','top',4,1,'top',NULL,NULL,0,'ENG'),(122,0,0,'Wed Jan 07 16:39:39.823940 2004 AUSEST','DELIVERY MODE','nvd',1,1,'nvd',NULL,NULL,0,'ENG'),(123,0,0,'Wed Jan 07 16:39:39.866940 2004 AUSEST','DELIVERY MODE','op vag deliv',2,1,'op vag deliv',NULL,NULL,0,'ENG'),(124,0,0,'Wed Jan 07 16:39:39.908940 2004 AUSEST','DELIVERY MODE','vag breech',3,1,'vag breech',NULL,NULL,0,'ENG'),(125,0,0,'Wed Jan 07 16:39:39.951940 2004 AUSEST','DELIVERY MODE','-',4,1,'-',NULL,NULL,0,'ENG'),(126,0,0,'Wed Jan 07 16:39:39.993940 2004 AUSEST','DELIVERY MODE','elec C/S',5,1,'elec C/S',NULL,NULL,0,'ENG'),(127,0,0,'Wed Jan 07 16:39:40.036940 2004 AUSEST','DELIVERY MODE','emerg C/S',6,1,'emerg C/S',NULL,NULL,0,'ENG'),(132,0,0,'Wed Jan 07 16:39:40.250940 2004 AUSEST','ETHNICITY','Caucasian',1,1,'Caucasian',NULL,NULL,0,'ENG'),(133,0,0,'Wed Jan 07 16:39:40.292940 2004 AUSEST','ETHNICITY','SE Asian',2,1,'SE Asian',NULL,NULL,0,'ENG'),(134,0,0,'Wed Jan 07 16:39:40.392940 2004 AUSEST','ETHNICITY','Indo-Pakistani',3,1,'Indo-Pakistani',NULL,NULL,0,'ENG'),(135,0,0,'Wed Jan 07 16:39:40.435940 2004 AUSEST','ETHNICITY','Middle Eastern',4,1,'Middle Eastern',NULL,NULL,0,'ENG'),(136,0,0,'Wed Jan 07 16:39:40.478940 2004 AUSEST','ETHNICITY','African',5,1,'African',NULL,NULL,0,'ENG'),(137,0,0,'Wed Jan 07 16:39:40.521940 2004 AUSEST','ETHNICITY','South/Central American',6,1,'South/Central American',NULL,NULL,0,'ENG'),(138,0,0,'Wed Jan 07 16:39:40.563940 2004 AUSEST','ETHNICITY','Aboriginal',7,1,'Aboriginal',NULL,NULL,0,'ENG'),(139,0,0,'Wed Jan 07 16:39:40.606940 2004 AUSEST','ETHNICITY','Islander',8,1,'Islander',NULL,NULL,0,'ENG'),(140,0,0,'Wed Jan 07 16:39:40.649940 2004 AUSEST','NUMBER OF FETUS','singleton',1,1,'singleton',NULL,NULL,0,'ENG'),(141,0,0,'Wed Jan 07 16:39:40.692940 2004 AUSEST','NUMBER OF FETUS','twins',2,1,'twins',NULL,NULL,0,'ENG'),(142,0,0,'Wed Jan 07 16:39:40.735940 2004 AUSEST','NUMBER OF FETUS','triplets',3,1,'triplets',NULL,NULL,0,'ENG'),(143,0,0,'Wed Jan 07 16:39:40.777940 2004 AUSEST','DIABETES MANAGEMENT','Diet alone',1,1,'Diet alone',NULL,NULL,0,'ENG'),(144,0,0,'Wed Jan 07 16:39:40.820940 2004 AUSEST','DIABETES MANAGEMENT','Insulin',2,1,'Insulin',NULL,NULL,0,'ENG'),(145,0,0,'Wed Jan 07 16:39:40.862940 2004 AUSEST','PET TREATMENT','none',1,1,'none',NULL,NULL,0,'ENG'),(146,0,0,'Wed Jan 07 16:39:40.905940 2004 AUSEST','PET TREATMENT','single agent',2,1,'single agent',NULL,NULL,0,'ENG'),(147,0,0,'Wed Jan 07 16:39:40.947940 2004 AUSEST','PET TREATMENT','double agent',3,1,'double agent',NULL,NULL,0,'ENG'),(148,0,0,'Wed Jan 07 16:39:40.990940 2004 AUSEST','PET TREATMENT','triple agent',4,1,'triple agent',NULL,NULL,0,'ENG'),(149,0,0,'Wed Jan 07 16:39:41.034940 2004 AUSEST','DELIVERY SITES','Monash Medical Centre - Clayton',1,1,'Monash Medical Centre - Clayton',NULL,NULL,0,'ENG'),(150,0,0,'Wed Jan 07 16:39:41.076940 2004 AUSEST','DELIVERY SITES','Monash Medical Centre - Moorabbin',2,1,'Monash Medical Centre - Moorabbin',NULL,NULL,0,'ENG'),(151,0,0,'Wed Jan 07 16:39:41.119940 2004 AUSEST','DELIVERY SITES','Monash Medical Centre - Birth Centre',3,1,'Monash Medical Centre - Birth Centre',NULL,NULL,0,'ENG'),(152,0,0,'Wed Jan 07 16:39:41.161940 2004 AUSEST','DELIVERY SITES','other',4,1,'other',NULL,NULL,0,'ENG'),(153,0,0,'Wed Jan 07 16:39:41.204940 2004 AUSEST','ALCOHOL CONSUMPTION','Nil',1,1,'Nil',NULL,NULL,0,'ENG'),(154,0,0,'Wed Jan 07 16:39:41.247940 2004 AUSEST','ALCOHOL CONSUMPTION','<2 drinks/day',2,1,'<2 drinks/day',NULL,NULL,0,'ENG'),(155,0,0,'Wed Jan 07 16:39:41.289940 2004 AUSEST','ALCOHOL CONSUMPTION','>2 drinks/day',3,1,'>2 drinks/day',NULL,NULL,0,'ENG'),(156,0,0,'Wed Jan 07 16:39:41.359940 2004 AUSEST','DIABETES','None',1,1,'None',NULL,NULL,0,'ENG'),(157,0,0,'Wed Jan 07 16:39:41.389940 2004 AUSEST','DIABETES','NIDDM',2,1,'NIDDM',NULL,NULL,0,'ENG'),(158,0,0,'Wed Jan 07 16:39:41.432940 2004 AUSEST','DIABETES','IDDM',3,1,'IDDM',NULL,NULL,0,'ENG'),(159,0,0,'Wed Jan 07 16:39:41.475940 2004 AUSEST','PROTEINURIA','none',1,1,'none',NULL,NULL,0,'ENG'),(160,0,0,'Wed Jan 07 16:39:41.517940 2004 AUSEST','PROTEINURIA','trace',2,1,'trace',NULL,NULL,0,'ENG'),(161,0,0,'Wed Jan 07 16:39:41.560940 2004 AUSEST','PROTEINURIA','+',3,1,'+',NULL,NULL,0,'ENG'),(162,0,0,'Wed Jan 07 16:39:41.602940 2004 AUSEST','PROTEINURIA','++',4,1,'++',NULL,NULL,0,'ENG'),(163,0,0,'Wed Jan 07 16:39:41.647940 2004 AUSEST','PROTEINURIA','+++',5,1,'+++',NULL,NULL,0,'ENG'),(164,0,0,'Wed Jan 07 16:39:41.689940 2004 AUSEST','PROTEINURIA','++++',6,1,'++++',NULL,NULL,0,'ENG'),(165,0,0,'Wed Jan 07 16:39:41.733940 2004 AUSEST','RENAL DISEASE','No',1,1,'No',NULL,NULL,0,'ENG'),(166,0,0,'Wed Jan 07 16:39:41.775940 2004 AUSEST','RENAL DISEASE','Yes, please specify',2,1,'Yes, please specify',NULL,NULL,0,'ENG'),(167,0,0,'Wed Jan 07 16:39:41.817940 2004 AUSEST','RENAL DISEASE','IgA',3,1,'IgA',NULL,NULL,0,'ENG'),(168,0,0,'Wed Jan 07 16:39:41.860940 2004 AUSEST','FDIU CAUSES','unknown',1,1,'unknown',NULL,NULL,0,'ENG'),(169,0,0,'Wed Jan 07 16:39:41.903940 2004 AUSEST','FDIU CAUSES','fetal abnormality',2,1,'fetal abnormality',NULL,NULL,0,'ENG'),(170,0,0,'Wed Jan 07 16:39:41.945940 2004 AUSEST','FDIU CAUSES','abruption',3,1,'abruption',NULL,NULL,0,'ENG'),(171,0,0,'Wed Jan 07 16:39:41.988940 2004 AUSEST','FDIU CAUSES','IUGR',4,1,'IUGR',NULL,NULL,0,'ENG'),(172,0,0,'Wed Jan 07 16:39:42.030940 2004 AUSEST','FDIU CAUSES','chorioamnioitis',5,1,'chorioamnioitis',NULL,NULL,0,'ENG'),(173,0,0,'Wed Jan 07 16:39:42.073940 2004 AUSEST','INDUCTION METHODS','Prostin gel',1,1,'Prostin gel',NULL,NULL,0,'ENG'),(174,0,0,'Wed Jan 07 16:39:42.115940 2004 AUSEST','INDUCTION METHODS','ARM',2,1,'ARM',NULL,NULL,0,'ENG'),(175,0,0,'Wed Jan 07 16:39:42.158940 2004 AUSEST','INDUCTION METHODS','Syntocinon Infusion',3,1,'Syntocinon Infusion',NULL,NULL,0,'ENG'),(176,0,0,'Wed Jan 07 16:39:42.201940 2004 AUSEST','INDUCTION METHODS','Balloon ripening device',4,1,'Balloon ripening device',NULL,NULL,0,'ENG'),(177,0,0,'Wed Jan 07 16:39:42.243940 2004 AUSEST','TYPE OF CARE','standard care',1,1,'standard care',NULL,NULL,0,'ENG'),(178,0,0,'Wed Jan 07 16:39:42.287940 2004 AUSEST','TYPE OF CARE','shared care',2,1,'shared care',NULL,NULL,0,'ENG'),(179,0,0,'Wed Jan 07 16:39:42.331940 2004 AUSEST','TYPE OF CARE','CPC',3,1,'CPC',NULL,NULL,0,'ENG'),(180,0,0,'Wed Jan 07 16:39:42.400940 2004 AUSEST','TYPE OF CARE','working womens',4,1,'working womens',NULL,NULL,0,'ENG'),(181,0,0,'Wed Jan 07 16:39:42.444940 2004 AUSEST','TYPE OF CARE','young womens',5,1,'young womens',NULL,NULL,0,'ENG'),(182,0,0,'Wed Jan 07 16:39:42.486940 2004 AUSEST','DELIVERY MODE','nvd',1,1,'nvd',NULL,NULL,0,'ENG'),(183,0,0,'Wed Jan 07 16:39:42.528940 2004 AUSEST','DELIVERY MODE','op vag deliv',2,1,'op vag deliv',NULL,NULL,0,'ENG'),(184,0,0,'Wed Jan 07 16:39:42.573940 2004 AUSEST','DELIVERY MODE','-',3,1,'-',NULL,NULL,0,'ENG'),(185,0,0,'Wed Jan 07 16:39:42.615940 2004 AUSEST','DELIVERY MODE','elec C/S',4,1,'elec C/S',NULL,NULL,0,'ENG'),(186,0,0,'Wed Jan 07 16:39:42.657940 2004 AUSEST','DELIVERY MODE','emerg C/S',5,1,'emerg C/S',NULL,NULL,0,'ENG'),(187,0,0,'Wed Jan 07 16:39:42.701940 2004 AUSEST','DELIVERY MODE','-',6,1,'-',NULL,NULL,0,'ENG'),(188,0,0,'Wed Jan 07 16:39:42.742940 2004 AUSEST','DELIVERY MODE','STOP',7,1,'STOP',NULL,NULL,0,'ENG'),(189,0,0,'Wed Jan 07 16:39:42.784940 2004 AUSEST','DELIVERY MODE','D and E',8,1,'D and E',NULL,NULL,0,'ENG'),(190,0,0,'Wed Jan 07 16:39:42.827940 2004 AUSEST','INDICATION FOR COLLECTION','AMA',1,1,'AMA',NULL,NULL,0,'ENG'),(191,0,0,'Wed Jan 07 16:39:42.870940 2004 AUSEST','INDICATION FOR COLLECTION','abnormal 1st trimester USS',2,1,'abnormal 1st trimester USS',NULL,NULL,0,'ENG'),(192,0,0,'Wed Jan 07 16:39:42.913940 2004 AUSEST','INDICATION FOR COLLECTION','abnormal 2nd trimester USS',3,1,'abnormal 2nd trimester USS',NULL,NULL,0,'ENG'),(193,0,0,'Wed Jan 07 16:39:42.958940 2004 AUSEST','INDICATION FOR COLLECTION','abnormal MSS',4,1,'abnormal MSS',NULL,NULL,0,'ENG'),(194,0,0,'Wed Jan 07 16:39:42.999940 2004 AUSEST','INDICATION FOR COLLECTION','rhesus',5,1,'rhesus',NULL,NULL,0,'ENG'),(195,0,0,'Wed Jan 07 16:39:43.041940 2004 AUSEST','INDICATION FOR COLLECTION','past history',6,1,'past history',NULL,NULL,0,'ENG'),(196,0,0,'Wed Jan 07 16:39:43.085940 2004 AUSEST','INDICATION FOR COLLECTION','anxiety',7,1,'anxiety',NULL,NULL,0,'ENG'),(197,0,0,'Wed Jan 07 16:39:43.127940 2004 AUSEST','INDICATION FOR COLLECTION','other',8,1,'other',NULL,NULL,0,'ENG'),(198,0,0,'Wed Jan 07 16:39:43.169940 2004 AUSEST','KARYOTYPE','XX',1,1,'XX',NULL,NULL,0,'ENG'),(199,0,0,'Wed Jan 07 16:39:43.214940 2004 AUSEST','KARYOTYPE','XY',2,1,'XY',NULL,NULL,0,'ENG'),(200,0,0,'Wed Jan 07 16:39:43.255940 2004 AUSEST','KARYOTYPE','other,specify',3,1,'other,specify',NULL,NULL,0,'ENG'),(201,0,0,'Wed Jan 07 16:39:43.298940 2004 AUSEST','TITLE','Mr',1,1,'Mr',NULL,NULL,0,'ENG'),(202,0,0,'Wed Jan 07 16:39:43.374940 2004 AUSEST','TITLE','Mrs',2,1,'Mrs',NULL,NULL,0,'ENG'),(203,0,0,'Wed Jan 07 16:39:43.411940 2004 AUSEST','TITLE','Ms',3,1,'Ms',NULL,NULL,0,'ENG'),(204,0,0,'Wed Jan 07 16:39:43.454940 2004 AUSEST','TITLE','Dr',4,1,'Dr',NULL,NULL,0,'ENG'),(206,0,0,'Wed Jan 07 16:39:43.540940 2004 AUSEST','TRAY_LABEL','Numeric',1,1,'Numerical label',NULL,NULL,0,'ENG'),(207,0,0,'Wed Jan 07 16:39:43.582940 2004 AUSEST','TRAY_LABEL','Alphabet',2,1,'Alphabet label',NULL,NULL,0,'ENG'),(208,0,0,'Wed Jan 07 16:39:43.611940 2004 AUSEST','INVTANKSTATUS','In use',1,1,'Be using',NULL,NULL,0,'ENG'),(209,0,0,'Wed Jan 07 16:39:43.654940 2004 AUSEST','INVTANKSTATUS','Not in use',2,1,'No longer use',NULL,NULL,0,'ENG'),(210,0,0,'Wed Jan 07 16:39:43.696940 2004 AUSEST','MONTH',NULL,10,1,'None',NULL,NULL,0,'ENG'),(211,0,0,'Wed Jan 07 16:39:43.725940 2004 AUSEST','MONTH','01',11,1,'January',NULL,NULL,0,'ENG'),(212,0,0,'Wed Jan 07 16:39:43.767940 2004 AUSEST','MONTH','02',12,1,'February',NULL,NULL,0,'ENG'),(213,0,0,'Wed Jan 07 16:39:43.811940 2004 AUSEST','MONTH','03',13,1,'March',NULL,NULL,0,'ENG'),(214,0,0,'Wed Jan 07 16:39:43.853940 2004 AUSEST','MONTH','04',14,1,'April',NULL,NULL,0,'ENG'),(215,0,0,'Wed Jan 07 16:39:43.896940 2004 AUSEST','MONTH','05',15,1,'May',NULL,NULL,0,'ENG'),(216,0,0,'Wed Jan 07 16:39:43.924940 2004 AUSEST','MONTH','06',16,1,'June',NULL,NULL,0,'ENG'),(217,0,0,'Wed Jan 07 16:39:43.971940 2004 AUSEST','MONTH','07',17,1,'July',NULL,NULL,0,'ENG'),(218,0,0,'Wed Jan 07 16:39:44.009940 2004 AUSEST','MONTH','08',18,1,'August',NULL,NULL,0,'ENG'),(219,0,0,'Wed Jan 07 16:39:44.053940 2004 AUSEST','MONTH','09',19,1,'September',NULL,NULL,0,'ENG'),(220,0,0,'Wed Jan 07 16:39:44.095940 2004 AUSEST','MONTH','10',20,1,'October',NULL,NULL,0,'ENG'),(221,0,0,'Wed Jan 07 16:39:44.140940 2004 AUSEST','MONTH','11',21,1,'November',NULL,NULL,0,'ENG'),(222,0,0,'Wed Jan 07 16:39:44.184940 2004 AUSEST','MONTH','12',22,1,'December',NULL,NULL,0,'ENG'),(223,0,0,'Wed Jan 07 16:39:44.223940 2004 AUSEST','DAY',NULL,10,1,'0',NULL,NULL,0,'ENG'),(224,0,0,'Wed Jan 07 16:39:44.252940 2004 AUSEST','DAY','01',11,1,'1',NULL,NULL,0,'ENG'),(225,0,0,'Wed Jan 07 16:39:44.294940 2004 AUSEST','DAY','02',12,1,'2',NULL,NULL,0,'ENG'),(226,0,0,'Wed Jan 07 16:39:44.323940 2004 AUSEST','DAY','03',13,1,'3',NULL,NULL,0,'ENG'),(227,0,0,'Wed Jan 07 16:39:44.409940 2004 AUSEST','DAY','04',14,1,'4',NULL,NULL,0,'ENG'),(228,0,0,'Wed Jan 07 16:39:44.439940 2004 AUSEST','DAY','05',15,1,'5',NULL,NULL,0,'ENG'),(229,0,0,'Wed Jan 07 16:39:44.480940 2004 AUSEST','DAY','06',16,1,'6',NULL,NULL,0,'ENG'),(230,0,0,'Wed Jan 07 16:39:44.523940 2004 AUSEST','DAY','07',17,1,'7',NULL,NULL,0,'ENG'),(231,0,0,'Wed Jan 07 16:39:44.565940 2004 AUSEST','DAY','08',18,1,'8',NULL,NULL,0,'ENG'),(232,0,0,'Wed Jan 07 16:39:44.607940 2004 AUSEST','DAY','09',19,1,'9',NULL,NULL,0,'ENG'),(233,0,0,'Wed Jan 07 16:39:44.637940 2004 AUSEST','DAY','10',20,1,'10',NULL,NULL,0,'ENG'),(234,0,0,'Wed Jan 07 16:39:44.678940 2004 AUSEST','DAY','11',21,1,'11',NULL,NULL,0,'ENG'),(235,0,0,'Wed Jan 07 16:39:44.707940 2004 AUSEST','DAY','12',22,1,'12',NULL,NULL,0,'ENG'),(236,0,0,'Wed Jan 07 16:39:44.762940 2004 AUSEST','DAY','13',23,1,'13',NULL,NULL,0,'ENG'),(237,0,0,'Wed Jan 07 16:39:44.793940 2004 AUSEST','DAY','14',24,1,'14',NULL,NULL,0,'ENG'),(238,0,0,'Wed Jan 07 16:39:44.826940 2004 AUSEST','DAY','15',25,1,'15',NULL,NULL,0,'ENG'),(239,0,0,'Wed Jan 07 16:39:44.864940 2004 AUSEST','DAY','16',26,1,'16',NULL,NULL,0,'ENG'),(240,0,0,'Wed Jan 07 16:39:44.892940 2004 AUSEST','DAY','17',27,1,'17',NULL,NULL,0,'ENG'),(241,0,0,'Wed Jan 07 16:39:44.924940 2004 AUSEST','DAY','18',28,1,'18',NULL,NULL,0,'ENG'),(242,0,0,'Wed Jan 07 16:39:44.977940 2004 AUSEST','DAY','19',29,1,'19',NULL,NULL,0,'ENG'),(243,0,0,'Wed Jan 07 16:39:45.111940 2004 AUSEST','DAY','20',30,1,'20',NULL,NULL,0,'ENG'),(244,0,0,'Wed Jan 07 16:39:45.177940 2004 AUSEST','DAY','21',31,1,'21',NULL,NULL,0,'ENG'),(245,0,0,'Wed Jan 07 16:39:45.206940 2004 AUSEST','DAY','22',32,1,'22',NULL,NULL,0,'ENG'),(246,0,0,'Wed Jan 07 16:39:45.235940 2004 AUSEST','DAY','23',33,1,'23',NULL,NULL,0,'ENG'),(247,0,0,'Wed Jan 07 16:39:45.277940 2004 AUSEST','DAY','24',34,1,'24',NULL,NULL,0,'ENG'),(248,0,0,'Wed Jan 07 16:39:45.320940 2004 AUSEST','DAY','25',35,1,'25',NULL,NULL,0,'ENG'),(249,0,0,'Wed Jan 07 16:39:45.391940 2004 AUSEST','DAY','26',36,1,'26',NULL,NULL,0,'ENG'),(250,0,0,'Wed Jan 07 16:39:45.448940 2004 AUSEST','DAY','27',37,1,'27',NULL,NULL,0,'ENG'),(251,0,0,'Wed Jan 07 16:39:45.476940 2004 AUSEST','DAY','28',38,1,'28',NULL,NULL,0,'ENG'),(252,0,0,'Wed Jan 07 16:39:45.505940 2004 AUSEST','DAY','29',39,1,'29',NULL,NULL,0,'ENG'),(253,0,0,'Wed Jan 07 16:39:45.538940 2004 AUSEST','DAY','30',40,1,'30',NULL,NULL,0,'ENG'),(254,0,0,'Wed Jan 07 16:39:45.576940 2004 AUSEST','DAY','31',41,1,'31',NULL,NULL,0,'ENG'),(255,0,0,'Wed Jan 07 16:39:45.604940 2004 AUSEST','HOUR','1',11,1,'1',NULL,NULL,0,'ENG'),(256,0,0,'Wed Jan 07 16:39:45.633940 2004 AUSEST','HOUR','2',12,1,'2',NULL,NULL,0,'ENG'),(257,0,0,'Wed Jan 07 16:39:45.663940 2004 AUSEST','HOUR','3',13,1,'3',NULL,NULL,0,'ENG'),(258,0,0,'Wed Jan 07 16:39:45.705940 2004 AUSEST','HOUR','4',14,1,'4',NULL,NULL,0,'ENG'),(259,0,0,'Wed Jan 07 16:39:45.733940 2004 AUSEST','HOUR','5',15,1,'5',NULL,NULL,0,'ENG'),(260,0,0,'Wed Jan 07 16:39:45.761940 2004 AUSEST','HOUR','6',16,1,'6',NULL,NULL,0,'ENG'),(261,0,0,'Wed Jan 07 16:39:45.790940 2004 AUSEST','HOUR','7',17,1,'7',NULL,NULL,0,'ENG'),(262,0,0,'Wed Jan 07 16:39:45.818940 2004 AUSEST','HOUR','8',18,1,'8',NULL,NULL,0,'ENG'),(263,0,0,'Wed Jan 07 16:39:45.846940 2004 AUSEST','HOUR','9',19,1,'9',NULL,NULL,0,'ENG'),(264,0,0,'Wed Jan 07 16:39:45.875940 2004 AUSEST','HOUR','10',20,1,'10',NULL,NULL,0,'ENG'),(265,0,0,'Wed Jan 07 16:39:45.903940 2004 AUSEST','HOUR','11',21,1,'11',NULL,NULL,0,'ENG'),(266,0,0,'Wed Jan 07 16:39:45.932940 2004 AUSEST','HOUR','12',22,1,'12',NULL,NULL,0,'ENG'),(267,0,0,'Wed Jan 07 16:39:45.960940 2004 AUSEST','MINUTE','00',10,1,'00',NULL,NULL,0,'ENG'),(268,0,0,'Wed Jan 07 16:39:45.988940 2004 AUSEST','MINUTE','01',11,1,'01',NULL,NULL,0,'ENG'),(269,0,0,'Wed Jan 07 16:39:46.020940 2004 AUSEST','MINUTE','02',12,1,'02',NULL,NULL,0,'ENG'),(270,0,0,'Wed Jan 07 16:39:46.060940 2004 AUSEST','MINUTE','03',13,1,'03',NULL,NULL,0,'ENG'),(271,0,0,'Wed Jan 07 16:39:46.089940 2004 AUSEST','MINUTE','04',14,1,'04',NULL,NULL,0,'ENG'),(272,0,0,'Wed Jan 07 16:39:46.117940 2004 AUSEST','MINUTE','05',15,1,'05',NULL,NULL,0,'ENG'),(273,0,0,'Wed Jan 07 16:39:46.159940 2004 AUSEST','MINUTE','06',16,1,'06',NULL,NULL,0,'ENG'),(274,0,0,'Wed Jan 07 16:39:46.188940 2004 AUSEST','MINUTE','07',17,1,'07',NULL,NULL,0,'ENG'),(275,0,0,'Wed Jan 07 16:39:46.217940 2004 AUSEST','MINUTE','08',18,1,'08',NULL,NULL,0,'ENG'),(276,0,0,'Wed Jan 07 16:39:46.245940 2004 AUSEST','MINUTE','09',19,1,'09',NULL,NULL,0,'ENG'),(277,0,0,'Wed Jan 07 16:39:46.273940 2004 AUSEST','MINUTE','10',20,1,'10',NULL,NULL,0,'ENG'),(278,0,0,'Wed Jan 07 16:39:46.302940 2004 AUSEST','MINUTE','11',21,1,'11',NULL,NULL,0,'ENG'),(279,0,0,'Wed Jan 07 16:39:46.332940 2004 AUSEST','MINUTE','12',22,1,'12',NULL,NULL,0,'ENG'),(280,0,0,'Wed Jan 07 16:39:46.417940 2004 AUSEST','MINUTE','13',23,1,'13',NULL,NULL,0,'ENG'),(281,0,0,'Wed Jan 07 16:39:46.448940 2004 AUSEST','MINUTE','14',24,1,'14',NULL,NULL,0,'ENG'),(282,0,0,'Wed Jan 07 16:39:46.530940 2004 AUSEST','MINUTE','15',25,1,'15',NULL,NULL,0,'ENG'),(283,0,0,'Wed Jan 07 16:39:46.558940 2004 AUSEST','MINUTE','16',26,1,'16',NULL,NULL,0,'ENG'),(284,0,0,'Wed Jan 07 16:39:46.586940 2004 AUSEST','MINUTE','17',27,1,'17',NULL,NULL,0,'ENG'),(285,0,0,'Wed Jan 07 16:39:46.615940 2004 AUSEST','MINUTE','18',28,1,'18',NULL,NULL,0,'ENG'),(286,0,0,'Wed Jan 07 16:39:46.644940 2004 AUSEST','MINUTE','19',29,1,'19',NULL,NULL,0,'ENG'),(287,0,0,'Wed Jan 07 16:39:46.673940 2004 AUSEST','MINUTE','20',30,1,'20',NULL,NULL,0,'ENG'),(288,0,0,'Wed Jan 07 16:39:46.701940 2004 AUSEST','MINUTE','21',31,1,'21',NULL,NULL,0,'ENG'),(289,0,0,'Wed Jan 07 16:39:46.729940 2004 AUSEST','MINUTE','22',32,1,'22',NULL,NULL,0,'ENG'),(290,0,0,'Wed Jan 07 16:39:46.758940 2004 AUSEST','MINUTE','23',33,1,'23',NULL,NULL,0,'ENG'),(291,0,0,'Wed Jan 07 16:39:46.786940 2004 AUSEST','MINUTE','24',34,1,'24',NULL,NULL,0,'ENG'),(292,0,0,'Wed Jan 07 16:39:46.814940 2004 AUSEST','MINUTE','25',35,1,'25',NULL,NULL,0,'ENG'),(293,0,0,'Wed Jan 07 16:39:46.843940 2004 AUSEST','MINUTE','26',36,1,'26',NULL,NULL,0,'ENG'),(294,0,0,'Wed Jan 07 16:39:46.871940 2004 AUSEST','MINUTE','27',37,1,'27',NULL,NULL,0,'ENG'),(295,0,0,'Wed Jan 07 16:39:46.900940 2004 AUSEST','MINUTE','28',38,1,'28',NULL,NULL,0,'ENG'),(296,0,0,'Wed Jan 07 16:39:46.928940 2004 AUSEST','MINUTE','29',39,1,'29',NULL,NULL,0,'ENG'),(297,0,0,'Wed Jan 07 16:39:46.963940 2004 AUSEST','MINUTE','30',40,1,'30',NULL,NULL,0,'ENG'),(298,0,0,'Wed Jan 07 16:39:47.000940 2004 AUSEST','MINUTE','31',41,1,'31',NULL,NULL,0,'ENG'),(299,0,0,'Wed Jan 07 16:39:47.028940 2004 AUSEST','MINUTE','32',42,1,'32',NULL,NULL,0,'ENG'),(300,0,0,'Wed Jan 07 16:39:47.057940 2004 AUSEST','MINUTE','33',43,1,'33',NULL,NULL,0,'ENG'),(301,0,0,'Wed Jan 07 16:39:47.085940 2004 AUSEST','MINUTE','34',44,1,'34',NULL,NULL,0,'ENG'),(302,0,0,'Wed Jan 07 16:39:47.114940 2004 AUSEST','MINUTE','35',45,1,'35',NULL,NULL,0,'ENG'),(303,0,0,'Wed Jan 07 16:39:47.142940 2004 AUSEST','MINUTE','36',46,1,'36',NULL,NULL,0,'ENG'),(304,0,0,'Wed Jan 07 16:39:47.170940 2004 AUSEST','MINUTE','37',47,1,'37',NULL,NULL,0,'ENG'),(305,0,0,'Wed Jan 07 16:39:47.199940 2004 AUSEST','MINUTE','38',48,1,'38',NULL,NULL,0,'ENG'),(306,0,0,'Wed Jan 07 16:39:47.227940 2004 AUSEST','MINUTE','39',49,1,'39',NULL,NULL,0,'ENG'),(307,0,0,'Wed Jan 07 16:39:47.256940 2004 AUSEST','MINUTE','40',50,1,'40',NULL,NULL,0,'ENG'),(308,0,0,'Wed Jan 07 16:39:47.285940 2004 AUSEST','MINUTE','41',51,1,'41',NULL,NULL,0,'ENG'),(309,0,0,'Wed Jan 07 16:39:47.314940 2004 AUSEST','MINUTE','42',52,1,'42',NULL,NULL,0,'ENG'),(310,0,0,'Wed Jan 07 16:39:47.401940 2004 AUSEST','MINUTE','43',53,1,'43',NULL,NULL,0,'ENG'),(311,0,0,'Wed Jan 07 16:39:47.469940 2004 AUSEST','MINUTE','44',54,1,'44',NULL,NULL,0,'ENG'),(312,0,0,'Wed Jan 07 16:39:47.498940 2004 AUSEST','MINUTE','45',55,1,'45',NULL,NULL,0,'ENG'),(313,0,0,'Wed Jan 07 16:39:47.526940 2004 AUSEST','MINUTE','46',56,1,'46',NULL,NULL,0,'ENG'),(314,0,0,'Wed Jan 07 16:39:47.555940 2004 AUSEST','MINUTE','47',57,1,'47',NULL,NULL,0,'ENG'),(315,0,0,'Wed Jan 07 16:39:47.584940 2004 AUSEST','MINUTE','48',58,1,'48',NULL,NULL,0,'ENG'),(316,0,0,'Wed Jan 07 16:39:47.612940 2004 AUSEST','MINUTE','49',59,1,'49',NULL,NULL,0,'ENG'),(317,0,0,'Wed Jan 07 16:39:47.641940 2004 AUSEST','MINUTE','50',60,1,'50',NULL,NULL,0,'ENG'),(318,0,0,'Wed Jan 07 16:39:47.669940 2004 AUSEST','MINUTE','51',61,1,'51',NULL,NULL,0,'ENG'),(319,0,0,'Wed Jan 07 16:39:47.698940 2004 AUSEST','MINUTE','52',62,1,'52',NULL,NULL,0,'ENG'),(320,0,0,'Wed Jan 07 16:39:47.726940 2004 AUSEST','MINUTE','53',63,1,'53',NULL,NULL,0,'ENG'),(321,0,0,'Wed Jan 07 16:39:47.754940 2004 AUSEST','MINUTE','54',64,1,'54',NULL,NULL,0,'ENG'),(322,0,0,'Wed Jan 07 16:39:47.783940 2004 AUSEST','MINUTE','55',65,1,'55',NULL,NULL,0,'ENG'),(323,0,0,'Wed Jan 07 16:39:47.811940 2004 AUSEST','MINUTE','56',66,1,'56',NULL,NULL,0,'ENG'),(324,0,0,'Wed Jan 07 16:39:47.840940 2004 AUSEST','MINUTE','57',67,1,'57',NULL,NULL,0,'ENG'),(325,0,0,'Wed Jan 07 16:39:47.868940 2004 AUSEST','MINUTE','58',68,1,'58',NULL,NULL,0,'ENG'),(326,0,0,'Wed Jan 07 16:39:47.897940 2004 AUSEST','MINUTE','59',69,1,'59',NULL,NULL,0,'ENG'),(327,0,0,'Wed Jan 07 16:39:47.926940 2004 AUSEST','TIME','AM',1,1,'AM',NULL,NULL,0,'ENG'),(328,0,0,'Wed Jan 07 16:39:47.954940 2004 AUSEST','TIME','PM',2,1,'PM',NULL,NULL,0,'ENG'),(329,0,0,'Wed Jan 07 16:39:47.982940 2004 AUSEST','BIO_QUANTITY_UNITS','ml',1,1,'Milliliter',NULL,NULL,0,'Eng'),(335,0,0,'Wed Jan 07 16:39:48.124940 2004 AUSEST','BIOSPECIES','Human',1,1,'Human',NULL,NULL,0,'Eng'),(336,0,0,'Wed Jan 07 16:39:48.124940 2004 AUSEST','BIOSPECIES','Rat',2,1,'Rat',NULL,NULL,0,'Eng'),(337,0,0,'Wed Jan 07 16:39:48.124940 2004 AUSEST','BIOSPECIES','Mouse',3,1,'Mouse',NULL,NULL,0,'Eng'),(338,0,0,'Wed Jan 07 16:39:48.124940 2004 AUSEST','BIOSPECIES','Sheep',4,1,'Sheep',NULL,NULL,0,'Eng'),(339,0,0,'Wed Jan 07 16:39:48.124940 2004 AUSEST','BIOSPECIES','Pig',5,1,'Pig',NULL,NULL,0,'Eng'),(347,0,0,'Wed Jan 07 16:39:34.282940 2004 AUSEST','Cardiac Tissue','Left Ventricle',1,1,NULL,NULL,NULL,0,'ENG'),(348,0,0,'Wed Jan 07 16:39:34.282940 2004 AUSEST','Cardiac Tissue','Right Ventricle',2,1,NULL,NULL,NULL,0,'ENG'),(349,0,0,'Wed Jan 07 16:39:34.282940 2004 AUSEST','Cardiac Tissue','Atrium',3,1,NULL,NULL,NULL,0,'ENG'),(1004,0,0,'Fri Jan 23 17:51:40.470774 2004 EST','RESEARCHER','John De Roach',1,1,'John Smith',NULL,NULL,0,'ENG'),(1009,0,0,'Fri Jan 23 17:53:31.770366 2004 EST','COLLABORATOR','None',0,1,'None',NULL,NULL,0,'ENG'),(1011,0,0,'Thu Jun 10 16:23:06.214124 2004 EST','CAUSEOD',NULL,0,1,NULL,NULL,NULL,0,'ENG'),(1012,0,0,'Tue Jun 08 13:08:20.777574 2004 EST','CAUSEOD','Accident',1,1,NULL,NULL,NULL,0,'ENG'),(1013,0,0,'Tue Jun 08 13:08:20.879340 2004 EST','CAUSEOD','Natural',2,1,NULL,NULL,NULL,0,'ENG'),(1017,0,0,'Tue Aug 10 14:59:10.365249 2004 EST','SMARTFORM_SENSITIVITY','Low',1,1,NULL,NULL,NULL,0,'ENG'),(1018,0,0,'Tue Aug 10 14:59:10.369181 2004 EST','SMARTFORM_SENSITIVITY','High',2,1,NULL,NULL,NULL,0,'ENG'),(1019,0,0,'Tue Aug 10 14:59:35.397826 2004 EST','SMARTFORM_DOMAINS','Study',1,1,NULL,NULL,NULL,0,'ENG'),(1020,0,0,'Mon Aug 23 13:08:49.127644 2004 EST','COLLECTION_TUBE','6 mls ACD',1,1,NULL,NULL,NULL,0,'ENG'),(1021,0,0,'Mon Aug 23 13:08:49.130497 2004 EST','COLLECTION_TUBE','9 mls ACD',2,1,NULL,NULL,NULL,0,'ENG'),(1022,0,0,'Mon Aug 23 13:08:49.134634 2004 EST','COLLECTION_TUBE','EDTA',3,1,NULL,NULL,NULL,0,'ENG'),(1023,0,0,'Mon Aug 23 13:08:49.137709 2004 EST','COLLECTION_TUBE','Other',4,1,NULL,NULL,NULL,0,'ENG'),(1024,0,0,'Mon Aug 23 13:08:49.140642 2004 EST','MRDG1WASH','Yes',1,1,NULL,NULL,NULL,0,'ENG'),(1025,0,0,'Mon Aug 23 13:08:49.143230 2004 EST','MRDG1WASH','No',2,1,NULL,NULL,NULL,0,'ENG'),(1026,0,0,'Mon Aug 23 13:08:49.145715 2004 EST','MRDRNA','Yes',1,1,NULL,NULL,NULL,0,'ENG'),(1027,0,0,'Mon Aug 23 13:08:49.148208 2004 EST','MRDRNA','No',2,1,NULL,NULL,NULL,0,'ENG'),(1028,0,0,'Mon Aug 23 13:08:49.167655 2004 EST','REPORT_PARAM_OPERATOR','<',0,1,'Less Than',NULL,NULL,0,'ENG'),(1029,0,0,'Mon Aug 23 13:08:49.170250 2004 EST','REPORT_PARAM_OPERATOR','=<',1,1,'Less Than OR Equal To',NULL,NULL,0,'ENG'),(1030,0,0,'Mon Aug 23 13:08:49.172855 2004 EST','REPORT_PARAM_OPERATOR','=>',2,1,'Greater Than or Equal To',NULL,NULL,0,'ENG'),(1031,0,0,'Mon Aug 23 13:08:49.175678 2004 EST','REPORT_PARAM_OPERATOR','>',3,1,'Greater Than',NULL,NULL,0,'ENG'),(1032,0,0,'Mon Aug 23 13:08:49.178530 2004 EST','REPORT_PARAM_OPERATOR','=',4,1,'Equal To',NULL,NULL,0,'ENG'),(1033,0,0,'Mon Aug 23 13:08:49.181306 2004 EST','REPORT_PARAM_TYPE','Numeric',0,1,'Number type for report parameter',NULL,NULL,0,'ENG'),(1034,0,0,'Mon Aug 23 13:08:49.185001 2004 EST','REPORT_PARAM_TYPE','String',1,1,'String type for report parameter',NULL,NULL,0,'ENG'),(1035,0,0,'Mon Aug 23 13:08:49.187962 2004 EST','REPORT_PARAM_TYPE','Date',2,1,'Date type for report parameter',NULL,NULL,0,'ENG'),(1044,0,0,'Mon Aug 23 13:08:49.481918 2004 EST','NOTETYPE','history',NULL,1,'history',NULL,NULL,0,'ENG'),(1045,0,0,'Wed Jan 07 16:39:40.080940 2004 AUSEST','SEX','Female',2,1,'Female',NULL,NULL,0,'ENG'),(1046,0,0,'Wed Jan 07 16:39:40.122940 2004 AUSEST','SEX','Male',1,1,'Male',NULL,NULL,0,'ENG'),(1047,0,0,'Wed Jan 07 16:39:40.208940 2004 AUSEST','SEX','Unknown',4,1,'unknown',NULL,NULL,0,'ENG'),(1048,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Human',1,1,'Human',NULL,NULL,0,'ENG'),(1049,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Baboon',2,1,'Baboon',NULL,NULL,0,'ENG'),(1050,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Mouse',3,1,'Mouse',NULL,NULL,0,'ENG'),(1051,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Rat',4,1,'Rat',NULL,NULL,0,'ENG'),(1052,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Rabbit',5,1,'Rabbit',NULL,NULL,0,'ENG'),(1053,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Goat',6,1,'Goat',NULL,NULL,0,'ENG'),(1054,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Sheep',7,1,'Sheep',NULL,NULL,0,'ENG'),(1055,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Cat',8,1,'Cat',NULL,NULL,0,'ENG'),(1056,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Dog',9,1,'Dog',NULL,NULL,0,'ENG'),(1057,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Cow',10,1,'Cow',NULL,NULL,0,'ENG'),(1058,0,0,'Mon Aug 23 13:08:49.190918 2004 EST','SPECIES','Pig',11,1,'Pig',NULL,NULL,0,'ENG'),(1059,0,0,'Mon Aug 30 10:56:56.222209 2004 AUSEST','HOSPITAL','Nedlands (E Block, SCGH)',1,1,'JHH',NULL,NULL,0,'ENG'),(1060,0,0,'Mon Aug 30 10:57:06.499209 2004 AUSEST','HOSPITAL','Port Hedland',2,1,'POWCH',NULL,NULL,0,'ENG'),(1061,0,0,'Mon Aug 30 10:57:14.585209 2004 AUSEST','HOSPITAL','Kalgoorlie (Kalgoorlie Regional Hospital)',3,1,'RAHC',NULL,NULL,0,'ENG'),(1062,0,0,'Mon Aug 30 10:57:39.409209 2004 AUSEST','HOSPITAL','Alexander Heights',4,1,'RGH Concord',NULL,NULL,0,'ENG'),(1063,0,0,'Mon Aug 30 10:57:49.078209 2004 AUSEST','HOSPITAL','Northam (Northam Regional Hospital)',5,1,'RPAH',NULL,NULL,0,'ENG'),(1064,0,0,'Mon Aug 30 10:58:01.326209 2004 AUSEST','HOSPITAL','Sydney Hospital',6,1,'Sydney Hospital',NULL,NULL,0,'ENG'),(1065,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','DMD',1,1,'DMD',NULL,NULL,0,'ENG'),(1066,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','BMD',2,1,'BMD',NULL,NULL,0,'ENG'),(1067,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','CMD',3,1,'CMD',NULL,NULL,0,'ENG'),(1068,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','LGMD',4,1,'LGMD',NULL,NULL,0,'ENG'),(1069,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','NM',5,1,'NM',NULL,NULL,0,'ENG'),(1070,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','Dystrophy',6,1,'Dystrophy',NULL,NULL,0,'ENG'),(1071,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','Myopathy',7,1,'Myopathy',NULL,NULL,0,'ENG'),(1072,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','Disease Control',8,1,'Disease Control',NULL,NULL,0,'ENG'),(1073,0,0,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','Healthy Control',9,1,'Healthy Control',NULL,NULL,0,'ENG'),(1074,0,10,'Mon Aug 23 13:08:49.193660 2004 EST','DIAGCATEGORY','Unknown',2,1,'Unknown',NULL,NULL,0,'ENG'),(1076,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE','DNA',1,1,'DNA','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(1088,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Biceps',1,1,'Biceps','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1089,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Deltoid',2,1,'Deltoid','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1090,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Diaphragm',3,1,'Diaphragm','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1091,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','EDL',4,1,'EDL','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1092,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Gastrocnemius',5,1,'Gastrocnemius','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1093,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Pectoralis Major',6,1,'Pectoralis Major','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1094,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Quadriceps',7,1,'Quadriceps','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1095,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Soleus',8,1,'Soleus','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1096,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Tibialis Anterior',9,1,'Tibialis Anterior','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1097,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Trapezius',10,1,'Trapezius','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1098,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Triceps',11,1,'Triceps','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1099,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_SKELETAL','Vastus Lateralis',12,1,'Vastus Lateralis','BIOSPECCOLLECTIONTYPE','Skeletal Muscle',0,'ENG'),(1100,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Biceps',1,1,'Biceps',NULL,NULL,0,'ENG'),(1101,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Deltoid',2,1,'Deltoid',NULL,NULL,0,'ENG'),(1102,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Diaphragm',3,1,'Diaphragm',NULL,NULL,0,'ENG'),(1103,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','EDL',4,1,'EDL',NULL,NULL,0,'ENG'),(1104,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Gastrocnemius',5,1,'Gastrocnemius',NULL,NULL,0,'ENG'),(1105,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Pectoralis Major',6,1,'Pectoralis Major',NULL,NULL,0,'ENG'),(1106,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Quadriceps',7,1,'Quadriceps',NULL,NULL,0,'ENG'),(1107,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Soleus',8,1,'Soleus',NULL,NULL,0,'ENG'),(1108,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Tibialis Anterior',9,1,'Tibialis Anterior',NULL,NULL,0,'ENG'),(1109,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Trapezius',10,1,'Trapezius',NULL,NULL,0,'ENG'),(1110,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Triceps',11,1,'Triceps',NULL,NULL,0,'ENG'),(1111,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Skeletal Muscle','Vastus Lateralis',12,1,'Vastus Lateralis',NULL,NULL,0,'ENG'),(1125,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_BRAIN','Spinal Cord',4,1,'Spinal Cord','BIOSPECCOLLECTIONTYPE','Brain',0,'ENG'),(1126,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_BRAIN','Cerebellum',1,1,'Cerebellum','BIOSPECCOLLECTIONTYPE','Brain',0,'ENG'),(1127,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_BRAIN','Cortex',2,1,'Cortex','BIOSPECCOLLECTIONTYPE','Brain',0,'ENG'),(1128,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Brain','Spinal Cord',4,1,'Spinal Cord',NULL,NULL,0,'ENG'),(1129,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Brain','Cerebellum',1,1,'Cerebellum',NULL,NULL,0,'ENG'),(1130,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Brain','Cortex',2,1,'Cortex',NULL,NULL,0,'ENG'),(1131,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY','Dystropic',1,1,'Dystropic','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY',NULL,0,'ENG'),(1132,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY','Myopathic',1,1,'Myopathic','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY',NULL,0,'ENG'),(1133,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY','Normal',1,1,'Normal','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY',NULL,0,'ENG'),(1134,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY','Unknown',1,1,'Unknown','BIOSPECCOLLECTIONTYPE_HISTOPATHOLOGY',NULL,0,'ENG'),(1135,0,0,'Tue Aug 24 02:33:18.647595 2004 EST','WBSIZE','Normal',NULL,1,NULL,NULL,NULL,0,'ENG'),(1136,0,0,'Tue Aug 24 02:33:21.343888 2004 EST','WBSIZE','Truncated',NULL,1,NULL,NULL,NULL,0,'ENG'),(1137,0,0,'Sat Aug 21 22:44:55.574312 2004 EST','BIOANALYSIS','Genetic',NULL,1,'Genetic',NULL,NULL,0,'ENG'),(1138,0,0,'Sat Aug 21 22:44:55.624108 2004 EST','BIOANALYSIS','Immunohistochemistry',NULL,1,'Immunhistochemistry',NULL,NULL,0,'ENG'),(1139,0,0,'Sat Aug 21 22:44:55.658177 2004 EST','BIOANALYSIS','Western Blot',NULL,1,'WesternBlot',NULL,NULL,0,'ENG'),(1140,0,0,'Tue Aug 24 02:29:14.363309 2004 EST','MUTATION','Mutation',NULL,1,NULL,NULL,NULL,0,'ENG'),(1141,0,0,'Tue Aug 24 02:29:29.725734 2004 EST','MUTATION','Polymorphism',NULL,1,NULL,NULL,NULL,0,'ENG'),(1142,0,0,'Tue Aug 24 02:30:05.791849 2004 EST','ZYGOSITY','Homozygous',NULL,1,NULL,NULL,NULL,0,'ENG'),(1143,0,0,'Tue Aug 24 02:30:16.961931 2004 EST','ZYGOSITY','Heterozygous',NULL,1,NULL,NULL,NULL,0,'ENG'),(1144,0,0,'Tue Aug 24 02:30:27.686499 2004 EST','ZYGOSITY','Hemizygous',NULL,1,NULL,NULL,NULL,0,'ENG'),(1145,0,0,'Tue Aug 24 02:31:38.512674 2004 EST','NORMAL','Normal',NULL,1,NULL,NULL,NULL,0,'ENG'),(1146,0,0,'Tue Aug 24 02:31:43.745428 2004 EST','NORMAL','Abnormal',NULL,1,NULL,NULL,NULL,0,'ENG'),(1147,0,0,'Tue Aug 24 02:31:13.173442 2004 EST','STAININGRESULT','<20%',5,1,NULL,NULL,NULL,0,'ENG'),(1148,0,0,'Tue Aug 24 02:31:13.173442 2004 EST','STAININGRESULT','+++',4,1,NULL,NULL,NULL,0,'ENG'),(1149,0,0,'Tue Aug 24 02:31:17.094798 2004 EST','STAININGRESULT','++',3,1,NULL,NULL,NULL,0,'ENG'),(1150,0,0,'Tue Aug 24 02:31:24.581328 2004 EST','STAININGRESULT','+',2,1,NULL,NULL,NULL,0,'ENG'),(1151,0,0,'Tue Aug 24 02:31:28.675697 2004 EST','STAININGRESULT','-',1,1,NULL,NULL,NULL,0,'ENG'),(1152,0,0,'Tue Aug 24 02:31:28.675697 2004 EST','STAININGRESULT','Note done',0,1,NULL,NULL,NULL,0,'ENG'),(1153,0,0,'Tue Aug 24 02:32:37.540446 2004 EST','EXPRESSION','-',NULL,1,NULL,NULL,NULL,0,'ENG'),(1154,0,0,'Tue Aug 24 02:33:10.266806 2004 EST','WBEXPRESSION','-',NULL,1,NULL,NULL,NULL,0,'ENG'),(1155,0,0,'Tue Aug 24 02:32:37.540446 2004 EST','EXPRESSION','>',NULL,1,NULL,NULL,NULL,0,'ENG'),(1156,0,0,'Tue Aug 24 02:33:10.266806 2004 EST','WBEXPRESSION','>',NULL,1,NULL,NULL,NULL,0,'ENG'),(1157,0,0,'Tue Aug 24 02:32:42.668785 2004 EST','EXPRESSION','<',NULL,1,NULL,NULL,NULL,0,'ENG'),(1158,0,0,'Tue Aug 24 02:33:08.100822 2004 EST','WBEXPRESSION','<',NULL,1,NULL,NULL,NULL,0,'ENG'),(1159,0,0,'Tue Aug 24 02:32:46.927774 2004 EST','EXPRESSION','=',NULL,1,NULL,NULL,NULL,0,'ENG'),(1160,0,0,'Tue Aug 24 02:33:04.351738 2004 EST','WBEXPRESSION','=',NULL,1,NULL,NULL,NULL,0,'ENG'),(1161,0,0,'Fri Oct 29 06:06:58.655649 2004 EST','SMARTFORM_DOMAINS','Bioanalysis',NULL,1,'Bioanalysis',NULL,NULL,0,'ENG'),(1162,0,0,'Fri Oct 29 15:54:22.058971 2004 EST','SMARTFORM_DOMAINS','Biospecimen',NULL,1,'Biospecimen',NULL,NULL,0,'ENG'),(1163,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_CARDIAC','Atrium',1,1,'Atrium','BIOSPECCOLLECTIONTYPE','Cardiac Muscle',0,'ENG'),(1164,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_CARDIAC','Left Ventricle',2,1,'Left Ventricle','BIOSPECCOLLECTIONTYPE','Cardiac Muscle',0,'ENG'),(1165,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','BIOSPECCOLLECTIONTYPE_CARDIAC','Right Ventricle',3,1,'Right Ventricle','BIOSPECCOLLECTIONTYPE','Cardiac Muscle',0,'ENG'),(1166,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Cardiac Muscle','Atrium',1,1,'Atrium',NULL,NULL,0,'ENG'),(1167,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Cardiac Muscle','Left Ventricle',2,1,'Left Ventricle',NULL,NULL,0,'ENG'),(1168,0,0,'Tue Jan 13 10:00:47.469667 2004 EST','Cardiac Muscle','Right Ventricle',3,1,'Right Ventricle',NULL,NULL,0,'ENG'),(1169,0,0,NULL,'EPISODE','Primary Cancer',NULL,1,'Primary Cancer',NULL,NULL,0,'ENG'),(1170,0,0,NULL,'BIOSPECSTOREDIN','0.5ml',1,1,NULL,NULL,NULL,0,'ENG'),(1171,0,0,NULL,'BIOSPECSTOREDIN','2ml',3,1,NULL,NULL,NULL,0,'ENG'),(1172,0,0,NULL,'BIOSPECSTOREDIN','1.5ml',2,1,NULL,NULL,NULL,0,'ENG'),(1173,0,0,NULL,'EPISODE','Secondary Episode',NULL,1,'Secondary Episode',NULL,NULL,0,'ENG'),(1180,0,0,NULL,'BIOTXN_STATUS','Available',1,1,'Available',NULL,NULL,0,'ENG'),(1181,0,0,NULL,'BIOTXN_STATUS','Allocated',2,1,'Allocated',NULL,NULL,0,'ENG'),(1182,0,0,NULL,'BIOTXN_STATUS','Delivered',3,1,'Delivered',NULL,NULL,0,'ENG'),(1223,0,0,'Feb 26 2005 12:19PM','CONSENTVERSION','v1.0',1,1,'v1.0',NULL,NULL,0,'ENG'),(1224,0,0,'Feb 26 2005 12:19PM','CONSENTVERSION','v2.0',2,1,'v2.0',NULL,NULL,0,'ENG'),(1225,0,0,'Feb 26 2005 12:19PM','CONSENTVERSION','v3.0',3,1,'v3.0',NULL,NULL,0,'ENG'),(1226,0,0,'Feb 26 2005 12:19PM','CONSENTVERSION','v4.0',4,1,'v4.0',NULL,NULL,0,'ENG'),(1227,0,0,'Feb 26 2005 12:19PM','CONSENTVERSION','v5.0',5,1,'v5.0',NULL,NULL,0,'ENG'),(1485,0,0,'26-MAY-06 12.18.34.408308 PM +08:00','SMARTFORM_DOMAINS','Admissions',4,NULL,'Admissions',NULL,NULL,0,'ENG'),(1486,0,0,'26-MAY-06 12.18.34.575155 PM +08:00','PAT_ADM_EPISODENO','1',1,NULL,'1',NULL,NULL,0,'ENG'),(1487,0,0,'26-MAY-06 12.18.34.579742 PM +08:00','PAT_ADM_EPISODENO','2',2,NULL,'2',NULL,NULL,0,'ENG'),(1488,0,0,'26-MAY-06 12.18.34.584031 PM +08:00','PAT_ADM_EPISODENO','3',3,NULL,'3',NULL,NULL,0,'ENG'),(1489,0,0,'26-MAY-06 12.18.34.588088 PM +08:00','PAT_ADM_EPISODENO','4',4,NULL,'4',NULL,NULL,0,'ENG'),(1490,0,0,'26-MAY-06 12.18.34.592233 PM +08:00','PAT_ADM_EPISODENO','5',5,NULL,'5',NULL,NULL,0,'ENG'),(1491,0,0,'26-MAY-06 12.18.34.676242 PM +08:00','PAT_ADM_EPISODEDESC','Primary Cancer',1,NULL,'Primary Cancer',NULL,NULL,0,'ENG'),(1492,0,0,'26-MAY-06 12.18.34.680815 PM +08:00','PAT_ADM_EPISODEDESC','Secondary Episode',2,NULL,'Secondary Episode',NULL,NULL,0,'ENG'),(1493,0,0,'26-MAY-06 12.18.34.685292 PM +08:00','PAT_ADM_EPISODEDESC','Recurrence',3,NULL,'Recurrence',NULL,NULL,0,'ENG'),(1494,0,0,'26-MAY-06 12.18.34.761073 PM +08:00','PAT_ADM_COLLGRP','1',1,NULL,'1',NULL,NULL,0,'ENG'),(1495,0,0,'26-MAY-06 12.18.34.765531 PM +08:00','PAT_ADM_COLLGRP','2',2,NULL,'2',NULL,NULL,0,'ENG'),(1496,0,0,'26-MAY-06 12.18.34.769885 PM +08:00','PAT_ADM_COLLGRP','3',3,NULL,'3',NULL,NULL,0,'ENG'),(1497,0,0,'26-MAY-06 12.18.34.774284 PM +08:00','PAT_ADM_COLLGRP','4',4,NULL,'4',NULL,NULL,0,'ENG'),(1498,0,0,'26-MAY-06 12.18.34.778688 PM +08:00','PAT_ADM_COLLGRP','5',5,NULL,'5',NULL,NULL,0,'ENG'),(1499,0,0,'26-MAY-06 12.18.34.821527 PM +08:00','PAT_ADM_TISSUETYPE',NULL,1,NULL,NULL,NULL,NULL,0,'ENG'),(1500,0,0,'26-MAY-06 12.18.34.826148 PM +08:00','PAT_ADM_TISSUETYPE','Liver',2,NULL,'Liver',NULL,NULL,0,'ENG'),(1501,0,0,'26-MAY-06 12.18.34.831252 PM +08:00','PAT_ADM_TISSUETYPE','Lung',3,NULL,'Lung',NULL,NULL,0,'ENG'),(1502,0,0,'26-MAY-06 12.18.34.835734 PM +08:00','PAT_ADM_TISSUETYPE','Bowel',3,NULL,'Bowel',NULL,NULL,0,'ENG'),(1503,0,0,'26-MAY-06 12.18.34.923673 PM +08:00','PAT_ADM_TISSUECLASS',NULL,1,NULL,NULL,NULL,NULL,0,'ENG'),(1504,0,0,'26-MAY-06 12.18.34.928329 PM +08:00','PAT_ADM_TISSUECLASS','Benign',2,NULL,'Benign',NULL,NULL,0,'ENG'),(1505,0,0,'26-MAY-06 12.18.34.932780 PM +08:00','PAT_ADM_TISSUECLASS','Normal',3,NULL,'Normal',NULL,NULL,0,'ENG'),(1506,0,0,'26-MAY-06 12.18.34.937207 PM +08:00','PAT_ADM_TISSUECLASS','Tumour',4,NULL,'Tumour',NULL,NULL,0,'ENG'),(1540,0,0,'22-JAN-07 04.27.02.023505 AM +00:00','RESEARCHER','Tina Lamey',2,NULL,NULL,NULL,NULL,0,'ENG'),(1541,0,0,'22-JAN-07 04.27.02.045394 AM +00:00','RESEARCHER','Enid Chelva',3,NULL,NULL,NULL,NULL,0,'ENG'),(1542,0,0,'22-JAN-07 04.27.02.063191 AM +00:00','RESEARCHER','Sarina Laurin',4,NULL,NULL,NULL,NULL,0,'ENG'),(1560,0,0,'22-JAN-07 04.27.02.063191 AM +00:00','BARCODE_SOURCE','ADMISSIONKEY',1,NULL,NULL,NULL,NULL,0,'ENG'),(1561,0,0,'21-SEP-07 11.51.11.880287 AM +08:00','BARCODE_SOURCE','PATIENTID',2,NULL,NULL,NULL,NULL,0,'ENG'),(1562,0,0,'21-SEP-07 11.51.11.880287 AM +08:00','BARCODE_SOURCE','ADMISSIONID',3,NULL,NULL,NULL,NULL,0,'ENG'),(1580,0,0,'24-SEP-07 03.20.34.265440 PM +08:00','BIOSPECCOLLECTIONTYPE','Serum',2,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1581,0,0,'24-SEP-07 03.20.34.300526 PM +08:00','BIOSPECCOLLECTIONTYPE','Plasma',2,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1582,0,0,'24-SEP-07 03.20.34.312305 PM +08:00','BIOSPECCOLLECTIONTYPE','Buffy Coat',3,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1600,0,0,'26-SEP-07 04.36.54.255685 PM +08:00','BIOSPECCOLLECTIONTYPE','RNA',7,1,'RNA','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(1620,0,0,'04-OCT-07 03.46.10.361018 PM +08:00','HOSPITAL','Unknown',7,NULL,NULL,NULL,NULL,0,'ENG'),(1621,0,0,'04-OCT-07 03.46.10.382754 PM +08:00','HOSPITAL','Nedlands (J Block, QEII MC)',8,NULL,NULL,NULL,NULL,0,'ENG'),(1622,0,0,'04-OCT-07 03.46.10.394776 PM +08:00','HOSPITAL','Perth (RPH Lvl 2, North Block)',9,NULL,NULL,NULL,NULL,0,'ENG'),(1640,0,0,'10-OCT-07 11.36.50.706558 AM AUSTRALIA/WEST','EXTRACTION_PROTOCOL','Salting out',1,NULL,NULL,NULL,NULL,0,'ENG'),(1660,0,0,'10-OCT-07 11.38.31.763436 AM AUSTRALIA/WEST','EXTRACTION_PROTOCOL','Phenol/chloroform',2,NULL,NULL,NULL,NULL,0,'ENG'),(1661,0,0,'10-OCT-07 11.38.31.779287 AM AUSTRALIA/WEST','EXTRACTION_PROTOCOL','Bead',3,NULL,NULL,NULL,NULL,0,'ENG'),(1662,0,0,'10-OCT-07 11.42.02.583144 AM AUSTRALIA/WEST','ANTICOAG','EDTA',1,NULL,NULL,NULL,NULL,0,'ENG'),(1663,0,0,'10-OCT-07 11.42.02.598138 AM AUSTRALIA/WEST','ANTICOAG','Lithium Heparin',2,NULL,NULL,NULL,NULL,0,'ENG'),(1664,0,0,'10-OCT-07 11.44.01.986249 AM AUSTRALIA/WEST','QUALITY','Fresh',1,NULL,NULL,NULL,NULL,0,'ENG'),(1665,0,0,'10-OCT-07 11.44.02.062597 AM AUSTRALIA/WEST','QUALITY','Frozen short term (<6mths)',2,NULL,NULL,NULL,NULL,0,'ENG'),(1666,0,0,'10-OCT-07 11.44.02.072345 AM AUSTRALIA/WEST','QUALITY','Frozen long term (>6mths)',3,NULL,NULL,NULL,NULL,0,'ENG'),(1680,0,0,'30-NOV-07 03.25.54.851714 PM +08:00','EXTRACTION_PROTOCOL','RNA',4,NULL,NULL,NULL,NULL,0,'ENG'),(1681,0,0,'30-NOV-07 03.25.54.899228 PM +08:00','EXTRACTION_PROTOCOL','Oragene',5,NULL,NULL,NULL,NULL,0,'ENG'),(1682,0,0,'30-NOV-07 03.26.34.436670 PM +08:00','EXTRACTION_PROTOCOL','Column',6,NULL,NULL,NULL,NULL,0,'ENG'),(1700,0,0,'15-APR-08 10.42.11.624901 AM +08:00','BIOSPECGRADE','Extracted',1,NULL,NULL,NULL,NULL,0,'ENG'),(1701,0,0,'15-APR-08 10.42.11.649133 AM +08:00','BIOSPECGRADE','Precipitated',2,NULL,NULL,NULL,NULL,0,'ENG'),(1720,0,0,'15-SEP-08 10.28.36.743188 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Saliva',4,NULL,NULL,NULL,NULL,0,'ENG'),(1721,0,0,'25/SEP/08 01:43:02.881702 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Urine',5,NULL,NULL,'BIOSPECCOLLECTIONTYPE',NULL,0,'ENG'),(1722,27,0,'25/SEP/08 01:43:02.913407 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Red Blood Cells',6,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1740,27,0,'30/SEP/08 08:39:56.454877 AM AUSTRALIA/PERTH','ANTICOAG','N/A',0,NULL,NULL,NULL,NULL,0,'ENG'),(1741,27,0,'30/SEP/08 08:39:56.454877 AM AUSTRALIA/PERTH','ANTICOAG','Sodium Citrate',3,NULL,NULL,NULL,NULL,0,'ENG'),(1742,17,0,'30/SEP/08 02:45:49.854590 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Lithium Heparin',4,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1743,17,0,'30/SEP/08 02:46:11.245432 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','EDTA Blood',4,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1744,17,0,'30/SEP/08 02:47:56.830851 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','DNA from TL',3,1,'DNA from TL','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(1745,17,0,'Mon Aug 30 10:58:01.326209 2004 AUSEST','HOSPITAL','Graylands Hospital',10,1,'Graylands Hospital',NULL,NULL,0,'ENG'),(1746,54,0,'10/DEC/08 09:41:53.887666 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Mothers Blood',1,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1747,54,0,'10/DEC/08 09:41:54.013641 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Cord Blood',2,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1749,17,0,'10/DEC/08 04:46:01.617595 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Frozen Lymphocytes (F)',4,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1750,17,0,'10/DEC/08 04:46:01.748162 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Transformed lymphoblasts (T)',5,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1754,17,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR','Danny Rock',4,NULL,NULL,NULL,NULL,0,'ENG'),(1755,17,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR','David Chandler',5,NULL,NULL,NULL,NULL,0,'ENG'),(1756,17,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR','Elaine Murphy',6,NULL,NULL,NULL,NULL,0,'ENG'),(1757,17,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR','Other Person',18,NULL,NULL,NULL,NULL,0,'ENG'),(1758,17,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR','Paul Connolly',19,NULL,NULL,NULL,NULL,0,'ENG'),(1759,17,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR','Sarah Howell',22,NULL,NULL,NULL,NULL,0,'ENG'),(1760,0,0,'12/DEC/08 10:11:03.351246 AM AUSTRALIA/PERTH','REF_DOCTOR',NULL,0,NULL,NULL,NULL,NULL,0,'ENG'),(1780,0,0,'29/01/2009 11:01:36.814167 AM AUSTRALIA/PERTH','HOSPITAL','Fremantle Hospital',11,1,'Fremantle Hospital',NULL,NULL,0,'ENG'),(1800,0,0,'10/FEB/09 02:53:41.881382 PM AUSTRALIA/PERTH','BIOSPECSTOREDIN','96 well plate',7,1,NULL,NULL,NULL,0,'ENG'),(1860,0,0,'22/JUN/09 06:45:13.718561 PM +08:00','CONSENTSTATUS','Not Consented',1,NULL,NULL,NULL,NULL,0,'ENG'),(1861,0,0,'22/JUN/09 06:45:13.764994 PM +08:00','CONSENTSTATUS','Consented',2,NULL,NULL,NULL,NULL,0,'ENG'),(1862,0,0,'22/JUN/09 06:45:13.785374 PM +08:00','CONSENTSTATUS','Refused',3,NULL,NULL,NULL,NULL,0,'ENG'),(1863,0,0,'22/JUN/09 06:45:13.794315 PM +08:00','CONSENTSTATUS','Withdrawn',4,NULL,NULL,NULL,NULL,0,'ENG'),(1865,0,0,'25/06/2009 11:14:03.410821 AM +08:00','BIOSPECCOLLECTIONTYPE','Whole Blood',1,1,'Whole Blood','BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1866,0,0,'25/06/2009 11:14:56.763814 AM +08:00','BIOSPECCOLLECTIONTYPE','Buccal Swab',1,1,'Buccal Swab','BIOSPECCOLLECTIONTYPE','Saliva',0,'ENG'),(1867,0,0,'25/06/2009 11:15:13.827592 AM +08:00','BIOSPECCOLLECTIONTYPE','Paxgene RNA',6,1,'Paxgene RNA','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(1880,17,0,'22/FEB/10 02:45:03.559403 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Protein from TL',4,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(1881,274,0,'Wed Jan 07 16:39:47.982940 2004 AUSEST','BIO_QUANTITY_UNITS','vial',2,1,'Vial',NULL,NULL,0,'Eng'),(1882,0,0,'27-MAR-08 01.04.09.410016 PM +08:00','BIOSPECCOLLECTIONTYPE','Nucleic Acid',3,NULL,NULL,NULL,NULL,0,'ENG'),(1883,0,0,'12-JUL-05 02.35.58.283839 PM +08:00','BIOSPECCOLLECTIONTYPE','Tissue',2,NULL,NULL,NULL,NULL,0,'ENG'),(1885,274,0,'12-JUL-05 02.35.58.284362 PM +08:00','BIOSPECCOLLECTIONTYPE','Sputum',4,NULL,NULL,NULL,NULL,0,'ENG'),(1886,0,0,'12-JUL-05 02.35.58.277649 PM +08:00','BIOSPECCOLLECTIONTYPE','Blood',1,NULL,NULL,NULL,NULL,0,'ENG'),(1887,274,0,'12-JUL-05 03.09.04.032889 PM +08:00','PRIMARYSITE','Soft tissues, NOS (C499)',281,NULL,NULL,NULL,NULL,0,'ENG'),(1888,274,0,'12-JUL-05 03.09.04.033752 PM +08:00','PRIMARYSITE','Soft tissues, pelvis (C495)',282,NULL,NULL,NULL,NULL,0,'ENG'),(1889,274,0,'12-JUL-05 03.09.04.034010 PM +08:00','PRIMARYSITE','Soft tissues, thorax (C493)',283,NULL,NULL,NULL,NULL,0,'ENG'),(1890,274,0,'12-JUL-05 03.09.04.034248 PM +08:00','PRIMARYSITE','Soft tissues, trunk (C496)',284,NULL,NULL,NULL,NULL,0,'ENG'),(1891,274,0,'12-JUL-05 03.09.04.034497 PM +08:00','PRIMARYSITE','Soft tissues, upper limb and shoulder (C491)',285,NULL,NULL,NULL,NULL,0,'ENG'),(1892,274,0,'12-JUL-05 03.09.04.034738 PM +08:00','PRIMARYSITE','Spermatic cord (C631)',286,NULL,NULL,NULL,NULL,0,'ENG'),(1893,274,0,'12-JUL-05 03.09.04.034970 PM +08:00','PRIMARYSITE','Sphenoid sinus (C313)',287,NULL,NULL,NULL,NULL,0,'ENG'),(1894,274,0,'12-JUL-05 03.09.04.035203 PM +08:00','PRIMARYSITE','Spinal cord (C720)',288,NULL,NULL,NULL,NULL,0,'ENG'),(1895,274,0,'12-JUL-05 03.09.04.035496 PM +08:00','PRIMARYSITE','Spleen (C422)',289,NULL,NULL,NULL,NULL,0,'ENG'),(1896,274,0,'12-JUL-05 03.09.04.035748 PM +08:00','PRIMARYSITE','Stomach, anturm (gastric/pyloric) (C163)',290,NULL,NULL,NULL,NULL,0,'ENG'),(1897,274,0,'12-JUL-05 03.09.04.035986 PM +08:00','PRIMARYSITE','Stomach, body (gastric/corpus) (C162)',291,NULL,NULL,NULL,NULL,0,'ENG'),(1898,274,0,'12-JUL-05 03.09.04.036223 PM +08:00','PRIMARYSITE','Stomach, fundus (gastric) (C161)',292,NULL,NULL,NULL,NULL,0,'ENG'),(1899,274,0,'12-JUL-05 03.09.04.036519 PM +08:00','PRIMARYSITE','Stomach, greater curvature, NOS (C166)',293,NULL,NULL,NULL,NULL,0,'ENG'),(1900,274,0,'12-JUL-05 03.09.04.036762 PM +08:00','PRIMARYSITE','Stomach, lesser curvature, NOS (C165)',294,NULL,NULL,NULL,NULL,0,'ENG'),(1901,274,0,'12-JUL-05 03.09.04.037000 PM +08:00','PRIMARYSITE','Stomach, NOS (Gastric) (C169)',295,NULL,NULL,NULL,NULL,0,'ENG'),(1902,274,0,'12-JUL-05 03.09.04.037235 PM +08:00','PRIMARYSITE','Stomach, other parts, OL (C168)',296,NULL,NULL,NULL,NULL,0,'ENG'),(1903,274,0,'12-JUL-05 03.09.04.038411 PM +08:00','PRIMARYSITE','Subglottis (C322)',297,NULL,NULL,NULL,NULL,0,'ENG'),(1904,274,0,'12-JUL-05 03.09.04.038721 PM +08:00','PRIMARYSITE','Sublingual gland (C081)',298,NULL,NULL,NULL,NULL,0,'ENG'),(1905,274,0,'12-JUL-05 03.09.04.038964 PM +08:00','PRIMARYSITE','Submandibular gland (C080)',299,NULL,NULL,NULL,NULL,0,'ENG'),(1906,274,0,'12-JUL-05 03.09.04.039198 PM +08:00','PRIMARYSITE','Supraglottis (C321)',300,NULL,NULL,NULL,NULL,0,'ENG'),(1907,274,0,'12-JUL-05 03.09.04.039478 PM +08:00','PRIMARYSITE','Temporal lobe (C712)',301,NULL,NULL,NULL,NULL,0,'ENG'),(1908,274,0,'12-JUL-05 03.09.04.040217 PM +08:00','PRIMARYSITE','Testis, descended (C621)',302,NULL,NULL,NULL,NULL,0,'ENG'),(1909,274,0,'12-JUL-05 03.09.04.040476 PM +08:00','PRIMARYSITE','Testis, NOS (C629)',303,NULL,NULL,NULL,NULL,0,'ENG'),(1910,274,0,'12-JUL-05 03.09.04.040718 PM +08:00','PRIMARYSITE','Testis, undescended (C620)',304,NULL,NULL,NULL,NULL,0,'ENG'),(1911,274,0,'12-JUL-05 03.09.04.040952 PM +08:00','PRIMARYSITE','Thorax, NOS (C761)',305,NULL,NULL,NULL,NULL,0,'ENG'),(1912,274,0,'12-JUL-05 03.09.04.041185 PM +08:00','PRIMARYSITE','Thymus (C379)',306,NULL,NULL,NULL,NULL,0,'ENG'),(1913,274,0,'12-JUL-05 03.09.04.041423 PM +08:00','PRIMARYSITE','Thyroid gland (C739)',307,NULL,NULL,NULL,NULL,0,'ENG'),(1914,274,0,'12-JUL-05 03.09.04.041663 PM +08:00','PRIMARYSITE','Tongue, anterior 2/3, NOS (C023)',308,NULL,NULL,NULL,NULL,0,'ENG'),(1915,274,0,'12-JUL-05 03.09.04.041895 PM +08:00','PRIMARYSITE','Tongue, base, NOS (C019)',309,NULL,NULL,NULL,NULL,0,'ENG'),(1916,274,0,'12-JUL-05 03.09.04.042128 PM +08:00','PRIMARYSITE','Tongue, border (C021)',310,NULL,NULL,NULL,NULL,0,'ENG'),(1917,274,0,'12-JUL-05 03.09.04.042361 PM +08:00','PRIMARYSITE','Tongue, dorsal surface, NOS (C020)',311,NULL,NULL,NULL,NULL,0,'ENG'),(1918,274,0,'12-JUL-05 03.09.04.042608 PM +08:00','PRIMARYSITE','Tongue, lingual follicles (tonsil) (C024)',312,NULL,NULL,NULL,NULL,0,'ENG'),(1919,274,0,'12-JUL-05 03.09.04.042843 PM +08:00','PRIMARYSITE','Tongue, NOS (C029)',313,NULL,NULL,NULL,NULL,0,'ENG'),(1920,274,0,'12-JUL-05 03.09.04.043071 PM +08:00','PRIMARYSITE','Tongue, OL (C028a)',314,NULL,NULL,NULL,NULL,0,'ENG'),(1921,274,0,'12-JUL-05 03.09.04.043301 PM +08:00','PRIMARYSITE','Tongue, OL (junctional) (C028b)',315,NULL,NULL,NULL,NULL,0,'ENG'),(1922,274,0,'12-JUL-05 03.09.04.043540 PM +08:00','PRIMARYSITE','Tongue, ventral surface, NOS (C022)',316,NULL,NULL,NULL,NULL,0,'ENG'),(1923,274,0,'12-JUL-05 03.09.04.043775 PM +08:00','PRIMARYSITE','Tonsil, NOS (C099)',317,NULL,NULL,NULL,NULL,0,'ENG'),(1924,274,0,'12-JUL-05 03.09.04.044008 PM +08:00','PRIMARYSITE','Tonsil, OL (C098)',318,NULL,NULL,NULL,NULL,0,'ENG'),(1925,274,0,'12-JUL-05 03.09.04.044240 PM +08:00','PRIMARYSITE','Tonsillar fossa (C090)',319,NULL,NULL,NULL,NULL,0,'ENG'),(1926,274,0,'12-JUL-05 03.09.04.044477 PM +08:00','PRIMARYSITE','Tonsillar pillar (C091)',320,NULL,NULL,NULL,NULL,0,'ENG'),(1927,274,0,'12-JUL-05 03.09.04.044714 PM +08:00','PRIMARYSITE','Trachea (C339)',321,NULL,NULL,NULL,NULL,0,'ENG'),(1928,274,0,'12-JUL-05 03.09.04.013786 PM +08:00','PRIMARYSITE','Ovary (C569)',210,NULL,NULL,NULL,NULL,0,'ENG'),(1929,274,0,'12-JUL-05 03.09.04.014064 PM +08:00','PRIMARYSITE','Overlapping lesion of ill-defined sites (C768)',211,NULL,NULL,NULL,NULL,0,'ENG'),(1930,274,0,'12-JUL-05 03.09.04.014304 PM +08:00','PRIMARYSITE','Palate, hard (C050)',212,NULL,NULL,NULL,NULL,0,'ENG'),(1931,274,0,'12-JUL-05 03.09.04.014548 PM +08:00','PRIMARYSITE','Palate, NOS (C059)',213,NULL,NULL,NULL,NULL,0,'ENG'),(1932,274,0,'12-JUL-05 03.09.04.014785 PM +08:00','PRIMARYSITE','Palate, OL (C058)',214,NULL,NULL,NULL,NULL,0,'ENG'),(1933,274,0,'12-JUL-05 03.09.04.015020 PM +08:00','PRIMARYSITE','Palate, soft, NOS (C051)',215,NULL,NULL,NULL,NULL,0,'ENG'),(1934,274,0,'12-JUL-05 03.09.04.015253 PM +08:00','PRIMARYSITE','Pancreas, body (C251)',216,NULL,NULL,NULL,NULL,0,'ENG'),(1935,274,0,'12-JUL-05 03.09.04.015490 PM +08:00','PRIMARYSITE','Pancreas, head (C250)',217,NULL,NULL,NULL,NULL,0,'ENG'),(1936,274,0,'12-JUL-05 03.09.04.015727 PM +08:00','PRIMARYSITE','Pancreas, NOS (C259)',218,NULL,NULL,NULL,NULL,0,'ENG'),(1937,274,0,'12-JUL-05 03.09.04.015962 PM +08:00','PRIMARYSITE','Pancreas, OL (C258)',219,NULL,NULL,NULL,NULL,0,'ENG'),(1938,274,0,'12-JUL-05 03.09.04.016208 PM +08:00','PRIMARYSITE','Pancreas, other parts (C257)',220,NULL,NULL,NULL,NULL,0,'ENG'),(1939,274,0,'12-JUL-05 03.09.04.016465 PM +08:00','PRIMARYSITE','Pancreas, tail (C252)',221,NULL,NULL,NULL,NULL,0,'ENG'),(1940,274,0,'12-JUL-05 03.09.04.017209 PM +08:00','PRIMARYSITE','Pancreatic duct (C253)',222,NULL,NULL,NULL,NULL,0,'ENG'),(1941,274,0,'12-JUL-05 03.09.04.017473 PM +08:00','PRIMARYSITE','Parametrium (C573)',223,NULL,NULL,NULL,NULL,0,'ENG'),(1942,274,0,'12-JUL-05 03.09.04.017718 PM +08:00','PRIMARYSITE','Parathyroid gland (C750)',224,NULL,NULL,NULL,NULL,0,'ENG'),(1943,274,0,'12-JUL-05 03.09.04.017956 PM +08:00','PRIMARYSITE','Paraurethral gland (C681)',225,NULL,NULL,NULL,NULL,0,'ENG'),(1944,274,0,'12-JUL-05 03.09.04.018192 PM +08:00','PRIMARYSITE','Parietal lobe (C713)',226,NULL,NULL,NULL,NULL,0,'ENG'),(1945,274,0,'12-JUL-05 03.09.04.018429 PM +08:00','PRIMARYSITE','Parotid gland (C079)',227,NULL,NULL,NULL,NULL,0,'ENG'),(1946,274,0,'12-JUL-05 03.09.04.018671 PM +08:00','PRIMARYSITE','Pelvis, NOS (C763)',228,NULL,NULL,NULL,NULL,0,'ENG'),(1947,274,0,'12-JUL-05 03.09.04.018904 PM +08:00','PRIMARYSITE','Penis, body (C602)',229,NULL,NULL,NULL,NULL,0,'ENG'),(1948,274,0,'12-JUL-05 03.09.04.019137 PM +08:00','PRIMARYSITE','Penis, glans (C601)',230,NULL,NULL,NULL,NULL,0,'ENG'),(1949,274,0,'12-JUL-05 03.09.04.019374 PM +08:00','PRIMARYSITE','Penis, NOS (C609)',231,NULL,NULL,NULL,NULL,0,'ENG'),(1950,274,0,'12-JUL-05 03.09.04.019621 PM +08:00','PRIMARYSITE','Penis, OL (C608)',232,NULL,NULL,NULL,NULL,0,'ENG'),(1951,274,0,'12-JUL-05 03.09.04.019855 PM +08:00','PRIMARYSITE','Peritoneum, NOS (C482)',233,NULL,NULL,NULL,NULL,0,'ENG'),(1952,274,0,'12-JUL-05 03.09.04.020091 PM +08:00','PRIMARYSITE','Peritoneum, OL (C488)',234,NULL,NULL,NULL,NULL,0,'ENG'),(1953,274,0,'12-JUL-05 03.09.04.020326 PM +08:00','PRIMARYSITE','Peritoneum, specified part (C481)',235,NULL,NULL,NULL,NULL,0,'ENG'),(1954,274,0,'12-JUL-05 03.09.04.020568 PM +08:00','PRIMARYSITE','Pharynx, NOS (C140)',236,NULL,NULL,NULL,NULL,0,'ENG'),(1955,274,0,'12-JUL-05 03.09.04.020805 PM +08:00','PRIMARYSITE','Pharynx/lip/oral cavity, ill-def. site (C149)',237,NULL,NULL,NULL,NULL,0,'ENG'),(1956,274,0,'12-JUL-05 03.09.04.021090 PM +08:00','PRIMARYSITE','Pharynx/lip/oral cavity, OL (C148)',238,NULL,NULL,NULL,NULL,0,'ENG'),(1957,274,0,'12-JUL-05 03.09.04.021331 PM +08:00','PRIMARYSITE','Pineal gland (C753)',239,NULL,NULL,NULL,NULL,0,'ENG'),(1958,274,0,'12-JUL-05 03.09.04.021620 PM +08:00','PRIMARYSITE','Pituitary gland (C751)',240,NULL,NULL,NULL,NULL,0,'ENG'),(1959,274,0,'12-JUL-05 03.09.04.021862 PM +08:00','PRIMARYSITE','Placenta (C589)',241,NULL,NULL,NULL,NULL,0,'ENG'),(1960,274,0,'12-JUL-05 03.09.04.022578 PM +08:00','PRIMARYSITE','Pleura, NOS (C384c)',242,NULL,NULL,NULL,NULL,0,'ENG'),(1961,274,0,'12-JUL-05 03.09.04.022835 PM +08:00','PRIMARYSITE','Pleura, OL (C388a)',243,NULL,NULL,NULL,NULL,0,'ENG'),(1962,274,0,'12-JUL-05 03.09.04.023076 PM +08:00','PRIMARYSITE','Pleura, parietal (C384a)',244,NULL,NULL,NULL,NULL,0,'ENG'),(1963,274,0,'12-JUL-05 03.09.04.023309 PM +08:00','PRIMARYSITE','Pleura, visceral (C384b)',245,NULL,NULL,NULL,NULL,0,'ENG'),(1964,274,0,'12-JUL-05 03.09.04.023561 PM +08:00','PRIMARYSITE','Postcricoid region (C130)',246,NULL,NULL,NULL,NULL,0,'ENG'),(1965,274,0,'12-JUL-05 03.09.04.023798 PM +08:00','PRIMARYSITE','Prepuce (C600)',247,NULL,NULL,NULL,NULL,0,'ENG'),(1966,274,0,'12-JUL-05 03.09.04.024035 PM +08:00','PRIMARYSITE','Prostate gland (C619)',248,NULL,NULL,NULL,NULL,0,'ENG'),(1967,274,0,'12-JUL-05 03.09.04.024272 PM +08:00','PRIMARYSITE','Pylorus (C164)',249,NULL,NULL,NULL,NULL,0,'ENG'),(1968,274,0,'12-JUL-05 03.09.04.024519 PM +08:00','PRIMARYSITE','Pyriform sinus (fossa) (C129)',250,NULL,NULL,NULL,NULL,0,'ENG'),(1969,274,0,'12-JUL-05 03.09.04.024759 PM +08:00','PRIMARYSITE','Rectosigmoid junction (C199)',251,NULL,NULL,NULL,NULL,0,'ENG'),(1970,274,0,'12-JUL-05 03.09.04.024997 PM +08:00','PRIMARYSITE','Rectum, NOS (C209)',252,NULL,NULL,NULL,NULL,0,'ENG'),(1971,274,0,'12-JUL-05 03.09.04.025233 PM +08:00','PRIMARYSITE','Rectum, other parts, OL (C218)',253,NULL,NULL,NULL,NULL,0,'ENG'),(1972,274,0,'12-JUL-05 03.09.04.025474 PM +08:00','PRIMARYSITE','Renal pelvis (C659)',254,NULL,NULL,NULL,NULL,0,'ENG'),(1973,274,0,'12-JUL-05 03.09.04.025715 PM +08:00','PRIMARYSITE','Respiratory tract, Ill-defined sites, NO (C399)',255,NULL,NULL,NULL,NULL,0,'ENG'),(1974,274,0,'12-JUL-05 03.09.04.025952 PM +08:00','PRIMARYSITE','Respiratory tract, other parts (C398)',256,NULL,NULL,NULL,NULL,0,'ENG'),(1975,274,0,'12-JUL-05 03.09.04.026189 PM +08:00','PRIMARYSITE','Respiratory tract, upper, NOS (C390)',257,NULL,NULL,NULL,NULL,0,'ENG'),(1976,274,0,'12-JUL-05 03.09.04.026428 PM +08:00','PRIMARYSITE','Reticuloendothelial system, NOS (C423)',258,NULL,NULL,NULL,NULL,0,'ENG'),(1977,274,0,'12-JUL-05 03.09.04.026668 PM +08:00','PRIMARYSITE','Retina (C692)',259,NULL,NULL,NULL,NULL,0,'ENG'),(1978,274,0,'12-JUL-05 03.09.04.026906 PM +08:00','PRIMARYSITE','Retromolar area (C062)',260,NULL,NULL,NULL,NULL,0,'ENG'),(1979,274,0,'12-JUL-05 03.09.04.027139 PM +08:00','PRIMARYSITE','Retroperitoneum (C480)',261,NULL,NULL,NULL,NULL,0,'ENG'),(1980,274,0,'12-JUL-05 03.09.04.027850 PM +08:00','PRIMARYSITE','Rib, sternum, clavicle (C413)',262,NULL,NULL,NULL,NULL,0,'ENG'),(1981,274,0,'12-JUL-05 03.09.04.028106 PM +08:00','PRIMARYSITE','Round ligament (C572)',263,NULL,NULL,NULL,NULL,0,'ENG'),(1982,274,0,'12-JUL-05 03.09.04.028345 PM +08:00','PRIMARYSITE','Salivary gland, major, NOS (C089)',264,NULL,NULL,NULL,NULL,0,'ENG'),(1983,274,0,'12-JUL-05 03.09.04.028592 PM +08:00','PRIMARYSITE','Salivary glands, OL (C088)',265,NULL,NULL,NULL,NULL,0,'ENG'),(1984,274,0,'12-JUL-05 03.09.04.028829 PM +08:00','PRIMARYSITE','Scrotum, NOS (C632)',266,NULL,NULL,NULL,NULL,0,'ENG'),(1985,274,0,'12-JUL-05 03.09.04.029066 PM +08:00','PRIMARYSITE','Skin of lip, NOS (C440)',267,NULL,NULL,NULL,NULL,0,'ENG'),(1986,274,0,'12-JUL-05 03.09.04.029303 PM +08:00','PRIMARYSITE','Skin, arm, shoulder (C446)',268,NULL,NULL,NULL,NULL,0,'ENG'),(1987,274,0,'12-JUL-05 03.09.04.029547 PM +08:00','PRIMARYSITE','Skin, face, other parts, NOS (C443)',269,NULL,NULL,NULL,NULL,0,'ENG'),(1988,274,0,'12-JUL-05 03.09.04.029785 PM +08:00','PRIMARYSITE','Skin, leg, hip (C447)',270,NULL,NULL,NULL,NULL,0,'ENG'),(1989,274,0,'12-JUL-05 03.09.04.030018 PM +08:00','PRIMARYSITE','Skin, NOS (C449)',271,NULL,NULL,NULL,NULL,0,'ENG'),(1990,274,0,'12-JUL-05 03.09.04.030253 PM +08:00','PRIMARYSITE','Skin, OL (C448)',272,NULL,NULL,NULL,NULL,0,'ENG'),(1991,274,0,'12-JUL-05 03.09.04.030521 PM +08:00','PRIMARYSITE','Skin, scalp, neck (C444)',273,NULL,NULL,NULL,NULL,0,'ENG'),(1992,274,0,'12-JUL-05 03.09.04.030772 PM +08:00','PRIMARYSITE','Skin, trunk (C445)',274,NULL,NULL,NULL,NULL,0,'ENG'),(1993,274,0,'12-JUL-05 03.09.04.031128 PM +08:00','PRIMARYSITE','Small intestine (C179)',275,NULL,NULL,NULL,NULL,0,'ENG'),(1994,274,0,'12-JUL-05 03.09.04.031456 PM +08:00','PRIMARYSITE','Small intestine, OL (C178)',276,NULL,NULL,NULL,NULL,0,'ENG'),(1995,274,0,'12-JUL-05 03.09.04.031715 PM +08:00','PRIMARYSITE','Soft tissue, OL (C498)',277,NULL,NULL,NULL,NULL,0,'ENG'),(1996,274,0,'12-JUL-05 03.09.04.031953 PM +08:00','PRIMARYSITE','Soft tissues, abdomen (C494)',278,NULL,NULL,NULL,NULL,0,'ENG'),(1997,274,0,'12-JUL-05 03.09.04.032247 PM +08:00','PRIMARYSITE','Soft tissues, head face & neck (C490)',279,NULL,NULL,NULL,NULL,0,'ENG'),(1998,274,0,'12-JUL-05 03.09.04.032545 PM +08:00','PRIMARYSITE','Soft tissues, lower limb and hip (C492)',280,NULL,NULL,NULL,NULL,0,'ENG'),(1999,274,0,'12-JUL-05 03.09.03.995214 PM +08:00','PRIMARYSITE','Lung, middle lobe (C342)',142,NULL,NULL,NULL,NULL,0,'ENG'),(2000,274,0,'12-JUL-05 03.09.03.995527 PM +08:00','PRIMARYSITE','Lung, NOS (C349)',143,NULL,NULL,NULL,NULL,0,'ENG'),(2001,274,0,'12-JUL-05 03.09.03.995773 PM +08:00','PRIMARYSITE','Lung, upper lobe (C341)',144,NULL,NULL,NULL,NULL,0,'ENG'),(2002,274,0,'12-JUL-05 03.09.03.996011 PM +08:00','PRIMARYSITE','Lymph node, NOS (C779)',145,NULL,NULL,NULL,NULL,0,'ENG'),(2003,274,0,'12-JUL-05 03.09.03.996245 PM +08:00','PRIMARYSITE','Lymph nodes of head, face, neck (C770)',146,NULL,NULL,NULL,NULL,0,'ENG'),(2004,274,0,'12-JUL-05 03.09.03.996484 PM +08:00','PRIMARYSITE','Lymph nodes, axilla, arm (C773)',147,NULL,NULL,NULL,NULL,0,'ENG'),(2005,274,0,'12-JUL-05 03.09.03.996727 PM +08:00','PRIMARYSITE','Lymph nodes, inguinal, leg (C774)',148,NULL,NULL,NULL,NULL,0,'ENG'),(2006,274,0,'12-JUL-05 03.09.03.996963 PM +08:00','PRIMARYSITE','Lymph nodes, intra-abdominal (C772)',149,NULL,NULL,NULL,NULL,0,'ENG'),(2007,274,0,'12-JUL-05 03.09.03.997197 PM +08:00','PRIMARYSITE','Lymph nodes, intrathoracic (C771)',150,NULL,NULL,NULL,NULL,0,'ENG'),(2008,274,0,'12-JUL-05 03.09.03.997448 PM +08:00','PRIMARYSITE','Lymph nodes, multiple regions (C778)',151,NULL,NULL,NULL,NULL,0,'ENG'),(2009,274,0,'12-JUL-05 03.09.03.997693 PM +08:00','PRIMARYSITE','Lymph nodes, pelvic (C775)',152,NULL,NULL,NULL,NULL,0,'ENG'),(2010,274,0,'12-JUL-05 03.09.03.997931 PM +08:00','PRIMARYSITE','Male genital organs, NOS (C639)',153,NULL,NULL,NULL,NULL,0,'ENG'),(2011,274,0,'12-JUL-05 03.09.03.998165 PM +08:00','PRIMARYSITE','Male genital organs, other parts (C637)',154,NULL,NULL,NULL,NULL,0,'ENG'),(2012,274,0,'12-JUL-05 03.09.03.998398 PM +08:00','PRIMARYSITE','Male genital organs, other parts, OL (C638)',155,NULL,NULL,NULL,NULL,0,'ENG'),(2013,274,0,'12-JUL-05 03.09.03.998644 PM +08:00','PRIMARYSITE','Mandible (C411)',156,NULL,NULL,NULL,NULL,0,'ENG'),(2014,274,0,'12-JUL-05 03.09.03.998880 PM +08:00','PRIMARYSITE','Maxillary sinus (C310)',157,NULL,NULL,NULL,NULL,0,'ENG'),(2015,274,0,'12-JUL-05 03.09.03.999114 PM +08:00','PRIMARYSITE','Meckel\'s diverticulum (C173)',158,NULL,NULL,NULL,NULL,0,'ENG'),(2016,274,0,'12-JUL-05 03.09.03.999344 PM +08:00','PRIMARYSITE','Mediastinum, anterior (C381)',159,NULL,NULL,NULL,NULL,0,'ENG'),(2017,274,0,'12-JUL-05 03.09.03.999587 PM +08:00','PRIMARYSITE','Mediastinum, NOS (C383)',160,NULL,NULL,NULL,NULL,0,'ENG'),(2018,274,0,'12-JUL-05 03.09.03.999826 PM +08:00','PRIMARYSITE','Mediastinum, OL (thymus/hrt/pleura/lung) (C388b)',161,NULL,NULL,NULL,NULL,0,'ENG'),(2019,274,0,'12-JUL-05 03.09.04.000587 PM +08:00','PRIMARYSITE','Mediastinum, posterior (C382)',162,NULL,NULL,NULL,NULL,0,'ENG'),(2020,274,0,'12-JUL-05 03.09.04.000931 PM +08:00','PRIMARYSITE','Meninges, cerebral (C700)',163,NULL,NULL,NULL,NULL,0,'ENG'),(2021,274,0,'12-JUL-05 03.09.04.001188 PM +08:00','PRIMARYSITE','Meninges, NOS (C709)',164,NULL,NULL,NULL,NULL,0,'ENG'),(2022,274,0,'12-JUL-05 03.09.04.001433 PM +08:00','PRIMARYSITE','Meninges, spinal (C701)',165,NULL,NULL,NULL,NULL,0,'ENG'),(2023,274,0,'12-JUL-05 03.09.04.001676 PM +08:00','PRIMARYSITE','Middle ear (C301)',166,NULL,NULL,NULL,NULL,0,'ENG'),(2024,274,0,'12-JUL-05 03.09.04.001913 PM +08:00','PRIMARYSITE','Mouth, anterior floor (C040)',167,NULL,NULL,NULL,NULL,0,'ENG'),(2025,274,0,'12-JUL-05 03.09.04.002148 PM +08:00','PRIMARYSITE','Mouth, floor, NOS (C049)',168,NULL,NULL,NULL,NULL,0,'ENG'),(2026,274,0,'12-JUL-05 03.09.04.002382 PM +08:00','PRIMARYSITE','Mouth, floor, OL (C048)',169,NULL,NULL,NULL,NULL,0,'ENG'),(2027,274,0,'12-JUL-05 03.09.04.002627 PM +08:00','PRIMARYSITE','Mouth, lateral floor (C041)',170,NULL,NULL,NULL,NULL,0,'ENG'),(2028,274,0,'12-JUL-05 03.09.04.002860 PM +08:00','PRIMARYSITE','Mouth, non-floor, OL (C068)',171,NULL,NULL,NULL,NULL,0,'ENG'),(2029,274,0,'12-JUL-05 03.09.04.003092 PM +08:00','PRIMARYSITE','Mouth, oral cavity, NOS (C069)',172,NULL,NULL,NULL,NULL,0,'ENG'),(2030,274,0,'12-JUL-05 03.09.04.003327 PM +08:00','PRIMARYSITE','Mouth, vestibule (C061)',173,NULL,NULL,NULL,NULL,0,'ENG'),(2031,274,0,'12-JUL-05 03.09.04.003649 PM +08:00','PRIMARYSITE','Myometrium, uterus (C542)',174,NULL,NULL,NULL,NULL,0,'ENG'),(2032,274,0,'12-JUL-05 03.09.04.004085 PM +08:00','PRIMARYSITE','Nasal cavity (C300)',175,NULL,NULL,NULL,NULL,0,'ENG'),(2033,274,0,'12-JUL-05 03.09.04.004434 PM +08:00','PRIMARYSITE','Nasopharynx, anterior wall (C113)',176,NULL,NULL,NULL,NULL,0,'ENG'),(2034,274,0,'12-JUL-05 03.09.04.004732 PM +08:00','PRIMARYSITE','Nasopharynx, lateral wall (C112)',177,NULL,NULL,NULL,NULL,0,'ENG'),(2035,274,0,'12-JUL-05 03.09.04.004971 PM +08:00','PRIMARYSITE','Nasopharynx, NOS (C119)',178,NULL,NULL,NULL,NULL,0,'ENG'),(2036,274,0,'12-JUL-05 03.09.04.005208 PM +08:00','PRIMARYSITE','Nasopharynx, OL (C118)',179,NULL,NULL,NULL,NULL,0,'ENG'),(2037,274,0,'12-JUL-05 03.09.04.005448 PM +08:00','PRIMARYSITE','Nasopharynx, posterior wall (C111)',180,NULL,NULL,NULL,NULL,0,'ENG'),(2038,274,0,'12-JUL-05 03.09.04.005691 PM +08:00','PRIMARYSITE','Nasopharynx, superior wall (C110)',181,NULL,NULL,NULL,NULL,0,'ENG'),(2039,274,0,'12-JUL-05 03.09.04.006488 PM +08:00','PRIMARYSITE','Nerves & ANS, abdomen (C474)',182,NULL,NULL,NULL,NULL,0,'ENG'),(2040,274,0,'12-JUL-05 03.09.04.006804 PM +08:00','PRIMARYSITE','Nerves & ANS, head, face, neck (C470)',183,NULL,NULL,NULL,NULL,0,'ENG'),(2041,274,0,'12-JUL-05 03.09.04.007049 PM +08:00','PRIMARYSITE','Nerves & ANS, lower limb, hip (C472)',184,NULL,NULL,NULL,NULL,0,'ENG'),(2042,274,0,'12-JUL-05 03.09.04.007285 PM +08:00','PRIMARYSITE','Nerves & ANS, OL (C478)',185,NULL,NULL,NULL,NULL,0,'ENG'),(2043,274,0,'12-JUL-05 03.09.04.007536 PM +08:00','PRIMARYSITE','Nerves & ANS, pelvis (C475)',186,NULL,NULL,NULL,NULL,0,'ENG'),(2044,274,0,'12-JUL-05 03.09.04.007769 PM +08:00','PRIMARYSITE','Nerves & ANS, thorax (C473)',187,NULL,NULL,NULL,NULL,0,'ENG'),(2045,274,0,'12-JUL-05 03.09.04.008000 PM +08:00','PRIMARYSITE','Nerves & ANS, trunk (C476)',188,NULL,NULL,NULL,NULL,0,'ENG'),(2046,274,0,'12-JUL-05 03.09.04.008235 PM +08:00','PRIMARYSITE','Nerves & ANS, upper limb, shoulder (C471)',189,NULL,NULL,NULL,NULL,0,'ENG'),(2047,274,0,'12-JUL-05 03.09.04.008476 PM +08:00','PRIMARYSITE','Nervous system, non-brain, OL (C728)',190,NULL,NULL,NULL,NULL,0,'ENG'),(2048,274,0,'12-JUL-05 03.09.04.008718 PM +08:00','PRIMARYSITE','Nervous system, NOS (C729)',191,NULL,NULL,NULL,NULL,0,'ENG'),(2049,274,0,'12-JUL-05 03.09.04.008953 PM +08:00','PRIMARYSITE','Nipple (C500)',192,NULL,NULL,NULL,NULL,0,'ENG'),(2050,274,0,'12-JUL-05 03.09.04.009185 PM +08:00','PRIMARYSITE','Occipital lobe (C714)',193,NULL,NULL,NULL,NULL,0,'ENG'),(2051,274,0,'12-JUL-05 03.09.04.009423 PM +08:00','PRIMARYSITE','Oesophagus, abdominal (C152)',194,NULL,NULL,NULL,NULL,0,'ENG'),(2052,274,0,'12-JUL-05 03.09.04.009664 PM +08:00','PRIMARYSITE','Oesophagus, cervical (C150)',195,NULL,NULL,NULL,NULL,0,'ENG'),(2053,274,0,'12-JUL-05 03.09.04.009899 PM +08:00','PRIMARYSITE','Oesophagus, lower third (C155)',196,NULL,NULL,NULL,NULL,0,'ENG'),(2054,274,0,'12-JUL-05 03.09.04.010134 PM +08:00','PRIMARYSITE','Oesophagus, middle third (C154)',197,NULL,NULL,NULL,NULL,0,'ENG'),(2055,274,0,'12-JUL-05 03.09.04.010369 PM +08:00','PRIMARYSITE','Oesophagus, NOS (C159)',198,NULL,NULL,NULL,NULL,0,'ENG'),(2056,274,0,'12-JUL-05 03.09.04.010616 PM +08:00','PRIMARYSITE','Oesophagus, OL (C158)',199,NULL,NULL,NULL,NULL,0,'ENG'),(2057,274,0,'12-JUL-05 03.09.04.010852 PM +08:00','PRIMARYSITE','Oesophagus, thoracic (C151)',200,NULL,NULL,NULL,NULL,0,'ENG'),(2058,274,0,'12-JUL-05 03.09.04.011088 PM +08:00','PRIMARYSITE','Oesophagus, upper third (C153)',201,NULL,NULL,NULL,NULL,0,'ENG'),(2059,274,0,'12-JUL-05 03.09.04.011816 PM +08:00','PRIMARYSITE','Olfactory nerve (C722)',202,NULL,NULL,NULL,NULL,0,'ENG'),(2060,274,0,'12-JUL-05 03.09.04.012071 PM +08:00','PRIMARYSITE','Optic nerve (C723)',203,NULL,NULL,NULL,NULL,0,'ENG'),(2061,274,0,'12-JUL-05 03.09.04.012333 PM +08:00','PRIMARYSITE','Orbit, NOS (C696)',204,NULL,NULL,NULL,NULL,0,'ENG'),(2062,274,0,'12-JUL-05 03.09.04.012593 PM +08:00','PRIMARYSITE','Oropharynx, lateral wall (C102)',205,NULL,NULL,NULL,NULL,0,'ENG'),(2063,274,0,'12-JUL-05 03.09.04.012833 PM +08:00','PRIMARYSITE','Oropharynx, NOS (C109)',206,NULL,NULL,NULL,NULL,0,'ENG'),(2064,274,0,'12-JUL-05 03.09.04.013069 PM +08:00','PRIMARYSITE','Oropharynx, OL (junctional) (C108a)',207,NULL,NULL,NULL,NULL,0,'ENG'),(2065,274,0,'12-JUL-05 03.09.04.013304 PM +08:00','PRIMARYSITE','Oropharynx, other parts(branchial cleft) (C108b)',208,NULL,NULL,NULL,NULL,0,'ENG'),(2066,274,0,'12-JUL-05 03.09.04.013549 PM +08:00','PRIMARYSITE','Oropharynx, posterior wall (C103)',209,NULL,NULL,NULL,NULL,0,'ENG'),(2067,274,0,'12-JUL-05 03.09.03.974849 PM +08:00','PRIMARYSITE','Colon, OL (C188)',70,NULL,NULL,NULL,NULL,0,'ENG'),(2068,274,0,'12-JUL-05 03.09.03.975142 PM +08:00','PRIMARYSITE','Colon, sigmoid (C187)',71,NULL,NULL,NULL,NULL,0,'ENG'),(2069,274,0,'12-JUL-05 03.09.03.975379 PM +08:00','PRIMARYSITE','Colon, splenic flexure (C185)',72,NULL,NULL,NULL,NULL,0,'ENG'),(2070,274,0,'12-JUL-05 03.09.03.975674 PM +08:00','PRIMARYSITE','Colon, transverse (C184)',73,NULL,NULL,NULL,NULL,0,'ENG'),(2071,274,0,'12-JUL-05 03.09.03.975916 PM +08:00','PRIMARYSITE','Conjunctiva (C690)',74,NULL,NULL,NULL,NULL,0,'ENG'),(2072,274,0,'12-JUL-05 03.09.03.976157 PM +08:00','PRIMARYSITE','Cornea, NOS (C691)',75,NULL,NULL,NULL,NULL,0,'ENG'),(2073,274,0,'12-JUL-05 03.09.03.976391 PM +08:00','PRIMARYSITE','Cranial nerve (C725)',76,NULL,NULL,NULL,NULL,0,'ENG'),(2074,274,0,'12-JUL-05 03.09.03.976730 PM +08:00','PRIMARYSITE','Craniopharyngeal duct (C752)',77,NULL,NULL,NULL,NULL,0,'ENG'),(2075,274,0,'12-JUL-05 03.09.03.977112 PM +08:00','PRIMARYSITE','Duodenum (C170)',78,NULL,NULL,NULL,NULL,0,'ENG'),(2076,274,0,'12-JUL-05 03.09.03.977384 PM +08:00','PRIMARYSITE','Endocervix (C530)',79,NULL,NULL,NULL,NULL,0,'ENG'),(2077,274,0,'12-JUL-05 03.09.03.977649 PM +08:00','PRIMARYSITE','Endocrine gland, NOS (C759)',80,NULL,NULL,NULL,NULL,0,'ENG'),(2078,274,0,'12-JUL-05 03.09.03.977887 PM +08:00','PRIMARYSITE','Endocrine, multiple glands (C758)',81,NULL,NULL,NULL,NULL,0,'ENG'),(2079,274,0,'12-JUL-05 03.09.03.978770 PM +08:00','PRIMARYSITE','Endometrium, uterus (C541)',82,NULL,NULL,NULL,NULL,0,'ENG'),(2080,274,0,'12-JUL-05 03.09.03.979039 PM +08:00','PRIMARYSITE','Epididymis (C630)',83,NULL,NULL,NULL,NULL,0,'ENG'),(2081,274,0,'12-JUL-05 03.09.03.979278 PM +08:00','PRIMARYSITE','Epiglottis, anterior surface (C101)',84,NULL,NULL,NULL,NULL,0,'ENG'),(2082,274,0,'12-JUL-05 03.09.03.979527 PM +08:00','PRIMARYSITE','Ethmoid sinus (C311)',85,NULL,NULL,NULL,NULL,0,'ENG'),(2083,274,0,'12-JUL-05 03.09.03.979764 PM +08:00','PRIMARYSITE','Exocervix (C531)',86,NULL,NULL,NULL,NULL,0,'ENG'),(2084,274,0,'12-JUL-05 03.09.03.980000 PM +08:00','PRIMARYSITE','External ear (C442)',87,NULL,NULL,NULL,NULL,0,'ENG'),(2085,274,0,'12-JUL-05 03.09.03.980236 PM +08:00','PRIMARYSITE','Eye, NOS (C699)',88,NULL,NULL,NULL,NULL,0,'ENG'),(2086,274,0,'12-JUL-05 03.09.03.980476 PM +08:00','PRIMARYSITE','Eye/lacrimal gland, OL (C698)',89,NULL,NULL,NULL,NULL,0,'ENG'),(2087,274,0,'12-JUL-05 03.09.03.980716 PM +08:00','PRIMARYSITE','Eyeball (C694)',90,NULL,NULL,NULL,NULL,0,'ENG'),(2088,274,0,'12-JUL-05 03.09.03.980953 PM +08:00','PRIMARYSITE','Eyelid (C441)',91,NULL,NULL,NULL,NULL,0,'ENG'),(2089,274,0,'12-JUL-05 03.09.03.981188 PM +08:00','PRIMARYSITE','Fallopian tube (C570)',92,NULL,NULL,NULL,NULL,0,'ENG'),(2090,274,0,'12-JUL-05 03.09.03.981436 PM +08:00','PRIMARYSITE','Female genital organs, other parts (C577)',93,NULL,NULL,NULL,NULL,0,'ENG'),(2091,274,0,'12-JUL-05 03.09.03.981700 PM +08:00','PRIMARYSITE','Female genital, NOS (C579)',94,NULL,NULL,NULL,NULL,0,'ENG'),(2092,274,0,'12-JUL-05 03.09.03.981938 PM +08:00','PRIMARYSITE','Female genital, NOS, OL (C578b)',95,NULL,NULL,NULL,NULL,0,'ENG'),(2093,274,0,'12-JUL-05 03.09.03.982173 PM +08:00','PRIMARYSITE','Frontal lobe (C711)',96,NULL,NULL,NULL,NULL,0,'ENG'),(2094,274,0,'12-JUL-05 03.09.03.982409 PM +08:00','PRIMARYSITE','Frontal sinus (C312)',97,NULL,NULL,NULL,NULL,0,'ENG'),(2095,274,0,'12-JUL-05 03.09.03.982654 PM +08:00','PRIMARYSITE','Fundus, uterus (C543)',98,NULL,NULL,NULL,NULL,0,'ENG'),(2096,274,0,'12-JUL-05 03.09.03.982889 PM +08:00','PRIMARYSITE','Gallbladder (C239)',99,NULL,NULL,NULL,NULL,0,'ENG'),(2097,274,0,'12-JUL-05 03.09.03.983124 PM +08:00','PRIMARYSITE','Gastrointestinal tract, NOS (C269)',100,NULL,NULL,NULL,NULL,0,'ENG'),(2098,274,0,'12-JUL-05 03.09.03.983357 PM +08:00','PRIMARYSITE','Gastrointestinal tract, other parts, OL (C268)',101,NULL,NULL,NULL,NULL,0,'ENG'),(2099,274,0,'12-JUL-05 03.09.03.984083 PM +08:00','PRIMARYSITE','Glottis (C320)',102,NULL,NULL,NULL,NULL,0,'ENG'),(2100,274,0,'12-JUL-05 03.09.03.984337 PM +08:00','PRIMARYSITE','Gum, lower (C031)',103,NULL,NULL,NULL,NULL,0,'ENG'),(2101,274,0,'12-JUL-05 03.09.03.984594 PM +08:00','PRIMARYSITE','Gum, NOS (C039)',104,NULL,NULL,NULL,NULL,0,'ENG'),(2102,274,0,'12-JUL-05 03.09.03.984831 PM +08:00','PRIMARYSITE','Gum, OL (C038)',105,NULL,NULL,NULL,NULL,0,'ENG'),(2103,274,0,'12-JUL-05 03.09.03.985065 PM +08:00','PRIMARYSITE','Gum, upper (C030)',106,NULL,NULL,NULL,NULL,0,'ENG'),(2104,274,0,'12-JUL-05 03.09.03.985299 PM +08:00','PRIMARYSITE','Head, face, neck, NOS (C760)',107,NULL,NULL,NULL,NULL,0,'ENG'),(2105,274,0,'12-JUL-05 03.09.03.985546 PM +08:00','PRIMARYSITE','Heart (C380)',108,NULL,NULL,NULL,NULL,0,'ENG'),(2106,274,0,'12-JUL-05 03.09.03.985781 PM +08:00','PRIMARYSITE','Hematopoietic system, NOS (C424)',109,NULL,NULL,NULL,NULL,0,'ENG'),(2107,274,0,'12-JUL-05 03.09.03.986014 PM +08:00','PRIMARYSITE','Hypopharynx, NOS (C139)',110,NULL,NULL,NULL,NULL,0,'ENG'),(2108,274,0,'12-JUL-05 03.09.03.986248 PM +08:00','PRIMARYSITE','Hypopharynx, OL (C138)',111,NULL,NULL,NULL,NULL,0,'ENG'),(2109,274,0,'12-JUL-05 03.09.03.986488 PM +08:00','PRIMARYSITE','Hypopharynx, posterior wall (C132)',112,NULL,NULL,NULL,NULL,0,'ENG'),(2110,274,0,'12-JUL-05 03.09.03.986729 PM +08:00','PRIMARYSITE','Ileum (C172)',113,NULL,NULL,NULL,NULL,0,'ENG'),(2111,274,0,'12-JUL-05 03.09.03.986963 PM +08:00','PRIMARYSITE','Ill-defined, other sites (C767)',114,NULL,NULL,NULL,NULL,0,'ENG'),(2112,274,0,'12-JUL-05 03.09.03.987195 PM +08:00','PRIMARYSITE','Intestinal tract, NOS (C260)',115,NULL,NULL,NULL,NULL,0,'ENG'),(2113,274,0,'12-JUL-05 03.09.03.987433 PM +08:00','PRIMARYSITE','Islets of Langerhans (pancreas) (C254)',116,NULL,NULL,NULL,NULL,0,'ENG'),(2114,274,0,'12-JUL-05 03.09.03.987673 PM +08:00','PRIMARYSITE','Jejunum (C171)',117,NULL,NULL,NULL,NULL,0,'ENG'),(2115,274,0,'12-JUL-05 03.09.03.987908 PM +08:00','PRIMARYSITE','Kidney, NOS (C649)',118,NULL,NULL,NULL,NULL,0,'ENG'),(2116,274,0,'12-JUL-05 03.09.03.988143 PM +08:00','PRIMARYSITE','Kidney/other urinary organs, OL (C688)',119,NULL,NULL,NULL,NULL,0,'ENG'),(2117,274,0,'12-JUL-05 03.09.03.988377 PM +08:00','PRIMARYSITE','Labium majus (C510)',120,NULL,NULL,NULL,NULL,0,'ENG'),(2118,274,0,'12-JUL-05 03.09.03.988625 PM +08:00','PRIMARYSITE','Labium minus (C511)',121,NULL,NULL,NULL,NULL,0,'ENG'),(2119,274,0,'12-JUL-05 03.09.03.989331 PM +08:00','PRIMARYSITE','Lacrimal duct, NOS (C695b)',122,NULL,NULL,NULL,NULL,0,'ENG'),(2120,274,0,'12-JUL-05 03.09.03.989645 PM +08:00','PRIMARYSITE','Lacrimal gland (C695a)',123,NULL,NULL,NULL,NULL,0,'ENG'),(2121,274,0,'12-JUL-05 03.09.03.989886 PM +08:00','PRIMARYSITE','Laryngeal cartilage (C323)',124,NULL,NULL,NULL,NULL,0,'ENG'),(2122,274,0,'12-JUL-05 03.09.03.990122 PM +08:00','PRIMARYSITE','Laryngopharynx (C141)',125,NULL,NULL,NULL,NULL,0,'ENG'),(2123,274,0,'12-JUL-05 03.09.03.990355 PM +08:00','PRIMARYSITE','Larynx, NOS (C329)',126,NULL,NULL,NULL,NULL,0,'ENG'),(2124,274,0,'12-JUL-05 03.09.03.990599 PM +08:00','PRIMARYSITE','Larynx, OL (C328)',127,NULL,NULL,NULL,NULL,0,'ENG'),(2125,274,0,'12-JUL-05 03.09.03.990899 PM +08:00','PRIMARYSITE','Limb lower, NOS (C765)',128,NULL,NULL,NULL,NULL,0,'ENG'),(2126,274,0,'12-JUL-05 03.09.03.991137 PM +08:00','PRIMARYSITE','Limb upper, NOS (C764)',129,NULL,NULL,NULL,NULL,0,'ENG'),(2127,274,0,'12-JUL-05 03.09.03.991364 PM +08:00','PRIMARYSITE','Lip, commissure (C006)',130,NULL,NULL,NULL,NULL,0,'ENG'),(2128,274,0,'12-JUL-05 03.09.03.991606 PM +08:00','PRIMARYSITE','Lip, external, NOS (C002)',131,NULL,NULL,NULL,NULL,0,'ENG'),(2129,274,0,'12-JUL-05 03.09.03.991841 PM +08:00','PRIMARYSITE','Lip, lower, external, NOS (C001)',132,NULL,NULL,NULL,NULL,0,'ENG'),(2130,274,0,'12-JUL-05 03.09.03.992076 PM +08:00','PRIMARYSITE','Lip, lower, mucosa (C004)',133,NULL,NULL,NULL,NULL,0,'ENG'),(2131,274,0,'12-JUL-05 03.09.03.992308 PM +08:00','PRIMARYSITE','Lip, mucosa, NOS (C005)',134,NULL,NULL,NULL,NULL,0,'ENG'),(2132,274,0,'12-JUL-05 03.09.03.992606 PM +08:00','PRIMARYSITE','Lip, NOS (C009)',135,NULL,NULL,NULL,NULL,0,'ENG'),(2133,274,0,'12-JUL-05 03.09.03.992872 PM +08:00','PRIMARYSITE','Lip, OL (C008)',136,NULL,NULL,NULL,NULL,0,'ENG'),(2134,274,0,'12-JUL-05 03.09.03.993108 PM +08:00','PRIMARYSITE','Lip, upper, external, NOS (C000)',137,NULL,NULL,NULL,NULL,0,'ENG'),(2135,274,0,'12-JUL-05 03.09.03.993457 PM +08:00','PRIMARYSITE','Lip, upper, mucosa (C003)',138,NULL,NULL,NULL,NULL,0,'ENG'),(2136,274,0,'12-JUL-05 03.09.03.993855 PM +08:00','PRIMARYSITE','Liver (C220)',139,NULL,NULL,NULL,NULL,0,'ENG'),(2137,274,0,'12-JUL-05 03.09.03.994141 PM +08:00','PRIMARYSITE','Lung or bronchus, other parts, OL (C348)',140,NULL,NULL,NULL,NULL,0,'ENG'),(2138,274,0,'12-JUL-05 03.09.03.994383 PM +08:00','PRIMARYSITE','Lung, lower lobe (C343)',141,NULL,NULL,NULL,NULL,0,'ENG'),(2139,274,0,'12-JUL-05 03.09.03.950039 PM +08:00','PRIMARYSITE','Abdomen, NOS (C762)',1,NULL,NULL,NULL,NULL,0,'ENG'),(2140,274,0,'12-JUL-05 03.09.03.955622 PM +08:00','PRIMARYSITE','Accessory sinus, NOS (C319)',2,NULL,NULL,NULL,NULL,0,'ENG'),(2141,274,0,'12-JUL-05 03.09.03.956932 PM +08:00','PRIMARYSITE','Accessory Sinuses, OL (nasal cavity/ear) (C318)',3,NULL,NULL,NULL,NULL,0,'ENG'),(2142,274,0,'12-JUL-05 03.09.03.957324 PM +08:00','PRIMARYSITE','Acoustic nerve (C724)',4,NULL,NULL,NULL,NULL,0,'ENG'),(2143,274,0,'12-JUL-05 03.09.03.957594 PM +08:00','PRIMARYSITE','Adrenal gland (C749)',5,NULL,NULL,NULL,NULL,0,'ENG'),(2144,274,0,'12-JUL-05 03.09.03.957839 PM +08:00','PRIMARYSITE','Adrenal gland, cortex (C740)',6,NULL,NULL,NULL,NULL,0,'ENG'),(2145,274,0,'12-JUL-05 03.09.03.958076 PM +08:00','PRIMARYSITE','Adrenal gland, medulla (C741)',7,NULL,NULL,NULL,NULL,0,'ENG'),(2146,274,0,'12-JUL-05 03.09.03.958311 PM +08:00','PRIMARYSITE','Ampulla of Vater (C241)',8,NULL,NULL,NULL,NULL,0,'ENG'),(2147,274,0,'12-JUL-05 03.09.03.958557 PM +08:00','PRIMARYSITE','Anal canal (sphincter) (C211)',9,NULL,NULL,NULL,NULL,0,'ENG'),(2148,274,0,'12-JUL-05 03.09.03.958822 PM +08:00','PRIMARYSITE','Anus, NOS (C210)',10,NULL,NULL,NULL,NULL,0,'ENG'),(2149,274,0,'12-JUL-05 03.09.03.959072 PM +08:00','PRIMARYSITE','Aortic body and other paraganglia (C755)',11,NULL,NULL,NULL,NULL,0,'ENG'),(2150,274,0,'12-JUL-05 03.09.03.959310 PM +08:00','PRIMARYSITE','Appendix (C181)',12,NULL,NULL,NULL,NULL,0,'ENG'),(2151,274,0,'12-JUL-05 03.09.03.959555 PM +08:00','PRIMARYSITE','Aryepiglottic fold, NOS (C131)',13,NULL,NULL,NULL,NULL,0,'ENG'),(2152,274,0,'12-JUL-05 03.09.03.959794 PM +08:00','PRIMARYSITE','Autonomic nervous system (C479)',14,NULL,NULL,NULL,NULL,0,'ENG'),(2153,274,0,'12-JUL-05 03.09.03.960031 PM +08:00','PRIMARYSITE','Bile duct, extrahepatic (C240)',15,NULL,NULL,NULL,NULL,0,'ENG'),(2154,274,0,'12-JUL-05 03.09.03.960263 PM +08:00','PRIMARYSITE','Bile duct, Intrahepatic (C221)',16,NULL,NULL,NULL,NULL,0,'ENG'),(2155,274,0,'12-JUL-05 03.09.03.960511 PM +08:00','PRIMARYSITE','Biliary tract, NOS (C249)',17,NULL,NULL,NULL,NULL,0,'ENG'),(2156,274,0,'12-JUL-05 03.09.03.960813 PM +08:00','PRIMARYSITE','Biliary tract, OL (C248)',18,NULL,NULL,NULL,NULL,0,'ENG'),(2157,274,0,'12-JUL-05 03.09.03.961055 PM +08:00','PRIMARYSITE','Bladder, anterior wall (C673)',19,NULL,NULL,NULL,NULL,0,'ENG'),(2158,274,0,'12-JUL-05 03.09.03.961291 PM +08:00','PRIMARYSITE','Bladder, dome (C671)',20,NULL,NULL,NULL,NULL,0,'ENG'),(2159,274,0,'12-JUL-05 03.09.03.961545 PM +08:00','PRIMARYSITE','Bladder, lateral wall (C672)',21,NULL,NULL,NULL,NULL,0,'ENG'),(2160,274,0,'12-JUL-05 03.09.03.962310 PM +08:00','PRIMARYSITE','Bladder, neck (C675)',22,NULL,NULL,NULL,NULL,0,'ENG'),(2161,274,0,'12-JUL-05 03.09.03.962639 PM +08:00','PRIMARYSITE','Bladder, NOS (C679)',23,NULL,NULL,NULL,NULL,0,'ENG'),(2162,274,0,'12-JUL-05 03.09.03.962882 PM +08:00','PRIMARYSITE','Bladder, OL (C678)',24,NULL,NULL,NULL,NULL,0,'ENG'),(2163,274,0,'12-JUL-05 03.09.03.963117 PM +08:00','PRIMARYSITE','Bladder, posterior wall (C674)',25,NULL,NULL,NULL,NULL,0,'ENG'),(2164,274,0,'12-JUL-05 03.09.03.963349 PM +08:00','PRIMARYSITE','Bladder, Trigone (C670)',26,NULL,NULL,NULL,NULL,0,'ENG'),(2165,274,0,'12-JUL-05 03.09.03.963594 PM +08:00','PRIMARYSITE','Blood (C420)',27,NULL,NULL,NULL,NULL,0,'ENG'),(2166,274,0,'12-JUL-05 03.09.03.963830 PM +08:00','PRIMARYSITE','Bone marrow (C421)',28,NULL,NULL,NULL,NULL,0,'ENG'),(2167,274,0,'12-JUL-05 03.09.03.964063 PM +08:00','PRIMARYSITE','Bone, limb, NOS (C409)',29,NULL,NULL,NULL,NULL,0,'ENG'),(2168,274,0,'12-JUL-05 03.09.03.964305 PM +08:00','PRIMARYSITE','Bone, NOS (C419)',30,NULL,NULL,NULL,NULL,0,'ENG'),(2169,274,0,'12-JUL-05 03.09.03.964552 PM +08:00','PRIMARYSITE','Bones of skull and face (C410)',31,NULL,NULL,NULL,NULL,0,'ENG'),(2170,274,0,'12-JUL-05 03.09.03.964790 PM +08:00','PRIMARYSITE','Bones, long of lower limb (+ joints) (C402)',32,NULL,NULL,NULL,NULL,0,'ENG'),(2171,274,0,'12-JUL-05 03.09.03.965024 PM +08:00','PRIMARYSITE','Bones, long of upper limb, scapula (+ jo (C400)',33,NULL,NULL,NULL,NULL,0,'ENG'),(2172,274,0,'12-JUL-05 03.09.03.965254 PM +08:00','PRIMARYSITE','Bones, pelvic , sacrum, coccyx (+ joints (C414)',34,NULL,NULL,NULL,NULL,0,'ENG'),(2173,274,0,'12-JUL-05 03.09.03.965491 PM +08:00','PRIMARYSITE','Bones, short of lower limb (+ joints) (C403)',35,NULL,NULL,NULL,NULL,0,'ENG'),(2174,274,0,'12-JUL-05 03.09.03.965727 PM +08:00','PRIMARYSITE','Bones, short of upper limb (+ joints) (C401)',36,NULL,NULL,NULL,NULL,0,'ENG'),(2175,274,0,'12-JUL-05 03.09.03.965968 PM +08:00','PRIMARYSITE','Bones/jounts/cartilage, limb, OL (C408)',37,NULL,NULL,NULL,NULL,0,'ENG'),(2176,274,0,'12-JUL-05 03.09.03.966203 PM +08:00','PRIMARYSITE','Bones/jounts/cartilage, non-limb, OL (C418)',38,NULL,NULL,NULL,NULL,0,'ENG'),(2177,274,0,'12-JUL-05 03.09.03.966444 PM +08:00','PRIMARYSITE','Brain stem (C717)',39,NULL,NULL,NULL,NULL,0,'ENG'),(2178,274,0,'12-JUL-05 03.09.03.966682 PM +08:00','PRIMARYSITE','Brain, NOS (C719)',40,NULL,NULL,NULL,NULL,0,'ENG'),(2179,274,0,'12-JUL-05 03.09.03.966915 PM +08:00','PRIMARYSITE','Brain, other parts, OL (C718)',41,NULL,NULL,NULL,NULL,0,'ENG'),(2180,274,0,'12-JUL-05 03.09.03.967660 PM +08:00','PRIMARYSITE','Branchial cleft (C104)',42,NULL,NULL,NULL,NULL,0,'ENG'),(2181,274,0,'12-JUL-05 03.09.03.967929 PM +08:00','PRIMARYSITE','Breast (female), NOS (C509a)',43,NULL,NULL,NULL,NULL,0,'ENG'),(2182,274,0,'12-JUL-05 03.09.03.968168 PM +08:00','PRIMARYSITE','Breast (male) NOS (WACR) (C509b)',44,NULL,NULL,NULL,NULL,0,'ENG'),(2183,274,0,'12-JUL-05 03.09.03.968403 PM +08:00','PRIMARYSITE','Breast, axillary tail (C506)',45,NULL,NULL,NULL,NULL,0,'ENG'),(2184,274,0,'12-JUL-05 03.09.03.968651 PM +08:00','PRIMARYSITE','Breast, central portion (C501)',46,NULL,NULL,NULL,NULL,0,'ENG'),(2185,274,0,'12-JUL-05 03.09.03.968886 PM +08:00','PRIMARYSITE','Breast, lower-inner quadrant (C503)',47,NULL,NULL,NULL,NULL,0,'ENG'),(2186,274,0,'12-JUL-05 03.09.03.969116 PM +08:00','PRIMARYSITE','Breast, lower-outer quadrant (C505)',48,NULL,NULL,NULL,NULL,0,'ENG'),(2187,274,0,'12-JUL-05 03.09.03.969357 PM +08:00','PRIMARYSITE','Breast, Other Parts, OL lesion (C508)',49,NULL,NULL,NULL,NULL,0,'ENG'),(2188,274,0,'12-JUL-05 03.09.03.969607 PM +08:00','PRIMARYSITE','Breast, upper-inner quadrant (C502)',50,NULL,NULL,NULL,NULL,0,'ENG'),(2189,274,0,'12-JUL-05 03.09.03.969838 PM +08:00','PRIMARYSITE','Breast, upper-outer quadrant (C504)',51,NULL,NULL,NULL,NULL,0,'ENG'),(2190,274,0,'12-JUL-05 03.09.03.970069 PM +08:00','PRIMARYSITE','Broad ligament (C571)',52,NULL,NULL,NULL,NULL,0,'ENG'),(2191,274,0,'12-JUL-05 03.09.03.970297 PM +08:00','PRIMARYSITE','Bronchus, main (C340)',53,NULL,NULL,NULL,NULL,0,'ENG'),(2192,274,0,'12-JUL-05 03.09.03.970543 PM +08:00','PRIMARYSITE','Caecum (ileocecal junction) (C180)',54,NULL,NULL,NULL,NULL,0,'ENG'),(2193,274,0,'12-JUL-05 03.09.03.970780 PM +08:00','PRIMARYSITE','Cardia, NOS (gastroesophageal junction) (C160)',55,NULL,NULL,NULL,NULL,0,'ENG'),(2194,274,0,'12-JUL-05 03.09.03.971019 PM +08:00','PRIMARYSITE','Carotid body (C754)',56,NULL,NULL,NULL,NULL,0,'ENG'),(2195,274,0,'12-JUL-05 03.09.03.971249 PM +08:00','PRIMARYSITE','Cauda equina (C721)',57,NULL,NULL,NULL,NULL,0,'ENG'),(2196,274,0,'12-JUL-05 03.09.03.971485 PM +08:00','PRIMARYSITE','Cerebellum, NOS (C716)',58,NULL,NULL,NULL,NULL,0,'ENG'),(2197,274,0,'12-JUL-05 03.09.03.971723 PM +08:00','PRIMARYSITE','Cerebrum (C710)',59,NULL,NULL,NULL,NULL,0,'ENG'),(2198,274,0,'12-JUL-05 03.09.03.971955 PM +08:00','PRIMARYSITE','Cervix, NOS (C539)',60,NULL,NULL,NULL,NULL,0,'ENG'),(2199,274,0,'12-JUL-05 03.09.03.972190 PM +08:00','PRIMARYSITE','Cervix, other parts, OL (C538)',61,NULL,NULL,NULL,NULL,0,'ENG'),(2200,274,0,'12-JUL-05 03.09.03.972917 PM +08:00','PRIMARYSITE','Cheek, mucosa (C060)',62,NULL,NULL,NULL,NULL,0,'ENG'),(2201,274,0,'12-JUL-05 03.09.03.973173 PM +08:00','PRIMARYSITE','Choroid (C693)',63,NULL,NULL,NULL,NULL,0,'ENG'),(2202,274,0,'12-JUL-05 03.09.03.973410 PM +08:00','PRIMARYSITE','Clitoris (C512)',64,NULL,NULL,NULL,NULL,0,'ENG'),(2203,274,0,'12-JUL-05 03.09.03.973655 PM +08:00','PRIMARYSITE','Cloacogenic zone (C212)',65,NULL,NULL,NULL,NULL,0,'ENG'),(2204,274,0,'12-JUL-05 03.09.03.973886 PM +08:00','PRIMARYSITE','Colon, ascending (C182)',66,NULL,NULL,NULL,NULL,0,'ENG'),(2205,274,0,'12-JUL-05 03.09.03.974117 PM +08:00','PRIMARYSITE','Colon, descending (C186)',67,NULL,NULL,NULL,NULL,0,'ENG'),(2206,274,0,'12-JUL-05 03.09.03.974350 PM +08:00','PRIMARYSITE','Colon, hepatic flexure (C183)',68,NULL,NULL,NULL,NULL,0,'ENG'),(2207,274,0,'12-JUL-05 03.09.03.974613 PM +08:00','PRIMARYSITE','Colon, NOS (C189)',69,NULL,NULL,NULL,NULL,0,'ENG'),(2208,274,0,'12-JUL-05 03.09.04.045424 PM +08:00','PRIMARYSITE','Unknown primary site (C809)',322,NULL,NULL,NULL,NULL,0,'ENG'),(2209,274,0,'12-JUL-05 03.09.04.045724 PM +08:00','PRIMARYSITE','Urachus (C677)',323,NULL,NULL,NULL,NULL,0,'ENG'),(2210,274,0,'12-JUL-05 03.09.04.045965 PM +08:00','PRIMARYSITE','Ureter (C669)',324,NULL,NULL,NULL,NULL,0,'ENG'),(2211,274,0,'12-JUL-05 03.09.04.046198 PM +08:00','PRIMARYSITE','Ureteric orifice (C676)',325,NULL,NULL,NULL,NULL,0,'ENG'),(2212,274,0,'12-JUL-05 03.09.04.046436 PM +08:00','PRIMARYSITE','Urethra (C680)',326,NULL,NULL,NULL,NULL,0,'ENG'),(2213,274,0,'12-JUL-05 03.09.04.046675 PM +08:00','PRIMARYSITE','Urinary system, NOS (C689)',327,NULL,NULL,NULL,NULL,0,'ENG'),(2214,274,0,'12-JUL-05 03.09.04.046904 PM +08:00','PRIMARYSITE','Uterine adnexa, NOS (C574)',328,NULL,NULL,NULL,NULL,0,'ENG'),(2215,274,0,'12-JUL-05 03.09.04.047135 PM +08:00','PRIMARYSITE','Uterine adnexa, other parts, OL (C578a)',329,NULL,NULL,NULL,NULL,0,'ENG'),(2216,274,0,'12-JUL-05 03.09.04.047368 PM +08:00','PRIMARYSITE','Uterus, body (corpus) (C549)',330,NULL,NULL,NULL,NULL,0,'ENG'),(2217,274,0,'12-JUL-05 03.09.04.047620 PM +08:00','PRIMARYSITE','Uterus, lower segment (Isthmus) (C540)',331,NULL,NULL,NULL,NULL,0,'ENG'),(2218,274,0,'12-JUL-05 03.09.04.047854 PM +08:00','PRIMARYSITE','Uterus, NOS (C559)',332,NULL,NULL,NULL,NULL,0,'ENG'),(2219,274,0,'12-JUL-05 03.09.04.048103 PM +08:00','PRIMARYSITE','Uterus, OL (C548)',333,NULL,NULL,NULL,NULL,0,'ENG'),(2220,274,0,'12-JUL-05 03.09.04.048337 PM +08:00','PRIMARYSITE','Uvula (C052)',334,NULL,NULL,NULL,NULL,0,'ENG'),(2221,274,0,'12-JUL-05 03.09.04.048581 PM +08:00','PRIMARYSITE','Vagina, NOS (C529)',335,NULL,NULL,NULL,NULL,0,'ENG'),(2222,274,0,'12-JUL-05 03.09.04.048815 PM +08:00','PRIMARYSITE','Vallecula (C100)',336,NULL,NULL,NULL,NULL,0,'ENG'),(2223,274,0,'12-JUL-05 03.09.04.049047 PM +08:00','PRIMARYSITE','Ventricle, NOS (C715)',337,NULL,NULL,NULL,NULL,0,'ENG'),(2224,274,0,'12-JUL-05 03.09.04.049280 PM +08:00','PRIMARYSITE','Vertebral column (C412)',338,NULL,NULL,NULL,NULL,0,'ENG'),(2225,274,0,'12-JUL-05 03.09.04.049522 PM +08:00','PRIMARYSITE','Vulva, NOS (C519)',339,NULL,NULL,NULL,NULL,0,'ENG'),(2226,274,0,'12-JUL-05 03.09.04.049758 PM +08:00','PRIMARYSITE','Vulva, OL (C518)',340,NULL,NULL,NULL,NULL,0,'ENG'),(2227,274,0,'12-JUL-05 03.09.04.050047 PM +08:00','PRIMARYSITE','Waldeyer\'s ring, NOS (C142)',341,NULL,NULL,NULL,NULL,0,'ENG'),(2228,274,0,'17-NOV-08 12.45.46.345405 PM +08:00','PRIMARYSITE','Multiple Tumour Sites',342,NULL,NULL,NULL,NULL,0,'ENG'),(2344,0,0,'03-JUL-06 02.41.42.607927 PM +08:00','BIOSPECCOLLECTIONTYPE','Caecum',4,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2345,274,-1,'27-MAR-08 01.10.24.366968 PM +08:00','BIOSPECCOLLECTIONTYPE','Ext\'ed from BC',5,1,'Ext\'ed from BC','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(2346,274,0,'27-MAR-08 01.16.43.197603 PM +08:00','BIOSPECCOLLECTIONTYPE','DNA from BC',2,1,'DNA from BC','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(2348,274,0,'12-JUL-05 02.35.58.285931 PM +08:00','BIOSPECCOLLECTIONTYPE','Plasma (EDTA)',5,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2349,274,0,'12-JUL-05 02.35.58.286192 PM +08:00','BIOSPECCOLLECTIONTYPE','Buffy Coat (ACD)',2,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2353,274,0,'12-JUL-05 02.35.58.287159 PM +08:00','BIOSPECCOLLECTIONTYPE','Plasma (ACD)',4,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2354,274,0,'12-JUL-05 02.35.58.287399 PM +08:00','BIOSPECCOLLECTIONTYPE','Buffy Coat (EDTA)',1,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2356,0,0,'12-JUL-05 02.35.58.288231 PM +08:00','BIOSPECCOLLECTIONTYPE','Breast',3,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2357,0,0,'12-JUL-05 02.35.58.288683 PM +08:00','BIOSPECCOLLECTIONTYPE','Colon, nos',8,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2358,0,0,'12-JUL-05 02.35.58.289033 PM +08:00','BIOSPECCOLLECTIONTYPE','Uterus, endometrium',30,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2359,0,0,'12-JUL-05 02.35.58.289293 PM +08:00','BIOSPECCOLLECTIONTYPE','Oesophagus',20,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2361,0,0,'12-JUL-05 02.35.58.289763 PM +08:00','BIOSPECCOLLECTIONTYPE','Liver',16,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2362,0,0,'12-JUL-05 02.35.58.289995 PM +08:00','BIOSPECCOLLECTIONTYPE','Ovary, left',23,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2363,0,0,'12-JUL-05 02.35.58.290781 PM +08:00','BIOSPECCOLLECTIONTYPE','Ovary, right',24,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2364,0,0,'12-JUL-05 02.35.58.291030 PM +08:00','BIOSPECCOLLECTIONTYPE','Pancreas',25,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2365,0,0,'12-JUL-05 02.35.58.291278 PM +08:00','BIOSPECCOLLECTIONTYPE','Rectum',27,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2366,0,0,'12-JUL-05 02.35.58.291517 PM +08:00','BIOSPECCOLLECTIONTYPE','Omentum',21,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2367,0,0,'12-JUL-05 02.35.58.291748 PM +08:00','BIOSPECCOLLECTIONTYPE','Stomach',29,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2368,0,0,'12-JUL-05 02.35.58.291978 PM +08:00','BIOSPECCOLLECTIONTYPE','Duodenum',12,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2369,0,0,'12-JUL-05 02.35.58.292227 PM +08:00','BIOSPECCOLLECTIONTYPE','Lung',17,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2370,0,0,'12-JUL-05 02.35.58.292467 PM +08:00','BIOSPECCOLLECTIONTYPE','Peritoneum, pelvic',26,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2372,0,0,'12-JUL-05 02.35.58.292934 PM +08:00','BIOSPECCOLLECTIONTYPE','Ovary',22,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2373,0,0,'12-JUL-05 02.35.58.293169 PM +08:00','BIOSPECCOLLECTIONTYPE','Small bowel, nos',28,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2375,0,0,'12-JUL-05 02.35.58.293642 PM +08:00','BIOSPECCOLLECTIONTYPE','Fallopian tube, left',13,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2376,0,0,'12-JUL-05 02.35.58.293881 PM +08:00','BIOSPECCOLLECTIONTYPE','Fallopian tube, right',14,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2378,274,0,'03-OCT-05 02.47.03.164362 AM +00:00','BIOSPECCOLLECTIONTYPE','Unprocessed',8,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2379,274,0,'03-OCT-05 02.47.03.176543 AM +00:00','BIOSPECCOLLECTIONTYPE','Plasma (LH)',6,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2380,274,0,'12-OCT-05 06.02.47.758650 AM +00:00','BIOSPECCOLLECTIONTYPE','Buffy Coat (LH)',3,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Blood',0,'ENG'),(2381,0,0,'18/MAR/09 12:44:52.511616 PM +08:00','BIOSPECCOLLECTIONTYPE','Lymph node',18,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2382,0,0,'01-DEC-08 12.37.53.185370 PM +08:00','BIOSPECCOLLECTIONTYPE','Colon, ascending',5,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2383,0,0,'01-DEC-08 12.40.37.396444 PM +08:00','BIOSPECCOLLECTIONTYPE','Colon, descending ',6,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2384,0,0,'01-DEC-08 12.40.37.411874 PM +08:00','BIOSPECCOLLECTIONTYPE','Colon, transverse',11,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2385,0,0,'03-FEB-09 09.26.33.545039 AM +08:00','BIOSPECCOLLECTIONTYPE','Colon, sigmoid',9,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2386,0,0,'03-FEB-09 09.26.33.569400 AM +08:00','BIOSPECCOLLECTIONTYPE','Colon, spenic flexure',10,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2387,0,0,'03-FEB-09 09.26.33.579937 AM +08:00','BIOSPECCOLLECTIONTYPE','Colon, hepatic flexure',7,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2388,0,0,'03-FEB-09 09.26.33.586791 AM +08:00','BIOSPECCOLLECTIONTYPE','Ileum',15,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2389,274,0,'21-FEB-06 07.10.28.788776 AM +00:00','HISTOLOGY','Cystadenoma, NOS (M8440/0)',73,NULL,NULL,NULL,NULL,0,'ENG'),(2390,274,0,'21-FEB-06 07.13.22.387268 AM +00:00','HISTOLOGY','Cyst, Dermoid NOS (M9084/0)',60,NULL,NULL,NULL,NULL,0,'ENG'),(2391,274,0,'21-FEB-06 07.13.57.400196 AM +00:00','HISTOLOGY','Carcinoma, Infiltrating duct NOS (8500/3)',35,NULL,NULL,NULL,NULL,0,'ENG'),(2392,274,0,'21-FEB-06 07.14.34.992897 AM +00:00','HISTOLOGY','Dysgerminoma (9060/3)',79,NULL,NULL,NULL,NULL,0,'ENG'),(2393,274,0,'21-FEB-06 07.15.08.389271 AM +00:00','HISTOLOGY','Fibroma, NOS (M8810/0)',85,NULL,NULL,NULL,NULL,0,'ENG'),(2394,274,0,'21-FEB-06 07.16.23.384800 AM +00:00','HISTOLOGY','Gastrointestinal Stromal Tumour (8936/3)',91,NULL,NULL,NULL,NULL,0,'ENG'),(2395,274,0,'21-FEB-06 07.16.23.412947 AM +00:00','HISTOLOGY','Teratoma, Immature (9080/3)',132,NULL,NULL,NULL,NULL,0,'ENG'),(2396,274,0,'21-FEB-06 07.16.23.425624 AM +00:00','HISTOLOGY','Teratoma, Mature Cystic (9080/0)',133,NULL,NULL,NULL,NULL,0,'ENG'),(2398,274,0,'21-FEB-06 07.18.38.384455 AM +00:00','HISTOLOGY','Cystadenofibroma, Mucinous (M9015/0)',68,NULL,NULL,NULL,NULL,0,'ENG'),(2399,274,-1,'21-FEB-06 07.20.23.997233 AM +00:00','HISTOLOGY','Mucinous Cystadenoma, borderli',113,NULL,NULL,NULL,NULL,0,'ENG'),(2401,274,0,'21-FEB-06 07.21.17.593883 AM +00:00','HISTOLOGY','Normal Tissue, NOS',120,NULL,NULL,NULL,NULL,0,'ENG'),(2402,274,0,'21-FEB-06 07.21.44.191536 AM +00:00','HISTOLOGY','Sarcoma, NOS (8800/3)',126,NULL,NULL,NULL,NULL,0,'ENG'),(2403,274,0,'21-FEB-06 07.22.15.390009 AM +00:00','HISTOLOGY','Adenocarcinoma, Serous(8441/3)',9,NULL,NULL,NULL,NULL,0,'ENG'),(2407,274,0,'21-FEB-06 07.25.04.597910 AM +00:00','HISTOLOGY','Cystadenofibroma,Serous borderline (9014/1)',71,NULL,NULL,NULL,NULL,0,'ENG'),(2408,274,0,'21-FEB-06 07.25.33.596146 AM +00:00','HISTOLOGY','Cystadenofibroma,Serous benign (9014/0)',70,NULL,NULL,NULL,NULL,0,'ENG'),(2409,274,0,'21-FEB-06 07.26.16.593169 AM +00:00','HISTOLOGY','Cyst, Serous (N83.2)',62,NULL,NULL,NULL,NULL,0,'ENG'),(2410,274,0,'21-FEB-06 07.27.10.009503 AM +00:00','HISTOLOGY','Serous Tumour, borderline (8442/1)',128,NULL,NULL,NULL,NULL,0,'ENG'),(2411,274,0,'21-FEB-06 07.27.10.032323 AM +00:00','HISTOLOGY','Steroid cell tumour(8670/3)',131,NULL,NULL,NULL,NULL,0,'ENG'),(2412,274,0,'21-FEB-06 07.28.12.632490 AM +00:00','HISTOLOGY','Yolk Sac/Endodermal sinus tumour (M9071/3)',139,NULL,NULL,NULL,NULL,0,'ENG'),(2413,274,0,'21-FEB-06 07.28.50.981925 AM +00:00','HISTOLOGY','Hydrosalpinx cyst (N70.1)',98,NULL,NULL,NULL,NULL,0,'ENG'),(2415,274,0,'21-FEB-06 07.29.53.988036 AM +00:00','HISTOLOGY','Endometrial Cyst (N80.9)',81,NULL,NULL,NULL,NULL,0,'ENG'),(2417,274,0,'21-FEB-06 07.30.56.393319 AM +00:00','HISTOLOGY','Cyst, ovarian benign (D27)',63,NULL,NULL,NULL,NULL,0,'ENG'),(2419,274,0,'21-FEB-06 07.33.05.794493 AM +00:00','HISTOLOGY','Atrophic changes (M83.3)',23,NULL,NULL,NULL,NULL,0,'ENG'),(2421,274,0,'21-FEB-06 07.34.25.998380 AM +00:00','HISTOLOGY','Cyst, Inclusion benign (N83.2)',61,NULL,NULL,NULL,NULL,0,'ENG'),(2423,274,0,'21-FEB-06 07.35.26.584821 AM +00:00','HISTOLOGY','Follicular cyst, ovary(N83.0)',90,NULL,NULL,NULL,NULL,0,'ENG'),(2424,274,0,'21-FEB-06 07.35.56.192147 AM +00:00','HISTOLOGY','Ectopic Tubal Pregnancy (O00)',80,NULL,NULL,NULL,NULL,0,'ENG'),(2425,274,0,'21-FEB-06 07.36.19.792433 AM +00:00','HISTOLOGY','Benign (M8000/0)',24,NULL,NULL,NULL,NULL,0,'ENG'),(2426,274,0,'21-FEB-06 07.36.51.188386 AM +00:00','HISTOLOGY','Cervical polyp, benign (D26.0)',54,NULL,NULL,NULL,NULL,0,'ENG'),(2429,274,0,'27-FEB-06 04.11.36.658003 AM +00:00','HISTOLOGY','Cystadenoma, Serous (8441/0)',76,NULL,NULL,NULL,NULL,0,'ENG'),(2430,274,0,'27-FEB-06 07.07.14.465533 AM +00:00','HISTOLOGY','Endometriosis (N80)',82,NULL,NULL,NULL,NULL,0,'ENG'),(2431,274,0,'02-MAR-06 07.32.24.961470 AM +00:00','HISTOLOGY','Unknown',137,NULL,NULL,NULL,NULL,0,'ENG'),(2432,274,0,'20-MAR-06 06.50.35.450125 AM +00:00','HISTOLOGY','Chronic Lymphocytic Leukaemia (9823/3)',58,NULL,NULL,NULL,NULL,0,'ENG'),(2434,274,0,'20-APR-06 05.50.32.757196 AM +00:00','HISTOLOGY','Lymphoma,Large B-cell (9680/3)',106,NULL,NULL,NULL,NULL,0,'ENG'),(2436,274,0,'21-APR-06 06.15.46.559470 AM +00:00','HISTOLOGY','Thecoma, Malignant (8600/3)',136,NULL,NULL,NULL,NULL,0,'ENG'),(2437,274,0,'12-JUL-06 04.20.15.557387 PM +08:00','HISTOLOGY','Carcinoma, Intraductal noninfiltrating, NOS (8500/2)',36,NULL,NULL,NULL,NULL,0,'ENG'),(2438,274,0,'18-SEP-06 11.12.38.388847 AM +08:00','HISTOLOGY','Haemangiopericytoma (9150/3)',96,NULL,NULL,NULL,NULL,0,'ENG'),(2439,274,0,'18-SEP-06 02.40.06.974932 PM +08:00','HISTOLOGY','Adenoma, Villous(M8261/1)',19,NULL,NULL,NULL,NULL,0,'ENG'),(2440,274,0,'14-JUL-05 10.49.33.473326 AM +08:00','HISTOLOGY','Adenocarcinoma, apocrine metaplasia(8573/3)',10,NULL,NULL,NULL,NULL,0,'ENG'),(2441,274,0,'14-JUL-05 10.49.33.475087 AM +08:00','HISTOLOGY','Adenocarcinoma in multiple aden. polyps (8221/3)',3,NULL,NULL,NULL,NULL,0,'ENG'),(2442,274,0,'14-JUL-05 10.49.33.475432 AM +08:00','HISTOLOGY','Adenocarcinoma in villous adenoma (8261/3)',4,NULL,NULL,NULL,NULL,0,'ENG'),(2443,274,0,'14-JUL-05 10.49.33.475659 AM +08:00','HISTOLOGY','Adenocarcinoma, intestinal type (8144/3)',11,NULL,NULL,NULL,NULL,0,'ENG'),(2444,274,0,'14-JUL-05 10.49.33.475874 AM +08:00','HISTOLOGY','Adenocarcinoma, NOS (8140/3)',7,NULL,NULL,NULL,NULL,0,'ENG'),(2445,274,0,'14-JUL-05 10.49.33.476155 AM +08:00','HISTOLOGY','Carcinoma, Adenoid cystic,NOS (8200/3)',30,NULL,NULL,NULL,NULL,0,'ENG'),(2446,274,0,'14-JUL-05 10.49.33.476386 AM +08:00','HISTOLOGY','Adenosarcoma (8933/3)',20,NULL,NULL,NULL,NULL,0,'ENG'),(2447,274,0,'14-JUL-05 10.49.33.476594 AM +08:00','HISTOLOGY','Carcinoma, Adenosquamous(8560/3)',31,NULL,NULL,NULL,NULL,0,'ENG'),(2448,274,0,'14-JUL-05 10.49.33.476805 AM +08:00','HISTOLOGY','Fibromatosis, Agressive (M8821/1)',87,NULL,NULL,NULL,NULL,0,'ENG'),(2449,274,0,'14-JUL-05 10.49.33.477021 AM +08:00','HISTOLOGY','Angiomyxoma (M8841/1)',21,NULL,NULL,NULL,NULL,0,'ENG'),(2450,274,0,'14-JUL-05 10.49.33.477307 AM +08:00','HISTOLOGY','Carcinoma, Basal cell (8090/3)',32,NULL,NULL,NULL,NULL,0,'ENG'),(2451,274,0,'14-JUL-05 10.49.33.477534 AM +08:00','HISTOLOGY','Bile duct cystadenocarcinoma (',25,NULL,NULL,NULL,NULL,0,'ENG'),(2452,274,0,'14-JUL-05 10.49.33.477743 AM +08:00','HISTOLOGY','Bowen\'s disease (8081/2)',26,NULL,NULL,NULL,NULL,0,'ENG'),(2453,274,0,'14-JUL-05 10.49.33.477957 AM +08:00','HISTOLOGY','Brenner tumour (9000/3)',27,NULL,NULL,NULL,NULL,0,'ENG'),(2454,274,0,'14-JUL-05 10.49.33.478242 AM +08:00','HISTOLOGY','Carcinoid tumour, NOS (8240/3)',28,NULL,NULL,NULL,NULL,0,'ENG'),(2455,274,0,'14-JUL-05 10.49.33.478469 AM +08:00','HISTOLOGY','Carcinoma simplex (8231/3)',29,NULL,NULL,NULL,NULL,0,'ENG'),(2456,274,0,'14-JUL-05 10.49.33.478677 AM +08:00','HISTOLOGY','Carcinoma, anaplastic, NOS (8021/3)',49,NULL,NULL,NULL,NULL,0,'ENG'),(2457,274,0,'14-JUL-05 10.49.33.478891 AM +08:00','HISTOLOGY','Carcinoma, undifferentiated, NOS (8020/3)',50,NULL,NULL,NULL,NULL,0,'ENG'),(2458,274,0,'14-JUL-05 10.49.33.479174 AM +08:00','HISTOLOGY','Carcinosarcoma, embryonal (8981/3)',53,NULL,NULL,NULL,NULL,0,'ENG'),(2459,274,0,'14-JUL-05 10.49.33.479427 AM +08:00','HISTOLOGY','Carcinosarcoma, NOS (8980/3)',52,NULL,NULL,NULL,NULL,0,'ENG'),(2460,274,0,'14-JUL-05 10.49.33.479639 AM +08:00','HISTOLOGY','Cholangiocarcinoma (8160/4)',55,NULL,NULL,NULL,NULL,0,'ENG'),(2461,274,0,'14-JUL-05 10.49.33.480716 AM +08:00','HISTOLOGY','Choriocarcinoma (9100/3)',56,NULL,NULL,NULL,NULL,0,'ENG'),(2462,274,0,'14-JUL-05 10.49.33.480946 AM +08:00','HISTOLOGY','Adenocarcinoma, Clear Cell NOS(8310/3)',5,NULL,NULL,NULL,NULL,0,'ENG'),(2463,274,0,'14-JUL-05 10.49.33.481246 AM +08:00','HISTOLOGY','Sarcoma, Clear Cell, kidney (8964/3)',124,NULL,NULL,NULL,NULL,0,'ENG'),(2465,274,0,'14-JUL-05 10.49.33.481677 AM +08:00','HISTOLOGY','Sarcoma, Endometrial stromal (8930/3)',125,NULL,NULL,NULL,NULL,0,'ENG'),(2466,274,0,'14-JUL-05 10.49.33.481892 AM +08:00','HISTOLOGY','Adenofibroma, Endometrioid (8381/3)',14,NULL,NULL,NULL,NULL,0,'ENG'),(2467,274,0,'14-JUL-05 10.49.33.482355 AM +08:00','HISTOLOGY','Carcinoma, Endometrioid (8380/3)',33,NULL,NULL,NULL,NULL,0,'ENG'),(2468,274,0,'14-JUL-05 10.49.33.482651 AM +08:00','HISTOLOGY','Ependymoma, NOS (9391/3)',83,NULL,NULL,NULL,NULL,0,'ENG'),(2470,274,0,'14-JUL-05 10.49.33.483164 AM +08:00','HISTOLOGY','Fibrosarcoma, NOS (8810/3)',88,NULL,NULL,NULL,NULL,0,'ENG'),(2471,274,0,'14-JUL-05 10.49.33.483398 AM +08:00','HISTOLOGY','Germinoma (9064/3)',92,NULL,NULL,NULL,NULL,0,'ENG'),(2472,274,0,'14-JUL-05 10.49.33.483705 AM +08:00','HISTOLOGY','Carcinoma, Giant cell/Spindle cell (8030/3)',34,NULL,NULL,NULL,NULL,0,'ENG'),(2473,274,0,'14-JUL-05 10.49.33.483927 AM +08:00','HISTOLOGY','Granular cell tumour (9580/3)',93,NULL,NULL,NULL,NULL,0,'ENG'),(2474,274,0,'14-JUL-05 10.49.33.484220 AM +08:00','HISTOLOGY','Granulosa cell tumour (8620/3)',94,NULL,NULL,NULL,NULL,0,'ENG'),(2475,274,0,'14-JUL-05 10.49.33.484445 AM +08:00','HISTOLOGY','Granulosa cell-theca cell tumour(8621/3)',95,NULL,NULL,NULL,NULL,0,'ENG'),(2477,274,0,'14-JUL-05 10.49.33.484859 AM +08:00','HISTOLOGY','Lymphoma, Hodgkin\'s (9650/3)',104,NULL,NULL,NULL,NULL,0,'ENG'),(2479,274,0,'14-JUL-05 10.49.33.485376 AM +08:00','HISTOLOGY','Insulinoma (8151/3)',99,NULL,NULL,NULL,NULL,0,'ENG'),(2480,274,0,'14-JUL-05 10.49.33.485587 AM +08:00','HISTOLOGY','Squamous intraepithelial neoplasia, G3 (8077/2)',130,NULL,NULL,NULL,NULL,0,'ENG'),(2481,274,0,'14-JUL-05 10.49.33.486427 AM +08:00','HISTOLOGY','Carcinoma, Large cell, NOS (8012/3)',37,NULL,NULL,NULL,NULL,0,'ENG'),(2482,274,0,'14-JUL-05 10.49.33.486659 AM +08:00','HISTOLOGY','Leiomyosarcoma, NOS (8890/3)',101,NULL,NULL,NULL,NULL,0,'ENG'),(2483,274,0,'14-JUL-05 10.49.33.486878 AM +08:00','HISTOLOGY','Leydig cell tumour (8650/3)',102,NULL,NULL,NULL,NULL,0,'ENG'),(2484,274,0,'14-JUL-05 10.49.33.487158 AM +08:00','HISTOLOGY','Carcinoma, Lobular, NOS (8520/3)',38,NULL,NULL,NULL,NULL,0,'ENG'),(2485,274,0,'14-JUL-05 10.49.33.487397 AM +08:00','HISTOLOGY','Lymphoma, non-Hodgkin(9591/3)',105,NULL,NULL,NULL,NULL,0,'ENG'),(2487,274,0,'14-JUL-05 10.49.33.487821 AM +08:00','HISTOLOGY','Melanoma, NOS (8720/3)',107,NULL,NULL,NULL,NULL,0,'ENG'),(2488,274,0,'14-JUL-05 10.49.33.488427 AM +08:00','HISTOLOGY','Mesodermal mixed tumour (8951/3)',108,NULL,NULL,NULL,NULL,0,'ENG'),(2489,274,0,'14-JUL-05 10.49.33.488748 AM +08:00','HISTOLOGY','Mesothelioma (9050/3)',109,NULL,NULL,NULL,NULL,0,'ENG'),(2490,274,0,'14-JUL-05 10.49.33.489228 AM +08:00','HISTOLOGY','Mixed germ cell tumour (9085/3)',111,NULL,NULL,NULL,NULL,0,'ENG'),(2491,274,0,'14-JUL-05 10.49.33.489530 AM +08:00','HISTOLOGY','Mixed tumour, NOS (8940/3)',112,NULL,NULL,NULL,NULL,0,'ENG'),(2492,274,0,'14-JUL-05 10.49.33.489771 AM +08:00','HISTOLOGY','Adenocarcinoma, Mucinous (8480/3)',6,NULL,NULL,NULL,NULL,0,'ENG'),(2493,274,0,'14-JUL-05 10.49.33.490009 AM +08:00','HISTOLOGY','Cystadenocarcinoma, Mucinous (8470/3)',64,NULL,NULL,NULL,NULL,0,'ENG'),(2495,274,0,'14-JUL-05 10.49.33.490572 AM +08:00','HISTOLOGY','Mullerian mixed tumour (8950/3)',115,NULL,NULL,NULL,NULL,0,'ENG'),(2496,274,0,'14-JUL-05 10.49.33.490806 AM +08:00','HISTOLOGY','Myosarcoma (8895/3)',116,NULL,NULL,NULL,NULL,0,'ENG'),(2497,274,0,'14-JUL-05 10.49.33.491052 AM +08:00','HISTOLOGY','Neoplasm (8000/3)',118,NULL,NULL,NULL,NULL,0,'ENG'),(2498,274,0,'14-JUL-05 10.49.33.491385 AM +08:00','HISTOLOGY','Neurilemmoma (9560/3)',119,NULL,NULL,NULL,NULL,0,'ENG'),(2499,274,0,'14-JUL-05 10.49.33.491625 AM +08:00','HISTOLOGY','Paget\'s disease, extramammary ',121,NULL,NULL,NULL,NULL,0,'ENG'),(2500,274,0,'14-JUL-05 10.49.33.491852 AM +08:00','HISTOLOGY','Adenocarcinoma, Papillary NOS (8260/3)',8,NULL,NULL,NULL,NULL,0,'ENG'),(2501,274,0,'14-JUL-05 10.49.33.492753 AM +08:00','HISTOLOGY','Carcinoma, Papillary, NOS(8050/3)',40,NULL,NULL,NULL,NULL,0,'ENG'),(2502,274,0,'14-JUL-05 10.49.33.493011 AM +08:00','HISTOLOGY','Cystadenocarcinoma, Papillary, NOS (8450/3)',66,NULL,NULL,NULL,NULL,0,'ENG'),(2503,274,0,'14-JUL-05 10.49.33.493342 AM +08:00','HISTOLOGY','Cystadenocarcinoma, Papillary Serous (8460/3)',65,NULL,NULL,NULL,NULL,0,'ENG'),(2504,274,0,'14-JUL-05 10.49.33.493600 AM +08:00','HISTOLOGY','Cystadenoma, Papillary Serous (M8460/0)',75,NULL,NULL,NULL,NULL,0,'ENG'),(2505,274,0,'14-JUL-05 10.49.33.493837 AM +08:00','HISTOLOGY','Carcinoma, Transitional Cell, Papillary(8130/3)',47,NULL,NULL,NULL,NULL,0,'ENG'),(2506,274,0,'14-JUL-05 10.49.33.494074 AM +08:00','HISTOLOGY','Adenofibroma, Serous (9014/0)',15,NULL,NULL,NULL,NULL,0,'ENG'),(2507,274,0,'14-JUL-05 10.49.33.494398 AM +08:00','HISTOLOGY','Cystadenocarcinoma, Serous (8441/3)',67,NULL,NULL,NULL,NULL,0,'ENG'),(2508,274,0,'14-JUL-05 10.49.33.494635 AM +08:00','HISTOLOGY','Cystadenoma, Serous, Borderline (8442/1)',77,NULL,NULL,NULL,NULL,0,'ENG'),(2509,274,0,'14-JUL-05 10.49.33.494868 AM +08:00','HISTOLOGY','Carcinoma, Serous Surface Papillary (8461/3)',41,NULL,NULL,NULL,NULL,0,'ENG'),(2510,274,0,'14-JUL-05 10.49.33.495291 AM +08:00','HISTOLOGY','Sertoli-Leydig cell tumour (8631/0)',129,NULL,NULL,NULL,NULL,0,'ENG'),(2511,274,0,'14-JUL-05 10.49.33.495542 AM +08:00','HISTOLOGY','Carcinoma, Signet ring cell (8490/3)',42,NULL,NULL,NULL,NULL,0,'ENG'),(2512,274,0,'14-JUL-05 10.49.33.495772 AM +08:00','HISTOLOGY','Carcinoma, Small Cell, NOS (8041/3)',43,NULL,NULL,NULL,NULL,0,'ENG'),(2513,274,0,'14-JUL-05 10.49.33.496006 AM +08:00','HISTOLOGY','Sarcoma, Spindle Cell (8801/3)',127,NULL,NULL,NULL,NULL,0,'ENG'),(2514,274,0,'14-JUL-05 10.49.33.496319 AM +08:00','HISTOLOGY','Carcinoma, Sq. cell, keratinizing, NOS(8071/3)',44,NULL,NULL,NULL,NULL,0,'ENG'),(2515,274,0,'14-JUL-05 10.49.33.496561 AM +08:00','HISTOLOGY','Carcinoma, Sq. cell, lg. cell, non-ker(8072/3)',45,NULL,NULL,NULL,NULL,0,'ENG'),(2516,274,0,'14-JUL-05 10.49.33.496793 AM +08:00','HISTOLOGY','Carcinoma, Squamous cell, NOS (8070/3)',46,NULL,NULL,NULL,NULL,0,'ENG'),(2517,274,0,'14-JUL-05 10.49.33.497036 AM +08:00','HISTOLOGY','Carcinoma, Transitional cell, NOS(8120/3)',48,NULL,NULL,NULL,NULL,0,'ENG'),(2518,274,0,'14-JUL-05 10.49.33.497498 AM +08:00','HISTOLOGY','Adenocarcinoma, villous (8262/3)',12,NULL,NULL,NULL,NULL,0,'ENG'),(2519,274,0,'03-OCT-05 06.37.47.162967 AM +00:00','HISTOLOGY','Cystadenoma, Mucinous,benign (8470/0)',72,NULL,NULL,NULL,NULL,0,'ENG'),(2520,274,-1,'04-OCT-05 07.30.05.406690 AM +00:00','HISTOLOGY','Fibroma, NOS (M8810/0)',86,NULL,NULL,NULL,NULL,0,'ENG'),(2521,274,0,'21-OCT-05 02.12.24.439310 AM +00:00','HISTOLOGY','Teratoma, Mature Cystic (9080/0)',134,NULL,NULL,NULL,NULL,0,'ENG'),(2522,274,0,'21-FEB-06 07.08.48.830181 AM +00:00','HISTOLOGY','Carcinoma, NOS (8010/3)',39,NULL,NULL,NULL,NULL,0,'ENG'),(2523,274,0,'21-FEB-06 07.10.03.590505 AM +00:00','HISTOLOGY','Cystadenofibroma, NOS (M9013/0)',69,NULL,NULL,NULL,NULL,0,'ENG'),(2524,274,0,'01/APR/09 02:12:30.096583 PM +08:00','HISTOLOGY','Appendicitis, unspecified (K36)',22,NULL,NULL,NULL,NULL,0,'ENG'),(2525,274,0,'06/NOV/09 10:59:02.924999 AM +08:00','HISTOLOGY','Chrohn\'s disease (K50)',57,NULL,NULL,NULL,NULL,0,'ENG'),(2526,274,0,'06/NOV/09 11:23:42.704070 AM +08:00','HISTOLOGY','Lipoma, NOS (8850/0)',103,NULL,NULL,NULL,NULL,0,'ENG'),(2527,274,0,'02-DEC-08 12.52.08.126162 PM +08:00','HISTOLOGY','Adenoma, NOS (8140/0)',16,NULL,NULL,NULL,NULL,0,'ENG'),(2528,274,0,'02-DEC-08 12.52.08.160544 PM +08:00','HISTOLOGY','Adenoma, Tubular(M8261/1)',17,NULL,NULL,NULL,NULL,0,'ENG'),(2529,274,0,'22-JAN-09 10.29.56.329449 AM +08:00','HISTOLOGY','Diverticular Disease (K57)',78,NULL,NULL,NULL,NULL,0,'ENG'),(2530,274,0,'11-FEB-09 02.49.57.175543 PM +08:00','HISTOLOGY','Negative for residual tumor',117,NULL,NULL,NULL,NULL,0,'ENG'),(2531,274,0,'05-DEC-08 01.13.59.704326 PM +08:00','HISTOLOGY','Harmartomatous Polyp (Q85.8)',97,NULL,NULL,NULL,NULL,0,'ENG'),(2533,274,0,'07-APR-06 03.56.44.383841 AM +00:00','BEHAVIOUR','Malignant',5,NULL,NULL,NULL,NULL,0,'ENG'),(2534,274,0,'19-APR-06 07.18.37.380988 AM +00:00','BEHAVIOUR','Borderline',2,NULL,NULL,NULL,NULL,0,'ENG'),(2535,274,0,'15-OCT-08 10.59.14.083056 AM +08:00','BEHAVIOUR','Pre-IORT',12,NULL,NULL,NULL,NULL,0,'ENG'),(2536,274,0,'17-NOV-08 12.37.21.527314 PM +08:00','BEHAVIOUR','Post-IORT',11,NULL,NULL,NULL,NULL,0,'ENG'),(2537,274,0,'21-JUL-05 02.18.53.430786 PM +08:00','BEHAVIOUR','Benign',1,NULL,NULL,NULL,NULL,0,'ENG'),(2543,274,0,'21-JUL-05 02.18.53.441134 PM +08:00','BEHAVIOUR','Malignant Metastic site',5,NULL,NULL,NULL,NULL,0,'ENG'),(2546,274,0,'21-SEP-05 04.11.56.573685 AM +00:00','BEHAVIOUR','Normal',10,NULL,NULL,NULL,NULL,0,'ENG'),(2547,274,0,'17-NOV-08 12.38.17.188919 PM +08:00','BEHAVIOUR','No residual tumour',9,NULL,NULL,NULL,NULL,0,'ENG'),(2548,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Archer, SG',1,NULL,NULL,NULL,NULL,1,'ENG'),(2549,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Barwood, NT',2,NULL,NULL,NULL,NULL,1,'ENG'),(2550,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Chandraratna, HS',3,NULL,NULL,NULL,NULL,1,'ENG'),(2552,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Joseph, D',9,NULL,NULL,NULL,NULL,1,'ENG'),(2554,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Levitt, MD',13,NULL,NULL,NULL,NULL,1,'ENG'),(2555,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Makin, G',14,NULL,NULL,NULL,NULL,1,'ENG'),(2556,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','McCartney, AJ',15,NULL,NULL,NULL,NULL,1,'ENG'),(2557,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Platell, CFE',20,NULL,NULL,NULL,NULL,1,'ENG'),(2558,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Saunders, C',23,NULL,NULL,NULL,NULL,1,'ENG'),(2559,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Tan, PYM',26,NULL,NULL,NULL,NULL,1,'ENG'),(2560,274,0,'30/MAR/10 01:26:08.871915 PM +08:00','REF_DOCTOR','Spry, N',24,NULL,NULL,NULL,NULL,1,'ENG'),(2561,274,0,'12-JUL-05 01.23.21.037087 PM +08:00','PATHSERVICE','PathCentre',11,NULL,NULL,NULL,NULL,0,'ENG'),(2562,274,0,'12-JUL-05 01.23.21.043586 PM +08:00','PATHSERVICE','Royal Perth Hospital Pathology',6,NULL,NULL,NULL,NULL,0,'ENG'),(2563,274,0,'12-JUL-05 01.23.21.043854 PM +08:00','PATHSERVICE','Fremantle Hospital Pathology',3,NULL,NULL,NULL,NULL,0,'ENG'),(2564,274,0,'12-JUL-05 01.23.21.044869 PM +08:00','PATHSERVICE','King Edward Memorial Hospital Pathology',4,NULL,NULL,NULL,NULL,0,'ENG'),(2565,274,0,'12-JUL-05 01.23.21.045220 PM +08:00','PATHSERVICE','Princess Margaret Hospital Pathology',5,NULL,NULL,NULL,NULL,0,'ENG'),(2566,274,0,'12-JUL-05 01.23.21.045443 PM +08:00','PATHSERVICE','St John of God Pathology',1,NULL,NULL,NULL,NULL,0,'ENG'),(2567,274,0,'12-JUL-05 01.23.21.045652 PM +08:00','PATHSERVICE','Western Diagnostics',7,NULL,NULL,NULL,NULL,0,'ENG'),(2568,274,0,'12-JUL-05 01.23.21.045907 PM +08:00','PATHSERVICE','Clinipath',8,NULL,NULL,NULL,NULL,0,'ENG'),(2569,274,0,'12-JUL-05 01.23.21.046126 PM +08:00','PATHSERVICE','Dept Respiratory Medicine',9,NULL,NULL,NULL,NULL,0,'ENG'),(2570,274,0,'12-JUL-05 01.23.21.046334 PM +08:00','PATHSERVICE','Radiation Oncology',10,NULL,NULL,NULL,NULL,0,'ENG'),(2571,274,0,'21-JAN-09 09.08.37.859769 AM +08:00','PATHSERVICE','Breast Clinic RPH',2,NULL,NULL,NULL,NULL,0,'ENG'),(2572,274,0,'12-JUL-05 12.39.13.514305 PM +08:00','HOSPITAL','Albany Regional Hospital (1024)',1,NULL,NULL,NULL,NULL,0,'ENG'),(2573,274,0,'12-JUL-05 12.39.13.515681 PM +08:00','HOSPITAL','Armadale Kelmscott Memorial Hospital (1014)',2,NULL,NULL,NULL,NULL,0,'ENG'),(2574,274,0,'12-JUL-05 12.39.13.515977 PM +08:00','HOSPITAL','Attadale Private Hospital (1025)',3,NULL,NULL,NULL,NULL,0,'ENG'),(2575,274,0,'12-JUL-05 12.39.13.516121 PM +08:00','HOSPITAL','Bentley Hospital (1020)',4,NULL,NULL,NULL,NULL,0,'ENG'),(2576,274,0,'12-JUL-05 12.39.13.516240 PM +08:00','HOSPITAL','Bridgetown District Hospital (1026)',5,NULL,NULL,NULL,NULL,0,'ENG'),(2577,274,0,'12-JUL-05 12.39.13.516376 PM +08:00','HOSPITAL','Broome District Hospital (1027)',6,NULL,NULL,NULL,NULL,0,'ENG'),(2578,274,0,'12-JUL-05 12.39.13.516499 PM +08:00','HOSPITAL','Bruck Rock Hospital (1028)',7,NULL,NULL,NULL,NULL,0,'ENG'),(2579,274,0,'12-JUL-05 12.39.13.516736 PM +08:00','HOSPITAL','Bunbury Regional Hospital (1011)',8,NULL,NULL,NULL,NULL,0,'ENG'),(2580,274,0,'12-JUL-05 12.39.13.516859 PM +08:00','HOSPITAL','Busselton District Hospital (1029)',9,NULL,NULL,NULL,NULL,0,'ENG'),(2581,274,0,'12-JUL-05 12.39.13.517046 PM +08:00','HOSPITAL','Carnarvon Regional Hospital (1033)',10,NULL,NULL,NULL,NULL,0,'ENG'),(2582,274,0,'12-JUL-05 12.39.13.517167 PM +08:00','HOSPITAL','Collie Health Service (1035)',11,NULL,NULL,NULL,NULL,0,'ENG'),(2583,274,0,'12-JUL-05 12.39.13.517292 PM +08:00','HOSPITAL','Collin Street Day Surgery (1034)',12,NULL,NULL,NULL,NULL,0,'ENG'),(2584,274,0,'12-JUL-05 12.39.13.517417 PM +08:00','HOSPITAL','Consultant Private Rooms (9999)',13,NULL,NULL,NULL,NULL,0,'ENG'),(2585,274,0,'12-JUL-05 12.39.13.517539 PM +08:00','HOSPITAL','Esperance District Hospital (1059)',14,NULL,NULL,NULL,NULL,0,'ENG'),(2586,274,0,'12-JUL-05 12.39.13.524778 PM +08:00','HOSPITAL','Exmouth District Hospital (1046)',15,NULL,NULL,NULL,NULL,0,'ENG'),(2587,274,0,'12-JUL-05 12.39.13.524950 PM +08:00','HOSPITAL','Fremantle Hospital (1001)',16,NULL,NULL,NULL,NULL,0,'ENG'),(2588,274,0,'12-JUL-05 12.39.13.525073 PM +08:00','HOSPITAL','Galliers Private Hospital (1036)',17,NULL,NULL,NULL,NULL,0,'ENG'),(2589,274,0,'12-JUL-05 12.39.13.525189 PM +08:00','HOSPITAL','Geraldton Regional Hospital (1031)',18,NULL,NULL,NULL,NULL,0,'ENG'),(2590,274,0,'12-JUL-05 12.39.13.525328 PM +08:00','HOSPITAL','Glengarry Hospital (1037)',19,NULL,NULL,NULL,NULL,0,'ENG'),(2591,274,0,'12-JUL-05 12.39.13.525446 PM +08:00','HOSPITAL','Harvey Health Service (1038)',20,NULL,NULL,NULL,NULL,0,'ENG'),(2592,274,0,'12-JUL-05 12.39.13.525563 PM +08:00','HOSPITAL','Hollywood Private Hospital (1002)',21,NULL,NULL,NULL,NULL,0,'ENG'),(2593,274,0,'12-JUL-05 12.39.13.525682 PM +08:00','HOSPITAL','Joondalup Private Hospital (1009)',22,NULL,NULL,NULL,NULL,0,'ENG'),(2594,274,0,'12-JUL-05 12.39.13.525804 PM +08:00','HOSPITAL','Kalamunda District Community Hospital (1015)',23,NULL,NULL,NULL,NULL,0,'ENG'),(2595,274,0,'12-JUL-05 12.39.13.525919 PM +08:00','HOSPITAL','Kaleeya Hospital (1039)',24,NULL,NULL,NULL,NULL,0,'ENG'),(2596,274,0,'12-JUL-05 12.39.13.526108 PM +08:00','HOSPITAL','Kalgoorlie Regional Hospital (1012)',25,NULL,NULL,NULL,NULL,0,'ENG'),(2597,274,0,'12-JUL-05 12.39.13.526235 PM +08:00','HOSPITAL','Katanning District Hospital (1040)',26,NULL,NULL,NULL,NULL,0,'ENG'),(2598,274,0,'12-JUL-05 12.39.13.526360 PM +08:00','HOSPITAL','King Edward Memorial Hospital (1003)',27,NULL,NULL,NULL,NULL,0,'ENG'),(2599,274,0,'12-JUL-05 12.39.13.526476 PM +08:00','HOSPITAL','Kununurra District Hospital (1041)',28,NULL,NULL,NULL,NULL,0,'ENG'),(2600,274,0,'12-JUL-05 12.39.13.526614 PM +08:00','HOSPITAL','Lake Grace District Hospital (1042)',29,NULL,NULL,NULL,NULL,0,'ENG'),(2601,274,0,'12-JUL-05 12.39.13.526760 PM +08:00','HOSPITAL','Margaret River District Hospital (1044)',30,NULL,NULL,NULL,NULL,0,'ENG'),(2602,274,0,'12-JUL-05 12.39.13.526889 PM +08:00','HOSPITAL','McCourt Street Day Surgery (1043)',31,NULL,NULL,NULL,NULL,0,'ENG'),(2603,274,0,'12-JUL-05 12.39.13.527007 PM +08:00','HOSPITAL','Meekatharra District Hospital (1045)',32,NULL,NULL,NULL,NULL,0,'ENG'),(2604,274,0,'12-JUL-05 12.39.13.527123 PM +08:00','HOSPITAL','Mercy Hospital Mount Lawley (1018)',33,NULL,NULL,NULL,NULL,0,'ENG'),(2605,274,0,'12-JUL-05 12.39.13.527237 PM +08:00','HOSPITAL','Merredin District Hospital (1047)',34,NULL,NULL,NULL,NULL,0,'ENG'),(2606,274,0,'12-JUL-05 12.39.13.527946 PM +08:00','HOSPITAL','Moora District Hospital (1048)',35,NULL,NULL,NULL,NULL,0,'ENG'),(2607,274,0,'12-JUL-05 12.39.13.528080 PM +08:00','HOSPITAL','Mt Barker Hospital (1049)',36,NULL,NULL,NULL,NULL,0,'ENG'),(2608,274,0,'12-JUL-05 12.39.13.528204 PM +08:00','HOSPITAL','Mt Lawley Private Hospital (1050)',37,NULL,NULL,NULL,NULL,0,'ENG'),(2609,274,0,'12-JUL-05 12.39.13.528325 PM +08:00','HOSPITAL','Narrogin Regional Hospital (1051)',38,NULL,NULL,NULL,NULL,0,'ENG'),(2610,274,0,'12-JUL-05 12.39.13.528447 PM +08:00','HOSPITAL','Newman District Hospital (1052)',39,NULL,NULL,NULL,NULL,0,'ENG'),(2611,274,0,'12-JUL-05 12.39.13.528562 PM +08:00','HOSPITAL','Nickol Bay Hospital (1030)',40,NULL,NULL,NULL,NULL,0,'ENG'),(2612,274,0,'12-JUL-05 12.39.13.528687 PM +08:00','HOSPITAL','Northam Regional Hospital (1053)',41,NULL,NULL,NULL,NULL,0,'ENG'),(2613,274,0,'12-JUL-05 12.39.13.529109 PM +08:00','HOSPITAL','Osborne Park Hospital (1023)',42,NULL,NULL,NULL,NULL,0,'ENG'),(2614,274,0,'12-JUL-05 12.39.13.529271 PM +08:00','HOSPITAL','Paraburdoo District Hospital (1054)',43,NULL,NULL,NULL,NULL,0,'ENG'),(2615,274,0,'12-JUL-05 12.39.13.529412 PM +08:00','HOSPITAL','Peel Health Campus (1013)',44,NULL,NULL,NULL,NULL,0,'ENG'),(2616,274,0,'12-JUL-05 12.39.13.529550 PM +08:00','HOSPITAL','Port Hedland Regional Hospital (1021)',45,NULL,NULL,NULL,NULL,0,'ENG'),(2617,274,0,'12-JUL-05 12.39.13.529694 PM +08:00','HOSPITAL','Princess Margaret Hospital (1007)',46,NULL,NULL,NULL,NULL,0,'ENG'),(2618,274,0,'12-JUL-05 12.39.13.529830 PM +08:00','HOSPITAL','Rockingham Family Hosptial (1019)',47,NULL,NULL,NULL,NULL,0,'ENG'),(2619,274,0,'12-JUL-05 12.39.13.529966 PM +08:00','HOSPITAL','Rockingham/Kwinana District Hospita (1032)',48,NULL,NULL,NULL,NULL,0,'ENG'),(2620,274,0,'12-JUL-05 12.39.13.530111 PM +08:00','HOSPITAL','Royal Australasian College of Surgeons (999)',49,NULL,NULL,NULL,NULL,0,'ENG'),(2621,274,0,'12-JUL-05 12.39.13.530250 PM +08:00','HOSPITAL','Royal Perth Hospital (1004)',50,NULL,NULL,NULL,NULL,0,'ENG'),(2622,274,0,'12-JUL-05 12.39.13.530396 PM +08:00','HOSPITAL','Sir Charles Gairdner Hospital (1005)',51,NULL,NULL,NULL,NULL,0,'ENG'),(2623,274,0,'12-JUL-05 12.39.13.530533 PM +08:00','HOSPITAL','South Perth Community Hospital (1056)',52,NULL,NULL,NULL,NULL,0,'ENG'),(2624,274,0,'12-JUL-05 12.39.13.530676 PM +08:00','HOSPITAL','Southern Cross District Hospital (1055)',53,NULL,NULL,NULL,NULL,0,'ENG'),(2625,274,0,'12-JUL-05 12.39.13.530815 PM +08:00','HOSPITAL','St John of God Hospital Subiaco (1006)',54,NULL,NULL,NULL,NULL,0,'ENG'),(2626,274,0,'12-JUL-05 12.39.13.531468 PM +08:00','HOSPITAL','St John of God Geraldton (1016)',55,NULL,NULL,NULL,NULL,0,'ENG'),(2627,274,0,'12-JUL-05 12.39.13.531633 PM +08:00','HOSPITAL','St John of God Health Care Bunbury (1010)',56,NULL,NULL,NULL,NULL,0,'ENG'),(2628,274,0,'12-JUL-05 12.39.13.531778 PM +08:00','HOSPITAL','St John of God Murdoch (1022)',57,NULL,NULL,NULL,NULL,0,'ENG'),(2629,274,0,'12-JUL-05 12.39.13.531913 PM +08:00','HOSPITAL','Swan District Hospital (1057)',58,NULL,NULL,NULL,NULL,0,'ENG'),(2630,274,0,'12-JUL-05 12.39.13.532049 PM +08:00','HOSPITAL','The Mount Hospital (1008)',59,NULL,NULL,NULL,NULL,0,'ENG'),(2631,274,0,'12-JUL-05 12.39.13.532187 PM +08:00','HOSPITAL','Warren District Hospital (1017)',60,NULL,NULL,NULL,NULL,0,'ENG'),(2632,274,0,'12-JUL-05 12.39.13.532331 PM +08:00','HOSPITAL','Westminister Day Surgery (1058)',61,NULL,NULL,NULL,NULL,0,'ENG'),(2633,0,0,'06/APR/10 12:23:07.520361 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Urine',1,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Urine',0,'ENG'),(2634,0,0,'06/APR/10 02:07:34.906458 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Saliva',8,1,'Saliva','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(2635,274,0,'12/APR/10 02:42:29.051801 PM AUSTRALIA/PERTH','HISTOLOGY','Thecoma, Benign (8600/0)',135,NULL,NULL,NULL,NULL,0,'ENG'),(2636,274,0,'12/APR/10 02:42:29.126475 PM AUSTRALIA/PERTH','HISTOLOGY','Adenocarcinoma,Mixed cell (8323/3)',13,NULL,NULL,NULL,NULL,0,'ENG'),(2637,274,0,'12/APR/10 02:42:29.172114 PM AUSTRALIA/PERTH','HISTOLOGY','Carcinoma,Neuroendocrine (8246/3)',51,NULL,NULL,NULL,NULL,0,'ENG'),(2638,274,0,'12/APR/10 02:42:29.180203 PM AUSTRALIA/PERTH','HISTOLOGY','Cystadenoma, Papillary Mucinous,borderline (8473/3)',74,NULL,NULL,NULL,NULL,0,'ENG'),(2639,274,0,'12/APR/10 02:42:29.190161 PM AUSTRALIA/PERTH','HISTOLOGY','Female pelvic periotonitis (N73.5)',84,NULL,NULL,NULL,NULL,0,'ENG'),(2640,274,0,'12/APR/10 02:42:29.198568 PM AUSTRALIA/PERTH','HISTOLOGY','Mixed Epithelial Tumour, borderline (8323/1)',110,NULL,NULL,NULL,NULL,0,'ENG'),(2642,274,0,'04/MAY/10 10:46:19.411111 AM AUSTRALIA/PERTH','HISTOLOGY','Abscess, tubo-ovarian (N70) ',1,NULL,NULL,NULL,NULL,0,'ENG'),(2643,274,0,'04/MAY/10 10:46:19.643356 AM AUSTRALIA/PERTH','HISTOLOGY','Adenocarcinoma in Tubulovillous Adenoma (8263/3) ',2,NULL,NULL,NULL,NULL,0,'ENG'),(2644,274,0,'04/MAY/10 10:46:19.803368 AM AUSTRALIA/PERTH','HISTOLOGY','Adenoma, Tubulovillous (8263/0) ',18,NULL,NULL,NULL,NULL,0,'ENG'),(2645,274,0,'04/MAY/10 10:46:19.929966 AM AUSTRALIA/PERTH','HISTOLOGY','Cyst, Corpus Luteum (N83.1) ',59,NULL,NULL,NULL,NULL,0,'ENG'),(2646,274,0,'04/MAY/10 10:46:20.053203 AM AUSTRALIA/PERTH','HISTOLOGY','Leiomyoma, M8890/0 ',100,NULL,NULL,NULL,NULL,0,'ENG'),(2647,274,0,'04/MAY/10 10:46:20.213465 AM AUSTRALIA/PERTH','HISTOLOGY','Mucinous Tumour, borderline (8472/1) ',114,NULL,NULL,NULL,NULL,0,'ENG'),(2648,274,0,'04/MAY/10 10:46:20.373313 AM AUSTRALIA/PERTH','HISTOLOGY','Peritonitis, Pelvic Female NOS (N73.5) ',122,NULL,NULL,NULL,NULL,0,'ENG'),(2649,274,0,'04/MAY/10 10:46:20.532925 AM AUSTRALIA/PERTH','HISTOLOGY','Polyp, Colon (N63.5) ',123,NULL,NULL,NULL,NULL,0,'ENG'),(2650,274,0,'04/MAY/10 10:46:20.692954 AM AUSTRALIA/PERTH','HISTOLOGY','Uterus, non-inflamm disorder (N85) ',138,NULL,NULL,NULL,NULL,0,'ENG'),(2651,0,0,'04/MAY/10 12:05:07.789240 PM +08:00','BIOSPECSTOREDIN','Parrafin Block',9,1,NULL,NULL,NULL,0,'ENG'),(2652,0,0,'04/MAY/10 12:07:54.733803 PM +08:00','BIOSPECSTOREDIN','10ml tube',5,1,NULL,NULL,NULL,0,'ENG'),(2653,0,0,'04/MAY/10 12:07:54.893646 PM +08:00','BIOSPECSTOREDIN','50ml tube',6,1,NULL,NULL,NULL,0,'ENG'),(2654,0,0,'04/MAY/10 12:08:10.183578 PM +08:00','BIOSPECSTOREDIN','2ml tube',4,1,NULL,NULL,NULL,0,'ENG'),(2655,274,0,'04/MAY/10 12:10:01.796567 PM +08:00','BIOSPECTREATMENTS','RN later',5,NULL,NULL,NULL,NULL,0,'ENG'),(2657,0,0,'06/MAY/10 08:50:27.407232 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Throat Swab',1,2,'Throat Swab','BIOSPECCOLLECTIONTYPE','Saliva',0,'ENG'),(2658,274,0,'18/MAY/2010 08:23:17.427117 AM AUSTRALIA/PERTH','BIO_QUANTITY_UNITS','pcs',3,1,'Pieces',NULL,NULL,0,'Eng'),(2673,274,0,'22/JUN/2010 10:10:11.517167 AM AUSTRALIA/PERTH','REF_DOCTOR','Millwood, M',16,NULL,NULL,NULL,NULL,1,'ENG'),(2674,274,0,'22/JUN/2010 10:10:11.783372 AM AUSTRALIA/PERTH','REF_DOCTOR','Salfinger, S',22,NULL,NULL,NULL,NULL,1,'ENG'),(2675,274,0,'22/JUN/2010 10:10:11.951612 AM AUSTRALIA/PERTH','REF_DOCTOR','Rao, S',21,NULL,NULL,NULL,NULL,1,'ENG'),(2676,274,0,'22/JUN/2010 10:10:12.111759 AM AUSTRALIA/PERTH','REF_DOCTOR','Ng, S',17,NULL,NULL,NULL,NULL,1,'ENG'),(2677,274,0,'22/JUN/2010 10:10:12.271550 AM AUSTRALIA/PERTH','REF_DOCTOR','Tebbutt, N',28,NULL,NULL,NULL,NULL,1,'ENG'),(2680,274,0,'22/JUN/2010 10:10:12.751688 AM AUSTRALIA/PERTH','REF_DOCTOR','Leung, Y',11,NULL,NULL,NULL,NULL,1,'ENG'),(2681,274,0,'22/JUN/2010 10:10:12.911589 AM AUSTRALIA/PERTH','REF_DOCTOR','Taylor, M',27,NULL,NULL,NULL,NULL,1,'ENG'),(2682,274,0,'22/JUN/2010 10:10:13.071556 AM AUSTRALIA/PERTH','REF_DOCTOR','Hammond, I',7,NULL,NULL,NULL,NULL,1,'ENG'),(2683,114,0,'22/JUN/2010 10:24:58.522875 AM AUSTRALIA/PERTH','HOSPITAL','Randwick (Prince of Wales Private Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2684,114,0,'22/JUN/2010 10:24:58.656922 AM AUSTRALIA/PERTH','HOSPITAL','Randwick (Prince of Wales Public Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2685,114,0,'22/JUN/2010 10:24:58.787244 AM AUSTRALIA/PERTH','HOSPITAL','St Leonards (North Shore Private Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2686,114,0,'22/JUN/2010 10:24:58.909463 AM AUSTRALIA/PERTH','HOSPITAL','St Leonards (Royal North Shore Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2687,114,0,'22/JUN/2010 10:24:58.999565 AM AUSTRALIA/PERTH','HOSPITAL','Liverpool (Liverpool Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2688,114,0,'22/JUN/2010 10:24:59.085007 AM AUSTRALIA/PERTH','HOSPITAL','Killara (Dalcross Private Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2689,114,0,'22/JUN/2010 10:24:59.172811 AM AUSTRALIA/PERTH','HOSPITAL','North Ryde (Macquarie University Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2690,114,0,'22/JUN/2010 10:24:59.259228 AM AUSTRALIA/PERTH','HOSPITAL','Camperdown (Royal Prince Alfred Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2691,114,0,'22/JUN/2010 10:24:59.344547 AM AUSTRALIA/PERTH','HOSPITAL','Darlinghurst (St Vincent\'s Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2692,114,0,'22/JUN/2010 10:24:59.429375 AM AUSTRALIA/PERTH','HOSPITAL','Westmead (Westmead Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2693,114,0,'22/JUN/2010 10:24:59.520787 AM AUSTRALIA/PERTH','HOSPITAL','Westmead (Westmead Private Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2694,114,0,'22/JUN/2010 10:24:59.608952 AM AUSTRALIA/PERTH','HOSPITAL','Wollongong (Wollongong Hospital)',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2695,114,0,'22/JUN/2010 10:24:59.727313 AM AUSTRALIA/PERTH','HOSPITAL','Kogarah (St George Hospital) ',NULL,NULL,NULL,NULL,NULL,0,'ENG'),(2713,0,0,'06/AUG/2010 09:49:25.356970 AM +08:00','ANTICOAG',NULL,0,NULL,NULL,NULL,NULL,0,'ENG'),(2714,0,0,'06/AUG/2010 09:49:25.653538 AM +08:00','BIOSPECGRADE',NULL,0,NULL,NULL,NULL,NULL,0,'ENG'),(2715,0,0,'06/AUG/2010 09:49:25.696537 AM +08:00','BIOSPECSTOREDIN',NULL,0,1,NULL,NULL,NULL,0,'ENG'),(2716,0,0,'06/AUG/2010 09:49:25.701247 AM +08:00','EXTRACTION_PROTOCOL',NULL,0,NULL,NULL,NULL,NULL,0,'ENG'),(2717,0,0,'06/AUG/2010 09:49:25.705303 AM +08:00','QUALITY',NULL,0,NULL,NULL,NULL,NULL,0,'ENG'),(2718,0,0,'06/AUG/2010 09:49:25.709454 AM +08:00','REF_DOCTOR',NULL,0,NULL,NULL,NULL,NULL,0,'ENG'),(2733,274,0,'30-AUG-10 15:40','HISTOLOGY','Fibrous Tumour, Malignant (M8815/3)',89,NULL,NULL,NULL,NULL,0,'ENG'),(2735,274,0,'12/OCT/2010 09:27:08.629173 AM +08:00','BIOSPECTREATMENTS','Unprocessed',8,NULL,NULL,NULL,NULL,0,'ENG'),(2736,274,0,'12/OCT/2010 09:27:09.659156 AM +08:00','BIOSPECTREATMENTS','RNA later, then snap frozen',7,NULL,NULL,NULL,NULL,0,'ENG'),(2737,274,0,'12/OCT/2010 09:27:10.769378 AM +08:00','BIOSPECTREATMENTS','RNA later, then formalin fixed',6,NULL,NULL,NULL,NULL,0,'ENG'),(2740,274,0,'12/OCT/2010 09:27:13.899092 AM +08:00','BIOSPECTREATMENTS','70% Alcohol Fixed',4,NULL,NULL,NULL,NULL,0,'ENG'),(2741,0,0,'01/11/2010 10:22:19.143181 AM AUSTRALIA/PERTH','BIOSPECSTOREDIN','Large tube',8,1,NULL,NULL,NULL,0,'ENG'),(2742,274,0,'23/11/2010 08:46:21.936079 AM AUSTRALIA/PERTH','COLLABORATOR','WA DNA bank',1,1,'None',NULL,NULL,0,'ENG'),(2743,274,0,'23/11/2010 08:46:22.147084 AM AUSTRALIA/PERTH','COLLABORATOR','SJOG Research',2,1,'None',NULL,NULL,0,'ENG'),(2744,274,0,'23/11/2010 08:46:22.348897 AM AUSTRALIA/PERTH','COLLABORATOR','Frank Van Boxmeer',3,1,'None',NULL,NULL,0,'ENG'),(2745,274,0,'23/11/2010 08:51:39.385023 AM AUSTRALIA/PERTH','OWNER','WARTN',0,1,'None',NULL,NULL,0,'ENG'),(2753,0,0,'13/12/2010 11:05:29.495335 AM AUSTRALIA/PERTH','EXTRACTION_PROTOCOL','Qiagen',7,NULL,NULL,NULL,NULL,0,'ENG'),(2754,274,0,'13/12/2010 11:07:40.136995 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','DNA from Tissue',4,1,'DNA from Tissue','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(2773,0,0,'22/12/2010 12:47:34.414352 PM AUSTRALIA/PERTH',NULL,'Saliva',8,1,'Saliva','BIOSPECCOLLECTIONTYPE','Saliva',0,'ENG'),(2774,0,0,'06/APR/10 02:07:34.906458 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Saliva',8,1,'Saliva','BIOSPECCOLLECTIONTYPE','Saliva',0,'ENG'),(2775,18,0,'22/12/2010 05:08:35.544745 PM +08:00','BIOSPECCOLLECTIONTYPE','mRNA',10,1,'mRNA','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(2776,18,0,'22/12/2010 05:08:35.731471 PM +08:00','BIOSPECCOLLECTIONTYPE','Total RNA',9,1,'Total RNA','BIOSPECCOLLECTIONTYPE','Nucleic Acid',0,'ENG'),(2793,0,0,'07/01/2011 09:58:02.237448 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Anus',1,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2794,274,0,'10/01/2011 10:24:10.429294 AM AUSTRALIA/PERTH','BEHAVIOUR','Recurrence',13,NULL,NULL,NULL,NULL,0,'ENG'),(2813,274,0,'24-FEB-11 10.42.19.856431 AM AUSTRALIA/PERTH','PATHSERVICE','PathWest',12,NULL,NULL,NULL,NULL,0,'ENG'),(2814,274,0,'24-FEB-11 10.42.20.218607 AM AUSTRALIA/PERTH','PATHSERVICE','Perth Medical Laboratories',13,NULL,NULL,NULL,NULL,0,'ENG'),(2815,274,0,'24-FEB-11 10.42.20.501385 AM AUSTRALIA/PERTH','PATHSERVICE','General Pathology Laboratories',14,NULL,NULL,NULL,NULL,0,'ENG'),(2816,274,0,'25-FEB-11 05.05.28.004359 PM +08:00','HISTOLOGY','Adenoma, serrated (8213/0)',16,NULL,NULL,NULL,NULL,0,'ENG'),(2833,274,0,'19/04/2011 10:26:02.643023 AM AUSTRALIA/PERTH','HISTOLOGY','Ulcerative Colitis (K51)',137,NULL,NULL,NULL,NULL,0,'ENG'),(2834,0,0,'28/04/2011 10:47:22.635019 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Appendix',2,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2835,0,0,'28/04/2011 10:48:00.255571 AM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Mesentary',19,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2836,114,0,'29-APR-11 02.54.35.248439 PM AUSTRALIA/PERTH','BIOSPECCOLLECTIONTYPE','Brain',2,NULL,NULL,'BIOSPECCOLLECTIONTYPE','Tissue',0,'ENG'),(2837,17,0,'03/05/2011 11:02:23.918282 AM AUSTRALIA/PERTH','REF_DOCTOR','Tammy Hall',22,NULL,NULL,NULL,NULL,0,'ENG'),(2838,17,0,'03/05/2011 11:02:24.074514 AM AUSTRALIA/PERTH','REF_DOCTOR','Melanie Clark',22,NULL,NULL,NULL,NULL,0,'ENG');
/*!40000 ALTER TABLE `listofvalues` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `listofvalues_description`
--

LOCK TABLES `listofvalues_description` WRITE;
/*!40000 ALTER TABLE `listofvalues_description` DISABLE KEYS */;
/*!40000 ALTER TABLE `listofvalues_description` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `samplecode`
--

LOCK TABLES `samplecode` WRITE;
/*!40000 ALTER TABLE `samplecode` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplecode` ENABLE KEYS */;
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
  KEY `fk_study_inv_site_study` (`STUDY_ID`),
  KEY `fk_study_inv_site_inv_site` (`INV_SITE_ID`),
  CONSTRAINT `fk_study_inv_site_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_inv_site` FOREIGN KEY (`INV_SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (1,'mm',NULL),(2,'ug/L',NULL),(3,'Years',NULL),(4,'mÂµ/l',NULL),(5,'bpm',NULL),(6,'g/L',NULL),(7,'fL',NULL),(8,'feet',NULL),(9,'IU/L',NULL),(10,'kg',NULL),(11,'U',NULL),(12,'ÂµV',NULL),(13,'Days',NULL),(14,'mg/l',NULL),(15,'Age',NULL),(16,'cm',NULL),(17,'m/L',NULL),(18,'IÂµ/mL',NULL),(19,'pg',NULL),(20,'row 2',NULL),(21,'grams',NULL),(22,'pred',NULL),(23,'Gy',NULL),(24,'Hours',NULL),(25,'Âµ/L',NULL),(26,'Mins',NULL),(27,'%',NULL),(28,'mS',NULL),(29,'mm/hr',NULL),(30,'mg/dl',NULL),(31,'mn',NULL),(33,'mg/L',NULL),(34,'kgm2',NULL),(35,'mm Hg',NULL),(36,'kg/m2',NULL),(37,'Pipes',NULL),(38,'L',NULL),(39,'S',NULL),(40,'m',NULL),(41,'fl',NULL),(42,'hours',NULL),(43,'mm/hg',NULL);
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `reporting`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `reporting` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `reporting`;

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
INSERT INTO `report_output_format` VALUES (1,'PDF','Portable Document Format (compatible with Adobe Reader)'),(2,'CSV','Comma Separated Value (compatible with Excel)');
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
  CONSTRAINT `FK_REPORTTEMPLATE_ARKFUNCTION` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_template`
--

LOCK TABLES `report_template` WRITE;
/*!40000 ALTER TABLE `report_template` DISABLE KEYS */;
INSERT INTO `report_template` VALUES (1,'Study Summary Report','This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>','StudySummaryReport.jrxml',1,23),(2,'Study-level Consent Details Report','This report lists detailed subject information for a particular study based on their consent status at the study-level.','ConsentDetailsReport.jrxml',2,24),(3,'Study Component Consent Details Report','This report lists detailed subject information for a particular study based on their consent status for a specific study component.','ConsentDetailsReport.jrxml',2,25),(4,'Phenotypic Field Details Report (Data Dictionary)','This report lists detailed field information for a particular study based on their associated phenotypic collection.','DataDictionaryReport.jrxml',3,26),(5,'Study User Role Permissions Report','This report lists all user role and permissions for the study in context.','StudyUserRolePermissions.jrxml',1,33);
/*!40000 ALTER TABLE `report_template` ENABLE KEYS */;
UNLOCK TABLES;

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
/*!50013 DEFINER=`arkadmin`@`localhost` SQL SECURITY DEFINER */
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
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `study_user_role_permission_view` AS select distinct `study`.`NAME` AS `studyName`,`ark_user`.`LDAP_USER_NAME` AS `userName`,`ark_role`.`NAME` AS `roleName`,`ark_module`.`NAME` AS `moduleName`,max(if((`arpt`.`ARK_PERMISSION_ID` = 1),_utf8'Y',_utf8'N')) AS `create`,max(if((`arpt`.`ARK_PERMISSION_ID` = 2),_utf8'Y',_utf8'N')) AS `read`,max(if((`arpt`.`ARK_PERMISSION_ID` = 3),_utf8'Y',_utf8'N')) AS `update`,max(if((`arpt`.`ARK_PERMISSION_ID` = 4),_utf8'Y',_utf8'N')) AS `delete` from ((((((`ark_role_policy_template` `arpt` join `ark_role`) join `ark_user_role`) join `ark_user`) join `ark_module`) join `ark_permission` `ap`) join `study`) where ((`arpt`.`ARK_ROLE_ID` = `ark_role`.`ID`) and (`arpt`.`ARK_MODULE_ID` = `ark_module`.`ID`) and (`arpt`.`ARK_PERMISSION_ID` = `ap`.`ID`) and (`arpt`.`ARK_ROLE_ID` = `ark_user_role`.`ARK_ROLE_ID`) and (`arpt`.`ARK_MODULE_ID` = `ark_user_role`.`ARK_MODULE_ID`) and (`ark_user_role`.`ARK_ROLE_ID` = `ark_role`.`ID`) and (`ark_user_role`.`ARK_MODULE_ID` = `ark_module`.`ID`) and (`ark_user_role`.`ARK_USER_ID` = `ark_user`.`ID`) and (`ark_user_role`.`STUDY_ID` = `study`.`ID`)) group by `study`.`NAME`,`ark_user`.`LDAP_USER_NAME`,`ark_role`.`NAME`,`ark_module`.`NAME` order by `ark_user_role`.`STUDY_ID`,`ark_user`.`LDAP_USER_NAME`,`ark_role`.`ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Current Database: `pheno`
--

USE `pheno`;

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
/*!50013 DEFINER=`arkadmin`@`localhost` SQL SECURITY DEFINER */
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
/*!50013 DEFINER=`arkadmin`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `field_upload_v` AS select distinct `upload`.`ID` AS `ID`,`upload`.`STUDY_ID` AS `STUDY_ID`,`upload`.`FILE_FORMAT_ID` AS `FILE_FORMAT_ID`,`upload`.`DELIMITER_TYPE_ID` AS `DELIMITER_TYPE_ID`,`upload`.`FILENAME` AS `FILENAME`,`upload`.`PAYLOAD` AS `PAYLOAD`,`upload`.`CHECKSUM` AS `CHECKSUM`,`upload`.`USER_ID` AS `USER_ID`,`upload`.`INSERT_TIME` AS `INSERT_TIME`,`upload`.`UPDATE_USER_ID` AS `UPDATE_USER_ID`,`upload`.`UPDATE_TIME` AS `UPDATE_TIME`,`upload`.`START_TIME` AS `START_TIME`,`upload`.`FINISH_TIME` AS `FINISH_TIME`,`upload`.`UPLOAD_REPORT` AS `UPLOAD_REPORT` from (`upload` join `field_upload`) where (`upload`.`ID` = `field_upload`.`UPLOAD_ID`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Current Database: `geno`
--

USE `geno`;

--
-- Current Database: `lims`
--

USE `lims`;

--
-- Current Database: `reporting`
--

USE `reporting`;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-09-02 14:02:24
