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
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `collection` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `NAME` varchar(50) default NULL,
  `DESCRIPTION` text,
  `STATUS_ID` int(11) NOT NULL,
  `START_DATE` datetime default NULL,
  `EXPIRY_DATE` datetime default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`STATUS_ID`),
  KEY `fk_collection_status` USING BTREE (`STATUS_ID`),
  CONSTRAINT `collection_ibfk_1` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`STATUS_ID`) REFER `geno/STATUS`(`ID`';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `collection_import`
--

DROP TABLE IF EXISTS `collection_import`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `collection_import` (
  `ID` int(11) NOT NULL auto_increment,
  `COLLECTION_ID` int(11) NOT NULL,
  `MARKER_GROUP_ID` int(11) NOT NULL,
  `IMPORT_TYPE_ID` int(11) NOT NULL,
  `START_TIME` datetime default NULL,
  `FINISH_TIME` datetime default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`COLLECTION_ID`,`MARKER_GROUP_ID`,`IMPORT_TYPE_ID`),
  KEY `fk_collection_import_collection` USING BTREE (`COLLECTION_ID`),
  KEY `fk_collection_import_import_type` USING BTREE (`IMPORT_TYPE_ID`),
  KEY `fk_collection_import_marker_group` USING BTREE (`MARKER_GROUP_ID`),
  CONSTRAINT `collection_import_ibfk_1` FOREIGN KEY (`MARKER_GROUP_ID`) REFERENCES `marker_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `collection_import_ibfk_3` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `collection_import_ibfk_4` FOREIGN KEY (`IMPORT_TYPE_ID`) REFERENCES `import_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `decode_mask`
--

DROP TABLE IF EXISTS `decode_mask`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `decode_mask` (
  `ID` int(11) NOT NULL auto_increment,
  `BIT_POSITION` int(11) NOT NULL,
  `MARKER_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`,`MARKER_ID`,`COLLECTION_ID`),
  KEY `fk_decode_mask_marker` USING BTREE (`MARKER_ID`),
  KEY `fk_decode_mask_collection` USING BTREE (`COLLECTION_ID`),
  CONSTRAINT `decode_mask_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `decode_mask_ibfk_2` FOREIGN KEY (`MARKER_ID`) REFERENCES `marker` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `delimiter_type`
--

DROP TABLE IF EXISTS `delimiter_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `delimiter_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `encoded_data`
--

DROP TABLE IF EXISTS `encoded_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `encoded_data` (
  `ID` int(11) NOT NULL auto_increment,
  `SUBJECT_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `ENCODED_BIT1` longblob,
  `ENCODED_BIT2` longblob,
  PRIMARY KEY  (`ID`,`SUBJECT_ID`,`COLLECTION_ID`),
  KEY `fk_encoded_data_collection` USING BTREE (`COLLECTION_ID`),
  CONSTRAINT `encoded_data_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `file_format`
--

DROP TABLE IF EXISTS `file_format`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `file_format` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `import_type`
--

DROP TABLE IF EXISTS `import_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `import_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `marker`
--

DROP TABLE IF EXISTS `marker`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `marker` (
  `ID` int(11) NOT NULL auto_increment,
  `MARKER_GROUP_ID` int(11) NOT NULL,
  `NAME` varchar(100) default NULL,
  `DESCRIPTION` text,
  `CHROMOSOME` varchar(50) NOT NULL,
  `POSITION` decimal(65,30) default NULL,
  `GENE` varchar(100) default NULL,
  `MAJOR_ALLELE` varchar(10) default NULL,
  `MINOR_ALLELE` varchar(10) default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`MARKER_GROUP_ID`),
  KEY `fk_marker_marker_group` USING BTREE (`MARKER_GROUP_ID`),
  CONSTRAINT `marker_ibfk_1` FOREIGN KEY (`MARKER_GROUP_ID`) REFERENCES `marker_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_GROUP_ID`) REFER `geno/MARKER';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `marker_group`
--

DROP TABLE IF EXISTS `marker_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `marker_group` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `MARKER_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(100) default NULL,
  `DESCRIPTION` text,
  `VISIBLE` decimal(1,0) default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`MARKER_TYPE_ID`),
  KEY `fk_marker_group_marker_type` USING BTREE (`MARKER_TYPE_ID`),
  KEY `ID` (`ID`),
  CONSTRAINT `marker_group_ibfk_1` FOREIGN KEY (`MARKER_TYPE_ID`) REFERENCES `marker_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_TYPE_ID`) REFER `geno/MARKER_';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `marker_meta_data`
--

DROP TABLE IF EXISTS `marker_meta_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `marker_meta_data` (
  `ID` int(11) NOT NULL auto_increment,
  `META_DATA_ID` int(11) NOT NULL,
  `MARKER_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`META_DATA_ID`,`MARKER_ID`),
  KEY `fk_marker_meta_data_marker` USING BTREE (`MARKER_ID`),
  KEY `fk_marker_meta_data_meta_data` USING BTREE (`META_DATA_ID`),
  CONSTRAINT `marker_meta_data_ibfk_1` FOREIGN KEY (`MARKER_ID`) REFERENCES `marker` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `marker_meta_data_ibfk_2` FOREIGN KEY (`META_DATA_ID`) REFERENCES `meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_ID`) REFER `geno/MARKER`(`ID`';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `marker_type`
--

DROP TABLE IF EXISTS `marker_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `marker_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `meta_data`
--

DROP TABLE IF EXISTS `meta_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `meta_data` (
  `ID` int(11) NOT NULL auto_increment,
  `META_DATA_FIELD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `VALUE` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`META_DATA_FIELD_ID`,`COLLECTION_ID`),
  KEY `fk_meta_data_meta_data_field` USING BTREE (`META_DATA_FIELD_ID`),
  KEY `fk_meta_data_collection` USING BTREE (`COLLECTION_ID`),
  CONSTRAINT `meta_data_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `meta_data_ibfk_2` FOREIGN KEY (`META_DATA_FIELD_ID`) REFERENCES `meta_data_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `meta_data_field`
