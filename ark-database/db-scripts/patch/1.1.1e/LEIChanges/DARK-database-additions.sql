-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 15, 2014 at 11:07 AM
-- Server version: 5.5.34
-- PHP Version: 5.3.10-1ubuntu3.8

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `reporting`
--

USE `reporting`;

-- --------------------------------------------------------
-- select * from consent_status_fielddrop table consent_status_field;

--
-- Table structure for table `consent_status_field`
--
CREATE TABLE IF NOT EXISTS `consent_status_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `FILTERABLE` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_demographic_field_field_type` (`FIELD_TYPE_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ;

--
-- Dumping data for table `consent_status_field`
--

INSERT INTO `consent_status_field` (`ID`, `ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`, `FILTERABLE`) VALUES
(1, 'StudyComp', 'name', 'Study Component Name', 1, 1),
(2, 'Consent', 'studyComponentStatus', 'Study Component Status', 1, 1),
(3, 'Consent', 'consentDate', 'Consent Date', 3, 1),
(4, 'Consent', 'consentedBy', 'Consented By', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `consent_status_field_search`
--

CREATE TABLE IF NOT EXISTS `consent_status_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONSENT_STATUS_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_dfs_df_s` (`CONSENT_STATUS_FIELD_ID`,`SEARCH_ID`),
  KEY `fk_dfs_demographic_field` (`CONSENT_STATUS_FIELD_ID`),
  KEY `fk_dfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='many2many join demographic_field and search'  ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `consent_status_field`
--
ALTER TABLE `consent_status_field`
  ADD CONSTRAINT `consent_status_field_ibfk_1` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

USE `study`;

--
-- Table structure for table `otherid`
--

CREATE TABLE IF NOT EXISTS `otherid` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PersonID` int(11) NOT NULL,
  `OtherID` varchar(100) NOT NULL,
  `OtherID_Source` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;


ALTER TABLE `study`.`otherid` 
  ADD CONSTRAINT `fk_otherid_personid`
  FOREIGN KEY (`PersonID` )
  REFERENCES `study`.`person` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_otherid_personid_idx` (`PersonID` ASC) ;

ALTER TABLE `reporting`.`query_filter` ADD COLUMN `CONSENT_STATUS_FIELD_ID` INT(11) NULL DEFAULT NULL  AFTER `SEARCH_ID` ;
