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

-- Dump completed on 2011-11-09 10:12:23
