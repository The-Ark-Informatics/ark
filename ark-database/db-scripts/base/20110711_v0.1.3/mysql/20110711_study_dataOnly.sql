-- MySQL dump 10.11
--
-- Host: localhost    Database: study
-- USE study;
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
INSERT INTO `address_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'HOME',NULL),(2,'WORK',NULL);
/*!40000 ALTER TABLE `address_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_function`
--

LOCK TABLES `ark_function` WRITE;
/*!40000 ALTER TABLE `ark_function` DISABLE KEYS */;
INSERT INTO `ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (1,'STUDY','Study Management  usecase. This is represented via the Study Detail Tab under the main Study  Tab. ',1,'tab.module.study.details'),(2,'STUDY_COMPONENT','Study Component usecase.This is represented via the StudyComponent Tab under the main Study  Tab. ',1,'tab.module.study.components'),(3,'MY_DETAIL','Edit My details usecase, this is represented via My Detail tab.',1,'tab.module.mydetails'),(4,'USER','User management usecase. This is represented via the User Tab under the main Study  Tab.',1,'tab.module.user.management'),(5,'SUBJECT','Subject management usecase. This is represented via the Subject Tab under the main Study  Tab.',1,'tab.module.subject.detail'),(6,'PHONE','Manage phone usecase. This is represented via the Phone tab under the main Study  Tab.',1,'tab.module.person.phone'),(7,'ADDRESS','Manage Address',1,'tab.module.person.address'),(8,'ATTACHMENT','Manage Consent and Component attachments. This is represented via the Attachment tab under Subject Main tab.',1,'tab.module.subject.subjectFile'),(9,'CONSENT','Manage Subject Consents. This is represented via the Consent tab under the main Study  Tab.',1,'tab.module.subject.consent'),(10,'SUBJECT_UPLOAD','Bulk upload of Subjects.',1,'tab.module.subject.subjectUpload'),(11,'SUBJECT_CUSTOM','Manage Custom Fields for Subjects.',1,'tab.module.subject.subjectcustom'),(12,'DATA_DICTIONARY','Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.field'),(13,'DATA_DICTIONARY_UPLOAD','Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldUpload'),(14,'PHENO_COLLECTION','Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.collection'),(15,'FIELD_DATA','Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.fieldData'),(16,'FIELD_DATA_UPLOAD','Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.',1,'tab.module.phenotypic.phenoUpload'),(17,'LIMS_SUBJECT','LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.',1,'tab.module.lims.subject.detail'),(18,'LIMS_COLLECTION','LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.',1,'tab.module.lims.collection'),(19,'BIOSPECIMEN','LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.',1,'tab.module.lims.biospecimen'),(20,'INVENTORY','LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.',1,'tab.module.lims.inventory'),(21,'CORRESPONDENCE','',1,'tab.module.subject.correspondence'),(22,'SUMMARY','Phenotypic Summary.',1,'tab.module.phenotypic.summary'),(23,'REPORT_STUDYSUMARY','Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>',2,NULL),(24,'REPORT_STUDYLEVELCONSENT','Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.',2,NULL),(25,'REPORT_STUDYCOMPCONSENT','Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.',2,NULL),(26,'REPORT_PHENOFIELDDETAILS','Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic collection.',2,NULL),(27,'GENO_COLLECTION','Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu',1,'tab.module.geno.collection');
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
INSERT INTO `ark_module` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Study',NULL),(2,'Subject',NULL),(3,'Phenotypic',NULL),(4,'Genotypic',NULL),(5,'LIMS',NULL),(6,'Report',NULL);
/*!40000 ALTER TABLE `ark_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module_function`
--

LOCK TABLES `ark_module_function` WRITE;
/*!40000 ALTER TABLE `ark_module_function` DISABLE KEYS */;
INSERT INTO `ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (1,1,1,1),(2,1,2,2),(3,1,3,4),(4,1,4,3),(5,2,5,1),(6,2,6,2),(7,2,7,3),(8,2,8,4),(9,2,9,5),(10,2,10,7),(11,2,11,8),(12,2,21,6),(13,3,12,2),(14,3,13,3),(15,3,14,4),(16,3,15,5),(17,3,16,6),(18,5,17,1),(19,5,18,2),(20,5,19,3),(21,5,20,4),(22,3,22,NULL),(23,1,23,NULL),(24,2,24,NULL),(25,2,25,NULL),(26,3,26,NULL),(27,4,27,1);
/*!40000 ALTER TABLE `ark_module_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_module_role`
--

LOCK TABLES `ark_module_role` WRITE;
/*!40000 ALTER TABLE `ark_module_role` DISABLE KEYS */;
INSERT INTO `ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES (1,1,2),(2,1,3),(3,2,4),(4,2,5),(5,2,6),(6,3,7),(7,3,8),(8,5,9),(9,5,10),(10,4,11),(11,5,12),(12,3,13);
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
INSERT INTO `ark_role` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Super Administrator',NULL),(2,'Study Administrator',NULL),(3,'Study Read-Only user',NULL),(4,'Subject Administrator',NULL),(5,'Subject Data Manager',NULL),(6,'Subject Read-Only user',NULL),(7,'Pheno Read-Only user',NULL),(8,'Pheno Data Manager',NULL),(9,'LIMS Read-Only user',NULL),(10,'LIMS Data Manager',NULL),(11,'Geno Read-Only User',NULL),(12,'LIMS Administrator',NULL),(13,'Pheno Administrator',NULL);
/*!40000 ALTER TABLE `ark_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ark_role_policy_template`
--

LOCK TABLES `ark_role_policy_template` WRITE;
/*!40000 ALTER TABLE `ark_role_policy_template` DISABLE KEYS */;
INSERT INTO `ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (1,1,NULL,NULL,1),(2,1,NULL,NULL,2),(3,1,NULL,NULL,3),(4,1,NULL,NULL,4),(5,2,1,1,2),(6,2,1,1,3),(7,2,1,2,1),(8,2,1,2,2),(9,2,1,2,3),(10,2,1,3,2),(11,2,1,3,3),(12,2,1,4,1),(13,2,1,4,2),(14,2,1,4,3),(15,3,1,1,2),(16,3,1,2,2),(18,3,1,3,2),(19,3,1,3,3),(20,4,2,5,1),(21,4,2,5,2),(22,4,2,5,3),(23,4,2,6,1),(24,4,2,6,2),(25,4,2,6,3),(26,4,2,6,4),(27,4,2,7,1),(28,4,2,7,2),(29,4,2,7,3),(30,4,2,7,4),(31,4,2,8,1),(32,4,2,8,2),(33,4,2,8,3),(34,4,2,8,4),(35,4,2,9,1),(36,4,2,9,2),(37,4,2,9,3),(38,4,2,9,4),(39,5,2,5,2),(40,5,2,6,2),(41,5,2,6,3),(42,5,2,7,2),(43,5,2,7,3),(44,5,2,8,2),(45,5,2,8,3),(46,5,2,9,2),(47,5,2,9,3),(48,4,2,10,1),(49,4,2,10,2),(50,4,2,10,3),(51,4,2,11,1),(52,4,2,11,2),(53,4,2,11,3),(54,5,2,5,1),(55,5,2,5,3),(56,5,2,6,1),(57,5,2,6,4),(58,5,2,7,1),(59,5,2,7,4),(60,5,2,8,1),(61,5,2,8,4),(62,5,2,9,1),(63,5,2,9,4),(64,6,2,5,2),(65,6,2,6,2),(66,6,2,7,2),(67,6,2,8,2),(68,6,2,9,2),(69,6,2,11,2),(70,8,3,12,2),(71,8,3,13,2),(73,8,3,15,2),(74,8,3,16,1),(75,10,5,17,3),(76,10,5,18,3),(77,10,5,19,3),(78,10,5,20,3),(79,9,5,17,2),(80,9,5,18,2),(81,9,5,19,2),(82,9,5,20,2),(83,7,3,12,2),(84,7,3,13,2),(85,7,3,14,2),(86,7,3,15,2),(87,7,3,16,2),(88,5,2,10,1),(89,8,3,22,2),(90,7,3,22,3),(91,10,5,17,4),(92,2,1,23,2),(93,3,1,23,2),(94,4,2,24,2),(95,5,2,24,2),(96,6,2,24,2),(97,4,2,25,2),(98,5,2,25,2),(99,6,2,25,2),(100,7,3,26,2),(101,8,3,26,2),(102,10,5,17,4),(103,10,5,19,4),(104,11,4,27,2),(106,8,3,14,2),(107,6,2,21,2),(108,5,2,21,1),(109,5,2,21,2),(110,5,2,21,3),(111,5,2,21,4),(112,4,2,21,1),(113,4,2,21,2),(114,4,2,21,3),(115,4,2,21,4),(116,12,5,17,1),(117,12,5,17,2),(118,12,5,17,3),(119,12,5,17,4),(120,12,5,18,1),(121,12,5,18,2),(122,12,5,18,3),(123,12,5,18,4),(124,12,5,19,1),(125,12,5,19,2),(126,12,5,19,3),(127,12,5,19,4),(128,12,5,20,1),(129,12,5,20,2),(130,12,5,20,3),(131,12,5,20,4),(132,13,3,12,1),(133,13,3,13,1),(134,13,3,14,1),(135,13,3,15,3),(136,13,3,16,1),(137,13,3,22,4),(138,13,3,26,2),(139,13,3,14,3),(140,13,3,14,2),(141,8,3,15,3);
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
INSERT INTO `consent_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'Consented',NULL),(2,'Not Consented',NULL),(3,'Ineligible',NULL),(4,'Refused',NULL),(5,'Withdrawn',NULL);
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
-- Dumping data for table `data_type`
--

LOCK TABLES `data_type` WRITE;
/*!40000 ALTER TABLE `data_type` DISABLE KEYS */;
INSERT INTO `data_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'TEXT',NULL),(2,'NUMBER',NULL),(3,'DATE',NULL);
/*!40000 ALTER TABLE `data_type` ENABLE KEYS */;
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
-- Dumping data for table `title_type`
--

LOCK TABLES `title_type` WRITE;
/*!40000 ALTER TABLE `title_type` DISABLE KEYS */;
INSERT INTO `title_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0,'Unknown',NULL),(1,'Br',NULL),(2,'Capt','Captain'),(3,'Col',NULL),(4,'Cpl',NULL),(5,'Dean',NULL),(6,'Dr',NULL),(7,'Fr',NULL),(8,'Lac',NULL),(9,'Major',NULL),(10,'Miss',NULL),(11,'Mr',NULL),(12,'Mrs',NULL),(13,'Ms.',NULL),(14,'Past',NULL),(15,'Prof',NULL),(16,'Pstr',NULL),(17,'Rev',NULL),(18,'Sir',NULL),(19,'Sr',NULL);
/*!40000 ALTER TABLE `title_type` ENABLE KEYS */;
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-11  6:20:03
