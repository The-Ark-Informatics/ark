-- MySQL dump 10.11
--
-- Host: localhost    Database: pheno
-- USE pheno;
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
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `STATUS_ID` int(11) NOT NULL,
  `START_DATE` datetime default NULL,
  `END_DATE` datetime default NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_collection_status` USING BTREE (`STATUS_ID`),
  KEY `fk_collection_study` (`STUDY_ID`),
  CONSTRAINT `fk_collection_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STATUS_ID`) REFER `pheno/status`(`ID';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `collection_upload`
--

DROP TABLE IF EXISTS `collection_upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `collection_upload` (
  `ID` int(11) NOT NULL auto_increment,
  `UPLOAD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_collection_upload_file_upload` USING BTREE (`UPLOAD_ID`),
  KEY `fk_collection_upload_collection` USING BTREE (`COLLECTION_ID`),
  CONSTRAINT `fk_collection_upload_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_collection_upload_upload` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
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
  `DESCRIPTION` text,
  `DELIMITER_CHARACTER` varchar(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `FIELD_TYPE_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `UNITS` varchar(50) default NULL,
  `SEQ_NUM` int(11) default NULL,
  `MIN_VALUE` varchar(100) default NULL,
  `MAX_VALUE` varchar(100) default NULL,
  `ENCODED_VALUES` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  `QUALITY_CONTROL_STATUS` int(11) NOT NULL default '1',
  `MISSING_VALUE` varchar(45) default NULL,
  PRIMARY KEY  (`ID`,`FIELD_TYPE_ID`),
  KEY `fk_field_field_type` USING BTREE (`FIELD_TYPE_ID`),
  KEY `ID` USING BTREE (`ID`),
  KEY `NAME` USING BTREE (`NAME`),
  CONSTRAINT `field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_TYPE_ID`) REFER `pheno/field_t';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_collection`
--

DROP TABLE IF EXISTS `field_collection`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_collection` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `COLLECTION_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_field_collection_study` USING BTREE (`STUDY_ID`),
  KEY `fk_field_collection_field` USING BTREE (`FIELD_ID`),
  KEY `fk_field_collection_collection` USING BTREE (`COLLECTION_ID`),
  CONSTRAINT `fk_field_collection_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_field_collection_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_data`
--

DROP TABLE IF EXISTS `field_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_data` (
  `ID` int(11) NOT NULL auto_increment,
  `COLLECTION_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `DATE_COLLECTED` datetime NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `VALUE` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  `PASSED_QUALITY_CONTROL` int(11) NOT NULL default '1',
  PRIMARY KEY  (`ID`),
  KEY `fk_field_data_collection` (`COLLECTION_ID`),
  KEY `fk_field_data_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_field_data_field` (`FIELD_ID`),
  CONSTRAINT `fk_field_data_collection` FOREIGN KEY (`COLLECTION_ID`) REFERENCES `collection` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_field_data_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_field_data_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`COLLECTION_ID`) REFER `pheno/collect';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_data_log`
--

DROP TABLE IF EXISTS `field_data_log`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_data_log` (
  `ID` int(11) NOT NULL auto_increment,
  `FIELD_DATA_ID` int(11) NOT NULL,
  `COMMENT` varchar(50) NOT NULL,
  `VALUE` text,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_field_group`
--

DROP TABLE IF EXISTS `field_field_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_field_group` (
  `ID` int(11) NOT NULL auto_increment,
  `FIELD_GROUP_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`,`FIELD_GROUP_ID`,`FIELD_ID`),
  KEY `fk_field_groups_field_group1` USING BTREE (`FIELD_GROUP_ID`),
  KEY `fk_field_groups_field` USING BTREE (`FIELD_ID`),
  CONSTRAINT `field_field_group_ibfk_1` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `field_field_group_ibfk_2` FOREIGN KEY (`FIELD_GROUP_ID`) REFERENCES `field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_ID`) REFER `pheno/field`(`ID`)';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_group`
--

DROP TABLE IF EXISTS `field_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_group` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text,
  `STUDY_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_group_upload`
--

