
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

-- Reference data values for [report_output_format]
INSERT INTO `reporting`.`report_output_format`
(`ID`, `NAME`, `DESCRIPTION`)
VALUES
(1, 'PDF', 'Portable Document Format (compatible with Adobe Reader)');

INSERT INTO `reporting`.`report_output_format`
(`ID`, `NAME`, `DESCRIPTION`)
VALUES
(2, 'CSV', 'Comma Separated Value (compatible with Excel)');

-- Reference data values for [report_template]
INSERT INTO `report_template` 
(ID,NAME,DESCRIPTION,TEMPLATE_PATH,MODULE_ID,FUNCTION_ID) 
VALUES 
(1,'Study Summary Report','This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>','StudySummaryReport.jrxml',NULL,NULL);

INSERT INTO `report_template` 
(ID,NAME,DESCRIPTION,TEMPLATE_PATH,MODULE_ID,FUNCTION_ID) 
VALUES 
(2,'Study-level Consent Details Report','This report lists detailed subject information for a particular study based on their consent status at the study-level.','ConsentDetailsReport.jrxml',NULL,NULL);

INSERT INTO `report_template`
(ID,NAME,DESCRIPTION,TEMPLATE_PATH,MODULE_ID,FUNCTION_ID) 
VALUES 
(3,'Study Component Consent Details Report','This report lists detailed subject information for a particular study based on their consent status for a specific study component.','ConsentDetailsReport.jrxml',NULL,NULL);
