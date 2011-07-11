-- MySQL dump 10.11
--
-- Host: localhost    Database: geno
-- USE geno;
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
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-11  6:24:46
