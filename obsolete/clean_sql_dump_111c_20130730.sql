-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: admin
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.12.04.2

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
INSERT INTO `billable_item_type_status` VALUES (1,'ACTIVE','Active record'),(2,'INACTIVE','Inactive record');
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
INSERT INTO `billing_type` VALUES (1,'EFT',NULL),(2,'CHEQUE',NULL),(3,'CASH',NULL);
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `researcher_role`
--

LOCK TABLES `researcher_role` WRITE;
/*!40000 ALTER TABLE `researcher_role` DISABLE KEYS */;
INSERT INTO `researcher_role` VALUES (0,'Principal Investigator',NULL),(1,'Chief Investigator',NULL),(2,'Assoc Investigator',NULL),(3,'Other Investigator',NULL),(4,'Research Assistant',NULL);
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
INSERT INTO `researcher_status` VALUES (1,'Active',NULL),(2,'Inactive','');
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
INSERT INTO `work_request_status` VALUES (1,'Not Commenced',''),(2,'Commenced',''),(3,'Completed','');
/*!40000 ALTER TABLE `work_request_status` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`),
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
) ENGINE=InnoDB AUTO_INCREMENT=65440 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_TYPE_ID`) REFER `study/addre';
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
INSERT INTO `address_type` VALUES (1,'RESIDENTIAL',NULL),(2,'WORK',NULL),(3,'POSTAL',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_function`
--

LOCK TABLES `ark_function` WRITE;
/*!40000 ALTER TABLE `ark_function` DISABLE KEYS */;
INSERT INTO `ark_function` VALUES (1,'STUDY','Study Management usecase. This is represented via the Study Detail Tab under the main Study  Tab. ',1,'tab.module.study.details'),(2,'STUDY_COMPONENT','Study Component usecase. This is represented via the StudyComponent Tab under the main Study  Tab. ',1,'tab.module.study.components'),(3,'MY_DETAIL','Edit My details usecase, this is represented via My Detail tab.',1,'tab.module.mydetails'),(4,'USER','User management usecase. This is represented via the User Tab under the main Study  Tab.',1,'tab.module.user.management'),(5,'SUBJECT','Subject management usecase. This is represented via the Subject Tab under the main Study  Tab.',1,'tab.module.subject.detail'),(6,'PHONE','Manage phone usecase. This is represented via the Phone tab under the main Study  Tab.',1,'tab.module.person.phone'),(7,'ADDRESS','Manage Address',1,'tab.module.person.address'),(8,'ATTACHMENT','Manage Consent and Component attachments. This is represented via the Attachment tab under Subject Main tab.',1,'tab.module.subject.subjectFile'),(9,'CONSENT','Manage Subject Consents. This is represented via the Consent tab under the main Study  Tab.',1,'tab.module.subject.consent'),(10,'SUBJECT_UPLOAD','Bulk upload of Subjects.',1,'tab.module.subject.subjectUpload'),(11,'SUBJECT_CUSTOM_FIELD','Manage Custom Fields for Subjects.',1,'tab.module.subject.subjectcustomfield'),(12,'DATA_DICTIONARY','Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.field'),(13,'DATA_DICTIONARY_UPLOAD','Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldUpload'),(14,'PHENO_COLLECTION','Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.collection'),(15,'FIELD_DATA','Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldData'),(16,'FIELD_DATA_UPLOAD','Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.phenoUpload'),(17,'LIMS_SUBJECT','LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.',1,'tab.module.lims.subject.detail'),(18,'LIMS_COLLECTION','LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.',1,'tab.module.lims.collection'),(19,'BIOSPECIMEN','LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.',1,'tab.module.lims.biospecimen'),(20,'INVENTORY','LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.',1,'tab.module.lims.inventory'),(21,'CORRESPONDENCE','',1,'tab.module.subject.correspondence'),(22,'SUMMARY','Phenotypic Summary.',1,'tab.module.phenotypic.summary'),(23,'REPORT_STUDYSUMARY','Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>',2,NULL),(24,'REPORT_STUDYLEVELCONSENT','Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.',2,NULL),(25,'REPORT_STUDYCOMPCONSENT','Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.',2,NULL),(26,'REPORT_PHENOFIELDDETAILS','Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic data set.',2,NULL),(27,'GENO_COLLECTION','Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu',1,'tab.module.geno.collection'),(28,'ROLE_POLICY_TEMPLATE','Allows CRUD operations on the ark_role_policy_template table for the Ark application',1,'tab.module.admin.rolePolicyTemplate'),(29,'MODULE','Allows CRUD operations on the ark_module table for the Ark application',1,'tab.module.admin.module'),(30,'FUNCTION','Allows CRUD operations on the ark_function table for the Ark application',1,'tab.module.admin.function'),(33,'REPORT_STUDY_USER_ROLE_PERMISSIONS','Study User Role Permissions Report lists all user role and permissions for the study in context.',2,NULL),(34,'SUBJECT_CUSTOM_DATA','Data entry for Subject Custom Fields.',1,'tab.module.subject.subjectcustomdata'),(35,'LIMS_COLLECTION_CUSTOM_FIELD','Manage Custom Fields for LIMS collections.',1,'tab.module.lims.collectioncustomfield'),(36,'LIMS_COLLECTION_CUSTOM_DATA','Data entry for LIMS collection Custom Fields.',1,'tab.module.lims.collectioncustomdata'),(37,'BIOSPECIMEN_CUSTOM_FIELD','Manage Custom Fields for Biospecimens.',1,'tab.module.lims.biospecimencustomfield'),(38,'BIOSPECIMEN_CUSTOM_DATA','Data entry for Biospecimen Custom Fields.',1,'tab.module.lims.biospecimencustomdata'),(41,'BIOSPECIMENUID_TEMPLATE','Manage BiospecimenUid templates for the study,',1,'tab.module.lims.biospecimenuidtemplate'),(42,'BARCODE_LABEL','Manage barcode label definitions the study,',1,'tab.module.lims.barcodelabel'),(43,'BARCODE_PRINTER','Manage barcode printers for the study,',1,'tab.module.lims.barcodeprinter'),(44,'MODULE_FUNCTION','Allows CRUD operations on the ark_module_function table for the Ark application',1,'tab.module.admin.modulefunction'),(45,'ROLE','Allows CRUD operations on user roles',1,'tab.module.admin.role'),(46,'MODULE_ROLE','Allows CRUD operations on module_role table',1,'tab.module.admin.modulerole'),(47,'BIOSPECIMEN_UPLOAD','Uploader for bispecimens',1,'tab.module.lims.biospecimenUpload'),(48,'SUBJECT_CUSTOM_FIELD_UPLOAD','Uploader for Subject Custom Fields',1,'tab.module.subject.subjectCustomFieldUpload'),(49,'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD','Uploader for BioCollection Custom Fields',1,'tab.module.lims.bioCollectionCustomFieldUpload'),(50,'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD','Uploader for Biospecimen Custom Fields',1,'tab.module.lims.biospecimenCustomFieldUpload'),(58,'BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD','Uploader for both Biospecimen and Biocollection Custom Fields',1,'tab.module.lims.bioupload'),(59,'RESEARCHER','Researcher tab',1,'tab.module.work.researcher'),(60,'BILLABLE_ITEM_TYPE','Billable item type tab',1,'tab.module.work.billableitemtype'),(61,'WORK_REQUEST','Work Request tab',1,'tab.module.work.workrequest'),(62,'BILLABLE_ITEM','Billable Item Tab',1,'tab.module.work.billableitem'),(63,'DATA_EXTRACTION','Advanced search for data extraction',1,'tab.reporting.dataextraction'),(64,'PEDIGREE','Pedigree visualization',1,'tab.module.subject.pedigree');
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module`
--

LOCK TABLES `ark_module` WRITE;
/*!40000 ALTER TABLE `ark_module` DISABLE KEYS */;
INSERT INTO `ark_module` VALUES (1,'Study',NULL),(2,'Subject',NULL),(3,'Datasets',NULL),(4,'Genotypic',NULL),(5,'LIMS',NULL),(6,'Reporting',NULL),(8,'Work Tracking','Work Tracking Module'),(9,'Admin',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module_function`
--

LOCK TABLES `ark_module_function` WRITE;
/*!40000 ALTER TABLE `ark_module_function` DISABLE KEYS */;
INSERT INTO `ark_module_function` VALUES (13,3,12,2),(14,3,13,3),(15,3,14,4),(26,3,26,NULL),(27,4,27,1),(88,9,29,1),(89,9,30,2),(90,9,44,3),(91,9,45,4),(92,9,46,5),(93,9,28,6),(111,5,20,3),(112,5,47,4),(113,5,35,5),(114,5,37,6),(115,5,43,7),(116,5,42,8),(118,5,49,50),(119,5,50,50),(120,8,59,1),(121,8,60,2),(122,8,61,3),(123,8,62,4),(136,1,23,1),(137,1,1,2),(138,1,2,3),(139,1,4,4),(140,1,11,5),(141,1,48,6),(142,6,63,1),(143,1,10,7),(157,2,24,1),(158,2,25,2),(159,2,5,3),(160,2,34,4),(161,2,6,5),(162,2,7,6),(163,2,8,7),(164,2,9,8),(165,2,21,9),(166,2,15,10),(167,2,17,11),(168,2,19,12);
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_module_role`
--

LOCK TABLES `ark_module_role` WRITE;
/*!40000 ALTER TABLE `ark_module_role` DISABLE KEYS */;
INSERT INTO `ark_module_role` VALUES (3,2,4),(4,2,5),(5,2,6),(6,3,7),(7,3,8),(8,5,9),(9,5,10),(10,4,11),(11,5,12),(12,3,13),(22,8,2),(23,8,15),(24,8,16),(25,8,17),(26,6,18),(27,6,19),(28,6,20),(35,1,2),(36,1,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_role`
--

LOCK TABLES `ark_role` WRITE;
/*!40000 ALTER TABLE `ark_role` DISABLE KEYS */;
INSERT INTO `ark_role` VALUES (1,'Super Administrator','Highest level user of the ARK system'),(2,'Study Administrator',NULL),(3,'Study Read-Only user',NULL),(4,'Subject Administrator',NULL),(5,'Subject Data Manager',NULL),(6,'Subject Read-Only user',NULL),(7,'Pheno Read-Only user',NULL),(8,'Pheno Data Manager',NULL),(9,'LIMS Read-Only user',NULL),(10,'LIMS Data Manager',NULL),(11,'Geno Read-Only User',NULL),(12,'LIMS Administrator',NULL),(13,'Pheno Administrator',NULL),(14,'New Role',NULL),(15,'Work Tracking Administrator','Work Tracking Administrator'),(16,'Work Tracking Data Manager','Work Tracking Data Manager'),(17,'Work Tracking Read-Only User','Work Tracking Read-Only User'),(18,'Reporting Administrator','Reporting Administrator'),(19,'Reporting Data Manager','Reporting Data Manager'),(20,'Reporting Read-Only User','Reporting Read-Only User');
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
) ENGINE=InnoDB AUTO_INCREMENT=610 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_role_policy_template`
--

LOCK TABLES `ark_role_policy_template` WRITE;
/*!40000 ALTER TABLE `ark_role_policy_template` DISABLE KEYS */;
INSERT INTO `ark_role_policy_template` VALUES (1,1,NULL,NULL,1),(2,1,NULL,NULL,2),(3,1,NULL,NULL,3),(4,1,NULL,NULL,4),(7,2,1,2,1),(8,2,1,2,2),(9,2,1,2,3),(10,2,1,3,2),(11,2,1,3,3),(12,2,1,4,1),(13,2,1,4,2),(14,2,1,4,3),(15,3,1,1,2),(16,3,1,2,2),(20,4,2,5,1),(21,4,2,5,2),(22,4,2,5,3),(23,4,2,6,1),(24,4,2,6,2),(25,4,2,6,3),(26,4,2,6,4),(27,4,2,7,1),(28,4,2,7,2),(29,4,2,7,3),(30,4,2,7,4),(31,4,2,8,1),(32,4,2,8,2),(33,4,2,8,3),(34,4,2,8,4),(35,4,2,9,1),(36,4,2,9,2),(37,4,2,9,3),(38,4,2,9,4),(39,5,2,5,2),(40,5,2,6,2),(41,5,2,6,3),(42,5,2,7,2),(43,5,2,7,3),(44,5,2,8,2),(45,5,2,8,3),(46,5,2,9,2),(47,5,2,9,3),(48,4,2,10,1),(49,4,2,10,2),(50,4,2,10,3),(51,4,2,11,1),(52,4,2,11,2),(53,4,2,11,3),(54,5,2,5,1),(55,5,2,5,3),(56,5,2,6,1),(57,5,2,6,4),(58,5,2,7,1),(59,5,2,7,4),(60,5,2,8,1),(61,5,2,8,4),(62,5,2,9,1),(63,5,2,9,4),(64,6,2,5,2),(65,6,2,6,2),(66,6,2,7,2),(67,6,2,8,2),(68,6,2,9,2),(69,6,2,34,2),(70,8,3,12,2),(71,8,3,13,2),(74,8,3,16,1),(75,10,5,17,3),(79,9,5,17,2),(80,9,5,18,2),(81,9,5,19,2),(82,9,5,20,2),(83,7,3,12,2),(84,7,3,13,2),(85,7,3,14,2),(86,7,3,15,2),(87,7,3,16,2),(88,5,2,10,1),(89,8,3,22,2),(91,10,5,17,4),(92,2,1,23,2),(93,3,1,23,2),(94,4,2,24,2),(95,5,2,24,2),(96,6,2,24,2),(98,5,2,25,2),(99,6,2,25,2),(100,7,3,26,2),(101,8,3,26,2),(102,10,5,17,4),(104,11,4,27,2),(106,8,3,14,2),(107,6,2,21,2),(108,5,2,21,1),(109,5,2,21,2),(110,5,2,21,3),(111,5,2,21,4),(112,4,2,21,1),(113,4,2,21,2),(114,4,2,21,3),(115,4,2,21,4),(116,12,5,17,1),(117,12,5,17,2),(118,12,5,17,3),(119,12,5,17,4),(120,12,5,18,1),(121,12,5,18,2),(122,12,5,18,3),(123,12,5,18,4),(124,12,5,19,1),(125,12,5,19,2),(126,12,5,19,3),(127,12,5,19,4),(128,12,5,20,1),(129,12,5,20,2),(130,12,5,20,3),(131,12,5,20,4),(133,13,3,13,1),(136,13,3,16,1),(138,13,3,26,2),(142,1,9,28,1),(143,1,9,28,2),(144,1,9,28,3),(145,1,9,28,4),(146,13,3,22,2),(147,7,3,22,2),(148,13,3,12,1),(149,13,3,12,2),(184,4,2,34,1),(185,4,2,34,2),(186,4,2,34,3),(187,4,2,34,4),(188,5,2,34,1),(189,5,2,34,2),(190,5,2,34,3),(191,5,2,34,4),(192,12,5,35,1),(193,12,5,35,2),(194,12,5,35,3),(195,12,5,36,1),(196,12,5,36,2),(197,12,5,36,3),(198,12,5,36,4),(199,10,5,36,1),(200,10,5,36,2),(201,10,5,36,3),(202,10,5,36,4),(203,9,5,36,2),(204,12,5,37,1),(205,12,5,37,2),(206,12,5,37,3),(207,12,5,38,1),(208,12,5,38,2),(209,12,5,38,3),(210,12,5,38,4),(211,10,5,38,1),(212,10,5,38,2),(213,10,5,38,3),(214,10,5,38,4),(215,9,5,38,2),(228,8,3,15,1),(229,8,3,15,2),(230,8,3,15,3),(233,3,1,3,2),(246,14,1,23,2),(247,14,1,1,2),(248,14,1,2,2),(249,14,1,4,2),(250,2,1,23,2),(252,2,1,2,2),(253,2,1,4,2),(254,3,1,23,2),(255,3,1,1,2),(256,3,1,2,2),(257,3,1,4,2),(258,4,1,23,2),(259,4,1,1,2),(260,4,1,2,2),(261,4,1,4,2),(262,2,1,33,2),(263,13,3,15,1),(264,13,3,15,2),(265,13,3,15,3),(266,13,3,15,4),(267,13,3,14,1),(268,13,3,14,2),(269,13,3,14,3),(270,13,3,14,4),(279,10,5,47,1),(280,10,5,47,2),(281,10,5,47,3),(282,10,5,47,4),(283,12,5,47,1),(284,12,5,47,2),(285,12,5,47,3),(286,12,5,47,4),(287,2,1,1,1),(288,2,1,1,2),(289,2,1,1,3),(290,12,5,42,1),(291,12,5,42,2),(292,12,5,42,3),(293,12,5,42,4),(294,12,5,43,1),(295,12,5,43,2),(296,12,5,43,3),(297,12,5,43,4),(298,4,2,48,1),(299,4,2,48,2),(300,4,2,48,3),(301,4,2,48,4),(302,12,5,49,1),(303,12,5,49,2),(304,12,5,49,3),(305,12,5,49,4),(306,12,5,50,1),(307,12,5,50,2),(308,12,5,50,3),(309,12,5,50,4),(310,12,5,58,1),(311,12,5,58,2),(312,12,5,58,3),(313,12,5,58,4),(314,2,8,59,1),(315,2,8,59,2),(316,2,8,59,3),(317,2,8,59,4),(318,2,8,60,1),(319,2,8,60,2),(320,2,8,60,3),(321,2,8,60,4),(322,2,8,61,1),(323,2,8,61,2),(324,2,8,61,3),(325,2,8,61,4),(326,2,8,62,1),(327,2,8,62,2),(328,2,8,62,3),(329,2,8,62,4),(330,15,8,59,1),(331,15,8,59,2),(332,15,8,59,3),(333,15,8,59,4),(334,15,8,60,1),(335,15,8,60,2),(336,15,8,60,3),(337,15,8,60,4),(338,15,8,61,1),(339,15,8,61,2),(340,15,8,61,3),(341,15,8,61,4),(342,15,8,62,1),(343,15,8,62,2),(344,15,8,62,3),(345,15,8,62,4),(346,16,8,59,1),(347,16,8,59,2),(348,16,8,59,3),(349,16,8,60,1),(350,16,8,60,2),(351,16,8,60,3),(352,16,8,61,1),(353,16,8,61,2),(354,16,8,61,3),(355,16,8,62,1),(356,16,8,62,2),(357,16,8,62,3),(358,17,8,59,2),(359,17,8,60,2),(360,17,8,61,2),(361,17,8,62,2),(362,18,6,63,1),(363,18,6,63,2),(364,18,6,63,3),(365,18,6,63,4),(366,19,6,63,1),(367,19,6,63,2),(368,19,6,63,3),(369,19,6,63,4),(370,20,6,63,2),(371,2,1,10,2),(372,2,1,10,1),(373,2,1,10,3),(374,10,5,18,1),(375,10,5,18,2),(376,10,5,18,3),(377,10,5,18,4),(378,10,5,19,1),(379,10,5,19,2),(380,10,5,19,3),(381,10,5,19,4),(382,10,5,20,1),(383,10,5,20,2),(384,10,5,20,3),(408,4,2,25,2),(420,5,2,25,2),(432,6,2,25,2),(444,7,2,25,2),(456,8,2,25,2),(468,13,2,25,2),(480,13,2,25,2),(492,8,2,25,2),(504,7,2,25,2),(516,4,2,25,2),(528,5,2,25,2),(540,6,2,25,2),(552,18,2,25,2),(585,4,2,25,2),(597,5,2,25,2),(609,6,2,25,2);
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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=5646 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ark_user_role`
--

LOCK TABLES `ark_user_role` WRITE;
/*!40000 ALTER TABLE `ark_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `ark_user_role` ENABLE KEYS */;
UNLOCK TABLES;


/*
-- Query: select
        *
    from
        study.ARK_USER_ROLE this_ 
    where
        this_.ARK_USER_ID=1
LIMIT 0, 2000

-- Date: 2013-07-31 16:59
*/
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (1,1,1,1,NULL);
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (2,1,1,2,NULL);
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (3,1,1,3,NULL);
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (4,1,1,4,NULL);
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (5,1,1,5,NULL);
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (7,1,1,9,NULL);
INSERT INTO study.`ark_user_role` (`ID`,`ARK_USER_ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`STUDY_ID`) VALUES (45,1,1,8,NULL);



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
) ENGINE=InnoDB AUTO_INCREMENT=11646 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ACTION_TYPE_ID`) REFER `study/action';
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
) ENGINE=InnoDB AUTO_INCREMENT=193667 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
INSERT INTO `consent_option` VALUES (1,'Yes'),(2,'No'),(3,'Pending'),(4,'Unavailable'),(5,'Limited'),(6,'Revoked');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consent_status`
--

LOCK TABLES `consent_status` WRITE;
/*!40000 ALTER TABLE `consent_status` DISABLE KEYS */;
INSERT INTO `consent_status` VALUES (1,'Consented','Subject Consented'),(2,'Not Consented','Subject Not Consented'),(3,'Ineligible','Ineligible'),(4,'Refused','Refused'),(5,'Withdrawn','Withdrawn'),(6,'Pending','Pending');
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
  `BILLABLE_ITEM_ID` int(11) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=12667 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
INSERT INTO `country` VALUES (1,'Australia','AU','AUS','036',''),(2,'United Kingdom','GB','GBR','826','United Kingdom of Great Britain and Northern '),(3,'Canada','CA','CAN','124',''),(4,'Afghanistan','AF','AFG','004','Islamic Republic of Afghanistan'),(5,'Aland Islands','AX','ALA','248','Åland Islands'),(6,'Albania','AL','ALB','008','Republic of Albania'),(7,'Algeria','DZ','DZA','012','People\'s Democratic Republic of Algeria'),(8,'American Samoa','AS','ASM','016',''),(9,'Andorra','AD','AND','020','Principality of Andorra'),(10,'Angola','AO','AGO','024','Republic of Angola'),(11,'Anguilla','AI','AIA','660',''),(12,'Antarctica','AQ','ATA','010',''),(13,'Antigua and Barbuda','AG','ATG','028',''),(14,'Argentina','AR','ARG','032','Argentine Republic'),(15,'Armenia','AM','ARM','051','Republic of Armenia'),(16,'Aruba','AW','ABW','533',''),(18,'Austria','AT','AUT','040','Republic of Austria'),(19,'Azerbaijan','AZ','AZE','031','Republic of Azerbaijan'),(20,'Bahamas','BS','BHS','044','Commonwealth of the Bahamas'),(21,'Bahrain','BH','BHR','048','Kingdom of Bahrain'),(22,'Bangladesh','BD','BGD','050','People\'s Republic of Bangladesh'),(23,'Barbados','BB','BRB','052',''),(24,'Belarus','BY','BLR','112','Republic of Belarus'),(25,'Belgium','BE','BEL','056','Kingdom of Belgium'),(26,'Belize','BZ','BLZ','084',''),(27,'Benin','BJ','BEN','204','Republic of Benin'),(28,'Bermuda','BM','BMU','060',''),(29,'Bhutan','BT','BTN','064','Kingdom of Bhutan'),(30,'Bolivia','BO','BOL','068','Republic of Bolivia'),(31,'BONAIRE, SAINT EUSTATIUS AND SABA','BQ','','',''),(32,'Bosnia and Herzegovina','BA','BIH','070','Republic of Bosnia and Herzegovina'),(33,'Botswana','BW','BWA','072','Republic of Botswana'),(34,'Bouvet Island','BV','BVT','074',''),(35,'Brazil','BR','BRA','076','Federative Republic of Brazil'),(36,'British Indian Ocean Territory','IO','IOT','086',''),(37,'Brunei Darussalam','BN','BRN','096',''),(38,'Bulgaria','BG','BGR','100','Republic of Bulgaria'),(39,'Burkina Faso','BF','BFA','854',''),(40,'Burundi','BI','BDI','108','Republic of Burundi'),(41,'Cambodia','KH','KHM','116','Kingdom of Cambodia'),(42,'Cameroon','CM','CMR','120','Republic of Cameroon'),(44,'Cape Verde','CV','CPV','132','Republic of Cape Verde'),(45,'Cayman Islands','KY','CYM','136',''),(46,'Central African Republic','CF','CAF','140',''),(47,'Chad','TD','TCD','148','Republic of Chad'),(48,'Chile','CL','CHL','152','Republic of Chile'),(49,'China','CN','CHN','156','People\'s Republic of China'),(50,'Christmas Island','CX','CXR','162',''),(51,'Cocos (Keeling) Islands','CC','CCK','166',''),(52,'Colombia','CO','COL','170','Republic of Colombia'),(53,'Comoros','KM','COM','174','Union of the Comoros'),(54,'Congo','CG','COG','178','Republic of the Congo'),(55,'Congo, The Democratic Republic of the','CD','COD','180',''),(56,'Cook Islands','CK','COK','184',''),(57,'Costa Rica','CR','CRI','188','Republic of Costa Rica'),(58,'Côte d\'Ivoire','CI','CIV','384','Republic of Côte d\'Ivoire'),(59,'Croatia','HR','HRV','191','Republic of Croatia'),(60,'Cuba','CU','CUB','192','Republic of Cuba'),(61,'CURACAO','CW','','',''),(62,'Cyprus','CY','CYP','196','Republic of Cyprus'),(63,'Czech Republic','CZ','CZE','203',''),(64,'Denmark','DK','DNK','208','Kingdom of Denmark'),(65,'Djibouti','DJ','DJI','262','Republic of Djibouti'),(66,'Dominica','DM','DMA','212','Commonwealth of Dominica'),(67,'Dominican Republic','DO','DOM','214',''),(68,'Ecuador','EC','ECU','218','Republic of Ecuador'),(69,'Egypt','EG','EGY','818','Arab Republic of Egypt'),(70,'El Salvador','SV','SLV','222','Republic of El Salvador'),(71,'Equatorial Guinea','GQ','GNQ','226','Republic of Equatorial Guinea'),(72,'Eritrea','ER','ERI','232',''),(73,'Estonia','EE','EST','233','Republic of Estonia'),(74,'Ethiopia','ET','ETH','231','Federal Democratic Republic of Ethiopia'),(75,'Falkland Islands (Malvinas)','FK','FLK','238',''),(76,'Faroe Islands','FO','FRO','234',''),(77,'Fiji','FJ','FJI','242','Republic of the Fiji Islands'),(78,'Finland','FI','FIN','246','Republic of Finland'),(79,'France','FR','FRA','250','French Republic'),(80,'French Guiana','GF','GUF','254',''),(81,'French Polynesia','PF','PYF','258',''),(82,'French Southern Territories','TF','ATF','260',''),(83,'Gabon','GA','GAB','266','Gabonese Republic'),(84,'Gambia','GM','GMB','270','Republic of the Gambia'),(85,'Georgia','GE','GEO','268',''),(86,'Germany','DE','DEU','276','Federal Republic of Germany'),(87,'Ghana','GH','GHA','288','Republic of Ghana'),(88,'Gibraltar','GI','GIB','292',''),(89,'Greece','GR','GRC','300','Hellenic Republic'),(90,'Greenland','GL','GRL','304',''),(91,'Grenada','GD','GRD','308',''),(92,'Guadeloupe','GP','GLP','312',''),(93,'Guam','GU','GUM','316',''),(94,'Guatemala','GT','GTM','320','Republic of Guatemala'),(95,'Guernsey','GG','GGY','831',''),(96,'Guinea','GN','GIN','324','Republic of Guinea'),(97,'Guinea-Bissau','GW','GNB','624','Republic of Guinea-Bissau'),(98,'Guyana','GY','GUY','328','Republic of Guyana'),(99,'Haiti','HT','HTI','332','Republic of Haiti'),(100,'Heard Island and McDonald Islands','HM','HMD','334',''),(101,'Holy See (Vatican City State)','VA','VAT','336',''),(102,'Honduras','HN','HND','340','Republic of Honduras'),(103,'Hong Kong','HK','HKG','344','Hong Kong Special Administrative Region of Ch'),(104,'Hungary','HU','HUN','348','Republic of Hungary'),(105,'Iceland','IS','ISL','352','Republic of Iceland'),(106,'India','IN','IND','356','Republic of India'),(107,'Indonesia','ID','IDN','360','Republic of Indonesia'),(108,'Iran, Islamic Republic of','IR','IRN','364','Islamic Republic of Iran'),(109,'Iraq','IQ','IRQ','368','Republic of Iraq'),(110,'Ireland','IE','IRL','372',''),(111,'Isle of Man','IM','IMN','833',''),(112,'Israel','IL','ISR','376','State of Israel'),(113,'Italy','IT','ITA','380','Italian Republic'),(114,'Jamaica','JM','JAM','388',''),(115,'Japan','JP','JPN','392',''),(116,'Jersey','JE','JEY','832',''),(117,'Jordan','JO','JOR','400','Hashemite Kingdom of Jordan'),(118,'Kazakhstan','KZ','KAZ','398','Republic of Kazakhstan'),(119,'Kenya','KE','KEN','404','Republic of Kenya'),(120,'Kiribati','KI','KIR','296','Republic of Kiribati'),(121,'Korea, Democratic People\'s Republic of','KP','PRK','408','Democratic People\'s Republic of Korea'),(122,'Korea, Republic of','KR','KOR','410',''),(123,'Kuwait','KW','KWT','414','State of Kuwait'),(124,'Kyrgyzstan','KG','KGZ','417','Kyrgyz Republic'),(125,'Lao People\'s Democratic Republic','LA','LAO','418',''),(126,'Latvia','LV','LVA','428','Republic of Latvia'),(127,'Lebanon','LB','LBN','422','Lebanese Republic'),(128,'Lesotho','LS','LSO','426','Kingdom of Lesotho'),(129,'Liberia','LR','LBR','430','Republic of Liberia'),(130,'Libyan Arab Jamahiriya','LY','LBY','434','Socialist People\'s Libyan Arab Jamahiriya'),(131,'Liechtenstein','LI','LIE','438','Principality of Liechtenstein'),(132,'Lithuania','LT','LTU','440','Republic of Lithuania'),(133,'Luxembourg','LU','LUX','442','Grand Duchy of Luxembourg'),(134,'Macao','MO','MAC','446','Macao Special Administrative Region of China'),(135,'Macedonia, Republic of','MK','MKD','807','The Former Yugoslav Republic of Macedonia'),(136,'Madagascar','MG','MDG','450','Republic of Madagascar'),(137,'Malawi','MW','MWI','454','Republic of Malawi'),(138,'Malaysia','MY','MYS','458',''),(139,'Maldives','MV','MDV','462','Republic of Maldives'),(140,'Mali','ML','MLI','466','Republic of Mali'),(141,'Malta','MT','MLT','470','Republic of Malta'),(142,'Marshall Islands','MH','MHL','584','Republic of the Marshall Islands'),(143,'Martinique','MQ','MTQ','474',''),(144,'Mauritania','MR','MRT','478','Islamic Republic of Mauritania'),(145,'Mauritius','MU','MUS','480','Republic of Mauritius'),(146,'Mayotte','YT','MYT','175',''),(147,'Mexico','MX','MEX','484','United Mexican States'),(148,'Micronesia, Federated States of','FM','FSM','583','Federated States of Micronesia'),(149,'Moldova','MD','MDA','498','Republic of Moldova'),(150,'Monaco','MC','MCO','492','Principality of Monaco'),(151,'Mongolia','MN','MNG','496',''),(152,'Montenegro','ME','MNE','499','Montenegro'),(153,'Montserrat','MS','MSR','500',''),(154,'Morocco','MA','MAR','504','Kingdom of Morocco'),(155,'Mozambique','MZ','MOZ','508','Republic of Mozambique'),(156,'Myanmar','MM','MMR','104','Union of Myanmar'),(157,'Namibia','NA','NAM','516','Republic of Namibia'),(158,'Nauru','NR','NRU','520','Republic of Nauru'),(159,'Nepal','NP','NPL','524','Kingdom of Nepal'),(160,'Netherlands','NL','NLD','528','Kingdom of the Netherlands'),(161,'New Caledonia','NC','NCL','540',''),(162,'New Zealand','NZ','NZL','554',''),(163,'Nicaragua','NI','NIC','558','Republic of Nicaragua'),(164,'Niger','NE','NER','562','Republic of the Niger'),(165,'Nigeria','NG','NGA','566','Federal Republic of Nigeria'),(166,'Niue','NU','NIU','570','Republic of Niue'),(167,'Norfolk Island','NF','NFK','574',''),(168,'Northern Mariana Islands','MP','MNP','580','Commonwealth of the Northern Mariana Islands'),(169,'Norway','NO','NOR','578','Kingdom of Norway'),(170,'Oman','OM','OMN','512','Sultanate of Oman'),(171,'Pakistan','PK','PAK','586','Islamic Republic of Pakistan'),(172,'Palau','PW','PLW','585','Republic of Palau'),(173,'Palestinian Territory, Occupied','PS','PSE','275','Occupied Palestinian Territory'),(174,'Panama','PA','PAN','591','Republic of Panama'),(175,'Papua New Guinea','PG','PNG','598',''),(176,'Paraguay','PY','PRY','600','Republic of Paraguay'),(177,'Peru','PE','PER','604','Republic of Peru'),(178,'Philippines','PH','PHL','608','Republic of the Philippines'),(179,'Pitcairn','PN','PCN','612',''),(180,'Poland','PL','POL','616','Republic of Poland'),(181,'Portugal','PT','PRT','620','Portuguese Republic'),(182,'Puerto Rico','PR','PRI','630',''),(183,'Qatar','QA','QAT','634','State of Qatar'),(184,'Reunion','RE','REU','638',''),(185,'Romania','RO','ROU','642',''),(186,'Russian Federation','RU','RUS','643',''),(187,'Rwanda','RW','RWA','646','Rwandese Republic'),(188,'Saint Barthélemy','BL','BLM','652',''),(189,'Saint Helena','SH','SHN','654','SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA'),(190,'Saint Kitts and Nevis','KN','KNA','659',''),(191,'Saint Lucia','LC','LCA','662',''),(192,'Saint Martin (French part)','MF','MAF','663',''),(193,'Saint Pierre and Miquelon','PM','SPM','666',''),(194,'Saint Vincent and the Grenadines','VC','VCT','670',''),(195,'Samoa','WS','WSM','882','Independent State of Samoa'),(196,'San Marino','SM','SMR','674','Republic of San Marino'),(197,'Sao Tome and Principe','ST','STP','678','Democratic Republic of Sao Tome and Principe'),(198,'Saudi Arabia','SA','SAU','682','Kingdom of Saudi Arabia'),(199,'Senegal','SN','SEN','686','Republic of Senegal'),(200,'Serbia','RS','SRB','688','Republic of Serbia'),(201,'Seychelles','SC','SYC','690','Republic of Seychelles'),(202,'Sierra Leone','SL','SLE','694','Republic of Sierra Leone'),(203,'Singapore','SG','SGP','702','Republic of Singapore'),(204,'SINT MAARTEN (DUTCH PART)','SX','','',''),(205,'Slovakia','SK','SVK','703','Slovak Republic'),(206,'Slovenia','SI','SVN','705','Republic of Slovenia'),(207,'Solomon Islands','SB','SLB','090',''),(208,'Somalia','SO','SOM','706','Somali Republic'),(209,'South Africa','ZA','ZAF','710','Republic of South Africa'),(210,'South Georgia and the South Sandwich Islands','GS','SGS','239',''),(211,'Spain','ES','ESP','724','Kingdom of Spain'),(212,'Sri Lanka','LK','LKA','144','Democratic Socialist Republic of Sri Lanka'),(213,'Sudan','SD','SDN','736','Republic of the Sudan'),(214,'Suriname','SR','SUR','740','Republic of Suriname'),(215,'Svalbard and Jan Mayen','SJ','SJM','744',''),(216,'Swaziland','SZ','SWZ','748','Kingdom of Swaziland'),(217,'Sweden','SE','SWE','752','Kingdom of Sweden'),(218,'Switzerland','CH','CHE','756','Swiss Confederation'),(219,'Syrian Arab Republic','SY','SYR','760',''),(220,'Taiwan, Province of China','TW','TWN','158','Taiwan, Province of China'),(221,'Tajikistan','TJ','TJK','762','Republic of Tajikistan'),(222,'Tanzania, United Republic of','TZ','TZA','834','United Republic of Tanzania'),(223,'Thailand','TH','THA','764','Kingdom of Thailand'),(224,'Timor-Leste','TL','TLS','626','Democratic Republic of Timor-Leste'),(225,'Togo','TG','TGO','768','Togolese Republic'),(226,'Tokelau','TK','TKL','772',''),(227,'Tonga','TO','TON','776','Kingdom of Tonga'),(228,'Trinidad and Tobago','TT','TTO','780','Republic of Trinidad and Tobago'),(229,'Tunisia','TN','TUN','788','Republic of Tunisia'),(230,'Turkey','TR','TUR','792','Republic of Turkey'),(231,'Turkmenistan','TM','TKM','795',''),(232,'Turks and Caicos Islands','TC','TCA','796',''),(233,'Tuvalu','TV','TUV','798',''),(234,'Uganda','UG','UGA','800','Republic of Uganda'),(235,'Ukraine','UA','UKR','804',''),(236,'United Arab Emirates','AE','ARE','784',''),(238,'United States','US','USA','840','United States of America'),(239,'United States Minor Outlying Islands','UM','UMI','581',''),(240,'Uruguay','UY','URY','858','Eastern Republic of Uruguay'),(241,'Uzbekistan','UZ','UZB','860','Republic of Uzbekistan'),(242,'Vanuatu','VU','VUT','548','Republic of Vanuatu'),(243,'VATICAN CITY STATE','VA','','',''),(244,'Venezuela','VE','VEN','862','Bolivarian Republic of Venezuela'),(245,'Viet Nam','VN','VNM','704','Socialist Republic of Viet Nam'),(246,'Virgin Islands, British','VG','VGB','092','British Virgin Islands'),(247,'Virgin Islands, U.S.','VI','VIR','850','Virgin Islands of the United States'),(248,'Wallis and Futuna','WF','WLF','876',''),(249,'Western Sahara','EH','ESH','732',''),(250,'Yemen','YE','YEM','887','Republic of Yemen'),(251,'Zambia','ZM','ZMB','894','Republic of Zambia'),(252,'Zimbabwe','ZW','ZWE','716','Republic of Zimbabwe');
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
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=latin1;
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
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `custom_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_group`
--

LOCK TABLES `custom_field_group` WRITE;
/*!40000 ALTER TABLE `custom_field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_group` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
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
INSERT INTO `email_status` VALUES (0,'Unknown','A default where status has not been specified'),(1,'Verified','Verified as a valid email'),(2,'Unverified','Not verified as a valid email address'),(3,'Bounced','An email to this address bounced');
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
INSERT INTO `field_type` VALUES (1,'CHARACTER',1),(2,'NUMBER',1),(3,'DATE',1),(4,'LOOKUP',0);
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
INSERT INTO `file_format` VALUES (1,'CSV','Comma separated values'),(2,'TXT','Tab separated text file'),(3,'XLS','Excel Spreadsheet'),(4,'PED','Pedigree format');
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
) ENGINE=InnoDB AUTO_INCREMENT=482 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_study_arkmodule`
--

LOCK TABLES `link_study_arkmodule` WRITE;
/*!40000 ALTER TABLE `link_study_arkmodule` DISABLE KEYS */;
INSERT INTO `link_study_arkmodule` VALUES (425,567,1),(426,567,2),(427,567,5),(428,567,6),(429,567,8);
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
  `FAMILY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) DEFAULT NULL,
  `RELATIVE_ID` int(11) DEFAULT NULL,
  `RELATIONSHIP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
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
  `SUBJECT_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `SUBJECT_UID` varchar(50) NOT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_ID` int(11) DEFAULT NULL,
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
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_PERSON_FK` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STUDY_FK` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` FOREIGN KEY (`SUBJECT_STATUS_ID`) REFERENCES `subject_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=87842 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
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
INSERT INTO `measurement_type` VALUES (1,'Distance',NULL),(2,'Volume',NULL),(3,'Time',NULL),(4,'Weight',NULL),(5,'Weight per Volume',NULL),(6,'Distance per Time',NULL),(7,'Percentage or Fraction',NULL),(999,'Other',NULL);
/*!40000 ALTER TABLE `measurement_type` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COMMENT='This is a simple table for storing LOBs and an id to represent them.';
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
  `OTHER_EMAIL_STATUS` int(11) DEFAULT '0',
  `PREFERRED_EMAIL_STATUS` int(11) DEFAULT '0',
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
) ENGINE=InnoDB AUTO_INCREMENT=60294 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`GENDER_TYPE_ID`) REFER `study/gender';
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
  `date_inserted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `SURNAME` (`LASTNAME`) USING BTREE,
  KEY `PERSON_ID` (`PERSON_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26897 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
  `PHONE_NUMBER` varchar(20) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `PHONE_TYPE_ID` int(11) NOT NULL,
  `PHONE_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `SOURCE` varchar(500) DEFAULT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENT` varchar(1000) DEFAULT NULL,
  `SILENT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `AREA_CODE_2` (`AREA_CODE`,`PHONE_NUMBER`,`PHONE_TYPE_ID`,`PERSON_ID`),
  KEY `PHONE_PHONE_TYPE_FK` (`PHONE_TYPE_ID`) USING BTREE,
  KEY `PHONE_PERSON_FK` (`PERSON_ID`) USING BTREE,
  KEY `phone_ibfk_3` (`PHONE_STATUS_ID`),
  KEY `phone_ibfk_4` (`SILENT`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_2` FOREIGN KEY (`PHONE_TYPE_ID`) REFERENCES `phone_type` (`ID`),
  CONSTRAINT `phone_ibfk_3` FOREIGN KEY (`PHONE_STATUS_ID`) REFERENCES `phone_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_4` FOREIGN KEY (`SILENT`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=51985 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
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
INSERT INTO `phone_status` VALUES (0,'Unknown','Status not known, this will be the default if no status provided'),(1,'Current',NULL),(2,'Current Alternative',NULL),(3,'Current Under Investigation',NULL),(4,'Valid Past',NULL),(5,'Incorrect or Disconnected',NULL);
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
INSERT INTO `relationship` VALUES (0,'Father','Father'),(1,'Mother','Mother');
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
INSERT INTO `state` VALUES (1,1,'State','Western Australia','AU-WA','WA'),(2,1,'State','New South Wales','AU-NSW','NSW'),(3,1,'State','Victoria','AU-VIC','VIC'),(4,1,'Territory','Australian Capital Territory','AU-ACT','ACT'),(5,1,'Territory','Northern Territory','AU-NT','NT'),(6,1,'State','Queensland','AU-QLD','QLD'),(7,3,'','','','Alberta'),(8,2,'','','','Bedfordshire'),(9,2,'','','','Berkshire'),(10,1,'State','Tasmania','AU-TAS','TAS'),(11,1,'State','South Australia','AU-SA','SA'),(12,9,'Parish','Andorra la Vella','AD-07',''),(13,9,'Parish','Canillo','AD-02',''),(14,9,'Parish','Encamp','AD-03',''),(15,9,'Parish','Escaldes-Engordany','AD-08',''),(16,9,'Parish','La Massana','AD-04',''),(17,9,'Parish','Ordino','AD-05',''),(18,9,'Parish','Sant Julià de Lòria','AD-06',''),(19,236,'Emirate','Ab? ?aby [Abu Dhabi]','AE-AZ',''),(20,236,'Emirate','\'Ajm?n','AE-AJ',''),(21,236,'Emirate','Al Fujayrah','AE-FU',''),(22,236,'Emirate','Ash Sh?riqah','AE-SH',''),(23,236,'Emirate','Dubayy','AE-DU',''),(24,236,'Emirate','Ra’s al Khaymah','AE-RK',''),(25,236,'Emirate','Umm al Qaywayn','AE-UQ',''),(26,4,'Province','Badakhsh?n','AF-BDS',''),(27,4,'Province','B?dgh?s','AF-BDG',''),(28,4,'Province','Baghl?n','AF-BGL',''),(29,4,'Province','Balkh','AF-BAL',''),(30,4,'Province','B?m??n','AF-BAM',''),(31,4,'Province','D?ykond?','AF-DAY',''),(32,4,'Province','Far?h','AF-FRA',''),(33,4,'Province','F?ry?b','AF-FYB',''),(34,4,'Province','Ghazn?','AF-GHA',''),(35,4,'Province','Ghowr','AF-GHO',''),(36,4,'Province','Helmand','AF-HEL',''),(37,4,'Province','Her?t','AF-HER',''),(38,4,'Province','Jowzj?n','AF-JOW',''),(39,4,'Province','K?bul [K?bol]','AF-KAB',''),(40,4,'Province','Kandah?r','AF-KAN',''),(41,4,'Province','K?p?s?','AF-KAP',''),(42,4,'Province','Khowst','AF-KHO',''),(43,4,'Province','Konar [Kunar]','AF-KNR',''),(44,4,'Province','Kondoz [Kunduz]','AF-KDZ',''),(45,4,'Province','Laghm?n','AF-LAG',''),(46,4,'Province','Lowgar','AF-LOW',''),(47,4,'Province','Nangrah?r [Nangarh?r]','AF-NAN',''),(48,4,'Province','N?mr?z','AF-NIM',''),(49,4,'Province','N?rest?n','AF-NUR',''),(50,4,'Province','Or?zg?n [Ur?zg?n]','AF-ORU',''),(51,4,'Province','Panjsh?r','AF-PAN',''),(52,4,'Province','Pakt??','AF-PIA',''),(53,4,'Province','Pakt?k?','AF-PKA',''),(54,4,'Province','Parw?n','AF-PAR',''),(55,4,'Province','Samang?n','AF-SAM',''),(56,4,'Province','Sar-e Pol','AF-SAR',''),(57,4,'Province','Takh?r','AF-TAK',''),(58,4,'Province','Wardak [Wardag]','AF-WAR',''),(59,4,'Province','Z?bol [Z?bul]','AF-ZAB',''),(60,13,'Parish','Saint George','AG-03',''),(61,13,'Parish','Saint John','AG-04',''),(62,13,'Parish','Saint Mary','AG-05',''),(63,13,'Parish','Saint Paul','AG-06',''),(64,13,'Parish','Saint Peter','AG-07',''),(65,13,'Parish','Saint Philip','AG-08',''),(66,13,'Dependency','Barbuda','AG-10',''),(67,6,'County','Berat','AL 1',''),(68,6,'County','Dibër','AL 9',''),(69,6,'County','Durrës','AL 2',''),(70,6,'County','Elbasan','AL 3',''),(71,6,'County','Fier','AL 4',''),(72,6,'County','Gjirokastër','AL 5',''),(73,6,'County','Korçë','AL 6',''),(74,6,'County','Kukës','AL 7',''),(75,6,'County','Lezhë','AL 8',''),(76,6,'County','Shkodër','AL 10',''),(77,6,'County','Tiranë','AL 11',''),(78,6,'County','Vlorë','AL 12',''),(79,6,'District','Berat','AL-BR',''),(80,6,'District','Bulqizë','AL-BU',''),(81,6,'District','Delvinë','AL-DL',''),(82,6,'District','Devoll','AL-DV',''),(83,6,'District','Dibër','AL-DI',''),(84,6,'District','Durrës','AL-DR',''),(85,6,'District','Elbasan','AL-EL',''),(86,6,'District','Fier','AL-FR',''),(87,6,'District','Gramsh','AL-GR',''),(88,6,'District','Gjirokastër','AL-GJ',''),(89,6,'District','Has','AL-HA',''),(90,6,'District','Kavajë','AL-KA',''),(91,6,'District','Kolonjë','AL-ER',''),(92,6,'District','Korçë','AL-KO',''),(93,6,'District','Krujë','AL-KR',''),(94,6,'District','Kuçovë','AL-KC',''),(95,6,'District','Kukës','AL-KU',''),(96,6,'District','Kurbin','AL-KB',''),(97,6,'District','Lezhë','AL-LE',''),(98,6,'District','Librazhd','AL-LB',''),(99,6,'District','Lushnjë','AL-LU',''),(100,6,'District','Malësi e Madhe','AL-MM',''),(101,6,'District','Mallakastër','AL-MK',''),(102,6,'District','Mat','AL-MT',''),(103,6,'District','Mirditë','AL-MR',''),(104,6,'District','Peqin','AL-PQ',''),(105,6,'District','Përmet','AL-PR',''),(106,6,'District','Pogradec','AL-PG',''),(107,6,'District','Pukë','AL-PU',''),(108,6,'District','Sarandë','AL-SR',''),(109,6,'District','Skrapar','AL-SK',''),(110,6,'District','Shkodër','AL-SH',''),(111,6,'District','Tepelenë','AL-TE',''),(112,6,'District','Tiranë','AL-TR',''),(113,6,'District','Tropojë','AL-TP',''),(114,6,'District','Vlorë','AL-VL',''),(115,15,'Province','Erevan','AM-ER',''),(116,15,'Province','Aragacotn','AM-AG',''),(117,15,'Province','Ararat','AM-AR',''),(118,15,'Province','Armavir','AM-AV',''),(119,15,'Province','Gegarkunik\'','AM-GR',''),(120,15,'Province','Kotayk\'','AM-KT',''),(121,15,'Province','Lory','AM-LO',''),(122,15,'Province','Sirak','AM-SH',''),(123,15,'Province','Syunik\'','AM-SU',''),(124,15,'Province','Tavus','AM-TV',''),(125,15,'Province','Vayoc Jor','AM-VD',''),(126,10,'Province','Bengo','AO-BGO',''),(127,10,'Province','Benguela','AO-BGU',''),(128,10,'Province','Bié','AO-BIE',''),(129,10,'Province','Cabinda','AO-CAB',''),(130,10,'Province','Cuando-Cubango','AO-CCU',''),(131,10,'Province','Cuanza Norte','AO-CNO',''),(132,10,'Province','Cuanza Sul','AO-CUS',''),(133,10,'Province','Cunene','AO-CNN',''),(134,10,'Province','Huambo','AO-HUA',''),(135,10,'Province','Huíla','AO-HUI',''),(136,10,'Province','Luanda','AO-LUA',''),(137,10,'Province','Lunda Norte','AO-LNO',''),(138,10,'Province','Lunda Sul','AO-LSU',''),(139,10,'Province','Malange','AO-MAL',''),(140,10,'Province','Moxico','AO-MOX',''),(141,10,'Province','Namibe','AO-NAM',''),(142,10,'Province','Uíge','AO-UIG',''),(143,10,'Province','Zaire','AO-ZAI',''),(144,14,'Province','Capital federal','AR-C',''),(145,14,'Province','Buenos Aires','AR-B',''),(146,14,'Province','Catamarca','AR-K',''),(147,14,'Province','Cordoba','AR-X',''),(148,14,'Province','Corrientes','AR-W',''),(149,14,'Province','Chaco','AR-H',''),(150,14,'Province','Chubut','AR-U',''),(151,14,'Province','Entre Rios','AR-E',''),(152,14,'Province','Formosa','AR-P',''),(153,14,'Province','Jujuy','AR-Y',''),(154,14,'Province','La Pampa','AR-L',''),(155,14,'Province','Mendoza','AR-M',''),(156,14,'Province','Misiones','AR-N',''),(157,14,'Province','Neuquen','AR-Q',''),(158,14,'Province','Rio Negro','AR-R',''),(159,14,'Province','Salta','AR-A',''),(160,14,'Province','San Juan','AR-J',''),(161,14,'Province','San Luis','AR-D',''),(162,14,'Province','Santa Cruz','AR-Z',''),(163,14,'Province','Santa Fe','AR-S',''),(164,14,'Province','Santiago del Estero','AR-G',''),(165,14,'Province','Tierra del Fuego','AR-V',''),(166,14,'Province','Tucuman','AR-T',''),(167,18,'State','Burgenland','AT-1',''),(168,18,'State','Kärnten','AT-2',''),(169,18,'State','Niederösterreich','AT-3',''),(170,18,'State','Oberösterreich','AT-4',''),(171,18,'State','Salzburg','AT-5',''),(172,18,'State','Steiermark','AT-6',''),(173,18,'State','Tirol','AT-7',''),(174,18,'State','Vorarlberg','AT-8',''),(175,18,'State','Wien','AT-9',''),(176,19,'Autonomous republic','Naxç?van','AZ NX',''),(177,19,'City','?li Bayraml?','AZ-AB',''),(178,19,'City','Bak?','AZ-BA',''),(179,19,'City','G?nc?','AZ-GA',''),(180,19,'City','L?nk?ran','AZ-LA',''),(181,19,'City','Ming?çevir','AZ-MI',''),(182,19,'City','Naftalan','AZ-NA',''),(183,19,'City','??ki','AZ-SA',''),(184,19,'City','Sumqay?t','AZ-SM',''),(185,19,'City','?u?a','AZ-SS',''),(186,19,'City','Xank?ndi','AZ-XA',''),(187,19,'City','Yevlax','AZ-YE',''),(188,19,'Rayon','Ab?eron','AZ-ABS',''),(189,19,'Rayon','A?cab?di','AZ-AGC',''),(190,19,'Rayon','A?dam','AZ-AGM',''),(191,19,'Rayon','A?da?','AZ-AGS',''),(192,19,'Rayon','A?stafa','AZ-AGA',''),(193,19,'Rayon','A?su','AZ-AGU',''),(194,19,'Rayon','Astara','AZ-AST',''),(195,19,'Rayon','Bab?k','AZ-BAB',''),(196,19,'Rayon','Balak?n','AZ-BAL',''),(197,19,'Rayon','B?rd?','AZ-BAR',''),(198,19,'Rayon','Beyl?qan','AZ-BEY',''),(199,19,'Rayon','Bil?suvar','AZ-BIL',''),(200,19,'Rayon','C?bray?l','AZ-CAB',''),(201,19,'Rayon','C?lilabab','AZ-CAL',''),(202,19,'Rayon','Culfa','AZ-CUL',''),(203,19,'Rayon','Da?k?s?n','AZ-DAS',''),(204,19,'Rayon','D?v?çi','AZ-DAV',''),(205,19,'Rayon','Füzuli','AZ-FUZ',''),(206,19,'Rayon','G?d?b?y','AZ-GAD',''),(207,19,'Rayon','Goranboy','AZ-GOR',''),(208,19,'Rayon','Göyçay','AZ-GOY',''),(209,19,'Rayon','Hac?qabul','AZ-HAC',''),(210,19,'Rayon','?mi?li','AZ-IMI',''),(211,19,'Rayon','?smay?ll?','AZ-ISM',''),(212,19,'Rayon','K?lb?c?r','AZ-KAL',''),(213,19,'Rayon','Kürd?mir','AZ-KUR',''),(214,19,'Rayon','Laç?n','AZ-LAC',''),(215,19,'Rayon','L?nk?ran','AZ-LAN',''),(216,19,'Rayon','Lerik','AZ-LER',''),(217,19,'Rayon','Masall?','AZ-MAS',''),(218,19,'Rayon','Neftçala','AZ-NEF',''),(219,19,'Rayon','O?uz','AZ-OGU',''),(220,19,'Rayon','Ordubad','AZ-ORD',''),(221,19,'Rayon','Q?b?l?','AZ-QAB',''),(222,19,'Rayon','Qax','AZ-QAX',''),(223,19,'Rayon','Qazax','AZ-QAZ',''),(224,19,'Rayon','Qobustan','AZ-QOB',''),(225,19,'Rayon','Quba','AZ-QBA',''),(226,19,'Rayon','Qubadl?','AZ-QBI',''),(227,19,'Rayon','Qusar','AZ-QUS',''),(228,19,'Rayon','Saatl?','AZ-SAT',''),(229,19,'Rayon','Sabirabad','AZ-SAB',''),(230,19,'Rayon','S?d?r?k','AZ-SAD',''),(231,19,'Rayon','?ahbuz','AZ-SAH',''),(232,19,'Rayon','??ki','AZ-SAK',''),(233,19,'Rayon','Salyan','AZ-SAL',''),(234,19,'Rayon','?amax?','AZ-SMI',''),(235,19,'Rayon','??mkir','AZ-SKR',''),(236,19,'Rayon','Samux','AZ-SMX',''),(237,19,'Rayon','??rur','AZ-SAR',''),(238,19,'Rayon','Siy?z?n','AZ-SIY',''),(239,19,'Rayon','?u?a','AZ-SUS',''),(240,19,'Rayon','T?rt?r','AZ-TAR',''),(241,19,'Rayon','Tovuz','AZ-TOV',''),(242,19,'Rayon','Ucar','AZ-UCA',''),(243,19,'Rayon','Xaçmaz','AZ-XAC',''),(244,19,'Rayon','Xanlar','AZ-XAN',''),(245,19,'Rayon','X?z?','AZ-XIZ',''),(246,19,'Rayon','Xocal?','AZ-XCI',''),(247,19,'Rayon','Xocav?nd','AZ-XVD',''),(248,19,'Rayon','Yard?ml?','AZ-YAR',''),(249,19,'Rayon','Yevlax','AZ-YEV',''),(250,19,'Rayon','Z?ngilan','AZ-ZAN',''),(251,19,'Rayon','Zaqatala','AZ-ZAQ',''),(252,19,'Rayon','Z?rdab','AZ-ZAR',''),(253,32,'Entity','Federacija Bosna i Hercegovina','BA-BIH',''),(254,32,'Entity','Republika Srpska','BA-SRP',''),(255,23,'Parish','Christ Church','BB-01',''),(256,23,'Parish','Saint Andrew','BB-02',''),(257,23,'Parish','Saint George','BB-03',''),(258,23,'Parish','Saint James','BB-04',''),(259,23,'Parish','Saint John','BB-05',''),(260,23,'Parish','Saint Joseph','BB-06',''),(261,23,'Parish','Saint Lucy','BB-07',''),(262,23,'Parish','Saint Michael','BB-08',''),(263,23,'Parish','Saint Peter','BB-09',''),(264,23,'Parish','Saint Philip','BB-10',''),(265,23,'Parish','Saint Thomas','BB-11',''),(266,22,'Division','Barisal bibhag','BD 1',''),(267,22,'Division','Chittagong bibhag','BD 2',''),(268,22,'Division','Dhaka bibhag','BD 3',''),(269,22,'Division','Khulna bibhag','BD 4',''),(270,22,'Division','Rajshahi bibhag','BD 5',''),(271,22,'Division','Sylhet bibhag','BD 6',''),(272,22,'District','Bagerhat zila','BD-05',''),(273,22,'District','Bandarban zila','BD-01',''),(274,22,'District','Barguna zila','BD-02',''),(275,22,'District','Barisal zila','BD-06',''),(276,22,'District','Bhola zila','BD-07',''),(277,22,'District','Bogra zila','BD-03',''),(278,22,'District','Brahmanbaria zila','BD-04',''),(279,22,'District','Chandpur zila','BD-09',''),(280,22,'District','Chittagong zila','BD-10',''),(281,22,'District','Chuadanga zila','BD-12',''),(282,22,'District','Comilla zila','BD-08',''),(283,22,'District','Cox\'s Bazar zila','BD-11',''),(284,22,'District','Dhaka zila','BD-13',''),(285,22,'District','Dinajpur zila','BD-14',''),(286,22,'District','Faridpur zila','BD-15',''),(287,22,'District','Feni zila','BD-16',''),(288,22,'District','Gaibandha zila','BD-19',''),(289,22,'District','Gazipur zila','BD-18',''),(290,22,'District','Gopalganj zila','BD-17',''),(291,22,'District','Habiganj zila','BD-20',''),(292,22,'District','Jaipurhat zila','BD-24',''),(293,22,'District','Jamalpur zila','BD-21',''),(294,22,'District','Jessore zila','BD-22',''),(295,22,'District','Jhalakati zila','BD-25',''),(296,22,'District','Jhenaidah zila','BD-23',''),(297,22,'District','Khagrachari zila','BD-29',''),(298,22,'District','Khulna zila','BD-27',''),(299,22,'District','Kishorganj zila','BD-26',''),(300,22,'District','Kurigram zila','BD-28',''),(301,22,'District','Kushtia zila','BD-30',''),(302,22,'District','Lakshmipur zila','BD-31',''),(303,22,'District','Lalmonirhat zila','BD-32',''),(304,22,'District','Madaripur zila','BD-36',''),(305,22,'District','Magura zila','BD-37',''),(306,22,'District','Manikganj zila','BD-33',''),(307,22,'District','Meherpur zila','BD-39',''),(308,22,'District','Moulvibazar zila','BD-38',''),(309,22,'District','Munshiganj zila','BD-35',''),(310,22,'District','Mymensingh zila','BD-34',''),(311,22,'District','Naogaon zila','BD-48',''),(312,22,'District','Narail zila','BD-43',''),(313,22,'District','Narayanganj zila','BD-40',''),(314,22,'District','Narsingdi zila','BD-42',''),(315,22,'District','Natore zila','BD-44',''),(316,22,'District','Nawabganj zila','BD-45',''),(317,22,'District','Netrakona zila','BD-41',''),(318,22,'District','Nilphamari zila','BD-46',''),(319,22,'District','Noakhali zila','BD-47',''),(320,22,'District','Pabna zila','BD-49',''),(321,22,'District','Panchagarh zila','BD-52',''),(322,22,'District','Patuakhali zila','BD-51',''),(323,22,'District','Pirojpur zila','BD-50',''),(324,22,'District','Rajbari zila','BD-53',''),(325,22,'District','Rajshahi zila','BD-54',''),(326,22,'District','Rangamati zila','BD-56',''),(327,22,'District','Rangpur zila','BD-55',''),(328,22,'District','Satkhira zila','BD-58',''),(329,22,'District','Shariatpur zila','BD-62',''),(330,22,'District','Sherpur zila','BD-57',''),(331,22,'District','Sirajganj zila','BD-59',''),(332,22,'District','Sunamganj zila','BD-61',''),(333,22,'District','Sylhet zila','BD-60',''),(334,22,'District','Tangail zila','BD-63',''),(335,22,'District','Thakurgaon zila','BD-64',''),(336,25,'Province','Antwerpen','BE-VAN',''),(337,25,'Province','Brabant Wallon','BE-WBR',''),(338,25,'Province','Brussels-Capital Region','BE-BRU',''),(339,25,'Province','Hainaut','BE-WHT',''),(340,25,'Province','Liege','BE-WLG',''),(341,25,'Province','Limburg','BE-VLI',''),(342,25,'Province','Luxembourg','BE-WLX',''),(343,25,'Province','Namur','BE-WNA',''),(344,25,'Province','Oost-Vlaanderen','BE-VOV',''),(345,25,'Province','Vlaams-Brabant','BE-VBR',''),(346,25,'Province','West-Vlaanderen','BE-VWV',''),(347,39,'Province','Balé','BF-BAL',''),(348,39,'Province','Bam','BF-BAM',''),(349,39,'Province','Banwa','BF-BAN',''),(350,39,'Province','Bazèga','BF-BAZ',''),(351,39,'Province','Bougouriba','BF-BGR',''),(352,39,'Province','Boulgou','BF-BLG',''),(353,39,'Province','Boulkiemdé','BF-BLK',''),(354,39,'Province','Comoé','BF-COM',''),(355,39,'Province','Ganzourgou','BF-GAN',''),(356,39,'Province','Gnagna','BF-GNA',''),(357,39,'Province','Gourma','BF-GOU',''),(358,39,'Province','Houet','BF-HOU',''),(359,39,'Province','Ioba','BF-IOB',''),(360,39,'Province','Kadiogo','BF-KAD',''),(361,39,'Province','Kénédougou','BF-KEN',''),(362,39,'Province','Komondjari','BF-KMD',''),(363,39,'Province','Kompienga','BF-KMP',''),(364,39,'Province','Kossi','BF-KOS',''),(365,39,'Province','Koulpélogo','BF-KOP',''),(366,39,'Province','Kouritenga','BF-KOT',''),(367,39,'Province','Kourwéogo','BF-KOW',''),(368,39,'Province','Léraba','BF-LER',''),(369,39,'Province','Loroum','BF-LOR',''),(370,39,'Province','Mouhoun','BF-MOU',''),(371,39,'Province','Naouri','BF-NAO',''),(372,39,'Province','Namentenga','BF-NAM',''),(373,39,'Province','Nayala','BF-NAY',''),(374,39,'Province','Noumbiel','BF-NOU',''),(375,39,'Province','Oubritenga','BF-OUB',''),(376,39,'Province','Oudalan','BF-OUD',''),(377,39,'Province','Passoré','BF-PAS',''),(378,39,'Province','Poni','BF-PON',''),(379,39,'Province','Sanguié','BF-SNG',''),(380,39,'Province','Sanmatenga','BF-SMT',''),(381,39,'Province','Séno','BF-SEN',''),(382,39,'Province','Sissili','BF-SIS',''),(383,39,'Province','Soum','BF-SOM',''),(384,39,'Province','Sourou','BF-SOR',''),(385,39,'Province','Tapoa','BF-TAP',''),(386,39,'Province','Tui','BF-TUI',''),(387,39,'Province','Yagha','BF-YAG',''),(388,39,'Province','Yatenga','BF-YAT',''),(389,39,'Province','Ziro','BF-ZIR',''),(390,39,'Province','Zondoma','BF-ZON',''),(391,39,'Province','Zoundwéogo','BF-ZOU',''),(392,38,'Region','Blagoevgrad','BG-01',''),(393,38,'Region','Burgas','BG-02',''),(394,38,'Region','Dobrich','BG-08',''),(395,38,'Region','Gabrovo','BG-07',''),(396,38,'Region','Haskovo','BG-26',''),(397,38,'Region','Kardzhali','BG-09',''),(398,38,'Region','Kyustendil','BG-10',''),(399,38,'Region','Lovech','BG-11',''),(400,38,'Region','Montana','BG-12',''),(401,38,'Region','Pazardzhik','BG-13',''),(402,38,'Region','Pernik','BG-14',''),(403,38,'Region','Pleven','BG-15',''),(404,38,'Region','Plovdiv','BG-16',''),(405,38,'Region','Razgrad','BG-17',''),(406,38,'Region','Ruse','BG-18',''),(407,38,'Region','Shumen','BG-27',''),(408,38,'Region','Silistra','BG-19',''),(409,38,'Region','Sliven','BG-20',''),(410,38,'Region','Smolyan','BG-21',''),(411,38,'Region','Sofia','BG-23',''),(412,38,'Region','Sofia-Grad','BG-22',''),(413,38,'Region','Stara Zagora','BG-24',''),(414,38,'Region','Targovishte','BG-25',''),(415,38,'Region','Varna','BG-03',''),(416,38,'Region','Veliko Tarnovo','BG-04',''),(417,38,'Region','Vidin','BG-05',''),(418,38,'Region','Vratsa','BG-06',''),(419,38,'Region','Yambol','BG-28',''),(420,21,'Governorate','Al Man?mah (Al ‘??imah)','BH-13',''),(421,21,'Governorate','Al Jan?b?yah','BH-14',''),(422,21,'Governorate','Al Mu?arraq','BH-15',''),(423,21,'Governorate','Al Wus?á','BH-16',''),(424,21,'Governorate','Ash Sham?l?yah','BH-17',''),(425,40,'Province','Bubanza','BI-BB',''),(426,40,'Province','Bujumbura','BI-BJ',''),(427,40,'Province','Bururi','BI-BR',''),(428,40,'Province','Cankuzo','BI-CA',''),(429,40,'Province','Cibitoke','BI-CI',''),(430,40,'Province','Gitega','BI-GI',''),(431,40,'Province','Karuzi','BI-KR',''),(432,40,'Province','Kayanza','BI-KY',''),(433,40,'Province','Kirundo','BI-KI',''),(434,40,'Province','Makamba','BI-MA',''),(435,40,'Province','Muramvya','BI-MU',''),(436,40,'Province','Mwaro','BI-MW',''),(437,40,'Province','Ngozi','BI-NG',''),(438,40,'Province','Rutana','BI-RT',''),(439,40,'Province','Ruyigi','BI-RY',''),(440,27,'Department','Alibori','BJ-AL',''),(441,27,'Department','Atakora','BJ-AK',''),(442,27,'Department','Atlantique','BJ-AQ',''),(443,27,'Department','Borgou','BJ-BO',''),(444,27,'Department','Collines','BJ-CO',''),(445,27,'Department','Donga','BJ-DO',''),(446,27,'Department','Kouffo','BJ-KO',''),(447,27,'Department','Littoral','BJ-LI',''),(448,27,'Department','Mono','BJ-MO',''),(449,27,'Department','Ouémé','BJ-OU',''),(450,27,'Department','Plateau','BJ-PL',''),(451,27,'Department','Zou','BJ-ZO',''),(452,37,'District','Belait','BN-BE',''),(453,37,'District','Brunei-Muara','BN-BM',''),(454,37,'District','Temburong','BN-TE',''),(455,37,'District','Tutong','BN-TU',''),(456,30,'Department','Cochabamba','BO-C',''),(457,30,'Department','Chuquisaca','BO-H',''),(458,30,'Department','El Beni','BO-B',''),(459,30,'Department','La Paz','BO-L',''),(460,30,'Department','Oruro','BO-O',''),(461,30,'Department','Pando','BO-N',''),(462,30,'Department','Potosí','BO-P',''),(463,30,'Department','Santa Cruz','BO-S',''),(464,30,'Department','Tarija','BO-T',''),(465,35,'State','Acre','BR-AC',''),(466,35,'State','Alagoas','BR-AL',''),(467,35,'State','Amazonas','BR-AM',''),(468,35,'State','Amapá','BR-AP',''),(469,35,'State','Bahia','BR-BA',''),(470,35,'State','Ceará','BR-CE',''),(471,35,'State','Espírito Santo','BR-ES',''),(472,35,'State','Fernando de Noronha','BR-FN',''),(473,35,'State','Goiás','BR-GO',''),(474,35,'State','Maranhão','BR-MA',''),(475,35,'State','Minas Gerais','BR-MG',''),(476,35,'State','Mato Grosso do Sul','BR-MS',''),(477,35,'State','Mato Grosso','BR-MT',''),(478,35,'State','Pará','BR-PA',''),(479,35,'State','Paraíba','BR-PB',''),(480,35,'State','Pernambuco','BR-PE',''),(481,35,'State','Piauí','BR-PI',''),(482,35,'State','Paraná','BR-PR',''),(483,35,'State','Rio de Janeiro','BR-RJ',''),(484,35,'State','Rio Grande do Norte','BR-RN',''),(485,35,'State','Rondônia','BR-RO',''),(486,35,'State','Roraima','BR-RR',''),(487,35,'State','Rio Grande do Sul','BR-RS',''),(488,35,'State','Santa Catarina','BR-SC',''),(489,35,'State','Sergipe','BR-SE',''),(490,35,'State','Sâo Paulo','BR-SP',''),(491,35,'State','Tocantins','BR-TO',''),(492,35,'Federal District','Distrito Federal','BR-DF',''),(493,20,'District','Acklins and Crooked Islands','BS-AC',''),(494,20,'District','Bimini','BS-BI',''),(495,20,'District','Cat Island','BS-CI',''),(496,20,'District','Exuma','BS-EX',''),(497,20,'District','Freeport','BS-FP',''),(498,20,'District','Fresh Creek','BS-FC',''),(499,20,'District','Governor\'s Harbour','BS-GH',''),(500,20,'District','Green Turtle Cay','BS-GT',''),(501,20,'District','Harbour Island','BS-HI',''),(502,20,'District','High Rock','BS-HR',''),(503,20,'District','Inagua','BS-IN',''),(504,20,'District','Kemps Bay','BS-KB',''),(505,20,'District','Long Island','BS-LI',''),(506,20,'District','Marsh Harbour','BS-MH',''),(507,20,'District','Mayaguana','BS-MG',''),(508,20,'District','New Providence','BS-NP',''),(509,20,'District','Nicholls Town and Berry Islands','BS-NB',''),(510,20,'District','Ragged Island','BS-RI',''),(511,20,'District','Rock Sound','BS-RS',''),(512,20,'District','Sandy Point','BS-SP',''),(513,20,'District','San Salvador and Rum Cay','BS-SR',''),(514,29,'District','Bumthang','BT-33',''),(515,29,'District','Chhukha','BT-12',''),(516,29,'District','Dagana','BT-22',''),(517,29,'District','Gasa','BT-GA',''),(518,29,'District','Ha','BT-13',''),(519,29,'District','Lhuentse','BT-44',''),(520,29,'District','Monggar','BT-42',''),(521,29,'District','Paro','BT-11',''),(522,29,'District','Pemagatshel','BT-43',''),(523,29,'District','Punakha','BT-23',''),(524,29,'District','Samdrup Jongkha','BT-45',''),(525,29,'District','Samtee','BT-14',''),(526,29,'District','Sarpang','BT-31',''),(527,29,'District','Thimphu','BT-15',''),(528,29,'District','Trashigang','BT-41',''),(529,29,'District','Trashi Yangtse','BT-TY',''),(530,29,'District','Trongsa','BT-32',''),(531,29,'District','Tsirang','BT-21',''),(532,29,'District','Wangdue Phodrang','BT-24',''),(533,29,'District','Zhemgang','BT-34',''),(534,33,'District','Central','BW-CE',''),(535,33,'District','Ghanzi','BW-GH',''),(536,33,'District','Kgalagadi','BW-KG',''),(537,33,'District','Kgatleng','BW-KL',''),(538,33,'District','Kweneng','BW-KW',''),(539,33,'District','Ngamiland','BW-NG',''),(540,33,'District','North-East','BW-NE',''),(541,33,'District','North-West (Botswana)','BW-NW',''),(542,33,'District','South-East','BW-SE',''),(543,33,'District','Southern (Botswana)','BW-SO',''),(544,24,'Oblast','Brèsckaja voblasc\'','BY-BR',''),(545,24,'Oblast','Homel\'skaja voblasc\'','BY-HO',''),(546,24,'Oblast','Hrodzenskaja voblasc\'','BY-HR',''),(547,24,'Oblast','Mahilëuskaja voblasc\'','BY-MA',''),(548,24,'Oblast','Minskaja voblasc\'','BY-MI',''),(549,24,'Oblast','Vicebskaja voblasc\'','BY-VI',''),(550,26,'District','Belize','BZ-BZ',''),(551,26,'District','Cayo','BZ-CY',''),(552,26,'District','Corozal','BZ-CZL',''),(553,26,'District','Orange Walk','BZ-OW',''),(554,26,'District','Stann Creek','BZ-SC',''),(555,26,'District','Toledo','BZ-TOL',''),(556,3,'Province','Alberta','CA-AB',''),(557,3,'Province','British Columbia','CA-BC',''),(558,3,'Province','Manitoba','CA-MB',''),(559,3,'Province','New Brunswick','CA-NB',''),(560,3,'Province','Newfoundland and Labrador','CA-NL',''),(561,3,'Province','Nova Scotia','CA-NS',''),(562,3,'Province','Ontario','CA-ON',''),(563,3,'Province','Prince Edward Island','CA-PE',''),(564,3,'Province','Quebec','CA-QC',''),(565,3,'Province','Saskatchewan','CA-SK',''),(566,3,'Territory','Northwest Territories','CA-NT',''),(567,3,'Territory','Nunavut','CA-NU',''),(568,3,'Territory','Yukon Territory','CA-YT',''),(569,55,'City','Kinshasa','CD-KN',''),(570,55,'Province','Bandundu','CD-BN',''),(571,55,'Province','Bas-Congo','CD-BC',''),(572,55,'Province','Équateur','CD-EQ',''),(573,55,'Province','Haut-Congo','CD-HC',''),(574,55,'Province','Kasai-Occidental','CD-KW',''),(575,55,'Province','Kasai-Oriental','CD-KE',''),(576,55,'Province','Katanga','CD-KA',''),(577,55,'Province','Maniema','CD-MA',''),(578,55,'Province','Nord-Kivu','CD-NK',''),(579,55,'Province','Orientale','CD-OR',''),(580,55,'Province','Sud-Kivu','CD-SK',''),(581,46,'Prefecture','Bamingui-Bangoran','CF-BB',''),(582,46,'Prefecture','Basse-Kotto','CF-BK',''),(583,46,'Prefecture','Haute-Kotto','CF-HK',''),(584,46,'Prefecture','Haut-Mbomou','CF-HM',''),(585,46,'Prefecture','Kémo','CF-KG',''),(586,46,'Prefecture','Lobaye','CF-LB',''),(587,46,'Prefecture','Mambéré-Kadéï','CF-HS',''),(588,46,'Prefecture','Mbomou','CF-MB',''),(589,46,'Prefecture','Nana-Mambéré','CF-NM',''),(590,46,'Prefecture','Ombella-M\'poko','CF-MP',''),(591,46,'Prefecture','Ouaka','CF-UK',''),(592,46,'Prefecture','Ouham','CF-AC',''),(593,46,'Prefecture','Ouham-Pendé','CF-OP',''),(594,46,'Prefecture','Vakaga','CF-VR',''),(595,46,'Economic Prefecture','Nana-Grébizi','CF-KB',''),(596,46,'Economic Prefecture','Sangha-Mbaéré','CF-SE',''),(597,46,'Autonomous Commune','Bangui','CF-BGF',''),(598,54,'Region','Bouenza','CG-11',''),(599,54,'Region','Cuvette','CG-8',''),(600,54,'Region','Cuvette-Ouest','CG-15',''),(601,54,'Region','Kouilou','CG-5',''),(602,54,'Region','Lékoumou','CG-2',''),(603,54,'Region','Likouala','CG-7',''),(604,54,'Region','Niari','CG-9',''),(605,54,'Region','Plateaux','CG-14',''),(606,54,'Region','Pool','CG-12',''),(607,54,'Region','Sangha','CG-13',''),(608,54,'Capital District','Brazzaville','CG-BZV',''),(609,218,'Canton','Aargau','CH-AG',''),(610,218,'Canton','Appenzell Innerrhoden','CH-AI',''),(611,218,'Canton','Appenzell Ausserrhoden','CH-AR',''),(612,218,'Canton','Bern','CH-BE',''),(613,218,'Canton','Basel-Landschaft','CH-BL',''),(614,218,'Canton','Basel-Stadt','CH-BS',''),(615,218,'Canton','Fribourg','CH-FR',''),(616,218,'Canton','Genève','CH-GE',''),(617,218,'Canton','Glarus','CH-GL',''),(618,218,'Canton','Graubünden','CH-GR',''),(619,218,'Canton','Jura','CH-JU',''),(620,218,'Canton','Luzern','CH-LU',''),(621,218,'Canton','Neuchâtel','CH-NE',''),(622,218,'Canton','Nidwalden','CH-NW',''),(623,218,'Canton','Obwalden','CH-OW',''),(624,218,'Canton','Sankt Gallen','CH-SG',''),(625,218,'Canton','Schaffhausen','CH-SH',''),(626,218,'Canton','Solothurn','CH-SO',''),(627,218,'Canton','Schwyz','CH-SZ',''),(628,218,'Canton','Thurgau','CH-TG',''),(629,218,'Canton','Ticino','CH-TI',''),(630,218,'Canton','Uri','CH-UR',''),(631,218,'Canton','Vaud','CH-VD',''),(632,218,'Canton','Valais','CH-VS',''),(633,218,'Canton','Zug','CH-ZG',''),(634,218,'Canton','Zürich','CH-ZH',''),(635,58,'Region','18 Montagnes (Région des)','CI-06',''),(636,58,'Region','Agnébi (Région de l\')','CI-16',''),(637,58,'Region','Bafing (Région du)','CI-17',''),(638,58,'Region','Bas-Sassandra (Région du)','CI-09',''),(639,58,'Region','Denguélé (Région du)','CI-10',''),(640,58,'Region','Fromager (Région du)','CI-18',''),(641,58,'Region','Haut-Sassandra (Région du)','CI-02',''),(642,58,'Region','Lacs (Région des)','CI-07',''),(643,58,'Region','Lagunes (Région des)','CI-01',''),(644,58,'Region','Marahoué (Région de la)','CI-12',''),(645,58,'Region','Moyen-Cavally (Région du)','CI-19',''),(646,58,'Region','Moyen-Comoé (Région du)','CI-05',''),(647,58,'Region','Nzi-Comoé (Région)','CI-11',''),(648,58,'Region','Savanes (Région des)','CI-03',''),(649,58,'Region','Sud-Bandama (Région du)','CI-15',''),(650,58,'Region','Sud-Comoé (Région du)','CI-13',''),(651,58,'Region','Vallée du Bandama (Région de la)','CI-04',''),(652,58,'Region','Worodouqou (Région du)','CI-14',''),(653,58,'Region','Zanzan (Région du)','CI-08',''),(654,48,'Region','Aisén del General Carlos Ibáñez del Campo','CL-AI',''),(655,48,'Region','Antofagasta','CL-AN',''),(656,48,'Region','Araucanía','CL-AR',''),(657,48,'Region','Atacama','CL-AT',''),(658,48,'Region','Bío-Bío','CL-BI',''),(659,48,'Region','Coquimbo','CL-CO',''),(660,48,'Region','Libertador General Bernardo O\'Higgins','CL-LI',''),(661,48,'Region','Los Lagos','CL-LL',''),(662,48,'Region','Magallanes y Antártica Chilena','CL-MA',''),(663,48,'Region','Maule','CL-ML',''),(664,48,'Region','Región Metropolitana de Santiago','CL-RM',''),(665,48,'Region','Tarapacá','CL-TA',''),(666,48,'Region','Valparaíso','CL-VS',''),(667,42,'Province','Adamaoua','CM-AD',''),(668,42,'Province','Centre','CM-CE',''),(669,42,'Province','East','CM-ES',''),(670,42,'Province','Far North','CM-EN',''),(671,42,'Province','Littoral','CM-LT',''),(672,42,'Province','North','CM-NO',''),(673,42,'Province','North-West (Cameroon)','CM-NW',''),(674,42,'Province','South','CM-SU',''),(675,42,'Province','South-West','CM-SW',''),(676,42,'Province','West','CM-OU',''),(677,49,'Municipality','Beijing','CN-11',''),(678,49,'Municipality','Chongqing','CN-50',''),(679,49,'Municipality','Shanghai','CN-31',''),(680,49,'Municipality','Tianjin','CN-12',''),(681,49,'Province','Anhui','CN-34',''),(682,49,'Province','Fujian','CN-35',''),(683,49,'Province','Gansu','CN-62',''),(684,49,'Province','Guangdong','CN-44',''),(685,49,'Province','Guizhou','CN-52',''),(686,49,'Province','Hainan','CN-46',''),(687,49,'Province','Hebei','CN-13',''),(688,49,'Province','Heilongjiang','CN-23',''),(689,49,'Province','Henan','CN-41',''),(690,49,'Province','Hubei','CN-42',''),(691,49,'Province','Hunan','CN-43',''),(692,49,'Province','Jiangsu','CN-32',''),(693,49,'Province','Jiangxi','CN-36',''),(694,49,'Province','Jilin','CN-22',''),(695,49,'Province','Liaoning','CN-21',''),(696,49,'Province','Qinghai','CN-63',''),(697,49,'Province','Shaanxi','CN-61',''),(698,49,'Province','Shandong','CN-37',''),(699,49,'Province','Shanxi','CN-14',''),(700,49,'Province','Sichuan','CN-51',''),(701,49,'Province','Taiwan','CN-71',''),(702,49,'Province','Yunnan','CN-53',''),(703,49,'Province','Zhejiang','CN-33',''),(704,49,'Autonomous region','Guangxi','CN-45',''),(705,49,'Autonomous region','Nei Mongol','CN-15',''),(706,49,'Autonomous region','Ningxia','CN-64',''),(707,49,'Autonomous region','Xinjiang','CN-65',''),(708,49,'Autonomous region','Xizang','CN-54',''),(709,49,'Special administrative region','Xianggang (Hong-Kong)','CN-91',''),(710,49,'Special administrative region','Aomen (Macau)','CN-92',''),(711,52,'Capital district','Distrito Capital de Bogotá','CO-DC',''),(712,52,'Department','Amazonas','CO-AMA',''),(713,52,'Department','Antioquia','CO-ANT',''),(714,52,'Department','Arauca','CO-ARA',''),(715,52,'Department','Atlántico','CO-ATL',''),(716,52,'Department','Bolívar','CO-BOL',''),(717,52,'Department','Boyacá','CO-BOY',''),(718,52,'Department','Caldas','CO-CAL',''),(719,52,'Department','Caquetá','CO-CAQ',''),(720,52,'Department','Casanare','CO-CAS',''),(721,52,'Department','Cauca','CO-CAU',''),(722,52,'Department','Cesar','CO-CES',''),(723,52,'Department','Chocó','CO-CHO',''),(724,52,'Department','Córdoba','CO-COR',''),(725,52,'Department','Cundinamarca','CO-CUN',''),(726,52,'Department','Guainía','CO-GUA',''),(727,52,'Department','Guaviare','CO-GUV',''),(728,52,'Department','Huila','CO-HUI',''),(729,52,'Department','La Guajira','CO-LAG',''),(730,52,'Department','Magdalena','CO-MAG',''),(731,52,'Department','Meta','CO-MET',''),(732,52,'Department','Nariño','CO-NAR',''),(733,52,'Department','Norte de Santander','CO-NSA',''),(734,52,'Department','Putumayo','CO-PUT',''),(735,52,'Department','Quindío','CO-QUI',''),(736,52,'Department','Risaralda','CO-RIS',''),(737,52,'Department','San Andrés, Providencia y Santa Catalina','CO-SAP',''),(738,52,'Department','Santander','CO-SAN',''),(739,52,'Department','Sucre','CO-SUC',''),(740,52,'Department','Tolima','CO-TOL',''),(741,52,'Department','Valle del Cauca','CO-VAC',''),(742,52,'Department','Vaupés','CO-VAU',''),(743,52,'Department','Vichada','CO-VID',''),(744,57,'Province','Alajuela','CR-A',''),(745,57,'Province','Cartago','CR-C',''),(746,57,'Province','Guanacaste','CR-G',''),(747,57,'Province','Heredia','CR-H',''),(748,57,'Province','Limón','CR-L',''),(749,57,'Province','Puntarenas','CR-P',''),(750,57,'Province','San José','CR-SJ',''),(751,60,'Province','Camagüey','CU-09',''),(752,60,'Province','Ciego de Ávila','CU-08',''),(753,60,'Province','Cienfuegos','CU-06',''),(754,60,'Province','Ciudad de La Habana','CU-03',''),(755,60,'Province','Granma','CU-12',''),(756,60,'Province','Guantánamo','CU-14',''),(757,60,'Province','Holguín','CU-11',''),(758,60,'Province','La Habana','CU-02',''),(759,60,'Province','Las Tunas','CU-10',''),(760,60,'Province','Matanzas','CU-04',''),(761,60,'Province','Pinar del Rio','CU-01',''),(762,60,'Province','Sancti Spíritus','CU-07',''),(763,60,'Province','Santiago de Cuba','CU-13',''),(764,60,'Province','Villa Clara','CU-05',''),(765,60,'Special municipality','Isla de la Juventud','CU-99',''),(766,44,'District','Ilhas de Barlavento','CV B',''),(767,44,'District','Ilhas de Sotavento','CV S',''),(768,44,'Municipality','Boa Vista','CV-BV',''),(769,44,'Municipality','Brava','CV-BR',''),(770,44,'Municipality','Calheta de São Miguel','CV-CS',''),(771,44,'Municipality','Maio','CV-MA',''),(772,44,'Municipality','Mosteiros','CV-MO',''),(773,44,'Municipality','Paúl','CV-PA',''),(774,44,'Municipality','Porto Novo','CV-PN',''),(775,44,'Municipality','Praia','CV-PR',''),(776,44,'Municipality','Ribeira Grande','CV-RG',''),(777,44,'Municipality','Sal','CV-SL',''),(778,44,'Municipality','Santa Catarina','CV-CA',''),(779,44,'Municipality','Santa Cruz','CV-CR',''),(780,44,'Municipality','São Domingos','CV-SD',''),(781,44,'Municipality','São Filipe','CV-SF',''),(782,44,'Municipality','São Nicolau','CV-SN',''),(783,44,'Municipality','São Vicente','CV-SV',''),(784,44,'Municipality','Tarrafal','CV-TA',''),(785,62,'District','Ammóchostos','CY-04',''),(786,62,'District','Kerýneia','CY-06',''),(787,62,'District','Lárnaka','CY-03',''),(788,62,'District','Lefkosía','CY-01',''),(789,62,'District','Lemesós','CY-02',''),(790,62,'District','Páfos','CY-05',''),(791,63,'Region','Jiho?eský kraj','CZ JC',''),(792,63,'Region','Jihomoravský kraj','CZ JM',''),(793,63,'Region','Karlovarský kraj','CZ KA',''),(794,63,'Region','Královéhradecký kraj','CZ KR',''),(795,63,'Region','Liberecký kraj','CZ LI',''),(796,63,'Region','Moravskoslezský kraj','CZ MO',''),(797,63,'Region','Olomoucký kraj','CZ OL',''),(798,63,'Region','Pardubický kraj','CZ PA',''),(799,63,'Region','Plze?ský kraj','CZ PL',''),(800,63,'Region','Praha, hlavní m?sto','CZ PR',''),(801,63,'Region','St?edo?eský kraj','CZ ST',''),(802,63,'Region','Ústecký kraj','CZ US',''),(803,63,'Region','Vyso?ina','CZ VY',''),(804,63,'Region','Zlínský kraj','CZ ZL',''),(805,63,'district','Benešov','CZ-201',''),(806,63,'district','Beroun','CZ-202',''),(807,63,'district','Kladno','CZ-203',''),(808,63,'district','Kolín','CZ-204',''),(809,63,'district','Kutná Hora','CZ-205',''),(810,63,'district','M?lník','CZ-206',''),(811,63,'district','Mladá Boleslav','CZ-207',''),(812,63,'district','Nymburk','CZ-208',''),(813,63,'district','Praha-východ','CZ-209',''),(814,63,'district','Praha-západ','CZ-20A',''),(815,63,'district','P?íbram','CZ-20B',''),(816,63,'district','Rakovník','CZ-20C',''),(817,63,'district','?eské Bud?jovice','CZ-311',''),(818,63,'district','?eský Krumlov','CZ-312',''),(819,63,'district','Jind?ich?v Hradec','CZ-313',''),(820,63,'district','Písek','CZ-314',''),(821,63,'district','Prachatice','CZ-315',''),(822,63,'district','Strakonice','CZ-316',''),(823,63,'district','Tábor','CZ-317',''),(824,63,'district','Domažlice','CZ-321',''),(825,63,'district','Klatovy','CZ-322',''),(826,63,'district','Plze?-m?sto','CZ-323',''),(827,63,'district','Plze?-jih','CZ-324',''),(828,63,'district','Plze?-sever','CZ-325',''),(829,63,'district','Rokycany','CZ-326',''),(830,63,'district','Tachov','CZ-327',''),(831,63,'district','Cheb','CZ-411',''),(832,63,'district','Karlovy Vary','CZ-412',''),(833,63,'district','Sokolov','CZ-413',''),(834,63,'district','D??ín','CZ-421',''),(835,63,'district','Chomutov','CZ-422',''),(836,63,'district','Litom??ice','CZ-423',''),(837,63,'district','Louny','CZ-424',''),(838,63,'district','Most','CZ-425',''),(839,63,'district','Teplice','CZ-426',''),(840,63,'district','Ústí nad Labem','CZ-427',''),(841,63,'district','?eská Lípa','CZ-511',''),(842,63,'district','Jablonec nad Nisou','CZ-512',''),(843,63,'district','Liberec','CZ-513',''),(844,63,'district','Semily','CZ-514',''),(845,63,'district','Hradec Králové','CZ-521',''),(846,63,'district','Ji?ín','CZ-522',''),(847,63,'district','Náchod','CZ-523',''),(848,63,'district','Rychnov nad Kn?žnou','CZ-524',''),(849,63,'district','Trutnov','CZ-525',''),(850,63,'district','Chrudim','CZ-531',''),(851,63,'district','Pardubice','CZ-532',''),(852,63,'district','Svitavy','CZ-533',''),(853,63,'district','Ústí nad Orlicí','CZ-534',''),(854,63,'district','Blansko','CZ-621',''),(855,63,'district','Brno-m?sto','CZ-622',''),(856,63,'district','Brno-venkov','CZ-623',''),(857,63,'district','B?eclav','CZ-624',''),(858,63,'district','Hodonín','CZ-625',''),(859,63,'district','Vyškov','CZ-626',''),(860,63,'district','Znojmo','CZ-627',''),(861,63,'district','Jeseník','CZ-711',''),(862,63,'district','Olomouc','CZ-712',''),(863,63,'district','Prost?jov','CZ-713',''),(864,63,'district','P?erov','CZ-714',''),(865,63,'district','Šumperk','CZ-715',''),(866,63,'district','Krom??íž','CZ-721',''),(867,63,'district','Uherské Hradišt?','CZ-722',''),(868,63,'district','Vsetín','CZ-723',''),(869,63,'district','Zlín','CZ-724',''),(870,63,'district','Bruntál','CZ-801',''),(871,63,'district','Frýdek - Místek','CZ-802',''),(872,63,'district','Karviná','CZ-803',''),(873,63,'district','Nový Ji?ín','CZ-804',''),(874,63,'district','Opava','CZ-805',''),(875,63,'district','Ostrava - m?sto','CZ-806',''),(876,63,'district','Praha 1','CZ-101',''),(877,63,'district','Praha 2','CZ-102',''),(878,63,'district','Praha 3','CZ-103',''),(879,63,'district','Praha 4','CZ-104',''),(880,63,'district','Praha 5','CZ-105',''),(881,63,'district','Praha 6','CZ-106',''),(882,63,'district','Praha 7','CZ-107',''),(883,63,'district','Praha 8','CZ-108',''),(884,63,'district','Praha 9','CZ-109',''),(885,63,'district','Praha 10','CZ-10A',''),(886,63,'district','Praha 11','CZ-10B',''),(887,63,'district','Praha 12','CZ-10C',''),(888,63,'district','Praha 13','CZ-10D',''),(889,63,'district','Praha 14','CZ-10E',''),(890,63,'district','Praha 15','CZ-10F',''),(891,63,'district','Havlí?k?v Brod','CZ-611',''),(892,63,'district','Jihlava','CZ-612',''),(893,63,'district','Pelh?imov','CZ-613',''),(894,63,'district','T?ebí?','CZ-614',''),(895,63,'district','Žd’ár nad Sázavou','CZ-615',''),(896,86,'State','Baden-Württemberg','DE-BW',''),(897,86,'State','Bayern','DE-BY',''),(898,86,'State','Bremen','DE-HB',''),(899,86,'State','Hamburg','DE-HH',''),(900,86,'State','Hessen','DE-HE',''),(901,86,'State','Niedersachsen','DE-NI',''),(902,86,'State','Nordrhein-Westfalen','DE-NW',''),(903,86,'State','Rheinland-Pfalz','DE-RP',''),(904,86,'State','Saarland','DE-SL',''),(905,86,'State','Schleswig-Holstein','DE-SH',''),(906,86,'State','Berlin','DE-BE',''),(907,86,'State','Brandenburg','DE-BB',''),(908,86,'State','Mecklenburg-Vorpommern','DE-MV',''),(909,86,'State','Sachsen','DE-SN',''),(910,86,'State','Sachsen-Anhalt','DE-ST',''),(911,86,'State','Thüringen','DE-TH',''),(912,65,'Region','Ali Sabieh','DJ-AS',''),(913,65,'Region','Arta','DJ-AR',''),(914,65,'Region','Dikhil','DJ-DI',''),(915,65,'Region','Obock','DJ-OB',''),(916,65,'Region','Tadjourah','DJ-TA',''),(917,65,'City','Djibouti','DJ-DJ',''),(918,64,'County','Copenhagen','DK-015',''),(919,64,'County','Frederiksborg','DK-020',''),(920,64,'County','Roskilde','DK-025',''),(921,64,'County','Western Zealand','DK-030',''),(922,64,'County','Storstrøm','DK-035',''),(923,64,'County','Bornholm','DK-040',''),(924,64,'County','Funen','DK-042',''),(925,64,'County','Southern Jutland','DK-050',''),(926,64,'County','Ribe','DK-055',''),(927,64,'County','Vejle','DK-060',''),(928,64,'County','Ringkøbing','DK-065',''),(929,64,'County','Aarhus','DK-070',''),(930,64,'County','Viborg','DK-076',''),(931,64,'County','Northern Jutland','DK-080',''),(932,64,'Municipality','Frederikaberg municipality','DK-147',''),(933,64,'Municipality','Copenhagen municipality','DK-101',''),(934,66,'Parish','Saint Andrew','DM-02',''),(935,66,'Parish','Saint David','DM-03',''),(936,66,'Parish','Saint George','DM-04',''),(937,66,'Parish','Saint John','DM-05',''),(938,66,'Parish','Saint Joseph','DM-06',''),(939,66,'Parish','Saint Luke','DM-07',''),(940,66,'Parish','Saint Mark','DM-08',''),(941,66,'Parish','Saint Patrick','DM-09',''),(942,66,'Parish','Saint Paul','DM-10',''),(943,66,'Parish','Saint Peter','DM-01',''),(944,67,'District','Distrito Nacional (Santo Domingo)','DO-01',''),(945,67,'Province','Azua','DO-02',''),(946,67,'Province','Bahoruco','DO-03',''),(947,67,'Province','Barahona','DO-04',''),(948,67,'Province','Dajabón','DO-05',''),(949,67,'Province','Duarte','DO-06',''),(950,67,'Province','El Seybo [El Seibo]','DO-08',''),(951,67,'Province','Espaillat','DO-09',''),(952,67,'Province','Hato Mayor','DO-30',''),(953,67,'Province','Independencia','DO-10',''),(954,67,'Province','La Altagracia','DO-11',''),(955,67,'Province','La Estrelleta [Elías Piña]','DO-07',''),(956,67,'Province','La Romana','DO-12',''),(957,67,'Province','La Vega','DO-13',''),(958,67,'Province','María Trinidad Sánchez','DO-14',''),(959,67,'Province','Monseñor Nouel','DO-28',''),(960,67,'Province','Monte Cristi','DO-15',''),(961,67,'Province','Monte Plata','DO-29',''),(962,67,'Province','Pedernales','DO-16',''),(963,67,'Province','Peravia','DO-17',''),(964,67,'Province','Puerto Plata','DO-18',''),(965,67,'Province','Salcedo','DO-19',''),(966,67,'Province','Samaná','DO-20',''),(967,67,'Province','San Cristóbal','DO-21',''),(968,67,'Province','San Juan','DO-22',''),(969,67,'Province','San Pedro de Macorís','DO-23',''),(970,67,'Province','Sánchez Ramírez','DO-24',''),(971,67,'Province','Santiago','DO-25',''),(972,67,'Province','Santiago Rodríguez','DO-26',''),(973,67,'Province','Valverde','DO-27',''),(974,7,'Province','Adrar','DZ-01',''),(975,7,'Province','Aïn Defla','DZ-44',''),(976,7,'Province','Aïn Témouchent','DZ-46',''),(977,7,'Province','Alger','DZ-16',''),(978,7,'Province','Annaba','DZ-23',''),(979,7,'Province','Batna','DZ-05',''),(980,7,'Province','Béchar','DZ-08',''),(981,7,'Province','Béjaïa','DZ-06',''),(982,7,'Province','Biskra','DZ-07',''),(983,7,'Province','Blida','DZ-09',''),(984,7,'Province','Bordj Bou Arréridj','DZ-34',''),(985,7,'Province','Bouira','DZ-10',''),(986,7,'Province','Boumerdès','DZ-35',''),(987,7,'Province','Chlef','DZ-02',''),(988,7,'Province','Constantine','DZ-25',''),(989,7,'Province','Djelfa','DZ-17',''),(990,7,'Province','El Bayadh','DZ-32',''),(991,7,'Province','El Oued','DZ-39',''),(992,7,'Province','El Tarf','DZ-36',''),(993,7,'Province','Ghardaïa','DZ-47',''),(994,7,'Province','Guelma','DZ-24',''),(995,7,'Province','Illizi','DZ-33',''),(996,7,'Province','Jijel','DZ-18',''),(997,7,'Province','Khenchela','DZ-40',''),(998,7,'Province','Laghouat','DZ-03',''),(999,7,'Province','Mascara','DZ-29',''),(1000,7,'Province','Médéa','DZ-26',''),(1001,7,'Province','Mila','DZ-43',''),(1002,7,'Province','Mostaganem','DZ-27',''),(1003,7,'Province','Msila','DZ-28',''),(1004,7,'Province','Naama','DZ-45',''),(1005,7,'Province','Oran','DZ-31',''),(1006,7,'Province','Ouargla','DZ-30',''),(1007,7,'Province','Oum el Bouaghi','DZ-04',''),(1008,7,'Province','Relizane','DZ-48',''),(1009,7,'Province','Saïda','DZ-20',''),(1010,7,'Province','Sétif','DZ-19',''),(1011,7,'Province','Sidi Bel Abbès','DZ-22',''),(1012,7,'Province','Skikda','DZ-21',''),(1013,7,'Province','Souk Ahras','DZ-41',''),(1014,7,'Province','Tamanghasset','DZ-11',''),(1015,7,'Province','Tébessa','DZ-12',''),(1016,7,'Province','Tiaret','DZ-14',''),(1017,7,'Province','Tindouf','DZ-37',''),(1018,7,'Province','Tipaza','DZ-42',''),(1019,7,'Province','Tissemsilt','DZ-38',''),(1020,7,'Province','Tizi Ouzou','DZ-15',''),(1021,7,'Province','Tlemcen','DZ-13',''),(1022,68,'Province','Azuay','EC-A',''),(1023,68,'Province','Bolívar','EC-B',''),(1024,68,'Province','Cañar','EC-F',''),(1025,68,'Province','Carchi','EC-C',''),(1026,68,'Province','Cotopaxi','EC-X',''),(1027,68,'Province','Chimborazo','EC-H',''),(1028,68,'Province','El Oro','EC-O',''),(1029,68,'Province','Esmeraldas','EC-E',''),(1030,68,'Province','Galápagos','EC-W',''),(1031,68,'Province','Guayas','EC-G',''),(1032,68,'Province','Imbabura','EC-I',''),(1033,68,'Province','Loja','EC-L',''),(1034,68,'Province','Los Ríos','EC-R',''),(1035,68,'Province','Manabí','EC-M',''),(1036,68,'Province','Morona-Santiago','EC-S',''),(1037,68,'Province','Napo','EC-N',''),(1038,68,'Province','Orellana','EC-D',''),(1039,68,'Province','Pastaza','EC-Y',''),(1040,68,'Province','Pichincha','EC-P',''),(1041,68,'Province','Sucumbíos','EC-U',''),(1042,68,'Province','Tungurahua','EC-T',''),(1043,68,'Province','Zamora-Chinchipe','EC-Z',''),(1044,73,'County','Harjumaa','EE-37',''),(1045,73,'County','Hiiumaa','EE-39',''),(1046,73,'County','Ida-Virumaa','EE-44',''),(1047,73,'County','Jõgevamaa','EE-49',''),(1048,73,'County','Järvamaa','EE-51',''),(1049,73,'County','Läänemaa','EE-57',''),(1050,73,'County','Lääne-Virumaa','EE-59',''),(1051,73,'County','Põlvamaa','EE-65',''),(1052,73,'County','Pärnumaa','EE-67',''),(1053,73,'County','Raplamaa','EE-70',''),(1054,73,'County','Saaremaa','EE-74',''),(1055,73,'County','Tartumaa','EE-78',''),(1056,73,'County','Valgamaa','EE-82',''),(1057,73,'County','Viljandimaa','EE-84',''),(1058,73,'County','Võrumaa','EE-86',''),(1059,69,'Governorate','Ad Daqahl?yah','EG-DK',''),(1060,69,'Governorate','Al Bahr al Ahmar','EG-BA',''),(1061,69,'Governorate','Al Buhayrah','EG-BH',''),(1062,69,'Governorate','Al Fayy?m','EG-FYM',''),(1063,69,'Governorate','Al Gharb?yah','EG-GH',''),(1064,69,'Governorate','Al Iskandar?yah','EG-ALX',''),(1065,69,'Governorate','Al Ism?`?l?yah','EG-IS',''),(1066,69,'Governorate','Al J?zah','EG-GZ',''),(1067,69,'Governorate','Al Min?f?yah','EG-MNF',''),(1068,69,'Governorate','Al Miny?','EG-MN',''),(1069,69,'Governorate','Al Q?hirah','EG-C',''),(1070,69,'Governorate','Al Qaly?b?yah','EG-KB',''),(1071,69,'Governorate','Al W?d? al Jad?d','EG-WAD',''),(1072,69,'Governorate','Ash Sharq?yah','EG-SHR',''),(1073,69,'Governorate','As Suways','EG-SUZ',''),(1074,69,'Governorate','Asw?n','EG-ASN',''),(1075,69,'Governorate','Asy?t','EG-AST',''),(1076,69,'Governorate','Ban? Suwayf','EG-BNS',''),(1077,69,'Governorate','B?r Sa`?d','EG-PTS',''),(1078,69,'Governorate','Dumy?t','EG-DT',''),(1079,69,'Governorate','Jan?b S?n?\'','EG-JS',''),(1080,69,'Governorate','Kafr ash Shaykh','EG-KFS',''),(1081,69,'Governorate','Matr?h','EG-MT',''),(1082,69,'Governorate','Qin?','EG-KN',''),(1083,69,'Governorate','Shamal S?n?\'','EG-SIN',''),(1084,69,'Governorate','S?h?j','EG-SHG',''),(1085,72,'Province','Anseba','ER-AN',''),(1086,72,'Province','Debub','ER-DU',''),(1087,72,'Province','Debubawi Keyih Bahri [Debub-Keih-Bahri]','ER-DK',''),(1088,72,'Province','Gash-Barka','ER-GB',''),(1089,72,'Province','Maakel [Maekel]','ER-MA',''),(1090,72,'Province','Semenawi Keyih Bahri [Semien-Keih-Bahri]','ER-SK',''),(1091,211,'Autonomous communities','Andalucía','ES AN',''),(1092,211,'Autonomous communities','Aragón','ES AR',''),(1093,211,'Autonomous communities','Asturias, Principado de','ES O',''),(1094,211,'Autonomous communities','Canarias','ES CN',''),(1095,211,'Autonomous communities','Cantabria','ES S',''),(1096,211,'Autonomous communities','Castilla-La Mancha','ES CM',''),(1097,211,'Autonomous communities','Castilla y León','ES CL',''),(1098,211,'Autonomous communities','Cataluña','ES CT',''),(1099,211,'Autonomous communities','Extremadura','ES EX',''),(1100,211,'Autonomous communities','Galicia','ES GA',''),(1101,211,'Autonomous communities','Illes Balears','ES IB',''),(1102,211,'Autonomous communities','La Rioja','ES LO',''),(1103,211,'Autonomous communities','Madrid, Comunidad de','ES M',''),(1104,211,'Autonomous communities','Murcia, Región de','ES MU',''),(1105,211,'Autonomous communities','Navarra, Comunidad Foral de','ES NA',''),(1106,211,'Autonomous communities','País Vasco','ES PV',''),(1107,211,'Autonomous communities','Valenciana, Comunidad','ES VC',''),(1108,211,'Province','Álava','ES-VI',''),(1109,211,'Province','Albacete','ES-AB',''),(1110,211,'Province','Alicante','ES-A',''),(1111,211,'Province','Almería','ES-AL',''),(1112,211,'Province','Asturias','ES-O',''),(1113,211,'Province','Ávila','ES-AV',''),(1114,211,'Province','Badajoz','ES-BA',''),(1115,211,'Province','Baleares','ES-IB',''),(1116,211,'Province','Barcelona','ES-B',''),(1117,211,'Province','Burgos','ES-BU',''),(1118,211,'Province','Cáceres','ES-CC',''),(1119,211,'Province','Cádiz','ES-CA',''),(1120,211,'Province','Cantabria','ES-S',''),(1121,211,'Province','Castellón','ES-CS',''),(1122,211,'Province','Ciudad Real','ES-CR',''),(1123,211,'Province','Córdoba','ES-CO',''),(1124,211,'Province','Cuenca','ES-CU',''),(1125,211,'Province','Girona','ES-GI',''),(1126,211,'Province','Granada','ES-GR',''),(1127,211,'Province','Guadalajara','ES-GU',''),(1128,211,'Province','Guipúzcoa','ES-SS',''),(1129,211,'Province','Huelva','ES-H',''),(1130,211,'Province','Huesca','ES-HU',''),(1131,211,'Province','Jaén','ES-J',''),(1132,211,'Province','A Coruña','ES-C',''),(1133,211,'Province','La Rioja','ES-LO',''),(1134,211,'Province','Las Palmas','ES-GC',''),(1135,211,'Province','León','ES-LE',''),(1136,211,'Province','Lleida','ES-L',''),(1137,211,'Province','Lugo','ES-LU',''),(1138,211,'Province','Madrid','ES-M',''),(1139,211,'Province','Málaga','ES-MA',''),(1140,211,'Province','Murcia','ES-MU',''),(1141,211,'Province','Navarra','ES-NA',''),(1142,211,'Province','Ourense','ES-OR',''),(1143,211,'Province','Palencia','ES-P',''),(1144,211,'Province','Pontevedra','ES-PO',''),(1145,211,'Province','Salamanca','ES-SA',''),(1146,211,'Province','Santa Cruz de Tenerife','ES-TF',''),(1147,211,'Province','Segovia','ES-SG',''),(1148,211,'Province','Sevilla','ES-SE',''),(1149,211,'Province','Soria','ES-SO',''),(1150,211,'Province','Tarragona','ES-T',''),(1151,211,'Province','Teruel','ES-TE',''),(1152,211,'Province','Toledo','ES-TO',''),(1153,211,'Province','Valencia','ES-V',''),(1154,211,'Province','Valladolid','ES-VA',''),(1155,211,'Province','Vizcaya','ES-BI',''),(1156,211,'Province','Zamora','ES-ZA',''),(1157,211,'Province','Zaragoza','ES-Z',''),(1158,211,'Autonomous city','Ceuta','ES-CE',''),(1159,211,'Autonomous city','Melilla','ES-ML',''),(1160,74,'Administration','?d?s ?beba','ET-AA',''),(1161,74,'Administration','Dir? Dawa','ET-DD',''),(1162,74,'State','?far','ET-AF',''),(1163,74,'State','?mara','ET-AM',''),(1164,74,'State','B?nshangul Gumuz','ET-BE',''),(1165,74,'State','Gamb?la Hizboch','ET-GA',''),(1166,74,'State','H?rer? Hizb','ET-HA',''),(1167,74,'State','Orom?ya','ET-OR',''),(1168,74,'State','Sumal?','ET-SO',''),(1169,74,'State','Tigray','ET-TI',''),(1170,74,'State','YeDebub Bih?roch Bih?reseboch na Hizboch','ET-SN',''),(1171,78,'Province','Ahvenanmaan lääni','FI-AL',''),(1172,78,'Province','Etelä-Suomen lääni','FI-ES',''),(1173,78,'Province','Itä-Suomen lääni','FI-IS',''),(1174,78,'Province','Lapin lääni','FI-LL',''),(1175,78,'Province','Länsi-Suomen lääni','FI-LS',''),(1176,78,'Province','Oulun lääni','FI-OL',''),(1177,77,'Division','Central','FJ-C',''),(1178,77,'Division','Eastern','FJ-E',''),(1179,77,'Division','Northern','FJ-N',''),(1180,77,'Division','Western','FJ-W',''),(1181,77,'Dependency','Rotuma','FJ-R',''),(1182,148,'State','Chuuk','FM-TRK',''),(1183,148,'State','Kosrae','FM-KSA',''),(1184,148,'State','Pohnpei','FM-PNI',''),(1185,148,'State','Yap','FM-YAP',''),(1186,79,'Metropolitan region','Alsace','FR A',''),(1187,79,'Metropolitan region','Aquitaine','FR B',''),(1188,79,'Metropolitan region','Auvergne','FR C',''),(1189,79,'Metropolitan region','Basse-Normandie','FR P',''),(1190,79,'Metropolitan region','Bourgogne','FR D',''),(1191,79,'Metropolitan region','Bretagne','FR E',''),(1192,79,'Metropolitan region','Centre','FR F',''),(1193,79,'Metropolitan region','Champagne-Ardenne','FR G',''),(1194,79,'Metropolitan region','Corse','FR H',''),(1195,79,'Metropolitan region','Franche-Comté','FR I',''),(1196,79,'Metropolitan region','Haute-Normandie','FR Q',''),(1197,79,'Metropolitan region','Île-de-France','FR J',''),(1198,79,'Metropolitan region','Languedoc-Roussillon','FR K',''),(1199,79,'Metropolitan region','Limousin','FR L',''),(1200,79,'Metropolitan region','Lorraine','FR M',''),(1201,79,'Metropolitan region','Midi-Pyrénées','FR N',''),(1202,79,'Metropolitan region','Nord - Pas-de-Calais','FR O',''),(1203,79,'Metropolitan region','Pays de la Loire','FR R',''),(1204,79,'Metropolitan region','Picardie','FR S',''),(1205,79,'Metropolitan region','Poitou-Charentes','FR T',''),(1206,79,'Metropolitan region','Provence-Alpes-Côte d\'Azur','FR U',''),(1207,79,'Metropolitan region','Rhône-Alpes','FR V',''),(1208,79,'Overseas region/department','Guadeloupe','FR GP',''),(1209,79,'Overseas region/department','Guyane','FR GF',''),(1210,79,'Overseas region/department','Martinique','FR MQ',''),(1211,79,'Overseas region/department','Réunion','FR RE',''),(1212,79,'Metropolitan department','Ain','FR-01',''),(1213,79,'Metropolitan department','Aisne','FR-02',''),(1214,79,'Metropolitan department','Allier','FR-03',''),(1215,79,'Metropolitan department','Alpes-de-Haute-Provence','FR-04',''),(1216,79,'Metropolitan department','Alpes-Maritimes','FR-06',''),(1217,79,'Metropolitan department','Ardèche','FR-07',''),(1218,79,'Metropolitan department','Ardennes','FR-08',''),(1219,79,'Metropolitan department','Ariège','FR-09',''),(1220,79,'Metropolitan department','Aube','FR-10',''),(1221,79,'Metropolitan department','Aude','FR-11',''),(1222,79,'Metropolitan department','Aveyron','FR-12',''),(1223,79,'Metropolitan department','Bas-Rhin','FR-67',''),(1224,79,'Metropolitan department','Bouches-du-Rhône','FR-13',''),(1225,79,'Metropolitan department','Calvados','FR-14',''),(1226,79,'Metropolitan department','Cantal','FR-15',''),(1227,79,'Metropolitan department','Charente','FR-16',''),(1228,79,'Metropolitan department','Charente-Maritime','FR-17',''),(1229,79,'Metropolitan department','Cher','FR-18',''),(1230,79,'Metropolitan department','Corrèze','FR-19',''),(1231,79,'Metropolitan department','Corse-du-Sud','FR-2A',''),(1232,79,'Metropolitan department','Côte-d\'Or','FR-21',''),(1233,79,'Metropolitan department','Côtes-d\'Armor','FR-22',''),(1234,79,'Metropolitan department','Creuse','FR-23',''),(1235,79,'Metropolitan department','Deux-Sèvres','FR-79',''),(1236,79,'Metropolitan department','Dordogne','FR-24',''),(1237,79,'Metropolitan department','Doubs','FR-25',''),(1238,79,'Metropolitan department','Drôme','FR-26',''),(1239,79,'Metropolitan department','Essonne','FR-91',''),(1240,79,'Metropolitan department','Eure','FR-27',''),(1241,79,'Metropolitan department','Eure-et-Loir','FR-28',''),(1242,79,'Metropolitan department','Finistère','FR-29',''),(1243,79,'Metropolitan department','Gard','FR-30',''),(1244,79,'Metropolitan department','Gers','FR-32',''),(1245,79,'Metropolitan department','Gironde','FR-33',''),(1246,79,'Metropolitan department','Haute-Corse','FR-2B',''),(1247,79,'Metropolitan department','Haute-Garonne','FR-31',''),(1248,79,'Metropolitan department','Haute-Loire','FR-43',''),(1249,79,'Metropolitan department','Haute-Marne','FR-52',''),(1250,79,'Metropolitan department','Hautes-Alpes','FR-05',''),(1251,79,'Metropolitan department','Haute-Saône','FR-70',''),(1252,79,'Metropolitan department','Haute-Savoie','FR-74',''),(1253,79,'Metropolitan department','Hautes-Pyrénées','FR-65',''),(1254,79,'Metropolitan department','Haute-Vienne','FR-87',''),(1255,79,'Metropolitan department','Haut-Rhin','FR-68',''),(1256,79,'Metropolitan department','Hauts-de-Seine','FR-92',''),(1257,79,'Metropolitan department','Hérault','FR-34',''),(1258,79,'Metropolitan department','Ille-et-Vilaine','FR-35',''),(1259,79,'Metropolitan department','Indre','FR-36',''),(1260,79,'Metropolitan department','Indre-et-Loire','FR-37',''),(1261,79,'Metropolitan department','Isère','FR-38',''),(1262,79,'Metropolitan department','Jura','FR-39',''),(1263,79,'Metropolitan department','Landes','FR-40',''),(1264,79,'Metropolitan department','Loir-et-Cher','FR-41',''),(1265,79,'Metropolitan department','Loire','FR-42',''),(1266,79,'Metropolitan department','Loire-Atlantique','FR-44',''),(1267,79,'Metropolitan department','Loiret','FR-45',''),(1268,79,'Metropolitan department','Lot','FR-46',''),(1269,79,'Metropolitan department','Lot-et-Garonne','FR-47',''),(1270,79,'Metropolitan department','Lozère','FR-48',''),(1271,79,'Metropolitan department','Maine-et-Loire','FR-49',''),(1272,79,'Metropolitan department','Manche','FR-50',''),(1273,79,'Metropolitan department','Marne','FR-51',''),(1274,79,'Metropolitan department','Mayenne','FR-53',''),(1275,79,'Metropolitan department','Meurthe-et-Moselle','FR-54',''),(1276,79,'Metropolitan department','Meuse','FR-55',''),(1277,79,'Metropolitan department','Morbihan','FR-56',''),(1278,79,'Metropolitan department','Moselle','FR-57',''),(1279,79,'Metropolitan department','Nièvre','FR-58',''),(1280,79,'Metropolitan department','Nord','FR-59',''),(1281,79,'Metropolitan department','Oise','FR-60',''),(1282,79,'Metropolitan department','Orne','FR-61',''),(1283,79,'Metropolitan department','Paris','FR-75',''),(1284,79,'Metropolitan department','Pas-de-Calais','FR-62',''),(1285,79,'Metropolitan department','Puy-de-Dôme','FR-63',''),(1286,79,'Metropolitan department','Pyrénées-Atlantiques','FR-64',''),(1287,79,'Metropolitan department','Pyrénées-Orientales','FR-66',''),(1288,79,'Metropolitan department','Rhône','FR-69',''),(1289,79,'Metropolitan department','Saône-et-Loire','FR-71',''),(1290,79,'Metropolitan department','Sarthe','FR-72',''),(1291,79,'Metropolitan department','Savoie','FR-73',''),(1292,79,'Metropolitan department','Seine-et-Marne','FR-77',''),(1293,79,'Metropolitan department','Seine-Maritime','FR-76',''),(1294,79,'Metropolitan department','Seine-Saint-Denis','FR-93',''),(1295,79,'Metropolitan department','Somme','FR-80',''),(1296,79,'Metropolitan department','Tarn','FR-81',''),(1297,79,'Metropolitan department','Tarn-et-Garonne','FR-82',''),(1298,79,'Metropolitan department','Territoire de Belfort','FR-90',''),(1299,79,'Metropolitan department','Val-de-Marne','FR-94',''),(1300,79,'Metropolitan department','Val d\'Oise','FR-95',''),(1301,79,'Metropolitan department','Var','FR-83',''),(1302,79,'Metropolitan department','Vaucluse','FR-84',''),(1303,79,'Metropolitan department','Vendée','FR-85',''),(1304,79,'Metropolitan department','Vienne','FR-86',''),(1305,79,'Metropolitan department','Vosges','FR-88',''),(1306,79,'Metropolitan department','Yonne','FR-89',''),(1307,79,'Metropolitan department','Yvelines','FR-78',''),(1308,79,'Dependency','Clipperton','FR-CP',''),(1309,79,'Overseas territorial collectivity','Saint-Barthélemy','FR-BL',''),(1310,79,'Overseas territorial collectivity','Saint-Martin','FR-MF',''),(1311,79,'Overseas territorial collectivity','Nouvelle-Calédonie','FR-NC',''),(1312,79,'Overseas territorial collectivity','Polynésie française','FR-PF',''),(1313,79,'Overseas territorial collectivity','Saint-Pierre-et-Miquelon','FR-PM',''),(1314,79,'Overseas territorial collectivity','Terres australes françaises','FR-TF',''),(1315,79,'Overseas territorial collectivity','Wallis et Futuna','FR-WF',''),(1316,79,'Overseas territorial collectivity','Mayotte','FR-YT',''),(1317,2,'Country','England','GB ENG',''),(1318,2,'Country','Scotland','GB SCT',''),(1319,2,'Province','Northern Ireland','GB NIR',''),(1320,2,'Principality','Wales','GB WLS',''),(1321,2,'Included for completeness','England and Wales','GB EAW',''),(1322,2,'Included for completeness','Great Britain','GB GBN',''),(1323,2,'Included for completeness','United Kingdom','GB UKM',''),(1324,2,'Two-tier county','Bedfordshire','GB-BDF',''),(1325,2,'Two-tier county','Buckinghamshire','GB-BKM',''),(1326,2,'Two-tier county','Cambridgeshire','GB-CAM',''),(1327,2,'Two-tier county','Cheshire','GB-CHS',''),(1328,2,'Two-tier county','Cornwall','GB-CON',''),(1329,2,'Two-tier county','Cumbria','GB-CMA',''),(1330,2,'Two-tier county','Derbyshire','GB-DBY',''),(1331,2,'Two-tier county','Devon','GB-DEV',''),(1332,2,'Two-tier county','Dorset','GB-DOR',''),(1333,2,'Two-tier county','Durham','GB-DUR',''),(1334,2,'Two-tier county','East Sussex','GB-ESX',''),(1335,2,'Two-tier county','Essex','GB-ESS',''),(1336,2,'Two-tier county','Gloucestershire','GB-GLS',''),(1337,2,'Two-tier county','Hampshire','GB-HAM',''),(1338,2,'Two-tier county','Hertfordshire','GB-HRT',''),(1339,2,'Two-tier county','Kent','GB-KEN',''),(1340,2,'Two-tier county','Lancashire','GB-LAN',''),(1341,2,'Two-tier county','Leicestershire','GB-LEC',''),(1342,2,'Two-tier county','Lincolnshire','GB-LIN',''),(1343,2,'Two-tier county','Norfolk','GB-NFK',''),(1344,2,'Two-tier county','North Yorkshire','GB-NYK',''),(1345,2,'Two-tier county','Northamptonshire','GB-NTH',''),(1346,2,'Two-tier county','Northumbarland','GB-NBL',''),(1347,2,'Two-tier county','Nottinghamshire','GB-NTT',''),(1348,2,'Two-tier county','Oxfordshire','GB-OXF',''),(1349,2,'Two-tier county','Somerset','GB-SOM',''),(1350,2,'Two-tier county','Staffordshire','GB-STS',''),(1351,2,'Two-tier county','Suffolk','GB-SFK',''),(1352,2,'Two-tier county','Surrey','GB-SRY',''),(1353,2,'Two-tier county','West Sussex','GB-WSX',''),(1354,2,'Two-tier county','Wiltshire','GB-WIL',''),(1355,2,'Two-tier county','Worcestershire','GB-WOR',''),(1356,2,'London borough','Barking and Dagenham','GB-BDG',''),(1357,2,'London borough','Barnet','GB-BNE',''),(1358,2,'London borough','Bexley','GB-BEX',''),(1359,2,'London borough','Brent','GB-BEN',''),(1360,2,'London borough','Bromley','GB-BRY',''),(1361,2,'London borough','Camden','GB-CMD',''),(1362,2,'London borough','Croydon','GB-CRY',''),(1363,2,'London borough','Ealing','GB-EAL',''),(1364,2,'London borough','Enfield','GB-ENF',''),(1365,2,'London borough','Greenwich','GB-GRE',''),(1366,2,'London borough','Hackney','GB-HCK',''),(1367,2,'London borough','Hammersmith and Fulham','GB-HMF',''),(1368,2,'London borough','Haringey','GB-HRY',''),(1369,2,'London borough','Harrow','GB-HRW',''),(1370,2,'London borough','Havering','GB-HAV',''),(1371,2,'London borough','Hillingdon','GB-HIL',''),(1372,2,'London borough','Hounslow','GB-HNS',''),(1373,2,'London borough','Islington','GB-ISL',''),(1374,2,'London borough','Kensington and Chelsea','GB-KEC',''),(1375,2,'London borough','Kingston upon Thames','GB-KTT',''),(1376,2,'London borough','Lambeth','GB-LBH',''),(1377,2,'London borough','Lewisham','GB-LEW',''),(1378,2,'London borough','Merton','GB-MRT',''),(1379,2,'London borough','Newham','GB-NWM',''),(1380,2,'London borough','Redbridge','GB-RDB',''),(1381,2,'London borough','Richmond upon Thames','GB-RIC',''),(1382,2,'London borough','Southwark','GB-SWK',''),(1383,2,'London borough','Sutton','GB-STN',''),(1384,2,'London borough','Tower Hamlets','GB-TWH',''),(1385,2,'London borough','Waltham Forest','GB-WFT',''),(1386,2,'London borough','Wandsworth','GB-WND',''),(1387,2,'London borough','Westminster','GB-WSM',''),(1388,2,'Metropolitan district','Barnsley','GB-BNS',''),(1389,2,'Metropolitan district','Birmingham','GB-BIR',''),(1390,2,'Metropolitan district','Bolton','GB-BOL',''),(1391,2,'Metropolitan district','Bradford','GB-BRD',''),(1392,2,'Metropolitan district','Bury','GB-BUR',''),(1393,2,'Metropolitan district','Calderdale','GB-CLD',''),(1394,2,'Metropolitan district','Coventry','GB-COV',''),(1395,2,'Metropolitan district','Doncaster','GB-DNC',''),(1396,2,'Metropolitan district','Dudley','GB-DUD',''),(1397,2,'Metropolitan district','Gateshead','GB-GAT',''),(1398,2,'Metropolitan district','Kirklees','GB-KIR',''),(1399,2,'Metropolitan district','Knowsley','GB-KWL',''),(1400,2,'Metropolitan district','Leeds','GB-LDS',''),(1401,2,'Metropolitan district','Liverpool','GB-LIV',''),(1402,2,'Metropolitan district','Manchester','GB-MAN',''),(1403,2,'Metropolitan district','Newcastle upon Tyne','GB-NET',''),(1404,2,'Metropolitan district','North Tyneside','GB-NTY',''),(1405,2,'Metropolitan district','Oldham','GB-OLD',''),(1406,2,'Metropolitan district','Rochdale','GB-RCH',''),(1407,2,'Metropolitan district','Rotherham','GB-ROT',''),(1408,2,'Metropolitan district','Salford','GB-SLF',''),(1409,2,'Metropolitan district','Sandwell','GB-SAW',''),(1410,2,'Metropolitan district','Sefton','GB-SFT',''),(1411,2,'Metropolitan district','Sheffield','GB-SHF',''),(1412,2,'Metropolitan district','Solihull','GB-SOL',''),(1413,2,'Metropolitan district','South Tyneside','GB-STY',''),(1414,2,'Metropolitan district','St. Helens','GB-SHN',''),(1415,2,'Metropolitan district','Stockport','GB-SKP',''),(1416,2,'Metropolitan district','Sunderland','GB-SND',''),(1417,2,'Metropolitan district','Tameside','GB-TAM',''),(1418,2,'Metropolitan district','Trafford','GB-TRF',''),(1419,2,'Metropolitan district','Wakefield','GB-WKF',''),(1420,2,'Metropolitan district','Walsall','GB-WLL',''),(1421,2,'Metropolitan district','Wigan','GB-WGN',''),(1422,2,'Metropolitan district','Wirral','GB-WRL',''),(1423,2,'Metropolitan district','Wolverhampton','GB-WLV',''),(1424,2,'City corporation','London, City of','GB-LND',''),(1425,2,'Division','Aberdeen City','GB-ABE',''),(1426,2,'Division','Aberdeenshire','GB-ABD',''),(1427,2,'Division','Angus','GB-ANS',''),(1428,2,'Division','Antrim','GB-ANT',''),(1429,2,'Division','Ards','GB-ARD',''),(1430,2,'Division','Argyll and Bute','GB-AGB',''),(1431,2,'Division','Armagh','GB-ARM',''),(1432,2,'Division','Ballymena','GB-BLA',''),(1433,2,'Division','Ballymoney','GB-BLY',''),(1434,2,'Division','Banbridge','GB-BNB',''),(1435,2,'Division','Bath and North East Somerset','GB-BAS',''),(1436,2,'Division','Belfast','GB-BFS',''),(1437,2,'Division','Blackburn with Darwen','GB-BBD',''),(1438,2,'Division','Blackpool','GB-BPL',''),(1439,2,'Division','Blaenau Gwent','GB-BGW',''),(1440,2,'Division','Bournemouth','GB-BMH',''),(1441,2,'Division','Bracknell Forest','GB-BRC',''),(1442,2,'Division','Bridgend','GB-BGE',''),(1443,2,'Division','Brighton and Hove','GB-BNH',''),(1444,2,'Division','Bristol, City of','GB-BST',''),(1445,2,'Division','Caerphilly','GB-CAY',''),(1446,2,'Division','Cardiff','GB-CRF',''),(1447,2,'Division','Carmarthenshire','GB-CMN',''),(1448,2,'Division','Carrickfergus','GB-CKF',''),(1449,2,'Division','Castlereagh','GB-CSR',''),(1450,2,'Division','Ceredigion','GB-CGN',''),(1451,2,'Division','Clackmannanshire','GB-CLK',''),(1452,2,'Division','Coleraine','GB-CLR',''),(1453,2,'Division','Conwy','GB-CWY',''),(1454,2,'Division','Cookstown','GB-CKT',''),(1455,2,'Division','Craigavon','GB-CGV',''),(1456,2,'Division','Darlington','GB-DAL',''),(1457,2,'Division','Denbighshire','GB-DEN',''),(1458,2,'Division','Derby','GB-DER',''),(1459,2,'Division','Derry','GB-DRY',''),(1460,2,'Division','Down','GB-DOW',''),(1461,2,'Division','Dumfries and Galloway','GB-DGY',''),(1462,2,'Division','Dundee City','GB-DND',''),(1463,2,'Division','Dungannon','GB-DGN',''),(1464,2,'Division','East Ayrshire','GB-EAY',''),(1465,2,'Division','East Dunbartonshire','GB-EDU',''),(1466,2,'Division','East Lothian','GB-ELN',''),(1467,2,'Division','East Renfrewshire','GB-ERW',''),(1468,2,'Division','East Riding of Yorkshire','GB-ERY',''),(1469,2,'Division','Edinburgh, City of','GB-EDH',''),(1470,2,'Division','Eilean Siar','GB-ELS',''),(1471,2,'Division','Falkirk','GB-FAL',''),(1472,2,'Division','Fermanagh','GB-FER',''),(1473,2,'Division','Fife','GB-FIF',''),(1474,2,'Division','Flintshire','GB-FLN',''),(1475,2,'Division','Glasgow City','GB-GLG',''),(1476,2,'Division','Gwynedd','GB-GWN',''),(1477,2,'Division','Halton','GB-HAL',''),(1478,2,'Division','Hartlepool','GB-HPL',''),(1479,2,'Division','Herefordshire, County of','GB-HEF',''),(1480,2,'Division','Highland','GB-HED',''),(1481,2,'Division','Inverclyde','GB-IVC',''),(1482,2,'Division','Isle of Anglesey','GB-AGY',''),(1483,2,'Division','Isle of Wight','GB-IOW',''),(1484,2,'Division','Isles of Scilly','GB-IOS',''),(1485,2,'Division','Kingston upon Hull, City of','GB-KHL',''),(1486,2,'Division','Larne','GB-LRN',''),(1487,2,'Division','Leicester','GB-LCE',''),(1488,2,'Division','Limavady','GB-LMV',''),(1489,2,'Division','Lisburn','GB-LSB',''),(1490,2,'Division','Luton','GB-LUT',''),(1491,2,'Division','Magherafelt','GB-MFT',''),(1492,2,'Division','Medway','GB-MDW',''),(1493,2,'Division','Merthyr Tydfil','GB-MTY',''),(1494,2,'Division','Middlesbrough','GB-MDB',''),(1495,2,'Division','Midlothian','GB-MLN',''),(1496,2,'Division','Milton Keynes','GB-MIK',''),(1497,2,'Division','Monmouthshire','GB-MON',''),(1498,2,'Division','Moray','GB-MRY',''),(1499,2,'Division','Moyle','GB-MYL',''),(1500,2,'Division','Neath Port Talbot','GB-NTL',''),(1501,2,'Division','Newport','GB-NWP',''),(1502,2,'Division','Newry and Mourne','GB-NYM',''),(1503,2,'Division','Newtownabbey','GB-NTA',''),(1504,2,'Division','North Ayrshire','GB-NAY',''),(1505,2,'Division','North Down','GB-NDN',''),(1506,2,'Division','North East Lincolnshire','GB-NEL',''),(1507,2,'Division','North Lanarkshire','GB-NLK',''),(1508,2,'Division','North Lincolnshire','GB-NLN',''),(1509,2,'Division','North Somerset','GB-NSM',''),(1510,2,'Division','Nottingham','GB-NGM',''),(1511,2,'Division','Omagh','GB-OMH',''),(1512,2,'Division','Orkney Islands','GB-ORR',''),(1513,2,'Division','Pembrokeshire','GB-PEM',''),(1514,2,'Division','Perth and Kinross','GB-PKN',''),(1515,2,'Division','Peterborough','GB-PTE',''),(1516,2,'Division','Plymouth','GB-PLY',''),(1517,2,'Division','Poole','GB-POL',''),(1518,2,'Division','Portsmouth','GB-POR',''),(1519,2,'Division','Powys','GB-POW',''),(1520,2,'Division','Reading','GB-RDG',''),(1521,2,'Division','Redcar and Cleveland','GB-RCC',''),(1522,2,'Division','Renfrewshire','GB-RFW',''),(1523,2,'Division','Rhondda, Cynon, Taff','GB-RCT',''),(1524,2,'Division','Rutland','GB-RUT',''),(1525,2,'Division','Scottish Borders, The','GB-SCB',''),(1526,2,'Division','Shetland Islands','GB-ZET',''),(1527,2,'Division','Shropshire','GB-SHR',''),(1528,2,'Division','Slough','GB-SLG',''),(1529,2,'Division','South Ayrshire','GB-SAY',''),(1530,2,'Division','South Gloucestershire','GB-SGC',''),(1531,2,'Division','South Lanarkshire','GB-SLK',''),(1532,2,'Division','Southampton','GB-STH',''),(1533,2,'Division','Southend-on-Sea','GB-SOS',''),(1534,2,'Division','Stirling','GB-STG',''),(1535,2,'Division','Stockton-on-Tees','GB-STT',''),(1536,2,'Division','Stoke-on-Trent','GB-STE',''),(1537,2,'Division','Strabane','GB-STB',''),(1538,2,'Division','Swansea','GB-SWA',''),(1539,2,'Division','Swindon','GB-SWD',''),(1540,2,'Division','Telford and Wrekin','GB-TFW',''),(1541,2,'Division','Thurrock','GB-THR',''),(1542,2,'Division','Torbay','GB-TOB',''),(1543,2,'Division','Torfaen','GB-TOF',''),(1544,2,'Division','Vale of Glamorgan, The','GB-VGL',''),(1545,2,'Division','Warrington','GB-WRT',''),(1546,2,'Division','Warwickshire','GB-WAR',''),(1547,2,'Division','West Berkshire','GB-WBX',''),(1548,2,'Division','West Dunbartonshire','GB-WDU',''),(1549,2,'Division','West Lothian','GB-WLN',''),(1550,2,'Division','Windsor and Maidenhead','GB-WNM',''),(1551,2,'Division','Wokingham','GB-WOK',''),(1552,2,'Division','Wrexham','GB-WRX',''),(1553,2,'Division','York','GB-YOR',''),(1554,91,'Parish','Saint Andrew','GD-01',''),(1555,91,'Parish','Saint David','GD-02',''),(1556,91,'Parish','Saint George','GD-03',''),(1557,91,'Parish','Saint John','GD-04',''),(1558,91,'Parish','Saint Mark','GD-05',''),(1559,91,'Parish','Saint Patrick','GD-06',''),(1560,91,'Dependency','Southern Grenadine Islands','GD-10',''),(1561,85,'Autonomous republic','Abkhazia','GE-AB',''),(1562,85,'Autonomous republic','Ajaria','GE-AJ',''),(1563,85,'City','T’bilisi','GE-TB',''),(1564,85,'Region','Guria','GE-GU',''),(1565,85,'Region','Imeret’i','GE-IM',''),(1566,85,'Region','Kakhet’i','GE-KA',''),(1567,85,'Region','K’vemo K’art’li','GE-KK',''),(1568,85,'Region','Mts’khet’a-Mt’ianet’i','GE-MM',''),(1569,85,'Region','Racha-Lech’khumi-K’vemo Svanet’i','GE-RL',''),(1570,85,'Region','Samegrelo-Zemo Svanet’i','GE-SZ',''),(1571,85,'Region','Samts’khe-Javakhet’i','GE-SJ',''),(1572,85,'Region','Shida K’art’li','GE-SK',''),(1573,87,'Region','Ashanti','GH-AH',''),(1574,87,'Region','Brong-Ahafo','GH-BA',''),(1575,87,'Region','Central','GH-CP',''),(1576,87,'Region','Eastern','GH-EP',''),(1577,87,'Region','Greater Accra','GH-AA',''),(1578,87,'Region','Northern','GH-NP',''),(1579,87,'Region','Upper East','GH-UE',''),(1580,87,'Region','Upper West','GH-UW',''),(1581,87,'Region','Volta','GH-TV',''),(1582,87,'Region','Western','GH-WP',''),(1583,84,'Division','Lower River','GM-L',''),(1584,84,'Division','Central River','GM-M',''),(1585,84,'Division','North Bank','GM-N',''),(1586,84,'Division','Upper River','GM-U',''),(1587,84,'Division','Western','GM-W',''),(1588,84,'City','Banjul','GM-B',''),(1589,96,'Governorate','Boké, Gouvernorat de','GN B',''),(1590,96,'Governorate','Faranah, Gouvernorat de','GN F',''),(1591,96,'Governorate','Kankan, Gouvernorat de','GN K',''),(1592,96,'Governorate','Kindia, Gouvernorat de','GN D',''),(1593,96,'Governorate','Labé, Gouvernorat de','GN L',''),(1594,96,'Governorate','Mamou, Gouvernorat de','GN M',''),(1595,96,'Governorate','Nzérékoré, Gouvernorat de','GN N',''),(1596,96,'City','Conakry','GN C',''),(1597,96,'Prefecture','Beyla','GN-BE',''),(1598,96,'Prefecture','Boffa','GN-BF',''),(1599,96,'Prefecture','Boké','GN-BK',''),(1600,96,'Prefecture','Coyah','GN-CO',''),(1601,96,'Prefecture','Dabola','GN-DB',''),(1602,96,'Prefecture','Dalaba','GN-DL',''),(1603,96,'Prefecture','Dinguiraye','GN-DI',''),(1604,96,'Prefecture','Dubréka','GN-DU',''),(1605,96,'Prefecture','Faranah','GN-FA',''),(1606,96,'Prefecture','Forécariah','GN-FO',''),(1607,96,'Prefecture','Fria','GN-FR',''),(1608,96,'Prefecture','Gaoual','GN-GA',''),(1609,96,'Prefecture','Guékédou','GN-GU',''),(1610,96,'Prefecture','Kankan','GN-KA',''),(1611,96,'Prefecture','Kérouané','GN-KE',''),(1612,96,'Prefecture','Kindia','GN-KD',''),(1613,96,'Prefecture','Kissidougou','GN-KS',''),(1614,96,'Prefecture','Koubia','GN-KB',''),(1615,96,'Prefecture','Koundara','GN-KN',''),(1616,96,'Prefecture','Kouroussa','GN-KO',''),(1617,96,'Prefecture','Labé','GN-LA',''),(1618,96,'Prefecture','Lélouma','GN-LE',''),(1619,96,'Prefecture','Lola','GN-LO',''),(1620,96,'Prefecture','Macenta','GN-MC',''),(1621,96,'Prefecture','Mali','GN-ML',''),(1622,96,'Prefecture','Mamou','GN-MM',''),(1623,96,'Prefecture','Mandiana','GN-MD',''),(1624,96,'Prefecture','Nzérékoré','GN-NZ',''),(1625,96,'Prefecture','Pita','GN-PI',''),(1626,96,'Prefecture','Siguiri','GN-SI',''),(1627,96,'Prefecture','Télimélé','GN-TE',''),(1628,96,'Prefecture','Tougué','GN-TO',''),(1629,96,'Prefecture','Yomou','GN-YO',''),(1630,71,'Province','Región Continental','GQ-C',''),(1631,71,'Province','Región Insular','GQ-I',''),(1632,71,'Province','Annobón','GQ-AN',''),(1633,71,'Province','Bioko Norte','GQ-BN',''),(1634,71,'Province','Bioko Sur','GQ-BS',''),(1635,71,'Province','Centro Sur','GQ-CS',''),(1636,71,'Province','Kié-Ntem','GQ-KN',''),(1637,71,'Province','Litoral','GQ-LI',''),(1638,71,'Province','Wele-Nzás','GQ-WN',''),(1639,89,'Region','Periféreia Anatolikís Makedonías kai Thrákis','GR I',''),(1640,89,'Region','Periféreia Kentrikís Makedonías','GR II',''),(1641,89,'Region','Periféreia Dytikís Makedonías','GR III',''),(1642,89,'Region','Periféreia Ipeírou','GR IV',''),(1643,89,'Region','Periféreia Thessalías','GR V',''),(1644,89,'Region','Periféreia Ioníon Níson','GR VI',''),(1645,89,'Region','Periféreia Dytikís Elládas','GR VII',''),(1646,89,'Region','Periféreia Stereás Elládas','GR VIII',''),(1647,89,'Region','Periféreia Attikís','GR IX',''),(1648,89,'Region','Periféreia Peloponnísou','GR X',''),(1649,89,'Region','Periféreia Voreíou Aigaíou','GR XI',''),(1650,89,'Region','Periféreia Notíou Aigaíou','GR XII',''),(1651,89,'Region','Periféreia Krítis','GR XIII',''),(1652,89,'Autonomous monastic state','Ágion Óros','GR-69',''),(1653,89,'Prefecture','Nomós Athinón','GR-A1',''),(1654,89,'Prefecture','Nomós Anatolikís Attikís','GR-A2',''),(1655,89,'Prefecture','Nomós Peiraiós','GR-A3',''),(1656,89,'Prefecture','Nomós Dytikís Attikís','GR-A4',''),(1657,89,'Prefecture','Nomós Aitoloakarnanías','GR-01',''),(1658,89,'Prefecture','Nomós Voiotías','GR-03',''),(1659,89,'Prefecture','Nomós Évvoias','GR-04',''),(1660,89,'Prefecture','Nomós Evrytanías','GR-05',''),(1661,89,'Prefecture','Nomós Fthiótidas','GR-06',''),(1662,89,'Prefecture','Nomós Fokídas','GR-07',''),(1663,89,'Prefecture','Nomós Argolídas','GR-11',''),(1664,89,'Prefecture','Nomós Arkadías','GR-12',''),(1665,89,'Prefecture','Nomós Acha?as','GR-13',''),(1666,89,'Prefecture','Nomós Ileías','GR-14',''),(1667,89,'Prefecture','Nomós Korinthías','GR-15',''),(1668,89,'Prefecture','Nomós Lakonías','GR-16',''),(1669,89,'Prefecture','Nomós Messinías','GR-17',''),(1670,89,'Prefecture','Nomós Zakýnthoy','GR-21',''),(1671,89,'Prefecture','Nomós Kérkyras','GR-22',''),(1672,89,'Prefecture','Nomós Kefaloniás kai Ithákis','GR-23',''),(1673,89,'Prefecture','Nomós Lefkádas','GR-24',''),(1674,89,'Prefecture','Nomós Ártas','GR-31',''),(1675,89,'Prefecture','Nomós Thesprotías','GR-32',''),(1676,89,'Prefecture','Nomós Ioannínon','GR-33',''),(1677,89,'Prefecture','Nomós Prévezas','GR-34',''),(1678,89,'Prefecture','Nomós Kardítsas','GR-41',''),(1679,89,'Prefecture','Nomós Lárissas','GR-42',''),(1680,89,'Prefecture','Nomós Magnisías','GR-43',''),(1681,89,'Prefecture','Nomós Trikálon','GR-44',''),(1682,89,'Prefecture','Nomós Grevenón','GR-51',''),(1683,89,'Prefecture','Nomós Drámas','GR-52',''),(1684,89,'Prefecture','Nomós Imathías','GR-53',''),(1685,89,'Prefecture','Nomós Thessaloníkis','GR-54',''),(1686,89,'Prefecture','Nomós Kaválas','GR-55',''),(1687,89,'Prefecture','Nomós Kastoriás','GR-56',''),(1688,89,'Prefecture','Nomós Kilkís','GR-57',''),(1689,89,'Prefecture','Nomós Kozánis','GR-58',''),(1690,89,'Prefecture','Nomós Péllas','GR-59',''),(1691,89,'Prefecture','Nomós Pierías','GR-61',''),(1692,89,'Prefecture','Nomós Serrón','GR-62',''),(1693,89,'Prefecture','Nomós Flórinas','GR-63',''),(1694,89,'Prefecture','Nomós Chalkidikís','GR-64',''),(1695,89,'Prefecture','Nomós Évroy','GR-71',''),(1696,89,'Prefecture','Nomós Xánthis','GR-72',''),(1697,89,'Prefecture','Nomós Rodópis','GR-73',''),(1698,89,'Prefecture','Nomós Dodekanísoy','GR-81',''),(1699,89,'Prefecture','Nomós Kykládon','GR-82',''),(1700,89,'Prefecture','Nomós Lésboy','GR-83',''),(1701,89,'Prefecture','Nomós Sámoy','GR-84',''),(1702,89,'Prefecture','Nomós Chíoy','GR-85',''),(1703,89,'Prefecture','Nomós Irakleíoy','GR-91',''),(1704,89,'Prefecture','Nomós Lasithíoy','GR-92',''),(1705,89,'Prefecture','Nomós Rethýmnoy','GR-93',''),(1706,89,'Prefecture','Nomós Chaníon','GR-94',''),(1707,94,'Department','Alta Verapaz','GT-AV',''),(1708,94,'Department','Baja Verapaz','GT-BV',''),(1709,94,'Department','Chimaltenango','GT-CM',''),(1710,94,'Department','Chiquimula','GT-CQ',''),(1711,94,'Department','El Progreso','GT-PR',''),(1712,94,'Department','Escuintla','GT-ES',''),(1713,94,'Department','Guatemala','GT-GU',''),(1714,94,'Department','Huehuetenango','GT-HU',''),(1715,94,'Department','Izabal','GT-IZ',''),(1716,94,'Department','Jalapa','GT-JA',''),(1717,94,'Department','Jutiapa','GT-JU',''),(1718,94,'Department','Petén','GT-PE',''),(1719,94,'Department','Quetzaltenango','GT-QZ',''),(1720,94,'Department','Quiché','GT-QC',''),(1721,94,'Department','Retalhuleu','GT-RE',''),(1722,94,'Department','Sacatepéquez','GT-SA',''),(1723,94,'Department','San Marcos','GT-SM',''),(1724,94,'Department','Santa Rosa','GT-SR',''),(1725,94,'Department','Sololá','GT-SO',''),(1726,94,'Department','Suchitepéquez','GT-SU',''),(1727,94,'Department','Totonicapán','GT-TO',''),(1728,94,'Department','Zacapa','GT-ZA',''),(1729,97,'Region','Bafatá','GW-BA',''),(1730,97,'Region','Biombo','GW-BM',''),(1731,97,'Region','Bolama','GW-BL',''),(1732,97,'Region','Cacheu','GW-CA',''),(1733,97,'Region','Gabú','GW-GA',''),(1734,97,'Region','Oio','GW-OI',''),(1735,97,'Region','Quinara','GW-QU',''),(1736,97,'Region','Tombali','GW-TO',''),(1737,97,'Autonomous sector','Bissau','GW-BS',''),(1738,98,'Region','Barima-Waini','GY-BA',''),(1739,98,'Region','Cuyuni-Mazaruni','GY-CU',''),(1740,98,'Region','Demerara-Mahaica','GY-DE',''),(1741,98,'Region','East Berbice-Corentyne','GY-EB',''),(1742,98,'Region','Essequibo Islands-West Demerara','GY-ES',''),(1743,98,'Region','Mahaica-Berbice','GY-MA',''),(1744,98,'Region','Pomeroon-Supenaam','GY-PM',''),(1745,98,'Region','Potaro-Siparuni','GY-PT',''),(1746,98,'Region','Upper Demerara-Berbice','GY-UD',''),(1747,98,'Region','Upper Takutu-Upper Essequibo','GY-UT',''),(1748,102,'Department','Atlántida','HN-AT',''),(1749,102,'Department','Colón','HN-CL',''),(1750,102,'Department','Comayagua','HN-CM',''),(1751,102,'Department','Copán','HN-CP',''),(1752,102,'Department','Cortés','HN-CR',''),(1753,102,'Department','Choluteca','HN-CH',''),(1754,102,'Department','El Paraíso','HN-EP',''),(1755,102,'Department','Francisco Morazán','HN-FM',''),(1756,102,'Department','Gracias a Dios','HN-GD',''),(1757,102,'Department','Intibucá','HN-IN',''),(1758,102,'Department','Islas de la Bahía','HN-IB',''),(1759,102,'Department','La Paz','HN-LP',''),(1760,102,'Department','Lempira','HN-LE',''),(1761,102,'Department','Ocotepeque','HN-OC',''),(1762,102,'Department','Olancho','HN-OL',''),(1763,102,'Department','Santa Bárbara','HN-SB',''),(1764,102,'Department','Valle','HN-VA',''),(1765,102,'Department','Yoro','HN-YO',''),(1766,59,'City','Grad Zagreb','HR-21',''),(1767,59,'County','Bjelovarsko-bilogorska županija','HR-07',''),(1768,59,'County','Brodsko-posavska županija','HR-12',''),(1769,59,'County','Dubrova?ko-neretvanska županija','HR-19',''),(1770,59,'County','Istarska županija','HR-18',''),(1771,59,'County','Karlova?ka županija','HR-04',''),(1772,59,'County','Koprivni?ko-križeva?ka županija','HR-06',''),(1773,59,'County','Krapinsko-zagorska županija','HR-02',''),(1774,59,'County','Li?ko-senjska županija','HR-09',''),(1775,59,'County','Me?imurska županija','HR-20',''),(1776,59,'County','Osje?ko-baranjska županija','HR-14',''),(1777,59,'County','Požeško-slavonska županija','HR-11',''),(1778,59,'County','Primorsko-goranska županija','HR-08',''),(1779,59,'County','Sisa?ko-moslava?ka županija','HR-03',''),(1780,59,'County','Splitsko-dalmatinska županija','HR-17',''),(1781,59,'County','Šibensko-kninska županija','HR-15',''),(1782,59,'County','Varaždinska županija','HR-05',''),(1783,59,'County','Viroviti?ko-podravska županija','HR-10',''),(1784,59,'County','Vukovarsko-srijemska županija','HR-16',''),(1785,59,'County','Zadarska županija','HR-13',''),(1786,59,'County','Zagreba?ka županija','HR-01',''),(1787,99,'Department','Artibonite','HT-AR',''),(1788,99,'Department','Centre','HT-CE',''),(1789,99,'Department','Grande-Anse','HT-GA',''),(1790,99,'Department','Nord','HT-ND',''),(1791,99,'Department','Nord-Est','HT-NE',''),(1792,99,'Department','Nord-Ouest','HT-NO',''),(1793,99,'Department','Ouest','HT-OU',''),(1794,99,'Department','Sud','HT-SD',''),(1795,99,'Department','Sud-Est','HT-SE',''),(1796,104,'County','Bács-Kiskun','HU-BK',''),(1797,104,'County','Baranya','HU-BA',''),(1798,104,'County','Békés','HU-BE',''),(1799,104,'County','Borsod-Abaúj-Zemplén','HU-BZ',''),(1800,104,'County','Csongrád','HU-CS',''),(1801,104,'County','Fejér','HU-FE',''),(1802,104,'County','Gy?r-Moson-Sopron','HU-GS',''),(1803,104,'County','Hajdú-Bihar','HU-HB',''),(1804,104,'County','Heves','HU-HE',''),(1805,104,'County','Jász-Nagykun-Szolnok','HU-JN',''),(1806,104,'County','Komárom-Esztergom','HU-KE',''),(1807,104,'County','Nógrád','HU-NO',''),(1808,104,'County','Pest','HU-PE',''),(1809,104,'County','Somogy','HU-SO',''),(1810,104,'County','Szabolcs-Szatmár-Bereg','HU-SZ',''),(1811,104,'County','Tolna','HU-TO',''),(1812,104,'County','Vas','HU-VA',''),(1813,104,'County','Veszprém (county)','HU-VE',''),(1814,104,'County','Zala','HU-ZA',''),(1815,104,'City with county rights','Békéscsaba','HU-BC',''),(1816,104,'City with county rights','Debrecen','HU-DE',''),(1817,104,'City with county rights','Dunaújváros','HU-DU',''),(1818,104,'City with county rights','Eger','HU-EG',''),(1819,104,'City with county rights','Gy?r','HU-GY',''),(1820,104,'City with county rights','Hódmez?vásárhely','HU-HV',''),(1821,104,'City with county rights','Kaposvár','HU-KV',''),(1822,104,'City with county rights','Kecskemét','HU-KM',''),(1823,104,'City with county rights','Miskolc','HU-MI',''),(1824,104,'City with county rights','Nagykanizsa','HU-NK',''),(1825,104,'City with county rights','Nyíregyháza','HU-NY',''),(1826,104,'City with county rights','Pécs','HU-PS',''),(1827,104,'City with county rights','Salgótarján','HU-ST',''),(1828,104,'City with county rights','Sopron','HU-SN',''),(1829,104,'City with county rights','Szeged','HU-SD',''),(1830,104,'City with county rights','Székesfehérvár','HU-SF',''),(1831,104,'City with county rights','Szekszárd','HU-SS',''),(1832,104,'City with county rights','Szolnok','HU-SK',''),(1833,104,'City with county rights','Szombathely','HU-SH',''),(1834,104,'City with county rights','Tatabánya','HU-TB',''),(1835,104,'City with county rights','Veszprém','HU-VM',''),(1836,104,'City with county rights','Zalaegerszeg','HU-ZE',''),(1837,104,'Capital city','Budapest','HU-BU',''),(1838,107,'Geographical unit','Papua','ID IJ',''),(1839,107,'Geographical unit','Jawa','ID JW',''),(1840,107,'Geographical unit','Kalimantan','ID KA',''),(1841,107,'Geographical unit','Maluku','ID MA',''),(1842,107,'Geographical unit','Nusa Tenggara','ID NU',''),(1843,107,'Geographical unit','Sulawesi','ID SL',''),(1844,107,'Geographical unit','Sumatera','ID SM',''),(1845,107,'Autonomous Province','Aceh','ID-AC',''),(1846,107,'Province','Bali','ID-BA',''),(1847,107,'Province','Bangka Belitung','ID-BB',''),(1848,107,'Province','Banten','ID-BT',''),(1849,107,'Province','Bengkulu','ID-BE',''),(1850,107,'Province','Gorontalo','ID-GO',''),(1851,107,'Province','Jambi','ID-JA',''),(1852,107,'Province','Jawa Barat','ID-JB',''),(1853,107,'Province','Jawa Tengah','ID-JT',''),(1854,107,'Province','Jawa Timur','ID-JI',''),(1855,107,'Province','Kalimantan Barat','ID-KB',''),(1856,107,'Province','Kalimantan Tengah','ID-KT',''),(1857,107,'Province','Kalimantan Selatan','ID-KS',''),(1858,107,'Province','Kalimantan Timur','ID-KI',''),(1859,107,'Province','Kepulauan Riau','ID-KR',''),(1860,107,'Province','Lampung','ID-LA',''),(1861,107,'Province','Maluku','ID-MA',''),(1862,107,'Province','Maluku Utara','ID-MU',''),(1863,107,'Province','Nusa Tenggara Barat','ID-NB',''),(1864,107,'Province','Nusa Tenggara Timur','ID-NT',''),(1865,107,'Province','Papua','ID-PA',''),(1866,107,'Province','Riau','ID-RI',''),(1867,107,'Province','Sulawesi Barat','ID-SR',''),(1868,107,'Province','Sulawesi Selatan','ID-SN',''),(1869,107,'Province','Sulawesi Tengah','ID-ST',''),(1870,107,'Province','Sulawesi Tenggara','ID-SG',''),(1871,107,'Province','Sulawesi Utara','ID-SA',''),(1872,107,'Province','Sumatra Barat','ID-SB',''),(1873,107,'Province','Sumatra Selatan','ID-SS',''),(1874,107,'Province','Sumatera Utara','ID-SU',''),(1875,107,'Special District','Jakarta Raya','ID-JK',''),(1876,107,'Special Region','Yogyakarta','ID-YO',''),(1877,110,'Province','Connacht','IE C',''),(1878,110,'Province','Leinster','IE L',''),(1879,110,'Province','Munster','IE M',''),(1880,110,'Province','Ulster','IE U',''),(1881,110,'County','Cork','IE-C',''),(1882,110,'County','Clare','IE-CE',''),(1883,110,'County','Cavan','IE-CN',''),(1884,110,'County','Carlow','IE-CW',''),(1885,110,'County','Dublin','IE-D',''),(1886,110,'County','Donegal','IE-DL',''),(1887,110,'County','Galway','IE-G',''),(1888,110,'County','Kildare','IE-KE',''),(1889,110,'County','Kilkenny','IE-KK',''),(1890,110,'County','Kerry','IE-KY',''),(1891,110,'County','Longford','IE-LD',''),(1892,110,'County','Louth','IE-LH',''),(1893,110,'County','Limerick','IE-LK',''),(1894,110,'County','Leitrim','IE-LM',''),(1895,110,'County','Laois','IE-LS',''),(1896,110,'County','Meath','IE-MH',''),(1897,110,'County','Monaghan','IE-MN',''),(1898,110,'County','Mayo','IE-MO',''),(1899,110,'County','Offaly','IE-OY',''),(1900,110,'County','Roscommon','IE-RN',''),(1901,110,'County','Sligo','IE-SO',''),(1902,110,'County','Tipperary','IE-TA',''),(1903,110,'County','Waterford','IE-WD',''),(1904,110,'County','Westmeath','IE-WH',''),(1905,110,'County','Wicklow','IE-WW',''),(1906,110,'County','Wexford','IE-WX',''),(1907,112,'District','HaDarom','IL-D',''),(1908,112,'District','HaMerkaz','IL-M',''),(1909,112,'District','HaZafon','IL-Z',''),(1910,112,'District','Hefa','IL-HA',''),(1911,112,'District','Tel-Aviv','IL-TA',''),(1912,112,'District','Yerushalayim Al Quds','IL-JM',''),(1913,106,'State','Andhra Pradesh','IN-AP',''),(1914,106,'State','Arun?chal Pradesh','IN-AR',''),(1915,106,'State','Assam','IN-AS',''),(1916,106,'State','Bih?r','IN-BR',''),(1917,106,'State','Chhatt?sgarh','IN-CT',''),(1918,106,'State','Goa','IN-GA',''),(1919,106,'State','Gujar?t','IN-GJ',''),(1920,106,'State','Hary?na','IN-HR',''),(1921,106,'State','Him?chal Pradesh','IN-HP',''),(1922,106,'State','Jammu and Kashm?r','IN-JK',''),(1923,106,'State','Jharkhand','IN-JH',''),(1924,106,'State','Karn?taka','IN-KA',''),(1925,106,'State','Kerala','IN-KL',''),(1926,106,'State','Madhya Pradesh','IN-MP',''),(1927,106,'State','Mah?r?shtra','IN-MH',''),(1928,106,'State','Manipur','IN-MN',''),(1929,106,'State','Megh?laya','IN-ML',''),(1930,106,'State','Mizoram','IN-MZ',''),(1931,106,'State','N?g?land','IN-NL',''),(1932,106,'State','Orissa','IN-OR',''),(1933,106,'State','Punjab','IN-PB',''),(1934,106,'State','R?jasth?n','IN-RJ',''),(1935,106,'State','Sikkim','IN-SK',''),(1936,106,'State','Tamil N?du','IN-TN',''),(1937,106,'State','Tripura','IN-TR',''),(1938,106,'State','Uttaranchal','IN-UL',''),(1939,106,'State','Uttar Pradesh','IN-UP',''),(1940,106,'State','West Bengal','IN-WB',''),(1941,106,'Union territory','Andaman and Nicobar Islands','IN-AN',''),(1942,106,'Union territory','Chand?garh','IN-CH',''),(1943,106,'Union territory','D?dra and Nagar Haveli','IN-DN',''),(1944,106,'Union territory','Dam?n and Diu','IN-DD',''),(1945,106,'Union territory','Delhi','IN-DL',''),(1946,106,'Union territory','Lakshadweep','IN-LD',''),(1947,106,'Union territory','Pondicherry','IN-PY',''),(1948,109,'Governorate','Al Anbar','IQ-AN',''),(1949,109,'Governorate','Al Basrah','IQ-BA',''),(1950,109,'Governorate','Al Muthanna','IQ-MU',''),(1951,109,'Governorate','Al Qadisiyah','IQ-QA',''),(1952,109,'Governorate','An Najef','IQ-NA',''),(1953,109,'Governorate','Arbil','IQ-AR',''),(1954,109,'Governorate','As Sulaymaniyah','IQ-SW',''),(1955,109,'Governorate','At Ta\'mim','IQ-TS',''),(1956,109,'Governorate','Babil','IQ-BB',''),(1957,109,'Governorate','Baghdad','IQ-BG',''),(1958,109,'Governorate','Dahuk','IQ-DA',''),(1959,109,'Governorate','Dhi Qar','IQ-DQ',''),(1960,109,'Governorate','Diyala','IQ-DI',''),(1961,109,'Governorate','Karbala\'','IQ-KA',''),(1962,109,'Governorate','Maysan','IQ-MA',''),(1963,109,'Governorate','Ninawa','IQ-NI',''),(1964,109,'Governorate','Salah ad Din','IQ-SD',''),(1965,109,'Governorate','Wasit','IQ-WA',''),(1966,108,'Province','Ardab?l','IR-03',''),(1967,108,'Province','?zarb?yj?n-e Gharb?','IR-02',''),(1968,108,'Province','?zarb?yj?n-e Sharq?','IR-01',''),(1969,108,'Province','B?shehr','IR-06',''),(1970,108,'Province','Chah?r Mah?ll va Bakht??r?','IR-08',''),(1971,108,'Province','E?fah?n','IR-04',''),(1972,108,'Province','F?rs','IR-14',''),(1973,108,'Province','G?l?n','IR-19',''),(1974,108,'Province','Golest?n','IR-27',''),(1975,108,'Province','Hamad?n','IR-24',''),(1976,108,'Province','Hormozg?n','IR-23',''),(1977,108,'Province','?l?m','IR-05',''),(1978,108,'Province','Kerm?n','IR-15',''),(1979,108,'Province','Kerm?nsh?h','IR-17',''),(1980,108,'Province','Khor?s?n-e Jan?b?','IR-29',''),(1981,108,'Province','Khor?s?n-e Razav?','IR-30',''),(1982,108,'Province','Khor?s?n-e Shem?l?','IR-31',''),(1983,108,'Province','Kh?zest?n','IR-10',''),(1984,108,'Province','Kohg?l?yeh va B?yer Ahmad','IR-18',''),(1985,108,'Province','Kordest?n','IR-16',''),(1986,108,'Province','Lorest?n','IR-20',''),(1987,108,'Province','Markaz?','IR-22',''),(1988,108,'Province','M?zandar?n','IR-21',''),(1989,108,'Province','Qazv?n','IR-28',''),(1990,108,'Province','Qom','IR-26',''),(1991,108,'Province','Semn?n','IR-12',''),(1992,108,'Province','S?st?n va Bal?chest?n','IR-13',''),(1993,108,'Province','Tehr?n','IR-07',''),(1994,108,'Province','Yazd','IR-25',''),(1995,108,'Province','Zanj?n','IR-11',''),(1996,105,'Region','Austurland','IS-7',''),(1997,105,'Region','Höfuðborgarsvæðið','IS-1',''),(1998,105,'Region','Norðurland eystra','IS-6',''),(1999,105,'Region','Norðurland vestra','IS-5',''),(2000,105,'Region','Suðurland','IS-8',''),(2001,105,'Region','Suðurnes','IS-2',''),(2002,105,'Region','Vestfirðir','IS-4',''),(2003,105,'Region','Vesturland','IS-3',''),(2004,105,'City','Reykjavík','IS-0',''),(2005,113,'Region','Abruzzo','IT 65',''),(2006,113,'Region','Basilicata','IT 77',''),(2007,113,'Region','Calabria','IT 78',''),(2008,113,'Region','Campania','IT 72',''),(2009,113,'Region','Emilia-Romagna','IT 45',''),(2010,113,'Region','Friuli-Venezia Giulia','IT 36',''),(2011,113,'Region','Lazio','IT 62',''),(2012,113,'Region','Liguria','IT 42',''),(2013,113,'Region','Lombardia','IT 25',''),(2014,113,'Region','Marche','IT 57',''),(2015,113,'Region','Molise','IT 67',''),(2016,113,'Region','Piemonte','IT 21',''),(2017,113,'Region','Puglia','IT 75',''),(2018,113,'Region','Sardegna','IT 88',''),(2019,113,'Region','Sicilia','IT 82',''),(2020,113,'Region','Toscana','IT 52',''),(2021,113,'Region','Trentino-Alto Adige','IT 32',''),(2022,113,'Region','Umbria','IT 55',''),(2023,113,'Region','Valle d\'Aosta','IT 23',''),(2024,113,'Region','Veneto','IT 34',''),(2025,113,'Province','Agrigento','IT-AG',''),(2026,113,'Province','Alessandria','IT-AL',''),(2027,113,'Province','Ancona','IT-AN',''),(2028,113,'Province','Aosta','IT-AO',''),(2029,113,'Province','Arezzo','IT-AR',''),(2030,113,'Province','Ascoli Piceno','IT-AP',''),(2031,113,'Province','Asti','IT-AT',''),(2032,113,'Province','Avellino','IT-AV',''),(2033,113,'Province','Bari','IT-BA',''),(2034,113,'Province','Belluno','IT-BL',''),(2035,113,'Province','Benevento','IT-BN',''),(2036,113,'Province','Bergamo','IT-BG',''),(2037,113,'Province','Biella','IT-BI',''),(2038,113,'Province','Bologna','IT-BO',''),(2039,113,'Province','Bolzano','IT-BZ',''),(2040,113,'Province','Brescia','IT-BS',''),(2041,113,'Province','Brindisi','IT-BR',''),(2042,113,'Province','Cagliari','IT-CA',''),(2043,113,'Province','Caltanissetta','IT-CL',''),(2044,113,'Province','Campobasso','IT-CB',''),(2045,113,'Province','Carbonia-Iglesias','IT-CI',''),(2046,113,'Province','Caserta','IT-CE',''),(2047,113,'Province','Catania','IT-CT',''),(2048,113,'Province','Catanzaro','IT-CZ',''),(2049,113,'Province','Chieti','IT-CH',''),(2050,113,'Province','Como','IT-CO',''),(2051,113,'Province','Cosenza','IT-CS',''),(2052,113,'Province','Cremona','IT-CR',''),(2053,113,'Province','Crotone','IT-KR',''),(2054,113,'Province','Cuneo','IT-CN',''),(2055,113,'Province','Enna','IT-EN',''),(2056,113,'Province','Ferrara','IT-FE',''),(2057,113,'Province','Firenze','IT-FI',''),(2058,113,'Province','Foggia','IT-FG',''),(2059,113,'Province','Forlì','IT-FO',''),(2060,113,'Province','Frosinone','IT-FR',''),(2061,113,'Province','Genova','IT-GE',''),(2062,113,'Province','Gorizia','IT-GO',''),(2063,113,'Province','Grosseto','IT-GR',''),(2064,113,'Province','Imperia','IT-IM',''),(2065,113,'Province','Isernia','IT-IS',''),(2066,113,'Province','La Spezia','IT-SP',''),(2067,113,'Province','L\'Aquila','IT-AQ',''),(2068,113,'Province','Latina','IT-LT',''),(2069,113,'Province','Lecce','IT-LE',''),(2070,113,'Province','Lecco','IT-LC',''),(2071,113,'Province','Livorno','IT-LI',''),(2072,113,'Province','Lodi','IT-LO',''),(2073,113,'Province','Lucca','IT-LU',''),(2074,113,'Province','Macerata','IT-SC',''),(2075,113,'Province','Mantova','IT-MN',''),(2076,113,'Province','Massa-Carrara','IT-MS',''),(2077,113,'Province','Matera','IT-MT',''),(2078,113,'Province','Medio Campidano','IT-VS',''),(2079,113,'Province','Messina','IT-ME',''),(2080,113,'Province','Milano','IT-MI',''),(2081,113,'Province','Modena','IT-MO',''),(2082,113,'Province','Napoli','IT-NA',''),(2083,113,'Province','Novara','IT-NO',''),(2084,113,'Province','Nuoro','IT-NU',''),(2085,113,'Province','Ogliastra','IT-OG',''),(2086,113,'Province','Olbia-Tempio','IT-OT',''),(2087,113,'Province','Oristano','IT-OR',''),(2088,113,'Province','Padova','IT-PD',''),(2089,113,'Province','Palermo','IT-PA',''),(2090,113,'Province','Parma','IT-PR',''),(2091,113,'Province','Pavia','IT-PV',''),(2092,113,'Province','Perugia','IT-PG',''),(2093,113,'Province','Pesaro e Urbino','IT-PS',''),(2094,113,'Province','Pescara','IT-PE',''),(2095,113,'Province','Piacenza','IT-PC',''),(2096,113,'Province','Pisa','IT-PI',''),(2097,113,'Province','Pistoia','IT-PT',''),(2098,113,'Province','Pordenone','IT-PN',''),(2099,113,'Province','Potenza','IT-PZ',''),(2100,113,'Province','Prato','IT-PO',''),(2101,113,'Province','Ragusa','IT-RG',''),(2102,113,'Province','Ravenna','IT-RA',''),(2103,113,'Province','Reggio Calabria','IT-RC',''),(2104,113,'Province','Reggio Emilia','IT-RE',''),(2105,113,'Province','Rieti','IT-RI',''),(2106,113,'Province','Rimini','IT-RN',''),(2107,113,'Province','Roma','IT-RM',''),(2108,113,'Province','Rovigo','IT-RO',''),(2109,113,'Province','Salerno','IT-SA',''),(2110,113,'Province','Sassari','IT-SS',''),(2111,113,'Province','Savona','IT-SV',''),(2112,113,'Province','Siena','IT-SI',''),(2113,113,'Province','Siracusa','IT-SR',''),(2114,113,'Province','Sondrio','IT-SO',''),(2115,113,'Province','Taranto','IT-TA',''),(2116,113,'Province','Teramo','IT-TE',''),(2117,113,'Province','Terni','IT-TR',''),(2118,113,'Province','Torino','IT-TO',''),(2119,113,'Province','Trapani','IT-TP',''),(2120,113,'Province','Trento','IT-TN',''),(2121,113,'Province','Treviso','IT-TV',''),(2122,113,'Province','Trieste','IT-TS',''),(2123,113,'Province','Udine','IT-UD',''),(2124,113,'Province','Varese','IT-VA',''),(2125,113,'Province','Venezia','IT-VE',''),(2126,113,'Province','Verbano-Cusio-Ossola','IT-VB',''),(2127,113,'Province','Vercelli','IT-VC',''),(2128,113,'Province','Verona','IT-VR',''),(2129,113,'Province','Vibo Valentia','IT-VV',''),(2130,113,'Province','Vicenza','IT-VI',''),(2131,113,'Province','Viterbo','IT-VT',''),(2132,114,'Parish','Clarendon','JM-13',''),(2133,114,'Parish','Hanover','JM-09',''),(2134,114,'Parish','Kingston','JM-01',''),(2135,114,'Parish','Manchester','JM-12',''),(2136,114,'Parish','Portland','JM-04',''),(2137,114,'Parish','Saint Andrew','JM-02',''),(2138,114,'Parish','Saint Ann','JM-06',''),(2139,114,'Parish','Saint Catherine','JM-14',''),(2140,114,'Parish','Saint Elizabeth','JM-11',''),(2141,114,'Parish','Saint James','JM-08',''),(2142,114,'Parish','Saint Mary','JM-05',''),(2143,114,'Parish','Saint Thomas','JM-03',''),(2144,114,'Parish','Trelawny','JM-07',''),(2145,114,'Parish','Westmoreland','JM-10',''),(2146,117,'Governorate','`Ajlun','JO-AJ',''),(2147,117,'Governorate','Al `Aqabah','JO-AQ',''),(2148,117,'Governorate','Al Balq?\'','JO-BA',''),(2149,117,'Governorate','Al Karak','JO-KA',''),(2150,117,'Governorate','Al Mafraq','JO-MA',''),(2151,117,'Governorate','Amman','JO-AM',''),(2152,117,'Governorate','A? ?af?lah','JO-AT',''),(2153,117,'Governorate','Az Zarq?\'','JO-AZ',''),(2154,117,'Governorate','Irbid','JO-JR',''),(2155,117,'Governorate','Jarash','JO-JA',''),(2156,117,'Governorate','Ma`?n','JO-MN',''),(2157,117,'Governorate','M?dab?','JO-MD',''),(2158,115,'Prefecture','Aichi','JP-23',''),(2159,115,'Prefecture','Akita','JP-05',''),(2160,115,'Prefecture','Aomori','JP-02',''),(2161,115,'Prefecture','Chiba','JP-12',''),(2162,115,'Prefecture','Ehime','JP-38',''),(2163,115,'Prefecture','Fukui','JP-18',''),(2164,115,'Prefecture','Fukuoka','JP-40',''),(2165,115,'Prefecture','Fukushima','JP-07',''),(2166,115,'Prefecture','Gifu','JP-21',''),(2167,115,'Prefecture','Gunma','JP-10',''),(2168,115,'Prefecture','Hiroshima','JP-34',''),(2169,115,'Prefecture','Hokkaido','JP-01',''),(2170,115,'Prefecture','Hyogo','JP-28',''),(2171,115,'Prefecture','Ibaraki','JP-08',''),(2172,115,'Prefecture','Ishikawa','JP-17',''),(2173,115,'Prefecture','Iwate','JP-03',''),(2174,115,'Prefecture','Kagawa','JP-37',''),(2175,115,'Prefecture','Kagoshima','JP-46',''),(2176,115,'Prefecture','Kanagawa','JP-14',''),(2177,115,'Prefecture','Kochi','JP-39',''),(2178,115,'Prefecture','Kumamoto','JP-43',''),(2179,115,'Prefecture','Kyoto','JP-26',''),(2180,115,'Prefecture','Mie','JP-24',''),(2181,115,'Prefecture','Miyagi','JP-04',''),(2182,115,'Prefecture','Miyazaki','JP-45',''),(2183,115,'Prefecture','Nagano','JP-20',''),(2184,115,'Prefecture','Nagasaki','JP-42',''),(2185,115,'Prefecture','Nara','JP-29',''),(2186,115,'Prefecture','Niigata','JP-15',''),(2187,115,'Prefecture','Oita','JP-44',''),(2188,115,'Prefecture','Okayama','JP-33',''),(2189,115,'Prefecture','Okinawa','JP-47',''),(2190,115,'Prefecture','Osaka','JP-27',''),(2191,115,'Prefecture','Saga','JP-41',''),(2192,115,'Prefecture','Saitama','JP-11',''),(2193,115,'Prefecture','Shiga','JP-25',''),(2194,115,'Prefecture','Shimane','JP-32',''),(2195,115,'Prefecture','Shizuoka','JP-22',''),(2196,115,'Prefecture','Tochigi','JP-09',''),(2197,115,'Prefecture','Tokushima','JP-36',''),(2198,115,'Prefecture','Tokyo','JP-13',''),(2199,115,'Prefecture','Tottori','JP-31',''),(2200,115,'Prefecture','Toyama','JP-16',''),(2201,115,'Prefecture','Wakayama','JP-30',''),(2202,115,'Prefecture','Yamagata','JP-06',''),(2203,115,'Prefecture','Yamaguchi','JP-35',''),(2204,115,'Prefecture','Yamanashi','JP-19',''),(2205,119,'Province','Nairobi Municipality','KE-110',''),(2206,119,'Province','Central','KE-200',''),(2207,119,'Province','Coast','KE-300',''),(2208,119,'Province','Eastern','KE-400',''),(2209,119,'Province','North-Eastern Kaskazini Mashariki','KE-500',''),(2210,119,'Province','Rift Valley','KE-700',''),(2211,119,'Province','Western Magharibi','KE-900',''),(2212,124,'City','Bishkek','KG-GB',''),(2213,124,'Region','Batken','KG-B',''),(2214,124,'Region','Chü','KG-C',''),(2215,124,'Region','Jalal-Abad','KG-J',''),(2216,124,'Region','Naryn','KG-N',''),(2217,124,'Region','Osh','KG-O',''),(2218,124,'Region','Talas','KG-T',''),(2219,124,'Region','Ysyk-Köl','KG-Y',''),(2220,41,'Autonomous municipality','Krong Kaeb','KH-23',''),(2221,41,'Autonomous municipality','Krong Pailin','KH-24',''),(2222,41,'Autonomous municipality','Krong Preah Sihanouk','KH-18',''),(2223,41,'Autonomous municipality','Phnom Penh','KH-12',''),(2224,41,'Province','Battambang','KH-2',''),(2225,41,'Province','Banteay Mean Chey','KH-1',''),(2226,41,'Province','Kampong Cham','KH-3',''),(2227,41,'Province','Kampong Chhnang','KH-4',''),(2228,41,'Province','Kampong Speu','KH-5',''),(2229,41,'Province','Kampong Thom','KH-6',''),(2230,41,'Province','Kampot','KH-7',''),(2231,41,'Province','Kandal','KH-8',''),(2232,41,'Province','Kach Kong','KH-9',''),(2233,41,'Province','Krachoh','KH-10',''),(2234,41,'Province','Mondol Kiri','KH-11',''),(2235,41,'Province','Otdar Mean Chey','KH-22',''),(2236,41,'Province','Pousaat','KH-15',''),(2237,41,'Province','Preah Vihear','KH-13',''),(2238,41,'Province','Prey Veaeng','KH-14',''),(2239,41,'Province','Rotanak Kiri','KH-16',''),(2240,41,'Province','Siem Reab','KH-17',''),(2241,41,'Province','Stueng Traeng','KH-19',''),(2242,41,'Province','Svaay Rieng','KH-20',''),(2243,41,'Province','Taakaev','KH-21',''),(2244,120,'Island group','Gilbert Islands','KI-G',''),(2245,120,'Island group','Line Islands','KI-L',''),(2246,120,'Island group','Phoenix Islands','KI-P',''),(2247,190,'State','Saint Kitts','KN K',''),(2248,190,'State','Nevis','KN N',''),(2249,190,'Parish','Christ Church Nichola Town','KN-01',''),(2250,190,'Parish','Saint Anne Sandy Point','KN-02',''),(2251,190,'Parish','Saint George Basseterre','KN-03',''),(2252,190,'Parish','Saint George Gingerland','KN-04',''),(2253,190,'Parish','Saint James Windward','KN-05',''),(2254,190,'Parish','Saint John Capisterre','KN-06',''),(2255,190,'Parish','Saint John Figtree','KN-07',''),(2256,190,'Parish','Saint Mary Cayon','KN-08',''),(2257,190,'Parish','Saint Paul Capisterre','KN-09',''),(2258,190,'Parish','Saint Paul Charlestown','KN-10',''),(2259,190,'Parish','Saint Peter Basseterre','KN-11',''),(2260,190,'Parish','Saint Thomas Lowland','KN-12',''),(2261,190,'Parish','Saint Thomas Middle Island','KN-13',''),(2262,190,'Parish','Trinity Palmetto Point','KN-15',''),(2263,53,'Autonomous island','Anjouan Ndzouani','KM-A',''),(2264,53,'Autonomous island','Grande Comore Ngazidja','KM-G',''),(2265,53,'Autonomous island','Mohéli Moili','KM-M',''),(2266,121,'Province','Chagang-do','KP-CHA',''),(2267,121,'Province','Hamgyongbuk-do','KP-HAB',''),(2268,121,'Province','Hamgyongnam-do','KP-HAN',''),(2269,121,'Province','Hwanghaebuk-do','KP-HWB',''),(2270,121,'Province','Hwanghaenam-do','KP-HWN',''),(2271,121,'Province','Kangwon-do','KP-KAN',''),(2272,121,'Province','Pyonganbuk-do','KP-PYB',''),(2273,121,'Province','Pyongannam-do','KP-PYN',''),(2274,121,'Province','Yanggang-do','KP-YAN',''),(2275,121,'Special city','Kaesong-si','KP-KAE',''),(2276,121,'Special city','Najin Sonbong-si','KP-NAJ',''),(2277,121,'Special city','Nampo-si','KP-NAM',''),(2278,121,'Special city','Pyongyang-si','KP-PYO',''),(2279,122,'Capital Metropolitan City','Seoul Teugbyeolsi','KR-11',''),(2280,122,'Metropolitan cities','Busan Gwang\'yeogsi','KR-26',''),(2281,122,'Metropolitan cities','Daegu Gwang\'yeogsi','KR-27',''),(2282,122,'Metropolitan cities','Daejeon Gwang\'yeogsi','KR-30',''),(2283,122,'Metropolitan cities','Gwangju Gwang\'yeogsi','KR-29',''),(2284,122,'Metropolitan cities','Incheon Gwang\'yeogsi','KR-28',''),(2285,122,'Metropolitan cities','Ulsan Gwang\'yeogsi','KR-31',''),(2286,122,'Province','Chungcheongbukdo','KR-43',''),(2287,122,'Province','Chungcheongnamdo','KR-44',''),(2288,122,'Province','Gang\'weondo','KR-42',''),(2289,122,'Province','Gyeonggido','KR-41',''),(2290,122,'Province','Gyeongsangbukdo','KR-47',''),(2291,122,'Province','Gyeongsangnamdo','KR-48',''),(2292,122,'Province','Jejudo','KR-49',''),(2293,122,'Province','Jeonrabukdo','KR-45',''),(2294,122,'Province','Jeonranamdo','KR-46',''),(2295,123,'Governorate','Al Ahmadi','KW-AH',''),(2296,123,'Governorate','Al Farw?n?yah','KW-FA',''),(2297,123,'Governorate','Al Jahrah','KW-JA',''),(2298,123,'Governorate','Al Kuwayt','KW-KU',''),(2299,123,'Governorate','Hawall?','KW-HA',''),(2300,118,'City','Almaty','KZ-ALA',''),(2301,118,'City','Astana','KZ-AST',''),(2302,118,'Region','Almaty oblysy','KZ-ALM',''),(2303,118,'Region','Aqmola oblysy','KZ-AKM',''),(2304,118,'Region','Aqtöbe oblysy','KZ-AKT',''),(2305,118,'Region','Atyra? oblysy','KZ-ATY',''),(2306,118,'Region','Batys Quzaqstan oblysy','KZ-ZAP',''),(2307,118,'Region','Mangghysta? oblysy','KZ-MAN',''),(2308,118,'Region','Ongtüstik Qazaqstan oblysy','KZ-YUZ',''),(2309,118,'Region','Pavlodar oblysy','KZ-PAV',''),(2310,118,'Region','Qaraghandy oblysy','KZ-KAR',''),(2311,118,'Region','Qostanay oblysy','KZ-KUS',''),(2312,118,'Region','Qyzylorda oblysy','KZ-KZY',''),(2313,118,'Region','Shyghys Qazaqstan oblysy','KZ-VOS',''),(2314,118,'Region','Soltüstik Quzaqstan oblysy','KZ-SEV',''),(2315,118,'Region','Zhambyl oblysy','KZ-ZHA',''),(2316,125,'Prefecture','Vientiane','LA-VT',''),(2317,125,'Province','Attapu','LA-AT',''),(2318,125,'Province','Bokèo','LA-BK',''),(2319,125,'Province','Bolikhamxai','LA-BL',''),(2320,125,'Province','Champasak','LA-CH',''),(2321,125,'Province','Houaphan','LA-HO',''),(2322,125,'Province','Khammouan','LA-KH',''),(2323,125,'Province','Louang Namtha','LA-LM',''),(2324,125,'Province','Louangphabang','LA-LP',''),(2325,125,'Province','Oudômxai','LA-OU',''),(2326,125,'Province','Phôngsali','LA-PH',''),(2327,125,'Province','Salavan','LA-SL',''),(2328,125,'Province','Savannakhét','LA-SV',''),(2329,125,'Province','Vientiane','LA-VI',''),(2330,125,'Province','Xaignabouli','LA-XA',''),(2331,125,'Province','Xékong','LA-XE',''),(2332,125,'Province','Xiangkhoang','LA-XI',''),(2333,125,'Special zone','Xiasômboun','LA-XN',''),(2334,131,'Commune','Balzers','LI-01',''),(2335,131,'Commune','Eschen','LI-02',''),(2336,131,'Commune','Gamprin','LI-03',''),(2337,131,'Commune','Mauren','LI-04',''),(2338,131,'Commune','Planken','LI-05',''),(2339,131,'Commune','Ruggell','LI-06',''),(2340,131,'Commune','Schaan','LI-07',''),(2341,131,'Commune','Schellenberg','LI-08',''),(2342,131,'Commune','Triesen','LI-09',''),(2343,131,'Commune','Triesenberg','LI-10',''),(2344,131,'Commune','Vaduz','LI-11',''),(2345,127,'Governorate','Aakkâr','LB-AK',''),(2346,127,'Governorate','Baalbek-Hermel','LB-BH',''),(2347,127,'Governorate','Béqaa','LB-BI',''),(2348,127,'Governorate','Beyrouth','LB-BA',''),(2349,127,'Governorate','Liban-Nord','LB-AS',''),(2350,127,'Governorate','Liban-Sud','LB-JA',''),(2351,127,'Governorate','Mont-Liban','LB-JL',''),(2352,127,'Governorate','Nabatîyé','LB-NA',''),(2353,212,'District','Ampara','LK-52',''),(2354,212,'District','Anuradhapura','LK-71',''),(2355,212,'District','Badulla','LK-81',''),(2356,212,'District','Batticaloa','LK-51',''),(2357,212,'District','Colombo','LK-11',''),(2358,212,'District','Galle','LK-31',''),(2359,212,'District','Gampaha','LK-12',''),(2360,212,'District','Hambantota','LK-33',''),(2361,212,'District','Jaffna','LK-41',''),(2362,212,'District','Kalutara','LK-13',''),(2363,212,'District','Kandy','LK-21',''),(2364,212,'District','Kegalla','LK-92',''),(2365,212,'District','Kilinochchi','LK-42',''),(2366,212,'District','Kurunegala','LK-61',''),(2367,212,'District','Mannar','LK-43',''),(2368,212,'District','Matale','LK-22',''),(2369,212,'District','Matara','LK-32',''),(2370,212,'District','Monaragala','LK-82',''),(2371,212,'District','Mullaittivu','LK-45',''),(2372,212,'District','Nuwara Eliya','LK-23',''),(2373,212,'District','Polonnaruwa','LK-72',''),(2374,212,'District','Puttalum','LK-62',''),(2375,212,'District','Ratnapura','LK-91',''),(2376,212,'District','Trincomalee','LK-53',''),(2377,212,'District','Vavuniya','LK-44',''),(2378,129,'County','Bomi','LR-BM',''),(2379,129,'County','Bong','LR-BG',''),(2380,129,'County','Grand Bassa','LR-GB',''),(2381,129,'County','Grand Cape Mount','LR-CM',''),(2382,129,'County','Grand Gedeh','LR-GG',''),(2383,129,'County','Grand Kru','LR-GK',''),(2384,129,'County','Lofa','LR-LO',''),(2385,129,'County','Margibi','LR-MG',''),(2386,129,'County','Maryland','LR-MY',''),(2387,129,'County','Montserrado','LR-MO',''),(2388,129,'County','Nimba','LR-NI',''),(2389,129,'County','Rivercess','LR-RI',''),(2390,129,'County','Sinoe','LR-SI',''),(2391,128,'District','Berea','LS-D',''),(2392,128,'District','Butha-Buthe','LS-B',''),(2393,128,'District','Leribe','LS-C',''),(2394,128,'District','Mafeteng','LS-E',''),(2395,128,'District','Maseru','LS-A',''),(2396,128,'District','Mohale\'s Hoek','LS-F',''),(2397,128,'District','Mokhotlong','LS-J',''),(2398,128,'District','Qacha\'s Nek','LS-H',''),(2399,128,'District','Quthing','LS-G',''),(2400,128,'District','Thaba-Tseka','LS-K',''),(2401,132,'County','Alytaus Apskritis','LT-AL',''),(2402,132,'County','Kauno Apskritis','LT-KU',''),(2403,132,'County','Klaip?dos Apskritis','LT-KL',''),(2404,132,'County','Marijampol?s Apskritis','LT-MR',''),(2405,132,'County','Panev?žio Apskritis','LT-PN',''),(2406,132,'County','Šiauli? Apskritis','LT-SA',''),(2407,132,'County','Tauragés Apskritis','LT-TA',''),(2408,132,'County','Telši? Apskritis','LT-TE',''),(2409,132,'County','Utenos Apskritis','LT-UT',''),(2410,132,'County','Vilniaus Apskritis','LT-VL',''),(2411,133,'District','Diekirch','LU-D',''),(2412,133,'District','Grevenmacher','LU-G',''),(2413,133,'District','Luxembourg','LU-L',''),(2414,126,'District','Aizkraukle','LV-AI',''),(2415,126,'District','Al?ksne','LV-AL',''),(2416,126,'District','Balvi','LV-BL',''),(2417,126,'District','Bauska','LV-BU',''),(2418,126,'District','C?sis','LV-CE',''),(2419,126,'District','Daugavpils','LV-DA',''),(2420,126,'District','Dobele','LV-DO',''),(2421,126,'District','Gulbene','LV-GU',''),(2422,126,'District','J?kabpils','LV-JK',''),(2423,126,'District','Jelgava','LV-JL',''),(2424,126,'District','Kr?slava','LV-KR',''),(2425,126,'District','Kuld?ga','LV-KU',''),(2426,126,'District','Liep?ja','LV-LE',''),(2427,126,'District','Limbaži','LV-LM',''),(2428,126,'District','Ludza','LV-LU',''),(2429,126,'District','Madona','LV-MA',''),(2430,126,'District','Ogre','LV-OG',''),(2431,126,'District','Prei?i','LV-PR',''),(2432,126,'District','R?zekne','LV-RE',''),(2433,126,'District','R?ga','LV-RI',''),(2434,126,'District','Saldus','LV-SA',''),(2435,126,'District','Talsi','LV-TA',''),(2436,126,'District','Tukums','LV-TU',''),(2437,126,'District','Valka','LV-VK',''),(2438,126,'District','Valmiera','LV-VM',''),(2439,126,'District','Ventspils','LV-VE',''),(2440,126,'City','Daugavpils','LV-DGV',''),(2441,126,'City','Jelgava','LV-JEL',''),(2442,126,'City','J?rmala','LV-JUR',''),(2443,126,'City','Liep?ja','LV-LPX',''),(2444,126,'City','R?zekne','LV-REZ',''),(2445,126,'City','R?ga','LV-RIX',''),(2446,126,'City','Ventspils','LV-VEN',''),(2447,130,'Municipality','Ajd?biy?','LY-AJ',''),(2448,130,'Municipality','Al Bu?n?n','LY-BU',''),(2449,130,'Municipality','Al ?iz?m al Akh?ar','LY-HZ',''),(2450,130,'Municipality','Al Jabal al Akh?ar','LY-JA',''),(2451,130,'Municipality','Al Jif?rah','LY-JI',''),(2452,130,'Municipality','Al Jufrah','LY-JU',''),(2453,130,'Municipality','Al Kufrah','LY-KF',''),(2454,130,'Municipality','Al Marj','LY-MJ',''),(2455,130,'Municipality','Al Marqab','LY-MB',''),(2456,130,'Municipality','Al Qa?r?n','LY-QT',''),(2457,130,'Municipality','Al Qubbah','LY-QB',''),(2458,130,'Municipality','Al W??ah','LY-WA',''),(2459,130,'Municipality','An Nuqa? al Khams','LY-NQ',''),(2460,130,'Municipality','Ash Sh??i\'','LY-SH',''),(2461,130,'Municipality','Az Z?wiyah','LY-ZA',''),(2462,130,'Municipality','Bangh?z?','LY-BA',''),(2463,130,'Municipality','Ban? Wal?d','LY-BW',''),(2464,130,'Municipality','Darnah','LY-DR',''),(2465,130,'Municipality','Ghad?mis','LY-GD',''),(2466,130,'Municipality','Ghary?n','LY-GR',''),(2467,130,'Municipality','Gh?t','LY-GT',''),(2468,130,'Municipality','Jaghb?b','LY-JB',''),(2469,130,'Municipality','Mi?r?tah','LY-MI',''),(2470,130,'Municipality','Mizdah','LY-MZ',''),(2471,130,'Municipality','Murzuq','LY-MQ',''),(2472,130,'Municipality','N?l?t','LY-NL',''),(2473,130,'Municipality','Sabh?','LY-SB',''),(2474,130,'Municipality','?abr?tah ?urm?n','LY-SS',''),(2475,130,'Municipality','Surt','LY-SR',''),(2476,130,'Municipality','T?j?r?\' wa an Naw??? al Arb??','LY-TN',''),(2477,130,'Municipality','?ar?bulus','LY-TB',''),(2478,130,'Municipality','Tarh?nah-Masall?tah','LY-TM',''),(2479,130,'Municipality','W?d? al ?ay?t','LY-WD',''),(2480,130,'Municipality','Yafran-J?d?','LY-YJ',''),(2481,154,'Economic region','Chaouia-Ouardigha','MA 09',''),(2482,154,'Economic region','Doukhala-Abda','MA 10',''),(2483,154,'Economic region','Fès-Boulemane','MA 05',''),(2484,154,'Economic region','Gharb-Chrarda-Beni Hssen','MA 02',''),(2485,154,'Economic region','Grand Casablanca','MA 08',''),(2486,154,'Economic region','Guelmim-Es Smara','MA 14',''),(2487,154,'Economic region','Laâyoune-Boujdour-Sakia el Hamra','MA 15',''),(2488,154,'Economic region','L\'Oriental','MA 04',''),(2489,154,'Economic region','Marrakech-Tensift-Al Haouz','MA 11',''),(2490,154,'Economic region','Meknès-Tafilalet','MA 06',''),(2491,154,'Economic region','Oued ed Dahab-Lagouira','MA 16',''),(2492,154,'Economic region','Rabat-Salé-Zemmour-Zaer','MA 07',''),(2493,154,'Economic region','Sous-Massa-Draa','MA 13',''),(2494,154,'Economic region','Tadla-Azilal','MA 12',''),(2495,154,'Economic region','Tanger-Tétouan','MA 01',''),(2496,154,'Economic region','Taza-Al Hoceima-Taounate','MA 03',''),(2497,154,'Province','Agadir','MA-AGD',''),(2498,154,'Province','Aït Baha','MA-BAH',''),(2499,154,'Province','Aït Melloul','MA-MEL',''),(2500,154,'Province','Al Haouz','MA-HAO',''),(2501,154,'Province','Al Hoceïma','MA-HOC',''),(2502,154,'Province','Assa-Zag','MA-ASZ',''),(2503,154,'Province','Azilal','MA-AZI',''),(2504,154,'Province','Beni Mellal','MA-BEM',''),(2505,154,'Province','Ben Sllmane','MA-BES',''),(2506,154,'Province','Berkane','MA-BER',''),(2507,154,'Province','Boujdour (EH)','MA-BOD',''),(2508,154,'Province','Boulemane','MA-BOM',''),(2509,154,'Province','Casablanca [Dar el Beïda]','MA-CAS',''),(2510,154,'Province','Chefchaouene','MA-CHE',''),(2511,154,'Province','Chichaoua','MA-CHI',''),(2512,154,'Province','El Hajeb','MA-HAJ',''),(2513,154,'Province','El Jadida','MA-JDI',''),(2514,154,'Province','Errachidia','MA-ERR',''),(2515,154,'Province','Essaouira','MA-ESI',''),(2516,154,'Province','Es Smara (EH)','MA-ESM',''),(2517,154,'Province','Fès','MA-FES',''),(2518,154,'Province','Figuig','MA-FIG',''),(2519,154,'Province','Guelmim','MA-GUE',''),(2520,154,'Province','Ifrane','MA-IFR',''),(2521,154,'Province','Jerada','MA-JRA',''),(2522,154,'Province','Kelaat Sraghna','MA-KES',''),(2523,154,'Province','Kénitra','MA-KEN',''),(2524,154,'Province','Khemisaet','MA-KHE',''),(2525,154,'Province','Khenifra','MA-KHN',''),(2526,154,'Province','Khouribga','MA-KHO',''),(2527,154,'Province','Laâyoune (EH)','MA-LAA',''),(2528,154,'Province','Larache','MA-LAP',''),(2529,154,'Province','Marrakech','MA-MAR',''),(2530,154,'Province','Meknsès','MA-MEK',''),(2531,154,'Province','Nador','MA-NAD',''),(2532,154,'Province','Ouarzazate','MA-OUA',''),(2533,154,'Province','Oued ed Dahab (EH)','MA-OUD',''),(2534,154,'Province','Oujda','MA-OUJ',''),(2535,154,'Province','Rabat-Salé','MA-RBA',''),(2536,154,'Province','Safi','MA-SAF',''),(2537,154,'Province','Sefrou','MA-SEF',''),(2538,154,'Province','Settat','MA-SET',''),(2539,154,'Province','Sidl Kacem','MA-SIK',''),(2540,154,'Province','Tanger','MA-TNG',''),(2541,154,'Province','Tan-Tan','MA-TNT',''),(2542,154,'Province','Taounate','MA-TAO',''),(2543,154,'Province','Taroudannt','MA-TAR',''),(2544,154,'Province','Tata','MA-TAT',''),(2545,154,'Province','Taza','MA-TAZ',''),(2546,154,'Province','Tétouan','MA-TET',''),(2547,154,'Province','Tiznit','MA-TIZ',''),(2548,149,'Autonomous territory','G?g?uzia, Unitate Teritorial? Autonom?','MD-GA',''),(2549,149,'City','Chi?in?u','MD-CU',''),(2550,149,'District','B?l?i','MD-BA',''),(2551,149,'District','Cahul','MD-CA',''),(2552,149,'District','Chi?in?u','MD-CH',''),(2553,149,'District','Edine?','MD-ED',''),(2554,149,'District','L?pu?na','MD-LA',''),(2555,149,'District','Orhei','MD-OR',''),(2556,149,'District','Soroca','MD-SO',''),(2557,149,'District','Taraclia','MD-TA',''),(2558,149,'District','Tighina','MD-TI',''),(2559,149,'District','Ungheni','MD-UN',''),(2560,149,'Territorial unit','Stînga Nistrului, unitatea teritorial? din','MD-SN',''),(2561,152,'Municipality','Andrijevica','ME-01',''),(2562,152,'Municipality','Bar','ME-02',''),(2563,152,'Municipality','Berane','ME-03',''),(2564,152,'Municipality','Bijelo Polje','ME-04',''),(2565,152,'Municipality','Budva','ME-05',''),(2566,152,'Municipality','Cetinje','ME-06',''),(2567,152,'Municipality','Danilovgrad','ME-07',''),(2568,152,'Municipality','Herceg-Novi','ME-08',''),(2569,152,'Municipality','Kolašin','ME-09',''),(2570,152,'Municipality','Kotor','ME-10',''),(2571,152,'Municipality','Mojkovac','ME-11',''),(2572,152,'Municipality','Nikši?','ME-12',''),(2573,152,'Municipality','Plav','ME-13',''),(2574,152,'Municipality','Pljevlja','ME-14',''),(2575,152,'Municipality','Plužine','ME-15',''),(2576,152,'Municipality','Podgorica','ME-16',''),(2577,152,'Municipality','Rožaje','ME-17',''),(2578,152,'Municipality','Šavnik','ME-18',''),(2579,152,'Municipality','Tivat','ME-19',''),(2580,152,'Municipality','Ulcinj','ME-20',''),(2581,152,'Municipality','Žabljak','ME-21',''),(2582,136,'Autonomous province','Antananarivo','MG-T',''),(2583,136,'Autonomous province','Antsiranana','MG-D',''),(2584,136,'Autonomous province','Fianarantsoa','MG-F',''),(2585,136,'Autonomous province','Mahajanga','MG-M',''),(2586,136,'Autonomous province','Toamasina','MG-A',''),(2587,136,'Autonomous province','Toliara','MG-U',''),(2588,142,'Municipality','Ailinglapalap','MH-ALL',''),(2589,142,'Municipality','Ailuk','MH-ALK',''),(2590,142,'Municipality','Arno','MH-ARN',''),(2591,142,'Municipality','Aur','MH-AUR',''),(2592,142,'Municipality','Ebon','MH-EBO',''),(2593,142,'Municipality','Eniwetok','MH-ENI',''),(2594,142,'Municipality','Jaluit','MH-JAL',''),(2595,142,'Municipality','Kili','MH-KIL',''),(2596,142,'Municipality','Kwajalein','MH-KWA',''),(2597,142,'Municipality','Lae','MH-LAE',''),(2598,142,'Municipality','Lib','MH-LIB',''),(2599,142,'Municipality','Likiep','MH-LIK',''),(2600,142,'Municipality','Majuro','MH-MAJ',''),(2601,142,'Municipality','Maloelap','MH-MAL',''),(2602,142,'Municipality','Mejit','MH-MEJ',''),(2603,142,'Municipality','Mili','MH-MIL',''),(2604,142,'Municipality','Namorik','MH-NMK',''),(2605,142,'Municipality','Namu','MH-NMU',''),(2606,142,'Municipality','Rongelap','MH-RON',''),(2607,142,'Municipality','Ujae','MH-UJA',''),(2608,142,'Municipality','Ujelang','MH-UJL',''),(2609,142,'Municipality','Utirik','MH-UTI',''),(2610,142,'Municipality','Wotho','MH-WTN',''),(2611,142,'Municipality','Wotje','MH-WTJ',''),(2612,135,'Municipality','Aerodrom','MK-01',''),(2613,135,'Municipality','Ara?inovo','MK-02',''),(2614,135,'Municipality','Berovo','MK-03',''),(2615,135,'Municipality','Bitola','MK-04',''),(2616,135,'Municipality','Bogdanci','MK-05',''),(2617,135,'Municipality','Bogovinje','MK-06',''),(2618,135,'Municipality','Bosilovo','MK-07',''),(2619,135,'Municipality','Brvenica','MK-08',''),(2620,135,'Municipality','Butel','MK-09',''),(2621,135,'Municipality','Centar','MK-77',''),(2622,135,'Municipality','Centar Župa','MK-78',''),(2623,135,'Municipality','?air','MK-79',''),(2624,135,'Municipality','?aška','MK-80',''),(2625,135,'Municipality','?ešinovo-Obleševo','MK-81',''),(2626,135,'Municipality','?u?er Sandevo','MK-82',''),(2627,135,'Municipality','Debar','MK-21',''),(2628,135,'Municipality','Debarca','MK-22',''),(2629,135,'Municipality','Del?evo','MK-23',''),(2630,135,'Municipality','Demir Hisar','MK-25',''),(2631,135,'Municipality','Demir Kapija','MK-24',''),(2632,135,'Municipality','Dojran','MK-26',''),(2633,135,'Municipality','Dolneni','MK-27',''),(2634,135,'Municipality','Drugovo','MK-28',''),(2635,135,'Municipality','Gazi Baba','MK-17',''),(2636,135,'Municipality','Gevgelija','MK-18',''),(2637,135,'Municipality','Gjor?e Petrov','MK-29',''),(2638,135,'Municipality','Gostivar','MK-19',''),(2639,135,'Municipality','Gradsko','MK-20',''),(2640,135,'Municipality','Ilinden','MK-34',''),(2641,135,'Municipality','Jegunovce','MK-35',''),(2642,135,'Municipality','Karbinci','MK-37',''),(2643,135,'Municipality','Karpoš','MK-38',''),(2644,135,'Municipality','Kavadarci','MK-36',''),(2645,135,'Municipality','Ki?evo','MK-40',''),(2646,135,'Municipality','Kisela Voda','MK-39',''),(2647,135,'Municipality','Ko?ani','MK-42',''),(2648,135,'Municipality','Kon?e','MK-41',''),(2649,135,'Municipality','Kratovo','MK-43',''),(2650,135,'Municipality','Kriva Palanka','MK-44',''),(2651,135,'Municipality','Krivogaštani','MK-45',''),(2652,135,'Municipality','Kruševo','MK-46',''),(2653,135,'Municipality','Kumanovo','MK-47',''),(2654,135,'Municipality','Lipkovo','MK-48',''),(2655,135,'Municipality','Lozovo','MK-49',''),(2656,135,'Municipality','Makedonska Kamenica','MK-51',''),(2657,135,'Municipality','Makedonski Brod','MK-52',''),(2658,135,'Municipality','Mavrovo-i-Rostuša','MK-50',''),(2659,135,'Municipality','Mogila','MK-53',''),(2660,135,'Municipality','Negotino','MK-54',''),(2661,135,'Municipality','Novaci','MK-55',''),(2662,135,'Municipality','Novo Selo','MK-56',''),(2663,135,'Municipality','Ohrid','MK-58',''),(2664,135,'Municipality','Oslomej','MK-57',''),(2665,135,'Municipality','Peh?evo','MK-60',''),(2666,135,'Municipality','Petrovec','MK-59',''),(2667,135,'Municipality','Plasnica','MK-61',''),(2668,135,'Municipality','Prilep','MK-62',''),(2669,135,'Municipality','Probištip','MK-63',''),(2670,135,'Municipality','Radoviš','MK-64',''),(2671,135,'Municipality','Rankovce','MK-65',''),(2672,135,'Municipality','Resen','MK-66',''),(2673,135,'Municipality','Rosoman','MK-67',''),(2674,135,'Municipality','Saraj','MK-68',''),(2675,135,'Municipality','Štip','MK-83',''),(2676,135,'Municipality','Šuto Orizari','MK-84',''),(2677,135,'Municipality','Sopište','MK-70',''),(2678,135,'Municipality','Staro Nagori?ane','MK-71',''),(2679,135,'Municipality','Struga','MK-72',''),(2680,135,'Municipality','Strumica','MK-73',''),(2681,135,'Municipality','Studeni?ani','MK-74',''),(2682,135,'Municipality','Sveti Nikole','MK-69',''),(2683,135,'Municipality','Tearce','MK-75',''),(2684,135,'Municipality','Tetovo','MK-76',''),(2685,135,'Municipality','Valandovo','MK-10',''),(2686,135,'Municipality','Vasilevo','MK-11',''),(2687,135,'Municipality','Veles','MK-13',''),(2688,135,'Municipality','Vev?ani','MK-12',''),(2689,135,'Municipality','Vinica','MK-14',''),(2690,135,'Municipality','Vraneštica','MK-15',''),(2691,135,'Municipality','Vrap?ište','MK-16',''),(2692,135,'Municipality','Zajas','MK-31',''),(2693,135,'Municipality','Zelenikovo','MK-32',''),(2694,135,'Municipality','Želino','MK-30',''),(2695,135,'Municipality','Zrnovci','MK-33',''),(2696,140,'District','Bamako','ML-BK0',''),(2697,140,'Region','Gao','ML-7',''),(2698,140,'Region','Kayes','ML-1',''),(2699,140,'Region','Kidal','ML-8',''),(2700,140,'Region','Koulikoro','ML-2',''),(2701,140,'Region','Mopti','ML-5',''),(2702,140,'Region','Ségou','ML-4',''),(2703,140,'Region','Sikasso','ML-3',''),(2704,140,'Region','Tombouctou','ML-6',''),(2705,156,'Division','Ayeyarwady','MM-07',''),(2706,156,'Division','Bago','MM-02',''),(2707,156,'Division','Magway','MM-03',''),(2708,156,'Division','Mandalay','MM-04',''),(2709,156,'Division','Sagaing','MM-01',''),(2710,156,'Division','Tanintharyi','MM-05',''),(2711,156,'Division','Yangon','MM-06',''),(2712,156,'State','Chin','MM-14',''),(2713,156,'State','Kachin','MM-11',''),(2714,156,'State','Kayah','MM-12',''),(2715,156,'State','Kayin','MM-13',''),(2716,156,'State','Mon','MM-15',''),(2717,156,'State','Rakhine','MM-16',''),(2718,156,'State','Shan','MM-17',''),(2719,151,'Province','Arhangay','MN-073',''),(2720,151,'Province','Bayanhongor','MN-069',''),(2721,151,'Province','Bayan-Ölgiy','MN-071',''),(2722,151,'Province','Bulgan','MN-067',''),(2723,151,'Province','Dornod','MN-061',''),(2724,151,'Province','Dornogovi','MN-063',''),(2725,151,'Province','Dundgovi','MN-059',''),(2726,151,'Province','Dzavhan','MN-057',''),(2727,151,'Province','Govi-Altay','MN-065',''),(2728,151,'Province','Hentiy','MN-039',''),(2729,151,'Province','Hovd','MN-043',''),(2730,151,'Province','Hövsgöl','MN-041',''),(2731,151,'Province','Ömnögovi','MN-053',''),(2732,151,'Province','Övörhangay','MN-055',''),(2733,151,'Province','Selenge','MN-049',''),(2734,151,'Province','Sühbaatar','MN-051',''),(2735,151,'Province','Töv','MN-047',''),(2736,151,'Province','Uvs','MN-046',''),(2737,151,'Municipality','Ulanbaatar','MN-1',''),(2738,151,'Municipality','Darhan uul','MN-037',''),(2739,151,'Municipality','Govi-Sumber','MN-064',''),(2740,151,'Municipality','Orhon','MN-035',''),(2741,144,'District','Nouakchott','MR-NKC',''),(2742,144,'Region','Adrar','MR-07',''),(2743,144,'Region','Assaba','MR-03',''),(2744,144,'Region','Brakna','MR-05',''),(2745,144,'Region','Dakhlet Nouadhibou','MR-08',''),(2746,144,'Region','Gorgol','MR-04',''),(2747,144,'Region','Guidimaka','MR-10',''),(2748,144,'Region','Hodh ech Chargui','MR-01',''),(2749,144,'Region','Hodh el Charbi','MR-02',''),(2750,144,'Region','Inchiri','MR-12',''),(2751,144,'Region','Tagant','MR-09',''),(2752,144,'Region','Tiris Zemmour','MR-11',''),(2753,144,'Region','Trarza','MR-06',''),(2754,141,'Local council','Attard','MT-01',''),(2755,141,'Local council','Balzan','MT-02',''),(2756,141,'Local council','Birgu','MT-03',''),(2757,141,'Local council','Birkirkara','MT-04',''),(2758,141,'Local council','Bir?ebbu?a','MT-05',''),(2759,141,'Local council','Bormla','MT-06',''),(2760,141,'Local council','Dingli','MT-07',''),(2761,141,'Local council','Fgura','MT-08',''),(2762,141,'Local council','Floriana','MT-09',''),(2763,141,'Local council','Fontana','MT-10',''),(2764,141,'Local council','Gudja','MT-11',''),(2765,141,'Local council','G?ira','MT-12',''),(2766,141,'Local council','G?ajnsielem','MT-13',''),(2767,141,'Local council','G?arb','MT-14',''),(2768,141,'Local council','G?arg?ur','MT-15',''),(2769,141,'Local council','G?asri','MT-16',''),(2770,141,'Local council','G?axaq','MT-17',''),(2771,141,'Local council','?amrun','MT-18',''),(2772,141,'Local council','Iklin','MT-19',''),(2773,141,'Local council','Isla','MT-20',''),(2774,141,'Local council','Kalkara','MT-21',''),(2775,141,'Local council','Ker?em','MT-22',''),(2776,141,'Local council','Kirkop','MT-23',''),(2777,141,'Local council','Lija','MT-24',''),(2778,141,'Local council','Luqa','MT-25',''),(2779,141,'Local council','Marsa','MT-26',''),(2780,141,'Local council','Marsaskala','MT-27',''),(2781,141,'Local council','Marsaxlokk','MT-28',''),(2782,141,'Local council','Mdina','MT-29',''),(2783,141,'Local council','Mellie?a','MT-30',''),(2784,141,'Local council','M?arr','MT-31',''),(2785,141,'Local council','Mosta','MT-32',''),(2786,141,'Local council','Mqabba','MT-33',''),(2787,141,'Local council','Msida','MT-34',''),(2788,141,'Local council','Mtarfa','MT-35',''),(2789,141,'Local council','Munxar','MT-36',''),(2790,141,'Local council','Nadur','MT-37',''),(2791,141,'Local council','Naxxar','MT-38',''),(2792,141,'Local council','Paola','MT-39',''),(2793,141,'Local council','Pembroke','MT-40',''),(2794,141,'Local council','Pietà','MT-41',''),(2795,141,'Local council','Qala','MT-42',''),(2796,141,'Local council','Qormi','MT-43',''),(2797,141,'Local council','Qrendi','MT-44',''),(2798,141,'Local council','Rabat G?awdex','MT-45',''),(2799,141,'Local council','Rabat Malta','MT-46',''),(2800,141,'Local council','Safi','MT-47',''),(2801,141,'Local council','San ?iljan','MT-48',''),(2802,141,'Local council','San ?wann','MT-49',''),(2803,141,'Local council','San Lawrenz','MT-50',''),(2804,141,'Local council','San Pawl il-Ba?ar','MT-51',''),(2805,141,'Local council','Sannat','MT-52',''),(2806,141,'Local council','Santa Lu?ija','MT-53',''),(2807,141,'Local council','Santa Venera','MT-54',''),(2808,141,'Local council','Si??iewi','MT-55',''),(2809,141,'Local council','Sliema','MT-56',''),(2810,141,'Local council','Swieqi','MT-57',''),(2811,141,'Local council','Ta’ Xbiex','MT-58',''),(2812,141,'Local council','Tarxien','MT-59',''),(2813,141,'Local council','Valletta','MT-60',''),(2814,141,'Local council','Xag?ra','MT-61',''),(2815,141,'Local council','Xewkija','MT-62',''),(2816,141,'Local council','Xg?ajra','MT-63',''),(2817,141,'Local council','?abbar','MT-64',''),(2818,141,'Local council','?ebbu? G?awdex','MT-65',''),(2819,141,'Local council','?ebbu? Malta','MT-66',''),(2820,141,'Local council','?ejtun','MT-67',''),(2821,141,'Local council','?urrieq','MT-68',''),(2822,145,'City','Beau Bassin-Rose Hill','MU-BR',''),(2823,145,'City','Curepipe','MU-CU',''),(2824,145,'City','Port Louis','MU-PU',''),(2825,145,'City','Quatre Bornes','MU-QB',''),(2826,145,'City','Vacoas-Phoenix','MU-VP',''),(2827,145,'Dependency','Agalega Islands','MU-AG',''),(2828,145,'Dependency','Cargados Carajos Shoals','MU-CC',''),(2829,145,'Dependency','Rodrigues Island','MU-RO',''),(2830,145,'District','Black River','MU-BL',''),(2831,145,'District','Flacq','MU-FL',''),(2832,145,'District','Grand Port','MU-GP',''),(2833,145,'District','Moka','MU-MO',''),(2834,145,'District','Pamplemousses','MU-PA',''),(2835,145,'District','Plaines Wilhems','MU-PW',''),(2836,145,'District','Port Louis','MU-PL',''),(2837,145,'District','Rivière du Rempart','MU-RP',''),(2838,145,'District','Savanne','MU-SA',''),(2839,139,'City','Male','MV-MLE',''),(2840,139,'Atoll','Alif','MV-02',''),(2841,139,'Atoll','Baa','MV-20',''),(2842,139,'Atoll','Dhaalu','MV-17',''),(2843,139,'Atoll','Faafu','MV-14',''),(2844,139,'Atoll','Gaafu Aliff','MV-27',''),(2845,139,'Atoll','Gaafu Daalu','MV-28',''),(2846,139,'Atoll','Gnaviyani','MV-29',''),(2847,139,'Atoll','Haa Alif','MV-07',''),(2848,139,'Atoll','Haa Dhaalu','MV-23',''),(2849,139,'Atoll','Kaafu','MV-26',''),(2850,139,'Atoll','Laamu','MV-05',''),(2851,139,'Atoll','Lhaviyani','MV-03',''),(2852,139,'Atoll','Meemu','MV-12',''),(2853,139,'Atoll','Noonu','MV-25',''),(2854,139,'Atoll','Raa','MV-13',''),(2855,139,'Atoll','Seenu','MV-01',''),(2856,139,'Atoll','Shaviyani','MV-24',''),(2857,139,'Atoll','Thaa','MV-08',''),(2858,139,'Atoll','Vaavu','MV-04',''),(2859,137,'Region','Central','MW C',''),(2860,137,'Region','Northern','MW N',''),(2861,137,'Region','Southern (Malawi)','MW S',''),(2862,137,'District','Balaka','MW-BA',''),(2863,137,'District','Blantyre','MW-BL',''),(2864,137,'District','Chikwawa','MW-CK',''),(2865,137,'District','Chiradzulu','MW-CR',''),(2866,137,'District','Chitipa','MW-CT',''),(2867,137,'District','Dedza','MW-DE',''),(2868,137,'District','Dowa','MW-DO',''),(2869,137,'District','Karonga','MW-KR',''),(2870,137,'District','Kasungu','MW-KS',''),(2871,137,'District','Likoma Island','MW-LK',''),(2872,137,'District','Lilongwe','MW-LI',''),(2873,137,'District','Machinga','MW-MH',''),(2874,137,'District','Mangochi','MW-MG',''),(2875,137,'District','Mchinji','MW-MC',''),(2876,137,'District','Mulanje','MW-MU',''),(2877,137,'District','Mwanza','MW-MW',''),(2878,137,'District','Mzimba','MW-MZ',''),(2879,137,'District','Nkhata Bay','MW-NB',''),(2880,137,'District','Nkhotakota','MW-NK',''),(2881,137,'District','Nsanje','MW-NS',''),(2882,137,'District','Ntcheu','MW-NU',''),(2883,137,'District','Ntchisi','MW-NI',''),(2884,137,'District','Phalombe','MW-PH',''),(2885,137,'District','Rumphi','MW-RU',''),(2886,137,'District','Salima','MW-SA',''),(2887,137,'District','Thyolo','MW-TH',''),(2888,137,'District','Zomba','MW-ZO',''),(2889,147,'Federal district','Distrito Federal','MX-DIF',''),(2890,147,'State','Aguascalientes','MX-AGU',''),(2891,147,'State','Baja California','MX-BCN',''),(2892,147,'State','Baja California Sur','MX-BCS',''),(2893,147,'State','Campeche','MX-CAM',''),(2894,147,'State','Coahuila','MX-COA',''),(2895,147,'State','Colima','MX-COL',''),(2896,147,'State','Chiapas','MX-CHP',''),(2897,147,'State','Chihuahua','MX-CHH',''),(2898,147,'State','Durango','MX-DUR',''),(2899,147,'State','Guanajuato','MX-GUA',''),(2900,147,'State','Guerrero','MX-GRO',''),(2901,147,'State','Hidalgo','MX-HID',''),(2902,147,'State','Jalisco','MX-JAL',''),(2903,147,'State','México','MX-MEX',''),(2904,147,'State','Michoacán','MX-MIC',''),(2905,147,'State','Morelos','MX-MOR',''),(2906,147,'State','Nayarit','MX-NAY',''),(2907,147,'State','Nuevo León','MX-NLE',''),(2908,147,'State','Oaxaca','MX-OAX',''),(2909,147,'State','Puebla','MX-PUE',''),(2910,147,'State','Querétaro','MX-QUE',''),(2911,147,'State','Quintana Roo','MX-ROO',''),(2912,147,'State','San Luis Potosí','MX-SLP',''),(2913,147,'State','Sinaloa','MX-SIN',''),(2914,147,'State','Sonora','MX-SON',''),(2915,147,'State','Tabasco','MX-TAB',''),(2916,147,'State','Tamaulipas','MX-TAM',''),(2917,147,'State','Tlaxcala','MX-TLA',''),(2918,147,'State','Veracruz','MX-VER',''),(2919,147,'State','Yucatán','MX-YUC',''),(2920,147,'State','Zacatecas','MX-ZAC',''),(2921,138,'Federal Territories','Wilayah Persekutuan Kuala Lumpur','MY-14',''),(2922,138,'Federal Territories','Wilayah Persekutuan Labuan','MY-15',''),(2923,138,'Federal Territories','Wilayah Persekutuan Putrajaya','MY-16',''),(2924,138,'State','Johor','MY-01',''),(2925,138,'State','Kedah','MY-02',''),(2926,138,'State','Kelantan','MY-03',''),(2927,138,'State','Melaka','MY-04',''),(2928,138,'State','Negeri Sembilan','MY-05',''),(2929,138,'State','Pahang','MY-06',''),(2930,138,'State','Perak','MY-08',''),(2931,138,'State','Perlis','MY-09',''),(2932,138,'State','Pulau Pinang','MY-07',''),(2933,138,'State','Sabah','MY-12',''),(2934,138,'State','Sarawak','MY-13',''),(2935,138,'State','Selangor','MY-10',''),(2936,138,'State','Terengganu','MY-11',''),(2937,155,'City','Maputo (city)','MZ-MPM',''),(2938,155,'Province','Cabo Delgado','MZ-P',''),(2939,155,'Province','Gaza','MZ-G',''),(2940,155,'Province','Inhambane','MZ-I',''),(2941,155,'Province','Manica','MZ-B',''),(2942,155,'Province','Maputo','MZ-L',''),(2943,155,'Province','Numpula','MZ-N',''),(2944,155,'Province','Niassa','MZ-A',''),(2945,155,'Province','Sofala','MZ-S',''),(2946,155,'Province','Tete','MZ-T',''),(2947,155,'Province','Zambezia','MZ-Q',''),(2948,157,'Region','Caprivi','NA-CA',''),(2949,157,'Region','Erongo','NA-ER',''),(2950,157,'Region','Hardap','NA-HA',''),(2951,157,'Region','Karas','NA-KA',''),(2952,157,'Region','Khomas','NA-KH',''),(2953,157,'Region','Kunene','NA-KU',''),(2954,157,'Region','Ohangwena','NA-OW',''),(2955,157,'Region','Okavango','NA-OK',''),(2956,157,'Region','Omaheke','NA-OH',''),(2957,157,'Region','Omusati','NA-OS',''),(2958,157,'Region','Oshana','NA-ON',''),(2959,157,'Region','Oshikoto','NA-OT',''),(2960,157,'Region','Otjozondjupa','NA-OD',''),(2961,164,'Capital District','Niamey','NE-8',''),(2962,164,'Department','Agadez','NE-1',''),(2963,164,'Department','Diffa','NE-2',''),(2964,164,'Department','Dosso','NE-3',''),(2965,164,'Department','Maradi','NE-4',''),(2966,164,'Department','Tahoua','NE-5',''),(2967,164,'Department','Tillabéri','NE-6',''),(2968,164,'Department','Zinder','NE-7',''),(2969,165,'Capital Territory','Abuja Capital Territory','NG-FC',''),(2970,165,'State','Abia','NG-AB',''),(2971,165,'State','Adamawa','NG-AD',''),(2972,165,'State','Akwa Ibom','NG-AK',''),(2973,165,'State','Anambra','NG-AN',''),(2974,165,'State','Bauchi','NG-BA',''),(2975,165,'State','Bayelsa','NG-BY',''),(2976,165,'State','Benue','NG-BE',''),(2977,165,'State','Borno','NG-BO',''),(2978,165,'State','Cross River','NG-CR',''),(2979,165,'State','Delta','NG-DE',''),(2980,165,'State','Ebonyi','NG-EB',''),(2981,165,'State','Edo','NG-ED',''),(2982,165,'State','Ekiti','NG-EK',''),(2983,165,'State','Enugu','NG-EN',''),(2984,165,'State','Gombe','NG-GO',''),(2985,165,'State','Imo','NG-IM',''),(2986,165,'State','Jigawa','NG-JI',''),(2987,165,'State','Kaduna','NG-KD',''),(2988,165,'State','Kano','NG-KN',''),(2989,165,'State','Katsina','NG-KT',''),(2990,165,'State','Kebbi','NG-KE',''),(2991,165,'State','Kogi','NG-KO',''),(2992,165,'State','Kwara','NG-KW',''),(2993,165,'State','Lagos','NG-LA',''),(2994,165,'State','Nassarawa','NG-NA',''),(2995,165,'State','Niger','NG-NI',''),(2996,165,'State','Ogun','NG-OG',''),(2997,165,'State','Ondo','NG-ON',''),(2998,165,'State','Osun','NG-OS',''),(2999,165,'State','Oyo','NG-OY',''),(3000,165,'State','Plateau','NG-PL',''),(3001,165,'State','Rivers','NG-RI',''),(3002,165,'State','Sokoto','NG-SO',''),(3003,165,'State','Taraba','NG-TA',''),(3004,165,'State','Yobe','NG-YO',''),(3005,165,'State','Zamfara','NG-ZA',''),(3006,163,'Department','Boaco','NI-BO',''),(3007,163,'Department','Carazo','NI-CA',''),(3008,163,'Department','Chinandega','NI-CI',''),(3009,163,'Department','Chontales','NI-CO',''),(3010,163,'Department','Estelí','NI-ES',''),(3011,163,'Department','Granada','NI-GR',''),(3012,163,'Department','Jinotega','NI-JI',''),(3013,163,'Department','León','NI-LE',''),(3014,163,'Department','Madriz','NI-MD',''),(3015,163,'Department','Managua','NI-MN',''),(3016,163,'Department','Masaya','NI-MS',''),(3017,163,'Department','Matagalpa','NI-MT',''),(3018,163,'Department','Nueva Segovia','NI-NS',''),(3019,163,'Department','Río San Juan','NI-SJ',''),(3020,163,'Department','Rivas','NI-RI',''),(3021,163,'Autonomous Region','Atlántico Norte','NI-AN',''),(3022,163,'Autonomous Region','Atlántico Sur','NI-AS',''),(3023,160,'Province','Drenthe','NL-DR',''),(3024,160,'Province','Flevoland','NL-FL',''),(3025,160,'Province','Friesland','NL-FR',''),(3026,160,'Province','Gelderland','NL-GE',''),(3027,160,'Province','Groningen','NL-GR',''),(3028,160,'Province','Limburg','NL-LI',''),(3029,160,'Province','Noord-Brabant','NL-NB',''),(3030,160,'Province','Noord-Holland','NL-NH',''),(3031,160,'Province','Overijssel','NL-OV',''),(3032,160,'Province','Utrecht','NL-UT',''),(3033,160,'Province','Zeeland','NL-ZE',''),(3034,160,'Province','Zuid-Holland','NL-ZH',''),(3035,169,'County','Akershus','NO-02',''),(3036,169,'County','Aust-Agder','NO-09',''),(3037,169,'County','Buskerud','NO-06',''),(3038,169,'County','Finnmark','NO-20',''),(3039,169,'County','Hedmark','NO-04',''),(3040,169,'County','Hordaland','NO-12',''),(3041,169,'County','Møre og Romsdal','NO-15',''),(3042,169,'County','Nordland','NO-18',''),(3043,169,'County','Nord-Trøndelag','NO-17',''),(3044,169,'County','Oppland','NO-05',''),(3045,169,'County','Oslo','NO-03',''),(3046,169,'County','Rogaland','NO-11',''),(3047,169,'County','Sogn og Fjordane','NO-14',''),(3048,169,'County','Sør-Trøndelag','NO-16',''),(3049,169,'County','Telemark','NO-08',''),(3050,169,'County','Troms','NO-19',''),(3051,169,'County','Vest-Agder','NO-10',''),(3052,169,'County','Vestfold','NO-07',''),(3053,169,'County','Østfold','NO-01',''),(3054,169,'County','Jan Mayen','NO-22',''),(3055,169,'County','Svalbard','NO-21',''),(3056,158,'District','Aiwo','NR-01',''),(3057,158,'District','Anabar','NR-02',''),(3058,158,'District','Anetan','NR-03',''),(3059,158,'District','Anibare','NR-04',''),(3060,158,'District','Baiti','NR-05',''),(3061,158,'District','Boe','NR-06',''),(3062,158,'District','Buada','NR-07',''),(3063,158,'District','Denigomodu','NR-08',''),(3064,158,'District','Ewa','NR-09',''),(3065,158,'District','Ijuw','NR-10',''),(3066,158,'District','Meneng','NR-11',''),(3067,158,'District','Nibok','NR-12',''),(3068,158,'District','Uaboe','NR-13',''),(3069,158,'District','Yaren','NR-14',''),(3070,162,'Regional council','Auckland','NZ-AUK',''),(3071,162,'Regional council','Bay of Plenty','NZ-BOP',''),(3072,162,'Regional council','Canterbury','NZ-CAN',''),(3073,162,'Regional council','Hawkes Bay','NZ-HKB',''),(3074,162,'Regional council','Manawatu-Wanganui','NZ-MWT',''),(3075,162,'Regional council','Northland','NZ-NTL',''),(3076,162,'Regional council','Otago','NZ-OTA',''),(3077,162,'Regional council','Southland','NZ-STL',''),(3078,162,'Regional council','Taranaki','NZ-TKI',''),(3079,162,'Regional council','Waikato','NZ-WKO',''),(3080,162,'Regional council','Wellington','NZ-WGN',''),(3081,162,'Regional council','West Coast','NZ-WTC',''),(3082,162,'Unitary authority','Gisborne','NZ-GIS',''),(3083,162,'Unitary authority','Marlborough','NZ-MBH',''),(3084,162,'Unitary authority','Nelson','NZ-NSN',''),(3085,162,'Unitary authority','Tasman','NZ-TAS',''),(3086,170,'Region','Ad Dakhillyah','OM-DA',''),(3087,170,'Region','Al Batinah','OM-BA',''),(3088,170,'Region','Al Wusta','OM-WU',''),(3089,170,'Region','Ash Sharqlyah','OM-SH',''),(3090,170,'Region','Az Zahirah','OM-ZA',''),(3091,170,'Governorate','Al Janblyah','OM-JA',''),(3092,170,'Governorate','Masqat','OM-MA',''),(3093,170,'Governorate','Musandam','OM-MU',''),(3094,174,'Province','Bocas del Toro','PA-1',''),(3095,174,'Province','Coclé','PA-2',''),(3096,174,'Province','Colón','PA-3',''),(3097,174,'Province','Chiriquí','PA-4',''),(3098,174,'Province','Darién','PA-5',''),(3099,174,'Province','Herrera','PA-6',''),(3100,174,'Province','Los Santos','PA-7',''),(3101,174,'Province','Panamá','PA-8',''),(3102,174,'Province','Veraguas','PA-9',''),(3103,174,'Province','Kuna Yala','PA-0',''),(3104,177,'Region','El Callao','PE-CAL',''),(3105,177,'Region','Amazonas','PE-AMA',''),(3106,177,'Region','Ancash','PE-ANC',''),(3107,177,'Region','Apurímac','PE-APU',''),(3108,177,'Region','Arequipa','PE-ARE',''),(3109,177,'Region','Ayacucho','PE-AYA',''),(3110,177,'Region','Cajamarca','PE-CAJ',''),(3111,177,'Region','Cusco','PE-CUS',''),(3112,177,'Region','Huancavelica','PE-HUV',''),(3113,177,'Region','Huánuco','PE-HUC',''),(3114,177,'Region','Ica','PE-ICA',''),(3115,177,'Region','Junín','PE-JUN',''),(3116,177,'Region','La Libertad','PE-LAL',''),(3117,177,'Region','Lambayeque','PE-LAM',''),(3118,177,'Region','Lima','PE-LIM',''),(3119,177,'Region','Loreto','PE-LOR',''),(3120,177,'Region','Madre de Dios','PE-MDD',''),(3121,177,'Region','Moquegua','PE-MOQ',''),(3122,177,'Region','Pasco','PE-PAS',''),(3123,177,'Region','Piura','PE-PIU',''),(3124,177,'Region','Puno','PE-PUN',''),(3125,177,'Region','San Martín','PE-SAM',''),(3126,177,'Region','Tacna','PE-TAC',''),(3127,177,'Region','Tumbes','PE-TUM',''),(3128,177,'Region','Ucayali','PE-UCA',''),(3129,175,'District','National Capital District (Port Moresby)','PG-NCD',''),(3130,175,'Province','Central','PG-CPM',''),(3131,175,'Province','Chimbu','PG-CPK',''),(3132,175,'Province','Eastern Highlands','PG-EHG',''),(3133,175,'Province','East New Britain','PG-EBR',''),(3134,175,'Province','East Sepik','PG-ESW',''),(3135,175,'Province','Enga','PG-EPW',''),(3136,175,'Province','Gulf','PG-GPK',''),(3137,175,'Province','Madang','PG-MPM',''),(3138,175,'Province','Manus','PG-MRL',''),(3139,175,'Province','Milne Bay','PG-MBA',''),(3140,175,'Province','Morobe','PG-MPL',''),(3141,175,'Province','New Ireland','PG-NIK',''),(3142,175,'Province','Northern','PG-NPP',''),(3143,175,'Province','North Solomons','PG-NSA',''),(3144,175,'Province','Sandaun','PG-SAN',''),(3145,175,'Province','Southern Highlands','PG-SHM',''),(3146,175,'Province','Western','PG-WPD',''),(3147,175,'Province','Western Highlands','PG-WHM',''),(3148,175,'Province','West New Britain','PG-WBK',''),(3149,178,'Region','Autonomous Region in Muslim Mindanao (ARMM)','PH 14',''),(3150,178,'Region','Bicol','PH 05',''),(3151,178,'Region','Cagayan Valley','PH 02',''),(3152,178,'Region','CARAGA','PH 13',''),(3153,178,'Region','Central Luzon','PH 03',''),(3154,178,'Region','Central Mindanao','PH 12',''),(3155,178,'Region','Central Visayas','PH 07',''),(3156,178,'Region','Cordillera Administrative Region (CAR)','PH 15',''),(3157,178,'Region','Eastern Visayas','PH 08',''),(3158,178,'Region','Ilocos','PH 01',''),(3159,178,'Region','National Capital Region (Manila)','PH 00',''),(3160,178,'Region','Northern Mindanao','PH 10',''),(3161,178,'Region','Southern Mindanao','PH 11',''),(3162,178,'Region','Southern Tagalog','PH 04',''),(3163,178,'Region','Western Mindanao','PH 09',''),(3164,178,'Region','Western Visayas','PH 06',''),(3165,178,'Province','Abra','PH-ABR',''),(3166,178,'Province','Agusan del Norte','PH-AGN',''),(3167,178,'Province','Agusan del Sur','PH-AGS',''),(3168,178,'Province','Aklan','PH-AKL',''),(3169,178,'Province','Albay','PH-ALB',''),(3170,178,'Province','Antique','PH-ANT',''),(3171,178,'Province','Apayao','PH-APA',''),(3172,178,'Province','Aurora','PH-AUR',''),(3173,178,'Province','Basilan','PH-BAS',''),(3174,178,'Province','Batasn','PH-BAN',''),(3175,178,'Province','Batanes','PH-BTN',''),(3176,178,'Province','Batangas','PH-BTG',''),(3177,178,'Province','Benguet','PH-BEN',''),(3178,178,'Province','Biliran','PH-BIL',''),(3179,178,'Province','Bohol','PH-BOH',''),(3180,178,'Province','Bukidnon','PH-BUK',''),(3181,178,'Province','Bulacan','PH-BUL',''),(3182,178,'Province','Cagayan','PH-CAG',''),(3183,178,'Province','Camarines Norte','PH-CAN',''),(3184,178,'Province','Camarines Sur','PH-CAS',''),(3185,178,'Province','Camiguin','PH-CAM',''),(3186,178,'Province','Capiz','PH-CAP',''),(3187,178,'Province','Catanduanes','PH-CAT',''),(3188,178,'Province','Cavite','PH-CAV',''),(3189,178,'Province','Cebu','PH-CEB',''),(3190,178,'Province','Compostela Valley','PH-COM',''),(3191,178,'Province','Davao del Norte','PH-DAV',''),(3192,178,'Province','Davao del Sur','PH-DAS',''),(3193,178,'Province','Davao Oriental','PH-DAO',''),(3194,178,'Province','Eastern Samar','PH-EAS',''),(3195,178,'Province','Guimaras','PH-GUI',''),(3196,178,'Province','Ifugao','PH-IFU',''),(3197,178,'Province','Ilocos Norte','PH-ILN',''),(3198,178,'Province','Ilocos Sur','PH-ILS',''),(3199,178,'Province','Iloilo','PH-ILI',''),(3200,178,'Province','Isabela','PH-ISA',''),(3201,178,'Province','Kalinga-Apayso','PH-KAL',''),(3202,178,'Province','Laguna','PH-LAG',''),(3203,178,'Province','Lanao del Norte','PH-LAN',''),(3204,178,'Province','Lanao del Sur','PH-LAS',''),(3205,178,'Province','La Union','PH-LUN',''),(3206,178,'Province','Leyte','PH-LEY',''),(3207,178,'Province','Maguindanao','PH-MAG',''),(3208,178,'Province','Marinduque','PH-MAD',''),(3209,178,'Province','Masbate','PH-MAS',''),(3210,178,'Province','Mindoro Occidental','PH-MDC',''),(3211,178,'Province','Mindoro Oriental','PH-MDR',''),(3212,178,'Province','Misamis Occidental','PH-MSC',''),(3213,178,'Province','Misamis Oriental','PH-MSR',''),(3214,178,'Province','Mountain Province','PH-MOU',''),(3215,178,'Province','Negroe Occidental','PH-NEC',''),(3216,178,'Province','Negros Oriental','PH-NER',''),(3217,178,'Province','North Cotabato','PH-NCO',''),(3218,178,'Province','Northern Samar','PH-NSA',''),(3219,178,'Province','Nueva Ecija','PH-NUE',''),(3220,178,'Province','Nueva Vizcaya','PH-NUV',''),(3221,178,'Province','Palawan','PH-PLW',''),(3222,178,'Province','Pampanga','PH-PAM',''),(3223,178,'Province','Pangasinan','PH-PAN',''),(3224,178,'Province','Quezon','PH-QUE',''),(3225,178,'Province','Quirino','PH-QUI',''),(3226,178,'Province','Rizal','PH-RIZ',''),(3227,178,'Province','Romblon','PH-ROM',''),(3228,178,'Province','Sarangani','PH-SAR',''),(3229,178,'Province','Siquijor','PH-SIG',''),(3230,178,'Province','Sorsogon','PH-SOR',''),(3231,178,'Province','South Cotabato','PH-SCO',''),(3232,178,'Province','Southern Leyte','PH-SLE',''),(3233,178,'Province','Sultan Kudarat','PH-SUK',''),(3234,178,'Province','Sulu','PH-SLU',''),(3235,178,'Province','Surigao del Norte','PH-SUN',''),(3236,178,'Province','Surigao del Sur','PH-SUR',''),(3237,178,'Province','Tarlac','PH-TAR',''),(3238,178,'Province','Tawi-Tawi','PH-TAW',''),(3239,178,'Province','Western Samar','PH-WSA',''),(3240,178,'Province','Zambales','PH-ZMB',''),(3241,178,'Province','Zamboanga del Norte','PH-ZAN',''),(3242,178,'Province','Zamboanga del Sur','PH-ZAS',''),(3243,178,'Province','Zamboanga Sibiguey','PH-ZSI',''),(3244,171,'Capital territory','Islamabad','PK-IS',''),(3245,171,'Province','Balochistan','PK-BA',''),(3246,171,'Province','North-West Frontier','PK-NW',''),(3247,171,'Province','Punjab','PK-PB',''),(3248,171,'Province','Sindh','PK-SD',''),(3249,171,'Area','Federally Administered Tribal Areas','PK-TA',''),(3250,171,'Area','Azad Rashmir','PK-JK',''),(3251,171,'Area','Northern Areas','PK-NA',''),(3252,180,'Province','Dolno?l?skie','PL-DS',''),(3253,180,'Province','Kujawsko-pomorskie','PL-KP',''),(3254,180,'Province','Lubelskie','PL-LU',''),(3255,180,'Province','Lubuskie','PL-LB',''),(3256,180,'Province','?ódzkie','PL-LD',''),(3257,180,'Province','Ma?opolskie','PL-MA',''),(3258,180,'Province','Mazowieckie','PL-MZ',''),(3259,180,'Province','Opolskie','PL-OP',''),(3260,180,'Province','Podkarpackie','PL-PK',''),(3261,180,'Province','Podlaskie','PL-PD',''),(3262,180,'Province','Pomorskie','PL-PM',''),(3263,180,'Province','?l?skie','PL-SL',''),(3264,180,'Province','?wi?tokrzyskie','PL-SK',''),(3265,180,'Province','Warmi?sko-mazurskie','PL-WN',''),(3266,180,'Province','Wielkopolskie','PL-WP',''),(3267,180,'Province','Zachodniopomorskie','PL-ZP',''),(3268,181,'District','Aveiro','PT-01',''),(3269,181,'District','Beja','PT-02',''),(3270,181,'District','Braga','PT-03',''),(3271,181,'District','Bragança','PT-04',''),(3272,181,'District','Castelo Branco','PT-05',''),(3273,181,'District','Coimbra','PT-06',''),(3274,181,'District','Évora','PT-07',''),(3275,181,'District','Faro','PT-08',''),(3276,181,'District','Guarda','PT-09',''),(3277,181,'District','Leiria','PT-10',''),(3278,181,'District','Lisboa','PT-11',''),(3279,181,'District','Portalegre','PT-12',''),(3280,181,'District','Porto','PT-13',''),(3281,181,'District','Santarém','PT-14',''),(3282,181,'District','Setúbal','PT-15',''),(3283,181,'District','Viana do Castelo','PT-16',''),(3284,181,'District','Vila Real','PT-17',''),(3285,181,'District','Viseu','PT-18',''),(3286,181,'Autonomous region','Região Autónoma dos Açores','PT-20',''),(3287,181,'Autonomous region','Região Autónoma da Madeira','PT-30',''),(3288,172,'State','Aimeliik','PW-002',''),(3289,172,'State','Airai','PW-004',''),(3290,172,'State','Angaur','PW-010',''),(3291,172,'State','Hatobohei','PW-050',''),(3292,172,'State','Kayangel','PW-100',''),(3293,172,'State','Koror','PW-150',''),(3294,172,'State','Melekeok','PW-212',''),(3295,172,'State','Ngaraard','PW-214',''),(3296,172,'State','Ngarchelong','PW-218',''),(3297,172,'State','Ngardmau','PW-222',''),(3298,172,'State','Ngatpang','PW-224',''),(3299,172,'State','Ngchesar','PW-226',''),(3300,172,'State','Ngeremlengui','PW-227',''),(3301,172,'State','Ngiwal','PW-228',''),(3302,172,'State','Peleliu','PW-350',''),(3303,172,'State','Sonsorol','PW-370',''),(3304,176,'Capital district','Asunción','PY-ASU',''),(3305,176,'Department','Alto Paraguay','PY-16',''),(3306,176,'Department','Alto Paraná','PY-10',''),(3307,176,'Department','Amambay','PY-13',''),(3308,176,'Department','Boquerón','PY-19',''),(3309,176,'Department','Caaguazú','PY-5',''),(3310,176,'Department','Caazapá','PY-6',''),(3311,176,'Department','Canindeyú','PY-14',''),(3312,176,'Department','Central','PY-11',''),(3313,176,'Department','Concepción','PY-1',''),(3314,176,'Department','Cordillera','PY-3',''),(3315,176,'Department','Guairá','PY-4',''),(3316,176,'Department','Itapúa','PY-7',''),(3317,176,'Department','Misiones','PY-8',''),(3318,176,'Department','Ñeembucú','PY-12',''),(3319,176,'Department','Paraguarí','PY-9',''),(3320,176,'Department','Presidente Hayes','PY-15',''),(3321,176,'Department','San Pedro','PY-2',''),(3322,183,'Municipality','Ad Dawhah','QA-DA',''),(3323,183,'Municipality','Al Ghuwayriyah','QA-GH',''),(3324,183,'Municipality','Al Jumayliyah','QA-JU',''),(3325,183,'Municipality','Al Khawr','QA-KH',''),(3326,183,'Municipality','Al Wakrah','QA-WA',''),(3327,183,'Municipality','Ar Rayyan','QA-RA',''),(3328,183,'Municipality','Jariyan al Batnah','QA-JB',''),(3329,183,'Municipality','Madinat ash Shamal','QA-MS',''),(3330,183,'Municipality','Umm Salal','QA-US',''),(3331,185,'Department','Alba','RO-AB',''),(3332,185,'Department','Arad','RO-AR',''),(3333,185,'Department','Arge?','RO-AG',''),(3334,185,'Department','Bac?u','RO-BC',''),(3335,185,'Department','Bihor','RO-BH',''),(3336,185,'Department','Bistri?a-N?s?ud','RO-BN',''),(3337,185,'Department','Boto?ani','RO-BT',''),(3338,185,'Department','Bra?ov','RO-BV',''),(3339,185,'Department','Br?ila','RO-BR',''),(3340,185,'Department','Buz?u','RO-BZ',''),(3341,185,'Department','Cara?-Severin','RO-CS',''),(3342,185,'Department','C?l?ra?i','RO-CL',''),(3343,185,'Department','Cluj','RO-CJ',''),(3344,185,'Department','Constan?a','RO-CT',''),(3345,185,'Department','Covasna','RO-CV',''),(3346,185,'Department','Dâmbovi?a','RO-DB',''),(3347,185,'Department','Dolj','RO-DJ',''),(3348,185,'Department','Gala?i','RO-GL',''),(3349,185,'Department','Giurgiu','RO-GR',''),(3350,185,'Department','Gorj','RO-GJ',''),(3351,185,'Department','Harghita','RO-HR',''),(3352,185,'Department','Hunedoara','RO-HD',''),(3353,185,'Department','Ialomi?a','RO-IL',''),(3354,185,'Department','Ia?i','RO-IS',''),(3355,185,'Department','Ilfov','RO-IF',''),(3356,185,'Department','Maramure?','RO-MM',''),(3357,185,'Department','Mehedin?i','RO-MH',''),(3358,185,'Department','Mure?','RO-MS',''),(3359,185,'Department','Neam?','RO-NT',''),(3360,185,'Department','Olt','RO-OT',''),(3361,185,'Department','Prahova','RO-PH',''),(3362,185,'Department','Satu Mare','RO-SM',''),(3363,185,'Department','S?laj','RO-SJ',''),(3364,185,'Department','Sibiu','RO-SB',''),(3365,185,'Department','Suceava','RO-SV',''),(3366,185,'Department','Teleorman','RO-TR',''),(3367,185,'Department','Timi?','RO-TM',''),(3368,185,'Department','Tulcea','RO-TL',''),(3369,185,'Department','Vaslui','RO-VS',''),(3370,185,'Department','Vâlcea','RO-VL',''),(3371,185,'Department','Vrancea','RO-VN',''),(3372,185,'Municipality','Bucure?ti','RO-B',''),(3373,200,'City','Beograd','RS-00',''),(3374,200,'Autonomous province','Kosovo-Metohija','RS KM',''),(3375,200,'Autonomous province','Vojvodina','RS VO',''),(3376,200,'District','Severna Ba?ka','RS-01',''),(3377,200,'District','Južna Ba?ka','RS-06',''),(3378,200,'District','Zapadna Ba?ka','RS-05',''),(3379,200,'District','Severni Banat','RS-03',''),(3380,200,'District','Srednji Banat','RS-02',''),(3381,200,'District','Južni Banat','RS-04',''),(3382,200,'District','Bor','RS-14',''),(3383,200,'District','Brani?evo','RS-11',''),(3384,200,'District','Jablanica','RS-23',''),(3385,200,'District','Kolubara','RS-09',''),(3386,200,'District','Kosovo','RS-25',''),(3387,200,'District','Kosovska Mitrovica','RS-28',''),(3388,200,'District','Kosovo-Pomoravlje','RS-29',''),(3389,200,'District','Ma?va','RS-08',''),(3390,200,'District','Moravica','RS-17',''),(3391,200,'District','Nišava','RS-20',''),(3392,200,'District','P?inja','RS-24',''),(3393,200,'District','Pe?','RS-26',''),(3394,200,'District','Pirot','RS-22',''),(3395,200,'District','Podunavlje','RS-10',''),(3396,200,'District','Pomoravlje','RS-13',''),(3397,200,'District','Prizren','RS-27',''),(3398,200,'District','Rasina','RS-19',''),(3399,200,'District','Raška','RS-18',''),(3400,200,'District','Srem','RS-07',''),(3401,200,'District','Šumadija','RS-12',''),(3402,200,'District','Toplica','RS-21',''),(3403,200,'District','Zaje?ar','RS-15',''),(3404,200,'District','Zlatibor','RS-16',''),(3405,186,'Republic','Adygeya, Respublika','RU-AD',''),(3406,186,'Republic','Altay, Respublika','RU-AL',''),(3407,186,'Republic','Bashkortostan, Respublika','RU-BA',''),(3408,186,'Republic','Buryatiya, Respublika','RU-BU',''),(3409,186,'Republic','Chechenskaya Respublika','RU-CE',''),(3410,186,'Republic','Chuvashskaya Respublika','RU-CU',''),(3411,186,'Republic','Dagestan, Respublika','RU-DA',''),(3412,186,'Republic','Respublika Ingushetiya','RU-IN',''),(3413,186,'Republic','Kabardino-Balkarskaya Respublika','RU-KB',''),(3414,186,'Republic','Kalmykiya, Respublika','RU-KL',''),(3415,186,'Republic','Karachayevo-Cherkesskaya Respublika','RU-KC',''),(3416,186,'Republic','Kareliya, Respublika','RU-KR',''),(3417,186,'Republic','Khakasiya, Respublika','RU-KK',''),(3418,186,'Republic','Komi, Respublika','RU-KO',''),(3419,186,'Republic','Mariy El, Respublika','RU-ME',''),(3420,186,'Republic','Mordoviya, Respublika','RU-MO',''),(3421,186,'Republic','Sakha, Respublika [Yakutiya]','RU-SA',''),(3422,186,'Republic','Severnaya Osetiya-Alaniya, Respublika','RU-SE',''),(3423,186,'Republic','Tatarstan, Respublika','RU-TA',''),(3424,186,'Republic','Tyva, Respublika [Tuva]','RU-TY',''),(3425,186,'Republic','Udmurtskaya Respublika','RU-UD',''),(3426,186,'Administrative Territory','Altayskiy kray','RU-ALT',''),(3427,186,'Administrative Territory','Kamchatskiy kray','RU-KAM',''),(3428,186,'Administrative Territory','Khabarovskiy kray','RU-KHA',''),(3429,186,'Administrative Territory','Krasnodarskiy kray','RU-KDA',''),(3430,186,'Administrative Territory','Krasnoyarskiy kray','RU-KYA',''),(3431,186,'Administrative Territory','Permskiy kray','RU-PER',''),(3432,186,'Administrative Territory','Primorskiy kray','RU-PRI',''),(3433,186,'Administrative Territory','Stavropol\'skiy kray','RU-STA',''),(3434,186,'Administrative Region','Amurskaya oblast\'','RU-AMU',''),(3435,186,'Administrative Region','Arkhangel\'skaya oblast\'','RU-ARK',''),(3436,186,'Administrative Region','Astrakhanskaya oblast\'','RU-AST',''),(3437,186,'Administrative Region','Belgorodskaya oblast\'','RU-BEL',''),(3438,186,'Administrative Region','Bryanskaya oblast\'','RU-BRY',''),(3439,186,'Administrative Region','Chelyabinskaya oblast\'','RU-CHE',''),(3440,186,'Administrative Region','Chitinskaya oblast\'','RU-CHI',''),(3441,186,'Administrative Region','Irkutiskaya oblast\'','RU-IRK',''),(3442,186,'Administrative Region','Ivanovskaya oblast\'','RU-IVA',''),(3443,186,'Administrative Region','Kaliningradskaya oblast\'','RU-KGD',''),(3444,186,'Administrative Region','Kaluzhskaya oblast\'','RU-KLU',''),(3445,186,'Administrative Region','Kemerovskaya oblast\'','RU-KEM',''),(3446,186,'Administrative Region','Kirovskaya oblast\'','RU-KIR',''),(3447,186,'Administrative Region','Kostromskaya oblast\'','RU-KOS',''),(3448,186,'Administrative Region','Kurganskaya oblast\'','RU-KGN',''),(3449,186,'Administrative Region','Kurskaya oblast\'','RU-KRS',''),(3450,186,'Administrative Region','Leningradskaya oblast\'','RU-LEN',''),(3451,186,'Administrative Region','Lipetskaya oblast\'','RU-LIP',''),(3452,186,'Administrative Region','Magadanskaya oblast\'','RU-MAG',''),(3453,186,'Administrative Region','Moskovskaya oblast\'','RU-MOS',''),(3454,186,'Administrative Region','Murmanskaya oblast\'','RU-MUR',''),(3455,186,'Administrative Region','Nizhegorodskaya oblast\'','RU-NIZ',''),(3456,186,'Administrative Region','Novgorodskaya oblast\'','RU-NGR',''),(3457,186,'Administrative Region','Novosibirskaya oblast\'','RU-NVS',''),(3458,186,'Administrative Region','Omskaya oblast\'','RU-OMS',''),(3459,186,'Administrative Region','Orenburgskaya oblast\'','RU-ORE',''),(3460,186,'Administrative Region','Orlovskaya oblast\'','RU-ORL',''),(3461,186,'Administrative Region','Penzenskaya oblast\'','RU-PNZ',''),(3462,186,'Administrative Region','Pskovskaya oblast\'','RU-PSK',''),(3463,186,'Administrative Region','Rostovskaya oblast\'','RU-ROS',''),(3464,186,'Administrative Region','Ryazanskaya oblast\'','RU-RYA',''),(3465,186,'Administrative Region','Sakhalinskaya oblast\'','RU-SAK',''),(3466,186,'Administrative Region','Samaraskaya oblast\'','RU-SAM',''),(3467,186,'Administrative Region','Saratovskaya oblast\'','RU-SAR',''),(3468,186,'Administrative Region','Smolenskaya oblast\'','RU-SMO',''),(3469,186,'Administrative Region','Sverdlovskaya oblast\'','RU-SVE',''),(3470,186,'Administrative Region','Tambovskaya oblast\'','RU-TAM',''),(3471,186,'Administrative Region','Tomskaya oblast\'','RU-TOM',''),(3472,186,'Administrative Region','Tul\'skaya oblast\'','RU-TUL',''),(3473,186,'Administrative Region','Tverskaya oblast\'','RU-TVE',''),(3474,186,'Administrative Region','Tyumenskaya oblast\'','RU-TYU',''),(3475,186,'Administrative Region','Ul\'yanovskaya oblast\'','RU-ULY',''),(3476,186,'Administrative Region','Vladimirskaya oblast\'','RU-VLA',''),(3477,186,'Administrative Region','Volgogradskaya oblast\'','RU-VGG',''),(3478,186,'Administrative Region','Vologodskaya oblast\'','RU-VLG',''),(3479,186,'Administrative Region','Voronezhskaya oblast\'','RU-VOR',''),(3480,186,'Administrative Region','Yaroslavskaya oblast\'','RU-YAR',''),(3481,186,'Autonomous City','Moskva','RU-MOW',''),(3482,186,'Autonomous City','Sankt-Peterburg','RU-SPE',''),(3483,186,'Autonomous Region','Yevreyskaya avtonomnaya oblast\'','RU-YEV',''),(3484,186,'Autonomous District','Aginsky Buryatskiy avtonomnyy okrug','RU-AGB',''),(3485,186,'Autonomous District','Chukotskiy avtonomnyy okrug','RU-CHU',''),(3486,186,'Autonomous District','Khanty-Mansiysky avtonomnyy okrug-Yugra','RU-KHM',''),(3487,186,'Autonomous District','Nenetskiy avtonomnyy okrug','RU-NEN',''),(3488,186,'Autonomous District','Ust\'-Ordynskiy Buryatskiy avtonomnyy okrug','RU-UOB',''),(3489,186,'Autonomous District','Yamalo-Nenetskiy avtonomnyy okrug','RU-YAN',''),(3490,187,'Town council','Ville de Kigali','RW-01',''),(3491,187,'Province','Est','RW-02',''),(3492,187,'Province','Nord','RW-03',''),(3493,187,'Province','Ouest','RW-04',''),(3494,187,'Province','Sud','RW-05',''),(3495,198,'Province','Al B?hah','SA-11',''),(3496,198,'Province','Al ?ud?d ash Sham?liyah','SA-08',''),(3497,198,'Province','Al Jawf','SA-12',''),(3498,198,'Province','Al Mad?nah','SA-03',''),(3499,198,'Province','Al Qa??m','SA-05',''),(3500,198,'Province','Ar Riy??','SA-01',''),(3501,198,'Province','Ash Sharq?yah','SA-04',''),(3502,198,'Province','`As?r','SA-14',''),(3503,198,'Province','??\'il','SA-06',''),(3504,198,'Province','J?zan','SA-09',''),(3505,198,'Province','Makkah','SA-02',''),(3506,198,'Province','Najr?n','SA-10',''),(3507,198,'Province','Tab?k','SA-07',''),(3508,207,'Capital territory','Capital Territory (Honiara)','SB-CT',''),(3509,207,'Province','Central','SB-CE',''),(3510,207,'Province','Choiseul','SB-CH',''),(3511,207,'Province','Guadalcanal','SB-GU',''),(3512,207,'Province','Isabel','SB-IS',''),(3513,207,'Province','Makira','SB-MK',''),(3514,207,'Province','Malaita','SB-ML',''),(3515,207,'Province','Rennell and Bellona','SB-RB',''),(3516,207,'Province','Temotu','SB-TE',''),(3517,207,'Province','Western','SB-WE',''),(3518,201,'District','Anse aux Pins','SC-01',''),(3519,201,'District','Anse Boileau','SC-02',''),(3520,201,'District','Anse Étoile','SC-03',''),(3521,201,'District','Anse Louis','SC-04',''),(3522,201,'District','Anse Royale','SC-05',''),(3523,201,'District','Baie Lazare','SC-06',''),(3524,201,'District','Baie Sainte Anne','SC-07',''),(3525,201,'District','Beau Vallon','SC-08',''),(3526,201,'District','Bel Air','SC-09',''),(3527,201,'District','Bel Ombre','SC-10',''),(3528,201,'District','Cascade','SC-11',''),(3529,201,'District','Glacis','SC-12',''),(3530,201,'District','Grand\' Anse (Mahé)','SC-13',''),(3531,201,'District','Grand\' Anse (Praslin)','SC-14',''),(3532,201,'District','La Digue','SC-15',''),(3533,201,'District','La Rivière Anglaise','SC-16',''),(3534,201,'District','Mont Buxton','SC-17',''),(3535,201,'District','Mont Fleuri','SC-18',''),(3536,201,'District','Plaisance','SC-19',''),(3537,201,'District','Pointe La Rue','SC-20',''),(3538,201,'District','Port Glaud','SC-21',''),(3539,201,'District','Saint Louis','SC-22',''),(3540,201,'District','Takamaka','SC-23',''),(3541,213,'state','Al Ba?r al A?mar','SD-26',''),(3542,213,'state','Al Bu?ayr?t','SD-18',''),(3543,213,'state','Al Jaz?rah','SD-07',''),(3544,213,'state','Al Khar??m','SD-03',''),(3545,213,'state','Al Qa??rif','SD-06',''),(3546,213,'state','Al Wa?dah','SD-22',''),(3547,213,'state','An N?l','SD-04',''),(3548,213,'state','An N?l al Abya?','SD-08',''),(3549,213,'state','An N?l al Azraq','SD-24',''),(3550,213,'state','Ash Sham?l?yah','SD-01',''),(3551,213,'state','A‘?l? an N?l','SD-23',''),(3552,213,'state','Ba?r al Jabal','SD-17',''),(3553,213,'state','Gharb al Istiw?\'?yah','SD-16',''),(3554,213,'state','Gharb Ba?r al Ghaz?l','SD-14',''),(3555,213,'state','Gharb D?rf?r','SD-12',''),(3556,213,'state','Jan?b D?rf?r','SD-11',''),(3557,213,'state','Jan?b Kurduf?n','SD-13',''),(3558,213,'state','J?nqal?','SD-20',''),(3559,213,'state','Kassal?','SD-05',''),(3560,213,'state','Sham?l Ba?r al Ghaz?l','SD-15',''),(3561,213,'state','Sham?l D?rf?r','SD-02',''),(3562,213,'state','Sham?l Kurduf?n','SD-09',''),(3563,213,'state','Sharq al Istiw?\'?yah','SD-19',''),(3564,213,'state','Sinn?r','SD-25',''),(3565,213,'state','W?r?b','SD-21',''),(3566,217,'County','Blekinge län','SE-K',''),(3567,217,'County','Dalarnas län','SE-W',''),(3568,217,'County','Gotlands län','SE-I',''),(3569,217,'County','Gävleborgs län','SE-X',''),(3570,217,'County','Hallands län','SE-N',''),(3571,217,'County','Jämtlande län','SE-Z',''),(3572,217,'County','Jönköpings län','SE-F',''),(3573,217,'County','Kalmar län','SE-H',''),(3574,217,'County','Kronobergs län','SE-G',''),(3575,217,'County','Norrbottens län','SE-BD',''),(3576,217,'County','Skåne län','SE-M',''),(3577,217,'County','Stockholms län','SE-AB',''),(3578,217,'County','Södermanlands län','SE-D',''),(3579,217,'County','Uppsala län','SE-C',''),(3580,217,'County','Värmlands län','SE-S',''),(3581,217,'County','Västerbottens län','SE-AC',''),(3582,217,'County','Västernorrlands län','SE-Y',''),(3583,217,'County','Västmanlands län','SE-U',''),(3584,217,'County','Västra Götalands län','SE-Q',''),(3585,217,'County','Örebro län','SE-T',''),(3586,217,'County','Östergötlands län','SE-E',''),(3587,203,'district','Central Singapore','SG-01',''),(3588,203,'district','North East','SG-02',''),(3589,203,'district','North West','SG-03',''),(3590,203,'district','South East','SG-04',''),(3591,203,'district','South West','SG-05',''),(3592,189,'Dependency','Saint Helena','SH-SH',''),(3593,189,'Dependency','Tristan da Cunha','SH-TA',''),(3594,189,'Administrative area','Ascension','SH-AC',''),(3595,206,'Municipalities','Ajdovš?ina','SI-001',''),(3596,206,'Municipalities','Beltinci','SI-002',''),(3597,206,'Municipalities','Benedikt','SI-148',''),(3598,206,'Municipalities','Bistrica ob Sotli','SI-149',''),(3599,206,'Municipalities','Bled','SI-003',''),(3600,206,'Municipalities','Bloke','SI-150',''),(3601,206,'Municipalities','Bohinj','SI-004',''),(3602,206,'Municipalities','Borovnica','SI-005',''),(3603,206,'Municipalities','Bovec','SI-006',''),(3604,206,'Municipalities','Braslov?e','SI-151',''),(3605,206,'Municipalities','Brda','SI-007',''),(3606,206,'Municipalities','Brežice','SI-009',''),(3607,206,'Municipalities','Brezovica','SI-008',''),(3608,206,'Municipalities','Cankova','SI-152',''),(3609,206,'Municipalities','Celje','SI-011',''),(3610,206,'Municipalities','Cerklje na Gorenjskem','SI-012',''),(3611,206,'Municipalities','Cerknica','SI-013',''),(3612,206,'Municipalities','Cerkno','SI-014',''),(3613,206,'Municipalities','Cerkvenjak','SI-153',''),(3614,206,'Municipalities','?renšovci','SI-015',''),(3615,206,'Municipalities','?rna na Koroškem','SI-016',''),(3616,206,'Municipalities','?rnomelj','SI-017',''),(3617,206,'Municipalities','Destrnik','SI-018',''),(3618,206,'Municipalities','Diva?a','SI-019',''),(3619,206,'Municipalities','Dobje','SI-154',''),(3620,206,'Municipalities','Dobrepolje','SI-020',''),(3621,206,'Municipalities','Dobrna','SI-155',''),(3622,206,'Municipalities','Dobrova-Polhov Gradec','SI-021',''),(3623,206,'Municipalities','Dobrovnik/Dobronak','SI-156',''),(3624,206,'Municipalities','Dol pri Ljubljani','SI-022',''),(3625,206,'Municipalities','Dolenjske Toplice','SI-157',''),(3626,206,'Municipalities','Domžale','SI-023',''),(3627,206,'Municipalities','Dornava','SI-024',''),(3628,206,'Municipalities','Dravograd','SI-025',''),(3629,206,'Municipalities','Duplek','SI-026',''),(3630,206,'Municipalities','Gorenja vas-Poljane','SI-027',''),(3631,206,'Municipalities','Gorišnica','SI-028',''),(3632,206,'Municipalities','Gornja Radgona','SI-029',''),(3633,206,'Municipalities','Gornji Grad','SI-030',''),(3634,206,'Municipalities','Gornji Petrovci','SI-031',''),(3635,206,'Municipalities','Grad','SI-158',''),(3636,206,'Municipalities','Grosuplje','SI-032',''),(3637,206,'Municipalities','Hajdina','SI-159',''),(3638,206,'Municipalities','Ho?e-Slivnica','SI-160',''),(3639,206,'Municipalities','Hodoš/Hodos','SI-161',''),(3640,206,'Municipalities','Horjul','SI-162',''),(3641,206,'Municipalities','Hrastnik','SI-034',''),(3642,206,'Municipalities','Hrpelje-Kozina','SI-035',''),(3643,206,'Municipalities','Idrija','SI-036',''),(3644,206,'Municipalities','Ig','SI-037',''),(3645,206,'Municipalities','Ilirska Bistrica','SI-038',''),(3646,206,'Municipalities','Ivan?na Gorica','SI-039',''),(3647,206,'Municipalities','Izola/Isola','SI-040',''),(3648,206,'Municipalities','Jesenice','SI-041',''),(3649,206,'Municipalities','Jezersko','SI-163',''),(3650,206,'Municipalities','Juršinci','SI-042',''),(3651,206,'Municipalities','Kamnik','SI-043',''),(3652,206,'Municipalities','Kanal','SI-044',''),(3653,206,'Municipalities','Kidri?evo','SI-045',''),(3654,206,'Municipalities','Kobarid','SI-046',''),(3655,206,'Municipalities','Kobilje','SI-047',''),(3656,206,'Municipalities','Ko?evje','SI-048',''),(3657,206,'Municipalities','Komen','SI-049',''),(3658,206,'Municipalities','Komenda','SI-164',''),(3659,206,'Municipalities','Koper/Capodistria','SI-050',''),(3660,206,'Municipalities','Kostel','SI-165',''),(3661,206,'Municipalities','Kozje','SI-051',''),(3662,206,'Municipalities','Kranj','SI-052',''),(3663,206,'Municipalities','Kranjska Gora','SI-053',''),(3664,206,'Municipalities','Križevci','SI-166',''),(3665,206,'Municipalities','Krško','SI-054',''),(3666,206,'Municipalities','Kungota','SI-055',''),(3667,206,'Municipalities','Kuzma','SI-056',''),(3668,206,'Municipalities','Laško','SI-057',''),(3669,206,'Municipalities','Lenart','SI-058',''),(3670,206,'Municipalities','Lendava/Lendva','SI-059',''),(3671,206,'Municipalities','Litija','SI-060',''),(3672,206,'Municipalities','Ljubljana','SI-061',''),(3673,206,'Municipalities','Ljubno','SI-062',''),(3674,206,'Municipalities','Ljutomer','SI-063',''),(3675,206,'Municipalities','Logatec','SI-064',''),(3676,206,'Municipalities','Loška dolina','SI-065',''),(3677,206,'Municipalities','Loški Potok','SI-066',''),(3678,206,'Municipalities','Lovrenc na Pohorju','SI-167',''),(3679,206,'Municipalities','Lu?e','SI-067',''),(3680,206,'Municipalities','Lukovica','SI-068',''),(3681,206,'Municipalities','Majšperk','SI-069',''),(3682,206,'Municipalities','Maribor','SI-070',''),(3683,206,'Municipalities','Markovci','SI-168',''),(3684,206,'Municipalities','Medvode','SI-071',''),(3685,206,'Municipalities','Mengeš','SI-072',''),(3686,206,'Municipalities','Metlika','SI-073',''),(3687,206,'Municipalities','Mežica','SI-074',''),(3688,206,'Municipalities','Miklavž na Dravskem polju','SI-169',''),(3689,206,'Municipalities','Miren-Kostanjevica','SI-075',''),(3690,206,'Municipalities','Mirna Pe?','SI-170',''),(3691,206,'Municipalities','Mislinja','SI-076',''),(3692,206,'Municipalities','Morav?e','SI-077',''),(3693,206,'Municipalities','Moravske Toplice','SI-078',''),(3694,206,'Municipalities','Mozirje','SI-079',''),(3695,206,'Municipalities','Murska Sobota','SI-080',''),(3696,206,'Municipalities','Muta','SI-081',''),(3697,206,'Municipalities','Naklo','SI-082',''),(3698,206,'Municipalities','Nazarje','SI-083',''),(3699,206,'Municipalities','Nova Gorica','SI-084',''),(3700,206,'Municipalities','Novo mesto','SI-085',''),(3701,206,'Municipalities','Odranci','SI-086',''),(3702,206,'Municipalities','Oplotnica','SI-171',''),(3703,206,'Municipalities','Ormož','SI-087',''),(3704,206,'Municipalities','Osilnica','SI-088',''),(3705,206,'Municipalities','Pesnica','SI-089',''),(3706,206,'Municipalities','Piran/Pirano','SI-090',''),(3707,206,'Municipalities','Pivka','SI-091',''),(3708,206,'Municipalities','Pod?etrtek','SI-092',''),(3709,206,'Municipalities','Podlehnik','SI-172',''),(3710,206,'Municipalities','Podvelka','SI-093',''),(3711,206,'Municipalities','Polzela','SI-173',''),(3712,206,'Municipalities','Postojna','SI-094',''),(3713,206,'Municipalities','Prebold','SI-174',''),(3714,206,'Municipalities','Preddvor','SI-095',''),(3715,206,'Municipalities','Prevalje','SI-175',''),(3716,206,'Municipalities','Ptuj','SI-096',''),(3717,206,'Municipalities','Puconci','SI-097',''),(3718,206,'Municipalities','Ra?e-Fram','SI-098',''),(3719,206,'Municipalities','Rade?e','SI-099',''),(3720,206,'Municipalities','Radenci','SI-100',''),(3721,206,'Municipalities','Radlje ob Dravi','SI-101',''),(3722,206,'Municipalities','Radovljica','SI-102',''),(3723,206,'Municipalities','Ravne na Koroškem','SI-103',''),(3724,206,'Municipalities','Razkrižje','SI-176',''),(3725,206,'Municipalities','Ribnica','SI-104',''),(3726,206,'Municipalities','Ribnica na Pohorju','SI-177',''),(3727,206,'Municipalities','Rogaška Slatina','SI-106',''),(3728,206,'Municipalities','Rogašovci','SI-105',''),(3729,206,'Municipalities','Rogatec','SI-107',''),(3730,206,'Municipalities','Ruše','SI-108',''),(3731,206,'Municipalities','Šalovci','SI-033',''),(3732,206,'Municipalities','Selnica ob Dravi','SI-178',''),(3733,206,'Municipalities','Semi?','SI-109',''),(3734,206,'Municipalities','Šempeter-Vrtojba','SI-183',''),(3735,206,'Municipalities','Šen?ur','SI-117',''),(3736,206,'Municipalities','Šentilj','SI-118',''),(3737,206,'Municipalities','Šentjernej','SI-119',''),(3738,206,'Municipalities','Šentjur pri Celju','SI-120',''),(3739,206,'Municipalities','Sevnica','SI-110',''),(3740,206,'Municipalities','Sežana','SI-111',''),(3741,206,'Municipalities','Škocjan','SI-121',''),(3742,206,'Municipalities','Škofja Loka','SI-122',''),(3743,206,'Municipalities','Škofljica','SI-123',''),(3744,206,'Municipalities','Slovenj Gradec','SI-112',''),(3745,206,'Municipalities','Slovenska Bistrica','SI-113',''),(3746,206,'Municipalities','Slovenske Konjice','SI-114',''),(3747,206,'Municipalities','Šmarje pri Jelšah','SI-124',''),(3748,206,'Municipalities','Šmartno ob Paki','SI-125',''),(3749,206,'Municipalities','Šmartno pri Litiji','SI-194',''),(3750,206,'Municipalities','Sodražica','SI-179',''),(3751,206,'Municipalities','Sol?ava','SI-180',''),(3752,206,'Municipalities','Šoštanj','SI-126',''),(3753,206,'Municipalities','Starše','SI-115',''),(3754,206,'Municipalities','Štore','SI-127',''),(3755,206,'Municipalities','Sveta Ana','SI-181',''),(3756,206,'Municipalities','Sveti Andraž v Slovenskih goricah','SI-182',''),(3757,206,'Municipalities','Sveti Jurij','SI-116',''),(3758,206,'Municipalities','Tabor','SI-184',''),(3759,206,'Municipalities','Tišina','SI-010',''),(3760,206,'Municipalities','Tolmin','SI-128',''),(3761,206,'Municipalities','Trbovlje','SI-129',''),(3762,206,'Municipalities','Trebnje','SI-130',''),(3763,206,'Municipalities','Trnovska vas','SI-185',''),(3764,206,'Municipalities','Trži?','SI-131',''),(3765,206,'Municipalities','Trzin','SI-186',''),(3766,206,'Municipalities','Turniš?e','SI-132',''),(3767,206,'Municipalities','Velenje','SI-133',''),(3768,206,'Municipalities','Velika Polana','SI-187',''),(3769,206,'Municipalities','Velike Laš?e','SI-134',''),(3770,206,'Municipalities','Veržej','SI-188',''),(3771,206,'Municipalities','Videm','SI-135',''),(3772,206,'Municipalities','Vipava','SI-136',''),(3773,206,'Municipalities','Vitanje','SI-137',''),(3774,206,'Municipalities','Vodice','SI-138',''),(3775,206,'Municipalities','Vojnik','SI-139',''),(3776,206,'Municipalities','Vransko','SI-189',''),(3777,206,'Municipalities','Vrhnika','SI-140',''),(3778,206,'Municipalities','Vuzenica','SI-141',''),(3779,206,'Municipalities','Zagorje ob Savi','SI-142',''),(3780,206,'Municipalities','Žalec','SI-190',''),(3781,206,'Municipalities','Zavr?','SI-143',''),(3782,206,'Municipalities','Železniki','SI-146',''),(3783,206,'Municipalities','Žetale','SI-191',''),(3784,206,'Municipalities','Žiri','SI-147',''),(3785,206,'Municipalities','Žirovnica','SI-192',''),(3786,206,'Municipalities','Zre?e','SI-144',''),(3787,206,'Municipalities','Žužemberk','SI-193',''),(3788,205,'Region','Banskobystrický kraj','SK-BC',''),(3789,205,'Region','Bratislavský kraj','SK-BL',''),(3790,205,'Region','Košický kraj','SK-KI',''),(3791,205,'Region','Nitriansky kraj','SK-NJ',''),(3792,205,'Region','Prešovský kraj','SK-PV',''),(3793,205,'Region','Tren?iansky kraj','SK-TC',''),(3794,205,'Region','Trnavský kraj','SK-TA',''),(3795,205,'Region','Žilinský kraj','SK-ZI',''),(3796,202,'Area','Western Area (Freetown)','SL-W',''),(3797,202,'Province','Eastern','SL-E',''),(3798,202,'Province','Northern','SL-N',''),(3799,202,'Province','Southern (Sierra Leone)','SL-S',''),(3800,196,'Municipalities','Acquaviva','SM-01',''),(3801,196,'Municipalities','Borgo Maggiore','SM-06',''),(3802,196,'Municipalities','Chiesanuova','SM-02',''),(3803,196,'Municipalities','Domagnano','SM-03',''),(3804,196,'Municipalities','Faetano','SM-04',''),(3805,196,'Municipalities','Fiorentino','SM-05',''),(3806,196,'Municipalities','Montegiardino','SM-08',''),(3807,196,'Municipalities','San Marino','SM-07',''),(3808,196,'Municipalities','Serravalle','SM-09',''),(3809,199,'Region','Dakar','SN-DK',''),(3810,199,'Region','Diourbel','SN-DB',''),(3811,199,'Region','Fatick','SN-FK',''),(3812,199,'Region','Kaolack','SN-KL',''),(3813,199,'Region','Kolda','SN-KD',''),(3814,199,'Region','Louga','SN-LG',''),(3815,199,'Region','Matam','SN-MT',''),(3816,199,'Region','Saint-Louis','SN-SL',''),(3817,199,'Region','Tambacounda','SN-TC',''),(3818,199,'Region','Thiès','SN-TH',''),(3819,199,'Region','Ziguinchor','SN-ZG',''),(3820,208,'Region','Awdal','SO-AW',''),(3821,208,'Region','Bakool','SO-BK',''),(3822,208,'Region','Banaadir','SO-BN',''),(3823,208,'Region','Bari','SO-BR',''),(3824,208,'Region','Bay','SO-BY',''),(3825,208,'Region','Galguduud','SO-GA',''),(3826,208,'Region','Gedo','SO-GE',''),(3827,208,'Region','Hiirsan','SO-HI',''),(3828,208,'Region','Jubbada Dhexe','SO-JD',''),(3829,208,'Region','Jubbada Hoose','SO-JH',''),(3830,208,'Region','Mudug','SO-MU',''),(3831,208,'Region','Nugaal','SO-NU',''),(3832,208,'Region','Saneag','SO-SA',''),(3833,208,'Region','Shabeellaha Dhexe','SO-SD',''),(3834,208,'Region','Shabeellaha Hoose','SO-SH',''),(3835,208,'Region','Sool','SO-SO',''),(3836,208,'Region','Togdheer','SO-TO',''),(3837,208,'Region','Woqooyi Galbeed','SO-WO',''),(3838,214,'District','Brokopondo','SR-BR',''),(3839,214,'District','Commewijne','SR-CM',''),(3840,214,'District','Coronie','SR-CR',''),(3841,214,'District','Marowijne','SR-MA',''),(3842,214,'District','Nickerie','SR-NI',''),(3843,214,'District','Para','SR-PR',''),(3844,214,'District','Paramaribo','SR-PM',''),(3845,214,'District','Saramacca','SR-SA',''),(3846,214,'District','Sipaliwini','SR-SI',''),(3847,214,'District','Wanica','SR-WA',''),(3848,197,'Municipality','Príncipe','ST-P',''),(3849,197,'Municipality','São Tomé','ST-S',''),(3850,70,'Department','Ahuachapán','SV-AH',''),(3851,70,'Department','Cabañas','SV-CA',''),(3852,70,'Department','Cuscatlán','SV-CU',''),(3853,70,'Department','Chalatenango','SV-CH',''),(3854,70,'Department','La Libertad','SV-LI',''),(3855,70,'Department','La Paz','SV-PA',''),(3856,70,'Department','La Unión','SV-UN',''),(3857,70,'Department','Morazán','SV-MO',''),(3858,70,'Department','San Miguel','SV-SM',''),(3859,70,'Department','San Salvador','SV-SS',''),(3860,70,'Department','Santa Ana','SV-SA',''),(3861,70,'Department','San Vicente','SV-SV',''),(3862,70,'Department','Sonsonate','SV-SO',''),(3863,70,'Department','Usulután','SV-US',''),(3864,219,'Governorate','Al Hasakah','SY-HA',''),(3865,219,'Governorate','Al Ladhiqiyah','SY-LA',''),(3866,219,'Governorate','Al Qunaytirah','SY-QU',''),(3867,219,'Governorate','Ar Raqqah','SY-RA',''),(3868,219,'Governorate','As Suwayda\'','SY-SU',''),(3869,219,'Governorate','Dar\'a','SY-DR',''),(3870,219,'Governorate','Dayr az Zawr','SY-DY',''),(3871,219,'Governorate','Dimashq','SY-DI',''),(3872,219,'Governorate','Halab','SY-HL',''),(3873,219,'Governorate','Hamah','SY-HM',''),(3874,219,'Governorate','Homs','SY-HI',''),(3875,219,'Governorate','Idlib','SY-ID',''),(3876,219,'Governorate','Rif Dimashq','SY-RD',''),(3877,219,'Governorate','Tartus','SY-TA',''),(3878,216,'District','Hhohho','SZ-HH',''),(3879,216,'District','Lubombo','SZ-LU',''),(3880,216,'District','Manzini','SZ-MA',''),(3881,216,'District','Shiselweni','SZ-SH',''),(3882,47,'Region','Batha','TD-BA',''),(3883,47,'Region','Borkou-Ennedi-Tibesti','TD-BET',''),(3884,47,'Region','Chari-Baguirmi','TD-CB',''),(3885,47,'Region','Guéra','TD-GR',''),(3886,47,'Region','Hadjer Lamis','TD-HL',''),(3887,47,'Region','Kanem','TD-KA',''),(3888,47,'Region','Lac','TD-LC',''),(3889,47,'Region','Logone-Occidental','TD-LO',''),(3890,47,'Region','Logone-Oriental','TD-LR',''),(3891,47,'Region','Mandoul','TD-MA',''),(3892,47,'Region','Mayo-Kébbi-Est','TD-ME',''),(3893,47,'Region','Mayo-Kébbi-Ouest','TD-MO',''),(3894,47,'Region','Moyen-Chari','TD-MC',''),(3895,47,'Region','Ndjamena','TD-ND',''),(3896,47,'Region','Ouaddaï','TD-OD',''),(3897,47,'Region','Salamat','TD-SA',''),(3898,47,'Region','Tandjilé','TD-TA',''),(3899,47,'Region','Wadi Fira','TD-WF',''),(3900,225,'Region','Région du Centre','TG-C',''),(3901,225,'Region','Région de la Kara','TG-K',''),(3902,225,'Region','Région Maritime','TG-M',''),(3903,225,'Region','Région des Plateaux','TG-P',''),(3904,225,'Region','Région des Savannes','TG-S',''),(3905,223,'Municipality','Krung Thep Maha Nakhon Bangkok','TH-10',''),(3906,223,'Province','Phatthaya','TH-S',''),(3907,223,'Province','Amnat Charoen','TH-37',''),(3908,223,'Province','Ang Thong','TH-15',''),(3909,223,'Province','Buri Ram','TH-31',''),(3910,223,'Province','Chachoengsao','TH-24',''),(3911,223,'Province','Chai Nat','TH-18',''),(3912,223,'Province','Chaiyaphum','TH-36',''),(3913,223,'Province','Chanthaburi','TH-22',''),(3914,223,'Province','Chiang Mai','TH-50',''),(3915,223,'Province','Chiang Rai','TH-57',''),(3916,223,'Province','Chon Buri','TH-20',''),(3917,223,'Province','Chumphon','TH-86',''),(3918,223,'Province','Kalasin','TH-46',''),(3919,223,'Province','Kamphaeng Phet','TH-62',''),(3920,223,'Province','Kanchanaburi','TH-71',''),(3921,223,'Province','Khon Kaen','TH-40',''),(3922,223,'Province','Krabi','TH-81',''),(3923,223,'Province','Lampang','TH-52',''),(3924,223,'Province','Lamphun','TH-51',''),(3925,223,'Province','Loei','TH-42',''),(3926,223,'Province','Lop Buri','TH-16',''),(3927,223,'Province','Mae Hong Son','TH-58',''),(3928,223,'Province','Maha Sarakham','TH-44',''),(3929,223,'Province','Mukdahan','TH-49',''),(3930,223,'Province','Nakhon Nayok','TH-26',''),(3931,223,'Province','Nakhon Pathom','TH-73',''),(3932,223,'Province','Nakhon Phanom','TH-48',''),(3933,223,'Province','Nakhon Ratchasima','TH-30',''),(3934,223,'Province','Nakhon Sawan','TH-60',''),(3935,223,'Province','Nakhon Si Thammarat','TH-80',''),(3936,223,'Province','Nan','TH-55',''),(3937,223,'Province','Narathiwat','TH-96',''),(3938,223,'Province','Nong Bua Lam Phu','TH-39',''),(3939,223,'Province','Nong Khai','TH-43',''),(3940,223,'Province','Nonthaburi','TH-12',''),(3941,223,'Province','Pathum Thani','TH-13',''),(3942,223,'Province','Pattani','TH-94',''),(3943,223,'Province','Phangnga','TH-82',''),(3944,223,'Province','Phatthalung','TH-93',''),(3945,223,'Province','Phayao','TH-56',''),(3946,223,'Province','Phetchabun','TH-67',''),(3947,223,'Province','Phetchaburi','TH-76',''),(3948,223,'Province','Phichit','TH-66',''),(3949,223,'Province','Phitsanulok','TH-65',''),(3950,223,'Province','Phrae','TH-54',''),(3951,223,'Province','Phra Nakhon Si Ayutthaya','TH-14',''),(3952,223,'Province','Phuket','TH-83',''),(3953,223,'Province','Prachin Buri','TH-25',''),(3954,223,'Province','Prachuap Khiri Khan','TH-77',''),(3955,223,'Province','Ranong','TH-85',''),(3956,223,'Province','Ratchaburi','TH-70',''),(3957,223,'Province','Rayong','TH-21',''),(3958,223,'Province','Roi Et','TH-45',''),(3959,223,'Province','Sa Kaeo','TH-27',''),(3960,223,'Province','Sakon Nakhon','TH-47',''),(3961,223,'Province','Samut Prakan','TH-11',''),(3962,223,'Province','Samut Sakhon','TH-74',''),(3963,223,'Province','Samut Songkhram','TH-75',''),(3964,223,'Province','Saraburi','TH-19',''),(3965,223,'Province','Satun','TH-91',''),(3966,223,'Province','Sing Buri','TH-17',''),(3967,223,'Province','Si Sa Ket','TH-33',''),(3968,223,'Province','Songkhla','TH-90',''),(3969,223,'Province','Sukhothai','TH-64',''),(3970,223,'Province','Suphan Buri','TH-72',''),(3971,223,'Province','Surat Thani','TH-84',''),(3972,223,'Province','Surin','TH-32',''),(3973,223,'Province','Tak','TH-63',''),(3974,223,'Province','Trang','TH-92',''),(3975,223,'Province','Trat','TH-23',''),(3976,223,'Province','Ubon Ratchathani','TH-34',''),(3977,223,'Province','Udon Thani','TH-41',''),(3978,223,'Province','Uthai Thani','TH-61',''),(3979,223,'Province','Uttaradit','TH-53',''),(3980,223,'Province','Yala','TH-95',''),(3981,223,'Province','Yasothon','TH-35',''),(3982,221,'Autonomous region','Gorno-Badakhshan','TJ-GB',''),(3983,221,'Region','Khatlon','TJ-KT',''),(3984,221,'Region','Sughd','TJ-SU',''),(3985,224,'District','Aileu','TL-AL',''),(3986,224,'District','Ainaro','TL-AN',''),(3987,224,'District','Baucau','TL-BA',''),(3988,224,'District','Bobonaro','TL-BO',''),(3989,224,'District','Cova Lima','TL-CO',''),(3990,224,'District','Dili','TL-DI',''),(3991,224,'District','Ermera','TL-ER',''),(3992,224,'District','Lautem','TL-LA',''),(3993,224,'District','Liquiça','TL-LI',''),(3994,224,'District','Manatuto','TL-MT',''),(3995,224,'District','Manufahi','TL-MF',''),(3996,224,'District','Oecussi','TL-OE',''),(3997,224,'District','Viqueque','TL-VI',''),(3998,231,'Region','Ahal','TM-A',''),(3999,231,'Region','Balkan','TM-B',''),(4000,231,'Region','Da?oguz','TM-D',''),(4001,231,'Region','Lebap','TM-L',''),(4002,231,'Region','Mary','TM-M',''),(4003,229,'Governorate','Béja','TN-31',''),(4004,229,'Governorate','Ben Arous','TN-13',''),(4005,229,'Governorate','Bizerte','TN-23',''),(4006,229,'Governorate','Gabès','TN-81',''),(4007,229,'Governorate','Gafsa','TN-71',''),(4008,229,'Governorate','Jendouba','TN-32',''),(4009,229,'Governorate','Kairouan','TN-41',''),(4010,229,'Governorate','Kasserine','TN-42',''),(4011,229,'Governorate','Kebili','TN-73',''),(4012,229,'Governorate','L\'Ariana','TN-12',''),(4013,229,'Governorate','Le Kef','TN-33',''),(4014,229,'Governorate','Mahdia','TN-53',''),(4015,229,'Governorate','La Manouba','TN-14',''),(4016,229,'Governorate','Medenine','TN-82',''),(4017,229,'Governorate','Monastir','TN-52',''),(4018,229,'Governorate','Nabeul','TN-21',''),(4019,229,'Governorate','Sfax','TN-61',''),(4020,229,'Governorate','Sidi Bouzid','TN-43',''),(4021,229,'Governorate','Siliana','TN-34',''),(4022,229,'Governorate','Sousse','TN-51',''),(4023,229,'Governorate','Tataouine','TN-83',''),(4024,229,'Governorate','Tozeur','TN-72',''),(4025,229,'Governorate','Tunis','TN-11',''),(4026,229,'Governorate','Zaghouan','TN-22',''),(4027,227,'Division','\'Eua','TO-01',''),(4028,227,'Division','Ha\'apai','TO-02',''),(4029,227,'Division','Niuas','TO-03',''),(4030,227,'Division','Tongatapu','TO-04',''),(4031,227,'Division','Vava\'u','TO-05',''),(4032,230,'Province','Adana','TR-01',''),(4033,230,'Province','Ad?yaman','TR-02',''),(4034,230,'Province','Afyon','TR-03',''),(4035,230,'Province','A?r?','TR-04',''),(4036,230,'Province','Aksaray','TR-68',''),(4037,230,'Province','Amasya','TR-05',''),(4038,230,'Province','Ankara','TR-06',''),(4039,230,'Province','Antalya','TR-07',''),(4040,230,'Province','Ardahan','TR-75',''),(4041,230,'Province','Artvin','TR-08',''),(4042,230,'Province','Ayd?n','TR-09',''),(4043,230,'Province','Bal?kesir','TR-10',''),(4044,230,'Province','Bart?n','TR-74',''),(4045,230,'Province','Batman','TR-72',''),(4046,230,'Province','Bayburt','TR-69',''),(4047,230,'Province','Bilecik','TR-11',''),(4048,230,'Province','Bingöl','TR-12',''),(4049,230,'Province','Bitlis','TR-13',''),(4050,230,'Province','Bolu','TR-14',''),(4051,230,'Province','Burdur','TR-15',''),(4052,230,'Province','Bursa','TR-16',''),(4053,230,'Province','Çanakkale','TR-17',''),(4054,230,'Province','Çank?r?','TR-18',''),(4055,230,'Province','Çorum','TR-19',''),(4056,230,'Province','Denizli','TR-20',''),(4057,230,'Province','Diyarbak?r','TR-21',''),(4058,230,'Province','Düzce','TR-81',''),(4059,230,'Province','Edirne','TR-22',''),(4060,230,'Province','Elaz??','TR-23',''),(4061,230,'Province','Erzincan','TR-24',''),(4062,230,'Province','Erzurum','TR-25',''),(4063,230,'Province','Eski?ehir','TR-26',''),(4064,230,'Province','Gaziantep','TR-27',''),(4065,230,'Province','Giresun','TR-28',''),(4066,230,'Province','Gümü?hane','TR-29',''),(4067,230,'Province','Hakkâri','TR-30',''),(4068,230,'Province','Hatay','TR-31',''),(4069,230,'Province','I?d?r','TR-76',''),(4070,230,'Province','Isparta','TR-32',''),(4071,230,'Province','?çel','TR-33',''),(4072,230,'Province','?stanbul','TR-34',''),(4073,230,'Province','?zmir','TR-35',''),(4074,230,'Province','Kahramanmara?','TR-46',''),(4075,230,'Province','Karabük','TR-78',''),(4076,230,'Province','Karaman','TR-70',''),(4077,230,'Province','Kars','TR-36',''),(4078,230,'Province','Kastamonu','TR-37',''),(4079,230,'Province','Kayseri','TR-38',''),(4080,230,'Province','K?r?kkale','TR-71',''),(4081,230,'Province','K?rklareli','TR-39',''),(4082,230,'Province','K?r?ehir','TR-40',''),(4083,230,'Province','Kilis','TR-79',''),(4084,230,'Province','Kocaeli','TR-41',''),(4085,230,'Province','Konya','TR-42',''),(4086,230,'Province','Kütahya','TR-43',''),(4087,230,'Province','Malatya','TR-44',''),(4088,230,'Province','Manisa','TR-45',''),(4089,230,'Province','Mardin','TR-47',''),(4090,230,'Province','Mu?la','TR-48',''),(4091,230,'Province','Mu?','TR-49',''),(4092,230,'Province','Nev?ehir','TR-50',''),(4093,230,'Province','Ni?de','TR-51',''),(4094,230,'Province','Ordu','TR-52',''),(4095,230,'Province','Osmaniye','TR-80',''),(4096,230,'Province','Rize','TR-53',''),(4097,230,'Province','Sakarya','TR-54',''),(4098,230,'Province','Samsun','TR-55',''),(4099,230,'Province','Siirt','TR-56',''),(4100,230,'Province','Sinop','TR-57',''),(4101,230,'Province','Sivas','TR-58',''),(4102,230,'Province','?anl?urfa','TR-63',''),(4103,230,'Province','??rnak','TR-73',''),(4104,230,'Province','Tekirda?','TR-59',''),(4105,230,'Province','Tokat','TR-60',''),(4106,230,'Province','Trabzon','TR-61',''),(4107,230,'Province','Tunceli','TR-62',''),(4108,230,'Province','U?ak','TR-64',''),(4109,230,'Province','Van','TR-65',''),(4110,230,'Province','Yalova','TR-77',''),(4111,230,'Province','Yozgat','TR-66',''),(4112,230,'Province','Zonguldak','TR-67',''),(4113,228,'Region','Couva-Tabaquite-Talparo','TT-CTT',''),(4114,228,'Region','Diego Martin','TT-DMN',''),(4115,228,'Region','Eastern Tobago','TT-ETO',''),(4116,228,'Region','Penal-Debe','TT-PED',''),(4117,228,'Region','Princes Town','TT-PRT',''),(4118,228,'Region','Rio Claro-Mayaro','TT-RCM',''),(4119,228,'Region','Sangre Grande','TT-SGE',''),(4120,228,'Region','San Juan-Laventille','TT-SJL',''),(4121,228,'Region','Siparia','TT-SIP',''),(4122,228,'Region','Tunapuna-Piarco','TT-TUP',''),(4123,228,'Region','Western Tobago','TT-WTO',''),(4124,228,'Borough','Arima','TT-ARI',''),(4125,228,'Borough','Chaguanas','TT-CHA',''),(4126,228,'Borough','Point Fortin','TT-PTF',''),(4127,228,'City','Port of Spain','TT-POS',''),(4128,228,'City','San Fernando','TT-SFO',''),(4129,233,'Town council','Funafuti','TV-FUN',''),(4130,233,'Island council','Nanumanga','TV-NMG',''),(4131,233,'Island council','Nanumea','TV-NMA',''),(4132,233,'Island council','Niutao','TV-NIT',''),(4133,233,'Island council','Nui','TV-NIU',''),(4134,233,'Island council','Nukufetau','TV-NKF',''),(4135,233,'Island council','Nukulaelae','TV-NKL',''),(4136,233,'Island council','Vaitupu','TV-VAI',''),(4137,220,'District','Changhua','TW-CHA',''),(4138,220,'District','Chiayi','TW-CYQ',''),(4139,220,'District','Hsinchu','TW-HSQ',''),(4140,220,'District','Hualien','TW-HUA',''),(4141,220,'District','Ilan','TW-ILA',''),(4142,220,'District','Kaohsiung','TW-KHQ',''),(4143,220,'District','Miaoli','TW-MIA',''),(4144,220,'District','Nantou','TW-NAN',''),(4145,220,'District','Penghu','TW-PEN',''),(4146,220,'District','Pingtung','TW-PIF',''),(4147,220,'District','Taichung','TW-TXQ',''),(4148,220,'District','Tainan','TW-TNQ',''),(4149,220,'District','Taipei','TW-TPQ',''),(4150,220,'District','Taitung','TW-TTT',''),(4151,220,'District','Taoyuan','TW-TAO',''),(4152,220,'District','Yunlin','TW-YUN',''),(4153,220,'Municipality','Chiay City','TW-CYI',''),(4154,220,'Municipality','Hsinchui City','TW-HSZ',''),(4155,220,'Municipality','Keelung City','TW-KEE',''),(4156,220,'Municipality','Taichung City','TW-TXG',''),(4157,220,'Municipality','Tainan City','TW-TNN',''),(4158,220,'Special Municipality','Kaohsiung City','TW-KHH',''),(4159,220,'Special Municipality','Taipei City','TW-TPE',''),(4160,222,'Region','Arusha','TZ-01',''),(4161,222,'Region','Dar-es-Salaam','TZ-02',''),(4162,222,'Region','Dodoma','TZ-03',''),(4163,222,'Region','Iringa','TZ-04',''),(4164,222,'Region','Kagera','TZ-05',''),(4165,222,'Region','Kaskazini Pemba','TZ-06',''),(4166,222,'Region','Kaskazini Unguja','TZ-07',''),(4167,222,'Region','Kigoma','TZ-08',''),(4168,222,'Region','Kilimanjaro','TZ-09',''),(4169,222,'Region','Kusini Pemba','TZ-10',''),(4170,222,'Region','Kusini Unguja','TZ-11',''),(4171,222,'Region','Lindi','TZ-12',''),(4172,222,'Region','Manyara','TZ-26',''),(4173,222,'Region','Mara','TZ-13',''),(4174,222,'Region','Mbeya','TZ-14',''),(4175,222,'Region','Mjini Magharibi','TZ-15',''),(4176,222,'Region','Morogoro','TZ-16',''),(4177,222,'Region','Mtwara','TZ-17',''),(4178,222,'Region','Mwanza','TZ-18',''),(4179,222,'Region','Pwani','TZ-19',''),(4180,222,'Region','Rukwa','TZ-20',''),(4181,222,'Region','Ruvuma','TZ-21',''),(4182,222,'Region','Shinyanga','TZ-22',''),(4183,222,'Region','Singida','TZ-23',''),(4184,222,'Region','Tabora','TZ-24',''),(4185,222,'Region','Tanga','TZ-25',''),(4186,235,'Province','Cherkas\'ka Oblast\'','UA-71',''),(4187,235,'Province','Chernihivs\'ka Oblast\'','UA-74',''),(4188,235,'Province','Chernivets\'ka Oblast\'','UA-77',''),(4189,235,'Province','Dnipropetrovs\'ka Oblast\'','UA-12',''),(4190,235,'Province','Donets\'ka Oblast\'','UA-14',''),(4191,235,'Province','Ivano-Frankivs\'ka Oblast\'','UA-26',''),(4192,235,'Province','Kharkivs\'ka Oblast\'','UA-63',''),(4193,235,'Province','Khersons\'ka Oblast\'','UA-65',''),(4194,235,'Province','Khmel\'nyts\'ka Oblast\'','UA-68',''),(4195,235,'Province','Kirovohrads\'ka Oblast\'','UA-35',''),(4196,235,'Province','Kyïvs\'ka Oblast\'','UA-32',''),(4197,235,'Province','Luhans\'ka Oblast\'','UA-09',''),(4198,235,'Province','L\'vivs\'ka Oblast\'','UA-46',''),(4199,235,'Province','Mykolaïvs\'ka Oblast\'','UA-48',''),(4200,235,'Province','Odes\'ka Oblast\'','UA-51',''),(4201,235,'Province','Poltavs\'ka Oblast\'','UA-53',''),(4202,235,'Province','Rivnens\'ka Oblast\'','UA-56',''),(4203,235,'Province','Sums \'ka Oblast\'','UA-59',''),(4204,235,'Province','Ternopil\'s\'ka Oblast\'','UA-61',''),(4205,235,'Province','Vinnyts\'ka Oblast\'','UA-05',''),(4206,235,'Province','Volyns\'ka Oblast\'','UA-07',''),(4207,235,'Province','Zakarpats\'ka Oblast\'','UA-21',''),(4208,235,'Province','Zaporiz\'ka Oblast\'','UA-23',''),(4209,235,'Province','Zhytomyrs\'ka Oblast\'','UA-18',''),(4210,235,'Autonomous republic','Respublika Krym','UA-43',''),(4211,235,'City','Kyïvs\'ka mis\'ka rada','UA-30',''),(4212,235,'City','Sevastopol','UA-40',''),(4213,234,'Geographical region','Central','UG C',''),(4214,234,'Geographical region','Eastern','UG E',''),(4215,234,'Geographical region','Northern','UG N',''),(4216,234,'Geographical region','Western','UG W',''),(4217,234,'District','Abim','UG-317',''),(4218,234,'District','Adjumani','UG-301',''),(4219,234,'District','Amolatar','UG-314',''),(4220,234,'District','Amuria','UG-216',''),(4221,234,'District','Amuru','UG-319',''),(4222,234,'District','Apac','UG-302',''),(4223,234,'District','Arua','UG-303',''),(4224,234,'District','Budaka','UG-217',''),(4225,234,'District','Bugiri','UG-201',''),(4226,234,'District','Bukwa','UG-218',''),(4227,234,'District','Bulisa','UG-419',''),(4228,234,'District','Bundibugyo','UG-401',''),(4229,234,'District','Bushenyi','UG-402',''),(4230,234,'District','Busia','UG-202',''),(4231,234,'District','Butaleja','UG-219',''),(4232,234,'District','Dokolo','UG-318',''),(4233,234,'District','Gulu','UG-304',''),(4234,234,'District','Hoima','UG-403',''),(4235,234,'District','Ibanda','UG-416',''),(4236,234,'District','Iganga','UG-203',''),(4237,234,'District','Isingiro','UG-417',''),(4238,234,'District','Jinja','UG-204',''),(4239,234,'District','Kaabong','UG-315',''),(4240,234,'District','Kabale','UG-404',''),(4241,234,'District','Kabarole','UG-405',''),(4242,234,'District','Kaberamaido','UG-213',''),(4243,234,'District','Kalangala','UG-101',''),(4244,234,'District','Kaliro','UG-220',''),(4245,234,'District','Kampala','UG-102',''),(4246,234,'District','Kamuli','UG-205',''),(4247,234,'District','Kamwenge','UG-413',''),(4248,234,'District','Kanungu','UG-414',''),(4249,234,'District','Kapchorwa','UG-206',''),(4250,234,'District','Kasese','UG-406',''),(4251,234,'District','Katakwi','UG-207',''),(4252,234,'District','Kayunga','UG-112',''),(4253,234,'District','Kibaale','UG-407',''),(4254,234,'District','Kiboga','UG-103',''),(4255,234,'District','Kiruhura','UG-418',''),(4256,234,'District','Kisoro','UG-408',''),(4257,234,'District','Kitgum','UG-305',''),(4258,234,'District','Koboko','UG-316',''),(4259,234,'District','Kotido','UG-306',''),(4260,234,'District','Kumi','UG-208',''),(4261,234,'District','Kyenjojo','UG-415',''),(4262,234,'District','Lira','UG-307',''),(4263,234,'District','Luwero','UG-104',''),(4264,234,'District','Manafwa','UG-221',''),(4265,234,'District','Maracha','UG-320',''),(4266,234,'District','Masaka','UG-105',''),(4267,234,'District','Masindi','UG-409',''),(4268,234,'District','Mayuge','UG-214',''),(4269,234,'District','Mbale','UG-209',''),(4270,234,'District','Mbarara','UG-410',''),(4271,234,'District','Mityana','UG-114',''),(4272,234,'District','Moroto','UG-308',''),(4273,234,'District','Moyo','UG-309',''),(4274,234,'District','Mpigi','UG-106',''),(4275,234,'District','Mubende','UG-107',''),(4276,234,'District','Mukono','UG-108',''),(4277,234,'District','Nakapiripirit','UG-311',''),(4278,234,'District','Nakaseke','UG-115',''),(4279,234,'District','Nakasongola','UG-109',''),(4280,234,'District','Namutumba','UG-222',''),(4281,234,'District','Nebbi','UG-310',''),(4282,234,'District','Ntungamo','UG-411',''),(4283,234,'District','Oyam','UG-321',''),(4284,234,'District','Pader','UG-312',''),(4285,234,'District','Pallisa','UG-210',''),(4286,234,'District','Rakai','UG-110',''),(4287,234,'District','Rukungiri','UG-412',''),(4288,234,'District','Sembabule','UG-111',''),(4289,234,'District','Sironko','UG-215',''),(4290,234,'District','Soroti','UG-211',''),(4291,234,'District','Tororo','UG-212',''),(4292,234,'District','Wakiso','UG-113',''),(4293,234,'District','Yumbe','UG-313',''),(4294,239,'Territory','Baker Island','UM-81',''),(4295,239,'Territory','Howland Island','UM-84',''),(4296,239,'Territory','Jarvis Island','UM-86',''),(4297,239,'Territory','Johnston Atoll','UM-67',''),(4298,239,'Territory','Kingman Reef','UM-89',''),(4299,239,'Territory','Midway Islands','UM-71',''),(4300,239,'Territory','Navassa Island','UM-76',''),(4301,239,'Territory','Palmyra Atoll','UM-95',''),(4302,239,'Territory','Wake Island','UM-79',''),(4303,238,'State','Alabama','US-AL',''),(4304,238,'State','Alaska','US-AK',''),(4305,238,'State','Arizona','US-AZ',''),(4306,238,'State','Arkansas','US-AR',''),(4307,238,'State','California','US-CA',''),(4308,238,'State','Colorado','US-CO',''),(4309,238,'State','Connecticut','US-CT',''),(4310,238,'State','Delaware','US-DE',''),(4311,238,'State','Florida','US-FL',''),(4312,238,'State','Georgia','US-GA',''),(4313,238,'State','Hawaii','US-HI',''),(4314,238,'State','Idaho','US-ID',''),(4315,238,'State','Illinois','US-IL',''),(4316,238,'State','Indiana','US-IN',''),(4317,238,'State','Iowa','US-IA',''),(4318,238,'State','Kansas','US-KS',''),(4319,238,'State','Kentucky','US-KY',''),(4320,238,'State','Louisiana','US-LA',''),(4321,238,'State','Maine','US-ME',''),(4322,238,'State','Maryland','US-MD',''),(4323,238,'State','Massachusetts','US-MA',''),(4324,238,'State','Michigan','US-MI',''),(4325,238,'State','Minnesota','US-MN',''),(4326,238,'State','Mississippi','US-MS',''),(4327,238,'State','Missouri','US-MO',''),(4328,238,'State','Montana','US-MT',''),(4329,238,'State','Nebraska','US-NE',''),(4330,238,'State','Nevada','US-NV',''),(4331,238,'State','New Hampshire','US-NH',''),(4332,238,'State','New Jersey','US-NJ',''),(4333,238,'State','New Mexico','US-NM',''),(4334,238,'State','New York','US-NY',''),(4335,238,'State','North Carolina','US-NC',''),(4336,238,'State','North Dakota','US-ND',''),(4337,238,'State','Ohio','US-OH',''),(4338,238,'State','Oklahoma','US-OK',''),(4339,238,'State','Oregon','US-OR',''),(4340,238,'State','Pennsylvania','US-PA',''),(4341,238,'State','Rhode Island','US-RI',''),(4342,238,'State','South Carolina','US-SC',''),(4343,238,'State','South Dakota','US-SD',''),(4344,238,'State','Tennessee','US-TN',''),(4345,238,'State','Texas','US-TX',''),(4346,238,'State','Utah','US-UT',''),(4347,238,'State','Vermont','US-VT',''),(4348,238,'State','Virginia','US-VA',''),(4349,238,'State','Washington','US-WA',''),(4350,238,'State','West Virginia','US-WV',''),(4351,238,'State','Wisconsin','US-WI',''),(4352,238,'State','Wyoming','US-WY',''),(4353,238,'District','District of Columbia','US-DC',''),(4354,238,'Outlying area','American Samoa','US-AS',''),(4355,238,'Outlying area','Guam','US-GU',''),(4356,238,'Outlying area','Northern Mariana Islands','US-MP',''),(4357,238,'Outlying area','Puerto Rico','US-PR',''),(4358,238,'Outlying area','United States Minor Outlying Islands','US-UM',''),(4359,238,'Outlying area','Virgin Islands','US-VI',''),(4360,240,'Department','Artigas','UY-AR',''),(4361,240,'Department','Canelones','UY-CA',''),(4362,240,'Department','Cerro Largo','UY-CL',''),(4363,240,'Department','Colonia','UY-CO',''),(4364,240,'Department','Durazno','UY-DU',''),(4365,240,'Department','Flores','UY-FS',''),(4366,240,'Department','Florida','UY-FD',''),(4367,240,'Department','Lavalleja','UY-LA',''),(4368,240,'Department','Maldonado','UY-MA',''),(4369,240,'Department','Montevideo','UY-MO',''),(4370,240,'Department','Paysandú','UY-PA',''),(4371,240,'Department','Río Negro','UY-RN',''),(4372,240,'Department','Rivera','UY-RV',''),(4373,240,'Department','Rocha','UY-RO',''),(4374,240,'Department','Salto','UY-SA',''),(4375,240,'Department','San José','UY-SJ',''),(4376,240,'Department','Soriano','UY-SO',''),(4377,240,'Department','Tacuarembó','UY-TA',''),(4378,240,'Department','Treinta y Tres','UY-TT',''),(4379,241,'City','Toshkent','UZ-TK',''),(4380,241,'Region','Andijon','UZ-AN',''),(4381,241,'Region','Buxoro','UZ-BU',''),(4382,241,'Region','Farg\'ona','UZ-FA',''),(4383,241,'Region','Jizzax','UZ-JI',''),(4384,241,'Region','Namangan','UZ-NG',''),(4385,241,'Region','Navoiy','UZ-NW',''),(4386,241,'Region','Qashqadaryo','UZ-QA',''),(4387,241,'Region','Samarqand','UZ-SA',''),(4388,241,'Region','Sirdaryo','UZ-SI',''),(4389,241,'Region','Surxondaryo','UZ-SU',''),(4390,241,'Region','Toshkent','UZ-TO',''),(4391,241,'Region','Xorazm','UZ-XO',''),(4392,241,'Republic','Qoraqalpog\'iston Respublikasi','UZ-QR',''),(4393,194,'Parish','Charlotte','VC-01',''),(4394,194,'Parish','Grenadines','VC-06',''),(4395,194,'Parish','Saint Andrew','VC-02',''),(4396,194,'Parish','Saint David','VC-03',''),(4397,194,'Parish','Saint George','VC-04',''),(4398,194,'Parish','Saint Patrick','VC-05',''),(4399,244,'Federal Dependency','Dependencias Federales','VE-W',''),(4400,244,'Federal District','Distrito Federal','VE-A',''),(4401,244,'State','Amazonas','VE-Z',''),(4402,244,'State','Anzoátegui','VE-B',''),(4403,244,'State','Apure','VE-C',''),(4404,244,'State','Aragua','VE-D',''),(4405,244,'State','Barinas','VE-E',''),(4406,244,'State','Bolívar','VE-F',''),(4407,244,'State','Carabobo','VE-G',''),(4408,244,'State','Cojedes','VE-H',''),(4409,244,'State','Delta Amacuro','VE-Y',''),(4410,244,'State','Falcón','VE-I',''),(4411,244,'State','Guárico','VE-J',''),(4412,244,'State','Lara','VE-K',''),(4413,244,'State','Mérida','VE-L',''),(4414,244,'State','Miranda','VE-M',''),(4415,244,'State','Monagas','VE-N',''),(4416,244,'State','Nueva Esparta','VE-O',''),(4417,244,'State','Portuguesa','VE-P',''),(4418,244,'State','Sucre','VE-R',''),(4419,244,'State','Táchira','VE-S',''),(4420,244,'State','Trujillo','VE-T',''),(4421,244,'State','Vargas','VE-X',''),(4422,244,'State','Yaracuy','VE-U',''),(4423,244,'State','Zulia','VE-V',''),(4424,245,'Province','An Giang','VN-44',''),(4425,245,'Province','Bà R?a - V?ng Tàu','VN-43',''),(4426,245,'Province','B?c K?n','VN-53',''),(4427,245,'Province','B?c Giang','VN-54',''),(4428,245,'Province','B?c Liêu','VN-55',''),(4429,245,'Province','B?c Ninh','VN-56',''),(4430,245,'Province','B?n Tre','VN-50',''),(4431,245,'Province','Bình ??nh','VN-31',''),(4432,245,'Province','Bình D??ng','VN-57',''),(4433,245,'Province','Bình Ph??c','VN-58',''),(4434,245,'Province','Bình Thu?n','VN-40',''),(4435,245,'Province','Cà Mau','VN-59',''),(4436,245,'Province','C?n Th?','VN-48',''),(4437,245,'Province','Cao B?ng','VN-04',''),(4438,245,'Province','?à N?ng, thành ph?','VN-60',''),(4439,245,'Province','??c L?k','VN-33',''),(4440,245,'Province','??k Nông','VN-72',''),(4441,245,'Province','?i?n Biên','VN-71',''),(4442,245,'Province','??ng Nai','VN-39',''),(4443,245,'Province','??ng Tháp','VN-45',''),(4444,245,'Province','Gia Lai','VN-30',''),(4445,245,'Province','Hà Giang','VN-03',''),(4446,245,'Province','Hà Nam','VN-63',''),(4447,245,'Province','Hà N?i, th? ?ô','VN-64',''),(4448,245,'Province','Hà Tây','VN-15',''),(4449,245,'Province','Hà T?nh','VN-23',''),(4450,245,'Province','H?i Duong','VN-61',''),(4451,245,'Province','H?i Phòng, thành ph?','VN-62',''),(4452,245,'Province','H?u Giang','VN-73',''),(4453,245,'Province','Hoà Bình','VN-14',''),(4454,245,'Province','H? Chí Minh, thành ph? [Sài Gòn]','VN-65',''),(4455,245,'Province','H?ng Yên','VN-66',''),(4456,245,'Province','Khánh Hòa','VN-34',''),(4457,245,'Province','Kiên Giang','VN-47',''),(4458,245,'Province','Kon Tum','VN-28',''),(4459,245,'Province','Lai Châu','VN-01',''),(4460,245,'Province','Lâm ??ng','VN-35',''),(4461,245,'Province','L?ng S?n','VN-09',''),(4462,245,'Province','Lào Cai','VN-02',''),(4463,245,'Province','Long An','VN-41',''),(4464,245,'Province','Nam ??nh','VN-67',''),(4465,245,'Province','Ngh? An','VN-22',''),(4466,245,'Province','Ninh Bình','VN-18',''),(4467,245,'Province','Ninh Thu?n','VN-36',''),(4468,245,'Province','Phú Th?','VN-68',''),(4469,245,'Province','Phú Yên','VN-32',''),(4470,245,'Province','Qu?ng Bình','VN-24',''),(4471,245,'Province','Qu?ng Nam','VN-27',''),(4472,245,'Province','Qu?ng Ngãi','VN-29',''),(4473,245,'Province','Qu?ng Ninh','VN-13',''),(4474,245,'Province','Qu?ng Tr?','VN-25',''),(4475,245,'Province','Sóc Tr?ng','VN-52',''),(4476,245,'Province','S?n La','VN-05',''),(4477,245,'Province','Tây Ninh','VN-37',''),(4478,245,'Province','Thái Bình','VN-20',''),(4479,245,'Province','Thái Nguyên','VN-69',''),(4480,245,'Province','Thanh Hóa','VN-21',''),(4481,245,'Province','Th?a Thiên-Hu?','VN-26',''),(4482,245,'Province','Ti?n Giang','VN-46',''),(4483,245,'Province','Trà Vinh','VN-51',''),(4484,245,'Province','Tuyên Quang','VN-07',''),(4485,245,'Province','V?nh Long','VN-49',''),(4486,245,'Province','V?nh Phúc','VN-70',''),(4487,245,'Province','Yên Bái','VN-06',''),(4488,242,'Province','Malampa','VU-MAP',''),(4489,242,'Province','Pénama','VU-PAM',''),(4490,242,'Province','Sanma','VU-SAM',''),(4491,242,'Province','Shéfa','VU-SEE',''),(4492,242,'Province','Taféa','VU-TAE',''),(4493,242,'Province','Torba','VU-TOB',''),(4494,195,'District','A\'ana','WS-AA',''),(4495,195,'District','Aiga-i-le-Tai','WS-AL',''),(4496,195,'District','Atua','WS-AT',''),(4497,195,'District','Fa\'asaleleaga','WS-FA',''),(4498,195,'District','Gaga\'emauga','WS-GE',''),(4499,195,'District','Gagaifomauga','WS-GI',''),(4500,195,'District','Palauli','WS-PA',''),(4501,195,'District','Satupa\'itea','WS-SA',''),(4502,195,'District','Tuamasaga','WS-TU',''),(4503,195,'District','Va\'a-o-Fonoti','WS-VF',''),(4504,195,'District','Vaisigano','WS-VS',''),(4505,250,'Governorate','Aby?n','YE-AB',''),(4506,250,'Governorate','\'Adan','YE-AD',''),(4507,250,'Governorate','A? ??li\'','YE-DA',''),(4508,250,'Governorate','Al Bay??\'','YE-BA',''),(4509,250,'Governorate','Al ?udaydah','YE-MU',''),(4510,250,'Governorate','Al Jawf','YE-JA',''),(4511,250,'Governorate','Al Mahrah','YE-MR',''),(4512,250,'Governorate','Al Ma?w?t','YE-MW',''),(4513,250,'Governorate','\'Amr?n','YE-AM',''),(4514,250,'Governorate','Dham?r','YE-DH',''),(4515,250,'Governorate','?a?ramawt','YE-HD',''),(4516,250,'Governorate','?ajjah','YE-HJ',''),(4517,250,'Governorate','Ibb','YE-IB',''),(4518,250,'Governorate','La?ij','YE-LA',''),(4519,250,'Governorate','Ma\'rib','YE-MA',''),(4520,250,'Governorate','?a\'dah','YE-SD',''),(4521,250,'Governorate','?an\'?\'','YE-SN',''),(4522,250,'Governorate','Shabwah','YE-SH',''),(4523,250,'Governorate','T?\'izz','YE-TA',''),(4524,209,'Province','Eastern Cape','ZA-EC',''),(4525,209,'Province','Free State','ZA-FS',''),(4526,209,'Province','Gauteng','ZA-GT',''),(4527,209,'Province','Kwazulu-Natal','ZA-NL',''),(4528,209,'Province','Limpopo','ZA-LP',''),(4529,209,'Province','Mpumalanga','ZA-MP',''),(4530,209,'Province','Northern Cape','ZA-NC',''),(4531,209,'Province','North-West (South Africa)','ZA-NW',''),(4532,209,'Province','Western Cape','ZA-WC',''),(4533,251,'Province','Central','ZM-02',''),(4534,251,'Province','Copperbelt','ZM-08',''),(4535,251,'Province','Eastern','ZM-03',''),(4536,251,'Province','Luapula','ZM-04',''),(4537,251,'Province','Lusaka','ZM-09',''),(4538,251,'Province','Northern','ZM-05',''),(4539,251,'Province','North-Western','ZM-06',''),(4540,251,'Province','Southern (Zambia)','ZM-07',''),(4541,251,'Province','Western','ZM-01',''),(4542,252,'City','Bulawayo','ZW-BU',''),(4543,252,'City','Harare','ZW-HA',''),(4544,252,'Province','Manicaland','ZW-MA',''),(4545,252,'Province','Mashonaland Central','ZW-MC',''),(4546,252,'Province','Mashonaland East','ZW-ME',''),(4547,252,'Province','Mashonaland West','ZW-MW',''),(4548,252,'Province','Masvingo','ZW-MV',''),(4549,252,'Province','Matabeleland North','ZW-MN',''),(4550,252,'Province','Matabeleland South','ZW-MS',''),(4551,252,'Province','Midlands','ZW-MI','');
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
) ENGINE=InnoDB AUTO_INCREMENT=577 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_STATUS_ID`) REFER `study/study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study`
--

LOCK TABLES `study` WRITE;
/*!40000 ALTER TABLE `study` DISABLE KEYS */;
INSERT INTO `study` VALUES (567,'TEST',NULL,NULL,NULL,'Christopher Ellis',NULL,1,1,3,'TST',NULL,NULL,NULL,1,NULL,NULL,NULL,1,5,NULL,NULL,567,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_comp`
--

LOCK TABLES `study_comp` WRITE;
/*!40000 ALTER TABLE `study_comp` DISABLE KEYS */;
INSERT INTO `study_comp` VALUES (9,'ThatTestComponent',NULL,567,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=91865 DEFAULT CHARSET=latin1;
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
  CONSTRAINT `fk_subject_file_subject` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjectuid_padchar`
--

LOCK TABLES `subjectuid_padchar` WRITE;
/*!40000 ALTER TABLE `subjectuid_padchar` DISABLE KEYS */;
INSERT INTO `subjectuid_padchar` VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');
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
INSERT INTO `subjectuid_sequence` VALUES ('Lifepool',0,0),('PathWest Demo Autogen',6,0),('PMH Demonstration',1,0),('RENAL',36,0),('TEST',4,0),('WARTN',5125,0);
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
INSERT INTO `subjectuid_token` VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');
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
INSERT INTO `title_type` VALUES (0,'Unknown',NULL),(1,'Br',NULL),(2,'Capt','Captain'),(3,'Col',NULL),(4,'Cpl',NULL),(5,'Dean',NULL),(6,'Dr',NULL),(7,'Fr',NULL),(8,'Lac',NULL),(9,'Major',NULL),(10,'Miss',NULL),(11,'Mr',NULL),(12,'Mrs',NULL),(13,'Ms',NULL),(14,'Past',NULL),(15,'Prof',NULL),(16,'Pstr',NULL),(17,'Rev',NULL),(18,'Sir',NULL),(19,'Sr',NULL),(20,'Lady',NULL),(21,'Sen','Senator'),(22,'Hons','Hons');
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
INSERT INTO `unit_type` VALUES (1,NULL,'mm','Millimetres (mm)',1,1),(2,NULL,'cm','Centimetres (cm)',1,2),(3,NULL,'m','Metres (m)',1,4),(4,NULL,'g','Grams (g)',4,1),(5,NULL,'kg','Kilograms (kg)',4,2),(6,NULL,'L','Litres (L)',2,1),(7,NULL,'Days','Days',3,6),(8,NULL,'Months','Months',3,7),(9,NULL,'Years','Years',3,8),(10,NULL,'hrs','Hours (hrs)',3,5),(11,NULL,'min','Minutes (min)',3,4),(12,NULL,'s','Seconds (s)',3,1),(13,18,'ug/L','ug/L',5,1),(14,18,'bpm','bpm',999,NULL),(15,18,'g/L','g/L',5,4),(16,18,'fL','fL',999,NULL),(17,18,'feet','feet',1,3),(18,18,'IU/L','IU/L',999,NULL),(19,18,'U','U',999,NULL),(20,18,'Age','Age',3,9),(21,18,'m/L','m/L',999,NULL),(22,18,'pg','pg',999,NULL),(23,18,'pred','pred',999,NULL),(24,18,'Gy','Gy',999,NULL),(25,18,'%','%',7,1),(26,18,'mS','mS',999,NULL),(27,18,'mm/hr','mm/hr',6,1),(28,18,'mg/dl','mg/dl',5,2),(29,18,'mn','mn',999,NULL),(30,18,'mg/L','mg/L',5,3),(31,18,'kgm2','kgm2',999,NULL),(32,18,'mm Hg','mm Hg',999,NULL),(33,18,'kg/m2','kg/m2',5,6),(34,18,'Pipes','Pipes',999,NULL),(35,18,'S','S',3,2),(36,18,'mm/hg','mm/hg',999,NULL),(37,19,'ug/L','ug/L',5,1),(38,19,'bpm','bpm',999,NULL),(39,19,'g/L','g/L',5,4),(40,19,'fL','fL',999,NULL),(41,19,'feet','feet',1,3),(42,19,'IU/L','IU/L',999,NULL),(43,19,'U','U',999,NULL),(44,19,'Age','Age',3,10),(45,19,'m/L','m/L',999,NULL),(46,19,'pg','pg',999,NULL),(47,19,'pred','pred',999,NULL),(48,19,'Gy','Gy',999,NULL),(49,19,'%','%',7,1),(50,19,'mS','mS',999,NULL),(51,19,'mm/hr','mm/hr',6,1),(52,19,'mg/dl','mg/dl',5,2),(53,19,'mn','mn',999,NULL),(54,19,'mg/L','mg/L',5,4),(55,19,'kgm2','kgm2',999,NULL),(56,19,'mm Hg','mm Hg',999,NULL),(57,19,'kg/m2','kg/m2',5,5),(58,19,'Pipes','Pipes',999,NULL),(59,19,'S','S',3,3),(60,19,'mm/hg','mm/hg',999,NULL);
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
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`) USING BTREE,
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  KEY `fk_upload_study` (`STUDY_ID`),
  KEY `fk_upload_ark_function_id` (`ARK_FUNCTION_ID`),
  KEY `fk_upload_payload` (`PAYLOAD_ID`),
  KEY `fk_upload_status` (`STATUS_ID`),
  CONSTRAINT `fk_upload_ark_function_id` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_payload` FOREIGN KEY (`PAYLOAD_ID`) REFERENCES `payload` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `upload_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `study/del';
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
INSERT INTO `upload_status` VALUES (-3,'ERROR_ON_DATA_IMPORT','Error while importing data','While the file passed validation, an error occured during the import of data.  Please contact your system administrator.'),(-2,'ERROR_IN_DATA_VALIDATION','Error while validating data','Error while validating data, prior to uploading'),(-1,'ERROR_IN_FILE_VALIDATION','Error validation file','Error in file format or header values.'),(0,'STATUS_NOT_DEFINED','Status not defined','Status not defined.  This may predate our adding status to uploads'),(1,'AWAITING_VALIDATION','Awaiting Validation','Successfully uploaded to our server, awaiting validation and upload to fields'),(2,'VALIDATED','Successfully validated','Successfully validated.  Awaiting upload into fields'),(3,'COMPLETED','Successfully completed','Successfully completed upload');
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
  PRIMARY KEY (`ID`),
  KEY `fk_upload_type_ark_module` (`ARK_MODULE_ID`),
  CONSTRAINT `fk_upload_type_ark_module` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COMMENT='Reference table to describe the type of an upload';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload_type`
--

LOCK TABLES `upload_type` WRITE;
/*!40000 ALTER TABLE `upload_type` DISABLE KEYS */;
INSERT INTO `upload_type` VALUES (1,'Biospecimen Custom Data','Custom Data to be associated with a biospecimen',5),(2,'Subject Demographic Data',NULL,2),(3,'Study-specific (custom) Data',NULL,2),(4,'Custom Data Sets','Custom Data Sets formerly known as Phenotypic Custom Data',3),(5,'Biocollection Custom Data','Custom Data to be associated with a biospecimen',5),(6,'Subject Consent Data',NULL,2),(7,'Pedigree Data','Pedigree Data associated with the subjects',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pheno_data`
--

LOCK TABLES `pheno_data` WRITE;
/*!40000 ALTER TABLE `pheno_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `pheno_data` ENABLE KEYS */;
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
INSERT INTO `questionnaire_status` VALUES (1,'In Progress','The Questionnaire is being provided with data and not yet completed.'),(2,'Data Entry Completed','Questionnaire data entry is completed and awaiting review.'),(3,'Review Ok','The Questionnaire data was reviewed successfully and questionnaire is now locked from further modification.'),(4,'Review Failed','The Questionnaire data failed review and is needs to be revisited for data correction.'),(5,'Uploaded From File','The Questionnaire data has been update from file, with no further action taken since then.');
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
-- Current Database: `audit`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `audit` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `audit`;

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
) ENGINE=InnoDB AUTO_INCREMENT=211452 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=29550 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lss_consent_history`
--

LOCK TABLES `lss_consent_history` WRITE;
/*!40000 ALTER TABLE `lss_consent_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `lss_consent_history` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biocollection_field`
--

LOCK TABLES `biocollection_field` WRITE;
/*!40000 ALTER TABLE `biocollection_field` DISABLE KEYS */;
INSERT INTO `biocollection_field` VALUES (3,'BioCollection','biocollectionUid','Biocollection UID',1),(4,'BioCollection','comments','Comments',1),(5,'BioCollection','collectionDate','CollectionDate',3),(6,'BioCollection','name','Collection Name',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COMMENT='many2many join biocollection_field and search';
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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_field`
--

LOCK TABLES `biospecimen_field` WRITE;
/*!40000 ALTER TABLE `biospecimen_field` DISABLE KEYS */;
INSERT INTO `biospecimen_field` VALUES (1,'Biospecimen','biospecimenUid','BiospecimenUID',1,1),(2,'Biospecimen','sampleType','Sample Type',4,1),(3,'Biospecimen','sampleDate','Sample Date',3,1),(4,'Biospecimen','sampleTime','Sample Time',3,1),(5,'Biospecimen','processedDate','Processed Date',3,1),(6,'Biospecimen','processedTime','Processed Time',3,1),(7,'Biospecimen','quantity','Quantity',1,1),(8,'Biospecimen','concentration','Concentration',1,1),(9,'Biospecimen','purity','Purity',1,1),(10,'Biospecimen','site','Site',1,0),(16,'Biospecimen','freezer','Freezer',1,0),(17,'Biospecimen','rack','Rack',1,0),(18,'Biospecimen','box','Box',1,0),(19,'Biospecimen','column','Column',1,0),(20,'Biospecimen','row','Row',1,0),(21,'Biospecimen','storedIn','Stored In',4,1),(22,'Biospecimen','grade','Grade',4,1),(23,'Biospecimen','comments','Comments',1,1),(24,'Biospecimen','unit','Unit',1,1),(25,'Biospecimen','treatmentType','Treatment Type',4,1),(26,'Biospecimen','quality','Quality',1,1),(27,'Biospecimen','anticoag','Anticoagulant',4,1),(28,'Biospecimen','status','Status',4,1),(29,'Biospecimen','biospecimenProtocol','Protocol',4,1),(30,'Biospecimen','amount','Amount',1,1),(31,'BioCollection','biocollectionUid','BiocollectionUid',4,1),(32,'BioCollection','name','Biocollection Name',4,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1 COMMENT='many2many join biospecimen_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_field_search`
--

LOCK TABLES `biospecimen_field_search` WRITE;
/*!40000 ALTER TABLE `biospecimen_field_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `biospecimen_field_search` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1 COMMENT='many2many join custom_field_display and search';
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
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demographic_field`
--

LOCK TABLES `demographic_field` WRITE;
/*!40000 ALTER TABLE `demographic_field` DISABLE KEYS */;
INSERT INTO `demographic_field` VALUES (5,'Person','firstName','First Name',1,1),(6,'LinkSubjectStudy','consentDate','Consent Date',3,1),(7,'LinkSubjectStudy','subjectUID','Subject UID',1,1),(8,'Address','postCode','Post Code',1,1),(9,'Person','lastName','Last Name',1,1),(10,'Person','genderType','Sex',4,1),(11,'Person','vitalStatus','Vital Status',4,1),(13,'Person','maritalStatus','Marital Status',4,1),(14,'Person','dateOfBirth','DOB',3,1),(15,'Person','dateOfDeath','Date of Death',3,1),(16,'Person','causeOfDeath','Cause of Death',1,1),(17,'Person','preferredEmail','Preferred Email',1,1),(18,'Person','otherEmail','Other Email',1,1),(19,'Person','dateLastKnownAlive','Last Known Alive',3,1),(20,'Address','addressLine1','Building Name/Unit',1,1),(21,'Address','streetAddress','Street Address',1,1),(22,'Address','city','City',1,1),(23,'Address','country','Country',4,1),(24,'Address','state','State',1,1),(25,'Address','otherState','Other State',1,1),(26,'Address','addressStatus','Address Status',4,1),(27,'Address','addressType','Address Type',4,1),(28,'Phone','phoneNumber','Phone Number',1,0),(29,'Person','titleType','Title',4,1),(30,'Person','maritalStatus','Marital Status',4,1),(31,'Person','contactMethod','Contact Method',4,1),(32,'LinkSubjectStudy','subjectStatus','Status',4,1),(33,'LinkSubjectStudy','consentStatus','Consent Status',4,1),(34,'LinkSubjectStudy','consentType','Consent Type',4,1),(35,'LinkSubjectStudy','consentDownloaded','Consent Downloaded',4,1),(36,'LinkSubjectStudy','consentToPassiveDataGathering','Consent To Passive Data Gathering',4,1),(37,'LinkSubjectStudy','consentToActiveContact','Consent To Active Contact',4,1),(38,'LinkSubjectStudy','consentToUseData','Consent To Use Data',4,1),(39,'LinkSubjectStudy','heardAboutStudy','Heard About Study',1,1),(40,'LinkSubjectStudy','comment','Subject Comments',1,1),(41,'Person','otherEmailStatus','Other Email Status',4,1),(42,'Person','preferredEmailStatus','PreferredEmailStatus',4,1),(43,'Address','dateReceived','Date Address Received',3,1),(44,'Address','comments','Address Comments',1,1),(45,'Address','source','Address Source',1,1),(46,'Phone','phoneType','Phone Type',4,0),(47,'Phone','areaCode','Area Code',1,0),(48,'Phone','phoneStatus','Phone Status',4,0),(49,'Phone','source','Phone Source',1,0),(50,'Phone','dateReceived','Date Received',1,0),(51,'Phone','silentMode','Silent Mode',4,0),(52,'Phone','comment','Phone Comment',1,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=latin1 COMMENT='many2many join demographic_field and search';
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
  PRIMARY KEY (`ID`),
  KEY `fk_qf_df_idx` (`DEMOGRAPHIC_FIELD_ID`),
  KEY `fk_qf_cfd_idx` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `fk_qf_bsf_idx` (`BIOSPECIMEN_FIELD_ID`),
  KEY `fk_qf_bcf_idx` (`BIOCOLLECTION_FIELD_ID`),
  KEY `fk_query_filter_1_idx` (`SEARCH_ID`),
  CONSTRAINT `query_filter_ibfk_1` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_ibfk_2` FOREIGN KEY (`BIOCOLLECTION_FIELD_ID`) REFERENCES `biocollection_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_ibfk_3` FOREIGN KEY (`BIOSPECIMEN_FIELD_ID`) REFERENCES `biospecimen_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `query_filter_ibfk_4` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `query_filter_ibfk_5` FOREIGN KEY (`DEMOGRAPHIC_FIELD_ID`) REFERENCES `demographic_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
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
  CONSTRAINT `report_template_ibfk_1` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `report_template_ibfk_2` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_template`
--

LOCK TABLES `report_template` WRITE;
/*!40000 ALTER TABLE `report_template` DISABLE KEYS */;
INSERT INTO `report_template` VALUES (1,'Study Summary Report','This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>','StudySummaryReport.jrxml',1,23),(2,'Study-level Consent Details Report','This report lists detailed subject information for a particular study based on their consent status at the study-level.','ConsentDetailsReport.jrxml',2,24),(3,'Study Component Consent Details Report','This report lists detailed subject information for a particular study based on their consent status for a specific study component.','ConsentDetailsReport.jrxml',2,25),(4,'Datasets Field Details Report (Data Dictionary)','This report lists detailed field information for a particular study based on their associated phenotypic collection.','DataDictionaryReport.jrxml',3,26),(5,'Study User Role Permissions Report','This report lists all user role and permissions for the study in context.','StudyUserRolePermissions.jrxml',1,33),(6,'Work Researcher Cost Report','This report lists the invoiced total billable item type costs related to a researcher','ResearcherCostReport.jrxml',8,62),(7,'Work Researcher Detail Cost Report','This report lists the invoiced individual billable item costs group by the billable item type related to a researcher','ResearcherDetailCostReport.jrxml',8,62),(8,'Work Study Detail Cost Report','This report lists the invoiced individual billable item costs group by the billable item type related to context study','StudyDetailCostReport.jrxml',8,62),(9,'Biospecimen Summary Report','This report lists the biospecimen summaries for the selected study','BiospecimenSummaryReport.jrxml',5,19),(10,'Biospecimen Detail Report','This report lists the biospecimen study details for the selected study - includes location information','BiospecimenDetailReport.jrxml',5,19);
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
  PRIMARY KEY (`ID`),
  KEY `fk_search_query_grouping` (`TOP_LEVEL_GROUPING_ID`),
  KEY `search_ibfk_1` (`STUDY_ID`),
  CONSTRAINT `search_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode_label`
--

LOCK TABLES `barcode_label` WRITE;
/*!40000 ALTER TABLE `barcode_label` DISABLE KEYS */;
INSERT INTO `barcode_label` VALUES (1,NULL,1,'zebra biospecimen','Generic Zebra Biospecimen Label','D14\nN','P1',1,NULL),(2,NULL,1,'zebra bioCollection','Generic Zebra BioCollection label','D15\nN','P1',1,NULL),(3,NULL,2,'straw barcode','Generic Brady Straw Biospecimen label','DIRECTION 0\nREFERENCE 0,0\nCLS','PRINT 1,1',1,NULL),(4,NULL,1,'zebra biospecimen v2','Generic Zebra Biospecimen Label v2','D14\nN','P1',2,NULL),(5,NULL,1,'straw barcode','Generic Zebra Straw Label','D14\nN','P1',3,NULL),(6,1,NULL,'zebra biospecimen',NULL,'D14\nN','P1',1,'Zebra'),(7,2,NULL,'zebra biospecimen',NULL,'D14\r\nN','P1',1,'1'),(9,563,NULL,'zebra biospecimen',NULL,'D14\r\nN','P1',1,'ZDesigner TLP 2844'),(10,274,NULL,'zebra biospecimen',NULL,'D14\r\nN','P1',1,'ZDesigner TLP 2844'),(12,567,NULL,'zebra biospecimen',NULL,'D14\r\nN','P1',1,'ZDesigner TLP 2844'),(13,409,NULL,'zebra biospecimen',NULL,'D14\nN','P1',1,'brady_bbp_11'),(14,409,NULL,'straw barcode',NULL,'DIRECTION 0\nREFERENCE 0,0\nCLS','PRINT 1,1',1,'brady_bbp_11'),(16,573,NULL,'zebra biospecimen',NULL,'D14\nN','P1',1,'brady_bbp_11');
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
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode_label_data`
--

LOCK TABLES `barcode_label_data` WRITE;
/*!40000 ALTER TABLE `barcode_label_data` DISABLE KEYS */;
INSERT INTO `barcode_label_data` VALUES (42,1,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(43,1,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(44,1,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(45,1,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(46,1,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(47,1,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(48,2,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ID: {$subjectUid} Family ID: ${familyId}','\"','\n'),(49,2,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','ASRB No: ${asrbno}','\"','\n'),(50,2,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Collection Date: ${collectionDate}','\"','\n'),(51,2,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','Researcher: ${refDoctor}','\"','\n'),(52,2,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'\"','DOB: ${dateOfBirth} Sex: {$sex}','\"','\n'),(53,3,'BARCODE',25,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(54,3,'TEXT',25,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(55,3,'TEXT',25,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(56,3,'BARCODE',250,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(57,3,'TEXT',250,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(58,3,'TEXT',250,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(93,4,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(94,4,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(95,4,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(96,4,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n'),(111,5,'B',242,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(112,5,'B',-80,0,'0','1','2','2','75','N',NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(113,5,'A',250,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(114,5,'A',-80,80,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n'),(115,6,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(116,6,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(117,6,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(118,6,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(119,6,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(120,6,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(121,7,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(122,7,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(123,7,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(124,7,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(125,7,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(126,7,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(133,9,'b',390,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(134,9,'A',295,10,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(135,9,'A',290,30,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(136,9,'A',305,50,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(137,9,'A',450,5,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(138,9,'A',450,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(139,10,'b',390,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(140,10,'A',295,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(141,10,'A',290,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(142,10,'A',300,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(143,10,'A',450,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(144,10,'A',450,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(151,12,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(152,12,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(153,12,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(154,12,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(155,12,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(156,12,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(157,13,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(158,13,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(159,13,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(160,13,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(161,13,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(162,13,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(163,14,'BARCODE',25,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(164,14,'TEXT',25,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(165,14,'TEXT',25,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(166,14,'BARCODE',250,120,'\"39\"','96','1','0','2','4',NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(167,14,'TEXT',250,145,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(168,14,'TEXT',250,160,'\"3\"','0','1','1',NULL,NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n'),(174,16,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(175,16,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n'),(176,16,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n'),(177,16,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n'),(178,16,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n'),(179,16,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n');
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
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bio_sampletype`
--

LOCK TABLES `bio_sampletype` WRITE;
/*!40000 ALTER TABLE `bio_sampletype` DISABLE KEYS */;
INSERT INTO `bio_sampletype` VALUES (1,'Blood / Blood','Blood','Blood',4),(2,'Blood / Buffy Coat','Blood','Buffy Coat',5),(3,'Blood / Buffy Coat (ACD)','Blood','Buffy Coat (ACD)',6),(4,'Blood / Buffy Coat (EDTA)','Blood','Buffy Coat (EDTA)',7),(5,'Blood / Buffy Coat (LH)','Blood','Buffy Coat (LH)',8),(6,'Blood / Cord blood','Blood','Cord blood',9),(7,'Blood / EDTA Blood','Blood','EDTA Blood',10),(8,'Blood / Frozen Lymphocytes (F)','Blood','Frozen Lymphocytes (F)',11),(9,'Blood / Lithium Heparin','Blood','Lithium Heparin',12),(10,'Blood / Mothers Blood','Blood','Mothers Blood',13),(11,'Blood / Plasma','Blood','Plasma',15),(12,'Blood / Plasma (ACD)','Blood','Plasma (ACD)',16),(13,'Blood / Plasma (EDTA)','Blood','Plasma (EDTA)',17),(14,'Blood / Plasma (LH)','Blood','Plasma (LH)',18),(15,'Blood / Protein from TL','Blood','Protein from TL',19),(16,'Blood / Red Blood Cells','Blood','Red Blood Cells',20),(17,'Blood / Serum','Blood','Serum',21),(18,'Blood / Transformed lymphoblasts (T)','Blood','Transformed lymphoblasts (T)',22),(19,'Blood / Unprocessed','Blood','Unprocessed',23),(20,'Blood / Whole Blood','Blood','Whole Blood',1),(21,'Blood / Whole Blood (EDTA)','Blood','Whole Blood (EDTA)',24),(22,'Blood / Whole Blood (LH)','Blood','Whole Blood (LH)',25),(23,'Nucleic Acid / Buccal Swab','Nucleic Acid','Buccal Swab',28),(24,'Nucleic Acid / DNA','Nucleic Acid','DNA',29),(25,'Nucleic Acid / DNA from BC','Nucleic Acid','DNA from BC',30),(26,'Nucleic Acid / DNA from TL','Nucleic Acid','DNA from TL',34),(27,'Nucleic Acid / DNA from Tissue','Nucleic Acid','DNA from Tissue',33),(28,'Nucleic Acid / Paxgene RNA','Nucleic Acid','Paxgene RNA',35),(29,'Nucleic Acid / RNA','Nucleic Acid','RNA',36),(30,'Nucleic Acid / Saliva','Nucleic Acid','Saliva',37),(31,'Protein from TL','Protein from TL',NULL,39),(32,'Saliva / Buccal Swab','Saliva','Buccal Swab',41),(33,'Saliva / Buccal Swab (SB)','Saliva','Buccal Swab (SB)',42),(34,'Saliva / Oragene (OS)','Saliva','Oragene (OS)',43),(35,'Saliva / Throat Swab','Saliva','Throat Swab',44),(36,'Saliva','Saliva',NULL,40),(37,'Tissue / Anus','Tissue','Anus',48),(38,'Tissue / Appendix','Tissue','Appendix',49),(39,'Tissue / Brain','Tissue','Brain',52),(40,'Tissue / Breast','Tissue','Breast',53),(41,'Tissue / Breast,Lt','Tissue','Breast,Lt',54),(42,'Tissue / Breast,Rt','Tissue','Breast,Rt',55),(43,'Tissue / Caecum','Tissue','Caecum',56),(44,'Tissue / Colon','Tissue','Colon',57),(45,'Tissue / Colon, ascending','Tissue','Colon, ascending',58),(46,'Tissue / Colon, descending','Tissue','Colon, descending',59),(47,'Tissue / Colon, hepatic flexure','Tissue','Colon, hepatic flexure',60),(48,'Tissue / Colon, nos','Tissue','Colon, nos',61),(49,'Tissue / Colon, sigmoid','Tissue','Colon, sigmoid',63),(50,'Tissue / Colon, spenic flexure','Tissue','Colon, spenic flexure',64),(51,'Tissue / Colon, splenic flexure','Tissue','Colon, splenic flexure',65),(52,'Tissue / Colon, transverse','Tissue','Colon, transverse',66),(53,'Tissue / Descending Colon','Tissue','Descending Colon',67),(54,'Tissue / Duodenum','Tissue','Duodenum',68),(55,'Tissue / Endometrium','Tissue','Endometrium',69),(56,'Tissue / Ileum','Tissue','Ileum',72),(57,'Tissue / Left Tube','Tissue','Left Tube',75),(58,'Tissue / Liver','Tissue','Liver',77),(59,'Tissue / Lung','Tissue','Lung',78),(60,'Tissue / Lymph Node','Tissue','Lymph Node',79),(61,'Tissue / Mesentary','Tissue','Mesentary',80),(62,'Tissue / Oesophagus','Tissue','Oesophagus',82),(63,'Tissue / Omentum','Tissue','Omentum',83),(64,'Tissue / Ovarian Cyst L','Tissue','Ovarian Cyst L',87),(65,'Tissue / Ovarian L','Tissue','Ovarian L',88),(66,'Tissue / Ovarian R','Tissue','Ovarian R',89),(67,'Tissue / Ovary','Tissue','Ovary',90),(68,'Tissue / Pancreas','Tissue','Pancreas',93),(69,'Tissue / Peritoneum, pelvic','Tissue','Peritoneum, pelvic',95),(70,'Tissue / Placenta','Tissue','Placenta',96),(71,'Tissue / Rectal Peritoneal Mass','Tissue','Rectal Peritoneal Mass',98),(72,'Tissue / Recto-sigmoid','Tissue','Recto-sigmoid',99),(73,'Tissue / Rectum','Tissue','Rectum',100),(74,'Tissue / Right Tube','Tissue','Right Tube',102),(75,'Tissue / Small Bowel','Tissue','Small Bowel',105),(76,'Tissue / Spleen','Tissue','Spleen',107),(77,'Tissue / Stomach','Tissue','Stomach',108),(78,'Tissue / Tissue','Tissue','Tissue',109),(79,'Tissue / Uterus','Tissue','Uterus',110),(80,'Tissue / Uterus, endometrium','Tissue','Uterus, endometrium',111),(81,'Urine / Urine','Urine','Urine',114),(82,'Urine','Urine',NULL,113),(83,'Blood / Peripheral Blood Lysed','Blood','Peripheral Blood Lysed',2),(84,'Blood / Peripheral Blood DNA','Blood','Peripheral Blood DNA',3),(85,'DNA / DNA','DNA','DNA',26),(86,'Nucleic Acid / DNA from SF Tissue','Nucleic Acid','DNA from SF Tissue',32),(87,'Tissue / Small bowel, nos','Tissue','Small bowel, nos',106),(88,'Tissue / Peritoneum','Tissue','Peritoneum',94),(89,'Tissue / Ovary, left','Tissue','Ovary, left',91),(90,'Tissue / Fallopian tube, left','Tissue','Fallopian tube, left',70),(91,'Tissue / Fallopian tube, right','Tissue','Fallopian tube, right',71),(92,'Tissue / Whole Blood','Tissue','Whole Blood',112),(93,'Tissue / Bone','Tissue','Bone',51),(94,'Tissue / Ovary, right','Tissue','Ovary, right',92),(95,'Tissue / Colon, rectosigmoid','Tissue','Colon, rectosigmoid',62),(96,'Tears / Tears (left eye)','Tears','Tears (left eye)',45),(97,'Tears / Tears (right eye)','Tears','Tears (right eye)',46),(98,'Tissue / Lens (L)','Tissue','Lens (L)',76),(99,'Tissue / Neurosensory Retina (R)','Tissue','Neurosensory Retina (R)',81),(100,'Tissue / RPE/Choroid (C)','Tissue','RPE/Choroid (C)',103),(101,'Tissue / Optic Nerve (O) ','Tissue','Optic Nerve (O) ',84),(102,'Blood / PBMC-EDTA','Blood','PBMC-EDTA',14),(103,'Tissue / Adrenal Gland','Tissue','Adrenal Gland',47),(105,'Tissue / Orbital Fat (F)','Tissue','Orbital Fat (F)',85),(106,'Tissue / Iris (I)','Tissue','Iris (I)',73),(107,'Tissue / Sacrum','Tissue','Sacrum',104),(108,'Tissue / Bladder','Tissue','Bladder',50),(109,'Tissue / Prostate','Tissue','Prostate',97),(110,'Nucleic Acid / Whole Blood ','Nucleic Acid','Whole Blood ',38),(111,'Tissue / Right Eye','Tissue','Right Eye',101),(112,'Tissue / Other','Tissue','Other',86),(113,'Tissue / Left Eye','Tissue','Left Eye',74),(114,'Nucleic Acid / DNA from FFPE ','Nucleic Acid','DNA from FFPE ',31),(117,'Nucleic Acid / Adrenal Gland','Nucleic Acid','Adrenal Gland',27);
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
  PRIMARY KEY (`ID`),
  KEY `FK_BIOTRANSACTION_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  KEY `FK_BIOTRANSACTION_STATUS_ID` (`STATUS_ID`),
  KEY `FK_BIOTRANSACTION_REQUEST_ID` (`REQUEST_ID`),
  CONSTRAINT `FK_BIOTRANSACTION_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_REQUEST_ID` FOREIGN KEY (`REQUEST_ID`) REFERENCES `access_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_STATUS_ID` FOREIGN KEY (`STATUS_ID`) REFERENCES `bio_transaction_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=269418 DEFAULT CHARSET=latin1;
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
INSERT INTO `bio_transaction_status` VALUES (5,'Aliquoted'),(3,'Completed'),(6,'Delivered'),(1,'Initial Quantity'),(2,'Pending'),(4,'Processed');
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
) ENGINE=InnoDB AUTO_INCREMENT=53783 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=74785 DEFAULT CHARSET=latin1;
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
INSERT INTO `biocollectionuid_padchar` VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');
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
INSERT INTO `biocollectionuid_sequence` VALUES ('PathWest Demo Autogen',3,0),('PMH Demonstration',1,0),('RENAL',36,0),('TEST',20,0),('WARTN',5107,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
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
INSERT INTO `biocollectionuid_token` VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');
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
  CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biocollection` FOREIGN KEY (`BIOCOLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biospecimen` FOREIGN KEY (`PARENT_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_protocol` FOREIGN KEY (`BIOSPECIMEN_PROTOCOL_ID`) REFERENCES `biospecimen_protocol` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_biospecimen_treatment_type_id` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=498197 DEFAULT CHARSET=latin1;
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
INSERT INTO `biospecimen_anticoagulant` VALUES (1,'N/A'),(2,'EDTA'),(3,'Lithium Heparin'),(4,'Sodium Citrate'),(5,'ACD');
/*!40000 ALTER TABLE `biospecimen_anticoagulant` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=99850 DEFAULT CHARSET=latin1;
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
INSERT INTO `biospecimen_grade` VALUES (1,'Extracted'),(2,'Precipitated'),(3,'Immediate'),(4,'Delay < 1 hr'),(5,'Delay < 1  '),(6,'N/A'),(7,'Undiffrentiated'),(9,'Not stated'),(10,'Poor'),(18,'Undiffrent');
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_protocol`
--

LOCK TABLES `biospecimen_protocol` WRITE;
/*!40000 ALTER TABLE `biospecimen_protocol` DISABLE KEYS */;
INSERT INTO `biospecimen_protocol` VALUES (1,'Salting out'),(2,'Unknown'),(3,'RNA'),(4,'Oragene'),(5,'Phenol/chloroform'),(6,'Bead'),(7,'Column'),(8,'Qiagen'),(9,'Machery-Nagel');
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
INSERT INTO `biospecimen_quality` VALUES (1,'Fresh'),(2,'Frozen short term (<6mths)'),(3,'Frozen long term (>6mths)');
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_species`
--

LOCK TABLES `biospecimen_species` WRITE;
/*!40000 ALTER TABLE `biospecimen_species` DISABLE KEYS */;
INSERT INTO `biospecimen_species` VALUES (1,'Human'),(2,'Baboon'),(3,'Cat'),(4,'Cow'),(5,'Dog'),(6,'Goat'),(7,'Mouse'),(8,'Pig'),(9,'Rabbit'),(10,'Rat'),(11,'Sheep');
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
INSERT INTO `biospecimen_status` VALUES (1,'New'),(2,'Archived');
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biospecimen_storage`
--

LOCK TABLES `biospecimen_storage` WRITE;
/*!40000 ALTER TABLE `biospecimen_storage` DISABLE KEYS */;
INSERT INTO `biospecimen_storage` VALUES (1,'0.5ml',0.5,17),(2,'1.5ml',1.5,17),(3,'10ml tube',10,17),(4,'2ml tube',2,17),(5,'2ml',2,17),(6,'50ml tube',50,17),(7,'96 well plate',NULL,NULL),(8,'Large tube',NULL,NULL),(9,'Parrafin Block',NULL,NULL),(10,'6ml tube',NULL,NULL),(11,'.5 mL',NULL,NULL),(12,'2 mL',NULL,NULL),(13,'plate',NULL,NULL),(14,'2 mL tube',NULL,NULL),(15,'0.5 mL tube',NULL,NULL),(16,'Not Stored',NULL,NULL),(17,'2.0ml',NULL,NULL),(18,'0.5 mL',NULL,NULL),(19,'SJOG Paraffin Archive',NULL,NULL),(20,'1.2ml tube',1.2,17);
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
INSERT INTO `biospecimenuid_padchar` VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');
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
INSERT INTO `biospecimenuid_sequence` VALUES ('PathWest Demo Autogen',14,0),('PMH Demonstration',9,0),('RENAL',464,0),('TEST',17,0),('WARTN',50610,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
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
INSERT INTO `biospecimenuid_token` VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');
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
INSERT INTO `cell_status` VALUES (1,'Empty','Cell is empty and available'),(2,'Used','Cell is used and unavailable'),(3,'Held','Cell is held for allocation');
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
) ENGINE=InnoDB AUTO_INCREMENT=3494 DEFAULT CHARSET=latin1;
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
  `BIOSPECIMENKEY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_cell_box_idx` (`BOX_ID`) USING BTREE,
  KEY `fk_inv_cell_biospecimen_idx` (`BIOSPECIMEN_ID`) USING BTREE,
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=305371 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
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
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DESCRIPTION` text,
  `CAPACITY` int(11) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_freezer_tray_idx` (`FREEZER_ID`) USING BTREE,
  CONSTRAINT `FK_inv_rack_inv_freezer` FOREIGN KEY (`FREEZER_ID`) REFERENCES `inv_freezer` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
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
  KEY `fk_study_inv_site_study` (`STUDY_ID`),
  KEY `fk_study_inv_site_inv_site` (`INV_SITE_ID`),
  CONSTRAINT `fk_study_inv_site_inv_site` FOREIGN KEY (`INV_SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treatment_type`
--

LOCK TABLES `treatment_type` WRITE;
/*!40000 ALTER TABLE `treatment_type` DISABLE KEYS */;
INSERT INTO `treatment_type` VALUES (4,'70% Alcohol Fixed'),(10,'Ficoll prep'),(2,'Formalin Fixed'),(1,'Frozen'),(5,'RNA Later'),(6,'RNA later, then Formalin Fixed'),(7,'RNA later, then Snap Frozen'),(3,'Tissue Cultured'),(9,'TRIS EDTA'),(8,'Unprocessed');
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (0,'unit',NULL),(1,'mm',NULL),(7,'fL',NULL),(10,'kg',NULL),(11,'U',NULL),(16,'cm',NULL),(17,'mL',NULL),(19,'pg',NULL),(21,'grams',NULL),(22,'pred',NULL),(23,'Gy',NULL),(27,'%',NULL),(28,'mS',NULL),(31,'mn',NULL),(35,'mm Hg',NULL),(38,'L',NULL),(39,'S',NULL),(40,'m',NULL),(41,'fl',NULL),(45,'vial',NULL),(46,'pcs',NULL),(47,'units',NULL),(100,'ul','microlitre');
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `admin`
--

USE `admin`;

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

--
-- Current Database: `pheno`
--

USE `pheno`;

--
-- Current Database: `audit`
--

USE `audit`;

--
-- Current Database: `reporting`
--

USE `reporting`;

--
-- Current Database: `lims`
--

USE `lims`;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-07-30 12:58:49