--

DROP TABLE IF EXISTS `meta_data_field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `meta_data_field` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `META_DATA_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `UNITS` varchar(50) default NULL,
  `SEQ_NUM` decimal(65,30) default NULL,
  `MIN_VALUE` varchar(100) default NULL,
  `MAX_VALUE` varchar(100) default NULL,
  `DISCRETE_VALUES` varchar(100) default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`STUDY_ID`,`META_DATA_TYPE_ID`),
  KEY `fk_meta_data_field_md_type` USING BTREE (`META_DATA_TYPE_ID`),
  CONSTRAINT `meta_data_field_ibfk_1` FOREIGN KEY (`META_DATA_TYPE_ID`) REFERENCES `meta_data_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`META_DATA_TYPE_ID`) REFER `geno/META';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `meta_data_type`
--

DROP TABLE IF EXISTS `meta_data_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `meta_data_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `status` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subject_marker_meta_data`
--

DROP TABLE IF EXISTS `subject_marker_meta_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `subject_marker_meta_data` (
  `ID` int(11) NOT NULL auto_increment,
  `SUBJECT_ID` int(11) NOT NULL,
  `META_DATA_ID` int(11) NOT NULL,
  `MARKER_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`SUBJECT_ID`,`META_DATA_ID`,`MARKER_ID`),
  KEY `fk_subject_marker_meta_data_meta_data` USING BTREE (`META_DATA_ID`),
  KEY `fk_subject_marker_meta_data_marker` USING BTREE (`MARKER_ID`),
  CONSTRAINT `subject_marker_meta_data_ibfk_1` FOREIGN KEY (`MARKER_ID`) REFERENCES `marker` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `subject_marker_meta_data_ibfk_2` FOREIGN KEY (`META_DATA_ID`) REFERENCES `meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_ID`) REFER `geno/MARKER`(`ID`';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subject_meta_data`
--

DROP TABLE IF EXISTS `subject_meta_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `subject_meta_data` (
  `ID` int(11) NOT NULL auto_increment,
  `SUBJECT_ID` int(11) NOT NULL,
  `META_DATA_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`SUBJECT_ID`,`META_DATA_ID`),
  KEY `fk_subject_meta_data_meta_data` USING BTREE (`META_DATA_ID`),
  CONSTRAINT `subject_meta_data_ibfk_1` FOREIGN KEY (`META_DATA_ID`) REFERENCES `meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`META_DATA_ID`) REFER `geno/META_DATA';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `upload` (
  `ID` int(11) NOT NULL auto_increment,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob,
  `CHECKSUM` varchar(20) default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`FILE_FORMAT_ID`),
  KEY `fk_upload_file_format` USING BTREE (`FILE_FORMAT_ID`),
  CONSTRAINT `upload_ibfk_1` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`FILE_FORMAT_ID`) REFER `geno/FILE_FO';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `upload_collection`
--

DROP TABLE IF EXISTS `upload_collection`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `upload_collection` (
  `ID` int(11) NOT NULL auto_increment,
  `UPLOAD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) default NULL,
  `INSERT_TIME` datetime default NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`COLLECTION_ID`,`UPLOAD_ID`),
  KEY `fk_upload_collection_collection` USING BTREE (`COLLECTION_ID`),
  KEY `fk_upload_collection_upload` USING BTREE (`UPLOAD_ID`),
  CONSTRAINT `upload_collection_ibfk_1` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `upload_collection_ibfk_2` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`COLLECTION_ID`) REFER `geno/COLLECTI';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `upload_marker_group`
--

DROP TABLE IF EXISTS `upload_marker_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `upload_marker_group` (
  `ID` int(11) NOT NULL auto_increment,
  `UPLOAD_ID` int(11) NOT NULL,
  `MARKER_GROUP_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime default NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`UPLOAD_ID`,`MARKER_GROUP_ID`),
  KEY `fk_upload_marker_group_upload` USING BTREE (`UPLOAD_ID`),
  KEY `fk_upload_marker_group_marker_group` USING BTREE (`MARKER_GROUP_ID`),
  CONSTRAINT `upload_marker_group_ibfk_1` FOREIGN KEY (`MARKER_GROUP_ID`) REFERENCES `marker_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `upload_marker_group_ibfk_2` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 6144 kB; (`MARKER_GROUP_ID`) REFER `geno/MARKER';
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

-- Dump completed on 2011-07-11  5:39:52