DROP TABLE IF EXISTS `field_group_upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_group_upload` (
  `ID` int(11) NOT NULL auto_increment,
  `UPLOAD_ID` int(11) NOT NULL,
  `FIELD_GROUP_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_field_group_upload_upload` USING BTREE (`UPLOAD_ID`),
  KEY `fk_field_group_upload_field_field_group` USING BTREE (`FIELD_GROUP_ID`),
  CONSTRAINT `field_group_upload_ibfk_1` FOREIGN KEY (`FIELD_GROUP_ID`) REFERENCES `field_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `field_group_upload_ibfk_2` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_GROUP_ID`) REFER `pheno/field_';
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `field_summary`
--

DROP TABLE IF EXISTS `field_summary`;
/*!50001 DROP VIEW IF EXISTS `field_summary`*/;
/*!50001 CREATE TABLE `field_summary` (
  `study_id` int(11),
  `fields` bigint(21),
  `fields_with_data` bigint(21)
) ENGINE=MyISAM */;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `field_upload`
--

DROP TABLE IF EXISTS `field_upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `field_upload` (
  `ID` int(11) NOT NULL auto_increment,
  `UPLOAD_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_field_groups_field` USING BTREE (`FIELD_ID`),
  KEY `fk_field_upload_upload` USING BTREE (`UPLOAD_ID`),
  CONSTRAINT `fk_field_upload_field` FOREIGN KEY (`FIELD_ID`) REFERENCES `field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_field_upload_upload` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`FIELD_ID`) REFER `pheno/field`(`ID`)';
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `field_upload_v`
--

DROP TABLE IF EXISTS `field_upload_v`;
/*!50001 DROP VIEW IF EXISTS `field_upload_v`*/;
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

--
-- Table structure for table `file_format`
--

DROP TABLE IF EXISTS `file_format`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `file_format` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
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
  `NAME` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `upload` (
  `ID` int(11) NOT NULL auto_increment,
  `STUDY_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) default NULL,
  `UPDATE_TIME` datetime default NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime default NULL,
  `UPLOAD_REPORT` longblob,
  `UPLOAD_TYPE` varchar(45) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_upload_file_format` USING BTREE (`FILE_FORMAT_ID`),
  KEY `fk_upload_delimiter` USING BTREE (`DELIMITER_TYPE_ID`),
  KEY `ID` USING BTREE (`ID`),
  KEY `fk_upload_study` USING BTREE (`STUDY_ID`),
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `pheno/del';
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `field_summary`
--

/*!50001 DROP TABLE `field_summary`*/;
/*!50001 DROP VIEW IF EXISTS `field_summary`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`192.168.113.%` SQL SECURITY DEFINER */
/*!50001 VIEW `field_summary` AS select `f`.`STUDY_ID` AS `study_id`,count(`f`.`ID`) AS `fields`,(select count(distinct `fd`.`FIELD_ID`) AS `count(distinct ``fd``.``FIELD_ID``)` from (`field_data` `fd` join `field_collection` `fc`) where ((`fd`.`COLLECTION_ID` = `fc`.`COLLECTION_ID`) and (`fc`.`STUDY_ID` = `f`.`STUDY_ID`))) AS `fields_with_data` from `field` `f` group by `f`.`STUDY_ID` */;

--
-- Final view structure for view `field_upload_v`
--

/*!50001 DROP TABLE `field_upload_v`*/;
/*!50001 DROP VIEW IF EXISTS `field_upload_v`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`192.168.113.%` SQL SECURITY DEFINER */
/*!50001 VIEW `field_upload_v` AS select distinct `upload`.`ID` AS `ID`,`upload`.`STUDY_ID` AS `STUDY_ID`,`upload`.`FILE_FORMAT_ID` AS `FILE_FORMAT_ID`,`upload`.`DELIMITER_TYPE_ID` AS `DELIMITER_TYPE_ID`,`upload`.`FILENAME` AS `FILENAME`,`upload`.`PAYLOAD` AS `PAYLOAD`,`upload`.`CHECKSUM` AS `CHECKSUM`,`upload`.`USER_ID` AS `USER_ID`,`upload`.`INSERT_TIME` AS `INSERT_TIME`,`upload`.`UPDATE_USER_ID` AS `UPDATE_USER_ID`,`upload`.`UPDATE_TIME` AS `UPDATE_TIME`,`upload`.`START_TIME` AS `START_TIME`,`upload`.`FINISH_TIME` AS `FINISH_TIME`,`upload`.`UPLOAD_REPORT` AS `UPLOAD_REPORT` from (`upload` join `field_upload`) where (`upload`.`ID` = `field_upload`.`UPLOAD_ID`) */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-11  5:38:51
