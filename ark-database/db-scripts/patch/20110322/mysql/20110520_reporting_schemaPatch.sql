CREATE DATABASE `reporting` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `reporting`;

CREATE TABLE `report_output_format` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(250) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `report_template` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1024) default NULL,
  `TEMPLATE_PATH` varchar(255) NOT NULL,
  `MODULE_ID` int(11) default NULL,
  `FUNCTION_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- reference data

INSERT INTO `reporting`.`report_output_format`
(`ID`, `NAME`, `DESCRIPTION`)
VALUES
(1, 'PDF', 'Portable Document Format (compatible with Adobe Reader)');

INSERT INTO `reporting`.`report_output_format`
(`ID`, `NAME`, `DESCRIPTION`)
VALUES
(2, 'CSV', 'Comma Separated Value (compatible with Excel)');

INSERT INTO `reporting`.`report_template`
(`ID`, `NAME`, `TEMPLATE_PATH`)
VALUES
(1, 'Study Summary Report', 'StudySummaryReport.jrxml');

INSERT INTO `reporting`.`report_template`
(`ID`, `NAME`, `TEMPLATE_PATH`)
VALUES 
(2, 'Study-level Consent Details Report', 'ConsentDetailsReport.jrxml');

INSERT INTO `reporting`.`report_template`
(`ID`, `NAME`, `TEMPLATE_PATH`)
VALUES 
(3, 'Study Component Consent Details Report', 'ConsentDetailsReport.jrxml');